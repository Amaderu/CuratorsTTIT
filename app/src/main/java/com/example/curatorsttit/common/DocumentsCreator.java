package com.example.curatorsttit.common;

import com.example.curatorsttit.models.Person;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.BorderExtent;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.PropertyTemplate;
import org.apache.poi.xssf.usermodel.XSSFPrintSetup;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

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

    CellStyle createDefaultCellStyle(Workbook wb) {
        //Style
        CellStyle defaultCellStyle = wb.createCellStyle();
        defaultCellStyle.setAlignment(HorizontalAlignment.CENTER);
        defaultCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        defaultCellStyle.setWrapText(true);
        //Font
        defaultCellStyle.setFont(createDefaultFont(wb));
        return defaultCellStyle;
    }

    Font createDefaultFont(Workbook wb) {
        Font defaultFont = wb.createFont();
        defaultFont.setFontHeightInPoints((short) 12);
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
        createDefaultCellStyle(wb);
        for (int rows = 0; rows < 36; rows++) {
            crRow = sheet.createRow(rows);
            if (rows == 0) crRow.setHeightInPoints(30F);
            else if (rows == 1) crRow.setHeightInPoints(34.5F);
            else if (rows == 2 || rows == 3) crRow.setHeightInPoints(15F);
            else if (rows == 4) crRow.setHeightInPoints(79.5F);
            else crRow.setHeightInPoints(20F);
            for (int i = 0; i < 24; i++) {
                crRow.createCell(i);
                crRow.getCell(i).setCellStyle(wb.getCellStyleAt(0));
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

        sheet.getRow(2).getCell(0).setCellValue("№ п/п");
        sheet.getRow(2).getCell(1).setCellValue("Фамилия Имя Отчество");
        sheet.getRow(2).getCell(4).setCellValue("Дисциплины/Оценки по итогам семестра");
        sheet.getRow(2).getCell(22).setCellValue("Пропуски, кол-во часов");
        sheet.getRow(2).getCell(23).setCellValue("Решение стипендиальной комиссии");
        for (int i = 0; i < 24; i++) {
            //int columnIndex, int width
            //sheet.setColumnWidth(0,3.71f);
            //9,9,23
            for (int j = 2; j < 5; j++) {
                sheet.getRow(j).getCell(i).setCellStyle(wb.getCellStyleAt(1));
                sheet.getRow(j).getCell(i).getCellStyle().setFont(wb.getFontAt((short) 1));
            }
        }
        //findFont(boldWeight, color, fontHeight, name, italic, strikeout, typeOffset, underline);
        /*boolean bold = (matchFont.getStyle() & org.apache.poi.hssf.usermodel.HSSFWorkbook.Font.BOLD) != 0;
        boolean italic = (matchFont.getStyle() & Font.ITALIC) != 0;*/
        //numberStudent.setCellStyle(wb.getCellStyleAt(0));


        sheet.getRow(0).getCell(0).setCellValue("Ведомость для назначения на стипендию");
        //Стиль для ячейки A1
        CellStyle styleTwo = createDefaultCellStyle(wb);
        styleTwo.setVerticalAlignment(VerticalAlignment.BOTTOM);
        Font fontTwo = wb.getFontAt((short) 2);
        fontTwo.setFontHeightInPoints((short) 20);
        sheet.getRow(0).getCell(0).setCellStyle(styleTwo);
        sheet.getRow(0).getCell(0).getCellStyle().setFont(fontTwo);
        //----
        sheet.getRow(1).getCell(0).setCellValue("\"____ \"  курс,     \"_______\" группа,      \"______\" семестр     20_____/20_____ уч.год");
        //Стиль для ячейки A2
        CellStyle styleThree = createDefaultCellStyle(wb);
        styleThree.setVerticalAlignment(VerticalAlignment.CENTER);
        Font fontThree = wb.getFontAt((short) 3);
        fontThree.setFontHeightInPoints((short) 14);
        sheet.getRow(1).getCell(0).setCellStyle(styleThree);
        sheet.getRow(1).getCell(0).getCellStyle().setFont(fontThree);
        //----
        //Задание ширины столбцам
        sheet.setColumnWidth(0, (int) (4.5 * 256));
        sheet.setColumnWidth(1, 9 * 256);
        sheet.setColumnWidth(2, 8 * 256);
        sheet.setColumnWidth(3, 23 * 256);
        for (int i = 4; i < 22; i++)
            sheet.setColumnWidth(i, 6 * 256);
        sheet.setColumnWidth(22, 15 * 256);
        sheet.setColumnWidth(23, 21 * 256);

        //Объединение
        CellRangeAddress cra = new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 23);
        sheet.addMergedRegion(cra);
        cra = new CellRangeAddress(1, 1, 0, 23);
        sheet.addMergedRegion(cra);
        cra = new CellRangeAddress(2, 4, 0, 0);
        sheet.addMergedRegion(cra);
        cra = new CellRangeAddress(2, 4, 1, 3);
        sheet.addMergedRegion(cra);
        cra = new CellRangeAddress(2, 3, 4, 21);
        sheet.addMergedRegion(cra);
        cra = new CellRangeAddress(2, 4, 22, 22);
        sheet.addMergedRegion(cra);
        cra = new CellRangeAddress(2, 4, 23, 23);
        sheet.addMergedRegion(cra);
        //переворот листа
        sheet.getPrintSetup().setLandscape(true);
        sheet.getPrintSetup().setPaperSize(XSSFPrintSetup.A4_PAPERSIZE);
        //sheet.getPrintSetup().setScale((short)48);
        //установка печати на одну страницу
        sheet.setFitToPage(true);
        sheet.getPrintSetup().setFitWidth((short) 1);
        sheet.getPrintSetup().setFitHeight((short) 1);//0для неважно какой длинны
        sheet.setAutobreaks(true);
        //Установка области печати
        wb.setPrintArea(0, "$A$1:$X$36");
        //TODO рассмотреть вариацию колонтитулов
        CellStyle style = createDefaultCellStyle(wb);
        for (int i = 32; i < 36; i++) {
            Row r = sheet.createRow(i);
            for (int j = 0; j < 24; j++) {
                r.createCell(j);
                style.setAlignment(HorizontalAlignment.LEFT);
                style.setVerticalAlignment(VerticalAlignment.BOTTOM);
                Font font = wb.getFontAt((short) 4);
                font.setBold(false);
                font.setFontHeightInPoints((short) 14);
                style.setFont(font);
                r.getCell(j).setCellStyle(style);
            }
        }
        //sheet.addMergedRegion(new CellRangeAddress(i, i, 1, 4););
        sheet.getRow(32).getCell(1).setCellValue("Руководитель");
        sheet.getRow(32).getCell(12).setCellValue("________________");
        sheet.getRow(33).getCell(1).setCellValue("Зав.отделением воспитательной работы");
        sheet.getRow(33).getCell(12).setCellValue("Н.Б.Добрыднева");
        sheet.getRow(34).getCell(1).setCellValue("Зав.очным отделением");
        sheet.getRow(34).getCell(12).setCellValue("Л.В. Коруз");
        sheet.getRow(35).getCell(1).setCellValue("Куратор группы");
        sheet.getRow(35).getCell(12).setCellValue("_________________");

        for (int i = 32; i < 36; i++) {
            sheet.addMergedRegion(new CellRangeAddress(i, i, 1, 4));
            sheet.addMergedRegion(new CellRangeAddress(i, i, 12, 15));
        }
        style = createDefaultCellStyle(wb);
        for (int i = 5; i < 31; i++) {
            Font font = wb.getFontAt((short) 5);
            font.setBold(false);
            font.setFontHeightInPoints((short) 12);
            style = wb.getCellStyleAt(5);
            style.setFont(font);
            sheet.createRow(i);
            sheet.getRow(i).createCell(0);
            sheet.getRow(i).getCell(0).setCellValue(i - 4);
            sheet.getRow(i).getCell(0).setCellStyle(style);
            //Объединение для фамилий
            sheet.addMergedRegion(new CellRangeAddress(i, i, 1, 3));
            //Стиль для ФИО
            createDefaultCellStyle(wb);
            style = wb.getCellStyleAt(6);
            style.setVerticalAlignment(VerticalAlignment.BOTTOM);
            style.setFont(wb.getFontAt((short) 5));
            sheet.getRow(i).createCell(1);
            sheet.getRow(i).getCell(1).setCellStyle(style);
        }
        //TODO объединение ячеек для таблицы и все границы
        //29Rx24C
        //Внешние гранницы
        PropertyTemplate pt = new PropertyTemplate();
        // #1) these borders will all be medium in default color
        //29Rx24C
        pt.drawBorders(new CellRangeAddress(2, 30, 0, 23), BorderStyle.THIN, BorderExtent.ALL);
        pt.applyBorders(sheet);

        // Write the output to a file
        try (OutputStream fileOut = new FileOutputStream(filePath)) {
            wb.write(fileOut);
            //fileOut.flush();
        }
        wb.close();
    }

    public void updateDocStep(List<Person> students, String filePath) throws Exception {
        Workbook workbook = null;
        try{
            FileInputStream inputStream = new FileInputStream(new File(filePath));
            workbook = WorkbookFactory.create(inputStream);
        } catch (FileNotFoundException | InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Sheet sheet = workbook.getSheetAt(0);
        //типо заполенине данными
        List<Person> persons = new ArrayList<Person>(students);
        persons.add(new Person("Алфимова","Светлана","Александровна","afdas@gmail","+745646"));
        persons.add(new Person("Шарапова","Наталья","Александровна","gagga1@gmail","+76641631"));
        int counter = 5;
        for(Person p : persons)
        {
            sheet.getRow(counter++).getCell(1).setCellValue(p.getFIO());
        }
        try (OutputStream outputStream = new FileOutputStream(filePath)) {
            workbook.write(outputStream);
            //fileOut.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        workbook.close();

    }
}
