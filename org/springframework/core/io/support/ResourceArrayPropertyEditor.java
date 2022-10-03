/*     */ package org.springframework.core.io.support;
/*     */ 
/*     */ import java.beans.PropertyEditorSupport;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.env.PropertyResolver;
/*     */ import org.springframework.core.env.StandardEnvironment;
/*     */ import org.springframework.core.io.Resource;
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
/*     */ public class ResourceArrayPropertyEditor
/*     */   extends PropertyEditorSupport
/*     */ {
/*  60 */   private static final Log logger = LogFactory.getLog(ResourceArrayPropertyEditor.class);
/*     */ 
/*     */ 
/*     */   
/*     */   private final ResourcePatternResolver resourcePatternResolver;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private PropertyResolver propertyResolver;
/*     */ 
/*     */   
/*     */   private final boolean ignoreUnresolvablePlaceholders;
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceArrayPropertyEditor() {
/*  77 */     this(new PathMatchingResourcePatternResolver(), null, true);
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
/*     */   public ResourceArrayPropertyEditor(ResourcePatternResolver resourcePatternResolver, @Nullable PropertyResolver propertyResolver) {
/*  89 */     this(resourcePatternResolver, propertyResolver, true);
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
/*     */   public ResourceArrayPropertyEditor(ResourcePatternResolver resourcePatternResolver, @Nullable PropertyResolver propertyResolver, boolean ignoreUnresolvablePlaceholders) {
/* 103 */     Assert.notNull(resourcePatternResolver, "ResourcePatternResolver must not be null");
/* 104 */     this.resourcePatternResolver = resourcePatternResolver;
/* 105 */     this.propertyResolver = propertyResolver;
/* 106 */     this.ignoreUnresolvablePlaceholders = ignoreUnresolvablePlaceholders;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAsText(String text) {
/* 115 */     String pattern = resolvePath(text).trim();
/*     */     try {
/* 117 */       setValue(this.resourcePatternResolver.getResources(pattern));
/*     */     }
/* 119 */     catch (IOException ex) {
/* 120 */       throw new IllegalArgumentException("Could not resolve resource location pattern [" + pattern + "]: " + ex
/* 121 */           .getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(Object value) throws IllegalArgumentException {
/* 131 */     if (value instanceof Collection || (value instanceof Object[] && !(value instanceof Resource[]))) {
/* 132 */       Collection<?> input = (value instanceof Collection) ? (Collection)value : Arrays.asList((Object[])value);
/* 133 */       Set<Resource> merged = new LinkedHashSet<>();
/* 134 */       for (Object element : input) {
/* 135 */         if (element instanceof String) {
/*     */ 
/*     */           
/* 138 */           String pattern = resolvePath((String)element).trim();
/*     */           try {
/* 140 */             Resource[] resources = this.resourcePatternResolver.getResources(pattern);
/* 141 */             Collections.addAll(merged, resources);
/*     */           }
/* 143 */           catch (IOException ex) {
/*     */             
/* 145 */             if (logger.isDebugEnabled())
/* 146 */               logger.debug("Could not retrieve resources for pattern '" + pattern + "'", ex); 
/*     */           } 
/*     */           continue;
/*     */         } 
/* 150 */         if (element instanceof Resource) {
/*     */           
/* 152 */           merged.add((Resource)element);
/*     */           continue;
/*     */         } 
/* 155 */         throw new IllegalArgumentException("Cannot convert element [" + element + "] to [" + Resource.class
/* 156 */             .getName() + "]: only location String and Resource object supported");
/*     */       } 
/*     */       
/* 159 */       super.setValue(merged.toArray(new Resource[0]));
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 165 */       super.setValue(value);
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
/* 178 */     if (this.propertyResolver == null) {
/* 179 */       this.propertyResolver = (PropertyResolver)new StandardEnvironment();
/*     */     }
/* 181 */     return this.ignoreUnresolvablePlaceholders ? this.propertyResolver.resolvePlaceholders(path) : this.propertyResolver
/* 182 */       .resolveRequiredPlaceholders(path);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\io\support\ResourceArrayPropertyEditor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */