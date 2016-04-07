package com.rawals.mymapa;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMyLocationChangeListener {

    private GoogleMap map;
    //Button bmapa;
    ImageButton btipomapa;
    Button bhibrido;
    Button bterreno;
    Button biniciar;
    Button bparar;
    TextView textvel;
    TextView textdis;

     Chronometer cronometro;
     String time = "";
    String date = "dd-MM-yyyy HH:mm:ss";

    private boolean needsInit = false;
    private Polyline polilinea;
    PolylineOptions po;
    private List<LatLng> list = new ArrayList<>();
    Location location = null;
    LatLng latLong = null;
    private boolean comenzar = false;
    // Referencia para el gestorbd que nos comunica con el SQLite
    private GestorBD gestorbd = null;

    JSONObject jDistance = null;
    JSONObject jDuration = null;
    List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String, String>>>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btipomapa = (ImageButton) findViewById(R.id.btipomapa);
       // bhibrido = (Button) findViewById(R.id.bhibrido);
        //bterreno = (Button) findViewById(R.id.bterreno);
        biniciar = (Button) findViewById(R.id.biniciar);
        bparar = (Button) findViewById(R.id.bparar);
        textvel= (TextView) findViewById(R.id.textvel);
        textdis= (TextView) findViewById(R.id.textdis);

        cronometro = (Chronometer) findViewById(R.id.cronometro);
        cronometro.setVisibility(View.INVISIBLE);


        btipomapa.setOnClickListener(this);
       // bhibrido.setOnClickListener(this);
        //bterreno.setOnClickListener(this);
        biniciar.setOnClickListener(this);
        bparar.setOnClickListener(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {


        map = googleMap;

        //mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);

        if (needsInit) {
            //Centramos la imagen del mapa al arrancar
            CameraUpdate center =
                    CameraUpdateFactory.newLatLng(new LatLng(40.416646,
                            -3.703818));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(4);

            map.moveCamera(center);
            map.animateCamera(zoom);


        }


        map.setOnInfoWindowClickListener(this);
        //Recoge las coordenadas cada 5 segundos
        map.setOnMyLocationChangeListener(this);

    }

    @Override
    public void onMyLocationChange(Location location) {
        // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
        // debido a la deteccion de un cambio de ubicacion
        if (comenzar == true) {
            if (map.getMyLocation() == null) {
                textvel.setText("-.- km/h");
            } else {
                float num = location.getSpeed();
                textvel.setText(num * 3.6 + "\n km/h");
            }

    LatLng latLong = new LatLng(location.getLatitude(),
            location.getLongitude());
    //Recogemos las coordenadas en un arrayList
    list.add(latLong);

    ruta();

        }
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.btipomapa:

                if (map.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {

                    map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

                } else if (map.getMapType() == GoogleMap.MAP_TYPE_SATELLITE) {

                    map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

                } else if (map.getMapType() == GoogleMap.MAP_TYPE_HYBRID){

                    map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                }

                break;

            case R.id.biniciar:
                cronometro.setVisibility(View.VISIBLE);

                date = (DateFormat.format("dd-MM-yyyy HH:mm:ss", new java.util.Date()).toString());
                    //Iniciar el cronómetro
                    cronometro.setBase(SystemClock.elapsedRealtime());
                    cronometro.start();


                    comenzar = true;

                    location = map.getMyLocation();
                    onMyLocationChange(location);
                    latLong = new LatLng(map.getMyLocation().getLatitude(),
                            map.getMyLocation().getLongitude());
                    String cad = String.valueOf((latLong.latitude));
                    cad = cad.substring(0, 9);
                    String cad2 = String.valueOf((latLong.longitude));
                    cad2 = cad2.substring(0, 8);

                    this.map.addMarker(new MarkerOptions().position(latLong)
                            .title("INICIO:")
                            .snippet("Latitud: " + String.valueOf(cad)
                                    + "\nLongitud: " + String.valueOf(cad2)));

                    list.add(latLong);

                    ruta();

                break;

            case R.id.bparar:
                cronometro.setVisibility(View.INVISIBLE);
                cronometro.stop();
                long minutos = ((SystemClock.elapsedRealtime()-cronometro.getBase())/1000)/60;
                long segundos = ((SystemClock.elapsedRealtime()-cronometro.getBase())/1000)%60;
                time =(minutos +" : "+segundos);
                cronometro.setText(time);


                latLong = new LatLng(map.getMyLocation().getLatitude(),
                        map.getMyLocation().getLongitude());

                list.add(latLong);

                comenzar = false;
                Intent intent = new Intent(MapsActivity.this,ResumenCarrera.class);
                intent.putExtra("duracion",cronometro.getText());
                intent.putExtra("date",date);
                intent.putExtra("recorrido",po);
                startActivity(intent);
                break;

            default:
                break;

        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    public void ruta () {



        if (polilinea == null) {

            po = new PolylineOptions();

            for (int i = 0, tam = list.size(); i < tam; i++) {
                po.add(list.get(i));

            }
            po.color(Color.BLACK);
            polilinea = map.addPolyline(po);
        } else {
            polilinea.setPoints(list);
        }

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

}
