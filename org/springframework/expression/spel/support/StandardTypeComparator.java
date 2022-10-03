/*     */ package org.springframework.expression.spel.support;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import org.springframework.expression.TypeComparator;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.NumberUtils;
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
/*     */ public class StandardTypeComparator
/*     */   implements TypeComparator
/*     */ {
/*     */   public boolean canCompare(@Nullable Object left, @Nullable Object right) {
/*  41 */     if (left == null || right == null) {
/*  42 */       return true;
/*     */     }
/*  44 */     if (left instanceof Number && right instanceof Number) {
/*  45 */       return true;
/*     */     }
/*  47 */     if (left instanceof Comparable) {
/*  48 */       return true;
/*     */     }
/*  50 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compare(@Nullable Object left, @Nullable Object right) throws SpelEvaluationException {
/*  57 */     if (left == null) {
/*  58 */       return (right == null) ? 0 : -1;
/*     */     }
/*  60 */     if (right == null) {
/*  61 */       return 1;
/*     */     }
/*     */ 
/*     */     
/*  65 */     if (left instanceof Number && right instanceof Number) {
/*  66 */       Number leftNumber = (Number)left;
/*  67 */       Number rightNumber = (Number)right;
/*     */       
/*  69 */       if (leftNumber instanceof BigDecimal || rightNumber instanceof BigDecimal) {
/*  70 */         BigDecimal leftBigDecimal = (BigDecimal)NumberUtils.convertNumberToTargetClass(leftNumber, BigDecimal.class);
/*  71 */         BigDecimal rightBigDecimal = (BigDecimal)NumberUtils.convertNumberToTargetClass(rightNumber, BigDecimal.class);
/*  72 */         return leftBigDecimal.compareTo(rightBigDecimal);
/*     */       } 
/*  74 */       if (leftNumber instanceof Double || rightNumber instanceof Double) {
/*  75 */         return Double.compare(leftNumber.doubleValue(), rightNumber.doubleValue());
/*     */       }
/*  77 */       if (leftNumber instanceof Float || rightNumber instanceof Float) {
/*  78 */         return Float.compare(leftNumber.floatValue(), rightNumber.floatValue());
/*     */       }
/*  80 */       if (leftNumber instanceof BigInteger || rightNumber instanceof BigInteger) {
/*  81 */         BigInteger leftBigInteger = (BigInteger)NumberUtils.convertNumberToTargetClass(leftNumber, BigInteger.class);
/*  82 */         BigInteger rightBigInteger = (BigInteger)NumberUtils.convertNumberToTargetClass(rightNumber, BigInteger.class);
/*  83 */         return leftBigInteger.compareTo(rightBigInteger);
/*     */       } 
/*  85 */       if (leftNumber instanceof Long || rightNumber instanceof Long) {
/*  86 */         return Long.compare(leftNumber.longValue(), rightNumber.longValue());
/*     */       }
/*  88 */       if (leftNumber instanceof Integer || rightNumber instanceof Integer) {
/*  89 */         return Integer.compare(leftNumber.intValue(), rightNumber.intValue());
/*     */       }
/*  91 */       if (leftNumber instanceof Short || rightNumber instanceof Short) {
/*  92 */         return Short.compare(leftNumber.shortValue(), rightNumber.shortValue());
/*     */       }
/*  94 */       if (leftNumber instanceof Byte || rightNumber instanceof Byte) {
/*  95 */         return Byte.compare(leftNumber.byteValue(), rightNumber.byteValue());
/*     */       }
/*     */ 
/*     */       
/*  99 */       return Double.compare(leftNumber.doubleValue(), rightNumber.doubleValue());
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 104 */       if (left instanceof Comparable) {
/* 105 */         return ((Comparable<Object>)left).compareTo(right);
/*     */       }
/*     */     }
/* 108 */     catch (ClassCastException ex) {
/* 109 */       throw new SpelEvaluationException(ex, SpelMessage.NOT_COMPARABLE, new Object[] { left.getClass(), right.getClass() });
/*     */     } 
/*     */     
/* 112 */     throw new SpelEvaluationException(SpelMessage.NOT_COMPARABLE, new Object[] { left.getClass(), right.getClass() });
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\support\StandardTypeComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */