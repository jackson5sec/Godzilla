/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public class Projection
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   private final boolean nullSafe;
/*     */   
/*     */   public Projection(boolean nullSafe, int startPos, int endPos, SpelNodeImpl expression) {
/*  50 */     super(startPos, endPos, new SpelNodeImpl[] { expression });
/*  51 */     this.nullSafe = nullSafe;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*  57 */     return getValueRef(state).getValue();
/*     */   }
/*     */ 
/*     */   
/*     */   protected ValueRef getValueRef(ExpressionState state) throws EvaluationException {
/*  62 */     TypedValue op = state.getActiveContextObject();
/*     */     
/*  64 */     Object operand = op.getValue();
/*  65 */     boolean operandIsArray = ObjectUtils.isArray(operand);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  73 */     if (operand instanceof Map) {
/*  74 */       Map<?, ?> mapData = (Map<?, ?>)operand;
/*  75 */       List<Object> result = new ArrayList();
/*  76 */       for (Map.Entry<?, ?> entry : mapData.entrySet()) {
/*     */         try {
/*  78 */           state.pushActiveContextObject(new TypedValue(entry));
/*  79 */           state.enterScope();
/*  80 */           result.add(this.children[0].getValueInternal(state).getValue());
/*     */         } finally {
/*     */           
/*  83 */           state.popActiveContextObject();
/*  84 */           state.exitScope();
/*     */         } 
/*     */       } 
/*  87 */       return new ValueRef.TypedValueHolderValueRef(new TypedValue(result), this);
/*     */     } 
/*     */     
/*  90 */     if (operand instanceof Iterable || operandIsArray) {
/*     */       
/*  92 */       Iterable<?> data = (operand instanceof Iterable) ? (Iterable)operand : Arrays.asList(ObjectUtils.toObjectArray(operand));
/*     */       
/*  94 */       List<Object> result = new ArrayList();
/*  95 */       Class<?> arrayElementType = null;
/*  96 */       for (Object element : data) {
/*     */         try {
/*  98 */           state.pushActiveContextObject(new TypedValue(element));
/*  99 */           state.enterScope("index", Integer.valueOf(result.size()));
/* 100 */           Object value = this.children[0].getValueInternal(state).getValue();
/* 101 */           if (value != null && operandIsArray) {
/* 102 */             arrayElementType = determineCommonType(arrayElementType, value.getClass());
/*     */           }
/* 104 */           result.add(value);
/*     */         } finally {
/*     */           
/* 107 */           state.exitScope();
/* 108 */           state.popActiveContextObject();
/*     */         } 
/*     */       } 
/*     */       
/* 112 */       if (operandIsArray) {
/* 113 */         if (arrayElementType == null) {
/* 114 */           arrayElementType = Object.class;
/*     */         }
/* 116 */         Object resultArray = Array.newInstance(arrayElementType, result.size());
/* 117 */         System.arraycopy(result.toArray(), 0, resultArray, 0, result.size());
/* 118 */         return new ValueRef.TypedValueHolderValueRef(new TypedValue(resultArray), this);
/*     */       } 
/*     */       
/* 121 */       return new ValueRef.TypedValueHolderValueRef(new TypedValue(result), this);
/*     */     } 
/*     */     
/* 124 */     if (operand == null) {
/* 125 */       if (this.nullSafe) {
/* 126 */         return ValueRef.NullValueRef.INSTANCE;
/*     */       }
/* 128 */       throw new SpelEvaluationException(getStartPosition(), SpelMessage.PROJECTION_NOT_SUPPORTED_ON_TYPE, new Object[] { "null" });
/*     */     } 
/*     */     
/* 131 */     throw new SpelEvaluationException(getStartPosition(), SpelMessage.PROJECTION_NOT_SUPPORTED_ON_TYPE, new Object[] { operand
/* 132 */           .getClass().getName() });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/* 137 */     return "![" + getChild(0).toStringAST() + "]";
/*     */   }
/*     */   
/*     */   private Class<?> determineCommonType(@Nullable Class<?> oldType, Class<?> newType) {
/* 141 */     if (oldType == null) {
/* 142 */       return newType;
/*     */     }
/* 144 */     if (oldType.isAssignableFrom(newType)) {
/* 145 */       return oldType;
/*     */     }
/* 147 */     Class<?> nextType = newType;
/* 148 */     while (nextType != Object.class) {
/* 149 */       if (nextType.isAssignableFrom(oldType)) {
/* 150 */         return nextType;
/*     */       }
/* 152 */       nextType = nextType.getSuperclass();
/*     */     } 
/* 154 */     for (Class<?> nextInterface : (Iterable<Class<?>>)ClassUtils.getAllInterfacesForClassAsSet(newType)) {
/* 155 */       if (nextInterface.isAssignableFrom(oldType)) {
/* 156 */         return nextInterface;
/*     */       }
/*     */     } 
/* 159 */     return Object.class;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\Projection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */