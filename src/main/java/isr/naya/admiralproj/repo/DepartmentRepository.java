package isr.naya.admiralproj.repo;

import isr.naya.admiralproj.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    @Query("select d from Department d join fetch d.employees")
    List<Department> getAllWithEmployees();
}
