/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import org.springframework.asm.Label;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class Elvis
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   public Elvis(int startPos, int endPos, SpelNodeImpl... args) {
/*  39 */     super(startPos, endPos, args);
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
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*  52 */     TypedValue value = this.children[0].getValueInternal(state);
/*     */     
/*  54 */     if (value.getValue() != null && !"".equals(value.getValue())) {
/*  55 */       return value;
/*     */     }
/*     */     
/*  58 */     TypedValue result = this.children[1].getValueInternal(state);
/*  59 */     computeExitTypeDescriptor();
/*  60 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/*  66 */     return getChild(0).toStringAST() + " ?: " + getChild(1).toStringAST();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/*  71 */     SpelNodeImpl condition = this.children[0];
/*  72 */     SpelNodeImpl ifNullValue = this.children[1];
/*  73 */     return (condition.isCompilable() && ifNullValue.isCompilable() && condition.exitTypeDescriptor != null && ifNullValue.exitTypeDescriptor != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/*  80 */     computeExitTypeDescriptor();
/*  81 */     cf.enterCompilationScope();
/*  82 */     this.children[0].generateCode(mv, cf);
/*  83 */     String lastDesc = cf.lastDescriptor();
/*  84 */     Assert.state((lastDesc != null), "No last descriptor");
/*  85 */     CodeFlow.insertBoxIfNecessary(mv, lastDesc.charAt(0));
/*  86 */     cf.exitCompilationScope();
/*  87 */     Label elseTarget = new Label();
/*  88 */     Label endOfIf = new Label();
/*  89 */     mv.visitInsn(89);
/*  90 */     mv.visitJumpInsn(198, elseTarget);
/*     */     
/*  92 */     mv.visitInsn(89);
/*  93 */     mv.visitLdcInsn("");
/*  94 */     mv.visitInsn(95);
/*  95 */     mv.visitMethodInsn(182, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
/*  96 */     mv.visitJumpInsn(153, endOfIf);
/*  97 */     mv.visitLabel(elseTarget);
/*  98 */     mv.visitInsn(87);
/*  99 */     cf.enterCompilationScope();
/* 100 */     this.children[1].generateCode(mv, cf);
/* 101 */     if (!CodeFlow.isPrimitive(this.exitTypeDescriptor)) {
/* 102 */       lastDesc = cf.lastDescriptor();
/* 103 */       Assert.state((lastDesc != null), "No last descriptor");
/* 104 */       CodeFlow.insertBoxIfNecessary(mv, lastDesc.charAt(0));
/*     */     } 
/* 106 */     cf.exitCompilationScope();
/* 107 */     mv.visitLabel(endOfIf);
/* 108 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */   }
/*     */   
/*     */   private void computeExitTypeDescriptor() {
/* 112 */     if (this.exitTypeDescriptor == null && (this.children[0]).exitTypeDescriptor != null && (this.children[1]).exitTypeDescriptor != null) {
/*     */       
/* 114 */       String conditionDescriptor = (this.children[0]).exitTypeDescriptor;
/* 115 */       String ifNullValueDescriptor = (this.children[1]).exitTypeDescriptor;
/* 116 */       if (ObjectUtils.nullSafeEquals(conditionDescriptor, ifNullValueDescriptor)) {
/* 117 */         this.exitTypeDescriptor = conditionDescriptor;
/*     */       }
/*     */       else {
/*     */         
/* 121 */         this.exitTypeDescriptor = "Ljava/lang/Object";
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\Elvis.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */