/*     */ package core.ui.component;
/*     */ import core.ui.component.dialog.GFileChooser;
/*     */ import core.ui.component.dialog.GOptionPane;
/*     */ import core.ui.component.listener.ActionDblClick;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.datatransfer.StringSelection;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.io.File;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.Arrays;
/*     */ import java.util.Vector;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.MenuElement;
/*     */ import javax.swing.RowFilter;
/*     */ import javax.swing.table.DefaultTableCellRenderer;
/*     */ import javax.swing.table.DefaultTableModel;
/*     */ import javax.swing.table.JTableHeader;
/*     */ import javax.swing.table.TableModel;
/*     */ import javax.swing.table.TableRowSorter;
/*     */ import util.Log;
/*     */ import util.functions;
/*     */ 
/*     */ public class DataView extends JTable {
/*     */   private static final long serialVersionUID = -8531006713898868252L;
/*     */   private JPopupMenu rightClickMenu;
/*  32 */   private String lastFiter = "*"; private RightClickEvent rightClickEvent; private final int imgColumn; private TableRowSorter sorter; private Vector columnNameVector;
/*     */   private DefaultTableModel model;
/*     */   
/*     */   private void initJtableConfig() {
/*  36 */     this.rightClickEvent = new RightClickEvent(this.rightClickMenu, this);
/*  37 */     addMouseListener(this.rightClickEvent);
/*  38 */     setSelectionMode(0);
/*  39 */     setAutoCreateRowSorter(true);
/*  40 */     setRowHeight(25);
/*     */     
/*  42 */     this.rightClickMenu = new JPopupMenu();
/*  43 */     JMenuItem copyselectItem = new JMenuItem("复制选中");
/*  44 */     copyselectItem.setActionCommand("copySelected");
/*  45 */     JMenuItem copyselectedLineItem = new JMenuItem("复制选中行");
/*  46 */     copyselectedLineItem.setActionCommand("copyselectedLine");
/*  47 */     JMenuItem exportAllItem = new JMenuItem("导出");
/*  48 */     exportAllItem.setActionCommand("exportData");
/*  49 */     this.rightClickMenu.add(copyselectItem);
/*  50 */     this.rightClickMenu.add(copyselectedLineItem);
/*  51 */     this.rightClickMenu.add(exportAllItem);
/*  52 */     setRightClickMenu(this.rightClickMenu);
/*  53 */     this.sorter = new TableRowSorter<>(this.dataModel);
/*  54 */     setRowSorter(this.sorter);
/*  55 */     automaticBindClick.bindMenuItemClick(this.rightClickMenu, null, this);
/*  56 */     addActionForKey("ctrl pressed F", new AbstractAction()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e)
/*     */           {
/*  60 */             DataView.this.ctrlPassF(e);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public DataView() {
/*  67 */     this(new Vector(), new Vector(), -1, -1);
/*     */   }
/*     */   public DataView(Vector rowData, Vector columnNames, int imgColumn, int imgMaxWidth) {
/*  70 */     super(rowData, columnNames);
/*  71 */     if (columnNames == null) {
/*  72 */       columnNames = new Vector();
/*     */     }
/*  74 */     if (rowData == null) {
/*  75 */       rowData = new Vector();
/*     */     }
/*  77 */     getModel().setDataVector(rowData, columnNames);
/*  78 */     this.columnNameVector = columnNames;
/*  79 */     this.imgColumn = imgColumn;
/*  80 */     if (imgColumn >= 0) {
/*  81 */       getColumnModel().getColumn(0).setMaxWidth(imgMaxWidth);
/*     */     }
/*  83 */     initJtableConfig();
/*  84 */     EasyI18N.installObject(this);
/*     */   }
/*     */   
/*     */   public void ctrlPassF(ActionEvent e) {
/*  88 */     Object filterObject = GOptionPane.showInputDialog(null, "input filter", "input filter", 3, null, null, this.lastFiter);
/*  89 */     if (filterObject != null) {
/*  90 */       String fiter = filterObject.toString();
/*  91 */       this.lastFiter = fiter;
/*  92 */       if (fiter.isEmpty()) {
/*  93 */         this.sorter.setRowFilter(null);
/*     */       } else {
/*  95 */         this.sorter.setRowFilter(new RowFilter<Object, Object>()
/*     */             {
/*     */               public boolean include(RowFilter.Entry entry) {
/*  98 */                 int count = entry.getValueCount();
/*  99 */                 for (int i = 0; i < count; i++) {
/* 100 */                   if (functions.isMatch(entry.getStringValue(i), DataView.this.lastFiter, false)) {
/* 101 */                     return true;
/*     */                   }
/*     */                 } 
/* 104 */                 return false;
/*     */               }
/*     */             });
/*     */       } 
/*     */     } else {
/* 109 */       Log.log("用户取消选择", new Object[0]);
/*     */     } 
/*     */   }
/*     */   public void setActionDblClick(ActionDblClick actionDblClick) {
/* 113 */     if (this.rightClickEvent != null)
/* 114 */       this.rightClickEvent.setActionListener(actionDblClick); 
/*     */   }
/*     */   
/*     */   public JPopupMenu getRightClickMenu() {
/* 118 */     return this.rightClickMenu;
/*     */   }
/*     */   public void addActionForKeyStroke(KeyStroke keyStroke, Action action) {
/* 121 */     getActionMap().put(keyStroke.toString(), action);
/* 122 */     getInputMap().put(keyStroke, keyStroke.toString());
/*     */   }
/*     */   public void addActionForKey(String keyString, Action action) {
/* 125 */     addActionForKeyStroke(KeyStroke.getKeyStroke(keyString), action);
/*     */   }
/*     */   public void RemoveALL() {
/* 128 */     DefaultTableModel defaultTableModel = getModel();
/* 129 */     while (defaultTableModel.getRowCount() > 0) {
/* 130 */       defaultTableModel.removeRow(0);
/*     */     }
/* 132 */     updateUI();
/*     */   }
/*     */   
/*     */   public TableRowSorter getSorter() {
/* 136 */     return this.sorter;
/*     */   }
/*     */   
/*     */   public void setSorter(TableRowSorter sorter) {
/* 140 */     this.sorter = sorter;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class getColumnClass(int column) {
/* 145 */     return (column == this.imgColumn) ? Icon.class : Object.class;
/*     */   }
/*     */   
/*     */   public Vector GetSelectRow() {
/* 149 */     int select_row_id = getSelectedRow();
/* 150 */     if (select_row_id != -1) {
/* 151 */       int column_num = getColumnCount();
/* 152 */       Vector<Object> vector = new Vector();
/* 153 */       for (int i = 0; i < column_num; i++) {
/* 154 */         vector.add(getValueAt(select_row_id, i));
/*     */       }
/* 156 */       return vector;
/*     */     } 
/* 158 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector getColumnVector() {
/* 163 */     return this.columnNameVector;
/*     */   }
/*     */   public String[] GetSelectRow1() {
/* 166 */     int select_row_id = getSelectedRow();
/* 167 */     if (select_row_id != -1) {
/* 168 */       int column_num = getColumnCount();
/* 169 */       String[] select_row_columns = new String[column_num];
/* 170 */       for (int i = 0; i < column_num; i++) {
/* 171 */         Object value = getValueAt(select_row_id, i);
/* 172 */         if (value instanceof String) {
/* 173 */           select_row_columns[i] = (String)value;
/* 174 */         } else if (value != null) {
/*     */           try {
/* 176 */             select_row_columns[i] = value.toString();
/* 177 */           } catch (Exception e) {
/* 178 */             select_row_columns[i] = "null";
/* 179 */             Log.error(e);
/*     */           } 
/*     */         } else {
/* 182 */           select_row_columns[i] = "null";
/*     */         } 
/*     */       } 
/* 185 */       return select_row_columns;
/*     */     } 
/* 187 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultTableModel getModel() {
/* 194 */     if (this.dataModel != null) {
/* 195 */       return (DefaultTableModel)this.dataModel;
/*     */     }
/* 197 */     return null;
/*     */   }
/*     */   
/*     */   public synchronized void AddRow(Object object) {
/* 201 */     Class<?> class1 = object.getClass();
/* 202 */     Field[] fields = class1.getFields();
/* 203 */     String field_name = null;
/* 204 */     String field_value = null;
/* 205 */     DefaultTableModel tableModel = getModel();
/* 206 */     Vector<String> rowVector = new Vector(tableModel.getColumnCount());
/* 207 */     String[] columns = new String[tableModel.getColumnCount()];
/* 208 */     for (int i = 0; i < tableModel.getColumnCount(); i++) {
/* 209 */       columns[i] = tableModel.getColumnName(i).toUpperCase();
/* 210 */       rowVector.add("NULL");
/*     */     } 
/* 212 */     for (Field field : fields) {
/* 213 */       field_name = field.getName();
/* 214 */       int find_id = Arrays.binarySearch((Object[])columns, field_name.substring(2).toUpperCase());
/* 215 */       if (field_name.startsWith("s_") && find_id != -1) {
/*     */         try {
/* 217 */           if (field.get(object) instanceof String) {
/* 218 */             field_value = (String)field.get(object);
/*     */           } else {
/* 220 */             field_value = "NULL";
/*     */           } 
/* 222 */         } catch (Exception e) {
/* 223 */           field_value = "NULL";
/*     */         } 
/* 225 */         rowVector.set(find_id, field_value);
/*     */       } 
/*     */     } 
/* 228 */     tableModel.addRow(rowVector);
/*     */   }
/*     */   public synchronized void AddRow(Vector one_row) {
/* 231 */     DefaultTableModel tableModel = getModel();
/* 232 */     tableModel.addRow(one_row);
/*     */   }
/*     */   public synchronized Vector<Vector> getDataVector() {
/* 235 */     this.sorter.setRowFilter(null);
/* 236 */     return getModel().getDataVector();
/*     */   }
/*     */   public synchronized void AddRows(Vector rows) {
/* 239 */     this.sorter.setRowFilter(null);
/* 240 */     DefaultTableModel tableModel = getModel();
/* 241 */     Vector columnVector = getColumnVector();
/* 242 */     tableModel.setDataVector(rows, columnVector);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void SetRow(int row_id, Object object) {
/* 248 */     Class<?> class1 = object.getClass();
/* 249 */     Field[] fields = class1.getFields();
/* 250 */     String field_name = null;
/* 251 */     String field_value = null;
/* 252 */     DefaultTableModel tableModel = getModel();
/* 253 */     Vector<String> rowVector = tableModel.getDataVector().get(row_id);
/* 254 */     String[] columns = new String[tableModel.getColumnCount()];
/* 255 */     for (int i = 0; i < tableModel.getColumnCount(); i++) {
/* 256 */       columns[i] = tableModel.getColumnName(i).toUpperCase();
/*     */     }
/* 258 */     for (Field field : fields) {
/* 259 */       field_name = field.getName();
/* 260 */       int find_id = Arrays.binarySearch((Object[])columns, field_name.substring(2).toUpperCase());
/* 261 */       if (field_name.startsWith("s_") && find_id != -1) {
/*     */         try {
/* 263 */           if (field.get(object) instanceof String) {
/* 264 */             field_value = (String)field.get(object);
/*     */           } else {
/* 266 */             field_value = "NULL";
/*     */           } 
/* 268 */         } catch (Exception e) {
/* 269 */           field_value = "NULL";
/*     */         } 
/* 271 */         rowVector.set(find_id, field_value);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public void setRightClickMenu(JPopupMenu rightClickMenu) {
/* 276 */     setRightClickMenu(rightClickMenu, false);
/*     */   }
/*     */   public void setRightClickMenu(JPopupMenu rightClickMenu, boolean append) {
/* 279 */     if (append) {
/* 280 */       for (MenuElement c : this.rightClickMenu.getSubElements()) {
/* 281 */         rightClickMenu.add(c.getComponent());
/*     */       }
/*     */     }
/* 284 */     this.rightClickMenu = rightClickMenu;
/* 285 */     this.rightClickEvent.setRightClickMenu(rightClickMenu);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JTableHeader getTableHeader() {
/* 291 */     JTableHeader tableHeader = super.getTableHeader();
/* 292 */     tableHeader.setReorderingAllowed(false);
/* 293 */     DefaultTableCellRenderer hr = (DefaultTableCellRenderer)tableHeader.getDefaultRenderer();
/* 294 */     hr.setHorizontalAlignment(0);
/* 295 */     return tableHeader;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addColumn(Object column) {
/* 300 */     getModel().addColumn(column);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TableCellRenderer getDefaultRenderer(Class<?> columnClass) {
/* 307 */     DefaultTableCellRenderer cr = (DefaultTableCellRenderer)super.getDefaultRenderer(columnClass);
/* 308 */     cr.setHorizontalAlignment(0);
/* 309 */     return cr;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCellEditable(int paramInt1, int paramInt2) {
/* 314 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private void copySelectedMenuItemClick(ActionEvent e) {
/* 319 */     int columnIndex = getSelectedColumn();
/* 320 */     if (columnIndex != -1) {
/* 321 */       Object o = getValueAt(getSelectedRow(), getSelectedColumn());
/* 322 */       if (o != null) {
/* 323 */         String value = (String)o;
/* 324 */         Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(value), null);
/* 325 */         GOptionPane.showMessageDialog(null, "复制成功", "提示", 1);
/*     */       } else {
/* 327 */         GOptionPane.showMessageDialog(null, "选中列是空的", "提示", 2);
/*     */       } 
/*     */     } else {
/* 330 */       GOptionPane.showMessageDialog(null, "未选中列", "提示", 2);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void copyselectedLineMenuItemClick(ActionEvent e) {
/* 335 */     int columnIndex = getSelectedColumn();
/* 336 */     if (columnIndex != -1) {
/* 337 */       String[] o = GetSelectRow1();
/* 338 */       if (o != null) {
/* 339 */         String value = Arrays.toString((Object[])o);
/* 340 */         GetSelectRow1();
/* 341 */         Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(value), null);
/* 342 */         GOptionPane.showMessageDialog(null, "复制成功", "提示", 1);
/*     */       } else {
/* 344 */         GOptionPane.showMessageDialog(null, "选中列是空的", "提示", 2);
/*     */       } 
/*     */     } else {
/* 347 */       GOptionPane.showMessageDialog(null, "未选中列", "提示", 2);
/*     */     } 
/*     */   }
/*     */   private void exportDataMenuItemClick(ActionEvent e) {
/* 351 */     GFileChooser chooser = new GFileChooser();
/* 352 */     chooser.setFileSelectionMode(0);
/* 353 */     chooser.setFileFilter(new FileNameExtensionFilter("*.csv", new String[] { "csv" }));
/* 354 */     boolean flag = (0 == chooser.showDialog(null, "选择"));
/* 355 */     File selectdFile = chooser.getSelectedFile();
/* 356 */     if (flag && selectdFile != null) {
/* 357 */       String fileString = selectdFile.getAbsolutePath();
/* 358 */       if (!fileString.endsWith(".csv")) {
/* 359 */         fileString = fileString = fileString + ".csv";
/*     */       }
/* 361 */       if (functions.saveDataViewToCsv(getColumnVector(), getModel().getDataVector(), fileString)) {
/* 362 */         GOptionPane.showMessageDialog(null, "导出成功", "提示", 1);
/*     */       } else {
/* 364 */         GOptionPane.showMessageDialog(null, "导出失败", "提示", 1);
/*     */       } 
/*     */     } else {
/* 367 */       Log.log("用户取消选择......", new Object[0]);
/*     */     } 
/*     */   }
/*     */   private class RightClickEvent extends MouseAdapter { private JPopupMenu rightClickMenu;
/*     */     private final DataView dataView;
/*     */     private ActionDblClick actionDblClick;
/*     */     
/*     */     public RightClickEvent(JPopupMenu rightClickMenu, DataView jtable) {
/* 375 */       this.rightClickMenu = rightClickMenu;
/* 376 */       this.dataView = jtable;
/*     */     }
/*     */     public void setRightClickMenu(JPopupMenu rightClickMenu) {
/* 379 */       this.rightClickMenu = rightClickMenu;
/*     */     }
/*     */     public void setActionListener(ActionDblClick event) {
/* 382 */       this.actionDblClick = event;
/*     */     }
/*     */     
/*     */     public void mouseClicked(MouseEvent mouseEvent) {
/* 386 */       if (mouseEvent.getButton() == 3) {
/* 387 */         if (this.rightClickMenu != null) {
/* 388 */           int i = this.dataView.rowAtPoint(mouseEvent.getPoint());
/* 389 */           if (i != -1) {
/* 390 */             this.rightClickMenu.show(this.dataView, mouseEvent.getX(), mouseEvent.getY());
/*     */             
/* 392 */             this.dataView.addRowSelectionInterval(i, i);
/*     */           } 
/*     */         } 
/* 395 */       } else if (mouseEvent.getClickCount() == 2 && 
/* 396 */         this.actionDblClick != null) {
/* 397 */         this.actionDblClick.dblClick(mouseEvent);
/*     */       } 
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\cor\\ui\component\DataView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */