package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.WorkAgreement;
import isr.naya.admiralproj.model.WorkUnit;
import isr.naya.admiralproj.repository.WorkAgreementRepository;
import isr.naya.admiralproj.repository.WorkUnitRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static isr.naya.admiralproj.util.ValidationUtil.checkNotFound;
import static isr.naya.admiralproj.util.ValidationUtil.checkTimeOverlap;

@Service
@AllArgsConstructor
public class WorkUnitServiceImpl implements WorkUnitService {

    private WorkAgreementRepository workAgreementRepository;
    private WorkUnitRepository workUnitRepository;

    @Override
    @Transactional
    public WorkUnit save(@NonNull Integer employeeId, @NonNull Integer workAgreementId, @NonNull WorkUnit workUnit) {
        workUnit.setWorkAgreement(checkNotFound(workAgreementRepository.findFirstByIdAndEmployeeId(workAgreementId, employeeId), workAgreementId, WorkAgreement.class));
        return checkTimeOverlap(workUnitRepository.countExistedByDateTimeRange(employeeId, workAgreementId, workUnit.isNew() ? -1 : workUnit.getId(), workUnit.getDate(), workUnit.getStart(), workUnit.getFinish())) ?
                workUnitRepository.save(workUnit) : null;
    }

    @Override
    @Transactional
    public void delete(Integer employeeId, Integer workUnitId) {
        checkNotFound(workUnitRepository.delete(employeeId, workUnitId), workUnitId, employeeId, WorkUnit.class);
    }
}
