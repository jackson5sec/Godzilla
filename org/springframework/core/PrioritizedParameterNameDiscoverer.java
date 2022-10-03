/*    */ package org.springframework.core;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ public class PrioritizedParameterNameDiscoverer
/*    */   implements ParameterNameDiscoverer
/*    */ {
/* 39 */   private final List<ParameterNameDiscoverer> parameterNameDiscoverers = new ArrayList<>(2);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addDiscoverer(ParameterNameDiscoverer pnd) {
/* 47 */     this.parameterNameDiscoverers.add(pnd);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String[] getParameterNames(Method method) {
/* 54 */     for (ParameterNameDiscoverer pnd : this.parameterNameDiscoverers) {
/* 55 */       String[] result = pnd.getParameterNames(method);
/* 56 */       if (result != null) {
/* 57 */         return result;
/*    */       }
/*    */     } 
/* 60 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String[] getParameterNames(Constructor<?> ctor) {
/* 66 */     for (ParameterNameDiscoverer pnd : this.parameterNameDiscoverers) {
/* 67 */       String[] result = pnd.getParameterNames(ctor);
/* 68 */       if (result != null) {
/* 69 */         return result;
/*    */       }
/*    */     } 
/* 72 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\PrioritizedParameterNameDiscoverer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */