/*    */ package org.springframework.cglib.transform.impl;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import org.springframework.asm.Type;
/*    */ import org.springframework.cglib.core.CodeEmitter;
/*    */ import org.springframework.cglib.core.Constants;
/*    */ import org.springframework.cglib.core.MethodInfo;
/*    */ import org.springframework.cglib.core.ReflectUtils;
/*    */ import org.springframework.cglib.core.Signature;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AddInitTransformer
/*    */   extends ClassEmitterTransformer
/*    */ {
/*    */   private MethodInfo info;
/*    */   
/*    */   public AddInitTransformer(Method method) {
/* 37 */     this.info = ReflectUtils.getMethodInfo(method);
/*    */     
/* 39 */     Type[] types = this.info.getSignature().getArgumentTypes();
/* 40 */     if (types.length != 1 || 
/* 41 */       !types[0].equals(Constants.TYPE_OBJECT) || 
/* 42 */       !this.info.getSignature().getReturnType().equals(Type.VOID_TYPE)) {
/* 43 */       throw new IllegalArgumentException(method + " illegal signature");
/*    */     }
/*    */   }
/*    */   
/*    */   public CodeEmitter begin_method(int access, Signature sig, Type[] exceptions) {
/* 48 */     CodeEmitter emitter = super.begin_method(access, sig, exceptions);
/* 49 */     if (sig.getName().equals("<init>")) {
/* 50 */       return new CodeEmitter(emitter) {
/*    */           public void visitInsn(int opcode) {
/* 52 */             if (opcode == 177) {
/* 53 */               load_this();
/* 54 */               invoke(AddInitTransformer.this.info);
/*    */             } 
/* 56 */             super.visitInsn(opcode);
/*    */           }
/*    */         };
/*    */     }
/* 60 */     return emitter;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\transform\impl\AddInitTransformer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */