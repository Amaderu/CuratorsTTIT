package com.example.curatorsttit.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.curatorsttit.R;
import com.example.curatorsttit.adapters.EventsRecViewAdapter;
import com.example.curatorsttit.ui.events.ListEventFragment;

public class CustomDialog extends DialogFragment {

    private CharSequence[] items;
    private ListEventFragment fragment;
    private EventsRecViewAdapter adapter;
    int pos;

    public CustomDialog(ListEventFragment fragment, CharSequence[] items, int pos) {
        this.fragment = fragment;
        this.items = items;
        this.pos = pos;

        this.adapter = (EventsRecViewAdapter)((RecyclerView)fragment.getView().findViewById(R.id.recyclerEvents)).getAdapter();

    }

    @SuppressLint("NotifyDataSetChanged")
    @NonNull
    public Dialog onCreateDialog(Bundle state){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        CharSequence[] fields = {
                "Изменить",
                "Удалить"
        };
        builder.setItems(fields, (dialogInterface, i) -> {
            switch (i) {
                case 0: {
                    Log.d("Custom", "onCreateDialog: update"+adapter.getItemId(0));
                    dismiss();
                    break;
                }
                case 1: {
                    Log.d("Custom", "onCreateDialog: delete");
                    adapter.getEventList().remove(0);
                    adapter.notifyDataSetChanged();

                    dismiss();
                    break;
                }
                default:{
                    dismiss();
                }
            }
        });

        Dialog dialog  = builder.create();

        //dialog.getWindow().setBackgroundDrawableResource(R.drawable.cr15dp);

        return dialog;
    }
}
