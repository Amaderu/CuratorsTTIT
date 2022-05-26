package com.example.curatorsttit.ui.events;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.curatorsttit.R;
import com.example.curatorsttit.common.DateConverter;
import com.example.curatorsttit.databinding.ActivityEventBinding;
import com.example.curatorsttit.models.events.EventData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class EventActivity extends AppCompatActivity {
    ActivityEventBinding binding;
    List<String> educationModules = new ArrayList<>();
    ArrayAdapter<String> eventEducationModulesAdapter;
    Toolbar toolbar;
    EventData event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getIntent().getExtras();

        binding = ActivityEventBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        if(args != null){
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            event  = gson.fromJson( args.getString("Event"),EventData.class);
            if(event != null)
            bindData(event);
        }

        initListView();
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventEducationModulesAdapter.add(binding.spinner.getSelectedItem().toString());
            }
        });
        initToolbar();


        //binding.recyclerEducationModules.setAdapter();
    }
    void bindData(@NonNull EventData event){
        if (event == null) return;
        if (event.getName() != null)
            binding.eventName.setText(event.getName());
        if (event.getOrganizer() != null)
            binding.eventOrganizer.setText(event.getOrganizer());
        if(event.getDate() != null)
            binding.eventDate.setText(new SimpleDateFormat(DateConverter.DATE_MY, Locale.getDefault()).format(event.getDate()));
        //binding.eventStatus.setText(event.getStatus());
        //if(event.getStatus().lastIndexOf(((String)binding.eventStatus.getSelectedItem()))
        if(event.getStatus() != null && event.getStatus().toLowerCase().contains("проведено")){
            binding.eventStatus.setSelection(1);
        }
        else
            binding.eventStatus.setSelection(0);

        String eventCodes = "";
        if(event.getCodes() != null){
            eventCodes = event.getCodes().stream()
                    .map( n -> n.toString() )
                    .collect( Collectors.joining( ", " ));
        }
        binding.eventCodes.setText(eventCodes);
        if(event.getModules() != null){
            educationModules.addAll(event.getModules());
        }
    }
    void collectData(@NonNull EventData event){
        event.setName(binding.eventName.getText().toString());
        event.setOrganizer(binding.eventOrganizer.getText().toString());
        try {
            event.setDate(new SimpleDateFormat(DateConverter.DATE_MY, Locale.getDefault()).parse(binding.eventDate.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
            event.setDate(Calendar.getInstance().getTime());
        }
        if(binding.eventStatus.getSelectedItemPosition() == 1){
            event.setStatus("Проведено");
        }
        else event.setStatus("Не проведено");

        event.setCodes(Arrays.stream(binding.eventCodes.getText().toString().split(", ")).collect(Collectors.toList()));

        event.setModules(educationModules);
        Log.d("collectData", "collectData: \neventCodes size: "+String.valueOf(event.getCodes().size()+
                "\neventModules size: "+String.valueOf(event.getModules().size())));
    }


    void initToolbar(){
        toolbar = binding.toolbar;
        //toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case android.R.id.home:
                        onBackPressed();
                        break;
                    case -1:
                        onBackPressed();
                        break;
                    case android.R.id.accessibilitySystemActionBack:
                        onBackPressed();
                        break;

                    default: break;
                }
            }
        });
        toolbar.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
                menuInflater.inflate(R.menu.action_bar_edit_event, menu);

                menu.findItem(R.id.cancel).setOnMenuItemClickListener(menuItem -> {
                    //TODO cancel edit EventData
                    bindData(event);
                    return onOptionsItemSelected(menuItem);
                });

                menu.findItem(R.id.done).setOnMenuItemClickListener(menuItem -> {
                    //TODO EventData update create: -post if event == null -put if event != null
                    collectData(event);
                    onBackPressed();
                    return onOptionsItemSelected(menuItem);
                });
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    void initListView(){
        eventEducationModulesAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, educationModules);
        binding.listViewEducationModules.setAdapter(eventEducationModulesAdapter);
        binding.listViewEducationModules.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(EventActivity.this);
                alert.setTitle("Удаление").setMessage("Удалить модуль/направление из мероприятия?")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //parent.removeViewInLayout(view);
                                eventEducationModulesAdapter.remove(eventEducationModulesAdapter.getItem(position));
                                eventEducationModulesAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Нет", null)
                        .show();
                //parent.notifyAll();
            }
        });
    }

}