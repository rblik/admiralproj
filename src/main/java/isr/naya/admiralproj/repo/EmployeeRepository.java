package isr.naya.admiralproj.repo;


import isr.naya.admiralproj.model.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Query("select e from Employee e join fetch e.workAgreements wa join fetch wa.tariff where e.department.name = ?1")
    List<Employee> findByDepartmentName(String departmentName);

}
