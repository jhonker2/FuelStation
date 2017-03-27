package com.example.genesis.genesisfuelstation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

/**
 * Created by GENESIS on 03/03/2017.
 */

public class ConfigipActivity extends AppCompatActivity {
    private Button btn_conf;
    private EditText txt_ip;
    private long mlas = 0;
    private long mTim = 2000;
    private static final Pattern PARTIAl_IP_ADDRESS =
            Pattern.compile("^((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[0-9])\\.){0,3}"+
                    "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[0-9])){0,1}$");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configip);

        btn_conf = (Button) findViewById(R.id.btn_config);
        txt_ip = (EditText) findViewById(R.id.Etxt_ip);

        txt_ip.addTextChangedListener(new TextWatcher() {
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void beforeTextChanged(CharSequence s,int start,int count,int after) {}

            private String mPreviousText = "";
            @Override
            public void afterTextChanged(Editable s) {
                if(PARTIAl_IP_ADDRESS.matcher(s).matches()) {
                    mPreviousText = s.toString();
                } else {
                    s.replace(0, s.length(), mPreviousText);
                }
            }
        });
        btn_conf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txt_ip.getText().toString().isEmpty()){
                    Toast.makeText(ConfigipActivity.this, "Datos Insuficientes!", Toast.LENGTH_SHORT).show();
                }else{
                    SharedPreferences datos = getSharedPreferences("perfil", Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = datos.edit();
                    editor.putString("p_ip",txt_ip.getText().toString());
                    editor.commit();
                    startActivity(new Intent(ConfigipActivity.this, LoginActivity.class));
                    finish();
                }
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



}
