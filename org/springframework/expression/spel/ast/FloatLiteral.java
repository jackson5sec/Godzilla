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
/*    */ 
/*    */ public class FloatLiteral
/*    */   extends Literal
/*    */ {
/*    */   private final TypedValue value;
/*    */   
/*    */   public FloatLiteral(String payload, int startPos, int endPos, float value) {
/* 36 */     super(payload, startPos, endPos);
/* 37 */     this.value = new TypedValue(Float.valueOf(value));
/* 38 */     this.exitTypeDescriptor = "F";
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
/* 54 */     mv.visitLdcInsn(this.value.getValue());
/* 55 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\FloatLiteral.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */