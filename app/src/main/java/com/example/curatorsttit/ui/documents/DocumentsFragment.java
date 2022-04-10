package com.example.curatorsttit.ui.documents;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;
import android.os.Environment;
import android.security.keystore.KeyGenParameterSpec;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.curatorsttit.network.ApiService;
import com.example.curatorsttit.ui.listdocs.ListDocumentsFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocumentsFragment extends Fragment {
    private String folderPath;
    FragmentDocumetsBinding binding;

    private static final String STUDENT_LIST = "student_list";
    private static final String GROUP_LIST = "group_list";
    private static final String CURATOR_ID = "CURATOR_ID";

    private int curatorId;
    private List<Group> groupList;
    private List<Person> studentsList;
    public Map<Group,List<Person>> studentsLists;
    Spinner spinner;
    SharedPreferences sharedPreferences;

    public DocumentsFragment() {
        // Required empty public constructor
    }
    private void getAppDataFromPrefs() throws GeneralSecurityException, IOException {
        KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;
        String masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec);
        Context context = requireContext();
        sharedPreferences = EncryptedSharedPreferences.create(
                "app_data",
                masterKeyAlias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
        curatorId = sharedPreferences.getInt(CURATOR_ID, -1);
        if(curatorId != -1)
            Log.i("LoadData", "getAppDataFromPrefs: successes");
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
        try {
            getAppDataFromPrefs();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDocumetsBinding.inflate(inflater, container, false);
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
        GroupSpinnerAdapter spinnerArrayAdapter = new GroupSpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item,android.R.id.text1, groupList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
    }
    private void mockGetGroups(){
        groupList = DataGenerator.mockGenerateGroup();
        initSpinner();
    }
    private void mockGetLists(){
        studentsLists = new HashMap<>();
        for (Group g :
                groupList) {
            studentsLists.put(g,DataGenerator.mockGenerateStudents(g));
        }

    }

    private void showError(String msg){
        alertDialog(msg).show();
    }
    android.app.AlertDialog.Builder alertDialog(String msg) {
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(requireContext());
        alert.setTitle("Ошибка");
        alert.setMessage(msg);
        alert.setPositiveButton("Ok", null);
        return alert;
    }

    private void getGroups(int curator_id) {
        final String ErrorMsg = getString(R.string.error);
        ApiService.getInstance().getApi().getGroupsByCuratorId(curator_id).enqueue(new Callback<List<Group>>() {
            @Override
            public void onResponse(Call<List<Group>> call, Response<List<Group>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    groupList = response.body();
                    initSpinner();
                }
                else {
                    showError(ErrorMsg);
                }
            }

            @Override
            public void onFailure(Call<List<Group>> call, Throwable t) {
                t.printStackTrace();
                showError(ErrorMsg);
            }
        });
    }

    private void getLists(){
        studentsLists = new HashMap<>();
        for (Group g :
                groupList) {
            studentsLists.put(g,getStudentList(g.getId()));
        }

    }

    private List<Person> getStudentList(int groupId) {
        ApiService.getInstance().getApi().getStudentsByGroup(groupId).enqueue(new Callback<List<Person>>() {
            @Override
            public void onResponse(Call<List<Person>> call, Response<List<Person>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    studentsList = response.body();
                } else {
                    Group groupName = groupList.stream().filter(group -> group.getId()==groupId).findFirst().get();
                    showError(getString(R.string.error)+" Данных группы "+groupName.getNumber());
                }
            }

            @Override
            public void onFailure(Call<List<Person>> call, Throwable t) {
                t.printStackTrace();
                Group groupName = groupList.stream().filter(group -> group.getId()==groupId).findFirst().get();
                showError(getString(R.string.error)+" Данных группы "+groupName.getNumber());
            }
        });
        return studentsList;
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

    private void shareFileWithApps(View v) {
        String filename = "Стипендиальная ведомость";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        File reportFile = new File(folderPath + "/" + filename + ".xlsx");
        intent.setDataAndType(Uri.fromFile(reportFile), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        Intent chooser = Intent.createChooser(intent, "Поделиться файлом");
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(chooser);
        }
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
    }

    @Override
    public void onStart() {
        super.onStart();
        initToolbar();
        spinner = (Spinner)binding.toolbar.findViewById(R.id.spinner_groups);
        //FIXME
        //getGroups(curatorId);
        mockGetGroups();
        if(groupList == null || groupList.isEmpty()) return;
        //Fixme
        //getLists();
        mockGetLists();
    }

}