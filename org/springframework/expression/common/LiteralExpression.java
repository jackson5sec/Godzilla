/*     */ package org.springframework.expression.common;
/*     */ 
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.Expression;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public class LiteralExpression
/*     */   implements Expression
/*     */ {
/*     */   private final String literalValue;
/*     */   
/*     */   public LiteralExpression(String literalValue) {
/*  43 */     this.literalValue = literalValue;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getExpressionString() {
/*  49 */     return this.literalValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getValueType(EvaluationContext context) {
/*  54 */     return String.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getValue() {
/*  59 */     return this.literalValue;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <T> T getValue(@Nullable Class<T> expectedResultType) throws EvaluationException {
/*  65 */     Object value = getValue();
/*  66 */     return ExpressionUtils.convertTypedValue(null, new TypedValue(value), expectedResultType);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getValue(@Nullable Object rootObject) {
/*  71 */     return this.literalValue;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <T> T getValue(@Nullable Object rootObject, @Nullable Class<T> desiredResultType) throws EvaluationException {
/*  77 */     Object value = getValue(rootObject);
/*  78 */     return ExpressionUtils.convertTypedValue(null, new TypedValue(value), desiredResultType);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getValue(EvaluationContext context) {
/*  83 */     return this.literalValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <T> T getValue(EvaluationContext context, @Nullable Class<T> expectedResultType) throws EvaluationException {
/*  91 */     Object value = getValue(context);
/*  92 */     return ExpressionUtils.convertTypedValue(context, new TypedValue(value), expectedResultType);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getValue(EvaluationContext context, @Nullable Object rootObject) throws EvaluationException {
/*  97 */     return this.literalValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <T> T getValue(EvaluationContext context, @Nullable Object rootObject, @Nullable Class<T> desiredResultType) throws EvaluationException {
/* 105 */     Object value = getValue(context, rootObject);
/* 106 */     return ExpressionUtils.convertTypedValue(context, new TypedValue(value), desiredResultType);
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getValueType() {
/* 111 */     return String.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getValueType(@Nullable Object rootObject) throws EvaluationException {
/* 116 */     return String.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getValueType(EvaluationContext context, @Nullable Object rootObject) throws EvaluationException {
/* 121 */     return String.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeDescriptor getValueTypeDescriptor() {
/* 126 */     return TypeDescriptor.valueOf(String.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeDescriptor getValueTypeDescriptor(@Nullable Object rootObject) throws EvaluationException {
/* 131 */     return TypeDescriptor.valueOf(String.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeDescriptor getValueTypeDescriptor(EvaluationContext context) {
/* 136 */     return TypeDescriptor.valueOf(String.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeDescriptor getValueTypeDescriptor(EvaluationContext context, @Nullable Object rootObject) throws EvaluationException {
/* 141 */     return TypeDescriptor.valueOf(String.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWritable(@Nullable Object rootObject) throws EvaluationException {
/* 146 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWritable(EvaluationContext context) {
/* 151 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWritable(EvaluationContext context, @Nullable Object rootObject) throws EvaluationException {
/* 156 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(@Nullable Object rootObject, @Nullable Object value) throws EvaluationException {
/* 161 */     throw new EvaluationException(this.literalValue, "Cannot call setValue() on a LiteralExpression");
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(EvaluationContext context, @Nullable Object value) throws EvaluationException {
/* 166 */     throw new EvaluationException(this.literalValue, "Cannot call setValue() on a LiteralExpression");
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(EvaluationContext context, @Nullable Object rootObject, @Nullable Object value) throws EvaluationException {
/* 171 */     throw new EvaluationException(this.literalValue, "Cannot call setValue() on a LiteralExpression");
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\common\LiteralExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */