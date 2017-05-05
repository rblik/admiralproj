package isr.naya.admiralproj.web.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import isr.naya.admiralproj.AuthorizedUser;
import isr.naya.admiralproj.model.Employee;
import isr.naya.admiralproj.service.EmployeeService;
import isr.naya.admiralproj.util.JsonUtil;
import isr.naya.admiralproj.web.security.CorsRestController;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@CorsRestController
@RequestMapping("/admin/employees")
@AllArgsConstructor
public class AdminEmployeesController {

    private EmployeeService employeeService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> saveEmployee(@Valid @RequestBody Employee employee,
                                                 @RequestParam("departmentId") Integer departmentId) {
        Employee saved = employeeService.save(departmentId, employee);
        log.info("Admin {} saved a new employee {} with id = {} for department (id = {})", AuthorizedUser.fullName(), saved.getName() + saved.getSurname(), saved.getId(), departmentId);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        List<Employee> employees = employeeService.getAllWithDepartments();
        log.info("Admin {} is retrieving all employees", AuthorizedUser.fullName());
        return employees;
    }

    @JsonView(JsonUtil.AdminView.class)
    @GetMapping("/{employeeId}")
    public Employee getEmployee(@PathVariable("employeeId") Integer employeeId) {
        Employee employee = employeeService.getWithDepartment(employeeId);
        log.info("Admin {} is retrieving employee with id = {}", AuthorizedUser.fullName(), employeeId);
        return employee;
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<?> updatePassword(@PathVariable("employeeId") Integer employeeId,
                                            @RequestParam("password") String password) {
        employeeService.updatePass(employeeId, password);
        return ResponseEntity.status(OK).build();
    }
}
