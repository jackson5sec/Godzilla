/*    */ package org.springframework.expression.common;
/*    */ 
/*    */ import org.springframework.expression.ParserContext;
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
/*    */ public class TemplateParserContext
/*    */   implements ParserContext
/*    */ {
/*    */   private final String expressionPrefix;
/*    */   private final String expressionSuffix;
/*    */   
/*    */   public TemplateParserContext() {
/* 39 */     this("#{", "}");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TemplateParserContext(String expressionPrefix, String expressionSuffix) {
/* 48 */     this.expressionPrefix = expressionPrefix;
/* 49 */     this.expressionSuffix = expressionSuffix;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public final boolean isTemplate() {
/* 55 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getExpressionPrefix() {
/* 60 */     return this.expressionPrefix;
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getExpressionSuffix() {
/* 65 */     return this.expressionSuffix;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\common\TemplateParserContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */