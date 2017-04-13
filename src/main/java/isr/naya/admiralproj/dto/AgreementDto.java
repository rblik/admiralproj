package isr.naya.admiralproj.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.io.Serializable;

@Value
@AllArgsConstructor
public class AgreementDto implements Serializable {

    private Integer agreementId;
    private Integer employeeId;
    private String employeeName;
    private String employeeSurname;
    private Integer departmentId;
    private String departmentName;
    private Integer projectId;
    private String projectName;
    private Integer clientId;
    private String clientName;

    public AgreementDto(Integer agreementId, Integer projectId, String projectName, Integer clientId, String clientName) {
        this(agreementId, null, null, null, null, null, projectId, projectName, clientId, clientName);
    }
}
