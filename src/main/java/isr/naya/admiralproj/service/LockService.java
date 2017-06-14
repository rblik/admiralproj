package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.DateLock;

public interface LockService {
    DateLock getLock(Integer employeeId, Integer year, Integer month);

    boolean isLockExists(Integer employeeId, Integer year, Integer month);

    DateLock saveLock(DateLock lock, Integer employeeId);

    void removeLock(Integer employeeId, Integer year, Integer month);
}
