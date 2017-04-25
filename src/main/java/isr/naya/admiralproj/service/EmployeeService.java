package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.Employee;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeService {

    Employee save(Integer departmentId, Employee employee);

    int updatePass(Integer employeeId, String pass);

    List<Employee> getAllWithDepartments();

    List<Employee> getAllByDepartment(Integer departmentId);

    Employee get(Integer id);

    Employee getWithDepartment(Integer id);

//    for missing days only
    List<Employee> getAllByDepartmentWithAgreements(Integer departmentId, LocalDate from, LocalDate to);

    Employee getWithDepartmentAndAgreements(Integer id, LocalDate from, LocalDate to);

    List<Employee> getAllWithDepartmentsAndAgreements(LocalDate from, LocalDate to);
}
