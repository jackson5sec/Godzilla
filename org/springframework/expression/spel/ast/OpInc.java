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
/*     */ public class OpInc
/*     */   extends Operator
/*     */ {
/*     */   private final boolean postfix;
/*     */   
/*     */   public OpInc(int startPos, int endPos, boolean postfix, SpelNodeImpl... operands) {
/*  45 */     super("++", startPos, endPos, operands);
/*  46 */     this.postfix = postfix;
/*  47 */     Assert.notEmpty((Object[])operands, "Operands must not be empty");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*  53 */     SpelNodeImpl operand = getLeftOperand();
/*  54 */     ValueRef valueRef = operand.getValueRef(state);
/*     */     
/*  56 */     TypedValue typedValue = valueRef.getValue();
/*  57 */     Object value = typedValue.getValue();
/*  58 */     TypedValue returnValue = typedValue;
/*  59 */     TypedValue newValue = null;
/*     */     
/*  61 */     if (value instanceof Number) {
/*  62 */       Number op1 = (Number)value;
/*  63 */       if (op1 instanceof BigDecimal) {
/*  64 */         newValue = new TypedValue(((BigDecimal)op1).add(BigDecimal.ONE), typedValue.getTypeDescriptor());
/*     */       }
/*  66 */       else if (op1 instanceof Double) {
/*  67 */         newValue = new TypedValue(Double.valueOf(op1.doubleValue() + 1.0D), typedValue.getTypeDescriptor());
/*     */       }
/*  69 */       else if (op1 instanceof Float) {
/*  70 */         newValue = new TypedValue(Float.valueOf(op1.floatValue() + 1.0F), typedValue.getTypeDescriptor());
/*     */       }
/*  72 */       else if (op1 instanceof BigInteger) {
/*  73 */         newValue = new TypedValue(((BigInteger)op1).add(BigInteger.ONE), typedValue.getTypeDescriptor());
/*     */       }
/*  75 */       else if (op1 instanceof Long) {
/*  76 */         newValue = new TypedValue(Long.valueOf(op1.longValue() + 1L), typedValue.getTypeDescriptor());
/*     */       }
/*  78 */       else if (op1 instanceof Integer) {
/*  79 */         newValue = new TypedValue(Integer.valueOf(op1.intValue() + 1), typedValue.getTypeDescriptor());
/*     */       }
/*  81 */       else if (op1 instanceof Short) {
/*  82 */         newValue = new TypedValue(Integer.valueOf(op1.shortValue() + 1), typedValue.getTypeDescriptor());
/*     */       }
/*  84 */       else if (op1 instanceof Byte) {
/*  85 */         newValue = new TypedValue(Integer.valueOf(op1.byteValue() + 1), typedValue.getTypeDescriptor());
/*     */       }
/*     */       else {
/*     */         
/*  89 */         newValue = new TypedValue(Double.valueOf(op1.doubleValue() + 1.0D), typedValue.getTypeDescriptor());
/*     */       } 
/*     */     } 
/*     */     
/*  93 */     if (newValue == null) {
/*     */       try {
/*  95 */         newValue = state.operate(Operation.ADD, returnValue.getValue(), Integer.valueOf(1));
/*     */       }
/*  97 */       catch (SpelEvaluationException ex) {
/*  98 */         if (ex.getMessageCode() == SpelMessage.OPERATOR_NOT_SUPPORTED_BETWEEN_TYPES)
/*     */         {
/* 100 */           throw new SpelEvaluationException(operand.getStartPosition(), SpelMessage.OPERAND_NOT_INCREMENTABLE, new Object[] { operand
/* 101 */                 .toStringAST() });
/*     */         }
/* 103 */         throw ex;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 109 */       valueRef.setValue(newValue.getValue());
/*     */     }
/* 111 */     catch (SpelEvaluationException see) {
/*     */       
/* 113 */       if (see.getMessageCode() == SpelMessage.SETVALUE_NOT_SUPPORTED) {
/* 114 */         throw new SpelEvaluationException(operand.getStartPosition(), SpelMessage.OPERAND_NOT_INCREMENTABLE, new Object[0]);
/*     */       }
/*     */       
/* 117 */       throw see;
/*     */     } 
/*     */ 
/*     */     
/* 121 */     if (!this.postfix)
/*     */     {
/* 123 */       returnValue = newValue;
/*     */     }
/*     */     
/* 126 */     return returnValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/* 131 */     return getLeftOperand().toStringAST() + "++";
/*     */   }
/*     */ 
/*     */   
/*     */   public SpelNodeImpl getRightOperand() {
/* 136 */     throw new IllegalStateException("No right operand");
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\OpInc.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */