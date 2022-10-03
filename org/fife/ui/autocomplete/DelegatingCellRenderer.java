/*    */ package org.fife.ui.autocomplete;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import javax.swing.DefaultListCellRenderer;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.JList;
/*    */ import javax.swing.ListCellRenderer;
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
/*    */ class DelegatingCellRenderer
/*    */   extends DefaultListCellRenderer
/*    */ {
/*    */   private ListCellRenderer<Object> fallback;
/*    */   
/*    */   public ListCellRenderer<Object> getFallbackCellRenderer() {
/* 44 */     return this.fallback;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean selected, boolean hasFocus) {
/* 51 */     Completion c = (Completion)value;
/* 52 */     CompletionProvider p = c.getProvider();
/* 53 */     ListCellRenderer<Object> r = p.getListCellRenderer();
/* 54 */     if (r != null) {
/* 55 */       return r.getListCellRendererComponent(list, value, index, selected, hasFocus);
/*    */     }
/*    */     
/* 58 */     if (this.fallback == null) {
/* 59 */       return super.getListCellRendererComponent(list, value, index, selected, hasFocus);
/*    */     }
/*    */     
/* 62 */     return this.fallback.getListCellRendererComponent(list, value, index, selected, hasFocus);
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
/*    */   
/*    */   public void setFallbackCellRenderer(ListCellRenderer<Object> fallback) {
/* 75 */     this.fallback = fallback;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void updateUI() {
/* 81 */     super.updateUI();
/* 82 */     if (this.fallback instanceof JComponent && this.fallback != this)
/* 83 */       ((JComponent)this.fallback).updateUI(); 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\autocomplete\DelegatingCellRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */