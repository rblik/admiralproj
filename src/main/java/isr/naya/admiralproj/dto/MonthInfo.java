package isr.naya.admiralproj.dto;

import isr.naya.admiralproj.model.DateLock;
import isr.naya.admiralproj.model.MonthlyStandard;
import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * Created by jonathan on 14/07/17.
 */

@Value
@AllArgsConstructor
public class MonthInfo {

    private int year;
    private int month;
    private Boolean locked;
    private int hoursSum;

    public MonthInfo(MonthlyStandard standardForMonth) {
        this.year = standardForMonth.getYearMonth().getYear();
        this.month = standardForMonth.getYearMonth().getMonthValue();
        this.hoursSum = standardForMonth.getHoursSum();
        this.locked = null;
    }

    public MonthInfo(MonthlyStandard standardForMonth, DateLock lock) {
        this.year = standardForMonth.getYearMonth().getYear();
        this.month = standardForMonth.getYearMonth().getMonthValue();
        this.hoursSum = standardForMonth.getHoursSum();
        this.locked = (lock != null);
    }
}
