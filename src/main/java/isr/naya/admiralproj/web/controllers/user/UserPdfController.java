package isr.naya.admiralproj.web.controllers.user;

import isr.naya.admiralproj.AuthorizedUser;
import isr.naya.admiralproj.report.ReportCreator;
import isr.naya.admiralproj.report.annotations.Pdf;
import isr.naya.admiralproj.service.WorkInfoService;
import isr.naya.admiralproj.web.security.CorsRestController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Optional;

import static isr.naya.admiralproj.report.ReportType.INDIVIDUAL_PIVOTAL;

@Slf4j
@CorsRestController
@RequestMapping("/backend/pdf")
public class UserPdfController {

    private static final String PDF_TYPE = "application/pdf";
    private final WorkInfoService workInfoService;
    private final ReportCreator reportCreator;

    @Autowired
    public UserPdfController(WorkInfoService workInfoService, @Pdf ReportCreator reportCreator) {
        this.workInfoService = workInfoService;
        this.reportCreator = reportCreator;
    }
    @GetMapping(value = "/download")
    public ResponseEntity<byte[]> getPivotalReport(@RequestParam("from") LocalDate from,
                                                                   @RequestParam("to") LocalDate to) {
        byte[] bytes = reportCreator.create(workInfoService.getWorkInfos(from, to, Optional.of(AuthorizedUser.id()), Optional.empty(), Optional.empty(), Optional.empty()), INDIVIDUAL_PIVOTAL, AuthorizedUser.fullName());
        log.info("Employee {} is creating pdf week report from {} to {}", AuthorizedUser.fullName(), from, to);
        return ResponseEntity.status(HttpStatus.OK).body(bytes);
    }
}
