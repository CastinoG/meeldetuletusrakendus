package com.example.meeldetuletuserakendus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    Activity activity;
    private ArrayList meeldetuletus_id, meeldetuletus_pealkiri, meeldetuletus_kirjeldus, meeldetuletus_kuupaev, meeldetuletus_kell;

    Animation translate_anim;


    CustomAdapter(Activity activity,
                  Context context,
                  ArrayList meeldetuletus_id,
                  ArrayList meeldetuletus_pealkiri,
                  ArrayList meeldetuletus_kirjeldus,
                  ArrayList meeldetuletus_kuupaev,
                  ArrayList meeldetuletus_kell) {
        this.activity = activity;
        this.context = context;
        this.meeldetuletus_id = meeldetuletus_id;
        this.meeldetuletus_pealkiri = meeldetuletus_pealkiri;
        this.meeldetuletus_kirjeldus = meeldetuletus_kirjeldus;
        this.meeldetuletus_kuupaev = meeldetuletus_kuupaev;
        this.meeldetuletus_kell = meeldetuletus_kell;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.meeldetuletus_id_txt.setText(String.valueOf(meeldetuletus_id.get(position)));
        holder.meeldetuletus_pealkiri_txt.setText(String.valueOf(meeldetuletus_pealkiri.get(position)));
        holder.meeldetuletus_kirjeldus_txt.setText(String.valueOf(meeldetuletus_kirjeldus.get(position)));
        holder.meeldetuletus_kuupaev_txt.setText(String.valueOf(meeldetuletus_kuupaev.get(position)));
        holder.meeldetuletus_kell_txt.setText(String.valueOf(meeldetuletus_kell.get(position)));
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", String.valueOf(meeldetuletus_id.get(position)));
                intent.putExtra("pealkiri", String.valueOf(meeldetuletus_pealkiri.get(position)));
                intent.putExtra("kirjeldus", String.valueOf(meeldetuletus_kirjeldus.get(position)));
                intent.putExtra("kuupaev", String.valueOf(meeldetuletus_kuupaev.get(position)));
                intent.putExtra("kell", String.valueOf(meeldetuletus_kell.get(position)));
                activity.startActivityForResult(intent, 1);

            }

        });

    }

    @Override
    public int getItemCount() {
        return meeldetuletus_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView meeldetuletus_id_txt, meeldetuletus_pealkiri_txt, meeldetuletus_kirjeldus_txt, meeldetuletus_kuupaev_txt, meeldetuletus_kell_txt;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            meeldetuletus_id_txt = itemView.findViewById(R.id.meeldetuletus_id_txt);
            meeldetuletus_pealkiri_txt = itemView.findViewById(R.id.meeldetuletus_pealkiri_txt);
            meeldetuletus_kirjeldus_txt = itemView.findViewById(R.id.meeldetuletus_kirjeldus_txt);
            meeldetuletus_kuupaev_txt = itemView.findViewById(R.id.meeldetuletus_kuupaev_txt);
            meeldetuletus_kell_txt = itemView.findViewById(R.id.meeldetuletus_kell_txt);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            mainLayout.setAnimation(translate_anim);
        }
    }
}
