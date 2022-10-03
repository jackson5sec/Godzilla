/*    */ package org.springframework.cglib.transform;
/*    */ 
/*    */ import org.springframework.asm.ClassVisitor;
/*    */ import org.springframework.asm.MethodVisitor;
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
/*    */ public class ClassTransformerChain
/*    */   extends AbstractClassTransformer
/*    */ {
/*    */   private ClassTransformer[] chain;
/*    */   
/*    */   public ClassTransformerChain(ClassTransformer[] chain) {
/* 24 */     this.chain = (ClassTransformer[])chain.clone();
/*    */   }
/*    */   
/*    */   public void setTarget(ClassVisitor v) {
/* 28 */     super.setTarget(this.chain[0]);
/* 29 */     ClassVisitor next = v;
/* 30 */     for (int i = this.chain.length - 1; i >= 0; i--) {
/* 31 */       this.chain[i].setTarget(next);
/* 32 */       next = this.chain[i];
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
/* 41 */     return this.cv.visitMethod(access, name, desc, signature, exceptions);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 45 */     StringBuffer sb = new StringBuffer();
/* 46 */     sb.append("ClassTransformerChain{");
/* 47 */     for (int i = 0; i < this.chain.length; i++) {
/* 48 */       if (i > 0) {
/* 49 */         sb.append(", ");
/*    */       }
/* 51 */       sb.append(this.chain[i].toString());
/*    */     } 
/* 53 */     sb.append("}");
/* 54 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\transform\ClassTransformerChain.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */