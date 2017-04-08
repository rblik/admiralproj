package isr.naya.admiralproj.util;

import isr.naya.admiralproj.dto.Day;
import isr.naya.admiralproj.dto.WorkInfo;
import isr.naya.admiralproj.exception.NotFoundException;
import isr.naya.admiralproj.model.Employee;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.Comparator.comparing;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MappingUtil {

    public static List<WorkInfo> generate(Set<WorkInfo> days, LocalDate from, LocalDate to, List<Employee> employees) {
        Set<WorkInfo> result = Sets.newHashSet();
        employees.forEach(e -> {
            Set<WorkInfo> collect = Stream.iterate(
                    new WorkInfo(e.getId(), e.getName(), e.getSurname(), e.getEmail(), e.getDepartment().getName(), from),
                    missingDay -> new WorkInfo(e.getId(), e.getName(), e.getSurname(), e.getEmail(), e.getDepartment().getName(), missingDay.getDate().plusDays(1))).limit(DAYS.between(from, to)).collect(Collectors.toSet());
            result.addAll(collect);
        });
        result.removeAll(days);
        ArrayList<WorkInfo> infos = Lists.newArrayList(result);
        infos.sort(comparing(WorkInfo::getDate).reversed());
        return infos;
    }

    public static String getDay(int index) throws NotFoundException {
        return Arrays.stream(Day.values()).filter(d -> d.getIndex() == index).findFirst().map(Day::getSymbol).orElse("N/A");
    }
}
