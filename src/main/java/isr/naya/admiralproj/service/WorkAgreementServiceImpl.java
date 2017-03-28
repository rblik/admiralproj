package isr.naya.admiralproj.service;

import isr.naya.admiralproj.dto.MissingDay;
import isr.naya.admiralproj.dto.PartialDay;
import isr.naya.admiralproj.model.*;
import isr.naya.admiralproj.repo.EmployeeRepository;
import isr.naya.admiralproj.repo.ProjectRepository;
import isr.naya.admiralproj.repo.WorkAgreementRepository;
import isr.naya.admiralproj.repo.WorkUnitRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static isr.naya.admiralproj.exception.ValidationUtil.checkNotFound;
import static isr.naya.admiralproj.exception.ValidationUtil.checkTimeOverlap;
import static isr.naya.admiralproj.util.MappingUtil.generate;
import static isr.naya.admiralproj.util.MappingUtil.intersectAgreements;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class WorkAgreementServiceImpl implements WorkAgreementService {

    private WorkAgreementRepository workAgreementRepository;
    private WorkUnitRepository workUnitRepository;
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
    @Transactional
    public WorkUnit saveUnit(@NonNull Integer employeeId, @NonNull Integer workAgreementId, @NonNull WorkUnit workUnit) {
        workUnit.setWorkAgreement(checkNotFound(workAgreementRepository.findFirstByIdAndEmployeeIdAndActiveIsTrue(workAgreementId, employeeId), workAgreementId, WorkAgreement.class));
        return checkTimeOverlap(workUnitRepository.countExisted(employeeId, workAgreementId, workUnit.getDate(), workUnit.getStart(), workUnit.getFinish())) ?
                workUnitRepository.save(workUnit) : null;
    }

    @Override
    public Set<PartialDay> getPartialDays(@NonNull LocalDate from, @NonNull LocalDate to,@NonNull Integer maxHours) {
        return workUnitRepository.getAllPartialBetweenDates(from, to, maxHours);
    }

    @Override
    public Set<MissingDay> getMissingDays(@NonNull LocalDate from, @NonNull LocalDate to) {
        List<Employee> employees = employeeRepository.getAllWithDepartments();
        return generate(workUnitRepository.getAllNonEmptyDays(from, to), from, to, employees);
    }

    @Override
    public Set<WorkUnit> getAllUnitsByDate(@NonNull LocalDate from, @NonNull LocalDate to) {
        return workUnitRepository.getAllByDateBetween(from, to);
    }

    @Override
    public Set<WorkUnit> getAllUnitsByDateAndEmployee(@NonNull LocalDate from, @NonNull LocalDate to, @NonNull Integer employeeId) {
        return workUnitRepository.getAllByDateBetweenAndEmployeeId(from, to, employeeId);
    }

    @Override
    public Set<WorkUnit> getAllUnitsByDateAndProject(@NonNull LocalDate from, @NonNull LocalDate to, @NonNull Integer projectId) {
        return workUnitRepository.getAllByDateBetweenAndProjectId(from, to, projectId);
    }

    @Override
    public Set<WorkUnit> getAllUnitsByDateAndEmployeeAndProject(@NonNull LocalDate from, @NonNull LocalDate to, @NonNull Integer employeeId, @NonNull Integer projectId) {
        return workUnitRepository.getAllByDateBetweenAndEmployeeIdAndProjectId(from, to, employeeId, projectId);
    }

    @Override
    public Set<WorkAgreement> getAgreementsGraph() {
        return workAgreementRepository.getAllWithEmployeesAndDepartmentsAndProjectsAndClients();
    }
}