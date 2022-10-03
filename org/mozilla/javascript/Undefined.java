/*    */ package org.mozilla.javascript;
/*    */ 
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
/*    */ public class Undefined
/*    */   implements Serializable
/*    */ {
/*    */   static final long serialVersionUID = 9195680630202616767L;
/* 18 */   public static final Object instance = new Undefined();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object readResolve() {
/* 26 */     return instance;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\Undefined.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */