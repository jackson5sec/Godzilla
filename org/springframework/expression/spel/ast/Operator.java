/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import org.springframework.asm.Label;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.NumberUtils;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ 
/*     */ public abstract class Operator
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   private final String operatorName;
/*     */   @Nullable
/*     */   protected String leftActualDescriptor;
/*     */   @Nullable
/*     */   protected String rightActualDescriptor;
/*     */   
/*     */   public Operator(String payload, int startPos, int endPos, SpelNodeImpl... operands) {
/*  58 */     super(startPos, endPos, operands);
/*  59 */     this.operatorName = payload;
/*     */   }
/*     */ 
/*     */   
/*     */   public SpelNodeImpl getLeftOperand() {
/*  64 */     return this.children[0];
/*     */   }
/*     */   
/*     */   public SpelNodeImpl getRightOperand() {
/*  68 */     return this.children[1];
/*     */   }
/*     */   
/*     */   public final String getOperatorName() {
/*  72 */     return this.operatorName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/*  81 */     StringBuilder sb = new StringBuilder("(");
/*  82 */     sb.append(getChild(0).toStringAST());
/*  83 */     for (int i = 1; i < getChildCount(); i++) {
/*  84 */       sb.append(' ').append(getOperatorName()).append(' ');
/*  85 */       sb.append(getChild(i).toStringAST());
/*     */     } 
/*  87 */     sb.append(')');
/*  88 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isCompilableOperatorUsingNumerics() {
/*  93 */     SpelNodeImpl left = getLeftOperand();
/*  94 */     SpelNodeImpl right = getRightOperand();
/*  95 */     if (!left.isCompilable() || !right.isCompilable()) {
/*  96 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 100 */     String leftDesc = left.exitTypeDescriptor;
/* 101 */     String rightDesc = right.exitTypeDescriptor;
/* 102 */     DescriptorComparison dc = DescriptorComparison.checkNumericCompatibility(leftDesc, rightDesc, this.leftActualDescriptor, this.rightActualDescriptor);
/*     */     
/* 104 */     return (dc.areNumbers && dc.areCompatible);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generateComparisonCode(MethodVisitor mv, CodeFlow cf, int compInstruction1, int compInstruction2) {
/* 112 */     SpelNodeImpl left = getLeftOperand();
/* 113 */     SpelNodeImpl right = getRightOperand();
/* 114 */     String leftDesc = left.exitTypeDescriptor;
/* 115 */     String rightDesc = right.exitTypeDescriptor;
/* 116 */     Label elseTarget = new Label();
/* 117 */     Label endOfIf = new Label();
/* 118 */     boolean unboxLeft = !CodeFlow.isPrimitive(leftDesc);
/* 119 */     boolean unboxRight = !CodeFlow.isPrimitive(rightDesc);
/* 120 */     DescriptorComparison dc = DescriptorComparison.checkNumericCompatibility(leftDesc, rightDesc, this.leftActualDescriptor, this.rightActualDescriptor);
/*     */     
/* 122 */     char targetType = dc.compatibleType;
/*     */     
/* 124 */     cf.enterCompilationScope();
/* 125 */     left.generateCode(mv, cf);
/* 126 */     cf.exitCompilationScope();
/* 127 */     if (CodeFlow.isPrimitive(leftDesc)) {
/* 128 */       CodeFlow.insertBoxIfNecessary(mv, leftDesc);
/* 129 */       unboxLeft = true;
/*     */     } 
/*     */     
/* 132 */     cf.enterCompilationScope();
/* 133 */     right.generateCode(mv, cf);
/* 134 */     cf.exitCompilationScope();
/* 135 */     if (CodeFlow.isPrimitive(rightDesc)) {
/* 136 */       CodeFlow.insertBoxIfNecessary(mv, rightDesc);
/* 137 */       unboxRight = true;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 142 */     Label rightIsNonNull = new Label();
/* 143 */     mv.visitInsn(89);
/* 144 */     mv.visitJumpInsn(199, rightIsNonNull);
/*     */     
/* 146 */     mv.visitInsn(95);
/* 147 */     Label leftNotNullRightIsNull = new Label();
/* 148 */     mv.visitJumpInsn(199, leftNotNullRightIsNull);
/*     */     
/* 150 */     mv.visitInsn(87);
/*     */     
/* 152 */     switch (compInstruction1) {
/*     */       case 156:
/*     */       case 158:
/* 155 */         mv.visitInsn(3);
/*     */         break;
/*     */       case 155:
/*     */       case 157:
/* 159 */         mv.visitInsn(4);
/*     */         break;
/*     */       default:
/* 162 */         throw new IllegalStateException("Unsupported: " + compInstruction1);
/*     */     } 
/* 164 */     mv.visitJumpInsn(167, endOfIf);
/* 165 */     mv.visitLabel(leftNotNullRightIsNull);
/*     */     
/* 167 */     mv.visitInsn(87);
/*     */     
/* 169 */     switch (compInstruction1) {
/*     */       case 156:
/*     */       case 157:
/* 172 */         mv.visitInsn(3);
/*     */         break;
/*     */       case 155:
/*     */       case 158:
/* 176 */         mv.visitInsn(4);
/*     */         break;
/*     */       default:
/* 179 */         throw new IllegalStateException("Unsupported: " + compInstruction1);
/*     */     } 
/* 181 */     mv.visitJumpInsn(167, endOfIf);
/*     */     
/* 183 */     mv.visitLabel(rightIsNonNull);
/*     */     
/* 185 */     mv.visitInsn(95);
/* 186 */     mv.visitInsn(89);
/* 187 */     Label neitherRightNorLeftAreNull = new Label();
/* 188 */     mv.visitJumpInsn(199, neitherRightNorLeftAreNull);
/*     */     
/* 190 */     mv.visitInsn(88);
/* 191 */     switch (compInstruction1) {
/*     */       case 156:
/*     */       case 157:
/* 194 */         mv.visitInsn(4);
/*     */         break;
/*     */       case 155:
/*     */       case 158:
/* 198 */         mv.visitInsn(3);
/*     */         break;
/*     */       default:
/* 201 */         throw new IllegalStateException("Unsupported: " + compInstruction1);
/*     */     } 
/* 203 */     mv.visitJumpInsn(167, endOfIf);
/* 204 */     mv.visitLabel(neitherRightNorLeftAreNull);
/*     */     
/* 206 */     if (unboxLeft) {
/* 207 */       CodeFlow.insertUnboxInsns(mv, targetType, leftDesc);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 212 */     if (targetType == 'D' || targetType == 'J') {
/* 213 */       mv.visitInsn(93);
/* 214 */       mv.visitInsn(88);
/*     */     } else {
/*     */       
/* 217 */       mv.visitInsn(95);
/*     */     } 
/*     */     
/* 220 */     if (unboxRight) {
/* 221 */       CodeFlow.insertUnboxInsns(mv, targetType, rightDesc);
/*     */     }
/*     */ 
/*     */     
/* 225 */     if (targetType == 'D') {
/* 226 */       mv.visitInsn(152);
/* 227 */       mv.visitJumpInsn(compInstruction1, elseTarget);
/*     */     }
/* 229 */     else if (targetType == 'F') {
/* 230 */       mv.visitInsn(150);
/* 231 */       mv.visitJumpInsn(compInstruction1, elseTarget);
/*     */     }
/* 233 */     else if (targetType == 'J') {
/* 234 */       mv.visitInsn(148);
/* 235 */       mv.visitJumpInsn(compInstruction1, elseTarget);
/*     */     }
/* 237 */     else if (targetType == 'I') {
/* 238 */       mv.visitJumpInsn(compInstruction2, elseTarget);
/*     */     } else {
/*     */       
/* 241 */       throw new IllegalStateException("Unexpected descriptor " + leftDesc);
/*     */     } 
/*     */ 
/*     */     
/* 245 */     mv.visitInsn(4);
/* 246 */     mv.visitJumpInsn(167, endOfIf);
/* 247 */     mv.visitLabel(elseTarget);
/* 248 */     mv.visitInsn(3);
/* 249 */     mv.visitLabel(endOfIf);
/* 250 */     cf.pushDescriptor("Z");
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
/*     */   
/*     */   public static boolean equalityCheck(EvaluationContext context, @Nullable Object left, @Nullable Object right) {
/* 264 */     if (left instanceof Number && right instanceof Number) {
/* 265 */       Number leftNumber = (Number)left;
/* 266 */       Number rightNumber = (Number)right;
/*     */       
/* 268 */       if (leftNumber instanceof BigDecimal || rightNumber instanceof BigDecimal) {
/* 269 */         BigDecimal leftBigDecimal = (BigDecimal)NumberUtils.convertNumberToTargetClass(leftNumber, BigDecimal.class);
/* 270 */         BigDecimal rightBigDecimal = (BigDecimal)NumberUtils.convertNumberToTargetClass(rightNumber, BigDecimal.class);
/* 271 */         return (leftBigDecimal.compareTo(rightBigDecimal) == 0);
/*     */       } 
/* 273 */       if (leftNumber instanceof Double || rightNumber instanceof Double) {
/* 274 */         return (leftNumber.doubleValue() == rightNumber.doubleValue());
/*     */       }
/* 276 */       if (leftNumber instanceof Float || rightNumber instanceof Float) {
/* 277 */         return (leftNumber.floatValue() == rightNumber.floatValue());
/*     */       }
/* 279 */       if (leftNumber instanceof BigInteger || rightNumber instanceof BigInteger) {
/* 280 */         BigInteger leftBigInteger = (BigInteger)NumberUtils.convertNumberToTargetClass(leftNumber, BigInteger.class);
/* 281 */         BigInteger rightBigInteger = (BigInteger)NumberUtils.convertNumberToTargetClass(rightNumber, BigInteger.class);
/* 282 */         return (leftBigInteger.compareTo(rightBigInteger) == 0);
/*     */       } 
/* 284 */       if (leftNumber instanceof Long || rightNumber instanceof Long) {
/* 285 */         return (leftNumber.longValue() == rightNumber.longValue());
/*     */       }
/* 287 */       if (leftNumber instanceof Integer || rightNumber instanceof Integer) {
/* 288 */         return (leftNumber.intValue() == rightNumber.intValue());
/*     */       }
/* 290 */       if (leftNumber instanceof Short || rightNumber instanceof Short) {
/* 291 */         return (leftNumber.shortValue() == rightNumber.shortValue());
/*     */       }
/* 293 */       if (leftNumber instanceof Byte || rightNumber instanceof Byte) {
/* 294 */         return (leftNumber.byteValue() == rightNumber.byteValue());
/*     */       }
/*     */ 
/*     */       
/* 298 */       return (leftNumber.doubleValue() == rightNumber.doubleValue());
/*     */     } 
/*     */ 
/*     */     
/* 302 */     if (left instanceof CharSequence && right instanceof CharSequence) {
/* 303 */       return left.toString().equals(right.toString());
/*     */     }
/*     */     
/* 306 */     if (left instanceof Boolean && right instanceof Boolean) {
/* 307 */       return left.equals(right);
/*     */     }
/*     */     
/* 310 */     if (ObjectUtils.nullSafeEquals(left, right)) {
/* 311 */       return true;
/*     */     }
/*     */     
/* 314 */     if (left instanceof Comparable && right instanceof Comparable) {
/* 315 */       Class<?> ancestor = ClassUtils.determineCommonAncestor(left.getClass(), right.getClass());
/* 316 */       if (ancestor != null && Comparable.class.isAssignableFrom(ancestor)) {
/* 317 */         return (context.getTypeComparator().compare(left, right) == 0);
/*     */       }
/*     */     } 
/*     */     
/* 321 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final class DescriptorComparison
/*     */   {
/* 331 */     static final DescriptorComparison NOT_NUMBERS = new DescriptorComparison(false, false, ' ');
/*     */     
/* 333 */     static final DescriptorComparison INCOMPATIBLE_NUMBERS = new DescriptorComparison(true, false, ' ');
/*     */     
/*     */     final boolean areNumbers;
/*     */     
/*     */     final boolean areCompatible;
/*     */     
/*     */     final char compatibleType;
/*     */     
/*     */     private DescriptorComparison(boolean areNumbers, boolean areCompatible, char compatibleType) {
/* 342 */       this.areNumbers = areNumbers;
/* 343 */       this.areCompatible = areCompatible;
/* 344 */       this.compatibleType = compatibleType;
/*     */     }
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
/*     */     public static DescriptorComparison checkNumericCompatibility(@Nullable String leftDeclaredDescriptor, @Nullable String rightDeclaredDescriptor, @Nullable String leftActualDescriptor, @Nullable String rightActualDescriptor) {
/* 365 */       String ld = leftDeclaredDescriptor;
/* 366 */       String rd = rightDeclaredDescriptor;
/*     */       
/* 368 */       boolean leftNumeric = CodeFlow.isPrimitiveOrUnboxableSupportedNumberOrBoolean(ld);
/* 369 */       boolean rightNumeric = CodeFlow.isPrimitiveOrUnboxableSupportedNumberOrBoolean(rd);
/*     */ 
/*     */       
/* 372 */       if (!leftNumeric && !ObjectUtils.nullSafeEquals(ld, leftActualDescriptor)) {
/* 373 */         ld = leftActualDescriptor;
/* 374 */         leftNumeric = CodeFlow.isPrimitiveOrUnboxableSupportedNumberOrBoolean(ld);
/*     */       } 
/* 376 */       if (!rightNumeric && !ObjectUtils.nullSafeEquals(rd, rightActualDescriptor)) {
/* 377 */         rd = rightActualDescriptor;
/* 378 */         rightNumeric = CodeFlow.isPrimitiveOrUnboxableSupportedNumberOrBoolean(rd);
/*     */       } 
/*     */       
/* 381 */       if (leftNumeric && rightNumeric) {
/* 382 */         if (CodeFlow.areBoxingCompatible(ld, rd)) {
/* 383 */           return new DescriptorComparison(true, true, CodeFlow.toPrimitiveTargetDesc(ld));
/*     */         }
/*     */         
/* 386 */         return INCOMPATIBLE_NUMBERS;
/*     */       } 
/*     */ 
/*     */       
/* 390 */       return NOT_NUMBERS;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\Operator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */