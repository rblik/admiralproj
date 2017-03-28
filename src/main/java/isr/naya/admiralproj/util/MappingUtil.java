package isr.naya.admiralproj.util;

import isr.naya.admiralproj.dto.PartialDay;
import isr.naya.admiralproj.model.WorkAgreement;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

public class MappingUtil {
    private MappingUtil() {
    }
    public static Set<WorkAgreement> intersectAgreements(Set<WorkAgreement> agreements, Set<WorkAgreement> agreementsWithUnits) {
        Set<WorkAgreement> intersect = agreements.stream()
                .filter(agreement -> !agreementsWithUnits.contains(agreement))
                .map(MappingUtil::populate)
                .collect(Collectors.toSet());
        intersect.addAll(agreementsWithUnits);
        return intersect;
    }

    private static WorkAgreement populate(WorkAgreement workAgreement) {
        workAgreement.setWorkUnits(newArrayList());
        return workAgreement;
    }
}
