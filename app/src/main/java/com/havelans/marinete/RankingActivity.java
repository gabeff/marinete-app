package com.havelans.marinete;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.havelans.marinete.dominio.Marinete;
import com.havelans.marinete.rest.MarineteRestClient;

import java.util.ArrayList;
import java.util.List;

public class RankingActivity extends MarineteActionBar {

    ListView listViewMarinete;
    ArrayList<Marinete> listaMarinete = new ArrayList<>();
    MarineteRestClient marineteRestClient;

    private MarineteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        setupActionBar();

        listViewMarinete = (ListView) findViewById(R.id.lista_marinete);

        listViewMarinete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), MarineteActivity.class);
                startActivity(intent);
            }
        });

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

            adapter = new MarineteAdapter(RankingActivity.this, marinetes);
            listViewMarinete.setAdapter(adapter);
        }
    }


}
