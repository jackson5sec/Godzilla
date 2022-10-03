/*    */ package org.springframework.util;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class PatternMatchUtils
/*    */ {
/*    */   public static boolean simpleMatch(@Nullable String pattern, @Nullable String str) {
/* 39 */     if (pattern == null || str == null) {
/* 40 */       return false;
/*    */     }
/*    */     
/* 43 */     int firstIndex = pattern.indexOf('*');
/* 44 */     if (firstIndex == -1) {
/* 45 */       return pattern.equals(str);
/*    */     }
/*    */     
/* 48 */     if (firstIndex == 0) {
/* 49 */       if (pattern.length() == 1) {
/* 50 */         return true;
/*    */       }
/* 52 */       int nextIndex = pattern.indexOf('*', 1);
/* 53 */       if (nextIndex == -1) {
/* 54 */         return str.endsWith(pattern.substring(1));
/*    */       }
/* 56 */       String part = pattern.substring(1, nextIndex);
/* 57 */       if (part.isEmpty()) {
/* 58 */         return simpleMatch(pattern.substring(nextIndex), str);
/*    */       }
/* 60 */       int partIndex = str.indexOf(part);
/* 61 */       while (partIndex != -1) {
/* 62 */         if (simpleMatch(pattern.substring(nextIndex), str.substring(partIndex + part.length()))) {
/* 63 */           return true;
/*    */         }
/* 65 */         partIndex = str.indexOf(part, partIndex + 1);
/*    */       } 
/* 67 */       return false;
/*    */     } 
/*    */     
/* 70 */     return (str.length() >= firstIndex && pattern
/* 71 */       .startsWith(str.substring(0, firstIndex)) && 
/* 72 */       simpleMatch(pattern.substring(firstIndex), str.substring(firstIndex)));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean simpleMatch(@Nullable String[] patterns, String str) {
/* 84 */     if (patterns != null) {
/* 85 */       for (String pattern : patterns) {
/* 86 */         if (simpleMatch(pattern, str)) {
/* 87 */           return true;
/*    */         }
/*    */       } 
/*    */     }
/* 91 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\PatternMatchUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */