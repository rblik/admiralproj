package isr.naya.admiralproj.web.controllers.admin;

import isr.naya.admiralproj.AuthorizedUser;
import isr.naya.admiralproj.dto.AgreementDto;
import isr.naya.admiralproj.model.WorkAgreement;
import isr.naya.admiralproj.service.WorkAgreementService;
import isr.naya.admiralproj.web.controllers.CorsRestController;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@CorsRestController
@RequestMapping("/backend/admin/agreements")
@AllArgsConstructor
public class AdminAgreementsController {

    private WorkAgreementService workAgreementService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkAgreement> saveWorkAgreement(@AuthenticationPrincipal AuthorizedUser admin,
                                                           @Valid @RequestBody WorkAgreement workAgreement,
                                                           @RequestParam("employeeId") Integer employeeId,
                                                           @RequestParam("projectId") Integer projectId) {
        WorkAgreement saved = workAgreementService.save(employeeId, projectId, workAgreement);
        log.info("Admin {} saved a new work agreement with id = {} for employee (id = {}) and project (id = {})", admin.getFullName(), saved.getId(), employeeId, projectId);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public List<AgreementDto> getAllAgreements(@AuthenticationPrincipal AuthorizedUser admin,
                                               @RequestParam(value = "employeeId", required = false) Integer employeeId) {
        List<AgreementDto> dtoList = employeeId == null ?
                workAgreementService.getAgreementsGraph() :
                workAgreementService.getAllForEmployee(employeeId);
        log.info("Admin {} is retrieving work agreements" + ((employeeId != null) ? " for employee (id = {})" : " for all employees"), admin.getFullName(), employeeId);
        return dtoList;
    }

    @DeleteMapping("/{agreementId}")
    public void removeAgreement(@AuthenticationPrincipal AuthorizedUser admin,
                                @PathVariable("agreementId") Integer agreementId) {
        workAgreementService.disable(agreementId);
        log.info("Admin {} is removing agreement with id = {}", admin.getFullName(), agreementId);
    }
}
