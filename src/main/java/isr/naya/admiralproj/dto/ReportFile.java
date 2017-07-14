package isr.naya.admiralproj.dto;

import lombok.Value;

@Value
public class ReportFile {
    private ReportFileType fileType;
    private byte[] content;

    public static ReportFile empty(ReportFileType type) {
        return new ReportFile(type, new byte[]{});
    }
}
