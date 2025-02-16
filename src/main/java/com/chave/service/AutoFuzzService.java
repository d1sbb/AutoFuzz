package com.chave.service;

import burp.api.montoya.http.handler.HttpRequestToBeSent;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.params.HttpParameter;
import burp.api.montoya.http.message.params.HttpParameterType;
import burp.api.montoya.http.message.params.ParsedHttpParameter;
import burp.api.montoya.http.message.requests.HttpRequest;
import com.chave.Main;
import com.chave.config.Config;
import com.chave.pojo.Data;
import com.chave.pojo.FuzzRequestItem;
import com.chave.pojo.OriginRequestItem;
import com.chave.utils.Util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AutoFuzzService {

    // 做一些准备工作  解析参数  准备待发送请求列表  初始化表格数据
    public void preFuzz(HttpRequestToBeSent requestToBeSent) throws UnsupportedEncodingException, MalformedURLException {
        // 如果没有参数 直接返回
        if (!requestToBeSent.hasParameters()) {
            return;
        }

        // 如果是静态资源 直接返回
        for (String suffix : Config.STATIC_RESOURCE) {
            if (requestToBeSent.pathWithoutQuery().endsWith(suffix)) {
                return;
            }
        }


        // 创建表格数据
        OriginRequestItem originRequestItem = new OriginRequestItem(new URL(requestToBeSent.url()).getHost(), requestToBeSent.pathWithoutQuery(), null, null);
        // 保存原请求数据
        originRequestItem.setOriginRequest(requestToBeSent);
        Data.ORIGIN_REQUEST_TABLE_DATA.put(requestToBeSent.messageId(), originRequestItem);
        // 创建newRequestToBeSent列表
        ArrayList<HttpRequest> newRequestToBeSentList = new ArrayList<>();
        Data.NEW_REQUEST_TO_BE_SENT_DATA.put(requestToBeSent.messageId(), newRequestToBeSentList);


        // 处理没有json参数的情况
        if (!requestToBeSent.hasParameters(HttpParameterType.JSON)) {
            // 获取所有请求参数
            List<ParsedHttpParameter> parameters = requestToBeSent.parameters();
            for (ParsedHttpParameter parameter : parameters) {
                // 这里不考虑cookie，multipart，xml
                if (parameter.type().equals(HttpParameterType.COOKIE) || parameter.type().equals(HttpParameterType.MULTIPART_ATTRIBUTE) || parameter.type().equals(HttpParameterType.XML) || parameter.type().equals(HttpParameterType.XML_ATTRIBUTE)) {
                    continue;
                }
                for (String payload : Data.PAYLOAD_LIST) {
                    // 创建新请求
                    HttpRequest newRequest = requestToBeSent.withParameter(HttpParameter.parameter(parameter.name(), Util.urlEncode(payload), parameter.type()));

                    newRequestToBeSentList.add(newRequest);  // 添加待发送请求
                    originRequestItem.getFuzzRequestItemArrayList().add(new FuzzRequestItem(parameter.name(), payload, null, null, originRequestItem));  // 添加表格数据
                }
            }

        } else {  // 处理请求中有json参数的情况
            // 获取所有非json参数
            for (ParsedHttpParameter parameter : requestToBeSent.parameters()) {

            }
        }
    }


    // 发送请求
    public void startFuzz(int msgId) {
        // 如果记录中没有的 直接return掉
        if (!Data.NEW_REQUEST_TO_BE_SENT_DATA.containsKey(msgId)) {
            return;
        }
        ArrayList<HttpRequest> requestToBeSentList = Data.NEW_REQUEST_TO_BE_SENT_DATA.get(msgId);
        ArrayList<FuzzRequestItem> fuzzRequestItemArrayList = Data.ORIGIN_REQUEST_TABLE_DATA.get(msgId).getFuzzRequestItemArrayList();

        int i = 0;
        for (HttpRequest request : requestToBeSentList) {
            HttpRequestResponse httpRequestResponse = Main.API.http().sendRequest(request);
            FuzzRequestItem fuzzRequestItem = fuzzRequestItemArrayList.get(i);
            fuzzRequestItem.setFuzzRequestResponse(httpRequestResponse);  // 与table数据关联

            // 获取返回包长度变化
            int lengthChange = httpRequestResponse.response().toString().length() - Integer.parseInt(fuzzRequestItem.getOriginRequestItem().getResponseLength());
            fuzzRequestItem.setResponseLengthChange((lengthChange > 0 ? "+" + lengthChange : String.valueOf(lengthChange)));
            fuzzRequestItem.setResponseCode(httpRequestResponse.response().statusCode() + "");

            i++;
        }

        Data.NEW_REQUEST_TO_BE_SENT_DATA.remove(msgId);

    }



}
