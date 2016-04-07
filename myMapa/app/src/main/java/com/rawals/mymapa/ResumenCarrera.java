package com.rawals.mymapa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.PolylineOptions;


public class ResumenCarrera extends AppCompatActivity implements View.OnClickListener {

    // Referencia para el gestorbd que nos comunica con el SQLite
    private GestorBD gestorbd = null;

    Button bguardar;
    TextView textdis;
    TextView textdur;
    TextView textvelmax;
    TextView textfecha;
    PolylineOptions recorrido;

    String duracion= "";
    String date = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen_carrera);


        bguardar = (Button) findViewById(R.id.bguardar);
        textfecha = (TextView) findViewById(R.id.textfecha);
        textdis = (TextView) findViewById(R.id.textdisres);
        textdur = (TextView) findViewById(R.id.textdurres);


        duracion= getIntent().getStringExtra("duracion");
        date= getIntent().getStringExtra("date");


        textfecha.setText(date);
        textdis.setText("12");
        textdur.setText(duracion);


        bguardar.setOnClickListener(this);

        findViewById(R.id.bcancelar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResumenCarrera.this, MapsActivity.class));
            }
        });


    }

    @Override
    public void onClick(View v) {
        // Arrancamos el gestor de BD
        gestorbd = new GestorBD(this);
        gestorbd.abrirBD();

        // Guardamos el objeto
        carrera c = new carrera(textfecha.getText(),textdis.getText(),textdur.getText());
        gestorbd.guardarCarrera(c);

        Intent intent = new Intent(v.getContext(), ListarCarreras.class);
        startActivity(intent);



    }
}
