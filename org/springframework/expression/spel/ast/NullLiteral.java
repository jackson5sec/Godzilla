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
/*    */ public class NullLiteral
/*    */   extends Literal
/*    */ {
/*    */   public NullLiteral(int startPos, int endPos) {
/* 32 */     super((String)null, startPos, endPos);
/* 33 */     this.exitTypeDescriptor = "Ljava/lang/Object";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypedValue getLiteralValue() {
/* 39 */     return TypedValue.NULL;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 44 */     return "null";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCompilable() {
/* 49 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 54 */     mv.visitInsn(1);
/* 55 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\NullLiteral.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */