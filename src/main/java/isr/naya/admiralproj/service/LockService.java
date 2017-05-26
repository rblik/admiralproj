package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.DateLock;

import java.time.LocalDate;
import java.util.Set;

public interface LockService {
    Set<DateLock> getAllLocks(Integer employeeId, LocalDate from, LocalDate to);

    DateLock saveLock(DateLock lock, Integer employeeId);

    void removeLock(Integer employeeId, Integer year, Integer month);
}
