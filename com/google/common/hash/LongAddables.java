/*    */ package com.google.common.hash;
/*    */ 
/*    */ import com.google.common.base.Supplier;
/*    */ import java.util.concurrent.atomic.AtomicLong;
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
/*    */ final class LongAddables
/*    */ {
/*    */   private static final Supplier<LongAddable> SUPPLIER;
/*    */   
/*    */   static {
/*    */     Supplier<LongAddable> supplier;
/*    */     try {
/* 31 */       new LongAdder();
/* 32 */       supplier = new Supplier<LongAddable>()
/*    */         {
/*    */           public LongAddable get()
/*    */           {
/* 36 */             return new LongAdder();
/*    */           }
/*    */         };
/* 39 */     } catch (Throwable t) {
/* 40 */       supplier = new Supplier<LongAddable>()
/*    */         {
/*    */           public LongAddable get()
/*    */           {
/* 44 */             return new LongAddables.PureJavaLongAddable();
/*    */           }
/*    */         };
/*    */     } 
/* 48 */     SUPPLIER = supplier;
/*    */   }
/*    */   
/*    */   public static LongAddable create() {
/* 52 */     return (LongAddable)SUPPLIER.get();
/*    */   }
/*    */   
/*    */   private static final class PureJavaLongAddable
/*    */     extends AtomicLong implements LongAddable {
/*    */     public void increment() {
/* 58 */       getAndIncrement();
/*    */     }
/*    */     private PureJavaLongAddable() {}
/*    */     
/*    */     public void add(long x) {
/* 63 */       getAndAdd(x);
/*    */     }
/*    */ 
/*    */     
/*    */     public long sum() {
/* 68 */       return get();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\hash\LongAddables.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */