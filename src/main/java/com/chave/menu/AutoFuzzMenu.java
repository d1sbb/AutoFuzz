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
        JMenuItem sentToAutoFuzzMenuItem = new JMenuItem("Send to AutoFuzz");

        HttpRequestResponse httpRequestResponse = event.messageEditorRequestResponse().get().requestResponse();
        HttpRequest request = httpRequestResponse.request();

        sentToAutoFuzzMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    autoFuzzService.preFuzz(request);

//                    ExecutorService executorService = Executors.newSingleThreadExecutor();
//                    executorService.submit(new Runnable() {
//                        @Override
//                        public void run() {
                            Main.API.http().sendRequest(request);  // 发送一个请求用于触发response handler
//                        }
//                    });
//                    executorService.shutdownNow();

                } catch (Exception exception) {
                    Main.LOG.logToError("右键主动fuzz出现异常" + exception.getCause());
                }
            }
        });

        menus.add(sentToAutoFuzzMenuItem);
        return menus;
    }
}
