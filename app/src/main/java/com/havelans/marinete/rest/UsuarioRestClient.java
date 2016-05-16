package com.havelans.marinete.rest;

import android.os.AsyncTask;

import com.google.gson.GsonBuilder;
import com.havelans.marinete.dominio.Usuario;

import java.io.IOException;

import okhttp3.HttpUrl;

/**
 * Created by gabriel.fernandes on 09/05/2016.
 */
public class UsuarioRestClient extends RestClient {

    private Usuario usuario;

    public UsuarioRestClient() {
        gson = new GsonBuilder().create();
    }

    public String SignUsuario(String opcao, Usuario usuario) {
        this.opcao = opcao;
        this.usuario = usuario;
        return new AsyncSignUsuario().doInBackground();
    }

    private class AsyncSignUsuario extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
            urlBuilder.addPathSegments("usuario");
            urlBuilder.addPathSegments(opcao);
            String url = urlBuilder.build().toString();
            String response = null;
            String json = gson.toJson(usuario);
            try {
                response = doPost(url, json.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }
    }
}
