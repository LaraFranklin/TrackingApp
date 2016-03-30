package com.rawals.trakingdaw;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener, GoogleMap.OnInfoWindowClickListener, View.OnClickListener, GoogleMap.OnMyLocationButtonClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private GoogleMap map;
    MapView mapView;
    private Button bmapa;
    private Button bhibrido;
    private Button bterreno;
    private Button biniciar;
    private Button bparar;
    TextView textvel;



    private boolean comenzar = false;
    private Polyline polilinea;
    private List<LatLng> list = new ArrayList<>();
    Location location = null;
    LatLng latLong = null;
    private boolean needsInit=false;

    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {







        View v = inflater.inflate(R.layout.fragment_map, container, false);

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) v.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(false);
        if (savedInstanceState == null) {
            needsInit=true;
        }

        mapView.getMapAsync(this);

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this.getActivity());

        // Updates the location and zoom of the MapView
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(40.416646, -3.703818), 6);
        map.animateCamera(cameraUpdate);

        bmapa = (Button) v.findViewById(R.id.bmapa);
        bhibrido = (Button) v.findViewById(R.id.bhibrido);
        bterreno = (Button) v.findViewById(R.id.bterreno);
        biniciar = (Button) v.findViewById(R.id.biniciar);
        bparar = (Button) v.findViewById(R.id.bparar);
        textvel= (TextView) v.findViewById(R.id.textvel);


        bmapa.setOnClickListener(this);
        bhibrido.setOnClickListener(this);
        bterreno.setOnClickListener(this);
        biniciar.setOnClickListener(this);
        bparar.setOnClickListener(this);

        onMapReady(map);


        return v;
    }


    public void onMapLongClick(LatLng latLng) {
        String cad = String.valueOf((latLng.latitude));
        cad = cad.substring(0, 9);
        String cad2 = String.valueOf((latLng.longitude));
        cad2 = cad2.substring(0, 8);

        this.map.addMarker(new MarkerOptions().position(latLng)
                .title("Coordenadas:")
                .snippet("Latitud: " + String.valueOf(cad)
                        + "\nLongitud: " + String.valueOf(cad2)));


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        //mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(mapView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mapView.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            CameraUpdate center=
                    CameraUpdateFactory.newLatLng(new LatLng(40.416646,
                            -3.703818));
            CameraUpdate zoom=CameraUpdateFactory.zoomTo(4);

            map.moveCamera(center);
            map.animateCamera(zoom);


        }



        map.setOnInfoWindowClickListener(this);


        //Recoge las coordenadas cada 5 segundos
        map.setOnMyLocationChangeListener(this);



    }

    private void buildAlertMessageNoGps() {
    }


    @Override
    public void onInfoWindowClick(Marker marker) {

    }
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    public void onMyLocationChange(Location lastKnownLocation) {





if (comenzar==true) {
    if (map.getMyLocation()==null){
        textvel.setText("-.- m/s");
    }else{
        float num = lastKnownLocation.getSpeed();
        textvel.setText(num + "m/s");
    }
    LatLng latLong = new LatLng(lastKnownLocation.getLatitude(),
            lastKnownLocation.getLongitude());
    //Recogemos las coordenadas en un arrayList
    list.add(latLong);

    ruta();
}
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.bmapa:

                map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

                break;
            case R.id.bhibrido:
                map.setMapType(GoogleMap.MAP_TYPE_HYBRID);


                break;

            case R.id.bterreno:
                map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);


                break;

            case R.id.biniciar:
              comenzar = true;

                location = map.getMyLocation();
                onMyLocationChange(location);
                latLong = new LatLng(map.getMyLocation().getLatitude(),
                        map.getMyLocation().getLongitude());
                onMapLongClick(latLong);

                this.map.addMarker(new MarkerOptions().position(latLong)
                        .title("Coordenadas:")
                        .snippet("Latitud: " + String.valueOf(map.getMyLocation().getLatitude())
                                + "\n Longitud: " + String.valueOf(map.getMyLocation().getLongitude())));
                list.add(latLong);

                ruta();
                break;

            case R.id.bparar:
                latLong = new LatLng(map.getMyLocation().getLatitude(),
                        map.getMyLocation().getLongitude());
                onMapLongClick(latLong);

                    this.map.addMarker(new MarkerOptions().position(latLong)
                            .title("Coordenadas:")
                            .snippet("Latitud: " + String.valueOf(map.getMyLocation().getLatitude())
                                    + "\n Longitud: " + String.valueOf(map.getMyLocation().getLongitude())));
                    list.add(latLong);

                comenzar = false;
                break;


            default:
                break;

        }

    }




    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }




    public void ruta () {

            PolylineOptions po;

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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
