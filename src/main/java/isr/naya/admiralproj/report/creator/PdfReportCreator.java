package isr.naya.admiralproj.report.creator;

import com.google.common.collect.ImmutableList;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import isr.naya.admiralproj.dto.AgreementDto;
import isr.naya.admiralproj.dto.ReportFile;
import isr.naya.admiralproj.dto.WorkInfo;
import isr.naya.admiralproj.model.WorkUnit;
import isr.naya.admiralproj.report.ReportCreator;
import isr.naya.admiralproj.report.ReportType;
import isr.naya.admiralproj.report.annotations.Pdf;
import isr.naya.admiralproj.util.MappingUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.google.common.collect.Lists.partition;
import static isr.naya.admiralproj.dto.ReportFileType.PDF;
import static isr.naya.admiralproj.report.ReportCreator.*;
import static isr.naya.admiralproj.report.ReportType.*;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

@Component
@Slf4j
@Pdf
public class PdfReportCreator implements ReportCreator {

    @Override
    public ReportFile create(@NonNull List<WorkInfo> infoList, @NonNull ReportType reportType, String employeeTitle) {

        ByteArrayOutputStream os;
        try {
            Rectangle orientation;
            int partitionSize;
            if (PIVOTAL == reportType) {
                orientation = PageSize.A4.rotate();
                partitionSize = 26;
            } else {
                orientation = PageSize.A4;
                partitionSize = 35;
            }
            os = new ByteArrayOutputStream();
            //            fonts
            BaseFont bf = BaseFont.createFont(DEJA_VU_SANS, BaseFont.IDENTITY_H, true);
            Document document = new Document(orientation);
            PdfWriter writer = PdfWriter.getInstance(document, os);
            document.open();
            List<List<WorkInfo>> partition = infoList.size() == 0 ? singletonList(emptyList()) : partition(infoList, partitionSize);
            //            render
            for (List<WorkInfo> infos : partition) {
                ColumnText column = createColumn(writer.getDirectContent(), reportType);
                column.addElement(createTitle(bf, reportType, 14, EMPTY_STR));
                column.addElement(createImage(reportType));
                if (INDIVIDUAL_EMPTY == reportType || INDIVIDUAL_PIVOTAL == reportType)
                    column.addElement(createTitle(bf, reportType, 10, employeeTitle));
                column.addElement(createTable(infos, bf, reportType));
                //            close
                column.go();
                document.newPage();
            }
            document.close();
            os.close();
        } catch (Exception e) {
            log.error("An error occurred while creating report. {}", e.getLocalizedMessage());
            throw new ExceptionConverter(e);
        }
        return new ReportFile(PDF, os.toByteArray());

    }

    @Override
    public ReportFile generateTemplate(List<AgreementDto> agreements, List<WorkInfo> infos) {
        return ReportFile.empty(PDF);
    }

    @Override
    public List<WorkUnit> readFromFile(ReportFile file, Integer year, Integer month) {
        return null;
    }

    private PdfPTable createTable(List<WorkInfo> infoList, BaseFont bf, ReportType reportType) throws DocumentException {

        int colNumber = (PIVOTAL == reportType) ? 11 : (INDIVIDUAL_PIVOTAL == reportType) ? 8 : (INCOME == reportType) ? 7 : (INDIVIDUAL_EMPTY == reportType) ? 2 : (PARTIAL == reportType) ? 6 : 5;
        PdfPTable table = new PdfPTable(colNumber);
        float cols[] = new float[0];

        if (PIVOTAL == reportType) {
            cols = new float[]{180, 50, 30, 30, 20, 70, 120, 50, 80, 100, 80};
        } else if (INDIVIDUAL_PIVOTAL == reportType) {
            cols = new float[]{180, 50, 30, 30, 20, 70, 100, 80};
        } else if (INCOME == reportType) {
            cols = new float[]{60, 50, 80, 120, 70, 100, 80};
        } else if (PARTIAL == reportType) {
            cols = new float[]{50, 20, 70, 120, 120, 120};
        } else if (EMPTY == reportType) {
            cols = new float[]{20, 70, 50, 120, 120};
        } else if (INDIVIDUAL_EMPTY == reportType) {
            cols = new float[]{20, 100};
        }

        table.setTotalWidth(cols);
        table.setLockedWidth(true);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        populate(infoList, bf, table, reportType);

        return table;
    }

