package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.DateLock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Month;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@ServiceTest
public class LockServiceTest {

    @Autowired
    private LockService service;

    @Test
    public void testSaveLock() throws Exception {
        service.saveLock(1, 2017, 5);
        DateLock lock = service.getLock(1, 2017, Month.MAY.getValue());
        assertThat(lock, notNullValue());
    }

    @Test
    public void testGetLock() {
        DateLock lock = service.getLock(1, 2017, Month.MARCH.getValue());
        assertThat(lock, notNullValue());
    }

    @Test
    public void testRemoveLock() throws Exception {
        service.removeLock(1, 2017, Month.MARCH.getValue());
        DateLock lock = service.getLock(1, 2017, Month.MARCH.getValue());
        assertThat(lock, nullValue());
    }

}