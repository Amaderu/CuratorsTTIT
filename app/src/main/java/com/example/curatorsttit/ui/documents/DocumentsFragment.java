package com.example.curatorsttit.ui.documents;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.curatorsttit.R;
import com.example.curatorsttit.common.DocumentsCreator;
import com.example.curatorsttit.databinding.FragmentDocumetsBinding;
import com.example.curatorsttit.databinding.FragmentMainBinding;
import com.example.curatorsttit.models.Users;
import com.google.gson.annotations.SerializedName;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DocumentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DocumentsFragment extends Fragment {
    private String folderPath;
    FragmentDocumetsBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DocumentsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DocumetsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DocumentsFragment newInstance(String param1, String param2) {
        DocumentsFragment fragment = new DocumentsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        createDirectoryAndSaveFile();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDocumetsBinding.inflate(inflater, container, false);
        binding.document.setOnClickListener(view -> {
            //generateFile(view);
            /*try {
                writeXLSXFile(folderPath+"/ExcelTest.xlsx");
            } catch (IOException | InvalidFormatException e) {
                e.printStackTrace();
            }
            try {
                readExcelFile(folderPath+"/ExcelTest.xlsx");
            } catch (IOException | InvalidFormatException e) {
                e.printStackTrace();
            }*/
            try {
                DocumentsCreator.getInstance().createDocumentStep(folderPath+"/ExcelTest.xlsx");
                Toast.makeText(requireContext(),"Успешно сгенерирован", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return binding.getRoot();
        /*return inflater.inflate(R.layout.fragment_documets, container, false);*/
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    /*Create directory to save files*/
    private void createDirectoryAndSaveFile() {
        //Danger premission Error
        String TAG = "Create_folder";
        if (!isExternalStorageWritable()) Log.d(TAG, "not Writable");
        if (!isExternalStorageReadable()) Log.d(TAG, "not Readable");
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath + "/Documents/"+getString(R.string.app_name));

        if (!file.exists()) {
            Log.d(TAG, "not exists");
            if (!file.mkdirs()) Log.d("Folder", "not Created");
            else {
                Log.d(TAG, "Created");
            }
        } else Log.d(TAG, "exists");
        folderPath = file.getPath();
        Log.d(TAG, folderPath);
        //file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg";

    }
    public void generateFile(View v){
        Log.i("generateFile", "generateFile: START generate");
        File sdcard = Environment.getExternalStorageDirectory();
        File excelFile = new File(folderPath,"ExcelTest.xlsx");
        if (!excelFile.exists()) {
            try {
                excelFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /*excelFile.setExecutable()*/
        //if (excelFile.exists()||excelFile.canRead())
        /*try {
            FileInputStream fileInput = new FileInputStream(excelFile);
            OPCPackage packg = OPCPackage.open(excelFile);
            Workbook wrkbk = new XSSFWorkbook(fileInput);
            Sheet sheet = wrkbk.createSheet("Sample sheet");
            Sheet sheet1 = wrkbk.getSheetAt(0);
            //Obtain reference to the Cell using getCell(int col, int row) method of sheet
            *//*Cell colArow1 = sheet1.getCellComment(0, 0);
            Cell colBrow1 = sheet1.getCellComment(1, 0);
            Cell colArow2 = sheet1.getCellComment(0, 1);
            //Read the contents of the Cell using getContents() method, which will return
            //it as a String
            String str_colArow1 = colArow1.getContents();
            String str_colBrow1 = colBrow1.getContents();
            String str_colArow2 = colArow2.getContents();*//*
            Cell cell = sheet1.getRow(1).getCell(0);
            cell.setCellValue(12134);
            //fileInput.close();
            packg.close();

            FileOutputStream outFile = new FileOutputStream(excelFile);
            wrkbk.write(outFile);
            outFile.close();

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("generateFile", "generateFile: Failed to generate");
        }
        catch(Exception e){
            e.printStackTrace();
            Log.d("generateFile", "generateFile: Failed to generate");

        }*/
        /*try (FileInputStream inp = new FileInputStream(excelFile)) {
            //InputStream inp = new FileInputStream("workbook.xlsx");
            Workbook wb = WorkbookFactory.create(inp);
            wb.createSheet("Data");
            Sheet sheet = wb.getSheetAt(0);
            sheet.createRow(2);
            Row row = sheet.getRow(2);
            Cell cell = row.getCell(3);
            if (cell == null)
                cell = row.createCell(3);
            cell.setCellType(CellType.STRING);
            cell.setCellValue("a test");
            // Write the output to a file
            try (OutputStream fileOut = new FileOutputStream(excelFile)) {
                wb.write(fileOut);
            }
        } catch (IOException | InvalidFormatException | EncryptedDocumentException e) {
            e.printStackTrace();
            Log.d("generateFile", "generateFile: Failed to generate");
        }*/
        /*List<Users> users = generateSampleUserData();
        try(InputStream is = ObjectCollectionDemo.class.getResourceAsStream("object_collection_template.xls")) {
            try (OutputStream os = new FileOutputStream("target/object_collection_output.xls")) {
                Context context = new Context();
                context.putVar("employees", users);
                JxlsHelper.getInstance().processTemplate(is, os, context);
            }
        }*/
    }
    void generateSampleUserData(){


    }
    private void readExcelFile(String filePath) throws IOException, InvalidFormatException {
        if(!new File(filePath).exists()) return;
        Workbook workbook = WorkbookFactory.create(new File(filePath));
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();
        sheet.forEach(row -> {
            row.forEach(cell -> {
                String cellValue = dataFormatter.formatCellValue(cell);
                System.out.print(cellValue + "\t");
            });
            System.out.println();
        });
        workbook.close();
    }
    public static void writeXLSXFile(String filePath) throws IOException, InvalidFormatException {
        String sheetName = "Sheet1";//name of sheet
        //XSSFWorkbook wb = new XSSFWorkbook();
        /*XSSFWorkbook wb = new XSSFWorkbook(XSSFWorkbookType.XLSX);
        XSSFSheet sheet = wb.createSheet(sheetName) ;*/
        File file = new File(filePath);
        //Workbook wb = WorkbookFactory.create(new File("ExcelTest.xlsx"));
        /*if(file.exists()) return;
        else file.createNewFile();
        if(!file.canRead()) return;*/
        Workbook wb = new XSSFWorkbook(XSSFWorkbookType.XLSX);
        Sheet sheet = wb.createSheet("Лист1");

        //iterating r number of rows
        for (int r=0;r < 5; r++ )
        {
            Row row = sheet.createRow(r);

            //iterating c number of columns
            for (int c=0;c < 5; c++ )
            {
                Cell cell = row.createCell(c);

                cell.setCellValue("Cell "+r+" "+c);
            }
        }

        FileOutputStream fileOut = new FileOutputStream(filePath);

        //write this workbook to an Outputstream.
        wb.write(fileOut);
        wb.close();
        fileOut.flush();
        fileOut.close();
    }

}