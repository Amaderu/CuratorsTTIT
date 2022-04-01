package com.example.curatorsttit.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.ThemedSpinnerAdapter;


import com.example.curatorsttit.models.Groups;

import java.util.List;

public class GroupSpinnerAdapter extends ArrayAdapter<Groups> {

    LayoutInflater inflater;

    public GroupSpinnerAdapter(Activity context, int resouceId, int textviewId, List<Groups> list){
        super(context,resouceId,textviewId,list);
        inflater = context.getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Groups rowItem = getItem(position);
        View rowview = inflater.inflate(android.R.layout.simple_spinner_item,null,false);
        TextView txtTitle = (TextView) rowview.findViewById(android.R.id.text1);
        txtTitle.setText(rowItem.getNumber());

        return rowview;
    }
}
