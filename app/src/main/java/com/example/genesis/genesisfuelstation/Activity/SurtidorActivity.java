package com.example.genesis.genesisfuelstation.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.genesis.genesisfuelstation.Adapter.BotonesAdapter;
import com.example.genesis.genesisfuelstation.Adapter.ProductosAdapter;
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

import model.ItemCompra;
import model.Productos;
import model.Surtidor;

import static com.example.genesis.genesisfuelstation.LoginActivity.id_Empresa;
import static com.example.genesis.genesisfuelstation.LoginActivity.ip_Server;

/**
 * Created by GENESIS on 07/03/2017.
 */

public class SurtidorActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    protected Activity activity;
    final Context context = this;
    public ArrayList<Surtidor> items = new ArrayList<Surtidor>();
    public ArrayList<Productos> items2 = new ArrayList<Productos>();
    public ArrayList<String> identificador = new ArrayList<String>();
    public static ArrayList<String> Iselecion = new ArrayList<String>();
    public static long isla = 0;
    public static int tamaño;
    public static String parametro,surtidorS,mangeraS,estacionS,puntoS,codigoSurtidor,cajaD;
    public static Double valorS;
    public Button  btn_des ;
    public EditText txt_saldo;
    public TextView text_producto,t_nombre;
    LoadProducto produ;
    public LinearLayout layout;
    GridView productos;
    private long mlas = 0;
    private long mTim = 2000;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surtidor);
        SharedPreferences dato = this.getSharedPreferences("perfil",Context.MODE_PRIVATE);
        t_nombre    =   (TextView) findViewById(R.id.txt_nombreIsla);
        if(dato.getString("p_isla",null)!=null){

            isla        = Integer.parseInt(dato.getString("p_isla",null));// 2 ES EL CODIGO DE LA ISLA
            estacionS   = dato.getString("p_estacion",null);
            puntoS      = dato.getString("p_punto",null);
            setTitle(dato.getString("p_islaN",null));
            t_nombre.setText(dato.getString("p_islaN",null));

        }else{
            setTitle(cajaD);
            t_nombre.setText(cajaD);
        }
        ///toolbar.setTitle("Isla 1");

        productos   =   (GridView)findViewById(R.id.GridProducto);
        btn_des     =   (Button) findViewById(R.id.btn_despachar);
        txt_saldo   =   (EditText) findViewById(R.id.txt_pago);
        text_producto =(TextView) findViewById(R.id.text_productoS);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        new LoadSurtidor().execute();  // Creamos los Surtidores de la Isla Seleccionada


    }


    @Override
    public void onBackPressed() {
        Toast ds = Toast.makeText(this, "Pulse de Nuevo para Salir", Toast.LENGTH_SHORT);
        /*Snackbar.make(findViewById(R.id.myCoordinatorLayout),"Pulse de nuevo para salir",
                Snackbar.LENGTH_SHORT)
                .show();*/
        long Cur = System.currentTimeMillis();

        if (Cur - mlas > mTim) {

            ds.show();
            mlas = Cur;

        } else {
            ds.cancel();
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            startActivity(new Intent(SurtidorActivity.this, PrincipalActivity.class));
            finish();


        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_send) {
            SharedPreferences da = getSharedPreferences("perfil", Context.MODE_PRIVATE);
            ip_Server = da.getString("p_ip", null);
            id_Empresa = da.getString("p_empresa", null);
            da.edit().clear().commit();
            SharedPreferences.Editor editor = da.edit();
            editor.putString("p_ip", ip_Server);
            editor.putString("p_empresa", id_Empresa);
            editor.commit();
            startActivity(new Intent(SurtidorActivity.this, LoginActivity.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class LoadSurtidor extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SurtidorActivity.this);
            pDialog.setMessage("Cargando Surtidores...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                items.clear();
                identificador.clear();
                items = obtenerSurtidores();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            LinearLayout contenedor ;
            contenedor = (LinearLayout) findViewById(R.id.contenedor);
            pDialog.dismiss();
            tamaño = identificador.size();
            ArrayList<String> elementos = new ArrayList<String>();
            int cont = 0;
            for (int x = 0; x < tamaño; x++) {
                Log.e("IDENTIFICADOR " + x + " :", identificador.get(x));
                if(identificador.get(x).equalsIgnoreCase("null")){

                }else{
                    if ((x + 1) == tamaño) {
                        elementos.add(identificador.get(x));
                    } else {
                        if (identificador.get(x).equalsIgnoreCase(identificador.get(x + 1))) {
                            cont++;
                        } else {
                            elementos.add(identificador.get(x));
                        }
                    }
                }

            }
            if(elementos.size()==0){

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set title
                alertDialogBuilder.setTitle("Atención");

                // set dialog message
                alertDialogBuilder
                        .setMessage("La Isla Seleccionada no contiene surtidores! desea regresar?")
                        .setCancelable(false)
                        .setPositiveButton("ACEPTAR",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, close
                                // current activity
                                SurtidorActivity.this.finish();
                            }
                        });


                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }else{
                for (int y = 0; y < elementos.size(); y++) {
                    Log.e("Elementos " + y + " :", elementos.get(y));
                    final Button bt= new Button(getApplicationContext());
                        bt.setText(elementos.get(y));
                        bt.setTextColor(Color.WHITE);
                    //bt.setBackgroundResource(R.drawable.water_pump);
                    bt.setBackgroundResource(R.drawable.redondo);
                        bt.setId(y);
                        contenedor.addView(bt);

                bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    Log.e("Var: ", bt.getText().toString());
                    surtidorS=bt.getText().toString(); // Almacenar el valor seleccionado (A,B,C) de los surtidores
                    parametro=bt.getText().toString();
                        bt.setBackgroundResource(R.drawable.redondo2);
                        produ= new LoadProducto();
                        produ.execute();

                    }
                });
            }
            }

        }


    }
    public ArrayList<Surtidor> obtenerSurtidores() throws ParseException {

        String values;
        SharedPreferences dato = getSharedPreferences("perfil", Context.MODE_PRIVATE);

        try {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet("http://" + ip_Server + ":90/Fuelstation/controlador/SurtidorController.php?empresa=" + dato.getString("p_empresa", null) + "&caja=" +isla);


            try {
                HttpResponse response = client.execute(request);
                HttpEntity entity = response.getEntity();
                values = EntityUtils.toString(entity);

                Log.e("CAJAS", values);

                JSONObject obj = new JSONObject(values);
                JSONArray resJson = obj.getJSONArray("data");
                for (int index = 0; index < resJson.length(); index++) {
                    JSONObject jsonObject = resJson.getJSONObject(index);
                    Long codigojson = jsonObject.getLong("codigo");
                    String surtidorjson = jsonObject.getString("surtidor");
                    String productojson = jsonObject.getString("producto");
                    String cajajson = jsonObject.getString("caja");
                    String identificadorjson = jsonObject.getString("identificador");
                    items.add(new model.Surtidor(codigojson, productojson, cajajson, identificadorjson, surtidorjson));
                    identificador.add(jsonObject.getString("identificador"));
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

    class LoadProducto extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                items2.clear();
                items2= obtenerProductos();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {

            Iselecion.add(surtidorS);
            productos.setAdapter(new ProductosAdapter(SurtidorActivity.this, items2));
            productos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(SurtidorActivity.this, items2.get(position).getProducto() +" "+ items2.get(position).getNombre() , Toast.LENGTH_SHORT).show();
                    Iselecion.add(items2.get(position).getProducto());
                    mangeraS=items2.get(position).getProducto(); // Almacenamos el id de la mangera seleccionada
                    codigoSurtidor=String.valueOf(items2.get(position).getCodigo());
                    text_producto.setText(items2.get(position).getNombre());
                }
            });


            btn_des.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(txt_saldo.getText().toString().equals("")){
                        valorS=0.0;
                    }else{
                        valorS=Double.parseDouble(txt_saldo.getText().toString());
                    }
                    if(mangeraS==null && valorS==0.0){
                        StyleableToast.makeText(SurtidorActivity.this, "Hay datos faltantes" , Toast.LENGTH_LONG, R.style.StyledToastError).show();

                    }else if(valorS==0.0){
                        StyleableToast.makeText(SurtidorActivity.this, "Por Favor ingrese la cantidad en dolares" , Toast.LENGTH_LONG, R.style.StyledToastError).show();
                    }
                    else{
                      //  Iselecion.add(txt_saldo.getText().toString());
                        Log.e("Iseleccion ", String.valueOf(Iselecion.size()));

                        startActivity(new Intent(SurtidorActivity.this, ClienteSearchActivity.class));

                    }
                }
            });
        }



    }

    public ArrayList<Productos> obtenerProductos() throws ParseException {

        String values;
        SharedPreferences dato = getSharedPreferences("perfil", Context.MODE_PRIVATE);

        try {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet("http://" + ip_Server + ":90/Fuelstation/controlador/Surtidor2Controller.php?empresa=" + dato.getString("p_empresa", null) + "&caja=" + isla+"&identificador="+ parametro);


            try {
                HttpResponse response = client.execute(request);
                HttpEntity entity = response.getEntity();
                values = EntityUtils.toString(entity);

                Log.e("CAJAS", values);

                JSONObject obj = new JSONObject(values);
                JSONArray resJson = obj.getJSONArray("data");
                for (int index = 0; index < resJson.length(); index++) {
                    JSONObject jsonObject = resJson.getJSONObject(index);
                    Long codigojson = jsonObject.getLong("codigo");
                    String productojson = jsonObject.getString("producto");
                    String nombrejson = jsonObject.getString("nombre");
                    String imagenjson = jsonObject.getString("imagenAndroid");
                    items2.add(new model.Productos(codigojson, productojson, nombrejson,imagenjson));

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
        return items2;
    }



}

