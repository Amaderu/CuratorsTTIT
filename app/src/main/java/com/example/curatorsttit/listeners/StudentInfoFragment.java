package com.example.curatorsttit.listeners;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.example.curatorsttit.MainActivity;
import com.example.curatorsttit.R;
import com.example.curatorsttit.databinding.FragmentLoginBinding;
import com.example.curatorsttit.databinding.FragmentStudentInfoBinding;
import com.example.curatorsttit.models.Person;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.xml.stream.events.Attribute;

public class StudentInfoFragment extends Fragment {
    private int personID;
    private Toolbar toolbar;
    private Person person;
    FragmentStudentInfoBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() !=  null)
            personID = getArguments().getInt("personID", -1);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        person = gson.fromJson( getArguments().getString("person"),Person.class);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        toolbar = (Toolbar) getView().findViewById(R.id.toolbar);//.findViewById(R.id.toolbar);
        //toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case android.R.id.home:
                        getActivity().onBackPressed();
                        break;
                    case -1:
                        getActivity().onBackPressed();
                        break;
                    case android.R.id.accessibilitySystemActionBack:
                        getActivity().onBackPressed();
                        break;

                    default: break;
                }
            }
        });
        //toolbar.setNavigationOnClickListener(view -> getActivity().onBackPressed());

        toolbar.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
                menuInflater.inflate(R.menu.action_bar_edit, menu);

                menu.findItem(R.id.edit).setOnMenuItemClickListener(menuItem -> {
                    menu.findItem(R.id.done).setVisible(true);
                    menu.findItem(R.id.cancel).setVisible(true);
                    menu.findItem(R.id.edit).setVisible(false);
                    canEdit(true);
                    return onOptionsItemSelected(menuItem);
                });

                menu.findItem(R.id.cancel).setOnMenuItemClickListener(menuItem -> {
                    menu.findItem(R.id.edit).setVisible(true);
                    menu.findItem(R.id.cancel).setVisible(false);
                    menu.findItem(R.id.done).setVisible(false);
                    canEdit(false);
                    return onOptionsItemSelected(menuItem);
                });

                menu.findItem(R.id.done).setOnMenuItemClickListener(menuItem -> {
                    menu.findItem(R.id.edit).setVisible(true);
                    menu.findItem(R.id.cancel).setVisible(false);
                    menu.findItem(R.id.done).setVisible(false);
                    canEdit(false);
                    return onOptionsItemSelected(menuItem);
                });
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });
        //toolbar.inflateMenu(R.menu.action_bar_edit);
        //ActionBar bar = getActivity().setActionBar(android.widget.Toolbar);
        //bar.setCustomView(toolbar);
        //((MainActivity)getActivity()).setActionBar(toolbar);
        //toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateInterpolator()).start();
        //toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
        //ActionBar actionBar = ((MainActivity)getActivity()).getActionBar();
        //actionBar.
        //actionBar.setDisplayShowTitleEnabled(false);
    }

    void canEdit(boolean isEditable){
        RelativeLayout rootLayout = getView().findViewById(R.id.expandable);
        EditText snp,birth,years;
        snp = (EditText)rootLayout.findViewById(R.id.studentSNP);
        birth = (EditText)rootLayout.findViewById(R.id.studentBirth);
        years = (EditText)rootLayout.findViewById(R.id.studentYears);

        //snp.setInputType(InputType.TYPE_CLASS_TEXT);
        //birth.setInputType(InputType.TYPE_CLASS_DATETIME);
        //years.setInputType(InputType.TYPE_CLASS_NUMBER);
        snp.setEnabled(isEditable);
        birth.setEnabled(isEditable);
        years.setEnabled(isEditable);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStudentInfoBinding.inflate(inflater, container, false);
        //View view =inflater.inflate(R.layout.fragment_student_info, container, false);
        View view = binding.getRoot();
        binding.studentPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                String number =((EditText)v).getText().toString();
                callIntent.setData(Uri.parse("tel:"+number));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(callIntent);
            }
        });
        loadStudentInfo(person);
        return view;
    }
    public void show(View view){
        Toast.makeText(getContext(), "Show more", Toast.LENGTH_SHORT).show();
    }
    //TODO написать загрузку/редактирование данных студента
    private void loadStudentInfo(int personID){
        Toast.makeText(requireContext(),String.valueOf(personID), Toast.LENGTH_SHORT);
    }
    private void loadStudentInfo(Person person){
        binding.studentSNP.setText(person.getSNP());
        binding.studentEmail.setText(person.getEmail());
        binding.studentPhone.setText(person.getPhone());
        Toast.makeText(requireContext(),String.valueOf(person.getId()), Toast.LENGTH_SHORT);
    }

}