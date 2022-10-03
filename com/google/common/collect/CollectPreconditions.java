/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible
/*    */ final class CollectPreconditions
/*    */ {
/*    */   static void checkEntryNotNull(Object key, Object value) {
/* 29 */     if (key == null)
/* 30 */       throw new NullPointerException("null key in entry: null=" + value); 
/* 31 */     if (value == null) {
/* 32 */       throw new NullPointerException("null value in entry: " + key + "=null");
/*    */     }
/*    */   }
/*    */   
/*    */   @CanIgnoreReturnValue
/*    */   static int checkNonnegative(int value, String name) {
/* 38 */     if (value < 0) {
/* 39 */       throw new IllegalArgumentException(name + " cannot be negative but was: " + value);
/*    */     }
/* 41 */     return value;
/*    */   }
/*    */   
/*    */   @CanIgnoreReturnValue
/*    */   static long checkNonnegative(long value, String name) {
/* 46 */     if (value < 0L) {
/* 47 */       throw new IllegalArgumentException(name + " cannot be negative but was: " + value);
/*    */     }
/* 49 */     return value;
/*    */   }
/*    */   
/*    */   static void checkPositive(int value, String name) {
/* 53 */     if (value <= 0) {
/* 54 */       throw new IllegalArgumentException(name + " must be positive but was: " + value);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static void checkRemove(boolean canRemove) {
/* 63 */     Preconditions.checkState(canRemove, "no calls to next() since the last call to remove()");
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\CollectPreconditions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */