package isr.naya.admiralproj.service;


import isr.naya.admiralproj.exception.NotFoundException;
import isr.naya.admiralproj.exception.TimeOverlappingException;
import isr.naya.admiralproj.exception.TimeRangeException;
import isr.naya.admiralproj.model.WorkUnit;
import isr.naya.admiralproj.repository.WorkUnitRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:data-postgres.sql")
public class WorkUnitServiceTest {

    @Autowired
    private WorkUnitService service;
    @Autowired
    private WorkUnitRepository repository;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testSave() {
        WorkUnit save = service.save(1, 1,
                WorkUnit.builder().
                        date(LocalDate.of(2017, 1, 1)).
                        start(LocalTime.of(10, 0)).
                        finish(LocalTime.of(12, 0)).build());
        assertThat(save, hasProperty("id", is(32)));
    }

    @Test
    public void testDeleteNotFound() {
        thrown.expect(NotFoundException.class);
        thrown.expectMessage("Not found work unit");
        service.delete(1, -1);
    }

    @Test
    public void testDelete() {
        long count1 = repository.count();
        service.delete(1, 1);
        long count2 = repository.count();
        assertThat(count2, not(count1));
    }

    @Test
    public void testSaveWithWrongWorkAgreementId() {
        thrown.expect(NotFoundException.class);
        thrown.expectMessage("Not found work agreement");
        WorkUnit save = service.save(1, -1,
                WorkUnit.builder().
                        date(LocalDate.of(2017, 1, 1)).
                        start(LocalTime.of(10, 0)).
                        finish(LocalTime.of(12, 0)).build());
        assertThat(save, hasProperty("id", is(31)));
    }

    @Test
    public void testSaveWithOverlappingTime() {
        thrown.expect(TimeOverlappingException.class);
        thrown.expectMessage("There is already a record");
        WorkUnit save = service.save(1, 1,
                WorkUnit.builder().date(LocalDate.of(2017, 3, 20)).
                        start(LocalTime.of(14, 0)).
                        finish(LocalTime.of(15, 0)).build());
    }

    @Test(expected = TimeRangeException.class)
    public void testTimeRange() {
        WorkUnit save = service.save(1, 1,
                WorkUnit.builder().date(LocalDate.of(2017, 4, 20)).
                        start(LocalTime.of(14, 0)).
                        finish(LocalTime.of(13, 0)).build());
    }
}
