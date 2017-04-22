package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.Department;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Unit test for {@link DepartmentService}
 */
@RunWith(SpringRunner.class)
@ServiceTest
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
}
