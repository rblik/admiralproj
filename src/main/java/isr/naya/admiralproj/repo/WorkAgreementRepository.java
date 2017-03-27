package isr.naya.admiralproj.repo;

import isr.naya.admiralproj.model.WorkAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;


public interface WorkAgreementRepository extends JpaRepository<WorkAgreement, Integer> {

    @Query("select distinct wa from WorkAgreement wa join fetch wa.project p join fetch p.client join fetch wa.workUnits wu where wa.employee.id = ?1 and wu.date>=?2 and wu.date<?3 and wa.active = true")
    List<WorkAgreement> findByEmployeeIdWithWorkUnitsBetween(Integer employeeId, LocalDate from, LocalDate to);

    @Query("select distinct wa from WorkAgreement wa join fetch wa.project p  join fetch p.client where wa.employee.id = ?1 and wa.active = true")
    List<WorkAgreement> findByEmployeeIdAndTimeRange(Integer id);

    WorkAgreement findFirstByIdAndEmployeeIdAndActiveIsTrue(Integer id, Integer employeeId);

    @Query("select distinct wa from WorkAgreement wa join fetch wa.employee join fetch wa.project p join fetch p.client join fetch wa.workUnits wu where wu.date>=?1 and wu.date<?2")
    List<WorkAgreement> findAllWithEmployeesAndWorkUnitsBetween(LocalDate from, LocalDate to);

    @Query("select wa from WorkAgreement wa join fetch wa.employee")
    List<WorkAgreement> findAllWithEmployees();
}
