package isr.naya.admiralproj.repository;

import isr.naya.admiralproj.dto.WorkInfo;
import isr.naya.admiralproj.model.WorkUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@SuppressWarnings({"JpaQlInspection", "SpringDataRepositoryMethodReturnTypeInspection"})
public interface WorkUnitRepository extends JpaRepository<WorkUnit, Integer> {

    @Query("select count(wu) from WorkUnit wu where wu.workAgreement.employee.id = ?1 and wu.workAgreement.id = ?2 and wu.id <>?3 and wu.date = ?4 and not ((wu.start < ?5 and wu.finish < ?5) or (wu.start > ?6 and wu.finish > ?6))")
    Integer countExistedByDateTimeRange(Integer employeeId, Integer workAgreementId, Integer workUnitId, LocalDate date, LocalTime starts, LocalTime ends);

    @Modifying
    @Query("delete from WorkUnit wu where wu.id in (select wut.id from WorkUnit wut join wut.workAgreement wat join wat.employee emp where wut.id =?2 and emp.id =?1)")
    int delete(Integer employeeId, Integer workUnitId);

    @Query("select new isr.naya.admiralproj.dto.WorkInfo(e.id, e.name, e.surname, wu.date, sum (wu.duration)) from WorkUnit wu join wu.workAgreement wa join wa.employee e where wa.id = wu.workAgreement.id and e.id = wa.employee.id and wu.date>=?1 and wu.date<?2 group by e.id, wu.date having sum(wu.duration)<60*?3 order by wu.date desc ")
    List<WorkInfo> getAllPartialBetweenDates(LocalDate from, LocalDate to, Integer maxHours);

    @Query("select new isr.naya.admiralproj.dto.WorkInfo(e.id, wu.date) from WorkUnit wu join wu.workAgreement wa join wa.employee e where wa.id = wu.workAgreement.id and e.id = wa.employee.id and wu.date>=?1 and wu.date<?2 group by e.id, wu.date")
    Set<WorkInfo> getAllNonEmptyDaysBetweenDates(LocalDate from, LocalDate to);

    @Query("select new isr.naya.admiralproj.dto.WorkInfo(wu.id, wa.id, wu.date, wu.start, wu.finish, wu.duration, wu.absenceType, wu.comment) from WorkUnit wu join wu.workAgreement wa join wa.employee e where e.id = ?1 and wa.id=?2 and wu.date =?3 order by wu.start desc ")
    List<WorkInfo> getAllForEmployeeByDay(Integer employeeId, Integer workAgreementId, LocalDate day);

    @Query("select new isr.naya.admiralproj.dto.WorkInfo(wa.id, wu.date, sum (wu.duration)) from WorkUnit wu join wu.workAgreement wa join wa.employee e where e.id = ?1 and wu.date between ?2 and ?3 group by wa.id, wu.date order by wu.date desc ")
    List<WorkInfo> getAllForEmployeeBetweenDates(Integer employeeId, LocalDate from, LocalDate to);

    // Pivotal Report Block
    @Query("select new isr.naya.admiralproj.dto.WorkInfo(wu.id, wa.id, e.id, e.name, e.surname, d.name, p.id, p.name, c.id, c.name, wu.date, wu.absenceType, wu.start, wu.finish, wu.duration, wu.comment) from WorkUnit wu join wu.workAgreement wa join wa.employee e join wa.project p join p.client c join e.department d where wu.date between ?1 and ?2 order by wu.date desc ")
    List<WorkInfo> getAllByDateBetween(LocalDate from, LocalDate to);

    @Query("select new isr.naya.admiralproj.dto.WorkInfo(wu.id, wa.id, e.id, e.name, e.surname, d.name, p.id, p.name, c.id, c.name, wu.date, wu.absenceType, wu.start, wu.finish, wu.duration, wu.comment) from WorkUnit wu join wu.workAgreement wa join wa.employee e join wa.project p join p.client c join e.department d where e.id =?3 and wu.date between ?1 and ?2 order by wu.date desc ")
    List<WorkInfo> getAllByDateBetweenAndEmployeeId(LocalDate from, LocalDate to, Integer employeeId);

    @Query("select new isr.naya.admiralproj.dto.WorkInfo(wu.id, wa.id, e.id, e.name, e.surname, d.name, p.id, p.name, c.id, c.name, wu.date, wu.absenceType, wu.start, wu.finish, wu.duration, wu.comment) from WorkUnit wu join wu.workAgreement wa join wa.employee e join wa.project p join p.client c join e.department d where p.id =?3 and wu.date between ?1 and ?2 order by wu.date desc ")
    List<WorkInfo> getAllByDateBetweenAndProjectId(LocalDate from, LocalDate to, Integer projectId);

    @Query("select new isr.naya.admiralproj.dto.WorkInfo(wu.id, wa.id, e.id, e.name, e.surname, d.name, p.id, p.name, c.id, c.name, wu.date, wu.absenceType, wu.start, wu.finish, wu.duration, wu.comment) from WorkUnit wu join wu.workAgreement wa join wa.employee e join wa.project p join p.client c join e.department d where e.id =?3 and p.id=?4 and wu.date between ?1 and ?2 order by wu.date desc ")
    List<WorkInfo> getAllByDateBetweenAndEmployeeIdAndProjectId(LocalDate from, LocalDate to, Integer employeeId, Integer projectId);
}
