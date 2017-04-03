package isr.naya.admiralproj.service;

import isr.naya.admiralproj.AuthorizedUser;
import isr.naya.admiralproj.model.Department;
import isr.naya.admiralproj.model.Employee;
import isr.naya.admiralproj.repository.DepartmentRepository;
import isr.naya.admiralproj.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static isr.naya.admiralproj.util.ValidationUtil.checkNotFound;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService, UserDetailsService {

    private EmployeeRepository employeeRepository;
    private DepartmentRepository departmentRepository;

    @Override
    @Transactional
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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employee employee = employeeRepository.getByEmailWithRoles(email.toLowerCase());
        if (employee == null) {
            throw new UsernameNotFoundException("Employee " + email + " is not found");
        }
        return new AuthorizedUser(employee);
    }
}
