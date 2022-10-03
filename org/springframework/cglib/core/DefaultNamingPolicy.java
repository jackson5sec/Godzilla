/*    */ package org.springframework.cglib.core;
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
/*    */ public class DefaultNamingPolicy
/*    */   implements NamingPolicy
/*    */ {
/* 31 */   public static final DefaultNamingPolicy INSTANCE = new DefaultNamingPolicy();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   private static final boolean STRESS_HASH_CODE = Boolean.getBoolean("org.springframework.cglib.test.stressHashCodes");
/*    */   
/*    */   public String getClassName(String prefix, String source, Object key, Predicate names) {
/* 39 */     if (prefix == null) {
/* 40 */       prefix = "org.springframework.cglib.empty.Object";
/* 41 */     } else if (prefix.startsWith("java")) {
/* 42 */       prefix = "$" + prefix;
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 48 */     String base = prefix + "$$" + source.substring(source.lastIndexOf('.') + 1) + getTag() + "$$" + Integer.toHexString(STRESS_HASH_CODE ? 0 : key.hashCode());
/* 49 */     String attempt = base;
/* 50 */     int index = 2;
/* 51 */     while (names.evaluate(attempt))
/* 52 */       attempt = base + "_" + index++; 
/* 53 */     return attempt;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected String getTag() {
/* 61 */     return "ByCGLIB";
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 65 */     return getTag().hashCode();
/*    */   }
/*    */   
/*    */   public boolean equals(Object o) {
/* 69 */     return (o instanceof DefaultNamingPolicy && ((DefaultNamingPolicy)o).getTag().equals(getTag()));
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\core\DefaultNamingPolicy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */