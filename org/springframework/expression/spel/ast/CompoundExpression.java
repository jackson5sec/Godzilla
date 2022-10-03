/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.util.StringJoiner;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.expression.EvaluationException;
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
/*     */ public class CompoundExpression
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   public CompoundExpression(int startPos, int endPos, SpelNodeImpl... expressionComponents) {
/*  39 */     super(startPos, endPos, expressionComponents);
/*  40 */     if (expressionComponents.length < 2) {
/*  41 */       throw new IllegalStateException("Do not build compound expressions with less than two entries: " + expressionComponents.length);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ValueRef getValueRef(ExpressionState state) throws EvaluationException {
/*  49 */     if (getChildCount() == 1) {
/*  50 */       return this.children[0].getValueRef(state);
/*     */     }
/*     */     
/*  53 */     SpelNodeImpl nextNode = this.children[0];
/*     */     try {
/*  55 */       TypedValue result = nextNode.getValueInternal(state);
/*  56 */       int cc = getChildCount();
/*  57 */       for (int i = 1; i < cc - 1; i++) {
/*     */         
/*  59 */         try { state.pushActiveContextObject(result);
/*  60 */           nextNode = this.children[i];
/*  61 */           result = nextNode.getValueInternal(state);
/*     */ 
/*     */           
/*  64 */           state.popActiveContextObject(); } finally { state.popActiveContextObject(); }
/*     */       
/*     */       } 
/*     */       try {
/*  68 */         state.pushActiveContextObject(result);
/*  69 */         nextNode = this.children[cc - 1];
/*  70 */         return nextNode.getValueRef(state);
/*     */       } finally {
/*     */         
/*  73 */         state.popActiveContextObject();
/*     */       }
/*     */     
/*  76 */     } catch (SpelEvaluationException ex) {
/*     */       
/*  78 */       ex.setPosition(nextNode.getStartPosition());
/*  79 */       throw ex;
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
/*     */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*  91 */     ValueRef ref = getValueRef(state);
/*  92 */     TypedValue result = ref.getValue();
/*  93 */     this.exitTypeDescriptor = (this.children[this.children.length - 1]).exitTypeDescriptor;
/*  94 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(ExpressionState state, @Nullable Object value) throws EvaluationException {
/*  99 */     getValueRef(state).setValue(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWritable(ExpressionState state) throws EvaluationException {
/* 104 */     return getValueRef(state).isWritable();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/* 109 */     StringJoiner sj = new StringJoiner(".");
/* 110 */     for (int i = 0; i < getChildCount(); i++) {
/* 111 */       sj.add(getChild(i).toStringAST());
/*     */     }
/* 113 */     return sj.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/* 118 */     for (SpelNodeImpl child : this.children) {
/* 119 */       if (!child.isCompilable()) {
/* 120 */         return false;
/*     */       }
/*     */     } 
/* 123 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 128 */     for (SpelNodeImpl child : this.children) {
/* 129 */       child.generateCode(mv, cf);
/*     */     }
/* 131 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\CompoundExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */