/*    */ package org.springframework.cglib.proxy;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import org.springframework.asm.Label;
/*    */ import org.springframework.asm.Type;
/*    */ import org.springframework.cglib.core.ClassEmitter;
/*    */ import org.springframework.cglib.core.CodeEmitter;
/*    */ import org.springframework.cglib.core.Constants;
/*    */ import org.springframework.cglib.core.MethodInfo;
/*    */ import org.springframework.cglib.core.Signature;
/*    */ import org.springframework.cglib.core.TypeUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class LazyLoaderGenerator
/*    */   implements CallbackGenerator
/*    */ {
/* 24 */   public static final LazyLoaderGenerator INSTANCE = new LazyLoaderGenerator();
/*    */ 
/*    */   
/* 27 */   private static final Signature LOAD_OBJECT = TypeUtils.parseSignature("Object loadObject()");
/*    */   
/* 29 */   private static final Type LAZY_LOADER = TypeUtils.parseType("org.springframework.cglib.proxy.LazyLoader");
/*    */   
/*    */   public void generate(ClassEmitter ce, CallbackGenerator.Context context, List methods) {
/* 32 */     Set<Integer> indexes = new HashSet();
/* 33 */     for (Iterator<MethodInfo> iterator = methods.iterator(); iterator.hasNext(); ) {
/* 34 */       MethodInfo method = iterator.next();
/* 35 */       if (TypeUtils.isProtected(method.getModifiers())) {
/*    */         continue;
/*    */       }
/* 38 */       int index = context.getIndex(method);
/* 39 */       indexes.add(new Integer(index));
/* 40 */       CodeEmitter e = context.beginMethod(ce, method);
/* 41 */       e.load_this();
/* 42 */       e.dup();
/* 43 */       e.invoke_virtual_this(loadMethod(index));
/* 44 */       e.checkcast(method.getClassInfo().getType());
/* 45 */       e.load_args();
/* 46 */       e.invoke(method);
/* 47 */       e.return_value();
/* 48 */       e.end_method();
/*    */     } 
/*    */ 
/*    */     
/* 52 */     for (Iterator<Integer> it = indexes.iterator(); it.hasNext(); ) {
/* 53 */       int index = ((Integer)it.next()).intValue();
/*    */       
/* 55 */       String delegate = "CGLIB$LAZY_LOADER_" + index;
/* 56 */       ce.declare_field(2, delegate, Constants.TYPE_OBJECT, null);
/*    */       
/* 58 */       CodeEmitter e = ce.begin_method(50, 
/*    */ 
/*    */           
/* 61 */           loadMethod(index), null);
/*    */       
/* 63 */       e.load_this();
/* 64 */       e.getfield(delegate);
/* 65 */       e.dup();
/* 66 */       Label end = e.make_label();
/* 67 */       e.ifnonnull(end);
/* 68 */       e.pop();
/* 69 */       e.load_this();
/* 70 */       context.emitCallback(e, index);
/* 71 */       e.invoke_interface(LAZY_LOADER, LOAD_OBJECT);
/* 72 */       e.dup_x1();
/* 73 */       e.putfield(delegate);
/* 74 */       e.mark(end);
/* 75 */       e.return_value();
/* 76 */       e.end_method();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   private Signature loadMethod(int index) {
/* 82 */     return new Signature("CGLIB$LOAD_PRIVATE_" + index, Constants.TYPE_OBJECT, Constants.TYPES_EMPTY);
/*    */   }
/*    */   
/*    */   public void generateStatic(CodeEmitter e, CallbackGenerator.Context context, List methods) {}
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\proxy\LazyLoaderGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */