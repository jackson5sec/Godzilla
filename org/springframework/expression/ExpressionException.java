/*     */ package org.springframework.expression;
/*     */ 
/*     */ import org.springframework.lang.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExpressionException
/*     */   extends RuntimeException
/*     */ {
/*     */   @Nullable
/*     */   protected final String expressionString;
/*     */   protected int position;
/*     */   
/*     */   public ExpressionException(String message) {
/*  42 */     super(message);
/*  43 */     this.expressionString = null;
/*  44 */     this.position = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExpressionException(String message, Throwable cause) {
/*  53 */     super(message, cause);
/*  54 */     this.expressionString = null;
/*  55 */     this.position = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExpressionException(@Nullable String expressionString, String message) {
/*  64 */     super(message);
/*  65 */     this.expressionString = expressionString;
/*  66 */     this.position = -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExpressionException(@Nullable String expressionString, int position, String message) {
/*  76 */     super(message);
/*  77 */     this.expressionString = expressionString;
/*  78 */     this.position = position;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExpressionException(int position, String message) {
/*  87 */     super(message);
/*  88 */     this.expressionString = null;
/*  89 */     this.position = position;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExpressionException(int position, String message, Throwable cause) {
/*  99 */     super(message, cause);
/* 100 */     this.expressionString = null;
/* 101 */     this.position = position;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final String getExpressionString() {
/* 110 */     return this.expressionString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getPosition() {
/* 117 */     return this.position;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage() {
/* 128 */     return toDetailedString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toDetailedString() {
/* 136 */     if (this.expressionString != null) {
/* 137 */       StringBuilder output = new StringBuilder();
/* 138 */       output.append("Expression [");
/* 139 */       output.append(this.expressionString);
/* 140 */       output.append(']');
/* 141 */       if (this.position >= 0) {
/* 142 */         output.append(" @");
/* 143 */         output.append(this.position);
/*     */       } 
/* 145 */       output.append(": ");
/* 146 */       output.append(getSimpleMessage());
/* 147 */       return output.toString();
/*     */     } 
/*     */     
/* 150 */     return getSimpleMessage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSimpleMessage() {
/* 160 */     return super.getMessage();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\ExpressionException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */