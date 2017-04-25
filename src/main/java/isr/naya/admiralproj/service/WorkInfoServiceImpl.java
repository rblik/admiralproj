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
import java.util.Set;

import static isr.naya.admiralproj.util.MappingUtil.generate;
import static java.util.Collections.emptyList;
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

    @Override
    public List<WorkInfo> getPartialWorkInfos(@NonNull LocalDate from, @NonNull LocalDate to,
                                              @NonNull Integer maxHours,
                                              @NonNull Optional<Integer> employeeId,
                                              @NonNull Optional<Integer> departmentId) {

        if (employeeId.isPresent()) {
            return workUnitRepository.getAllPartialBetweenDatesByEmployeeId(from, to, maxHours, employeeId.get());
        } else if (departmentId.isPresent()) {
            return workUnitRepository.getAllPartialBetweenDatesByDepartmentId(from, to, maxHours, departmentId.get());
        } else {
            return workUnitRepository.getAllPartialBetweenDates(from, to, maxHours);
        }
    }

    @Override
    public List<WorkInfo> getMissingWorkInfos(@NonNull LocalDate from, @NonNull LocalDate to,
                                              @NonNull Optional<Integer> employeeId,
                                              @NonNull Optional<Integer> departmentId) {
        if (employeeId.isPresent()) {
            Employee employee = employeeService.getWithDepartmentAndAgreements(employeeId.get(), from, to);
            return employee == null ? emptyList() : generate(workUnitRepository.getAllNonEmptyDaysByDateBetweenAndEmployeeId(from, to, employeeId.get()), from, to, singletonList(employee));
        } else if (departmentId.isPresent()) {
            List<Employee> employees = employeeService.getAllByDepartmentWithAgreements(departmentId.get(), from, to);
            Set<WorkInfo> workInfos = workUnitRepository.getAllNonEmptyDaysByDateBetweenAndDepartmentId(from, to, departmentId.get());
            return generate(workInfos, from, to, employees);
        } else {
            List<Employee> employees = employeeService.getAllWithDepartmentsAndAgreements(from, to);
            Set<WorkInfo> workInfos = workUnitRepository.getAllNonEmptyDaysBetweenDates(from, to);
            return generate(workInfos, from, to, employees);
        }
    }

    public List<WorkInfo> getWorkInfos(@NonNull LocalDate from, @NonNull LocalDate to,
                                       @NonNull Optional<Integer> employeeId,
                                       @NonNull Optional<Integer> departmentId,
                                       @NonNull Optional<Integer> projectId,
                                       @NonNull Optional<Integer> clientId) {
        if (employeeId.isPresent() && projectId.isPresent()) {
            return workUnitRepository.getAllByDateBetweenAndEmployeeIdAndProjectId(from, to, employeeId.get(), projectId.get());
        } else if (employeeId.isPresent()) {
            return clientId.map(
                    integer -> workUnitRepository.getAllByDateBetweenAndEmployeeIdAndClientId(from, to, employeeId.get(), integer)).
                    orElseGet(() -> workUnitRepository.getAllByDateBetweenAndEmployeeId(from, to, employeeId.get()));
        } else if (projectId.isPresent()) {
            return departmentId.map(
                    integer -> workUnitRepository.getAllByDateBetweenAndDepartmentIdAndProjectId(from, to, integer, projectId.get())).
                    orElseGet(() -> workUnitRepository.getAllByDateBetweenAndProjectId(from, to, projectId.get()));
        } else if (departmentId.isPresent()) {
            return clientId.map(
                    integer -> workUnitRepository.getAllByDateBetweenAndDepartmentIdAndClientId(from, to, departmentId.get(), integer)).
                    orElseGet(() -> workUnitRepository.getAllByDateBetweenAndDepartmentId(from, to, departmentId.get()));
        } else
            return clientId.map(
                    integer -> workUnitRepository.getAllByDateBetweenAndClientId(from, to, integer)).
                    orElseGet(() -> workUnitRepository.getAllByDateBetween(from, to));
    }
}
