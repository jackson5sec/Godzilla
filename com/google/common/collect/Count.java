/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.io.Serializable;
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
/*    */ final class Count
/*    */   implements Serializable
/*    */ {
/*    */   private int value;
/*    */   
/*    */   Count(int value) {
/* 31 */     this.value = value;
/*    */   }
/*    */   
/*    */   public int get() {
/* 35 */     return this.value;
/*    */   }
/*    */   
/*    */   public void add(int delta) {
/* 39 */     this.value += delta;
/*    */   }
/*    */   
/*    */   public int addAndGet(int delta) {
/* 43 */     return this.value += delta;
/*    */   }
/*    */   
/*    */   public void set(int newValue) {
/* 47 */     this.value = newValue;
/*    */   }
/*    */   
/*    */   public int getAndSet(int newValue) {
/* 51 */     int result = this.value;
/* 52 */     this.value = newValue;
/* 53 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 58 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 63 */     return (obj instanceof Count && ((Count)obj).value == this.value);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 68 */     return Integer.toString(this.value);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\Count.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */