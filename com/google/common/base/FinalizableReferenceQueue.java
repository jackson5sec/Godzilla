/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.io.Closeable;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.lang.ref.PhantomReference;
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ @GwtIncompatible
/*     */ public class FinalizableReferenceQueue
/*     */   implements Closeable
/*     */ {
/* 133 */   private static final Logger logger = Logger.getLogger(FinalizableReferenceQueue.class.getName());
/*     */ 
/*     */   
/*     */   private static final String FINALIZER_CLASS_NAME = "com.google.common.base.internal.Finalizer";
/*     */   
/*     */   private static final Method startFinalizer;
/*     */ 
/*     */   
/*     */   static {
/* 142 */     Class<?> finalizer = loadFinalizer(new FinalizerLoader[] { new SystemLoader(), new DecoupledLoader(), new DirectLoader() });
/* 143 */     startFinalizer = getStartFinalizer(finalizer);
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
/* 157 */   final ReferenceQueue<Object> queue = new ReferenceQueue();
/* 158 */   final PhantomReference<Object> frqRef = new PhantomReference(this, this.queue); public FinalizableReferenceQueue() {
/* 159 */     boolean threadStarted = false;
/*     */     try {
/* 161 */       startFinalizer.invoke(null, new Object[] { FinalizableReference.class, this.queue, this.frqRef });
/* 162 */       threadStarted = true;
/* 163 */     } catch (IllegalAccessException impossible) {
/* 164 */       throw new AssertionError(impossible);
/* 165 */     } catch (Throwable t) {
/* 166 */       logger.log(Level.INFO, "Failed to start reference finalizer thread. Reference cleanup will only occur when new references are created.", t);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 173 */     this.threadStarted = threadStarted;
/*     */   }
/*     */   final boolean threadStarted;
/*     */   
/*     */   public void close() {
/* 178 */     this.frqRef.enqueue();
/* 179 */     cleanUp();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void cleanUp() {
/* 188 */     if (this.threadStarted) {
/*     */       return;
/*     */     }
/*     */     
/*     */     Reference<?> reference;
/* 193 */     while ((reference = this.queue.poll()) != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 198 */       reference.clear();
/*     */       try {
/* 200 */         ((FinalizableReference)reference).finalizeReferent();
/* 201 */       } catch (Throwable t) {
/* 202 */         logger.log(Level.SEVERE, "Error cleaning up after reference.", t);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Class<?> loadFinalizer(FinalizerLoader... loaders) {
/* 213 */     for (FinalizerLoader loader : loaders) {
/* 214 */       Class<?> finalizer = loader.loadFinalizer();
/* 215 */       if (finalizer != null) {
/* 216 */         return finalizer;
/*     */       }
/*     */     } 
/*     */     
/* 220 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static interface FinalizerLoader
/*     */   {
/*     */     Class<?> loadFinalizer();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class SystemLoader
/*     */     implements FinalizerLoader
/*     */   {
/*     */     @VisibleForTesting
/*     */     static boolean disabled;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Class<?> loadFinalizer() {
/*     */       ClassLoader systemLoader;
/* 246 */       if (disabled) {
/* 247 */         return null;
/*     */       }
/*     */       
/*     */       try {
/* 251 */         systemLoader = ClassLoader.getSystemClassLoader();
/* 252 */       } catch (SecurityException e) {
/* 253 */         FinalizableReferenceQueue.logger.info("Not allowed to access system class loader.");
/* 254 */         return null;
/*     */       } 
/* 256 */       if (systemLoader != null) {
/*     */         try {
/* 258 */           return systemLoader.loadClass("com.google.common.base.internal.Finalizer");
/* 259 */         } catch (ClassNotFoundException e) {
/*     */           
/* 261 */           return null;
/*     */         } 
/*     */       }
/* 264 */       return null;
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
/*     */   static class DecoupledLoader
/*     */     implements FinalizerLoader
/*     */   {
/*     */     private static final String LOADING_ERROR = "Could not load Finalizer in its own class loader. Loading Finalizer in the current class loader instead. As a result, you will not be able to garbage collect this class loader. To support reclaiming this class loader, either resolve the underlying issue, or move Guava to your system class path.";
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
/*     */     public Class<?> loadFinalizer() {
/*     */       try {
/* 293 */         ClassLoader finalizerLoader = newLoader(getBaseUrl());
/* 294 */         return finalizerLoader.loadClass("com.google.common.base.internal.Finalizer");
/* 295 */       } catch (Exception e) {
/* 296 */         FinalizableReferenceQueue.logger.log(Level.WARNING, "Could not load Finalizer in its own class loader. Loading Finalizer in the current class loader instead. As a result, you will not be able to garbage collect this class loader. To support reclaiming this class loader, either resolve the underlying issue, or move Guava to your system class path.", e);
/* 297 */         return null;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     URL getBaseUrl() throws IOException {
/* 304 */       String finalizerPath = "com.google.common.base.internal.Finalizer".replace('.', '/') + ".class";
/* 305 */       URL finalizerUrl = getClass().getClassLoader().getResource(finalizerPath);
/* 306 */       if (finalizerUrl == null) {
/* 307 */         throw new FileNotFoundException(finalizerPath);
/*     */       }
/*     */ 
/*     */       
/* 311 */       String urlString = finalizerUrl.toString();
/* 312 */       if (!urlString.endsWith(finalizerPath)) {
/* 313 */         throw new IOException("Unsupported path style: " + urlString);
/*     */       }
/* 315 */       urlString = urlString.substring(0, urlString.length() - finalizerPath.length());
/* 316 */       return new URL(finalizerUrl, urlString);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     URLClassLoader newLoader(URL base) {
/* 324 */       return new URLClassLoader(new URL[] { base }, null);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class DirectLoader
/*     */     implements FinalizerLoader
/*     */   {
/*     */     public Class<?> loadFinalizer() {
/*     */       try {
/* 336 */         return Class.forName("com.google.common.base.internal.Finalizer");
/* 337 */       } catch (ClassNotFoundException e) {
/* 338 */         throw new AssertionError(e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static Method getStartFinalizer(Class<?> finalizer) {
/*     */     try {
/* 346 */       return finalizer.getMethod("startFinalizer", new Class[] { Class.class, ReferenceQueue.class, PhantomReference.class });
/*     */     }
/* 348 */     catch (NoSuchMethodException e) {
/* 349 */       throw new AssertionError(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\base\FinalizableReferenceQueue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */