/*    */ package org.mozilla.javascript;
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
/*    */ public interface Scriptable
/*    */ {
/* 44 */   public static final Object NOT_FOUND = UniqueTag.NOT_FOUND;
/*    */   
/*    */   String getClassName();
/*    */   
/*    */   Object get(String paramString, Scriptable paramScriptable);
/*    */   
/*    */   Object get(int paramInt, Scriptable paramScriptable);
/*    */   
/*    */   boolean has(String paramString, Scriptable paramScriptable);
/*    */   
/*    */   boolean has(int paramInt, Scriptable paramScriptable);
/*    */   
/*    */   void put(String paramString, Scriptable paramScriptable, Object paramObject);
/*    */   
/*    */   void put(int paramInt, Scriptable paramScriptable, Object paramObject);
/*    */   
/*    */   void delete(String paramString);
/*    */   
/*    */   void delete(int paramInt);
/*    */   
/*    */   Scriptable getPrototype();
/*    */   
/*    */   void setPrototype(Scriptable paramScriptable);
/*    */   
/*    */   Scriptable getParentScope();
/*    */   
/*    */   void setParentScope(Scriptable paramScriptable);
/*    */   
/*    */   Object[] getIds();
/*    */   
/*    */   Object getDefaultValue(Class<?> paramClass);
/*    */   
/*    */   boolean hasInstance(Scriptable paramScriptable);
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\Scriptable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */