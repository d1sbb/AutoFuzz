package com.chave.menu;

import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.ui.contextmenu.ContextMenuEvent;
import burp.api.montoya.ui.contextmenu.ContextMenuItemsProvider;
import com.chave.Main;
import com.chave.service.AutoFuzzService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AutoFuzzMenu implements ContextMenuItemsProvider {
    private AutoFuzzService autoFuzzService = new AutoFuzzService();
    public static Integer ID = -1;

    @Override
    public List<Component> provideMenuItems(ContextMenuEvent event) {

        // 定义菜单
        ArrayList<Component> menus = new ArrayList<>();
        event.messageEditorRequestResponse().ifPresent(editorReqResp -> {
            HttpRequest request = editorReqResp.requestResponse().request();

            JMenuItem sentToAutoFuzzMenuItem = new JMenuItem("Send to AutoFuzz");
            sentToAutoFuzzMenuItem.addActionListener(e -> {
                try {
                    autoFuzzService.preFuzz(request);

                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    executorService.submit(() -> {
                        Main.API.http().sendRequest(request);
                    });
                    executorService.shutdownNow();

                } catch (Exception ex) {
                    Main.LOG.logToError("右键主动fuzz出现异常: " + ex.getMessage());
                }
            });

            menus.add(sentToAutoFuzzMenuItem);
        });
        return menus;
    }
}
