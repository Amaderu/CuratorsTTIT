package com.example.curatorsttit;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.curatorsttit.adapters.StudentListViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainFragment() {
        // Required empty public constructor
    }
    ListView namesList;
    StudentListViewAdapter adapter;
    SearchView editsearch;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment mainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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

        if(namesList==null)
            return;
        // получаем ресурс
        String[] names = getResources().getStringArray(R.array.names);
        // создаем адаптер
        ArrayAdapter<String> adapter1 = new ArrayAdapter(getContext(),
                android.R.layout.simple_list_item_1, names);
        // устанавливаем для списка адаптер
        namesList.setAdapter(adapter1);





    }

    @Override
    public void onStart() {
        super.onStart();
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.getMenu().clear();
        toolbar.removeViewAt(0);
        toolbar.addView(getLayoutInflater().inflate(R.layout.toolbar,null));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        /*Spinner spinner = (Spinner) view.findViewById(R.id.spinner_groups);
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(getContext(), R.array.groups,R.layout.cus_spinn);
        adapter.setDropDownViewResource(R.layout.cus_drop_spinn);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);*/
        namesList = view.findViewById(R.id.lvMain);
        List<String> listStudents = new ArrayList<>();
        String[] students = getResources().getStringArray(R.array.names);
        for (String wp : students) {
            listStudents.add(wp);
        }
        adapter = new StudentListViewAdapter(getContext(), listStudents);
        editsearch = (SearchView) view.findViewById(R.id.simpleSearchView);
        //editsearch = (SearchView) fragment.getView().findViewById(R.id.simpleSearchView);
        editsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String text = newText;
                adapter.filter(text);
                return false;
            }
        });
        namesList.setAdapter(adapter);

        return view;
    }
}