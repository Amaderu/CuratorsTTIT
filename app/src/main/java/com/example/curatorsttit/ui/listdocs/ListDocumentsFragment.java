package com.example.curatorsttit.ui.listdocs;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.curatorsttit.R;
import com.example.curatorsttit.databinding.FragmentListDocumentsBinding;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListDocumentsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    FragmentListDocumentsBinding binding;
    List<String> items = new ArrayList<>();
    String folderPath;

    public ListDocumentsFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        folderPath = Environment.getExternalStorageDirectory().getPath()+ "/Documents/" + getResources().getString(R.string.app_name);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentListDocumentsBinding.inflate(inflater,container,false);
        initList(folderPath);
        initListView();
        return binding.getRoot();
    }
    void initListView(){
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, items);
        binding.listView.setAdapter(itemsAdapter);
        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openFile(((TextView)view).getText().toString());
            }
        });

        /*binding.listView.setOnItemLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openFile(((TextView)view).getText().toString());
            }
        });*/
    }
    //search all reports in folder
    private void initList(String path) {

        try {
            File file = new File(path);
            File[] fileList = file.listFiles();
            String fileName;
            for (File f : fileList) {
                if (f.isDirectory()) {
                    initList(f.getAbsolutePath());
                } else {
                    fileName = f.getName();
                    if (fileName.endsWith(".xlsx")||fileName.endsWith(".docx")) {
                        items.add(fileName);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void openFile(String filename) {
        if(filename==null||filename.isEmpty()){
            new AlertDialog.Builder(requireContext()).setTitle("Ошибка открытия").setMessage("Файл по пути "+filename+" не сущуствует").setPositiveButton("Ок",null).show();
            return;
        }
        File reportFile = new File(folderPath + "/" + filename);

        if(!reportFile.exists()){
            new AlertDialog.Builder(requireContext()).setTitle("Ошибка открытия").setMessage("Файл по пути "+filename+" не сущуствует").setPositiveButton("Ок",null).show();
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //intent.setData(Uri.parse("file:"+folderPath+"/"+filename+".xlsx"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent.setDataAndType(Uri.fromFile(reportFile), "application/application/vnd.ms-excel");
        //intent.setDataAndType(Uri.fromFile(reportFile), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        intent.setDataAndType(Uri.fromFile(reportFile), getMimeType(Uri.fromFile(reportFile)));
        Intent chooser = Intent.createChooser(intent, "Открыть файл в...");
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(chooser);
        }

    }

    public String getMimeType(Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = getContext().getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }
}