package com.havelans.marinete;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.havelans.marinete.dominio.Marinete;
import com.havelans.marinete.rest.MarineteRestClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RankingActivity extends Utils {

    //Declarar todos elementos que devem ser manuseados da atividade aqui
    private ListView listViewMarinete;
    private SwipeRefreshLayout swipeRefreshLayout;

    //Demais declarações
    private ArrayList<Marinete> listaMarinete = new ArrayList<>();
    private MarineteRestClient marineteRestClient;
    private MarineteAdapter adapter;

    //Inicialização de variáveis
    private void init() {
        setupActionBar();

        listViewMarinete = (ListView) findViewById(R.id.lista_marinete);

        //carregar marinete selecionada na lista para a tela de detalhamento
        listViewMarinete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Marinete marinete = adapter.getItem(position);
                Intent intent = new Intent(view.getContext(), MarineteActivity.class);
                intent.putExtra("marinete", marinete);
                startActivity(intent);
            }
        });

        sharedPreferences = getSharedPreferences();
        editor = sharedPreferences.edit();

        gson = new GsonBuilder().create();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        //implementar o que acontece quando o usuario rola a tela para cima, no topo da lista
        //recarregar lista de marinetes do webservice
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listaMarinete = new ArrayList<>();
                new CarregaMarinetesAsynkTask().execute();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        init();

        //No caso de não haver uma instancia salva da tela, carregar marinetes do webservice
        if (savedInstanceState == null) {
            new CarregaMarinetesAsynkTask().execute();
        }
    }

    @Override
    //salvar lista de marinetes na tela antes da destruição da tela
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("lista", listaMarinete);
    }

    @Override
    //carregar lista de marinetes salva anteriormente, quando tela for chamada novamente
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        listaMarinete = savedInstanceState.getParcelableArrayList("lista");
        new CarregaMarinetesAsynkTask().execute();
    }

    //voltar para tela de login no caso de sessão expirada
    private void loginAgain() {
        //remover token da sharedprefences, ja que o mesmo não é mais valido
        editor.remove("token");
        editor.commit();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    //chamadas assincronas para o webservice
    public class CarregaMarinetesAsynkTask extends AsyncTask<Void, Void, JSONObject> {


        @Override
        //verifica se ha uma lista de marinetes salva, se houver, carrega a propria
        //se nao, carrega a lista atraves do webservice
        protected JSONObject doInBackground(Void... params) {

            if (listaMarinete == null || listaMarinete.isEmpty()) {
                marineteRestClient = new MarineteRestClient();
                JSONObject jsonObject = marineteRestClient.ListarMarinetes(sharedPreferences.getString("token", null));

                if (jsonObject != null) {
                    return jsonObject;
                }
                return null;
            }
            return null;
        }

        @Override
        //antes de executar, mostrar a animação de carregando
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected void onPostExecute(JSONObject response) {
            super.onPostExecute(response);

            String retorno = null;
            String msg = null;

            //parar animação de carregamento
            swipeRefreshLayout.setRefreshing(false);

            //no caso de sucesso na chamada com o webservice, ou de haver uma lista salva
            //mostrar lista de marinetes disponiveis no ranking
            //se não, mostrar erro ocorrido no carregamento
            if (response != null) {
                try {
                    retorno = response.getString("resp");
                    msg = response.getString("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (retorno.equals("1")) {
                    Type type = new TypeToken<List<Marinete>>() {
                    }.getType();
                    listaMarinete = gson.fromJson(msg, type);
                    if (listaMarinete != null) {
                        adapter = new MarineteAdapter(RankingActivity.this, listaMarinete);
                        listViewMarinete.setAdapter(adapter);
                    }
                } else if (msg.equals("UNAUTHORIZED")) {
                    Toast.makeText(getApplicationContext(), "Autorização Inválida", Toast.LENGTH_LONG).show();
                    loginAgain();
                } else {
                    Toast.makeText(getApplicationContext(), "Erro ao carregar Marinetes: " + msg, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Erro ao carregar Marinetes, favor tentar novamente", Toast.LENGTH_LONG).show();
            }

        }
    }


}
