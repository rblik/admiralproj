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
    private LocalDate date;
    private LocalTime from;
    private LocalTime to;
    private Long duration;

    public WorkInfo(Integer employeeId, String employeeName, String employeeSurname, LocalDate date, Long duration) {
        this(null, employeeId, employeeName, employeeSurname, date, null, null, duration);
    }

    public WorkInfo(Integer agreementId, LocalDate date, LocalTime from, LocalTime to, Long duration) {
        this(agreementId, null, null, null, date, from, to, duration);
    }
}
