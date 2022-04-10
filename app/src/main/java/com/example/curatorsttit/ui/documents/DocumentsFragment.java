package com.example.curatorsttit.ui.documents;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.curatorsttit.MainActivity;
import com.example.curatorsttit.R;
import com.example.curatorsttit.adapters.GroupSpinnerAdapter;
import com.example.curatorsttit.common.DataGenerator;
import com.example.curatorsttit.common.DocumentsCreator;
import com.example.curatorsttit.databinding.FragmentDocumetsBinding;
import com.example.curatorsttit.models.Group;
import com.example.curatorsttit.models.Person;
import com.example.curatorsttit.ui.listdocs.ListDocumentsFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocumentsFragment extends Fragment {
    private String folderPath;
    FragmentDocumetsBinding binding;

    private static final String STUDENT_LIST = "student_list";
    private static final String GROUP_LIST = "group_list";


    private List<Group> groupList;
    private List<Person> studentsList;
    public Map<Group,List<Person>> studentsLists;
    Spinner spinner;

    public DocumentsFragment() {
        // Required empty public constructor
    }


    public static DocumentsFragment newInstance(String studentsList, String groupList) {
        DocumentsFragment fragment = new DocumentsFragment();
        Bundle args = new Bundle();
        args.putString(STUDENT_LIST, studentsList);
        args.putString(GROUP_LIST, groupList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            studentsList = gson.fromJson( getArguments().getString(STUDENT_LIST),new TypeToken<List<Person>>(){}.getType());
            groupList = gson.fromJson( getArguments().getString(GROUP_LIST),new TypeToken<List<Group>>(){}.getType());
        }
        createDirectoryAndSaveFile();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDocumetsBinding.inflate(inflater, container, false);

        /*
        <string name="scholarship_statement_doc">Стипендиальная ведомость</string>
    <string name="svod_info_group_doc">Сводная информация по группе</string>
    <string name="curator_report_doc">Отчет куратора за семестр</string>
    <string name="month_report_doc">Ежемесячный отчет по воспитательной работе в группе</string>
    <string name="month_plan_doc">Ежемесячный план воспитательной работы в группе</string>
    <string name="year_plan_doc">Календарный план куратора на год</string>
         */
        binding.document.setOnClickListener(view -> {
            generateStepDoc("");
        });
        binding.document2.setOnClickListener(view -> {
            generateSvodInfo("");
        });
        binding.document3.setOnClickListener(view -> {
            generateCuratorReport("");
        });
        binding.document4.setOnClickListener(view -> {
            generateMonthReport("");
        });
        binding.document5.setOnClickListener(view -> {
            generateMonthPlan("");
        });
        binding.document6.setOnClickListener(view -> {
            generateYearPlan("");
        });
        return binding.getRoot();
    }
    private void initSpinner() {
        getGroups();
        GroupSpinnerAdapter spinnerArrayAdapter = new GroupSpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item,android.R.id.text1, groupList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
    }
    private void getGroups(){
        groupList = DataGenerator.mockGenerateGroup();
    }
    private void getLists(){
        studentsLists = new HashMap<>();
        for (Group g :
                groupList) {
            studentsLists.put(g,DataGenerator.mockGenerateStudents(g));
        }

    }

    private void initToolbar(){
        binding.toolbar.addView(getLayoutInflater().inflate(R.layout.toolbar_doc, null));
        binding.toolbar.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
                menuInflater.inflate(R.menu.action_bar_open, menu);

                menu.findItem(R.id.open).setOnMenuItemClickListener(menuItem -> {
                    ((MainActivity)getActivity()).openActivivty(new ListDocumentsFragment(), true);
                    return onOptionsItemSelected(menuItem);
                });
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });
    }


    void generateStepDoc(String filename){
        try {
            filename = getString(R.string.scholarship_statement_doc);
            Group g = ((Group)spinner.getSelectedItem());
            List<Person> data = studentsLists.get(g);
            DocumentsCreator.getInstance().createDocumentStep(data,folderPath,filename);
            String toast = "Документ "+filename+"Успешно сгенерирован";
            Toast.makeText(requireContext(), toast, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void generateSvodInfo(String filename){
        try {
            filename = getString(R.string.svod_info_group_doc);
            DocumentsCreator.getInstance().createFile(folderPath,filename, getContext(), new Integer[]{
                    R.raw.svod_info_group
            });
            String toast = "Документ "+filename+"Успешно сгенерирован";
            Toast.makeText(requireContext(), toast, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void generateCuratorReport(String filename){
        try {
            filename = getString(R.string.curator_report_doc);
            DocumentsCreator.getInstance().createFile(folderPath,filename, getContext(), new Integer[]{
                    R.raw.svod_info_group
            });
            String toast = "Документ "+filename+"Успешно сгенерирован";
            Toast.makeText(requireContext(), toast, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void generateMonthReport(String filename){
        try {
            filename = getString(R.string.month_report_doc);
            DocumentsCreator.getInstance().createFile(folderPath,filename, getContext(), new Integer[]{
                    R.raw.month_report
            });
            String toast = "Документ "+filename+"\nуспешно сгенерирован";
            Toast.makeText(requireContext(), toast, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void generateMonthPlan(String filename){
        try {
            filename = getString(R.string.month_plan_doc);
            DocumentsCreator.getInstance().createFile(folderPath,filename, getContext(), new Integer[]{
                    R.raw.month_plan
            });
            String toast = "Документ "+filename+"\nуспешно сгенерирован";
            Toast.makeText(requireContext(), toast, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void generateYearPlan(String filename){
        try {
            filename = getString(R.string.year_plan_doc);
            DocumentsCreator.getInstance().createFile(folderPath,filename, getContext(), new Integer[]{
                    R.raw.year_plan
            });
            String toast = "Документ "+filename+"\nуспешно сгенерирован";
            Toast.makeText(requireContext(), toast, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    AlertDialog.Builder customdialog() {

        AlertDialog.Builder alert = new AlertDialog.Builder(requireContext());

        alert.setTitle("Title");
        alert.setMessage("Message");

        // Set an EditText view to get user input
        final EditText input = new EditText(requireContext());
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                // Do something with value!
                Toast.makeText(requireContext(), "Начинается генерация документа", Toast.LENGTH_LONG).show();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        return alert;
    }
    //FixMe не работает
    private void Sharing(View v) {
        String filename = "Стипендиальная ведомость";
        Intent intent = new Intent(Intent.ACTION_SEND);
        //intent.setData(Uri.parse("file:"+folderPath+"/"+filename+".xlsx"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        File reportFile = new File(folderPath + "/" + filename + ".xlsx");
        intent.setDataAndType(Uri.fromFile(reportFile), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

// Always use string resources for UI text.
// This says something like "Share this photo with"
// Create intent to show chooser
        Intent chooser = Intent.createChooser(intent, "Поделиться файлом");

// Verify the intent will resolve to at least one activity
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(chooser);
        }
        Uri.parse("content://contacts/people/1");

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
        File file = new File(filepath + "/Documents/" + getString(R.string.app_name));

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

    @Override
    public void onStart() {
        super.onStart();
        initToolbar();
        spinner = (Spinner)binding.toolbar.findViewById(R.id.spinner_groups);
        initSpinner();
        getLists();
    }

}