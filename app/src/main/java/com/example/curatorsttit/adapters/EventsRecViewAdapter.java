package com.example.curatorsttit.adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.curatorsttit.R;
import com.example.curatorsttit.common.DateConverter;
import com.example.curatorsttit.models.events.Event;
import com.example.curatorsttit.models.events.EventData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class EventsRecViewAdapter extends RecyclerView.Adapter<EventsRecViewAdapter.EventsViewHolder>{

    private onEventListener mListener;

    private List<EventData> eventList = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    public void setEventList(List<EventData> eventList) {
        this.eventList = eventList;
        this.notifyDataSetChanged();
    }

    public List<EventData> getEventList() {
        return eventList;
    }

    public onEventListener getmListener() {
        return mListener;
    }

    public void setmListener(onEventListener mListener) {
        this.mListener = mListener;
    }

    public EventsRecViewAdapter() {
    }

    public EventsRecViewAdapter(List<EventData> eventList) {
        this.eventList = eventList;
    }

    public EventsRecViewAdapter(onEventListener mListener, List<EventData> eventList) {
        this.mListener = mListener;
        this.eventList = eventList;
    }

    @Override
    public EventsRecViewAdapter.EventsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list_item, parent, false);
        return new EventsRecViewAdapter.EventsViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(EventsRecViewAdapter.EventsViewHolder holder, int position) {
        Log.d("EventsRecViewAdapter -> ", "onBindViewHolder: " + position);
        holder.bind(eventList.get(position));
    }

    @Override
    public int getItemCount() {
        return eventList == null ? 0 : eventList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateEventList(Collection<EventData> events) {
        clearItems();
        eventList.addAll(events);
        notifyItemRangeInserted(0,eventList.size());
    }
    @SuppressLint("NotifyDataSetChanged")
    public void addItems(Collection<EventData> events) {
        final int positionStart = eventList.size() + 1;
        eventList.addAll(events);
        notifyItemRangeInserted(positionStart,events.size());
        //notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addItem(EventData events) {
        final int positionStart = eventList.size() + 1;
        eventList.add(events);
        notifyItemRangeInserted(positionStart,1);
    }
    @SuppressLint("NotifyDataSetChanged")
    public void clearItems() {
        int oldCount = eventList.size();
        eventList.clear();
        notifyItemRangeRemoved(0, oldCount);
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setItems(Collection<EventData> newList) {
        eventList.clear();
        eventList.addAll(newList);
        this.notifyDataSetChanged();
    }


    public class EventsViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener{

        private TextView eventName;
        private TextView eventDate;
        private TextView eventStatus;
        onEventListener listener;

        public EventsViewHolder(View itemView) {
            super(itemView);
            eventName = (TextView) itemView.findViewById(R.id.eventName);
            eventDate = (TextView) itemView.findViewById(R.id.eventDate);
            eventStatus = (TextView) itemView.findViewById(R.id.eventStatus);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }
        public EventsViewHolder(View itemView, EventsRecViewAdapter.onEventListener listener) {
            super(itemView);
            eventName = (TextView) itemView.findViewById(R.id.eventName);
            eventDate = (TextView) itemView.findViewById(R.id.eventDate);
            eventStatus = (TextView) itemView.findViewById(R.id.eventStatus);
            this.listener = listener;

            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        public void bind(EventData event) {
            eventName.setText(event.getName());
            SimpleDateFormat dateFormatter = new SimpleDateFormat(DateConverter.DATE_MY, Locale.getDefault());
            eventDate.setText(dateFormatter.format(event.getDate()));
            eventStatus.setText(event.getStatus());
        }

        @Override
        public boolean onLongClick(View v) {
            if(listener!=null)
                listener.onEventLongClick(getAdapterPosition());
            else return false;
            return true;
            //Toast.makeText(v.getContext(),Integer.toString(position),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onClick(View v) {
            if(listener!=null)
                listener.onEventClick(getAdapterPosition());
            //Toast.makeText(v.getContext(),Integer.toString(position),Toast.LENGTH_SHORT).show();
        }
    }
    //клик на мероприятие
    public interface onEventListener{
        //void onStudentClick(int position);
        void onEventClick(int eventAdapterPosition);
        void onEventLongClick(int eventAdapterPosition);
    }
}
