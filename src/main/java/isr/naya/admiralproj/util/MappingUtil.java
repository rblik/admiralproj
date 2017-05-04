package isr.naya.admiralproj.util;

import isr.naya.admiralproj.dto.Day;
import isr.naya.admiralproj.dto.WorkInfo;
import isr.naya.admiralproj.exception.NotFoundException;
import isr.naya.admiralproj.model.Employee;
import isr.naya.admiralproj.model.WorkAgreement;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.Comparator.comparing;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MappingUtil {

    public static List<WorkInfo> generate(Set<WorkInfo> days, LocalDate from, LocalDate to, List<Employee> employees) {
        Set<WorkInfo> result = Sets.newHashSet();
        employees.forEach(e -> {
            List<WorkAgreement> agreements = e.getWorkAgreements();
            Set<WorkInfo> collect = newHashSet();
            for (int i = 0; i < DAYS.between(from, to); i++) {
                LocalDate date = from.plusDays(i);
                if (isActive(agreements)) {
                    WorkInfo info = new WorkInfo(e.getId(), e.getName(), e.getSurname(), e.getEmail(), e.getEmployeeNumber(), e.getDepartment().getName(), date);
                    collect.add(info);
                }
            }
            result.addAll(collect);
        });
        result.removeAll(days);
        ArrayList<WorkInfo> infos = Lists.newArrayList(result);
        infos.sort(comparing(WorkInfo::getDate).reversed());
        return infos;
    }

    private static boolean isActive(List<WorkAgreement> workAgreements) {
        for (WorkAgreement agreement : workAgreements) {
            if (agreement.isActive()) {
                return true;
            }
        }
        return false;
    }

    public static String getDay(int index) throws NotFoundException {
        return Arrays.stream(Day.values()).filter(d -> d.getIndex() == index).findFirst().map(Day::getSymbol).orElse("N/A");
    }
}
