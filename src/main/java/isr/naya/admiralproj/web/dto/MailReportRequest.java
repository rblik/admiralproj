package isr.naya.admiralproj.web.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
public class MailReportRequest {
    @NotNull
    private LocalDate from;
    @NotNull
    private LocalDate to;
    @NotNull
    private List<Integer> employeeIds;
    @NotNull
    private String email;
    private String message;
}
