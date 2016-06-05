package com.havelans.marinete.rest;

import android.os.AsyncTask;

import com.google.gson.GsonBuilder;
import com.havelans.marinete.dominio.Usuario;

import org.json.JSONObject;

import okhttp3.HttpUrl;

/**
 * Created by gabriel.fernandes on 09/05/2016.
 */
public class UsuarioRestClient extends RestClient {

    private Usuario usuario;

    public UsuarioRestClient() {
        gson = new GsonBuilder().create();
    }

    public JSONObject SignUsuario(String opcao, Usuario usuario) {
        this.opcao = opcao;
        this.usuario = usuario;
        return new AsyncSignUsuario().doInBackground();
    }

    private class AsyncSignUsuario extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Void... params) {
            HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
            urlBuilder.addPathSegments("usuario");
            urlBuilder.addPathSegments(opcao);
            String url = urlBuilder.build().toString();
            String json = gson.toJson(usuario);
            JSONObject jsonObject = doPost(url, json.toString());

            if (jsonObject != null) {
                return jsonObject;
            }
            return null;
        }
    }
}
