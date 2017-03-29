package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.WorkAgreement;

import java.time.LocalDate;
import java.util.Set;

public interface WorkAgreementService {

    Set<WorkAgreement> getAllForEmployee(Integer employeeId, LocalDate from, LocalDate to);

    WorkAgreement save(Integer employeeId, Integer projectId, WorkAgreement workAgreement);

    Set<WorkAgreement> getAgreementsGraph();
}
