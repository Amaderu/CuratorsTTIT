package com.example.curatorsttit.ui.documents;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.curatorsttit.R;
import com.example.curatorsttit.databinding.FragmentDocumetsBinding;
import com.example.curatorsttit.databinding.FragmentMainBinding;
import com.example.curatorsttit.models.Users;
import com.google.gson.annotations.SerializedName;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
            generateFile(view);
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
        Log.i("generateFile", "generateFile: Running Object Collection demo");
        File sdcard = Environment.getExternalStorageDirectory();
        File Excelfile = new File(sdcard,"/Documents/"+getString(R.string.app_name)+"/ExcelTest.xlsx");

        try {
            FileInputStream fileInput = new FileInputStream(Excelfile);
            XSSFWorkbook wrkbk = new XSSFWorkbook(fileInput);
            XSSFSheet sheet = wrkbk.createSheet("Sample sheet");
            XSSFSheet sheet1 = wrkbk.getSheetAt(0);
            //Obtain reference to the Cell using getCell(int col, int row) method of sheet
            /*Cell colArow1 = sheet1.getCellComment(0, 0);
            Cell colBrow1 = sheet1.getCellComment(1, 0);
            Cell colArow2 = sheet1.getCellComment(0, 1);*/
            //Read the contents of the Cell using getContents() method, which will return
            //it as a String
            /*String str_colArow1 = colArow1.getContents();
            String str_colBrow1 = colBrow1.getContents();
            String str_colArow2 = colArow2.getContents();*/
            Cell cell = sheet1.getRow(1).getCell(0);
            cell.setCellValue(12134);
            //fileInput.close();

            FileOutputStream outFile = new FileOutputStream(Excelfile);
            wrkbk.write(outFile);
            outFile.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }
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
}