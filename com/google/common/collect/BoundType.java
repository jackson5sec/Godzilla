/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
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
/*    */ @GwtCompatible
/*    */ public enum BoundType
/*    */ {
/* 29 */   OPEN(false),
/* 30 */   CLOSED(true);
/*    */   
/*    */   final boolean inclusive;
/*    */   
/*    */   BoundType(boolean inclusive) {
/* 35 */     this.inclusive = inclusive;
/*    */   }
/*    */ 
/*    */   
/*    */   static BoundType forBoolean(boolean inclusive) {
/* 40 */     return inclusive ? CLOSED : OPEN;
/*    */   }
/*    */   
/*    */   BoundType flip() {
/* 44 */     return forBoolean(!this.inclusive);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\BoundType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */