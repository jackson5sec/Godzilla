/*     */ package org.springframework.cglib.reflect;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.ProtectionDomain;
/*     */ import org.springframework.asm.ClassVisitor;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.cglib.core.AbstractClassGenerator;
/*     */ import org.springframework.cglib.core.Constants;
/*     */ import org.springframework.cglib.core.ReflectUtils;
/*     */ import org.springframework.cglib.core.Signature;
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
/*     */ public abstract class FastClass
/*     */ {
/*     */   private Class type;
/*     */   
/*     */   protected FastClass() {
/*  31 */     throw new Error("Using the FastClass empty constructor--please report to the cglib-devel mailing list");
/*     */   }
/*     */   
/*     */   protected FastClass(Class type) {
/*  35 */     this.type = type;
/*     */   }
/*     */ 
/*     */   
/*     */   public static FastClass create(Class type) {
/*  40 */     return create(type.getClassLoader(), type);
/*     */   }
/*     */   
/*     */   public static FastClass create(ClassLoader loader, Class type) {
/*  44 */     Generator gen = new Generator();
/*  45 */     gen.setType(type);
/*  46 */     gen.setClassLoader(loader);
/*  47 */     return gen.create();
/*     */   }
/*     */   
/*     */   public static class Generator
/*     */     extends AbstractClassGenerator {
/*  52 */     private static final AbstractClassGenerator.Source SOURCE = new AbstractClassGenerator.Source(FastClass.class.getName());
/*     */     private Class type;
/*     */     
/*     */     public Generator() {
/*  56 */       super(SOURCE);
/*     */     }
/*     */     
/*     */     public void setType(Class type) {
/*  60 */       this.type = type;
/*     */     }
/*     */     
/*     */     public FastClass create() {
/*  64 */       setNamePrefix(this.type.getName());
/*  65 */       return (FastClass)create(this.type.getName());
/*     */     }
/*     */     
/*     */     protected ClassLoader getDefaultClassLoader() {
/*  69 */       return this.type.getClassLoader();
/*     */     }
/*     */     
/*     */     protected ProtectionDomain getProtectionDomain() {
/*  73 */       return ReflectUtils.getProtectionDomain(this.type);
/*     */     }
/*     */     
/*     */     public void generateClass(ClassVisitor v) throws Exception {
/*  77 */       new FastClassEmitter(v, getClassName(), this.type);
/*     */     }
/*     */     
/*     */     protected Object firstInstance(Class type) {
/*  81 */       return ReflectUtils.newInstance(type, new Class[] { Class.class }, new Object[] { this.type });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected Object nextInstance(Object instance) {
/*  87 */       return instance;
/*     */     }
/*     */   }
/*     */   
/*     */   public Object invoke(String name, Class[] parameterTypes, Object obj, Object[] args) throws InvocationTargetException {
/*  92 */     return invoke(getIndex(name, parameterTypes), obj, args);
/*     */   }
/*     */   
/*     */   public Object newInstance() throws InvocationTargetException {
/*  96 */     return newInstance(getIndex(Constants.EMPTY_CLASS_ARRAY), (Object[])null);
/*     */   }
/*     */   
/*     */   public Object newInstance(Class[] parameterTypes, Object[] args) throws InvocationTargetException {
/* 100 */     return newInstance(getIndex(parameterTypes), args);
/*     */   }
/*     */   
/*     */   public FastMethod getMethod(Method method) {
/* 104 */     return new FastMethod(this, method);
/*     */   }
/*     */   
/*     */   public FastConstructor getConstructor(Constructor constructor) {
/* 108 */     return new FastConstructor(this, constructor);
/*     */   }
/*     */   
/*     */   public FastMethod getMethod(String name, Class[] parameterTypes) {
/*     */     try {
/* 113 */       return getMethod(this.type.getMethod(name, parameterTypes));
/* 114 */     } catch (NoSuchMethodException e) {
/* 115 */       throw new NoSuchMethodError(e.getMessage());
/*     */     } 
/*     */   }
/*     */   
/*     */   public FastConstructor getConstructor(Class[] parameterTypes) {
/*     */     try {
/* 121 */       return getConstructor(this.type.getConstructor(parameterTypes));
/* 122 */     } catch (NoSuchMethodException e) {
/* 123 */       throw new NoSuchMethodError(e.getMessage());
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getName() {
/* 128 */     return this.type.getName();
/*     */   }
/*     */   
/*     */   public Class getJavaClass() {
/* 132 */     return this.type;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 136 */     return this.type.toString();
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 140 */     return this.type.hashCode();
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 144 */     if (o == null || !(o instanceof FastClass)) {
/* 145 */       return false;
/*     */     }
/* 147 */     return this.type.equals(((FastClass)o).type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract int getIndex(String paramString, Class[] paramArrayOfClass);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract int getIndex(Class[] paramArrayOfClass);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Object invoke(int paramInt, Object paramObject, Object[] paramArrayOfObject) throws InvocationTargetException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Object newInstance(int paramInt, Object[] paramArrayOfObject) throws InvocationTargetException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract int getIndex(Signature paramSignature);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract int getMaxIndex();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static String getSignatureWithoutReturnType(String name, Class[] parameterTypes) {
/* 198 */     StringBuffer sb = new StringBuffer();
/* 199 */     sb.append(name);
/* 200 */     sb.append('(');
/* 201 */     for (int i = 0; i < parameterTypes.length; i++) {
/* 202 */       sb.append(Type.getDescriptor(parameterTypes[i]));
/*     */     }
/* 204 */     sb.append(')');
/* 205 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\reflect\FastClass.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */