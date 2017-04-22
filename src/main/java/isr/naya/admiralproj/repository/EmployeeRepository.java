package isr.naya.admiralproj.repository;


import isr.naya.admiralproj.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Transactional
    @Modifying
    @Query("update Employee e set e.password=?2 where e.id=?1")
    int updatePassword(Integer employeeId, String pass);

    @Query("select e from Employee e join fetch e.department")
    List<Employee> getAllWithDepartments();

    @Query("select distinct e from Employee e join fetch e.department join fetch e.roles where e.id=?1")
    Employee getOneWithDepartment(Integer id);

    @Query("select e from Employee e join fetch e.roles where e.email=?1")
    Employee getByEmailWithRoles(String email);

    @Query("select e from Employee e join fetch e.department d where d.id=?1")
    List<Employee> getAllByDepartment(Integer departmentId);
}
