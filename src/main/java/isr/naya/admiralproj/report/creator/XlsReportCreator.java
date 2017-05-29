package isr.naya.admiralproj.report.creator;


import isr.naya.admiralproj.dto.WorkInfo;
import isr.naya.admiralproj.report.ReportCreator;
import isr.naya.admiralproj.report.ReportType;
import isr.naya.admiralproj.report.annotations.Xlsx;
import isr.naya.admiralproj.util.MappingUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static isr.naya.admiralproj.report.ReportCreator.*;
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
            if (PIVOTAL == reportType) addFullRow(row, data);
            else if (INDIVIDUAL_PIVOTAL == reportType) addIndividualFullRow(row, data);
            else if (INCOME == reportType) addIncomeRow(row, data);
            else if (PARTIAL == reportType) addPartialRow(row, data);
            else if (EMPTY == reportType) addMissedRow(row, data);
            else log.error("Not compatible report type");
            for (int i = 0; i < sheet.getRow(0).getLastCellNum(); i++) {
                sheet.setColumnWidth(i, 4000);

            }
            sheet.getCTWorksheet().getSheetViews().getSheetViewArray(0).setRightToLeft(true);
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
            titles = Arrays.asList(CLIENT, PROJECT, DEPARTMENT, EMPL_NUMBER, EMPLOYEE, DATE, DAY, SINCE, UNTIL, DURATION, DESCRIPTION);
        } else if (INDIVIDUAL_PIVOTAL == reportType) {
            titles = Arrays.asList(CLIENT, PROJECT, DATE, DAY, SINCE, UNTIL, DURATION, DESCRIPTION);
        } else if (INCOME == reportType) {
            titles = Arrays.asList(CLIENT, PROJECT, DEPARTMENT, EMPLOYEE, AMOUNT, TARIFF, DURATION, TOTAL);
        } else if (PARTIAL == reportType) {
            titles = Arrays.asList(EMPL_NAME, EMPL_SURNAME, DEPARTMENT, DATE, DAY, DURATION);
        } else if (EMPTY == reportType) {
            titles = Arrays.asList(EMPL_NAME, EMPL_SURNAME, DEPARTMENT, DATE, DAY);
        } else {
            titles = Collections.emptyList();
            log.error("Not compatible report type");
        }
        for (int i = 0; i < titles.size(); i++)
            row.createCell(i).setCellValue(titles.get(i));
        alignCenter(row);
    }

    private void addIncomeRow(Row row, WorkInfo workInfo) {
        row.createCell(0).setCellValue(workInfo.getClientName());
        row.createCell(1).setCellValue(workInfo.getProjectName());
        row.createCell(2).setCellValue(workInfo.getDepartmentName());
        row.createCell(3).setCellValue(workInfo.getEmployeeSurname() + ' ' + workInfo.getEmployeeName());
        Cell cell4 = row.createCell(4);
        cell4.setCellValue(((workInfo.getAmount()) != null ? workInfo.getAmount() : 0));
        cell4.setCellType(CellType.NUMERIC);
        row.createCell(5).setCellValue(getCurrencySign(workInfo.getCurrency()) + ", " +
                ((workInfo.getType() != null) ? workInfo.getType().getName() : ""));
        Cell cell6 = row.createCell(6);
        cell6.setCellFormula(workInfo.getDuration() + "/60.0");
        Cell cell7 = row.createCell(7);
        cell7.setCellValue(Double.valueOf(calculateIncome(workInfo.getAmount(), workInfo.getType(), workInfo.getDuration())));
        cell7.setCellType(CellType.NUMERIC);
        alignCenter(row);
    }

    private void addFullRow(Row row, WorkInfo workInfo) {

        row.createCell(0).setCellValue(workInfo.getClientName());
        row.createCell(1).setCellValue(workInfo.getProjectName());
        row.createCell(2).setCellValue(workInfo.getDepartmentName());
        row.createCell(3).setCellValue(workInfo.getEmployeeNumber());
        row.createCell(4).setCellValue(workInfo.getEmployeeSurname() + ' ' + workInfo.getEmployeeName());
        row.createCell(5).setCellValue(workInfo.getDate() != null ? workInfo.getDate().toString() : null);
        row.createCell(6).setCellValue(workInfo.getDate() != null ? MappingUtil.getDay(workInfo.getDate().getDayOfWeek().getValue()) : null);
        row.createCell(7).setCellValue(workInfo.getFrom() != null ? workInfo.getFrom().truncatedTo(ChronoUnit.MINUTES).toString() : null);
        row.createCell(8).setCellValue(workInfo.getTo() != null ? workInfo.getTo().truncatedTo(ChronoUnit.MINUTES).toString() : null);
        row.createCell(9).setCellValue(durationToTimeString(workInfo.getDuration()));
        row.createCell(10).setCellValue(workInfo.getComment());
        alignCenter(row);
    }

    private void addIndividualFullRow(Row row, WorkInfo workInfo) {

        row.createCell(0).setCellValue(workInfo.getClientName());
        row.createCell(1).setCellValue(workInfo.getProjectName());
        row.createCell(2).setCellValue(workInfo.getDate() != null ? workInfo.getDate().toString() : null);
        row.createCell(3).setCellValue(workInfo.getDate() != null ? MappingUtil.getDay(workInfo.getDate().getDayOfWeek().getValue()) : null);
        row.createCell(4).setCellValue(workInfo.getFrom() != null ? workInfo.getFrom().truncatedTo(ChronoUnit.MINUTES).toString() : null);
        row.createCell(5).setCellValue(workInfo.getTo() != null ? workInfo.getTo().truncatedTo(ChronoUnit.MINUTES).toString() : null);
        row.createCell(6).setCellValue(durationToTimeString(workInfo.getDuration()));
        row.createCell(7).setCellValue(workInfo.getComment());
        alignCenter(row);
    }

    private void addMissedRow(Row row, WorkInfo workInfo) {

        row.createCell(0).setCellValue(workInfo.getEmployeeName());
        row.createCell(1).setCellValue(workInfo.getEmployeeSurname());
        row.createCell(2).setCellValue(workInfo.getDepartmentName());
        row.createCell(3).setCellValue(workInfo.getDate() != null ? workInfo.getDate().toString() : null);
        row.createCell(4).setCellValue(workInfo.getDate() != null ? MappingUtil.getDay(workInfo.getDate().getDayOfWeek().getValue()) : null);
        alignCenter(row);
    }

    private void addPartialRow(Row row, WorkInfo workInfo) {

        row.createCell(0).setCellValue(workInfo.getEmployeeName());
        row.createCell(1).setCellValue(workInfo.getEmployeeSurname());
        row.createCell(2).setCellValue(workInfo.getDepartmentName());
        row.createCell(3).setCellValue(workInfo.getDate() != null ? workInfo.getDate().toString() : null);
        row.createCell(4).setCellValue(workInfo.getDate() != null ? MappingUtil.getDay(workInfo.getDate().getDayOfWeek().getValue()) : null);
        row.createCell(5).setCellValue(durationToTimeString(workInfo.getDuration()));
        alignCenter(row);
    }

    private void alignCenter(Row row) {
        CellStyle cellStyle = row.getSheet().getWorkbook().createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        row.cellIterator().forEachRemaining(cell -> cell.setCellStyle(cellStyle));
    }
}
