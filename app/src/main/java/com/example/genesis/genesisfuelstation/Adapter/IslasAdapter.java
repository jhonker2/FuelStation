package com.example.genesis.genesisfuelstation.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.genesis.genesisfuelstation.Activity.SurtidorActivity;
import com.example.genesis.genesisfuelstation.R;
import java.util.ArrayList;
import model.Caja;
import model.Surtidor;

/**
 * Created by GENESIS on 06/03/2017.
 */

public class IslasAdapter extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<Caja> items;

    public TextView numero_caja;
    public ImageButton btn_isla;
    public Caja item;


    public IslasAdapter(Activity activity, ArrayList<Caja> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getCodigo();
    }

    @Override
    public View getView( final int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.item_isla, null);
        }
        item = items.get(position);

        numero_caja = (TextView) vi.findViewById(R.id.txt_caja);
        numero_caja.setText(String.valueOf(item.getCaja()));
        btn_isla=(ImageButton) vi.findViewById(R.id.im_isla);


        btn_isla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(v.getContext(),items.get(position).getCodigo()+" - "+items.get(position).getCaja(),Toast.LENGTH_LONG).show();
                activity.startActivity(new Intent(activity, SurtidorActivity.class));
                SurtidorActivity.isla=items.get(position).getCodigo();
                SurtidorActivity.estacionS=items.get(position).getEstacion();
                SurtidorActivity.puntoS = items.get(position).getPunto();
                SurtidorActivity.cajaD = items.get(position).getCaja();

                //new LoadSurtidor().execute();

            }
        });



        return vi;
    }



}
