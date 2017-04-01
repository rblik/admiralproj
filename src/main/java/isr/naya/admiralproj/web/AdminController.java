package isr.naya.admiralproj.web;

import com.fasterxml.jackson.annotation.JsonView;
import isr.naya.admiralproj.dto.AgreementDto;
import isr.naya.admiralproj.dto.WorkInfo;
import isr.naya.admiralproj.model.*;
import isr.naya.admiralproj.report.ReportCreator;
import isr.naya.admiralproj.service.*;
import isr.naya.admiralproj.util.JsonUtil.AdminView;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class AdminController {

    private static final String PDF_TYPE = "application/pdf";
    private static final String XLS_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    @Autowired
    private WorkInfoService workInfoService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private WorkAgreementService workAgreementService;
    @Autowired
    private WorkUnitService workUnitService;
    @Qualifier("PDF")
    @Autowired
    private ReportCreator pdfCreator;
    @Qualifier("XLS")
    @Autowired
    private ReportCreator xlsCreator;

    @GetMapping("/info/partial")
    public List<WorkInfo> getJsonPartialDaysReport(@RequestParam(value = "from") LocalDate from,
                                                   @RequestParam("to") LocalDate to,
                                                   @RequestParam("limit") Integer limit) {
        return workInfoService.getPartialDays(from, to, limit);
    }

    @GetMapping("/info/missing")
    public List<WorkInfo> getJsonMissingDaysReport(@RequestParam("from") LocalDate from,
                                                   @RequestParam("to") LocalDate to) {
        return workInfoService.getMissingDays(from, to);
    }

    @GetMapping("/info/pivotal")
    public List<WorkInfo> getJsonPivotalReport(@RequestParam("from") LocalDate from,
                                               @RequestParam("to") LocalDate to,
                                               @RequestParam("employeeId") Optional<Integer> employeeId,
                                               @RequestParam("projectId") Optional<Integer> projectId) {
        return getWorkInfos(from, to, employeeId, projectId);
    }

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

    @GetMapping(value = "/pdf/pivotal")
    public void getPDFPivotalReport(@RequestParam("from") LocalDate from,
                                    @RequestParam("to") LocalDate to,
                                    @RequestParam("employeeId") Optional<Integer> employeeId,
                                    @RequestParam("projectId") Optional<Integer> projectId,
                                    HttpServletResponse response) {
        byte[] bytes = pdfCreator.create(getWorkInfos(from, to, employeeId, projectId), PIVOTAL);
        sendReport(response, bytes, PDF_TYPE);
    }

    @GetMapping(value = "/pdf/partial")
    public void getPDFPartialDaysReport(@RequestParam(value = "from") LocalDate from,
                                        @RequestParam("to") LocalDate to,
                                        @RequestParam("limit") Integer limit,
                                        HttpServletResponse response) {
        byte[] bytes = pdfCreator.create(workInfoService.getPartialDays(from, to, limit), PARTIAL);
        sendReport(response, bytes, PDF_TYPE);
    }

    @GetMapping(value = "/pdf/missing")
    public void getPDFMissingDaysReport(@RequestParam("from") LocalDate from,
                                        @RequestParam("to") LocalDate to,
                                        HttpServletResponse response) {
        byte[] bytes = pdfCreator.create(workInfoService.getMissingDays(from, to), EMPTY);
        sendReport(response, bytes, PDF_TYPE);
    }

    @GetMapping(value = "/xls/pivotal")
    public void getXLSPivotalReport(@RequestParam("from") LocalDate from,
                                    @RequestParam("to") LocalDate to,
                                    @RequestParam("employeeId") Optional<Integer> employeeId,
                                    @RequestParam("projectId") Optional<Integer> projectId,
                                    HttpServletResponse response) {
        byte[] bytes = xlsCreator.create(getWorkInfos(from, to, employeeId, projectId), PIVOTAL);
        sendReport(response, bytes, XLS_TYPE);
    }

    @GetMapping(value = "/xls/partial")
    public void getXLSPartialDaysReport(@RequestParam(value = "from") LocalDate from,
                                        @RequestParam("to") LocalDate to,
                                        @RequestParam("limit") Integer limit,
                                        HttpServletResponse response) {
        byte[] bytes = xlsCreator.create(workInfoService.getPartialDays(from, to, limit), PARTIAL);
        sendReport(response, bytes, XLS_TYPE);
    }

    @GetMapping(value = "/xls/missing")
    public void getXLSMissingDaysReport(@RequestParam("from") LocalDate from,
                                        @RequestParam("to") LocalDate to,
                                        HttpServletResponse response) {
        byte[] bytes = xlsCreator.create(workInfoService.getMissingDays(from, to), EMPTY);
        sendReport(response, bytes, XLS_TYPE);
    }

    @SneakyThrows
    private void sendReport(HttpServletResponse response, byte[] bytes, String returnType) {
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType(returnType);
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
