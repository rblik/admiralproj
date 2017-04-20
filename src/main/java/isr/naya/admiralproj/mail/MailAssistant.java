package isr.naya.admiralproj.mail;

import isr.naya.admiralproj.dto.WorkInfo;
import isr.naya.admiralproj.report.ReportCreator;
import isr.naya.admiralproj.report.annotations.Pdf;
import isr.naya.admiralproj.service.WorkInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static isr.naya.admiralproj.report.ReportType.INDIVIDUAL_EMPTY;
import static java.time.LocalDateTime.now;
import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;

@Component
public class MailAssistant {

    @Autowired
    private MailSender sender;

    @Autowired
    private WorkInfoService service;

    @Autowired
    @Pdf
    private ReportCreator creator;

    @Value("${notifyScheduleMessage}")
    private String message;

//    @Scheduled(cron = "${notifySchedule}")
    public void send() {
        if (now().toLocalDate().lengthOfMonth() != now().getDayOfMonth()) {
            List<WorkInfo> missingDays = service.getMissingWorkInfos(LocalDate.now().with(firstDayOfMonth()), LocalDate.now().plusMonths(1).with(firstDayOfMonth()), Optional.empty(), Optional.empty());
            Map<String, List<WorkInfo>> infos = missingDays.stream().collect(Collectors.groupingBy(WorkInfo::getEmployeeEmail));
            infos.forEach((email, infoList) -> {
                byte[] pdfFile = creator.create(infoList, INDIVIDUAL_EMPTY);
                sender.sendEmail(email, "ימים חסרים", message, pdfFile);
            });
        }
    }
}
