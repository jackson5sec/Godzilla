/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.NotSerializableException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectStreamClass;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConfigurableObjectInputStream
/*     */   extends ObjectInputStream
/*     */ {
/*     */   @Nullable
/*     */   private final ClassLoader classLoader;
/*     */   private final boolean acceptProxyClasses;
/*     */   
/*     */   public ConfigurableObjectInputStream(InputStream in, @Nullable ClassLoader classLoader) throws IOException {
/*  51 */     this(in, classLoader, true);
/*     */   }
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
/*     */   public ConfigurableObjectInputStream(InputStream in, @Nullable ClassLoader classLoader, boolean acceptProxyClasses) throws IOException {
/*  65 */     super(in);
/*  66 */     this.classLoader = classLoader;
/*  67 */     this.acceptProxyClasses = acceptProxyClasses;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class<?> resolveClass(ObjectStreamClass classDesc) throws IOException, ClassNotFoundException {
/*     */     try {
/*  74 */       if (this.classLoader != null)
/*     */       {
/*  76 */         return ClassUtils.forName(classDesc.getName(), this.classLoader);
/*     */       }
/*     */ 
/*     */       
/*  80 */       return super.resolveClass(classDesc);
/*     */     
/*     */     }
/*  83 */     catch (ClassNotFoundException ex) {
/*  84 */       return resolveFallbackIfPossible(classDesc.getName(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected Class<?> resolveProxyClass(String[] interfaces) throws IOException, ClassNotFoundException {
/*  90 */     if (!this.acceptProxyClasses) {
/*  91 */       throw new NotSerializableException("Not allowed to accept serialized proxy classes");
/*     */     }
/*  93 */     if (this.classLoader != null) {
/*     */       
/*  95 */       Class<?>[] resolvedInterfaces = new Class[interfaces.length];
/*  96 */       for (int i = 0; i < interfaces.length; i++) {
/*     */         try {
/*  98 */           resolvedInterfaces[i] = ClassUtils.forName(interfaces[i], this.classLoader);
/*     */         }
/* 100 */         catch (ClassNotFoundException ex) {
/* 101 */           resolvedInterfaces[i] = resolveFallbackIfPossible(interfaces[i], ex);
/*     */         } 
/*     */       } 
/*     */       try {
/* 105 */         return ClassUtils.createCompositeInterface(resolvedInterfaces, this.classLoader);
/*     */       }
/* 107 */       catch (IllegalArgumentException ex) {
/* 108 */         throw new ClassNotFoundException(null, ex);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 114 */       return super.resolveProxyClass(interfaces);
/*     */     }
/* 116 */     catch (ClassNotFoundException ex) {
/* 117 */       Class<?>[] resolvedInterfaces = new Class[interfaces.length];
/* 118 */       for (int i = 0; i < interfaces.length; i++) {
/* 119 */         resolvedInterfaces[i] = resolveFallbackIfPossible(interfaces[i], ex);
/*     */       }
/* 121 */       return ClassUtils.createCompositeInterface(resolvedInterfaces, getFallbackClassLoader());
/*     */     } 
/*     */   }
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
/*     */   protected Class<?> resolveFallbackIfPossible(String className, ClassNotFoundException ex) throws IOException, ClassNotFoundException {
/* 138 */     throw ex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected ClassLoader getFallbackClassLoader() throws IOException {
/* 149 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\ConfigurableObjectInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */