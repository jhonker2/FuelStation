package com.example.genesis.genesisfuelstation;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.genesis.genesisfuelstation.Activity.EmpresaActivity;
import com.example.genesis.genesisfuelstation.Activity.IslaActivity;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import Utils.ConexionInternet;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "jhony", "12345"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */


    // UI references.
    private EditText mUserView;
    private EditText mClaveView;
    private View mProgressView;
    private View mLoginFormView;
    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    public static String ip_Server;
    public static String id_Empresa;
    boolean resul=false;

    List<NameValuePair> nameValuePairs;
    ProgressDialog dialog = null;
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton  floatingActionButton2, floatingActionButton3,floatingActionButton4;

    public static String varusuario,empresa,vendedor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);

        floatingActionButton2 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);
        floatingActionButton3 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item3);
        floatingActionButton4 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);

        SharedPreferences dato = this.getSharedPreferences("perfil",Context.MODE_PRIVATE);

            if (dato.getString("p_ip", null) == null) {
                Toast.makeText(LoginActivity.this, "Es necesario configura una IP!", Toast.LENGTH_SHORT).show();


                startActivity(new Intent(LoginActivity.this, ConfigipActivity.class));
                finish();
            } else {
                if(dato.getString("p_empresa",null) == null){
                    ip_Server = dato.getString("p_ip", null);
                    Toast.makeText(LoginActivity.this,"Es necesario Configurar Su Empresa!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, EmpresaActivity.class));
                    finish();
                }else {
                    ip_Server = dato.getString("p_ip", null);
                    Toast.makeText(LoginActivity.this, ip_Server, Toast.LENGTH_SHORT).show();
                        if (dato.getString("p_nombre", null) != null) {
                            startActivity(new Intent(LoginActivity.this, PrincipalActivity.class));
                            finish();
                        } else {
                    // Set up the login form.
                    mUserView = (EditText) findViewById(R.id.usuario);
                    mClaveView = (EditText) findViewById(R.id.clave);

                    Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
                    mEmailSignInButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (new ConexionInternet(getBaseContext()).estaConectado()) {
                                if (!mClaveView.getText().toString().equalsIgnoreCase("") && !mUserView.getText().toString().equalsIgnoreCase("")) {
                                    mClaveView.setError(null);
                                    mUserView.setError(null);
                                    dialog = ProgressDialog.show(LoginActivity.this, "",
                                            "Espere un momento...", true);
                                    new Thread(new Runnable() {
                                        public void run() {
                                            login();
                                        }
                                    }).start();
                                } else
                                    Toast.makeText(LoginActivity.this, "Datos Insuficientes!", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(LoginActivity.this, "No tiene conexion a internet!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    floatingActionButton2.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(LoginActivity.this, ConfigipActivity.class));
                        }
                    });

                    floatingActionButton3.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(LoginActivity.this, EmpresaActivity.class));
                        }
                    });
                    floatingActionButton4.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(LoginActivity.this, IslaActivity.class));
                        }
                    });
                    mLoginFormView = findViewById(R.id.login_form);
                    mProgressView = findViewById(R.id.login_progress);
                }
            }
        }
    }

    public void login(){
        try{
            String values;
            httpclient=new DefaultHttpClient();
            httppost= new HttpPost("http://"+ip_Server+":90/Fuelstation/controlador/LoginController.php");
            SharedPreferences dato = this.getSharedPreferences("perfil",Context.MODE_PRIVATE);
            //add your data
            nameValuePairs = new ArrayList<NameValuePair>(3);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("clave",mClaveView.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("usuario",mUserView.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("empresa",dato.getString("p_empresa",null)));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            //Execute HTTP Post Request

            try
            {
                response=httpclient.execute(httppost);
                HttpEntity ent= response.getEntity();
                values= EntityUtils.toString(ent);

                JSONObject obj= new JSONObject(values);
                JSONArray resJson= obj.getJSONArray("data");
                //JSONArray json=new JSONArray("data");
                for(int i=0;i<resJson.length(); i++)
                {
                    JSONObject obj2=resJson.getJSONObject(i);
                    String user=obj2.getString("usuario");
                    String empre=obj2.getString("empresa");
                    String vended=obj2.getString("vendedor");
                    varusuario = user;
                    empresa = empre;
                    vendedor = vended;
                    resul = true;
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpclient.execute(httppost, responseHandler);
            System.out.println("Response : " + response);
            runOnUiThread(new Runnable() {
                public void run() {
                    dialog.dismiss();
                }
            });
            if (mUserView.getText().toString().isEmpty() && mClaveView.getText().toString().isEmpty()) {
                showAlert2();
            }
            else {
                if(resul){
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(LoginActivity.this, response,
                                    Toast.LENGTH_SHORT).show();
                            Toast.makeText(LoginActivity.this, "Usuario Existente",
                                    Toast.LENGTH_SHORT).show();
                            SharedPreferences datos = getSharedPreferences("perfil", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = datos.edit();
                            editor.putString("p_nombre",varusuario);
                            editor.putString("p_id",empresa);
                            editor.putString("p_vendedor",vendedor);
                           // editor.putString("p_isla","ISLA1");
                            editor.commit();

                            System.out.println("VENDEDOR : " + datos.getString("p_vendedor",null));



                        }
                    });
                    /*Intent intent = null;
                    intent = new Intent(LoginActivity.this, PrincipalActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    (LoginActivity.this).startActivity(intent);*/
                    startActivity(new Intent(LoginActivity.this, PrincipalActivity.class));
                    finish();
                }else{
                    showAlert();
                }
            }
        }catch(Exception e){
            dialog.dismiss();
            System.out.println("Exception : " + e.getMessage());

        }

    }
    public void showAlert(){
        LoginActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Error de Ingreso");
                builder.setMessage("Usuario no Registrado.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
    public void showAlert2(){
        LoginActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Error de Ingreso");
                builder.setMessage("Rellene los campos.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }


}

