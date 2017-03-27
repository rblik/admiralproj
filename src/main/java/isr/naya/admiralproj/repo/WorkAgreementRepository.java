package isr.naya.admiralproj.repo;

import isr.naya.admiralproj.dto.WorkAgreementWithCount;
import isr.naya.admiralproj.model.WorkAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;


public interface WorkAgreementRepository extends JpaRepository<WorkAgreement, Integer> {

    @Query("select distinct wa from WorkAgreement wa join fetch wa.project p join fetch p.client join fetch wa.workUnits wu where wa.employee.id = ?1 and wu.date>=?2 and wu.date<?3 and wa.active = true")
    Set<WorkAgreement> findByEmployeeIdWithWorkUnitsBetween(Integer employeeId, LocalDate from, LocalDate to);

    @Query("select distinct wa from WorkAgreement wa join fetch wa.project p  join fetch p.client where wa.employee.id = ?1 and wa.active = true")
    Set<WorkAgreement> findByEmployeeIdAndTimeRange(Integer id);

    WorkAgreement findFirstByIdAndEmployeeIdAndActiveIsTrue(Integer id, Integer employeeId);

    @Query("select wa from WorkAgreement wa join fetch wa.employee join fetch wa.project p join fetch p.client")
    Set<WorkAgreement> findAllWithEmployees();

    @Query("select new isr.naya.admiralproj.dto.WorkAgreementWithCount(wa.id, p.id, p.name, e.id, e.name, e.surname, c.id, c.name, sum (wu.duration)) from WorkAgreement wa join wa.employee e join wa.project p join p.client c join wa.workUnits wu where wa.employee.id = e.id and wa.project.id = p.id and p.client.id = c.id and wu.date>=?1 and wu.date<?2 and wu.absenceType is null group by e.id, wa.id, c.id, p.id")
    Set<WorkAgreementWithCount> getWithTimeSumTime(LocalDate from, LocalDate to);
}
