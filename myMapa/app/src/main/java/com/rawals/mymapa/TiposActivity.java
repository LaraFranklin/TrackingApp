package com.rawals.mymapa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class TiposActivity extends AppCompatActivity {

    ImageView caminar;
    ImageView correr;
    ImageView bici;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiposcarrera);

        caminar = (ImageView) findViewById(R.id.imageView);
        correr = (ImageView) findViewById(R.id.imageView);
        bici = (ImageView) findViewById(R.id.imageView);
        findViewById(R.id.imageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TiposActivity.this, MapsActivity.class));
            }
        });
        findViewById(R.id.imageButton3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TiposActivity.this, MapsActivity.class));
            }
        });
        findViewById(R.id.imageButton2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TiposActivity.this, MapsActivity.class));
            }
        });

    }
}