    private void populate(List<WorkInfo> infoList, BaseFont bf, PdfPTable table, ReportType reportType) {
        Font font10 = new Font(bf, 10);
        Font font8 = new Font(bf, 8);

        if (PIVOTAL == reportType) {
            ImmutableList.of(DESCRIPTION, DURATION, UNTIL, SINCE, DAY, DATE, EMPLOYEE, EMPL_NUMBER, DEPARTMENT, PROJECT, CLIENT)
                    .forEach(s -> table.addCell(createCell(s, font10, true)));
            infoList.forEach(workInfo -> addFullRow(table, workInfo, font8));
        } else {
            if (INDIVIDUAL_PIVOTAL == reportType) {
                ImmutableList.of(DESCRIPTION, DURATION, UNTIL, SINCE, DAY, DATE, PROJECT, CLIENT)
                        .forEach(s -> table.addCell(createCell(s, font10, true)));
                infoList.forEach(workInfo -> addIndividualFullRow(table, workInfo, font8));
            } else if (INCOME == reportType) {
                ImmutableList.of(TOTAL, DURATION, TARIFF, EMPLOYEE, DEPARTMENT, PROJECT, CLIENT)
                        .forEach(s -> table.addCell(createCell(s, font10, true)));
                infoList.forEach(workInfo -> addIncomeRow(table, workInfo, font8));
            } else if (PARTIAL == reportType) {
                ImmutableList.of(DURATION, DAY, DATE, DEPARTMENT, EMPL_SURNAME, EMPL_NAME)
                        .forEach(s -> table.addCell(createCell(s, font10, true)));
                infoList.forEach(workInfo -> addPartialRow(table, workInfo, font8));
            } else if (EMPTY == reportType) {
                ImmutableList.of(DAY, DATE, DEPARTMENT, EMPL_SURNAME, EMPL_NAME)
                        .forEach(s -> table.addCell(createCell(s, font10, true)));
                infoList.forEach(workInfo -> addMissedRow(table, workInfo, font8));
            } else if (INDIVIDUAL_EMPTY == reportType) {
                ImmutableList.of(DAY, DATE)
                        .forEach(s -> table.addCell(createCell(s, font10, true)));
                infoList.forEach(workInfo -> addIndividualMissedRow(table, workInfo, font8));
            } else {
                log.error("Not compatible report type");
            }
        }
    }

    private void addIncomeRow(PdfPTable table, WorkInfo workInfo, Font font) {
        table.addCell(createCell(calculateIncome(workInfo.getAmount(), workInfo.getType(), workInfo.getDuration()) + ' ' + getCurrencySign(workInfo.getCurrency()), font));
        table.addCell(createCell(durationToTimeString(workInfo.getDuration()), font));
        table.addCell(createCell(
                ((workInfo.getAmount()) != null ? workInfo.getAmount().toString() : "") + ' ' +
                        getCurrencySign(workInfo.getCurrency()) + ' ' +
                        ((workInfo.getType() != null) ? workInfo.getType().getName() : ""), font));
        table.addCell(createCell(workInfo.getEmployeeSurname() + ' ' + workInfo.getEmployeeName(), font));
        table.addCell(createCell(workInfo.getDepartmentName(), font));
        table.addCell(createCell(workInfo.getProjectName(), font));
        table.addCell(createCell(workInfo.getClientName(), font));
    }

    private void addFullRow(PdfPTable table, WorkInfo workInfo, Font font) {

        table.addCell(createCell(workInfo.getComment(), font));
        table.addCell(createCell(durationToTimeString(workInfo.getDuration()), font));
        table.addCell(createCell(workInfo.getTo() != null ? workInfo.getTo().truncatedTo(ChronoUnit.MINUTES).toString() : null, font));
        table.addCell(createCell(workInfo.getFrom() != null ? workInfo.getFrom().truncatedTo(ChronoUnit.MINUTES).toString() : null, font));
        table.addCell(createCell(workInfo.getDate() != null ? MappingUtil.getDay(workInfo.getDate().getDayOfWeek().getValue()) : null, font));
        table.addCell(createCell(workInfo.getDate() != null ? workInfo.getDate().toString() : null, font));
        table.addCell(createCell(workInfo.getEmployeeSurname() + ' ' + workInfo.getEmployeeName(), font));
        table.addCell(createCell(workInfo.getEmployeeNumber(), font));
        table.addCell(createCell(workInfo.getDepartmentName(), font));
        table.addCell(createCell(workInfo.getProjectName(), font));
        table.addCell(createCell(workInfo.getClientName(), font));
    }

