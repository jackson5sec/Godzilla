/*     */ package shells.plugins.generic.gui.dialog;
/*     */ 
/*     */ import com.intellij.uiDesigner.core.GridConstraints;
/*     */ import core.socksServer.SocksServerConfig;
/*     */ import java.awt.event.ActionListener;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JTextField;
/*     */ 
/*     */ public class SocksClientRetransmissionConfigManage extends JDialog {
/*     */   private JPanel contentPane;
/*     */   public JLabel clientSocketOnceReadSizeLabel;
/*     */   public JTextField clientSocketOnceReadSizeTextField;
/*     */   public JLabel clientPacketSizeLabel;
/*     */   public JTextField clientPacketTextField;
/*     */   public JLabel capacityLabel;
/*     */   public JTextField capacityTextField;
/*     */   public JLabel requestDelayLabel;
/*     */   public JTextField requestDelayTextField;
/*     */   public JLabel requestErrRetryLabel;
/*     */   public JTextField requestErrRetryTextField;
/*     */   public JLabel requestErrDelayLabel;
/*     */   public JTextField requestErrDelayTextField;
/*     */   public JButton saveButton;
/*     */   public JButton cancelButton;
/*     */   private SocksServerConfig socksServerConfig;
/*     */   
/*     */   public static void socksServerConfig(Window parentWindow, SocksServerConfig socksServerConfig) {
/*     */     SocksClientRetransmissionConfigManage dialog = new SocksClientRetransmissionConfigManage(parentWindow, socksServerConfig);
/*     */     dialog.pack();
/*     */     dialog.setLocationRelativeTo(parentWindow);
/*     */     EasyI18N.installObject(dialog);
/*     */     dialog.setVisible(true);
/*     */   }
/*     */   
/*  36 */   public SocksClientRetransmissionConfigManage(Window parentWindow, SocksServerConfig socksServerConfig) { super(parentWindow, "配置代理");
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
/* 122 */     $$$setupUI$$$(); setContentPane(this.contentPane); setModal(true); getRootPane().setDefaultButton(this.saveButton); this.socksServerConfig = socksServerConfig; this.clientSocketOnceReadSizeTextField.setText(String.valueOf(socksServerConfig.clientSocketOnceReadSize.get())); this.clientPacketTextField.setText(String.valueOf(socksServerConfig.clientPacketSize.get())); this.capacityTextField.setText(String.valueOf(socksServerConfig.capacity.get())); this.requestDelayTextField.setText(String.valueOf(socksServerConfig.requestDelay.get()));
/*     */     this.requestErrRetryTextField.setText(String.valueOf(socksServerConfig.requestErrRetry.get()));
/*     */     this.requestErrDelayTextField.setText(String.valueOf(socksServerConfig.requestErrDelay.get()));
/*     */     this.saveButton.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { SocksClientRetransmissionConfigManage.this.onOK(); } }
/*     */       );
/*     */     this.cancelButton.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { SocksClientRetransmissionConfigManage.this.onCancel(); } }
/*     */       );
/*     */     setDefaultCloseOperation(0);
/*     */     addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { SocksClientRetransmissionConfigManage.this.onCancel(); } }
/*     */       );
/*     */     this.contentPane.registerKeyboardAction(new ActionListener() { public void actionPerformed(ActionEvent e) { SocksClientRetransmissionConfigManage.this.onCancel(); }
/* 133 */         },  KeyStroke.getKeyStroke(27, 0), 1); } private void $$$setupUI$$$() { this.contentPane = new JPanel();
/* 134 */     this.contentPane.setLayout((LayoutManager)new GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), -1, -1));
/* 135 */     JPanel panel1 = new JPanel();
/* 136 */     panel1.setLayout((LayoutManager)new GridLayoutManager(8, 2, new Insets(0, 0, 0, 0), -1, -1));
/* 137 */     this.contentPane.add(panel1, new GridConstraints(0, 0, 1, 1, 0, 0, 3, 3, null, null, null, 0, false));
/* 138 */     this.clientSocketOnceReadSizeTextField = new JTextField();
/* 139 */     this.clientSocketOnceReadSizeTextField.setText("102400");
/* 140 */     panel1.add(this.clientSocketOnceReadSizeTextField, new GridConstraints(0, 1, 1, 1, 8, 1, 4, 0, null, new Dimension(150, -1), null, 0, false));
/* 141 */     this.clientPacketSizeLabel = new JLabel();
/* 142 */     this.clientPacketSizeLabel.setText("Client单次读取大小:");
/* 143 */     panel1.add(this.clientPacketSizeLabel, new GridConstraints(1, 0, 1, 1, 0, 0, 0, 0, null, null, null, 0, false));
/* 144 */     this.clientPacketTextField = new JTextField();
/* 145 */     this.clientPacketTextField.setText("1024000");
/* 146 */     panel1.add(this.clientPacketTextField, new GridConstraints(1, 1, 1, 1, 8, 1, 4, 0, null, new Dimension(150, -1), null, 0, false));
/* 147 */     JPanel panel2 = new JPanel();
/* 148 */     panel2.setLayout((LayoutManager)new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
/* 149 */     panel1.add(panel2, new GridConstraints(6, 0, 1, 2, 0, 3, 3, 3, null, null, null, 0, false));
/* 150 */     this.saveButton = new JButton();
/* 151 */     this.saveButton.setText("保存");
/* 152 */     panel2.add(this.saveButton, new GridConstraints(0, 0, 1, 1, 0, 1, 3, 0, null, null, null, 0, false));
/* 153 */     this.cancelButton = new JButton();
/* 154 */     this.cancelButton.setText("取消");
/* 155 */     panel2.add(this.cancelButton, new GridConstraints(0, 1, 1, 1, 0, 1, 3, 0, null, null, null, 0, false));
/* 156 */     JPanel panel3 = new JPanel();
/* 157 */     panel3.setLayout((LayoutManager)new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
/* 158 */     panel1.add(panel3, new GridConstraints(7, 0, 1, 2, 0, 3, 3, 3, null, null, null, 0, false));
/* 159 */     this.requestDelayLabel = new JLabel();
/* 160 */     this.requestDelayLabel.setText("请求抖动延迟(ms)");
/* 161 */     panel1.add(this.requestDelayLabel, new GridConstraints(3, 0, 1, 1, 0, 0, 0, 0, null, null, null, 0, false));
/* 162 */     this.requestDelayTextField = new JTextField();
/* 163 */     this.requestDelayTextField.setText("10");
/* 164 */     panel1.add(this.requestDelayTextField, new GridConstraints(3, 1, 1, 1, 8, 1, 4, 0, null, new Dimension(150, -1), null, 0, false));
/* 165 */     this.requestErrRetryLabel = new JLabel();
/* 166 */     this.requestErrRetryLabel.setText("错误重试最大次数");
/* 167 */     panel1.add(this.requestErrRetryLabel, new GridConstraints(4, 0, 1, 1, 0, 0, 0, 0, null, null, null, 0, false));
/* 168 */     this.requestErrRetryTextField = new JTextField();
/* 169 */     this.requestErrRetryTextField.setText("20");
/* 170 */     panel1.add(this.requestErrRetryTextField, new GridConstraints(4, 1, 1, 1, 8, 1, 4, 0, null, new Dimension(150, -1), null, 0, false));
/* 171 */     this.requestErrDelayLabel = new JLabel();
/* 172 */     this.requestErrDelayLabel.setText("请求错误重试抖动延时(ms)");
/* 173 */     panel1.add(this.requestErrDelayLabel, new GridConstraints(5, 0, 1, 1, 0, 0, 0, 0, null, null, null, 1, false));
/* 174 */     this.requestErrDelayTextField = new JTextField();
/* 175 */     this.requestErrDelayTextField.setText("30");
/* 176 */     panel1.add(this.requestErrDelayTextField, new GridConstraints(5, 1, 1, 1, 8, 1, 4, 0, null, new Dimension(150, -1), null, 0, false));
/* 177 */     this.capacityLabel = new JLabel();
/* 178 */     this.capacityLabel.setText("套接字缓冲队列数");
/* 179 */     panel1.add(this.capacityLabel, new GridConstraints(2, 0, 1, 1, 0, 0, 0, 0, null, null, null, 0, false));
/* 180 */     this.capacityTextField = new JTextField();
/* 181 */     this.capacityTextField.setText("5");
/* 182 */     panel1.add(this.capacityTextField, new GridConstraints(2, 1, 1, 1, 8, 1, 4, 0, null, new Dimension(150, -1), null, 0, false));
/* 183 */     this.clientSocketOnceReadSizeLabel = new JLabel();
/* 184 */     this.clientSocketOnceReadSizeLabel.setText("Client套接字单次读取大小:");
/* 185 */     panel1.add(this.clientSocketOnceReadSizeLabel, new GridConstraints(0, 0, 1, 1, 0, 0, 0, 0, null, null, null, 0, false)); } private void onOK() { try {
/*     */       int clientSocketOnceReadSize = Integer.parseInt(this.clientSocketOnceReadSizeTextField.getText().trim()); int clientPacketSize = Integer.parseInt(this.clientPacketTextField.getText().trim()); int capacity = Integer.parseInt(this.capacityTextField.getText().trim()); int requestDelay = Integer.parseInt(this.requestDelayTextField.getText().trim()); int requestErrRetry = Integer.parseInt(this.requestErrRetryTextField.getText().trim()); int requestErrDelay = Integer.parseInt(this.requestErrDelayTextField.getText().trim()); this.socksServerConfig.clientSocketOnceReadSize.set(clientSocketOnceReadSize); this.socksServerConfig.clientPacketSize.set(clientPacketSize); this.socksServerConfig.capacity.set(capacity); this.socksServerConfig.requestDelay.set(requestDelay); this.socksServerConfig.requestErrRetry.set(requestErrRetry); this.socksServerConfig.requestErrDelay.set(requestErrDelay); GOptionPane.showMessageDialog(this, "修改成功 配置已生效");
/*     */     } catch (Exception e) {
/*     */       GOptionPane.showMessageDialog(this, e.getLocalizedMessage()); return;
/*     */     } 
/*     */     dispose(); }
/*     */   private void onCancel() { dispose(); }
/* 192 */   public JComponent $$$getRootComponent$$$() { return this.contentPane; }
/*     */ 
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\generic\gui\dialog\SocksClientRetransmissionConfigManage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */