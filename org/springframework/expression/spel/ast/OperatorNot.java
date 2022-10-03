/*    */ package org.springframework.expression.spel.ast;
/*    */ 
/*    */ import org.springframework.asm.Label;
/*    */ import org.springframework.asm.MethodVisitor;
/*    */ import org.springframework.expression.EvaluationException;
/*    */ import org.springframework.expression.TypedValue;
/*    */ import org.springframework.expression.spel.CodeFlow;
/*    */ import org.springframework.expression.spel.ExpressionState;
/*    */ import org.springframework.expression.spel.SpelEvaluationException;
/*    */ import org.springframework.expression.spel.SpelMessage;
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
/*    */ 
/*    */ 
/*    */ public class OperatorNot
/*    */   extends SpelNodeImpl
/*    */ {
/*    */   public OperatorNot(int startPos, int endPos, SpelNodeImpl operand) {
/* 39 */     super(startPos, endPos, new SpelNodeImpl[] { operand });
/* 40 */     this.exitTypeDescriptor = "Z";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public BooleanTypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*    */     try {
/* 47 */       Boolean value = this.children[0].<Boolean>getValue(state, Boolean.class);
/* 48 */       if (value == null) {
/* 49 */         throw new SpelEvaluationException(SpelMessage.TYPE_CONVERSION_ERROR, new Object[] { "null", "boolean" });
/*    */       }
/* 51 */       return BooleanTypedValue.forValue(!value.booleanValue());
/*    */     }
/* 53 */     catch (SpelEvaluationException ex) {
/* 54 */       ex.setPosition(getChild(0).getStartPosition());
/* 55 */       throw ex;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String toStringAST() {
/* 61 */     return "!" + getChild(0).toStringAST();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCompilable() {
/* 66 */     SpelNodeImpl child = this.children[0];
/* 67 */     return (child.isCompilable() && CodeFlow.isBooleanCompatible(child.exitTypeDescriptor));
/*    */   }
/*    */ 
/*    */   
/*    */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 72 */     this.children[0].generateCode(mv, cf);
/* 73 */     cf.unboxBooleanIfNecessary(mv);
/* 74 */     Label elseTarget = new Label();
/* 75 */     Label endOfIf = new Label();
/* 76 */     mv.visitJumpInsn(154, elseTarget);
/* 77 */     mv.visitInsn(4);
/* 78 */     mv.visitJumpInsn(167, endOfIf);
/* 79 */     mv.visitLabel(elseTarget);
/* 80 */     mv.visitInsn(3);
/* 81 */     mv.visitLabel(endOfIf);
/* 82 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\OperatorNot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */