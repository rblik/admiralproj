package isr.naya.admiralproj.web.controllers.admin;

import isr.naya.admiralproj.AuthorizedUser;
import isr.naya.admiralproj.report.ReportCreator;
import isr.naya.admiralproj.report.annotations.Xlsx;
import isr.naya.admiralproj.service.WorkInfoService;
import isr.naya.admiralproj.web.security.CorsRestController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static isr.naya.admiralproj.report.ReportType.*;
import static isr.naya.admiralproj.web.util.ReportSender.defaultResponse;
import static isr.naya.admiralproj.web.util.ReportSender.report;

@Slf4j
@CorsRestController
@RequestMapping("/admin/xlsx")
public class XLSReportController {

    private static final String XLS_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    private WorkInfoService workInfoService;
    private ReportCreator reportCreator;

    @Autowired
    public XLSReportController(WorkInfoService workInfoService, @Xlsx ReportCreator reportCreator) {
        this.workInfoService = workInfoService;
        this.reportCreator = reportCreator;
    }

    @GetMapping(value = "/pivotal")
    public DeferredResult<ResponseEntity<byte[]>> getPivotalReport(@RequestParam("from") LocalDate from,
                                                                   @RequestParam("to") LocalDate to,
                                                                   @RequestParam("employeeId") Optional<Integer> employeeId,
                                                                   @RequestParam("departmentId") Optional<Integer> departmentId,
                                                                   @RequestParam("projectId") Optional<Integer> projectId,
                                                                   @RequestParam("clientId") Optional<Integer> clientId) {
        DeferredResult<ResponseEntity<byte[]>> result = new DeferredResult<>(30000L, defaultResponse());
        CompletableFuture
                .supplyAsync(() -> reportCreator.create(workInfoService.getWorkInfos(from, to, employeeId, departmentId, projectId, clientId), PIVOTAL))
                .thenApplyAsync(bytes -> result.setResult(report(bytes, XLS_TYPE)));
        log.info("Admin {} is creating xls pivotal report from {} to {}" +
                (employeeId.isPresent() ? "for employee (id = {})" : "") +
                (projectId.isPresent() ? "and project (id = {})" : ""), AuthorizedUser.fullName(), from, to);
        return result;
    }

    @GetMapping(value = "/income")
    public DeferredResult<ResponseEntity<byte[]>> getIncomeReport(@RequestParam("from") LocalDate from,
                                                                   @RequestParam("to") LocalDate to,
                                                                   @RequestParam("employeeId") Optional<Integer> employeeId,
                                                                   @RequestParam("departmentId") Optional<Integer> departmentId,
                                                                   @RequestParam("projectId") Optional<Integer> projectId,
                                                                   @RequestParam("clientId") Optional<Integer> clientId) {
        DeferredResult<ResponseEntity<byte[]>> result = new DeferredResult<>(30000L, defaultResponse());
        CompletableFuture
                .supplyAsync(() -> reportCreator.create(workInfoService.getIncomeReports(from, to, employeeId, departmentId, projectId, clientId), INCOME))
                .thenApplyAsync(bytes -> result.setResult(report(bytes, XLS_TYPE)));
        log.info("Admin {} is creating xls income report from {} to {}" +
                (employeeId.isPresent() ? "for employee (id = {})" : "") +
                (projectId.isPresent() ? "and project (id = {})" : ""), AuthorizedUser.fullName(), from, to);
        return result;
    }

    @GetMapping(value = "/partial")
    public DeferredResult<ResponseEntity<byte[]>> getPartialDaysReport(@RequestParam(value = "from") LocalDate from,
                                                                       @RequestParam("to") LocalDate to,
                                                                       @RequestParam("limit") Integer limit,
                                                                       @RequestParam("employeeId") Optional<Integer> employeeId,
                                                                       @RequestParam("departmentId") Optional<Integer> departmentId) {
        DeferredResult<ResponseEntity<byte[]>> result = new DeferredResult<>(30000L, defaultResponse());
        CompletableFuture
                .supplyAsync(() -> reportCreator.create(workInfoService.getPartialWorkInfos(from, to, limit, employeeId, departmentId), PARTIAL))
                .thenApplyAsync(bytes -> result.setResult(report(bytes, XLS_TYPE)));
        log.info("Admin {} is creating excel partial report from {} to {}" +
                (employeeId.isPresent() ? " for employee (id = {})" : "") +
                (departmentId.isPresent() ? " and department (id = {})" : ""), AuthorizedUser.fullName(), from, to);
        return result;
    }

    @GetMapping(value = "/missing")
    public DeferredResult<ResponseEntity<byte[]>> getMissingDaysReport(@RequestParam("from") LocalDate from,
                                                                       @RequestParam("to") LocalDate to,
                                                                       @RequestParam("employeeId") Optional<Integer> employeeId,
                                                                       @RequestParam("departmentId") Optional<Integer> departmentId) {
        DeferredResult<ResponseEntity<byte[]>> result = new DeferredResult<>(30000L, defaultResponse());
        CompletableFuture
                .supplyAsync(() -> reportCreator.create(workInfoService.getMissingWorkInfos(from, to, employeeId, departmentId), EMPTY))
                .thenApplyAsync(bytes -> result.setResult(report(bytes, XLS_TYPE)));
        log.info("Admin {} is creating excel missing report from {} to {}" +
                (employeeId.isPresent() ? " for employee (id = {})" : "") +
                (departmentId.isPresent() ? " and department (id = {})" : ""), AuthorizedUser.fullName(), from, to);
        return result;
    }
}
