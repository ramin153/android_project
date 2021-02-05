package com.example.androidproject.recyclerview;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.R;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MapAdapter extends RecyclerView.Adapter<MapAdapter.MapHolder> {
    Context context;
    MyOnClick delete;
    List<MapData> mapDatas;

    public MapAdapter(Context context, MyOnClick delete, List<MapData> mapDatas) {
        this.context = context;
        this.delete = delete;
        this.mapDatas = mapDatas;
    }

    @NonNull
    @Override
    public MapHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_mapitem,parent,false);

        return new MapHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MapHolder holder, int position) {
        MapData mapData = mapDatas.get(position);
        holder.textView.setText(mapData.detail);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Do you want to delete it?")
                        .setConfirmText("Yes")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(final SweetAlertDialog sDialog) {
                                AsyncTask.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        delete.click(position,mapData);
                                        sDialog.cancel();
                                    }
                                });

                            }
                        })
                        .setCancelText("No!")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                            }
                        }).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mapDatas.size();
    }

    static class MapHolder extends RecyclerView.ViewHolder
    {
        TextView textView;
        Button button;
        public MapHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.recyclerMapItem_text);
            button = itemView.findViewById(R.id.recyclerMapItem_delete);
        }
    }
}
