package com.havelans.marinete;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.google.gson.Gson;

/**
 * Created by gabriel.fernandes on 22/05/2016.
 * Classe de utilidades comuns para as demais
 * atividades do app, todos os códigos que
 * possam ser reutilizados em mais de uma
 * atividade devem postos aqui.
 */
public class Utils extends AppCompatActivity {

    //gson para de/serializacao entre json e objetos java
    public Gson gson;

    //SharedPreferences é onde devem ser armazenados dados
    //simples e pequenos. Ex.: token de sessão
    //manuseamos o conteudo da sharedpreferences atraves do editor
    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor editor;

    //carregar nossa actionbar customizada na atividade
    protected void setupActionBar() {
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
        LayoutInflater inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.action_bar_marinete, null);

        ab.setCustomView(v);
    }

    //carregar sharedpreferences
    public SharedPreferences getSharedPreferences() {
        sharedPreferences = getApplication().getSharedPreferences("MarinetePref", 0); // 0 - para modo privado;
        return sharedPreferences;
    }
}
