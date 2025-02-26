package com.chave;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.logging.Logging;
import com.chave.handler.AutoFuzzHandler;
import com.chave.menu.AutoFuzzMenu;
import com.chave.ui.MainUI;

public class Main implements BurpExtension {
    public static MontoyaApi API;
    public static Logging LOG;
    public static MainUI MainUI;
    @Override
    public void initialize(MontoyaApi api) {
        // 初始化api与log
        API = api;
        LOG = api.logging();

        // banner info
        API.extension().setName("AutoFuzz");
        LOG.logToOutput("AutoFuzz v1.0");
        LOG.logToOutput("Author: Chave");
        LOG.logToOutput("Github: https://github.com/Chave0v0/AutoFuzz");

        // 初始化ui
        MainUI = new MainUI();
        API.userInterface().registerSuiteTab("AutoFuzz", MainUI.getMainSplitPane());
        API.http().registerHttpHandler(new AutoFuzzHandler());
        API.userInterface().registerContextMenuItemsProvider(new AutoFuzzMenu());
    }
}
