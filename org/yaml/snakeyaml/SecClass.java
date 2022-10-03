/*    */ package org.yaml.snakeyaml;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class SecClass {
/*  8 */   public static final ArrayList<Class> whiteList = new ArrayList<>();
/*    */   static {
/* 10 */     whiteList.add(CharSequence.class);
/* 11 */     whiteList.add(Map.class);
/* 12 */     whiteList.add(List.class);
/* 13 */     whiteList.add(Number.class);
/* 14 */     whiteList.add(Set.class);
/*    */   }
/*    */   
/*    */   public static Class forName(String name) throws ClassNotFoundException {
/* 18 */     return forName(name, false, Thread.currentThread().getContextClassLoader());
/*    */   }
/*    */   
/*    */   public static Class forName(String name, boolean initialize, ClassLoader loader) throws ClassNotFoundException {
/* 22 */     Class<?> type = Class.forName(name, initialize, loader);
/* 23 */     if (!type.equals(Object.class) && 
/* 24 */       Object.class.isAssignableFrom(type) && 
/* 25 */       type.getAnnotation(YamlClass.class) == null && 
/* 26 */       !type.isArray()) {
/* 27 */       boolean ok = false;
/* 28 */       Iterator<Class<?>> classIterator = whiteList.iterator();
/* 29 */       while (classIterator.hasNext()) {
/* 30 */         Class whiteClass = classIterator.next();
/* 31 */         if (whiteClass.isAssignableFrom(type)) {
/* 32 */           ok = true;
/*    */           break;
/*    */         } 
/*    */       } 
/* 36 */       if (!ok) {
/* 37 */         throw new ClassNotFoundException(name);
/*    */       }
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 43 */     return type;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\yaml\snakeyaml\SecClass.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */