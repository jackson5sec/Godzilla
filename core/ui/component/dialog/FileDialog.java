/*     */ package core.ui.component.dialog;
/*     */ 
/*     */ import core.EasyI18N;
/*     */ import core.ui.component.GBC;
/*     */ import core.ui.component.model.FileOpertionInfo;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Frame;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.awt.event.WindowListener;
/*     */ import java.io.File;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JTextField;
/*     */ import util.automaticBindClick;
/*     */ import util.functions;
/*     */ 
/*     */ public class FileDialog
/*     */   extends JDialog {
/*     */   private final JTextField srcFileTextField;
/*     */   private final JTextField destFileTextField;
/*     */   private final JLabel srcFileLabel;
/*     */   private final JLabel destFileLabel;
/*     */   private final JButton okButton;
/*  27 */   Dimension TextFieldDim = new Dimension(500, 23); private final JButton cancelButton; private final FileOpertionInfo fileOpertionInfo; private final JButton srcSelectdFileButton; private final JButton destSelectdFileButton; private boolean state;
/*     */   
/*     */   private FileDialog(Frame frame, String tipString, String srcFileString, String destString) {
/*  30 */     super(frame, tipString, true);
/*     */     
/*  32 */     this.fileOpertionInfo = new FileOpertionInfo();
/*     */     
/*  34 */     this.srcFileTextField = new JTextField("srcFileText", 30);
/*  35 */     this.destFileTextField = new JTextField("destText", 30);
/*  36 */     this.srcFileLabel = new JLabel("srcFile");
/*  37 */     this.destFileLabel = new JLabel("destFile");
/*     */     
/*  39 */     this.okButton = new JButton("ok");
/*  40 */     this.cancelButton = new JButton("cancel");
/*  41 */     this.srcSelectdFileButton = new JButton("select File");
/*  42 */     this.destSelectdFileButton = new JButton("select File");
/*  43 */     Dimension TextFieldDim = new Dimension(200, 23);
/*     */     
/*  45 */     GBC gbcLSrcFile = (new GBC(0, 0)).setInsets(5, -40, 0, 0);
/*  46 */     GBC gbcSrcFile = (new GBC(1, 0, 3, 1)).setInsets(5, 20, 0, 0);
/*  47 */     GBC gbcSrcSelectdFie = (new GBC(4, 0, 7, 1)).setInsets(5, 50, 0, 10);
/*  48 */     GBC gbcDestSelectdFie = (new GBC(4, 1, 7, 1)).setInsets(5, 50, 0, 10);
/*  49 */     GBC gbcLDestFile = (new GBC(0, 1)).setInsets(5, -40, 0, 0);
/*  50 */     GBC gbcDestFile = (new GBC(1, 1, 3, 1)).setInsets(5, 20, 0, 0);
/*  51 */     GBC gbcOkButton = (new GBC(0, 2, 2, 1)).setInsets(5, 20, 0, 0);
/*  52 */     GBC gbcCancelButton = (new GBC(2, 2, 1, 1)).setInsets(5, 20, 0, 0);
/*     */     
/*  54 */     this.srcFileTextField.setPreferredSize(TextFieldDim);
/*  55 */     this.destFileTextField.setPreferredSize(TextFieldDim);
/*     */     
/*  57 */     setLayout(new GridBagLayout());
/*     */     
/*  59 */     add(this.srcFileLabel, gbcLSrcFile);
/*  60 */     add(this.srcFileTextField, gbcSrcFile);
/*  61 */     add(this.srcSelectdFileButton, gbcSrcSelectdFie);
/*  62 */     add(this.destSelectdFileButton, gbcDestSelectdFie);
/*  63 */     add(this.destFileLabel, gbcLDestFile);
/*  64 */     add(this.destFileTextField, gbcDestFile);
/*  65 */     add(this.okButton, gbcOkButton);
/*  66 */     add(this.cancelButton, gbcCancelButton);
/*     */     
/*  68 */     automaticBindClick.bindJButtonClick(this, this);
/*     */     
/*  70 */     addWindowListener(new WindowListener()
/*     */         {
/*     */           public void windowOpened(WindowEvent paramWindowEvent) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void windowIconified(WindowEvent paramWindowEvent) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void windowDeiconified(WindowEvent paramWindowEvent) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void windowDeactivated(WindowEvent paramWindowEvent) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void windowClosing(WindowEvent paramWindowEvent) {
/*  98 */             FileDialog.this.cancelButtonClick((ActionEvent)null);
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void windowClosed(WindowEvent paramWindowEvent) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void windowActivated(WindowEvent paramWindowEvent) {}
/*     */         });
/* 114 */     this.srcFileTextField.setText(srcFileString);
/* 115 */     this.destFileTextField.setText(destString);
/*     */     
/* 117 */     functions.setWindowSize(this, 650, 180);
/* 118 */     setLocationRelativeTo(frame);
/* 119 */     setDefaultCloseOperation(2);
/* 120 */     EasyI18N.installObject(this);
/* 121 */     setVisible(true);
/*     */   }
/*     */   
/*     */   public FileOpertionInfo getResult() {
/* 125 */     return this.fileOpertionInfo;
/*     */   }
/*     */   
/*     */   private void okButtonClick(ActionEvent actionEvent) {
/* 129 */     this.fileOpertionInfo.setOpertionStatus(Boolean.valueOf(true));
/* 130 */     changeFileInfo();
/*     */   }
/*     */   
/*     */   private void cancelButtonClick(ActionEvent actionEvent) {
/* 134 */     this.fileOpertionInfo.setOpertionStatus(Boolean.valueOf(false));
/* 135 */     changeFileInfo();
/*     */   }
/*     */   private void changeFileInfo() {
/* 138 */     this.fileOpertionInfo.setSrcFileName(this.srcFileTextField.getText());
/* 139 */     this.fileOpertionInfo.setDestFileName(this.destFileTextField.getText());
/* 140 */     this.state = true;
/* 141 */     dispose();
/*     */   }
/*     */   private void srcSelectdFileButtonClick(ActionEvent actionEvent) {
/* 144 */     GFileChooser chooser = new GFileChooser();
/* 145 */     chooser.setFileSelectionMode(0);
/* 146 */     boolean flag = (0 == chooser.showDialog(new JLabel(), "选择"));
/* 147 */     File selectdFile = chooser.getSelectedFile();
/* 148 */     if (flag && selectdFile != null) {
/* 149 */       String fileString = selectdFile.getAbsolutePath();
/* 150 */       this.srcFileTextField.setText(fileString);
/*     */     } 
/*     */   }
/*     */   private void destSelectdFileButtonClick(ActionEvent actionEvent) {
/* 154 */     GFileChooser chooser = new GFileChooser();
/* 155 */     chooser.setFileSelectionMode(0);
/* 156 */     boolean flag = (0 == chooser.showDialog(new JLabel(), "选择"));
/* 157 */     File selectdFile = chooser.getSelectedFile();
/* 158 */     if (flag && selectdFile != null) {
/* 159 */       String fileString = selectdFile.getAbsolutePath();
/* 160 */       this.destFileTextField.setText(fileString);
/*     */     } 
/*     */   }
/*     */   public static FileOpertionInfo showFileOpertion(Frame frame, String title, String srcFileString, String destString) {
/* 164 */     return (new FileDialog(frame, title, srcFileString, destString)).getResult();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\cor\\ui\component\dialog\FileDialog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */