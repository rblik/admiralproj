package isr.naya.admiralproj.report.creator;


import isr.naya.admiralproj.dto.AgreementDto;
import isr.naya.admiralproj.dto.ReportFile;
import isr.naya.admiralproj.dto.WorkInfo;
import isr.naya.admiralproj.exception.IllegalCellFormatException;
import isr.naya.admiralproj.model.WorkUnit;
import isr.naya.admiralproj.report.ReportCreator;
import isr.naya.admiralproj.report.ReportType;
import isr.naya.admiralproj.report.annotations.Xlsx;
import isr.naya.admiralproj.repository.WorkAgreementRepository;
import isr.naya.admiralproj.util.MappingUtil;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static isr.naya.admiralproj.dto.ReportFileType.XLS;
import static isr.naya.admiralproj.report.ReportCreator.*;
import static isr.naya.admiralproj.report.ReportType.*;
import static org.apache.poi.ss.util.WorkbookUtil.createSafeSheetName;

@Component
@Slf4j
@Xlsx
public class XlsReportCreator implements ReportCreator {

    private final WorkAgreementRepository workAgreementRepository;

    @Autowired
    public XlsReportCreator(WorkAgreementRepository workAgreementRepository) {
        this.workAgreementRepository = workAgreementRepository;
    }

    @Override
    public ReportFile create(@NonNull List<WorkInfo> infoList, @NonNull ReportType reportType, String employeeTitle) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(createSafeSheetName(reportType.name() + LocalDate.now().toString()));

        Row row = sheet.createRow(0);

