package isr.naya.admiralproj.util;

import isr.naya.admiralproj.dto.MissingDay;
import isr.naya.admiralproj.model.Employee;
import isr.naya.admiralproj.model.WorkAgreement;
import org.assertj.core.util.Sets;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.collect.Lists.newArrayList;
import static java.time.temporal.ChronoUnit.DAYS;

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

    public static Set<MissingDay> generate(Set<MissingDay> days, LocalDate from, LocalDate to, List<Employee> employees) {
        Set<MissingDay> result = Sets.newHashSet();
        employees.forEach(e -> {
            Set<MissingDay> collect = Stream.iterate(
                    new MissingDay(e.getId(), e.getName(), e.getSurname(), e.getDepartment().getName(), from),
                    missingDay -> new MissingDay(e.getId(), e.getName(), e.getSurname(), e.getDepartment().getName(), missingDay.getDate().plusDays(1))).limit(DAYS.between(from, to)).collect(Collectors.toSet());
            result.addAll(collect);
        });
        result.removeAll(days);
        return result;
    }
}
