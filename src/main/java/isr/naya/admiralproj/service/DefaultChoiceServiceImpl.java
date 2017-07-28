package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.DefaultChoice;
import isr.naya.admiralproj.model.WorkAgreement;
import isr.naya.admiralproj.repository.DefaultChoiceRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class DefaultChoiceServiceImpl implements DefaultChoiceService {

    private DefaultChoiceRepository choiceRepository;
    private WorkAgreementService agreementService;

    @CacheEvict(value = "choices", allEntries = true)
    @Override
    @Transactional
    public DefaultChoice save(@NonNull DefaultChoice choice, @NonNull Integer employeeId, @NonNull Integer agreementId) {
        WorkAgreement agreement = agreementService.get(agreementId);
        choice.setAgreement(agreement);
        choice.setId(employeeId);
        return choiceRepository.save(choice);
    }

    @Cacheable(value = "choices", key = "getMethodName() + #employeeId")
    @Override
    public DefaultChoice get(@NonNull Integer employeeId) {
        return choiceRepository.findOne(employeeId);
    }
}
