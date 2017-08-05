package isr.naya.admiralproj.repository;

import isr.naya.admiralproj.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Integer> {

    @Query("select distinct c from Client c left join fetch c.addresses a left join fetch c.phones order by c.name")
    List<Client> getAllWithAddressesAndPhones();

    @Query("select distinct c from Client c left join fetch c.addresses a left join fetch c.phones where c.id=?1  order by c.name")
    Client getWithAddressesAndPhones(Integer clientId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Client c WHERE c.id=:id")
    int remove(@Param("id") int id);
}
