package com.rawals.mymapa;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.common.GoogleApiAvailability;

/**
 * Created by Ramon on 14/4/16.
 */
public class LegalNoticesActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.legal);

        TextView legal=(TextView)findViewById(R.id.legal);

        legal.setText(
                GoogleApiAvailability
                        .getInstance()
                        .getOpenSourceSoftwareLicenseInfo(this));
    }

}
