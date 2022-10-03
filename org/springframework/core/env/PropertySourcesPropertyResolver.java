/*     */ package org.springframework.core.env;
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
/*     */ public class PropertySourcesPropertyResolver
/*     */   extends AbstractPropertyResolver
/*     */ {
/*     */   @Nullable
/*     */   private final PropertySources propertySources;
/*     */   
/*     */   public PropertySourcesPropertyResolver(@Nullable PropertySources propertySources) {
/*  43 */     this.propertySources = propertySources;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsProperty(String key) {
/*  49 */     if (this.propertySources != null) {
/*  50 */       for (PropertySource<?> propertySource : (Iterable<PropertySource<?>>)this.propertySources) {
/*  51 */         if (propertySource.containsProperty(key)) {
/*  52 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/*  56 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getProperty(String key) {
/*  62 */     return getProperty(key, String.class, true);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <T> T getProperty(String key, Class<T> targetValueType) {
/*  68 */     return getProperty(key, targetValueType, true);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getPropertyAsRawString(String key) {
/*  74 */     return getProperty(key, String.class, false);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected <T> T getProperty(String key, Class<T> targetValueType, boolean resolveNestedPlaceholders) {
/*  79 */     if (this.propertySources != null) {
/*  80 */       for (PropertySource<?> propertySource : (Iterable<PropertySource<?>>)this.propertySources) {
/*  81 */         if (this.logger.isTraceEnabled()) {
/*  82 */           this.logger.trace("Searching for key '" + key + "' in PropertySource '" + propertySource
/*  83 */               .getName() + "'");
/*     */         }
/*  85 */         Object value = propertySource.getProperty(key);
/*  86 */         if (value != null) {
/*  87 */           if (resolveNestedPlaceholders && value instanceof String) {
/*  88 */             value = resolveNestedPlaceholders((String)value);
/*     */           }
/*  90 */           logKeyFound(key, propertySource, value);
/*  91 */           return convertValueIfNecessary(value, targetValueType);
/*     */         } 
/*     */       } 
/*     */     }
/*  95 */     if (this.logger.isTraceEnabled()) {
/*  96 */       this.logger.trace("Could not find key '" + key + "' in any property source");
/*     */     }
/*  98 */     return null;
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
/*     */   protected void logKeyFound(String key, PropertySource<?> propertySource, Object value) {
/* 114 */     if (this.logger.isDebugEnabled())
/* 115 */       this.logger.debug("Found key '" + key + "' in PropertySource '" + propertySource.getName() + "' with value of type " + value
/* 116 */           .getClass().getSimpleName()); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\env\PropertySourcesPropertyResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */