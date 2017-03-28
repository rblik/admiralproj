package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.Department;
import isr.naya.admiralproj.model.Employee;
import isr.naya.admiralproj.repo.DepartmentRepository;
import isr.naya.admiralproj.repo.EmployeeRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static isr.naya.admiralproj.exception.ValidationUtil.checkNotFound;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public Employee save(@NonNull Integer departmentId, @NonNull Employee employee) {
        employee.setDepartment(checkNotFound(departmentRepository.findOne(departmentId), departmentId, Department.class));
        return employeeRepository.save(employee);
    }
}
