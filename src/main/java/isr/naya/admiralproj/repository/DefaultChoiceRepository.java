package isr.naya.admiralproj.repository;

import isr.naya.admiralproj.model.DefaultChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public interface DefaultChoiceRepository extends JpaRepository<DefaultChoice, Integer> {

    DefaultChoice findByIdAndAgreementActive(Integer integer, boolean active);

    Collection<DefaultChoice> findAllByEmployeeIdAndAgreementActive(Integer integer, boolean active);

    Integer removeDefaultChoiceById(Integer id);

    @Modifying
    @Transactional
    @Query("delete from DefaultChoice deff where deff.id = (select def.id FROM DefaultChoice def WHERE def.agreement.project.id=:projectId)")
    int cleanDefaultChoicesByProjectId(@Param("projectId") int projectId);

    @Modifying
    @Transactional
    @Query("delete from DefaultChoice deff where deff.id = (select def.id FROM DefaultChoice def WHERE def.agreement.id=:agreementId)")
    int cleanDefaultChoicesByAgreementId(@Param("agreementId") int agreementId);
}
