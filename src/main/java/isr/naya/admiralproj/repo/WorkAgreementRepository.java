package isr.naya.admiralproj.repo;

import isr.naya.admiralproj.dto.PartialDay;
import isr.naya.admiralproj.model.WorkAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Set;


public interface WorkAgreementRepository extends JpaRepository<WorkAgreement, Integer> {

    @Query("select distinct wa from WorkAgreement wa join fetch wa.project p join fetch p.client join fetch wa.workUnits wu where wa.employee.id = ?1 and wu.date>=?2 and wu.date<?3 and wa.active = true")
    Set<WorkAgreement> findByEmployeeIdWithWorkUnitsBetween(Integer employeeId, LocalDate from, LocalDate to);

    @Query("select distinct wa from WorkAgreement wa join fetch wa.project p  join fetch p.client where wa.employee.id = ?1 and wa.active = true")
    Set<WorkAgreement> findByEmployeeIdAndTimeRange(Integer id);

    WorkAgreement findFirstByIdAndEmployeeId(Integer id, Integer employeeId);

    @Query("select wa from WorkAgreement wa join fetch wa.employee e join fetch wa.project p join fetch p.client c join fetch e.department")
    Set<WorkAgreement> getAllWithEmployeesAndDepartmentsAndProjectsAndClients();
}
