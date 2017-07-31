package isr.naya.admiralproj.repository;

import isr.naya.admiralproj.model.MonthlyStandard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.YearMonth;
import java.util.List;

public interface MonthlyStandardRepository extends JpaRepository<MonthlyStandard, YearMonth> {

    List<MonthlyStandard> findAllByYearMonthGreaterThan(YearMonth yearMonth);

    List<MonthlyStandard> findAllByYearMonthGreaterThanEqualAndYearMonthIsLessThan(YearMonth from, YearMonth to);
}
