package isr.naya.admiralproj.service;

import isr.naya.admiralproj.dto.WorkInfo;
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
import java.util.List;

import static isr.naya.admiralproj.SpringProfiles.TEST;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Unit test for {@link WorkInfoService}
 */
@RunWith(SpringRunner.class)
@ActiveProfiles(TEST)
@SpringBootTest
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:db/data-postgres.sql")
public class ReportServiceTest {

    @Autowired
    private WorkInfoService service;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testGetAllWithTimeSum() {
        List<WorkInfo> all = service.getPartialDays(LocalDate.of(2017, 3, 1), LocalDate.of(2017, 4, 1), 12);
        assertThat(all, hasItem(allOf(
                hasProperty("employeeId", equalTo(1)),
                hasProperty("date", equalTo(LocalDate.of(2017, 3, 19))),
                hasProperty("duration", equalTo(180L)))));
        assertThat(all, hasSize(21));
    }

    @Test
    public void testGetAllWorkUnitsForEmployee() {
        List<WorkInfo> workInfos = service.getAllForEmployee(1, LocalDate.of(2017, 3, 1), LocalDate.of(2017, 4, 1));
        assertThat(workInfos, hasSize(5));
    }

    @Test
    public void testGetAllWorkUnitsForEmployeeByDateAndWorkAgreement() {
        List<WorkInfo> workInfos = service.getAllForEmployeeByDate(1, 1, LocalDate.of(2017, 3, 20));
        assertThat(workInfos, hasSize(1));
    }

    @Test
    public void testGetAllForMissing() {
        List<WorkInfo> days = service.getMissingDays(LocalDate.of(2017, 3, 19), LocalDate.of(2017, 3, 24));
        assertThat(days, hasSize(0));
    }

    @Test
    public void testGetAllForMissingByEmployee() {
        List<WorkInfo> days = service.getMissingDaysByEmployee(LocalDate.of(2017, 3, 19), LocalDate.of(2017, 3, 24), 1);
        assertThat(days, hasSize(0));
    }

    @Test
    public void testGetAllForMissingByDepartment() {
        List<WorkInfo> days = service.getMissingDaysByDepartment(LocalDate.of(2017, 3, 19), LocalDate.of(2017, 3, 24), 1);
        assertThat(days, hasSize(0));
    }

    @Test
    public void testGetAllUnitsByDate() {
        List<WorkInfo> workUnits = service.getAllUnitsByDate(LocalDate.of(2017, 3, 1), LocalDate.of(2017, 4, 1));
        assertThat(workUnits, hasSize(26));
    }

    @Test
    public void testGetAllUnitsByDateAndEmployee() {
        List<WorkInfo> workUnits = service.getAllUnitsByDateAndEmployee(LocalDate.of(2017, 3, 1), LocalDate.of(2017, 4, 1), 1);
        assertThat(workUnits, hasSize(6));
    }

    @Test
    public void testGetAllUnitsByDateAndProject() {
        List<WorkInfo> workUnits = service.getAllUnitsByDateAndProject(LocalDate.of(2017, 3, 1), LocalDate.of(2017, 4, 1), 7);
        assertThat(workUnits, hasSize(6));
    }

    @Test
    public void testGetAllUnitsByDateAndEmployeeProject() {
        List<WorkInfo> workUnits = service.getAllUnitsByDateAndEmployeeAndProject(LocalDate.of(2017, 3, 1), LocalDate.of(2017, 4, 1), 1, 7);
        assertThat(workUnits, hasSize(6));
    }
}
