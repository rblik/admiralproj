package isr.naya.admiralproj.mail;

public interface MailProperties {

    String senderEmail = "mail.smtp.user";
    String host = "mail.smtp.host";
    String port = "mail.smtp.port";
    String tls = "mail.smtp.starttls.enable";
    String auth = "mail.smtp.auth";
    String socketFactoryPort = "mail.smtp.socketFactory.port";
    String socketFactoryClass = "mail.smtp.socketFactory.class";
    String socketFactoryFallback = "mail.smtp.socketFactory.fallback";
    String name = "Naya Technologies";
}
