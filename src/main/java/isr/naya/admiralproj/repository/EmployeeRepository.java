package isr.naya.admiralproj.repository;


import isr.naya.admiralproj.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Query("select e from Employee e join fetch e.department")
    List<Employee> getAllWithDepartments();

    @Query("select e from Employee e join fetch e.department join fetch e.roles where e.id=?1")
    Employee getOneWithDepartment(Integer id);

    @Query("select e from Employee e join fetch e.roles where e.email=?1")
    Employee getByEmailWithRoles(String email);
}
