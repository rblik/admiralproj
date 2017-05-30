package isr.naya.admiralproj.web.controllers.user;

import com.fasterxml.jackson.annotation.JsonView;
import isr.naya.admiralproj.AuthorizedUser;
import isr.naya.admiralproj.dto.AgreementDto;
import isr.naya.admiralproj.dto.WorkInfo;
import isr.naya.admiralproj.model.DateLock;
import isr.naya.admiralproj.model.Employee;
import isr.naya.admiralproj.model.WorkUnit;
import isr.naya.admiralproj.service.*;
import isr.naya.admiralproj.util.JsonUtil.UserView;
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
import java.util.Set;

@Slf4j
@CorsRestController
@AllArgsConstructor
@RequestMapping("/backend")
public class UserController {

    private EmployeeService employeeService;
    private LockService lockService;
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

    @GetMapping("/locks")
    public Set<DateLock> getLocks(@RequestParam("from") LocalDate from,
                                  @RequestParam("to") LocalDate to) {
        Set<DateLock> locks = lockService.getAllLocks(AuthorizedUser.id(), from, to);
        log.info("Employee {} is retrieving his locks from {} to {}", AuthorizedUser.fullName(), from.toString(), to.toString());
        return locks;
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
                                             @RequestParam("agreementId") Optional<Integer> agreementId) {
        List<WorkInfo> infos = workInfoService.getAllForEmployeeByDate(AuthorizedUser.id(), agreementId, date);
        log.info("Employee {} is retrieving work units for {}" + (agreementId.isPresent()? " (agreementId = {})" : ""), AuthorizedUser.fullName(), date, agreementId.orElse(null));
        return infos;
    }
}
