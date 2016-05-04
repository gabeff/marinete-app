package com.havelans.marinete;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String URL = "https://marinete.herokuapp.com/webservice/usuarioResource";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    private EditText editTextNome;
    private EditText editTextSenha;
    private Button buttonLogin;
    private TextView textViewCadastrar;
    private String nomeUsuario;
    private String senhaUsuario;

    private void init() {
        editTextNome = (EditText) findViewById(R.id.edit_text_nome);
        editTextSenha = (EditText) findViewById(R.id.edit_text_senha);
        buttonLogin = (Button) findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(this);
        textViewCadastrar = (TextView) findViewById(R.id.text_view_cadastrar);
        textViewCadastrar.setOnClickListener(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        Em caso de precisar realizar request nao assincrono na classe principal
//        int SDK_INT = android.os.Build.VERSION.SDK_INT;
//        if (SDK_INT > 8) {
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
//                    .permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//        }

        init();
    }

    @Override
    public void onClick(View v) {
        nomeUsuario = editTextNome.getText().toString();
        senhaUsuario = editTextSenha.getText().toString();
        switch (v.getId()) {
            case R.id.button_login:
                new AsyncLogin().execute();
                break;
            case R.id.text_view_cadastrar:
                new AsyncCadastrar().execute();
                break;
        }


    }

    private void imprimirMensagem(String mensagem) {
        Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_LONG).show();
    }

    private class AsyncLogin extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls)  {
            String response = null;
            String json = buildJson(nomeUsuario,senhaUsuario);
            try {
                response = login(URL, json.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            imprimirMensagem(result);
        }

    }

    private class AsyncCadastrar extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls)  {
            String response = null;
            String json = buildJson(nomeUsuario,senhaUsuario);
            try {
                response = cadastrar(URL, json.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            imprimirMensagem(result);
        }

    }

    private String login(String url, String json) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        urlBuilder.addPathSegments("login");
        url = urlBuilder.build().toString();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private String cadastrar(String url, String json) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        urlBuilder.addPathSegments("cadastrar");
        url = urlBuilder.build().toString();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private String buildJson(String nome, String senha) {
        JSONObject json = new JSONObject();
        try {
            json.put("nome", nome);
            json.put("senha", senha);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }
}
