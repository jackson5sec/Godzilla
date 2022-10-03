/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface ValueRef
/*     */ {
/*     */   TypedValue getValue();
/*     */   
/*     */   void setValue(@Nullable Object paramObject);
/*     */   
/*     */   boolean isWritable();
/*     */   
/*     */   public static class NullValueRef
/*     */     implements ValueRef
/*     */   {
/*  63 */     static final NullValueRef INSTANCE = new NullValueRef();
/*     */ 
/*     */     
/*     */     public TypedValue getValue() {
/*  67 */       return TypedValue.NULL;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setValue(@Nullable Object newValue) {
/*  75 */       throw new SpelEvaluationException(0, SpelMessage.NOT_ASSIGNABLE, new Object[] { "null" });
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isWritable() {
/*  80 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class TypedValueHolderValueRef
/*     */     implements ValueRef
/*     */   {
/*     */     private final TypedValue typedValue;
/*     */     
/*     */     private final SpelNodeImpl node;
/*     */ 
/*     */     
/*     */     public TypedValueHolderValueRef(TypedValue typedValue, SpelNodeImpl node) {
/*  95 */       this.typedValue = typedValue;
/*  96 */       this.node = node;
/*     */     }
/*     */ 
/*     */     
/*     */     public TypedValue getValue() {
/* 101 */       return this.typedValue;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setValue(@Nullable Object newValue) {
/* 106 */       throw new SpelEvaluationException(this.node
/* 107 */           .getStartPosition(), SpelMessage.NOT_ASSIGNABLE, new Object[] { this.node.toStringAST() });
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isWritable() {
/* 112 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\ValueRef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */