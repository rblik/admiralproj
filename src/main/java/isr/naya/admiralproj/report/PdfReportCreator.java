package isr.naya.admiralproj.report;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import isr.naya.admiralproj.dto.WorkInfo;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.IntStream;

import static isr.naya.admiralproj.report.ReportType.*;

@Component
@Slf4j
public class PdfReportCreator implements ReportCreator {

    @Override
    public byte[] create(@NonNull List<WorkInfo> infoList, @NonNull ReportType reportType) {
        ByteArrayOutputStream os;
        try {
            List<List<WorkInfo>> partition = Lists.partition(infoList, 35);
            os = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(document, os);
            document.open();
//            fonts
            BaseFont bf = BaseFont.createFont("NotoSansHebrew-Regular.ttf", BaseFont.IDENTITY_H, true);
//            render
            for (List<WorkInfo> infos : partition) {
                ColumnText column = createColumn(writer.getDirectContent());
                column.addElement(createTitle(bf));
                column.addElement(createImage());
                column.addElement(createTable(infos, bf, reportType));
//            close
                column.go();
                document.newPage();
            }
            document.close();
        } catch (Exception e) {
            log.error("An error occurred while creating report. {}", e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
        return os.toByteArray();

    }

    private PdfPTable createTable(List<WorkInfo> infoList, BaseFont bf, ReportType reportType) throws DocumentException {
        int colNumber = (PIVOTAL == reportType) ? 11 : 4;
        PdfPTable table = new PdfPTable(colNumber);
        int[] doubles = IntStream.generate(() -> 50).limit(colNumber).toArray();
        table.setTotalWidth(Floats.toArray(Ints.asList(doubles)));
        table.setLockedWidth(true);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        populate(infoList, bf, table, reportType);

        return table;
    }

    private void populate(List<WorkInfo> infoList, BaseFont bf, PdfPTable table, ReportType reportType) {
        Font font = new Font(bf, 10);
        if (PIVOTAL == reportType) {
            ImmutableList.of("תאור", "משך", "עד-", "מ-", "חופשה", "תעריך", "לקוח", "פרויקט", "צוות", "שם משפחה", "שם")
                    .forEach(s -> table.addCell(createCell(s, font, true)));
            infoList.forEach(workInfo -> addFullRow(table, workInfo));
        } else if (PARTIAL == reportType) {
            ImmutableList.of("משך", "תעריך", "שם משפחה", "שם")
                    .forEach(s -> table.addCell(createCell(s, font, true)));
            infoList.forEach(workInfo -> addPartialRow(table, workInfo));
        } else if (EMPTY == reportType) {
            ImmutableList.of("תעריך", "צוות", "שם משפחה", "שם")
                    .forEach(s -> table.addCell(createCell(s, font, true)));
            infoList.forEach(workInfo -> addMissedRow(table, workInfo));
        } else {
            log.error("Not compatible report type");
        }
    }

    private void addFullRow(PdfPTable table, WorkInfo workInfo) {
        table.addCell(createCell(workInfo.getComment()));
        table.addCell(createCell(workInfo.getDuration().toString()));
        table.addCell(createCell(workInfo.getTo() != null ? workInfo.getTo().truncatedTo(ChronoUnit.MINUTES).toString() : null));
        table.addCell(createCell(workInfo.getFrom() != null ? workInfo.getFrom().truncatedTo(ChronoUnit.MINUTES).toString() : null));
        table.addCell(createCell(workInfo.getAbsenceType() != null ? workInfo.getAbsenceType().toString() : null));
        table.addCell(createCell(workInfo.getDate() != null ? workInfo.getDate().toString() : null));
        table.addCell(createCell(workInfo.getClientName()));
        table.addCell(createCell(workInfo.getProjectName()));
        table.addCell(createCell(workInfo.getDepartmentName()));
        table.addCell(createCell(workInfo.getEmployeeSurname()));
        table.addCell(createCell(workInfo.getEmployeeName()));
    }

    private void addMissedRow(PdfPTable table, WorkInfo workInfo) {
        table.addCell(createCell(workInfo.getDate() != null ? workInfo.getDate().toString() : null));
        table.addCell(createCell(workInfo.getDepartmentName()));
        table.addCell(createCell(workInfo.getEmployeeSurname()));
        table.addCell(createCell(workInfo.getEmployeeName()));
    }

    private void addPartialRow(PdfPTable table, WorkInfo workInfo) {
        table.addCell(createCell(workInfo.getDuration().toString()));
        table.addCell(createCell(workInfo.getDate() != null ? workInfo.getDate().toString() : null));
        table.addCell(createCell(workInfo.getEmployeeSurname()));
        table.addCell(createCell(workInfo.getEmployeeName()));
    }

    private PdfPCell createCell(String description) {
        return createCell(description, null, false);
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

    private ColumnText createColumn(PdfContentByte content) {
        ColumnText column = new ColumnText(content);
        column.setSimpleColumn(36, 770, 569, 36);
        column.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        return column;
    }

    private Paragraph createTitle(BaseFont bf) {
        Paragraph paragraph = new Paragraph("שעות גולמי", new Font(bf, 14));
        paragraph.setAlignment(Element.ALIGN_CENTER);
        return paragraph;
    }

    private Image createImage() throws BadElementException, IOException {
        Image image = Image.getInstance("reports/pic.png");
        image.scaleAbsolute(50, 50);
        image.setAlignment(Element.ALIGN_CENTER);
        image.setSpacingAfter(25);
        return image;
    }
}