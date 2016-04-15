package com.rawals.mymapa;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
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
import com.google.maps.android.PolyUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMyLocationChangeListener {
    private PolyUtil polyUtil;
    private GoogleMap map;
    ImageButton btipomapa;
    Button biniciar;
    Button bparar;
    TextView textvel;
    TextView textdis;
    String tipo = "";

     Chronometer cronometro;
     String time = "";
    String date = "dd-MM-yyyy HH:mm:ss";
    String carrera="";


    private boolean needsInit = false;
    private Polyline polilinea;
    PolylineOptions po;
    private List<LatLng> list = new ArrayList<>();
    private List<String> listString = new ArrayList<>();
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

        biniciar = (Button) findViewById(R.id.biniciar);
        bparar = (Button) findViewById(R.id.bparar);
        textvel= (TextView) findViewById(R.id.textvel);
        textdis= (TextView) findViewById(R.id.textdis);

        cronometro = (Chronometer) findViewById(R.id.cronometro);
        cronometro.setVisibility(View.INVISIBLE);

        biniciar.setOnClickListener(this);
        bparar.setOnClickListener(this);
        btipomapa.setOnClickListener(this);

        tipo= getIntent().getStringExtra("tipo");



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
    listString.add(String.valueOf(latLong));

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

                biniciar.setEnabled(false);
                bparar.setEnabled(true);

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
                String[] latLng = "-34.8799074,174.7565664".split(",");
                double latitude = Double.parseDouble(latLng[0]);
                double longitude = Double.parseDouble(latLng[1]);

                    this.map.addMarker(new MarkerOptions().position(latLong)
                            .title("INICIO:")
                            .snippet("Latitud: " + String.valueOf(cad)
                                    + "\nLongitud: " + String.valueOf(cad2)));

                    list.add(latLong);
                    listString.add(String.valueOf(latLong));

                    ruta();

                break;

            case R.id.bparar:



            new AlertDialog.Builder(this)
                    .setTitle("PARAR")
                    .setMessage("¿Seguro que quieres terminar la actividad?")
                    .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            cronometro.setVisibility(View.INVISIBLE);
                            cronometro.stop();
                            long minutos = ((SystemClock.elapsedRealtime()-cronometro.getBase())/1000)/60;
                            long segundos = ((SystemClock.elapsedRealtime()-cronometro.getBase())/1000)%60;
                            time =(minutos +" : "+segundos);
                            cronometro.setText(time);

                            latLong = new LatLng(map.getMyLocation().getLatitude(),
                                    map.getMyLocation().getLongitude());

                            list.add(latLong);
                            String polilinea = polyUtil.encode(list);

                            listString.add(String.valueOf(latLong));

                            comenzar = false;
                            Intent intent = new Intent(MapsActivity.this,ResumenCarrera.class);
                            intent.putExtra("duracion",cronometro.getText());
                            intent.putExtra("date",date);
                            intent.putExtra("recorrido", po);
                            intent.putExtra("tipo",tipo);
                            intent.putExtra("polilinea",polilinea);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    })
                    .show();

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



}
