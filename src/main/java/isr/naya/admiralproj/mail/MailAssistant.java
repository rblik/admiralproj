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

import static isr.naya.admiralproj.report.ReportCreator.EMPTY_STR;
import static isr.naya.admiralproj.report.ReportType.EMPTY;
import static isr.naya.admiralproj.report.ReportType.INDIVIDUAL_EMPTY;
import static java.util.stream.Collectors.groupingBy;
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
        String msg = (message == null) ? EMPTY_STR : message;
        List<WorkInfo> missingDays = service.getMissingWorkForParticularEmployees(from, to, employeeIds);
        if (isEmpty(email)) {
            Map<String, List<WorkInfo>> infos = missingDays.stream().collect(groupingBy(WorkInfo::getEmployeeEmail));
            infos.forEach((emplEmail, infoList) -> {
                WorkInfo info = infoList.get(0);
                byte[] pdfFile = creator.create(infoList, INDIVIDUAL_EMPTY, info.getEmployeeSurname() + ' ' + info.getEmployeeName());
                sender.sendEmail(emplEmail, "ימים חסרים", msg, pdfFile);
            });
        } else {
            byte[] pdfFile = creator.create(missingDays, EMPTY, null);
            sender.sendEmail(email, "ימים חסרים", msg, pdfFile);
        }
    }
}
