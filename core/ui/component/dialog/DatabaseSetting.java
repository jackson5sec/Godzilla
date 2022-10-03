/*     */ package core.ui.component.dialog;
/*     */ 
/*     */ import core.EasyI18N;
/*     */ import core.Encoding;
/*     */ import core.shell.ShellEntity;
/*     */ import core.ui.component.GBC;
/*     */ import core.ui.component.model.DbInfo;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Frame;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.event.ActionEvent;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DatabaseSetting
/*     */   extends JDialog
/*     */ {
/*     */   private JComboBox<String> dbTypeComboBox;
/*     */   private JTextField dbHostTextField;
/*     */   private JTextField dbPortTextField;
/*     */   private JTextField dbUserNameTextField;
/*     */   private JTextField dbPasswordTextField;
/*     */   private JComboBox<String> dbCharsetComboBox;
/*     */   private JLabel dbTypeLabel;
/*     */   private JLabel dbHostLabel;
/*     */   private JLabel dbPortLabel;
/*     */   private JLabel dbUserNameLabel;
/*     */   private JLabel dbPasswordLabel;
/*     */   private JLabel dbCharsetLabel;
/*     */   private JButton updateButton;
/*  42 */   private Dimension TextFieldDim = new Dimension(200, 23);
/*  43 */   private Dimension labelDim = new Dimension(150, 23);
/*     */   
/*     */   public DatabaseSetting(ShellEntity shellEntity, DbInfo dbInfo) {
/*  46 */     super((Frame)shellEntity.getFrame(), "DbInfo Setting", true);
/*  47 */     String[] databaseTypeArray = shellEntity.getPayloadModule().getAllDatabaseType();
/*  48 */     Container c = getContentPane();
/*  49 */     setLayout(new GridBagLayout());
/*  50 */     this.dbInfo = dbInfo;
/*  51 */     GBC gbcLDbType = (new GBC(0, 0)).setInsets(5, -40, 0, 0);
/*  52 */     GBC gbcDbType = (new GBC(1, 0, 3, 1)).setInsets(5, 20, 0, 0);
/*  53 */     GBC gbcLDbHost = (new GBC(0, 1)).setInsets(5, -40, 0, 0);
/*  54 */     GBC gbcDbHost = (new GBC(1, 1, 3, 1)).setInsets(5, 20, 0, 0);
/*  55 */     GBC gbcLDbPort = (new GBC(0, 2)).setInsets(5, -40, 0, 0);
/*  56 */     GBC gbcDbPort = (new GBC(1, 2, 3, 1)).setInsets(5, 20, 0, 0);
/*  57 */     GBC gbcLDbUserName = (new GBC(0, 3)).setInsets(5, -40, 0, 0);
/*  58 */     GBC gbcDbUserName = (new GBC(1, 3, 3, 1)).setInsets(5, 20, 0, 0);
/*  59 */     GBC gbcLDbPassword = (new GBC(0, 4)).setInsets(5, -40, 0, 0);
/*  60 */     GBC gbcDbPassword = (new GBC(1, 4, 3, 1)).setInsets(5, 20, 0, 0);
/*  61 */     GBC gbcLDbCharset = (new GBC(0, 5)).setInsets(5, -40, 0, 0);
/*  62 */     GBC gbcDbCharset = (new GBC(1, 5, 3, 1)).setInsets(5, 20, 0, 0);
/*  63 */     GBC gbcUpdate = (new GBC(1, 6, 4, 1)).setInsets(5, 20, 0, 0);
/*  64 */     this.updateButton = new JButton("Update Db Info");
/*     */     
/*  66 */     this.dbTypeLabel = new JLabel("数据库类型");
/*  67 */     this.dbHostLabel = new JLabel("数据库主机");
/*  68 */     this.dbPortLabel = new JLabel("数据库端口");
/*  69 */     this.dbUserNameLabel = new JLabel("数据库用户名");
/*  70 */     this.dbPasswordLabel = new JLabel("数据库密码");
/*  71 */     this.dbCharsetLabel = new JLabel("数据库编码");
/*     */     
/*  73 */     this.dbTypeLabel.setPreferredSize(this.labelDim);
/*  74 */     this.dbHostLabel.setPreferredSize(this.labelDim);
/*  75 */     this.dbPortLabel.setPreferredSize(this.labelDim);
/*  76 */     this.dbUserNameLabel.setPreferredSize(this.labelDim);
/*  77 */     this.dbPasswordLabel.setPreferredSize(this.labelDim);
/*  78 */     this.dbCharsetLabel.setPreferredSize(this.labelDim);
/*     */ 
/*     */     
/*  81 */     this.dbHostTextField = new JTextField(dbInfo.getDbHost());
/*  82 */     this.dbPortTextField = new JTextField(Integer.toString(dbInfo.getDbPort()));
/*  83 */     this.dbUserNameTextField = new JTextField(dbInfo.getDbUserName());
/*  84 */     this.dbPasswordTextField = new JTextField(dbInfo.getDbPassword());
/*  85 */     this.dbTypeComboBox = new JComboBox<>(databaseTypeArray);
/*  86 */     this.dbCharsetComboBox = new JComboBox<>(Encoding.getAllEncodingTypes());
/*  87 */     this.dbCharsetComboBox.setEditable(false);
/*  88 */     this.dbCharsetComboBox.setSelectedItem(dbInfo.getCharset().toString());
/*  89 */     this.dbTypeComboBox.setEditable(false);
/*  90 */     this.dbTypeComboBox.setSelectedItem(dbInfo.getDbType());
/*     */     
/*  92 */     this.dbTypeComboBox.setPreferredSize(this.TextFieldDim);
/*  93 */     this.dbHostTextField.setPreferredSize(this.TextFieldDim);
/*  94 */     this.dbPortTextField.setPreferredSize(this.TextFieldDim);
/*  95 */     this.dbUserNameTextField.setPreferredSize(this.TextFieldDim);
/*  96 */     this.dbPasswordTextField.setPreferredSize(this.TextFieldDim);
/*  97 */     this.dbCharsetComboBox.setPreferredSize(this.TextFieldDim);
/*     */ 
/*     */     
/* 100 */     this.updateButton.setPreferredSize(this.TextFieldDim);
/*     */ 
/*     */     
/* 103 */     c.add(this.dbTypeLabel, gbcLDbType);
/* 104 */     c.add(this.dbTypeComboBox, gbcDbType);
/* 105 */     c.add(this.dbHostLabel, gbcLDbHost);
/* 106 */     c.add(this.dbHostTextField, gbcDbHost);
/* 107 */     c.add(this.dbPortLabel, gbcLDbPort);
/* 108 */     c.add(this.dbPortTextField, gbcDbPort);
/* 109 */     c.add(this.dbUserNameLabel, gbcLDbUserName);
/* 110 */     c.add(this.dbUserNameTextField, gbcDbUserName);
/* 111 */     c.add(this.dbPasswordLabel, gbcLDbPassword);
/* 112 */     c.add(this.dbPasswordTextField, gbcDbPassword);
/* 113 */     c.add(this.dbCharsetLabel, gbcLDbCharset);
/* 114 */     c.add(this.dbCharsetComboBox, gbcDbCharset);
/* 115 */     c.add(this.updateButton, gbcUpdate);
/*     */ 
/*     */     
/* 118 */     automaticBindClick.bindJButtonClick(this, this);
/*     */     
/* 120 */     functions.setWindowSize(this, 460, 270);
/* 121 */     setLocationRelativeTo(null);
/* 122 */     setDefaultCloseOperation(2);
/* 123 */     EasyI18N.installObject(this);
/* 124 */     setVisible(true);
/*     */   }
/*     */   private DbInfo dbInfo;
/*     */   private void updateButtonClick(ActionEvent actionEvent) {
/*     */     try {
/* 129 */       String dbType = (String)this.dbTypeComboBox.getSelectedItem();
/* 130 */       if (dbType.trim().length() > 0) {
/* 131 */         this.dbInfo.setDbHost(this.dbHostTextField.getText());
/* 132 */         this.dbInfo.setDbPort(Integer.parseInt(this.dbPortTextField.getText()));
/* 133 */         this.dbInfo.setDbUserName(this.dbUserNameTextField.getText());
/* 134 */         this.dbInfo.setDbPassword(this.dbPasswordTextField.getText());
/* 135 */         this.dbInfo.setDbType(dbType);
/* 136 */         this.dbInfo.setCharset(this.dbCharsetComboBox.getSelectedItem().toString());
/* 137 */         GOptionPane.showMessageDialog(this, "success", "提示", 1);
/*     */       } else {
/* 139 */         GOptionPane.showMessageDialog(this, "no selected DbType", "提示", 2);
/*     */       } 
/* 141 */     } catch (Exception e) {
/* 142 */       GOptionPane.showMessageDialog(this, e.getMessage(), "提示", 2);
/* 143 */       Log.error(e);
/*     */     } 
/* 145 */     dispose();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\cor\\ui\component\dialog\DatabaseSetting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */