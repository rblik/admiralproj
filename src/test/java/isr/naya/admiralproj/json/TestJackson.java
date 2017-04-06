package isr.naya.admiralproj.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import isr.naya.admiralproj.model.WorkUnit;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.LocalTime;

/**
 * JSON test for {@link WorkUnit}
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@RunWith(SpringRunner.class)
@JsonTest
public class TestJackson {

    @Autowired
    private JacksonTester<WorkUnit> tester;
    @Autowired
    private ObjectMapper mapper;

    @Test
    public void serializeJson() throws IOException {
        WorkUnit build = WorkUnit.builder().start(LocalTime.now()).finish(LocalTime.now().plusHours(1)).build();
        JsonContent<WorkUnit> write = this.tester.write(build);
        System.out.println(write.getJson());
    }

    @Test
    @SneakyThrows
    public void deserializeJson() {
        String json = "{\"start\":\"15:19:00\",\"finish\":\"16:19:00\"}";
        this.mapper.readValue(json, WorkUnit.class);
        System.out.println("json = " + json);
    }
}
