package isr.naya.admiralproj.web.controllers.admin;

import isr.naya.admiralproj.AuthorizedUser;
import isr.naya.admiralproj.dto.WorkInfo;
import isr.naya.admiralproj.mail.PdfMailService;
import isr.naya.admiralproj.service.WorkInfoService;
import isr.naya.admiralproj.web.controllers.CorsRestController;
import isr.naya.admiralproj.web.dto.MailReportRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

import static isr.naya.admiralproj.web.util.ReportSender.defaultResponse;
import static java.util.concurrent.CompletableFuture.runAsync;

@Slf4j
@CorsRestController
@RequestMapping("/backend/admin/mail")
@AllArgsConstructor
public class MailController {

    private static String MISSING_DAYS = "ימים חסרים";

    private WorkInfoService service;
    private PdfMailService assistant;

    @PostMapping(value = "/missing", consumes = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<ResponseEntity<?>> report(@AuthenticationPrincipal AuthorizedUser admin,
                                                    @RequestBody MailReportRequest request) {
        DeferredResult<ResponseEntity<?>> result = new DeferredResult<>(30000L, defaultResponse());
        runAsync(() -> {
            List<WorkInfo> missingDays = service.getMissingWorkForParticularEmployees(request.getFrom(), request.getTo(), request.getEmployeeIds());
            assistant.sendReport(missingDays, request.getEmail(), MISSING_DAYS, request.getMessage());
        }).thenApplyAsync(aVoid -> {
            log.info("Admin {} has sent emails about missing days", admin.getFullName());
            return result.setResult(ResponseEntity.ok("Emails're' sent"));
        });
        return result;
    }
}
