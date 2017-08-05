package isr.naya.admiralproj.service;

import isr.naya.admiralproj.dto.AgreementDto;
import isr.naya.admiralproj.model.Employee;
import isr.naya.admiralproj.model.Project;
import isr.naya.admiralproj.model.Tariff;
import isr.naya.admiralproj.model.WorkAgreement;
import isr.naya.admiralproj.repository.*;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.cache.annotation.CacheEvict;
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
    private DefaultChoiceRepository defaultChoiceRepository;

    @Override
    public List<AgreementDto> getAllForEmployee(@NonNull Integer employeeId) {
        return workAgreementRepository.getAllActiveWithProjectsAndClientsByEmployeeId(employeeId);
    }

    @Override
    @CacheEvict(value = {"clients", "projects"}, allEntries = true)
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

    @Override
    public WorkAgreement get(@NonNull Integer agreementId) {
        return checkNotFound(workAgreementRepository.findOne(agreementId), agreementId, WorkAgreement.class);
    }

    @Override
    public void remove(@NonNull Integer agreementId) {
        defaultChoiceRepository.cleanDefaultChoicesByAgreementId(agreementId);
        workAgreementRepository.delete(agreementId);
    }
}