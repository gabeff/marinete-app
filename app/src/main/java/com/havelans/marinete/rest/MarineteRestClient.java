package com.havelans.marinete.rest;

import android.os.AsyncTask;

import com.google.gson.GsonBuilder;
import com.havelans.marinete.dominio.Marinete;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.HttpUrl;

/**
 * Created by gabriel.fernandes on 09/05/2016.
 */
public class MarineteRestClient extends RestClient {

    ArrayList<Marinete> listaMarinete = new ArrayList<>();

    public MarineteRestClient() {
        gson = new GsonBuilder().create();
    }

    public JSONObject ListarMarinetes(String token) {
        this.opcao = "listar";
        return new AsyncListarMarinetes().doInBackground(token);
    }

    public class AsyncListarMarinetes extends AsyncTask<String, Void, JSONObject> {


        @Override
        protected JSONObject doInBackground(String... params) {
            HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
            urlBuilder.addPathSegments("marinete");
            urlBuilder.addPathSegments(opcao);
            String url = urlBuilder.build().toString();
            JSONObject jsonObject = doGet(url, params[0]);
            if (jsonObject != null) {
                return jsonObject;
            }
            return null;
        }
    }
}
