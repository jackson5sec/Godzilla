/*    */ package org.fife.rsta.ac.java;
/*    */ 
/*    */ import javax.swing.text.JTextComponent;
/*    */ import org.fife.ui.autocomplete.BasicCompletion;
/*    */ import org.fife.ui.autocomplete.Completion;
/*    */ import org.fife.ui.autocomplete.CompletionProvider;
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
/*    */ public abstract class AbstractJavaSourceCompletion
/*    */   extends BasicCompletion
/*    */   implements JavaSourceCompletion
/*    */ {
/*    */   public AbstractJavaSourceCompletion(CompletionProvider provider, String replacementText) {
/* 32 */     super(provider, replacementText);
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
/*    */ 
/*    */ 
/*    */   
/*    */   public int compareTo(Completion c2) {
/* 48 */     int rc = -1;
/*    */     
/* 50 */     if (c2 == this) {
/* 51 */       rc = 0;
/*    */     
/*    */     }
/* 54 */     else if (c2 != null) {
/* 55 */       rc = toString().compareToIgnoreCase(c2.toString());
/* 56 */       if (rc == 0) {
/* 57 */         String clazz1 = getClass().getName();
/* 58 */         clazz1 = clazz1.substring(clazz1.lastIndexOf('.'));
/* 59 */         String clazz2 = c2.getClass().getName();
/* 60 */         clazz2 = clazz2.substring(clazz2.lastIndexOf('.'));
/* 61 */         rc = clazz1.compareTo(clazz2);
/*    */       } 
/*    */     } 
/*    */     
/* 65 */     return rc;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getAlreadyEntered(JTextComponent comp) {
/* 72 */     String temp = getProvider().getAlreadyEnteredText(comp);
/* 73 */     int lastDot = temp.lastIndexOf('.');
/* 74 */     if (lastDot > -1) {
/* 75 */       temp = temp.substring(lastDot + 1);
/*    */     }
/* 77 */     return temp;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\AbstractJavaSourceCompletion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */