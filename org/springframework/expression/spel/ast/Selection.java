/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.CollectionUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Selection
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   public static final int ALL = 0;
/*     */   public static final int FIRST = 1;
/*     */   public static final int LAST = 2;
/*     */   private final int variant;
/*     */   private final boolean nullSafe;
/*     */   
/*     */   public Selection(boolean nullSafe, int variant, int startPos, int endPos, SpelNodeImpl expression) {
/*  73 */     super(startPos, endPos, new SpelNodeImpl[] { expression });
/*  74 */     this.nullSafe = nullSafe;
/*  75 */     this.variant = variant;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*  81 */     return getValueRef(state).getValue();
/*     */   }
/*     */ 
/*     */   
/*     */   protected ValueRef getValueRef(ExpressionState state) throws EvaluationException {
/*  86 */     TypedValue op = state.getActiveContextObject();
/*  87 */     Object operand = op.getValue();
/*  88 */     SpelNodeImpl selectionCriteria = this.children[0];
/*     */     
/*  90 */     if (operand instanceof Map) {
/*  91 */       Map<?, ?> mapdata = (Map<?, ?>)operand;
/*     */       
/*  93 */       Map<Object, Object> result = new HashMap<>();
/*  94 */       Object lastKey = null;
/*     */       
/*  96 */       for (Map.Entry<?, ?> entry : mapdata.entrySet()) {
/*     */         try {
/*  98 */           TypedValue kvPair = new TypedValue(entry);
/*  99 */           state.pushActiveContextObject(kvPair);
/* 100 */           state.enterScope();
/* 101 */           Object val = selectionCriteria.getValueInternal(state).getValue();
/* 102 */           if (val instanceof Boolean) {
/* 103 */             if (((Boolean)val).booleanValue()) {
/* 104 */               if (this.variant == 1) {
/* 105 */                 result.put(entry.getKey(), entry.getValue());
/* 106 */                 return new ValueRef.TypedValueHolderValueRef(new TypedValue(result), this);
/*     */               } 
/* 108 */               result.put(entry.getKey(), entry.getValue());
/* 109 */               lastKey = entry.getKey();
/*     */             } 
/*     */           } else {
/*     */             
/* 113 */             throw new SpelEvaluationException(selectionCriteria.getStartPosition(), SpelMessage.RESULT_OF_SELECTION_CRITERIA_IS_NOT_BOOLEAN, new Object[0]);
/*     */           }
/*     */         
/*     */         } finally {
/*     */           
/* 118 */           state.popActiveContextObject();
/* 119 */           state.exitScope();
/*     */         } 
/*     */       } 
/*     */       
/* 123 */       if ((this.variant == 1 || this.variant == 2) && result.isEmpty()) {
/* 124 */         return new ValueRef.TypedValueHolderValueRef(new TypedValue(null), this);
/*     */       }
/*     */       
/* 127 */       if (this.variant == 2) {
/* 128 */         Map<Object, Object> resultMap = new HashMap<>();
/* 129 */         Object lastValue = result.get(lastKey);
/* 130 */         resultMap.put(lastKey, lastValue);
/* 131 */         return new ValueRef.TypedValueHolderValueRef(new TypedValue(resultMap), this);
/*     */       } 
/*     */       
/* 134 */       return new ValueRef.TypedValueHolderValueRef(new TypedValue(result), this);
/*     */     } 
/*     */     
/* 137 */     if (operand instanceof Iterable || ObjectUtils.isArray(operand)) {
/*     */       
/* 139 */       Iterable<?> data = (operand instanceof Iterable) ? (Iterable)operand : Arrays.asList(ObjectUtils.toObjectArray(operand));
/*     */       
/* 141 */       List<Object> result = new ArrayList();
/* 142 */       int index = 0;
/* 143 */       for (Object element : data) {
/*     */         try {
/* 145 */           state.pushActiveContextObject(new TypedValue(element));
/* 146 */           state.enterScope("index", Integer.valueOf(index));
/* 147 */           Object val = selectionCriteria.getValueInternal(state).getValue();
/* 148 */           if (val instanceof Boolean) {
/* 149 */             if (((Boolean)val).booleanValue()) {
/* 150 */               if (this.variant == 1) {
/* 151 */                 return new ValueRef.TypedValueHolderValueRef(new TypedValue(element), this);
/*     */               }
/* 153 */               result.add(element);
/*     */             } 
/*     */           } else {
/*     */             
/* 157 */             throw new SpelEvaluationException(selectionCriteria.getStartPosition(), SpelMessage.RESULT_OF_SELECTION_CRITERIA_IS_NOT_BOOLEAN, new Object[0]);
/*     */           } 
/*     */           
/* 160 */           index++;
/*     */         } finally {
/*     */           
/* 163 */           state.exitScope();
/* 164 */           state.popActiveContextObject();
/*     */         } 
/*     */       } 
/*     */       
/* 168 */       if ((this.variant == 1 || this.variant == 2) && result.isEmpty()) {
/* 169 */         return ValueRef.NullValueRef.INSTANCE;
/*     */       }
/*     */       
/* 172 */       if (this.variant == 2) {
/* 173 */         return new ValueRef.TypedValueHolderValueRef(new TypedValue(CollectionUtils.lastElement(result)), this);
/*     */       }
/*     */       
/* 176 */       if (operand instanceof Iterable) {
/* 177 */         return new ValueRef.TypedValueHolderValueRef(new TypedValue(result), this);
/*     */       }
/*     */       
/* 180 */       Class<?> elementType = null;
/* 181 */       TypeDescriptor typeDesc = op.getTypeDescriptor();
/* 182 */       if (typeDesc != null) {
/* 183 */         TypeDescriptor elementTypeDesc = typeDesc.getElementTypeDescriptor();
/* 184 */         if (elementTypeDesc != null) {
/* 185 */           elementType = ClassUtils.resolvePrimitiveIfNecessary(elementTypeDesc.getType());
/*     */         }
/*     */       } 
/* 188 */       Assert.state((elementType != null), "Unresolvable element type");
/*     */       
/* 190 */       Object resultArray = Array.newInstance(elementType, result.size());
/* 191 */       System.arraycopy(result.toArray(), 0, resultArray, 0, result.size());
/* 192 */       return new ValueRef.TypedValueHolderValueRef(new TypedValue(resultArray), this);
/*     */     } 
/*     */     
/* 195 */     if (operand == null) {
/* 196 */       if (this.nullSafe) {
/* 197 */         return ValueRef.NullValueRef.INSTANCE;
/*     */       }
/* 199 */       throw new SpelEvaluationException(getStartPosition(), SpelMessage.INVALID_TYPE_FOR_SELECTION, new Object[] { "null" });
/*     */     } 
/*     */     
/* 202 */     throw new SpelEvaluationException(getStartPosition(), SpelMessage.INVALID_TYPE_FOR_SELECTION, new Object[] { operand
/* 203 */           .getClass().getName() });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/* 208 */     return prefix() + getChild(0).toStringAST() + "]";
/*     */   }
/*     */   
/*     */   private String prefix() {
/* 212 */     switch (this.variant) { case 0:
/* 213 */         return "?[";
/* 214 */       case 1: return "^[";
/* 215 */       case 2: return "$["; }
/*     */     
/* 217 */     return "";
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\Selection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */