/*     */ package org.springframework.expression.spel;
/*     */ 
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Collections;
/*     */ import java.util.Deque;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.Operation;
/*     */ import org.springframework.expression.OperatorOverloader;
/*     */ import org.springframework.expression.PropertyAccessor;
/*     */ import org.springframework.expression.TypeComparator;
/*     */ import org.springframework.expression.TypeConverter;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
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
/*     */ 
/*     */ 
/*     */ public class ExpressionState
/*     */ {
/*     */   private final EvaluationContext relatedContext;
/*     */   private final TypedValue rootObject;
/*     */   private final SpelParserConfiguration configuration;
/*     */   @Nullable
/*     */   private Deque<TypedValue> contextObjects;
/*     */   @Nullable
/*     */   private Deque<VariableScope> variableScopes;
/*     */   @Nullable
/*     */   private ArrayDeque<TypedValue> scopeRootObjects;
/*     */   
/*     */   public ExpressionState(EvaluationContext context) {
/*  81 */     this(context, context.getRootObject(), new SpelParserConfiguration(false, false));
/*     */   }
/*     */   
/*     */   public ExpressionState(EvaluationContext context, SpelParserConfiguration configuration) {
/*  85 */     this(context, context.getRootObject(), configuration);
/*     */   }
/*     */   
/*     */   public ExpressionState(EvaluationContext context, TypedValue rootObject) {
/*  89 */     this(context, rootObject, new SpelParserConfiguration(false, false));
/*     */   }
/*     */   
/*     */   public ExpressionState(EvaluationContext context, TypedValue rootObject, SpelParserConfiguration configuration) {
/*  93 */     Assert.notNull(context, "EvaluationContext must not be null");
/*  94 */     Assert.notNull(configuration, "SpelParserConfiguration must not be null");
/*  95 */     this.relatedContext = context;
/*  96 */     this.rootObject = rootObject;
/*  97 */     this.configuration = configuration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedValue getActiveContextObject() {
/* 105 */     if (CollectionUtils.isEmpty(this.contextObjects)) {
/* 106 */       return this.rootObject;
/*     */     }
/* 108 */     return this.contextObjects.element();
/*     */   }
/*     */   
/*     */   public void pushActiveContextObject(TypedValue obj) {
/* 112 */     if (this.contextObjects == null) {
/* 113 */       this.contextObjects = new ArrayDeque<>();
/*     */     }
/* 115 */     this.contextObjects.push(obj);
/*     */   }
/*     */   
/*     */   public void popActiveContextObject() {
/* 119 */     if (this.contextObjects == null) {
/* 120 */       this.contextObjects = new ArrayDeque<>();
/*     */     }
/*     */     try {
/* 123 */       this.contextObjects.pop();
/*     */     }
/* 125 */     catch (NoSuchElementException ex) {
/* 126 */       throw new IllegalStateException("Cannot pop active context object: stack is empty");
/*     */     } 
/*     */   }
/*     */   
/*     */   public TypedValue getRootContextObject() {
/* 131 */     return this.rootObject;
/*     */   }
/*     */   
/*     */   public TypedValue getScopeRootContextObject() {
/* 135 */     if (CollectionUtils.isEmpty(this.scopeRootObjects)) {
/* 136 */       return this.rootObject;
/*     */     }
/* 138 */     return this.scopeRootObjects.element();
/*     */   }
/*     */   
/*     */   public void setVariable(String name, @Nullable Object value) {
/* 142 */     this.relatedContext.setVariable(name, value);
/*     */   }
/*     */   
/*     */   public TypedValue lookupVariable(String name) {
/* 146 */     Object value = this.relatedContext.lookupVariable(name);
/* 147 */     return (value != null) ? new TypedValue(value) : TypedValue.NULL;
/*     */   }
/*     */   
/*     */   public TypeComparator getTypeComparator() {
/* 151 */     return this.relatedContext.getTypeComparator();
/*     */   }
/*     */   
/*     */   public Class<?> findType(String type) throws EvaluationException {
/* 155 */     return this.relatedContext.getTypeLocator().findType(type);
/*     */   }
/*     */   
/*     */   public Object convertValue(Object value, TypeDescriptor targetTypeDescriptor) throws EvaluationException {
/* 159 */     Object result = this.relatedContext.getTypeConverter().convertValue(value, 
/* 160 */         TypeDescriptor.forObject(value), targetTypeDescriptor);
/* 161 */     if (result == null) {
/* 162 */       throw new IllegalStateException("Null conversion result for value [" + value + "]");
/*     */     }
/* 164 */     return result;
/*     */   }
/*     */   
/*     */   public TypeConverter getTypeConverter() {
/* 168 */     return this.relatedContext.getTypeConverter();
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Object convertValue(TypedValue value, TypeDescriptor targetTypeDescriptor) throws EvaluationException {
/* 173 */     Object val = value.getValue();
/* 174 */     return this.relatedContext.getTypeConverter().convertValue(val, 
/* 175 */         TypeDescriptor.forObject(val), targetTypeDescriptor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterScope(Map<String, Object> argMap) {
/* 182 */     initVariableScopes().push(new VariableScope(argMap));
/* 183 */     initScopeRootObjects().push(getActiveContextObject());
/*     */   }
/*     */   
/*     */   public void enterScope() {
/* 187 */     initVariableScopes().push(new VariableScope(Collections.emptyMap()));
/* 188 */     initScopeRootObjects().push(getActiveContextObject());
/*     */   }
/*     */   
/*     */   public void enterScope(String name, Object value) {
/* 192 */     initVariableScopes().push(new VariableScope(name, value));
/* 193 */     initScopeRootObjects().push(getActiveContextObject());
/*     */   }
/*     */   
/*     */   public void exitScope() {
/* 197 */     initVariableScopes().pop();
/* 198 */     initScopeRootObjects().pop();
/*     */   }
/*     */   
/*     */   public void setLocalVariable(String name, Object value) {
/* 202 */     ((VariableScope)initVariableScopes().element()).setVariable(name, value);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Object lookupLocalVariable(String name) {
/* 207 */     for (VariableScope scope : initVariableScopes()) {
/* 208 */       if (scope.definesVariable(name)) {
/* 209 */         return scope.lookupVariable(name);
/*     */       }
/*     */     } 
/* 212 */     return null;
/*     */   }
/*     */   
/*     */   private Deque<VariableScope> initVariableScopes() {
/* 216 */     if (this.variableScopes == null) {
/* 217 */       this.variableScopes = new ArrayDeque<>();
/*     */       
/* 219 */       this.variableScopes.add(new VariableScope());
/*     */     } 
/* 221 */     return this.variableScopes;
/*     */   }
/*     */   
/*     */   private Deque<TypedValue> initScopeRootObjects() {
/* 225 */     if (this.scopeRootObjects == null) {
/* 226 */       this.scopeRootObjects = new ArrayDeque<>();
/*     */     }
/* 228 */     return this.scopeRootObjects;
/*     */   }
/*     */   
/*     */   public TypedValue operate(Operation op, @Nullable Object left, @Nullable Object right) throws EvaluationException {
/* 232 */     OperatorOverloader overloader = this.relatedContext.getOperatorOverloader();
/* 233 */     if (overloader.overridesOperation(op, left, right)) {
/* 234 */       Object returnValue = overloader.operate(op, left, right);
/* 235 */       return new TypedValue(returnValue);
/*     */     } 
/*     */     
/* 238 */     String leftType = (left == null) ? "null" : left.getClass().getName();
/* 239 */     String rightType = (right == null) ? "null" : right.getClass().getName();
/* 240 */     throw new SpelEvaluationException(SpelMessage.OPERATOR_NOT_SUPPORTED_BETWEEN_TYPES, new Object[] { op, leftType, rightType });
/*     */   }
/*     */ 
/*     */   
/*     */   public List<PropertyAccessor> getPropertyAccessors() {
/* 245 */     return this.relatedContext.getPropertyAccessors();
/*     */   }
/*     */   
/*     */   public EvaluationContext getEvaluationContext() {
/* 249 */     return this.relatedContext;
/*     */   }
/*     */   
/*     */   public SpelParserConfiguration getConfiguration() {
/* 253 */     return this.configuration;
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
/*     */   private static class VariableScope
/*     */   {
/* 266 */     private final Map<String, Object> vars = new HashMap<>();
/*     */ 
/*     */     
/*     */     public VariableScope() {}
/*     */     
/*     */     public VariableScope(@Nullable Map<String, Object> arguments) {
/* 272 */       if (arguments != null) {
/* 273 */         this.vars.putAll(arguments);
/*     */       }
/*     */     }
/*     */     
/*     */     public VariableScope(String name, Object value) {
/* 278 */       this.vars.put(name, value);
/*     */     }
/*     */     
/*     */     public Object lookupVariable(String name) {
/* 282 */       return this.vars.get(name);
/*     */     }
/*     */     
/*     */     public void setVariable(String name, Object value) {
/* 286 */       this.vars.put(name, value);
/*     */     }
/*     */     
/*     */     public boolean definesVariable(String name) {
/* 290 */       return this.vars.containsKey(name);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ExpressionState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */