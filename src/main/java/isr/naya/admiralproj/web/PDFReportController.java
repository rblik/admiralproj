package isr.naya.admiralproj.web;

import isr.naya.admiralproj.AuthorizedUser;
import isr.naya.admiralproj.report.ReportCreator;
import isr.naya.admiralproj.service.WorkInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.Optional;

import static isr.naya.admiralproj.report.ReportType.*;
import static isr.naya.admiralproj.web.util.ReportSender.sendReport;

@Slf4j
@RestController
@RequestMapping("/admin/pdf")
public class PDFReportController {

    private static final String PDF_TYPE = "application/pdf";

    private WorkInfoService workInfoService;
    private ReportCreator reportCreator;

    @Autowired
    public PDFReportController(WorkInfoService workInfoService, @Qualifier("PDF") ReportCreator reportCreator) {
        this.workInfoService = workInfoService;
        this.reportCreator = reportCreator;
    }

    @GetMapping(value = "/pivotal")
    public void getPivotalReport(@RequestParam("from") LocalDate from,
                                 @RequestParam("to") LocalDate to,
                                 @RequestParam("employeeId") Optional<Integer> employeeId,
                                 @RequestParam("projectId") Optional<Integer> projectId,
                                 HttpServletResponse response) {
        byte[] bytes = reportCreator.create(workInfoService.getWorkInfos(from, to, employeeId, projectId), PIVOTAL);
        log.info("Admin {} is creating pdf pivotal report from {} to {}" +
                (employeeId.isPresent() ? "for employee (id = {})" : "") +
                (projectId.isPresent() ? "and project (id = {})" : ""), AuthorizedUser.fullName(), from, to);
        sendReport(response, bytes, PDF_TYPE);
    }

    @GetMapping(value = "/partial")
    public void getPartialDaysReport(@RequestParam(value = "from") LocalDate from,
                                     @RequestParam("to") LocalDate to,
                                     @RequestParam("limit") Integer limit,
                                     HttpServletResponse response) {
        byte[] bytes = reportCreator.create(workInfoService.getPartialDays(from, to, limit), PARTIAL);
        log.info("Admin {} is creating pdf partial report from {} to {}", AuthorizedUser.fullName(), from, to);
        sendReport(response, bytes, PDF_TYPE);
    }

    @GetMapping(value = "/missing")
    public void getMissingDaysReport(@RequestParam("from") LocalDate from,
                                     @RequestParam("to") LocalDate to,
                                     HttpServletResponse response) {
        byte[] bytes = reportCreator.create(workInfoService.getMissingDays(from, to), EMPTY);
        log.info("Admin {} is creating pdf missing report from {} to {}", AuthorizedUser.fullName(), from, to);
        sendReport(response, bytes, PDF_TYPE);
    }
}
