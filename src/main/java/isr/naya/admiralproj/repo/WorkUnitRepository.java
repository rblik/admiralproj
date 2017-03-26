package isr.naya.admiralproj.repo;

import isr.naya.admiralproj.model.WorkUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface WorkUnitRepository extends JpaRepository<WorkUnit, Integer> {
    @Query("select count(wu) from WorkUnit wu where wu.workAgreement.employee.id = ?1 and wu.workAgreement.id = ?2 and not ((wu.start < ?3 and wu.finish < ?4) or (wu.start > ?3 and wu.finish > ?4))")
    Integer countExisted(Integer employeeId, Integer workAgreementId, LocalDateTime starts, LocalDateTime ends);
}
