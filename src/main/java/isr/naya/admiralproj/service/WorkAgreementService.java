package isr.naya.admiralproj.service;

import isr.naya.admiralproj.dto.AgreementDto;
import isr.naya.admiralproj.model.WorkAgreement;

import java.util.List;

public interface WorkAgreementService {

    List<AgreementDto> getAllActiveForEmployee(Integer employeeId);

    List<AgreementDto> getAllForEmployee(Integer employeeId);

    WorkAgreement save(Integer employeeId, Integer projectId, WorkAgreement workAgreement);

    List<AgreementDto> getAgreementsGraph();

    WorkAgreement get(Integer agreementId);

    void disable(Integer agreementId);

    void enable(Integer agreementId);
}
