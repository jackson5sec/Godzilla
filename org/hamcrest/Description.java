/*    */ package org.hamcrest;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface Description
/*    */ {
/* 13 */   public static final Description NONE = new NullDescription();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   Description appendText(String paramString);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   Description appendDescriptionOf(SelfDescribing paramSelfDescribing);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   Description appendValue(Object paramObject);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   <T> Description appendValueList(String paramString1, String paramString2, String paramString3, T... paramVarArgs);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   <T> Description appendValueList(String paramString1, String paramString2, String paramString3, Iterable<T> paramIterable);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   Description appendList(String paramString1, String paramString2, String paramString3, Iterable<? extends SelfDescribing> paramIterable);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static final class NullDescription
/*    */     implements Description
/*    */   {
/*    */     public Description appendDescriptionOf(SelfDescribing value) {
/* 53 */       return this;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public Description appendList(String start, String separator, String end, Iterable<? extends SelfDescribing> values) {
/* 59 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public Description appendText(String text) {
/* 64 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public Description appendValue(Object value) {
/* 69 */       return this;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public <T> Description appendValueList(String start, String separator, String end, T... values) {
/* 75 */       return this;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public <T> Description appendValueList(String start, String separator, String end, Iterable<T> values) {
/* 81 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public String toString() {
/* 86 */       return "";
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\hamcrest\Description.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */