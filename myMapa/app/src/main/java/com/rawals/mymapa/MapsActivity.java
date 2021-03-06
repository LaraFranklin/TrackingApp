package com.rawals.mymapa;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnInfoWindowClickListener {
    private PolyUtil polyUtil;
    private GoogleMap map;
    ImageButton btipomapa;
    Button biniciar;
    Button bparar;
    TextView textvel;
    TextView textdis;
    String tipo = "";
    Marker marker;

    Double distancia;

     Chronometer cronometro;
     String time = "";
    String date = "dd-MM-yyyy HH:mm:ss";
    String carrera="";


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

        biniciar = (Button) findViewById(R.id.biniciar);
        bparar = (Button) findViewById(R.id.bparar);
        textvel= (TextView) findViewById(R.id.textvel);
        textdis= (TextView) findViewById(R.id.textdis);

        textdis.setText("0,00 km");
        textvel.setText("0,00 km/h");

        cronometro = (Chronometer) findViewById(R.id.cronometro);
        cronometro.setVisibility(View.VISIBLE);

        biniciar.setOnClickListener(this);
        bparar.setOnClickListener(this);
        btipomapa.setOnClickListener(this);

        bparar.setEnabled(false);

        tipo= getIntent().getStringExtra("tipo");

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
       // googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLong.latitude, latLong.longitude), 13));
        
        map.setOnInfoWindowClickListener(this);
    }

    public void iniciar_ruta() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                if (comenzar == true) {
                    if (map.getMyLocation() == null) {
                        textvel.setText("-,- km/h");
                    } else {
                        float num = location.getSpeed();
                        textvel.setText(String.format("%,2f", num) + " km/h");

                    }

                    LatLng latLong = new LatLng(location.getLatitude(),
                            location.getLongitude());
                    //Centra la cámara con el movimiento
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLong.latitude, latLong.longitude), 19));
                    //Recogemos las coordenadas en un arrayList
                    list.add(latLong);
                    distancia = dame_Distancia()/1000;
                    ruta();

                }

                DecimalFormat df = new DecimalFormat();
                df.setMaximumFractionDigits(3);
                textdis.setText(df.format(distancia) + " km");

                df.setMaximumFractionDigits(2);
                float num = location.getSpeed();
                textvel.setText(df.format(num * 3.6) + " km/h");

                if (marker != null) {
                    marker.remove();
                }

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                //Toast.makeText(Mapa.this, ">>>>> onStatusChanged ", Toast.LENGTH_LONG).show();
            }

            public void onProviderEnabled(String provider) {
                Toast.makeText(MapsActivity.this, "GPS activado", Toast.LENGTH_LONG).show();
            }

            public void onProviderDisabled(String provider) {
                Toast.makeText(MapsActivity.this, "Activar el GPS", Toast.LENGTH_LONG).show();
            }
        };
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, locationListener);
        }
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.btipomapa:

                if (map.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {

                    map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    Toast.makeText(MapsActivity.this, "MAPA SATELITE", Toast.LENGTH_LONG).show();


                } else if (map.getMapType() == GoogleMap.MAP_TYPE_SATELLITE) {

                    map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    Toast.makeText(MapsActivity.this, "MAPA HIBRIDO", Toast.LENGTH_LONG).show();

                } else if (map.getMapType() == GoogleMap.MAP_TYPE_HYBRID){

                    map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    Toast.makeText(MapsActivity.this, "MAPA NORMAL", Toast.LENGTH_LONG).show();

                }

                break;

            case R.id.biniciar:

                try {

                   location = map.getMyLocation();

                    latLong = new LatLng(location.getLatitude(),location.getLongitude());
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLong.latitude, latLong.longitude), 20));
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

                    ruta();

                    iniciar_ruta();
                    biniciar.setEnabled(false);
                    bparar.setEnabled(true);



                    date = (DateFormat.format("dd-MM-yyyy HH:mm:ss", new java.util.Date()).toString());
                    //Iniciar el cronómetro
                    cronometro.setBase(SystemClock.elapsedRealtime());
                    cronometro.start();


                    comenzar = true;



                } catch (java.lang.IllegalStateException noloc){

                    Toast.makeText(MapsActivity.this, "Localización no disponible", Toast.LENGTH_LONG).show();
                }
                catch (java.lang.NullPointerException noloc){

                    Toast.makeText(MapsActivity.this, "Localización no disponible", Toast.LENGTH_LONG).show();
                }



                break;

            case R.id.bparar:



            new AlertDialog.Builder(this)
                    .setTitle("PARAR")
                    .setMessage("¿Seguro que quieres terminar la actividad?")
                    .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            //cronometro.setVisibility(View.INVISIBLE);
                            cronometro.stop();
                            long minutos = ((SystemClock.elapsedRealtime()-cronometro.getBase())/1000)/60;
                            long segundos = ((SystemClock.elapsedRealtime()-cronometro.getBase())/1000)%60;

                            if (segundos<10){
                                time = (minutos +":0"+segundos);
                            }else {
                                time = (minutos + ":" + segundos);
                            }

                            cronometro.setText(time);
                            //Recoge ultimas coordenadas
                            latLong = new LatLng(location.getLatitude(),location.getLongitude());
                            //Añade als coordenadas a la lista
                            list.add(latLong);

                            //Codifica la loista de coordenadas en un string para gurdarlo despues en la bd
                            String polilinea = polyUtil.encode(list);


                            comenzar = false;
                            // Cargamos los datos en el Intent
                            Intent intent = new Intent(MapsActivity.this,ResumenCarrera.class);
                            intent.putExtra("duracion",cronometro.getText());
                            intent.putExtra("date",date);
                            intent.putExtra("distancia",textdis.getText());
                            intent.putExtra("recorrido", po);
                            intent.putExtra("tipo",tipo);
                            intent.putExtra("polilinea",polilinea);
                            startActivity(intent);

                            finish();
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
            po.color(Color.RED);
            polilinea = map.addPolyline(po);
        } else {
            polilinea.setPoints(list);
        }

    }

    public double dame_Distancia () {
        double distance=0;
        for (int i = 0, tam = list.size(); i < tam; i++){
            if (i < tam -1) {
                distance += calcular_Distancia(list.get(i), list.get(i + 1));
            }
        }
        return distance;
    }

    public static double calcular_Distancia(LatLng StartP,LatLng EndP){
        // fórmula de Haversine
        int Radius = 6371000; //Radio de la tierra
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double d = (Radius * c); // en Km
        return (double) (Math.round(d*1)/1); // Redondeo a un decimal
    }

}
