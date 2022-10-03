/*     */ package org.apache.log4j.lf5.util;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
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
/*     */ public class ResourceUtils
/*     */ {
/*     */   public static InputStream getResourceAsStream(Object object, Resource resource) {
/*  73 */     ClassLoader loader = object.getClass().getClassLoader();
/*     */     
/*  75 */     InputStream in = null;
/*     */     
/*  77 */     if (loader != null) {
/*  78 */       in = loader.getResourceAsStream(resource.getName());
/*     */     } else {
/*  80 */       in = ClassLoader.getSystemResourceAsStream(resource.getName());
/*     */     } 
/*     */     
/*  83 */     return in;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static URL getResourceAsURL(Object object, Resource resource) {
/* 102 */     ClassLoader loader = object.getClass().getClassLoader();
/*     */     
/* 104 */     URL url = null;
/*     */     
/* 106 */     if (loader != null) {
/* 107 */       url = loader.getResource(resource.getName());
/*     */     } else {
/* 109 */       url = ClassLoader.getSystemResource(resource.getName());
/*     */     } 
/*     */     
/* 112 */     return url;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\lf\\util\ResourceUtils.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */