package Utility;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

public class DocxUtility {

    public static void addFooterToDocument(XWPFDocument doc, String footerText) {
        XWPFHeaderFooterPolicy headerFooterPolicy = doc.getHeaderFooterPolicy();
        if (headerFooterPolicy != null) {
            XWPFFooter footer = headerFooterPolicy.createFooter(XWPFHeaderFooterPolicy.DEFAULT);
            XWPFParagraph paragraph = footer.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText(footerText);
        }
    }

    public static void addHeaderToDocument(XWPFDocument doc, String headerText) {
        XWPFHeaderFooterPolicy headerFooterPolicy = doc.getHeaderFooterPolicy();
        if (headerFooterPolicy != null) {
            XWPFHeader header = headerFooterPolicy.createHeader(XWPFHeaderFooterPolicy.DEFAULT);
            XWPFParagraph paragraph = header.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText(headerText);
        }
    }

    public static void addTableToDocument(XWPFDocument doc, int i, int i1) {
        XWPFTable table = doc.createTable(i, i1);
        for (XWPFTableRow row : table.getRows()) {
            for (XWPFTableCell cell : row.getTableCells()) {
                XWPFParagraph cellParagraph = cell.getParagraphs().get(0);
                XWPFRun run = cellParagraph.createRun();
                run.setText("Cell Content");
            }
        }
    }

    public static void addTextToDocument(XWPFDocument doc, String text) {
        XWPFParagraph paragraph = doc.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.setText(text);
    }

    

    private static void copyParagraph(XWPFParagraph source, XWPFParagraph destination) {
        destination.getCTP().set(source.getCTP().copy());
    }

    public static void deleteParagraph(XWPFDocument doc, int paragraphIndex) {
        if (paragraphIndex >= 0 && paragraphIndex < doc.getParagraphs().size()) {
            doc.removeBodyElement(paragraphIndex);
        }
    }

    public static XWPFDocument openDocument(String filePath) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        return new XWPFDocument(fis);
    }






    public static void readDocument(String filePath) throws IOException {
        XWPFDocument document = openDocument(filePath);
        List<XWPFParagraph> paragraphs = document.getParagraphs();

        for (XWPFParagraph paragraph : paragraphs) {
            List<XWPFRun> runs = paragraph.getRuns();
            for (XWPFRun run : runs) {
                String text = run.getText(0);
                if (text != null) {
                    System.out.println(text);
                }
            }
        }
    }

    public static void replaceTextInDocument(XWPFDocument doc, String search, String replace) {
        for (XWPFParagraph paragraph : doc.getParagraphs()) {
            for (XWPFRun run : paragraph.getRuns()) {
                String text = run.getText(0);
                if (text != null && text.contains(search)) {
                    text = text.replace(search, replace);
                    run.setText(text, 0);
                }
            }
        }
    }

    public static void saveDocument(XWPFDocument doc, String filePath) throws IOException {
        FileOutputStream fos = new FileOutputStream(filePath);
        doc.write(fos);
        fos.close();
    }



}
