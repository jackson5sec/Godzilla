/*    */ package org.springframework.core;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ public interface AttributeAccessor
/*    */ {
/*    */   void setAttribute(String paramString, @Nullable Object paramObject);
/*    */   
/*    */   @Nullable
/*    */   Object getAttribute(String paramString);
/*    */   
/*    */   default <T> T computeAttribute(String name, Function<String, T> computeFunction) {
/* 74 */     Assert.notNull(name, "Name must not be null");
/* 75 */     Assert.notNull(computeFunction, "Compute function must not be null");
/* 76 */     Object value = getAttribute(name);
/* 77 */     if (value == null) {
/* 78 */       value = computeFunction.apply(name);
/* 79 */       Assert.state((value != null), () -> String.format("Compute function must not return null for attribute named '%s'", new Object[] { name }));
/*    */       
/* 81 */       setAttribute(name, value);
/*    */     } 
/* 83 */     return (T)value;
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   Object removeAttribute(String paramString);
/*    */   
/*    */   boolean hasAttribute(String paramString);
/*    */   
/*    */   String[] attributeNames();
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\AttributeAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */