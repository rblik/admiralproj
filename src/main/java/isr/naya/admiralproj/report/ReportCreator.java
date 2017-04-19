package isr.naya.admiralproj.report;

import isr.naya.admiralproj.dto.WorkInfo;

import java.util.List;

public interface ReportCreator {
    String XLSX = "xlsx";
    String PDF = "pdf";

    static String durationToTimeString(Long duration) {
        int round = Math.round(((duration + 2) / 5) * 5);
        int hours = round / 60;
        int minutes = round % 60;
        return hours + "h " + ((minutes == 0) ? "" : minutes + "min");
    }

    byte[] create(List<WorkInfo> infoList, ReportType reportType);
}
