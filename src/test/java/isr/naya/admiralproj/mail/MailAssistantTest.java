package isr.naya.admiralproj.mail;

import isr.naya.admiralproj.service.ServiceTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Arrays;

@RunWith(SpringRunner.class)
@ServiceTest
public class MailAssistantTest {
    @Autowired
    private MailAssistant assistant;

    @Test
    public void testSend() {
        assistant.send(LocalDate.of(2017,1,1), LocalDate.of(2017,2,1), Arrays.asList(1,2), "jblik1989@gmail.com", "asasas");
    }
}