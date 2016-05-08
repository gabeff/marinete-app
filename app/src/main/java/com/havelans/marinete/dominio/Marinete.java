package com.havelans.marinete.dominio;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by gabriel.fernandes on 04/05/2016.
 */
public class Marinete implements Parcelable {

    private Integer id;
    private String nome;
    private Date nascimento;
    private String cidade;
    private String estado;
    private Double avaliacao;

    public Marinete() {
    }

    protected Marinete(Parcel in) {
        id = in.readInt();
        nome = in.readString();
        nascimento = (Date) in.readSerializable();
        cidade = in.readString();
        estado = in.readString();
        avaliacao = in.readDouble();
    }

    public static final Creator<Marinete> CREATOR = new Creator<Marinete>() {
        @Override
        public Marinete createFromParcel(Parcel in) {
            return new Marinete(in);
        }

        @Override
        public Marinete[] newArray(int size) {
            return new Marinete[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nome);
        dest.writeSerializable(nascimento);
        dest.writeString(cidade);
        dest.writeString(estado);
        dest.writeDouble(avaliacao);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getNascimento() {
        return nascimento;
    }

    public void setNascimento(Date nascimento) {
        this.nascimento = nascimento;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public int getIdade() {

        GregorianCalendar cal = new GregorianCalendar();
        int y, m, d, a;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        int _year = Integer.parseInt(formatter.format(getNascimento()));

        formatter = new SimpleDateFormat("MM");
        int _month = Integer.parseInt(formatter.format(getNascimento()));

        formatter = new SimpleDateFormat("dd");
        int _day = Integer.parseInt(formatter.format(getNascimento()));


        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH);
        d = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(_year, _month, _day);
        a = y - cal.get(Calendar.YEAR);
        if ((m < cal.get(Calendar.MONTH))
                || ((m == cal.get(Calendar.MONTH)) && (d < cal
                .get(Calendar.DAY_OF_MONTH)))) {
            --a;
        }
        if (a < 0)
            throw new IllegalArgumentException("Age < 0");
        return a;
    }
}
