package com.havelans.marinete;

import android.os.Bundle;

public class MarineteActivity extends MarineteActionBar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marinete);

        setupActionBar();
    }
}
