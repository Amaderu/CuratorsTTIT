package com.example.curatorsttit.ui.events;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.curatorsttit.MainActivity;
import com.example.curatorsttit.R;
import com.example.curatorsttit.adapters.EventsRecViewAdapter;
import com.example.curatorsttit.common.DataGenerator;
import com.example.curatorsttit.databinding.FragmentListEventBinding;
import com.example.curatorsttit.models.events.Event;
import com.example.curatorsttit.models.events.EventData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class ListEventFragment extends Fragment {

    FragmentListEventBinding binding;
    EventsRecViewAdapter eventsRecViewAdapter;
    RecyclerView eventRecyclerView;

    public ListEventFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO рассмотреть инициализацию с передачей параметров
    }

    @Override
    public void onResume() {
        super.onResume();
        updateEventRecycler(mockGetEvents());
    }

    List<EventData> mockGetEvents(){
        return DataGenerator.mockGetEventData();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateEventRecycler(List<EventData> events) {
        eventsRecViewAdapter.updateEventList(events);
        Log.i("RecyclerView", "updateEventRecycler: "+String.valueOf(eventsRecViewAdapter.getItemCount()));

        binding.refresh.setRefreshing(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_list_event, container, false);
        binding = FragmentListEventBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        //FixME нормальную инициализацию списка
        /*------------------------------------*/
        eventRecyclerView = view.findViewById(R.id.recyclerEvents);
        initRecView();
        initToolbar();
        /*------------------------------------*/
        binding.refresh.setOnRefreshListener(() -> {
            updateEventRecycler(mockGetEvents());
        });

        return view;
    }

    public class EventClicksListener implements EventsRecViewAdapter.onEventListener{

        @Override
        public void onEventClick(int eventAdapterPosition) {
            eventsListener(eventAdapterPosition);
        }

        @Override
        public void onEventLongClick(int eventAdapterPosition) {
            eventsListenerTwo(eventAdapterPosition);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    void initRecView(){
        eventsRecViewAdapter = new EventsRecViewAdapter();
        eventsRecViewAdapter.setmListener(new EventClicksListener());
        eventRecyclerView.setHasFixedSize(false);
        eventRecyclerView.setAdapter(eventsRecViewAdapter);
        eventsRecViewAdapter.notifyDataSetChanged();
    }

    void eventsListener(int position){
        List<EventData> loadedEvents = eventsRecViewAdapter.getEventList();
        Log.d("Events", "Dialog: \'open event\' \"" + loadedEvents.get(position).getName()+"\" позиция в адаптере " +String.valueOf(position));
        addNewEventActivity(loadedEvents.get(position));

    }

    void eventsListenerTwo(int position){
        AlertDialog.Builder alert = new AlertDialog.Builder(ListEventFragment.this.requireContext());
        CharSequence[] fields = new CharSequence[]{
                "Изменить", "Удалить"
        };
        List<EventData> loadedEvents = eventsRecViewAdapter.getEventList();
        alert.setItems(fields, (dialogInterface, i) -> {
            switch (i) {
                case 0: {
                    //TODO изменение в окне
                    Log.d("Events", "Dialog: \'"+fields[i]+ "\' \"" + loadedEvents.get(position).getName()+"\" позиция в адаптере " +String.valueOf(position));
                    addNewEventActivity(loadedEvents.get(position));
                    dialogInterface.dismiss();
                    break;
                }
                case 1: {
                    Log.d("Events", "Dialog: \'"+fields[i]+ "\' \"" + loadedEvents.get(position).getName()+"\" позиция в адаптере " +String.valueOf(position));
                    eventsRecViewAdapter.getEventList().remove(position);
                    eventsRecViewAdapter.notifyDataSetChanged();
                    dialogInterface.dismiss();
                    break;
                }
                default: {
                    dialogInterface.dismiss();
                }
            }
        });
        alert.show();
    }

    private void initToolbar(){
        binding.toolbar.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
                menuInflater.inflate(R.menu.action_bar_addevent, menu);

                menu.findItem(R.id.add).setOnMenuItemClickListener(menuItem -> {
                    addNewEventActivity(null);
                    return onOptionsItemSelected(menuItem);
                });
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });
    }

    private void addNewEventActivity(EventData event) {
        Intent intent = new Intent(getContext(), EventActivity.class);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String eventParce = gson.toJson(event);
        intent.putExtra("Event",eventParce);
        getContext().startActivity(intent);

    }

    private void addNewEventFragment() {
        //TODO Сделать фрагмент и заменить
        ((MainActivity)getActivity()).openActivivty(new Fragment(), true);

    }


}