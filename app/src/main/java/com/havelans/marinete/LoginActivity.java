package com.havelans.marinete;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.havelans.marinete.dominio.Usuario;
import com.havelans.marinete.rest.UsuarioRestClient;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Utils implements View.OnClickListener {

    private EditText editTextNome;
    private EditText editTextSenha;
    private Button buttonLogin;
    private TextView textViewCadastrar;
    private Usuario usuario;
    private UsuarioRestClient usuarioRestClient;

    private void init() {
        editTextNome = (EditText) findViewById(R.id.edit_text_nome);
        editTextSenha = (EditText) findViewById(R.id.edit_text_senha);
        editTextSenha.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            v.setId(R.id.button_login);
                            onClick(v);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        buttonLogin = (Button) findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(this);
        textViewCadastrar = (TextView) findViewById(R.id.text_view_cadastrar);
        textViewCadastrar.setOnClickListener(this);

        sharedPreferences = getSharedPreferences();
        editor = sharedPreferences.edit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        if (sharedPreferences.getString("token", null) != null) {
            listar();
        }
    }

    @Override
    public void onClick(View v) {
        usuario = new Usuario();
        usuario.setNome(editTextNome.getText().toString());
        usuario.setSenha(editTextSenha.getText().toString());
        switch (v.getId()) {
            case R.id.button_login:
                new AsyncUsuario().execute("logar");
                break;
            case R.id.text_view_cadastrar:
                new AsyncUsuario().execute("cadastrar");
                break;
        }

    }

    private void listar() {
        Intent intent = new Intent(this, RankingActivity.class);
        startActivity(intent);
    }

    private class AsyncUsuario extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            usuarioRestClient = new UsuarioRestClient();
            JSONObject jsonObject = usuarioRestClient.SignUsuario(params[0], usuario);

            String retorno = null;
            String msg = null;
            try {
                retorno = jsonObject.getString("resp");
                msg = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (params[0].equals("logar")) {
                if (retorno != null && retorno.equals("1")) {
                    editor.putString("token", msg);
                    editor.commit();
                    listar();
                    return "Bem-vindo, " + usuario.getNome() + "!";
                } else if (msg.equals("UNAUTHORIZED")) {
                    return "Login e/ou senha inválidos";
                } else {
                    return msg;
                }
            } else if (params[0].equals("cadastrar")) {
                if (retorno != null && retorno.equals("1")) {
                    return "Cadastro realizado com sucesso!";
                } else {
                    return msg;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
        }
    }
}
