package isr.naya.admiralproj.service;

import isr.naya.admiralproj.AuthorizedUser;
import isr.naya.admiralproj.model.Department;
import isr.naya.admiralproj.model.Employee;
import isr.naya.admiralproj.repository.DepartmentRepository;
import isr.naya.admiralproj.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static isr.naya.admiralproj.util.ValidationUtil.checkNotFound;
import static isr.naya.admiralproj.web.security.password.PasswordUtil.encode;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService, UserDetailsService {

    private EmployeeRepository employeeRepository;
    private DepartmentRepository departmentRepository;

    @CacheEvict(value = "employees", allEntries = true)
    @Override
    @Transactional
    public Employee save(@NonNull Integer departmentId, @NonNull Employee employee) {
        employee.setDepartment(checkNotFound(departmentRepository.findOne(departmentId), departmentId, Department.class));
        return employeeRepository.save(employee);
    }

    @CacheEvict(value = "employees", allEntries = true)
    @Override
    public int updatePass(@NonNull Integer employeeId, @NonNull String pass) {
        return checkNotFound(employeeRepository.updatePassword(employeeId, encode(pass)), employeeId, Employee.class);
    }

    @CacheEvict(value = "employees", allEntries = true)
    @Override
    public int refreshPass(Integer employeeId, String pass) {
        long ts = System.currentTimeMillis();
        return checkNotFound(employeeRepository.refreshPassword(employeeId, encode(pass), ts), employeeId, Employee.class);
    }

    @Override
    public int enable(@NonNull Integer employeeId) {
        return checkNotFound(employeeRepository.enable(employeeId), employeeId, Employee.class);
    }

    @Override
    public int disable(@NonNull Integer employeeId) {
        return checkNotFound(employeeRepository.disable(employeeId), employeeId, Employee.class);
    }

    @Cacheable(value = "employees", key = "getMethodName()")
    @Override
    public List<Employee> getAllWithDepartments() {
        return employeeRepository.getAllWithDepartments();
    }

    @Cacheable(value = "employees", key = "getMethodName() + #departmentId")
    @Override
    public List<Employee> getAllByDepartment(@NonNull Integer departmentId) {
        return employeeRepository.getAllByDepartment(departmentId);
    }

    @Override
    public Employee get(@NonNull Integer id) {
        return checkNotFound(employeeRepository.findOne(id), id, Employee.class);
    }

    @Cacheable(value = "employees", key = "getMethodName() + #id")
    @Override
    public Employee getWithDepartment(@NonNull Integer id) {
        return checkNotFound(employeeRepository.getOneWithDepartment(id), id, Employee.class);
    }

//    for missing days only
    @Override
    public List<Employee> getAllByDepartmentWithAgreements(@NonNull Integer departmentId) {
        return employeeRepository.getAllByDepartmentWithAgreements(departmentId);
    }

    @Override
    public Employee getWithDepartmentAndAgreements(@NonNull Integer id) {
        return employeeRepository.getOneWithDepartmentAndAgreements(id);
    }

    @Override
    public List<Employee> getAllWithDepartmentsAndAgreements() {
        return employeeRepository.getAllWithDepartmentsAndAgreements();
    }

    @Override
    public List<Employee> getParticularWithDepartmentsAndAgreements(@NonNull List<Integer> employeeIds) {
        return employeeRepository.getPartucularWithDepartmentsAndAgreements(employeeIds);
    }

    //    security
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employee employee = employeeRepository.getByEmailWithRoles(email.toLowerCase());
        if (employee == null) {
            throw new UsernameNotFoundException("Employee " + email + " is not found");
        }
        return new AuthorizedUser(employee);
    }
}
