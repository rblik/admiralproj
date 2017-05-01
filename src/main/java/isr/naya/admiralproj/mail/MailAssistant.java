package isr.naya.admiralproj.mail;

import isr.naya.admiralproj.dto.WorkInfo;
import isr.naya.admiralproj.report.ReportCreator;
import isr.naya.admiralproj.report.annotations.Pdf;
import isr.naya.admiralproj.service.WorkInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static isr.naya.admiralproj.report.ReportType.INDIVIDUAL_EMPTY;
import static org.springframework.util.StringUtils.isEmpty;

@Component
public class MailAssistant {

    @Autowired
    private MailSender sender;

    @Autowired
    private WorkInfoService service;

    @Autowired
    @Pdf
    private ReportCreator creator;

    public void send(LocalDate from, LocalDate to, List<Integer> employeeIds, String email, String message) {
        String msg = (message == null) ? "" : message;
        List<WorkInfo> missingDays = service.getMissingWorkForParticularEmployees(from, to, employeeIds);
        if (isEmpty(email)) {
            Map<String, List<WorkInfo>> infos = missingDays.stream().collect(Collectors.groupingBy(WorkInfo::getEmployeeEmail));
            infos.forEach((emplEmail, infoList) -> {
                byte[] pdfFile = creator.create(infoList, INDIVIDUAL_EMPTY);
                sender.sendEmail(emplEmail, "ימים חסרים", msg, pdfFile);
            });
        } else {
            byte[] pdfFile = creator.create(missingDays, INDIVIDUAL_EMPTY);
            sender.sendEmail(email, "ימים חסרים", msg, pdfFile);
        }
    }
}
