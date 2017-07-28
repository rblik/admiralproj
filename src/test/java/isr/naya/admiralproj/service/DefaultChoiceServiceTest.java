package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.DefaultChoice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalTime;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@ServiceTest
public class DefaultChoiceServiceTest {

    @Autowired
    private DefaultChoiceService service;

    @Test
    public void testSave() {
        service.save(DefaultChoice
                        .builder()
                        .start(LocalTime.of(9, 0))
                        .finish(LocalTime.of(12, 0))
                        .build(),
                5, 5);
        DefaultChoice choice = service.get(5);
        assertNotNull(choice);
    }

    @Test
    public void testGet() {
        DefaultChoice choice = service.get(1);
        assertNull(choice);
    }
}