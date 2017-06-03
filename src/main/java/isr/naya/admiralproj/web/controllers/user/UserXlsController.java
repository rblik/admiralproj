package isr.naya.admiralproj.web.controllers.user;

import isr.naya.admiralproj.AuthorizedUser;
import isr.naya.admiralproj.dto.ReportFile;
import isr.naya.admiralproj.report.ReportCreator;
import isr.naya.admiralproj.report.annotations.Xlsx;
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

import static isr.naya.admiralproj.report.ReportType.INDIVIDUAL_PIVOTAL;
import static isr.naya.admiralproj.web.util.ReportSender.report;

@Slf4j
@CorsRestController
@RequestMapping("/backend/xlsx")
public class UserXlsController {
    private WorkInfoService workInfoService;
    private ReportCreator reportCreator;

    @Autowired
    public UserXlsController(WorkInfoService workInfoService, @Xlsx ReportCreator reportCreator) {
        this.workInfoService = workInfoService;
        this.reportCreator = reportCreator;
    }

    @GetMapping(value = "/download")
    public ResponseEntity<byte[]> getPivotalReport(@AuthenticationPrincipal AuthorizedUser user,
                                                   @RequestParam("from") LocalDate from,
                                                   @RequestParam("to") LocalDate to) {
        ReportFile file = reportCreator.create(workInfoService.getWorkInfos(from, to, Optional.of(user.getId()), Optional.empty(), Optional.empty(), Optional.empty()), INDIVIDUAL_PIVOTAL);
        log.info("Employee {} is creating xls week report from {} to {}", user.getFullName(), from, to);
        return report(file);
    }

}
