package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.WorkAgreement;
import isr.naya.admiralproj.model.WorkUnit;

import java.time.LocalDate;
import java.util.List;

public interface WorkAgreementService {
    List<WorkAgreement> getAllForEmployee(Integer employeeId, LocalDate from, LocalDate to);

    WorkAgreement save(Integer employeeId, Integer projectId, WorkAgreement workAgreement);

    WorkUnit saveUnit(Integer employeeId, Integer workAgreementId, WorkUnit workUnit);
}
