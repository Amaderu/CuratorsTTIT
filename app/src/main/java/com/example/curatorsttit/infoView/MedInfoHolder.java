package com.example.curatorsttit.infoView;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.curatorsttit.databinding.MedInfoItemBinding;

class MedInfoHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    onInfoListener listener;
    MedInfoItemBinding binding;

    public MedInfoHolder(View itemView) {
        super(itemView);
        this.listener = view -> {
            TransitionManager.beginDelayedTransition((ViewGroup)view, new AutoTransition());
            view.setVisibility(view.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        };
        itemView.setOnClickListener(this);
    }
    public MedInfoHolder(MedInfoItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        this.listener = view -> {
            TransitionManager.beginDelayedTransition((ViewGroup)view, new AutoTransition());
            view.setVisibility(view.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        };
        itemView.setOnClickListener(this);
    }
    public MedInfoHolder(View itemView, onInfoListener listener) {
        super(itemView);
    }
    @Override
    public void onClick(View v) {
        //listener.onStudentClick(getAdapterPosition());
        ViewGroup constLay = (ViewGroup) v;
        View card = constLay.getChildAt(0);
        View relLay = ((ViewGroup)card).getChildAt(1);
        //listener.onItemClick(relLay);
        listener.onItemClick(binding.expandable2);
    }
}