package isr.naya.admiralproj.repo;

import isr.naya.admiralproj.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Integer> {

    @Query("select distinct c from Client c left join fetch c.projects")
    List<Client> getAllWithProjects();
}
