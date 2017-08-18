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

    @Query("select count(wu) from WorkUnit wu where wu.workAgreement.employee.id = ?1 and wu.workAgreement.id = ?2 and wu.id <>?3 and wu.date = ?4 and not ((wu.start <= ?5 and wu.finish <= ?5) or (wu.start >= ?6 and wu.finish >= ?6))")
    Integer countExistedByDateTimeRange(Integer employeeId, Integer workAgreementId, Integer workUnitId, LocalDate date, LocalTime starts, LocalTime ends);

    @Modifying
    @Query("delete from WorkUnit wu where wu.id in (select wut.id from WorkUnit wut join wut.workAgreement wat join wat.employee emp where wut.id =?2 and emp.id =?1)")
    int delete(Integer employeeId, Integer workUnitId);

    // Dashboard
    @Query("select new isr.naya.admiralproj.dto.WorkInfo(wu.id, wa.id, p.id, p.name, c.id, c.name, wu.date, wu.start, wu.finish, wu.duration, wu.comment) from WorkUnit wu join wu.workAgreement wa join wa.employee e join wa.project p join p.client c where e.id = ?1 and wa.id=?2 and wu.date =?3 order by wu.start desc ")
    List<WorkInfo> getAllForEmployeeByDayAndAgreement(Integer employeeId, Integer workAgreementId, LocalDate day);

    @Query("select new isr.naya.admiralproj.dto.WorkInfo(wu.id, wa.id, p.id, p.name, c.id, c.name, wu.date, wu.start, wu.finish, wu.duration, wu.comment, wa.active) from WorkUnit wu join wu.workAgreement wa join wa.employee e join wa.project p join p.client c where e.id = ?1 and wu.date =?2 order by wu.start desc ")
    List<WorkInfo> getAllForEmployeeByDay(Integer employeeId, LocalDate day);

    @Query("select new isr.naya.admiralproj.dto.WorkInfo(wa.id, wu.date, wa.project.client.name, sum (wu.duration)) from WorkUnit wu join wu.workAgreement wa join wa.employee e join wa.project p join p.client c where e.id = ?1 and wu.date>=?2 and wu.date<?3 group by wa.id, wa.project.client.id, wa.project.client.name, wu.date order by wu.date desc ")
    List<WorkInfo> getAllForEmployeeBetweenDates(Integer employeeId, LocalDate from, LocalDate to);

    // Partial Report Block
    @Query("select new isr.naya.admiralproj.dto.WorkInfo(e.id, e.name, e.surname, e.email, e.employeeNumber, d.name, wu.date, sum (wu.duration)) from WorkUnit wu join wu.workAgreement wa join wa.employee e join e.department d where wa.id = wu.workAgreement.id and e.id = wa.employee.id and d.id = e.department.id and wu.date>=?1 and wu.date<?2 group by e.id, wu.date, d.id having sum(wu.duration)<60*?3 order by wu.date desc ")
    List<WorkInfo> getAllPartialBetweenDates(LocalDate from, LocalDate to, Integer maxHours);

    @Query("select new isr.naya.admiralproj.dto.WorkInfo(e.id, e.name, e.surname, e.email, e.employeeNumber, d.name, wu.date, sum (wu.duration)) from WorkUnit wu join wu.workAgreement wa join wa.employee e join e.department d where wa.id = wu.workAgreement.id and e.id = wa.employee.id and d.id = e.department.id and e.id=?4 and wu.date>=?1 and wu.date<?2 group by e.id, wu.date, d.id having sum(wu.duration)<60*?3 order by wu.date desc ")
    List<WorkInfo> getAllPartialBetweenDatesByEmployeeId(LocalDate from, LocalDate to, Integer maxHours, Integer employeeId);

    @Query("select new isr.naya.admiralproj.dto.WorkInfo(e.id, e.name, e.surname, e.email, e.employeeNumber, d.name, wu.date, sum (wu.duration)) from WorkUnit wu join wu.workAgreement wa join wa.employee e join e.department d where wa.id = wu.workAgreement.id and e.id = wa.employee.id and d.id = e.department.id and d.id=?4 and wu.date>=?1 and wu.date<?2 group by e.id, wu.date, d.id having sum(wu.duration)<60*?3 order by wu.date desc ")
    List<WorkInfo> getAllPartialBetweenDatesByDepartmentId(LocalDate from, LocalDate to, Integer maxHours, Integer departmentId);

    // Missing Report Block
    @Query("select new isr.naya.admiralproj.dto.WorkInfo(e.id, wu.date) from WorkUnit wu join wu.workAgreement wa join wa.employee e where wa.id = wu.workAgreement.id and e.id = wa.employee.id and wu.date>=?1 and wu.date<?2 group by e.id, wu.date")
    Set<WorkInfo> getAllNonEmptyDaysBetweenDates(LocalDate from, LocalDate to);

    @Query("select new isr.naya.admiralproj.dto.WorkInfo(e.id, wu.date) from WorkUnit wu join wu.workAgreement wa join wa.employee e where wa.id = wu.workAgreement.id and e.id = wa.employee.id and e.id=?3 and wu.date>=?1 and wu.date<?2 group by e.id, wu.date")
    Set<WorkInfo> getAllNonEmptyDaysByDateBetweenAndEmployeeId(LocalDate from, LocalDate to, Integer employeeId);

    @Query("select new isr.naya.admiralproj.dto.WorkInfo(e.id, wu.date) from WorkUnit wu join wu.workAgreement wa join wa.employee e join e.department d where wa.id = wu.workAgreement.id and e.id = wa.employee.id and d.id=?3 and wu.date>=?1 and wu.date<?2 group by e.id, wu.date")
    Set<WorkInfo> getAllNonEmptyDaysByDateBetweenAndDepartmentId(LocalDate from, LocalDate to, Integer departmentId);

    @Query("select new isr.naya.admiralproj.dto.WorkInfo(e.id, wu.date) from WorkUnit wu join wu.workAgreement wa join wa.employee e where wa.id = wu.workAgreement.id and e.id = wa.employee.id and wu.date>=?1 and wu.date<?2 and e.id in ?3 group by e.id, wu.date")
    Set<WorkInfo> getAllNonEmptyDaysBetweenAndEmployeeIds(LocalDate from, LocalDate to, List<Integer> employeeIds);

    // Pivotal Report Block
    @Query("select new isr.naya.admiralproj.dto.WorkInfo(wu.id, wa.id, e.id, e.name, e.surname, e.email, e.employeeNumber, d.name, p.id, p.name, c.id, c.name, wu.date, wu.start, wu.finish, wu.duration, wu.comment) from WorkUnit wu join wu.workAgreement wa join wa.employee e join wa.project p join p.client c join e.department d where wu.date>=?1 and wu.date<?2 order by wu.date desc ")
    List<WorkInfo> getAllByDateBetween(LocalDate from, LocalDate to);

    @Query("select new isr.naya.admiralproj.dto.WorkInfo(wu.id, wa.id, e.id, e.name, e.surname, e.email, e.employeeNumber, d.name, p.id, p.name, c.id, c.name, wu.date, wu.start, wu.finish, wu.duration, wu.comment) from WorkUnit wu join wu.workAgreement wa join wa.employee e join wa.project p join p.client c join e.department d where e.id =?3 and wu.date>=?1 and wu.date<?2 order by wu.date desc ")
    List<WorkInfo> getAllByDateBetweenAndEmployeeId(LocalDate from, LocalDate to, Integer employeeId);

    @Query("select new isr.naya.admiralproj.dto.WorkInfo(wu.id, wa.id, e.id, e.name, e.surname, e.email, e.employeeNumber, d.name, p.id, p.name, c.id, c.name, wu.date, wu.start, wu.finish, wu.duration, wu.comment) from WorkUnit wu join wu.workAgreement wa join wa.employee e join wa.project p join p.client c join e.department d where d.id =?3 and wu.date>=?1 and wu.date<?2 order by wu.date desc ")
    List<WorkInfo> getAllByDateBetweenAndDepartmentId(LocalDate from, LocalDate to, Integer departmentId);

    @Query("select new isr.naya.admiralproj.dto.WorkInfo(wu.id, wa.id, e.id, e.name, e.surname, e.email, e.employeeNumber, d.name, p.id, p.name, c.id, c.name, wu.date, wu.start, wu.finish, wu.duration, wu.comment) from WorkUnit wu join wu.workAgreement wa join wa.employee e join wa.project p join p.client c join e.department d where p.id =?3 and wu.date>=?1 and wu.date<?2 order by wu.date desc ")
    List<WorkInfo> getAllByDateBetweenAndProjectId(LocalDate from, LocalDate to, Integer projectId);

    @Query("select new isr.naya.admiralproj.dto.WorkInfo(wu.id, wa.id, e.id, e.name, e.surname, e.email, e.employeeNumber, d.name, p.id, p.name, c.id, c.name, wu.date, wu.start, wu.finish, wu.duration, wu.comment) from WorkUnit wu join wu.workAgreement wa join wa.employee e join wa.project p join p.client c join e.department d where c.id =?3 and wu.date>=?1 and wu.date<?2 order by wu.date desc ")
    List<WorkInfo> getAllByDateBetweenAndClientId(LocalDate from, LocalDate to, Integer clientId);

    @Query("select new isr.naya.admiralproj.dto.WorkInfo(wu.id, wa.id, e.id, e.name, e.surname, e.email, e.employeeNumber, d.name, p.id, p.name, c.id, c.name, wu.date, wu.start, wu.finish, wu.duration, wu.comment) from WorkUnit wu join wu.workAgreement wa join wa.employee e join wa.project p join p.client c join e.department d where e.id =?3 and c.id=?4 and wu.date>=?1 and wu.date<?2 order by wu.date desc ")
    List<WorkInfo> getAllByDateBetweenAndEmployeeIdAndClientId(LocalDate from, LocalDate to, Integer employeeId, Integer clientId);

    @Query("select new isr.naya.admiralproj.dto.WorkInfo(wu.id, wa.id, e.id, e.name, e.surname, e.email, e.employeeNumber, d.name, p.id, p.name, c.id, c.name, wu.date, wu.start, wu.finish, wu.duration, wu.comment) from WorkUnit wu join wu.workAgreement wa join wa.employee e join wa.project p join p.client c join e.department d where d.id =?3 and p.id=?4 and wu.date>=?1 and wu.date<?2 order by wu.date desc ")
    List<WorkInfo> getAllByDateBetweenAndDepartmentIdAndProjectId(LocalDate from, LocalDate to, Integer departmentId, Integer projectId);

    @Query("select new isr.naya.admiralproj.dto.WorkInfo(wu.id, wa.id, e.id, e.name, e.surname, e.email, e.employeeNumber, d.name, p.id, p.name, c.id, c.name, wu.date, wu.start, wu.finish, wu.duration, wu.comment) from WorkUnit wu join wu.workAgreement wa join wa.employee e join wa.project p join p.client c join e.department d where d.id =?3 and c.id=?4 and wu.date>=?1 and wu.date<?2 order by wu.date desc ")
    List<WorkInfo> getAllByDateBetweenAndDepartmentIdAndClientId(LocalDate from, LocalDate to, Integer departmentId, Integer clientId);

    @Query("select new isr.naya.admiralproj.dto.WorkInfo(wu.id, wa.id, e.id, e.name, e.surname, e.email, e.employeeNumber, d.name, p.id, p.name, c.id, c.name, wu.date, wu.start, wu.finish, wu.duration, wu.comment) from WorkUnit wu join wu.workAgreement wa join wa.employee e join wa.project p join p.client c join e.department d where e.id =?3 and p.id=?4 and wu.date>=?1 and wu.date<?2 order by wu.date desc ")
    List<WorkInfo> getAllByDateBetweenAndEmployeeIdAndProjectId(LocalDate from, LocalDate to, Integer employeeId, Integer projectId);

    // Income Report Block
    @Query("select new isr.naya.admiralproj.dto.WorkInfo(e.id, e.name, e.surname, d.name, p.id, p.name, c.name, sum(wu.duration), t.amount, t.currency, t.type) from WorkUnit wu join wu.workAgreement wa join wa.employee e join e.department d join wa.project p join p.client c join wa.tariff t where wu.date>=?1 and wu.date<?2 group by wa.id, e.id, d.id, p.id, c.id, t.id order by e.name asc")
    List<WorkInfo> getAllIncomeReportsBetween(LocalDate from, LocalDate to);

    @Query("select new isr.naya.admiralproj.dto.WorkInfo(e.id, e.name, e.surname, d.name, p.id, p.name, c.name, sum(wu.duration), t.amount, t.currency, t.type) from WorkUnit wu join wu.workAgreement wa join wa.employee e join e.department d join wa.project p join p.client c join wa.tariff t where e.id=?3 and wu.date>=?1 and wu.date<?2 group by wa.id, e.id, d.id, p.id, c.id, t.id order by e.name asc")
    List<WorkInfo> getAllIncomeReportsBetweenAndEmployeeId(LocalDate from, LocalDate to, Integer employeeId);

    @Query("select new isr.naya.admiralproj.dto.WorkInfo(e.id, e.name, e.surname, d.name, p.id, p.name, c.name, sum(wu.duration), t.amount, t.currency, t.type) from WorkUnit wu join wu.workAgreement wa join wa.employee e join e.department d join wa.project p join p.client c join wa.tariff t where d.id=?3 and wu.date>=?1 and wu.date<?2 group by wa.id, e.id, d.id, p.id, c.id, t.id order by e.name asc")
    List<WorkInfo> getAllIncomeReportsBetweenAndDepartmentId(LocalDate from, LocalDate to, Integer departmentId);

    @Query("select new isr.naya.admiralproj.dto.WorkInfo(e.id, e.name, e.surname, d.name, p.id, p.name, c.name, sum(wu.duration), t.amount, t.currency, t.type) from WorkUnit wu join wu.workAgreement wa join wa.employee e join e.department d join wa.project p join p.client c join wa.tariff t where p.id=?3 and wu.date>=?1 and wu.date<?2 group by wa.id, e.id, d.id, p.id, c.id, t.id order by e.name asc")
    List<WorkInfo> getAllIncomeReportsBetweenAndProjectId(LocalDate from, LocalDate to, Integer projectId);

    @Query("select new isr.naya.admiralproj.dto.WorkInfo(e.id, e.name, e.surname, d.name, p.id, p.name, c.name, sum(wu.duration), t.amount, t.currency, t.type) from WorkUnit wu join wu.workAgreement wa join wa.employee e join e.department d join wa.project p join p.client c join wa.tariff t where c.id=?3 and wu.date>=?1 and wu.date<?2 group by wa.id, e.id, d.id, p.id, c.id, t.id order by e.name asc")
    List<WorkInfo> getAllIncomeReportsBetweenAndClientId(LocalDate from, LocalDate to, Integer clientId);

    @Query("select new isr.naya.admiralproj.dto.WorkInfo(e.id, e.name, e.surname, d.name, p.id, p.name, c.name, sum(wu.duration), t.amount, t.currency, t.type) from WorkUnit wu join wu.workAgreement wa join wa.employee e join e.department d join wa.project p join p.client c join wa.tariff t where e.id=?3 and c.id=?4 and wu.date>=?1 and wu.date<?2 group by wa.id, e.id, d.id, p.id, c.id, t.id order by e.name asc")
    List<WorkInfo> getAllIncomeReportsBetweenAndEmployeeIdAndClientId(LocalDate from, LocalDate to, Integer employeeId, Integer clientId);

    @Query("select new isr.naya.admiralproj.dto.WorkInfo(e.id, e.name, e.surname, d.name, p.id, p.name, c.name, sum(wu.duration), t.amount, t.currency, t.type) from WorkUnit wu join wu.workAgreement wa join wa.employee e join e.department d join wa.project p join p.client c join wa.tariff t where d.id=?3 and p.id=?4 and wu.date>=?1 and wu.date<?2 group by wa.id, e.id, d.id, p.id, c.id, t.id order by e.name asc")
    List<WorkInfo> getAllIncomeReportsBetweenAndDepartmentIdAndProjectId(LocalDate from, LocalDate to, Integer departmentId, Integer projectId);

    @Query("select new isr.naya.admiralproj.dto.WorkInfo(e.id, e.name, e.surname, d.name, p.id, p.name, c.name, sum(wu.duration), t.amount, t.currency, t.type) from WorkUnit wu join wu.workAgreement wa join wa.employee e join e.department d join wa.project p join p.client c join wa.tariff t where d.id=?3 and c.id=?4 and wu.date>=?1 and wu.date<?2 group by wa.id, e.id, d.id, p.id, c.id, t.id order by e.name asc")
    List<WorkInfo> getAllIncomeReportsBetweenAndDepartmentIdAndClientId(LocalDate from, LocalDate to, Integer departmentId, Integer clientId);

    @Query("select new isr.naya.admiralproj.dto.WorkInfo(e.id, e.name, e.surname, d.name, p.id, p.name, c.name, sum(wu.duration), t.amount, t.currency, t.type) from WorkUnit wu join wu.workAgreement wa join wa.employee e join e.department d join wa.project p join p.client c join wa.tariff t where d.id=?3 and c.id=?4 and wu.date>=?1 and wu.date<?2 group by wa.id, e.id, d.id, p.id, c.id, t.id order by e.name asc")
    List<WorkInfo> getAllIncomeReportsBetweenAndEmployeeIdAndProjectId(LocalDate from, LocalDate to, Integer employeeId, Integer projectId);
}
