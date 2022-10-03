/*    */ package com.google.common.cache;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
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
/*    */ @GwtCompatible(emulated = true)
/*    */ final class LongAddables
/*    */ {
/*    */   private static final Supplier<LongAddable> SUPPLIER;
/*    */   
/*    */   static {
/*    */     Supplier<LongAddable> supplier;
/*    */     try {
/* 33 */       new LongAdder();
/* 34 */       supplier = new Supplier<LongAddable>()
/*    */         {
/*    */           public LongAddable get()
/*    */           {
/* 38 */             return new LongAdder();
/*    */           }
/*    */         };
/* 41 */     } catch (Throwable t) {
/* 42 */       supplier = new Supplier<LongAddable>()
/*    */         {
/*    */           public LongAddable get()
/*    */           {
/* 46 */             return new LongAddables.PureJavaLongAddable();
/*    */           }
/*    */         };
/*    */     } 
/* 50 */     SUPPLIER = supplier;
/*    */   }
/*    */   
/*    */   public static LongAddable create() {
/* 54 */     return (LongAddable)SUPPLIER.get();
/*    */   }
/*    */   
/*    */   private static final class PureJavaLongAddable
/*    */     extends AtomicLong implements LongAddable {
/*    */     public void increment() {
/* 60 */       getAndIncrement();
/*    */     }
/*    */     private PureJavaLongAddable() {}
/*    */     
/*    */     public void add(long x) {
/* 65 */       getAndAdd(x);
/*    */     }
/*    */ 
/*    */     
/*    */     public long sum() {
/* 70 */       return get();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\cache\LongAddables.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */