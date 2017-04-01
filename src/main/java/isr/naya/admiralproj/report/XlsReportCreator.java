package isr.naya.admiralproj.report;


import isr.naya.admiralproj.dto.WorkInfo;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static isr.naya.admiralproj.report.ReportType.*;

@Component
@Slf4j
@Qualifier("XLS")
public class XlsReportCreator implements ReportCreator {

    @Override
    public byte[] create(@NonNull List<WorkInfo> infoList, @NonNull ReportType reportType) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(reportType.name());

        Row row = sheet.createRow(0);
        populateTitle(row, reportType);

        int rowNum = 1;
        for (WorkInfo data : infoList) {
            row = sheet.createRow(rowNum++);
            if (PIVOTAL == reportType)
                addFullRow(row, data);
            else if (PARTIAL == reportType)
                addPartialRow(row, data);
            else if (EMPTY == reportType)
                addMissedRow(row, data);
            else
                log.error("Not compatible report type");
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
            titles = Arrays.asList("תאור", "משך", "עד-", "מ-", "חופשה", "תעריך", "לקוח", "פרויקט", "צוות", "שם משפחה", "שם");
        } else if (PARTIAL == reportType) {
            titles = Arrays.asList("משך", "תעריך", "שם משפחה", "שם");
        } else if (EMPTY == reportType) {
            titles = Arrays.asList("תעריך", "צוות", "שם משפחה", "שם");
        } else {
            titles = Collections.emptyList();
            log.error("Not compatible report type");
        }
        for (int i = 0; i < titles.size(); i++)
            row.createCell(i).setCellValue(titles.get(i));
    }

    private void addFullRow(Row row, WorkInfo workInfo) {

        row.createCell(0).setCellValue(workInfo.getComment());
        row.createCell(1).setCellValue(workInfo.getDuration().toString());
        row.createCell(2).setCellValue(workInfo.getTo() != null ? workInfo.getTo().truncatedTo(ChronoUnit.MINUTES).toString() : null);
        row.createCell(3).setCellValue(workInfo.getFrom() != null ? workInfo.getFrom().truncatedTo(ChronoUnit.MINUTES).toString() : null);
        row.createCell(4).setCellValue(workInfo.getAbsenceType() != null ? workInfo.getAbsenceType().toString() : null);
        row.createCell(5).setCellValue(workInfo.getDate() != null ? workInfo.getDate().toString() : null);
        row.createCell(6).setCellValue(workInfo.getClientName());
        row.createCell(7).setCellValue(workInfo.getProjectName());
        row.createCell(8).setCellValue(workInfo.getDepartmentName());
        row.createCell(9).setCellValue(workInfo.getEmployeeSurname());
        row.createCell(10).setCellValue(workInfo.getEmployeeName());
    }

    private void addMissedRow(Row row, WorkInfo workInfo) {

        row.createCell(0).setCellValue(workInfo.getDate() != null ? workInfo.getDate().toString() : null);
        row.createCell(1).setCellValue(workInfo.getDepartmentName());
        row.createCell(2).setCellValue(workInfo.getEmployeeSurname());
        row.createCell(3).setCellValue(workInfo.getEmployeeName());
    }

    private void addPartialRow(Row row, WorkInfo workInfo) {

        row.createCell(0).setCellValue(workInfo.getDuration().toString());
        row.createCell(1).setCellValue(workInfo.getDate() != null ? workInfo.getDate().toString() : null);
        row.createCell(2).setCellValue(workInfo.getEmployeeSurname());
        row.createCell(3).setCellValue(workInfo.getEmployeeName());
    }
}
