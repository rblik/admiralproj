package isr.naya.admiralproj.web;

import isr.naya.admiralproj.AuthorizedUser;
import isr.naya.admiralproj.model.Employee;
import isr.naya.admiralproj.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping()
    public Employee get() {
        return employeeService.get(AuthorizedUser.id());
    }
}
