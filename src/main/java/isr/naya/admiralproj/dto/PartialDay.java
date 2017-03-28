package isr.naya.admiralproj.dto;

import isr.naya.admiralproj.model.AbsenceType;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"employeeId", "date"})
public class PartialDay {
    private Integer employeeId;
    private String employeeName;
    private String employeeSurname;
    private LocalDate date;
    private Long duration;

    public PartialDay(Integer employeeId, String employeeName, String employeeSurname, LocalDate date, Long duration) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeeSurname = employeeSurname;
        this.date = date;
        this.duration = duration;
    }
}
