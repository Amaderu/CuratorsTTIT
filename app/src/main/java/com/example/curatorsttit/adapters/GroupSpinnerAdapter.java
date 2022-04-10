package com.example.curatorsttit.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.curatorsttit.models.Group;

import java.util.List;

public class GroupSpinnerAdapter extends ArrayAdapter<Group> {

    LayoutInflater inflater;
    List<Group> list;

    public GroupSpinnerAdapter(Activity activity, int resouceId, int textviewId, List<Group> list){
        super(activity,resouceId,textviewId,list);
        inflater = activity.getLayoutInflater();
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Group rowItem = getItem(position);
        View rowview = inflater.inflate(android.R.layout.simple_spinner_item,null,false);
        TextView txtTitle = (TextView) rowview.findViewById(android.R.id.text1);
        txtTitle.setText(rowItem.getNumber());

        return rowview;
    }

    public void updateDate(List<Group> list){
        this.list = list;
        this.notifyDataSetChanged();
    }


}
