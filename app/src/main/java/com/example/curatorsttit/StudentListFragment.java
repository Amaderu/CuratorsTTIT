package com.example.curatorsttit;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.text.Transliterator;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Parcelable;
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
import android.widget.TextView;

import com.example.curatorsttit.adapters.GroupSpinnerAdapter;
import com.example.curatorsttit.adapters.StudentListViewAdapter;
import com.example.curatorsttit.adapters.StudentsRecViewAdapter;
import com.example.curatorsttit.common.DataGenerator;
import com.example.curatorsttit.models.Group;
import com.example.curatorsttit.models.Person;
import com.example.curatorsttit.network.ApiService;
import com.example.curatorsttit.ui.login.LoginFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Enumeration;
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
    RecyclerView studentsList;
    StudentListViewAdapter studentListAdapter;
    StudentsRecViewAdapter studentRecAdapter;
    SearchView editsearch;
    SwipeRefreshLayout refreshLayout;
    Toolbar toolbar;
    SearchView.OnQueryTextListener queryTextListener;
    Spinner groups;
    List<String> ArraySpinnerGroup = new ArrayList<String>();
    List<Group> arraySpinnerGroup2 = new ArrayList<Group>();
    List<String> listStudents = new ArrayList<>();
    List<Person> listStudents2 = new ArrayList<>();


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
        //namesList = view.findViewById(R.id.lvMain);
        refreshLayout = view.findViewById(R.id.refresh);
        studentsList = view.findViewById(R.id.recyclerStudents);
        //Адаптер для студентов
        initRecyclerView();
        initRefreshLayout();
        /*studentListAdapter = new StudentListViewAdapter(getContext(), listStudents);
        //namesList.setAdapter(studentListAdapter);
        //переделка на recyclerView
        studentRecAdapter = new StudentsRecViewAdapter(listStudents2);
        studentsList.setAdapter(studentRecAdapter);
        studentRecAdapter.setItems(listStudents2);*/


        //Настройка toolbar
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        initToolbar();
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //TODO Проверить работу поиска спиннера после создания
        groups = toolbar.findViewById(R.id.spinner_groups);
        initSpinner();
        //Заполнение групп куратора
        curator_id = requireActivity().getPreferences(Context.MODE_PRIVATE).getInt("CURATOR_ID", -1);
        //FIXME Заменить мок на загрузку loadCuratorGroup()
        mockLoadCuratorGroup();
        //TODO написать адаптер для групп реализующий логику интерфейса
        groups.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int myPosition, long myID) {
                Log.i("renderSpinner -> ", "onItemSelected: " + myPosition + "/" + myID);
                //String selectedGroup = ((TextView) parentView.getChildAt(0)).getText().toString();
                updateStudentsList(arraySpinnerGroup2.get(myPosition).getId());
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
        //mockLoadStudents();
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
    //TODO проверить в api проблемы при запросе давнных от имени Кирилла
    private void loadCuratorGroup(int curator_id) {
        ApiService.getInstance().getApi().getGroupsByCuratorId(curator_id).enqueue(new Callback<List<Group>>() {
            @Override
            public void onResponse(Call<List<Group>> call, Response<List<Group>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateGroupSpinner(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Group>> call, Throwable t) {
                t.printStackTrace();
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
                }
            }

            @Override
            public void onFailure(Call<List<Person>> call, Throwable t) {
                t.printStackTrace();
                refreshLayout.setRefreshing(false);
            }
        });
    }



    private void mockLoadCuratorGroup() {
        List<Group> newGroups = DataGenerator.mockGenerateGroup();
        updateGroupSpinner(newGroups);
    }
    private void mockLoadStudents() {
        List<Person> newStudents = DataGenerator.mockGenerateStudents((Group)groups.getSelectedItem());
        /*listStudents.clear();
        listStudents2.clear();*/
        refreshLayout.setRefreshing(false);
        updateStudentRecycler(newStudents);
    }
    private void mockLoadStudentsStringAdapter(int groupId){
        ApiService.getInstance().getApi().getStudentsByGroup(groupId).enqueue(new Callback<List<Person>>() {
            @Override
            public void onResponse(Call<List<Person>> call, Response<List<Person>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listStudents.clear();
                    for (Person p :
                            response.body()) {
                        String FIO = p.getSurname() + p.getName() + p.getPatronymic();
                        listStudents.add(FIO);
                    }
                    studentListAdapter.notifyDataSetChanged();
                }
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Person>> call, Throwable t) {
                t.printStackTrace();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void updateGroupSpinner(List<Group> newGroups) {
        if (groups.getSelectedItem() != null) {
            String mySelected = groups.getSelectedItem().toString();
            Log.i("TPRenderECommerce_Dialogue -> ", "updateInitSpinners -> mySelected: " + mySelected);
        }

        ArraySpinnerGroup.clear();
        arraySpinnerGroup2.clear();
        for (Group g :
                newGroups) {
            ArraySpinnerGroup.add(g.getNumber());
        }
        arraySpinnerGroup2.addAll(newGroups);

        ((BaseAdapter) groups.getAdapter()).notifyDataSetChanged();
        groups.invalidate();
        groups.setSelection(0);
        Log.i("Spinner", "UpdateStudentsList: " + "Success getGroup" +
                groups.getSelectedItem().toString());
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateStudentRecycler(List<Person> persons) {
        listStudents2 = persons;
        StudentsRecViewAdapter adapter =  (StudentsRecViewAdapter)studentsList.getAdapter();
        adapter.updateStudentList(persons);
        Log.i("RecyclerView", "updateStudentRecycler: "+String.valueOf(adapter.getAllItemCount()));
    }

    private void updateStudentsList(int groupId) {
        if(groups.getSelectedItem() == null) {
            refreshLayout.setRefreshing(false);
            return;
        }
        //Fixme Почему-то не отображаются элементы
        // в дальнейшем заменить на loadStudents(groupId);
        mockLoadStudents();
    }


    private void initToolbar(){
        toolbar.addView(getLayoutInflater().inflate(R.layout.toolbar, null));
        queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                studentListAdapter.notifyDataSetChanged();
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
        GroupSpinnerAdapter spinnerArrayAdapter = new GroupSpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item,android.R.id.text1, arraySpinnerGroup2);
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
        refreshLayout.setOnRefreshListener(() -> updateStudentsList(1));
    }

    // FixMe  доработать прослушиватель нажатий на элемент
    //  при обновлении данных приходят новые объекты и equal не подходит к сравнению
    private void initRecyclerView(){
        studentRecAdapter = new StudentsRecViewAdapter(listStudents2, new StudentsRecViewAdapter.onStudentListener() {
            @Override
            public void onStudentClick(Person person) {
                navigateToStudentInfo(person);
            }
        });
        //когда размер элементов списка одинаковый (высота/ширина) - true.
        studentsList.setHasFixedSize(false);
        studentsList.setAdapter(studentRecAdapter);
    }

    // FixMe  доработать передачу данных пользователя
    void navigateToStudentInfo(Person person){
        Log.d("RecyclerView", "onStudentClick: "+person.getSNP());
        MainActivity m = ((MainActivity)requireActivity());
        Fragment toFragment = m.whichFragment(R.id.fragment_student_info);
        Bundle bundle = new Bundle();
        /*Person selectPerson = listStudents2.stream()
                .filter(searchPerson -> searchPerson.equals(person))
                .findFirst()
                .orElse(null);*/
        bundle.putInt("personID", person.getId());

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String personParce = gson.toJson(person);
        bundle.putString("person", personParce);
        toFragment.setArguments(bundle);
        m.loadFragment(toFragment);
    }
}