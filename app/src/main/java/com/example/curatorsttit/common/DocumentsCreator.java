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
    CellStyle createDefaultCellStyle(Workbook wb){
        //Style
        CellStyle defaultCellStyle = wb.createCellStyle();
        defaultCellStyle.setAlignment(HorizontalAlignment.CENTER);
        defaultCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        defaultCellStyle.setWrapText(true);
        //Font
        defaultCellStyle.setFont(createDefaultFont(wb));
        return defaultCellStyle;
    }
    Font createDefaultFont(Workbook wb){
        Font defaultFont = wb.createFont();
        defaultFont.setFontHeightInPoints((short)12);
        defaultFont.setFontName("PT Astra Serif");
        //defaultFont.setColor(IndexedColors.BLACK.getIndex());
        defaultFont.setBold(true);
        return defaultFont;
    }

    public void createDocumentStep(String filePath) throws Exception {
        Workbook wb = new XSSFWorkbook(XSSFWorkbookType.XLSX); //or new HSSFWorkbook();
        Sheet sheet = wb.createSheet("Лист1");
        Row row = sheet.createRow(0);
        row.setHeightInPoints(30);
        Row row2 = sheet.createRow(1);
        Row crRow;
        for (int rows =0;rows <5;rows++){
            crRow = sheet.createRow(rows);
            if(rows==0) crRow.setHeightInPoints(30F);
            else if(rows==1) crRow.setHeightInPoints(34.5F);
            else if(rows==2||rows==3) crRow.setHeightInPoints(15F);
            else if(rows==4) crRow.setHeightInPoints(79.5F);
            else crRow.setHeightInPoints(20F);
            for (int i=0;i<24;i++){
                crRow.createCell(i);
                crRow.getCell(i).setCellStyle(createDefaultCellStyle(wb));
            }
        }
        /*createCell(wb, row, 0, HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);
        createCell(wb, row, 1, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.BOTTOM);
        createCell(wb, row, 2, HorizontalAlignment.FILL, VerticalAlignment.CENTER);
        createCell(wb, row, 3, HorizontalAlignment.GENERAL, VerticalAlignment.CENTER);
        createCell(wb, row, 4, HorizontalAlignment.JUSTIFY, VerticalAlignment.JUSTIFY);
        createCell(wb, row, 5, HorizontalAlignment.LEFT, VerticalAlignment.TOP);
        createCell(wb, row, 6, HorizontalAlignment.RIGHT, VerticalAlignment.TOP);*/
        //createCell(wb, row, 6, HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);
        for (int i=0;i<24;i++){
            //int columnIndex, int width
            //sheet.setColumnWidth(0,3.71f);
            //9,9,23
            //sheet.autoSizeColumn(i,true);

        }
        Cell numberStudent = sheet.getRow(2).createCell(0);
        numberStudent.setCellValue("№ п/п");
        //findFont(boldWeight, color, fontHeight, name, italic, strikeout, typeOffset, underline);
        /*boolean bold = (matchFont.getStyle() & org.apache.poi.hssf.usermodel.HSSFWorkbook.Font.BOLD) != 0;
        boolean italic = (matchFont.getStyle() & Font.ITALIC) != 0;*/
        numberStudent.setCellStyle(createDefaultCellStyle(wb));


        sheet.getRow(0).getCell(0).setCellValue("Ведомость для назначения на стипендию");
        sheet.getRow(0).getCell(0).getCellStyle().setVerticalAlignment(VerticalAlignment.BOTTOM);
        Font font = createDefaultFont(wb);
        font.setFontHeight((short)20);
        sheet.getRow(0).getCell(0).getCellStyle().setFont(font);
        sheet.getRow(1).createCell(0).getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        font.setFontHeight((short)14);
        sheet.getRow(1).getCell(0).getCellStyle().setFont(font);
        sheet.getRow(1).getCell(0).setCellValue("\"____ \"  курс,     \"_______\" группа,      \"______\" семестр     20_____/20_____ уч.год");

        CellRangeAddress cra = new CellRangeAddress(row.getRowNum(),row.getRowNum(),0,23);
        sheet.addMergedRegion(cra);
        cra.setFirstRow(1);
        cra.setLastRow(1);
        sheet.addMergedRegion(cra);
        cra = new CellRangeAddress(2,4,0,0);
        sheet.addMergedRegion(cra);
        cra = new CellRangeAddress(2,4,1,3);
        sheet.addMergedRegion(cra);
        cra = new CellRangeAddress(2,3,4,21);
        sheet.addMergedRegion(cra);
        cra = new CellRangeAddress(2,4,22,22);
        sheet.addMergedRegion(cra);
        cra = new CellRangeAddress(2,4,23,23);
        sheet.addMergedRegion(cra);
        /*for (int i=0;i<24;i++){
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
        }*/
        for (int i=0;i<24;i++){
            Cell cell2 =  row2.createCell(i);
            CellStyle cellStyle = wb.createCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            //PT Astra Serif
            Font font2 = wb.createFont();
            font2.setFontHeightInPoints((short)14);
            font2.setFontName("PT Astra Serif");
            font2.setBold(true);
            cellStyle.setFont(font2);
            cell2.setCellStyle(cellStyle);
        }

        // Write the output to a file
        try (OutputStream fileOut = new FileOutputStream(filePath)) {
            wb.write(fileOut);
            //fileOut.flush();
        }
        wb.close();
    }
}