    private void addMissedRow(PdfPTable table, WorkInfo workInfo, Font font) {
        table.addCell(createCell(workInfo.getDate() != null ? MappingUtil.getDay(workInfo.getDate().getDayOfWeek().getValue()) : null, font));
        table.addCell(createCell(workInfo.getDate() != null ? workInfo.getDate().toString() : null, font));
        table.addCell(createCell(workInfo.getDepartmentName(), font));
        table.addCell(createCell(workInfo.getEmployeeSurname(), font));
        table.addCell(createCell(workInfo.getEmployeeName(), font));
    }

    private void addPartialRow(PdfPTable table, WorkInfo workInfo, Font font) {
        table.addCell(createCell(durationToTimeString(workInfo.getDuration()), font));
        table.addCell(createCell(workInfo.getDate() != null ? MappingUtil.getDay(workInfo.getDate().getDayOfWeek().getValue()) : null, font));
        table.addCell(createCell(workInfo.getDate() != null ? workInfo.getDate().toString() : null, font));
        table.addCell(createCell(workInfo.getDepartmentName(), font));
        table.addCell(createCell(workInfo.getEmployeeSurname(), font));
        table.addCell(createCell(workInfo.getEmployeeName(), font));
    }

    private void addIndividualMissedRow(PdfPTable table, WorkInfo workInfo, Font font) {
        table.addCell(createCell(workInfo.getDate() != null ? MappingUtil.getDay(workInfo.getDate().getDayOfWeek().getValue()) : null, font));
        table.addCell(createCell(workInfo.getDate() != null ? workInfo.getDate().toString() : null, font));
    }

    private void addIndividualFullRow(PdfPTable table, WorkInfo workInfo, Font font) {
        table.addCell(createCell(workInfo.getComment(), font));
        table.addCell(createCell(durationToTimeString(workInfo.getDuration()), font));
        table.addCell(createCell(workInfo.getTo() != null ? workInfo.getTo().truncatedTo(ChronoUnit.MINUTES).toString() : null, font));
        table.addCell(createCell(workInfo.getFrom() != null ? workInfo.getFrom().truncatedTo(ChronoUnit.MINUTES).toString() : null, font));
        table.addCell(createCell(workInfo.getDate() != null ? MappingUtil.getDay(workInfo.getDate().getDayOfWeek().getValue()) : null, font));
        table.addCell(createCell(workInfo.getDate() != null ? workInfo.getDate().toString() : null, font));
        table.addCell(createCell(workInfo.getProjectName(), font));
        table.addCell(createCell(workInfo.getClientName(), font));
    }

    private PdfPCell createCell(String description, Font font) {
        return createCell(description, font, false);
    }

    private PdfPCell createCell(String description, Font font, boolean isHeader) {
        PdfPCell cell;
        try {
            cell = new PdfPCell(font == null ? new Phrase(description, new Font(BaseFont.createFont(BaseFont.COURIER, UTF8, true), 7)) : new Phrase(description, font));
            cell.setFixedHeight(isHeader ? 25 : 15);
            cell.setColspan(1);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            cell.setBorder(Rectangle.NO_BORDER);
        } catch (Exception e) {
            log.error("An error occurred while creating cell. {}", e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
        return cell;
    }

    private ColumnText createColumn(PdfContentByte content, ReportType reportType) {
        ColumnText column = new ColumnText(content);
        if (PIVOTAL == reportType)
            column.setSimpleColumn(36, 569, 770, 36);
        else
            column.setSimpleColumn(36, 770, 569, 36);
        column.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        return column;
    }

    private Paragraph createTitle(BaseFont bf, ReportType reportType, int fontSize, String employeeTitle) {

        String title = EMPTY_STR;
        if (PIVOTAL == reportType)
            title = "שעות גולמי";
        else if (INDIVIDUAL_PIVOTAL == reportType)
            title = employeeTitle.isEmpty() ? "שעות בשבוע" : employeeTitle;
        else if (INCOME == reportType)
            title = "דוח הכנסות";
        else if (PARTIAL == reportType)
            title = "ימים חלקיים";
        else if (EMPTY == reportType)
            title = "ימים חסרים";
        else if (INDIVIDUAL_EMPTY == reportType)
            title = employeeTitle.isEmpty() ? "ימים חסרים" : employeeTitle;
        else
            log.error("Not compatible report type");

        Paragraph paragraph = new Paragraph(title, new Font(bf, fontSize));
        paragraph.setAlignment(Element.ALIGN_CENTER);
        return paragraph;
    }

    private Image createImage(ReportType reportType) throws BadElementException, IOException {
        Image image = Image.getInstance("reports/pic.png");
        image.scaleAbsolute(50, 50);
        image.setAlignment(Element.ALIGN_CENTER);
        image.setSpacingAfter(25);
        return image;
    }
}