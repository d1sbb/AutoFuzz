package com.chave.utils;

import com.chave.Main;
import com.chave.pojo.Data;
import com.chave.pojo.OriginRequestItem;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Util {

    // 刷新fuzz请求列表
    public static synchronized void addOriginRequestItem(OriginRequestItem item) {
        try {
            JTable originRequestItemTable = Main.MainUI.getOriginRequestItemTable();
            DefaultTableModel model = (DefaultTableModel) originRequestItemTable.getModel();

            model.addRow(new Object[]{item.getId(), item.getMethod(), item.getHost(), item.getPath(), item.getResponseLength(), item.getResponseCode()});

            // 始终以id排序
            TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) originRequestItemTable.getRowSorter();
            sorter.sort();
        } catch (NullPointerException nullPointerException) {
            // 不明原因出现空指针
        }

    }

    public static String fullyURLEncode(String input) throws UnsupportedEncodingException {
        StringBuilder encodedString = new StringBuilder();

        // Iterate over each character in the input string
        for (char ch : input.toCharArray()) {
            // Encode each character to its %XX format
            encodedString.append(String.format("%%%02X", (int) ch));
        }

        return encodedString.toString();
    }

    public synchronized static void setOriginRequestId(OriginRequestItem item) {
        item.setId(Data.ORIGIN_REQUEST_TABLE_DATA.size());
    }

    // 刷新domain表格
    public static synchronized void flushConfigTable(String type, JTable table) {
        if (type.equals("domain")) {
            ArrayList<String> targetList = Data.DOMAIN_LIST;
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);
            for (String item : targetList) {
                model.addRow(new Object[]{item});
            }
        } else if (type.equals("payload")) {
            ArrayList<String> targetList = Data.PAYLOAD_LIST;
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);
            for (String item : targetList) {
                model.addRow(new Object[]{item});
            }
        } else if (type.equals("header")) {
            LinkedHashMap<String, String> headerList = Data.HEADER_MAP;
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);
            for (Map.Entry<String, String> entry : headerList.entrySet()) {
                model.addRow(new Object[]{entry.getKey(), entry.getValue()});
            }
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


        if (type.equals("domain")) {
            ArrayList targetList = Data.DOMAIN_LIST;
            String[] lines = userInput.split("\n");
            for (String line : lines) {
                line = line.trim();
                if (!targetList.contains(line) && line != null && line.length() != 0) {
                    targetList.add(line);
                }
            }
        } else if (type.equals("payload")) {
            ArrayList targetList = Data.PAYLOAD_LIST;
            String[] lines = userInput.split("\n");
            for (String line : lines) {
                line = line.trim();
                if (!targetList.contains(line) && line != null && line.length() != 0) {
                    targetList.add(line);
                }
            }
        } else if (type.equals("header")) {
            LinkedHashMap<String, String> headerMap = Data.HEADER_MAP;
            String[] lines = userInput.split("\n");  // 获取每行请求头key-value
            for (String line : lines) {
                int index = line.indexOf(":"); // 找到第一个 : 的索引
                if (index != -1) {  // 确保是合法的header的key-value
                    String header = line.substring(0, index);  // 左边部分
                    String value = line.substring(index + 1); // 右边部分（去掉冒号）
                    if (!headerMap.containsKey(header) && header != null && value != null) {
                        if (header.trim().length() != 0 && value.trim().length() != 0) {
                            headerMap.put(header.trim(), value.trim());
                        }
                    }
                }
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


        if (type.equals("domain")) {
            ArrayList targetList = Data.DOMAIN_LIST;
            if (!targetList.contains(userInput)) {
                targetList.set(row, userInput);
            }
        } else if (type.equals("payload")) {
            ArrayList targetList = Data.PAYLOAD_LIST;
            if (!targetList.contains(userInput)) {
                targetList.set(row, userInput);
            }
        } else if (type.equals("header")) {
            LinkedHashMap<String, String> headerMap = Data.HEADER_MAP;

            int index = userInput.indexOf(":");
            if (index != -1) {  // 确保是合法的header的key-value
                String header = userInput.substring(0, index);  // 左边部分
                String value = userInput.substring(index + 1); // 右边部分（去掉冒号）
                if (headerMap.containsKey(header) && header != null && value != null) {
                    if (header.trim().length() != 0 && value.trim().length() != 0) {
                        headerMap.put(header.trim(), value.trim());
                    }
                }
            }
        }
    }

    // 用户删除配置
    public static void removeConfigData(String type, int[] rows) {
        if (type.equals("domain")) {
            ArrayList targetList = Data.DOMAIN_LIST;
            // 如果有选中的行
            if (rows.length > 0) {
                // 倒序删除 避免索引影响
                for (int i = rows.length - 1; i >= 0; i--) {
                    int row = rows[i];
                    // 删除选中的行
                    targetList.remove(row);
                }
            }
        } else if (type.equals("payload")) {
            ArrayList targetList = Data.PAYLOAD_LIST;
            // 如果有选中的行
            if (rows.length > 0) {
                // 倒序删除 避免索引影响
                for (int i = rows.length - 1; i >= 0; i--) {
                    int row = rows[i];
                    // 删除选中的行
                    targetList.remove(row);
                }
            }
        } else if (type.equals("header")) {
            LinkedHashMap<String, String> headerMap = Data.HEADER_MAP;
            // 如果有选中的行
            if (rows.length > 0) {
                // 倒序删除
                for (int i = rows.length - 1; i >= 0; i--) {
                    int index = 0;
                    for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                        if (index == rows[i]) {
                            headerMap.remove(entry.getKey());
                            break;
                        }
                        index++;
                    }
                }
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
                encodedString.append("%").append(String.format("%02X", (int) c)); // 其他字符进行URL编码
            }
        }

        return encodedString.toString();
    }


    public static Object isNumber(String value) {
        // 尝试将字符串转化为整数
        try {
            return Integer.parseInt(value);  // 如果能转化为整数，返回整数
        } catch (NumberFormatException e1) {
            // 如果转换为整数失败，尝试将其转化为 BigDecimal
            try {
                return new BigDecimal(value);  // 如果能转化为 BigDecimal，返回 BigDecimal
            } catch (NumberFormatException e2) {
                // 如果无法转换为整数或 BigDecimal，返回原始的字符串
                return value;
            }
        }
    }

    public static Object isBoolean(String value) {
        if (value.equals("true")) {
            return true;
        } else if (value.equals("false")) {
            return false;
        } else {
            return value;
        }
    }
}
