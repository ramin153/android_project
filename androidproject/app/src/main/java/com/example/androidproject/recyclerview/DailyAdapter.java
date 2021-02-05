package com.example.androidproject.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.motion.widget.MotionScene;
import androidx.constraintlayout.motion.widget.TransitionBuilder;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.R;

import java.util.Arrays;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintSet;
public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.DayViewHolder>  {
    private Context context;
    private List<String> items;
    private MyOnClick mclick;

    private int positionClick = 0 ;
    private DayViewHolder dayHolder ;

    public DailyAdapter(Context context, MyOnClick click,String dayName) {
        this(context,Arrays.asList("SATURDAY","SUNDAY","MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY"),click, dayName);
    }

    public DailyAdapter(Context context, List<String> items, MyOnClick click,String dayName) {
        this.context = context;
        this.items = items;
        this.mclick = click;
        this.positionClick = (items.indexOf(dayName) == -1? 0:items.indexOf(dayName));
    }


    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_dailyname,parent,false);
        final MyOnClick theClick = new MyOnClick() {
            @Override
            public Object[] click(Object... items) {

                int thePos = (int) items[0];
                DayViewHolder holder = (DayViewHolder) items[1];

                changeClickLocation(thePos,holder);
                return new Object[1];

            }
        };



        DayViewHolder viewHolder = new DayViewHolder(v,theClick);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        holder.text.setText(items.get(position).substring(0,3));
        if(position == this.positionClick){
            dayHolder = holder;
            dayHolder.main.setBackground(context.getDrawable(R.drawable.bg_pdd_fullround));
            dayHolder.text.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void changeClickLocation(int thePos ,DayViewHolder holder)
    {

        if(thePos == positionClick)
            return;

        dayHolder.main.setBackground(context.getDrawable(R.drawable.bg_w_fullround));
        dayHolder.text.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        dayHolder = holder;
        positionClick = thePos;
        dayHolder.main.setBackground(context.getDrawable(R.drawable.bg_pdd_fullround));
        dayHolder.text.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        dayHolder.createTransition();
        mclick.click(items.get(thePos));

    }


    public static class DayViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView text;
        MotionLayout main;
        MyOnClick theClick;

        public DayViewHolder(@NonNull View itemView,MyOnClick theClick) {// click come from outside for handel the fragments but theClick create inside the adapter
            super(itemView);
            this.theClick = theClick;

            main = itemView.findViewById(R.id.recyclerDaily_main);
            text = itemView.findViewById(R.id.recyclerDaily_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {



            theClick.click((Integer) getAdapterPosition(),this);

        }

        public void    createTransition()
        {
            main.setTransition(R.id.start,R.id.end );
            main.setTransitionDuration(600);
            main.transitionToEnd();

        }


    }
}
