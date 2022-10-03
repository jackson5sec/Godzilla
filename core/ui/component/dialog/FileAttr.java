/*     */ package core.ui.component.dialog;
/*     */ 
/*     */ import core.EasyI18N;
/*     */ import core.imp.Payload;
/*     */ import core.shell.ShellEntity;
/*     */ import core.ui.component.GBC;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Frame;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.text.SimpleDateFormat;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JTextField;
/*     */ import util.Log;
/*     */ import util.automaticBindClick;
/*     */ import util.functions;
/*     */ 
/*     */ 
/*     */ public class FileAttr
/*     */   extends JDialog
/*     */ {
/*     */   private final Payload payload;
/*     */   private final ShellEntity shellEntity;
/*     */   private final JLabel fileLabel;
/*     */   private final JTextField fileTextField;
/*     */   private final JLabel filePermissionLabel;
/*     */   private final JTextField filePermissionTextField;
/*     */   private final JButton updateFilePermissionButton;
/*     */   private final JLabel fileTimeLabel;
/*     */   private final JTextField fileTimeTextField;
/*     */   private final JButton updateFileTimeButton;
/*     */   
/*     */   public FileAttr(ShellEntity shellEntity, String file, String filePermission, String fileTime) {
/*  37 */     super((Frame)shellEntity.getFrame(), "FileAttr", true);
/*     */     
/*  39 */     this.fileLabel = new JLabel("文件路径: ");
/*  40 */     this.fileTextField = new JTextField(20);
/*     */     
/*  42 */     this.filePermissionLabel = new JLabel("文件权限: ");
/*  43 */     this.filePermissionTextField = new JTextField(5);
/*  44 */     this.updateFilePermissionButton = new JButton("修改");
/*     */     
/*  46 */     this.fileTimeLabel = new JLabel("文件修改时间: ");
/*  47 */     this.fileTimeTextField = new JTextField(15);
/*  48 */     this.updateFileTimeButton = new JButton("修改");
/*     */     
/*  50 */     GBC gbcFileLabel = new GBC(0, 0);
/*  51 */     GBC gbcFileTextField = (new GBC(1, 0)).setInsets(0, 10, 0, 10);
/*  52 */     GBC gbcFilePermissionLabel = new GBC(0, 1);
/*  53 */     GBC gbcFilePermissionTextField = (new GBC(1, 1)).setInsets(0, 20, 0, 20);
/*  54 */     GBC gbcUpdateFilePermissionButton = (new GBC(2, 1)).setInsets(0, 20, 0, 20);
/*  55 */     GBC gbcFileTimeLabel = new GBC(0, 2);
/*  56 */     GBC gbcFileTimeTextField = (new GBC(1, 2)).setInsets(0, 10, 0, 10);
/*  57 */     GBC gbcUpdateFileTimeButton = (new GBC(2, 2)).setInsets(0, 20, 0, 20);
/*     */     
/*  59 */     this.fileTextField.setText(file);
/*  60 */     this.filePermissionTextField.setText(filePermission);
/*  61 */     this.fileTimeTextField.setText(fileTime);
/*     */ 
/*     */     
/*  64 */     setLayout(new GridBagLayout());
/*  65 */     Container container = getContentPane();
/*     */ 
/*     */     
/*  68 */     container.add(this.fileLabel, gbcFileLabel);
/*  69 */     container.add(this.fileTextField, gbcFileTextField);
/*  70 */     container.add(this.filePermissionLabel, gbcFilePermissionLabel);
/*  71 */     container.add(this.filePermissionTextField, gbcFilePermissionTextField);
/*  72 */     container.add(this.updateFilePermissionButton, gbcUpdateFilePermissionButton);
/*  73 */     container.add(this.fileTimeLabel, gbcFileTimeLabel);
/*  74 */     container.add(this.fileTimeTextField, gbcFileTimeTextField);
/*  75 */     container.add(this.updateFileTimeButton, gbcUpdateFileTimeButton);
/*     */ 
/*     */     
/*  78 */     this.shellEntity = shellEntity;
/*  79 */     this.payload = shellEntity.getPayloadModule();
/*     */     
/*  81 */     automaticBindClick.bindJButtonClick(this, this);
/*     */ 
/*     */     
/*  84 */     functions.setWindowSize(this, 520, 130);
/*  85 */     setLocationRelativeTo((Component)shellEntity.getFrame());
/*  86 */     EasyI18N.installObject(this);
/*  87 */     setVisible(true);
/*     */   }
/*     */   
/*     */   public void updateFilePermissionButtonClick(ActionEvent e) {
/*  91 */     boolean state = false;
/*  92 */     String fileName = this.fileTextField.getText().trim();
/*  93 */     String filePermission = this.filePermissionTextField.getText().trim();
/*     */     try {
/*  95 */       state = this.payload.setFileAttr(fileName, "fileBasicAttr", filePermission);
/*  96 */     } catch (Exception ex) {
/*  97 */       Log.error(ex);
/*     */     } finally {
/*     */       
/* 100 */       if (state) {
/* 101 */         GOptionPane.showMessageDialog((Component)this.shellEntity.getFrame(), "修改成功", "提示", 1);
/* 102 */         dispose();
/*     */       } else {
/* 104 */         GOptionPane.showMessageDialog((Component)this.shellEntity.getFrame(), "修改失败", "提示", 2);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public void updateFileTimeButtonClick(ActionEvent e) {
/* 109 */     boolean state = false;
/* 110 */     String fileName = this.fileTextField.getText().trim();
/* 111 */     String fileTime = this.fileTimeTextField.getText().trim();
/*     */     try {
/* 113 */       SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/* 114 */       String timestampString = Long.toString(simpleDateFormat.parse(fileTime).getTime());
/* 115 */       if (timestampString.length() > 10) {
/* 116 */         timestampString = timestampString.substring(0, 10);
/*     */       }
/* 118 */       state = this.payload.setFileAttr(fileName, "fileTimeAttr", timestampString);
/* 119 */     } catch (Exception ex) {
/* 120 */       Log.error(ex);
/*     */     } finally {
/*     */       
/* 123 */       if (state) {
/* 124 */         GOptionPane.showMessageDialog((Component)this.shellEntity.getFrame(), "修改成功", "提示", 1);
/* 125 */         dispose();
/*     */       } else {
/* 127 */         GOptionPane.showMessageDialog((Component)this.shellEntity.getFrame(), "修改失败", "提示", 2);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\cor\\ui\component\dialog\FileAttr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */