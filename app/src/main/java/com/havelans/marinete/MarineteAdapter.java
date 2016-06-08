package com.havelans.marinete;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.havelans.marinete.dominio.Marinete;
import com.havelans.marinete.rest.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by gabriel.fernandes on 04/05/2016.
 * adapter do objeto marinete para carregar views
 */
public class MarineteAdapter extends BaseAdapter {

    //variaveis
    private Context context;
    private List<Marinete> marinetes;

    //construtor, inicializando variaveis
    public MarineteAdapter(Context context, List<Marinete> marinetes) {
        this.context = context;
        this.marinetes = marinetes;
    }

    @Override
    //retorna tamanho da lista
    public int getCount() {
        return marinetes.size();
    }

    @Override
    //retorna item selecionado da lista
    public Marinete getItem(int position) {
        return marinetes.get(position);
    }

    @Override
    //retorna id do item selecionado
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    //carrega view, nesse caso a list view da atividade ranking
    public View getView(int position, View convertView, ViewGroup parent) {
        //inflar view com o layout customizado
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_marinete, null);
        }

        //carregar elementos da view com os atributos das marinetes
        ImageView imageView = (ImageView) convertView.findViewById(R.id.foto_marinete);
        Picasso.with(context)
                .load("http://www.marineteapp.com.br/imagens/marinetes/marinete-"+getItemId(position)+".png")
                .error(R.mipmap.ic_launcher)
                .transform(new CircleTransform())
                .into(imageView);

        TextView text = (TextView) convertView.findViewById(R.id.nome_marinete);
        text.setText(getItem(position).getNome());
        TextView text2 = (TextView) convertView.findViewById(R.id.idade_marinete);
        text2.setText(getItem(position).getIdade()+" anos");
        RatingBar progress = (RatingBar) convertView.findViewById(R.id.avaliacao_marinete);
        progress.setRating(getItem(position).getAvaliacao().floatValue());

        return convertView;
    }
}
