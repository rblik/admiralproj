package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.Employee;
import isr.naya.admiralproj.model.MonthInfo;
import isr.naya.admiralproj.repository.EmployeeRepository;
import isr.naya.admiralproj.repository.MonthInfoRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static isr.naya.admiralproj.util.ValidationUtil.checkNotFound;

/**
 * Created by jonathan on 14/07/17.
 */
@Service
@AllArgsConstructor
public class MonthInfoServiceImpl implements MonthInfoService {

    private MonthInfoRepository repository;
    private EmployeeRepository employeeRepository;

    @Override
    public MonthInfo getOrNew(@NonNull Integer employeeId, @NonNull Integer year, @NonNull Integer month) {
        MonthInfo monthInfo = get(employeeId, year, month);
        return monthInfo != null ? monthInfo : new MonthInfo(year, month, false,0, null);
    }

    @Override
    public MonthInfo get(@NonNull Integer employeeId, @NonNull Integer year, @NonNull Integer month) {
        List<MonthInfo> infos = repository.getByEmployeeIdAndYearAndMonth(employeeId, year, month);
        return infos.size() == 0 ? null : infos.get(0);
    }

    @Override
    public boolean isLockExists(@NonNull Integer employeeId, @NonNull Integer year, @NonNull Integer month) {
        MonthInfo info = get(employeeId, year, month);
        return info != null && info.isLocked();
    }

    @Override
    @Transactional
    public MonthInfo save(@NonNull MonthInfo info, @NonNull Integer employeeId) {
        MonthInfo monthInfo = get(employeeId, info.getYear(), info.getMonth());
        if (monthInfo == null) {
            info.setEmployee(checkNotFound(employeeRepository.findOne(employeeId), employeeId, Employee.class));
            return repository.save(info);
        } else {
            monthInfo.setLocked(info.isLocked());
            monthInfo.setHoursSum(info.getHoursSum());
            return repository.save(monthInfo);
        }
    }

    private MonthInfo updateHoursForEmployee(@NonNull MonthInfo info, @NonNull Integer employeeId) {
        MonthInfo existedInfo = get(employeeId, info.getYear(), info.getMonth());
        if (existedInfo == null) {
            MonthInfo newInfo = new MonthInfo(info);
            newInfo.setEmployee(checkNotFound(employeeRepository.findOne(employeeId), employeeId, Employee.class));
            return repository.saveAndFlush(newInfo);
        } else {
            existedInfo.setHoursSum(info.getHoursSum());
            return repository.save(existedInfo);
        }
    }

    @Override
    public Iterable<MonthInfo> updateHoursSumForAllEmployees(MonthInfo info, Iterable<Integer> employeeIds) {
        List<MonthInfo> monthInfos = new ArrayList<>();
        employeeIds.forEach(employeeId -> {
            MonthInfo monthInfo = updateHoursForEmployee(info, employeeId);
            monthInfos.add(monthInfo);
        });
        return monthInfos;
    }

    @Override
    @Transactional
    public void removeLock(Integer employeeId, Integer year, Integer month) {
        checkNotFound(repository.deleteLock(employeeId, year, month), year, month, employeeId, MonthInfo.class);
    }
}
