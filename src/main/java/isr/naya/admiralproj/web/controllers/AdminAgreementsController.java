package isr.naya.admiralproj.web.controllers;

import isr.naya.admiralproj.AuthorizedUser;
import isr.naya.admiralproj.dto.AgreementDto;
import isr.naya.admiralproj.model.WorkAgreement;
import isr.naya.admiralproj.service.WorkAgreementService;
import isr.naya.admiralproj.web.security.CorsRestController;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@CorsRestController
@RequestMapping("/admin/agreements")
@AllArgsConstructor
public class AdminAgreementsController {

    private WorkAgreementService workAgreementService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkAgreement> saveWorkAgreement(@Valid @RequestBody WorkAgreement workAgreement,
                                                           @RequestParam("employeeId") Integer employeeId,
                                                           @RequestParam("projectId") Integer projectId) {
        WorkAgreement saved = workAgreementService.save(employeeId, projectId, workAgreement);
        log.info("Admin {} saved a new work agreement with id = {} for employee (id = {}) and project (id = {})", AuthorizedUser.fullName(), saved.getId(), employeeId, projectId);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public List<AgreementDto> getAllAgreements(@RequestParam(value = "employeeId", required = false) Integer employeeId) {
        return employeeId == null ?
                workAgreementService.getAgreementsGraph() :
                workAgreementService.getAllForEmployee(employeeId);
    }
}
