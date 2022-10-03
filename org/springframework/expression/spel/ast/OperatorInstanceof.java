/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
/*     */ import org.springframework.expression.spel.support.BooleanTypedValue;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class OperatorInstanceof
/*     */   extends Operator
/*     */ {
/*     */   @Nullable
/*     */   private Class<?> type;
/*     */   
/*     */   public OperatorInstanceof(int startPos, int endPos, SpelNodeImpl... operands) {
/*  45 */     super("instanceof", startPos, endPos, operands);
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
/*     */   public BooleanTypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*     */     BooleanTypedValue result;
/*  59 */     SpelNodeImpl rightOperand = getRightOperand();
/*  60 */     TypedValue left = getLeftOperand().getValueInternal(state);
/*  61 */     TypedValue right = rightOperand.getValueInternal(state);
/*  62 */     Object leftValue = left.getValue();
/*  63 */     Object rightValue = right.getValue();
/*     */     
/*  65 */     if (!(rightValue instanceof Class)) {
/*  66 */       throw new SpelEvaluationException(getRightOperand().getStartPosition(), SpelMessage.INSTANCEOF_OPERATOR_NEEDS_CLASS_OPERAND, new Object[] { (rightValue == null) ? "null" : rightValue
/*     */             
/*  68 */             .getClass().getName() });
/*     */     }
/*  70 */     Class<?> rightClass = (Class)rightValue;
/*  71 */     if (leftValue == null) {
/*  72 */       result = BooleanTypedValue.FALSE;
/*     */     } else {
/*     */       
/*  75 */       result = BooleanTypedValue.forValue(rightClass.isAssignableFrom(leftValue.getClass()));
/*     */     } 
/*  77 */     this.type = rightClass;
/*  78 */     if (rightOperand instanceof TypeReference)
/*     */     {
/*     */       
/*  81 */       this.exitTypeDescriptor = "Z";
/*     */     }
/*  83 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/*  88 */     return (this.exitTypeDescriptor != null && getLeftOperand().isCompilable());
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/*  93 */     getLeftOperand().generateCode(mv, cf);
/*  94 */     CodeFlow.insertBoxIfNecessary(mv, cf.lastDescriptor());
/*  95 */     Assert.state((this.type != null), "No type available");
/*  96 */     if (this.type.isPrimitive()) {
/*     */ 
/*     */       
/*  99 */       mv.visitInsn(87);
/* 100 */       mv.visitInsn(3);
/*     */     } else {
/*     */       
/* 103 */       mv.visitTypeInsn(193, Type.getInternalName(this.type));
/*     */     } 
/* 105 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\OperatorInstanceof.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */