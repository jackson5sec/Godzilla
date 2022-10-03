/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import java.util.concurrent.atomic.AtomicReference;
/*    */ import java.util.concurrent.atomic.AtomicReferenceArray;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtIncompatible
/*    */ public final class Atomics
/*    */ {
/*    */   public static <V> AtomicReference<V> newReference() {
/* 38 */     return new AtomicReference<>();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <V> AtomicReference<V> newReference(V initialValue) {
/* 48 */     return new AtomicReference<>(initialValue);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <E> AtomicReferenceArray<E> newReferenceArray(int length) {
/* 58 */     return new AtomicReferenceArray<>(length);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <E> AtomicReferenceArray<E> newReferenceArray(E[] array) {
/* 69 */     return new AtomicReferenceArray<>(array);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\Atomics.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */