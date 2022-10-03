/*     */ package shells.plugins.cshap;
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
/*     */ @PluginAnnotation(payloadName = "CShapDynamicPayload", Name = "Zip", DisplayName = "ZIP压缩")
/*     */ public class CZip
/*     */   implements Plugin
/*     */ {
/*     */   private static final String CLASS_NAME = "CZip.Run";
/*     */   private ShellEntity shellEntity;
/*     */   private Payload payload;
/*  42 */   private final JPanel panel = new JPanel(new GridBagLayout()); private final JLabel compressSrcDirLabel; private final JLabel compressDestFileLabel; private final JTextField compressDestFileTextField; private final JTextField compressSrcDirTextField;
/*     */   public CZip() {
/*  44 */     GBC gbcLCompressSrcDir = (new GBC(0, 0)).setInsets(5, -40, 0, 0);
/*  45 */     GBC gbcCompressSrcDir = (new GBC(1, 0, 3, 1)).setInsets(5, 20, 0, 0);
/*  46 */     GBC gbcLCompressDestFileLabel = (new GBC(0, 1)).setInsets(5, -40, 0, 0);
/*  47 */     GBC gbcCompressDestFile = (new GBC(1, 1, 3, 1)).setInsets(5, 20, 0, 0);
/*  48 */     GBC gbcZipButton = (new GBC(0, 2)).setInsets(5, -20, 0, 0);
/*  49 */     GBC gbcUnZipButton = (new GBC(0, 2, 5, 1)).setInsets(5, 20, 0, 0);
/*     */     
/*  51 */     this.compressSrcDirLabel = new JLabel("目标文件夹");
/*  52 */     this.compressDestFileLabel = new JLabel("压缩文件");
/*     */     
/*  54 */     this.zipButton = new JButton("压缩");
/*  55 */     this.unZipButton = new JButton("解压");
/*     */     
/*  57 */     this.compressSrcDirTextField = new JTextField(50);
/*  58 */     this.compressDestFileTextField = new JTextField(50);
/*     */ 
/*     */ 
/*     */     
/*  62 */     this.panel.add(this.compressSrcDirLabel, gbcLCompressSrcDir);
/*  63 */     this.panel.add(this.compressSrcDirTextField, gbcCompressSrcDir);
/*  64 */     this.panel.add(this.compressDestFileLabel, gbcLCompressDestFileLabel);
/*  65 */     this.panel.add(this.compressDestFileTextField, gbcCompressDestFile);
/*  66 */     this.panel.add(this.zipButton, gbcZipButton);
/*  67 */     this.panel.add(this.unZipButton, gbcUnZipButton);
/*     */   }
/*     */   private final JButton zipButton; private final JButton unZipButton; private Encoding encoding; private boolean loadState;
/*     */   
/*     */   private void load() {
/*  72 */     if (!this.loadState)
/*     */       try {
/*  74 */         InputStream inputStream = getClass().getResourceAsStream(String.format("assets/%s.dll", new Object[] { "CZip.Run".substring(0, "CZip.Run".indexOf(".")) }));
/*  75 */         byte[] binCode = functions.readInputStream(inputStream);
/*  76 */         inputStream.close();
/*  77 */         this.loadState = this.payload.include("CZip.Run", binCode);
/*  78 */         if (this.loadState) {
/*  79 */           Log.log("Load success", new Object[0]);
/*     */         } else {
/*  81 */           Log.log("Load fail", new Object[0]);
/*     */         } 
/*  83 */       } catch (Exception e) {
/*  84 */         Log.error(e);
/*     */       }  
/*     */   }
/*     */   
/*     */   private void zipButtonClick(ActionEvent actionEvent) {
/*  89 */     load();
/*  90 */     if (this.compressDestFileTextField.getText().trim().length() > 0 && this.compressSrcDirTextField.getText().trim().length() > 0) {
/*  91 */       ReqParameter reqParameter = new ReqParameter();
/*  92 */       reqParameter.add("compressFile", this.compressDestFileTextField.getText().trim());
/*  93 */       reqParameter.add("compressDir", this.compressSrcDirTextField.getText().trim());
/*  94 */       String resultString = this.encoding.Decoding(this.payload.evalFunc("CZip.Run", "zip", reqParameter));
/*  95 */       GOptionPane.showMessageDialog(null, resultString, "提示", 1);
/*     */     } else {
/*  97 */       GOptionPane.showMessageDialog(null, "请检查是否填写完整", "提示", 1);
/*     */     } 
/*     */   }
/*     */   private void unZipButtonClick(ActionEvent actionEvent) {
/* 101 */     load();
/* 102 */     if (this.compressDestFileTextField.getText().trim().length() > 0 && this.compressSrcDirTextField.getText().trim().length() > 0) {
/* 103 */       ReqParameter reqParameter = new ReqParameter();
/* 104 */       reqParameter.add("compressFile", this.compressDestFileTextField.getText().trim());
/* 105 */       reqParameter.add("compressDir", this.compressSrcDirTextField.getText().trim());
/* 106 */       String resultString = this.encoding.Decoding(this.payload.evalFunc("CZip.Run", "unZip", reqParameter));
/* 107 */       GOptionPane.showMessageDialog(null, resultString, "提示", 1);
/*     */     } else {
/* 109 */       GOptionPane.showMessageDialog(null, "请检查是否填写完整", "提示", 1);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void init(ShellEntity shellEntity) {
/* 114 */     this.shellEntity = shellEntity;
/* 115 */     this.payload = this.shellEntity.getPayloadModule();
/* 116 */     this.encoding = Encoding.getEncoding(this.shellEntity);
/*     */     
/* 118 */     this.compressSrcDirTextField.setText(this.payload.currentDir());
/* 119 */     this.compressDestFileTextField.setText(this.payload.currentDir() + functions.getLastFileName(this.payload.currentDir()) + ".zip");
/*     */     
/* 121 */     automaticBindClick.bindJButtonClick(this, this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JPanel getView() {
/* 127 */     return this.panel;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\cshap\CZip.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */