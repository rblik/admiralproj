package isr.naya.admiralproj.mail;

import isr.naya.admiralproj.dto.WorkInfo;
import isr.naya.admiralproj.service.ServiceTest;
import isr.naya.admiralproj.service.WorkInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@ServiceTest
public class MailAssistantTest {
    @Autowired
    private MailService mailService;
    @Autowired
    private WorkInfoService infoService;

    @Test
    public void testSend() {
        List<WorkInfo> missingDays = infoService.getMissingWorkForParticularEmployees(LocalDate.of(2017,1,1), LocalDate.of(2017,2,1), Arrays.asList(1,2));
        mailService.sendReport(missingDays, "jblik1989@gmail.com", "ימים חסרים", "asasas");
    }
}