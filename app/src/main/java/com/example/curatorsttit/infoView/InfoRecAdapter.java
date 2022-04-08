package com.example.curatorsttit.infoView;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;

import com.example.curatorsttit.R;
import com.example.curatorsttit.databinding.CommonInfoItemBinding;
import com.example.curatorsttit.databinding.FragmentStudentInfoTwoBinding;

public class InfoRecAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    class DefaultHolder extends RecyclerView.ViewHolder {

        public DefaultHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:{
                 view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.common_info_item, parent, false);
                return new CommonInfoHolder(
                        CommonInfoItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false)
                );
            }
            case 1:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.med_info_item, parent, false);
                return new MedInfoHolder(view);
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            break;
        }
        return new DefaultHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case 0:
                CommonInfoHolder commonInfoHolder = (CommonInfoHolder) holder;
                break;

            case 1:
                MedInfoHolder medInfoHolder = (MedInfoHolder) holder;
                break;
        }
    }

}