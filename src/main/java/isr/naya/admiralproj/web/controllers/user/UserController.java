package isr.naya.admiralproj.web.controllers.user;

import com.fasterxml.jackson.annotation.JsonView;
import isr.naya.admiralproj.AuthorizedUser;
import isr.naya.admiralproj.dto.AgreementDto;
import isr.naya.admiralproj.dto.MonthInfo;
import isr.naya.admiralproj.dto.WorkInfo;
import isr.naya.admiralproj.mail.MailService;
import isr.naya.admiralproj.model.*;
import isr.naya.admiralproj.service.*;
import isr.naya.admiralproj.util.JsonUtil.UserView;
import isr.naya.admiralproj.web.controllers.CorsRestController;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@CorsRestController
@AllArgsConstructor
@RequestMapping("/backend")
public class UserController {

    private EmployeeService employeeService;
    private WorkUnitService workUnitService;
    private WorkAgreementService workAgreementService;
    private WorkInfoService workInfoService;
    private MonthInfoService monthInfoService;
    private MailService service;
    private DefaultChoiceService choiceService;
    private LockService lockService;
    private ProjectService projectService;
    private ClientService clientService;

    @JsonView(UserView.class)
    @GetMapping("/profile")
    public Employee getProfile(@AuthenticationPrincipal AuthorizedUser user) {
        log.info("Employee {} is retrieving his profile", user.getFullName());
        return employeeService.getWithDepartment(user.getId());
    }

    @PostMapping(value = "/units", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkUnit> saveWorkUnit(@AuthenticationPrincipal AuthorizedUser user,
                                                 @RequestParam("agreementId") Integer agreementId,
                                                 @RequestParam(value = "isDefault", required = false) boolean isDefault,
                                                 @Valid @RequestBody WorkUnit unit) {
        boolean aNew = unit.isNew();
        WorkUnit save = workUnitService.save(user.getId(), agreementId, unit);
        log.info("Employee {} {} new unit of work (id = {})", user.getFullName(), aNew ? "saved" : "updated", save.getId());
        if (isDefault) choiceService.saveAsDefault(unit, user.getId(), agreementId);
        return ResponseEntity.status(HttpStatus.CREATED).body(save);
    }

    @GetMapping(value = "/profile/defaultchoice")
    public DefaultChoice getDefaultChoice(@AuthenticationPrincipal AuthorizedUser user) {
        DefaultChoice choice = choiceService.get(user.getId());
        log.info("Employee {} is retrieving his default choice", user.getFullName());
        return choice;
    }

    @GetMapping(value = "/profile/defaultchoices")
    public Collection<DefaultChoice> getAllDefaultChoices(@AuthenticationPrincipal AuthorizedUser user) {
        Collection<DefaultChoice> choice = choiceService.getAll(user.getId());
        log.info("Employee {} is retrieving all his default choices", user.getFullName());
        return choice;
    }

    @DeleteMapping(value = "/profile/defaultchoices/{defaultChoiceId}")
    public void deleteDefaultChoiceById(@AuthenticationPrincipal AuthorizedUser user, @PathVariable("defaultChoiceId") Integer defaultChoiceId) {
        choiceService.delete(defaultChoiceId);
        log.info("Employee {} is removing default choice record on id {}", user.getFullName(),defaultChoiceId);
    }

    @PutMapping("/profile/password")
    public ResponseEntity<?> updatePassword(@AuthenticationPrincipal AuthorizedUser user,
                                            @RequestParam("password") String password) {
        employeeService.refreshPass(user.getId(), password);
        log.info("Employee {} is updating his password", user.getFullName());
        CompletableFuture.runAsync(() ->
                service.sendSimpleMessage(user.getUsername(), "סיסמא חדשה", password));
        return ResponseEntity.status(OK).build();
    }

    @DeleteMapping("/units/{id}")
    public void deleteWorkUnit(@AuthenticationPrincipal AuthorizedUser user, @PathVariable("id") Integer id) {
        log.info("Employee {} is removing wirk unit (id = {})", user.getFullName(), id);
        workUnitService.delete(user.getId(), id);
    }

    @GetMapping("/agreements")
    public List<AgreementDto> getAgreements(@AuthenticationPrincipal AuthorizedUser user) {
        List<AgreementDto> agreements = workAgreementService.getAllActiveForEmployee(user.getId());
        List<AgreementDto> filteredAgreement=new ArrayList<>();
        for (AgreementDto agreement : agreements) {
            if (projectService.get(agreement.getProjectId()).isEnabled()&clientService.get(agreement.getClientId()).isEnabled()){
                filteredAgreement.add(agreement);
            }
        }
        log.info("Employee {} is retrieving his agreements", user.getFullName());
        return filteredAgreement;
    }

    @GetMapping("/monthinfo")
    public MonthInfo getMonthInfo(@AuthenticationPrincipal AuthorizedUser user,
                                  @RequestParam("year") Integer year,
                                  @RequestParam("month") Integer month) {
        log.info("Employee {} is retrieving his month info for {} {}", user.getFullName(), month.toString(), year.toString());
        MonthlyStandard monthStandard = monthInfoService.getStandardForMonth(year, month);
        DateLock monthLock = lockService.getLock(user.getId(), year, month);
        return new MonthInfo(monthStandard, monthLock);
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
        log.info("Employee {} is retrieving work units for {}" + (agreementId.isPresent() ? " (agreementId = {})" : ""), user.getFullName(), date, agreementId.orElse(null));
        return infos;
    }
}
