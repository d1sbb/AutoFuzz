package com.chave.ui;

import burp.api.montoya.ui.editor.EditorOptions;
import burp.api.montoya.ui.editor.HttpRequestEditor;
import burp.api.montoya.ui.editor.HttpResponseEditor;
import com.chave.Main;
import com.chave.config.UserConfig;
import com.chave.pojo.Data;
import com.chave.pojo.FuzzRequestItem;
import com.chave.pojo.OriginRequestItem;
import com.chave.pojo.SearchScope;
import com.chave.utils.Util;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@lombok.Data
public class MainUI {
    // 创建组件
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel rightTopPanel;
    private JPanel rightBottomPanel;
    private JPanel turnOnPanel;
    private JPanel domainOperatePanel;
    private JPanel domainMainPanel;
    private JPanel listenProxyPanel;
    private JPanel listenRepeterPanel;
    private JPanel basicTitlePanel;
    private JPanel domainTitlePanel;
    private JPanel payloadTitlePanel;
    private JPanel cleanRequestListPanel;
    private JPanel payloadMainPanel;
    private JPanel payloadOperatePanel;
    private JPanel searchPanel;
    private JPanel tablePanel;
    private JSplitPane mainSplitPane;
    private JSplitPane rightSplitPane;
    private JTable fuzzRequestItemTable;
    private JTable originRequestItemTable;
    private JTable domainTable;
    private JTable payloadTable;
    private JButton addDomainButton;
    private JButton removeDomainButton;
    private JButton cleanFuzzRequestItemButton;
    private JButton editDomainButton;
    private JButton addPayloadButton;
    private JButton editPayloadButton;
    private JButton removePayloadButton;
    private JButton searchButton;
    private JButton cleanSearchResultButton;
    private JCheckBox turnOnCheckBox;
    private JCheckBox listenProxyCheckBox;
    private JCheckBox listenRepeterCheckBox;
    private JCheckBox includeSubDomainCheckBox;
    private JCheckBox emptyParamCheckBox;
    private JCheckBox paramURLEncodeCheckBox;
    private JLabel basicTitleLabel;
    private JLabel domainTitleLabel;
    private JLabel payloadTitleLabel;
    private JTextField searchTextField;
    private JComboBox<String> searchScopeComboBox;
    private HttpRequestEditor requestEditor;
    private HttpResponseEditor responseEditor;
    private HashMap<Integer, ArrayList<Integer>> highlightMap;


    public MainUI() {
        init();
    }

