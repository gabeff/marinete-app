package com.havelans.marinete;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.havelans.marinete.dominio.Usuario;
import com.havelans.marinete.rest.UsuarioRestClient;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

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

    private void imprimirMensagem(String mensagem) {
        Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_LONG).show();
    }

    private class AsyncUsuario extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            usuarioRestClient = new UsuarioRestClient();
            String retorno = usuarioRestClient.SignUsuario(params[0], usuario);
            if (params[0].equals("logar")) {
                if (retorno != null && retorno.equals("1")) {
                    listar();
                } else {
                    imprimirMensagem("Erro: " + retorno);
                }
            } else if (params[0].equals("cadastrar")) {
                if (retorno != null && retorno.equals("1")) {
                    imprimirMensagem("Cadastro realizado com sucesso!");
                } else {
                    imprimirMensagem("Erro: " + retorno);
                }
            }
            return null;
        }
    }
}
