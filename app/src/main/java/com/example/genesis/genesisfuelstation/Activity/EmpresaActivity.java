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

import com.example.genesis.genesisfuelstation.Adapter.EmpresaAdapter;
import com.example.genesis.genesisfuelstation.ConfigipActivity;
import com.example.genesis.genesisfuelstation.LoginActivity;
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

public class EmpresaActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    ListView lista;
    ArrayList<Empresa> items=new ArrayList<Empresa>();
    public static ArrayList<Empresa> ListaE=new ArrayList<Empresa>();
    EmpresaAdapter Ea;
    int number,number1;
    String varempresa,varperiodo,codigo;
    long varcodigo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_empresa);
        lista =(ListView) findViewById(R.id.list);

        new LoadEmpresas().execute();

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(items.get(position).isSeleccionado()){
                    items.get(position).setSeleccionado(false);
                }else{
                    items.get(position).setSeleccionado(true);
                }
                Ea.notifyDataSetChanged();
                Empresa elegido = (Empresa) parent.getItemAtPosition(position);

                Long r=lista.getAdapter().getItemId(position);
                number=position;
                number1 = Integer.valueOf(r.intValue());

                // long h=lv.;
                //int ads=Integer.valueOf(r.intValue());
                varempresa=elegido.getEmpresa();
                //pre1 = elegido.getPrecio();
                varperiodo= elegido.getPeriodo();
                varcodigo = elegido.getCodigo();
                if(varcodigo<10){codigo="00"+varcodigo;}else if(varcodigo<100){codigo="0"+varcodigo;}else if(varcodigo<1000){codigo=String.valueOf(varcodigo);}
                SharedPreferences datos = getSharedPreferences("perfil", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = datos.edit();
                editor.putString("p_empresa",codigo);
                editor.commit();
                Toast.makeText(EmpresaActivity.this, "Datos elegidos:"+codigo+" "+varempresa + " - "+varperiodo, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EmpresaActivity.this, LoginActivity.class));
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
        Ea.notifyDataSetChanged();
    }


    class LoadEmpresas extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EmpresaActivity.this);
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
            Ea = new EmpresaAdapter(EmpresaActivity.this, items);

            lista.setAdapter(Ea);
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
