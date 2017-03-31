package isr.naya.admiralproj.service;


import isr.naya.admiralproj.model.WorkUnit;

public interface WorkUnitService {

    WorkUnit save(Integer employeeId, Integer workAgreementId, WorkUnit workUnit);

    void delete(Integer employeeId, Integer workUnitId);
}
