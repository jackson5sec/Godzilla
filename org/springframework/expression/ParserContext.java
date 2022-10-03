/*    */ package org.springframework.expression;
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
/*    */ public interface ParserContext
/*    */ {
/* 61 */   public static final ParserContext TEMPLATE_EXPRESSION = new ParserContext()
/*    */     {
/*    */       public boolean isTemplate()
/*    */       {
/* 65 */         return true;
/*    */       }
/*    */ 
/*    */       
/*    */       public String getExpressionPrefix() {
/* 70 */         return "#{";
/*    */       }
/*    */ 
/*    */       
/*    */       public String getExpressionSuffix() {
/* 75 */         return "}";
/*    */       }
/*    */     };
/*    */   
/*    */   boolean isTemplate();
/*    */   
/*    */   String getExpressionPrefix();
/*    */   
/*    */   String getExpressionSuffix();
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\ParserContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */