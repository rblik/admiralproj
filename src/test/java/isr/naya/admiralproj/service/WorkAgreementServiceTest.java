package isr.naya.admiralproj.service;

import isr.naya.admiralproj.dto.AgreementDto;
import isr.naya.admiralproj.exception.NotFoundException;
import isr.naya.admiralproj.model.WorkAgreement;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static isr.naya.admiralproj.constants.SpringProfiles.TEST;
import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Unit test for {@link WorkAgreementService}
 */
@RunWith(SpringRunner.class)
@ActiveProfiles(TEST)
@SpringBootTest
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:db/data-postgres.sql")
public class WorkAgreementServiceTest {

    @Autowired
    private WorkAgreementService service;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testSave() {
        WorkAgreement save = service.save(1, 14, WorkAgreement.builder().
                workUnits(emptyList()).build());
        assertThat(save, hasProperty("id", is(7)));
    }

    @Test
    public void testSaveWithWrongEmployeeId() {
        thrown.expect(NotFoundException.class);
        thrown.expectMessage("Not found employee");
        service.save(-1, 1, WorkAgreement.builder().
                workUnits(emptyList()).build());
    }

    @Test
    public void testSaveWithWrongProjectId() {
        thrown.expect(NotFoundException.class);
        thrown.expectMessage("Not found project");
        service.save(1, -1, WorkAgreement.builder().
                workUnits(emptyList()).build());
    }

    @Test
    public void testGetAgreementsGraph() {
        List<AgreementDto> agreements = service.getAgreementsGraph();
        assertThat(agreements, hasSize(6));
    }
}