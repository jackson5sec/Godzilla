/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.lang.reflect.Modifier;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
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
/*     */ public class VariableReference
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   private static final String THIS = "this";
/*     */   private static final String ROOT = "root";
/*     */   private final String name;
/*     */   
/*     */   public VariableReference(String variableName, int startPos, int endPos) {
/*  48 */     super(startPos, endPos, new SpelNodeImpl[0]);
/*  49 */     this.name = variableName;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ValueRef getValueRef(ExpressionState state) throws SpelEvaluationException {
/*  55 */     if (this.name.equals("this")) {
/*  56 */       return new ValueRef.TypedValueHolderValueRef(state.getActiveContextObject(), this);
/*     */     }
/*  58 */     if (this.name.equals("root")) {
/*  59 */       return new ValueRef.TypedValueHolderValueRef(state.getRootContextObject(), this);
/*     */     }
/*  61 */     TypedValue result = state.lookupVariable(this.name);
/*     */     
/*  63 */     return new VariableRef(this.name, result, state.getEvaluationContext());
/*     */   }
/*     */ 
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state) throws SpelEvaluationException {
/*  68 */     if (this.name.equals("this")) {
/*  69 */       return state.getActiveContextObject();
/*     */     }
/*  71 */     if (this.name.equals("root")) {
/*  72 */       TypedValue typedValue = state.getRootContextObject();
/*  73 */       this.exitTypeDescriptor = CodeFlow.toDescriptorFromObject(typedValue.getValue());
/*  74 */       return typedValue;
/*     */     } 
/*  76 */     TypedValue result = state.lookupVariable(this.name);
/*  77 */     Object value = result.getValue();
/*  78 */     if (value == null || !Modifier.isPublic(value.getClass().getModifiers())) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  83 */       this.exitTypeDescriptor = "Ljava/lang/Object";
/*     */     } else {
/*     */       
/*  86 */       this.exitTypeDescriptor = CodeFlow.toDescriptorFromObject(value);
/*     */     } 
/*     */     
/*  89 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(ExpressionState state, @Nullable Object value) throws SpelEvaluationException {
/*  94 */     state.setVariable(this.name, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/*  99 */     return "#" + this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWritable(ExpressionState expressionState) throws SpelEvaluationException {
/* 104 */     return (!this.name.equals("this") && !this.name.equals("root"));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/* 109 */     return (this.exitTypeDescriptor != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 114 */     if (this.name.equals("root")) {
/* 115 */       mv.visitVarInsn(25, 1);
/*     */     } else {
/*     */       
/* 118 */       mv.visitVarInsn(25, 2);
/* 119 */       mv.visitLdcInsn(this.name);
/* 120 */       mv.visitMethodInsn(185, "org/springframework/expression/EvaluationContext", "lookupVariable", "(Ljava/lang/String;)Ljava/lang/Object;", true);
/*     */     } 
/* 122 */     CodeFlow.insertCheckCast(mv, this.exitTypeDescriptor);
/* 123 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */   }
/*     */ 
/*     */   
/*     */   private static class VariableRef
/*     */     implements ValueRef
/*     */   {
/*     */     private final String name;
/*     */     
/*     */     private final TypedValue value;
/*     */     private final EvaluationContext evaluationContext;
/*     */     
/*     */     public VariableRef(String name, TypedValue value, EvaluationContext evaluationContext) {
/* 136 */       this.name = name;
/* 137 */       this.value = value;
/* 138 */       this.evaluationContext = evaluationContext;
/*     */     }
/*     */ 
/*     */     
/*     */     public TypedValue getValue() {
/* 143 */       return this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setValue(@Nullable Object newValue) {
/* 148 */       this.evaluationContext.setVariable(this.name, newValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isWritable() {
/* 153 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\VariableReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */