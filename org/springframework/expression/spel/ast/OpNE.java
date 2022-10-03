/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import org.springframework.asm.Label;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.support.BooleanTypedValue;
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
/*     */ public class OpNE
/*     */   extends Operator
/*     */ {
/*     */   public OpNE(int startPos, int endPos, SpelNodeImpl... operands) {
/*  36 */     super("!=", startPos, endPos, operands);
/*  37 */     this.exitTypeDescriptor = "Z";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BooleanTypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*  43 */     Object leftValue = getLeftOperand().getValueInternal(state).getValue();
/*  44 */     Object rightValue = getRightOperand().getValueInternal(state).getValue();
/*  45 */     this.leftActualDescriptor = CodeFlow.toDescriptorFromObject(leftValue);
/*  46 */     this.rightActualDescriptor = CodeFlow.toDescriptorFromObject(rightValue);
/*  47 */     return BooleanTypedValue.forValue(!equalityCheck(state.getEvaluationContext(), leftValue, rightValue));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/*  54 */     SpelNodeImpl left = getLeftOperand();
/*  55 */     SpelNodeImpl right = getRightOperand();
/*  56 */     if (!left.isCompilable() || !right.isCompilable()) {
/*  57 */       return false;
/*     */     }
/*     */     
/*  60 */     String leftDesc = left.exitTypeDescriptor;
/*  61 */     String rightDesc = right.exitTypeDescriptor;
/*  62 */     Operator.DescriptorComparison dc = Operator.DescriptorComparison.checkNumericCompatibility(leftDesc, rightDesc, this.leftActualDescriptor, this.rightActualDescriptor);
/*     */     
/*  64 */     return (!dc.areNumbers || dc.areCompatible);
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/*  69 */     cf.loadEvaluationContext(mv);
/*  70 */     String leftDesc = (getLeftOperand()).exitTypeDescriptor;
/*  71 */     String rightDesc = (getRightOperand()).exitTypeDescriptor;
/*  72 */     boolean leftPrim = CodeFlow.isPrimitive(leftDesc);
/*  73 */     boolean rightPrim = CodeFlow.isPrimitive(rightDesc);
/*     */     
/*  75 */     cf.enterCompilationScope();
/*  76 */     getLeftOperand().generateCode(mv, cf);
/*  77 */     cf.exitCompilationScope();
/*  78 */     if (leftPrim) {
/*  79 */       CodeFlow.insertBoxIfNecessary(mv, leftDesc.charAt(0));
/*     */     }
/*  81 */     cf.enterCompilationScope();
/*  82 */     getRightOperand().generateCode(mv, cf);
/*  83 */     cf.exitCompilationScope();
/*  84 */     if (rightPrim) {
/*  85 */       CodeFlow.insertBoxIfNecessary(mv, rightDesc.charAt(0));
/*     */     }
/*     */     
/*  88 */     String operatorClassName = Operator.class.getName().replace('.', '/');
/*  89 */     String evaluationContextClassName = EvaluationContext.class.getName().replace('.', '/');
/*  90 */     mv.visitMethodInsn(184, operatorClassName, "equalityCheck", "(L" + evaluationContextClassName + ";Ljava/lang/Object;Ljava/lang/Object;)Z", false);
/*     */ 
/*     */ 
/*     */     
/*  94 */     Label notZero = new Label();
/*  95 */     Label end = new Label();
/*  96 */     mv.visitJumpInsn(154, notZero);
/*  97 */     mv.visitInsn(4);
/*  98 */     mv.visitJumpInsn(167, end);
/*  99 */     mv.visitLabel(notZero);
/* 100 */     mv.visitInsn(3);
/* 101 */     mv.visitLabel(end);
/*     */     
/* 103 */     cf.pushDescriptor("Z");
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\OpNE.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */