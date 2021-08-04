package com.glowteam.Models;

public class Search {
    String q, page,url;
    int dataPerPage;

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public int getDataPerPage() {
        return dataPerPage;
    }

    public void setDataPerPage(int dataPerPage) {
        this.dataPerPage = dataPerPage;
    }
}
