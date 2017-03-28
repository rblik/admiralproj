package isr.naya.admiralproj.dto;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.LocalDate;

@Value
@EqualsAndHashCode(of = {"employeeId", "date"})
public class PartialDay {
    private Integer employeeId;
    private String employeeName;
    private String employeeSurname;
    private LocalDate date;
    private Long duration;
}
