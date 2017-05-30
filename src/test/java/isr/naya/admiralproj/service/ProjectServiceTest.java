package isr.naya.admiralproj.service;

import isr.naya.admiralproj.dto.Currency;
import isr.naya.admiralproj.dto.TariffType;
import isr.naya.admiralproj.exception.NotFoundException;
import isr.naya.admiralproj.model.Project;
import isr.naya.admiralproj.model.Tariff;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Unit test for {@link ProjectService}
 */
@RunWith(SpringRunner.class)
@ServiceTest
public class ProjectServiceTest {
    @Autowired
    private ProjectService service;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testSave() {
        Project proj = service.save(6, Project.builder().name("NewProj").tariff(Tariff.builder().amount(100).currency(Currency.SHEKEL).type(TariffType.HOUR).build()).build());
        assertThat(service.getWithClient(proj.getId()), allOf(hasProperty("name", is("NewProj")), hasProperty("client", hasProperty("name", is("Elbit")))));
    }

    @Test(expected = ConstraintViolationException.class)
    public void testSaveWrong() {
        service.save(6, Project.builder().tariff(Tariff.builder().amount(100).currency(Currency.SHEKEL).type(TariffType.HOUR).build()).build());
    }

    @Test
    public void testSaveWithWrongClient() {
        thrown.expect(NotFoundException.class);
        thrown.expectMessage("Not found client");
        service.save(-1, Project.builder().name("NewProj").tariff(Tariff.builder().amount(100).currency(Currency.SHEKEL).type(TariffType.HOUR).build()).build());
    }

    @Test
    public void testGetAllWithClients() {
        // TODO: 04/24/2017 testing differences
        List<Project> projects = service.getAllWithClients();
        assertThat(projects, hasSize(14));
        assertThat(projects, hasItem(allOf(hasProperty("name", is("Project1")), hasProperty("client", hasProperty("name", is("Elbit"))))));
    }

    @Test
    public void testGetAllWithClientsByEmployee() {
        List<Project> projects = service.getAllWithClientsByEmployee(1);
        assertThat(projects, hasSize(1));
    }
}