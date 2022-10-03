/*    */ package org.springframework.cglib.proxy;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import org.springframework.cglib.core.ClassEmitter;
/*    */ import org.springframework.cglib.core.CodeEmitter;
/*    */ import org.springframework.cglib.core.EmitUtils;
/*    */ import org.springframework.cglib.core.MethodInfo;
/*    */ import org.springframework.cglib.core.TypeUtils;
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
/*    */ class NoOpGenerator
/*    */   implements CallbackGenerator
/*    */ {
/* 25 */   public static final NoOpGenerator INSTANCE = new NoOpGenerator();
/*    */   
/*    */   public void generate(ClassEmitter ce, CallbackGenerator.Context context, List methods) {
/* 28 */     for (Iterator<MethodInfo> it = methods.iterator(); it.hasNext(); ) {
/* 29 */       MethodInfo method = it.next();
/* 30 */       if (TypeUtils.isBridge(method.getModifiers()) || (
/* 31 */         TypeUtils.isProtected(context.getOriginalModifiers(method)) && 
/* 32 */         TypeUtils.isPublic(method.getModifiers()))) {
/* 33 */         CodeEmitter e = EmitUtils.begin_method(ce, method);
/* 34 */         e.load_this();
/* 35 */         context.emitLoadArgsAndInvoke(e, method);
/* 36 */         e.return_value();
/* 37 */         e.end_method();
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public void generateStatic(CodeEmitter e, CallbackGenerator.Context context, List methods) {}
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\proxy\NoOpGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */