/*     */ package org.springframework.cglib.beans;
/*     */ 
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.asm.ClassVisitor;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.cglib.core.AbstractClassGenerator;
/*     */ import org.springframework.cglib.core.ClassEmitter;
/*     */ import org.springframework.cglib.core.CodeEmitter;
/*     */ import org.springframework.cglib.core.Constants;
/*     */ import org.springframework.cglib.core.Converter;
/*     */ import org.springframework.cglib.core.EmitUtils;
/*     */ import org.springframework.cglib.core.KeyFactory;
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
/*     */ public abstract class BeanCopier
/*     */ {
/*  32 */   private static final BeanCopierKey KEY_FACTORY = (BeanCopierKey)KeyFactory.create(BeanCopierKey.class);
/*     */   
/*  34 */   private static final Type CONVERTER = TypeUtils.parseType("org.springframework.cglib.core.Converter");
/*     */   
/*  36 */   private static final Type BEAN_COPIER = TypeUtils.parseType("org.springframework.cglib.beans.BeanCopier");
/*  37 */   private static final Signature COPY = new Signature("copy", Type.VOID_TYPE, new Type[] { Constants.TYPE_OBJECT, Constants.TYPE_OBJECT, CONVERTER });
/*     */ 
/*     */   
/*  40 */   private static final Signature CONVERT = TypeUtils.parseSignature("Object convert(Object, Class, Object)");
/*     */ 
/*     */   
/*     */   public abstract void copy(Object paramObject1, Object paramObject2, Converter paramConverter);
/*     */ 
/*     */   
/*     */   public static BeanCopier create(Class source, Class target, boolean useConverter) {
/*  47 */     Generator gen = new Generator();
/*  48 */     gen.setSource(source);
/*  49 */     gen.setTarget(target);
/*  50 */     gen.setUseConverter(useConverter);
/*  51 */     return gen.create();
/*     */   }
/*     */   
/*     */   public static class Generator
/*     */     extends AbstractClassGenerator
/*     */   {
/*  57 */     private static final AbstractClassGenerator.Source SOURCE = new AbstractClassGenerator.Source(BeanCopier.class.getName());
/*     */     private Class source;
/*     */     private Class target;
/*     */     private boolean useConverter;
/*     */     
/*     */     public Generator() {
/*  63 */       super(SOURCE);
/*     */     }
/*     */     
/*     */     public void setSource(Class source) {
/*  67 */       if (!Modifier.isPublic(source.getModifiers())) {
/*  68 */         setNamePrefix(source.getName());
/*     */       }
/*  70 */       this.source = source;
/*     */     }
/*     */     
/*     */     public void setTarget(Class target) {
/*  74 */       if (!Modifier.isPublic(target.getModifiers())) {
/*  75 */         setNamePrefix(target.getName());
/*     */       }
/*     */       
/*  78 */       this.target = target;
/*     */     }
/*     */     
/*     */     public void setUseConverter(boolean useConverter) {
/*  82 */       this.useConverter = useConverter;
/*     */     }
/*     */     
/*     */     protected ClassLoader getDefaultClassLoader() {
/*  86 */       return this.source.getClassLoader();
/*     */     }
/*     */     
/*     */     protected ProtectionDomain getProtectionDomain() {
/*  90 */       return ReflectUtils.getProtectionDomain(this.source);
/*     */     }
/*     */     
/*     */     public BeanCopier create() {
/*  94 */       Object key = BeanCopier.KEY_FACTORY.newInstance(this.source.getName(), this.target.getName(), this.useConverter);
/*  95 */       return (BeanCopier)create(key);
/*     */     }
/*     */     
/*     */     public void generateClass(ClassVisitor v) {
/*  99 */       Type sourceType = Type.getType(this.source);
/* 100 */       Type targetType = Type.getType(this.target);
/* 101 */       ClassEmitter ce = new ClassEmitter(v);
/* 102 */       ce.begin_class(52, 1, 
/*     */           
/* 104 */           getClassName(), BeanCopier
/* 105 */           .BEAN_COPIER, null, "<generated>");
/*     */ 
/*     */ 
/*     */       
/* 109 */       EmitUtils.null_constructor(ce);
/* 110 */       CodeEmitter e = ce.begin_method(1, BeanCopier.COPY, null);
/* 111 */       PropertyDescriptor[] getters = ReflectUtils.getBeanGetters(this.source);
/* 112 */       PropertyDescriptor[] setters = ReflectUtils.getBeanSetters(this.target);
/*     */       
/* 114 */       Map<Object, Object> names = new HashMap<Object, Object>();
/* 115 */       for (int i = 0; i < getters.length; i++) {
/* 116 */         names.put(getters[i].getName(), getters[i]);
/*     */       }
/* 118 */       Local targetLocal = e.make_local();
/* 119 */       Local sourceLocal = e.make_local();
/* 120 */       if (this.useConverter) {
/* 121 */         e.load_arg(1);
/* 122 */         e.checkcast(targetType);
/* 123 */         e.store_local(targetLocal);
/* 124 */         e.load_arg(0);
/* 125 */         e.checkcast(sourceType);
/* 126 */         e.store_local(sourceLocal);
/*     */       } else {
/* 128 */         e.load_arg(1);
/* 129 */         e.checkcast(targetType);
/* 130 */         e.load_arg(0);
/* 131 */         e.checkcast(sourceType);
/*     */       } 
/* 133 */       for (int j = 0; j < setters.length; j++) {
/* 134 */         PropertyDescriptor setter = setters[j];
/* 135 */         PropertyDescriptor getter = (PropertyDescriptor)names.get(setter.getName());
/* 136 */         if (getter != null) {
/* 137 */           MethodInfo read = ReflectUtils.getMethodInfo(getter.getReadMethod());
/* 138 */           MethodInfo write = ReflectUtils.getMethodInfo(setter.getWriteMethod());
/* 139 */           if (this.useConverter) {
/* 140 */             Type setterType = write.getSignature().getArgumentTypes()[0];
/* 141 */             e.load_local(targetLocal);
/* 142 */             e.load_arg(2);
/* 143 */             e.load_local(sourceLocal);
/* 144 */             e.invoke(read);
/* 145 */             e.box(read.getSignature().getReturnType());
/* 146 */             EmitUtils.load_class(e, setterType);
/* 147 */             e.push(write.getSignature().getName());
/* 148 */             e.invoke_interface(BeanCopier.CONVERTER, BeanCopier.CONVERT);
/* 149 */             e.unbox_or_zero(setterType);
/* 150 */             e.invoke(write);
/* 151 */           } else if (compatible(getter, setter)) {
/* 152 */             e.dup2();
/* 153 */             e.invoke(read);
/* 154 */             e.invoke(write);
/*     */           } 
/*     */         } 
/*     */       } 
/* 158 */       e.return_value();
/* 159 */       e.end_method();
/* 160 */       ce.end_class();
/*     */     }
/*     */ 
/*     */     
/*     */     private static boolean compatible(PropertyDescriptor getter, PropertyDescriptor setter) {
/* 165 */       return setter.getPropertyType().isAssignableFrom(getter.getPropertyType());
/*     */     }
/*     */     
/*     */     protected Object firstInstance(Class type) {
/* 169 */       return ReflectUtils.newInstance(type);
/*     */     }
/*     */     
/*     */     protected Object nextInstance(Object instance) {
/* 173 */       return instance;
/*     */     }
/*     */   }
/*     */   
/*     */   static interface BeanCopierKey {
/*     */     Object newInstance(String param1String1, String param1String2, boolean param1Boolean);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\beans\BeanCopier.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */