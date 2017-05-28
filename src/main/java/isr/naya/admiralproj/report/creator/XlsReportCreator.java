package isr.naya.admiralproj.report.creator;


import isr.naya.admiralproj.dto.WorkInfo;
import isr.naya.admiralproj.report.ReportCreator;
import isr.naya.admiralproj.report.ReportType;
import isr.naya.admiralproj.report.annotations.Xlsx;
import isr.naya.admiralproj.util.MappingUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static isr.naya.admiralproj.report.ReportCreator.durationToTimeString;
import static isr.naya.admiralproj.report.ReportType.*;
import static org.apache.poi.ss.util.WorkbookUtil.createSafeSheetName;

@Component
@Slf4j
@Xlsx
public class XlsReportCreator implements ReportCreator {

    @Override
    public byte[] create(@NonNull List<WorkInfo> infoList, @NonNull ReportType reportType, String employeeTitle) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(createSafeSheetName(reportType.name() + LocalDate.now().toString()));

        Row row = sheet.createRow(0);
        populateTitle(row, reportType);

        int rowNum = 1;
        for (WorkInfo data : infoList) {
            row = sheet.createRow(rowNum++);
            if (PIVOTAL == reportType)
                addFullRow(row, data);
            else if (INDIVIDUAL_PIVOTAL == reportType) {
                addIndividualFullRow(row, data);
            } else {
                if (PARTIAL == reportType)
                    addPartialRow(row, data);
                else if (EMPTY == reportType)
                    addMissedRow(row, data);
                else
                    log.error("Not compatible report type");
            }
            for (int i = 0; i < sheet.getRow(0).getLastCellNum(); i++) {
                sheet.setColumnWidth(i, 4000);

            }
        }

        try {
            workbook.write(os);
            workbook.close();
        } catch (Exception e) {
            log.error("An error occurred while creating report. {}", e.getLocalizedMessage());
            throw new RuntimeException(e);
        }

        return os.toByteArray();
    }

    private void populateTitle(Row row, ReportType reportType) {

        List<String> titles;
        if (PIVOTAL == reportType) {
            titles = Arrays.asList("תאור", "משך", "עד-", "מ-", "יום", "תעריך", "עובד", "מ''ע", "צוות", "פרויקט", "לקוח");
        } else if (INDIVIDUAL_PIVOTAL == reportType) {
            titles = Arrays.asList("תאור", "משך", "עד-", "מ-", "יום", "תעריך", "פרויקט", "לקוח");
        } else if (PARTIAL == reportType) {
            titles = Arrays.asList("משך", "יום", "תעריך", "צוות", "שם משפחה", "שם");
        } else if (EMPTY == reportType) {
            titles = Arrays.asList("יום", "תעריך", "צוות", "שם משפחה", "שם");
        } else {
            titles = Collections.emptyList();
            log.error("Not compatible report type");
        }
        for (int i = 0; i < titles.size(); i++)
            row.createCell(i).setCellValue(titles.get(i));
        alignCenter(row);
    }

    private void addFullRow(Row row, WorkInfo workInfo) {

        row.createCell(0).setCellValue(workInfo.getComment());
        row.createCell(1).setCellValue(durationToTimeString(workInfo.getDuration()));
        row.createCell(2).setCellValue(workInfo.getTo() != null ? workInfo.getTo().truncatedTo(ChronoUnit.MINUTES).toString() : null);
        row.createCell(3).setCellValue(workInfo.getFrom() != null ? workInfo.getFrom().truncatedTo(ChronoUnit.MINUTES).toString() : null);
        row.createCell(4).setCellValue(workInfo.getDate() != null ? MappingUtil.getDay(workInfo.getDate().getDayOfWeek().getValue()) : null);
        row.createCell(5).setCellValue(workInfo.getDate() != null ? workInfo.getDate().toString() : null);
        row.createCell(6).setCellValue(workInfo.getEmployeeSurname() + ' ' + workInfo.getEmployeeName());
        row.createCell(7).setCellValue(workInfo.getEmployeeNumber());
        row.createCell(8).setCellValue(workInfo.getDepartmentName());
        row.createCell(9).setCellValue(workInfo.getProjectName());
        row.createCell(10).setCellValue(workInfo.getClientName());
        alignCenter(row);
    }

    private void addIndividualFullRow(Row row, WorkInfo workInfo) {

        row.createCell(0).setCellValue(workInfo.getComment());
        row.createCell(1).setCellValue(durationToTimeString(workInfo.getDuration()));
        row.createCell(2).setCellValue(workInfo.getTo() != null ? workInfo.getTo().truncatedTo(ChronoUnit.MINUTES).toString() : null);
        row.createCell(3).setCellValue(workInfo.getFrom() != null ? workInfo.getFrom().truncatedTo(ChronoUnit.MINUTES).toString() : null);
        row.createCell(4).setCellValue(workInfo.getDate() != null ? MappingUtil.getDay(workInfo.getDate().getDayOfWeek().getValue()) : null);
        row.createCell(5).setCellValue(workInfo.getDate() != null ? workInfo.getDate().toString() : null);
        row.createCell(6).setCellValue(workInfo.getProjectName());
        row.createCell(7).setCellValue(workInfo.getClientName());
        alignCenter(row);
    }

    private void addMissedRow(Row row, WorkInfo workInfo) {

        row.createCell(0).setCellValue(workInfo.getDate() != null ? MappingUtil.getDay(workInfo.getDate().getDayOfWeek().getValue()) : null);
        row.createCell(1).setCellValue(workInfo.getDate() != null ? workInfo.getDate().toString() : null);
        row.createCell(2).setCellValue(workInfo.getDepartmentName());
        row.createCell(3).setCellValue(workInfo.getEmployeeSurname());
        row.createCell(4).setCellValue(workInfo.getEmployeeName());
        alignCenter(row);
    }

    private void addPartialRow(Row row, WorkInfo workInfo) {

        row.createCell(0).setCellValue(durationToTimeString(workInfo.getDuration()));
        row.createCell(1).setCellValue(workInfo.getDate() != null ? MappingUtil.getDay(workInfo.getDate().getDayOfWeek().getValue()) : null);
        row.createCell(2).setCellValue(workInfo.getDate() != null ? workInfo.getDate().toString() : null);
        row.createCell(3).setCellValue(workInfo.getDepartmentName());
        row.createCell(4).setCellValue(workInfo.getEmployeeSurname());
        row.createCell(5).setCellValue(workInfo.getEmployeeName());
        alignCenter(row);
    }

    private void alignCenter(Row row) {
        CellStyle cellStyle = row.getSheet().getWorkbook().createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        row.cellIterator().forEachRemaining(cell -> cell.setCellStyle(cellStyle));
    }
}
