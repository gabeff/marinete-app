package com.havelans.marinete.rest;

import android.os.AsyncTask;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.havelans.marinete.dominio.Marinete;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;

/**
 * Created by gabriel.fernandes on 09/05/2016.
 */
public class MarineteRestClient extends RestClient {

    ArrayList<Marinete> listaMarinete = new ArrayList<>();

    public MarineteRestClient() {
        gson = new GsonBuilder().create();
    }

    public ArrayList<Marinete> ListarMarinetes() {
        this.opcao = "listar";
        return new AsyncListarMarinetes().doInBackground();
    }

    public class AsyncListarMarinetes extends AsyncTask<Void, Void, ArrayList<Marinete>> {


        @Override
        protected ArrayList<Marinete> doInBackground(Void... params) {
            HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
            urlBuilder.addPathSegments("marinete");
            urlBuilder.addPathSegments(opcao);
            String url = urlBuilder.build().toString();
            try {
                String json = doGet(url);
                Type type = new TypeToken<List<Marinete>>() {
                }.getType();
                listaMarinete = gson.fromJson(json, type);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return listaMarinete;
        }
    }
}