    private void init() {
        // 初始化各个组件
        leftPanel = new JPanel();
        rightPanel = new JPanel();
        rightTopPanel = new JPanel();
        rightBottomPanel = new JPanel();
        turnOnPanel = new JPanel();
        domainOperatePanel = new JPanel();
        domainMainPanel = new JPanel();
        listenProxyPanel = new JPanel();
        listenRepeterPanel = new JPanel();
        cleanRequestListPanel = new JPanel();
        basicTitlePanel = new JPanel();
        domainTitlePanel = new JPanel();
        payloadTitlePanel = new JPanel();
        payloadMainPanel = new JPanel();
        payloadOperatePanel = new JPanel();
        searchPanel = new JPanel();
        tablePanel = new JPanel();
        turnOnCheckBox = new JCheckBox("启用插件");
        listenProxyCheckBox = new JCheckBox("监听Proxy");
        listenRepeterCheckBox = new JCheckBox("监听Repeter");
        emptyParamCheckBox = new JCheckBox("参数置空");
        paramURLEncodeCheckBox = new JCheckBox("URL编码");
        addDomainButton = new JButton("添加");
        editDomainButton = new JButton("编辑");
        removeDomainButton = new JButton("删除");
        cleanFuzzRequestItemButton = new JButton("清空请求记录");
        addPayloadButton = new JButton("添加");
        editPayloadButton = new JButton("编辑");
        removePayloadButton = new JButton("删除");
        searchButton = new JButton("查找");
        cleanSearchResultButton = new JButton("清空查找结果");
        includeSubDomainCheckBox = new JCheckBox("包含子域名");
        searchTextField = new JTextField();
        basicTitleLabel = new JLabel("-----------------------------基本功能-----------------------------");
        domainTitleLabel = new JLabel("-----------------------------域名设置-----------------------------");
        payloadTitleLabel = new JLabel("---------------------------Payload设置---------------------------");
        requestEditor = Main.API.userInterface().createHttpRequestEditor(EditorOptions.READ_ONLY);
        responseEditor = Main.API.userInterface().createHttpResponseEditor(EditorOptions.READ_ONLY);
        highlightMap = new HashMap<>();  // 这里防止空指针 后面还会重新初始化

        // 创建左边表格
        String[] originRequestItemTableColumnName = {"Host", "Path", "返回包长度", "响应码"};
        DefaultTableModel originRequestItemTableModel = new DefaultTableModel(originRequestItemTableColumnName, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };
        originRequestItemTable = new JTable(originRequestItemTableModel);
        // 设置表头背景
        JTableHeader originRequestItemTableHeader = originRequestItemTable.getTableHeader();
        originRequestItemTableHeader.setBackground(new Color(230, 230, 230));
        // 禁止整个表格编辑
        originRequestItemTable.setDefaultEditor(Object.class, null);
        // 设置0,2,3列数据居中对齐
        TableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        ((DefaultTableCellRenderer) centerRenderer).setHorizontalAlignment(SwingConstants.CENTER);
        originRequestItemTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        originRequestItemTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        // 创建表格滚动面板
        JScrollPane originRequestItemTableScrollPane = new JScrollPane(originRequestItemTable);


        // 创建右边表格
        String[] fuzzRequestItemTableColumnName = {"Param", "Payload", "返回包变化", "响应码"};
        DefaultTableModel fuzzRequestItemTableModel = new DefaultTableModel(fuzzRequestItemTableColumnName, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };
        fuzzRequestItemTable = new JTable(fuzzRequestItemTableModel);
        // 设置表头背景
        JTableHeader fuzzRequestItemTableHeader = fuzzRequestItemTable.getTableHeader();
        fuzzRequestItemTableHeader.setBackground(new Color(230, 230, 230));
        // 禁止整个表格编辑
        fuzzRequestItemTable.setDefaultEditor(Object.class, null);
        // 设置0,1,2,3列数据居中对齐
        fuzzRequestItemTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        fuzzRequestItemTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        fuzzRequestItemTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        fuzzRequestItemTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        // 创建表格滚动面板
        JScrollPane fuzzRequestItemTableScrollPane = new JScrollPane(fuzzRequestItemTable);


        // 绘制左侧面板(用户配置面板)
        // 设置左边主面板布局
        BoxLayout leftBoxLayout = new BoxLayout(leftPanel, BoxLayout.Y_AXIS);
        leftPanel.setLayout(leftBoxLayout);
        // 设置三个标题label
        BoxLayout basicTitleLayout = new BoxLayout(basicTitlePanel, BoxLayout.X_AXIS);
        BoxLayout domainTitleLayout = new BoxLayout(domainTitlePanel, BoxLayout.X_AXIS);
        BoxLayout payloadTitleLayout = new BoxLayout(payloadTitlePanel, BoxLayout.X_AXIS);
        basicTitlePanel.setLayout(basicTitleLayout);
        domainTitlePanel.setLayout(domainTitleLayout);
        payloadTitlePanel.setLayout(payloadTitleLayout);
        basicTitlePanel.add(basicTitleLabel, Component.CENTER_ALIGNMENT);
        domainTitlePanel.add(domainTitleLabel, Component.CENTER_ALIGNMENT);
        payloadTitlePanel.add(payloadTitleLabel, Component.CENTER_ALIGNMENT);
        // 设置复选框默认勾选状态 居中放置
        BoxLayout turnOnLayout = new BoxLayout(turnOnPanel, BoxLayout.X_AXIS);
        turnOnPanel.setLayout(turnOnLayout);
        turnOnCheckBox.setSelected(true);
        turnOnPanel.add(Box.createHorizontalStrut(100));
        turnOnPanel.add(turnOnCheckBox);
        turnOnPanel.add(Box.createHorizontalGlue());
        turnOnPanel.setMaximumSize(new Dimension(20000, 22));
        // 选择监听proxy 监听repeter 居中放置
        BoxLayout listenProxyLayout = new BoxLayout(listenProxyPanel, BoxLayout.X_AXIS);
        listenProxyPanel.setLayout(listenProxyLayout);
        listenProxyPanel.add(Box.createHorizontalStrut(100));
        listenProxyPanel.add(listenProxyCheckBox);
        listenProxyPanel.add(Box.createHorizontalGlue());
        listenProxyPanel.setMaximumSize(new Dimension(20000, 22));
        BoxLayout listenRepeterLayout = new BoxLayout(listenRepeterPanel, BoxLayout.X_AXIS);
        listenRepeterPanel.setLayout(listenRepeterLayout);
        listenRepeterPanel.add(Box.createHorizontalStrut(100));
        listenRepeterPanel.add(listenRepeterCheckBox);
        listenRepeterPanel.add(Box.createHorizontalGlue());
        listenRepeterPanel.setMaximumSize(new Dimension(20000, 22));
        // 清空请求列表
        BoxLayout cleanRequestListLayout = new BoxLayout(cleanRequestListPanel, BoxLayout.X_AXIS);
        cleanRequestListPanel.setLayout(cleanRequestListLayout);
        cleanRequestListPanel.add(Box.createHorizontalStrut(100));
        cleanRequestListPanel.add(cleanFuzzRequestItemButton);
        cleanRequestListPanel.add(Box.createHorizontalGlue());
        cleanRequestListPanel.setMaximumSize(new Dimension(20000, 22));
        // 域名配置部分绘制
        // 用户操作部分
        BoxLayout domainMainLayout = new BoxLayout(domainMainPanel, BoxLayout.X_AXIS);
        BoxLayout domainOperateLayout = new BoxLayout(domainOperatePanel, BoxLayout.Y_AXIS);
        domainMainPanel.setLayout(domainMainLayout);
        domainOperatePanel.setLayout(domainOperateLayout);
        domainOperatePanel.add(addDomainButton, Component.CENTER_ALIGNMENT);
        domainOperatePanel.add(Box.createVerticalStrut(10));
        domainOperatePanel.add(editDomainButton, Component.CENTER_ALIGNMENT);
        domainOperatePanel.add(Box.createVerticalStrut(10));
        domainOperatePanel.add(removeDomainButton, Component.CENTER_ALIGNMENT);
        domainOperatePanel.add(Box.createVerticalStrut(10));
        domainOperatePanel.add(includeSubDomainCheckBox, Component.CENTER_ALIGNMENT);
        domainMainPanel.add(Box.createHorizontalStrut(5));
        domainMainPanel.add(domainOperatePanel);
        // 初始化域名表格
        String[] domainTableColumnName = {"Domain"};
        DefaultTableModel domainModel = new DefaultTableModel(domainTableColumnName, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };
        domainTable = new JTable(domainModel);
        // 支持多行选中
        domainTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        // 设置表头背景
        JTableHeader domainTableHeader = domainTable.getTableHeader();
        domainTableHeader.setBackground(new Color(230, 230, 230));
        // 禁止表格编辑
//        domainTable.getColumnModel().getColumn(0).setCellEditor(disabledEditor);
        domainTable.setDefaultEditor(Object.class, null);
        // 创建表格滚动面板
        JScrollPane domainTableScrollPane = new JScrollPane(domainTable);
        domainTableScrollPane.setMaximumSize(new Dimension(150, 2000));
        domainMainPanel.add(Box.createHorizontalStrut(5));
        domainMainPanel.add(domainTableScrollPane);
        domainMainPanel.add(Box.createHorizontalStrut(5));
        // payload配置部分绘制
        // 用户操作部分
        BoxLayout payloadMainLayout = new BoxLayout(payloadMainPanel, BoxLayout.X_AXIS);
        BoxLayout payloadOperateLayout = new BoxLayout(payloadOperatePanel, BoxLayout.Y_AXIS);
        payloadMainPanel.setLayout(payloadMainLayout);
        payloadOperatePanel.setLayout(payloadOperateLayout);
        payloadOperatePanel.add(addPayloadButton, Component.CENTER_ALIGNMENT);
        payloadOperatePanel.add(Box.createVerticalStrut(10));
        payloadOperatePanel.add(editPayloadButton, Component.CENTER_ALIGNMENT);
        payloadOperatePanel.add(Box.createVerticalStrut(10));
        payloadOperatePanel.add(removePayloadButton, Component.CENTER_ALIGNMENT);
        payloadOperatePanel.add(Box.createVerticalStrut(10));
        payloadOperatePanel.add(emptyParamCheckBox, Component.CENTER_ALIGNMENT);
        payloadOperatePanel.add(Box.createVerticalStrut(10));
        payloadOperatePanel.add(paramURLEncodeCheckBox, Component.CENTER_ALIGNMENT);
        payloadMainPanel.add(Box.createHorizontalStrut(5));
        payloadMainPanel.add(payloadOperatePanel);
        // 初始化payload表格
        String[] payloadTableColumnName = {"Payload"};
        DefaultTableModel payloadModel = new DefaultTableModel(payloadTableColumnName, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };
        payloadTable = new JTable(payloadModel);
        // 支持多行选中
        payloadTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        // 禁止表格编辑
//        payloadTable.getColumnModel().getColumn(0).setCellEditor(disabledEditor);
        payloadTable.setDefaultEditor(Object.class, null);
        // 设置表头背景
        JTableHeader payloadTableHeader = payloadTable.getTableHeader();
        payloadTableHeader.setBackground(new Color(230, 230, 230));
        // 创建表格滚动面板
        JScrollPane payloadTableScrollPane = new JScrollPane(payloadTable);
        payloadTableScrollPane.setMaximumSize(new Dimension(150, 2000));
        payloadMainPanel.add(Box.createHorizontalStrut(11));
        payloadMainPanel.add(payloadTableScrollPane);
        payloadMainPanel.add(Box.createHorizontalStrut(5));
        // 左侧面板添加各个组件
        leftPanel.add(Box.createVerticalStrut(30));
        leftPanel.add(basicTitlePanel);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(turnOnPanel);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(listenProxyPanel);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(listenRepeterPanel);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(cleanRequestListPanel);
        leftPanel.add(Box.createVerticalStrut(30));
        leftPanel.add(domainTitlePanel);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(domainMainPanel);
        leftPanel.add(Box.createVerticalStrut(30));
        leftPanel.add(payloadTitlePanel);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(payloadMainPanel);
        leftPanel.add(Box.createVerticalStrut(30));

        BoxLayout tableLayout = new BoxLayout(tablePanel, BoxLayout.X_AXIS);
        tablePanel.setLayout(tableLayout);
        tablePanel.add(originRequestItemTableScrollPane);
        tablePanel.add(fuzzRequestItemTableScrollPane);

        // 右上方面板绘制
        BoxLayout rightTopLayout = new BoxLayout(rightTopPanel, BoxLayout.Y_AXIS);
        rightTopPanel.setLayout(rightTopLayout);
        // 搜索框绘制
        String[] searchScopeOptions = {"request", "response"};
        searchScopeComboBox = new JComboBox<>(searchScopeOptions);
        searchScopeComboBox.setSelectedItem("request");
        UserConfig.SEARCH_SCOPE = SearchScope.REQUEST;
        BoxLayout searchPanelLayout = new BoxLayout(searchPanel, BoxLayout.X_AXIS);
        searchPanel.setLayout(searchPanelLayout);
        searchPanel.add(Box.createHorizontalStrut(2));
        searchPanel.add(searchScopeComboBox);
        searchPanel.add(Box.createHorizontalStrut(2));
        searchPanel.add(searchTextField);
        searchPanel.add(Box.createHorizontalStrut(2));
        searchPanel.add(searchButton);
        searchPanel.add(Box.createHorizontalStrut(2));
        searchPanel.add(cleanSearchResultButton);
        searchPanel.add(Box.createHorizontalStrut(5));
        searchPanel.setMaximumSize(new Dimension(20000, 30));
        rightTopPanel.add(tablePanel);
        rightTopPanel.add(Box.createVerticalStrut(3));
        rightTopPanel.add(searchPanel);
        rightTopPanel.add(Box.createVerticalStrut(2));

        // 创建request/response展示面板
        BoxLayout rightBottomLayout = new BoxLayout(rightBottomPanel, BoxLayout.X_AXIS);
        rightBottomPanel.setLayout(rightBottomLayout);
        rightBottomPanel.add(requestEditor.uiComponent());
        rightBottomPanel.add(responseEditor.uiComponent());

        // 创建右侧分隔面板 上下分隔
        rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, rightTopPanel, rightBottomPanel);
        rightSplitPane.setDividerLocation(400);

