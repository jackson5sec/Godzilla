/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.support.BooleanTypedValue;
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
/*     */ public class OpGE
/*     */   extends Operator
/*     */ {
/*     */   public OpGE(int startPos, int endPos, SpelNodeImpl... operands) {
/*  40 */     super(">=", startPos, endPos, operands);
/*  41 */     this.exitTypeDescriptor = "Z";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BooleanTypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*  47 */     Object left = getLeftOperand().getValueInternal(state).getValue();
/*  48 */     Object right = getRightOperand().getValueInternal(state).getValue();
/*     */     
/*  50 */     this.leftActualDescriptor = CodeFlow.toDescriptorFromObject(left);
/*  51 */     this.rightActualDescriptor = CodeFlow.toDescriptorFromObject(right);
/*     */     
/*  53 */     if (left instanceof Number && right instanceof Number) {
/*  54 */       Number leftNumber = (Number)left;
/*  55 */       Number rightNumber = (Number)right;
/*     */       
/*  57 */       if (leftNumber instanceof BigDecimal || rightNumber instanceof BigDecimal) {
/*  58 */         BigDecimal leftBigDecimal = (BigDecimal)NumberUtils.convertNumberToTargetClass(leftNumber, BigDecimal.class);
/*  59 */         BigDecimal rightBigDecimal = (BigDecimal)NumberUtils.convertNumberToTargetClass(rightNumber, BigDecimal.class);
/*  60 */         return BooleanTypedValue.forValue((leftBigDecimal.compareTo(rightBigDecimal) >= 0));
/*     */       } 
/*  62 */       if (leftNumber instanceof Double || rightNumber instanceof Double) {
/*  63 */         return BooleanTypedValue.forValue((leftNumber.doubleValue() >= rightNumber.doubleValue()));
/*     */       }
/*  65 */       if (leftNumber instanceof Float || rightNumber instanceof Float) {
/*  66 */         return BooleanTypedValue.forValue((leftNumber.floatValue() >= rightNumber.floatValue()));
/*     */       }
/*  68 */       if (leftNumber instanceof BigInteger || rightNumber instanceof BigInteger) {
/*  69 */         BigInteger leftBigInteger = (BigInteger)NumberUtils.convertNumberToTargetClass(leftNumber, BigInteger.class);
/*  70 */         BigInteger rightBigInteger = (BigInteger)NumberUtils.convertNumberToTargetClass(rightNumber, BigInteger.class);
/*  71 */         return BooleanTypedValue.forValue((leftBigInteger.compareTo(rightBigInteger) >= 0));
/*     */       } 
/*  73 */       if (leftNumber instanceof Long || rightNumber instanceof Long) {
/*  74 */         return BooleanTypedValue.forValue((leftNumber.longValue() >= rightNumber.longValue()));
/*     */       }
/*  76 */       if (leftNumber instanceof Integer || rightNumber instanceof Integer) {
/*  77 */         return BooleanTypedValue.forValue((leftNumber.intValue() >= rightNumber.intValue()));
/*     */       }
/*  79 */       if (leftNumber instanceof Short || rightNumber instanceof Short) {
/*  80 */         return BooleanTypedValue.forValue((leftNumber.shortValue() >= rightNumber.shortValue()));
/*     */       }
/*  82 */       if (leftNumber instanceof Byte || rightNumber instanceof Byte) {
/*  83 */         return BooleanTypedValue.forValue((leftNumber.byteValue() >= rightNumber.byteValue()));
/*     */       }
/*     */ 
/*     */       
/*  87 */       return BooleanTypedValue.forValue((leftNumber.doubleValue() >= rightNumber.doubleValue()));
/*     */     } 
/*     */ 
/*     */     
/*  91 */     return BooleanTypedValue.forValue((state.getTypeComparator().compare(left, right) >= 0));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/*  96 */     return isCompilableOperatorUsingNumerics();
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 101 */     generateComparisonCode(mv, cf, 155, 161);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\OpGE.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */