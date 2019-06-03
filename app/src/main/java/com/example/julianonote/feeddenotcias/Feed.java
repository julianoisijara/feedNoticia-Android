package com.example.julianonote.feeddenotcias;

public class Feed {

    private String mTema;
    private String mTitulo;
    private String mTime;
    private String mUrl;

    public Feed(String tema, String titulo, String time, String url) {

        mTema = tema;
        mTitulo = titulo;
        mTime = time;
        mUrl = url;
    }

    public String getTema() {
        return mTema;
    }

    public String getTitulo() {
        return mTitulo;
    }

    public String getTime() {
        return mTime;
    }

    public String getUrl() {
        return mUrl;
    }
}
