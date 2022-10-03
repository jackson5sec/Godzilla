/*     */ package org.mozilla.javascript;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
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
/*     */ public final class LazilyLoadedCtor
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private static final int STATE_BEFORE_INIT = 0;
/*     */   private static final int STATE_INITIALIZING = 1;
/*     */   private static final int STATE_WITH_VALUE = 2;
/*     */   private final ScriptableObject scope;
/*     */   private final String propertyName;
/*     */   private final String className;
/*     */   private final boolean sealed;
/*     */   private final boolean privileged;
/*     */   private Object initializedValue;
/*     */   private int state;
/*     */   
/*     */   public LazilyLoadedCtor(ScriptableObject scope, String propertyName, String className, boolean sealed) {
/*  36 */     this(scope, propertyName, className, sealed, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   LazilyLoadedCtor(ScriptableObject scope, String propertyName, String className, boolean sealed, boolean privileged) {
/*  43 */     this.scope = scope;
/*  44 */     this.propertyName = propertyName;
/*  45 */     this.className = className;
/*  46 */     this.sealed = sealed;
/*  47 */     this.privileged = privileged;
/*  48 */     this.state = 0;
/*     */     
/*  50 */     scope.addLazilyInitializedValue(propertyName, 0, this, 2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void init() {
/*  56 */     synchronized (this) {
/*  57 */       if (this.state == 1) {
/*  58 */         throw new IllegalStateException("Recursive initialization for " + this.propertyName);
/*     */       }
/*  60 */       if (this.state == 0) {
/*  61 */         this.state = 1;
/*     */ 
/*     */         
/*  64 */         Object value = Scriptable.NOT_FOUND;
/*     */         try {
/*  66 */           value = buildValue();
/*     */         } finally {
/*  68 */           this.initializedValue = value;
/*  69 */           this.state = 2;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   Object getValue() {
/*  77 */     if (this.state != 2)
/*  78 */       throw new IllegalStateException(this.propertyName); 
/*  79 */     return this.initializedValue;
/*     */   }
/*     */ 
/*     */   
/*     */   private Object buildValue() {
/*  84 */     if (this.privileged)
/*     */     {
/*  86 */       return AccessController.doPrivileged(new PrivilegedAction()
/*     */           {
/*     */             public Object run()
/*     */             {
/*  90 */               return LazilyLoadedCtor.this.buildValue0();
/*     */             }
/*     */           });
/*     */     }
/*     */ 
/*     */     
/*  96 */     return buildValue0();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Object buildValue0() {
/* 102 */     Class<? extends Scriptable> cl = cast(Kit.classOrNull(this.className));
/* 103 */     if (cl != null) {
/*     */       
/* 105 */       try { Object value = ScriptableObject.buildClassCtor(this.scope, cl, this.sealed, false);
/*     */         
/* 107 */         if (value != null) {
/* 108 */           return value;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 113 */         value = this.scope.get(this.propertyName, this.scope);
/* 114 */         if (value != Scriptable.NOT_FOUND) {
/* 115 */           return value;
/*     */         } }
/* 117 */       catch (InvocationTargetException ex)
/* 118 */       { Throwable target = ex.getTargetException();
/* 119 */         if (target instanceof RuntimeException) {
/* 120 */           throw (RuntimeException)target;
/*     */         } }
/* 122 */       catch (RhinoException ex) {  }
/* 123 */       catch (InstantiationException ex) {  }
/* 124 */       catch (IllegalAccessException ex) {  }
/* 125 */       catch (SecurityException ex) {}
/*     */     }
/*     */     
/* 128 */     return Scriptable.NOT_FOUND;
/*     */   }
/*     */ 
/*     */   
/*     */   private Class<? extends Scriptable> cast(Class<?> cl) {
/* 133 */     return (Class)cl;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\LazilyLoadedCtor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */