package com.chave.pojo;

import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.http.message.responses.HttpResponse;
import lombok.Data;

import java.util.ArrayList;

@Data
public class OriginRequestItem {
    private String host;
    private String path;
    private String responseLength;
    private String responseCode;
    private ArrayList<FuzzRequestItem> fuzzRequestArrayList;
    private HttpRequest originRequest;
    private HttpResponse originResponse;

    public OriginRequestItem(String host, String path, String responseLength, String responseCode) {
        this.host = host;
        this.path = path;
        this.responseLength = responseLength;
        this.responseCode = responseCode;
        this.fuzzRequestArrayList = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        if (((OriginRequestItem) o).getPath().equals(this.path) && ((OriginRequestItem) o).getHost().equals(this.host)) {
            return true;
        }

        return false;
    }
}
