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

    ImageView fotoMarinete;
    TextView nomeMarinete;
    TextView idadeMarinete;
    RatingBar avaliacaoMarinete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marinete);

        setupActionBar();

        fotoMarinete = (ImageView) findViewById(R.id.foto_marinete);
        nomeMarinete = (TextView) findViewById(R.id.nome_marinete);
        idadeMarinete = (TextView) findViewById(R.id.idade_marinete);
        avaliacaoMarinete = (RatingBar) findViewById(R.id.avaliacao_marinete);
        WebView mWebView;


        Intent intent = getIntent();
        Marinete marinete = intent.getParcelableExtra("marinete");

        Picasso.with(this)
                .load("http://www.marineteapp.com.br/imagens/marinetes/marinete-"+marinete.getId()+".png")
                .error(R.mipmap.ic_launcher)
                .transform(new CircleTransform())
                .into(fotoMarinete);
        nomeMarinete.setText(marinete.getNome());
        idadeMarinete.setText(marinete.getIdade()+" anos");
        avaliacaoMarinete.setRating(marinete.getAvaliacao().floatValue());

        mWebView = (WebView) findViewById(R.id.webview);

        String text = "<html><body>"
                + "<p align=\"justify\">"
                + "<b>Sobre mim: </b>"
                +getString(R.string.lorem_ipsum)
                + "</p> "
                + "</body></html>";

        mWebView.loadData(text, "text/html", "utf-8");

    }
}
