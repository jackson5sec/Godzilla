/*     */ package shells.plugins.php;
/*     */ 
/*     */ import core.Encoding;
/*     */ import core.annotation.PluginAnnotation;
/*     */ import core.imp.Payload;
/*     */ import core.imp.Plugin;
/*     */ import core.shell.ShellEntity;
/*     */ import core.ui.component.DataView;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.io.InputStream;
/*     */ import java.util.Vector;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
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
/*     */ @PluginAnnotation(payloadName = "PhpDynamicPayload", Name = "PWebShellScan", DisplayName = "WebShellScan")
/*     */ public class PWebShellScan
/*     */   implements Plugin
/*     */ {
/*     */   private static final String CLASS_NAME = "WebShellScan";
/*  31 */   private static final Vector COLUMNS_VECTOR = new Vector(new CopyOnWriteArrayList((Object[])new String[] { "File", "Line", "SuspiciousCode" }));
/*     */   
/*     */   private final JPanel panel;
/*     */   
/*     */   private final DataView dataView;
/*     */   
/*     */   private final JButton scanButton;
/*     */   
/*     */   private final JLabel scanPathLabel;
/*     */   
/*     */   private final JTextField scanPathTextField;
/*     */   private boolean loadState;
/*     */   private final JSplitPane portScanSplitPane;
/*     */   private ShellEntity shellEntity;
/*     */   private Payload payload;
/*     */   private Encoding encoding;
/*     */   
/*     */   public PWebShellScan() {
/*  49 */     this.panel = new JPanel(new BorderLayout());
/*     */ 
/*     */     
/*  52 */     this.scanPathLabel = new JLabel("scanPath :");
/*  53 */     this.scanButton = new JButton("scan");
/*  54 */     this.dataView = new DataView(null, COLUMNS_VECTOR, -1, -1);
/*  55 */     this.scanPathTextField = new JTextField(30);
/*  56 */     this.portScanSplitPane = new JSplitPane();
/*     */ 
/*     */     
/*  59 */     this.portScanSplitPane.setOrientation(0);
/*  60 */     this.portScanSplitPane.setDividerSize(0);
/*     */     
/*  62 */     JPanel topPanel = new JPanel();
/*  63 */     topPanel.add(this.scanPathLabel);
/*  64 */     topPanel.add(this.scanPathTextField);
/*  65 */     topPanel.add(this.scanButton);
/*     */     
/*  67 */     this.portScanSplitPane.setTopComponent(topPanel);
/*  68 */     this.portScanSplitPane.setBottomComponent(new JScrollPane((Component)this.dataView));
/*     */ 
/*     */     
/*  71 */     this.panel.add(this.portScanSplitPane);
/*     */   }
/*     */   
/*     */   private void load() {
/*  75 */     if (!this.loadState) {
/*     */       try {
/*  77 */         InputStream inputStream = getClass().getResourceAsStream(String.format("assets/%s.php", new Object[] { "WebShellScan" }));
/*  78 */         byte[] data = functions.readInputStream(inputStream);
/*  79 */         inputStream.close();
/*  80 */         if (this.loadState = this.payload.include("WebShellScan", data)) {
/*  81 */           this.loadState = true;
/*  82 */           Log.log("Load success", new Object[0]);
/*     */         } else {
/*  84 */           Log.log("Load fail", new Object[0]);
/*     */         } 
/*  86 */       } catch (Exception e) {
/*  87 */         Log.error(e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private void scanButtonClick(ActionEvent actionEvent) {
/*  93 */     load();
/*  94 */     String scanPath = this.scanPathTextField.getText().trim();
/*  95 */     ReqParameter reqParamete = new ReqParameter();
/*  96 */     reqParamete.add("scanPath", scanPath);
/*  97 */     byte[] result = this.payload.evalFunc("WebShellScan", "run", reqParamete);
/*  98 */     String resultString = this.encoding.Decoding(result);
/*  99 */     formatResult(resultString);
/*     */   }
/*     */ 
/*     */   
/*     */   private void formatResult(String resultString) {
/* 104 */     String[] lines = resultString.split("\n");
/* 105 */     String[] infos = null;
/* 106 */     Vector<Vector<String>> rowsVector = new Vector();
/* 107 */     for (String line : lines) {
/* 108 */       infos = line.split("\t");
/* 109 */       if (infos.length >= 3) {
/* 110 */         Vector<String> oneRowVector = new Vector();
/* 111 */         boolean st = false;
/* 112 */         oneRowVector.add(functions.base64DecodeToString(infos[0]));
/* 113 */         oneRowVector.add(functions.base64DecodeToString(infos[1]));
/* 114 */         oneRowVector.add(functions.base64DecodeToString(infos[2]));
/* 115 */         for (Object object : rowsVector) {
/* 116 */           if (object.equals(oneRowVector)) {
/* 117 */             st = true;
/*     */             break;
/*     */           } 
/*     */         } 
/* 121 */         if (!st) {
/* 122 */           rowsVector.add(oneRowVector);
/*     */         }
/*     */       } else {
/* 125 */         Log.error(line);
/*     */       } 
/*     */     } 
/* 128 */     this.dataView.AddRows(rowsVector);
/*     */   }
/*     */ 
/*     */   
/*     */   public void init(ShellEntity shellEntity) {
/* 133 */     this.shellEntity = shellEntity;
/* 134 */     this.payload = this.shellEntity.getPayloadModule();
/* 135 */     this.encoding = Encoding.getEncoding(this.shellEntity);
/* 136 */     this.scanPathTextField.setText(this.payload.currentDir());
/* 137 */     automaticBindClick.bindJButtonClick(this, this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JPanel getView() {
/* 143 */     return this.panel;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\php\PWebShellScan.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */