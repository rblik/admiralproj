package isr.naya.admiralproj.service;

import isr.naya.admiralproj.exception.NotFoundException;
import isr.naya.admiralproj.model.Project;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static isr.naya.admiralproj.constants.SpringProfiles.TEST;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Unit test for {@link ProjectService}
 */
@RunWith(SpringRunner.class)
@ActiveProfiles(TEST)
@SpringBootTest
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:db/data-postgres.sql")
public class ProjectServiceTest {
    @Autowired
    private ProjectService service;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testSave() {
        Project proj = service.save(6, Project.builder().name("NewProj").build());
        assertThat(service.getWithClient(proj.getId()), allOf(hasProperty("name", is("NewProj")), hasProperty("client", hasProperty("name", is("Elbit")))));
    }

    @Test(expected = ConstraintViolationException.class)
    public void testSaveWrong() {
        service.save(6, Project.builder().build());
    }

    @Test
    public void testSaveWithWrongClient() {
        thrown.expect(NotFoundException.class);
        thrown.expectMessage("Not found client");
        service.save(-1, Project.builder().name("NewProj").build());
    }

    @Test
    public void testGetAllWithClients() {
        List<Project> projects = service.getAllWithClients();
        assertThat(projects, hasSize(14));
        assertThat(projects, hasItem(allOf(hasProperty("name", is("Developing")), hasProperty("client", hasProperty("name", is("Elbit"))))));
    }
}