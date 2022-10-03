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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CompositeStringExpression
/*     */   implements Expression
/*     */ {
/*     */   private final String expressionString;
/*     */   private final Expression[] expressions;
/*     */   
/*     */   public CompositeStringExpression(String expressionString, Expression[] expressions) {
/*  52 */     this.expressionString = expressionString;
/*  53 */     this.expressions = expressions;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getExpressionString() {
/*  59 */     return this.expressionString;
/*     */   }
/*     */   
/*     */   public final Expression[] getExpressions() {
/*  63 */     return this.expressions;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getValue() throws EvaluationException {
/*  68 */     StringBuilder sb = new StringBuilder();
/*  69 */     for (Expression expression : this.expressions) {
/*  70 */       String value = (String)expression.getValue(String.class);
/*  71 */       if (value != null) {
/*  72 */         sb.append(value);
/*     */       }
/*     */     } 
/*  75 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <T> T getValue(@Nullable Class<T> expectedResultType) throws EvaluationException {
/*  81 */     Object value = getValue();
/*  82 */     return ExpressionUtils.convertTypedValue(null, new TypedValue(value), expectedResultType);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getValue(@Nullable Object rootObject) throws EvaluationException {
/*  87 */     StringBuilder sb = new StringBuilder();
/*  88 */     for (Expression expression : this.expressions) {
/*  89 */       String value = (String)expression.getValue(rootObject, String.class);
/*  90 */       if (value != null) {
/*  91 */         sb.append(value);
/*     */       }
/*     */     } 
/*  94 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <T> T getValue(@Nullable Object rootObject, @Nullable Class<T> desiredResultType) throws EvaluationException {
/* 100 */     Object value = getValue(rootObject);
/* 101 */     return ExpressionUtils.convertTypedValue(null, new TypedValue(value), desiredResultType);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getValue(EvaluationContext context) throws EvaluationException {
/* 106 */     StringBuilder sb = new StringBuilder();
/* 107 */     for (Expression expression : this.expressions) {
/* 108 */       String value = (String)expression.getValue(context, String.class);
/* 109 */       if (value != null) {
/* 110 */         sb.append(value);
/*     */       }
/*     */     } 
/* 113 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <T> T getValue(EvaluationContext context, @Nullable Class<T> expectedResultType) throws EvaluationException {
/* 121 */     Object value = getValue(context);
/* 122 */     return ExpressionUtils.convertTypedValue(context, new TypedValue(value), expectedResultType);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getValue(EvaluationContext context, @Nullable Object rootObject) throws EvaluationException {
/* 127 */     StringBuilder sb = new StringBuilder();
/* 128 */     for (Expression expression : this.expressions) {
/* 129 */       String value = (String)expression.getValue(context, rootObject, String.class);
/* 130 */       if (value != null) {
/* 131 */         sb.append(value);
/*     */       }
/*     */     } 
/* 134 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <T> T getValue(EvaluationContext context, @Nullable Object rootObject, @Nullable Class<T> desiredResultType) throws EvaluationException {
/* 142 */     Object value = getValue(context, rootObject);
/* 143 */     return ExpressionUtils.convertTypedValue(context, new TypedValue(value), desiredResultType);
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getValueType() {
/* 148 */     return String.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getValueType(EvaluationContext context) {
/* 153 */     return String.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getValueType(@Nullable Object rootObject) throws EvaluationException {
/* 158 */     return String.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getValueType(EvaluationContext context, @Nullable Object rootObject) throws EvaluationException {
/* 163 */     return String.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeDescriptor getValueTypeDescriptor() {
/* 168 */     return TypeDescriptor.valueOf(String.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeDescriptor getValueTypeDescriptor(@Nullable Object rootObject) throws EvaluationException {
/* 173 */     return TypeDescriptor.valueOf(String.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeDescriptor getValueTypeDescriptor(EvaluationContext context) {
/* 178 */     return TypeDescriptor.valueOf(String.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeDescriptor getValueTypeDescriptor(EvaluationContext context, @Nullable Object rootObject) throws EvaluationException {
/* 185 */     return TypeDescriptor.valueOf(String.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWritable(@Nullable Object rootObject) throws EvaluationException {
/* 190 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWritable(EvaluationContext context) {
/* 195 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWritable(EvaluationContext context, @Nullable Object rootObject) throws EvaluationException {
/* 200 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(@Nullable Object rootObject, @Nullable Object value) throws EvaluationException {
/* 205 */     throw new EvaluationException(this.expressionString, "Cannot call setValue on a composite expression");
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(EvaluationContext context, @Nullable Object value) throws EvaluationException {
/* 210 */     throw new EvaluationException(this.expressionString, "Cannot call setValue on a composite expression");
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(EvaluationContext context, @Nullable Object rootObject, @Nullable Object value) throws EvaluationException {
/* 215 */     throw new EvaluationException(this.expressionString, "Cannot call setValue on a composite expression");
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\common\CompositeStringExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */