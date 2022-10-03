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
/*    */ @GwtCompatible(serializable = true)
/*    */ class EmptyImmutableListMultimap
/*    */   extends ImmutableListMultimap<Object, Object>
/*    */ {
/* 28 */   static final EmptyImmutableListMultimap INSTANCE = new EmptyImmutableListMultimap();
/*    */   
/*    */   private EmptyImmutableListMultimap() {
/* 31 */     super(ImmutableMap.of(), 0);
/*    */   }
/*    */   private static final long serialVersionUID = 0L;
/*    */   private Object readResolve() {
/* 35 */     return INSTANCE;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\EmptyImmutableListMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */