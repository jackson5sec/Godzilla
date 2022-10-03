/*    */ package org.springframework.expression.spel.ast;
/*    */ 
/*    */ import org.springframework.asm.MethodVisitor;
/*    */ import org.springframework.expression.EvaluationContext;
/*    */ import org.springframework.expression.EvaluationException;
/*    */ import org.springframework.expression.TypedValue;
/*    */ import org.springframework.expression.spel.CodeFlow;
/*    */ import org.springframework.expression.spel.ExpressionState;
/*    */ import org.springframework.expression.spel.support.BooleanTypedValue;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class OpEQ
/*    */   extends Operator
/*    */ {
/*    */   public OpEQ(int startPos, int endPos, SpelNodeImpl... operands) {
/* 35 */     super("==", startPos, endPos, operands);
/* 36 */     this.exitTypeDescriptor = "Z";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public BooleanTypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/* 42 */     Object left = getLeftOperand().getValueInternal(state).getValue();
/* 43 */     Object right = getRightOperand().getValueInternal(state).getValue();
/* 44 */     this.leftActualDescriptor = CodeFlow.toDescriptorFromObject(left);
/* 45 */     this.rightActualDescriptor = CodeFlow.toDescriptorFromObject(right);
/* 46 */     return BooleanTypedValue.forValue(equalityCheck(state.getEvaluationContext(), left, right));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isCompilable() {
/* 53 */     SpelNodeImpl left = getLeftOperand();
/* 54 */     SpelNodeImpl right = getRightOperand();
/* 55 */     if (!left.isCompilable() || !right.isCompilable()) {
/* 56 */       return false;
/*    */     }
/*    */     
/* 59 */     String leftDesc = left.exitTypeDescriptor;
/* 60 */     String rightDesc = right.exitTypeDescriptor;
/* 61 */     Operator.DescriptorComparison dc = Operator.DescriptorComparison.checkNumericCompatibility(leftDesc, rightDesc, this.leftActualDescriptor, this.rightActualDescriptor);
/*    */     
/* 63 */     return (!dc.areNumbers || dc.areCompatible);
/*    */   }
/*    */ 
/*    */   
/*    */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 68 */     cf.loadEvaluationContext(mv);
/* 69 */     String leftDesc = (getLeftOperand()).exitTypeDescriptor;
/* 70 */     String rightDesc = (getRightOperand()).exitTypeDescriptor;
/* 71 */     boolean leftPrim = CodeFlow.isPrimitive(leftDesc);
/* 72 */     boolean rightPrim = CodeFlow.isPrimitive(rightDesc);
/*    */     
/* 74 */     cf.enterCompilationScope();
/* 75 */     getLeftOperand().generateCode(mv, cf);
/* 76 */     cf.exitCompilationScope();
/* 77 */     if (leftPrim) {
/* 78 */       CodeFlow.insertBoxIfNecessary(mv, leftDesc.charAt(0));
/*    */     }
/* 80 */     cf.enterCompilationScope();
/* 81 */     getRightOperand().generateCode(mv, cf);
/* 82 */     cf.exitCompilationScope();
/* 83 */     if (rightPrim) {
/* 84 */       CodeFlow.insertBoxIfNecessary(mv, rightDesc.charAt(0));
/*    */     }
/*    */     
/* 87 */     String operatorClassName = Operator.class.getName().replace('.', '/');
/* 88 */     String evaluationContextClassName = EvaluationContext.class.getName().replace('.', '/');
/* 89 */     mv.visitMethodInsn(184, operatorClassName, "equalityCheck", "(L" + evaluationContextClassName + ";Ljava/lang/Object;Ljava/lang/Object;)Z", false);
/*    */     
/* 91 */     cf.pushDescriptor("Z");
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\OpEQ.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */