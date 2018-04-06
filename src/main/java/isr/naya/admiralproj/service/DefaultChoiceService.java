package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.DefaultChoice;
import isr.naya.admiralproj.model.WorkUnit;

import java.util.Collection;

public interface DefaultChoiceService {

    DefaultChoice save(DefaultChoice choice, Integer employeeId, Integer agreementId);

    DefaultChoice get(Integer employeeId);

    default DefaultChoice saveAsDefault(WorkUnit unit, Integer employeeId, Integer agreementId) {
        DefaultChoice choice = DefaultChoice.builder().start(unit.getStart()).finish(unit.getFinish()).build();
        return save(choice, employeeId, agreementId);
    }

    Collection<DefaultChoice> getAll(Integer employeeId);

    void delete(Integer id);
}
