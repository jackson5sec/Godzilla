/*    */ package org.mozilla.javascript.tools.debugger.treetable;
/*    */ 
/*    */ import java.util.EventObject;
/*    */ import javax.swing.CellEditor;
/*    */ import javax.swing.event.CellEditorListener;
/*    */ import javax.swing.event.ChangeEvent;
/*    */ import javax.swing.event.EventListenerList;
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
/*    */ public class AbstractCellEditor
/*    */   implements CellEditor
/*    */ {
/* 40 */   protected EventListenerList listenerList = new EventListenerList();
/*    */   
/* 42 */   public Object getCellEditorValue() { return null; }
/* 43 */   public boolean isCellEditable(EventObject e) { return true; }
/* 44 */   public boolean shouldSelectCell(EventObject anEvent) { return false; } public boolean stopCellEditing() {
/* 45 */     return true;
/*    */   }
/*    */   public void cancelCellEditing() {}
/*    */   public void addCellEditorListener(CellEditorListener l) {
/* 49 */     this.listenerList.add(CellEditorListener.class, l);
/*    */   }
/*    */   
/*    */   public void removeCellEditorListener(CellEditorListener l) {
/* 53 */     this.listenerList.remove(CellEditorListener.class, l);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void fireEditingStopped() {
/* 63 */     Object[] listeners = this.listenerList.getListenerList();
/*    */ 
/*    */     
/* 66 */     for (int i = listeners.length - 2; i >= 0; i -= 2) {
/* 67 */       if (listeners[i] == CellEditorListener.class) {
/* 68 */         ((CellEditorListener)listeners[i + 1]).editingStopped(new ChangeEvent(this));
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void fireEditingCanceled() {
/* 80 */     Object[] listeners = this.listenerList.getListenerList();
/*    */ 
/*    */     
/* 83 */     for (int i = listeners.length - 2; i >= 0; i -= 2) {
/* 84 */       if (listeners[i] == CellEditorListener.class)
/* 85 */         ((CellEditorListener)listeners[i + 1]).editingCanceled(new ChangeEvent(this)); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\tools\debugger\treetable\AbstractCellEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */