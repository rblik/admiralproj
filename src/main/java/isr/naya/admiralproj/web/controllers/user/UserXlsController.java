package isr.naya.admiralproj.web.controllers.user;

import isr.naya.admiralproj.AuthorizedUser;
import isr.naya.admiralproj.dto.AgreementDto;
import isr.naya.admiralproj.dto.ReportFile;
import isr.naya.admiralproj.dto.ReportFileType;
import isr.naya.admiralproj.dto.WorkInfo;
import isr.naya.admiralproj.model.WorkUnit;
import isr.naya.admiralproj.report.ReportCreator;
import isr.naya.admiralproj.report.annotations.Xlsx;
import isr.naya.admiralproj.service.WorkAgreementService;
import isr.naya.admiralproj.service.WorkInfoService;
import isr.naya.admiralproj.service.WorkUnitService;
import isr.naya.admiralproj.web.controllers.CorsRestController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static isr.naya.admiralproj.report.ReportType.INDIVIDUAL_PIVOTAL;
import static isr.naya.admiralproj.web.util.ReportSender.report;

@Slf4j
@CorsRestController
@RequestMapping("/backend/xlsx")
public class UserXlsController {
    private WorkInfoService workInfoService;
    private WorkUnitService workUnitService;
    private ReportCreator reportCreator;
    private WorkAgreementService workAgreementService;

    @Autowired
    public UserXlsController(WorkInfoService workInfoService,
                             WorkUnitService workUnitService,
                             @Xlsx ReportCreator reportCreator,
                             WorkAgreementService workAgreementService) {
        this.workInfoService = workInfoService;
        this.workUnitService = workUnitService;
        this.reportCreator = reportCreator;
        this.workAgreementService = workAgreementService;
    }

    @GetMapping(value = "/download")
    public ResponseEntity<byte[]> getPivotalReport(@AuthenticationPrincipal AuthorizedUser user,
                                                   @RequestParam("from") LocalDate from,
                                                   @RequestParam("to") LocalDate to) {
        ReportFile file = reportCreator.create(workInfoService.getWorkInfos(from, to, Optional.of(user.getId()), Optional.empty(), Optional.empty(), Optional.empty()), INDIVIDUAL_PIVOTAL);
        log.info("Employee {} is creating xls week report from {} to {}", user.getFullName(), from, to);
        return report(file);
    }

    @GetMapping(value = "/downloadTemplate")
    public ResponseEntity<byte[]> getTemplate(@AuthenticationPrincipal AuthorizedUser user,
                                              @RequestParam("from") LocalDate from,
                                              @RequestParam("to") LocalDate to) {
        List<AgreementDto> agreements = workAgreementService.getAllActiveForEmployee(user.getId());
        List<WorkInfo> infos = workInfoService.getWorkInfos(from, to, Optional.of(user.getId()), Optional.empty(), Optional.empty(), Optional.empty());
        ReportFile file = reportCreator.generateTemplate(agreements, infos);
        log.info("Employee {} is getting his xls report template from {} to {}", user.getFullName(), from, to);
        return report(file);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadReport(@AuthenticationPrincipal AuthorizedUser user,
                             Integer year,
                             Integer month,
                             @RequestPart(name = "file", required = false) MultipartFile file) throws IOException {
        List<WorkUnit> units = reportCreator.readFromFile(new ReportFile(ReportFileType.XLS, file.getBytes()), year, month);
        units.forEach(workUnit -> workUnitService.save(user.getId(), workUnit.getWorkAgreement().getId(), workUnit ));
        log.info("Employee {} saved units batch for {}, {}", user.getFullName(), Month.of(month), year);
    }
}
