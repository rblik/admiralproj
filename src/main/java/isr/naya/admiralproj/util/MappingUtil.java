package isr.naya.admiralproj.util;

import isr.naya.admiralproj.dto.WorkInfo;
import isr.naya.admiralproj.model.Employee;
import org.assertj.core.util.Sets;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.temporal.ChronoUnit.DAYS;

public class MappingUtil {
    private MappingUtil() {
    }

    public static Set<WorkInfo> generate(Set<WorkInfo> days, LocalDate from, LocalDate to, List<Employee> employees) {
        Set<WorkInfo> result = Sets.newHashSet();
        employees.forEach(e -> {
            Set<WorkInfo> collect = Stream.iterate(
                    new WorkInfo(e.getId(), e.getName(), e.getSurname(), e.getDepartment().getName(), from),
                    missingDay -> new WorkInfo(e.getId(), e.getName(), e.getSurname(), e.getDepartment().getName(), missingDay.getDate().plusDays(1))).limit(DAYS.between(from, to)).collect(Collectors.toSet());
            result.addAll(collect);
        });
        result.removeAll(days);
        return result;
    }
}
