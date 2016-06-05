package com.havelans.marinete.rest;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by gabriel.fernandes on 06/05/2016.
 */
public class RestClient {
    //public static final String BASE_URL = "https://marinete.herokuapp.com/webservice";
    public static final String BASE_URL = "http://192.168.1.6:8080/Marinete/rest/";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();
    public Gson gson;
    public String opcao;
    private String resp;
    private String msg;
    private JSONObject jsonObject;
    private Response response;

    protected JSONObject doGet(String url, String token) {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization","Bearer "+token)
                .build();

        execRequest(request);

        buildJson();

        return jsonObject;
    }

    protected JSONObject doPost(String url, String json) {
        response = null;
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        execRequest(request);

        buildJson();

        return jsonObject;
    }

    private void execRequest(Request request) {
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                resp = response.header("response");
                msg = response.body().string();
            } else if (response.code() == 401) {
                resp = "0";
                msg = "UNAUTHORIZED";
            } else {
                resp = "0";
                msg = "Algo está errado: "+response.message();
            }
        } catch (IOException e) {
            resp = "0";
            e.printStackTrace(); //lembrar de remover em produção
            Log.e("ERRO", e.getClass().getCanonicalName()); //lembrar de remover em produção
            if (e.getClass().getCanonicalName().equals("javax.net.ssl.SSLHandshakeException")) {
                msg = "Problema de certificado SSL, favor verificar sua rede.";
            } else if (e.getClass().getCanonicalName().equals("java.net.SocketTimeoutException")) {
                msg = "Servidor offline, tente novamente em alguns segundos";
            } else {
                msg = "Algo está errado, favor tentar novamente";
            }
        }
    }

    private void buildJson() {
        jsonObject = new JSONObject();
        try {
            jsonObject.put("resp",resp);
            jsonObject.put("msg",msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
