/*    */ package org.springframework.expression.spel.ast;
/*    */ 
/*    */ import java.math.BigDecimal;
/*    */ import java.math.BigInteger;
/*    */ import org.springframework.expression.EvaluationException;
/*    */ import org.springframework.expression.Operation;
/*    */ import org.springframework.expression.TypedValue;
/*    */ import org.springframework.expression.spel.ExpressionState;
/*    */ import org.springframework.util.NumberUtils;
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
/*    */ public class OperatorPower
/*    */   extends Operator
/*    */ {
/*    */   public OperatorPower(int startPos, int endPos, SpelNodeImpl... operands) {
/* 38 */     super("^", startPos, endPos, operands);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/* 44 */     SpelNodeImpl leftOp = getLeftOperand();
/* 45 */     SpelNodeImpl rightOp = getRightOperand();
/*    */     
/* 47 */     Object leftOperand = leftOp.getValueInternal(state).getValue();
/* 48 */     Object rightOperand = rightOp.getValueInternal(state).getValue();
/*    */     
/* 50 */     if (leftOperand instanceof Number && rightOperand instanceof Number) {
/* 51 */       Number leftNumber = (Number)leftOperand;
/* 52 */       Number rightNumber = (Number)rightOperand;
/*    */       
/* 54 */       if (leftNumber instanceof BigDecimal) {
/* 55 */         BigDecimal leftBigDecimal = (BigDecimal)NumberUtils.convertNumberToTargetClass(leftNumber, BigDecimal.class);
/* 56 */         return new TypedValue(leftBigDecimal.pow(rightNumber.intValue()));
/*    */       } 
/* 58 */       if (leftNumber instanceof BigInteger) {
/* 59 */         BigInteger leftBigInteger = (BigInteger)NumberUtils.convertNumberToTargetClass(leftNumber, BigInteger.class);
/* 60 */         return new TypedValue(leftBigInteger.pow(rightNumber.intValue()));
/*    */       } 
/* 62 */       if (leftNumber instanceof Double || rightNumber instanceof Double) {
/* 63 */         return new TypedValue(Double.valueOf(Math.pow(leftNumber.doubleValue(), rightNumber.doubleValue())));
/*    */       }
/* 65 */       if (leftNumber instanceof Float || rightNumber instanceof Float) {
/* 66 */         return new TypedValue(Double.valueOf(Math.pow(leftNumber.floatValue(), rightNumber.floatValue())));
/*    */       }
/*    */       
/* 69 */       double d = Math.pow(leftNumber.doubleValue(), rightNumber.doubleValue());
/* 70 */       if (d > 2.147483647E9D || leftNumber instanceof Long || rightNumber instanceof Long) {
/* 71 */         return new TypedValue(Long.valueOf((long)d));
/*    */       }
/*    */       
/* 74 */       return new TypedValue(Integer.valueOf((int)d));
/*    */     } 
/*    */ 
/*    */     
/* 78 */     return state.operate(Operation.POWER, leftOperand, rightOperand);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\OperatorPower.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */