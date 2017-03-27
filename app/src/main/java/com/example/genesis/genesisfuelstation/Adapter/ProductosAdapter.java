package com.example.genesis.genesisfuelstation.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.genesis.genesisfuelstation.R;
import com.github.snowdream.android.widget.SmartImageView;
import com.loopj.android.http.*;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import model.Productos;
import model.Surtidor;

import static com.example.genesis.genesisfuelstation.LoginActivity.ip_Server;

/**
 * Created by GENESIS on 08/03/2017.
 */

public class ProductosAdapter extends BaseAdapter {
    protected Activity activity;
    protected ArrayList<Productos> items;

    public TextView nombre;
    public SmartImageView imgProd;
    public Productos item;
    private static LayoutInflater inflater=null;

    public ProductosAdapter(Activity activity, ArrayList<Productos> items) {
        this.activity = activity;
        this.items = items;
       // inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
            vi = inflater.inflate(R.layout.item_producto, null);
        }
        item = items.get(position);

        nombre = (TextView) vi.findViewById(R.id.nombre_producto);
        nombre.setText(String.valueOf(item.getNombre()));
        imgProd = (SmartImageView) vi.findViewById(R.id.imgProducto);
        Rect re = new Rect(imgProd.getLeft(),imgProd.getTop(),imgProd.getRight(), imgProd.getBottom());
        imgProd.setImageUrl("http://"+ip_Server+":90"+item.getImagen(),re);
        return vi;
    }


}
