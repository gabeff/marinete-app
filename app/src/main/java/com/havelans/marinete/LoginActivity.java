package com.havelans.marinete;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.havelans.marinete.dominio.Usuario;
import com.havelans.marinete.rest.RestUsuario;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextNome;
    private EditText editTextSenha;
    private Button buttonLogin;
    private TextView textViewCadastrar;
    private Usuario usuario;
    private RestUsuario restUsuario;

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
        init();
    }

    @Override
    public void onClick(View v) {
        usuario = new Usuario();
        usuario.setNome(editTextNome.getText().toString());
        usuario.setSenha(editTextSenha.getText().toString());
        String retorno;
        switch (v.getId()) {
            case R.id.button_login:
//                restUsuario = new RestUsuario("login",usuario);
//                retorno = restUsuario.signUsuario();
//                if (retorno.equals(1)) {
//                    listar();
//                } else {
//                    imprimirMensagem("Login e/ou Senha inválidos");
//                }
                new AsyncSignUsuario().execute("login");
                break;
            case R.id.text_view_cadastrar:
                restUsuario = new RestUsuario("cadastrar", usuario);
                retorno = restUsuario.signUsuario();
                if (retorno.equals(1)) {
                    imprimirMensagem("Cadastro realizado com sucesso!");
                } else {
                    imprimirMensagem("Erro no cadastro, favor tentar novamente.");
                }
                break;
        }

    }

    private void listar() {
        Intent intent = new Intent(this, MarineteActivity.class);
        startActivity(intent);
    }

    private void imprimirMensagem(String mensagem) {
        Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_LONG).show();
    }

    private class AsyncSignUsuario extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            restUsuario = new RestUsuario(params[0], usuario);
            String retorno = restUsuario.signUsuario();
            return retorno;
        }

        @Override
        protected void onPostExecute(String retorno) {
            if (retorno != null && retorno.equals("1")) {
                listar();
            } else {
                imprimirMensagem("Login e/ou Senha inválidos");
            }
        }
    }
}
