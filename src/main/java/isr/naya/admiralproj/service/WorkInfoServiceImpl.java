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

import static isr.naya.admiralproj.util.MappingUtil.filterWeekEndDays;
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
    public List<WorkInfo> getAllForEmployeeByDate(@NonNull Integer employeeId, @NonNull Optional<Integer> agreementId, @NonNull LocalDate date) {
        return agreementId.map(
                integer -> workUnitRepository.getAllForEmployeeByDayAndAgreement(employeeId, integer, date)).
                orElseGet(() -> workUnitRepository.getAllForEmployeeByDay(employeeId, date));
    }

    @Override
    public List<WorkInfo> getPartialWorkInfos(@NonNull LocalDate from, @NonNull LocalDate to,
                                              @NonNull Integer maxHours,
                                              @NonNull Optional<Integer> employeeId,
                                              @NonNull Optional<Integer> departmentId) {

        if (employeeId.isPresent()) {
            return filterWeekEndDays(workUnitRepository.getAllPartialBetweenDatesByEmployeeId(from, to, maxHours, employeeId.get()));
        } else if (departmentId.isPresent()) {
            return filterWeekEndDays(workUnitRepository.getAllPartialBetweenDatesByDepartmentId(from, to, maxHours, departmentId.get()));
        } else {
            return filterWeekEndDays(workUnitRepository.getAllPartialBetweenDates(from, to, maxHours));
        }
    }

    @Override
    public List<WorkInfo> getMissingWorkForParticularEmployees(@NonNull LocalDate from, @NonNull LocalDate to,
                                                               @NonNull List<Integer> employeeIds) {
        List<Employee> employees = employeeService.getParticularWithDepartmentsAndAgreements(employeeIds);
        Set<WorkInfo> infos = workUnitRepository.getAllNonEmptyDaysBetweenAndEmployeeIds(from, to, employeeIds);
        return generate(infos, from, to, employees);
    }

    @Override
    public List<WorkInfo> getMissingWorkInfos(@NonNull LocalDate from, @NonNull LocalDate to,
                                              @NonNull Optional<Integer> employeeId,
                                              @NonNull Optional<Integer> departmentId) {
        if (employeeId.isPresent()) {
            Employee employee = employeeService.getWithDepartmentAndAgreements(employeeId.get());
            return employee == null ? emptyList() : generate(workUnitRepository.getAllNonEmptyDaysByDateBetweenAndEmployeeId(from, to, employeeId.get()), from, to, singletonList(employee));
        } else if (departmentId.isPresent()) {
            List<Employee> employees = employeeService.getAllByDepartmentWithAgreements(departmentId.get());
            Set<WorkInfo> workInfos = workUnitRepository.getAllNonEmptyDaysByDateBetweenAndDepartmentId(from, to, departmentId.get());
            return generate(workInfos, from, to, employees);
        } else {
            List<Employee> employees = employeeService.getAllWithDepartmentsAndAgreements();
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

    @Override
    public List<WorkInfo> getIncomeReports(@NonNull LocalDate from, @NonNull LocalDate to,
                                               @NonNull Optional<Integer> employeeId,
                                               @NonNull Optional<Integer> departmentId,
                                               @NonNull Optional<Integer> projectId,
                                               @NonNull Optional<Integer> clientId) {
        if (employeeId.isPresent() && projectId.isPresent()) {
            return workUnitRepository.getAllIncomeReportsBetweenAndEmployeeIdAndProjectId(from, to, employeeId.get(), projectId.get());
        } else if (employeeId.isPresent()) {
            return clientId.map(
                    integer -> workUnitRepository.getAllIncomeReportsBetweenAndEmployeeIdAndClientId(from, to, employeeId.get(), integer)).
                    orElseGet(() -> workUnitRepository.getAllIncomeReportsBetweenAndEmployeeId(from, to, employeeId.get()));
        } else if (projectId.isPresent()) {
            return departmentId.map(
                    integer -> workUnitRepository.getAllIncomeReportsBetweenAndDepartmentIdAndProjectId(from, to, departmentId.get(), projectId.get())).
                    orElseGet(() -> workUnitRepository.getAllIncomeReportsBetweenAndProjectId(from, to, projectId.get()));
        } else if (departmentId.isPresent()) {
            return clientId.map(
                    integer -> workUnitRepository.getAllIncomeReportsBetweenAndDepartmentIdAndClientId(from, to, departmentId.get(), clientId.get())).
                    orElseGet(() -> workUnitRepository.getAllIncomeReportsBetweenAndDepartmentId(from, to, departmentId.get()));
        } else
            return clientId.map(
                    integer -> workUnitRepository.getAllIncomeReportsBetweenAndClientId(from, to, clientId.get())).
                    orElseGet(() -> workUnitRepository.getAllIncomeReportsBetween(from, to));
    }
}
