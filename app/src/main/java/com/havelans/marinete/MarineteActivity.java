package com.havelans.marinete;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.havelans.marinete.dominio.Marinete;
import com.havelans.marinete.rest.CircleTransform;
import com.squareup.picasso.Picasso;

public class MarineteActivity extends Utils {

    //Declarar todos elementos que devem ser manuseados da atividade aqui
    private ImageView fotoMarinete;
    private TextView nomeMarinete;
    private TextView idadeMarinete;
    private RatingBar avaliacaoMarinete;
    private WebView mWebView;

    //Inicialização de variáveis
    private void init() {
        setupActionBar();

        fotoMarinete = (ImageView) findViewById(R.id.foto_marinete);
        nomeMarinete = (TextView) findViewById(R.id.nome_marinete);
        idadeMarinete = (TextView) findViewById(R.id.idade_marinete);
        avaliacaoMarinete = (RatingBar) findViewById(R.id.avaliacao_marinete);

        //Carregar marinete selecionada na lista
        Intent intent = getIntent();
        Marinete marinete = intent.getParcelableExtra("marinete");

        //Carregar foto da marinete no servidor passando o id da mesma
        Picasso.with(this)
                .load("http://www.marineteapp.com.br/imagens/marinetes/marinete-" +
                        marinete.getId() +
                        ".png")
                .error(R.mipmap.ic_launcher) //img a mostrar em caso de erro
                .transform(new CircleTransform()) //transformar img em um circulo
                .into(fotoMarinete); //elemento onde imagem sera mostrada
        //setar demais elementos da tela
        nomeMarinete.setText(marinete.getNome());
        idadeMarinete.setText(marinete.getIdade() + " anos");
        avaliacaoMarinete.setRating(marinete.getAvaliacao().floatValue());

        //webview com descricao da marinete, utilizado devido a falta de suporte
        //para texto justificado do elemento textview do android
        mWebView = (WebView) findViewById(R.id.webview);

        String text = "<html><body>"
                + "<p align=\"justify\">"
                + "<b>Sobre mim: </b>"
                + getString(R.string.lorem_ipsum)
                + "</p> "
                + "</body></html>";

        mWebView.loadData(text, "text/html", "utf-8");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marinete);
        init();
    }
}
