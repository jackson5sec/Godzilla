/*    */ package org.apache.log4j.helpers;
/*    */ 
/*    */ import java.util.Enumeration;
/*    */ import java.util.NoSuchElementException;
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
/*    */ public class NullEnumeration
/*    */   implements Enumeration
/*    */ {
/* 31 */   private static final NullEnumeration instance = new NullEnumeration();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static NullEnumeration getInstance() {
/* 38 */     return instance;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasMoreElements() {
/* 43 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object nextElement() {
/* 48 */     throw new NoSuchElementException();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\helpers\NullEnumeration.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */