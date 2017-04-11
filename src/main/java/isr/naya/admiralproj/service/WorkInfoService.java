package isr.naya.admiralproj.service;

import isr.naya.admiralproj.dto.WorkInfo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WorkInfoService {

    List<WorkInfo> getAllForEmployee(Integer id, LocalDate from, LocalDate to);

    List<WorkInfo> getAllForEmployeeByDate(Integer employeeId, Integer workAgreementId, LocalDate date);

    // Delegating Block
    List<WorkInfo> getPartialWorkInfos(LocalDate from, LocalDate to, Integer maxHours, Optional<Integer> employeeId, Optional<Integer> departmentId);

    List<WorkInfo> getMissingWorkInfos(LocalDate from, LocalDate to, Optional<Integer> employeeId, Optional<Integer> departmentId);

    List<WorkInfo> getWorkInfos(LocalDate from, LocalDate to, Optional<Integer> employeeId, Optional<Integer> departmentId, Optional<Integer> projectId, Optional<Integer> clientId);
}
