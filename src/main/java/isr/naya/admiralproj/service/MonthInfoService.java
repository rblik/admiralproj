package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.MonthInfo;

/**
 * Created by jonathan on 14/07/17.
 */
public interface MonthInfoService {

    MonthInfo getOrNew(Integer employeeId, Integer year, Integer month);

    MonthInfo get(Integer employeeId, Integer year, Integer month);

    boolean isLockExists(Integer employeeId, Integer year, Integer month);

    MonthInfo save(MonthInfo info, Integer employeeId);

    Iterable<MonthInfo> updateHoursSumForAllEmployees(MonthInfo info, Iterable<Integer> employeeIds);

    void removeLock(Integer employeeId, Integer year, Integer month);
}
