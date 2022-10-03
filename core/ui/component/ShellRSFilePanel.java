/*     */ package core.ui.component;
/*     */ 
/*     */ import core.ApplicationContext;
/*     */ import core.EasyI18N;
/*     */ import core.imp.Payload;
/*     */ import core.shell.ShellEntity;
/*     */ import core.ui.component.dialog.GOptionPane;
/*     */ import core.ui.component.frame.EditFileFrame;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.CardLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JSplitPane;
/*     */ import javax.swing.JTextField;
/*     */ import org.fife.ui.rtextarea.RTextArea;
/*     */ import org.fife.ui.rtextarea.RTextScrollPane;
/*     */ import util.Log;
/*     */ import util.UiFunction;
/*     */ import util.automaticBindClick;
/*     */ import util.functions;
/*     */ 
/*     */ public class ShellRSFilePanel
/*     */   extends JPanel
/*     */ {
/*     */   private JComboBox<String> encodingComboBox;
/*     */   private JButton saveButton;
/*     */   private JButton refreshButton;
/*     */   private JButton backButton;
/*     */   private JTextField readFileTextField;
/*     */   private RTextArea fileDataTextArea;
/*     */   private JPanel parentPanel;
/*     */   private Payload payload;
/*  37 */   private CardLayout cardLayout = null;
/*     */   private JSplitPane splitPane;
/*     */   private ShellEntity shellContext;
/*     */   private byte[] fileData;
/*  41 */   private String containerName = null;
/*     */   
/*     */   private JPanel topPanel;
/*     */   private RTextScrollPane scrollPane;
/*     */   
/*     */   public ShellRSFilePanel(ShellEntity shellContext, JPanel parentPanel, String containerName) {
/*  47 */     super(new BorderLayout());
/*     */     
/*  49 */     this.parentPanel = parentPanel;
/*  50 */     if (parentPanel != null) {
/*  51 */       this.cardLayout = (CardLayout)parentPanel.getLayout();
/*  52 */       this.containerName = containerName;
/*     */     } 
/*  54 */     this.payload = shellContext.getPayloadModule();
/*  55 */     this.shellContext = shellContext;
/*     */     
/*  57 */     this.topPanel = new JPanel();
/*  58 */     this.encodingComboBox = new JComboBox<>(ApplicationContext.getAllEncodingTypes());
/*  59 */     this.saveButton = new JButton("保存");
/*  60 */     this.refreshButton = new JButton("刷新");
/*  61 */     this.backButton = new JButton("返回");
/*  62 */     this.splitPane = new JSplitPane();
/*  63 */     this.readFileTextField = new JTextField(80);
/*  64 */     this.fileDataTextArea = new RTextArea();
/*  65 */     this.scrollPane = new RTextScrollPane((RTextArea)this.fileDataTextArea, true);
/*  66 */     this.scrollPane.setIconRowHeaderEnabled(true);
/*  67 */     this.scrollPane.getGutter().setBookmarkingEnabled(true);
/*     */     
/*  69 */     this.topPanel.add(this.readFileTextField, (new GBC(1, 1)).setFill(2).setWeight(1.0D, 0.0D));
/*  70 */     this.topPanel.add(this.encodingComboBox);
/*  71 */     this.topPanel.add(this.saveButton);
/*  72 */     this.topPanel.add(this.refreshButton);
/*  73 */     this.topPanel.add(this.backButton);
/*     */     
/*  75 */     this.splitPane.setOrientation(0);
/*  76 */     this.splitPane.setTopComponent(this.topPanel);
/*  77 */     this.splitPane.setBottomComponent((Component)this.scrollPane);
/*     */ 
/*     */ 
/*     */     
/*  81 */     this.encodingComboBox.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent paramActionEvent)
/*     */           {
/*  85 */             if (ShellRSFilePanel.this.fileData != null) {
/*  86 */               ShellRSFilePanel.this.encodingTypeString = (String)ShellRSFilePanel.this.encodingComboBox.getSelectedItem();
/*  87 */               ShellRSFilePanel.this.refreshData();
/*     */             } 
/*     */           }
/*     */         });
/*     */ 
/*     */     
/*  93 */     automaticBindClick.bindJButtonClick(this, this);
/*     */     
/*  95 */     this.encodingTypeString = (String)this.encodingComboBox.getSelectedItem();
/*     */     
/*  97 */     add(this.splitPane);
/*  98 */     EasyI18N.installObject(this);
/*     */   }
/*     */   private String encodingTypeString; private String currentFile;
/*     */   
/*     */   public void rsFile(String file) {
/* 103 */     if (this.payload.isAlive() || this.shellContext.isUseCache()) {
/* 104 */       this.currentFile = file;
/* 105 */       this.readFileTextField.setText(file);
/* 106 */       this.fileData = this.payload.downloadFile(file);
/* 107 */       UiFunction.setSyntaxEditingStyle(this.fileDataTextArea, file);
/* 108 */       refreshData();
/*     */     } else {
/* 110 */       this.readFileTextField.setText(this.currentFile);
/* 111 */       GOptionPane.showMessageDialog(this, "刷新失败 有效载荷已经销毁", "提示", 2);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void refreshData() {
/*     */     try {
/* 117 */       this.fileDataTextArea.setText(new String(this.fileData, this.encodingTypeString));
/* 118 */     } catch (Exception e) {
/* 119 */       this.fileDataTextArea.setText(new String(this.fileData));
/* 120 */       Log.error(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void saveButtonClick(ActionEvent e) {
/* 125 */     if (this.payload.isAlive()) {
/* 126 */       String fileString = this.readFileTextField.getText();
/* 127 */       boolean uploadState = this.payload.uploadFile(fileString, functions.stringToByteArray(this.fileDataTextArea.getText(), this.encodingTypeString));
/* 128 */       if (uploadState) {
/* 129 */         GOptionPane.showMessageDialog(this, "保存成功", "提示", 1);
/*     */       } else {
/* 131 */         GOptionPane.showMessageDialog(this, "保存失败", "提示", 2);
/*     */       } 
/*     */     } else {
/* 134 */       GOptionPane.showMessageDialog(this, "保存失败 有效载荷已经销毁", "提示", 2);
/*     */     } 
/*     */   }
/*     */   public void refreshButtonClick(ActionEvent e) {
/* 138 */     rsFile(this.readFileTextField.getText());
/*     */   }
/*     */   public void backButtonClick(ActionEvent e) {
/* 141 */     if (this.cardLayout != null) {
/* 142 */       this.fileData = null;
/* 143 */       this.fileDataTextArea.setText("");
/* 144 */       this.readFileTextField.setText("");
/* 145 */       this.cardLayout.show(this.parentPanel, this.containerName);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFile() {
/* 151 */     return this.currentFile;
/*     */   }
/*     */   public String getShellId() {
/* 154 */     return this.shellContext.getId();
/*     */   }
/*     */   
/*     */   public void openThisToEditFileFrame() {
/* 158 */     EditFileFrame.OpenNewEdit(this);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\cor\\ui\component\ShellRSFilePanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */