package isr.naya.admiralproj.web.controllers;

import isr.naya.admiralproj.AuthorizedUser;
import isr.naya.admiralproj.dto.WorkInfo;
import isr.naya.admiralproj.model.WorkUnit;
import isr.naya.admiralproj.service.WorkInfoService;
import isr.naya.admiralproj.service.WorkUnitService;
import isr.naya.admiralproj.web.security.CorsRestController;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@CorsRestController
@RequestMapping("/admin/units")
@AllArgsConstructor
public class AdminWorkController {

    private WorkInfoService workInfoService;
    private WorkUnitService workUnitService;

    @GetMapping
    public List<WorkInfo> getWorkUnits(@RequestParam("from") LocalDate from,
                                       @RequestParam("to") LocalDate to,
                                       @RequestParam("employeeId") Integer employeeId) {
        List<WorkInfo> infos = workInfoService.getAllForEmployee(employeeId, from, to);
        log.info("Admin {} is retrieving all work info for employee (id = {}) from {} to {}", AuthorizedUser.fullName(), employeeId, from, to);
        return infos;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkUnit> saveWorkUnit(@Valid @RequestBody WorkUnit workUnit,
                                                 @RequestParam("employeeId") Integer employeeId,
                                                 @RequestParam("agreementId") Integer agreementId) {
        WorkUnit saved = workUnitService.save(employeeId, agreementId, workUnit);
        log.info("Admin {} saved a new work unit (id = {}) for employee (id = {}) and agreement (id = {})", AuthorizedUser.fullName(), saved.getId(), employeeId, agreementId);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @DeleteMapping("/{unitId}")
    public void deleteWorkUnit(@PathVariable("unitId") Integer unitId,
                               @RequestParam("employeeId") Integer employeeId) {
        workUnitService.delete(employeeId, unitId);
        log.info("Admin {} removed work unit with id = {}", AuthorizedUser.fullName(), unitId);
    }
}
