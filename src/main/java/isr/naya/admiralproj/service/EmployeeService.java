package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.Employee;

import java.util.List;

public interface EmployeeService {

    Employee save(Integer departmentId, Employee employee);

    int updatePass(Integer employeeId, String pass);

    int refreshPass(Integer employeeId, String pass);

    int enable(Integer employeeId);

    int disable(Integer employeeId);

    List<Employee> getAllWithDepartments();

    List<Employee> getAllByDepartment(Integer departmentId);

    Employee get(Integer id);

    Employee getWithDepartment(Integer id);

//    for missing days only
    List<Employee> getAllByDepartmentWithAgreements(Integer departmentId);

    Employee getWithDepartmentAndAgreements(Integer id);

    List<Employee> getAllWithDepartmentsAndAgreements();

    List<Employee> getParticularWithDepartmentsAndAgreements(List<Integer> employeeIds);

    Employee getByEmail(String email);
}
