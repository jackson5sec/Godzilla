/*    */ package org.springframework.util.xml;
/*    */ 
/*    */ import javax.xml.transform.Transformer;
/*    */ import org.springframework.util.Assert;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class TransformerUtils
/*    */ {
/*    */   public static final int DEFAULT_INDENT_AMOUNT = 2;
/*    */   
/*    */   public static void enableIndenting(Transformer transformer) {
/* 50 */     enableIndenting(transformer, 2);
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
/*    */   public static void enableIndenting(Transformer transformer, int indentAmount) {
/* 63 */     Assert.notNull(transformer, "Transformer must not be null");
/* 64 */     if (indentAmount < 0) {
/* 65 */       throw new IllegalArgumentException("Invalid indent amount (must not be less than zero): " + indentAmount);
/*    */     }
/* 67 */     transformer.setOutputProperty("indent", "yes");
/*    */     
/*    */     try {
/* 70 */       transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", String.valueOf(indentAmount));
/*    */     }
/* 72 */     catch (IllegalArgumentException illegalArgumentException) {}
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void disableIndenting(Transformer transformer) {
/* 82 */     Assert.notNull(transformer, "Transformer must not be null");
/* 83 */     transformer.setOutputProperty("indent", "no");
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\xml\TransformerUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */