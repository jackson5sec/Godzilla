/*    */ package org.springframework.core;
/*    */ 
/*    */ import org.springframework.util.Assert;
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
/*    */ public class NamedThreadLocal<T>
/*    */   extends ThreadLocal<T>
/*    */ {
/*    */   private final String name;
/*    */   
/*    */   public NamedThreadLocal(String name) {
/* 40 */     Assert.hasText(name, "Name must not be empty");
/* 41 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 46 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\NamedThreadLocal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */