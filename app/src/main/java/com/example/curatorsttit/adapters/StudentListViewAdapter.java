package com.example.curatorsttit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.curatorsttit.MainActivity;
import com.example.curatorsttit.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StudentListViewAdapter extends BaseAdapter {
    // Declare Variables

    Context mContext;
    LayoutInflater inflater;
    //лист для отображения
    private List<String> StudentsList = null;
    //лист для всех объектов
    private ArrayList<String> arraylist;

    public StudentListViewAdapter(Context context, List<String> StudentsList) {
        mContext = context;
        this.StudentsList = StudentsList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<String>();
        this.arraylist.addAll(StudentsList);
    }
    public void setGroupVisability(int visability) {
        this.groupVisability = visability;
    }

    public class ViewHolder {
        TextView name;
        TextView group;
    }
    private int groupVisability = View.GONE;

    @Override
    public int getCount() {
        return StudentsList.size();
    }

    @Override
    public String getItem(int position) {
        return StudentsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            //view = inflater.inflate(android.R.layout.simple_list_item_1, null);
            view = inflater.inflate(R.layout.student_list_item, null);
            // Locate the TextViews in listview_item.xml
            //holder.name = (TextView) view.findViewById(android.R.id.text1);
            holder.name = (TextView) view.findViewById(R.id.studentFIO);
            holder.group = (TextView) view.findViewById(R.id.studentGroup);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(StudentsList.get(position).toString());
        holder.group.setText("89");
        holder.group.setVisibility(groupVisability);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity m = ((MainActivity)mContext);
                m.loadFragment(m.whichFragment(R.id.fragment_student_info));
            }
        });
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        StudentsList.clear();
        if (charText.length() == 0) {
            StudentsList.addAll(arraylist);
        } else {
            for (String wp : arraylist) {
                if (wp.toLowerCase(Locale.getDefault()).contains(charText)) {
                    StudentsList.add(wp);
                }
            }
        }
        //notifyDataSetChanged();
    }

}
