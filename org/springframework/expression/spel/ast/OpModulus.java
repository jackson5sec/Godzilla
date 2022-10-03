/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
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
/*     */ public class OpModulus
/*     */   extends Operator
/*     */ {
/*     */   public OpModulus(int startPos, int endPos, SpelNodeImpl... operands) {
/*  42 */     super("%", startPos, endPos, operands);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*  48 */     Object leftOperand = getLeftOperand().getValueInternal(state).getValue();
/*  49 */     Object rightOperand = getRightOperand().getValueInternal(state).getValue();
/*     */     
/*  51 */     if (leftOperand instanceof Number && rightOperand instanceof Number) {
/*  52 */       Number leftNumber = (Number)leftOperand;
/*  53 */       Number rightNumber = (Number)rightOperand;
/*     */       
/*  55 */       if (leftNumber instanceof BigDecimal || rightNumber instanceof BigDecimal) {
/*  56 */         BigDecimal leftBigDecimal = (BigDecimal)NumberUtils.convertNumberToTargetClass(leftNumber, BigDecimal.class);
/*  57 */         BigDecimal rightBigDecimal = (BigDecimal)NumberUtils.convertNumberToTargetClass(rightNumber, BigDecimal.class);
/*  58 */         return new TypedValue(leftBigDecimal.remainder(rightBigDecimal));
/*     */       } 
/*  60 */       if (leftNumber instanceof Double || rightNumber instanceof Double) {
/*  61 */         this.exitTypeDescriptor = "D";
/*  62 */         return new TypedValue(Double.valueOf(leftNumber.doubleValue() % rightNumber.doubleValue()));
/*     */       } 
/*  64 */       if (leftNumber instanceof Float || rightNumber instanceof Float) {
/*  65 */         this.exitTypeDescriptor = "F";
/*  66 */         return new TypedValue(Float.valueOf(leftNumber.floatValue() % rightNumber.floatValue()));
/*     */       } 
/*  68 */       if (leftNumber instanceof BigInteger || rightNumber instanceof BigInteger) {
/*  69 */         BigInteger leftBigInteger = (BigInteger)NumberUtils.convertNumberToTargetClass(leftNumber, BigInteger.class);
/*  70 */         BigInteger rightBigInteger = (BigInteger)NumberUtils.convertNumberToTargetClass(rightNumber, BigInteger.class);
/*  71 */         return new TypedValue(leftBigInteger.remainder(rightBigInteger));
/*     */       } 
/*  73 */       if (leftNumber instanceof Long || rightNumber instanceof Long) {
/*  74 */         this.exitTypeDescriptor = "J";
/*  75 */         return new TypedValue(Long.valueOf(leftNumber.longValue() % rightNumber.longValue()));
/*     */       } 
/*  77 */       if (CodeFlow.isIntegerForNumericOp(leftNumber) || CodeFlow.isIntegerForNumericOp(rightNumber)) {
/*  78 */         this.exitTypeDescriptor = "I";
/*  79 */         return new TypedValue(Integer.valueOf(leftNumber.intValue() % rightNumber.intValue()));
/*     */       } 
/*     */ 
/*     */       
/*  83 */       return new TypedValue(Double.valueOf(leftNumber.doubleValue() % rightNumber.doubleValue()));
/*     */     } 
/*     */ 
/*     */     
/*  87 */     return state.operate(Operation.MODULUS, leftOperand, rightOperand);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/*  92 */     if (!getLeftOperand().isCompilable()) {
/*  93 */       return false;
/*     */     }
/*  95 */     if (this.children.length > 1 && 
/*  96 */       !getRightOperand().isCompilable()) {
/*  97 */       return false;
/*     */     }
/*     */     
/* 100 */     return (this.exitTypeDescriptor != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 105 */     getLeftOperand().generateCode(mv, cf);
/* 106 */     String leftDesc = (getLeftOperand()).exitTypeDescriptor;
/* 107 */     String exitDesc = this.exitTypeDescriptor;
/* 108 */     Assert.state((exitDesc != null), "No exit type descriptor");
/* 109 */     char targetDesc = exitDesc.charAt(0);
/* 110 */     CodeFlow.insertNumericUnboxOrPrimitiveTypeCoercion(mv, leftDesc, targetDesc);
/* 111 */     if (this.children.length > 1) {
/* 112 */       cf.enterCompilationScope();
/* 113 */       getRightOperand().generateCode(mv, cf);
/* 114 */       String rightDesc = (getRightOperand()).exitTypeDescriptor;
/* 115 */       cf.exitCompilationScope();
/* 116 */       CodeFlow.insertNumericUnboxOrPrimitiveTypeCoercion(mv, rightDesc, targetDesc);
/* 117 */       switch (targetDesc) {
/*     */         case 'I':
/* 119 */           mv.visitInsn(112);
/*     */           break;
/*     */         case 'J':
/* 122 */           mv.visitInsn(113);
/*     */           break;
/*     */         case 'F':
/* 125 */           mv.visitInsn(114);
/*     */           break;
/*     */         case 'D':
/* 128 */           mv.visitInsn(115);
/*     */           break;
/*     */         default:
/* 131 */           throw new IllegalStateException("Unrecognized exit type descriptor: '" + this.exitTypeDescriptor + "'");
/*     */       } 
/*     */     
/*     */     } 
/* 135 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\OpModulus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */