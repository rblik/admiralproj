package isr.naya.admiralproj.service;

import isr.naya.admiralproj.dto.MissingDay;
import isr.naya.admiralproj.dto.PartialDay;
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
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:data-postgres.sql")
public class ReportServiceTest {

    @Autowired
    private ReportService service;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testGetAllWithTimeSum() {
        Set<PartialDay> all = service.getPartialDays(LocalDate.of(2017, 3, 1), LocalDate.of(2017, 4, 1), 12);
        assertThat(all, hasItem(allOf(
                hasProperty("employeeId", equalTo(1)),
                hasProperty("date", equalTo(LocalDate.of(2017, 3, 19))),
                hasProperty("duration", equalTo(180L)))));
        assertThat(all, hasSize(21));
    }

    @Test
    public void testGetAllForMissing() {
        Set<MissingDay> days = service.getMissingDays(LocalDate.of(2017, 3, 19), LocalDate.of(2017, 3, 24));
        assertThat(days, hasSize(0));
    }


    @Test
    public void testGetAllUnitsByDate() {
        Set<WorkUnit> workUnits = service.getAllUnitsByDate(LocalDate.of(2017, 3, 1), LocalDate.of(2017, 4, 1));
        assertThat(workUnits, hasSize(26));
    }

    @Test
    public void testGetAllUnitsByDateAndEmployee() {
        Set<WorkUnit> workUnits = service.getAllUnitsByDateAndEmployee(LocalDate.of(2017, 3, 1), LocalDate.of(2017, 4, 1), 1);
        assertThat(workUnits, hasSize(6));
    }

    @Test
    public void testGetAllUnitsByDateAndProject() {
        Set<WorkUnit> workUnits = service.getAllUnitsByDateAndProject(LocalDate.of(2017, 3, 1), LocalDate.of(2017, 4, 1), 7);
        assertThat(workUnits, hasSize(6));
    }

    @Test
    public void testGetAllUnitsByDateAndEmployeeProject() {
        Set<WorkUnit> workUnits = service.getAllUnitsByDateAndEmployeeAndProject(LocalDate.of(2017, 3, 1), LocalDate.of(2017, 4, 1), 1, 7);
        assertThat(workUnits, hasSize(6));
    }
}
