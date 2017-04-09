package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.Employee;

import java.util.List;

public interface EmployeeService {

    Employee save(Integer departmentId, Employee employee);

    List<Employee> getAllWithDepartments();

    List<Employee> getAllByDepartment(Integer departmentId);

    Employee get(Integer id);

    Employee getWithDepartment(Integer id);
}
