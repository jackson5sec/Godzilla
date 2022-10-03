/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.StringJoiner;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypeConverter;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
/*     */ import org.springframework.expression.spel.support.ReflectionHelper;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FunctionReference
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   private final String name;
/*     */   @Nullable
/*     */   private volatile Method method;
/*     */   
/*     */   public FunctionReference(String functionName, int startPos, int endPos, SpelNodeImpl... arguments) {
/*  62 */     super(startPos, endPos, arguments);
/*  63 */     this.name = functionName;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*  69 */     TypedValue value = state.lookupVariable(this.name);
/*  70 */     if (value == TypedValue.NULL) {
/*  71 */       throw new SpelEvaluationException(getStartPosition(), SpelMessage.FUNCTION_NOT_DEFINED, new Object[] { this.name });
/*     */     }
/*  73 */     if (!(value.getValue() instanceof Method))
/*     */     {
/*  75 */       throw new SpelEvaluationException(SpelMessage.FUNCTION_REFERENCE_CANNOT_BE_INVOKED, new Object[] { this.name, value
/*  76 */             .getClass() });
/*     */     }
/*     */     
/*     */     try {
/*  80 */       return executeFunctionJLRMethod(state, (Method)value.getValue());
/*     */     }
/*  82 */     catch (SpelEvaluationException ex) {
/*  83 */       ex.setPosition(getStartPosition());
/*  84 */       throw ex;
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
/*     */   private TypedValue executeFunctionJLRMethod(ExpressionState state, Method method) throws EvaluationException {
/*  96 */     Object[] functionArgs = getArguments(state);
/*     */     
/*  98 */     if (!method.isVarArgs()) {
/*  99 */       int declaredParamCount = method.getParameterCount();
/* 100 */       if (declaredParamCount != functionArgs.length)
/* 101 */         throw new SpelEvaluationException(SpelMessage.INCORRECT_NUMBER_OF_ARGUMENTS_TO_FUNCTION, new Object[] {
/* 102 */               Integer.valueOf(functionArgs.length), Integer.valueOf(declaredParamCount)
/*     */             }); 
/*     */     } 
/* 105 */     if (!Modifier.isStatic(method.getModifiers())) {
/* 106 */       throw new SpelEvaluationException(getStartPosition(), SpelMessage.FUNCTION_MUST_BE_STATIC, new Object[] {
/* 107 */             ClassUtils.getQualifiedMethodName(method), this.name
/*     */           });
/*     */     }
/*     */     
/* 111 */     TypeConverter converter = state.getEvaluationContext().getTypeConverter();
/* 112 */     boolean argumentConversionOccurred = ReflectionHelper.convertAllArguments(converter, functionArgs, method);
/* 113 */     if (method.isVarArgs()) {
/* 114 */       functionArgs = ReflectionHelper.setupArgumentsForVarargsInvocation(method
/* 115 */           .getParameterTypes(), functionArgs);
/*     */     }
/* 117 */     boolean compilable = false;
/*     */     
/*     */     try {
/* 120 */       ReflectionUtils.makeAccessible(method);
/* 121 */       Object result = method.invoke(method.getClass(), functionArgs);
/* 122 */       compilable = !argumentConversionOccurred;
/* 123 */       return new TypedValue(result, (new TypeDescriptor(new MethodParameter(method, -1))).narrow(result));
/*     */     }
/* 125 */     catch (Exception ex) {
/* 126 */       throw new SpelEvaluationException(getStartPosition(), ex, SpelMessage.EXCEPTION_DURING_FUNCTION_CALL, new Object[] { this.name, ex
/* 127 */             .getMessage() });
/*     */     } finally {
/*     */       
/* 130 */       if (compilable) {
/* 131 */         this.exitTypeDescriptor = CodeFlow.toDescriptor(method.getReturnType());
/* 132 */         this.method = method;
/*     */       } else {
/*     */         
/* 135 */         this.exitTypeDescriptor = null;
/* 136 */         this.method = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/* 143 */     StringJoiner sj = new StringJoiner(",", "(", ")");
/* 144 */     for (int i = 0; i < getChildCount(); i++) {
/* 145 */       sj.add(getChild(i).toStringAST());
/*     */     }
/* 147 */     return '#' + this.name + sj.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object[] getArguments(ExpressionState state) throws EvaluationException {
/* 156 */     Object[] arguments = new Object[getChildCount()];
/* 157 */     for (int i = 0; i < arguments.length; i++) {
/* 158 */       arguments[i] = this.children[i].getValueInternal(state).getValue();
/*     */     }
/* 160 */     return arguments;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/* 165 */     Method method = this.method;
/* 166 */     if (method == null) {
/* 167 */       return false;
/*     */     }
/* 169 */     int methodModifiers = method.getModifiers();
/* 170 */     if (!Modifier.isStatic(methodModifiers) || !Modifier.isPublic(methodModifiers) || 
/* 171 */       !Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
/* 172 */       return false;
/*     */     }
/* 174 */     for (SpelNodeImpl child : this.children) {
/* 175 */       if (!child.isCompilable()) {
/* 176 */         return false;
/*     */       }
/*     */     } 
/* 179 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 184 */     Method method = this.method;
/* 185 */     Assert.state((method != null), "No method handle");
/* 186 */     String classDesc = method.getDeclaringClass().getName().replace('.', '/');
/* 187 */     generateCodeForArguments(mv, cf, method, this.children);
/* 188 */     mv.visitMethodInsn(184, classDesc, method.getName(), 
/* 189 */         CodeFlow.createSignatureDescriptor(method), false);
/* 190 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\FunctionReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */