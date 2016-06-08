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

    //Declarar todos elementos que devem ser manuseados da atividade aqui
    private EditText editTextNome;
    private EditText editTextSenha;
    private Button buttonLogin;
    private TextView textViewCadastrar;

    //Demais declarações
    private Usuario usuario;
    private UsuarioRestClient usuarioRestClient;

    //Inicialização de variáveis
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
        //No caso de existir um token salvo, tentar autenticar usuário automaticamente
        if (sharedPreferences.getString("token", null) != null) {
            listar();
        }
    }

    @Override
    //implementar onclick da classe View.OnClickListener
    //instanciar um usuario com o login e senha fornecidos
    //e executar a ação desejada verificando qual botão acionado
    public void onClick(View v) {
        usuario = new Usuario();
        usuario.setNome(editTextNome.getText().toString());
        usuario.setSenha(editTextSenha.getText().toString());
        switch (v.getId()) {
            case R.id.button_login:
                new AsyncUsuario().execute("logar");
                break;
            case R.id.text_view_cadastrar:
                Intent intent = new Intent(this, CadastrarActivity.class);
                startActivity(intent);
                break;
        }

    }

    //chamar atividade de ranking das marinetes
    private void listar() {
        Intent intent = new Intent(this, RankingActivity.class);
        startActivity(intent);
    }

    //chamadas assincronas para o webservice
    //o webservice retornar um JSON, interpretar retorno e msg do webservice
    //em caso de retorno = 1, método chamado obteve sucesso
    //se 0, houve algum erro que deve ser especificado na msg
    private class AsyncUsuario extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... opcao) {
            usuarioRestClient = new UsuarioRestClient();
            JSONObject jsonObject = usuarioRestClient.SignUsuario(opcao[0], usuario);

            String retorno = null;
            String msg = null;
            try {
                retorno = jsonObject.getString("resp");
                msg = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (opcao[0].equals("logar")) {
                if (retorno != null && retorno.equals("1")) {
                    //logado com sucesso, salvar token para utilizar em todas futuras
                    //comunicações com o webservice
                    editor.putString("token", msg);
                    editor.commit();
                    listar();
                    return "Bem-vindo, " + usuario.getNome() + "!";
                } else if (msg.equals("UNAUTHORIZED")) {
                    return "Login e/ou senha inválidos";
                } else {
                    return msg;
                }
            } else if (opcao[0].equals("cadastrar")) {
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
