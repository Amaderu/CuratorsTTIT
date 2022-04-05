package com.example.curatorsttit.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.curatorsttit.R;
import com.example.curatorsttit.models.Person;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class StudentsRecViewAdapter extends RecyclerView.Adapter<StudentsRecViewAdapter.StudentsViewHolder> {
    private int groupVisibility = View.INVISIBLE;
    private onStudentListener mListener;

    private List<Person> studentsList = new ArrayList<>();
    //для отображения поиска
    private ArrayList<Person> arraylist;
    public StudentsRecViewAdapter(List<Person> studentsList,onStudentListener listener) {
        this.studentsList = studentsList;
        this.arraylist = new ArrayList<Person>();
        this.arraylist.addAll(studentsList);
        mListener = listener;
    }
    public StudentsRecViewAdapter(List<Person> studentsList) {
        this.studentsList = studentsList;
        this.arraylist = new ArrayList<Person>();
        this.arraylist.addAll(studentsList);
    }
    public StudentsRecViewAdapter() {
    }

    @Override
    public StudentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_list_item, parent, false);
        return new StudentsViewHolder(view, mListener);
    }
    public void setGroupVisibility(int visibility) {
        this.groupVisibility = visibility;
    }

    @Override
    public void onBindViewHolder(StudentsViewHolder holder, int position) {
        //Аналог
        // holder.group.setText(studentsList.get(position).getFIO());
        Log.d("StudentsRecViewAdapter -> ", "onBindViewHolder: " + position);
        holder.name.setText(arraylist.get(position).getSNP());
        holder.group.setText(arraylist.get(position).getGroup());
        holder.group.setVisibility(groupVisibility);
        //holder.bind(arraylist.get(position));
    }

    @Override
    public int getItemCount() {
        return arraylist == null ? 0 : arraylist.size();
    }

    public int getAllItemCount() {
        return studentsList == null ? 0 : studentsList.size();
    }

    public void setItems(Collection<Person> students) {
        clearItems();
        studentsList.addAll(students);
        arraylist.addAll(studentsList);
        notifyItemRangeInserted(0,arraylist.size());
    }

    public void clearItems() {
        int oldCount = arraylist.size();
        studentsList.clear();
        arraylist.clear();
        notifyItemRangeRemoved(0,oldCount);
    }
    public void updateStudentList(Collection<Person> newList) {
        studentsList.clear();
        arraylist.clear();
        studentsList.addAll(newList);
        arraylist.addAll(studentsList);
        this.notifyDataSetChanged();
    }

    class StudentsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView name;
        private TextView group;
        onStudentListener listener;

        public StudentsViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.studentFIO);
            group = (TextView) itemView.findViewById(R.id.studentGroup);
            itemView.setOnClickListener(this);
        }
        public StudentsViewHolder(View itemView, onStudentListener listener) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.studentFIO);
            group = (TextView) itemView.findViewById(R.id.studentGroup);
            this.listener = listener;

            itemView.setOnClickListener(this);
        }

        public void bind(Person person) {
            name.setText(person.getSNP());
            group.setText(person.getGroup());
            group.setVisibility(groupVisibility);
            //group.setVisibility(group.getVisibility() != View.VISIBLE ? View.VISIBLE : View.INVISIBLE);
        }

        @Override
        public void onClick(View v) {
            //listener.onStudentClick(getAdapterPosition());
            listener.onStudentClick(arraylist.get(getAdapterPosition()));
        }
    }
    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        arraylist.clear();
        if (charText.length() == 0) {
            arraylist.addAll(studentsList);
        } else {
            for (Person person : studentsList) {
                if (person.getSNP().toLowerCase(Locale.getDefault()).contains(charText)) {
                    arraylist.add(person);
                }
            }
        }
        notifyDataSetChanged();
    }
    //клик на студента
    public interface onStudentListener{
        //void onStudentClick(int position);
        void onStudentClick(Person person);
    }
}
