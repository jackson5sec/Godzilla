/*    */ package org.apache.log4j.lf5.viewer;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import javax.swing.JTable;
/*    */ import javax.swing.table.DefaultTableCellRenderer;
/*    */ import org.apache.log4j.lf5.LogLevel;
/*    */ import org.apache.log4j.lf5.LogRecord;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LogTableRowRenderer
/*    */   extends DefaultTableCellRenderer
/*    */ {
/*    */   private static final long serialVersionUID = -3951639953706443213L;
/*    */   protected boolean _highlightFatal = true;
/* 48 */   protected Color _color = new Color(230, 230, 230);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
/* 69 */     if (row % 2 == 0) {
/* 70 */       setBackground(this._color);
/*    */     } else {
/* 72 */       setBackground(Color.white);
/*    */     } 
/*    */     
/* 75 */     FilteredLogTableModel model = (FilteredLogTableModel)table.getModel();
/* 76 */     LogRecord record = model.getFilteredRecord(row);
/*    */     
/* 78 */     setForeground(getLogLevelColor(record.getLevel()));
/*    */     
/* 80 */     return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Color getLogLevelColor(LogLevel level) {
/* 92 */     return (Color)LogLevel.getLogLevelColorMap().get(level);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\lf5\viewer\LogTableRowRenderer.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */