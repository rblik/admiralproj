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
@RequestMapping("/admin/xlsx")
public class XLSReportController {

    private static final String XLS_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    private WorkInfoService workInfoService;
    private ReportCreator reportCreator;

    @Autowired
    public XLSReportController(WorkInfoService workInfoService, @Qualifier("XLS") ReportCreator reportCreator) {
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
        log.info("Admin {} is creating xls pivotal report from {} to {}" +
                (employeeId.isPresent() ? "for employee (id = {})" : "") +
                (projectId.isPresent() ? "and project (id = {})" : ""), AuthorizedUser.fullName(), from, to);
        sendReport(response, bytes, XLS_TYPE);
    }

    @GetMapping(value = "/partial")
    public void getPartialDaysReport(@RequestParam(value = "from") LocalDate from,
                                     @RequestParam("to") LocalDate to,
                                     @RequestParam("limit") Integer limit,
                                     HttpServletResponse response) {
        byte[] bytes = reportCreator.create(workInfoService.getPartialDays(from, to, limit), PARTIAL);
        log.info("Admin {} is creating xls partial report from {} to {}", AuthorizedUser.fullName(), from, to);
        sendReport(response, bytes, XLS_TYPE);
    }

    @GetMapping(value = "/missing")
    public void getMissingDaysReport(@RequestParam("from") LocalDate from,
                                     @RequestParam("to") LocalDate to,
                                     HttpServletResponse response) {
        byte[] bytes = reportCreator.create(workInfoService.getMissingDays(from, to), EMPTY);
        log.info("Admin {} is creating xls missing report from {} to {}", AuthorizedUser.fullName(), from, to);
        sendReport(response, bytes, XLS_TYPE);
    }
}
