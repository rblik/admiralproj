package isr.naya.admiralproj.report;

import isr.naya.admiralproj.dto.WorkInfo;

import java.util.List;

public interface ReportCreator {
    String XLSX = "xlsx";
    String PDF = "pdf";
    String DEJA_VU_SANS = "DejaVuSans.ttf";
    String UTF8 ="utf-8";
    String CLIENT = "לקוח";
    String PROJECT = "פרויקט";
    String DEPARTMENT = "צוות";
    String EMPL_NUMBER = "מ''ע";
    String EMPLOYEE = "עובד";
    String DATE = "תעריך";
    String DAY = "יום";
    String SINCE = "מ-";
    String UNTIL = "עד-";
    String DURATION = "משך";
    String DESCRIPTION = "תאור";
    String EMPL_SURNAME = "שם משפחה";
    String EMPL_NAME = "שם";
    String EMPTY_STR = "";

    static String durationToTimeString(Long duration) {
        int round = Math.round(((duration + 2) / 5) * 5);
        int hours = round / 60;
        int minutes = round % 60;
        return hours + "h " + ((minutes == 0) ? EMPTY_STR : minutes + "min");
    }

    byte[] create(List<WorkInfo> infoList, ReportType reportType, String employeeTitle);
}
