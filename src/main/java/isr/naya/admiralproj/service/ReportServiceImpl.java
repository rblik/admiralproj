package isr.naya.admiralproj.service;


import isr.naya.admiralproj.dto.MissingDay;
import isr.naya.admiralproj.dto.WorkInfo;
import isr.naya.admiralproj.model.Employee;
import isr.naya.admiralproj.model.WorkUnit;
import isr.naya.admiralproj.repo.EmployeeRepository;
import isr.naya.admiralproj.repo.WorkUnitRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static isr.naya.admiralproj.util.MappingUtil.generate;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    private WorkUnitRepository workUnitRepository;
    private EmployeeRepository employeeRepository;

    @Override
    public Set<WorkInfo> getPartialDays(@NonNull LocalDate from, @NonNull LocalDate to, @NonNull Integer maxHours) {
        return workUnitRepository.getAllPartialBetweenDates(from, to, maxHours);
    }

    @Override
    public List<WorkInfo> getAllWorkUnitsForEmployee(@NonNull Integer employeeId, @NonNull LocalDate from, @NonNull LocalDate to) {
        return workUnitRepository.getAllWithAgreements(employeeId, from, to);
    }

    @Override
    public Set<MissingDay> getMissingDays(@NonNull LocalDate from, @NonNull LocalDate to) {
        List<Employee> employees = employeeRepository.getAllWithDepartments();
        return generate(workUnitRepository.getAllNonEmptyDays(from, to), from, to, employees);
    }

    // Pivotal Report Block
    @Override
    public Set<WorkUnit> getAllUnitsByDate(@NonNull LocalDate from, @NonNull LocalDate to) {
        return workUnitRepository.getAllByDateBetween(from, to);
    }

    @Override
    public Set<WorkUnit> getAllUnitsByDateAndEmployee(@NonNull LocalDate from, @NonNull LocalDate to, @NonNull Integer employeeId) {
        return workUnitRepository.getAllByDateBetweenAndEmployeeId(from, to, employeeId);
    }

    @Override
    public Set<WorkUnit> getAllUnitsByDateAndProject(@NonNull LocalDate from, @NonNull LocalDate to, @NonNull Integer projectId) {
        return workUnitRepository.getAllByDateBetweenAndProjectId(from, to, projectId);
    }

    @Override
    public Set<WorkUnit> getAllUnitsByDateAndEmployeeAndProject(@NonNull LocalDate from, @NonNull LocalDate to, @NonNull Integer employeeId, @NonNull Integer projectId) {
        return workUnitRepository.getAllByDateBetweenAndEmployeeIdAndProjectId(from, to, employeeId, projectId);
    }
}
