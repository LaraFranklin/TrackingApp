package com.rawals.mymapa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.PolylineOptions;


public class ResumenCarrera extends AppCompatActivity implements View.OnClickListener {

    // Referencia para el gestorbd que nos comunica con el SQLite
    private GestorBD gestorbd = null;

    Button bguardar;
    Button bcancelar;
    TextView textdis;
    TextView textdur;
    TextView textvelmax;
    TextView textfecha;
    PolylineOptions recorrido;
    String date = "";
    String tipo="";
    String duracion= "";
    String distancia="";
    String polilinea="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen_carrera);

        bguardar = (Button) findViewById(R.id.bguardar);
        bcancelar = (Button) findViewById(R.id.bcancelar);
        textfecha = (TextView) findViewById(R.id.textfecha);
        textdis = (TextView) findViewById(R.id.textdisres);
        textdur = (TextView) findViewById(R.id.textdurres);
        //Recogemos los valores
        date= getIntent().getStringExtra("date");
        tipo= getIntent().getStringExtra("tipo");
        duracion= getIntent().getStringExtra("duracion");
        distancia= getIntent().getStringExtra("distancia");
        polilinea= getIntent().getStringExtra("polilinea");

        textfecha.setText(date);
        textdis.setText(distancia);
        textdur.setText(duracion);

        bguardar.setOnClickListener(this);
        bcancelar.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.bguardar:
                // Arrancamos el gestor de BD
                gestorbd = new GestorBD(this);
                gestorbd.abrirBD();

                // Guardamos el objeto
                carrera c = new carrera(textfecha.getText(),textdis.getText(),textdur.getText(),polilinea,tipo);
                gestorbd.guardarCarrera(c);
                gestorbd.cerrarBD();
                //Pasamos automaticamente al guardar a la lista de carreras
                Intent intent = new Intent(this, CarreraList.class);
                startActivity(intent);
                finish();
                break;

            case R.id.bcancelar:
                intent = new Intent(ResumenCarrera.this, MainActivity.class);
                startActivity(intent);

            default:
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //Menu de los 3 puntitos
        getMenuInflater().inflate(R.menu.main, menu);

        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Menu de las opciones de los 3 puntitos
        if (item.getItemId() == R.id.legal) {
            startActivity(new Intent(this, LegalNoticesActivity.class));

            return(true);
        }

        return(super.onOptionsItemSelected(item));
    }
}
