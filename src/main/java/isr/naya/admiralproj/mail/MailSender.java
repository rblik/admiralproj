package isr.naya.admiralproj.mail;

import isr.naya.admiralproj.report.ReportCreator;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.annotation.PostConstruct;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.util.Properties;

import static isr.naya.admiralproj.mail.MailProperties.*;
import static isr.naya.admiralproj.report.ReportCreator.PDF;
import static javax.mail.Message.RecipientType.TO;
import static javax.mail.Transport.send;

@Slf4j
@Component
@PropertySource("classpath:mail/mail.properties")
public class MailSender {

    private static final String TEXT_MIME_TYPE = "text/html";
    private static final String PDF_MIME_TYPE = "application/pdf";

    @Value("${senderEmail}")
    private String senderEmailID;
    @Value("${senderPassword}")
    private String senderPassword;
    @Value("${emailSMTPserver}")
    private String emailSMTPserver;
    @Value("${emailServerPort}")
    private String emailServerPort;

    @Qualifier(PDF)
    @Autowired
    private ReportCreator creator;

    private Properties props;

    @PostConstruct
    public void init() {
        props = new Properties();
        props.put(senderEmail, senderEmailID);
        props.put(host, emailSMTPserver);
        props.put(port, emailServerPort);
        props.put(tls, "true");
        props.put(auth, "true");
        props.put(socketFactoryPort, emailServerPort);
        props.put(socketFactoryClass, "javax.net.ssl.SSLSocketFactory");
        props.put(socketFactoryFallback, "false");
    }

    public void sendEmail(@NonNull String receiverEmailID, @NonNull String emailSubject, @NonNull String message, byte[] attachedContent) {
        try {
            Session session = Session.getInstance(props, new SMTPAuthenticator());
            MimeMessage msg = new MimeMessage(session);
            msg.setSubject(emailSubject);
            msg.setFrom(new InternetAddress(senderEmailID, name));
            msg.addRecipient(TO, new InternetAddress(receiverEmailID));
            msg.setContent(buildBody(message, attachedContent));
            send(msg);
            log.info("Message to {}  was successfully sent", receiverEmailID);
        } catch (Exception ex) {
            log.warn("Message to {} was not send. The reason is: {}", receiverEmailID, ex.getLocalizedMessage());
        }
    }

    @SneakyThrows
    private Multipart buildBody(String message, byte[] attached) {
        Multipart multipart = new MimeMultipart();
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(message, TEXT_MIME_TYPE);

        multipart.addBodyPart(messageBodyPart);

        MimeBodyPart attachedBodyPart = new MimeBodyPart();
        attachedBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(attached, PDF_MIME_TYPE)));
        multipart.addBodyPart(attachedBodyPart);
        return multipart;
    }

    public class SMTPAuthenticator extends javax.mail.Authenticator {
        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(senderEmailID, senderPassword);
        }
    }
}
