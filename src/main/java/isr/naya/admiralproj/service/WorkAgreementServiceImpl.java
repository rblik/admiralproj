package isr.naya.admiralproj.service;

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
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;
import static isr.naya.admiralproj.exception.ValidationUtil.checkNotFound;
import static isr.naya.admiralproj.exception.ValidationUtil.checkTimeOverlap;
import static isr.naya.admiralproj.util.MappingUtil.intersectAgreements;
import static isr.naya.admiralproj.util.MappingUtil.intersectDays;

@Service
@AllArgsConstructor
public class WorkAgreementServiceImpl implements WorkAgreementService {

    private WorkAgreementRepository workAgreementRepository;
    private WorkUnitRepository workUnitRepository;
    private EmployeeRepository employeeRepository;
    private ProjectRepository projectRepository;

    @Override
    @Transactional(readOnly = true)
    public Set<WorkAgreement> getAllForEmployee(@NonNull Integer employeeId, @NonNull LocalDate from, @NonNull LocalDate to) {
        Set<WorkAgreement> agreements = workAgreementRepository.findByEmployeeIdAndTimeRange(employeeId);
        Set<WorkAgreement> agreementsWithUnits = workAgreementRepository.findByEmployeeIdWithWorkUnitsBetween(employeeId, from, to);
        return intersectAgreements(agreements, agreementsWithUnits);
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
    public Set<PartialDay> getPartialDays(@NonNull LocalDate from, @NonNull LocalDate to,@NonNull Integer maxHours) {
        Set<PartialDay> partialDays = workUnitRepository.getWithSumTime(from, to, maxHours);
        Set<PartialDay> partialDaysWithAbsence = workUnitRepository.getAbsenceWithSumTime(from, to);
        return intersectDays(partialDays, partialDaysWithAbsence);
    }
}
