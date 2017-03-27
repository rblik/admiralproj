package isr.naya.admiralproj.service;

import isr.naya.admiralproj.dto.WorkAgreementWithCount;
import isr.naya.admiralproj.model.Employee;
import isr.naya.admiralproj.model.Project;
import isr.naya.admiralproj.model.WorkAgreement;
import isr.naya.admiralproj.model.WorkUnit;
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
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;
import static isr.naya.admiralproj.exception.ValidationUtil.checkNotFound;
import static isr.naya.admiralproj.exception.ValidationUtil.checkTimeOverlap;

@Service
@AllArgsConstructor
public class WorkAgreementServiceImpl implements WorkAgreementService {

    private WorkAgreementRepository workAgreementRepository;
    private WorkUnitRepository workUnitRepository;
    private EmployeeRepository employeeRepository;
    private ProjectRepository projectRepository;

    @Override
    @Transactional(readOnly = true)
    public List<WorkAgreement> getAllForEmployee(@NonNull Integer employeeId, @NonNull LocalDate from, @NonNull LocalDate to) {
        List<WorkAgreement> agreements = workAgreementRepository.findByEmployeeIdAndTimeRange(employeeId);
        List<WorkAgreement> agreementsWithUnits = workAgreementRepository.findByEmployeeIdWithWorkUnitsBetween(employeeId, from, to);
        return intersect(agreements, agreementsWithUnits);
    }

    @Transactional
    @Override
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
    @Transactional(readOnly = true)
    public List<WorkAgreement> getAllWithTimeSum(LocalDate from, LocalDate to) {
        List<WorkAgreement> agreementsWithWork = workAgreementRepository.getMany(from, to).stream().map(WorkAgreementWithCount::getAgreement).collect(Collectors.toList());
        List<WorkAgreement> agreements = workAgreementRepository.findAllWithEmployees();
        return intersect(agreements, agreementsWithWork);
    }

    private List<WorkAgreement> intersect(List<WorkAgreement> agreements, List<WorkAgreement> agreementsWithUnits) {
        List<WorkAgreement> intersect = agreements.stream()
                .filter(agreement -> !agreementsWithUnits.contains(agreement))
                .map(this::populate)
                .collect(Collectors.toList());
        intersect.addAll(agreementsWithUnits);
        return intersect;
    }

    private WorkAgreement populate(WorkAgreement workAgreement) {
        workAgreement.setWorkUnits(newArrayList());
        return workAgreement;
    }
}
