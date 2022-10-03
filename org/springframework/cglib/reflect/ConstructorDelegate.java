/*     */ package org.springframework.cglib.reflect;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.ProtectionDomain;
/*     */ import org.springframework.asm.ClassVisitor;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.cglib.core.AbstractClassGenerator;
/*     */ import org.springframework.cglib.core.ClassEmitter;
/*     */ import org.springframework.cglib.core.CodeEmitter;
/*     */ import org.springframework.cglib.core.EmitUtils;
/*     */ import org.springframework.cglib.core.KeyFactory;
/*     */ import org.springframework.cglib.core.ReflectUtils;
/*     */ import org.springframework.cglib.core.TypeUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ConstructorDelegate
/*     */ {
/*  30 */   private static final ConstructorKey KEY_FACTORY = (ConstructorKey)KeyFactory.create(ConstructorKey.class, KeyFactory.CLASS_BY_NAME);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ConstructorDelegate create(Class targetClass, Class iface) {
/*  40 */     Generator gen = new Generator();
/*  41 */     gen.setTargetClass(targetClass);
/*  42 */     gen.setInterface(iface);
/*  43 */     return gen.create();
/*     */   }
/*     */   
/*     */   public static class Generator extends AbstractClassGenerator {
/*  47 */     private static final AbstractClassGenerator.Source SOURCE = new AbstractClassGenerator.Source(ConstructorDelegate.class.getName());
/*     */     
/*  49 */     private static final Type CONSTRUCTOR_DELEGATE = TypeUtils.parseType("org.springframework.cglib.reflect.ConstructorDelegate");
/*     */     
/*     */     private Class iface;
/*     */     private Class targetClass;
/*     */     
/*     */     public Generator() {
/*  55 */       super(SOURCE);
/*     */     }
/*     */     
/*     */     public void setInterface(Class iface) {
/*  59 */       this.iface = iface;
/*     */     }
/*     */     
/*     */     public void setTargetClass(Class targetClass) {
/*  63 */       this.targetClass = targetClass;
/*     */     }
/*     */     
/*     */     public ConstructorDelegate create() {
/*  67 */       setNamePrefix(this.targetClass.getName());
/*  68 */       Object key = ConstructorDelegate.KEY_FACTORY.newInstance(this.iface.getName(), this.targetClass.getName());
/*  69 */       return (ConstructorDelegate)create(key);
/*     */     }
/*     */     
/*     */     protected ClassLoader getDefaultClassLoader() {
/*  73 */       return this.targetClass.getClassLoader();
/*     */     }
/*     */     
/*     */     protected ProtectionDomain getProtectionDomain() {
/*  77 */       return ReflectUtils.getProtectionDomain(this.targetClass);
/*     */     }
/*     */     public void generateClass(ClassVisitor v) {
/*     */       Constructor constructor;
/*  81 */       setNamePrefix(this.targetClass.getName());
/*     */       
/*  83 */       Method newInstance = ReflectUtils.findNewInstance(this.iface);
/*  84 */       if (!newInstance.getReturnType().isAssignableFrom(this.targetClass)) {
/*  85 */         throw new IllegalArgumentException("incompatible return type");
/*     */       }
/*     */       
/*     */       try {
/*  89 */         constructor = this.targetClass.getDeclaredConstructor(newInstance.getParameterTypes());
/*  90 */       } catch (NoSuchMethodException noSuchMethodException) {
/*  91 */         throw new IllegalArgumentException("interface does not match any known constructor");
/*     */       } 
/*     */       
/*  94 */       ClassEmitter ce = new ClassEmitter(v);
/*  95 */       ce.begin_class(52, 1, 
/*     */           
/*  97 */           getClassName(), CONSTRUCTOR_DELEGATE, new Type[] {
/*     */             
/*  99 */             Type.getType(this.iface) }, "<generated>");
/*     */       
/* 101 */       Type declaring = Type.getType(constructor.getDeclaringClass());
/* 102 */       EmitUtils.null_constructor(ce);
/* 103 */       CodeEmitter e = ce.begin_method(1, 
/* 104 */           ReflectUtils.getSignature(newInstance), 
/* 105 */           ReflectUtils.getExceptionTypes(newInstance));
/* 106 */       e.new_instance(declaring);
/* 107 */       e.dup();
/* 108 */       e.load_args();
/* 109 */       e.invoke_constructor(declaring, ReflectUtils.getSignature(constructor));
/* 110 */       e.return_value();
/* 111 */       e.end_method();
/* 112 */       ce.end_class();
/*     */     }
/*     */     
/*     */     protected Object firstInstance(Class type) {
/* 116 */       return ReflectUtils.newInstance(type);
/*     */     }
/*     */     
/*     */     protected Object nextInstance(Object instance) {
/* 120 */       return instance;
/*     */     }
/*     */   }
/*     */   
/*     */   static interface ConstructorKey {
/*     */     Object newInstance(String param1String1, String param1String2);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\reflect\ConstructorDelegate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */