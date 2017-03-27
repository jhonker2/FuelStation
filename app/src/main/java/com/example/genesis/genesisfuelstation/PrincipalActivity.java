package com.example.genesis.genesisfuelstation;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.genesis.genesisfuelstation.Activity.SurtidorActivity;
import com.example.genesis.genesisfuelstation.Adapter.IslasAdapter;

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
import model.Empresa;

import static com.example.genesis.genesisfuelstation.LoginActivity.id_Empresa;
import static com.example.genesis.genesisfuelstation.LoginActivity.ip_Server;


public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    GridView islas;
    IslasAdapter Ia;
    ArrayList<Caja> items = new ArrayList<Caja>();
    public SwipeRefreshLayout swipeContainer;
    public TextView nombreU;

    public static String isla;
    private long mlas = 0;
    private long mTim = 2000;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        /*swipeContainer = (SwipeRefreshLayout) findViewById(R.id.srlContainer);
        swipeContainer.setColorSchemeResources(
                android.R.color.holo_red_light);*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
        //setTitle("ESto no va");

        SharedPreferences dato = this.getSharedPreferences("perfil",Context.MODE_PRIVATE);
        if (dato.getString("p_isla", null) != null) {
            startActivity(new Intent(PrincipalActivity.this, SurtidorActivity.class));
            finish();
        }else{
            islas = (GridView) findViewById(R.id.gridViewP);
            islas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(PrincipalActivity.this, items.get(position).getCaja(), Toast.LENGTH_SHORT).show();
                }
            });

            new LoadCajas().execute();

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            //SharedPreferences dato = this.getSharedPreferences("perfil", Context.MODE_PRIVATE);
       /*
        nombreU.setText(dato.getString("p_nombre",null));*/

       /* Navigation navHeaderView= (NavigationView) navigationView.inflateHeaderView(R.layout.nav_header_principal);
        nombreU= (TextView) navHeaderView.findViewById(R.id.lb_usuario);
        nombreU.setText(dato.getString("p_nombre",null));*/

        //}

        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id == R.id.action_update){
            new LoadCajas().execute();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            startActivity(new Intent(PrincipalActivity.this, PrincipalActivity.class));
            finish();


        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_send) {
            SharedPreferences da = getSharedPreferences("perfil", Context.MODE_PRIVATE);
            ip_Server=da.getString("p_ip",null);
            id_Empresa=da.getString("p_empresa",null);
            da.edit().clear().commit();
            SharedPreferences.Editor editor = da.edit();
            editor.putString("p_ip",ip_Server);
            editor.putString("p_empresa",id_Empresa);
            editor.commit();
            startActivity(new Intent(PrincipalActivity.this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    class LoadCajas extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PrincipalActivity.this);
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
            Ia = new IslasAdapter(PrincipalActivity.this, items);
            islas.setAdapter(Ia);


        }

    }

    public ArrayList<Caja>obtenerIslas() throws ParseException {

        String values;
        SharedPreferences dato = this.getSharedPreferences("perfil",Context.MODE_PRIVATE);

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
