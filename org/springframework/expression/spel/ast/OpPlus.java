/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.Operation;
/*     */ import org.springframework.expression.TypeConverter;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public class OpPlus
/*     */   extends Operator
/*     */ {
/*     */   public OpPlus(int startPos, int endPos, SpelNodeImpl... operands) {
/*  54 */     super("+", startPos, endPos, operands);
/*  55 */     Assert.notEmpty((Object[])operands, "Operands must not be empty");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*  61 */     SpelNodeImpl leftOp = getLeftOperand();
/*     */     
/*  63 */     if (this.children.length < 2) {
/*  64 */       Object operandOne = leftOp.getValueInternal(state).getValue();
/*  65 */       if (operandOne instanceof Number) {
/*  66 */         if (operandOne instanceof Double) {
/*  67 */           this.exitTypeDescriptor = "D";
/*     */         }
/*  69 */         else if (operandOne instanceof Float) {
/*  70 */           this.exitTypeDescriptor = "F";
/*     */         }
/*  72 */         else if (operandOne instanceof Long) {
/*  73 */           this.exitTypeDescriptor = "J";
/*     */         }
/*  75 */         else if (operandOne instanceof Integer) {
/*  76 */           this.exitTypeDescriptor = "I";
/*     */         } 
/*  78 */         return new TypedValue(operandOne);
/*     */       } 
/*  80 */       return state.operate(Operation.ADD, operandOne, null);
/*     */     } 
/*     */     
/*  83 */     TypedValue operandOneValue = leftOp.getValueInternal(state);
/*  84 */     Object leftOperand = operandOneValue.getValue();
/*  85 */     TypedValue operandTwoValue = getRightOperand().getValueInternal(state);
/*  86 */     Object rightOperand = operandTwoValue.getValue();
/*     */     
/*  88 */     if (leftOperand instanceof Number && rightOperand instanceof Number) {
/*  89 */       Number leftNumber = (Number)leftOperand;
/*  90 */       Number rightNumber = (Number)rightOperand;
/*     */       
/*  92 */       if (leftNumber instanceof BigDecimal || rightNumber instanceof BigDecimal) {
/*  93 */         BigDecimal leftBigDecimal = (BigDecimal)NumberUtils.convertNumberToTargetClass(leftNumber, BigDecimal.class);
/*  94 */         BigDecimal rightBigDecimal = (BigDecimal)NumberUtils.convertNumberToTargetClass(rightNumber, BigDecimal.class);
/*  95 */         return new TypedValue(leftBigDecimal.add(rightBigDecimal));
/*     */       } 
/*  97 */       if (leftNumber instanceof Double || rightNumber instanceof Double) {
/*  98 */         this.exitTypeDescriptor = "D";
/*  99 */         return new TypedValue(Double.valueOf(leftNumber.doubleValue() + rightNumber.doubleValue()));
/*     */       } 
/* 101 */       if (leftNumber instanceof Float || rightNumber instanceof Float) {
/* 102 */         this.exitTypeDescriptor = "F";
/* 103 */         return new TypedValue(Float.valueOf(leftNumber.floatValue() + rightNumber.floatValue()));
/*     */       } 
/* 105 */       if (leftNumber instanceof BigInteger || rightNumber instanceof BigInteger) {
/* 106 */         BigInteger leftBigInteger = (BigInteger)NumberUtils.convertNumberToTargetClass(leftNumber, BigInteger.class);
/* 107 */         BigInteger rightBigInteger = (BigInteger)NumberUtils.convertNumberToTargetClass(rightNumber, BigInteger.class);
/* 108 */         return new TypedValue(leftBigInteger.add(rightBigInteger));
/*     */       } 
/* 110 */       if (leftNumber instanceof Long || rightNumber instanceof Long) {
/* 111 */         this.exitTypeDescriptor = "J";
/* 112 */         return new TypedValue(Long.valueOf(leftNumber.longValue() + rightNumber.longValue()));
/*     */       } 
/* 114 */       if (CodeFlow.isIntegerForNumericOp(leftNumber) || CodeFlow.isIntegerForNumericOp(rightNumber)) {
/* 115 */         this.exitTypeDescriptor = "I";
/* 116 */         return new TypedValue(Integer.valueOf(leftNumber.intValue() + rightNumber.intValue()));
/*     */       } 
/*     */ 
/*     */       
/* 120 */       return new TypedValue(Double.valueOf(leftNumber.doubleValue() + rightNumber.doubleValue()));
/*     */     } 
/*     */ 
/*     */     
/* 124 */     if (leftOperand instanceof String && rightOperand instanceof String) {
/* 125 */       this.exitTypeDescriptor = "Ljava/lang/String";
/* 126 */       return new TypedValue((String)leftOperand + rightOperand);
/*     */     } 
/*     */     
/* 129 */     if (leftOperand instanceof String) {
/* 130 */       return new TypedValue(leftOperand + ((rightOperand == null) ? "null" : 
/* 131 */           convertTypedValueToString(operandTwoValue, state)));
/*     */     }
/*     */     
/* 134 */     if (rightOperand instanceof String) {
/* 135 */       return new TypedValue(((leftOperand == null) ? "null" : 
/* 136 */           convertTypedValueToString(operandOneValue, state)) + rightOperand);
/*     */     }
/*     */     
/* 139 */     return state.operate(Operation.ADD, leftOperand, rightOperand);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/* 144 */     if (this.children.length < 2) {
/* 145 */       return "+" + getLeftOperand().toStringAST();
/*     */     }
/* 147 */     return super.toStringAST();
/*     */   }
/*     */ 
/*     */   
/*     */   public SpelNodeImpl getRightOperand() {
/* 152 */     if (this.children.length < 2) {
/* 153 */       throw new IllegalStateException("No right operand");
/*     */     }
/* 155 */     return this.children[1];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String convertTypedValueToString(TypedValue value, ExpressionState state) {
/* 166 */     TypeConverter typeConverter = state.getEvaluationContext().getTypeConverter();
/* 167 */     TypeDescriptor typeDescriptor = TypeDescriptor.valueOf(String.class);
/* 168 */     if (typeConverter.canConvert(value.getTypeDescriptor(), typeDescriptor)) {
/* 169 */       return String.valueOf(typeConverter.convertValue(value.getValue(), value
/* 170 */             .getTypeDescriptor(), typeDescriptor));
/*     */     }
/* 172 */     return String.valueOf(value.getValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/* 177 */     if (!getLeftOperand().isCompilable()) {
/* 178 */       return false;
/*     */     }
/* 180 */     if (this.children.length > 1 && 
/* 181 */       !getRightOperand().isCompilable()) {
/* 182 */       return false;
/*     */     }
/*     */     
/* 185 */     return (this.exitTypeDescriptor != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void walk(MethodVisitor mv, CodeFlow cf, @Nullable SpelNodeImpl operand) {
/* 193 */     if (operand instanceof OpPlus) {
/* 194 */       OpPlus plus = (OpPlus)operand;
/* 195 */       walk(mv, cf, plus.getLeftOperand());
/* 196 */       walk(mv, cf, plus.getRightOperand());
/*     */     }
/* 198 */     else if (operand != null) {
/* 199 */       cf.enterCompilationScope();
/* 200 */       operand.generateCode(mv, cf);
/* 201 */       if (!"Ljava/lang/String".equals(cf.lastDescriptor())) {
/* 202 */         mv.visitTypeInsn(192, "java/lang/String");
/*     */       }
/* 204 */       cf.exitCompilationScope();
/* 205 */       mv.visitMethodInsn(182, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 211 */     if ("Ljava/lang/String".equals(this.exitTypeDescriptor)) {
/* 212 */       mv.visitTypeInsn(187, "java/lang/StringBuilder");
/* 213 */       mv.visitInsn(89);
/* 214 */       mv.visitMethodInsn(183, "java/lang/StringBuilder", "<init>", "()V", false);
/* 215 */       walk(mv, cf, getLeftOperand());
/* 216 */       walk(mv, cf, getRightOperand());
/* 217 */       mv.visitMethodInsn(182, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
/*     */     } else {
/*     */       
/* 220 */       this.children[0].generateCode(mv, cf);
/* 221 */       String leftDesc = (this.children[0]).exitTypeDescriptor;
/* 222 */       String exitDesc = this.exitTypeDescriptor;
/* 223 */       Assert.state((exitDesc != null), "No exit type descriptor");
/* 224 */       char targetDesc = exitDesc.charAt(0);
/* 225 */       CodeFlow.insertNumericUnboxOrPrimitiveTypeCoercion(mv, leftDesc, targetDesc);
/* 226 */       if (this.children.length > 1) {
/* 227 */         cf.enterCompilationScope();
/* 228 */         this.children[1].generateCode(mv, cf);
/* 229 */         String rightDesc = (this.children[1]).exitTypeDescriptor;
/* 230 */         cf.exitCompilationScope();
/* 231 */         CodeFlow.insertNumericUnboxOrPrimitiveTypeCoercion(mv, rightDesc, targetDesc);
/* 232 */         switch (targetDesc) {
/*     */           case 'I':
/* 234 */             mv.visitInsn(96);
/*     */             break;
/*     */           case 'J':
/* 237 */             mv.visitInsn(97);
/*     */             break;
/*     */           case 'F':
/* 240 */             mv.visitInsn(98);
/*     */             break;
/*     */           case 'D':
/* 243 */             mv.visitInsn(99);
/*     */             break;
/*     */           default:
/* 246 */             throw new IllegalStateException("Unrecognized exit type descriptor: '" + this.exitTypeDescriptor + "'");
/*     */         } 
/*     */       
/*     */       } 
/*     */     } 
/* 251 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\OpPlus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */