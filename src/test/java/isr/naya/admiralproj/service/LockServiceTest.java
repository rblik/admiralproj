package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.DateLock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@ServiceTest
public class LockServiceTest {

    @Autowired
    private LockService service;

    @Test
    public void testSaveLock() throws Exception {
        service.saveLock(DateLock.builder().year(2017).month(Month.MAY.getValue()).build(), 1);
        Set<DateLock> locks = service.getAllLocks(1, LocalDate.of(2017, Month.JANUARY, 1), LocalDate.MAX);
        assertThat(locks, hasSize(4));
    }

    @Test
    public void testGetAllLocks() {
        Set<DateLock> locks = service.getAllLocks(1, LocalDate.of(2017, Month.JANUARY, 1), LocalDate.MAX);
        assertThat(locks, hasSize(3));
    }

    @Test
    public void testRemoveLock() throws Exception {
        service.removeLock(1, 2017, Month.MARCH.getValue());
        Set<DateLock> locks = service.getAllLocks(1, LocalDate.of(2017, Month.JANUARY, 1), LocalDate.MAX);
        assertThat(locks, hasSize(2));
    }

}