package com.chave;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.logging.Logging;

public class Main implements BurpExtension {
    public static MontoyaApi API;
    public static Logging LOG;
    @Override
    public void initialize(MontoyaApi api) {
        // 初始化api与log
        Main.API = api;
        Main.LOG = api.logging();

        Main.LOG.logToOutput("AutoFuzz v1.0");
    }
}