        sheet.getCTWorksheet().getSheetViews().getSheetViewArray(0).setRightToLeft(true);
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
        }

        try {
            workbook.write(os);
            workbook.close();
            os.close();
        } catch (Exception e) {
            log.error("An error occurred while creating report. {}", e.getLocalizedMessage());
            throw new RuntimeException(e);
        }

        return new ReportFile(XLS, os.toByteArray());
    }

    @Override
    public ReportFile generateTemplate(List<AgreementDto> agreements, List<WorkInfo> infos) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        XSSFWorkbook workbook = new XSSFWorkbook();

        // prepare format text to all cells
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFCreationHelper helper = workbook.getCreationHelper();
        cellStyle.setDataFormat(helper.createDataFormat().getFormat("@"));

        XSSFSheet sheet = workbook.createSheet(createSafeSheetName(TEMPLATE.name() + LocalDate.now().toString()));

        for (int i = 0; i < 5; i++) {
            sheet.setDefaultColumnStyle(i, cellStyle);
        }

        Row row = sheet.createRow(0);

        sheet.getCTWorksheet().getSheetViews().getSheetViewArray(0).setRightToLeft(true);

        populateTitle(row, TEMPLATE);

        populateHint(agreements, sheet);

        for (int i = 0; i < sheet.getRow(0).getLastCellNum(); i++) {
            sheet.setColumnWidth(i, 4000);
        }

        for (int i = 7; i < 10; i++) {
            sheet.setColumnWidth(i, 4000);
        }

        try {
            workbook.write(os);
            workbook.close();
            os.close();
        } catch (Exception e) {
            log.error("An error occurred while creating template. {}", e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
        return new ReportFile(XLS, os.toByteArray());
    }

    @Override
    @SneakyThrows
    public List<WorkUnit> readFromFile(ReportFile file, Integer year, Integer month) {
        byte[] content = file.getContent();
        List<WorkUnit> workUnits = new ArrayList<>();
        XSSFWorkbook workbook = new XSSFWorkbook(new ByteArrayInputStream(content));
        XSSFSheet sheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = sheet.iterator();
        iterator.next();
        List<String> brokenCellsList = new ArrayList<>();
        List<String> brokenRowsList = new ArrayList<>();
        AtomicInteger count = new AtomicInteger(1);
        iterator.forEachRemaining(row -> {
            Cell cell0 = row.getCell(0);
            Cell cell1 = row.getCell(1);
            Cell cell2 = row.getCell(2);
            Cell cell3 = row.getCell(3);
            Cell cell4 = row.getCell(4);

            if (cell0 != null && cell1 != null && cell2 != null && cell3 != null) {
                int agreementId = parseInt(cell0, brokenCellsList);//
                int dayOfMonth = parseInt(cell1, brokenCellsList);

                workUnits.add(new WorkUnit(LocalDate.of(year, month,
                        dayOfMonth),
                        parseTime(cell2, brokenCellsList),
                        parseTime(cell3, brokenCellsList),
                        null, cell4 == null ? "" : cell4.toString(),
                        workAgreementRepository.findOne(agreementId)));
                count.incrementAndGet();
            } else if (cell0 != null || cell1 != null || cell2 != null || cell3 != null) {
                int i = count.incrementAndGet();
                brokenRowsList.add(String.valueOf(i));
            } else {
                count.incrementAndGet();
            }
        });
        if (brokenCellsList.size() > 0 || brokenRowsList.size() > 0) {
            String brokenCellsSection = StringUtils.collectionToDelimitedString(brokenCellsList, ", ");
            String brokenRowsSection = StringUtils.collectionToDelimitedString(brokenRowsList, ", ");
            throw new IllegalCellFormatException(brokenCellsSection + ";" + brokenRowsSection);
        }
        return workUnits;
    }

    private int parseInt(Cell cell, List<String> list) {
        try {
            return Integer.parseInt(cell.toString().split("\\.")[0]);
        } catch (Throwable e) {
            list.add(cell.getAddress().formatAsString());
            return 0;
        }
    }

    private LocalTime parseTime(Cell cell, List<String> list) {
        try {
            return LocalTime.parse(cell.toString());
        } catch (Throwable e) {
            list.add(cell.getAddress().formatAsString());
            return null;
        }
    }

    private void populateTemplate(List<WorkInfo> infos, XSSFSheet sheet) {
        for (int i = 0; i < infos.size(); i++) {
            XSSFRow dataRow = sheet.createRow(i + 1);
            addTemplateRow(infos, i, dataRow);
        }
    }

    private void populateHint(List<AgreementDto> agreements, XSSFSheet sheet) {
        XSSFRow rowHeader = sheet.getRow(0);
        XSSFCell cell6 = rowHeader.createCell(6);
        cell6.setCellValue(CLIENT);
        XSSFCell cell7 = rowHeader.createCell(7);
        cell7.setCellValue(PROJECT);
        XSSFCell cell8 = rowHeader.createCell(8);
        cell8.setCellValue(PROJECT_ID);
        alignCenter(rowHeader, true);

        for (int i = 0; i < agreements.size(); i++) {
            XSSFRow rowBody = sheet.getRow(i + 1);
            if (rowBody == null) {
                rowBody = sheet.createRow(i + 1);
            }
            rowBody.createCell(6).setCellValue(agreements.get(i).getClientName());
            rowBody.createCell(7).setCellValue(agreements.get(i).getProjectName());
            rowBody.createCell(8).setCellValue(agreements.get(i).getAgreementId());
            alignCenter(rowBody, true);
        }
    }

    private void addTemplateRow(List<WorkInfo> infos, int i, XSSFRow dataRow) {
        dataRow.createCell(0).setCellValue(infos.get(i).getAgreementId());
        dataRow.createCell(1).setCellValue(infos.get(i).getDate().getDayOfMonth());
        dataRow.createCell(2).setCellValue(infos.get(i).getFrom().toString());
        dataRow.createCell(3).setCellValue(infos.get(i).getTo().toString());
        dataRow.createCell(4).setCellValue(infos.get(i).getComment());
        alignCenter(dataRow);
    }

    private void populateTitle(Row row, ReportType reportType) {

        List<String> titles;
        if (TEMPLATE == reportType) {
            titles = Arrays.asList(PROJECT_ID, DAY_OF_MONTH, SINCE, UNTIL, DESCRIPTION);
        } else if (PIVOTAL == reportType) {
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
        for (int i = 0; i < titles.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(titles.get(i));
        }
        alignCenter(row, true);
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
        this.alignCenter(row, false);
    }

    private void alignCenter(Row row, boolean isBold) {
        Workbook workbook = row.getSheet().getWorkbook();
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(isBold);
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        row.cellIterator().forEachRemaining(cell -> cell.setCellStyle(cellStyle));
    }
}
