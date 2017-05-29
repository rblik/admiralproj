package isr.naya.admiralproj.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Value
@AllArgsConstructor
@EqualsAndHashCode(of = {"employeeId", "date", "from", "to"})
public class WorkInfo implements Serializable {

    private Integer unitId;
    private Integer agreementId;
    private Integer employeeId;
    private String employeeName;
    private String employeeSurname;
    private String employeeEmail;
    private String employeeNumber;
    private String departmentName;
    private Integer projectId;
    private String projectName;
    private Integer clientId;
    private String clientName;
    private LocalDate date;
    private LocalTime from;
    private LocalTime to;
    private Long duration;
    private String comment;
    private Integer amount;
    private Currency currency;
    private TariffType type;

    public WorkInfo(Integer unitId, Integer agreementId, Integer employeeId, String employeeName, String employeeSurname, String employeeEmail, String employeeNumber, String departmentName, Integer projectId, String projectName, Integer clientId, String clientName, LocalDate date, LocalTime from, LocalTime to, Long duration, String comment) {
        this(unitId, agreementId, employeeId, employeeName, employeeSurname, employeeEmail, employeeNumber, departmentName, projectId, projectName, clientId, clientName, date, from, to, duration, comment, null, null, null);
    }

    public WorkInfo(Integer employeeId, String employeeName, String employeeSurname, String employeeEmail, String employeeNumber, String departmentName, LocalDate date, Long duration) {
        this(null, null, employeeId, employeeName, employeeSurname, employeeEmail, employeeNumber, departmentName, null, null, null, null, date, null, null, duration, null);
    }

    public WorkInfo(Integer employeeId, LocalDate date) {
        this(null, null, employeeId, null, null, null, null, null, null, null, null, null, date, null, null, null, null);
    }

    public WorkInfo(Integer unitId, Integer agreementId, Integer projectId, String projectName, Integer clientId, String clientName, LocalDate date, LocalTime from, LocalTime to, Long duration, String comment) {
        this(unitId, agreementId, null, null, null, null, null, null, projectId, projectName, clientId, clientName, date, from, to, duration, comment);
    }

    public WorkInfo(Integer employeeId, String employeeName, String employeeSurname, String employeeEmail, String employeeNumber, String departmentName, LocalDate date) {
        this(null, null, employeeId, employeeName, employeeSurname, employeeEmail, employeeNumber, departmentName, null, null, null, null, date, null, null, null, null);
    }

    public WorkInfo(Integer agreementId, LocalDate date, Long duration) {
        this(null, agreementId, null, null, null, null, null, null, null, null, null, null, date, null, null, duration, null);
    }

    public WorkInfo(Integer agreementId, Integer employeeId, String employeeName, String employeeSurname, String departmentName, Integer projectId, String projectName, Integer clientId, String clientName, LocalDate date) {
        this(null, agreementId, employeeId, employeeName, employeeSurname, null, null, departmentName, projectId, projectName, clientId, clientName, date, null, null, null, null);
    }

    public WorkInfo(Integer employeeId, String employeeName, String employeeSurname, String departmentName, Integer projectId, String projectName, String clientName, Long duration, Integer amount, Currency currency, TariffType type) {
        this(null, null, employeeId, employeeName, employeeSurname, null, null, departmentName, projectId, projectName, null, clientName, null, null, null, duration, null, amount, currency, type);
    }
}
