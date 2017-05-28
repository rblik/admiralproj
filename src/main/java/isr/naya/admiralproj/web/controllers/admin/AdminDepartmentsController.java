package isr.naya.admiralproj.web.controllers.admin;

import isr.naya.admiralproj.AuthorizedUser;
import isr.naya.admiralproj.model.Department;
import isr.naya.admiralproj.service.DepartmentService;
import isr.naya.admiralproj.web.security.CorsRestController;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@CorsRestController
@RequestMapping("/admin/departments")
@AllArgsConstructor
public class AdminDepartmentsController {

    private DepartmentService departmentService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Department> saveDepartment(@Valid @RequestBody Department department) {
        Department saved = departmentService.save(department);
        log.info("Admin {} saved a new department {} with id = {}", AuthorizedUser.fullName(), saved.getName(), saved.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public List<Department> getAllDepartments() {
        List<Department> departments = departmentService.getAll();
        log.info("Admin {} is retrieving all departments", AuthorizedUser.fullName());
        return departments;
    }

    @GetMapping("/{departmentId}")
    public Department getDepartment(@PathVariable("departmentId") Integer departmentId) {
        Department department = departmentService.get(departmentId);
        log.info("Admin {} is retrieving department with id = {}", AuthorizedUser.fullName(), departmentId);
        return department;
    }
}
