package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.WorkAgreement;
import isr.naya.admiralproj.model.WorkUnit;
import isr.naya.admiralproj.repo.WorkAgreementRepository;
import isr.naya.admiralproj.repo.WorkUnitRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static isr.naya.admiralproj.exception.ValidationUtil.checkNotFound;
import static isr.naya.admiralproj.exception.ValidationUtil.checkTimeOverlap;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class WorkUnitServiceImpl implements WorkUnitService {

    private WorkAgreementRepository workAgreementRepository;
    private WorkUnitRepository workUnitRepository;

    @Override
    @Transactional
    public WorkUnit saveUnit(@NonNull Integer employeeId, @NonNull Integer workAgreementId, @NonNull WorkUnit workUnit) {
        workUnit.setWorkAgreement(checkNotFound(workAgreementRepository.findFirstByIdAndEmployeeId(workAgreementId, employeeId), workAgreementId, WorkAgreement.class));
        return checkTimeOverlap(workUnitRepository.countExisted(employeeId, workAgreementId, workUnit.getDate(), workUnit.getStart(), workUnit.getFinish())) ?
                workUnitRepository.save(workUnit) : null;
    }
}
