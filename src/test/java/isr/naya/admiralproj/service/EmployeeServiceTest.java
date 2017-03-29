package isr.naya.admiralproj.service;

import isr.naya.admiralproj.exception.NotFoundException;
import isr.naya.admiralproj.model.Employee;
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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:data-postgres.sql")
public class EmployeeServiceTest {
    @Autowired
    private EmployeeService service;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testGetAllWithDepartments() {
        List<Employee> all = service.getAllWithDepartments();
        assertThat(all, hasSize(5));
        assertThat(all, hasItem(allOf(hasProperty("name", is("Name1")), hasProperty("department", hasProperty("name", is("Java"))))));
    }

    @Test
    public void testSave() {
        Employee save = service.save(1, Employee.builder().name("AnotherN").surname("AnotherS").passportId("999999999").password("123123Aa").email("email@gg.com").build());
        Employee employee = service.getWithDepartment(save.getId());
        assertThat(employee, allOf(hasProperty("name", is("AnotherN")), hasProperty("surname", is("AnotherS")), hasProperty("department", hasProperty("name", is("Java")))));
    }

    @Test(expected = ConstraintViolationException.class)
    public void testSaveWrongEmployee() {
        service.save(1, Employee.builder().name("AnotherN").passportId("999999999").password("123123Aa").email("email@gg.com").build());
    }

    @Test
    public void testSaveWithWrongDepartment() {
        thrown.expect(NotFoundException.class);
        thrown.expectMessage("Not found department");
        service.save(-1, Employee.builder().name("AnotherN").surname("AnotherS").passportId("999999999").password("123123Aa").email("email@gg.com").build());
    }
}