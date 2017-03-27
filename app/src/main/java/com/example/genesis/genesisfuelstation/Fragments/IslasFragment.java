package com.example.genesis.genesisfuelstation.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.genesis.genesisfuelstation.Adapter.IslaAdapter;
import com.example.genesis.genesisfuelstation.Adapter.IslasAdapter;
import com.example.genesis.genesisfuelstation.PrincipalActivity;
import com.example.genesis.genesisfuelstation.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import model.Caja;

import static com.example.genesis.genesisfuelstation.LoginActivity.ip_Server;

public class IslasFragment extends ListFragment implements AdapterView.OnItemClickListener {

    ListView Lislas;
    IslaAdapter Ia;
    ArrayList<Caja> items= new ArrayList<Caja>();
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.list_islas, container, false);
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Lislas = getListView();
        getListView().setOnItemClickListener(this);
        //new LoadIslas().execute();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (items.get(position).isSeleccionado()) {
            items.get(position).setSeleccionado(false);
        } else {
            items.get(position).setSeleccionado(true);
        }
        Ia.notifyDataSetChanged();
    }



}
