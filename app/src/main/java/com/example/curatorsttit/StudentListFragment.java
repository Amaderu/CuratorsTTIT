package com.example.curatorsttit;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.curatorsttit.adapters.StudentListViewAdapter;
import com.example.curatorsttit.models.Groups;
import com.example.curatorsttit.network.ApiService;
import com.example.curatorsttit.ui.login.LoginFragment;
import com.example.curatorsttit.ui.login.MainFragment;

import org.mockito.internal.matchers.Null;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentListFragment extends Fragment {
    private static final String CURATOR_ID = "CURATOR_ID";
    private int curator_id;
    private String username;

    public StudentListFragment() {
        // Required empty public constructor
    }

    ListView namesList;
    StudentListViewAdapter adapter;
    SearchView editsearch;
    SwipeRefreshLayout refreshLayout;
    Toolbar toolbar;
    SearchView.OnQueryTextListener queryTextListener;
    Spinner groups;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            username = getArguments().getString(getString(R.string.user_key));
            curator_id = getArguments().getInt(CURATOR_ID);
        }
        //getActivity().getActionBar();
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshLayout = getView().findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(() -> UpdateStudentsList());
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
        View view = inflater.inflate(R.layout.fragment_student_list, container, false);
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
        //TODO добавить прослушиватель нажатий на элемент

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
                } else {
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
        //TODO Проверить работу поиска спиннера после создания
        groups = toolbar.findViewById(R.id.spinner_groups);
        ImageView imageView = (ImageView) toolbar.findViewById(R.id.logo_icon);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        return view;
    }

    void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Выход из аккаунта").setMessage("Вы точно хотите выйти из аккаунта?")
                .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        (requireActivity()).getPreferences(Context.MODE_PRIVATE).edit().remove(getString(R.string.user_key)).commit();
                        Fragment toFragment = new LoginFragment();
                        ((NavigationHost) getActivity()).navigateTo(toFragment, false); // Navigate to the next Fragment
                    }
                })
                .setNegativeButton("Нет", null);
        builder.create().show();
    }


    private void UpdateStudentsList() {
        /*ApiService.getInstance().getApi().getGroupsByCuratorId(curator_id).enqueue(new Callback<List<Groups>>() {
            @Override
            public void onResponse(Call<List<Groups>> call, Response<List<Groups>> response) {
                if (response.isSuccessful()) {
                    ArrayAdapter<String> adapter = ((ArrayAdapter<String>) groups.getAdapter());
                    adapter.clear();
                    for (Groups item :
                            response.body()) {
                        if(item.getNumber()!= null)
                            adapter.add(item.getNumber());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Groups>> call, Throwable t) {
                Toast.makeText(requireContext(), "Ошибка сети поиска групп", Toast.LENGTH_SHORT).show();
            }
        });*/
        List<Groups> newGroups = null;
        try {
            newGroups = ApiService.getInstance().getApi().getGroupsByCuratorId(curator_id).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (RuntimeException e) {
            e.printStackTrace();
        }
            ArrayAdapter<String> adapter = ((ArrayAdapter<String>) groups.getAdapter());
        adapter.clear();
        for (Groups item :
                newGroups) {
            if (item.getNumber() != null)
                adapter.add(item.getNumber());
        }

        // TODO написать обновление данных для групп и списка студентов
        //TODO в api проблемы при запросе давнных от имени Кирилла
        refreshLayout.setRefreshing(false); // Disables the refresh icon
    }
}