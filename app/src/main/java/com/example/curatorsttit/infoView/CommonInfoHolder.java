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

class CommonInfoHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private EditText studentSNP;
    private EditText studentBirth;
    CommonInfoItemBinding binding;
    onInfoListener listener;

    public CommonInfoHolder(CommonInfoItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        this.listener = view -> {
            TransitionManager.beginDelayedTransition((ViewGroup)view, new AutoTransition());
            view.setVisibility(view.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        };
        itemView.setOnClickListener(this);
    }


    public CommonInfoHolder(View itemView) {
        super(itemView);
        studentSNP = (EditText) itemView.findViewById(R.id.studentSNP);
        studentBirth = (EditText) itemView.findViewById(R.id.studentBirth);
        this.listener = view -> {
            TransitionManager.beginDelayedTransition((ViewGroup)view, new AutoTransition());
            view.setVisibility(view.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        };
        itemView.setOnClickListener(this);
    }
    static CommonInfoHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.common_info_item, parent, false);
        return new CommonInfoHolder(view);
    }

    public CommonInfoHolder(View itemView, onInfoListener listener) {
        super(itemView);
        studentSNP = (EditText) itemView.findViewById(R.id.studentSNP);
        studentBirth = (EditText) itemView.findViewById(R.id.studentBirth);
        this.listener = listener;
        this.listener = view -> {
            TransitionManager.beginDelayedTransition((ViewGroup)view, new AutoTransition());
            view.setVisibility(view.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        };
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //listener.onStudentClick(getAdapterPosition());
        ViewGroup constLay = (ViewGroup) v;
        View card = constLay.getChildAt(0);
        View relLay = ((ViewGroup)card).getChildAt(1);
        if(listener != null){
            listener.onItemClick(relLay);
        }
    }


}
