package isr.naya.admiralproj.service;

import isr.naya.admiralproj.exception.NotFoundException;
import isr.naya.admiralproj.model.Client;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;

import static com.google.common.collect.ImmutableSet.of;
import static java.util.Collections.emptySet;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:data-postgres.sql")
public class ClientServiceTest {

    @Autowired
    private ClientService service;

    @Test
    public void testSave() {
        Client save = service.save(Client.builder().addresses(emptySet()).name("Another Comp.").companyNumber(111111111).phones(of("055-5555555")).build());
        Client client = service.get(save.getId());
        assertThat(client, hasProperty("name", is("Another Comp.")));
    }

    @Test
    public void testGet() {
        Client client = service.get(1);
        assertThat(client, notNullValue());
    }

    @Test(expected = NotFoundException.class)
    public void testGetWithWrongId() {
        service.get(-1);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testSaveWrong() {
        service.save(Client.builder().companyNumber(321123321).build());
    }

    @Test
    public void testGetAll() {
        assertThat(service.getAll(), hasSize(6));
    }
}