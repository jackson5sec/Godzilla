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
/*    */ class FixedValueGenerator
/*    */   implements CallbackGenerator
/*    */ {
/* 23 */   public static final FixedValueGenerator INSTANCE = new FixedValueGenerator();
/*    */   
/* 25 */   private static final Type FIXED_VALUE = TypeUtils.parseType("org.springframework.cglib.proxy.FixedValue");
/*    */   
/* 27 */   private static final Signature LOAD_OBJECT = TypeUtils.parseSignature("Object loadObject()");
/*    */   
/*    */   public void generate(ClassEmitter ce, CallbackGenerator.Context context, List methods) {
/* 30 */     for (Iterator<MethodInfo> it = methods.iterator(); it.hasNext(); ) {
/* 31 */       MethodInfo method = it.next();
/* 32 */       CodeEmitter e = context.beginMethod(ce, method);
/* 33 */       context.emitCallback(e, context.getIndex(method));
/* 34 */       e.invoke_interface(FIXED_VALUE, LOAD_OBJECT);
/* 35 */       e.unbox_or_zero(e.getReturnType());
/* 36 */       e.return_value();
/* 37 */       e.end_method();
/*    */     } 
/*    */   }
/*    */   
/*    */   public void generateStatic(CodeEmitter e, CallbackGenerator.Context context, List methods) {}
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\proxy\FixedValueGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */