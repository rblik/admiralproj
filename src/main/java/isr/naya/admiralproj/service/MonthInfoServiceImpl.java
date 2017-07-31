package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.MonthlyStandard;
import isr.naya.admiralproj.repository.MonthlyStandardRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;

import static java.time.YearMonth.now;
import static java.time.YearMonth.of;

/**
 * Created by jonathan on 14/07/17.
 */
@Service
@AllArgsConstructor
public class MonthInfoServiceImpl implements MonthInfoService {

    private MonthlyStandardRepository monthlyStandardRepository;

    @Override
    public MonthlyStandard saveStandardForMonth(@NonNull Integer year, @NonNull Integer month, @NonNull Integer hoursSum) {
        MonthlyStandard standard = MonthlyStandard
                .builder()
                .yearMonth(of(year, month))
                .hoursSum(hoursSum)
                .build();
        return monthlyStandardRepository.save(standard);
    }

    @Override
    public List<MonthlyStandard> getAllStandards() {
        return monthlyStandardRepository.findAll();
    }

    @Override
    public List<MonthlyStandard> getStandardsForNLastMonths(int monthsNumber) {
        YearMonth month = now().minusMonths(monthsNumber);
        return monthlyStandardRepository.findAllByYearMonthGreaterThan(month);
    }

    @Override
    public List<MonthlyStandard> getStandardsForYear(@NonNull Integer year) {
        YearMonth from = YearMonth.of(year, 1);
        YearMonth to = YearMonth.of(year + 1, 1);
        return monthlyStandardRepository.findAllByYearMonthGreaterThanEqualAndYearMonthIsLessThan(from, to);
    }

    @Override
    public MonthlyStandard getStandardForMonth(Integer year, Integer month) {
        MonthlyStandard monthlyStandard = monthlyStandardRepository.findOne(of(year, month));
        if (monthlyStandard == null) monthlyStandard = MonthlyStandard.builder().yearMonth(YearMonth.of(year, month)).hoursSum(0).build();
        return monthlyStandard;
    }
}
