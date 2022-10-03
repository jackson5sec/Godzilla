/*    */ package org.fife.rsta.ui.search;
/*    */ import java.awt.Component;
/*    */ import javax.swing.JOptionPane;
/*    */ import javax.swing.JTextArea;
/*    */ import javax.swing.UIManager;
/*    */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
/*    */ import org.fife.ui.rtextarea.RTextArea;
/*    */ import org.fife.ui.rtextarea.SearchContext;
/*    */ import org.fife.ui.rtextarea.SearchEngine;
/*    */ import org.fife.ui.rtextarea.SearchResult;
/*    */ 
/*    */ public class SearchListenerImpl implements SearchListener {
/*    */   public SearchListenerImpl(RSyntaxTextArea textArea) {
/* 14 */     this.textArea = textArea;
/*    */   }
/*    */   private RSyntaxTextArea textArea;
/*    */   public void searchEvent(SearchEvent e) {
/*    */     SearchResult result;
/* 19 */     SearchEvent.Type type = e.getType();
/* 20 */     SearchContext context = e.getSearchContext();
/*    */ 
/*    */     
/* 23 */     switch (type) {
/*    */       
/*    */       default:
/* 26 */         result = SearchEngine.markAll((RTextArea)this.textArea, context);
/*    */         break;
/*    */       case FIND:
/* 29 */         result = SearchEngine.find((JTextArea)this.textArea, context);
/* 30 */         if (!result.wasFound() || result.isWrapped()) {
/* 31 */           UIManager.getLookAndFeel().provideErrorFeedback((Component)this.textArea);
/*    */         }
/*    */         break;
/*    */       case REPLACE:
/* 35 */         result = SearchEngine.replace((RTextArea)this.textArea, context);
/* 36 */         if (!result.wasFound() || result.isWrapped()) {
/* 37 */           UIManager.getLookAndFeel().provideErrorFeedback((Component)this.textArea);
/*    */         }
/*    */         break;
/*    */       case REPLACE_ALL:
/* 41 */         result = SearchEngine.replaceAll((RTextArea)this.textArea, context);
/* 42 */         JOptionPane.showMessageDialog(null, result.getCount() + " occurrences replaced.");
/*    */         break;
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 48 */     if (result.wasFound()) {
/* 49 */       String text = "Text found; occurrences marked: " + result.getMarkedCount();
/*    */     }
/* 51 */     else if (type == SearchEvent.Type.MARK_ALL) {
/* 52 */       if (result.getMarkedCount() > 0) {
/* 53 */         String text = "Occurrences marked: " + result.getMarkedCount();
/*    */       } else {
/*    */         
/* 56 */         String text = "";
/*    */       } 
/*    */     } else {
/*    */       
/* 60 */       String text = "Text not found";
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSelectedText() {
/* 68 */     return this.textArea.getSelectedText();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rst\\ui\search\SearchListenerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */