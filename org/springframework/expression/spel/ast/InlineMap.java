/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelNode;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public class InlineMap
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   @Nullable
/*     */   private TypedValue constant;
/*     */   
/*     */   public InlineMap(int startPos, int endPos, SpelNodeImpl... args) {
/*  44 */     super(startPos, endPos, args);
/*  45 */     checkIfConstant();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkIfConstant() {
/*  55 */     boolean isConstant = true;
/*  56 */     for (int c = 0, max = getChildCount(); c < max; c++) {
/*  57 */       SpelNode child = getChild(c);
/*  58 */       if (!(child instanceof Literal)) {
/*  59 */         if (child instanceof InlineList) {
/*  60 */           InlineList inlineList = (InlineList)child;
/*  61 */           if (!inlineList.isConstant()) {
/*  62 */             isConstant = false;
/*     */             
/*     */             break;
/*     */           } 
/*  66 */         } else if (child instanceof InlineMap) {
/*  67 */           InlineMap inlineMap = (InlineMap)child;
/*  68 */           if (!inlineMap.isConstant()) {
/*  69 */             isConstant = false;
/*     */             
/*     */             break;
/*     */           } 
/*  73 */         } else if (c % 2 != 0 || !(child instanceof PropertyOrFieldReference)) {
/*  74 */           isConstant = false;
/*     */           break;
/*     */         } 
/*     */       }
/*     */     } 
/*  79 */     if (isConstant) {
/*  80 */       Map<Object, Object> constantMap = new LinkedHashMap<>();
/*  81 */       int childCount = getChildCount();
/*  82 */       for (int i = 0; i < childCount; i++) {
/*  83 */         SpelNode keyChild = getChild(i++);
/*  84 */         SpelNode valueChild = getChild(i);
/*  85 */         Object key = null;
/*  86 */         Object<Object> value = null;
/*  87 */         if (keyChild instanceof Literal) {
/*  88 */           key = ((Literal)keyChild).getLiteralValue().getValue();
/*     */         }
/*  90 */         else if (keyChild instanceof PropertyOrFieldReference) {
/*  91 */           key = ((PropertyOrFieldReference)keyChild).getName();
/*     */         } else {
/*     */           return;
/*     */         } 
/*     */         
/*  96 */         if (valueChild instanceof Literal) {
/*  97 */           value = (Object<Object>)((Literal)valueChild).getLiteralValue().getValue();
/*     */         }
/*  99 */         else if (valueChild instanceof InlineList) {
/* 100 */           value = (Object<Object>)((InlineList)valueChild).getConstantValue();
/*     */         }
/* 102 */         else if (valueChild instanceof InlineMap) {
/* 103 */           value = (Object)((InlineMap)valueChild).getConstantValue();
/*     */         } 
/* 105 */         constantMap.put(key, value);
/*     */       } 
/* 107 */       this.constant = new TypedValue(Collections.unmodifiableMap(constantMap));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState expressionState) throws EvaluationException {
/* 113 */     if (this.constant != null) {
/* 114 */       return this.constant;
/*     */     }
/*     */     
/* 117 */     Map<Object, Object> returnValue = new LinkedHashMap<>();
/* 118 */     int childcount = getChildCount();
/* 119 */     for (int c = 0; c < childcount; c++) {
/*     */       
/* 121 */       SpelNode keyChild = getChild(c++);
/* 122 */       Object key = null;
/* 123 */       if (keyChild instanceof PropertyOrFieldReference) {
/* 124 */         PropertyOrFieldReference reference = (PropertyOrFieldReference)keyChild;
/* 125 */         key = reference.getName();
/*     */       } else {
/*     */         
/* 128 */         key = keyChild.getValue(expressionState);
/*     */       } 
/* 130 */       Object value = getChild(c).getValue(expressionState);
/* 131 */       returnValue.put(key, value);
/*     */     } 
/* 133 */     return new TypedValue(returnValue);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/* 139 */     StringBuilder sb = new StringBuilder("{");
/* 140 */     int count = getChildCount();
/* 141 */     for (int c = 0; c < count; c++) {
/* 142 */       if (c > 0) {
/* 143 */         sb.append(',');
/*     */       }
/* 145 */       sb.append(getChild(c++).toStringAST());
/* 146 */       sb.append(':');
/* 147 */       sb.append(getChild(c).toStringAST());
/*     */     } 
/* 149 */     sb.append('}');
/* 150 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConstant() {
/* 157 */     return (this.constant != null);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Map<Object, Object> getConstantValue() {
/* 163 */     Assert.state((this.constant != null), "No constant");
/* 164 */     return (Map<Object, Object>)this.constant.getValue();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\InlineMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */