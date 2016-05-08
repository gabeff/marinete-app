package com.havelans.marinete.rest;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.havelans.marinete.dominio.Usuario;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by gabriel.fernandes on 06/05/2016.
 */
public class RestUsuario {
    private static final String URL = "http://192.168.43.52:8080/Marinete/webservice/usuario";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private Usuario usuario;
    private String opcao;

    private Gson gson;

    OkHttpClient client = new OkHttpClient();

    public RestUsuario(String opcao, Usuario usuario) {
        gson = new GsonBuilder().create();
        this.opcao = opcao;
        this.usuario = usuario;
    }

    public String signUsuario() {
        return new AsyncPostUsuario().doInBackground();
    }

    private class AsyncPostUsuario extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String response = null;
            String json = gson.toJson(usuario);
            Log.d("TESTE", json);
            try {
                response = doPost(URL, json.toString());
                Log.d("TESTE", response);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }
    }

    private String doPost(String url, String json) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        urlBuilder.addPathSegments(opcao);
        url = urlBuilder.build().toString();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
