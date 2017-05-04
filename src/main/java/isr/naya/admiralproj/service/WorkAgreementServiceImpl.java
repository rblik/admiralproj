package isr.naya.admiralproj.service;

import isr.naya.admiralproj.dto.AgreementDto;
import isr.naya.admiralproj.model.Employee;
import isr.naya.admiralproj.model.Project;
import isr.naya.admiralproj.model.Tariff;
import isr.naya.admiralproj.model.WorkAgreement;
import isr.naya.admiralproj.repository.EmployeeRepository;
import isr.naya.admiralproj.repository.ProjectRepository;
import isr.naya.admiralproj.repository.TariffRepository;
import isr.naya.admiralproj.repository.WorkAgreementRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static isr.naya.admiralproj.util.ValidationUtil.checkNotFound;

@Service
@AllArgsConstructor
public class WorkAgreementServiceImpl implements WorkAgreementService {

    private WorkAgreementRepository workAgreementRepository;
    private EmployeeRepository employeeRepository;
    private ProjectRepository projectRepository;
    private TariffRepository tariffRepository;

    @Override
    public List<AgreementDto> getAllForEmployee(@NonNull Integer employeeId) {
        return workAgreementRepository.getAllActiveWithProjectsAndClientsByEmployeeId(employeeId);
    }

    @Override
    @Transactional
    public WorkAgreement save(@NonNull Integer employeeId, @NonNull Integer projectId, @NonNull WorkAgreement workAgreement) {
        Tariff tariffSaved = tariffRepository.save(workAgreement.getTariff());
        workAgreement.setTariff(tariffSaved);
        workAgreement.setEmployee(checkNotFound(employeeRepository.findOne(employeeId), employeeId, Employee.class));
        workAgreement.setProject(checkNotFound(projectRepository.findOne(projectId), projectId, Project.class));
        return workAgreementRepository.save(workAgreement);
    }

    @Override
    public List<AgreementDto> getAgreementsGraph() {
        return workAgreementRepository.getAllWithEmployeesAndDepartmentsAndProjectsAndClients();
    }
}