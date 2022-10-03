/*    */ package org.springframework.expression.spel.ast;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.springframework.expression.EvaluationException;
/*    */ import org.springframework.expression.TypeComparator;
/*    */ import org.springframework.expression.TypedValue;
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
/*    */ 
/*    */ 
/*    */ public class OperatorBetween
/*    */   extends Operator
/*    */ {
/*    */   public OperatorBetween(int startPos, int endPos, SpelNodeImpl... operands) {
/* 40 */     super("between", startPos, endPos, operands);
/*    */   }
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
/*    */   public BooleanTypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/* 54 */     Object left = getLeftOperand().getValueInternal(state).getValue();
/* 55 */     Object right = getRightOperand().getValueInternal(state).getValue();
/* 56 */     if (!(right instanceof List) || ((List)right).size() != 2) {
/* 57 */       throw new SpelEvaluationException(getRightOperand().getStartPosition(), SpelMessage.BETWEEN_RIGHT_OPERAND_MUST_BE_TWO_ELEMENT_LIST, new Object[0]);
/*    */     }
/*    */ 
/*    */     
/* 61 */     List<?> list = (List)right;
/* 62 */     Object low = list.get(0);
/* 63 */     Object high = list.get(1);
/* 64 */     TypeComparator comp = state.getTypeComparator();
/*    */     try {
/* 66 */       return BooleanTypedValue.forValue((comp.compare(left, low) >= 0 && comp.compare(left, high) <= 0));
/*    */     }
/* 68 */     catch (SpelEvaluationException ex) {
/* 69 */       ex.setPosition(getStartPosition());
/* 70 */       throw ex;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\OperatorBetween.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */