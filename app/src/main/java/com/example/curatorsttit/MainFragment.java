package com.example.curatorsttit;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class MainFragment extends Fragment {
    public MainFragment() {
        // Required empty public constructor
    }

    ListView namesList;
    StudentListViewAdapter adapter;
    SearchView editsearch;
    SwipeRefreshLayout refreshLayout;
    Toolbar toolbar;
    SearchView.OnQueryTextListener queryTextListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //getActivity().getActionBar();
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshLayout = getView().findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(() -> doYourUpdate());
        //Toolbar toolbar;
        //toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        //ActionBar bar = (ActionBar) getActivity().getActionBar();
        //bar.setDisplayShowTitleEnabled(false)
        //toolbar.getMenu().removeItem(0);
        //.removeViewAt(0);
        //toolbar.(getLayoutInflater().inflate(R.layout.toolbar, null));

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

        //editsearch = (SearchView) view.findViewById(R.id.simpleSearchView);
        //editsearch = (SearchView) fragment.getView().findViewById(R.id.simpleSearchView);
        queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String text = newText;
                if (adapter != null)
                    adapter.filter(text);
                if (!newText.isEmpty()) {
                    adapter.setGroupVisability(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                }
                else{
                    adapter.setGroupVisability(View.GONE);
                    adapter.notifyDataSetChanged();
                }
                return false;
            }
        };
        namesList.setAdapter(adapter);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.addView(getLayoutInflater().inflate(R.layout.toolbar, null));
        toolbar.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.dashboard, menu);

                MenuItem searchItem = menu.findItem(R.id.action_search);

                SearchManager searchManager = (SearchManager) getActivity().getBaseContext().getSystemService(Context.SEARCH_SERVICE);

                SearchView searchView = null;
                if (searchItem != null) {
                    searchView = (SearchView) searchItem.getActionView();
                }
                if (searchView != null) {
                    searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
                }
                searchView.setQueryHint("Найти студента...");
                searchView.setOnQueryTextListener(queryTextListener);
                searchView.setIconifiedByDefault(false);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        return view;
    }


    private void doYourUpdate() {
        // TODO implement a refresh
        refreshLayout.setRefreshing(false); // Disables the refresh icon
    }
}