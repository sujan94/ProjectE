package sample.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import sample.model.Report;
import sample.model.ScanReport;
import sample.scenes.ApplicationConstant;

import java.io.IOException;

public class PdfReportGenerator {
    public static final String DEST = "./output/";
    public static final String EXTENSION = ".pdf";

    public static void generatePDF(Report report) {
        PDDocument document = null;
        String filename = DEST + report.getEmployee().getFname() + EXTENSION;

        PDDocument doc = null;
        doc = new PDDocument();
        try {
            PDPage page = new PDPage();
            doc.addPage(page);
            PDFont font = PDType1Font.HELVETICA_BOLD;
            PDPageContentStream contents = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.OVERWRITE, true);
            contents.beginText();
            contents.newLineAtOffset(10, 750);
            contents.newLine();
            contents.setFont(font, 20);
            contents.showText(ApplicationConstant.APPLICATION_TITLE);
            font = PDType1Font.HELVETICA;
            contents.setFont(font, 14);
            contents.newLineAtOffset(40, -50);
            for (String s : report.toString().split("\n")) {
                contents.showText(s);
                contents.newLine();
                contents.newLineAtOffset(0, -20);
            }
            contents.endText();
            contents.close();
            doc.save(filename);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                doc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void generatePDF(ScanReport report) {
        PDDocument document = null;
        String filename = DEST + report.getScanTimeStamp() + EXTENSION;

        PDDocument doc = null;
        doc = new PDDocument();
        try {
            PDPage page = new PDPage();
            doc.addPage(page);
            PDFont font = PDType1Font.HELVETICA_BOLD;
            PDPageContentStream contents = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.PREPEND, true);
            contents.beginText();
            contents.newLineAtOffset(10, 750);
            contents.newLine();
            contents.setFont(font, 20);
            contents.showText(ApplicationConstant.APPLICATION_TITLE);
            font = PDType1Font.HELVETICA;
            contents.setFont(font, 14);
            contents.newLineAtOffset(40, -50);
            for (String s : report.toString().split("\n")) {
                contents.showText(s);
                contents.newLine();
                contents.newLineAtOffset(0, -20);
            }
            contents.endText();
            contents.close();
            doc.save(filename);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                doc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
