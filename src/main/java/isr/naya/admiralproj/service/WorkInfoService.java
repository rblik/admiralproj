package isr.naya.admiralproj.service;

import isr.naya.admiralproj.dto.WorkInfo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WorkInfoService {

    List<WorkInfo> getAllForEmployee(Integer id, LocalDate from, LocalDate to);

    List<WorkInfo> getAllForEmployeeByDate(Integer employeeId, Integer workAgreementId, LocalDate date);

    // Partial Report Block
    List<WorkInfo> getPartialDays(LocalDate from, LocalDate to, Integer maxHours);

    List<WorkInfo> getPartialDaysByEmployee(LocalDate from, LocalDate to, Integer maxHours, Integer employeeId);

    List<WorkInfo> getPartialDaysByDepartment(LocalDate from, LocalDate to, Integer maxHours, Integer departmentId);

    // Missing Report Block
    List<WorkInfo> getMissingDays(LocalDate from, LocalDate to);

    List<WorkInfo> getMissingDaysByEmployee(LocalDate from, LocalDate to, Integer employeeId);

    List<WorkInfo> getMissingDaysByDepartment(LocalDate from, LocalDate to, Integer departmentId);

    // Pivotal Report Block
    List<WorkInfo> getAllUnitsByDate(LocalDate from, LocalDate to);

    List<WorkInfo> getAllUnitsByDateAndEmployee(LocalDate from, LocalDate to, Integer employeeId);

    List<WorkInfo> getAllUnitsByDateAndProject(LocalDate from, LocalDate to, Integer projectId);

    List<WorkInfo> getAllUnitsByDateAndEmployeeAndProject(LocalDate from, LocalDate to, Integer employeeId, Integer projectId);

    // Delegating Block
    List<WorkInfo> getPartialWorkInfos(LocalDate from, LocalDate to, Integer maxHours, Optional<Integer> employeeId, Optional<Integer> departmentId);

    List<WorkInfo> getMissingWorkInfos(LocalDate from, LocalDate to, Optional<Integer> employeeId, Optional<Integer> departmentId);

    List<WorkInfo> getWorkInfos(LocalDate from, LocalDate to, Optional<Integer> employeeId, Optional<Integer> projectId);
}
