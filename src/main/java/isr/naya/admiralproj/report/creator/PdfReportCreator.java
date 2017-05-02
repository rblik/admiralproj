package isr.naya.admiralproj.report.creator;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import isr.naya.admiralproj.dto.WorkInfo;
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

import static isr.naya.admiralproj.report.ReportCreator.durationToTimeString;
import static isr.naya.admiralproj.report.ReportType.*;

@Component
@Slf4j
@Pdf
public class PdfReportCreator implements ReportCreator {

    @Override
    public byte[] create(@NonNull List<WorkInfo> infoList, @NonNull ReportType reportType) {

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
            Document document = new Document(orientation);
            List<List<WorkInfo>> partition = Lists.partition(infoList, partitionSize);
            os = new ByteArrayOutputStream();

            PdfWriter writer = PdfWriter.getInstance(document, os);
            document.open();
            //            fonts
            BaseFont bf = BaseFont.createFont("DejaVuSans.ttf", BaseFont.IDENTITY_H, true);
            //            render
            for (List<WorkInfo> infos : partition) {
                ColumnText column = createColumn(writer.getDirectContent(), reportType);
                column.addElement(createTitle(bf, reportType));
                column.addElement(createImage(reportType));
                column.addElement(createTable(infos, bf, reportType));
                //            close
                column.go();
                document.newPage();
            }
            document.close();
        } catch (Exception e) {
            log.error("An error occurred while creating report. {}", e.getLocalizedMessage());
            throw new ExceptionConverter(e);
        }
        return os.toByteArray();

    }

    private PdfPTable createTable(List<WorkInfo> infoList, BaseFont bf, ReportType reportType) throws DocumentException {

        int colNumber = (PIVOTAL == reportType) ? 12 : (INDIVIDUAL_EMPTY == reportType) ? 2 : (PARTIAL == reportType) ? 6 : 5;
        PdfPTable table = new PdfPTable(colNumber);
        float cols[] = new float[0];

        if (PIVOTAL == reportType) {
            cols = new float[]{180, 50, 30, 30, 40, 20, 70, 120, 50, 80, 100, 80};
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
            ImmutableList.of("תאור", "משך", "עד-", "מ-", "חופשה", "יום", "תעריך", "עובד", "מ''ע", "צוות", "פרויקט", "לקוח")
                    .forEach(s -> table.addCell(createCell(s, font10, true)));
            infoList.forEach(workInfo -> addFullRow(table, workInfo, font8));
        } else if (PARTIAL == reportType) {
            ImmutableList.of("משך", "יום", "תעריך", "צוות", "שם משפחה", "שם")
                    .forEach(s -> table.addCell(createCell(s, font10, true)));
            infoList.forEach(workInfo -> addPartialRow(table, workInfo, font8));
        } else if (EMPTY == reportType) {
            ImmutableList.of("יום", "תעריך", "צוות", "שם משפחה", "שם")
                    .forEach(s -> table.addCell(createCell(s, font10, true)));
            infoList.forEach(workInfo -> addMissedRow(table, workInfo, font8));
        } else if (INDIVIDUAL_EMPTY == reportType) {
            ImmutableList.of("יום", "תעריך")
                    .forEach(s -> table.addCell(createCell(s, font10, true)));
            infoList.forEach(workInfo -> addIndividualMissedRow(table, workInfo, font8));
        } else {
            log.error("Not compatible report type");
        }
    }

    private void addFullRow(PdfPTable table, WorkInfo workInfo, Font font) {

        table.addCell(createCell(workInfo.getComment(), font));
        table.addCell(createCell(durationToTimeString(workInfo.getDuration()), font));
        table.addCell(createCell(workInfo.getTo() != null ? workInfo.getTo().truncatedTo(ChronoUnit.MINUTES).toString() : null, font));
        table.addCell(createCell(workInfo.getFrom() != null ? workInfo.getFrom().truncatedTo(ChronoUnit.MINUTES).toString() : null, font));
        table.addCell(createCell(workInfo.getAbsenceType() != null ? workInfo.getAbsenceType().getTranslation() : null, font));
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

    private PdfPCell createCell(String description, Font font) {
        return createCell(description, font, false);
    }

    private void addIndividualMissedRow(PdfPTable table, WorkInfo workInfo, Font font) {
        table.addCell(createCell(workInfo.getDate() != null ? MappingUtil.getDay(workInfo.getDate().getDayOfWeek().getValue()) : null, font));
        table.addCell(createCell(workInfo.getDate() != null ? workInfo.getDate().toString() : null, font));
    }

    private PdfPCell createCell(String description, Font font, boolean isHeader) {
        PdfPCell cell;
        try {
            cell = new PdfPCell(font == null ? new Phrase(description, new Font(BaseFont.createFont(BaseFont.COURIER, "utf-8", true), 7)) : new Phrase(description, font));
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

    private Paragraph createTitle(BaseFont bf, ReportType reportType) {

        String title = "";
        if (PIVOTAL == reportType)
            title = "שעות גולמי";
        else if (PARTIAL == reportType)
            title = "ימים חלקיים";
        else if (EMPTY == reportType)
            title = "ימים חסרים";
        else if (INDIVIDUAL_EMPTY == reportType)
            title = "ימים חסרים";
        else
            log.error("Not compatible report type");

        Paragraph paragraph = new Paragraph(title, new Font(bf, 14));
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