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

import model.Empresa;

/**
 * Created by GENESIS on 04/03/2017.
 */

public class EmpresaAdapter extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<Empresa> items;
    public TextView empresa,codigo,periodo;
    public Empresa item;

    public EmpresaAdapter(Activity activity, ArrayList<Empresa> items){
        this.activity= activity;
        this.items= items;
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
            vi = inflater.inflate(R.layout.list_empresa, null);

        }else{

        }
        item = items.get(position);
        codigo = (TextView) vi.findViewById(R.id.textCodigo);
        codigo.setText(String.valueOf(item.getCodigo()));
        empresa = (TextView) vi.findViewById(R.id.textCaja);
        empresa.setText(String.valueOf(item.getEmpresa()));
        periodo = (TextView) vi.findViewById(R.id.textperio);
        periodo.setText(String.valueOf(item.getPeriodo()));
        return vi;
    }
}
