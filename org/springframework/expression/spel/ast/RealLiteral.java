/*    */ package org.springframework.expression.spel.ast;
/*    */ 
/*    */ import org.springframework.asm.MethodVisitor;
/*    */ import org.springframework.expression.TypedValue;
/*    */ import org.springframework.expression.spel.CodeFlow;
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
/*    */ public class RealLiteral
/*    */   extends Literal
/*    */ {
/*    */   private final TypedValue value;
/*    */   
/*    */   public RealLiteral(String payload, int startPos, int endPos, double value) {
/* 35 */     super(payload, startPos, endPos);
/* 36 */     this.value = new TypedValue(Double.valueOf(value));
/* 37 */     this.exitTypeDescriptor = "D";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypedValue getLiteralValue() {
/* 43 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCompilable() {
/* 48 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 53 */     mv.visitLdcInsn(this.value.getValue());
/* 54 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\RealLiteral.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */