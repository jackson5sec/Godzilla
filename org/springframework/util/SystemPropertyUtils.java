/*     */ package org.springframework.util;
/*     */ 
/*     */ import org.springframework.lang.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class SystemPropertyUtils
/*     */ {
/*     */   public static final String PLACEHOLDER_PREFIX = "${";
/*     */   public static final String PLACEHOLDER_SUFFIX = "}";
/*     */   public static final String VALUE_SEPARATOR = ":";
/*  48 */   private static final PropertyPlaceholderHelper strictHelper = new PropertyPlaceholderHelper("${", "}", ":", false);
/*     */ 
/*     */   
/*  51 */   private static final PropertyPlaceholderHelper nonStrictHelper = new PropertyPlaceholderHelper("${", "}", ":", true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String resolvePlaceholders(String text) {
/*  65 */     return resolvePlaceholders(text, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String resolvePlaceholders(String text, boolean ignoreUnresolvablePlaceholders) {
/*  81 */     if (text.isEmpty()) {
/*  82 */       return text;
/*     */     }
/*  84 */     PropertyPlaceholderHelper helper = ignoreUnresolvablePlaceholders ? nonStrictHelper : strictHelper;
/*  85 */     return helper.replacePlaceholders(text, new SystemPropertyPlaceholderResolver(text));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SystemPropertyPlaceholderResolver
/*     */     implements PropertyPlaceholderHelper.PlaceholderResolver
/*     */   {
/*     */     private final String text;
/*     */ 
/*     */ 
/*     */     
/*     */     public SystemPropertyPlaceholderResolver(String text) {
/*  98 */       this.text = text;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String resolvePlaceholder(String placeholderName) {
/*     */       try {
/* 105 */         String propVal = System.getProperty(placeholderName);
/* 106 */         if (propVal == null)
/*     */         {
/* 108 */           propVal = System.getenv(placeholderName);
/*     */         }
/* 110 */         return propVal;
/*     */       }
/* 112 */       catch (Throwable ex) {
/* 113 */         System.err.println("Could not resolve placeholder '" + placeholderName + "' in [" + this.text + "] as system property: " + ex);
/*     */         
/* 115 */         return null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\SystemPropertyUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */