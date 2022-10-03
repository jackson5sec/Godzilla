/*    */ package org.springframework.cglib.proxy;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import org.springframework.asm.Type;
/*    */ import org.springframework.cglib.core.ClassEmitter;
/*    */ import org.springframework.cglib.core.CodeEmitter;
/*    */ import org.springframework.cglib.core.MethodInfo;
/*    */ import org.springframework.cglib.core.Signature;
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
/*    */ class DispatcherGenerator
/*    */   implements CallbackGenerator
/*    */ {
/* 23 */   public static final DispatcherGenerator INSTANCE = new DispatcherGenerator(false);
/*    */   
/* 25 */   public static final DispatcherGenerator PROXY_REF_INSTANCE = new DispatcherGenerator(true);
/*    */ 
/*    */ 
/*    */   
/* 29 */   private static final Type DISPATCHER = TypeUtils.parseType("org.springframework.cglib.proxy.Dispatcher");
/*    */   
/* 31 */   private static final Type PROXY_REF_DISPATCHER = TypeUtils.parseType("org.springframework.cglib.proxy.ProxyRefDispatcher");
/*    */   
/* 33 */   private static final Signature LOAD_OBJECT = TypeUtils.parseSignature("Object loadObject()");
/*    */   
/* 35 */   private static final Signature PROXY_REF_LOAD_OBJECT = TypeUtils.parseSignature("Object loadObject(Object)");
/*    */   
/*    */   private boolean proxyRef;
/*    */   
/*    */   private DispatcherGenerator(boolean proxyRef) {
/* 40 */     this.proxyRef = proxyRef;
/*    */   }
/*    */   
/*    */   public void generate(ClassEmitter ce, CallbackGenerator.Context context, List methods) {
/* 44 */     for (Iterator<MethodInfo> it = methods.iterator(); it.hasNext(); ) {
/* 45 */       MethodInfo method = it.next();
/* 46 */       if (!TypeUtils.isProtected(method.getModifiers())) {
/* 47 */         CodeEmitter e = context.beginMethod(ce, method);
/* 48 */         context.emitCallback(e, context.getIndex(method));
/* 49 */         if (this.proxyRef) {
/* 50 */           e.load_this();
/* 51 */           e.invoke_interface(PROXY_REF_DISPATCHER, PROXY_REF_LOAD_OBJECT);
/*    */         } else {
/* 53 */           e.invoke_interface(DISPATCHER, LOAD_OBJECT);
/*    */         } 
/* 55 */         e.checkcast(method.getClassInfo().getType());
/* 56 */         e.load_args();
/* 57 */         e.invoke(method);
/* 58 */         e.return_value();
/* 59 */         e.end_method();
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public void generateStatic(CodeEmitter e, CallbackGenerator.Context context, List methods) {}
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\proxy\DispatcherGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */