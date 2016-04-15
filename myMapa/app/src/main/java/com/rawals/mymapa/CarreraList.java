package com.rawals.mymapa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ramon on 10/4/16.
 */
public class CarreraList extends ActionBarActivity {
    Comunicador com = null;
    ListViewAdapter listViewAdapter;
    private GestorBD gestorbd = null;
    List<carrera> carreras = new ArrayList<carrera>();
    private GestorBD gbd = null;
    carrera c = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_carreras);


        gestorbd = new GestorBD(this);
        gestorbd.abrirBD();
        carreras = gestorbd.leerCarreras();
        gestorbd.cerrarBD();

        ListView listView = (ListView) findViewById(R.id.listView);


        listViewAdapter = new ListViewAdapter(this, carreras);
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int posicion, long l) {
                c = carreras.get(posicion);

                com.setObjeto(c);

                Intent ii = new Intent(getApplicationContext(), DetallesCarrera.class);
                startActivity(ii);
                finish();


            }
        });



    }



    public class ListViewAdapter extends BaseAdapter {

        LayoutInflater inflater;
        private List<carrera> carreras = null;  // Estructura con los datos

        public ListViewAdapter(Activity activity, List<carrera> carreras ) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.carreras = carreras;

        }

        @Override
        public int getCount() {
            return carreras.size();   // Número de filas a crear
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // Crear una fila
            View vi = inflater.inflate(R.layout.listar_carreras_row, null);

            // Obtener las referencias a los controles
            TextView fec = (TextView) vi.findViewById(R.id.fecha);
            TextView dis = (TextView) vi.findViewById(R.id.distancia);
            TextView dur = (TextView) vi.findViewById(R.id.duracion);


            // Rellenar los datos
            c = carreras.get(position);
            fec.setText(c.getFecha());
            dis.setText(c.getDistancia());
            dur.setText(c.getDuracion());

            // Devolver la fila generada y rellenada
            return vi;
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
