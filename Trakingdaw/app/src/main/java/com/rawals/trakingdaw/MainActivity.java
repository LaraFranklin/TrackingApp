package com.rawals.trakingdaw;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.rawals.trakingdaw.dummy.DummyContent;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentInicio.OnFragmentInteractionListener, FragmentListaRutas.OnListFragmentInteractionListener, MapFragment.OnFragmentInteractionListener {

    LocationManager locationManager;
    AlertDialog alert = null;
    private GoogleMap map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // initialising the object of the FragmentManager. Here I'm passing getSupportFragmentManager(). You can pass getFragmentManager() if you are coding for Android 3.0 or above.


        //Barra de arriba
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Enlaza con el Navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
//
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //Miramos si esta encendido en GPS

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //Alerta para que el usuario active el GPS
            AlertNoGps();

        }

    }

    @Override
    public void onBackPressed() {
        //Abre el Navigation Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //Controla elementos del Navigation Drawer
        int id = item.getItemId();

        boolean FragmentTransaction = false;
        Fragment fragment = null;

        if (id == R.id.nav_camera) {
            fragment = new FragmentInicio();
            FragmentTransaction = true;
        } else if (id == R.id.nav_gallery) {
            fragment = new MapFragment();
            FragmentTransaction = true;
        } else if (id == R.id.nav_slideshow) {
            fragment = new FragmentListaRutas();
            FragmentTransaction = true;
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }


        if (FragmentTransaction){
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main,fragment).commit();
            //Nombre en la etiqueta
            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
    private void AlertNoGps() {

        final AlertDialog.Builder buider = new AlertDialog.Builder(this);
        buider.setMessage("El GPS esta desactivado, ¿Quieres activarlo?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {

                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });

        alert = buider.create();
        alert.show();
    }


    //Liberar memoria despues de la alerta
    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (alert != null){
            alert.dismiss();
        }
    }

}
