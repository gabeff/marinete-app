package com.havelans.marinete;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.havelans.marinete.dominio.Marinete;
import com.havelans.marinete.rest.MarineteRestClient;

import java.util.ArrayList;
import java.util.List;

public class MarineteActivity extends AppCompatActivity {

    ListView listViewMarinete;
    ArrayList<Marinete> listaMarinete = new ArrayList<>();
    MarineteRestClient marineteRestClient;

    private MarineteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marinete);

        setupActionBar();

        listViewMarinete = (ListView) findViewById(R.id.lista_marinete);

        if (savedInstanceState == null) {
            new CarregaMarinetesAsynkTask().execute();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("lista", listaMarinete);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        listaMarinete = savedInstanceState.getParcelableArrayList("lista");
        new CarregaMarinetesAsynkTask().execute();
    }

    private void setupActionBar() {
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
        LayoutInflater inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.action_bar_marinete, null);

        ab.setCustomView(v);
    }

    public class CarregaMarinetesAsynkTask extends AsyncTask<Void, Void, List<Marinete>> {


        @Override
        protected List<Marinete> doInBackground(Void... params) {
            if (listaMarinete == null || listaMarinete.isEmpty()) {
                marineteRestClient = new MarineteRestClient();
                listaMarinete = marineteRestClient.ListarMarinetes();
                return listaMarinete;
            }
            return listaMarinete;
        }

        @Override
        protected void onPostExecute(List<Marinete> marinetes) {
            super.onPostExecute(marinetes);

            adapter = new MarineteAdapter(MarineteActivity.this, marinetes);
            listViewMarinete.setAdapter(adapter);
        }
    }


}
