/*     */ package com.google.common.base.internal;
/*     */ 
/*     */ import java.lang.ref.PhantomReference;
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
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
/*     */ public class Finalizer
/*     */   implements Runnable
/*     */ {
/*  48 */   private static final Logger logger = Logger.getLogger(Finalizer.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String FINALIZABLE_REFERENCE = "com.google.common.base.FinalizableReference";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final WeakReference<Class<?>> finalizableReferenceClassReference;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final PhantomReference<Object> frqReference;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ReferenceQueue<Object> queue;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void startFinalizer(Class<?> finalizableReferenceClass, ReferenceQueue<Object> queue, PhantomReference<Object> frqReference) {
/*  74 */     if (!finalizableReferenceClass.getName().equals("com.google.common.base.FinalizableReference")) {
/*  75 */       throw new IllegalArgumentException("Expected com.google.common.base.FinalizableReference.");
/*     */     }
/*     */     
/*  78 */     Finalizer finalizer = new Finalizer(finalizableReferenceClass, queue, frqReference);
/*  79 */     String threadName = Finalizer.class.getName();
/*  80 */     Thread thread = null;
/*  81 */     if (bigThreadConstructor != null) {
/*     */       try {
/*  83 */         boolean inheritThreadLocals = false;
/*  84 */         long defaultStackSize = 0L;
/*     */         
/*  86 */         thread = bigThreadConstructor.newInstance(new Object[] {
/*  87 */               null, finalizer, threadName, Long.valueOf(defaultStackSize), Boolean.valueOf(inheritThreadLocals) });
/*  88 */       } catch (Throwable t) {
/*  89 */         logger.log(Level.INFO, "Failed to create a thread without inherited thread-local values", t);
/*     */       } 
/*     */     }
/*     */     
/*  93 */     if (thread == null) {
/*  94 */       thread = new Thread((ThreadGroup)null, finalizer, threadName);
/*     */     }
/*  96 */     thread.setDaemon(true);
/*     */     
/*     */     try {
/*  99 */       if (inheritableThreadLocals != null) {
/* 100 */         inheritableThreadLocals.set(thread, (Object)null);
/*     */       }
/* 102 */     } catch (Throwable t) {
/* 103 */       logger.log(Level.INFO, "Failed to clear thread local values inherited by reference finalizer thread.", t);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 109 */     thread.start();
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
/* 120 */   private static final Constructor<Thread> bigThreadConstructor = getBigThreadConstructor();
/*     */   
/* 122 */   private static final Field inheritableThreadLocals = (bigThreadConstructor == null) ? 
/* 123 */     getInheritableThreadLocalsField() : null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Finalizer(Class<?> finalizableReferenceClass, ReferenceQueue<Object> queue, PhantomReference<Object> frqReference) {
/* 130 */     this.queue = queue;
/*     */     
/* 132 */     this.finalizableReferenceClassReference = new WeakReference<>(finalizableReferenceClass);
/*     */ 
/*     */ 
/*     */     
/* 136 */     this.frqReference = frqReference;
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*     */     while (true) {
/*     */       try {
/*     */         do {
/*     */         
/* 145 */         } while (cleanUp(this.queue.remove()));
/*     */         
/*     */         break;
/* 148 */       } catch (InterruptedException interruptedException) {}
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
/*     */   private boolean cleanUp(Reference<?> reference) {
/* 161 */     Method finalizeReferentMethod = getFinalizeReferentMethod();
/* 162 */     if (finalizeReferentMethod == null) {
/* 163 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/* 170 */       reference.clear();
/*     */       
/* 172 */       if (reference == this.frqReference)
/*     */       {
/*     */ 
/*     */         
/* 176 */         return false;
/*     */       }
/*     */       
/*     */       try {
/* 180 */         finalizeReferentMethod.invoke(reference, new Object[0]);
/* 181 */       } catch (Throwable t) {
/* 182 */         logger.log(Level.SEVERE, "Error cleaning up after reference.", t);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 189 */       if ((reference = this.queue.poll()) == null)
/* 190 */         return true; 
/*     */     } 
/*     */   }
/*     */   
/*     */   private Method getFinalizeReferentMethod() {
/* 195 */     Class<?> finalizableReferenceClass = this.finalizableReferenceClassReference.get();
/* 196 */     if (finalizableReferenceClass == null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 203 */       return null;
/*     */     }
/*     */     try {
/* 206 */       return finalizableReferenceClass.getMethod("finalizeReferent", new Class[0]);
/* 207 */     } catch (NoSuchMethodException e) {
/* 208 */       throw new AssertionError(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Field getInheritableThreadLocalsField() {
/*     */     try {
/* 214 */       Field inheritableThreadLocals = Thread.class.getDeclaredField("inheritableThreadLocals");
/* 215 */       inheritableThreadLocals.setAccessible(true);
/* 216 */       return inheritableThreadLocals;
/* 217 */     } catch (Throwable t) {
/* 218 */       logger.log(Level.INFO, "Couldn't access Thread.inheritableThreadLocals. Reference finalizer threads will inherit thread local values.");
/*     */ 
/*     */ 
/*     */       
/* 222 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Constructor<Thread> getBigThreadConstructor() {
/*     */     try {
/* 228 */       return Thread.class.getConstructor(new Class[] { ThreadGroup.class, Runnable.class, String.class, long.class, boolean.class });
/*     */     }
/* 230 */     catch (Throwable t) {
/*     */       
/* 232 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\base\internal\Finalizer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */