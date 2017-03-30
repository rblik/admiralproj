package isr.naya.admiralproj.service;

import isr.naya.admiralproj.dto.WorkInfo;
import isr.naya.admiralproj.model.WorkUnit;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface ReportService {

    Set<WorkInfo> getPartialDays(LocalDate from, LocalDate to, Integer maxHours);

    List<WorkInfo> getAllWorkUnitsForEmployee(Integer id, LocalDate from, LocalDate to);

    Set<WorkInfo> getMissingDays(LocalDate from, LocalDate to);

    // Pivotal Report Block
    Set<WorkUnit> getAllUnitsByDate(LocalDate from, LocalDate to);

    Set<WorkUnit> getAllUnitsByDateAndEmployee(LocalDate from, LocalDate to, Integer employeeId);

    Set<WorkUnit> getAllUnitsByDateAndProject(LocalDate from, LocalDate to, Integer projectId);

    Set<WorkUnit> getAllUnitsByDateAndEmployeeAndProject(LocalDate from, LocalDate to, Integer employeeId, Integer projectId);
}
