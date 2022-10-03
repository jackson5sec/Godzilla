/*     */ package org.springframework.cglib.beans;
/*     */ 
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.ProtectionDomain;
/*     */ import org.springframework.asm.ClassVisitor;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.cglib.core.AbstractClassGenerator;
/*     */ import org.springframework.cglib.core.ClassEmitter;
/*     */ import org.springframework.cglib.core.CodeEmitter;
/*     */ import org.springframework.cglib.core.EmitUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ImmutableBean
/*     */ {
/*  30 */   private static final Type ILLEGAL_STATE_EXCEPTION = TypeUtils.parseType("IllegalStateException");
/*     */   
/*  32 */   private static final Signature CSTRUCT_OBJECT = TypeUtils.parseConstructor("Object");
/*  33 */   private static final Class[] OBJECT_CLASSES = new Class[] { Object.class };
/*     */ 
/*     */   
/*     */   private static final String FIELD_NAME = "CGLIB$RWBean";
/*     */ 
/*     */   
/*     */   public static Object create(Object bean) {
/*  40 */     Generator gen = new Generator();
/*  41 */     gen.setBean(bean);
/*  42 */     return gen.create();
/*     */   }
/*     */   
/*     */   public static class Generator extends AbstractClassGenerator {
/*  46 */     private static final AbstractClassGenerator.Source SOURCE = new AbstractClassGenerator.Source(ImmutableBean.class.getName());
/*     */     private Object bean;
/*     */     private Class target;
/*     */     
/*     */     public Generator() {
/*  51 */       super(SOURCE);
/*     */     }
/*     */     
/*     */     public void setBean(Object bean) {
/*  55 */       this.bean = bean;
/*  56 */       this.target = bean.getClass();
/*     */     }
/*     */     
/*     */     protected ClassLoader getDefaultClassLoader() {
/*  60 */       return this.target.getClassLoader();
/*     */     }
/*     */     
/*     */     protected ProtectionDomain getProtectionDomain() {
/*  64 */       return ReflectUtils.getProtectionDomain(this.target);
/*     */     }
/*     */     
/*     */     public Object create() {
/*  68 */       String name = this.target.getName();
/*  69 */       setNamePrefix(name);
/*  70 */       return create(name);
/*     */     }
/*     */     
/*     */     public void generateClass(ClassVisitor v) {
/*  74 */       Type targetType = Type.getType(this.target);
/*  75 */       ClassEmitter ce = new ClassEmitter(v);
/*  76 */       ce.begin_class(52, 1, 
/*     */           
/*  78 */           getClassName(), targetType, null, "<generated>");
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  83 */       ce.declare_field(18, "CGLIB$RWBean", targetType, null);
/*     */       
/*  85 */       CodeEmitter e = ce.begin_method(1, ImmutableBean.CSTRUCT_OBJECT, null);
/*  86 */       e.load_this();
/*  87 */       e.super_invoke_constructor();
/*  88 */       e.load_this();
/*  89 */       e.load_arg(0);
/*  90 */       e.checkcast(targetType);
/*  91 */       e.putfield("CGLIB$RWBean");
/*  92 */       e.return_value();
/*  93 */       e.end_method();
/*     */       
/*  95 */       PropertyDescriptor[] descriptors = ReflectUtils.getBeanProperties(this.target);
/*  96 */       Method[] getters = ReflectUtils.getPropertyMethods(descriptors, true, false);
/*  97 */       Method[] setters = ReflectUtils.getPropertyMethods(descriptors, false, true);
/*     */       int i;
/*  99 */       for (i = 0; i < getters.length; i++) {
/* 100 */         MethodInfo getter = ReflectUtils.getMethodInfo(getters[i]);
/* 101 */         e = EmitUtils.begin_method(ce, getter, 1);
/* 102 */         e.load_this();
/* 103 */         e.getfield("CGLIB$RWBean");
/* 104 */         e.invoke(getter);
/* 105 */         e.return_value();
/* 106 */         e.end_method();
/*     */       } 
/*     */       
/* 109 */       for (i = 0; i < setters.length; i++) {
/* 110 */         MethodInfo setter = ReflectUtils.getMethodInfo(setters[i]);
/* 111 */         e = EmitUtils.begin_method(ce, setter, 1);
/* 112 */         e.throw_exception(ImmutableBean.ILLEGAL_STATE_EXCEPTION, "Bean is immutable");
/* 113 */         e.end_method();
/*     */       } 
/*     */       
/* 116 */       ce.end_class();
/*     */     }
/*     */     
/*     */     protected Object firstInstance(Class type) {
/* 120 */       return ReflectUtils.newInstance(type, ImmutableBean.OBJECT_CLASSES, new Object[] { this.bean });
/*     */     }
/*     */ 
/*     */     
/*     */     protected Object nextInstance(Object instance) {
/* 125 */       return firstInstance(instance.getClass());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\beans\ImmutableBean.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */