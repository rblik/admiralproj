package isr.naya.admiralproj.repo;

import isr.naya.admiralproj.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    @Query("select p from Project p join fetch p.client")
    List<Project> getAllWithClients();
}
