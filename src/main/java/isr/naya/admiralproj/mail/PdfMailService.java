package isr.naya.admiralproj.mail;

import isr.naya.admiralproj.dto.ReportFile;
import isr.naya.admiralproj.dto.WorkInfo;
import isr.naya.admiralproj.report.ReportCreator;
import isr.naya.admiralproj.report.annotations.Pdf;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.util.List;
import java.util.Map;

import static isr.naya.admiralproj.report.ReportCreator.EMPTY_STR;
import static isr.naya.admiralproj.report.ReportType.EMPTY;
import static isr.naya.admiralproj.report.ReportType.INDIVIDUAL_EMPTY;
import static java.util.stream.Collectors.groupingBy;
import static org.springframework.util.StringUtils.isEmpty;

@Slf4j
@Component
public class PdfMailService implements MailService {

    @Pdf
    @Autowired
    private ReportCreator creator;
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String address;
    @Value("${spring.mail.titlename}")
    private String titleName;

    @Override
    public void sendSimpleMessage(@NonNull String email, @NonNull String subject, String message) {
        doSend(email, subject, message, null);
    }

    @Override
    public void sendReport(@NonNull List<WorkInfo> infos, String email, @NonNull String subject, String message) {
        String msg = (message == null) ? EMPTY_STR : message;
        if (isEmpty(email)) {
            Map<String, List<WorkInfo>> infosGrouped = infos.stream().collect(groupingBy(WorkInfo::getEmployeeEmail));
            infosGrouped.forEach((emplEmail, infoList) -> {
                WorkInfo info = infoList.get(0);
                ReportFile pdfFile = creator.create(infoList, INDIVIDUAL_EMPTY, info.getEmployeeSurname() + ' ' + info.getEmployeeName());
                doSend(emplEmail, subject, msg, pdfFile);
            });
        } else {
            ReportFile pdfFile = creator.create(infos, EMPTY);
            doSend(email, subject, msg, pdfFile);
        }
    }

    private void doSend(String email, String subject, String msg, ReportFile file) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(address, titleName);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(msg);
            if (file != null) helper.addAttachment(subject + ".pdf", new ByteArrayDataSource(file.getContent(), file.getFileType().getName()));
            mailSender.send(message);
            log.info("Message to {}  was successfully sent", email);
        } catch (Exception ex) {
            log.warn("Message to {} was not send. The reason is: {}", email, ex.getLocalizedMessage());
        }
    }
}
