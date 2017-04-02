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
    private AbsenceType absenceType;
    private LocalTime from;
    private LocalTime to;
    private Long duration;
    private String comment;

    public WorkInfo(Integer employeeId, String employeeName, String employeeSurname, LocalDate date, Long duration) {
        this(null, employeeId, employeeName, employeeSurname, null, null, null, null, null, date, null,null, null, duration, null);
    }

    public WorkInfo(Integer employeeId, LocalDate date) {
        this(null, employeeId, null, null, null, null, null, null, null, date, null,null, null, null, null);
    }

    public WorkInfo(Integer agreementId, LocalDate date, LocalTime from, LocalTime to, Long duration, AbsenceType absenceType, String comment) {
        this(agreementId, null, null, null, null, null, null, null, null, date, absenceType, from, to, duration, comment);
    }

    public WorkInfo(Integer employeeId, String employeeName, String employeeSurname, String departmentName, LocalDate date) {
        this(null, employeeId, employeeName, employeeSurname, departmentName, null, null, null, null, date, null,null, null, null, null);
    }

    public WorkInfo(Integer agreementId, LocalDate date, Long duration) {
        this(agreementId, null, null, null, null, null, null, null, null, date, null, null, null, duration, null);
    }

    public WorkInfo(Integer agreementId, Integer employeeId, String employeeName, String employeeSurname, String departmentName, Integer projectId, String projectName, Integer clientId, String clientName, LocalDate date) {
        this(agreementId, employeeId, employeeName, employeeSurname, departmentName, projectId, projectName, clientId, clientName, date, null, null, null, null, null);
    }
}
