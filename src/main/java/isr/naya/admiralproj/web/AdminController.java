package isr.naya.admiralproj.web;

import com.fasterxml.jackson.annotation.JsonView;
import isr.naya.admiralproj.dto.AgreementDto;
import isr.naya.admiralproj.dto.WorkInfo;
import isr.naya.admiralproj.model.*;
import isr.naya.admiralproj.service.*;
import isr.naya.admiralproj.util.JsonUtil.AdminView;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private WorkInfoService workInfoService;
    private ClientService clientService;
    private DepartmentService departmentService;
    private EmployeeService employeeService;
    private ProjectService projectService;
    private WorkAgreementService workAgreementService;
    private WorkUnitService workUnitService;

    @GetMapping("/units")
    public List<WorkInfo> getWorkUnits(@RequestParam("from") LocalDate from,
                                       @RequestParam("to") LocalDate to,
                                       @RequestParam("employeeId") Integer employeeId) {
        return workInfoService.getAllForEmployee(employeeId, from, to);
    }

    @PostMapping(value = "/clients", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Client> saveClient(@Valid @RequestBody Client client) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(clientService.save(client));
    }

    @GetMapping("/clients")
    public List<Client> getAllClients() {
        return clientService.getAll();
    }

    @GetMapping("/clients/{clientId}")
    public Client getClient(@PathVariable("clientId") Integer clientId) {
        return clientService.get(clientId);
    }

    @PostMapping(value = "/departments", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Department> saveDepartment(@Valid @RequestBody Department department) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(departmentService.save(department));
    }

    @GetMapping("/departments")
    public List<Department> getAllDepartments() {
        return departmentService.getAll();
    }

    @GetMapping("/departments/{departmentId}")
    public Department getDepartment(@PathVariable("departmentId") Integer departmentId) {
        return departmentService.get(departmentId);
    }

    @PostMapping(value = "/employees", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> saveEmployee(@Valid @RequestBody Employee employee,
                                                 @RequestParam("departmentId") Integer departmentId) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(employeeService.save(departmentId, employee));
    }

    @GetMapping(value = "/employees")
    public List<Employee> getAllEmployees() {
        return employeeService.getAllWithDepartments();
    }

    @JsonView(AdminView.class)
    @GetMapping(value = "/employees/{employeeId}")
    public Employee getEmployee(@PathVariable("employeeId") Integer employeeId) {
        return employeeService.getWithDepartment(employeeId);
    }

    @PostMapping(value = "/projects", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Project> saveProject(@Valid @RequestBody Project project,
                                               @RequestParam("clientId") Integer clientId) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(projectService.save(clientId, project));
    }

    @GetMapping("/projects")
    public List<Project> getAllProjects() {
        return projectService.getAllWithClients();
    }

    @GetMapping("/projects/{projectId}")
    public Project getProject(@PathVariable("projectId") Integer projectId) {
        return projectService.getWithClient(projectId);
    }

    @PostMapping(value = "/agreements", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkAgreement> saveWorkAgreement(@Valid @RequestBody WorkAgreement workAgreement,
                                                           @RequestParam("employeeId") Integer employeeId,
                                                           @RequestParam("projectId") Integer projectId) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(workAgreementService.save(employeeId, projectId, workAgreement));
    }

    @GetMapping(value = "/agreements")
    public List<AgreementDto> getAllAgreements(@RequestParam(value = "employeeId", required = false) Integer employeeId) {
        return employeeId == null ?
                workAgreementService.getAgreementsGraph() :
                workAgreementService.getAllForEmployee(employeeId);
    }

    @PostMapping(value = "/units", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkUnit> saveWorkUnit(@Valid @RequestBody WorkUnit workUnit,
                                                 @RequestParam("employeeId") Integer employeeId,
                                                 @RequestParam("agreementId") Integer agreementId) {
        return new ResponseEntity<>(workUnitService.save(employeeId, agreementId, workUnit), HttpStatus.CREATED);
    }

    @DeleteMapping("/units/{unitId}")
    public void deleteWorkUnit(@PathVariable("unitId") Integer unitId,
                               @RequestParam("employeeId") Integer employeeId) {
        workUnitService.delete(employeeId, unitId);
    }
}
