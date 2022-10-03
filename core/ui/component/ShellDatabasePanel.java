/*     */ package core.ui.component;
/*     */ import core.shell.ShellEntity;
/*     */ import core.ui.component.dialog.GFileChooser;
/*     */ import core.ui.component.dialog.GOptionPane;
/*     */ import core.ui.component.listener.ActionDblClick;
/*     */ import core.ui.component.model.DbInfo;
/*     */ import core.ui.config.DatabaseSql;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.datatransfer.StringSelection;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.io.File;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Vector;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JSplitPane;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.tree.DefaultMutableTreeNode;
/*     */ import util.Log;
/*     */ import util.UiFunction;
/*     */ import util.automaticBindClick;
/*     */ import util.functions;
/*     */ 
/*     */ @DisplayName(DisplayName = "数据库管理")
/*     */ public class ShellDatabasePanel extends JPanel {
/*  35 */   private static final String[] EXEC_TYPES = new String[] { "select", "update" };
/*  36 */   private static final String[] SQL_EXAMPLE = new String[] { "SELECT 1;", "SELECT COUNT(1) FROM tableName", "SELECT VERSION();" };
/*     */   
/*     */   private ShellEntity shellEntity;
/*     */   
/*     */   private JSplitPane splitPane;
/*     */   private JButton execButton;
/*     */   private JButton dbsetButton;
/*     */   private DataTree dblist;
/*     */   private DataView dataView;
/*     */   private RTextArea sqlCommand;
/*     */   private JScrollPane dblistpane;
/*     */   private JScrollPane datalistpane;
/*     */   private JScrollPane commandpane;
/*     */   private JComboBox<String> execTypeComboBox;
/*     */   private JComboBox<String> commonsql;
/*     */   private JLabel statusLabel;
/*     */   private JLabel execTypeLabel;
/*     */   private JLabel currentDbLabel;
/*     */   private JLabel sql_listLabel;
/*     */   private JTextField currentDbTextField;
/*     */   private DefaultMutableTreeNode databaseTreeNode;
/*     */   private Payload payload;
/*     */   private DbInfo dbInfo;
/*     */   private Encoding encoding;
/*     */   private JPopupMenu dataViewPopupMenu;
/*     */   private JPopupMenu dblistPopupMenu;
/*     */   
/*     */   public ShellDatabasePanel(ShellEntity shellEntity) {
/*  64 */     this.shellEntity = shellEntity;
/*  65 */     this.payload = this.shellEntity.getPayloadModule();
/*  66 */     this.encoding = shellEntity.getDbEncodingModule();
/*  67 */     this.dbInfo = new DbInfo(this.encoding);
/*  68 */     this.splitPane = new JSplitPane();
/*     */     
/*  70 */     this.databaseTreeNode = new DefaultMutableTreeNode("Database");
/*     */     
/*  72 */     this.splitPane.setOrientation(0);
/*     */ 
/*     */     
/*  75 */     this.statusLabel = new JLabel("state");
/*  76 */     this.execTypeLabel = new JLabel("Exec Type");
/*  77 */     this.currentDbLabel = new JLabel("CurrentDatabase");
/*  78 */     this.sql_listLabel = new JLabel("SQL Statement");
/*     */ 
/*     */ 
/*     */     
/*  82 */     this.currentDbTextField = new JTextField("", 10);
/*     */ 
/*     */     
/*  85 */     this.dblist = new DataTree("", this.databaseTreeNode);
/*  86 */     this.dblistpane = new JScrollPane(this.dblist);
/*  87 */     this.dblistpane.setPreferredSize(new Dimension(25, 0));
/*  88 */     this.dblist.setShowsRootHandles(true);
/*  89 */     this.dblist.setRootVisible(false);
/*  90 */     this.execTypeComboBox = new JComboBox<>(EXEC_TYPES);
/*     */     
/*  92 */     this.dataView = new DataView(null, null, -1, -1);
/*  93 */     this.datalistpane = new JScrollPane(this.dataView);
/*     */     
/*  95 */     this.dataView.setAutoResizeMode(0);
/*  96 */     this.datalistpane.setPreferredSize(new Dimension(0, 0));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 101 */     this.sqlCommand = new RTextArea();
/* 102 */     this.commandpane = new JScrollPane((Component)this.sqlCommand);
/* 103 */     this.sqlCommand.setText("");
/* 104 */     UiFunction.setSyntaxEditingStyle(this.sqlCommand, "user.sql");
/*     */ 
/*     */ 
/*     */     
/* 108 */     this.commonsql = new JComboBox<>(SQL_EXAMPLE);
/* 109 */     this.commonsql.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e)
/*     */           {
/* 113 */             ShellDatabasePanel.this.sqlCommand.setText((String)ShellDatabasePanel.this.commonsql.getSelectedItem());
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 118 */     this.dbsetButton = new JButton("DbInfoConfig");
/*     */ 
/*     */ 
/*     */     
/* 122 */     this.dataViewPopupMenu = new JPopupMenu();
/* 123 */     JMenuItem copyselectItem = new JMenuItem("复制选中");
/* 124 */     copyselectItem.setActionCommand("copySelected");
/* 125 */     JMenuItem copyselectedLineItem = new JMenuItem("复制选中行");
/* 126 */     copyselectedLineItem.setActionCommand("copyselectedLine");
/* 127 */     JMenuItem exportAllItem = new JMenuItem("导出");
/* 128 */     exportAllItem.setActionCommand("exportData");
/* 129 */     this.dataViewPopupMenu.add(copyselectItem);
/* 130 */     this.dataViewPopupMenu.add(copyselectedLineItem);
/* 131 */     this.dataViewPopupMenu.add(exportAllItem);
/* 132 */     this.dataView.setRightClickMenu(this.dataViewPopupMenu);
/* 133 */     automaticBindClick.bindMenuItemClick(this.dataViewPopupMenu, null, this);
/*     */     
/* 135 */     this.dblistPopupMenu = new JPopupMenu();
/* 136 */     JMenuItem countTableItem = new JMenuItem("Count");
/* 137 */     countTableItem.setActionCommand("countTable");
/* 138 */     this.dblistPopupMenu.add(countTableItem);
/* 139 */     automaticBindClick.bindMenuItemClick(this.dblistPopupMenu, null, this);
/* 140 */     this.dblist.setChildPopupMenu(this.dblistPopupMenu);
/*     */     
/* 142 */     this.execButton = new JButton("Exec SQL");
/*     */     
/* 144 */     setLayout(new GridBagLayout());
/* 145 */     GBC gbcleft = (new GBC(0, 0, 2, 4)).setFill(3).setWeight(0.0D, 1.0D).setIpad(200, 0);
/*     */ 
/*     */     
/* 148 */     GBC gbcright1 = (new GBC(2, 0, 7, 1)).setFill(1).setWeight(1.0D, 0.7D).setInsets(0, 7, 0, 0);
/*     */ 
/*     */     
/* 151 */     GBC gbcright2_1 = (new GBC(2, 1, 1, 1)).setFill(0).setInsets(0, 5, 0, 0);
/* 152 */     GBC gbcright2_2 = (new GBC(3, 1, 1, 1)).setFill(2).setWeight(1.0D, 0.0D);
/* 153 */     GBC gbcright2_3 = (new GBC(4, 1, 1, 1)).setFill(0);
/* 154 */     GBC gbcright2_4 = (new GBC(5, 1, 1, 1)).setFill(2).setWeight(1.0D, 0.0D);
/* 155 */     GBC gbcright2_5 = (new GBC(6, 1, 1, 1)).setFill(0);
/* 156 */     GBC gbcright2_6 = (new GBC(7, 1, 1, 1)).setFill(2).setWeight(1.0D, 0.0D);
/* 157 */     GBC gbcright2_7 = (new GBC(8, 1, 1, 1)).setFill(0);
/*     */ 
/*     */     
/* 160 */     GBC gbcright3 = (new GBC(2, 2, 8, 1)).setFill(1).setWeight(1.0D, 0.3D).setInsets(0, 5, 0, 0);
/*     */ 
/*     */     
/* 163 */     GBC gbcright4_1 = (new GBC(2, 3, 7, 1)).setFill(2).setWeight(1.0D, 0.0D).setInsets(0, 7, 0, 0);
/*     */     
/* 165 */     GBC gbcstatus = (new GBC(0, 4, 9, 1)).setFill(2).setWeight(1.0D, 0.0D);
/*     */ 
/*     */     
/* 168 */     add(this.dblistpane, gbcleft);
/*     */     
/* 170 */     add(this.datalistpane, gbcright1);
/*     */ 
/*     */     
/* 173 */     add(this.execTypeLabel, gbcright2_1);
/* 174 */     add(this.execTypeComboBox, gbcright2_2);
/*     */ 
/*     */ 
/*     */     
/* 178 */     add(this.currentDbLabel, gbcright2_3);
/* 179 */     add(this.currentDbTextField, gbcright2_4);
/*     */ 
/*     */     
/* 182 */     add(this.sql_listLabel, gbcright2_5);
/* 183 */     add(this.commonsql, gbcright2_6);
/*     */ 
/*     */     
/* 186 */     add(this.commandpane, gbcright3);
/*     */ 
/*     */     
/* 189 */     add(this.dbsetButton, gbcright2_7);
/*     */ 
/*     */     
/* 192 */     add(this.execButton, gbcright4_1);
/* 193 */     add(this.statusLabel, gbcstatus);
/*     */     
/* 195 */     automaticBindClick.bindJButtonClick(this, this);
/*     */     
/* 197 */     this.dblist.setActionDbclick(new ActionDblClick()
/*     */         {
/*     */           
/*     */           public void dblClick(MouseEvent e)
/*     */           {
/* 202 */             ShellDatabasePanel.this.fileDataTreeDbClick(e);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void fileDataTreeDbClick(MouseEvent e) {
/* 211 */     String[] s = this.dblist.GetSelectFile().split("/");
/* 212 */     if (s.length == 1) {
/* 213 */       fillDbListByTable(s[0]);
/* 214 */     } else if (s.length == 2) {
/* 215 */       fillDataviewByDT(s[0], s[1]);
/*     */     } 
/*     */   }
/*     */   private void dbsetButtonClick(ActionEvent actionEvent) {
/* 219 */     String lastConfig = this.dbInfo.toString();
/* 220 */     new DatabaseSetting(this.shellEntity, this.dbInfo);
/* 221 */     String newConfig = this.dbInfo.toString();
/* 222 */     this.encoding = this.dbInfo.getCharset();
/* 223 */     if (!lastConfig.equals(newConfig))
/* 224 */       (new Thread(new Runnable()
/*     */           {
/*     */             public void run()
/*     */             {
/* 228 */               SwingUtilities.invokeLater(new Runnable()
/*     */                   {
/*     */                     public void run() {
/* 231 */                       ShellDatabasePanel.this.fillDbListByDatabase();
/*     */                     }
/*     */                   });
/*     */             }
/* 235 */           })).start(); 
/*     */   }
/*     */   
/*     */   private void fillDbListByDatabase() {
/* 239 */     this.currentDbTextField.setText("");
/* 240 */     this.dblist.removeAll();
/* 241 */     String sqlString = (String)DatabaseSql.sqlMap.get(String.format("%s-getAllDatabase", new Object[] { this.dbInfo.getDbType().toLowerCase() }));
/* 242 */     if (sqlString != null) {
/* 243 */       this.sqlCommand.setText(sqlString);
/* 244 */       String result = execSql("select", sqlString);
/* 245 */       if (showData(result) && 
/* 246 */         this.dataView.getModel().getColumnCount() == 1) {
/*     */         
/* 248 */         Vector<Vector> rows = this.dataView.getModel().getDataVector();
/*     */ 
/*     */         
/* 251 */         for (int i = 0; i < rows.size(); i++) {
/* 252 */           Vector<E> row = rows.get(i);
/* 253 */           this.dblist.AddNote(row.get(0).toString());
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/* 258 */       Log.error(String.format("Fill Database Fail! NO SQL %s", new Object[] { this.dbInfo.getDbType() }));
/*     */     } 
/*     */   }
/*     */   private void fillDbListByTable(String databaseName) {
/* 262 */     this.currentDbTextField.setText(databaseName);
/* 263 */     String sqlString = (String)DatabaseSql.sqlMap.get(String.format("%s-getTableByDatabase", new Object[] { this.dbInfo.getDbType().toLowerCase() }));
/* 264 */     if (sqlString != null) {
/* 265 */       sqlString = formatSql(sqlString, (String)null);
/* 266 */       this.sqlCommand.setText(sqlString);
/* 267 */       String result = execSql("select", sqlString);
/* 268 */       if (showData(result) && 
/* 269 */         this.dataView.getModel().getColumnCount() == 1) {
/* 270 */         Vector<Vector> rows = this.dataView.getModel().getDataVector();
/*     */         
/* 272 */         for (int i = 0; i < rows.size(); i++) {
/* 273 */           Vector row = rows.get(i);
/* 274 */           this.dblist.AddNote(String.format("%s/%s", new Object[] { databaseName, row.get(0) }));
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/* 279 */       Log.error(String.format("Fill Table Fail! NO SQL %s", new Object[] { this.dbInfo.getDbType() }));
/*     */     } 
/*     */   }
/*     */   private void fillDataviewByDT(String databaseName, String tableName) {
/* 283 */     this.currentDbTextField.setText(databaseName);
/* 284 */     String sqlString = (String)DatabaseSql.sqlMap.get(String.format("%s-getTableDataByDT", new Object[] { this.dbInfo.getDbType().toLowerCase() }));
/* 285 */     if (sqlString != null) {
/* 286 */       sqlString = formatSql(sqlString, tableName);
/* 287 */       this.sqlCommand.setText(sqlString);
/* 288 */       String result = execSql("select", sqlString);
/* 289 */       showData(result);
/*     */     } else {
/* 291 */       Log.error(String.format("Fill TableData Fail! NO SQL %s", new Object[] { this.dbInfo.getDbType() }));
/*     */     } 
/*     */   }
/*     */   private String formatSql(String sql, String tableName) {
/* 295 */     String databaseName = this.currentDbTextField.getText().trim();
/* 296 */     return sql.replace("{tableName}", (tableName == null) ? "null" : tableName).replace("{databaseName}", databaseName);
/*     */   }
/*     */   private void execButtonClick(ActionEvent actionEvent) {
/* 299 */     String execSql = this.sqlCommand.getText();
/* 300 */     String execType = (String)this.execTypeComboBox.getSelectedItem();
/* 301 */     if (execSql != null && execSql.trim().length() > 0) {
/* 302 */       String result = execSql(execType, execSql);
/* 303 */       showData(result);
/*     */     } else {
/* 305 */       GOptionPane.showMessageDialog(null, "SQL语句是空的", "提示", 2);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String execSql(String execType, String execSql) {
/* 310 */     Map<Object, Object> options = new HashMap<>();
/* 311 */     options.put("dbCharset", this.encoding.getCharsetString());
/* 312 */     String currentDb = this.currentDbTextField.getText().trim();
/* 313 */     if (currentDb.length() > 0) {
/* 314 */       options.put("currentDb", currentDb);
/*     */     }
/* 316 */     return this.payload.execSql(this.dbInfo.getDbType(), this.dbInfo.getDbHost(), this.dbInfo.getDbPort(), this.dbInfo.getDbUserName(), this.dbInfo.getDbPassword(), execType, options, execSql);
/*     */   }
/*     */   
/*     */   public boolean showData(String data) {
/* 320 */     boolean state = false;
/* 321 */     if (data != null) {
/* 322 */       String[] datas = data.split("\n");
/* 323 */       Vector<String> columns = this.dataView.getColumnVector();
/*     */       
/* 325 */       Vector<Vector<String>> rowsVector = new Vector<>();
/* 326 */       if (datas[0].equals("ok")) {
/* 327 */         if (datas.length > 1) {
/* 328 */           columns.clear();
/* 329 */           formatSqlResult(datas[1], columns);
/* 330 */           for (int i = 2; i < datas.length; i++) {
/* 331 */             Vector<String> row = new Vector<>();
/* 332 */             formatSqlResult(datas[i], row);
/* 333 */             rowsVector.add(row);
/*     */           } 
/* 335 */           showData(rowsVector);
/* 336 */           state = true;
/*     */         } else {
/* 338 */           Vector<String> row = new Vector<>();
/* 339 */           row.add("");
/* 340 */           rowsVector.add(row);
/* 341 */           this.dataView.getModel().setColumnIdentifiers(row);
/* 342 */           showData(rowsVector);
/*     */         } 
/*     */       } else {
/* 345 */         GOptionPane.showMessageDialog(null, data, "提示", 2);
/* 346 */         Log.error(data);
/*     */       } 
/*     */     } else {
/* 349 */       Log.error("exec SQL Result Is Null");
/*     */     } 
/* 351 */     return state;
/*     */   }
/*     */   public void showData(Vector<Vector<String>> rowsVector) {
/* 354 */     this.dataView.AddRows(rowsVector);
/* 355 */     this.dataView.getModel().fireTableDataChanged();
/*     */   }
/*     */   public void formatSqlResult(String row, Vector<String> destVector) {
/* 358 */     String[] line = row.split("\t");
/* 359 */     for (int i = 0; i < line.length; i++)
/*     */     {
/* 361 */       destVector.add(this.encoding.Decoding(functions.base64Decode(line[i])));
/*     */     }
/*     */   }
/*     */   
/*     */   private void copySelectedMenuItemClick(ActionEvent e) {
/* 366 */     int columnIndex = this.dataView.getSelectedColumn();
/* 367 */     if (columnIndex != -1) {
/* 368 */       Object o = this.dataView.getValueAt(this.dataView.getSelectedRow(), this.dataView.getSelectedColumn());
/* 369 */       if (o != null) {
/* 370 */         String value = (String)o;
/* 371 */         Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(value), null);
/* 372 */         GOptionPane.showMessageDialog(null, "复制成功", "提示", 1);
/*     */       } else {
/* 374 */         GOptionPane.showMessageDialog(null, "选中列是空的", "提示", 2);
/*     */       } 
/*     */     } else {
/* 377 */       GOptionPane.showMessageDialog(null, "未选中列", "提示", 2);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void copyselectedLineMenuItemClick(ActionEvent e) {
/* 382 */     int columnIndex = this.dataView.getSelectedColumn();
/* 383 */     if (columnIndex != -1) {
/* 384 */       String[] o = this.dataView.GetSelectRow1();
/* 385 */       if (o != null) {
/* 386 */         String value = Arrays.toString((Object[])o);
/* 387 */         this.dataView.GetSelectRow1();
/* 388 */         Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(value), null);
/* 389 */         GOptionPane.showMessageDialog(null, "复制成功", "提示", 1);
/*     */       } else {
/* 391 */         GOptionPane.showMessageDialog(null, "选中列是空的", "提示", 2);
/*     */       } 
/*     */     } else {
/* 394 */       GOptionPane.showMessageDialog(null, "未选中列", "提示", 2);
/*     */     } 
/*     */   }
/*     */   private void countTableMenuItemClick(ActionEvent e) {
/* 398 */     String[] s = this.dblist.GetSelectFile().split("/");
/* 399 */     if (s.length == 2) {
/* 400 */       this.currentDbTextField.setText(s[0]);
/* 401 */       String sqlString = (String)DatabaseSql.sqlMap.get(String.format("%s-getCountByDT", new Object[] { this.dbInfo.getDbType().toLowerCase() }));
/* 402 */       if (sqlString != null) {
/* 403 */         sqlString = formatSql(sqlString, s[1]);
/* 404 */         this.sqlCommand.setText(sqlString);
/* 405 */         String result = execSql("select", sqlString);
/* 406 */         showData(result);
/*     */       } else {
/* 408 */         Log.error(String.format("Fill TableData Fail! NO SQL %s", new Object[] { this.dbInfo.getDbType() }));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private void exportDataMenuItemClick(ActionEvent e) {
/* 413 */     GFileChooser chooser = new GFileChooser();
/* 414 */     chooser.setFileSelectionMode(0);
/* 415 */     chooser.setFileFilter(new FileNameExtensionFilter("*.csv", new String[] { "csv" }));
/* 416 */     boolean flag = (0 == chooser.showDialog(new JLabel(), "选择"));
/* 417 */     File selectdFile = chooser.getSelectedFile();
/* 418 */     if (flag && selectdFile != null) {
/* 419 */       String fileString = selectdFile.getAbsolutePath();
/* 420 */       if (!fileString.endsWith(".csv")) {
/* 421 */         fileString = fileString = fileString + ".csv";
/*     */       }
/* 423 */       if (functions.saveDataViewToCsv(this.dataView.getColumnVector(), this.dataView.getModel().getDataVector(), fileString)) {
/* 424 */         GOptionPane.showMessageDialog(null, "导出成功", "提示", 1);
/*     */       } else {
/* 426 */         GOptionPane.showMessageDialog(null, "导出失败", "提示", 1);
/*     */       } 
/*     */     } else {
/* 429 */       Log.log("用户取消选择......", new Object[0]);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\cor\\ui\component\ShellDatabasePanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */