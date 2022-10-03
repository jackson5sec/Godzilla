/*    */ package org.apache.log4j.jmx;
/*    */ 
/*    */ import java.lang.reflect.Method;
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
/*    */ class MethodUnion
/*    */ {
/*    */   Method readMethod;
/*    */   Method writeMethod;
/*    */   
/*    */   MethodUnion(Method readMethod, Method writeMethod) {
/* 28 */     this.readMethod = readMethod;
/* 29 */     this.writeMethod = writeMethod;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\jmx\MethodUnion.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */