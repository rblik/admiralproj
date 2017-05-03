package isr.naya.admiralproj.repository;

import isr.naya.admiralproj.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    @Query("select p from Project p join fetch p.client")
    List<Project> getAllWithClients();

    @Query("select p from Project p join fetch p.client join p.workAgreements wa join wa.employee e where e.id=?1")
    List<Project> getAllWithClientsByEmployeeId(Integer employeeId);

    @Query("select p from Project p join fetch p.client where p.id=?1")
    Project getOneWithClient(Integer id);

    @Query("select p from Project p join fetch p.tariff where p.client.id=?1")
    List<Project> findAllByClientId(Integer clientId);
}
