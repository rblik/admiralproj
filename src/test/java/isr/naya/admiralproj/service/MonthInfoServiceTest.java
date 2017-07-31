package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.MonthlyStandard;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Month;
import java.time.YearMonth;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Created by jonathan on 14/07/17.
 */
@RunWith(SpringRunner.class)
@ServiceTest
public class MonthInfoServiceTest {
    @Autowired
    private MonthInfoService monthInfoService;

    @Test
    public void testSaveStandard() {
        MonthlyStandard month = monthInfoService.saveStandardForMonth(2017, Month.JULY.getValue(), 200);
        assertNotNull(month);
        MonthlyStandard month1 = monthInfoService.saveStandardForMonth(2017, Month.JUNE.getValue(), 200);
        List<MonthlyStandard> allStandards = monthInfoService.getAllStandards();
        MonthlyStandard standardForMonth = monthInfoService.getStandardForMonth(2017, 7);
        List<MonthlyStandard> standardsForNLastMonths = monthInfoService.getStandardsForNLastMonths(5);
        monthInfoService.saveStandardForMonth(2017, Month.JULY.getValue(), 150);
        List<MonthlyStandard> standardsForNLastMonths1 = monthInfoService.getStandardsForNLastMonths(5);
        assertThat(standardsForNLastMonths1, hasSize(2));
        assertThat(standardsForNLastMonths1, hasItem(MonthlyStandard.builder().yearMonth(YearMonth.of(2017,Month.JULY)).hoursSum(150).build()));
    }
}