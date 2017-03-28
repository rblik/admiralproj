package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.Employee;

public interface EmployeeService {

    Employee save(Integer departmentId, Employee employee);
}
