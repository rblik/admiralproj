package isr.naya.admiralproj.web;

import com.fasterxml.jackson.annotation.JsonView;
import isr.naya.admiralproj.AuthorizedUser;
import isr.naya.admiralproj.dto.AgreementDto;
import isr.naya.admiralproj.dto.ClientDto;
import isr.naya.admiralproj.dto.WorkInfo;
import isr.naya.admiralproj.model.*;
import isr.naya.admiralproj.service.*;
import isr.naya.admiralproj.util.JsonUtil.AdminView;
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
        List<WorkInfo> infos = workInfoService.getAllForEmployee(employeeId, from, to);
        log.info("Admin {} is retrieving all work info for employee (id = {}) from {} to {}", AuthorizedUser.fullName(), employeeId, from, to);
        return infos;
    }

    @PostMapping(value = "/clients", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Client> saveClient(@Valid @RequestBody Client client) {
        Client saved = clientService.save(client);
        log.info("Admin {} saved a new client {} with id = {}", AuthorizedUser.fullName(), saved.getName(), saved.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/clients")
    public List<Client> getAllClients() {
        List<Client> clients = clientService.getAll();
        log.info("Admin {} is retrieving all clients", AuthorizedUser.fullName());
        return clients;
    }

    @GetMapping("/clients/{clientId}")
    public ClientDto getClient(@PathVariable("clientId") Integer clientId) {
        ClientDto client = clientService.get(clientId);
        log.info("Admin {} is retrieving client with id = {}", AuthorizedUser.fullName(), clientId);
        return client;
    }

    @PostMapping(value = "/departments", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Department> saveDepartment(@Valid @RequestBody Department department) {
        Department saved = departmentService.save(department);
        log.info("Admin {} saved a new department {} with id = {}", AuthorizedUser.fullName(), saved.getName(), saved.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/departments")
    public List<Department> getAllDepartments() {
        List<Department> departments = departmentService.getAll();
        log.info("Admin {} is retrieving all departments", AuthorizedUser.fullName());
        return departments;
    }

    @GetMapping("/departments/{departmentId}")
    public Department getDepartment(@PathVariable("departmentId") Integer departmentId) {
        Department department = departmentService.get(departmentId);
        log.info("Admin {} is retrieving department with id = {}", AuthorizedUser.fullName(), departmentId);
        return department;
    }

    @PostMapping(value = "/employees", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> saveEmployee(@Valid @RequestBody Employee employee,
                                                 @RequestParam("departmentId") Integer departmentId) {
        Employee saved = employeeService.save(departmentId, employee);
        log.info("Admin {} saved a new employee {} with id = {} for department (id = {})", AuthorizedUser.fullName(), saved.getName() + saved.getSurname(), saved.getId(), departmentId);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping(value = "/employees")
    public List<Employee> getAllEmployees() {
        List<Employee> employees = employeeService.getAllWithDepartments();
        log.info("Admin {} is retrieving all employees", AuthorizedUser.fullName());
        return employees;
    }

    @JsonView(AdminView.class)
    @GetMapping(value = "/employees/{employeeId}")
    public Employee getEmployee(@PathVariable("employeeId") Integer employeeId) {
        Employee employee = employeeService.getWithDepartment(employeeId);
        log.info("Admin {} is retrieving employee with id = {}", AuthorizedUser.fullName(), employeeId);
        return employee;
    }

    @PostMapping(value = "/projects", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Project> saveProject(@Valid @RequestBody Project project,
                                               @RequestParam("clientId") Integer clientId) {
        Project saved = projectService.save(clientId, project);
        log.info("Admin {} saved a new project {} with id = {} for client (id = {})", AuthorizedUser.fullName(), saved.getName(), saved.getId(), clientId);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/projects")
    public List<Project> getAllProjects() {
        List<Project> projects = projectService.getAllWithClients();
        log.info("Admin {} is retrieving all projects", AuthorizedUser.fullName());
        return projects;
    }

    @GetMapping("/projects")
    public List<Project> getAllProjects(@RequestParam("employeeId") Integer employeeId) {
        List<Project> projects = projectService.getAllWithClientsByEmployee(employeeId);
        log.info("Admin {} is retrieving all projects for employee (id = {})", AuthorizedUser.fullName(), employeeId);
        return projects;
    }

    @GetMapping("/projects/{projectId}")
    public Project getProject(@PathVariable("projectId") Integer projectId) {
        Project project = projectService.getWithClient(projectId);
        log.info("Admin {} is retrieving project with id = {}", AuthorizedUser.fullName(), projectId);
        return project;
    }

    @PostMapping(value = "/agreements", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkAgreement> saveWorkAgreement(@Valid @RequestBody WorkAgreement workAgreement,
                                                           @RequestParam("employeeId") Integer employeeId,
                                                           @RequestParam("projectId") Integer projectId) {
        WorkAgreement saved = workAgreementService.save(employeeId, projectId, workAgreement);
        log.info("Admin {} saved a new work agreement with id = {} for employee (id = {}) and project (id = {})", AuthorizedUser.fullName(), saved.getId(), employeeId, projectId);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
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
        WorkUnit saved = workUnitService.save(employeeId, agreementId, workUnit);
        log.info("Admin {} saved a new work unit (id = {}) for employee (id = {}) and agreement (id = {})", AuthorizedUser.fullName(), saved.getId(), employeeId, agreementId);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @DeleteMapping("/units/{unitId}")
    public void deleteWorkUnit(@PathVariable("unitId") Integer unitId,
                               @RequestParam("employeeId") Integer employeeId) {
        workUnitService.delete(employeeId, unitId);
        log.info("Admin {} removed work unit with id = {}", AuthorizedUser.fullName(), unitId);
    }
}
