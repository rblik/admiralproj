package isr.naya.admiralproj.web;

import isr.naya.admiralproj.AuthorizedUser;
import isr.naya.admiralproj.dto.AgreementDto;
import isr.naya.admiralproj.dto.WorkInfo;
import isr.naya.admiralproj.model.Employee;
import isr.naya.admiralproj.model.WorkUnit;
import isr.naya.admiralproj.service.EmployeeService;
import isr.naya.admiralproj.service.WorkAgreementService;
import isr.naya.admiralproj.service.WorkInfoService;
import isr.naya.admiralproj.service.WorkUnitService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
public class EmployeeController {

    private EmployeeService employeeService;
    private WorkUnitService workUnitService;
    private WorkAgreementService workAgreementService;
    private WorkInfoService workInfoService;

    @GetMapping("/profile")
    public Employee getProfile() {
        return employeeService.get(AuthorizedUser.id());
    }

    @PostMapping(value = "/units", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity saveUnit(@Valid @RequestBody WorkUnit unit) {

        WorkUnit workUnit = workUnitService.saveUnit(AuthorizedUser.id(), 1, unit);
        return ResponseEntity.status(HttpStatus.CREATED).body(workUnit);
    }

    @GetMapping("/agreements")
    public List<AgreementDto> getAgreements() {
        return workAgreementService.getAllForEmployee(AuthorizedUser.id());
    }

    @GetMapping("/units")
    public List<WorkInfo> getWorkUnits(@RequestParam("from") LocalDate from,
                                       @RequestParam("to") LocalDate to) {
        return workInfoService.getAllWorkUnitsForEmployee(AuthorizedUser.id(), from, to);
    }
}
