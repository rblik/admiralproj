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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public Employee getProfile(@AuthenticationPrincipal AuthorizedUser user) {
        log.info("Employee {} is retrieving his profile", user.getFullName());
        return employeeService.getWithDepartment(user.getId());
    }

    @PostMapping(value = "/units", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkUnit> saveWorkUnit(@AuthenticationPrincipal AuthorizedUser user,
                                                 @RequestParam("agreementId") Integer agreementId,
                                                 @Valid @RequestBody WorkUnit unit) {
        boolean aNew = unit.isNew();
        WorkUnit save = workUnitService.save(user.getId(), agreementId, unit);
        log.info("Employee {} {} new unit of work (id = {})", user.getFullName(), aNew? "saved": "updated", save.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(save);
    }

    @DeleteMapping("/units/{id}")
    public void deleteWorkUnit(@AuthenticationPrincipal AuthorizedUser user, @PathVariable("id") Integer id) {
        log.info("Employee {} is removing wirk unit (id = {})", user.getFullName(), id);
        workUnitService.delete(user.getId(), id);
    }

    @GetMapping("/agreements")
    public List<AgreementDto> getAgreements(@AuthenticationPrincipal AuthorizedUser user) {
        List<AgreementDto> agreements = workAgreementService.getAllForEmployee(user.getId());
        log.info("Employee {} is retrieving his agreements", user.getFullName());
        return agreements;
    }

    @GetMapping("/locks")
    public Set<DateLock> getLocks(@AuthenticationPrincipal AuthorizedUser user,
                                  @RequestParam("from") LocalDate from,
                                  @RequestParam("to") LocalDate to) {
        Set<DateLock> locks = lockService.getAllLocks(user.getId(), from, to);
        log.info("Employee {} is retrieving his locks from {} to {}", user.getFullName(), from.toString(), to.toString());
        return locks;
    }

    @GetMapping("/units")
    public List<WorkInfo> getWorkUnits(@AuthenticationPrincipal AuthorizedUser user,
                                       @RequestParam("from") LocalDate from,
                                       @RequestParam("to") LocalDate to) {
        List<WorkInfo> infos = workInfoService.getAllForEmployee(user.getId(), from, to);
        log.info("Employee {} is retrieving work units from {} to {}", user.getFullName(), from, to);
        return infos;
    }

    @GetMapping("/units/{date}")
    public List<WorkInfo> getWorkUnitsForDay(@AuthenticationPrincipal AuthorizedUser user,
                                             @PathVariable("date") LocalDate date,
                                             @RequestParam("agreementId") Optional<Integer> agreementId) {
        List<WorkInfo> infos = workInfoService.getAllForEmployeeByDate(user.getId(), agreementId, date);
        log.info("Employee {} is retrieving work units for {}" + (agreementId.isPresent()? " (agreementId = {})" : ""), user.getFullName(), date, agreementId.orElse(null));
        return infos;
    }
}
