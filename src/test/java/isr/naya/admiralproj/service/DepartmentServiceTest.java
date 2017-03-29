package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.Department;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:data-postgres.sql")
public class DepartmentServiceTest {
    @Autowired
    private DepartmentService service;

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

    @Test
    public void testGetAllWithEmployees() {
        List<Department> departments = service.getAllWithEmployees();
        assertThat(departments, hasSize(9));
        assertThat(departments, hasItem(allOf(hasProperty("employees", hasSize(1)), hasProperty("name", is("Java")))));
    }
}
