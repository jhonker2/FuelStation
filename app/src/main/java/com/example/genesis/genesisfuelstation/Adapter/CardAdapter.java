package com.example.genesis.genesisfuelstation.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.genesis.genesisfuelstation.R;

import java.util.ArrayList;
import java.util.List;

import model.Caja;
import model.Surtidor;

/**
 * Created by GENESIS on 07/03/2017.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    //List<Caja> items;

    protected Activity activity;
    protected ArrayList<Caja> items;
    protected ArrayList<Surtidor> items2;
    public TextView numero_caja;
    public ImageButton btn_isla;
    public Caja item;
    public static String isla;
   /* public CardAdapter(String[] names){
        super();
        items = new ArrayList<Caja>();
        for(int i =0; i<names.length; i++){
            Caja item = new Caja();
            item.setCaja(names[i]);
            items.add(item);
        }
    }*/
    public CardAdapter(Activity activity, ArrayList<Caja> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_card_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Caja list =  items.get(position);
        holder.textViewName.setText(list.getCaja());
    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewName;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.textsurtidor);

        }
    }


}
