package isr.naya.admiralproj.mail;

import isr.naya.admiralproj.dto.WorkInfo;

import java.util.List;

public interface MailService {

    void sendReport(List<WorkInfo> infos, String email, String subject, String message);
}
