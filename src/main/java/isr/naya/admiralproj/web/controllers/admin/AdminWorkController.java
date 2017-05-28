package isr.naya.admiralproj.web.controllers.admin;

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
import java.util.Optional;

@Slf4j
@CorsRestController
@RequestMapping("/admin/dashboard/units")
@AllArgsConstructor
public class AdminWorkController {

    private WorkInfoService workInfoService;
    private WorkUnitService workUnitService;

    @GetMapping
    public List<WorkInfo> getWorkUnits(@RequestParam("employeeId") Integer employeeId,
                                       @RequestParam("from") LocalDate from,
                                       @RequestParam("to") LocalDate to) {
        List<WorkInfo> infos = workInfoService.getAllForEmployee(employeeId, from, to);
        log.info("Admin {} is retrieving work units for employee (id = {}) from {} to {}", AuthorizedUser.fullName(), employeeId, from, to);
        return infos;
    }

    @GetMapping("/{date}")
    public List<WorkInfo> getWorkUnitsForDay(@RequestParam("employeeId") Integer employeeId,
                                             @PathVariable("date") LocalDate date,
                                             @RequestParam("agreementId") Optional<Integer> agreementId) {
        List<WorkInfo> infos = workInfoService.getAllForEmployeeByDate(employeeId, agreementId, date);
        log.info("Admin {} is retrieving work units for employee {} for {}" + (agreementId.isPresent() ? " (agreementId = {})" : ""), AuthorizedUser.fullName(), employeeId, date, agreementId.orElse(null));
        return infos;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkUnit> saveWorkUnit(@RequestParam("employeeId") Integer employeeId,
                                                 @RequestParam("agreementId") Integer agreementId,
                                                 @Valid @RequestBody WorkUnit unit) {
        boolean aNew = unit.isNew();
        WorkUnit save = workUnitService.save(employeeId, agreementId, unit);
        log.info("Admin {} {} new unit of work (id = {}) for employee (id = {})", AuthorizedUser.fullName(), aNew ? "saved" : "updated", save.getId(), employeeId);
        return ResponseEntity.status(HttpStatus.CREATED).body(save);
    }

    @DeleteMapping("/{id}")
    public void deleteWorkUnit(@PathVariable("id") Integer id,
                               @RequestParam("employeeId") Integer employeeId) {
        log.info("Admin {} is removing work unit (id = {}) of employee (id = {})", AuthorizedUser.fullName(), id, employeeId);
        workUnitService.delete(employeeId, id);
    }
}
