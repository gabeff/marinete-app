package com.havelans.marinete;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.havelans.marinete.dominio.Marinete;
import com.havelans.marinete.rest.MarineteRestClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RankingActivity extends Utils {

    ListView listViewMarinete;
    ArrayList<Marinete> listaMarinete = new ArrayList<>();
    MarineteRestClient marineteRestClient;
    SwipeRefreshLayout swipeRefreshLayout;

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
                Marinete marinete = adapter.getItem(position);
                Intent intent = new Intent(view.getContext(), MarineteActivity.class);
                intent.putExtra("marinete", marinete);
                startActivity(intent);
            }
        });

        sharedPreferences = getSharedPreferences();
        editor = sharedPreferences.edit();

        gson = new GsonBuilder().create();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listaMarinete = new ArrayList<>();
                new CarregaMarinetesAsynkTask().execute();
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

    private void loginAgain() {
        editor.remove("token");
        editor.commit();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public class CarregaMarinetesAsynkTask extends AsyncTask<Void, Void, JSONObject> {


        @Override
        protected JSONObject doInBackground(Void... params) {

            if (listaMarinete == null || listaMarinete.isEmpty()) {
                marineteRestClient = new MarineteRestClient();
                JSONObject jsonObject = marineteRestClient.ListarMarinetes(sharedPreferences.getString("token", null));

                if (jsonObject != null) {
                    return jsonObject;
                }
                return null;
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected void onPostExecute(JSONObject response) {
            super.onPostExecute(response);

            String retorno = null;
            String msg = null;

            swipeRefreshLayout.setRefreshing(false);

            if (response != null) {
                try {
                    retorno = response.getString("resp");
                    msg = response.getString("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (retorno.equals("1")) {
                    Type type = new TypeToken<List<Marinete>>() {
                    }.getType();
                    listaMarinete = gson.fromJson(msg, type);
                    if (listaMarinete != null) {
                        adapter = new MarineteAdapter(RankingActivity.this, listaMarinete);
                        listViewMarinete.setAdapter(adapter);
                    }
                } else if (msg.equals("UNAUTHORIZED")) {
                    Toast.makeText(getApplicationContext(), "Autorização Inválida", Toast.LENGTH_LONG).show();
                    loginAgain();
                } else {
                    Toast.makeText(getApplicationContext(), "Erro ao carregar Marinetes: " + msg, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Erro ao carregar Marinetes, favor tentar novamente", Toast.LENGTH_LONG).show();
            }

        }
    }


}
