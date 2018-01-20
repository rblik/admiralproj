package isr.naya.admiralproj.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.io.Serializable;

@Value
@AllArgsConstructor
public class AgreementDto implements Serializable {

    private Integer agreementId;
    private boolean active;
    private Integer tariffId;
    private Double tariffAmount;
    private Currency tariffCurrency;
    private TariffType tariffType;
    private Integer employeeId;
    private String employeeName;
    private String employeeSurname;
    private Integer departmentId;
    private String departmentName;
    private Integer projectId;
    private String projectName;
    private Integer projectTariffTypeId;
    private Integer projectTariffAmount;
    private Currency projectTariffCurrency;
    private TariffType projectTariffType;
    private Integer clientId;
    private String clientName;

    public AgreementDto(Integer agreementId, boolean active, Integer projectId, String projectName, Integer clientId, String clientName) {
        this(agreementId, active, null,
                null, null, null,
                null, null, null,
                null, null,
                projectId, projectName,
                null, null, null, null,
                clientId, clientName);
    }

    public AgreementDto(Integer agreementId, boolean active, Integer employeeId, String employeeName, String employeeSurname, Integer departmentId, String departmentName, Integer projectId, String projectName, Integer clientId, String clientName) {
        this(agreementId, active, null, null, null, null, employeeId, employeeName, employeeSurname, departmentId, departmentName, projectId, projectName, null, null, null, null, clientId, clientName);
    }

    public AgreementDto(Integer agreementId, boolean active, Integer tariffId, Double tariffAmount, Currency tariffCurrency, TariffType tariffType, Integer projectId, String projectName, Integer clientId, String clientName) {
        this(agreementId, active, tariffId, tariffAmount, tariffCurrency, tariffType, null, null, null, null, null, projectId, projectName, null, null, null, null, clientId, clientName);
    }
}
