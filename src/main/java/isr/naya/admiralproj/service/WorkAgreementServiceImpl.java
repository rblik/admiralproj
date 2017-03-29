package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.Employee;
import isr.naya.admiralproj.model.Project;
import isr.naya.admiralproj.model.WorkAgreement;
import isr.naya.admiralproj.repo.EmployeeRepository;
import isr.naya.admiralproj.repo.ProjectRepository;
import isr.naya.admiralproj.repo.WorkAgreementRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;

import static isr.naya.admiralproj.exception.ValidationUtil.checkNotFound;
import static isr.naya.admiralproj.util.MappingUtil.intersectAgreements;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class WorkAgreementServiceImpl implements WorkAgreementService {

    private WorkAgreementRepository workAgreementRepository;
    private EmployeeRepository employeeRepository;
    private ProjectRepository projectRepository;

    @Override
    public Set<WorkAgreement> getAllForEmployee(@NonNull Integer employeeId, @NonNull LocalDate from, @NonNull LocalDate to) {
        Set<WorkAgreement> agreements = workAgreementRepository.findByEmployeeIdAndTimeRange(employeeId);
        Set<WorkAgreement> agreementsWithUnits = workAgreementRepository.findByEmployeeIdWithWorkUnitsBetween(employeeId, from, to);
        return intersectAgreements(agreements, agreementsWithUnits);
    }

    @Override
    @Transactional
    public WorkAgreement save(@NonNull Integer employeeId, @NonNull Integer projectId, @NonNull WorkAgreement workAgreement) {
        workAgreement.setEmployee(checkNotFound(employeeRepository.findOne(employeeId), employeeId, Employee.class));
        workAgreement.setProject(checkNotFound(projectRepository.findOne(projectId), projectId, Project.class));
        return workAgreementRepository.save(workAgreement);
    }

    @Override
    public Set<WorkAgreement> getAgreementsGraph() {
        return workAgreementRepository.getAllWithEmployeesAndDepartmentsAndProjectsAndClients();
    }
}