package isr.naya.admiralproj.web;

import isr.naya.admiralproj.dto.AgreementDto;
import isr.naya.admiralproj.dto.WorkInfo;
import isr.naya.admiralproj.model.*;
import isr.naya.admiralproj.report.ReportCreator;
import isr.naya.admiralproj.service.*;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static isr.naya.admiralproj.report.ReportType.*;

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
    private ReportCreator pdfCreator;

    @GetMapping("/info/partial")
    public List<WorkInfo> getPartialDaysReport(@RequestParam(value = "from") LocalDate from,
                                               @RequestParam("to") LocalDate to,
                                               @RequestParam("limit") Integer limit) {
        return workInfoService.getPartialDays(from, to, limit);
    }

    @GetMapping("/info/missing")
    public List<WorkInfo> getMissingDaysReport(@RequestParam("from") LocalDate from,
                                              @RequestParam("to") LocalDate to) {
        return workInfoService.getMissingDays(from, to);
    }

    @GetMapping("/info/pivotal")
    public List<WorkInfo> getPivotalReport(@RequestParam("from") LocalDate from,
                                           @RequestParam("to") LocalDate to,
                                           @RequestParam("employeeId") Optional<Integer> employeeId,
                                           @RequestParam("projectId") Optional<Integer> projectId) {
        return getWorkInfos(from, to, employeeId, projectId);
    }

    @GetMapping("/units")
    public List<WorkInfo> getWorkUnits(@RequestParam("from") LocalDate from,
                                       @RequestParam("to") LocalDate to,
                                       @RequestParam("employeeId") Integer employeeId) {
        return workInfoService.getAllWorkUnitsForEmployee(employeeId, from, to);
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

    @PostMapping(value = "/employees/{departmentId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> saveEmployee(@Valid @RequestBody Employee employee,
                                                 @PathVariable("departmentId") Integer departmentId) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(employeeService.save(departmentId, employee));
    }

    @GetMapping(value = "/employees")
    public List<Employee> getAllEmployees() {
        return employeeService.getAllWithDepartments();
    }

    @GetMapping(value = "/employees/{employeeId}")
    public Employee getEmployee(@PathVariable("employeeId") Integer employeeId) {
        return employeeService.getWithDepartment(employeeId);
    }

    @PostMapping(value = "/projects/{clientId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Project> saveProject(@Valid @RequestBody Project project,
                                               @PathVariable("clientId") Integer clientId) {
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

    @PostMapping(value = "/agreements/{args}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkAgreement> saveWorkAgreement(@Valid @RequestBody WorkAgreement workAgreement,
                                                           @PathVariable("employeeId") Integer employeeId,
                                                           @PathVariable("projectId") Integer projectId) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(workAgreementService.save(employeeId, projectId, workAgreement));
    }

    @GetMapping(value = "/agreements/{employeeId}")
    public List<AgreementDto> getAllAgreements(@PathVariable(value = "employeeId", required = false) Integer employeeId) {
        return employeeId == null ?
                workAgreementService.getAgreementsGraph() :
                workAgreementService.getAllForEmployee(employeeId);
    }

    @PostMapping(value = "/units/{args}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkUnit> saveWorkUnit(@Valid @RequestBody WorkUnit workUnit,
                                                 @PathVariable("employeeId") Integer employeeId,
                                                 @PathVariable("agreementId") Integer agreementId) {
        return new ResponseEntity<>(workUnitService.save(employeeId, agreementId, workUnit), HttpStatus.CREATED);
    }

    @DeleteMapping("/units/{id}")
    public void deleteWorkUnit(@PathVariable("employeeId") Integer employeeId, @PathVariable("unitId") Integer unitId) {
        workUnitService.delete(employeeId, unitId);
    }

    @GetMapping(value = "/report/pivotal/pdf")
    @SneakyThrows
    public void getFullReport(@RequestParam("from") LocalDate from,
                              @RequestParam("to") LocalDate to,
                              @RequestParam("employeeId") Optional<Integer> employeeId,
                              @RequestParam("projectId") Optional<Integer> projectId,
                              HttpServletResponse response) {
        byte[] bytes = pdfCreator.create(getWorkInfos(from, to, employeeId, projectId), PIVOTAL);
        sendPdf(response, bytes);
    }

    @GetMapping(value = "/report/partial/pdf")
    public void getPartialReport(@RequestParam(value = "from") LocalDate from,
                                 @RequestParam("to") LocalDate to,
                                 @RequestParam("limit") Integer limit,
                                 HttpServletResponse response) {
        byte[] bytes = pdfCreator.create(workInfoService.getPartialDays(from, to, limit), PARTIAL);
        sendPdf(response, bytes);
    }

    @GetMapping(value = "/report/missing/pdf")
    public void getMissingReport(@RequestParam("from") LocalDate from,
                                 @RequestParam("to") LocalDate to,
                                 HttpServletResponse response) {
        byte[] bytes = pdfCreator.create(workInfoService.getMissingDays(from, to), EMPTY);
        sendPdf(response, bytes);
    }

    @SneakyThrows
    private void sendPdf(HttpServletResponse response, byte[] bytes) {
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        ServletOutputStream stream = response.getOutputStream();
        stream.write(bytes);
        stream.flush();
        stream.close();
    }

    private List<WorkInfo> getWorkInfos(LocalDate from, LocalDate to,
                                        Optional<Integer> employeeId,
                                        Optional<Integer> projectId) {
        List<WorkInfo> workInfos;
        if (employeeId.isPresent() && projectId.isPresent()) {
            workInfos = workInfoService.getAllUnitsByDateAndEmployeeAndProject(from, to, employeeId.get(), projectId.get());
        } else if (employeeId.isPresent())
            workInfos = workInfoService.getAllUnitsByDateAndEmployee(from, to, employeeId.get());
        else if (projectId.isPresent())
            workInfos = workInfoService.getAllUnitsByDateAndProject(from, to, projectId.get());
        else
            workInfos = workInfoService.getAllUnitsByDate(from, to);
        return workInfos;
    }
}
