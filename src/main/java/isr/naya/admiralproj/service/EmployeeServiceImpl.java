package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.Department;
import isr.naya.admiralproj.model.Employee;
import isr.naya.admiralproj.repo.DepartmentRepository;
import isr.naya.admiralproj.repo.EmployeeRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

import static isr.naya.admiralproj.exception.ValidationUtil.checkNotFound;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;
    private DepartmentRepository departmentRepository;

    @Override
    public Employee save(@NonNull Integer departmentId, @NonNull Employee employee) {
        employee.setDepartment(checkNotFound(departmentRepository.findOne(departmentId), departmentId, Department.class));
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllWithDepartments() {
        return employeeRepository.getAllWithDepartments();
    }

    @Override
    public Employee get(@NonNull Integer id) {
        return checkNotFound(employeeRepository.findOne(id), id, Employee.class);
    }

    @Override
    public Employee getWithDepartment(@NonNull Integer id) {
        return checkNotFound(employeeRepository.getOneWithDepartment(id), id, Employee.class);
    }
}
