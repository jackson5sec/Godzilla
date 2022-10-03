/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public final class Verify
/*     */ {
/*     */   public static void verify(boolean expression) {
/*  99 */     if (!expression) {
/* 100 */       throw new VerifyException();
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
/*     */   public static void verify(boolean expression, String errorMessageTemplate, Object... errorMessageArgs) {
/* 123 */     if (!expression) {
/* 124 */       throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, errorMessageArgs));
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
/*     */   public static void verify(boolean expression, String errorMessageTemplate, char p1) {
/* 137 */     if (!expression) {
/* 138 */       throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Character.valueOf(p1) }));
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
/*     */   public static void verify(boolean expression, String errorMessageTemplate, int p1) {
/* 151 */     if (!expression) {
/* 152 */       throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Integer.valueOf(p1) }));
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
/*     */   public static void verify(boolean expression, String errorMessageTemplate, long p1) {
/* 165 */     if (!expression) {
/* 166 */       throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Long.valueOf(p1) }));
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
/*     */   public static void verify(boolean expression, String errorMessageTemplate, Object p1) {
/* 180 */     if (!expression) {
/* 181 */       throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1 }));
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
/*     */   public static void verify(boolean expression, String errorMessageTemplate, char p1, char p2) {
/* 195 */     if (!expression) {
/* 196 */       throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Character.valueOf(p1), Character.valueOf(p2) }));
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
/*     */   public static void verify(boolean expression, String errorMessageTemplate, int p1, char p2) {
/* 210 */     if (!expression) {
/* 211 */       throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Integer.valueOf(p1), Character.valueOf(p2) }));
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
/*     */   public static void verify(boolean expression, String errorMessageTemplate, long p1, char p2) {
/* 225 */     if (!expression) {
/* 226 */       throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Long.valueOf(p1), Character.valueOf(p2) }));
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
/*     */   public static void verify(boolean expression, String errorMessageTemplate, Object p1, char p2) {
/* 240 */     if (!expression) {
/* 241 */       throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, Character.valueOf(p2) }));
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
/*     */   public static void verify(boolean expression, String errorMessageTemplate, char p1, int p2) {
/* 255 */     if (!expression) {
/* 256 */       throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Character.valueOf(p1), Integer.valueOf(p2) }));
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
/*     */   public static void verify(boolean expression, String errorMessageTemplate, int p1, int p2) {
/* 270 */     if (!expression) {
/* 271 */       throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Integer.valueOf(p1), Integer.valueOf(p2) }));
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
/*     */   public static void verify(boolean expression, String errorMessageTemplate, long p1, int p2) {
/* 285 */     if (!expression) {
/* 286 */       throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Long.valueOf(p1), Integer.valueOf(p2) }));
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
/*     */   public static void verify(boolean expression, String errorMessageTemplate, Object p1, int p2) {
/* 300 */     if (!expression) {
/* 301 */       throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, Integer.valueOf(p2) }));
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
/*     */   public static void verify(boolean expression, String errorMessageTemplate, char p1, long p2) {
/* 315 */     if (!expression) {
/* 316 */       throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Character.valueOf(p1), Long.valueOf(p2) }));
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
/*     */   public static void verify(boolean expression, String errorMessageTemplate, int p1, long p2) {
/* 330 */     if (!expression) {
/* 331 */       throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Integer.valueOf(p1), Long.valueOf(p2) }));
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
/*     */   public static void verify(boolean expression, String errorMessageTemplate, long p1, long p2) {
/* 345 */     if (!expression) {
/* 346 */       throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Long.valueOf(p1), Long.valueOf(p2) }));
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
/*     */   public static void verify(boolean expression, String errorMessageTemplate, Object p1, long p2) {
/* 360 */     if (!expression) {
/* 361 */       throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, Long.valueOf(p2) }));
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
/*     */   public static void verify(boolean expression, String errorMessageTemplate, char p1, Object p2) {
/* 375 */     if (!expression) {
/* 376 */       throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Character.valueOf(p1), p2 }));
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
/*     */   public static void verify(boolean expression, String errorMessageTemplate, int p1, Object p2) {
/* 390 */     if (!expression) {
/* 391 */       throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Integer.valueOf(p1), p2 }));
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
/*     */   public static void verify(boolean expression, String errorMessageTemplate, long p1, Object p2) {
/* 405 */     if (!expression) {
/* 406 */       throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Long.valueOf(p1), p2 }));
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
/*     */   public static void verify(boolean expression, String errorMessageTemplate, Object p1, Object p2) {
/* 423 */     if (!expression) {
/* 424 */       throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, p2 }));
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
/*     */   public static void verify(boolean expression, String errorMessageTemplate, Object p1, Object p2, Object p3) {
/* 442 */     if (!expression) {
/* 443 */       throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, p2, p3 }));
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
/*     */   public static void verify(boolean expression, String errorMessageTemplate, Object p1, Object p2, Object p3, Object p4) {
/* 462 */     if (!expression) {
/* 463 */       throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, p2, p3, p4 }));
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
/*     */   @CanIgnoreReturnValue
/*     */   public static <T> T verifyNotNull(T reference) {
/* 477 */     return verifyNotNull(reference, "expected a non-null reference", new Object[0]);
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
/*     */   @CanIgnoreReturnValue
/*     */   public static <T> T verifyNotNull(T reference, String errorMessageTemplate, Object... errorMessageArgs) {
/* 500 */     verify((reference != null), errorMessageTemplate, errorMessageArgs);
/* 501 */     return reference;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\base\Verify.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */