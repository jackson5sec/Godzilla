/*     */ package shells.plugins.generic.gui.dialog;
/*     */ 
/*     */ import com.intellij.uiDesigner.core.GridConstraints;
/*     */ import com.intellij.uiDesigner.core.GridLayoutManager;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import javax.swing.DefaultComboBoxModel;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JTextField;
/*     */ 
/*     */ public class ChooseNewRetransmissionDialog extends JDialog {
/*     */   private JPanel contentPane;
/*     */   private JButton buttonOK;
/*     */   private JButton buttonCancel;
/*     */   public JTextField listenAddressTextField;
/*     */   public JTextField listenPortTextField;
/*     */   public JComboBox proxyTypeComboBox;
/*     */   public JTextField targetAddressTextField;
/*     */   public JTextField targetPortTextField;
/*     */   public JLabel listenAddressLabel;
/*     */   public JLabel listenPortLabel;
/*     */   public JLabel proxyTypeLabel;
/*     */   public JLabel targetAddressLabel;
/*     */   public JLabel targetPortLabel;
/*     */   public Retransmission proxyModel;
/*     */   
/*  32 */   public ChooseNewRetransmissionDialog(Window parentWindow) { super(parentWindow);
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
/* 102 */     $$$setupUI$$$(); setContentPane(this.contentPane);
/*     */     setModal(true);
/*     */     getRootPane().setDefaultButton(this.buttonOK);
/*     */     this.buttonOK.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { ChooseNewRetransmissionDialog.this.onOK(); } }
/*     */       );
/*     */     this.buttonCancel.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { ChooseNewRetransmissionDialog.this.onCancel(); } }
/*     */       );
/*     */     setDefaultCloseOperation(0);
/*     */     addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { ChooseNewRetransmissionDialog.this.onCancel(); } }
/*     */       );
/*     */     this.contentPane.registerKeyboardAction(new ActionListener() { public void actionPerformed(ActionEvent e) { ChooseNewRetransmissionDialog.this.onCancel(); }
/* 113 */         },  KeyStroke.getKeyStroke(27, 0), 1); } private void $$$setupUI$$$() { this.contentPane = new JPanel();
/* 114 */     this.contentPane.setLayout((LayoutManager)new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
/* 115 */     JPanel panel1 = new JPanel();
/* 116 */     panel1.setLayout((LayoutManager)new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
/* 117 */     this.contentPane.add(panel1, new GridConstraints(1, 0, 1, 1, 0, 3, 3, 1, null, null, null, 0, false));
/* 118 */     JPanel panel2 = new JPanel();
/* 119 */     panel2.setLayout((LayoutManager)new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
/* 120 */     panel1.add(panel2, new GridConstraints(0, 0, 1, 1, 0, 3, 3, 3, null, null, null, 0, false));
/* 121 */     this.buttonOK = new JButton();
/* 122 */     this.buttonOK.setText("添加");
/* 123 */     panel2.add(this.buttonOK, new GridConstraints(0, 0, 1, 1, 0, 1, 3, 0, null, null, null, 0, false));
/* 124 */     this.buttonCancel = new JButton();
/* 125 */     this.buttonCancel.setText("取消");
/* 126 */     panel2.add(this.buttonCancel, new GridConstraints(0, 1, 1, 1, 0, 1, 3, 0, null, null, null, 0, false));
/* 127 */     JPanel panel3 = new JPanel();
/* 128 */     panel3.setLayout((LayoutManager)new GridLayoutManager(5, 2, new Insets(0, 0, 0, 0), -1, -1));
/* 129 */     this.contentPane.add(panel3, new GridConstraints(0, 0, 1, 1, 0, 3, 3, 3, null, null, null, 0, false));
/* 130 */     this.listenAddressLabel = new JLabel();
/* 131 */     this.listenAddressLabel.setText("监听地址");
/* 132 */     panel3.add(this.listenAddressLabel, new GridConstraints(0, 0, 1, 1, 8, 0, 0, 0, null, null, null, 0, false));
/* 133 */     this.listenAddressTextField = new JTextField();
/* 134 */     this.listenAddressTextField.setText("127.0.0.1");
/* 135 */     panel3.add(this.listenAddressTextField, new GridConstraints(0, 1, 1, 1, 8, 1, 4, 0, null, new Dimension(150, -1), null, 0, false));
/* 136 */     this.listenPortLabel = new JLabel();
/* 137 */     this.listenPortLabel.setText("监听端口");
/* 138 */     panel3.add(this.listenPortLabel, new GridConstraints(1, 0, 1, 1, 8, 0, 0, 0, null, null, null, 0, false));
/* 139 */     this.listenPortTextField = new JTextField();
/* 140 */     this.listenPortTextField.setText("6666");
/* 141 */     panel3.add(this.listenPortTextField, new GridConstraints(1, 1, 1, 1, 8, 1, 4, 0, null, new Dimension(150, -1), null, 0, false));
/* 142 */     this.proxyTypeLabel = new JLabel();
/* 143 */     this.proxyTypeLabel.setText("代理类型");
/* 144 */     panel3.add(this.proxyTypeLabel, new GridConstraints(2, 0, 1, 1, 8, 0, 0, 0, null, null, null, 0, false));
/* 145 */     this.proxyTypeComboBox = new JComboBox();
/* 146 */     DefaultComboBoxModel<String> defaultComboBoxModel1 = new DefaultComboBoxModel();
/* 147 */     defaultComboBoxModel1.addElement("PORT_FORWARD");
/* 148 */     defaultComboBoxModel1.addElement("PORT_MAP");
/* 149 */     this.proxyTypeComboBox.setModel(defaultComboBoxModel1);
/* 150 */     panel3.add(this.proxyTypeComboBox, new GridConstraints(2, 1, 1, 1, 8, 1, 2, 0, null, null, null, 0, false));
/* 151 */     this.targetAddressLabel = new JLabel();
/* 152 */     this.targetAddressLabel.setText("目标地址");
/* 153 */     panel3.add(this.targetAddressLabel, new GridConstraints(3, 0, 1, 1, 8, 0, 0, 0, null, null, null, 0, false));
/* 154 */     this.targetAddressTextField = new JTextField();
/* 155 */     this.targetAddressTextField.setText("8.8.8.8");
/* 156 */     panel3.add(this.targetAddressTextField, new GridConstraints(3, 1, 1, 1, 8, 1, 4, 0, null, new Dimension(150, -1), null, 0, false));
/* 157 */     this.targetPortLabel = new JLabel();
/* 158 */     this.targetPortLabel.setText("目标端口");
/* 159 */     panel3.add(this.targetPortLabel, new GridConstraints(4, 0, 1, 1, 8, 0, 0, 0, null, null, null, 0, false));
/* 160 */     this.targetPortTextField = new JTextField();
/* 161 */     this.targetPortTextField.setText("53");
/* 162 */     panel3.add(this.targetPortTextField, new GridConstraints(4, 1, 1, 1, 8, 1, 4, 0, null, new Dimension(150, -1), null, 0, false)); }
/*     */   private void onOK() { if (this.proxyModel == null)
/*     */       this.proxyModel = new Retransmission();  try { String chooseProxy = this.proxyTypeComboBox.getSelectedItem().toString().toUpperCase(); this.proxyModel.listenAddress = this.listenAddressTextField.getText().trim(); this.proxyModel.listenPort = Integer.parseInt(this.listenPortTextField.getText().trim()); this.proxyModel.targetAddress = this.targetAddressTextField.getText().trim(); this.proxyModel.targetPort = Integer.parseInt(this.targetPortTextField.getText().trim()); this.proxyModel.retransmissionType = "PORT_FORWARD".equals(chooseProxy) ? RetransmissionType.PORT_FORWARD : ("PORT_MAP".equals(chooseProxy) ? RetransmissionType.PORT_MAP : RetransmissionType.NULL); }
/*     */     catch (Exception e)
/*     */     { GOptionPane.showMessageDialog(this, e.getLocalizedMessage()); return; }
/*     */      dispose(); }
/*     */   private void onCancel() { this.proxyModel = null; dispose(); }
/* 169 */   public static Retransmission chooseNewProxy(Window parentWindow) { ChooseNewRetransmissionDialog dialog = new ChooseNewRetransmissionDialog(parentWindow); dialog.pack(); dialog.setLocationRelativeTo(parentWindow); EasyI18N.installObject(dialog); dialog.setVisible(true); return dialog.proxyModel; } public JComponent $$$getRootComponent$$$() { return this.contentPane; }
/*     */ 
/*     */   
/*     */   private void createUIComponents() {}
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\generic\gui\dialog\ChooseNewRetransmissionDialog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */