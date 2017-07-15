package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.MonthInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Month;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by jonathan on 14/07/17.
 */
@RunWith(SpringRunner.class)
@ServiceTest
public class MonthInfoServiceTest {
    @Autowired
    private MonthInfoService monthInfoService;

    @Test
    public void testSave() {
        MonthInfo monthInfo = monthInfoService.save(new MonthInfo(2017, Month.JULY.getValue(), false, 1500, null), 1);
        assertNotNull(monthInfo);
    }

    @Test
    public void testGet() {
        MonthInfo monthInfo = monthInfoService.save(new MonthInfo(2017, Month.JULY.getValue(), false, 1500, null), 1);
        boolean lockNotExists = monthInfoService.isLockExists(1, 2017, Month.JULY.getValue());
        assertFalse(lockNotExists);
        monthInfoService.removeLock(1, 2017, Month.JULY.getValue());
        lockNotExists = monthInfoService.isLockExists(1, 2017, Month.JULY.getValue());
        assertTrue(lockNotExists);
    }

    @Test
    public void testUpdateHoursToAll() {
        Iterable<MonthInfo> monthInfos =
                monthInfoService.updateHoursSumForAllEmployees(
                        new MonthInfo(2017, Month.MARCH.getValue(), 200),
                        Arrays.asList(1, 2, 3, 4, 5)
                );
    }
}