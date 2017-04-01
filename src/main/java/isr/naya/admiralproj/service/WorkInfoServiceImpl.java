package isr.naya.admiralproj.service;

import isr.naya.admiralproj.dto.WorkInfo;
import isr.naya.admiralproj.model.Employee;
import isr.naya.admiralproj.repo.EmployeeRepository;
import isr.naya.admiralproj.repo.WorkUnitRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static isr.naya.admiralproj.util.MappingUtil.generate;

@Service
@AllArgsConstructor
public class WorkInfoServiceImpl implements WorkInfoService {

    private WorkUnitRepository workUnitRepository;
    private EmployeeRepository employeeRepository;

    @Override
    public List<WorkInfo> getPartialDays(@NonNull LocalDate from, @NonNull LocalDate to, @NonNull Integer maxHours) {
        return workUnitRepository.getAllPartialBetweenDates(from, to, maxHours);
    }

    @Override
    public List<WorkInfo> getAllForEmployee(@NonNull Integer employeeId, @NonNull LocalDate from, @NonNull LocalDate to) {
        return workUnitRepository.getAllForEmployeeBetweenDates(employeeId, from, to);
    }

    @Override
    public List<WorkInfo> getAllForEmployeeByDate(@NonNull Integer employeeId, @NonNull Integer workAgreementId, @NonNull LocalDate date) {
        return workUnitRepository.getAllForEmployeeByDay(employeeId, workAgreementId, date);
    }

    @Override
    public List<WorkInfo> getMissingDays(@NonNull LocalDate from, @NonNull LocalDate to) {
        List<Employee> employees = employeeRepository.getAllWithDepartments();
        return generate(workUnitRepository.getAllNonEmptyDaysBetweenDates(from, to), from, to, employees);
    }

    // Pivotal Report Block
    @Override
    public List<WorkInfo> getAllUnitsByDate(@NonNull LocalDate from, @NonNull LocalDate to) {
        return workUnitRepository.getAllByDateBetween(from, to);
    }

    @Override
    public List<WorkInfo> getAllUnitsByDateAndEmployee(@NonNull LocalDate from, @NonNull LocalDate to, @NonNull Integer employeeId) {
        return workUnitRepository.getAllByDateBetweenAndEmployeeId(from, to, employeeId);
    }

    @Override
    public List<WorkInfo> getAllUnitsByDateAndProject(@NonNull LocalDate from, @NonNull LocalDate to, @NonNull Integer projectId) {
        return workUnitRepository.getAllByDateBetweenAndProjectId(from, to, projectId);
    }

    @Override
    public List<WorkInfo> getAllUnitsByDateAndEmployeeAndProject(@NonNull LocalDate from, @NonNull LocalDate to, @NonNull Integer employeeId, @NonNull Integer projectId) {
        return workUnitRepository.getAllByDateBetweenAndEmployeeIdAndProjectId(from, to, employeeId, projectId);
    }
}
