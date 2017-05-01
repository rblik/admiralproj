package isr.naya.admiralproj.web;

import isr.naya.admiralproj.mail.MailAssistant;
import isr.naya.admiralproj.web.dto.MailReportRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.CompletableFuture;

import static isr.naya.admiralproj.web.util.ReportSender.defaultResponse;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/admin/mail")
public class MailController {

    @Autowired
    private MailAssistant assistant;

    @PostMapping(value = "/missing", consumes = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<ResponseEntity<?>> report(@RequestBody MailReportRequest request) {
        DeferredResult<ResponseEntity<?>> result = new DeferredResult<>(30000L, defaultResponse());
        CompletableFuture
                .runAsync(() -> assistant.send(request.getFrom(), request.getTo(), request.getEmployeeIds(), request.getEmail(), request.getMessage()))
                .thenApplyAsync(aVoid -> result.setResult(ResponseEntity.ok("Emails're' sent")));
        return result;
    }
}
