/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import org.springframework.asm.Label;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
/*     */ import org.springframework.expression.spel.support.BooleanTypedValue;
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
/*     */ public class OpAnd
/*     */   extends Operator
/*     */ {
/*     */   public OpAnd(int startPos, int endPos, SpelNodeImpl... operands) {
/*  41 */     super("and", startPos, endPos, operands);
/*  42 */     this.exitTypeDescriptor = "Z";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*  48 */     if (!getBooleanValue(state, getLeftOperand()))
/*     */     {
/*  50 */       return (TypedValue)BooleanTypedValue.FALSE;
/*     */     }
/*  52 */     return (TypedValue)BooleanTypedValue.forValue(getBooleanValue(state, getRightOperand()));
/*     */   }
/*     */   
/*     */   private boolean getBooleanValue(ExpressionState state, SpelNodeImpl operand) {
/*     */     try {
/*  57 */       Boolean value = operand.<Boolean>getValue(state, Boolean.class);
/*  58 */       assertValueNotNull(value);
/*  59 */       return value.booleanValue();
/*     */     }
/*  61 */     catch (SpelEvaluationException ex) {
/*  62 */       ex.setPosition(operand.getStartPosition());
/*  63 */       throw ex;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void assertValueNotNull(@Nullable Boolean value) {
/*  68 */     if (value == null) {
/*  69 */       throw new SpelEvaluationException(SpelMessage.TYPE_CONVERSION_ERROR, new Object[] { "null", "boolean" });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/*  75 */     SpelNodeImpl left = getLeftOperand();
/*  76 */     SpelNodeImpl right = getRightOperand();
/*  77 */     return (left.isCompilable() && right.isCompilable() && 
/*  78 */       CodeFlow.isBooleanCompatible(left.exitTypeDescriptor) && 
/*  79 */       CodeFlow.isBooleanCompatible(right.exitTypeDescriptor));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/*  85 */     Label elseTarget = new Label();
/*  86 */     Label endOfIf = new Label();
/*  87 */     cf.enterCompilationScope();
/*  88 */     getLeftOperand().generateCode(mv, cf);
/*  89 */     cf.unboxBooleanIfNecessary(mv);
/*  90 */     cf.exitCompilationScope();
/*  91 */     mv.visitJumpInsn(154, elseTarget);
/*  92 */     mv.visitLdcInsn(Integer.valueOf(0));
/*  93 */     mv.visitJumpInsn(167, endOfIf);
/*  94 */     mv.visitLabel(elseTarget);
/*  95 */     cf.enterCompilationScope();
/*  96 */     getRightOperand().generateCode(mv, cf);
/*  97 */     cf.unboxBooleanIfNecessary(mv);
/*  98 */     cf.exitCompilationScope();
/*  99 */     mv.visitLabel(endOfIf);
/* 100 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\OpAnd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */