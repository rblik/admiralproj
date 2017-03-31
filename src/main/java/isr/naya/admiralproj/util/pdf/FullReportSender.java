package isr.naya.admiralproj.util.pdf;

import com.google.common.collect.ImmutableList;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import isr.naya.admiralproj.dto.WorkInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Component
public class FullReportSender {

    public void send(List<WorkInfo> infoList) {
        try {
            Document document = new Document(PageSize.A4);
            String filename = "reports/Hours Report.pdf";
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.open();
//            fonts
            BaseFont bf = BaseFont.createFont("NotoSansHebrew-Regular.ttf", BaseFont.IDENTITY_H, true);
//            render
            ColumnText column = createColumn(writer.getDirectContent());
            column.addElement(createTitle(bf));
            column.addElement(createImage());
            column.addElement(createTable(infoList, bf));
//            close
            column.go();
            document.close();
        } catch (Exception e) {
            log.error("An error occurred while creating report");
            throw new RuntimeException(e);
        }

    }

    private PdfPTable createTable(List<WorkInfo> infoList, BaseFont bf) throws DocumentException {
        PdfPTable table = new PdfPTable(11);
        table.setTotalWidth(new float[]{50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50});
        table.setLockedWidth(true);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
//        header
        ImmutableList<String> cellNames = ImmutableList.of("תאור", "משך", "עד-", "מ-", "חופשה", "תעריך", "לקוח", "פרויקט", "צוות", "שם משפחה", "שם");
        for (String s : cellNames) {
            table.addCell(createCell(s, new Font(bf, 8)));
        }
//        body
        infoList.forEach(workInfo -> {
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
        });

        return table;
    }

    private PdfPCell createCell(String description) {
        return createCell(description, null);
    }

    private PdfPCell createCell(String description, Font font) {
        PdfPCell cell;
        try {
            cell = new PdfPCell(font == null ? new Phrase(description, new Font(BaseFont.createFont(BaseFont.COURIER, "utf-8", true), 6)) : new Phrase(description, font));
            cell.setFixedHeight(15);
            cell.setColspan(1);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            cell.setBorder(Rectangle.NO_BORDER);
        } catch (Exception e) {
            log.error("An error occurred while creating cell");
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
        image.scaleAbsolute(100, 100);
        image.setAlignment(Element.ALIGN_CENTER);
        image.setSpacingAfter(25);
        return image;
    }
}