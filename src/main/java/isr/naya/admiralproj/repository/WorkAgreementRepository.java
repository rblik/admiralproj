package isr.naya.admiralproj.repository;

import isr.naya.admiralproj.dto.AgreementDto;
import isr.naya.admiralproj.model.WorkAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


@SuppressWarnings("SpringDataRepositoryMethodReturnTypeInspection")
public interface WorkAgreementRepository extends JpaRepository<WorkAgreement, Integer> {

    WorkAgreement findFirstByIdAndEmployeeId(Integer id, Integer employeeId);

    @Query("select new isr.naya.admiralproj.dto.AgreementDto(wa.id, wa.active, e.id, e.name, e.surname, d.id, d.name, p.id, p.name, c.id, c.name) from WorkAgreement wa join wa.employee e join wa.project p join p.client c join e.department d")
    List<AgreementDto> getAllWithEmployeesAndDepartmentsAndProjectsAndClients();

    @Query("select new isr.naya.admiralproj.dto.AgreementDto(wa.id, wa.active, wa.tariff.id, wa.tariff.amount, wa.tariff.currency, wa.tariff.type, p.id, p.name, c.id, c.name) from WorkAgreement wa join wa.project p join p.client c join wa.employee e where e.id=?1")
    List<AgreementDto> getAllActiveWithProjectsAndClientsByEmployeeId(Integer id);
}
