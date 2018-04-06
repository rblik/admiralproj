package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.DefaultChoice;
import isr.naya.admiralproj.model.WorkAgreement;
import isr.naya.admiralproj.repository.DefaultChoiceRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.Collection;

import static isr.naya.admiralproj.util.ValidationUtil.checkNotFound;

@Service
@AllArgsConstructor
public class DefaultChoiceServiceImpl implements DefaultChoiceService {

    private DefaultChoiceRepository choiceRepository;
    private WorkAgreementService agreementService;
    private EmployeeService employeeService;

    @Override
    @Transactional
    public DefaultChoice save(@NonNull DefaultChoice choice, @NonNull Integer employeeId, @NonNull Integer agreementId) {
        WorkAgreement agreement = agreementService.get(agreementId);
        choice.setAgreement(agreement);
        choice.setEmployee(employeeService.get(employeeId));
        return choiceRepository.save(choice);
    }

    @Override
    public DefaultChoice get(@NonNull Integer employeeId) {
        return checkNotFound(choiceRepository.findByIdAndAgreementActive(employeeId, true), employeeId, DefaultChoice.class);
    }

    @Override
    public Collection<DefaultChoice> getAll(@NonNull Integer employeeId) {
        return choiceRepository.findAllByEmployeeIdAndAgreementActive(employeeId, true);
    }

    @Override
    @Transactional
    public void delete(@NonNull Integer id){
        choiceRepository.removeDefaultChoiceById(id);
    }
}
