package isr.naya.admiralproj.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.LocalDate;
import java.time.LocalTime;

@Value
@AllArgsConstructor
@EqualsAndHashCode(of = {"employeeId", "date", "from", "to"})
public class WorkInfo {

    private Integer agreementId;
    private Integer employeeId;
    private String employeeName;
    private String employeeSurname;
    private String departmentName;
    private Integer projectId;
    private String projectName;
    private Integer clientId;
    private String clientName;
    private LocalDate date;
    private LocalTime from;
    private LocalTime to;
    private Long duration;

    public WorkInfo(Integer employeeId, String employeeName, String employeeSurname, LocalDate date, Long duration) {
        this(null, employeeId, employeeName, employeeSurname, null, null, null, null, null, date, null, null, duration);
    }

    public WorkInfo(Integer employeeId, LocalDate date) {
        this(null, employeeId, null, null, null, null, null, null, null, date, null, null, null);
    }

    public WorkInfo(Integer agreementId, LocalDate date, LocalTime from, LocalTime to, Long duration) {
        this(agreementId, null, null, null, null, null, null, null, null, date, from, to, duration);
    }

    public WorkInfo(Integer employeeId, String employeeName, String employeeSurname, String departmentName, LocalDate date) {
        this(null, employeeId, employeeName, employeeSurname, departmentName, null, null, null, null, date, null, null, null);
    }

    public WorkInfo(Integer agreementId, Integer employeeId, String employeeName, String employeeSurname, String departmentName, Integer projectId, String projectName, Integer clientId, String clientName, LocalDate date) {
        this(agreementId, employeeId, employeeName, employeeSurname, departmentName, projectId, projectName, clientId, clientName, date, null, null, null);
    }
}
