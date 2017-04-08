package isr.naya.admiralproj.web;

import com.fasterxml.jackson.annotation.JsonView;
import isr.naya.admiralproj.AuthorizedUser;
import isr.naya.admiralproj.dto.AgreementDto;
import isr.naya.admiralproj.dto.WorkInfo;
import isr.naya.admiralproj.model.Employee;
import isr.naya.admiralproj.model.WorkUnit;
import isr.naya.admiralproj.service.EmployeeService;
import isr.naya.admiralproj.service.WorkAgreementService;
import isr.naya.admiralproj.service.WorkInfoService;
import isr.naya.admiralproj.service.WorkUnitService;
import isr.naya.admiralproj.util.JsonUtil.UserView;
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
@CrossOrigin
@RestController
@AllArgsConstructor
public class UserController {

    private EmployeeService employeeService;
    private WorkUnitService workUnitService;
    private WorkAgreementService workAgreementService;
    private WorkInfoService workInfoService;

    @JsonView(UserView.class)
    @GetMapping("/profile")
    public Employee getProfile() {
        log.info("Employee {} is retrieving his profile", AuthorizedUser.fullName());
        return employeeService.getWithDepartment(AuthorizedUser.id());
    }

    @PostMapping(value = "/units", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkUnit> saveWorkUnit(@RequestParam("agreementId") Integer agreementId,
                                                 @Valid @RequestBody WorkUnit unit) {
        boolean aNew = unit.isNew();
        WorkUnit save = workUnitService.save(AuthorizedUser.id(), agreementId, unit);
        log.info("Employee {} {} new unit of work (id = {})", AuthorizedUser.fullName(), aNew? "saved": "updated", save.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(save);
    }

    @DeleteMapping("/units/{id}")
    public void deleteWorkUnit(@PathVariable("id") Integer id) {
        log.info("Employee {} is removing wirk unit (id = {})", AuthorizedUser.fullName(), id);
        workUnitService.delete(AuthorizedUser.id(), id);
    }

    @GetMapping("/agreements")
    public List<AgreementDto> getAgreements() {
        List<AgreementDto> agreements = workAgreementService.getAllForEmployee(AuthorizedUser.id());
        log.info("Employee {} is retrieving his agreements", AuthorizedUser.fullName());
        return agreements;
    }

    @GetMapping("/units")
    public List<WorkInfo> getWorkUnits(@RequestParam("from") LocalDate from,
                                       @RequestParam("to") LocalDate to) {
        List<WorkInfo> infos = workInfoService.getAllForEmployee(AuthorizedUser.id(), from, to);
        log.info("Employee {} is retrieving work units from {} to {}", AuthorizedUser.fullName(), from, to);
        return infos;
    }

    @GetMapping("/units/{date}")
    public List<WorkInfo> getWorkUnitsForDay(@PathVariable("date") LocalDate date,
                                             @RequestParam("agreementId") Integer agreementId) {
        List<WorkInfo> infos = workInfoService.getAllForEmployeeByDate(AuthorizedUser.id(), agreementId, date);
        log.info("Employee {} is retrieving work units for {} (agreementId = {})", AuthorizedUser.fullName(), date, agreementId);
        return infos;
    }
}
