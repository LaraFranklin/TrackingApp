package com.rawals.mymapa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.List;

public class DetallesCarrera extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap map;
    Comunicador com = null;
    carrera c = null;
    Button borrar;
    TextView fecha;
    TextView distancia;
    TextView duracion;



    private GestorBD gestorbd = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_carrera);

        gestorbd = new GestorBD(this);
        c = (carrera)com.getObjeto();

        fecha = (TextView) findViewById(R.id.fecha);
        distancia = (TextView) findViewById(R.id.distancia);
        duracion = (TextView) findViewById(R.id.duracion);

        borrar = (Button) findViewById(R.id.b_borrar_carrera);

        fecha.setText(c.getFecha());
        distancia.setText(c.getDistancia());
        duracion.setText(c.getDuracion());

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gestorbd.abrirBD();
                carrera c_elegido = new carrera(c.getFecha(), c.getDistancia(), c.getDuracion(),c.getPolilinea());
                gestorbd.borrarCarrera(c_elegido);
                gestorbd.cerrarBD();
                Intent intent = new Intent(DetallesCarrera.this, CarreraList.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        List<LatLng> decodedPath = PolyUtil.decode(c.getPolilinea());
        LatLng zoom = decodedPath.get(0);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(zoom.latitude,zoom.longitude),13));


        googleMap.addPolyline(new PolylineOptions().addAll(decodedPath));

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
