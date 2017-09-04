package isr.naya.admiralproj.report;

import isr.naya.admiralproj.dto.*;
import isr.naya.admiralproj.model.WorkUnit;

import java.text.DecimalFormat;
import java.util.List;

import static isr.naya.admiralproj.dto.TariffType.HOUR;

public interface ReportCreator {
    String DEJA_VU_SANS = "DejaVuSans.ttf";
    String UTF8 ="utf-8";
    String CLIENT = "לקוח";
    String PROJECT = "פרויקט";
    String DEPARTMENT = "צוות";
    String EMPL_NUMBER = "מ''ע";
    String EMPLOYEE = "עובד";
    String DATE = "תאריך";
    String DAY_OF_MONTH = "יום בחודש";
    String DAY = "יום";
    String PROJECT_ID = "קוד פעילות";
    String SINCE = "מ-";
    String UNTIL = "עד-";
    String DURATION = "'משך ש";
    String DESCRIPTION = "תאור";
    String EMPL_SURNAME = "שם משפחה";
    String EMPL_NAME = "שם";
    String EMPTY_STR = "";
    String TOTAL = "סיכום";
    String TARIFF = "תעריף";
    String AMOUNT = "סכום";

    static String durationToTimeString(Long duration) {
        return new DecimalFormat("0.00").format(duration/60.0);
    }

    static String calculateIncome(Integer amount, TariffType type, Long duration) {
        return String.valueOf(amount * ((type == HOUR) ? duration / 60.0 : 1));
    }

    static String getCurrencySign(Currency currency) {
        switch (currency) {
            case SHEKEL:
                return "\u20AA";
            case DOLLAR:
                return "$";
            case EURO:
                return "\u20AC";
            default:
                return "";
        }
    }

    default ReportFile create(List<WorkInfo> infoList, ReportType reportType) {
        return create(infoList, reportType, null);
    }

    ReportFile create(List<WorkInfo> infoList, ReportType reportType, String employeeTitle);

    ReportFile generateTemplate(List<AgreementDto> agreements, List<WorkInfo> infos);

    List<WorkUnit> readFromFile(ReportFile file, Integer year, Integer month);
}
