/*     */ package org.springframework.core.io.support;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import org.springframework.core.io.DefaultResourceLoader;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class LocalizedResourceHelper
/*     */ {
/*     */   public static final String DEFAULT_SEPARATOR = "_";
/*     */   private final ResourceLoader resourceLoader;
/*  42 */   private String separator = "_";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LocalizedResourceHelper() {
/*  50 */     this.resourceLoader = (ResourceLoader)new DefaultResourceLoader();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LocalizedResourceHelper(ResourceLoader resourceLoader) {
/*  58 */     Assert.notNull(resourceLoader, "ResourceLoader must not be null");
/*  59 */     this.resourceLoader = resourceLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSeparator(@Nullable String separator) {
/*  67 */     this.separator = (separator != null) ? separator : "_";
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Resource findLocalizedResource(String name, String extension, @Nullable Locale locale) {
/*  91 */     Assert.notNull(name, "Name must not be null");
/*  92 */     Assert.notNull(extension, "Extension must not be null");
/*     */     
/*  94 */     Resource resource = null;
/*     */     
/*  96 */     if (locale != null) {
/*  97 */       String lang = locale.getLanguage();
/*  98 */       String country = locale.getCountry();
/*  99 */       String variant = locale.getVariant();
/*     */ 
/*     */       
/* 102 */       if (variant.length() > 0) {
/* 103 */         String location = name + this.separator + lang + this.separator + country + this.separator + variant + extension;
/*     */         
/* 105 */         resource = this.resourceLoader.getResource(location);
/*     */       } 
/*     */ 
/*     */       
/* 109 */       if ((resource == null || !resource.exists()) && country.length() > 0) {
/* 110 */         String location = name + this.separator + lang + this.separator + country + extension;
/* 111 */         resource = this.resourceLoader.getResource(location);
/*     */       } 
/*     */ 
/*     */       
/* 115 */       if ((resource == null || !resource.exists()) && lang.length() > 0) {
/* 116 */         String location = name + this.separator + lang + extension;
/* 117 */         resource = this.resourceLoader.getResource(location);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 122 */     if (resource == null || !resource.exists()) {
/* 123 */       String location = name + extension;
/* 124 */       resource = this.resourceLoader.getResource(location);
/*     */     } 
/*     */     
/* 127 */     return resource;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\io\support\LocalizedResourceHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */