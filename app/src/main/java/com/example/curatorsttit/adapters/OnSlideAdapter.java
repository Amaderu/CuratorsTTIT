package com.example.curatorsttit.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.curatorsttit.R;
import com.google.android.material.color.ColorRoles;

import java.util.ResourceBundle;

public class OnSlideAdapter extends PagerAdapter {
    private Context ctx;
    LayoutInflater inflater;

    int pages[];

    int colors[];

    public OnSlideAdapter(Context ctx) {
        this.ctx = ctx;
        colors = new int[] {
                R.color.black,
                R.color.yellow,
                R.color.purple,
                R.color.orange,
        };
        pages = new int[] {
            R.drawable.oval
        };
    }

    @Override
    public int getCount() {
        return colors.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //inflater = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
        inflater = (LayoutInflater) LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.slides_layout,container,false);

        TextView textView = view.findViewById(R.id.slider_text);

        Resources res = ctx.getResources();
        String[] slides = res.getStringArray(R.array.slides);
        //String[] colors = res.getColorStateList(,);
        //sets texts
        textView.setText(slides[position]);
        int step = position % 4 + 1;
        if(step>=4) step = 0;
        view.findViewById(R.id.slide_root).setBackgroundResource(colors[position]);
        textView.setTextColor(res.getColor(colors[step]));

        container.addView(view);

        //return super.instantiateItem(container, position);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //super.destroyItem(container, position, object);
        //container.removeView((ConstraintLayout)object);
    }

    /*@Override
    public void destroyItem(@NonNull View container, int position, @NonNull Object object) {
        //super.destroyItem(container, position, object);
        container.removeView((ConstraintLayout)object);
    }*/
}
