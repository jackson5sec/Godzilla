/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import org.springframework.asm.Label;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
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
/*     */ public class Ternary
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   public Ternary(int startPos, int endPos, SpelNodeImpl... args) {
/*  40 */     super(startPos, endPos, args);
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
/*  53 */     Boolean value = this.children[0].<Boolean>getValue(state, Boolean.class);
/*  54 */     if (value == null) {
/*  55 */       throw new SpelEvaluationException(getChild(0).getStartPosition(), SpelMessage.TYPE_CONVERSION_ERROR, new Object[] { "null", "boolean" });
/*     */     }
/*     */     
/*  58 */     TypedValue result = this.children[value.booleanValue() ? 1 : 2].getValueInternal(state);
/*  59 */     computeExitTypeDescriptor();
/*  60 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/*  65 */     return getChild(0).toStringAST() + " ? " + getChild(1).toStringAST() + " : " + getChild(2).toStringAST();
/*     */   }
/*     */   
/*     */   private void computeExitTypeDescriptor() {
/*  69 */     if (this.exitTypeDescriptor == null && (this.children[1]).exitTypeDescriptor != null && (this.children[2]).exitTypeDescriptor != null) {
/*     */       
/*  71 */       String leftDescriptor = (this.children[1]).exitTypeDescriptor;
/*  72 */       String rightDescriptor = (this.children[2]).exitTypeDescriptor;
/*  73 */       if (ObjectUtils.nullSafeEquals(leftDescriptor, rightDescriptor)) {
/*  74 */         this.exitTypeDescriptor = leftDescriptor;
/*     */       }
/*     */       else {
/*     */         
/*  78 */         this.exitTypeDescriptor = "Ljava/lang/Object";
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/*  85 */     SpelNodeImpl condition = this.children[0];
/*  86 */     SpelNodeImpl left = this.children[1];
/*  87 */     SpelNodeImpl right = this.children[2];
/*  88 */     return (condition.isCompilable() && left.isCompilable() && right.isCompilable() && 
/*  89 */       CodeFlow.isBooleanCompatible(condition.exitTypeDescriptor) && left.exitTypeDescriptor != null && right.exitTypeDescriptor != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/*  96 */     computeExitTypeDescriptor();
/*  97 */     cf.enterCompilationScope();
/*  98 */     this.children[0].generateCode(mv, cf);
/*  99 */     String lastDesc = cf.lastDescriptor();
/* 100 */     Assert.state((lastDesc != null), "No last descriptor");
/* 101 */     if (!CodeFlow.isPrimitive(lastDesc)) {
/* 102 */       CodeFlow.insertUnboxInsns(mv, 'Z', lastDesc);
/*     */     }
/* 104 */     cf.exitCompilationScope();
/* 105 */     Label elseTarget = new Label();
/* 106 */     Label endOfIf = new Label();
/* 107 */     mv.visitJumpInsn(153, elseTarget);
/* 108 */     cf.enterCompilationScope();
/* 109 */     this.children[1].generateCode(mv, cf);
/* 110 */     if (!CodeFlow.isPrimitive(this.exitTypeDescriptor)) {
/* 111 */       lastDesc = cf.lastDescriptor();
/* 112 */       Assert.state((lastDesc != null), "No last descriptor");
/* 113 */       CodeFlow.insertBoxIfNecessary(mv, lastDesc.charAt(0));
/*     */     } 
/* 115 */     cf.exitCompilationScope();
/* 116 */     mv.visitJumpInsn(167, endOfIf);
/* 117 */     mv.visitLabel(elseTarget);
/* 118 */     cf.enterCompilationScope();
/* 119 */     this.children[2].generateCode(mv, cf);
/* 120 */     if (!CodeFlow.isPrimitive(this.exitTypeDescriptor)) {
/* 121 */       lastDesc = cf.lastDescriptor();
/* 122 */       Assert.state((lastDesc != null), "No last descriptor");
/* 123 */       CodeFlow.insertBoxIfNecessary(mv, lastDesc.charAt(0));
/*     */     } 
/* 125 */     cf.exitCompilationScope();
/* 126 */     mv.visitLabel(endOfIf);
/* 127 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\Ternary.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */