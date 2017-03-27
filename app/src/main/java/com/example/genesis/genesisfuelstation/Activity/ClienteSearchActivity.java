package com.example.genesis.genesisfuelstation.Activity;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.genesis.genesisfuelstation.Adapter.ProductosAdapter;
import com.example.genesis.genesisfuelstation.PrincipalActivity;
import com.example.genesis.genesisfuelstation.R;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import Dialogs.newClienteFragment;
import Utils.validadcion;
import model.Cliente;
import model.Producto;
import model.Productos;

import static com.example.genesis.genesisfuelstation.LoginActivity.ip_Server;

/**
 * Created by GENESIS on 08/03/2017.
 */

public class ClienteSearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener, MenuItemCompat.OnActionExpandListener {
    public ArrayList<Cliente> items = new ArrayList<Cliente>();

    public ArrayList<Producto> p = new ArrayList<Producto>();
    final Context context = this;
    public TextView txtcodigo;
    public TextView txtcliente,t_cedul;
    public TextView txtdireccion;
    public TextView txttelefono;
    public TextView txtproducto,txtcantidad,txtpvp,txtsubtotal,txtiva,txttotalp,total;
    //public CheckBox filtro;
    public EditText txtcedula;
    public RadioGroup rgOpciones;

    public EditText ced,nomb,dir,tel;
    public Button   btnsearch,btnSaveCliente,buttonVenta;
    public static String data;
    public String check2="";
    private long mlas = 0;
    private long mTim = 2000;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);

        txtcodigo       =(TextView) findViewById(R.id.txt_codigo);
        txtcliente      =(TextView) findViewById(R.id.txt_cliente);
        txtdireccion    =(TextView) findViewById(R.id.txt_direccion);
        txttelefono     =(TextView) findViewById(R.id.txt_telefono);
        txtcedula       =(EditText) findViewById(R.id.txt_cedula);
        //btnsearch       =(Button)   findViewById(R.id.btn_search);
        buttonVenta     =(Button)   findViewById(R.id.buttonVenta);
        txtproducto     =(TextView) findViewById(R.id.tproducto);
        txtcantidad     =(TextView) findViewById(R.id.tcantidad);
        txtpvp          =(TextView) findViewById(R.id.tpvp);
        txtsubtotal     =(TextView) findViewById(R.id.tsubtotal);
        txtiva          =(TextView) findViewById(R.id.tiva);
        txttotalp       =(TextView) findViewById(R.id.ttotalp);
        total           =(TextView) findViewById(R.id.total);
        //filtro          =(CheckBox) findViewById(R.id.ckfiltro);
        t_cedul         =(TextView) findViewById(R.id.t_cedu);

        new LoadProdu().execute();// Ejecuta el Hilo de Busqueda de Producto

        /*
        *   Action del Boton de guardar Venta
        *
        */


        buttonVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txtcodigo.getText().toString().equalsIgnoreCase("Registrar")){

                    /*
                    * Registro de Clientes que no se encuentra en la
                    * tabla Cliente2 a In_Ciente
                    */

                    new Registrar2().execute();

                    /**/
                }else{
                    /*Obtenemos la Fecha del Sistema*/
                    Calendar cal = new GregorianCalendar();
                    Date date = cal.getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String fecha = df.format(date);
                    /*Instaciamos la preferencia*/
                    SharedPreferences datos = getSharedPreferences("perfil", Context.MODE_PRIVATE);
                    /*Declaramos los dos arrayjson para alcenar los valores*/
                    JSONArray cabecera = new JSONArray();
                    JSONArray movimiento = new JSONArray();
                    JSONArray cxcAuxi = new JSONArray();
                    try {

                        JSONObject pc = new JSONObject();
                            pc.put("fecha",fecha);
                            pc.put("fechav",fecha);
                            pc.put("cod_cliente",txtcodigo.getText());
                            pc.put("vendedor",datos.getString("p_vendedor",null));
                            pc.put("caja",SurtidorActivity.isla);
                            pc.put("estado","V");
                            pc.put("surtidor",SurtidorActivity.mangeraS);
                            pc.put("usuario",datos.getString("p_nombre",null));
                            pc.put("nombretrans",txtcliente.getText());
                            pc.put("cedulatrans",t_cedul.getText());
                            pc.put("direciontrans",txtdireccion.getText());
                            pc.put("estacion",SurtidorActivity.estacionS);
                            pc.put("punto",SurtidorActivity.puntoS);
                            pc.put("Codsurtidor",SurtidorActivity.codigoSurtidor);

                        cabecera.put(pc);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    for(int x=0 ; x<p.size(); x++){
                        JSONObject pm= new JSONObject();
                        try {
                            pm.put("cantidad",txtcantidad.getText());
                            pm.put("producto",p.get(x).getCodigos());
                            pm.put("impuesto",p.get(x).getIvaValor());
                            pm.put("valor1",p.get(x).getPvp1());
                            pm.put("Costo",p.get(x).getCosto());
                            pm.put("valor",p.get(x).getPvp1()-(p.get(x).getPvp1()*(p.get(x).getIvaValor())/100));
                            movimiento.put(pm);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    JSONObject  cA= new JSONObject();
                    try {
                        cA.put("fechae",fecha);
                        cA.put("fechav",fecha);
                        cA.put("valor",txttotalp.getText());
                        cA.put("entidad",txtcliente.getText());
                        cA.put("estacion",SurtidorActivity.estacionS);
                        cA.put("punto",SurtidorActivity.puntoS);
                        cxcAuxi.put(cA);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    new StroreVenta().execute( cabecera.toString(),movimiento.toString(),cxcAuxi.toString());
                }

            }
        });

        /*
        *  Accion de buscar del EditText cedula
        *  permite buscar al cliente
        * */
        txtcedula.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean procesado = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // Mostrar mensaje
                    switch (check2){
                        case "codigo":
                            new LoadCliente().execute(v.getText().toString());
                            break;
                        case "cedula":
                            if(validadcion.esCedulaValida(v.getText().toString())){
                                new LoadCliente().execute(v.getText().toString());
                            }else{
                                StyleableToast.makeText(ClienteSearchActivity.this, "La Cedula ingresada es incorrecta ", Toast.LENGTH_LONG, R.style.StyledToastError).show();
                            }
                            break;
                        case "placa":

                            break;
                        default:
                            if(validadcion.esCedulaValida(v.getText().toString())){
                                new LoadCliente().execute(v.getText().toString());
                            }else{
                                StyleableToast.makeText(ClienteSearchActivity.this, "La Cedula ingresada es incorrecta ", Toast.LENGTH_LONG, R.style.StyledToastError).show();
                            }


                    }


                    // Ocultar teclado virtual
                    InputMethodManager imm =
                            (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    procesado = true;
                }
                return procesado;
            }
        });



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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        //getMenuInflater().inflate(R.menu.principal, menu);

        MenuItem searchItem = menu.findItem(R.id.menu3_buscar);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(searchItem, this);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.Filtrar) {

            AlertDialog.Builder buil = new AlertDialog.Builder(ClienteSearchActivity.this);
            View mView = getLayoutInflater().inflate(R.layout.filtro_cliente,null);
            rgOpciones = (RadioGroup) mView.findViewById(R.id.GrbGrupo1);
            buil.setView(mView);
            final AlertDialog alertDialog = buil.create();
            alertDialog.show();
            rgOpciones.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    String opcion = "";
                    switch(checkedId) {
                        case R.id.RbOpcion1:
                            opcion = "Por Codigo";
                            check2="codigo";
                            alertDialog.dismiss();
                            txtcedula.setInputType(InputType.TYPE_CLASS_TEXT);
                            break;
                        case R.id.RbOpcion2:
                            opcion = "Por Cedula";
                            alertDialog.dismiss();
                            check2="cedula";
                            txtcedula.setInputType(InputType.TYPE_CLASS_NUMBER);

                            break;
                        case R.id.RbOpcion3:
                            opcion = "Por Placa";
                            alertDialog.dismiss();
                            check2="placa";
                            txtcedula.setInputType(InputType.TYPE_CLASS_TEXT);

                            break;
                    }

                    StyleableToast.makeText(ClienteSearchActivity.this, "Filtrado "+opcion, Toast.LENGTH_LONG, R.style.StyledToast).show();

                }
            });


        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
       /* switch (check2){
            case "codigo":
                new LoadCliente().execute(v.getText().toString());
                break;
            case "cedula":
                if(validadcion.esCedulaValida(v.getText().toString())){
                    new LoadCliente().execute(v.getText().toString());
                }else{
                    Toast.makeText(ClienteSearchActivity.this,
                            "La cedula Ingresada en invalida", Toast.LENGTH_LONG).show();
                }
                break;
            case "placa":

                break;
        }*/
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
       // Toast.makeText(getApplicationContext(), "EXPAND", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        //Toast.makeText(getApplicationContext(), "COLLAPSE", Toast.LENGTH_SHORT).show();
        return true;
    }

    class LoadCliente extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ClienteSearchActivity.this);
            pDialog.setMessage("Buscando al Cliente...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                items.clear();
                items= obtenerCliente(params[0]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return params[0];
        }
        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();
            if(items.size()==0){
                new LoadCliente2().execute(s);  // Hilo para buscar en la tabla Cliente2
            }else {
                for (int x = 0; x < items.size(); x++) {
                    //txtcedula.setText(items.get(x).cedula_ruc);
                    txtcodigo.setText(String.valueOf(items.get(x).getCodigo()));
                    txtcliente.setText(items.get(x).getNombre());
                    txtdireccion.setText(items.get(x).getDirecion());
                    txttelefono.setText(items.get(x).getTelefono());
                    t_cedul.setText(items.get(x).getCedula_ruc());
                }
            }
            //mAdapter = new ClienteAdapter(myDa);

        }



    }

    public ArrayList<Cliente> obtenerCliente(String x) throws ParseException {

        String values;
        SharedPreferences dato = getSharedPreferences("perfil", Context.MODE_PRIVATE);

        try {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet("http://" + ip_Server + ":90/Fuelstation/controlador/ClientePorCedulaController.php?empresa=" + dato.getString("p_empresa", null)+"&cedula="+x);


            try {
                HttpResponse response = client.execute(request);
                HttpEntity entity = response.getEntity();
                values = EntityUtils.toString(entity);

                Log.e("IN_CLIENTE", values);

                JSONObject obj = new JSONObject(values);
                JSONArray resJson = obj.getJSONArray("data");
                for (int index = 0; index < resJson.length(); index++) {
                    JSONObject jsonObject = resJson.getJSONObject(index);
                    Long codigojson = jsonObject.getLong("codigo");
                    String nombrejson = jsonObject.getString("nombre");
                    String cedulajson = jsonObject.getString("cedula_ruc");
                    String direcionjson = jsonObject.getString("direccion1");
                    String telefonojson = jsonObject.getString("telefono");
                    items.add(new model.Cliente(codigojson, nombrejson,direcionjson,telefonojson, cedulajson));

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

    class LoadCliente2 extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ClienteSearchActivity.this);
            pDialog.setMessage("Buscando al Cliente...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                items.clear();
                items= obtenerCliente2(params[0]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();
            if(items.size()==0){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Genesis Informa");
                alertDialogBuilder
                        .setMessage("El cliente no se encuentra registrado.¿Desea registrar al cliente?")
                        .setCancelable(false)
                        .setPositiveButton("Si",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                AlertDialog.Builder buil = new AlertDialog.Builder(ClienteSearchActivity.this);
                                View mView = getLayoutInflater().inflate(R.layout.activity_newcliente,null);
                                ced = (EditText) mView.findViewById(R.id.textCedulaCliente);
                                nomb = (EditText) mView.findViewById(R.id.textNombreCliente);
                                dir = (EditText) mView.findViewById(R.id.textDirecionCliente);
                                tel = (EditText) mView.findViewById(R.id.textTelefonoCliente);
                                btnSaveCliente = (Button) mView.findViewById(R.id.buttonNewC);
                                buil.setView(mView);
                                final AlertDialog alertDialog = buil.create();
                                alertDialog.show();

                                ced.setText(txtcedula.getText());

                                /*Funcion del BOTON GUARDAR CLIENTES
                                *fecha: 08/03/2017
                                *
                                **/
                                btnSaveCliente.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        new Registrar().execute();
                                        alertDialog.dismiss();

                                    }


                                });
                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){

                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }else {
                for (int x = 0; x < items.size(); x++) {
                    txtcodigo.setText("Registrar");
                    txtcliente.setText(items.get(x).getNombre());
                    txtdireccion.setText(items.get(x).getDirecion());
                    txttelefono.setText(items.get(x).getTelefono());
                    t_cedul.setText(items.get(x).getCedula_ruc());
                }
            }
            //mAdapter = new ClienteAdapter(myDa);

        }



    }

    public ArrayList<Cliente> obtenerCliente2(String pcedula) throws ParseException {

        String values;
        SharedPreferences dato = getSharedPreferences("perfil", Context.MODE_PRIVATE);

        try {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet("http://" + ip_Server + ":90/Fuelstation/Controller/SearchCliente2Controller.php?cedula="+pcedula);


            try {
                HttpResponse response = client.execute(request);
                HttpEntity entity = response.getEntity();
                values = EntityUtils.toString(entity);

                Log.e("Clientes2:", values);

                JSONObject obj = new JSONObject(values);
                JSONArray resJson = obj.getJSONArray("data");
                for (int index = 0; index < resJson.length(); index++) {
                    JSONObject jsonObject = resJson.getJSONObject(index);
                    String nombrejson = jsonObject.getString("nombre");
                    String cedulajson = jsonObject.getString("cedula_ruc");
                    String direcionjson = jsonObject.getString("direccion1");
                    items.add(new model.Cliente( nombrejson,direcionjson, cedulajson));

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

    class LoadProdu extends  AsyncTask<String, String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            for(int x=0 ; x<p.size() ; x++){
                txtproducto.setText(p.get(x).getDescripcion1());
                txtsubtotal.setText(String.valueOf(redondearDecimales(SurtidorActivity.valorS/((p.get(x).getIvaValor()/100)+1),3)));
                total.setText(String.valueOf(redondearDecimales(SurtidorActivity.valorS/((p.get(x).getIvaValor()/100)+1),3)));
                txtpvp.setText(String.valueOf(redondearDecimales(Double.parseDouble(p.get(x).getPvp1().toString()),3)));
                txtcantidad.setText(String.valueOf(redondearDecimales(Double.parseDouble((String) txtsubtotal.getText())/p.get(x).getPvp1(),3)));
                txtiva.setText(String.valueOf (redondearDecimales(SurtidorActivity.valorS-Double.parseDouble((String) txtsubtotal.getText()),3)));
                txttotalp.setText(String.valueOf(redondearDecimales(Double.parseDouble((String) txtsubtotal.getText())+Double.parseDouble((String) txtiva.getText()),3)));
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                p.clear();
                p= obtenerProdu();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public ArrayList<Producto> obtenerProdu() throws ParseException {

        String values;
        SharedPreferences dato = getSharedPreferences("perfil", Context.MODE_PRIVATE);

        try {
            DefaultHttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet("http://" + ip_Server + ":90/Fuelstation/Controller/BuscarProductoController.php?empresa=" + dato.getString("p_empresa", null)+"&codigo="+SurtidorActivity.mangeraS);


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
                    String empresajson = jsonObject.getString("empresa");
                    String descripcionjson = jsonObject.getString("descripcion1");
                    String ivajson = jsonObject.getString("iva");
                    Double pvpjson = jsonObject.getDouble("pvp1");
                    Double ivaValorjson = jsonObject.getDouble("ivaValor");
                    Double costojson = jsonObject.getDouble("costo");
                    p.add(new model.Producto(codigojson, empresajson,descripcionjson,ivajson, pvpjson,ivaValorjson,costojson));

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
        return p;
    }

    class Registrar extends AsyncTask<String,Void,Boolean>{

        @Override
        protected Boolean doInBackground(String... params) {
            Boolean resul=StoreCliente();
            return resul;
        }
        @Override
        protected void onPreExecute() {
                   }

        @Override
        protected void onPostExecute(Boolean s) {
            if(s){
                 Toast.makeText(ClienteSearchActivity.this, "Cliente creada!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(ClienteSearchActivity.this, "Error creando el cliente!", Toast.LENGTH_SHORT).show();
            }
        }


    }

    public Boolean StoreCliente(){
        //subtobase = Float.parseFloat((String) txt_total.getText());
        SharedPreferences dato = getSharedPreferences("perfil", Context.MODE_PRIVATE);
        // http post
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("empresa", dato.getString("p_empresa", null)));
        nameValuePairs.add(new BasicNameValuePair("cedula_ruc",t_cedul.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("nombre", nomb.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("direccion1", dir.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("telefono", tel.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("vendedor", dato.getString("p_vendedor",null)));
        nameValuePairs.add(new BasicNameValuePair("zona", String.valueOf(1)));
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://" + ip_Server + ":90/Fuelstation/Controller/CrearClienteController.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            //is = entity.getContent();
            data = EntityUtils.toString(entity);
            Log.e("ultimoid", data);
            // Toast.makeText(getApplicationContext(), "Su Pedido Fue Registrado Correctamente", Toast.LENGTH_LONG).show();
            // Intent iab = new Intent(Registrar.this, AndroidPHPConnectionDemo.class);
            //startActivity(iab);
            if(!data.equalsIgnoreCase(""))
            return false;
        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
            return false;
        }
        return true;

    }


    class Registrar2 extends AsyncTask<String,Void,Boolean>{
        private ProgressDialog pDialog;
        String resul;
        @Override
        protected Boolean doInBackground(String... params) {
            StoreCliente2();
            return true;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ClienteSearchActivity.this);
            pDialog.setMessage("Validando Datos...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean s) {
            if(s){
                String cedula_registrada=t_cedul.getText().toString();
                pDialog.dismiss();
                //alertDialog.dismiss();
                Toast.makeText(ClienteSearchActivity.this, "Cliente creada!", Toast.LENGTH_SHORT).show();

               // new SearchCodigo().execute(cedula_registrada);
               // new LoadCliente2().execute(cedula_registrada);
            }else{
                Toast.makeText(ClienteSearchActivity.this, "Error creando el cliente!", Toast.LENGTH_SHORT).show();

            }
        }


    }

    class SearchCodigo extends AsyncTask<String, Void, Boolean>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return null;
        }
    }
    public void StoreCliente2(){
        //subtobase = Float.parseFloat((String) txt_total.getText());
        SharedPreferences dato = getSharedPreferences("perfil", Context.MODE_PRIVATE);
        // http post
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("empresa", dato.getString("p_empresa", null)));
        nameValuePairs.add(new BasicNameValuePair("cedula_ruc",t_cedul.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("nombre", txtcliente.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("direccion1", txtdireccion.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("telefono", txttelefono.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("vendedor", dato.getString("p_vendedor",null)));
        nameValuePairs.add(new BasicNameValuePair("zona", String.valueOf(1)));
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://" + ip_Server + ":90/Fuelstation/Controller/CrearClienteController.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            //is = entity.getContent();
            data = EntityUtils.toString(entity);
            Log.e("ultimoid", data);
            // Toast.makeText(getApplicationContext(), "Su Pedido Fue Registrado Correctamente", Toast.LENGTH_LONG).show();
            // Intent iab = new Intent(Registrar.this, AndroidPHPConnectionDemo.class);
            //startActivity(iab);
        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
        }
    }

    public static double redondearDecimales(double valorInicial, int numeroDecimales) {
        double parteEntera, resultado;
        resultado = valorInicial;
        parteEntera = Math.floor(resultado);
        resultado=(resultado-parteEntera)*Math.pow(10, numeroDecimales);
        resultado=Math.round(resultado);
        resultado=(resultado/Math.pow(10, numeroDecimales))+parteEntera;
        return resultado;
    }


    /*Funcion para registrar la venta en las tablas in_cabecera y in_movimiento*/

    class StroreVenta extends AsyncTask<String, Void, Boolean>{
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ClienteSearchActivity.this);
            pDialog.setMessage("Validando Orden...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        @Override
        protected Boolean doInBackground(String... params) {
            SharedPreferences dato = getSharedPreferences("perfil", Context.MODE_PRIVATE);
            try
            {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://" + ip_Server + ":90/Fuelstation/Controller/CrearVentaController.php");

                //Post Data
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(4);
                nameValuePair.add(new BasicNameValuePair("cabecera", params[0]));
                nameValuePair.add(new BasicNameValuePair("movimiento", params[1]));
                nameValuePair.add(new BasicNameValuePair("cxcAux", params[2]));
                nameValuePair.add(new BasicNameValuePair("empresa",  dato.getString("p_empresa", null)));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));

                HttpResponse response = httpClient.execute(httpPost);

                String v=EntityUtils.toString(response.getEntity());
                Log.e("Resul deCrear Venta:",v);
                if(!v.equalsIgnoreCase(""))
                    return false;

            }
            catch(Exception ex)
            {
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean)
            {
                pDialog.dismiss();
                Toast.makeText(ClienteSearchActivity.this, "Se ha registrado la Venta Satisfactoria!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ClienteSearchActivity.this, PrincipalActivity.class));
                p.clear();
                finish();
                //LimpiarGeneral();
            }
            else
            {
                pDialog.dismiss();
                Toast.makeText(ClienteSearchActivity.this, "Error creando la venta!", Toast.LENGTH_SHORT).show();
            }
        }


    }

}
