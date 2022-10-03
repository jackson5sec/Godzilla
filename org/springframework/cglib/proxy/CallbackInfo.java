/*     */ package org.springframework.cglib.proxy;
/*     */ 
/*     */ import org.springframework.asm.Type;
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
/*     */ class CallbackInfo
/*     */ {
/*     */   private Class cls;
/*     */   private CallbackGenerator generator;
/*     */   private Type type;
/*     */   
/*     */   public static Type[] determineTypes(Class[] callbackTypes) {
/*  23 */     return determineTypes(callbackTypes, true);
/*     */   }
/*     */   
/*     */   public static Type[] determineTypes(Class[] callbackTypes, boolean checkAll) {
/*  27 */     Type[] types = new Type[callbackTypes.length];
/*  28 */     for (int i = 0; i < types.length; i++) {
/*  29 */       types[i] = determineType(callbackTypes[i], checkAll);
/*     */     }
/*  31 */     return types;
/*     */   }
/*     */   
/*     */   public static Type[] determineTypes(Callback[] callbacks) {
/*  35 */     return determineTypes(callbacks, true);
/*     */   }
/*     */   
/*     */   public static Type[] determineTypes(Callback[] callbacks, boolean checkAll) {
/*  39 */     Type[] types = new Type[callbacks.length];
/*  40 */     for (int i = 0; i < types.length; i++) {
/*  41 */       types[i] = determineType(callbacks[i], checkAll);
/*     */     }
/*  43 */     return types;
/*     */   }
/*     */   
/*     */   public static CallbackGenerator[] getGenerators(Type[] callbackTypes) {
/*  47 */     CallbackGenerator[] generators = new CallbackGenerator[callbackTypes.length];
/*  48 */     for (int i = 0; i < generators.length; i++) {
/*  49 */       generators[i] = getGenerator(callbackTypes[i]);
/*     */     }
/*  51 */     return generators;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  60 */   private static final CallbackInfo[] CALLBACKS = new CallbackInfo[] { new CallbackInfo(NoOp.class, NoOpGenerator.INSTANCE), new CallbackInfo(MethodInterceptor.class, MethodInterceptorGenerator.INSTANCE), new CallbackInfo(InvocationHandler.class, InvocationHandlerGenerator.INSTANCE), new CallbackInfo(LazyLoader.class, LazyLoaderGenerator.INSTANCE), new CallbackInfo(Dispatcher.class, DispatcherGenerator.INSTANCE), new CallbackInfo(FixedValue.class, FixedValueGenerator.INSTANCE), new CallbackInfo(ProxyRefDispatcher.class, DispatcherGenerator.PROXY_REF_INSTANCE) };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CallbackInfo(Class cls, CallbackGenerator generator) {
/*  71 */     this.cls = cls;
/*  72 */     this.generator = generator;
/*  73 */     this.type = Type.getType(cls);
/*     */   }
/*     */   
/*     */   private static Type determineType(Callback callback, boolean checkAll) {
/*  77 */     if (callback == null) {
/*  78 */       throw new IllegalStateException("Callback is null");
/*     */     }
/*  80 */     return determineType(callback.getClass(), checkAll);
/*     */   }
/*     */   
/*     */   private static Type determineType(Class<?> callbackType, boolean checkAll) {
/*  84 */     Class cur = null;
/*  85 */     Type type = null;
/*  86 */     for (int i = 0; i < CALLBACKS.length; i++) {
/*  87 */       CallbackInfo info = CALLBACKS[i];
/*  88 */       if (info.cls.isAssignableFrom(callbackType)) {
/*  89 */         if (cur != null) {
/*  90 */           throw new IllegalStateException("Callback implements both " + cur + " and " + info.cls);
/*     */         }
/*  92 */         cur = info.cls;
/*  93 */         type = info.type;
/*  94 */         if (!checkAll) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } 
/*  99 */     if (cur == null) {
/* 100 */       throw new IllegalStateException("Unknown callback type " + callbackType);
/*     */     }
/* 102 */     return type;
/*     */   }
/*     */   
/*     */   private static CallbackGenerator getGenerator(Type callbackType) {
/* 106 */     for (int i = 0; i < CALLBACKS.length; i++) {
/* 107 */       CallbackInfo info = CALLBACKS[i];
/* 108 */       if (info.type.equals(callbackType)) {
/* 109 */         return info.generator;
/*     */       }
/*     */     } 
/* 112 */     throw new IllegalStateException("Unknown callback type " + callbackType);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\proxy\CallbackInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */