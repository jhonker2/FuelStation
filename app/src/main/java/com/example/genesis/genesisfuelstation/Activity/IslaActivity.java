package com.example.genesis.genesisfuelstation.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.genesis.genesisfuelstation.Adapter.IslaAdapter;
import com.example.genesis.genesisfuelstation.Adapter.IslasAdapter;
import com.example.genesis.genesisfuelstation.LoginActivity;
import com.example.genesis.genesisfuelstation.PrincipalActivity;
import com.example.genesis.genesisfuelstation.R;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

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

/**
 * Created by GENESIS on 17/03/2017.
 */

public class IslaActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView lista;
    ArrayList<Caja> items=new ArrayList<Caja>();
   // public static ArrayList<Empresa> ListaE=new ArrayList<Empresa>();
    IslaAdapter Ia;
    int number,number1;
    String varempresa,varperiodo,codigo;
    long varcodigo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_islas);
        lista =(ListView) findViewById(R.id.list_islas);
        new LoadCajas().execute();
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(items.get(position).isSeleccionado()){
                    items.get(position).setSeleccionado(false);
                }else{
                    items.get(position).setSeleccionado(true);
                }
                Ia.notifyDataSetChanged();
                Caja elegido = (Caja) parent.getItemAtPosition(position);

                Long r=lista.getAdapter().getItemId(position);
                number=position;
                number1 = Integer.valueOf(r.intValue());

                SharedPreferences datos = getSharedPreferences("perfil", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = datos.edit();
                editor.putString("p_isla", String.valueOf(elegido.getCodigo()));
                editor.putString("p_islaN",elegido.getCaja());
                editor.putString("p_estacion",elegido.getEstacion());
                editor.putString("p_punto",elegido.getPunto());
                editor.commit();
                StyleableToast.makeText(IslaActivity.this, "Guardando Isla por Defaulf: "+ elegido.getCaja() , Toast.LENGTH_LONG, R.style.StyledToast).show();

                startActivity(new Intent(IslaActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(items.get(position).isSeleccionado()){
            items.get(position).setSeleccionado(false);
        }else{
            items.get(position).setSeleccionado(true);
        }
        Ia.notifyDataSetChanged();
    }


    class LoadCajas extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(IslaActivity.this);
            pDialog.setMessage("Cargando Islas...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                items.clear();
                items = obtenerIslas();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all albums
            pDialog.dismiss();
            Ia = new IslaAdapter(IslaActivity.this, items);
            lista.setAdapter(Ia);

        }

    }

    public ArrayList<Caja>obtenerIslas() throws ParseException {

        String values;
        SharedPreferences dato = this.getSharedPreferences("perfil", Context.MODE_PRIVATE);

        try {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet("http://"+ ip_Server+":90/Fuelstation/controlador/CajaController.php?empresa="+dato.getString("p_empresa",null));//


            try {
                HttpResponse response = client.execute(request);
                HttpEntity entity=response.getEntity();
                values= EntityUtils.toString(entity);

                Log.e("CAJAS", values);

                JSONObject obj= new JSONObject(values);
                JSONArray resJson= obj.getJSONArray("data");
                for (int index = 0; index < resJson.length(); index++) {
                    JSONObject jsonObject = resJson.getJSONObject(index);
                    Long      codigojson=jsonObject.getLong("codigo");
                    String    cajajson=jsonObject.getString("caja");
                    String    estacionjson = jsonObject.getString("estacion");
                    String    puntojson = jsonObject.getString("punto");
                    items.add( new Caja(codigojson,cajajson,puntojson,estacionjson));
                    //identificador.add(jsonObject.getString("identificador"));
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
