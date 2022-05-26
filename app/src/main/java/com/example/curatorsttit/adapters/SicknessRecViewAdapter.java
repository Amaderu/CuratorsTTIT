package com.example.curatorsttit.adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.curatorsttit.R;
import com.example.curatorsttit.common.DateConverter;
import com.example.curatorsttit.models.Misses;
import com.example.curatorsttit.models.Person;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class SicknessRecViewAdapter extends RecyclerView.Adapter<SicknessRecViewAdapter.SicknessViewHolder> {
    private onMissListener mListener;

    private List<Misses> missesList = new ArrayList<>();

    public void setmListener(onMissListener mListener) {
        this.mListener = mListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setMissesList(List<Misses> missesList) {
        this.missesList = missesList;
        this.notifyDataSetChanged();
    }

    public List<Misses> getMissesList() {
        return missesList;
    }

    public SicknessRecViewAdapter(List<Misses> missesList, onMissListener listener) {
        this.missesList = missesList;
        mListener = listener;
    }

    public SicknessRecViewAdapter(List<Misses> studentsList) {
        this.missesList = missesList;
    }

    public SicknessRecViewAdapter() {
    }

    @Override
    public SicknessViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.miss_list_item, parent, false);
        return new SicknessViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(SicknessViewHolder holder, int position) {
        //Аналог
        // holder.group.setText(studentsList.get(position).getFIO());
        Log.d("SicknessRecViewAdapter -> ", "onBindViewHolder: " + position);
        holder.bind(missesList.get(position));
    }

    @Override
    public int getItemCount() {
        return missesList == null ? 0 : missesList.size();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void updateMissesList(Collection<Misses> misses) {
        clearItems();
        missesList.addAll(misses);
        notifyItemRangeInserted(0,misses.size());
    }
    @SuppressLint("NotifyDataSetChanged")
    public void addItems(Collection<Misses> misses) {
        final int positionStart = missesList.size() + 1;
        missesList.addAll(misses);
        notifyItemRangeInserted(positionStart,misses.size());
        //notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addItem(Misses misses) {
        final int positionStart = missesList.size() + 1;
        missesList.add(misses);
        notifyItemRangeInserted(positionStart,1);
    }
    @SuppressLint("NotifyDataSetChanged")
    public void clearItems() {
        int oldCount = missesList.size();
        missesList.clear();
        notifyItemRangeRemoved(0, oldCount);
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setItems(Collection<Misses> newList) {
        missesList.clear();
        missesList.addAll(newList);
        this.notifyDataSetChanged();
    }

    class SicknessViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView sDateIllness;
        private TextView eDateIllness;
        private Button downloadScan;
        onMissListener listener;

        public SicknessViewHolder(View itemView) {
            super(itemView);
            sDateIllness = (TextView) itemView.findViewById(R.id.studentFIO);
            eDateIllness = (TextView) itemView.findViewById(R.id.studentGroup);
            downloadScan = (Button) itemView.findViewById(R.id.downloadMissScan);
        }

        public SicknessViewHolder(View itemView, onMissListener listener) {
            super(itemView);
            sDateIllness = (TextView) itemView.findViewById(R.id.sDateIllness);
            eDateIllness = (TextView) itemView.findViewById(R.id.eDateIllness);
            downloadScan = (Button) itemView.findViewById(R.id.downloadMissScan);
            this.listener = listener;
            if(listener != null)
                downloadScan.setOnClickListener(this);
        }

        public void bind(Misses miss) {
            sDateIllness.setText(DateConverter.getNyFormattedDate(miss.getIllnessDate().toString()));
            eDateIllness.setText(DateConverter.getNyFormattedDate(miss.getRecoveryDate().toString()));
        }

        @Override
        public void onClick(View v) {
            //listener.onStudentClick(getAdapterPosition());
            if(listener != null)
                listener.onMissClick(missesList.get(getAdapterPosition()));
        }
    }

    //клик на студента
    public interface onMissListener {
        //void onStudentClick(int position);
        void onMissClick(Misses misses);

    }
}
