/*    */ package org.springframework.cglib.proxy;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import org.springframework.asm.Type;
/*    */ import org.springframework.cglib.core.Block;
/*    */ import org.springframework.cglib.core.ClassEmitter;
/*    */ import org.springframework.cglib.core.CodeEmitter;
/*    */ import org.springframework.cglib.core.EmitUtils;
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
/*    */ class InvocationHandlerGenerator
/*    */   implements CallbackGenerator
/*    */ {
/* 25 */   public static final InvocationHandlerGenerator INSTANCE = new InvocationHandlerGenerator();
/*    */ 
/*    */   
/* 28 */   private static final Type INVOCATION_HANDLER = TypeUtils.parseType("org.springframework.cglib.proxy.InvocationHandler");
/*    */   
/* 30 */   private static final Type UNDECLARED_THROWABLE_EXCEPTION = TypeUtils.parseType("org.springframework.cglib.proxy.UndeclaredThrowableException");
/*    */   
/* 32 */   private static final Type METHOD = TypeUtils.parseType("java.lang.reflect.Method");
/*    */   
/* 34 */   private static final Signature INVOKE = TypeUtils.parseSignature("Object invoke(Object, java.lang.reflect.Method, Object[])");
/*    */   
/*    */   public void generate(ClassEmitter ce, CallbackGenerator.Context context, List methods) {
/* 37 */     for (Iterator<MethodInfo> it = methods.iterator(); it.hasNext(); ) {
/* 38 */       MethodInfo method = it.next();
/* 39 */       Signature impl = context.getImplSignature(method);
/* 40 */       ce.declare_field(26, impl.getName(), METHOD, null);
/*    */       
/* 42 */       CodeEmitter e = context.beginMethod(ce, method);
/* 43 */       Block handler = e.begin_block();
/* 44 */       context.emitCallback(e, context.getIndex(method));
/* 45 */       e.load_this();
/* 46 */       e.getfield(impl.getName());
/* 47 */       e.create_arg_array();
/* 48 */       e.invoke_interface(INVOCATION_HANDLER, INVOKE);
/* 49 */       e.unbox(method.getSignature().getReturnType());
/* 50 */       e.return_value();
/* 51 */       handler.end();
/* 52 */       EmitUtils.wrap_undeclared_throwable(e, handler, method.getExceptionTypes(), UNDECLARED_THROWABLE_EXCEPTION);
/* 53 */       e.end_method();
/*    */     } 
/*    */   }
/*    */   
/*    */   public void generateStatic(CodeEmitter e, CallbackGenerator.Context context, List methods) {
/* 58 */     for (Iterator<MethodInfo> it = methods.iterator(); it.hasNext(); ) {
/* 59 */       MethodInfo method = it.next();
/* 60 */       EmitUtils.load_method(e, method);
/* 61 */       e.putfield(context.getImplSignature(method).getName());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\proxy\InvocationHandlerGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */