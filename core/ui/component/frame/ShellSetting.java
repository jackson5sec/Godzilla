/*     */ package core.ui.component.frame;
/*     */ 
/*     */ import core.ApplicationContext;
/*     */ import core.Db;
/*     */ import core.EasyI18N;
/*     */ import core.shell.ShellEntity;
/*     */ import core.ui.MainActivity;
/*     */ import core.ui.ShellManage;
/*     */ import core.ui.component.GBC;
/*     */ import core.ui.component.RTextArea;
/*     */ import core.ui.component.dialog.ChooseGroup;
/*     */ import core.ui.component.dialog.GOptionPane;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.lang.reflect.Field;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JSplitPane;
/*     */ import javax.swing.JTabbedPane;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.border.TitledBorder;
/*     */ import util.Log;
/*     */ import util.UiFunction;
/*     */ import util.automaticBindClick;
/*     */ import util.functions;
/*     */ 
/*     */ public class ShellSetting
/*     */   extends JFrame
/*     */ {
/*     */   private final JTabbedPane tabbedPane;
/*     */   private final JPanel basicsPanel;
/*     */   private final JPanel reqPanel;
/*     */   private JTextField urlTextField;
/*     */   private JTextField passwordTextField;
/*     */   private JTextField secretKeyTextField;
/*     */   private JTextField proxyHostTextField;
/*     */   private JTextField proxyPortTextField;
/*     */   private JTextField connTimeOutTextField;
/*     */   private JTextField readTimeOutTextField;
/*  51 */   private final Dimension TextFieldDim = new Dimension(200, 23); private JTextField remarkTextField; private JTextField groupIdTextField; private final RTextArea headersTextArea; private final JButton setButton; private final JButton testButton; private final RTextArea leftTextArea; private final RTextArea rightTextArea; private JComboBox<String> proxyComboBox; private JComboBox<String> cryptionComboBox; private JComboBox<String> payloadComboBox; private JComboBox<String> encodingComboBox;
/*  52 */   private final Dimension labelDim = new Dimension(150, 23);
/*     */   private JLabel urlLabel;
/*     */   private JLabel passwordLabel;
/*     */   private JLabel secretKeyLabel;
/*     */   private JLabel proxyHostLabel;
/*     */   private JLabel proxyPortLabel;
/*     */   private JLabel connTimeOutLabel;
/*     */   private JLabel readTimeOutLabel;
/*     */   private JLabel proxyLabel;
/*     */   private JLabel remarkLabel;
/*     */   private JLabel cryptionLabel;
/*     */   private JLabel payloadLabel;
/*     */   private JLabel encodingLabel;
/*     */   private JLabel groupLabel;
/*     */   private ShellEntity shellContext;
/*     */   private final String shellId;
/*     */   private String error;
/*     */   private String currentGroup;
/*     */   
/*     */   public ShellSetting(String id, String defaultGroup) {
/*  72 */     super("Shell Setting");
/*  73 */     this.shellId = id;
/*     */     
/*  75 */     this.currentGroup = defaultGroup;
/*     */ 
/*     */     
/*  78 */     initLabel();
/*  79 */     initTextField();
/*  80 */     initComboBox();
/*  81 */     Container c = getContentPane();
/*  82 */     this.tabbedPane = new JTabbedPane();
/*     */     
/*  84 */     this.basicsPanel = new JPanel();
/*  85 */     this.reqPanel = new JPanel();
/*     */     
/*  87 */     this.basicsPanel.setLayout(new GridBagLayout());
/*     */     
/*  89 */     GBC gbcLUrl = (new GBC(0, 0)).setInsets(5, -40, 0, 0);
/*  90 */     GBC gbcUrl = (new GBC(1, 0, 3, 1)).setInsets(5, 20, 0, 0);
/*  91 */     GBC gbcLPassword = (new GBC(0, 1)).setInsets(5, -40, 0, 0);
/*  92 */     GBC gbcPassword = (new GBC(1, 1, 3, 1)).setInsets(5, 20, 0, 0);
/*  93 */     GBC gbcLSecretKey = (new GBC(0, 2)).setInsets(5, -40, 0, 0);
/*  94 */     GBC gbcSecretKey = (new GBC(1, 2, 3, 1)).setInsets(5, 20, 0, 0);
/*  95 */     GBC gbcLConnTimeOut = (new GBC(0, 3)).setInsets(5, -40, 0, 0);
/*  96 */     GBC gbcConnTimeOut = (new GBC(1, 3, 3, 1)).setInsets(5, 20, 0, 0);
/*  97 */     GBC gbcLReadTimeOut = (new GBC(0, 4)).setInsets(5, -40, 0, 0);
/*  98 */     GBC gbcReadTimeOut = (new GBC(1, 4, 3, 1)).setInsets(5, 20, 0, 0);
/*  99 */     GBC gbcLProxyHost = (new GBC(0, 5)).setInsets(5, -40, 0, 0);
/* 100 */     GBC gbcProxyHost = (new GBC(1, 5, 3, 1)).setInsets(5, 20, 0, 0);
/* 101 */     GBC gbcLProxyPort = (new GBC(0, 6)).setInsets(5, -40, 0, 0);
/* 102 */     GBC gbcProxyPort = (new GBC(1, 6, 3, 1)).setInsets(5, 20, 0, 0);
/* 103 */     GBC gbcLRemark = (new GBC(0, 7)).setInsets(5, -40, 0, 0);
/* 104 */     GBC gbcRemark = (new GBC(1, 7, 3, 1)).setInsets(5, 20, 0, 0);
/* 105 */     GBC gbcLGroup = (new GBC(0, 8)).setInsets(5, -40, 0, 0);
/* 106 */     GBC gbcGroup = (new GBC(1, 8, 3, 1)).setInsets(5, 20, 0, 0);
/* 107 */     GBC gbcLProxy = (new GBC(0, 9)).setInsets(5, -40, 0, 0);
/* 108 */     GBC gbcProxy = (new GBC(1, 9, 3, 1)).setInsets(5, 20, 0, 0);
/* 109 */     GBC gbcLEncoding = (new GBC(0, 10)).setInsets(5, -40, 0, 0);
/* 110 */     GBC gbcEncoding = (new GBC(1, 10, 3, 1)).setInsets(5, 20, 0, 0);
/* 111 */     GBC gbcLPayload = (new GBC(0, 11)).setInsets(5, -40, 0, 0);
/* 112 */     GBC gbcPayload = (new GBC(1, 11, 3, 1)).setInsets(5, 20, 0, 0);
/* 113 */     GBC gbcLCryption = (new GBC(0, 12)).setInsets(5, -40, 0, 0);
/* 114 */     GBC gbcCryption = (new GBC(1, 12, 3, 1)).setInsets(5, 20, 0, 0);
/* 115 */     GBC gbcSet = (new GBC(0, 13, 3, 1)).setInsets(5, 20, 0, 0);
/* 116 */     GBC gbcTest = (new GBC(1, 13, 3, 1)).setInsets(5, 20, 0, 0);
/*     */ 
/*     */     
/* 119 */     this.setButton = new JButton((this.shellId != null && this.shellId.trim().length() > 0) ? "修改" : "添加");
/*     */     
/* 121 */     this.testButton = new JButton("测试连接");
/*     */ 
/*     */     
/* 124 */     this.basicsPanel.add(this.urlLabel, gbcLUrl);
/* 125 */     this.basicsPanel.add(this.urlTextField, gbcUrl);
/* 126 */     this.basicsPanel.add(this.passwordLabel, gbcLPassword);
/* 127 */     this.basicsPanel.add(this.passwordTextField, gbcPassword);
/* 128 */     this.basicsPanel.add(this.secretKeyLabel, gbcLSecretKey);
/* 129 */     this.basicsPanel.add(this.secretKeyTextField, gbcSecretKey);
/* 130 */     this.basicsPanel.add(this.connTimeOutLabel, gbcLConnTimeOut);
/* 131 */     this.basicsPanel.add(this.connTimeOutTextField, gbcConnTimeOut);
/* 132 */     this.basicsPanel.add(this.readTimeOutLabel, gbcLReadTimeOut);
/* 133 */     this.basicsPanel.add(this.readTimeOutTextField, gbcReadTimeOut);
/* 134 */     this.basicsPanel.add(this.proxyHostLabel, gbcLProxyHost);
/* 135 */     this.basicsPanel.add(this.proxyHostTextField, gbcProxyHost);
/* 136 */     this.basicsPanel.add(this.proxyPortLabel, gbcLProxyPort);
/* 137 */     this.basicsPanel.add(this.proxyPortTextField, gbcProxyPort);
/* 138 */     this.basicsPanel.add(this.remarkLabel, gbcLRemark);
/* 139 */     this.basicsPanel.add(this.remarkTextField, gbcRemark);
/* 140 */     this.basicsPanel.add(this.groupLabel, gbcLGroup);
/* 141 */     this.basicsPanel.add(this.groupIdTextField, gbcGroup);
/* 142 */     this.basicsPanel.add(this.proxyLabel, gbcLProxy);
/* 143 */     this.basicsPanel.add(this.proxyComboBox, gbcProxy);
/* 144 */     this.basicsPanel.add(this.encodingLabel, gbcLEncoding);
/* 145 */     this.basicsPanel.add(this.encodingComboBox, gbcEncoding);
/* 146 */     this.basicsPanel.add(this.payloadLabel, gbcLPayload);
/* 147 */     this.basicsPanel.add(this.payloadComboBox, gbcPayload);
/* 148 */     this.basicsPanel.add(this.cryptionLabel, gbcLCryption);
/* 149 */     this.basicsPanel.add(this.cryptionComboBox, gbcCryption);
/* 150 */     this.basicsPanel.add(this.setButton, gbcSet);
/* 151 */     this.basicsPanel.add(this.testButton, gbcTest);
/*     */     
/* 153 */     this.headersTextArea = new RTextArea();
/* 154 */     this.rightTextArea = new RTextArea();
/* 155 */     this.leftTextArea = new RTextArea();
/* 156 */     this.headersTextArea.setRows(6);
/* 157 */     this.rightTextArea.setRows(3);
/* 158 */     this.leftTextArea.setRows(3);
/*     */     
/* 160 */     this.headersTextArea.setBorder(new TitledBorder("协议头"));
/* 161 */     this.rightTextArea.setBorder(new TitledBorder("右边追加数据"));
/* 162 */     this.leftTextArea.setBorder(new TitledBorder("左边追加数据"));
/*     */     
/* 164 */     JSplitPane reqSplitPane = new JSplitPane();
/* 165 */     JSplitPane lrSplitPane = new JSplitPane();
/*     */     
/* 167 */     lrSplitPane.setOrientation(0);
/* 168 */     reqSplitPane.setOrientation(0);
/*     */     
/* 170 */     lrSplitPane.setTopComponent(new JScrollPane((Component)this.leftTextArea));
/* 171 */     lrSplitPane.setBottomComponent(new JScrollPane((Component)this.rightTextArea));
/*     */     
/* 173 */     reqSplitPane.setTopComponent(new JScrollPane((Component)this.headersTextArea));
/* 174 */     reqSplitPane.setDividerLocation(0.2D);
/* 175 */     reqSplitPane.setBottomComponent(lrSplitPane);
/* 176 */     this.reqPanel.setLayout(new BorderLayout(1, 1));
/* 177 */     this.reqPanel.add(reqSplitPane);
/*     */     
/* 179 */     addToComboBox(this.proxyComboBox, ApplicationContext.getAllProxy());
/* 180 */     addToComboBox(this.encodingComboBox, ApplicationContext.getAllEncodingTypes());
/* 181 */     addToComboBox(this.payloadComboBox, ApplicationContext.getAllPayload());
/*     */ 
/*     */ 
/*     */     
/* 185 */     this.payloadComboBox.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent paramActionEvent)
/*     */           {
/* 189 */             String seleteItemString = (String)ShellSetting.this.payloadComboBox.getSelectedItem();
/* 190 */             ShellSetting.this.cryptionComboBox.removeAllItems();
/* 191 */             ShellSetting.this.addToComboBox(ShellSetting.this.cryptionComboBox, ApplicationContext.getAllCryption(seleteItemString));
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 196 */     this.groupIdTextField.setEditable(false);
/* 197 */     this.groupIdTextField.addMouseListener(new MouseAdapter()
/*     */         {
/*     */           public void mouseClicked(MouseEvent e) {
/* 200 */             String group = (new ChooseGroup(UiFunction.getParentWindow(ShellSetting.this.groupIdTextField), ShellSetting.this.groupIdTextField.getText())).getChooseGroup();
/* 201 */             if (group != null) {
/* 202 */               ShellSetting.this.groupIdTextField.setText(group);
/*     */             } else {
/* 204 */               Log.log("取消选择......", new Object[0]);
/*     */             } 
/*     */           }
/*     */         });
/*     */ 
/*     */ 
/*     */     
/* 211 */     this.tabbedPane.addTab("基础配置", this.basicsPanel);
/* 212 */     this.tabbedPane.addTab("请求配置", this.reqPanel);
/* 213 */     c.add(this.tabbedPane);
/*     */     
/* 215 */     functions.fireActionEventByJComboBox(this.payloadComboBox);
/*     */     
/* 217 */     initShellContent();
/*     */     
/* 219 */     automaticBindClick.bindJButtonClick(this, this);
/*     */ 
/*     */     
/* 222 */     functions.setWindowSize(this, 490, 520);
/* 223 */     setLocationRelativeTo((Component)MainActivity.getFrame());
/* 224 */     setDefaultCloseOperation(2);
/* 225 */     EasyI18N.installObject(this);
/* 226 */     setVisible(true);
/*     */   }
/*     */ 
/*     */   
/*     */   private void initShellContent() {
/* 231 */     if (this.shellId != null && this.shellId.trim().length() > 0) {
/* 232 */       initUpdateShellValue(this.shellId);
/*     */     } else {
/* 234 */       initAddShellValue();
/*     */     } 
/* 236 */     this.groupIdTextField.setText(this.currentGroup);
/*     */   }
/*     */   private void initLabel() {
/* 239 */     Field[] fields = getClass().getDeclaredFields();
/*     */ 
/*     */     
/* 242 */     String endString = "Label".toUpperCase();
/* 243 */     for (int i = 0; i < fields.length; i++) {
/* 244 */       Field field = fields[i];
/* 245 */       if (field.getType().isAssignableFrom(JLabel.class)) {
/* 246 */         String labelString = field.getName().toUpperCase();
/* 247 */         if (labelString.endsWith(endString) && labelString.length() > endString.length()) {
/* 248 */           field.setAccessible(true);
/*     */           try {
/* 250 */             JLabel label = new JLabel(ShellManage.getCNName(labelString.substring(0, labelString.length() - endString.length())));
/* 251 */             label.setPreferredSize(this.labelDim);
/* 252 */             field.set(this, label);
/* 253 */           } catch (Exception e) {
/*     */             
/* 255 */             e.printStackTrace();
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private void initTextField() {
/* 262 */     Field[] fields = getClass().getDeclaredFields();
/*     */ 
/*     */     
/* 265 */     String endString = "TextField".toUpperCase();
/*     */     
/* 267 */     for (int i = 0; i < fields.length; i++) {
/* 268 */       Field field = fields[i];
/* 269 */       if (field.getType().isAssignableFrom(JTextField.class)) {
/* 270 */         String labelString = field.getName().toUpperCase();
/* 271 */         if (labelString.endsWith(endString) && labelString.length() > endString.length()) {
/* 272 */           field.setAccessible(true);
/*     */           try {
/* 274 */             JTextField textField = new JTextField();
/* 275 */             textField.setPreferredSize(this.TextFieldDim);
/* 276 */             field.set(this, textField);
/* 277 */           } catch (Exception e) {
/*     */             
/* 279 */             e.printStackTrace();
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private void initComboBox() {
/* 286 */     Field[] fields = getClass().getDeclaredFields();
/*     */ 
/*     */     
/* 289 */     String endString = "ComboBox".toUpperCase();
/* 290 */     for (int i = 0; i < fields.length; i++) {
/* 291 */       Field field = fields[i];
/* 292 */       if (field.getType().isAssignableFrom(JComboBox.class)) {
/* 293 */         String labelString = field.getName().toUpperCase();
/* 294 */         if (labelString.endsWith(endString) && labelString.length() > endString.length()) {
/* 295 */           field.setAccessible(true);
/*     */           try {
/* 297 */             field.set(this, new JComboBox());
/* 298 */           } catch (Exception e) {
/*     */             
/* 300 */             e.printStackTrace();
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private void addToComboBox(JComboBox<String> comboBox, String[] data) {
/* 307 */     for (int i = 0; i < data.length; i++) {
/* 308 */       comboBox.addItem(data[i]);
/*     */     }
/*     */   }
/*     */   
/*     */   private void initAddShellValue() {
/* 313 */     this.shellContext = new ShellEntity();
/*     */     
/* 315 */     this.urlTextField.setText("http://127.0.0.1/shell.jsp");
/* 316 */     this.passwordTextField.setText("pass");
/* 317 */     this.secretKeyTextField.setText("key");
/* 318 */     this.proxyHostTextField.setText("127.0.0.1");
/* 319 */     this.proxyPortTextField.setText("8888");
/* 320 */     this.connTimeOutTextField.setText("3000");
/* 321 */     this.readTimeOutTextField.setText("60000");
/* 322 */     this.remarkTextField.setText(EasyI18N.getI18nString("备注"));
/* 323 */     this.headersTextArea.setText("User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:84.0) Gecko/20100101 Firefox/84.0\nAccept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\nAccept-Language: zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2\n");
/*     */ 
/*     */     
/* 326 */     this.leftTextArea.setText("");
/* 327 */     this.rightTextArea.setText("");
/* 328 */     if (this.currentGroup == null) {
/* 329 */       this.currentGroup = "/";
/*     */     }
/*     */   }
/*     */   
/*     */   private void initUpdateShellValue(String id) {
/* 334 */     this.shellContext = Db.getOneShell(id);
/* 335 */     this.urlTextField.setText(this.shellContext.getUrl());
/* 336 */     this.passwordTextField.setText(this.shellContext.getPassword());
/* 337 */     this.secretKeyTextField.setText(this.shellContext.getSecretKey());
/* 338 */     this.proxyHostTextField.setText(this.shellContext.getProxyHost());
/* 339 */     this.proxyPortTextField.setText(Integer.toString(this.shellContext.getProxyPort()));
/* 340 */     this.connTimeOutTextField.setText(Integer.toString(this.shellContext.getConnTimeout()));
/* 341 */     this.readTimeOutTextField.setText(Integer.toString(this.shellContext.getReadTimeout()));
/* 342 */     this.remarkTextField.setText(this.shellContext.getRemark());
/* 343 */     this.headersTextArea.setText(this.shellContext.getHeaderS());
/* 344 */     this.leftTextArea.setText(this.shellContext.getReqLeft());
/* 345 */     this.rightTextArea.setText(this.shellContext.getReqRight());
/* 346 */     this.proxyComboBox.setSelectedItem(this.shellContext.getProxyType());
/* 347 */     this.encodingComboBox.setSelectedItem(this.shellContext.getEncoding());
/* 348 */     this.payloadComboBox.setSelectedItem(this.shellContext.getPayload());
/* 349 */     this.cryptionComboBox.setSelectedItem(this.shellContext.getCryption());
/* 350 */     if (this.shellId != null && this.currentGroup == null)
/* 351 */       this.currentGroup = this.shellContext.getGroup(); 
/*     */   }
/*     */   
/*     */   private void testButtonClick(ActionEvent actionEvent) {
/* 355 */     if (updateTempShellEntity()) {
/* 356 */       if (this.shellContext.initShellOpertion()) {
/* 357 */         GOptionPane.showMessageDialog(this, "Success!", "提示", 1);
/* 358 */         Log.log(String.format("CloseShellState: %s\tShellId: %s\tShellHash: %s", new Object[] { Boolean.valueOf(this.shellContext.getPayloadModule().close()), this.shellContext.getId(), Integer.valueOf(this.shellContext.hashCode()) }), new Object[0]);
/*     */       } else {
/* 360 */         GOptionPane.showMessageDialog(this, "initShellOpertion Fail", "提示", 2);
/*     */       } 
/*     */     } else {
/* 363 */       GOptionPane.showMessageDialog(this, this.error, "提示", 2);
/* 364 */       this.error = null;
/*     */     } 
/*     */   }
/*     */   private void setButtonClick(ActionEvent actionEvent) {
/* 368 */     this.currentGroup = this.groupIdTextField.getText().trim();
/* 369 */     if (updateTempShellEntity()) {
/* 370 */       if (this.shellId != null && this.shellId.trim().length() > 0) {
/* 371 */         if (Db.updateShell(this.shellContext) > 0) {
/* 372 */           this.shellContext.setGroup(this.currentGroup);
/* 373 */           GOptionPane.showMessageDialog(this, "修改成功", "提示", 1);
/* 374 */           dispose();
/*     */         } else {
/* 376 */           GOptionPane.showMessageDialog(this, "修改失败", "提示", 2);
/*     */         }
/*     */       
/*     */       }
/* 380 */       else if (Db.addShell(this.shellContext) > 0) {
/* 381 */         this.shellContext.setGroup(this.currentGroup);
/* 382 */         GOptionPane.showMessageDialog(this, "添加成功", "提示", 1);
/* 383 */         dispose();
/*     */       } else {
/* 385 */         GOptionPane.showMessageDialog(this, "添加失败", "提示", 2);
/*     */       } 
/*     */     } else {
/*     */       
/* 389 */       GOptionPane.showMessageDialog(this, this.error, "提示", 2);
/* 390 */       this.error = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean updateTempShellEntity() {
/* 396 */     String url = this.urlTextField.getText();
/* 397 */     String password = this.passwordTextField.getText();
/* 398 */     String secretKey = this.secretKeyTextField.getText();
/* 399 */     String payload = (String)this.payloadComboBox.getSelectedItem();
/* 400 */     String cryption = (String)this.cryptionComboBox.getSelectedItem();
/* 401 */     String encoding = (String)this.encodingComboBox.getSelectedItem();
/* 402 */     String headers = this.headersTextArea.getText();
/* 403 */     String reqLeft = this.leftTextArea.getText();
/* 404 */     String reqRight = this.rightTextArea.getText();
/* 405 */     String proxyType = (String)this.proxyComboBox.getSelectedItem();
/* 406 */     String proxyHost = this.proxyHostTextField.getText();
/* 407 */     String remark = this.remarkTextField.getText();
/* 408 */     int proxyPort = 8888;
/* 409 */     int connTimeout = 30000;
/* 410 */     int readTimeout = 30000;
/*     */     try {
/* 412 */       proxyPort = Integer.parseInt(this.proxyPortTextField.getText());
/* 413 */       connTimeout = Integer.parseInt(this.connTimeOutTextField.getText());
/* 414 */       readTimeout = Integer.parseInt(this.readTimeOutTextField.getText());
/* 415 */     } catch (Exception e) {
/* 416 */       Log.error(e);
/* 417 */       this.error = e.getMessage();
/* 418 */       return false;
/*     */     } 
/* 420 */     if (url != null && url.trim().length() > 0 && password != null && password.trim().length() > 0 && secretKey != null && secretKey.trim().length() > 0 && payload != null && payload.trim().length() > 0 && cryption != null && cryption.trim().length() > 0 && encoding != null && encoding.trim().length() > 0) {
/* 421 */       this.shellContext.setUrl((url == null) ? "" : url);
/* 422 */       this.shellContext.setPassword((password == null) ? "" : password);
/* 423 */       this.shellContext.setSecretKey((secretKey == null) ? "" : secretKey);
/* 424 */       this.shellContext.setPayload((payload == null) ? "" : payload);
/* 425 */       this.shellContext.setCryption((cryption == null) ? "" : cryption);
/* 426 */       this.shellContext.setEncoding((encoding == null) ? "" : encoding);
/* 427 */       this.shellContext.setHeader((headers == null) ? "" : headers);
/* 428 */       this.shellContext.setReqLeft((reqLeft == null) ? "" : reqLeft);
/* 429 */       this.shellContext.setReqRight((reqRight == null) ? "" : reqRight);
/* 430 */       this.shellContext.setConnTimeout(connTimeout);
/* 431 */       this.shellContext.setReadTimeout(readTimeout);
/* 432 */       this.shellContext.setProxyType((proxyType == null) ? "" : proxyType);
/* 433 */       this.shellContext.setProxyHost((proxyHost == null) ? "" : proxyHost);
/* 434 */       this.shellContext.setProxyPort(proxyPort);
/* 435 */       this.shellContext.setRemark((remark == null) ? "" : remark);
/* 436 */       return true;
/*     */     } 
/* 438 */     this.error = "请检查  url password secretKey payload cryption encoding 是否填写完整";
/* 439 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 445 */     super.dispose();
/* 446 */     MainActivity.getMainActivityFrame().refreshShellView();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\cor\\ui\component\frame\ShellSetting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */