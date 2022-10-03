/*     */ package shells.plugins.generic.gui;
/*     */ 
/*     */ import com.intellij.uiDesigner.core.GridConstraints;
/*     */ import com.intellij.uiDesigner.core.GridLayoutManager;
/*     */ import core.EasyI18N;
/*     */ import core.ui.component.DataView;
/*     */ import java.awt.CardLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTextField;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SocksProxyManagePanel
/*     */ {
/*     */   private JPanel mainPanel;
/*     */   public JPanel startSocksProxyPanel;
/*     */   public JPanel socksProxyManagePanel;
/*     */   public JButton testButton;
/*     */   public JTextField socksBindAddressTextField;
/*     */   public JLabel socksBindAddressLabel;
/*     */   public JLabel socksBindPortLabel;
/*     */   public JTextField socksBindPortTextField;
/*     */   public JLabel remoteSocksProxyUrlLabel;
/*     */   public JTextField remoteSocksProxyUrlTextField;
/*     */   public JTextField remoteKeyTextField;
/*     */   public JTextField serverPacketSizeTextField;
/*     */   public JTextField serverSocketOnceReadSizeTextField;
/*     */   public JTextField clientSocketOnceReadSizeTextField;
/*     */   public JTextField clientPacketTextField;
/*     */   public JButton startSocksServerButton;
/*     */   public JButton addNewProxyButton;
/*     */   public JButton stopProxyButton;
/*     */   
/*     */   public SocksProxyManagePanel() {
/*  74 */     $$$setupUI$$$();
/*     */     this.proxyManageDataView.addColumn("ID");
/*     */     this.proxyManageDataView.addColumn(EasyI18N.getI18nString("监听地址"));
/*     */     this.proxyManageDataView.addColumn(EasyI18N.getI18nString("监听端口"));
/*     */     this.proxyManageDataView.addColumn(EasyI18N.getI18nString("类型"));
/*     */     this.proxyManageDataView.addColumn(EasyI18N.getI18nString("目标地址"));
/*     */     this.proxyManageDataView.addColumn(EasyI18N.getI18nString("目标端口"));
/*     */     this.proxyManageDataView.addColumn(EasyI18N.getI18nString("状态"));
/*     */     this.proxyManageDataView.addColumn(EasyI18N.getI18nString("错误信息"));
/*     */   } public JButton serverProxyConfigButton; public JTextField requestDelayTextField; public JTextField requestErrRetryTextField; public JTextField requestErrDelayTextField; public JLabel remoteKeyLabel; public JLabel serverSocketOnceReadSizeLabel; public JLabel serverPacketSizeLabel; public JLabel clientSocketOnceReadSizeLabel; public JLabel clientPacketSizeLabel; public JLabel requestDelayLabel; public JLabel requestErrRetryLabel; public JLabel requestErrDelayLabel; public JLabel statusLabel; public JLabel capacityLabel; public JTextField capacityTextField; public JPanel stopSocksProxy; public JScrollPane dataViewScrollPane; public DataView proxyManageDataView; public JButton addProxyType;
/*     */   private void $$$setupUI$$$() {
/*  85 */     this.mainPanel = new JPanel();
/*  86 */     this.mainPanel.setLayout(new CardLayout(0, 0));
/*  87 */     this.startSocksProxyPanel = new JPanel();
/*  88 */     this.startSocksProxyPanel.setLayout((LayoutManager)new GridLayoutManager(14, 2, new Insets(0, 0, 0, 0), -1, -1));
/*  89 */     this.mainPanel.add(this.startSocksProxyPanel, "startSocksProxy");
/*  90 */     this.socksBindAddressLabel = new JLabel();
/*  91 */     this.socksBindAddressLabel.setText("socks4a/5 绑定地址:");
/*  92 */     this.startSocksProxyPanel.add(this.socksBindAddressLabel, new GridConstraints(0, 0, 1, 1, 1, 0, 0, 0, null, null, null, 0, false));
/*  93 */     this.socksBindAddressTextField = new JTextField();
/*  94 */     this.socksBindAddressTextField.setText("127.0.0.1");
/*  95 */     this.startSocksProxyPanel.add(this.socksBindAddressTextField, new GridConstraints(0, 1, 1, 1, 1, 1, 4, 0, null, new Dimension(150, -1), null, 0, false));
/*  96 */     this.socksBindPortLabel = new JLabel();
/*  97 */     this.socksBindPortLabel.setText("socks4a/5 绑定端口:");
/*  98 */     this.startSocksProxyPanel.add(this.socksBindPortLabel, new GridConstraints(1, 0, 1, 1, 1, 0, 0, 0, null, null, null, 0, false));
/*  99 */     this.socksBindPortTextField = new JTextField();
/* 100 */     this.socksBindPortTextField.setText("10806");
/* 101 */     this.startSocksProxyPanel.add(this.socksBindPortTextField, new GridConstraints(1, 1, 1, 1, 9, 1, 4, 0, null, new Dimension(150, -1), null, 0, false));
/* 102 */     this.remoteSocksProxyUrlLabel = new JLabel();
/* 103 */     this.remoteSocksProxyUrlLabel.setText("远程Socks URL地址:");
/* 104 */     this.startSocksProxyPanel.add(this.remoteSocksProxyUrlLabel, new GridConstraints(2, 0, 1, 1, 1, 0, 0, 0, null, null, null, 0, false));
/* 105 */     this.remoteKeyLabel = new JLabel();
/* 106 */     this.remoteKeyLabel.setText("远程Socks 加密Key:");
/* 107 */     this.startSocksProxyPanel.add(this.remoteKeyLabel, new GridConstraints(3, 0, 1, 1, 1, 0, 0, 0, null, null, null, 0, false));
/* 108 */     this.remoteKeyTextField = new JTextField();
/* 109 */     this.remoteKeyTextField.setText("remoteKey");
/* 110 */     this.startSocksProxyPanel.add(this.remoteKeyTextField, new GridConstraints(3, 1, 1, 1, 9, 1, 4, 0, null, new Dimension(150, -1), null, 0, false));
/* 111 */     this.serverPacketSizeLabel = new JLabel();
/* 112 */     this.serverPacketSizeLabel.setText("Server单次读取大小:");
/* 113 */     this.startSocksProxyPanel.add(this.serverPacketSizeLabel, new GridConstraints(5, 0, 1, 1, 1, 0, 0, 0, null, null, null, 0, false));
/* 114 */     this.serverPacketSizeTextField = new JTextField();
/* 115 */     this.serverPacketSizeTextField.setText("1024000");
/* 116 */     this.startSocksProxyPanel.add(this.serverPacketSizeTextField, new GridConstraints(5, 1, 1, 1, 9, 1, 4, 0, null, new Dimension(150, -1), null, 0, false));
/* 117 */     this.serverSocketOnceReadSizeLabel = new JLabel();
/* 118 */     this.serverSocketOnceReadSizeLabel.setText("Server套接字单次读取大小:");
/* 119 */     this.startSocksProxyPanel.add(this.serverSocketOnceReadSizeLabel, new GridConstraints(4, 0, 1, 1, 0, 0, 0, 0, null, null, null, 0, false));
/* 120 */     this.serverSocketOnceReadSizeTextField = new JTextField();
/* 121 */     this.serverSocketOnceReadSizeTextField.setText("102400");
/* 122 */     this.startSocksProxyPanel.add(this.serverSocketOnceReadSizeTextField, new GridConstraints(4, 1, 1, 1, 8, 1, 4, 0, null, new Dimension(150, -1), null, 0, false));
/* 123 */     this.clientSocketOnceReadSizeTextField = new JTextField();
/* 124 */     this.clientSocketOnceReadSizeTextField.setText("102400");
/* 125 */     this.startSocksProxyPanel.add(this.clientSocketOnceReadSizeTextField, new GridConstraints(6, 1, 1, 1, 8, 1, 4, 0, null, new Dimension(150, -1), null, 0, false));
/* 126 */     this.clientPacketSizeLabel = new JLabel();
/* 127 */     this.clientPacketSizeLabel.setText("Client单次读取大小:");
/* 128 */     this.startSocksProxyPanel.add(this.clientPacketSizeLabel, new GridConstraints(7, 0, 1, 1, 0, 0, 0, 0, null, null, null, 0, false));
/* 129 */     this.clientPacketTextField = new JTextField();
/* 130 */     this.clientPacketTextField.setText("1024000");
/* 131 */     this.startSocksProxyPanel.add(this.clientPacketTextField, new GridConstraints(7, 1, 1, 1, 8, 1, 4, 0, null, new Dimension(150, -1), null, 0, false));
/* 132 */     JPanel panel1 = new JPanel();
/* 133 */     panel1.setLayout((LayoutManager)new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
/* 134 */     this.startSocksProxyPanel.add(panel1, new GridConstraints(12, 0, 1, 2, 0, 3, 3, 3, null, null, null, 0, false));
/* 135 */     this.testButton = new JButton();
/* 136 */     this.testButton.setText("测试连接");
/* 137 */     panel1.add(this.testButton, new GridConstraints(0, 0, 1, 1, 0, 1, 3, 0, null, null, null, 0, false));
/* 138 */     this.startSocksServerButton = new JButton();
/* 139 */     this.startSocksServerButton.setText("开启SocksServer");
/* 140 */     panel1.add(this.startSocksServerButton, new GridConstraints(0, 1, 1, 1, 0, 1, 3, 0, null, null, null, 0, false));
/* 141 */     JPanel panel2 = new JPanel();
/* 142 */     panel2.setLayout((LayoutManager)new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
/* 143 */     this.startSocksProxyPanel.add(panel2, new GridConstraints(13, 0, 1, 2, 0, 3, 3, 3, null, null, null, 0, false));
/* 144 */     this.requestDelayLabel = new JLabel();
/* 145 */     this.requestDelayLabel.setText("请求抖动延迟(ms)");
/* 146 */     this.startSocksProxyPanel.add(this.requestDelayLabel, new GridConstraints(9, 0, 1, 1, 0, 0, 0, 0, null, null, null, 0, false));
/* 147 */     this.requestDelayTextField = new JTextField();
/* 148 */     this.requestDelayTextField.setText("10");
/* 149 */     this.startSocksProxyPanel.add(this.requestDelayTextField, new GridConstraints(9, 1, 1, 1, 8, 1, 4, 0, null, new Dimension(150, -1), null, 0, false));
/* 150 */     this.requestErrRetryLabel = new JLabel();
/* 151 */     this.requestErrRetryLabel.setText("错误重试最大次数");
/* 152 */     this.startSocksProxyPanel.add(this.requestErrRetryLabel, new GridConstraints(10, 0, 1, 1, 0, 0, 0, 0, null, null, null, 0, false));
/* 153 */     this.requestErrRetryTextField = new JTextField();
/* 154 */     this.requestErrRetryTextField.setText("20");
/* 155 */     this.startSocksProxyPanel.add(this.requestErrRetryTextField, new GridConstraints(10, 1, 1, 1, 8, 1, 4, 0, null, new Dimension(150, -1), null, 0, false));
/* 156 */     this.requestErrDelayLabel = new JLabel();
/* 157 */     this.requestErrDelayLabel.setText("请求错误重试抖动延时(ms)");
/* 158 */     this.startSocksProxyPanel.add(this.requestErrDelayLabel, new GridConstraints(11, 0, 1, 1, 0, 0, 0, 0, null, null, null, 1, false));
/* 159 */     this.requestErrDelayTextField = new JTextField();
/* 160 */     this.requestErrDelayTextField.setText("30");
/* 161 */     this.startSocksProxyPanel.add(this.requestErrDelayTextField, new GridConstraints(11, 1, 1, 1, 8, 1, 4, 0, null, new Dimension(150, -1), null, 0, false));
/* 162 */     this.capacityLabel = new JLabel();
/* 163 */     this.capacityLabel.setText("套接字缓冲队列数");
/* 164 */     this.startSocksProxyPanel.add(this.capacityLabel, new GridConstraints(8, 0, 1, 1, 0, 0, 0, 0, null, null, null, 0, false));
/* 165 */     this.capacityTextField = new JTextField();
/* 166 */     this.capacityTextField.setText("5");
/* 167 */     this.startSocksProxyPanel.add(this.capacityTextField, new GridConstraints(8, 1, 1, 1, 8, 1, 4, 0, null, new Dimension(150, -1), null, 0, false));
/* 168 */     this.remoteSocksProxyUrlTextField = new JTextField();
/* 169 */     this.remoteSocksProxyUrlTextField.setText("http://127.0.0.1:8088/");
/* 170 */     this.startSocksProxyPanel.add(this.remoteSocksProxyUrlTextField, new GridConstraints(2, 1, 1, 1, 9, 1, 4, 0, null, new Dimension(150, -1), null, 0, false));
/* 171 */     this.clientSocketOnceReadSizeLabel = new JLabel();
/* 172 */     this.clientSocketOnceReadSizeLabel.setText("Client套接字单次读取大小:");
/* 173 */     this.startSocksProxyPanel.add(this.clientSocketOnceReadSizeLabel, new GridConstraints(6, 0, 1, 1, 0, 0, 0, 0, null, null, null, 0, false));
/* 174 */     this.socksProxyManagePanel = new JPanel();
/* 175 */     this.socksProxyManagePanel.setLayout((LayoutManager)new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
/* 176 */     this.mainPanel.add(this.socksProxyManagePanel, "socksProxyManage");
/* 177 */     this.statusLabel = new JLabel();
/* 178 */     this.statusLabel.setText("当前连接数:10 当前速度:100k/s 已发包:1000 已上传:10mb 已下载:20mb  运行时间:1h");
/* 179 */     this.socksProxyManagePanel.add(this.statusLabel, new GridConstraints(0, 0, 1, 1, 8, 0, 0, 0, null, null, null, 0, false));
/* 180 */     this.stopSocksProxy = new JPanel();
/* 181 */     this.stopSocksProxy.setLayout((LayoutManager)new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
/* 182 */     this.socksProxyManagePanel.add(this.stopSocksProxy, new GridConstraints(3, 0, 1, 1, 0, 3, 3, 3, null, null, null, 0, false));
/* 183 */     this.addNewProxyButton = new JButton();
/* 184 */     this.addNewProxyButton.setText("添加代理类型");
/* 185 */     this.stopSocksProxy.add(this.addNewProxyButton, new GridConstraints(0, 0, 1, 1, 0, 1, 3, 0, null, null, null, 0, false));
/* 186 */     this.stopProxyButton = new JButton();
/* 187 */     this.stopProxyButton.setText("停止代理");
/* 188 */     this.stopSocksProxy.add(this.stopProxyButton, new GridConstraints(0, 2, 1, 1, 0, 1, 3, 0, null, null, null, 0, false));
/* 189 */     this.serverProxyConfigButton = new JButton();
/* 190 */     this.serverProxyConfigButton.setText("通信配置");
/* 191 */     this.stopSocksProxy.add(this.serverProxyConfigButton, new GridConstraints(0, 1, 1, 1, 0, 1, 3, 0, null, null, null, 0, false));
/* 192 */     JPanel panel3 = new JPanel();
/* 193 */     panel3.setLayout((LayoutManager)new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
/* 194 */     this.socksProxyManagePanel.add(panel3, new GridConstraints(1, 0, 1, 1, 0, 3, 3, 3, null, null, null, 0, false));
/* 195 */     this.dataViewScrollPane = new JScrollPane();
/* 196 */     panel3.add(this.dataViewScrollPane, new GridConstraints(0, 0, 1, 1, 0, 3, 5, 5, null, null, null, 0, false));
/* 197 */     this.proxyManageDataView = new DataView();
/* 198 */     this.proxyManageDataView.setAutoCreateRowSorter(true);
/* 199 */     this.proxyManageDataView.setFillsViewportHeight(true);
/* 200 */     this.dataViewScrollPane.setViewportView((Component)this.proxyManageDataView);
/*     */   }
/*     */   public JPanel getMainPanel() {
/*     */     return this.mainPanel;
/*     */   }
/*     */   
/*     */   public JComponent $$$getRootComponent$$$() {
/* 207 */     return this.mainPanel;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\generic\gui\SocksProxyManagePanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */