/*    */ package org.fife.rsta.ac.xml;
/*    */ 
/*    */ import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
/*    */ import org.fife.ui.rsyntaxtextarea.Token;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ValidationConfigSniffer
/*    */ {
/*    */   public ValidationConfig sniff(RSyntaxDocument doc) {
/* 12 */     ValidationConfig config = null;
/*    */ 
/*    */     
/* 15 */     for (Token token : doc) {
/* 16 */       switch (token.getType()) {
/*    */         case 30:
/*    */         case 26:
/*    */           break;
/*    */       } 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     } 
/* 27 */     return config;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\xml\ValidationConfigSniffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */