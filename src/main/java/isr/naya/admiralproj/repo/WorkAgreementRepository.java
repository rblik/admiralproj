package isr.naya.admiralproj.repo;

import isr.naya.admiralproj.model.WorkAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;


public interface WorkAgreementRepository extends JpaRepository<WorkAgreement, Integer> {

    @Query("select distinct wa from WorkAgreement wa join fetch wa.project p join fetch p.client join fetch wa.workUnits wu where wa.employee.id = ?1 and wu.date>=?2 and wu.date<?3")
    List<WorkAgreement> findByEmployeeIdWithWorkUnitsBetween(Integer employeeId, LocalDate from, LocalDate to);

    @Query("select distinct wa from WorkAgreement wa join fetch wa.project p  join fetch p.client where wa.employee.id = ?1")
    List<WorkAgreement> findByEmployeeIdAndTimeRange(Integer id);
}
