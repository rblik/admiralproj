package isr.naya.admiralproj.report;

import isr.naya.admiralproj.dto.WorkInfo;

import java.util.List;

public interface ReportCreator {
    byte[] create(List<WorkInfo> infoList, ReportType reportType);
}