        // 创建主分隔面板 左右分隔
        mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightSplitPane);
        mainSplitPane.setDividerLocation(300);
        mainSplitPane.setEnabled(false);

        for (int i = 0; i < originRequestItemTable.getColumnCount(); i++) {
            originRequestItemTable.getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    // 调用默认的渲染器来获取单元格组件
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                    if (highlightMap.containsKey(row)) {
                        c.setForeground(Color.RED);
                    } else {
                        c.setForeground(Color.BLACK);
                    }

                    return c;
                }
            });
        }

        for (int i = 0; i < fuzzRequestItemTable.getColumnCount(); i++) {
            fuzzRequestItemTable.getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    // 调用默认的渲染器来获取单元格组件
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                    try {
                        int originRequestSelectedRow = originRequestItemTable.getSelectedRows()[0];

                        if (highlightMap.get(originRequestSelectedRow).contains(row)) {
                            c.setForeground(Color.RED);
                        } else {
                            c.setForeground(Color.BLACK);
                        }
                    } catch (Exception e) {
                        c.setForeground(Color.BLACK);
                    }

                    return c;
                }
            });
        }



        // 设置启用插件复选框监听器
        turnOnCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    UserConfig.TURN_ON = Boolean.TRUE;
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    UserConfig.TURN_ON = Boolean.FALSE;
                }
            }
        });


        // 监听proxy复选框监听器
        listenProxyCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    UserConfig.LISTEN_PROXY = Boolean.TRUE;
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    UserConfig.LISTEN_PROXY = Boolean.FALSE;
                }
            }
        });


        // 监听repeter复选框监听器
        listenRepeterCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    UserConfig.LISTEN_REPETER = Boolean.TRUE;
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    UserConfig.LISTEN_REPETER = Boolean.FALSE;
                }
            }
        });


        // 清空请求记录按钮监听器
        cleanFuzzRequestItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Data.ORIGIN_REQUEST_TABLE_DATA.clear();
                originRequestItemTableModel.setRowCount(0);
                fuzzRequestItemTableModel.setRowCount(0);
            }
        });


        // 添加域名按钮监听器
        addDomainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddDataDialog("domain");
            }
        });

        // 添加payload按钮监听器
        addPayloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddDataDialog("payload");
            }
        });

        // 编辑域名按钮监听器
        editDomainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 如果选择多行 默认编辑选中的第一行
                showEditDataDialog("domain", domainTable.getSelectedRows()[0]);
            }
        });

        // 编辑payload按钮监听器
        editPayloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 如果选中多行 默认编辑选中的第一行
                showEditDataDialog("payload", payloadTable.getSelectedRows()[0]);
            }
        });

        // 删除domain按钮监听器
        removeDomainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Util.removeConfigData("domain", domainTable.getSelectedRows());
                Util.flushConfigTable("domain", domainTable);
            }
        });

        // 删除payload按钮监听器
        removePayloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Util.removeConfigData("payload", payloadTable.getSelectedRows());
                Util.flushConfigTable("payload", payloadTable);
            }
        });

        // 参数置空监听器
        emptyParamCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Data.PAYLOAD_LIST.add(0, "");
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    Data.PAYLOAD_LIST.remove("");
                }

                Util.flushConfigTable("payload", payloadTable);
            }
        });

        paramURLEncodeCheckBox.addItemListener((new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    UserConfig.PARAM_URL_ENCODE = Boolean.TRUE;
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    UserConfig.PARAM_URL_ENCODE = Boolean.FALSE;
                }
            }
        }));

        // 包含子域名复选框监听器
        includeSubDomainCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    UserConfig.INCLUDE_SUBDOMAIN = Boolean.TRUE;
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    UserConfig.INCLUDE_SUBDOMAIN = Boolean.FALSE;
                }
            }
        });

        // 查找作用域下拉框监听器
        searchScopeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 获取用户选择
                String selectedOption = (String) searchScopeComboBox.getSelectedItem();

                if (SearchScope.REQUEST.scopeName().equals(selectedOption)) {
                    UserConfig.SEARCH_SCOPE = SearchScope.REQUEST;
                } else if (SearchScope.RESPONSE.scopeName().equals(selectedOption)) {
                    UserConfig.SEARCH_SCOPE = SearchScope.RESPONSE;
                }
            }
        });


        // 查找按钮监听器
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword = searchTextField.getText().trim();

                if (UserConfig.SEARCH_SCOPE == SearchScope.REQUEST) {
                    searchAllRequestResponse(keyword, SearchScope.REQUEST);
                } else if (UserConfig.SEARCH_SCOPE == SearchScope.RESPONSE) {
                    searchAllRequestResponse(keyword, SearchScope.RESPONSE);
                }
            }
        });

        // 清空查找结果按钮监听器
        cleanSearchResultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                highlightMap.clear();

                originRequestItemTable.repaint();
                fuzzRequestItemTable.repaint();
            }
        });

        // 创建fuzzRequestItem被点击时的监听事件  用于展示request response
        fuzzRequestItemTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 展示原请求响应数据
                int originRequestItemRow = originRequestItemTable.getSelectedRows()[0];
                int fuzzRequestItemRow = fuzzRequestItemTable.rowAtPoint(e.getPoint());
                OriginRequestItem selectedOriginRequestItem = null;
                String hostText = (String) originRequestItemTableModel.getValueAt(originRequestItemRow, 0);
                String pathText = (String) originRequestItemTableModel.getValueAt(originRequestItemRow, 1);
                OriginRequestItem tempItem = new OriginRequestItem(hostText, pathText, null, null);
                for (Map.Entry<Integer, OriginRequestItem> entry : Data.ORIGIN_REQUEST_TABLE_DATA.entrySet()) {
                    OriginRequestItem item = entry.getValue();
                    if (item.equals(tempItem)) {
                        selectedOriginRequestItem = item;
                        break;
                    }
                }

                if (fuzzRequestItemRow >= 0) {
                    FuzzRequestItem fuzzRequestItem = selectedOriginRequestItem.getFuzzRequestArrayList().get(fuzzRequestItemRow);
                    requestEditor.setRequest(fuzzRequestItem.getFuzzRequestResponse().request());
                    responseEditor.setResponse(fuzzRequestItem.getFuzzRequestResponse().response());
                }
            }
        });

        // 创建oritinRequestItem被点击时的监听事件  用于展示request response
        originRequestItemTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 找到点击的条目
                int originRequestItemRow = originRequestItemTable.rowAtPoint(e.getPoint());
                OriginRequestItem clickedItem = null;
                String hostText = (String) originRequestItemTableModel.getValueAt(originRequestItemRow, 0);
                String pathText = (String) originRequestItemTableModel.getValueAt(originRequestItemRow, 1);
                OriginRequestItem tempItem = new OriginRequestItem(hostText, pathText, null, null);
                for (Map.Entry<Integer, OriginRequestItem> entry : Data.ORIGIN_REQUEST_TABLE_DATA.entrySet()) {
                    OriginRequestItem item = entry.getValue();
                    if (item.equals(tempItem)) {
                        clickedItem = item;
                        break;
                    }
                }

                requestEditor.setRequest(clickedItem.getOriginRequest());
                responseEditor.setResponse(clickedItem.getOriginResponse());

                // 刷新fuzzRequestItem列表
                ArrayList<FuzzRequestItem> fuzzRequestItemArrayList = clickedItem.getFuzzRequestArrayList();
                fuzzRequestItemTableModel.setRowCount(0);
                for (FuzzRequestItem fuzzRequestItem : fuzzRequestItemArrayList) {
                    fuzzRequestItemTableModel.addRow(new Object[]{fuzzRequestItem.getParam(), fuzzRequestItem.getPayload(), fuzzRequestItem.getResponseLengthChange(), fuzzRequestItem.getResponseCode()});
                }
                fuzzRequestItemTable.updateUI();
            }
        });
    }

    private void showAddDataDialog(String type) {
        TitledBorder titledBorder = null;
        if (type.equals("domain")) {
            titledBorder = BorderFactory.createTitledBorder("添加域名 每行一个");
        } else if (type.equals("payload")) {
            titledBorder = BorderFactory.createTitledBorder("添加Payload 每行一个");
        }

        JTextArea userInputTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(userInputTextArea);
        userInputTextArea.setBorder(BorderFactory.createCompoundBorder(userInputTextArea.getBorder(), titledBorder));
        scrollPane.setPreferredSize(new Dimension(350, 250));

        int option = 0;
        if (type.equals("domain")) {
            option = JOptionPane.showConfirmDialog(null, scrollPane, "添加域名", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        } else if (type.equals("payload")) {
            option = JOptionPane.showConfirmDialog(null, scrollPane, "添加Payload", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        }


        // 检测用户选择
        if (option == JOptionPane.YES_OPTION) {
            // 用户选择了 "是" 则添加数据，其他情况不做操作
            Util.addConfigData(type, userInputTextArea);

            if (type.equals("domain")) {
                Util.flushConfigTable(type, domainTable);
            } else if (type.equals("payload")) {
                Util.flushConfigTable(type, payloadTable);
            }
        }
    }

    private void showEditDataDialog(String type, int row) {
        JTextField dataTextField = new JTextField();

        if (type.equals("domain")) {
            dataTextField.setText((String) domainTable.getModel().getValueAt(row, 0));
        } else if (type.equals("payload")) {
            dataTextField.setText((String) payloadTable.getModel().getValueAt(row, 0));
        }

        int option = 0;
        if (type.equals("domain")) {
            option = JOptionPane.showConfirmDialog(null, dataTextField, "编辑域名", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        } else if (type.equals("payload")) {
            option = JOptionPane.showConfirmDialog(null, dataTextField, "编辑Payload", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        }


        // 检测用户选择
        if (option == JOptionPane.YES_OPTION) {
            // 用户选择了 "是" 则添加数据，其他情况不做操作
            Util.editConfigData(type, dataTextField, row);

            if (type.equals("domain")) {
                Util.flushConfigTable(type, domainTable);
            } else if (type.equals("payload")) {
                Util.flushConfigTable(type, payloadTable);
            }
        }
    }

    private void searchAllRequestResponse(String keyword, SearchScope searchScope) {
        // 先初始化map
        highlightMap = new HashMap<>();

        // 获取列表中所有origin的行
        TableModel model = originRequestItemTable.getModel();
        int rowCount = model.getRowCount();

        for (int i = 0; i < rowCount; i++) {
            // 由于顺序不一样 根据表中的值先创建一个originRequest
            String host = (String) model.getValueAt(i, 0);
            String path = (String) model.getValueAt(i, 1);
            OriginRequestItem selectOriginRequestItem = new OriginRequestItem(host, path, null, null);

            // 遍历data中的数据找到对应的originRequest 检查内容
            for (Map.Entry<Integer, OriginRequestItem> originRequestItemEntry : Data.ORIGIN_REQUEST_TABLE_DATA.entrySet()) {
                OriginRequestItem originRequestItem = originRequestItemEntry.getValue();
                if (originRequestItem.equals(selectOriginRequestItem)) {
                    String originRequestString = originRequestItem.getOriginRequest().toString();
                    String originResponseString = originRequestItem.getOriginResponse().toString();
                    // 进行不区分大小写的比对originRequest
                    if (originRequestString.toLowerCase().contains(keyword.toLowerCase()) && searchScope.equals(SearchScope.REQUEST)) {
                        highlightMap.put(i, new ArrayList<>());
                    } else if (originResponseString.toLowerCase().contains(keyword.toLowerCase()) && searchScope.equals(SearchScope.RESPONSE)) {
                        highlightMap.put(i, new ArrayList<>());
                    }

                    // 比对fuzzRequest
                    ArrayList<FuzzRequestItem> fuzzRequestArrayList = originRequestItem.getFuzzRequestArrayList();
                    int index = 0;
                    for (FuzzRequestItem fuzzRequestItem : fuzzRequestArrayList) {
                        String fuzzRequestString = fuzzRequestItem.getFuzzRequestResponse().request().toString();
                        String fuzzResponseString = fuzzRequestItem.getFuzzRequestResponse().response().toString();
                        if (fuzzRequestString.toLowerCase().contains(keyword.toLowerCase()) && searchScope.equals(SearchScope.REQUEST)) {
                            if (highlightMap.containsKey(i)) {
                                highlightMap.get(i).add(index);
                            } else {  // 如果originRequest没匹配到 fuzzRequest匹配到了 也加入map
                                ArrayList<Integer> fuzzRequestHighlightList = new ArrayList<>();
                                fuzzRequestHighlightList.add(index);
                                highlightMap.put(i, fuzzRequestHighlightList);
                            }
                        } else if (fuzzResponseString.toLowerCase().contains(keyword.toLowerCase()) && searchScope.equals(SearchScope.RESPONSE)) {
                            if (highlightMap.containsKey(i)) {
                                highlightMap.get(i).add(index);
                            } else {  // 如果originResponse没匹配到 fuzzResponse匹配到了 也加入map
                                ArrayList<Integer> fuzzRequestHighlightList = new ArrayList<>();
                                fuzzRequestHighlightList.add(index);
                                highlightMap.put(i, fuzzRequestHighlightList);
                            }
                        }

                        index++;
                    }
                }
            }

            // 遍历完之后 重新渲染表格
            originRequestItemTable.repaint();
            fuzzRequestItemTable.repaint();
        }
    }


}
