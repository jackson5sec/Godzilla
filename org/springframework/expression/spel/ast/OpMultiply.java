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
/*     */ public class OpMultiply
/*     */   extends Operator
/*     */ {
/*     */   public OpMultiply(int startPos, int endPos, SpelNodeImpl... operands) {
/*  56 */     super("*", startPos, endPos, operands);
/*     */   }
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
/*     */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*  72 */     Object leftOperand = getLeftOperand().getValueInternal(state).getValue();
/*  73 */     Object rightOperand = getRightOperand().getValueInternal(state).getValue();
/*     */     
/*  75 */     if (leftOperand instanceof Number && rightOperand instanceof Number) {
/*  76 */       Number leftNumber = (Number)leftOperand;
/*  77 */       Number rightNumber = (Number)rightOperand;
/*     */       
/*  79 */       if (leftNumber instanceof BigDecimal || rightNumber instanceof BigDecimal) {
/*  80 */         BigDecimal leftBigDecimal = (BigDecimal)NumberUtils.convertNumberToTargetClass(leftNumber, BigDecimal.class);
/*  81 */         BigDecimal rightBigDecimal = (BigDecimal)NumberUtils.convertNumberToTargetClass(rightNumber, BigDecimal.class);
/*  82 */         return new TypedValue(leftBigDecimal.multiply(rightBigDecimal));
/*     */       } 
/*  84 */       if (leftNumber instanceof Double || rightNumber instanceof Double) {
/*  85 */         this.exitTypeDescriptor = "D";
/*  86 */         return new TypedValue(Double.valueOf(leftNumber.doubleValue() * rightNumber.doubleValue()));
/*     */       } 
/*  88 */       if (leftNumber instanceof Float || rightNumber instanceof Float) {
/*  89 */         this.exitTypeDescriptor = "F";
/*  90 */         return new TypedValue(Float.valueOf(leftNumber.floatValue() * rightNumber.floatValue()));
/*     */       } 
/*  92 */       if (leftNumber instanceof BigInteger || rightNumber instanceof BigInteger) {
/*  93 */         BigInteger leftBigInteger = (BigInteger)NumberUtils.convertNumberToTargetClass(leftNumber, BigInteger.class);
/*  94 */         BigInteger rightBigInteger = (BigInteger)NumberUtils.convertNumberToTargetClass(rightNumber, BigInteger.class);
/*  95 */         return new TypedValue(leftBigInteger.multiply(rightBigInteger));
/*     */       } 
/*  97 */       if (leftNumber instanceof Long || rightNumber instanceof Long) {
/*  98 */         this.exitTypeDescriptor = "J";
/*  99 */         return new TypedValue(Long.valueOf(leftNumber.longValue() * rightNumber.longValue()));
/*     */       } 
/* 101 */       if (CodeFlow.isIntegerForNumericOp(leftNumber) || CodeFlow.isIntegerForNumericOp(rightNumber)) {
/* 102 */         this.exitTypeDescriptor = "I";
/* 103 */         return new TypedValue(Integer.valueOf(leftNumber.intValue() * rightNumber.intValue()));
/*     */       } 
/*     */ 
/*     */       
/* 107 */       return new TypedValue(Double.valueOf(leftNumber.doubleValue() * rightNumber.doubleValue()));
/*     */     } 
/*     */ 
/*     */     
/* 111 */     if (leftOperand instanceof String && rightOperand instanceof Integer) {
/* 112 */       int repeats = ((Integer)rightOperand).intValue();
/* 113 */       StringBuilder result = new StringBuilder();
/* 114 */       for (int i = 0; i < repeats; i++) {
/* 115 */         result.append(leftOperand);
/*     */       }
/* 117 */       return new TypedValue(result.toString());
/*     */     } 
/*     */     
/* 120 */     return state.operate(Operation.MULTIPLY, leftOperand, rightOperand);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/* 125 */     if (!getLeftOperand().isCompilable()) {
/* 126 */       return false;
/*     */     }
/* 128 */     if (this.children.length > 1 && 
/* 129 */       !getRightOperand().isCompilable()) {
/* 130 */       return false;
/*     */     }
/*     */     
/* 133 */     return (this.exitTypeDescriptor != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 138 */     getLeftOperand().generateCode(mv, cf);
/* 139 */     String leftDesc = (getLeftOperand()).exitTypeDescriptor;
/* 140 */     String exitDesc = this.exitTypeDescriptor;
/* 141 */     Assert.state((exitDesc != null), "No exit type descriptor");
/* 142 */     char targetDesc = exitDesc.charAt(0);
/* 143 */     CodeFlow.insertNumericUnboxOrPrimitiveTypeCoercion(mv, leftDesc, targetDesc);
/* 144 */     if (this.children.length > 1) {
/* 145 */       cf.enterCompilationScope();
/* 146 */       getRightOperand().generateCode(mv, cf);
/* 147 */       String rightDesc = (getRightOperand()).exitTypeDescriptor;
/* 148 */       cf.exitCompilationScope();
/* 149 */       CodeFlow.insertNumericUnboxOrPrimitiveTypeCoercion(mv, rightDesc, targetDesc);
/* 150 */       switch (targetDesc) {
/*     */         case 'I':
/* 152 */           mv.visitInsn(104);
/*     */           break;
/*     */         case 'J':
/* 155 */           mv.visitInsn(105);
/*     */           break;
/*     */         case 'F':
/* 158 */           mv.visitInsn(106);
/*     */           break;
/*     */         case 'D':
/* 161 */           mv.visitInsn(107);
/*     */           break;
/*     */         default:
/* 164 */           throw new IllegalStateException("Unrecognized exit type descriptor: '" + this.exitTypeDescriptor + "'");
/*     */       } 
/*     */     
/*     */     } 
/* 168 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\OpMultiply.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */