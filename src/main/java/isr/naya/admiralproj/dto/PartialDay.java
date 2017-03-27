package isr.naya.admiralproj.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.LocalDate;

@Value
@AllArgsConstructor
@EqualsAndHashCode(of = {"agreementId", "date"})
public class PartialDay {
    private Integer agreementId;
    private Integer employeeId;
    private String employeeName;
    private String employeeSurname;
    private LocalDate date;
    private Long duration;
}
