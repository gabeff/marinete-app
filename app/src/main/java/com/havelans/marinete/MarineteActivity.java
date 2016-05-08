package com.havelans.marinete;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.havelans.marinete.dominio.Marinete;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MarineteActivity extends AppCompatActivity {

    private static final String URL = "http://192.168.43.52:8080/Marinete/webservice/marinete";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();
    private Gson gson;

    ListView listViewMarinete;
    ArrayList<Marinete> listaMarinete = new ArrayList<>();

    private MarineteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marinete);

        setupActionBar();

        listViewMarinete = (ListView) findViewById(R.id.lista_marinete);

        gson = new GsonBuilder().create();

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
            try {
                if (listaMarinete == null || listaMarinete.isEmpty()) {
                    String json = listarMarinetes(URL);
                    Type type = new TypeToken<List<Marinete>>() {
                    }.getType();
                    listaMarinete = gson.fromJson(json, type);
                }
            } catch (IOException e) {
                e.printStackTrace();
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

    private String listarMarinetes(String url) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        urlBuilder.addPathSegments("listar");
        url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }


}
