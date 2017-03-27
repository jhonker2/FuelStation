package com.example.genesis.genesisfuelstation.Fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.genesis.genesisfuelstation.Adapter.EmpresaAdapter;
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

import model.Empresa;

import static com.example.genesis.genesisfuelstation.LoginActivity.ip_Server;

/**
 * Created by GENESIS on 04/03/2017.
 */

public class EmpresaFragment extends ListFragment implements AdapterView.OnItemClickListener {
    ListView LEmpresas;
    EmpresaAdapter Ea;
    ArrayList<Empresa> items= new ArrayList<Empresa>();
    View view;

    public static Long codigo;
    public static String empresa;

    public EmpresaFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.list_empresa, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LEmpresas = getListView();
        getListView().setOnItemClickListener(this);
        new LoadEmpresas().execute();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (items.get(position).isSeleccionado()) {
            items.get(position).setSeleccionado(false);
        } else {
            items.get(position).setSeleccionado(true);
        }
        Ea.notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }


    class LoadEmpresas extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Cargando Empresas...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                items = ObtenerEmpresas();

            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all albums
            pDialog.dismiss();
            Ea = new EmpresaAdapter(getActivity(), items);

            LEmpresas.setAdapter(Ea);
        }
    } // FIN DE LA FUNCION loadalbum


    /**
     *@autor  	Jhony Guaman
     *@date		4-Marzo-2017
     *@name		ObtenerEmpresas
     *          Metodo ObternerEmpresa permite obtener todas las Empresas con su codigo y su periodo
     *          disponibles en la base de datos
     *@return   items (retorna una arraylist con los datos de las Empresas)
     */
    public ArrayList<Empresa>ObtenerEmpresas() throws ParseException {

        String values;

        try {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet("http://"+ ip_Server+":90/Fuelstation/controlador/EmpresaController.php");//


            try {
                HttpResponse response = client.execute(request);
                HttpEntity entity=response.getEntity();


                values= EntityUtils.toString(entity);
                Log.e("Empresas", values);
                JSONObject obj= new JSONObject(values);
                JSONArray resJson= obj.getJSONArray("data");
                for (int index = 0; index < resJson.length(); index++) {
                    JSONObject jsonObject = resJson.getJSONObject(index);
                    String      codigojson=jsonObject.getString("codigo");
                    String    empresajson=jsonObject.getString("empresa");
                    String    periodojson=jsonObject.getString("periodo");
                    items.add( new Empresa(codigojson,empresajson,periodojson));
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (ClientProtocolException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        } catch (IOException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        }
        return items;
    }


}
