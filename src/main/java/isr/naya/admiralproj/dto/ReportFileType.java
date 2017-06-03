package isr.naya.admiralproj.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReportFileType {
    PDF("application/pdf"),
    XLS("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    private String name;
}
