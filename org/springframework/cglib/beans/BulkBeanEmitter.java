/*     */ package org.springframework.cglib.beans;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import org.springframework.asm.ClassVisitor;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.cglib.core.Block;
/*     */ import org.springframework.cglib.core.ClassEmitter;
/*     */ import org.springframework.cglib.core.CodeEmitter;
/*     */ import org.springframework.cglib.core.Constants;
/*     */ import org.springframework.cglib.core.EmitUtils;
/*     */ import org.springframework.cglib.core.Local;
/*     */ import org.springframework.cglib.core.MethodInfo;
/*     */ import org.springframework.cglib.core.ReflectUtils;
/*     */ import org.springframework.cglib.core.Signature;
/*     */ import org.springframework.cglib.core.TypeUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class BulkBeanEmitter
/*     */   extends ClassEmitter
/*     */ {
/*  28 */   private static final Signature GET_PROPERTY_VALUES = TypeUtils.parseSignature("void getPropertyValues(Object, Object[])");
/*     */   
/*  30 */   private static final Signature SET_PROPERTY_VALUES = TypeUtils.parseSignature("void setPropertyValues(Object, Object[])");
/*     */   
/*  32 */   private static final Signature CSTRUCT_EXCEPTION = TypeUtils.parseConstructor("Throwable, int");
/*     */   
/*  34 */   private static final Type BULK_BEAN = TypeUtils.parseType("org.springframework.cglib.beans.BulkBean");
/*     */   
/*  36 */   private static final Type BULK_BEAN_EXCEPTION = TypeUtils.parseType("org.springframework.cglib.beans.BulkBeanException");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BulkBeanEmitter(ClassVisitor v, String className, Class target, String[] getterNames, String[] setterNames, Class[] types) {
/*  44 */     super(v);
/*     */     
/*  46 */     Method[] getters = new Method[getterNames.length];
/*  47 */     Method[] setters = new Method[setterNames.length];
/*  48 */     validate(target, getterNames, setterNames, types, getters, setters);
/*     */     
/*  50 */     begin_class(52, 1, className, BULK_BEAN, null, "<generated>");
/*  51 */     EmitUtils.null_constructor(this);
/*  52 */     generateGet(target, getters);
/*  53 */     generateSet(target, setters);
/*  54 */     end_class();
/*     */   }
/*     */   
/*     */   private void generateGet(Class target, Method[] getters) {
/*  58 */     CodeEmitter e = begin_method(1, GET_PROPERTY_VALUES, null);
/*  59 */     if (getters.length > 0) {
/*  60 */       e.load_arg(0);
/*  61 */       e.checkcast(Type.getType(target));
/*  62 */       Local bean = e.make_local();
/*  63 */       e.store_local(bean);
/*  64 */       for (int i = 0; i < getters.length; i++) {
/*  65 */         if (getters[i] != null) {
/*  66 */           MethodInfo getter = ReflectUtils.getMethodInfo(getters[i]);
/*  67 */           e.load_arg(1);
/*  68 */           e.push(i);
/*  69 */           e.load_local(bean);
/*  70 */           e.invoke(getter);
/*  71 */           e.box(getter.getSignature().getReturnType());
/*  72 */           e.aastore();
/*     */         } 
/*     */       } 
/*     */     } 
/*  76 */     e.return_value();
/*  77 */     e.end_method();
/*     */   }
/*     */ 
/*     */   
/*     */   private void generateSet(Class target, Method[] setters) {
/*  82 */     CodeEmitter e = begin_method(1, SET_PROPERTY_VALUES, null);
/*  83 */     if (setters.length > 0) {
/*  84 */       Local index = e.make_local(Type.INT_TYPE);
/*  85 */       e.push(0);
/*  86 */       e.store_local(index);
/*  87 */       e.load_arg(0);
/*  88 */       e.checkcast(Type.getType(target));
/*  89 */       e.load_arg(1);
/*  90 */       Block handler = e.begin_block();
/*  91 */       int lastIndex = 0;
/*  92 */       for (int i = 0; i < setters.length; i++) {
/*  93 */         if (setters[i] != null) {
/*  94 */           MethodInfo setter = ReflectUtils.getMethodInfo(setters[i]);
/*  95 */           int diff = i - lastIndex;
/*  96 */           if (diff > 0) {
/*  97 */             e.iinc(index, diff);
/*  98 */             lastIndex = i;
/*     */           } 
/* 100 */           e.dup2();
/* 101 */           e.aaload(i);
/* 102 */           e.unbox(setter.getSignature().getArgumentTypes()[0]);
/* 103 */           e.invoke(setter);
/*     */         } 
/*     */       } 
/* 106 */       handler.end();
/* 107 */       e.return_value();
/* 108 */       e.catch_exception(handler, Constants.TYPE_THROWABLE);
/* 109 */       e.new_instance(BULK_BEAN_EXCEPTION);
/* 110 */       e.dup_x1();
/* 111 */       e.swap();
/* 112 */       e.load_local(index);
/* 113 */       e.invoke_constructor(BULK_BEAN_EXCEPTION, CSTRUCT_EXCEPTION);
/* 114 */       e.athrow();
/*     */     } else {
/* 116 */       e.return_value();
/*     */     } 
/* 118 */     e.end_method();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void validate(Class target, String[] getters, String[] setters, Class[] types, Method[] getters_out, Method[] setters_out) {
/* 127 */     int i = -1;
/* 128 */     if (setters.length != types.length || getters.length != types.length) {
/* 129 */       throw new BulkBeanException("accessor array length must be equal type array length", i);
/*     */     }
/*     */     try {
/* 132 */       for (i = 0; i < types.length; i++) {
/* 133 */         if (getters[i] != null) {
/* 134 */           Method method = ReflectUtils.findDeclaredMethod(target, getters[i], null);
/* 135 */           if (method.getReturnType() != types[i]) {
/* 136 */             throw new BulkBeanException("Specified type " + types[i] + " does not match declared type " + method
/* 137 */                 .getReturnType(), i);
/*     */           }
/* 139 */           if (Modifier.isPrivate(method.getModifiers())) {
/* 140 */             throw new BulkBeanException("Property is private", i);
/*     */           }
/* 142 */           getters_out[i] = method;
/*     */         } 
/* 144 */         if (setters[i] != null) {
/* 145 */           Method method = ReflectUtils.findDeclaredMethod(target, setters[i], new Class[] { types[i] });
/* 146 */           if (Modifier.isPrivate(method.getModifiers())) {
/* 147 */             throw new BulkBeanException("Property is private", i);
/*     */           }
/* 149 */           setters_out[i] = method;
/*     */         } 
/*     */       } 
/* 152 */     } catch (NoSuchMethodException e) {
/* 153 */       throw new BulkBeanException("Cannot find specified property", i);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\beans\BulkBeanEmitter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */