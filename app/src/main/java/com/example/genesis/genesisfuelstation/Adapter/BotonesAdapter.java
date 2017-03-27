package com.example.genesis.genesisfuelstation.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;

import com.example.genesis.genesisfuelstation.R;

import java.util.ArrayList;

import model.Identificadores;

/**
 * Created by GENESIS on 07/03/2017.
 */

public class BotonesAdapter extends BaseAdapter {
    protected Activity activity;
    public ArrayList<String> items= new ArrayList<String>();
    public ImageButton btn1,btn2,btn3;
    public String item;

    public BotonesAdapter(Activity activity, ArrayList<String> items) {
        this.activity = activity;
        this.items = items;

    }
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View vi =convertView;
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.item_botones_surtidor, null);
        }
        item = items.get(position);

        btn1 = (ImageButton) vi.findViewById(R.id.btn1);
        btn2 = (ImageButton) vi.findViewById(R.id.btn2);
        btn3 = (ImageButton) vi.findViewById(R.id.btn3);


        if(item.equalsIgnoreCase("A")){
            btn1.setVisibility(View.VISIBLE);
            btn2.setVisibility(View.VISIBLE);
            btn3.setVisibility(View.VISIBLE);
        }


        return vi;
    }
}
