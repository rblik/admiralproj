package isr.naya.admiralproj.web;

import isr.naya.admiralproj.AuthorizedUser;
import isr.naya.admiralproj.report.ReportCreator;
import isr.naya.admiralproj.service.WorkInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.Optional;

import static isr.naya.admiralproj.report.ReportCreator.XLSX;
import static isr.naya.admiralproj.report.ReportType.*;
import static isr.naya.admiralproj.web.util.ReportSender.sendReport;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/admin/xlsx")
public class XLSReportController {

    private static final String XLS_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    private WorkInfoService workInfoService;
    private ReportCreator reportCreator;

    @Autowired
    public XLSReportController(WorkInfoService workInfoService, @Qualifier(XLSX) ReportCreator reportCreator) {
        this.workInfoService = workInfoService;
        this.reportCreator = reportCreator;
    }

    @GetMapping(value = "/pivotal")
    public void getPivotalReport(@RequestParam("from") LocalDate from,
                                 @RequestParam("to") LocalDate to,
                                 @RequestParam("employeeId") Optional<Integer> employeeId,
                                 @RequestParam("departmentId") Optional<Integer> departmentId,
                                 @RequestParam("projectId") Optional<Integer> projectId,
                                 @RequestParam("clientId") Optional<Integer> clientId,
                                 HttpServletResponse response) {
        byte[] bytes = reportCreator.create(workInfoService.getWorkInfos(from, to, employeeId, departmentId, projectId, clientId), PIVOTAL);
        log.info("Admin {} is creating xls pivotal report from {} to {}" +
                (employeeId.isPresent() ? "for employee (id = {})" : "") +
                (projectId.isPresent() ? "and project (id = {})" : ""), AuthorizedUser.fullName(), from, to);
        sendReport(response, bytes, XLS_TYPE);
    }

    @GetMapping(value = "/partial")
    public void getPartialDaysReport(@RequestParam(value = "from") LocalDate from,
                                     @RequestParam("to") LocalDate to,
                                     @RequestParam("limit") Integer limit,
                                     @RequestParam("employeeId") Optional<Integer> employeeId,
                                     @RequestParam("departmentId") Optional<Integer> departmentId,
                                     HttpServletResponse response) {
        byte[] bytes = reportCreator.create(workInfoService.getPartialWorkInfos(from, to, limit, employeeId, departmentId), PARTIAL);
        log.info("Admin {} is creating excel partial report from {} to {}" +
                (employeeId.isPresent() ? " for employee (id = {})" : "") +
                (departmentId.isPresent() ? " and department (id = {})" : ""), AuthorizedUser.fullName(), from, to);
        sendReport(response, bytes, XLS_TYPE);
    }

    @GetMapping(value = "/missing")
    public void getMissingDaysReport(@RequestParam("from") LocalDate from,
                                     @RequestParam("to") LocalDate to,
                                     @RequestParam("employeeId") Optional<Integer> employeeId,
                                     @RequestParam("departmentId") Optional<Integer> departmentId,
                                     HttpServletResponse response) {
        byte[] bytes = reportCreator.create(workInfoService.getMissingWorkInfos(from, to, employeeId, departmentId), EMPTY);
        log.info("Admin {} is creating excel missing report from {} to {}" +
                (employeeId.isPresent() ? " for employee (id = {})" : "") +
                (departmentId.isPresent() ? " and department (id = {})" : ""), AuthorizedUser.fullName(), from, to);
        sendReport(response, bytes, XLS_TYPE);
    }
}
