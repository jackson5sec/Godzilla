/*    */ package org.springframework.cglib.proxy;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import org.springframework.asm.ClassVisitor;
/*    */ import org.springframework.asm.Type;
/*    */ import org.springframework.cglib.core.ClassEmitter;
/*    */ import org.springframework.cglib.core.CodeEmitter;
/*    */ import org.springframework.cglib.core.Constants;
/*    */ import org.springframework.cglib.core.EmitUtils;
/*    */ import org.springframework.cglib.core.MethodInfo;
/*    */ import org.springframework.cglib.core.MethodWrapper;
/*    */ import org.springframework.cglib.core.ReflectUtils;
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
/*    */ 
/*    */ class MixinEmitter
/*    */   extends ClassEmitter
/*    */ {
/*    */   private static final String FIELD_NAME = "CGLIB$DELEGATES";
/* 31 */   private static final Signature CSTRUCT_OBJECT_ARRAY = TypeUtils.parseConstructor("Object[]");
/*    */   
/* 33 */   private static final Type MIXIN = TypeUtils.parseType("org.springframework.cglib.proxy.Mixin");
/* 34 */   private static final Signature NEW_INSTANCE = new Signature("newInstance", MIXIN, new Type[] { Constants.TYPE_OBJECT_ARRAY });
/*    */ 
/*    */   
/*    */   public MixinEmitter(ClassVisitor v, String className, Class[] classes, int[] route) {
/* 38 */     super(v);
/*    */     
/* 40 */     begin_class(52, 1, className, MIXIN, 
/*    */ 
/*    */ 
/*    */         
/* 44 */         TypeUtils.getTypes(getInterfaces(classes)), "<generated>");
/*    */     
/* 46 */     EmitUtils.null_constructor(this);
/* 47 */     EmitUtils.factory_method(this, NEW_INSTANCE);
/*    */     
/* 49 */     declare_field(2, "CGLIB$DELEGATES", Constants.TYPE_OBJECT_ARRAY, null);
/*    */     
/* 51 */     CodeEmitter e = begin_method(1, CSTRUCT_OBJECT_ARRAY, null);
/* 52 */     e.load_this();
/* 53 */     e.super_invoke_constructor();
/* 54 */     e.load_this();
/* 55 */     e.load_arg(0);
/* 56 */     e.putfield("CGLIB$DELEGATES");
/* 57 */     e.return_value();
/* 58 */     e.end_method();
/*    */     
/* 60 */     Set<Object> unique = new HashSet();
/* 61 */     for (int i = 0; i < classes.length; i++) {
/* 62 */       Method[] methods = getMethods(classes[i]);
/* 63 */       for (int j = 0; j < methods.length; j++) {
/* 64 */         if (unique.add(MethodWrapper.create(methods[j]))) {
/* 65 */           MethodInfo method = ReflectUtils.getMethodInfo(methods[j]);
/* 66 */           int modifiers = 1;
/* 67 */           if ((method.getModifiers() & 0x80) == 128) {
/* 68 */             modifiers |= 0x80;
/*    */           }
/* 70 */           e = EmitUtils.begin_method(this, method, modifiers);
/* 71 */           e.load_this();
/* 72 */           e.getfield("CGLIB$DELEGATES");
/* 73 */           e.aaload((route != null) ? route[i] : i);
/* 74 */           e.checkcast(method.getClassInfo().getType());
/* 75 */           e.load_args();
/* 76 */           e.invoke(method);
/* 77 */           e.return_value();
/* 78 */           e.end_method();
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 83 */     end_class();
/*    */   }
/*    */   
/*    */   protected Class[] getInterfaces(Class[] classes) {
/* 87 */     return classes;
/*    */   }
/*    */   
/*    */   protected Method[] getMethods(Class type) {
/* 91 */     return type.getMethods();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\proxy\MixinEmitter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */