/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.Operation;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OpDec
/*     */   extends Operator
/*     */ {
/*     */   private final boolean postfix;
/*     */   
/*     */   public OpDec(int startPos, int endPos, boolean postfix, SpelNodeImpl... operands) {
/*  45 */     super("--", startPos, endPos, operands);
/*  46 */     this.postfix = postfix;
/*  47 */     Assert.notEmpty((Object[])operands, "Operands must not be empty");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*  53 */     SpelNodeImpl operand = getLeftOperand();
/*     */ 
/*     */     
/*  56 */     ValueRef lvalue = operand.getValueRef(state);
/*     */     
/*  58 */     TypedValue operandTypedValue = lvalue.getValue();
/*  59 */     Object operandValue = operandTypedValue.getValue();
/*  60 */     TypedValue returnValue = operandTypedValue;
/*  61 */     TypedValue newValue = null;
/*     */     
/*  63 */     if (operandValue instanceof Number) {
/*  64 */       Number op1 = (Number)operandValue;
/*  65 */       if (op1 instanceof BigDecimal) {
/*  66 */         newValue = new TypedValue(((BigDecimal)op1).subtract(BigDecimal.ONE), operandTypedValue.getTypeDescriptor());
/*     */       }
/*  68 */       else if (op1 instanceof Double) {
/*  69 */         newValue = new TypedValue(Double.valueOf(op1.doubleValue() - 1.0D), operandTypedValue.getTypeDescriptor());
/*     */       }
/*  71 */       else if (op1 instanceof Float) {
/*  72 */         newValue = new TypedValue(Float.valueOf(op1.floatValue() - 1.0F), operandTypedValue.getTypeDescriptor());
/*     */       }
/*  74 */       else if (op1 instanceof BigInteger) {
/*  75 */         newValue = new TypedValue(((BigInteger)op1).subtract(BigInteger.ONE), operandTypedValue.getTypeDescriptor());
/*     */       }
/*  77 */       else if (op1 instanceof Long) {
/*  78 */         newValue = new TypedValue(Long.valueOf(op1.longValue() - 1L), operandTypedValue.getTypeDescriptor());
/*     */       }
/*  80 */       else if (op1 instanceof Integer) {
/*  81 */         newValue = new TypedValue(Integer.valueOf(op1.intValue() - 1), operandTypedValue.getTypeDescriptor());
/*     */       }
/*  83 */       else if (op1 instanceof Short) {
/*  84 */         newValue = new TypedValue(Integer.valueOf(op1.shortValue() - 1), operandTypedValue.getTypeDescriptor());
/*     */       }
/*  86 */       else if (op1 instanceof Byte) {
/*  87 */         newValue = new TypedValue(Integer.valueOf(op1.byteValue() - 1), operandTypedValue.getTypeDescriptor());
/*     */       }
/*     */       else {
/*     */         
/*  91 */         newValue = new TypedValue(Double.valueOf(op1.doubleValue() - 1.0D), operandTypedValue.getTypeDescriptor());
/*     */       } 
/*     */     } 
/*     */     
/*  95 */     if (newValue == null) {
/*     */       try {
/*  97 */         newValue = state.operate(Operation.SUBTRACT, returnValue.getValue(), Integer.valueOf(1));
/*     */       }
/*  99 */       catch (SpelEvaluationException ex) {
/* 100 */         if (ex.getMessageCode() == SpelMessage.OPERATOR_NOT_SUPPORTED_BETWEEN_TYPES)
/*     */         {
/* 102 */           throw new SpelEvaluationException(operand.getStartPosition(), SpelMessage.OPERAND_NOT_DECREMENTABLE, new Object[] { operand
/* 103 */                 .toStringAST() });
/*     */         }
/*     */         
/* 106 */         throw ex;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 113 */       lvalue.setValue(newValue.getValue());
/*     */     }
/* 115 */     catch (SpelEvaluationException see) {
/*     */       
/* 117 */       if (see.getMessageCode() == SpelMessage.SETVALUE_NOT_SUPPORTED) {
/* 118 */         throw new SpelEvaluationException(operand.getStartPosition(), SpelMessage.OPERAND_NOT_DECREMENTABLE, new Object[0]);
/*     */       }
/*     */ 
/*     */       
/* 122 */       throw see;
/*     */     } 
/*     */ 
/*     */     
/* 126 */     if (!this.postfix)
/*     */     {
/* 128 */       returnValue = newValue;
/*     */     }
/*     */     
/* 131 */     return returnValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/* 136 */     return getLeftOperand().toStringAST() + "--";
/*     */   }
/*     */ 
/*     */   
/*     */   public SpelNodeImpl getRightOperand() {
/* 141 */     throw new IllegalStateException("No right operand");
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\OpDec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */