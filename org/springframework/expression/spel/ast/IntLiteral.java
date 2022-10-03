/*    */ package org.springframework.expression.spel.ast;
/*    */ 
/*    */ import org.springframework.asm.MethodVisitor;
/*    */ import org.springframework.expression.TypedValue;
/*    */ import org.springframework.expression.spel.CodeFlow;
/*    */ import org.springframework.util.Assert;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IntLiteral
/*    */   extends Literal
/*    */ {
/*    */   private final TypedValue value;
/*    */   
/*    */   public IntLiteral(String payload, int startPos, int endPos, int value) {
/* 36 */     super(payload, startPos, endPos);
/* 37 */     this.value = new TypedValue(Integer.valueOf(value));
/* 38 */     this.exitTypeDescriptor = "I";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypedValue getLiteralValue() {
/* 44 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCompilable() {
/* 49 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 54 */     Integer intValue = (Integer)this.value.getValue();
/* 55 */     Assert.state((intValue != null), "No int value");
/* 56 */     if (intValue.intValue() == -1) {
/*    */       
/* 58 */       mv.visitInsn(2);
/*    */     }
/* 60 */     else if (intValue.intValue() >= 0 && intValue.intValue() < 6) {
/* 61 */       mv.visitInsn(3 + intValue.intValue());
/*    */     } else {
/*    */       
/* 64 */       mv.visitLdcInsn(intValue);
/*    */     } 
/* 66 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\IntLiteral.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */