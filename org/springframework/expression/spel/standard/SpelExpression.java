/*     */ package org.springframework.expression.spel.standard;
/*     */ 
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.Expression;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.common.ExpressionUtils;
/*     */ import org.springframework.expression.spel.CompiledExpression;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelCompilerMode;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
/*     */ import org.springframework.expression.spel.SpelNode;
/*     */ import org.springframework.expression.spel.SpelParserConfiguration;
/*     */ import org.springframework.expression.spel.ast.SpelNodeImpl;
/*     */ import org.springframework.expression.spel.support.StandardEvaluationContext;
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
/*     */ public class SpelExpression
/*     */   implements Expression
/*     */ {
/*     */   private static final int INTERPRETED_COUNT_THRESHOLD = 100;
/*     */   private static final int FAILED_ATTEMPTS_THRESHOLD = 100;
/*     */   private final String expression;
/*     */   private final SpelNodeImpl ast;
/*     */   private final SpelParserConfiguration configuration;
/*     */   @Nullable
/*     */   private EvaluationContext evaluationContext;
/*     */   @Nullable
/*     */   private volatile CompiledExpression compiledAst;
/*  74 */   private final AtomicInteger interpretedCount = new AtomicInteger();
/*     */ 
/*     */ 
/*     */   
/*  78 */   private final AtomicInteger failedAttempts = new AtomicInteger();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SpelExpression(String expression, SpelNodeImpl ast, SpelParserConfiguration configuration) {
/*  85 */     this.expression = expression;
/*  86 */     this.ast = ast;
/*  87 */     this.configuration = configuration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEvaluationContext(EvaluationContext evaluationContext) {
/*  96 */     this.evaluationContext = evaluationContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EvaluationContext getEvaluationContext() {
/* 104 */     if (this.evaluationContext == null) {
/* 105 */       this.evaluationContext = (EvaluationContext)new StandardEvaluationContext();
/*     */     }
/* 107 */     return this.evaluationContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getExpressionString() {
/* 115 */     return this.expression;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getValue() throws EvaluationException {
/* 121 */     CompiledExpression compiledAst = this.compiledAst;
/* 122 */     if (compiledAst != null) {
/*     */       try {
/* 124 */         EvaluationContext context = getEvaluationContext();
/* 125 */         return compiledAst.getValue(context.getRootObject().getValue(), context);
/*     */       }
/* 127 */       catch (Throwable ex) {
/*     */         
/* 129 */         if (this.configuration.getCompilerMode() == SpelCompilerMode.MIXED) {
/* 130 */           this.compiledAst = null;
/* 131 */           this.interpretedCount.set(0);
/*     */         }
/*     */         else {
/*     */           
/* 135 */           throw new SpelEvaluationException(ex, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION, new Object[0]);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 140 */     ExpressionState expressionState = new ExpressionState(getEvaluationContext(), this.configuration);
/* 141 */     Object result = this.ast.getValue(expressionState);
/* 142 */     checkCompile(expressionState);
/* 143 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <T> T getValue(@Nullable Class<T> expectedResultType) throws EvaluationException {
/* 150 */     CompiledExpression compiledAst = this.compiledAst;
/* 151 */     if (compiledAst != null) {
/*     */       try {
/* 153 */         EvaluationContext context = getEvaluationContext();
/* 154 */         Object result = compiledAst.getValue(context.getRootObject().getValue(), context);
/* 155 */         if (expectedResultType == null) {
/* 156 */           return (T)result;
/*     */         }
/*     */         
/* 159 */         return (T)ExpressionUtils.convertTypedValue(
/* 160 */             getEvaluationContext(), new TypedValue(result), expectedResultType);
/*     */       
/*     */       }
/* 163 */       catch (Throwable ex) {
/*     */         
/* 165 */         if (this.configuration.getCompilerMode() == SpelCompilerMode.MIXED) {
/* 166 */           this.compiledAst = null;
/* 167 */           this.interpretedCount.set(0);
/*     */         }
/*     */         else {
/*     */           
/* 171 */           throw new SpelEvaluationException(ex, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION, new Object[0]);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 176 */     ExpressionState expressionState = new ExpressionState(getEvaluationContext(), this.configuration);
/* 177 */     TypedValue typedResultValue = this.ast.getTypedValue(expressionState);
/* 178 */     checkCompile(expressionState);
/* 179 */     return (T)ExpressionUtils.convertTypedValue(expressionState
/* 180 */         .getEvaluationContext(), typedResultValue, expectedResultType);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getValue(@Nullable Object rootObject) throws EvaluationException {
/* 186 */     CompiledExpression compiledAst = this.compiledAst;
/* 187 */     if (compiledAst != null) {
/*     */       try {
/* 189 */         return compiledAst.getValue(rootObject, getEvaluationContext());
/*     */       }
/* 191 */       catch (Throwable ex) {
/*     */         
/* 193 */         if (this.configuration.getCompilerMode() == SpelCompilerMode.MIXED) {
/* 194 */           this.compiledAst = null;
/* 195 */           this.interpretedCount.set(0);
/*     */         }
/*     */         else {
/*     */           
/* 199 */           throw new SpelEvaluationException(ex, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION, new Object[0]);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 205 */     ExpressionState expressionState = new ExpressionState(getEvaluationContext(), toTypedValue(rootObject), this.configuration);
/* 206 */     Object result = this.ast.getValue(expressionState);
/* 207 */     checkCompile(expressionState);
/* 208 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <T> T getValue(@Nullable Object rootObject, @Nullable Class<T> expectedResultType) throws EvaluationException {
/* 215 */     CompiledExpression compiledAst = this.compiledAst;
/* 216 */     if (compiledAst != null) {
/*     */       try {
/* 218 */         Object result = compiledAst.getValue(rootObject, getEvaluationContext());
/* 219 */         if (expectedResultType == null) {
/* 220 */           return (T)result;
/*     */         }
/*     */         
/* 223 */         return (T)ExpressionUtils.convertTypedValue(
/* 224 */             getEvaluationContext(), new TypedValue(result), expectedResultType);
/*     */       
/*     */       }
/* 227 */       catch (Throwable ex) {
/*     */         
/* 229 */         if (this.configuration.getCompilerMode() == SpelCompilerMode.MIXED) {
/* 230 */           this.compiledAst = null;
/* 231 */           this.interpretedCount.set(0);
/*     */         }
/*     */         else {
/*     */           
/* 235 */           throw new SpelEvaluationException(ex, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION, new Object[0]);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 241 */     ExpressionState expressionState = new ExpressionState(getEvaluationContext(), toTypedValue(rootObject), this.configuration);
/* 242 */     TypedValue typedResultValue = this.ast.getTypedValue(expressionState);
/* 243 */     checkCompile(expressionState);
/* 244 */     return (T)ExpressionUtils.convertTypedValue(expressionState
/* 245 */         .getEvaluationContext(), typedResultValue, expectedResultType);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getValue(EvaluationContext context) throws EvaluationException {
/* 251 */     Assert.notNull(context, "EvaluationContext is required");
/*     */     
/* 253 */     CompiledExpression compiledAst = this.compiledAst;
/* 254 */     if (compiledAst != null) {
/*     */       try {
/* 256 */         return compiledAst.getValue(context.getRootObject().getValue(), context);
/*     */       }
/* 258 */       catch (Throwable ex) {
/*     */         
/* 260 */         if (this.configuration.getCompilerMode() == SpelCompilerMode.MIXED) {
/* 261 */           this.compiledAst = null;
/* 262 */           this.interpretedCount.set(0);
/*     */         }
/*     */         else {
/*     */           
/* 266 */           throw new SpelEvaluationException(ex, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION, new Object[0]);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 271 */     ExpressionState expressionState = new ExpressionState(context, this.configuration);
/* 272 */     Object result = this.ast.getValue(expressionState);
/* 273 */     checkCompile(expressionState);
/* 274 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <T> T getValue(EvaluationContext context, @Nullable Class<T> expectedResultType) throws EvaluationException {
/* 281 */     Assert.notNull(context, "EvaluationContext is required");
/*     */     
/* 283 */     CompiledExpression compiledAst = this.compiledAst;
/* 284 */     if (compiledAst != null) {
/*     */       try {
/* 286 */         Object result = compiledAst.getValue(context.getRootObject().getValue(), context);
/* 287 */         if (expectedResultType != null) {
/* 288 */           return (T)ExpressionUtils.convertTypedValue(context, new TypedValue(result), expectedResultType);
/*     */         }
/*     */         
/* 291 */         return (T)result;
/*     */       
/*     */       }
/* 294 */       catch (Throwable ex) {
/*     */         
/* 296 */         if (this.configuration.getCompilerMode() == SpelCompilerMode.MIXED) {
/* 297 */           this.compiledAst = null;
/* 298 */           this.interpretedCount.set(0);
/*     */         }
/*     */         else {
/*     */           
/* 302 */           throw new SpelEvaluationException(ex, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION, new Object[0]);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 307 */     ExpressionState expressionState = new ExpressionState(context, this.configuration);
/* 308 */     TypedValue typedResultValue = this.ast.getTypedValue(expressionState);
/* 309 */     checkCompile(expressionState);
/* 310 */     return (T)ExpressionUtils.convertTypedValue(context, typedResultValue, expectedResultType);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getValue(EvaluationContext context, @Nullable Object rootObject) throws EvaluationException {
/* 316 */     Assert.notNull(context, "EvaluationContext is required");
/*     */     
/* 318 */     CompiledExpression compiledAst = this.compiledAst;
/* 319 */     if (compiledAst != null) {
/*     */       try {
/* 321 */         return compiledAst.getValue(rootObject, context);
/*     */       }
/* 323 */       catch (Throwable ex) {
/*     */         
/* 325 */         if (this.configuration.getCompilerMode() == SpelCompilerMode.MIXED) {
/* 326 */           this.compiledAst = null;
/* 327 */           this.interpretedCount.set(0);
/*     */         }
/*     */         else {
/*     */           
/* 331 */           throw new SpelEvaluationException(ex, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION, new Object[0]);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 336 */     ExpressionState expressionState = new ExpressionState(context, toTypedValue(rootObject), this.configuration);
/* 337 */     Object result = this.ast.getValue(expressionState);
/* 338 */     checkCompile(expressionState);
/* 339 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <T> T getValue(EvaluationContext context, @Nullable Object rootObject, @Nullable Class<T> expectedResultType) throws EvaluationException {
/* 348 */     Assert.notNull(context, "EvaluationContext is required");
/*     */     
/* 350 */     CompiledExpression compiledAst = this.compiledAst;
/* 351 */     if (compiledAst != null) {
/*     */       try {
/* 353 */         Object result = compiledAst.getValue(rootObject, context);
/* 354 */         if (expectedResultType != null) {
/* 355 */           return (T)ExpressionUtils.convertTypedValue(context, new TypedValue(result), expectedResultType);
/*     */         }
/*     */         
/* 358 */         return (T)result;
/*     */       
/*     */       }
/* 361 */       catch (Throwable ex) {
/*     */         
/* 363 */         if (this.configuration.getCompilerMode() == SpelCompilerMode.MIXED) {
/* 364 */           this.compiledAst = null;
/* 365 */           this.interpretedCount.set(0);
/*     */         }
/*     */         else {
/*     */           
/* 369 */           throw new SpelEvaluationException(ex, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION, new Object[0]);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 374 */     ExpressionState expressionState = new ExpressionState(context, toTypedValue(rootObject), this.configuration);
/* 375 */     TypedValue typedResultValue = this.ast.getTypedValue(expressionState);
/* 376 */     checkCompile(expressionState);
/* 377 */     return (T)ExpressionUtils.convertTypedValue(context, typedResultValue, expectedResultType);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?> getValueType() throws EvaluationException {
/* 383 */     return getValueType(getEvaluationContext());
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?> getValueType(@Nullable Object rootObject) throws EvaluationException {
/* 389 */     return getValueType(getEvaluationContext(), rootObject);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?> getValueType(EvaluationContext context) throws EvaluationException {
/* 395 */     Assert.notNull(context, "EvaluationContext is required");
/* 396 */     ExpressionState expressionState = new ExpressionState(context, this.configuration);
/* 397 */     TypeDescriptor typeDescriptor = this.ast.getValueInternal(expressionState).getTypeDescriptor();
/* 398 */     return (typeDescriptor != null) ? typeDescriptor.getType() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?> getValueType(EvaluationContext context, @Nullable Object rootObject) throws EvaluationException {
/* 404 */     ExpressionState expressionState = new ExpressionState(context, toTypedValue(rootObject), this.configuration);
/* 405 */     TypeDescriptor typeDescriptor = this.ast.getValueInternal(expressionState).getTypeDescriptor();
/* 406 */     return (typeDescriptor != null) ? typeDescriptor.getType() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public TypeDescriptor getValueTypeDescriptor() throws EvaluationException {
/* 412 */     return getValueTypeDescriptor(getEvaluationContext());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public TypeDescriptor getValueTypeDescriptor(@Nullable Object rootObject) throws EvaluationException {
/* 419 */     ExpressionState expressionState = new ExpressionState(getEvaluationContext(), toTypedValue(rootObject), this.configuration);
/* 420 */     return this.ast.getValueInternal(expressionState).getTypeDescriptor();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public TypeDescriptor getValueTypeDescriptor(EvaluationContext context) throws EvaluationException {
/* 426 */     Assert.notNull(context, "EvaluationContext is required");
/* 427 */     ExpressionState expressionState = new ExpressionState(context, this.configuration);
/* 428 */     return this.ast.getValueInternal(expressionState).getTypeDescriptor();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public TypeDescriptor getValueTypeDescriptor(EvaluationContext context, @Nullable Object rootObject) throws EvaluationException {
/* 436 */     Assert.notNull(context, "EvaluationContext is required");
/* 437 */     ExpressionState expressionState = new ExpressionState(context, toTypedValue(rootObject), this.configuration);
/* 438 */     return this.ast.getValueInternal(expressionState).getTypeDescriptor();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWritable(@Nullable Object rootObject) throws EvaluationException {
/* 443 */     return this.ast.isWritable(new ExpressionState(
/* 444 */           getEvaluationContext(), toTypedValue(rootObject), this.configuration));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWritable(EvaluationContext context) throws EvaluationException {
/* 449 */     Assert.notNull(context, "EvaluationContext is required");
/* 450 */     return this.ast.isWritable(new ExpressionState(context, this.configuration));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWritable(EvaluationContext context, @Nullable Object rootObject) throws EvaluationException {
/* 455 */     Assert.notNull(context, "EvaluationContext is required");
/* 456 */     return this.ast.isWritable(new ExpressionState(context, toTypedValue(rootObject), this.configuration));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(@Nullable Object rootObject, @Nullable Object value) throws EvaluationException {
/* 461 */     this.ast.setValue(new ExpressionState(
/* 462 */           getEvaluationContext(), toTypedValue(rootObject), this.configuration), value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(EvaluationContext context, @Nullable Object value) throws EvaluationException {
/* 467 */     Assert.notNull(context, "EvaluationContext is required");
/* 468 */     this.ast.setValue(new ExpressionState(context, this.configuration), value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(EvaluationContext context, @Nullable Object rootObject, @Nullable Object value) throws EvaluationException {
/* 475 */     Assert.notNull(context, "EvaluationContext is required");
/* 476 */     this.ast.setValue(new ExpressionState(context, toTypedValue(rootObject), this.configuration), value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkCompile(ExpressionState expressionState) {
/* 486 */     this.interpretedCount.incrementAndGet();
/* 487 */     SpelCompilerMode compilerMode = expressionState.getConfiguration().getCompilerMode();
/* 488 */     if (compilerMode != SpelCompilerMode.OFF) {
/* 489 */       if (compilerMode == SpelCompilerMode.IMMEDIATE) {
/* 490 */         if (this.interpretedCount.get() > 1) {
/* 491 */           compileExpression();
/*     */         
/*     */         }
/*     */       
/*     */       }
/* 496 */       else if (this.interpretedCount.get() > 100) {
/* 497 */         compileExpression();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean compileExpression() {
/* 510 */     CompiledExpression compiledAst = this.compiledAst;
/* 511 */     if (compiledAst != null)
/*     */     {
/* 513 */       return true;
/*     */     }
/* 515 */     if (this.failedAttempts.get() > 100)
/*     */     {
/* 517 */       return false;
/*     */     }
/*     */     
/* 520 */     synchronized (this) {
/* 521 */       if (this.compiledAst != null)
/*     */       {
/* 523 */         return true;
/*     */       }
/* 525 */       SpelCompiler compiler = SpelCompiler.getCompiler(this.configuration.getCompilerClassLoader());
/* 526 */       compiledAst = compiler.compile(this.ast);
/* 527 */       if (compiledAst != null) {
/*     */         
/* 529 */         this.compiledAst = compiledAst;
/* 530 */         return true;
/*     */       } 
/*     */ 
/*     */       
/* 534 */       this.failedAttempts.incrementAndGet();
/* 535 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void revertToInterpreted() {
/* 546 */     this.compiledAst = null;
/* 547 */     this.interpretedCount.set(0);
/* 548 */     this.failedAttempts.set(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SpelNode getAST() {
/* 555 */     return (SpelNode)this.ast;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/* 565 */     return this.ast.toStringAST();
/*     */   }
/*     */   
/*     */   private TypedValue toTypedValue(@Nullable Object object) {
/* 569 */     return (object != null) ? new TypedValue(object) : TypedValue.NULL;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\standard\SpelExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */