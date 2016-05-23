package com.havelans.marinete;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by gabriel.fernandes on 22/05/2016.
 */
public class MarineteActionBar extends AppCompatActivity {

    protected void setupActionBar() {
            ActionBar ab = getSupportActionBar();
            ab.setDisplayShowCustomEnabled(true);
            ab.setDisplayShowTitleEnabled(false);
            LayoutInflater inflator = (LayoutInflater) this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflator.inflate(R.layout.action_bar_marinete, null);

            ab.setCustomView(v);
        }
}
