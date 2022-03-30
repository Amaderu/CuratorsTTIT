package com.example.curatorsttit.common;

import com.example.curatorsttit.network.ApiService;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellRange;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FontFamily;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookType;

import java.io.FileOutputStream;
import java.io.OutputStream;

public class DocumentsCreator {
    private static DocumentsCreator mInstance;
    public static DocumentsCreator getInstance() {
        if (mInstance == null) {
            mInstance = new DocumentsCreator();
        }
        return mInstance;
    }
    public void createCell(Workbook wb, Row row, int column, HorizontalAlignment halign, VerticalAlignment valign) {
        Cell cell = row.createCell(column);
        /*cell.setCellValue("Align It");*/
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(halign);
        cellStyle.setVerticalAlignment(valign);
        cell.setCellStyle(cellStyle);
    }

    public void createDocumentStep(String filePath) throws Exception {
        Workbook wb = new XSSFWorkbook(XSSFWorkbookType.XLSX); //or new HSSFWorkbook();
        Sheet sheet = wb.createSheet("Лист1");
        Row row = sheet.createRow(0);
        row.setHeightInPoints(30);
        Row row2 = sheet.createRow(1);
        row2.setHeightInPoints(34.5f);
        /*createCell(wb, row, 0, HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);
        createCell(wb, row, 1, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.BOTTOM);
        createCell(wb, row, 2, HorizontalAlignment.FILL, VerticalAlignment.CENTER);
        createCell(wb, row, 3, HorizontalAlignment.GENERAL, VerticalAlignment.CENTER);
        createCell(wb, row, 4, HorizontalAlignment.JUSTIFY, VerticalAlignment.JUSTIFY);
        createCell(wb, row, 5, HorizontalAlignment.LEFT, VerticalAlignment.TOP);
        createCell(wb, row, 6, HorizontalAlignment.RIGHT, VerticalAlignment.TOP);*/
        //createCell(wb, row, 6, HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);
        for (int i=0;i<24;i++){
            Cell cell =  row.createCell(i);
            CellStyle cellStyle = wb.createCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.BOTTOM);
            //PT Astra Serif
            Font font = wb.createFont();
            font.setFontHeightInPoints((short)20);
            font.setFontName("PT Astra Serif");
            font.setBold(true);
            cellStyle.setFont(font);
            cell.setCellStyle(cellStyle);
        }
        Cell cell = row.getCell(0);
        cell.setCellValue("Ведомость для назначения на стипендию");
        for (int i=0;i<24;i++){
            Cell cell2 =  row2.createCell(i);
            CellStyle cellStyle = wb.createCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            //PT Astra Serif
            Font font = wb.createFont();
            font.setFontHeightInPoints((short)14);
            font.setFontName("PT Astra Serif");
            font.setBold(true);
            cellStyle.setFont(font);
            cell2.setCellStyle(cellStyle);
        }
        sheet.getRow(1).getCell(0).setCellValue("\"____ \"  курс,     \"_______\" группа,      \"______\" семестр     20_____/20_____ уч.год");
        CellRangeAddress cra = new CellRangeAddress(row.getRowNum(),row.getRowNum(),0,23);
        sheet.addMergedRegion(cra);
        cra.setFirstRow(1);
        cra.setLastRow(1);
        sheet.addMergedRegion(cra);
        // Write the output to a file
        try (OutputStream fileOut = new FileOutputStream(filePath)) {
            wb.write(fileOut);
            //fileOut.flush();
        }
        wb.close();
    }
}
