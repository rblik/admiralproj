package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.Department;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;

import static isr.naya.admiralproj.constants.SpringProfiles.TEST;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Unit test for {@link DepartmentService}
 */
@RunWith(SpringRunner.class)
@ActiveProfiles(TEST)
@SpringBootTest
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:data-postgres.sql")
public class DepartmentServiceTest {
    @Autowired
    private DepartmentService service;

    @After
    public void evictCache() {
        service.evictCache();
    }

    @Test
    public void testSave() {
        Department save = service.save(Department.builder().name("New Department").build());
        assertThat(service.get(save.getId()), hasProperty("name", is("New Department")));
    }

    @Test(expected = ConstraintViolationException.class)
    public void testSaveWrong() {
        service.save(Department.builder().build());
    }

    @Test
    public void testGetAll() {
        assertThat(service.getAll(), hasSize(9));
    }
}
