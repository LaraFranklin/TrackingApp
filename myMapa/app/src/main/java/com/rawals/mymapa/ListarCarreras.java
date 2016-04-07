package com.rawals.mymapa;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;


public class ListarCarreras extends AppCompatActivity implements View.OnClickListener {
    private LazyAdapter la = null;
    private GestorBD gestorbd = null;
    List<carrera> carreras = new ArrayList<carrera>();
    private Button bborrar;
    private int posicion;
    private GestorBD gbd = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_carreras);

        bborrar = (Button) findViewById(R.id.bborrar);
        bborrar.setOnClickListener(this);
        gestorbd = new GestorBD(this);
        gestorbd.abrirBD();
        carreras = gestorbd.leerCarreras();

        // Creamos un adaptador con carga diferida o "perezosa"
        LazyAdapter adaptador = new LazyAdapter(this, carreras);

        // Conectamos el adaptador al control
        Spinner sp3 = (Spinner) findViewById(R.id.spinner1);
        sp3.setAdapter(adaptador);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        bborrar.setTag(posicion);
        carrera c = carreras.get(posicion);
        gestorbd.borrarCarrera(c);
    }
}
