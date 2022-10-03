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
/*    */ public class ClassesKey
/*    */ {
/* 19 */   private static final Key FACTORY = (Key)KeyFactory.create(Key.class);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Object create(Object[] array) {
/* 29 */     return FACTORY.newInstance((Object[])classNames(array));
/*    */   }
/*    */   
/*    */   private static String[] classNames(Object[] objects) {
/* 33 */     if (objects == null) {
/* 34 */       return null;
/*    */     }
/* 36 */     String[] classNames = new String[objects.length];
/* 37 */     for (int i = 0; i < objects.length; i++) {
/* 38 */       Object object = objects[i];
/* 39 */       if (object != null) {
/* 40 */         Class<?> aClass = object.getClass();
/* 41 */         classNames[i] = (aClass == null) ? null : aClass.getName();
/*    */       } 
/*    */     } 
/* 44 */     return classNames;
/*    */   }
/*    */   
/*    */   static interface Key {
/*    */     Object newInstance(Object[] param1ArrayOfObject);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\core\ClassesKey.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */