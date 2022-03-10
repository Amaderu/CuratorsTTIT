package com.example.curatorsttit.listeners;

import android.app.ActionBar;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.curatorsttit.MainActivity;
import com.example.curatorsttit.R;

import javax.xml.stream.events.Attribute;

public class StudentInfoFragment extends Fragment {

    android.widget.Toolbar toolbar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        //test
        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.addView(getLayoutInflater().inflate(R.layout.toolbar,null));

        //setSupportActionBar(toolbar);

        toolbar = (android.widget.Toolbar) getView().findViewById(R.id.toolbar);//.findViewById(R.id.toolbar);

        /*LinearLayout linLayout = new LinearLayout(getActivity().getBaseContext());
        linLayout.setOrientation(LinearLayout.VERTICAL);
        ViewGroup.LayoutParams linLayoutParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView textView = new TextView(getActivity().getBaseContext());
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        Spinner spinner = (Spinner)toolbar.findViewById(R.id.spinner_groups);*/

        //textView.setText(String.format("Студент %s", spinner.getSelectedItem().toString()));
        //bundle
        //toolbar.removeViewAt(0);
        //toolbar.addView(textView,linLayoutParam);

        toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material);
        //toolbar.setNavigationOnClickListener(view -> getActivity().onBackPressed());

        /*toolbar.addMenuProvider(new MenuProvider() {
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
        });*/
        //toolbar.inflateMenu(R.menu.action_bar_edit);
        //ActionBar bar = getActivity().setActionBar(android.widget.Toolbar);
        //bar.setCustomView(toolbar);
        ((MainActivity)getActivity()).setActionBar(toolbar);
        ActionBar actionBar = ((MainActivity)getActivity()).getActionBar();

        actionBar.setDisplayShowTitleEnabled(false);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
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
        super.onCreateOptionsMenu(menu, menuInflater);
    }
    /*@Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

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
        return inflater.inflate(R.layout.fragment_student_info, container, false);
    }
    public void show(View view){
        Toast.makeText(getContext(), "Show more", Toast.LENGTH_SHORT).show();
    }

}