package isr.naya.admiralproj.repository;

import isr.naya.admiralproj.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    @Query("select distinct d from Department d left join fetch d.employees order by d.name")
    List<Department> getAllWithEmployees();

    List<Department> findAllByOrderByName();
}
