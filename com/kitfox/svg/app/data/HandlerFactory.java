/*    */ package com.kitfox.svg.app.data;
/*    */ 
/*    */ import java.net.URLStreamHandler;
/*    */ import java.net.URLStreamHandlerFactory;
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
/*    */ public class HandlerFactory
/*    */   implements URLStreamHandlerFactory
/*    */ {
/* 46 */   static Handler handler = new Handler();
/*    */ 
/*    */   
/*    */   public URLStreamHandler createURLStreamHandler(String protocol) {
/* 50 */     if ("data".equals(protocol))
/*    */     {
/* 52 */       return handler;
/*    */     }
/* 54 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\app\data\HandlerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */