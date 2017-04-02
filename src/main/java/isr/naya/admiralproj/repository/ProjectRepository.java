package isr.naya.admiralproj.repository;

import isr.naya.admiralproj.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    @Query("select p from Project p join fetch p.client")
    List<Project> getAllWithClients();

    @Query("select p from Project p join fetch p.client where p.id=?1")
    Project getOneWithClient(Integer id);
}
