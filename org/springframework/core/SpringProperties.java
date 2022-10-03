/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.util.Properties;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SpringProperties
/*     */ {
/*     */   private static final String PROPERTIES_RESOURCE_LOCATION = "spring.properties";
/*  56 */   private static final Properties localProperties = new Properties();
/*     */ 
/*     */   
/*     */   static {
/*     */     try {
/*  61 */       ClassLoader cl = SpringProperties.class.getClassLoader();
/*     */       
/*  63 */       URL url = (cl != null) ? cl.getResource("spring.properties") : ClassLoader.getSystemResource("spring.properties");
/*  64 */       if (url != null) {
/*  65 */         try (InputStream is = url.openStream()) {
/*  66 */           localProperties.load(is);
/*     */         }
/*     */       
/*     */       }
/*  70 */     } catch (IOException ex) {
/*  71 */       System.err.println("Could not load 'spring.properties' file from local classpath: " + ex);
/*     */     } 
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
/*     */   public static void setProperty(String key, @Nullable String value) {
/*  87 */     if (value != null) {
/*  88 */       localProperties.setProperty(key, value);
/*     */     } else {
/*     */       
/*  91 */       localProperties.remove(key);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static String getProperty(String key) {
/* 103 */     String value = localProperties.getProperty(key);
/* 104 */     if (value == null) {
/*     */       try {
/* 106 */         value = System.getProperty(key);
/*     */       }
/* 108 */       catch (Throwable ex) {
/* 109 */         System.err.println("Could not retrieve system property '" + key + "': " + ex);
/*     */       } 
/*     */     }
/* 112 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setFlag(String key) {
/* 121 */     localProperties.put(key, Boolean.TRUE.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean getFlag(String key) {
/* 131 */     return Boolean.parseBoolean(getProperty(key));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\SpringProperties.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */