package isr.naya.admiralproj.repo;

import isr.naya.admiralproj.dto.WorkInfo;
import isr.naya.admiralproj.model.WorkUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@SuppressWarnings("JpaQlInspection")
public interface WorkUnitRepository extends JpaRepository<WorkUnit, Integer> {
    @Query("select count(wu) from WorkUnit wu where wu.workAgreement.employee.id = ?1 and wu.workAgreement.id = ?2 and wu.date = ?3 and not ((wu.start < ?4 and wu.finish < ?5) or (wu.start > ?4 and wu.finish > ?5))")
    Integer countExisted(Integer employeeId, Integer workAgreementId, LocalDate date, LocalTime starts, LocalTime ends);

    @Query("select new isr.naya.admiralproj.dto.WorkInfo(e.id, e.name, e.surname, wu.date, sum (wu.duration)) from WorkUnit wu join wu.workAgreement wa join wa.employee e where wa.id = wu.workAgreement.id and e.id = wa.employee.id and wu.date>=?1 and wu.date<?2 group by e.id, wu.date having sum(wu.duration)<60*?3")
    Set<WorkInfo> getAllPartialBetweenDates(LocalDate from, LocalDate to, Integer maxHours);

    @Query("select new isr.naya.admiralproj.dto.WorkInfo(e.id, wu.date) from WorkUnit wu join wu.workAgreement wa join wa.employee e where wa.id = wu.workAgreement.id and e.id = wa.employee.id and wu.date>=?1 and wu.date<?2 group by e.id, wu.date")
    Set<WorkInfo> getAllNonEmptyDays(LocalDate from, LocalDate to);

    @Query("select new isr.naya.admiralproj.dto.WorkInfo(wa.id, wu.date, wu.start, wu.finish, wu.duration) from WorkUnit wu join wu.workAgreement wa join wa.employee e where e.id = ?1 and wu.date between ?2 and ?3")
    List<WorkInfo> getAllWithAgreements(Integer employeeId, LocalDate from, LocalDate to);

    @Query("select wu from WorkUnit wu join fetch wu.workAgreement wa join fetch wa.employee e join fetch wa.project p join fetch p.client c join fetch e.department where wu.date between ?1 and ?2")
    Set<WorkUnit> getAllByDateBetween(LocalDate from, LocalDate to);

    @Query("select wu from WorkUnit wu join fetch wu.workAgreement wa join fetch wa.employee e join fetch wa.project p join fetch p.client c join fetch e.department where e.id =?3 and wu.date between ?1 and ?2")
    Set<WorkUnit> getAllByDateBetweenAndEmployeeId(LocalDate from, LocalDate to, Integer employeeId);

    @Query("select wu from WorkUnit wu join fetch wu.workAgreement wa join fetch wa.employee e join fetch wa.project p join fetch p.client c join fetch e.department where p.id =?3 and wu.date between ?1 and ?2")
    Set<WorkUnit> getAllByDateBetweenAndProjectId(LocalDate from, LocalDate to, Integer projectId);

    @Query("select wu from WorkUnit wu join fetch wu.workAgreement wa join fetch wa.employee e join fetch wa.project p join fetch p.client c join fetch e.department where e.id =?3 and p.id=?4 and wu.date between ?1 and ?2")
    Set<WorkUnit> getAllByDateBetweenAndEmployeeIdAndProjectId(LocalDate from, LocalDate to, Integer employeeId, Integer projectId);
}
