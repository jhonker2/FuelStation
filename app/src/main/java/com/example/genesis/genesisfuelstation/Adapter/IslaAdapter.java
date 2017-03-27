package com.example.genesis.genesisfuelstation.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.genesis.genesisfuelstation.R;

import java.util.ArrayList;

import model.Caja;

/**
 * Created by GENESIS on 17/03/2017.
 * Este adapter permite llenar una lista
 * para poder selecionar la isla y dejarla configurada por defecto
 */

public class IslaAdapter extends BaseAdapter {
    protected Activity activity;
    protected ArrayList<Caja> items;
    public TextView caja,codigo,punto, estacion;
    public Caja item;

    public IslaAdapter(Activity activity, ArrayList<Caja> items) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.list_islas, null);

        }else{

        }
        item = items.get(position);
        codigo = (TextView) vi.findViewById(R.id.textCodigoIsla);
        codigo.setText(String.valueOf(item.getCodigo()));
        caja = (TextView) vi.findViewById(R.id.textCaja);
        caja.setText(String.valueOf(item.getCaja()));
        punto = (TextView) vi.findViewById(R.id.textpunto);
        punto.setText(String.valueOf(item.getPunto()));
        estacion= (TextView) vi.findViewById(R.id.textEstacion);
        estacion.setText(String.valueOf(item.getEstacion()));

        return vi;
    }

}
