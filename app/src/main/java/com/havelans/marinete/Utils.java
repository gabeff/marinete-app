package com.havelans.marinete;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

/**
 * Created by gabriel.fernandes on 22/05/2016.
 */
public class Utils extends AppCompatActivity {

    public Gson gson;

    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor editor;

    protected void setupActionBar() {
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
        LayoutInflater inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.action_bar_marinete, null);

        ab.setCustomView(v);
    }

    public SharedPreferences getSharedPreferences() {
        sharedPreferences = getApplication().getSharedPreferences("MarinetePref", 0); // 0 - para modo privado;
        return sharedPreferences;
    }

    public void showToast(final String toast) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(Utils.this, toast, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
