package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.MonthlyStandard;

import java.util.List;

/**
 * Created by jonathan on 14/07/17.
 */
public interface MonthInfoService {

    MonthlyStandard saveStandardForMonth(Integer year, Integer month, Integer hoursSum);

    List<MonthlyStandard> getAllStandards();

    List<MonthlyStandard> getStandardsForNLastMonths(int monthsNumber);

    List<MonthlyStandard> getStandardsForYear(Integer year);

    MonthlyStandard getStandardForMonth(Integer year, Integer month);
}
