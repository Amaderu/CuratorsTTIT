package com.example.curatorsttit;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.security.keystore.KeyGenParameterSpec;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;

import com.example.curatorsttit.adapters.GroupSpinnerAdapter;
import com.example.curatorsttit.adapters.StudentListViewAdapter;
import com.example.curatorsttit.adapters.StudentsRecViewAdapter;
import com.example.curatorsttit.common.DataGenerator;
import com.example.curatorsttit.models.Group;
import com.example.curatorsttit.models.Person;
import com.example.curatorsttit.network.ApiService;
import com.example.curatorsttit.ui.StudentInfoActivity;
import com.example.curatorsttit.ui.login.LoginFragment;
import com.example.curatorsttit.ui.students.StudentInfoFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentListFragment extends Fragment {
    private static final String CURATOR_ID = "CURATOR_ID";
    private int curatorId;
    private String username;

    public StudentListFragment() {
        // Required empty public constructor
    }

    ListView namesList;
    RecyclerView studentsList;
    StudentListViewAdapter studentListAdapter;
    StudentsRecViewAdapter studentRecAdapter;
    SearchView editsearch;
    SwipeRefreshLayout refreshLayout;
    Toolbar toolbar;
    SearchView.OnQueryTextListener queryTextListener;
    Spinner groups;
    List<Group> arraySpinnerGroup = new ArrayList<Group>();
    List<Person> listStudents = new ArrayList<>();
    SharedPreferences sharedPreferences;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            username = getArguments().getString(getString(R.string.user_key));
            curatorId = getArguments().getInt(CURATOR_ID);
        }
        //getActivity().getActionBar();
    }

    @Override
    public void onStart() {
        super.onStart();
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
        refreshLayout = view.findViewById(R.id.refresh);
        refreshLayout.setRefreshing(true);
        studentsList = view.findViewById(R.id.recyclerStudents);
        //Адаптер для студентов
        initRecyclerView();
        initRefreshLayout();
        //Configure toolbar
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        initToolbar();
        groups = toolbar.findViewById(R.id.spinner_groups);
        initSpinner();
        //load curator id from prefs;
        try {
            getAppDataFromPrefs();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //curatorId = requireActivity().getPreferences(Context.MODE_PRIVATE).getInt("CURATOR_ID", -1);
        //FIXME Заменить мок на загрузку
        //loadCuratorGroup(curatorId);
        mockLoadCuratorGroup();
        groups.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int myPosition, long myID) {
                Log.i("renderSpinner -> ", "onItemSelected: " + myPosition + "/" + myID);
                //String selectedGroup = ((TextView) parentView.getChildAt(0)).getText().toString();
                updateStudentsList(arraySpinnerGroup.get(myPosition).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Log.i("renderSpinner -> ", "onNothingSelected: " +
                        "nothing selected");
            }

        });
        ImageView imageView = (ImageView) toolbar.findViewById(R.id.logo_icon);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        return view;
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
        // reading a value
        username = sharedPreferences.getString(getString(R.string.user_key), "");
        curatorId = sharedPreferences.getInt(CURATOR_ID, -1);
        if(curatorId != -1 && !username.isEmpty())
            Log.i("LoadData", "getAppDataFromPrefs: successes");
    }

    void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Выход из аккаунта").setMessage("Вы точно хотите выйти из аккаунта?")
                .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sharedPreferences.edit().remove(getString(R.string.user_key)).remove(CURATOR_ID).apply();
                        Fragment toFragment = new LoginFragment();
                        ((NavigationHost) getActivity()).navigateTo(toFragment, false); // Navigate to the next Fragment
                    }
                })
                .setNegativeButton("Нет", null);
        builder.create().show();
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
    //TODO проверить в api проблемы при запросе давнных от имени Кирилла
    private void loadCuratorGroup(int curator_id) {
        final String ErrorMsg = getString(R.string.error);
        ApiService.getInstance().getApi().getGroupsByCuratorId(curator_id).enqueue(new Callback<List<Group>>() {
            @Override
            public void onResponse(Call<List<Group>> call, Response<List<Group>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateGroupSpinner(response.body());
                }
                else {
                    refreshLayout.setRefreshing(false);
                    showError(ErrorMsg);
                }
            }

            @Override
            public void onFailure(Call<List<Group>> call, Throwable t) {
                t.printStackTrace();
                refreshLayout.setRefreshing(false);
                showError(ErrorMsg);
            }
        });
    }

    //TODO проверить в api загрузку
    private void loadStudents(int groupId) {
        ApiService.getInstance().getApi().getStudentsByGroup(groupId).enqueue(new Callback<List<Person>>() {
            @Override
            public void onResponse(Call<List<Person>> call, Response<List<Person>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateStudentRecycler(response.body());
                    refreshLayout.setRefreshing(false);
                } else {
                    refreshLayout.setRefreshing(false);
                    showError(getString(R.string.error));
                }
            }

            @Override
            public void onFailure(Call<List<Person>> call, Throwable t) {
                t.printStackTrace();
                refreshLayout.setRefreshing(false);
                showError(getString(R.string.error));
            }
        });
    }

    private void mockLoadCuratorGroup() {
        List<Group> newGroups = DataGenerator.mockGenerateGroup();
        updateGroupSpinner(newGroups);
    }
    private void mockLoadStudents() {
        List<Person> newStudents = DataGenerator.mockGenerateStudents((Group)groups.getSelectedItem());
        updateStudentRecycler(newStudents);
    }

    private void updateGroupSpinner(List<Group> newGroups) {
        if (groups.getSelectedItem() != null) {
            String mySelected = groups.getSelectedItem().toString();
            Log.i("TPRenderECommerce_Dialogue -> ", "updateInitSpinners -> mySelected: " + mySelected);
        }
        arraySpinnerGroup.clear();
        arraySpinnerGroup.addAll(newGroups);
        ((BaseAdapter) groups.getAdapter()).notifyDataSetChanged();
        groups.invalidate();
        groups.setSelection(0);
        Log.i("Spinner", "UpdateStudentsList: " + "Success getGroup" +
                groups.getSelectedItem().toString());
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateStudentRecycler(List<Person> persons) {
        listStudents = persons;
        StudentsRecViewAdapter adapter =  (StudentsRecViewAdapter)studentsList.getAdapter();
        adapter.updateStudentList(persons);
        Log.i("RecyclerView", "updateStudentRecycler: "+String.valueOf(adapter.getAllItemCount()));

        refreshLayout.setRefreshing(false);
    }

    private void updateStudentsList(int groupId) {
        if(groups.getSelectedItem() == null) {
            refreshLayout.setRefreshing(false);
            return;
        }
        //FIXME в дальнейшем заменить на
        //loadStudents(groupId);
        mockLoadStudents();
    }


    private void initToolbar(){
        toolbar.addView(getLayoutInflater().inflate(R.layout.toolbar, null));
        queryTextListener = new SearchView.OnQueryTextListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public boolean onQueryTextSubmit(String query) {
                studentRecAdapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (studentRecAdapter != null)
                    studentRecAdapter.filter(newText);
                if (!newText.isEmpty()) {
                    studentRecAdapter.setGroupVisibility(View.VISIBLE);
                    //adapter.notifyDataSetChanged();
                } else {
                    studentRecAdapter.setGroupVisibility(View.INVISIBLE);
                    //adapter.notifyDataSetChanged();
                }
                return false;
            }
        };
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
    }

    private void initSpinner() {
        GroupSpinnerAdapter spinnerArrayAdapter = new GroupSpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item,android.R.id.text1, arraySpinnerGroup);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groups.setAdapter(spinnerArrayAdapter);
    }

    private void initRefreshLayout(){
        refreshLayout.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp(@NonNull SwipeRefreshLayout parent, @Nullable View child) {
                if (child != null) {
                    return child.canScrollVertically(-1);
                }
                return false;
            }
        });
        refreshLayout.setOnRefreshListener(() -> {
            if(groups.getSelectedItem() ==  null)  return;
            Group selectedGroup = ((Group) groups.getSelectedItem());
            updateStudentsList(selectedGroup.getId());
        });
    }

    private void initRecyclerView(){
        studentRecAdapter = new StudentsRecViewAdapter(listStudents, new StudentsRecViewAdapter.onStudentListener() {
            @Override
            public void onStudentClick(Person person) {
                navigateToStudentInfo(person);
            }
        });
        studentsList.setHasFixedSize(false);
        studentsList.setAdapter(studentRecAdapter);
    }


    void navigateToStudentInfo(Person person){
        Log.d("RecyclerView", "onStudentClick: "+person.getSNP());
        //MainActivity m = ((MainActivity)requireActivity());
        //Fragment toFragment = m.whichFragment(R.id.fragment_student_info);
        //Bundle bundle = new Bundle();
        //bundle.putInt("personID", person.getId());
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String personParce = gson.toJson(person);
        //bundle.putString("person", personParce);
        //toFragment.setArguments(bundle);
        //m.loadFragment(toFragment);

        Intent intent = new Intent(getContext(), StudentInfoActivity.class);
        intent.putExtra("personID", person.getId());
        intent.putExtra("person", personParce);

        getContext().startActivity(intent);

    }
}