package com.rawals.mymapa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button b_bici;
    Button b_andar;
    Button b_correr;
    String tipo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        b_bici = (Button) findViewById(R.id.b_bici);
        b_andar = (Button) findViewById(R.id.b_andar);
        b_correr = (Button) findViewById(R.id.b_correr);

        b_bici.setOnClickListener(this);
        b_correr.setOnClickListener(this);
        b_andar.setOnClickListener(this);

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CarreraList.class));
            }
        });


    }



    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.b_bici:
                tipo = "Bici";
                //Mandamos tipo actividad  para guardar en la bd
                Intent intent = new Intent(MainActivity.this,MapsActivity.class);
                intent.putExtra("tipo",tipo);
                startActivity(intent);

                break;
            case R.id.b_andar:
                tipo = "Andar";
                //Mandamos tipo actividad  para guardar en la bd
                intent = new Intent(MainActivity.this,MapsActivity.class);
                intent.putExtra("tipo",tipo);
                startActivity(intent);

                break;
            case R.id.b_correr:
                tipo = "Correr";
                //Mandamos tipo actividad  para guardar en la bd
                intent = new Intent(MainActivity.this,MapsActivity.class);
                intent.putExtra("tipo",tipo);
                startActivity(intent);

            default:
                break;

        }
    }
}