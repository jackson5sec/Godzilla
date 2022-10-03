/*     */ package org.springframework.core.io;
/*     */ 
/*     */ import java.beans.PropertyEditorSupport;
/*     */ import java.io.IOException;
/*     */ import org.springframework.core.env.PropertyResolver;
/*     */ import org.springframework.core.env.StandardEnvironment;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public class ResourceEditor
/*     */   extends PropertyEditorSupport
/*     */ {
/*     */   private final ResourceLoader resourceLoader;
/*     */   @Nullable
/*     */   private PropertyResolver propertyResolver;
/*     */   private final boolean ignoreUnresolvablePlaceholders;
/*     */   
/*     */   public ResourceEditor() {
/*  65 */     this(new DefaultResourceLoader(), (PropertyResolver)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceEditor(ResourceLoader resourceLoader, @Nullable PropertyResolver propertyResolver) {
/*  75 */     this(resourceLoader, propertyResolver, true);
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
/*     */   public ResourceEditor(ResourceLoader resourceLoader, @Nullable PropertyResolver propertyResolver, boolean ignoreUnresolvablePlaceholders) {
/*  89 */     Assert.notNull(resourceLoader, "ResourceLoader must not be null");
/*  90 */     this.resourceLoader = resourceLoader;
/*  91 */     this.propertyResolver = propertyResolver;
/*  92 */     this.ignoreUnresolvablePlaceholders = ignoreUnresolvablePlaceholders;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAsText(String text) {
/*  98 */     if (StringUtils.hasText(text)) {
/*  99 */       String locationToUse = resolvePath(text).trim();
/* 100 */       setValue(this.resourceLoader.getResource(locationToUse));
/*     */     } else {
/*     */       
/* 103 */       setValue(null);
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
/*     */   protected String resolvePath(String path) {
/* 116 */     if (this.propertyResolver == null) {
/* 117 */       this.propertyResolver = (PropertyResolver)new StandardEnvironment();
/*     */     }
/* 119 */     return this.ignoreUnresolvablePlaceholders ? this.propertyResolver.resolvePlaceholders(path) : this.propertyResolver
/* 120 */       .resolveRequiredPlaceholders(path);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getAsText() {
/* 127 */     Resource value = (Resource)getValue();
/*     */     
/*     */     try {
/* 130 */       return (value != null) ? value.getURL().toExternalForm() : "";
/*     */     }
/* 132 */     catch (IOException ex) {
/*     */ 
/*     */       
/* 135 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\io\ResourceEditor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */