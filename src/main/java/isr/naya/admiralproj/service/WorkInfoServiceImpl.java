package isr.naya.admiralproj.service;

import isr.naya.admiralproj.dto.WorkInfo;
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

    @Override
    public List<WorkInfo> getPartialWorkInfos(LocalDate from, LocalDate to,
                                              Integer maxHours,
                                              Optional<Integer> employeeId,
                                              Optional<Integer> departmentId) {

        List<WorkInfo> workInfos;
        if (employeeId.isPresent()) {
            workInfos = workUnitRepository.getAllPartialBetweenDatesByEmployeeId(from, to, maxHours, employeeId.get());
        } else if (departmentId.isPresent()) {
            workInfos = workUnitRepository.getAllPartialBetweenDatesByDepartmentId(from, to, maxHours, departmentId.get());
        } else {
            workInfos = workUnitRepository.getAllPartialBetweenDates(from, to, maxHours);
        }
        return workInfos;
    }

    @Override
    public List<WorkInfo> getMissingWorkInfos(LocalDate from, LocalDate to,
                                              Optional<Integer> employeeId,
                                              Optional<Integer> departmentId) {
        List<WorkInfo> workInfos;
        if (employeeId.isPresent()) {
            workInfos = generate(workUnitRepository.getAllNonEmptyDaysByDateBetweenAndEmployeeId(from, to, employeeId.get()), from, to, singletonList(employeeService.getWithDepartment(employeeId.get())));
        } else if (departmentId.isPresent()) {
            workInfos = generate(workUnitRepository.getAllNonEmptyDaysByDateBetweenAndDepartmentId(from, to, departmentId.get()), from, to, employeeService.getAllByDepartment(departmentId.get()));
        } else {
            workInfos = generate(workUnitRepository.getAllNonEmptyDaysBetweenDates(from, to), from, to, employeeService.getAllWithDepartments());
        }
        return workInfos;
    }

    public List<WorkInfo> getWorkInfos(LocalDate from, LocalDate to,
                                       Optional<Integer> employeeId,
                                       Optional<Integer> departmentId,
                                       Optional<Integer> projectId,
                                       Optional<Integer> clientId) {
        List<WorkInfo> workInfos;
        if (employeeId.isPresent() && projectId.isPresent()) {
            workInfos = workUnitRepository.getAllByDateBetweenAndEmployeeIdAndProjectId(from, to, employeeId.get(), projectId.get());
        } else if (employeeId.isPresent()) {
            if (clientId.isPresent()) {
                workInfos = workUnitRepository.getAllByDateBetweenAndEmployeeIdAndClientId(from, to, employeeId.get(), clientId.get());
            } else {
                workInfos = workUnitRepository.getAllByDateBetweenAndEmployeeId(from, to, employeeId.get());
            }
        } else if (projectId.isPresent()) {
            if (departmentId.isPresent()) {
                workInfos = workUnitRepository.getAllByDateBetweenAndDepartmentIdAndProjectId(from, to, departmentId.get(), projectId.get());
            } else {
                workInfos = workUnitRepository.getAllByDateBetweenAndProjectId(from, to, projectId.get());
            }
        } else if (departmentId.isPresent()) {
            if (clientId.isPresent()) {
                workInfos = workUnitRepository.getAllByDateBetweenAndDepartmentIdAndClientId(from, to, departmentId.get(), clientId.get());
            } else {
                workInfos = workUnitRepository.getAllByDateBetweenAndDepartmentId(from, to, departmentId.get());
            }
        } else if (clientId.isPresent()) {
            workInfos = workUnitRepository.getAllByDateBetweenAndClientId(from, to, clientId.get());
        } else {
            workInfos = workUnitRepository.getAllByDateBetween(from, to);
        }
        return workInfos;
    }
}
