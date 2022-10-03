/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.InternalParseException;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
/*     */ import org.springframework.expression.spel.SpelParseException;
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
/*     */ public abstract class Literal
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   @Nullable
/*     */   private final String originalValue;
/*     */   
/*     */   public Literal(@Nullable String originalValue, int startPos, int endPos) {
/*  40 */     super(startPos, endPos, new SpelNodeImpl[0]);
/*  41 */     this.originalValue = originalValue;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final String getOriginalValue() {
/*  47 */     return this.originalValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public final TypedValue getValueInternal(ExpressionState state) throws SpelEvaluationException {
/*  52 */     return getLiteralValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  57 */     return String.valueOf(getLiteralValue().getValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/*  62 */     return toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract TypedValue getLiteralValue();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Literal getIntLiteral(String numberToken, int startPos, int endPos, int radix) {
/*     */     try {
/*  79 */       int value = Integer.parseInt(numberToken, radix);
/*  80 */       return new IntLiteral(numberToken, startPos, endPos, value);
/*     */     }
/*  82 */     catch (NumberFormatException ex) {
/*  83 */       throw new InternalParseException(new SpelParseException(startPos, ex, SpelMessage.NOT_AN_INTEGER, new Object[] { numberToken }));
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Literal getLongLiteral(String numberToken, int startPos, int endPos, int radix) {
/*     */     try {
/*  89 */       long value = Long.parseLong(numberToken, radix);
/*  90 */       return new LongLiteral(numberToken, startPos, endPos, value);
/*     */     }
/*  92 */     catch (NumberFormatException ex) {
/*  93 */       throw new InternalParseException(new SpelParseException(startPos, ex, SpelMessage.NOT_A_LONG, new Object[] { numberToken }));
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Literal getRealLiteral(String numberToken, int startPos, int endPos, boolean isFloat) {
/*     */     try {
/*  99 */       if (isFloat) {
/* 100 */         float f = Float.parseFloat(numberToken);
/* 101 */         return new FloatLiteral(numberToken, startPos, endPos, f);
/*     */       } 
/*     */       
/* 104 */       double value = Double.parseDouble(numberToken);
/* 105 */       return new RealLiteral(numberToken, startPos, endPos, value);
/*     */     
/*     */     }
/* 108 */     catch (NumberFormatException ex) {
/* 109 */       throw new InternalParseException(new SpelParseException(startPos, ex, SpelMessage.NOT_A_REAL, new Object[] { numberToken }));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\Literal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */