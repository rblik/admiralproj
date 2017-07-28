package isr.naya.admiralproj.repository;

import isr.naya.admiralproj.model.DefaultChoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DefaultChoiceRepository extends JpaRepository<DefaultChoice, Integer> {

    DefaultChoice findByIdAndAgreementActive(Integer integer, boolean active);
}
