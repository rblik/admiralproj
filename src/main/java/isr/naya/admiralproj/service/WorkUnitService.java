package isr.naya.admiralproj.service;


import isr.naya.admiralproj.model.WorkUnit;

public interface WorkUnitService {

    WorkUnit saveUnit(Integer employeeId, Integer workAgreementId, WorkUnit workUnit);
}
