package isr.naya.admiralproj.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.LocalDate;

@Value
@AllArgsConstructor
@EqualsAndHashCode(of = {"employeeId", "date"})
public class MissingDay {
    private Integer employeeId;
    private String employeeName;
    private String employeeSurname;
    private String departmentName;
    private LocalDate date;

    public MissingDay(Integer employeeId, LocalDate date) {
        this(employeeId, null, null, null, date);
    }
}
