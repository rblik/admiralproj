package isr.naya.admiralproj.repo;

import isr.naya.admiralproj.model.WorkUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;

public interface WorkUnitRepository extends JpaRepository<WorkUnit, Integer> {
    @Query("select count(wu) from WorkUnit wu where wu.workAgreement.employee.id = ?1 and wu.workAgreement.id = ?2 and wu.date = ?3 and not ((wu.start < ?4 and wu.finish < ?5) or (wu.start > ?4 and wu.finish > ?5))")
    Integer countExisted(Integer employeeId, Integer workAgreementId, LocalDate date, LocalTime starts, LocalTime ends);
}
