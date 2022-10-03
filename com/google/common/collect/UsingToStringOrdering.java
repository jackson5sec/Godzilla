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
/*    */ @GwtCompatible(serializable = true)
/*    */ final class UsingToStringOrdering
/*    */   extends Ordering<Object>
/*    */   implements Serializable
/*    */ {
/* 25 */   static final UsingToStringOrdering INSTANCE = new UsingToStringOrdering();
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   public int compare(Object left, Object right) {
/* 29 */     return left.toString().compareTo(right.toString());
/*    */   }
/*    */ 
/*    */   
/*    */   private Object readResolve() {
/* 34 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 39 */     return "Ordering.usingToString()";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\UsingToStringOrdering.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */