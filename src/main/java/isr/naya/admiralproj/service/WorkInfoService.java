package isr.naya.admiralproj.service;

import isr.naya.admiralproj.dto.WorkInfo;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface WorkInfoService {

    List<WorkInfo> getPartialDays(LocalDate from, LocalDate to, Integer maxHours);

    List<WorkInfo> getAllWorkUnitsForEmployee(Integer id, LocalDate from, LocalDate to);

    Set<WorkInfo> getMissingDays(LocalDate from, LocalDate to);

    // Pivotal Report Block
    List<WorkInfo> getAllUnitsByDate(LocalDate from, LocalDate to);

    List<WorkInfo> getAllUnitsByDateAndEmployee(LocalDate from, LocalDate to, Integer employeeId);

    List<WorkInfo> getAllUnitsByDateAndProject(LocalDate from, LocalDate to, Integer projectId);

    List<WorkInfo> getAllUnitsByDateAndEmployeeAndProject(LocalDate from, LocalDate to, Integer employeeId, Integer projectId);
}
