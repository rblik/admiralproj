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
    private AbsenceType absenceType;
    private Long absenceMinutes = 0L;

    public PartialDay(Integer employeeId, String employeeName, String employeeSurname, LocalDate date, Long duration) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeeSurname = employeeSurname;
        this.date = date;
        this.duration = duration;
    }

    public PartialDay(Integer employeeId, LocalDate date, AbsenceType absenceType, Long absenceMinutes) {
        this.employeeId = employeeId;
        this.date = date;
        this.absenceType = absenceType;
        this.absenceMinutes = absenceMinutes;
    }

    public PartialDay setAbsence(AbsenceType type, Long duration) {
        this.absenceType = type;
        this.absenceMinutes += duration;
        return this;
    }
}
