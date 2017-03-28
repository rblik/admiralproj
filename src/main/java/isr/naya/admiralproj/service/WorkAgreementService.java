package isr.naya.admiralproj.service;

import isr.naya.admiralproj.dto.MissingDay;
import isr.naya.admiralproj.dto.PartialDay;
import isr.naya.admiralproj.model.WorkAgreement;
import isr.naya.admiralproj.model.WorkUnit;

import java.time.LocalDate;
import java.util.Set;

public interface WorkAgreementService {
    Set<WorkAgreement> getAllForEmployee(Integer employeeId, LocalDate from, LocalDate to);

    WorkAgreement save(Integer employeeId, Integer projectId, WorkAgreement workAgreement);

    WorkUnit saveUnit(Integer employeeId, Integer workAgreementId, WorkUnit workUnit);

    Set<PartialDay> getPartialDays(LocalDate from, LocalDate to, Integer maxHours);

    Set<MissingDay> getMissingDays(LocalDate from, LocalDate to);

    Set<WorkUnit> getAllUnitsByDate(LocalDate from, LocalDate to);

    Set<WorkUnit> getAllUnitsByDateAndEmployee(LocalDate from, LocalDate to, Integer employeeId);

    Set<WorkUnit> getAllUnitsByDateAndProject(LocalDate from, LocalDate to, Integer projectId);

    Set<WorkUnit> getAllUnitsByDateAndEmployeeAndProject(LocalDate from, LocalDate to, Integer employeeId, Integer projectId);
}
