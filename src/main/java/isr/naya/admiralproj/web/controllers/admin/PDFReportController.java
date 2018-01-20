package isr.naya.admiralproj.web.controllers.admin;

import isr.naya.admiralproj.AuthorizedUser;
import isr.naya.admiralproj.dto.ReportFile;
import isr.naya.admiralproj.report.ReportCreator;
import isr.naya.admiralproj.report.annotations.Pdf;
import isr.naya.admiralproj.service.WorkInfoService;
import isr.naya.admiralproj.web.controllers.CorsRestController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Optional;

import static isr.naya.admiralproj.report.ReportType.*;
import static isr.naya.admiralproj.web.util.LogUtil.logMessage;
import static isr.naya.admiralproj.web.util.ReportSender.report;

@Slf4j
@CorsRestController
@RequestMapping("/backend/admin/pdf")
public class PDFReportController {

    private WorkInfoService workInfoService;
    private ReportCreator reportCreator;

    @Autowired
    public PDFReportController(WorkInfoService workInfoService, @Pdf ReportCreator reportCreator) {
        this.workInfoService = workInfoService;
        this.reportCreator = reportCreator;
    }

    @GetMapping(value = "/pivotal")
    public ResponseEntity<byte[]> getPivotalReport(@AuthenticationPrincipal AuthorizedUser admin,
                                                                   @RequestParam("from") LocalDate from,
                                                                   @RequestParam("to") LocalDate to,
                                                                   @RequestParam("employeeId") Optional<Integer> employeeId,
                                                                   @RequestParam("departmentId") Optional<Integer> departmentId,
                                                                   @RequestParam("projectId") Optional<Integer> projectId,
                                                                   @RequestParam("clientId") Optional<Integer> clientId) {
        ReportFile reportFile = reportCreator.create(workInfoService.getWorkInfos(from, to, employeeId, departmentId, projectId, clientId), PIVOTAL);
        log.info(logMessage("pivotal", "pdf", employeeId, departmentId, projectId, clientId),
                admin.getFullName(), from, to,
                employeeId.orElseGet(() -> departmentId.orElseGet(() -> null)),
                projectId.orElseGet(() -> clientId.orElseGet(() -> null)));
        return report(reportFile);
    }

    @GetMapping(value = "/income")
    public ResponseEntity<byte[]> getIncomeReport(@AuthenticationPrincipal AuthorizedUser admin,
                                                                  @RequestParam("from") LocalDate from,
                                                                   @RequestParam("to") LocalDate to,
                                                                   @RequestParam("employeeId") Optional<Integer> employeeId,
                                                                   @RequestParam("departmentId") Optional<Integer> departmentId,
                                                                   @RequestParam("projectId") Optional<Integer> projectId,
                                                                   @RequestParam("clientId") Optional<Integer> clientId) {
        ReportFile reportFile = reportCreator.create(workInfoService.getIncomeReports(from, to, employeeId, departmentId, projectId, clientId), INCOME, from);
        log.info(logMessage("income", "pdf", employeeId, departmentId, projectId, clientId),
                admin.getFullName(), from, to,
                employeeId.orElseGet(() -> departmentId.orElseGet(() -> null)),
                projectId.orElseGet(() -> clientId.orElseGet(() -> null)));
        return report(reportFile);
    }

    @GetMapping(value = "/partial")
    public ResponseEntity<byte[]> getPartialDaysReport(@AuthenticationPrincipal AuthorizedUser admin,
                                                                       @RequestParam(value = "from") LocalDate from,
                                                                       @RequestParam("to") LocalDate to,
                                                                       @RequestParam("limit") Integer limit,
                                                                       @RequestParam("employeeId") Optional<Integer> employeeId,
                                                                       @RequestParam("departmentId") Optional<Integer> departmentId) {
        ReportFile reportFile = reportCreator.create(workInfoService.getPartialWorkInfos(from, to, limit, employeeId, departmentId), PARTIAL);
        log.info(logMessage("partial", "pdf", employeeId, departmentId),
                admin.getFullName(), from, to,
                employeeId.orElseGet(() -> departmentId.orElseGet(() -> null)));
        return report(reportFile);
    }

    @GetMapping(value = "/missing")
    public ResponseEntity<byte[]> getMissingDaysReport(@AuthenticationPrincipal AuthorizedUser admin,
                                                                       @RequestParam("from") LocalDate from,
                                                                       @RequestParam("to") LocalDate to,
                                                                       @RequestParam("employeeId") Optional<Integer> employeeId,
                                                                       @RequestParam("departmentId") Optional<Integer> departmentId) {
        ReportFile reportFile = reportCreator.create(workInfoService.getMissingWorkInfos(from, to, employeeId, departmentId), EMPTY);
        log.info(logMessage("missing", "pdf", employeeId, departmentId),
                admin.getFullName(), from, to,
                employeeId.orElseGet(() -> departmentId.orElseGet(() -> null)));
        return report(reportFile);
    }
}
