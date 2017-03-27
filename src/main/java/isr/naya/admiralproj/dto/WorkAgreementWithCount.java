package isr.naya.admiralproj.dto;

import isr.naya.admiralproj.model.Client;
import isr.naya.admiralproj.model.Employee;
import isr.naya.admiralproj.model.Project;
import isr.naya.admiralproj.model.WorkAgreement;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class WorkAgreementWithCount {
    private Integer agreementId;
    private Integer projectId;
    private String projectName;
    private Integer employeeId;
    private String employeeName;
    private String employeeSurname;
    private Integer clientId;
    private String clientName;
    private Long duration;

    public WorkAgreement getAgreement() {
        Client client = new Client();
        client.setId(clientId);
        client.setName(clientName);
        Project project = new Project(projectName, null, client);
        project.setId(projectId);
        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setName(employeeName);
        employee.setSurname(employeeSurname);
        WorkAgreement agreement = new WorkAgreement(true, project, employee, null, duration);
        agreement.setId(agreementId);
        return agreement;
    }
}
