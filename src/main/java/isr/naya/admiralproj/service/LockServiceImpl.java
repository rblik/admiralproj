package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.DateLock;
import isr.naya.admiralproj.model.Employee;
import isr.naya.admiralproj.repository.EmployeeRepository;
import isr.naya.admiralproj.repository.LockRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;

import static isr.naya.admiralproj.util.ValidationUtil.checkNotFound;

@Service
@AllArgsConstructor
public class LockServiceImpl implements LockService {

    private LockRepository lockRepository;
    private EmployeeRepository employeeRepository;

    @Override
    public Set<DateLock> getAllLocks(@NonNull Integer employeeId, @NonNull LocalDate from, @NonNull LocalDate to) {
        return lockRepository.getAllLocksByEmployeeIdAndDateRange(employeeId, from.getYear(), from.getMonth().getValue(), to.getYear(), to.getMonth().getValue());
    }

    @Override
    @Transactional
    public DateLock saveLock(@NonNull DateLock lock, @NonNull Integer employeeId) {
        lock.setEmployee(checkNotFound(employeeRepository.findOne(employeeId), employeeId, Employee.class));
        return lockRepository.save(lock);
    }

    @Override
    @Transactional
    public void removeLock(Integer employeeId, Integer year, Integer month) {
        checkNotFound(lockRepository.delete(employeeId, year, month), year, month, employeeId, DateLock.class);
    }
}
