package isr.naya.admiralproj.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class AgreementDto {
    private Integer agreementId;
    private Integer employeeId;
    private String employeeName;
    private String employeeSurname;
    private String departmentName;
    private Integer projectId;
    private String projectName;
    private Integer clientId;
    private String clientName;

    public AgreementDto(Integer agreementId, Integer projectId, String projectName, Integer clientId, String clientName) {
        this(agreementId, null, null, null, null, projectId, projectName, clientId, clientName);
    }
}
