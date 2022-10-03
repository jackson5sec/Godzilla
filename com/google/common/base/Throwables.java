/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.AbstractList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class Throwables
/*     */ {
/*     */   @GwtIncompatible
/*     */   private static final String JAVA_LANG_ACCESS_CLASSNAME = "sun.misc.JavaLangAccess";
/*     */   @GwtIncompatible
/*     */   @VisibleForTesting
/*     */   static final String SHARED_SECRETS_CLASSNAME = "sun.misc.SharedSecrets";
/*     */   
/*     */   @GwtIncompatible
/*     */   public static <X extends Throwable> void throwIfInstanceOf(Throwable throwable, Class<X> declaredType) throws X {
/*  74 */     Preconditions.checkNotNull(throwable);
/*  75 */     if (declaredType.isInstance(throwable)) {
/*  76 */       throw (X)declaredType.cast(throwable);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @GwtIncompatible
/*     */   public static <X extends Throwable> void propagateIfInstanceOf(Throwable throwable, Class<X> declaredType) throws X {
/* 103 */     if (throwable != null) {
/* 104 */       throwIfInstanceOf(throwable, declaredType);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void throwIfUnchecked(Throwable throwable) {
/* 128 */     Preconditions.checkNotNull(throwable);
/* 129 */     if (throwable instanceof RuntimeException) {
/* 130 */       throw (RuntimeException)throwable;
/*     */     }
/* 132 */     if (throwable instanceof Error) {
/* 133 */       throw (Error)throwable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @GwtIncompatible
/*     */   public static void propagateIfPossible(Throwable throwable) {
/* 158 */     if (throwable != null) {
/* 159 */       throwIfUnchecked(throwable);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static <X extends Throwable> void propagateIfPossible(Throwable throwable, Class<X> declaredType) throws X {
/* 184 */     propagateIfInstanceOf(throwable, declaredType);
/* 185 */     propagateIfPossible(throwable);
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
/*     */   
/*     */   @GwtIncompatible
/*     */   public static <X1 extends Throwable, X2 extends Throwable> void propagateIfPossible(Throwable throwable, Class<X1> declaredType1, Class<X2> declaredType2) throws X1, X2 {
/* 203 */     Preconditions.checkNotNull(declaredType2);
/* 204 */     propagateIfInstanceOf(throwable, declaredType1);
/* 205 */     propagateIfPossible(throwable, declaredType2);
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
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   @GwtIncompatible
/*     */   public static RuntimeException propagate(Throwable throwable) {
/* 240 */     throwIfUnchecked(throwable);
/* 241 */     throw new RuntimeException(throwable);
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
/*     */   public static Throwable getRootCause(Throwable throwable) {
/* 257 */     Throwable slowPointer = throwable;
/* 258 */     boolean advanceSlowPointer = false;
/*     */     
/*     */     Throwable cause;
/* 261 */     while ((cause = throwable.getCause()) != null) {
/* 262 */       throwable = cause;
/*     */       
/* 264 */       if (throwable == slowPointer) {
/* 265 */         throw new IllegalArgumentException("Loop in causal chain detected.", throwable);
/*     */       }
/* 267 */       if (advanceSlowPointer) {
/* 268 */         slowPointer = slowPointer.getCause();
/*     */       }
/* 270 */       advanceSlowPointer = !advanceSlowPointer;
/*     */     } 
/* 272 */     return throwable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static List<Throwable> getCausalChain(Throwable throwable) {
/* 293 */     Preconditions.checkNotNull(throwable);
/* 294 */     List<Throwable> causes = new ArrayList<>(4);
/* 295 */     causes.add(throwable);
/*     */ 
/*     */ 
/*     */     
/* 299 */     Throwable slowPointer = throwable;
/* 300 */     boolean advanceSlowPointer = false;
/*     */     
/*     */     Throwable cause;
/* 303 */     while ((cause = throwable.getCause()) != null) {
/* 304 */       throwable = cause;
/* 305 */       causes.add(throwable);
/*     */       
/* 307 */       if (throwable == slowPointer) {
/* 308 */         throw new IllegalArgumentException("Loop in causal chain detected.", throwable);
/*     */       }
/* 310 */       if (advanceSlowPointer) {
/* 311 */         slowPointer = slowPointer.getCause();
/*     */       }
/* 313 */       advanceSlowPointer = !advanceSlowPointer;
/*     */     } 
/* 315 */     return Collections.unmodifiableList(causes);
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
/*     */ 
/*     */   
/*     */   @Beta
/*     */   @GwtIncompatible
/*     */   public static <X extends Throwable> X getCauseAs(Throwable throwable, Class<X> expectedCauseType) {
/*     */     try {
/* 336 */       return expectedCauseType.cast(throwable.getCause());
/* 337 */     } catch (ClassCastException e) {
/* 338 */       e.initCause(throwable);
/* 339 */       throw e;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static String getStackTraceAsString(Throwable throwable) {
/* 351 */     StringWriter stringWriter = new StringWriter();
/* 352 */     throwable.printStackTrace(new PrintWriter(stringWriter));
/* 353 */     return stringWriter.toString();
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
/*     */   @Beta
/*     */   @GwtIncompatible
/*     */   public static List<StackTraceElement> lazyStackTrace(Throwable throwable) {
/* 388 */     return lazyStackTraceIsLazy() ? 
/* 389 */       jlaStackTrace(throwable) : 
/* 390 */       Collections.<StackTraceElement>unmodifiableList(Arrays.asList(throwable.getStackTrace()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   @GwtIncompatible
/*     */   public static boolean lazyStackTraceIsLazy() {
/* 402 */     return (getStackTraceElementMethod != null && getStackTraceDepthMethod != null);
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private static List<StackTraceElement> jlaStackTrace(final Throwable t) {
/* 407 */     Preconditions.checkNotNull(t);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 414 */     return new AbstractList<StackTraceElement>()
/*     */       {
/*     */         public StackTraceElement get(int n) {
/* 417 */           return 
/* 418 */             (StackTraceElement)Throwables.invokeAccessibleNonThrowingMethod(Throwables.getStackTraceElementMethod, Throwables.jla, new Object[] { this.val$t, Integer.valueOf(n) });
/*     */         }
/*     */ 
/*     */         
/*     */         public int size() {
/* 423 */           return ((Integer)Throwables.invokeAccessibleNonThrowingMethod(Throwables.getStackTraceDepthMethod, Throwables.jla, new Object[] { this.val$t })).intValue();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private static Object invokeAccessibleNonThrowingMethod(Method method, Object receiver, Object... params) {
/*     */     try {
/* 432 */       return method.invoke(receiver, params);
/* 433 */     } catch (IllegalAccessException e) {
/* 434 */       throw new RuntimeException(e);
/* 435 */     } catch (InvocationTargetException e) {
/* 436 */       throw propagate(e.getCause());
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
/*     */   @GwtIncompatible
/* 451 */   private static final Object jla = getJLA();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/* 458 */   private static final Method getStackTraceElementMethod = (jla == null) ? null : 
/* 459 */     getGetMethod();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/* 466 */   private static final Method getStackTraceDepthMethod = (jla == null) ? null : 
/* 467 */     getSizeMethod();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private static Object getJLA() {
/*     */     try {
/* 480 */       Class<?> sharedSecrets = Class.forName("sun.misc.SharedSecrets", false, null);
/* 481 */       Method langAccess = sharedSecrets.getMethod("getJavaLangAccess", new Class[0]);
/* 482 */       return langAccess.invoke(null, new Object[0]);
/* 483 */     } catch (ThreadDeath death) {
/* 484 */       throw death;
/* 485 */     } catch (Throwable t) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 490 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private static Method getGetMethod() {
/* 500 */     return getJlaMethod("getStackTraceElement", new Class[] { Throwable.class, int.class });
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
/*     */   @GwtIncompatible
/*     */   private static Method getSizeMethod() {
/*     */     try {
/* 515 */       Method getStackTraceDepth = getJlaMethod("getStackTraceDepth", new Class[] { Throwable.class });
/* 516 */       if (getStackTraceDepth == null) {
/* 517 */         return null;
/*     */       }
/* 519 */       getStackTraceDepth.invoke(getJLA(), new Object[] { new Throwable() });
/* 520 */       return getStackTraceDepth;
/* 521 */     } catch (UnsupportedOperationException|IllegalAccessException|InvocationTargetException e) {
/* 522 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private static Method getJlaMethod(String name, Class<?>... parameterTypes) throws ThreadDeath {
/*     */     try {
/* 530 */       return Class.forName("sun.misc.JavaLangAccess", false, null).getMethod(name, parameterTypes);
/* 531 */     } catch (ThreadDeath death) {
/* 532 */       throw death;
/* 533 */     } catch (Throwable t) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 538 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\base\Throwables.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */