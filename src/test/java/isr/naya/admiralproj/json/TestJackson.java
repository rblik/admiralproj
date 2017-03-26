package isr.naya.admiralproj.json;

import isr.naya.admiralproj.model.WorkUnit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * JSON test for {@link WorkUnit}
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@RunWith(SpringRunner.class)
@JsonTest
public class TestJackson {

    @Autowired
    private JacksonTester<WorkUnit> tester;

    @Test
    public void serializeJson() throws IOException {
        WorkUnit build = WorkUnit.builder().start(LocalDateTime.now()).finish(LocalDateTime.now().plusHours(1)).build();
        JsonContent<WorkUnit> write = this.tester.write(build);
        System.out.println(write.getJson());

    }
}
