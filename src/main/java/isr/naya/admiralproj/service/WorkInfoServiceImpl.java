package isr.naya.admiralproj.service;

import isr.naya.admiralproj.dto.WorkInfo;
import isr.naya.admiralproj.model.Employee;
import isr.naya.admiralproj.repository.WorkUnitRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static isr.naya.admiralproj.util.MappingUtil.generate;
import static java.util.Collections.singletonList;

@Service
@AllArgsConstructor
public class WorkInfoServiceImpl implements WorkInfoService {

    private WorkUnitRepository workUnitRepository;
    private EmployeeService employeeService;

    @Override
    public List<WorkInfo> getAllForEmployee(@NonNull Integer employeeId, @NonNull LocalDate from, @NonNull LocalDate to) {
        return workUnitRepository.getAllForEmployeeBetweenDates(employeeId, from, to);
    }

    @Override
    public List<WorkInfo> getAllForEmployeeByDate(@NonNull Integer employeeId, @NonNull Integer workAgreementId, @NonNull LocalDate date) {
        return workUnitRepository.getAllForEmployeeByDay(employeeId, workAgreementId, date);
    }

    // Partial Report Block
    @Override
    public List<WorkInfo> getPartialDays(@NonNull LocalDate from, @NonNull LocalDate to, @NonNull Integer maxHours) {
        return workUnitRepository.getAllPartialBetweenDates(from, to, maxHours);
    }

    @Override
    public List<WorkInfo> getPartialDaysByEmployee(@NonNull LocalDate from, @NonNull LocalDate to, @NonNull Integer maxHours, Integer employeeId) {
        return workUnitRepository.getAllPartialBetweenDatesByEmployeeId(from, to, maxHours, employeeId);
    }

    @Override
    public List<WorkInfo> getPartialDaysByDepartment(@NonNull LocalDate from, @NonNull LocalDate to, @NonNull Integer maxHours, Integer departmentId) {
        return workUnitRepository.getAllPartialBetweenDatesByDepartmentId(from, to, maxHours, departmentId);
    }

    // Missing Report Block
    @Override
    public List<WorkInfo> getMissingDays(@NonNull LocalDate from, @NonNull LocalDate to) {
        List<Employee> employees = employeeService.getAllWithDepartments();
        return generate(workUnitRepository.getAllNonEmptyDaysBetweenDates(from, to), from, to, employees);
    }

    @Override
    public List<WorkInfo> getMissingDaysByEmployee(@NonNull LocalDate from, @NonNull LocalDate to, @NonNull Integer employeeId) {
        Employee employee = employeeService.getWithDepartment(employeeId);
        return generate(workUnitRepository.getAllNonEmptyDaysByDateBetweenAndEmployeeId(from, to, employeeId), from, to, singletonList(employee));
    }

    @Override
    public List<WorkInfo> getMissingDaysByDepartment(@NonNull LocalDate from, @NonNull LocalDate to, @NonNull Integer departmentId) {
        List<Employee> employees = employeeService.getAllByDepartment(departmentId);
        return generate(workUnitRepository.getAllNonEmptyDaysByDateBetweenAndEmployeeId(from, to, departmentId), from, to, employees);
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

    @Override
    public List<WorkInfo> getPartialWorkInfos(LocalDate from, LocalDate to,
                                              Integer maxHours,
                                              Optional<Integer> employeeId,
                                              Optional<Integer> departmentId) {

        List<WorkInfo> workInfos;
        if (employeeId.isPresent()) {
            workInfos = getPartialDaysByEmployee(from, to, maxHours, employeeId.get());
        } else if (departmentId.isPresent()) {
            workInfos = getPartialDaysByDepartment(from, to, maxHours, departmentId.get());
        } else {
            workInfos = getPartialDays(from, to, maxHours);
        }
        return workInfos;
    }

    @Override
    public List<WorkInfo> getMissingWorkInfos(LocalDate from, LocalDate to,
                                              Optional<Integer> employeeId,
                                              Optional<Integer> departmentId) {
        List<WorkInfo> workInfos;
        if (employeeId.isPresent()) {
            workInfos = getMissingDaysByEmployee(from, to, employeeId.get());
        } else if (departmentId.isPresent()) {
            workInfos = getMissingDaysByDepartment(from, to, departmentId.get());
        } else {
            workInfos = getMissingDays(from, to);
        }
        return workInfos;
    }

    public List<WorkInfo> getWorkInfos(LocalDate from, LocalDate to,
                                       Optional<Integer> employeeId,
                                       Optional<Integer> projectId) {
        List<WorkInfo> workInfos;
        if (employeeId.isPresent() && projectId.isPresent()) {
            workInfos = getAllUnitsByDateAndEmployeeAndProject(from, to, employeeId.get(), projectId.get());
        } else if (employeeId.isPresent())
            workInfos = getAllUnitsByDateAndEmployee(from, to, employeeId.get());
        else if (projectId.isPresent())
            workInfos = getAllUnitsByDateAndProject(from, to, projectId.get());
        else
            workInfos = getAllUnitsByDate(from, to);
        return workInfos;
    }
}
