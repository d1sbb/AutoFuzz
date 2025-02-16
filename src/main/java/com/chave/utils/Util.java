package com.chave.utils;

import com.chave.Main;
import com.chave.pojo.Data;
import com.chave.pojo.FuzzRequestItem;
import com.chave.pojo.OriginRequestItem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

public class Util {

    // 刷新fuzz请求列表
    public static void addOriginRequestItem(OriginRequestItem item) {
        JTable originRequestItemTable = Main.MainUI.getOriginRequestItemTable();
        DefaultTableModel model = (DefaultTableModel) originRequestItemTable.getModel();

        model.addRow(new Object[]{item.getHost(), item.getPath(), item.getResponseLength(), item.getResponseCode()});
    }



    // 刷新domain表格
    public static synchronized void flushConfigTable(String type, JTable table) {
        ArrayList<String> targetList = null;
        if (type.equals("domain")) {
            targetList = Data.DOMAIN_LIST;
        } else if (type.equals("payload")) {
            targetList = Data.PAYLOAD_LIST;
        }
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        for (String item : targetList) {
            model.addRow(new Object[]{item});
        }
    }


    // 用户添加配置
    public static void addConfigData(String type, JTextArea userInputTextArea) {

        String userInput = userInputTextArea.getText();
        // 防止空指针
        if (userInput == null) {
            return;
        }

        // 去除前后空格换行符
        userInput = userInput.trim();

        // 防止只有空白字符
        if (userInput.length() == 0) {
            return;
        }

        ArrayList targetList = null;
        if (type.equals("domain")) {
            targetList = Data.DOMAIN_LIST;
        } else if (type.equals("payload")) {
            targetList = Data.PAYLOAD_LIST;
        }

        String[] lines = userInput.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (!targetList.contains(line) && line != null && line.length() != 0) {
                targetList.add(line);
            }
        }
    }

    // 用户编辑配置
    public static void editConfigData(String type, JTextField userInputTextField, int row) {
        String userInput = userInputTextField.getText();
        // 防止空指针
        if (userInput == null) {
            return;
        }

        // 去除前后空格换行符
        userInput = userInput.trim();

        // 防止只有空白字符
        if (userInput.length() == 0) {
            return;
        }

        ArrayList targetList = null;
        if (type.equals("domain")) {
            targetList = Data.DOMAIN_LIST;
        } else if (type.equals("payload")) {
            targetList = Data.PAYLOAD_LIST;
        }

        if (!targetList.contains(userInput)) {
            targetList.set(row, userInput);
        }

    }

    // 用户删除配置
    public static void removeConfigData(String type, int[] rows) {
        ArrayList targetList = null;
        if (type.equals("domain")) {
            targetList = Data.DOMAIN_LIST;
        } else if (type.equals("payload")) {
            targetList = Data.PAYLOAD_LIST;
        }

        // 如果有选中的行
        if (rows.length > 0) {
            // 倒序删除 避免索引影响
            for (int i = rows.length - 1; i >= 0; i--) {
                int row = rows[i];
                // 删除选中的行
                targetList.remove(row);
            }
        }
    }

    // 对有歧义的字符进行url编码
    public static String urlEncode(String input) throws UnsupportedEncodingException {
        StringBuilder encodedString = new StringBuilder();

        // 遍历字符串中的每个字符
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            // 如果是字母或数字，则不进行编码
            if (Character.isLetterOrDigit(c)) {
                encodedString.append(c);
            } else {
                // 对所有其他字符进行URL编码
                encodedString.append(URLEncoder.encode(String.valueOf(c), "UTF-8"));
            }
        }

        return encodedString.toString();
    }
}
