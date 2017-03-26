package isr.naya.admiralproj.service;

import isr.naya.admiralproj.exception.NotFoundException;
import isr.naya.admiralproj.exception.TimeOverlappingException;
import isr.naya.admiralproj.model.Tariff;
import isr.naya.admiralproj.model.TariffType;
import isr.naya.admiralproj.model.WorkAgreement;
import isr.naya.admiralproj.model.WorkUnit;
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
import java.time.LocalDateTime;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Unit test for {@link WorkAgreementService}
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:data-postgres.sql")
public class WorkAgreementServiceTest {

    @Autowired
    private WorkAgreementService service;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testGetAllForEmployeeByDateRange() {
        List<WorkAgreement> agreements = service.getAllForEmployeeByDateRange(5,
                LocalDate.of(2017, 4, 19),
                LocalDate.of(2017, 4, 26));
        assertThat(agreements, allOf(hasSize(2), contains(
                hasProperty("workUnits", hasSize(0)),
                hasProperty("workUnits", hasSize(5)))));
    }

    @Test
    public void testSave() {
        WorkAgreement save = service.save(1, 14, WorkAgreement.builder().
                since(LocalDate.of(2017, 2, 2)).
                until(LocalDate.of(2018, 2, 2)).
                tariff(Tariff.builder().tariffType(TariffType.HOURLY).tariffAmount(100).build()).
                workUnits(emptyList()).build());
        assertThat(save, hasProperty("id", is(7)));
    }

    @Test
    public void testSaveWithWrongEmployeeId() {
        thrown.expect(NotFoundException.class);
        thrown.expectMessage("Not found employee");
        service.save(-1, 1, WorkAgreement.builder().
                since(LocalDate.of(2017, 2, 2)).
                until(LocalDate.of(2018, 2, 2)).
                tariff(Tariff.builder().tariffType(TariffType.HOURLY).tariffAmount(100).build()).
                workUnits(emptyList()).build());
    }

    @Test
    public void testSaveWithWrongProjectId() {
        thrown.expect(NotFoundException.class);
        thrown.expectMessage("Not found project");
        service.save(1, -1, WorkAgreement.builder().
                since(LocalDate.of(2017, 2, 2)).
                until(LocalDate.of(2018, 2, 2)).
                tariff(Tariff.builder().tariffType(TariffType.HOURLY).tariffAmount(100).build()).
                workUnits(emptyList()).build());
    }

    @Test
    public void testSaveUnit() {
        WorkUnit save = service.saveUnit(1, 1,
                WorkUnit.builder().start(LocalDateTime.of(2017, 1, 1, 10, 0))
                        .finish(LocalDateTime.of(2017, 1, 1, 12, 0)).build());
        assertThat(save, hasProperty("id", is(31)));
    }

    @Test
    public void testSaveUnitWithWrongWorkAgreementId() {
        thrown.expect(NotFoundException.class);
        thrown.expectMessage("Not found work agreement");
        WorkUnit save = service.saveUnit(1, -1,
                WorkUnit.builder().start(LocalDateTime.of(2017, 1, 1, 10, 0))
                        .finish(LocalDateTime.of(2017, 1, 1, 12, 0)).build());
        assertThat(save, hasProperty("id", is(31)));
    }

    @Test
    public void testSaveUnitWithWrongEmployeeId() {
        thrown.expect(TimeOverlappingException.class);
        thrown.expectMessage("There is already a record");
        WorkUnit save = service.saveUnit(1, 1,
                WorkUnit.builder().start(LocalDateTime.of(2017, 3, 19, 10, 0))
                        .finish(LocalDateTime.of(2017, 3, 19, 12, 0)).build());
    }
}