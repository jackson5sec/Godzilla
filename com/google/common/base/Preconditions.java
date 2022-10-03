/*      */ package com.google.common.base;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @GwtCompatible
/*      */ public final class Preconditions
/*      */ {
/*      */   public static void checkArgument(boolean expression) {
/*  127 */     if (!expression) {
/*  128 */       throw new IllegalArgumentException();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean expression, Object errorMessage) {
/*  141 */     if (!expression) {
/*  142 */       throw new IllegalArgumentException(String.valueOf(errorMessage));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean expression, String errorMessageTemplate, Object... errorMessageArgs) {
/*  163 */     if (!expression) {
/*  164 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, errorMessageArgs));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, char p1) {
/*  176 */     if (!b) {
/*  177 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Character.valueOf(p1) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, int p1) {
/*  189 */     if (!b) {
/*  190 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Integer.valueOf(p1) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, long p1) {
/*  202 */     if (!b) {
/*  203 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Long.valueOf(p1) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, Object p1) {
/*  216 */     if (!b) {
/*  217 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1 }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, char p1, char p2) {
/*  230 */     if (!b) {
/*  231 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Character.valueOf(p1), Character.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, char p1, int p2) {
/*  244 */     if (!b) {
/*  245 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Character.valueOf(p1), Integer.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, char p1, long p2) {
/*  258 */     if (!b) {
/*  259 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Character.valueOf(p1), Long.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, char p1, Object p2) {
/*  272 */     if (!b) {
/*  273 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Character.valueOf(p1), p2 }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, int p1, char p2) {
/*  286 */     if (!b) {
/*  287 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Integer.valueOf(p1), Character.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, int p1, int p2) {
/*  300 */     if (!b) {
/*  301 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Integer.valueOf(p1), Integer.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, int p1, long p2) {
/*  314 */     if (!b) {
/*  315 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Integer.valueOf(p1), Long.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, int p1, Object p2) {
/*  328 */     if (!b) {
/*  329 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Integer.valueOf(p1), p2 }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, long p1, char p2) {
/*  342 */     if (!b) {
/*  343 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Long.valueOf(p1), Character.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, long p1, int p2) {
/*  356 */     if (!b) {
/*  357 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Long.valueOf(p1), Integer.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, long p1, long p2) {
/*  370 */     if (!b) {
/*  371 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Long.valueOf(p1), Long.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, long p1, Object p2) {
/*  384 */     if (!b) {
/*  385 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Long.valueOf(p1), p2 }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, Object p1, char p2) {
/*  398 */     if (!b) {
/*  399 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, Character.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, Object p1, int p2) {
/*  412 */     if (!b) {
/*  413 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, Integer.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, Object p1, long p2) {
/*  426 */     if (!b) {
/*  427 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, Long.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, Object p1, Object p2) {
/*  440 */     if (!b) {
/*  441 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, p2 }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, Object p1, Object p2, Object p3) {
/*  458 */     if (!b) {
/*  459 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, p2, p3 }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, Object p1, Object p2, Object p3, Object p4) {
/*  477 */     if (!b) {
/*  478 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, p2, p3, p4 }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkState(boolean expression) {
/*  491 */     if (!expression) {
/*  492 */       throw new IllegalStateException();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkState(boolean expression, Object errorMessage) {
/*  507 */     if (!expression) {
/*  508 */       throw new IllegalStateException(String.valueOf(errorMessage));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkState(boolean expression, String errorMessageTemplate, Object... errorMessageArgs) {
/*  531 */     if (!expression) {
/*  532 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, errorMessageArgs));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkState(boolean b, String errorMessageTemplate, char p1) {
/*  545 */     if (!b) {
/*  546 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Character.valueOf(p1) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkState(boolean b, String errorMessageTemplate, int p1) {
/*  559 */     if (!b) {
/*  560 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Integer.valueOf(p1) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkState(boolean b, String errorMessageTemplate, long p1) {
/*  573 */     if (!b) {
/*  574 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Long.valueOf(p1) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkState(boolean b, String errorMessageTemplate, Object p1) {
/*  588 */     if (!b) {
/*  589 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1 }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkState(boolean b, String errorMessageTemplate, char p1, char p2) {
/*  603 */     if (!b) {
/*  604 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Character.valueOf(p1), Character.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkState(boolean b, String errorMessageTemplate, char p1, int p2) {
/*  617 */     if (!b) {
/*  618 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Character.valueOf(p1), Integer.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkState(boolean b, String errorMessageTemplate, char p1, long p2) {
/*  632 */     if (!b) {
/*  633 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Character.valueOf(p1), Long.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkState(boolean b, String errorMessageTemplate, char p1, Object p2) {
/*  647 */     if (!b) {
/*  648 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Character.valueOf(p1), p2 }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkState(boolean b, String errorMessageTemplate, int p1, char p2) {
/*  661 */     if (!b) {
/*  662 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Integer.valueOf(p1), Character.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkState(boolean b, String errorMessageTemplate, int p1, int p2) {
/*  675 */     if (!b) {
/*  676 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Integer.valueOf(p1), Integer.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkState(boolean b, String errorMessageTemplate, int p1, long p2) {
/*  689 */     if (!b) {
/*  690 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Integer.valueOf(p1), Long.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkState(boolean b, String errorMessageTemplate, int p1, Object p2) {
/*  704 */     if (!b) {
/*  705 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Integer.valueOf(p1), p2 }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkState(boolean b, String errorMessageTemplate, long p1, char p2) {
/*  719 */     if (!b) {
/*  720 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Long.valueOf(p1), Character.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkState(boolean b, String errorMessageTemplate, long p1, int p2) {
/*  733 */     if (!b) {
/*  734 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Long.valueOf(p1), Integer.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkState(boolean b, String errorMessageTemplate, long p1, long p2) {
/*  748 */     if (!b) {
/*  749 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Long.valueOf(p1), Long.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkState(boolean b, String errorMessageTemplate, long p1, Object p2) {
/*  763 */     if (!b) {
/*  764 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Long.valueOf(p1), p2 }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkState(boolean b, String errorMessageTemplate, Object p1, char p2) {
/*  778 */     if (!b) {
/*  779 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, Character.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkState(boolean b, String errorMessageTemplate, Object p1, int p2) {
/*  793 */     if (!b) {
/*  794 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, Integer.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkState(boolean b, String errorMessageTemplate, Object p1, long p2) {
/*  808 */     if (!b) {
/*  809 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, Long.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkState(boolean b, String errorMessageTemplate, Object p1, Object p2) {
/*  823 */     if (!b) {
/*  824 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, p2 }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkState(boolean b, String errorMessageTemplate, Object p1, Object p2, Object p3) {
/*  842 */     if (!b) {
/*  843 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, p2, p3 }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkState(boolean b, String errorMessageTemplate, Object p1, Object p2, Object p3, Object p4) {
/*  862 */     if (!b) {
/*  863 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, p2, p3, p4 }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T reference) {
/*  877 */     if (reference == null) {
/*  878 */       throw new NullPointerException();
/*      */     }
/*  880 */     return reference;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T reference, Object errorMessage) {
/*  896 */     if (reference == null) {
/*  897 */       throw new NullPointerException(String.valueOf(errorMessage));
/*      */     }
/*  899 */     return reference;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T reference, String errorMessageTemplate, Object... errorMessageArgs) {
/*  920 */     if (reference == null) {
/*  921 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, errorMessageArgs));
/*      */     }
/*  923 */     return reference;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, char p1) {
/*  936 */     if (obj == null) {
/*  937 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Character.valueOf(p1) }));
/*      */     }
/*  939 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, int p1) {
/*  952 */     if (obj == null) {
/*  953 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Integer.valueOf(p1) }));
/*      */     }
/*  955 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, long p1) {
/*  968 */     if (obj == null) {
/*  969 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Long.valueOf(p1) }));
/*      */     }
/*  971 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, Object p1) {
/*  984 */     if (obj == null) {
/*  985 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1 }));
/*      */     }
/*  987 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, char p1, char p2) {
/* 1000 */     if (obj == null) {
/* 1001 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Character.valueOf(p1), Character.valueOf(p2) }));
/*      */     }
/* 1003 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, char p1, int p2) {
/* 1016 */     if (obj == null) {
/* 1017 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Character.valueOf(p1), Integer.valueOf(p2) }));
/*      */     }
/* 1019 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, char p1, long p2) {
/* 1032 */     if (obj == null) {
/* 1033 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Character.valueOf(p1), Long.valueOf(p2) }));
/*      */     }
/* 1035 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, char p1, Object p2) {
/* 1048 */     if (obj == null) {
/* 1049 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Character.valueOf(p1), p2 }));
/*      */     }
/* 1051 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, int p1, char p2) {
/* 1064 */     if (obj == null) {
/* 1065 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Integer.valueOf(p1), Character.valueOf(p2) }));
/*      */     }
/* 1067 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, int p1, int p2) {
/* 1080 */     if (obj == null) {
/* 1081 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Integer.valueOf(p1), Integer.valueOf(p2) }));
/*      */     }
/* 1083 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, int p1, long p2) {
/* 1096 */     if (obj == null) {
/* 1097 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Integer.valueOf(p1), Long.valueOf(p2) }));
/*      */     }
/* 1099 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, int p1, Object p2) {
/* 1112 */     if (obj == null) {
/* 1113 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Integer.valueOf(p1), p2 }));
/*      */     }
/* 1115 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, long p1, char p2) {
/* 1128 */     if (obj == null) {
/* 1129 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Long.valueOf(p1), Character.valueOf(p2) }));
/*      */     }
/* 1131 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, long p1, int p2) {
/* 1144 */     if (obj == null) {
/* 1145 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Long.valueOf(p1), Integer.valueOf(p2) }));
/*      */     }
/* 1147 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, long p1, long p2) {
/* 1160 */     if (obj == null) {
/* 1161 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Long.valueOf(p1), Long.valueOf(p2) }));
/*      */     }
/* 1163 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, long p1, Object p2) {
/* 1176 */     if (obj == null) {
/* 1177 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Long.valueOf(p1), p2 }));
/*      */     }
/* 1179 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, Object p1, char p2) {
/* 1192 */     if (obj == null) {
/* 1193 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, Character.valueOf(p2) }));
/*      */     }
/* 1195 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, Object p1, int p2) {
/* 1208 */     if (obj == null) {
/* 1209 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, Integer.valueOf(p2) }));
/*      */     }
/* 1211 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, Object p1, long p2) {
/* 1224 */     if (obj == null) {
/* 1225 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, Long.valueOf(p2) }));
/*      */     }
/* 1227 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, Object p1, Object p2) {
/* 1240 */     if (obj == null) {
/* 1241 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, p2 }));
/*      */     }
/* 1243 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, Object p1, Object p2, Object p3) {
/* 1260 */     if (obj == null) {
/* 1261 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, p2, p3 }));
/*      */     }
/* 1263 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, Object p1, Object p2, Object p3, Object p4) {
/* 1281 */     if (obj == null) {
/* 1282 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, p2, p3, p4 }));
/*      */     }
/* 1284 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static int checkElementIndex(int index, int size) {
/* 1325 */     return checkElementIndex(index, size, "index");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static int checkElementIndex(int index, int size, String desc) {
/* 1342 */     if (index < 0 || index >= size) {
/* 1343 */       throw new IndexOutOfBoundsException(badElementIndex(index, size, desc));
/*      */     }
/* 1345 */     return index;
/*      */   }
/*      */   
/*      */   private static String badElementIndex(int index, int size, String desc) {
/* 1349 */     if (index < 0)
/* 1350 */       return Strings.lenientFormat("%s (%s) must not be negative", new Object[] { desc, Integer.valueOf(index) }); 
/* 1351 */     if (size < 0) {
/* 1352 */       throw new IllegalArgumentException("negative size: " + size);
/*      */     }
/* 1354 */     return Strings.lenientFormat("%s (%s) must be less than size (%s)", new Object[] { desc, Integer.valueOf(index), Integer.valueOf(size) });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static int checkPositionIndex(int index, int size) {
/* 1370 */     return checkPositionIndex(index, size, "index");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static int checkPositionIndex(int index, int size, String desc) {
/* 1387 */     if (index < 0 || index > size) {
/* 1388 */       throw new IndexOutOfBoundsException(badPositionIndex(index, size, desc));
/*      */     }
/* 1390 */     return index;
/*      */   }
/*      */   
/*      */   private static String badPositionIndex(int index, int size, String desc) {
/* 1394 */     if (index < 0)
/* 1395 */       return Strings.lenientFormat("%s (%s) must not be negative", new Object[] { desc, Integer.valueOf(index) }); 
/* 1396 */     if (size < 0) {
/* 1397 */       throw new IllegalArgumentException("negative size: " + size);
/*      */     }
/* 1399 */     return Strings.lenientFormat("%s (%s) must not be greater than size (%s)", new Object[] { desc, Integer.valueOf(index), Integer.valueOf(size) });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkPositionIndexes(int start, int end, int size) {
/* 1417 */     if (start < 0 || end < start || end > size) {
/* 1418 */       throw new IndexOutOfBoundsException(badPositionIndexes(start, end, size));
/*      */     }
/*      */   }
/*      */   
/*      */   private static String badPositionIndexes(int start, int end, int size) {
/* 1423 */     if (start < 0 || start > size) {
/* 1424 */       return badPositionIndex(start, size, "start index");
/*      */     }
/* 1426 */     if (end < 0 || end > size) {
/* 1427 */       return badPositionIndex(end, size, "end index");
/*      */     }
/*      */     
/* 1430 */     return Strings.lenientFormat("end index (%s) must not be less than start index (%s)", new Object[] { Integer.valueOf(end), Integer.valueOf(start) });
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\base\Preconditions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */