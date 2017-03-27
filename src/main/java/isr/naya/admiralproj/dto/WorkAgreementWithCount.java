package isr.naya.admiralproj.dto;

import isr.naya.admiralproj.model.*;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(of = "agreement")
public class WorkAgreementWithCount {
    private Employee employee;
    private WorkAgreement agreement;
    private Client client;
    private Project project;
    private Long count;


    public WorkAgreement getAgreement() {
        this.project.setClient(client);
        this.agreement.setProject(project);
        this.agreement.setEmployee(employee);
        this.agreement.setCountTime(count);
        return this.agreement;
    }
}
