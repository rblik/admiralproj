package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.DateLock;

public interface LockService {
    DateLock getLock(Integer employeeId, Integer year, Integer month);

    boolean isLockExists(Integer employeeId, Integer year, Integer month);

    DateLock saveLock(Integer employeeId, Integer year, Integer month);

    void removeLock(Integer employeeId, Integer year, Integer month);
}
