/*    */ package org.springframework.cglib.transform.impl;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import org.springframework.asm.Type;
/*    */ import org.springframework.cglib.core.CodeEmitter;
/*    */ import org.springframework.cglib.core.Constants;
/*    */ import org.springframework.cglib.core.EmitUtils;
/*    */ import org.springframework.cglib.core.MethodInfo;
/*    */ import org.springframework.cglib.core.ReflectUtils;
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
/*    */ 
/*    */ public class AddStaticInitTransformer
/*    */   extends ClassEmitterTransformer
/*    */ {
/*    */   private MethodInfo info;
/*    */   
/*    */   public AddStaticInitTransformer(Method classInit) {
/* 30 */     this.info = ReflectUtils.getMethodInfo(classInit);
/* 31 */     if (!TypeUtils.isStatic(this.info.getModifiers())) {
/* 32 */       throw new IllegalArgumentException(classInit + " is not static");
/*    */     }
/* 34 */     Type[] types = this.info.getSignature().getArgumentTypes();
/* 35 */     if (types.length != 1 || 
/* 36 */       !types[0].equals(Constants.TYPE_CLASS) || 
/* 37 */       !this.info.getSignature().getReturnType().equals(Type.VOID_TYPE)) {
/* 38 */       throw new IllegalArgumentException(classInit + " illegal signature");
/*    */     }
/*    */   }
/*    */   
/*    */   protected void init() {
/* 43 */     if (!TypeUtils.isInterface(getAccess())) {
/* 44 */       CodeEmitter e = getStaticHook();
/* 45 */       EmitUtils.load_class_this(e);
/* 46 */       e.invoke(this.info);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\transform\impl\AddStaticInitTransformer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */