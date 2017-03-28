package isr.naya.admiralproj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(of = {"employeeId", "date"})
@AllArgsConstructor
public class MissingDay {
    private Integer employeeId;
    private String employeeName;
    private String employeeSurname;
    private String departmentName;
    private LocalDate date;
}
