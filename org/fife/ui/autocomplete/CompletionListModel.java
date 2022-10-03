/*    */ package org.fife.ui.autocomplete;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import javax.swing.AbstractListModel;
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
/*    */ class CompletionListModel
/*    */   extends AbstractListModel<Completion>
/*    */ {
/* 38 */   private List<Completion> delegate = new ArrayList<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void clear() {
/* 49 */     int end = this.delegate.size() - 1;
/* 50 */     this.delegate.clear();
/* 51 */     if (end >= 0) {
/* 52 */       fireIntervalRemoved(this, 0, end);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Completion getElementAt(int index) {
/* 59 */     return this.delegate.get(index);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getSize() {
/* 65 */     return this.delegate.size();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setContents(Collection<Completion> contents) {
/* 75 */     clear();
/* 76 */     int count = contents.size();
/* 77 */     if (count > 0) {
/* 78 */       this.delegate.addAll(contents);
/* 79 */       fireIntervalAdded(this, 0, count - 1);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\autocomplete\CompletionListModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */