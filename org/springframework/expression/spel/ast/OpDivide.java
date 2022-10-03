/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.math.RoundingMode;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.Operation;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class OpDivide
/*     */   extends Operator
/*     */ {
/*     */   public OpDivide(int startPos, int endPos, SpelNodeImpl... operands) {
/*  43 */     super("/", startPos, endPos, operands);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*  49 */     Object leftOperand = getLeftOperand().getValueInternal(state).getValue();
/*  50 */     Object rightOperand = getRightOperand().getValueInternal(state).getValue();
/*     */     
/*  52 */     if (leftOperand instanceof Number && rightOperand instanceof Number) {
/*  53 */       Number leftNumber = (Number)leftOperand;
/*  54 */       Number rightNumber = (Number)rightOperand;
/*     */       
/*  56 */       if (leftNumber instanceof BigDecimal || rightNumber instanceof BigDecimal) {
/*  57 */         BigDecimal leftBigDecimal = (BigDecimal)NumberUtils.convertNumberToTargetClass(leftNumber, BigDecimal.class);
/*  58 */         BigDecimal rightBigDecimal = (BigDecimal)NumberUtils.convertNumberToTargetClass(rightNumber, BigDecimal.class);
/*  59 */         int scale = Math.max(leftBigDecimal.scale(), rightBigDecimal.scale());
/*  60 */         return new TypedValue(leftBigDecimal.divide(rightBigDecimal, scale, RoundingMode.HALF_EVEN));
/*     */       } 
/*  62 */       if (leftNumber instanceof Double || rightNumber instanceof Double) {
/*  63 */         this.exitTypeDescriptor = "D";
/*  64 */         return new TypedValue(Double.valueOf(leftNumber.doubleValue() / rightNumber.doubleValue()));
/*     */       } 
/*  66 */       if (leftNumber instanceof Float || rightNumber instanceof Float) {
/*  67 */         this.exitTypeDescriptor = "F";
/*  68 */         return new TypedValue(Float.valueOf(leftNumber.floatValue() / rightNumber.floatValue()));
/*     */       } 
/*  70 */       if (leftNumber instanceof BigInteger || rightNumber instanceof BigInteger) {
/*  71 */         BigInteger leftBigInteger = (BigInteger)NumberUtils.convertNumberToTargetClass(leftNumber, BigInteger.class);
/*  72 */         BigInteger rightBigInteger = (BigInteger)NumberUtils.convertNumberToTargetClass(rightNumber, BigInteger.class);
/*  73 */         return new TypedValue(leftBigInteger.divide(rightBigInteger));
/*     */       } 
/*  75 */       if (leftNumber instanceof Long || rightNumber instanceof Long) {
/*  76 */         this.exitTypeDescriptor = "J";
/*  77 */         return new TypedValue(Long.valueOf(leftNumber.longValue() / rightNumber.longValue()));
/*     */       } 
/*  79 */       if (CodeFlow.isIntegerForNumericOp(leftNumber) || CodeFlow.isIntegerForNumericOp(rightNumber)) {
/*  80 */         this.exitTypeDescriptor = "I";
/*  81 */         return new TypedValue(Integer.valueOf(leftNumber.intValue() / rightNumber.intValue()));
/*     */       } 
/*     */ 
/*     */       
/*  85 */       return new TypedValue(Double.valueOf(leftNumber.doubleValue() / rightNumber.doubleValue()));
/*     */     } 
/*     */ 
/*     */     
/*  89 */     return state.operate(Operation.DIVIDE, leftOperand, rightOperand);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/*  94 */     if (!getLeftOperand().isCompilable()) {
/*  95 */       return false;
/*     */     }
/*  97 */     if (this.children.length > 1 && 
/*  98 */       !getRightOperand().isCompilable()) {
/*  99 */       return false;
/*     */     }
/*     */     
/* 102 */     return (this.exitTypeDescriptor != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 107 */     getLeftOperand().generateCode(mv, cf);
/* 108 */     String leftDesc = (getLeftOperand()).exitTypeDescriptor;
/* 109 */     String exitDesc = this.exitTypeDescriptor;
/* 110 */     Assert.state((exitDesc != null), "No exit type descriptor");
/* 111 */     char targetDesc = exitDesc.charAt(0);
/* 112 */     CodeFlow.insertNumericUnboxOrPrimitiveTypeCoercion(mv, leftDesc, targetDesc);
/* 113 */     if (this.children.length > 1) {
/* 114 */       cf.enterCompilationScope();
/* 115 */       getRightOperand().generateCode(mv, cf);
/* 116 */       String rightDesc = (getRightOperand()).exitTypeDescriptor;
/* 117 */       cf.exitCompilationScope();
/* 118 */       CodeFlow.insertNumericUnboxOrPrimitiveTypeCoercion(mv, rightDesc, targetDesc);
/* 119 */       switch (targetDesc) {
/*     */         case 'I':
/* 121 */           mv.visitInsn(108);
/*     */           break;
/*     */         case 'J':
/* 124 */           mv.visitInsn(109);
/*     */           break;
/*     */         case 'F':
/* 127 */           mv.visitInsn(110);
/*     */           break;
/*     */         case 'D':
/* 130 */           mv.visitInsn(111);
/*     */           break;
/*     */         default:
/* 133 */           throw new IllegalStateException("Unrecognized exit type descriptor: '" + this.exitTypeDescriptor + "'");
/*     */       } 
/*     */     
/*     */     } 
/* 137 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\OpDivide.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */