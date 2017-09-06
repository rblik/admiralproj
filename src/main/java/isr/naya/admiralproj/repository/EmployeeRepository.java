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
    @Query("update Employee e set e.password=?2, e.lastRegistrationCheck=0 where e.id=?1")
    int updatePassword(Integer employeeId, String pass);

    @Transactional
    @Modifying
    @Query("update Employee e set e.password=?2, e.lastRegistrationCheck=?3 where e.id=?1")
    int refreshPassword(Integer employeeId, String pass, long ts);

    @Transactional
    @Modifying
    @Query("update Employee e set e.enabled=false where e.id=?1")
    int disable(Integer employeeId);

    @Transactional
    @Modifying
    @Query("update Employee e set e.enabled=true where e.id=?1")
    int enable(Integer employeeId);

    @Query("select e from Employee e join fetch e.department order by e.name, e.surname")
    List<Employee> getAllWithDepartments();

    @Query("select distinct e from Employee e join fetch e.department join fetch e.roles where e.id=?1")
    Employee getOneWithDepartment(Integer id);

    @Query("select e from Employee e join fetch e.roles where lower(e.email)=?1")
    Employee getByEmailWithRoles(String email);

    @Query("select e from Employee e join fetch e.department d where d.id=?1")
    List<Employee> getAllByDepartment(Integer departmentId);

//    for missing days only
    @Query("select e from Employee e join fetch e.department d join fetch e.workAgreements wa where d.id=?1")
    List<Employee> getAllByDepartmentWithAgreements(Integer departmentId);

    @Query("select e from Employee e join fetch e.department d join fetch e.workAgreements wa where e.id=?1")
    Employee getOneWithDepartmentAndAgreements(Integer id);

    @Query("select e from Employee e join fetch e.department d join fetch e.workAgreements wa")
    List<Employee> getAllWithDepartmentsAndAgreements();

    @Query("select e from Employee e join fetch e.department d join fetch e.workAgreements wa where e.id in ?1")
    List<Employee> getPartucularWithDepartmentsAndAgreements(List<Integer> employeeIds);

    @Query("select e from Employee e where lower(e.email)=?1")
    Employee getByEmail(String email);

    List<Employee> findAll();
}
