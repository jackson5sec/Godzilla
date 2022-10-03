/*    */ package org.mozilla.javascript.commonjs.module.provider;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.net.URLConnection;
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
/*    */ public class DefaultUrlConnectionExpiryCalculator
/*    */   implements UrlConnectionExpiryCalculator, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private final long relativeExpiry;
/*    */   
/*    */   public DefaultUrlConnectionExpiryCalculator() {
/* 29 */     this(60000L);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultUrlConnectionExpiryCalculator(long relativeExpiry) {
/* 38 */     if (relativeExpiry < 0L) {
/* 39 */       throw new IllegalArgumentException("relativeExpiry < 0");
/*    */     }
/* 41 */     this.relativeExpiry = relativeExpiry;
/*    */   }
/*    */   
/*    */   public long calculateExpiry(URLConnection urlConnection) {
/* 45 */     return System.currentTimeMillis() + this.relativeExpiry;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\commonjs\module\provider\DefaultUrlConnectionExpiryCalculator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */