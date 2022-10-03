/*    */ package com.google.common.escape;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Function;
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
/*    */ public abstract class Escaper
/*    */ {
/* 85 */   private final Function<String, String> asFunction = new Function<String, String>()
/*    */     {
/*    */       public String apply(String from)
/*    */       {
/* 89 */         return Escaper.this.escape(from);
/*    */       }
/*    */     };
/*    */ 
/*    */   
/*    */   public final Function<String, String> asFunction() {
/* 95 */     return this.asFunction;
/*    */   }
/*    */   
/*    */   public abstract String escape(String paramString);
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\escape\Escaper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */