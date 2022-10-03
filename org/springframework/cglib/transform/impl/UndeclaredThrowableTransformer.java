/*    */ package org.springframework.cglib.transform.impl;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import org.springframework.asm.Type;
/*    */ import org.springframework.cglib.core.Block;
/*    */ import org.springframework.cglib.core.CodeEmitter;
/*    */ import org.springframework.cglib.core.Constants;
/*    */ import org.springframework.cglib.core.EmitUtils;
/*    */ import org.springframework.cglib.core.Signature;
/*    */ import org.springframework.cglib.core.TypeUtils;
/*    */ import org.springframework.cglib.transform.ClassEmitterTransformer;
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
/*    */ public class UndeclaredThrowableTransformer
/*    */   extends ClassEmitterTransformer
/*    */ {
/*    */   private Type wrapper;
/*    */   
/*    */   public UndeclaredThrowableTransformer(Class wrapper) {
/* 29 */     this.wrapper = Type.getType(wrapper);
/* 30 */     boolean found = false;
/* 31 */     Constructor[] cstructs = (Constructor[])wrapper.getConstructors();
/* 32 */     for (int i = 0; i < cstructs.length; i++) {
/* 33 */       Class[] types = cstructs[i].getParameterTypes();
/* 34 */       if (types.length == 1 && types[0].equals(Throwable.class)) {
/* 35 */         found = true;
/*    */         break;
/*    */       } 
/*    */     } 
/* 39 */     if (!found)
/* 40 */       throw new IllegalArgumentException(wrapper + " does not have a single-arg constructor that takes a Throwable"); 
/*    */   }
/*    */   
/*    */   public CodeEmitter begin_method(int access, Signature sig, final Type[] exceptions) {
/* 44 */     CodeEmitter e = super.begin_method(access, sig, exceptions);
/* 45 */     if (TypeUtils.isAbstract(access) || sig.equals(Constants.SIG_STATIC)) {
/* 46 */       return e;
/*    */     }
/* 48 */     return new CodeEmitter(e)
/*    */       {
/*    */         
/* 51 */         private Block handler = begin_block();
/*    */         
/*    */         public void visitMaxs(int maxStack, int maxLocals) {
/* 54 */           this.handler.end();
/* 55 */           EmitUtils.wrap_undeclared_throwable(this, this.handler, exceptions, UndeclaredThrowableTransformer.this.wrapper);
/* 56 */           super.visitMaxs(maxStack, maxLocals);
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\transform\impl\UndeclaredThrowableTransformer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */