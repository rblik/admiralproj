package isr.naya.admiralproj.repo;

import isr.naya.admiralproj.dto.MissingDay;
import isr.naya.admiralproj.dto.PartialDay;
import isr.naya.admiralproj.model.WorkUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public interface WorkUnitRepository extends JpaRepository<WorkUnit, Integer> {
    @Query("select count(wu) from WorkUnit wu where wu.workAgreement.employee.id = ?1 and wu.workAgreement.id = ?2 and wu.date = ?3 and not ((wu.start < ?4 and wu.finish < ?5) or (wu.start > ?4 and wu.finish > ?5))")
    Integer countExisted(Integer employeeId, Integer workAgreementId, LocalDate date, LocalTime starts, LocalTime ends);

    @Query("select new isr.naya.admiralproj.dto.PartialDay(e.id, e.name, e.surname, wu.date, sum (wu.duration)) from WorkUnit wu join wu.workAgreement wa join wa.employee e where wa.id = wu.workAgreement.id and e.id = wa.employee.id and wu.date>=?1 and wu.date<?2 group by e.id, wu.date having sum(wu.duration)<60*?3")
    Set<PartialDay> getAllPartialBetweenDates(LocalDate from, LocalDate to, Integer maxHours);

    @Query("select new isr.naya.admiralproj.dto.MissingDay(e.id, wu.date) from WorkUnit wu join wu.workAgreement wa join wa.employee e join e.department d where wa.id = wu.workAgreement.id and e.id = wa.employee.id and wu.date>=?1 and wu.date<?2 group by e.id, wu.date")
    Set<MissingDay> getAllNonEmptyDays(LocalDate from, LocalDate to);

    @Query("select wu from WorkUnit wu join fetch wu.workAgreement wa join fetch wa.employee join fetch wa.project p join fetch p.client where wu.date between ?1 and ?2")
    Set<WorkUnit> getAllByDateBetween(LocalDate from, LocalDate to);
}
