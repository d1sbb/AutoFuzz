package com.chave.pojo;

import burp.api.montoya.http.message.HttpRequestResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.util.Objects;

@Data
@NoArgsConstructor
public class FuzzRequestItem {
    private String param;
    private String payload;
    private String responseLength;
    private String responseLengthChange;
    private String responseCode;
    private OriginRequestItem originRequestItem;
    private HttpRequestResponse fuzzRequestResponse;

    public FuzzRequestItem(String param, String payload, String responseLength, String responseLengthChange, String responseCode, OriginRequestItem originRequestItem) {
        this.param = param;
        this.payload = payload;
        this.responseLength = responseLength;
        this.responseLengthChange = responseLengthChange;
        this.responseCode = responseCode;
        this.originRequestItem = originRequestItem;
    }
}
