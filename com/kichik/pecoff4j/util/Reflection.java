/*    */ package com.kichik.pecoff4j.util;
/*    */ 
/*    */ import java.lang.reflect.Field;
/*    */ import java.lang.reflect.Modifier;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Reflection
/*    */ {
/*    */   public static String toString(Object o) {
/* 17 */     StringBuilder sb = new StringBuilder();
/* 18 */     Field[] fields = o.getClass().getDeclaredFields();
/* 19 */     for (Field f : fields) {
/* 20 */       if (!Modifier.isStatic(f.getModifiers())) {
/*    */         
/* 22 */         f.setAccessible(true);
/* 23 */         sb.append(f.getName());
/* 24 */         sb.append(": ");
/*    */         try {
/* 26 */           Object val = f.get(o);
/* 27 */           if (val instanceof Integer) {
/* 28 */             sb.append(f.get(o));
/* 29 */             sb.append(" (0x");
/* 30 */             sb.append(Integer.toHexString(((Integer)val).intValue()));
/* 31 */             sb.append(")");
/* 32 */           } else if (val instanceof Long) {
/* 33 */             sb.append(f.get(o));
/* 34 */             sb.append(" (0x");
/* 35 */             sb.append(Long.toHexString(((Long)val).longValue()));
/* 36 */             sb.append(")");
/* 37 */           } else if (val != null && val.getClass().isArray()) {
/* 38 */             if (val instanceof int[]) {
/* 39 */               int[] arr = (int[])val;
/* 40 */               for (int i = 0; i < arr.length && i < 10; i++) {
/* 41 */                 if (i != 0)
/* 42 */                   sb.append(", "); 
/* 43 */                 sb.append(arr[i]);
/*    */               } 
/* 45 */             } else if (val instanceof byte[]) {
/* 46 */               byte[] arr = (byte[])val;
/* 47 */               for (int i = 0; i < arr.length && i < 10; i++) {
/* 48 */                 if (i != 0)
/* 49 */                   sb.append(", "); 
/* 50 */                 sb.append(Integer.toHexString(arr[i] & 0xFF));
/*    */               } 
/*    */             } else {
/* 53 */               Object[] arr = (Object[])val;
/* 54 */               for (int i = 0; i < arr.length && i < 10; i++) {
/* 55 */                 if (i != 0)
/* 56 */                   sb.append(", "); 
/* 57 */                 sb.append(arr[i]);
/*    */               } 
/*    */             } 
/*    */           } else {
/* 61 */             sb.append(f.get(o));
/*    */           } 
/* 63 */         } catch (Exception e) {
/* 64 */           sb.append(e.getMessage());
/*    */         } 
/* 66 */         sb.append("\n");
/*    */       } 
/* 68 */     }  return sb.toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public static String getConstantName(Class clazz, int value) throws Exception {
/* 73 */     Field[] fields = clazz.getDeclaredFields();
/* 74 */     Integer valObj = Integer.valueOf(value);
/* 75 */     for (int i = 0; i < fields.length; i++) {
/* 76 */       Field f = fields[i];
/* 77 */       if (Modifier.isStatic(f.getModifiers()) && 
/* 78 */         Modifier.isPublic(f.getModifiers()) && 
/* 79 */         f.get((Object)null).equals(valObj)) {
/* 80 */         return f.getName();
/*    */       }
/*    */     } 
/*    */ 
/*    */     
/* 85 */     return null;
/*    */   }
/*    */   
/*    */   public static void println(Object o) {
/* 89 */     System.out.println(toString(o));
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4\\util\Reflection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */