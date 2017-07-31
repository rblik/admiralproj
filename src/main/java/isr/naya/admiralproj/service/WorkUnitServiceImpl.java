package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.WorkAgreement;
import isr.naya.admiralproj.model.WorkUnit;
import isr.naya.admiralproj.repository.LockRepository;
import isr.naya.admiralproj.repository.WorkAgreementRepository;
import isr.naya.admiralproj.repository.WorkUnitRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;

import static isr.naya.admiralproj.util.ValidationUtil.*;

@Service
@AllArgsConstructor
public class WorkUnitServiceImpl implements WorkUnitService {

    private WorkAgreementRepository workAgreementRepository;
    private WorkUnitRepository workUnitRepository;
    private LockRepository lockRepository;

    @Override
    @Transactional
    public WorkUnit save(@NonNull Integer employeeId, @NonNull Integer workAgreementId, @NonNull WorkUnit workUnit) {
        checkTimeRange(workUnit).setWorkAgreement(checkNotFound(workAgreementRepository.findFirstByIdAndEmployeeId(workAgreementId, employeeId), workAgreementId, WorkAgreement.class));
        return checkTimeOverlap(workUnitRepository.countExistedByDateTimeRange(employeeId, workAgreementId, workUnit.isNew() ? -1 : workUnit.getId(), workUnit.getDate(), workUnit.getStart(), workUnit.getFinish())) ?
                checkLock(workUnitRepository.save(workUnit), lockRepository.getLockByEmployeeIdAndYearAndMonth(employeeId, YearMonth.of(workUnit.getDate().getYear(), workUnit.getDate().getMonthValue()))) : null;
    }

    @Override
    @Transactional
    public void delete(Integer employeeId, Integer workUnitId) {
        checkNotFound(workUnitRepository.delete(employeeId, workUnitId), workUnitId, employeeId, WorkUnit.class);
    }
}
