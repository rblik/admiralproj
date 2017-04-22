package isr.naya.admiralproj.service;

import isr.naya.admiralproj.dto.WorkInfo;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Unit test for {@link WorkInfoService}
 */
@RunWith(SpringRunner.class)
@ServiceTest
public class ReportServiceTest {

    @Autowired
    private WorkInfoService service;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testGetAllForPartialByEmployee() {
        List<WorkInfo> all = service.getPartialWorkInfos(LocalDate.of(2017, 3, 1), LocalDate.of(2017, 4, 1), 5, Optional.of(1), Optional.empty());
        assertThat(all, hasSize(1));
    }

    @Test
    public void testGetAllForPartialByDepartment() {
        List<WorkInfo> all = service.getPartialWorkInfos(LocalDate.of(2017, 3, 1), LocalDate.of(2017, 4, 1), 5, Optional.empty(), Optional.of(5));
        assertThat(all, hasSize(3));
    }

    @Test
    public void testGetAllForPartial() {
        List<WorkInfo> all = service.getPartialWorkInfos(LocalDate.of(2017, 3, 1), LocalDate.of(2017, 4, 1), 12, Optional.empty(), Optional.empty());
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
        List<WorkInfo> days = service.getMissingWorkInfos(LocalDate.of(2017, 3, 19), LocalDate.of(2017, 3, 24), Optional.empty(), Optional.empty());
        assertThat(days, hasSize(0));
    }

    @Test
    public void testGetAllForMissingByEmployee() {
        List<WorkInfo> days = service.getMissingWorkInfos(LocalDate.of(2017, 3, 19), LocalDate.of(2017, 3, 24), Optional.of(1), Optional.empty());
        assertThat(days, hasSize(0));
    }

    @Test
    public void testGetAllForMissingByDepartment() {
        List<WorkInfo> days = service.getMissingWorkInfos(LocalDate.of(2017, 3, 19), LocalDate.of(2017, 3, 24), Optional.empty(), Optional.of(1));
        assertThat(days, hasSize(0));
    }

    @Test
    public void testGetAllUnitsByDate() {
        List<WorkInfo> workUnits = service.getWorkInfos(LocalDate.of(2017, 3, 1), LocalDate.of(2017, 4, 1), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        assertThat(workUnits, hasSize(26));
    }

    @Test
    public void testGetAllUnitsByDateAndEmployee() {
        List<WorkInfo> workUnits = service.getWorkInfos(LocalDate.of(2017, 3, 1), LocalDate.of(2017, 4, 1), Optional.of(1), Optional.empty(), Optional.empty(), Optional.empty());
        assertThat(workUnits, hasSize(6));
    }

    @Test
    public void testGetAllUnitsByDateAndDepartment() {
        List<WorkInfo> workUnits = service.getWorkInfos(LocalDate.of(2017, 3, 1), LocalDate.of(2017, 4, 1), Optional.empty(), Optional.of(5), Optional.empty(), Optional.empty());
        assertThat(workUnits, hasSize(10));
    }

    @Test
    public void testGetAllUnitsByDateAndProject() {
        List<WorkInfo> workUnits = service.getWorkInfos(LocalDate.of(2017, 3, 1), LocalDate.of(2017, 4, 1), Optional.empty(), Optional.empty(), Optional.of(7), Optional.empty());
        assertThat(workUnits, hasSize(6));
    }

    @Test
    public void testGetAllUnitsByDateAndClient() {
        List<WorkInfo> workUnits = service.getWorkInfos(LocalDate.of(2017, 3, 1), LocalDate.of(2017, 4, 1), Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(3));
        assertThat(workUnits, hasSize(11));
    }

    @Test
    public void testGetAllUnitsByDateAndEmployeeAndProject() {
        List<WorkInfo> workUnits = service.getWorkInfos(LocalDate.of(2017, 3, 1), LocalDate.of(2017, 4, 1), Optional.of(1), Optional.empty(), Optional.of(7), Optional.empty());
        assertThat(workUnits, hasSize(6));
    }

    @Test
    public void testGetAllUnitsByDateAndDepartmentAndClient() {
        List<WorkInfo> workUnits = service.getWorkInfos(LocalDate.of(2017, 3, 1), LocalDate.of(2017, 4, 1), Optional.empty(), Optional.of(7), Optional.empty(), Optional.of(1));
        assertThat(workUnits, hasSize(5));
    }

    @Test
    public void testGetAllUnitsByDateAndDepartmentAndProject() {
        List<WorkInfo> workUnits = service.getWorkInfos(LocalDate.of(2017, 3, 1), LocalDate.of(2017, 4, 1), Optional.empty(), Optional.of(7), Optional.of(1), Optional.empty());
        assertThat(workUnits, hasSize(5));
    }

    @Test
    public void testGetAllUnitsByDateAndEmployeeAndClient() {
        List<WorkInfo> workUnits = service.getWorkInfos(LocalDate.of(2017, 3, 1), LocalDate.of(2017, 4, 1), Optional.of(3), Optional.empty(), Optional.empty(), Optional.of(1));
        assertThat(workUnits, hasSize(5));
    }

}
