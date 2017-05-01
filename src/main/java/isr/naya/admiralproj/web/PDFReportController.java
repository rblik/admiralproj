package isr.naya.admiralproj.web;

import isr.naya.admiralproj.AuthorizedUser;
import isr.naya.admiralproj.report.ReportCreator;
import isr.naya.admiralproj.report.annotations.Pdf;
import isr.naya.admiralproj.service.WorkInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static isr.naya.admiralproj.report.ReportType.*;
import static isr.naya.admiralproj.web.util.ReportSender.defaultResponse;
import static isr.naya.admiralproj.web.util.ReportSender.report;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/admin/pdf")
public class PDFReportController {

    private static final String PDF_TYPE = "application/pdf";

    private WorkInfoService workInfoService;
    private ReportCreator reportCreator;

    @Autowired
    public PDFReportController(WorkInfoService workInfoService, @Pdf ReportCreator reportCreator) {
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
                .thenApplyAsync(bytes -> result.setResult(report(bytes, PDF_TYPE)));
        log.info("Admin {} is creating pdf pivotal report from {} to {}" +
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
                .thenApplyAsync(bytes -> result.setResult(report(bytes, PDF_TYPE)));
        log.info("Admin {} is creating pdf partial report from {} to {}" +
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
                .thenApplyAsync(bytes -> result.setResult(report(bytes, PDF_TYPE)));
        log.info("Admin {} is creating pdf missing report from {} to {}" +
                (employeeId.isPresent() ? " for employee (id = {})" : "") +
                (departmentId.isPresent() ? " and department (id = {})" : ""), AuthorizedUser.fullName(), from, to);
        return result;
    }
}
