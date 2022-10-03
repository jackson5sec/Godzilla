/*     */ package org.springframework.core.env;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.core.convert.support.ConfigurableConversionService;
/*     */ import org.springframework.core.convert.support.DefaultConversionService;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.PropertyPlaceholderHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractPropertyResolver
/*     */   implements ConfigurablePropertyResolver
/*     */ {
/*  44 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   @Nullable
/*     */   private volatile ConfigurableConversionService conversionService;
/*     */   
/*     */   @Nullable
/*     */   private PropertyPlaceholderHelper nonStrictHelper;
/*     */   
/*     */   @Nullable
/*     */   private PropertyPlaceholderHelper strictHelper;
/*     */   
/*     */   private boolean ignoreUnresolvableNestedPlaceholders = false;
/*     */   
/*  57 */   private String placeholderPrefix = "${";
/*     */   
/*  59 */   private String placeholderSuffix = "}";
/*     */   @Nullable
/*  61 */   private String valueSeparator = ":";
/*     */ 
/*     */   
/*  64 */   private final Set<String> requiredProperties = new LinkedHashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConfigurableConversionService getConversionService() {
/*     */     DefaultConversionService defaultConversionService;
/*  71 */     ConfigurableConversionService cs = this.conversionService;
/*  72 */     if (cs == null) {
/*  73 */       synchronized (this) {
/*  74 */         cs = this.conversionService;
/*  75 */         if (cs == null) {
/*  76 */           defaultConversionService = new DefaultConversionService();
/*  77 */           this.conversionService = (ConfigurableConversionService)defaultConversionService;
/*     */         } 
/*     */       } 
/*     */     }
/*  81 */     return (ConfigurableConversionService)defaultConversionService;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setConversionService(ConfigurableConversionService conversionService) {
/*  86 */     Assert.notNull(conversionService, "ConversionService must not be null");
/*  87 */     this.conversionService = conversionService;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPlaceholderPrefix(String placeholderPrefix) {
/*  97 */     Assert.notNull(placeholderPrefix, "'placeholderPrefix' must not be null");
/*  98 */     this.placeholderPrefix = placeholderPrefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPlaceholderSuffix(String placeholderSuffix) {
/* 108 */     Assert.notNull(placeholderSuffix, "'placeholderSuffix' must not be null");
/* 109 */     this.placeholderSuffix = placeholderSuffix;
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
/*     */   public void setValueSeparator(@Nullable String valueSeparator) {
/* 121 */     this.valueSeparator = valueSeparator;
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
/*     */   public void setIgnoreUnresolvableNestedPlaceholders(boolean ignoreUnresolvableNestedPlaceholders) {
/* 135 */     this.ignoreUnresolvableNestedPlaceholders = ignoreUnresolvableNestedPlaceholders;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRequiredProperties(String... requiredProperties) {
/* 140 */     Collections.addAll(this.requiredProperties, requiredProperties);
/*     */   }
/*     */ 
/*     */   
/*     */   public void validateRequiredProperties() {
/* 145 */     MissingRequiredPropertiesException ex = new MissingRequiredPropertiesException();
/* 146 */     for (String key : this.requiredProperties) {
/* 147 */       if (getProperty(key) == null) {
/* 148 */         ex.addMissingRequiredProperty(key);
/*     */       }
/*     */     } 
/* 151 */     if (!ex.getMissingRequiredProperties().isEmpty()) {
/* 152 */       throw ex;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsProperty(String key) {
/* 158 */     return (getProperty(key) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getProperty(String key) {
/* 164 */     return getProperty(key, String.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getProperty(String key, String defaultValue) {
/* 169 */     String value = getProperty(key);
/* 170 */     return (value != null) ? value : defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
/* 175 */     T value = getProperty(key, targetType);
/* 176 */     return (value != null) ? value : defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getRequiredProperty(String key) throws IllegalStateException {
/* 181 */     String value = getProperty(key);
/* 182 */     if (value == null) {
/* 183 */       throw new IllegalStateException("Required key '" + key + "' not found");
/*     */     }
/* 185 */     return value;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getRequiredProperty(String key, Class<T> valueType) throws IllegalStateException {
/* 190 */     T value = getProperty(key, valueType);
/* 191 */     if (value == null) {
/* 192 */       throw new IllegalStateException("Required key '" + key + "' not found");
/*     */     }
/* 194 */     return value;
/*     */   }
/*     */ 
/*     */   
/*     */   public String resolvePlaceholders(String text) {
/* 199 */     if (this.nonStrictHelper == null) {
/* 200 */       this.nonStrictHelper = createPlaceholderHelper(true);
/*     */     }
/* 202 */     return doResolvePlaceholders(text, this.nonStrictHelper);
/*     */   }
/*     */ 
/*     */   
/*     */   public String resolveRequiredPlaceholders(String text) throws IllegalArgumentException {
/* 207 */     if (this.strictHelper == null) {
/* 208 */       this.strictHelper = createPlaceholderHelper(false);
/*     */     }
/* 210 */     return doResolvePlaceholders(text, this.strictHelper);
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
/*     */   protected String resolveNestedPlaceholders(String value) {
/* 226 */     if (value.isEmpty()) {
/* 227 */       return value;
/*     */     }
/* 229 */     return this.ignoreUnresolvableNestedPlaceholders ? 
/* 230 */       resolvePlaceholders(value) : resolveRequiredPlaceholders(value);
/*     */   }
/*     */   
/*     */   private PropertyPlaceholderHelper createPlaceholderHelper(boolean ignoreUnresolvablePlaceholders) {
/* 234 */     return new PropertyPlaceholderHelper(this.placeholderPrefix, this.placeholderSuffix, this.valueSeparator, ignoreUnresolvablePlaceholders);
/*     */   }
/*     */ 
/*     */   
/*     */   private String doResolvePlaceholders(String text, PropertyPlaceholderHelper helper) {
/* 239 */     return helper.replacePlaceholders(text, this::getPropertyAsRawString);
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
/*     */   @Nullable
/*     */   protected <T> T convertValueIfNecessary(Object value, @Nullable Class<T> targetType) {
/*     */     ConversionService conversionService;
/* 253 */     if (targetType == null) {
/* 254 */       return (T)value;
/*     */     }
/* 256 */     ConfigurableConversionService configurableConversionService = this.conversionService;
/* 257 */     if (configurableConversionService == null) {
/*     */ 
/*     */       
/* 260 */       if (ClassUtils.isAssignableValue(targetType, value)) {
/* 261 */         return (T)value;
/*     */       }
/* 263 */       conversionService = DefaultConversionService.getSharedInstance();
/*     */     } 
/* 265 */     return (T)conversionService.convert(value, targetType);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected abstract String getPropertyAsRawString(String paramString);
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\env\AbstractPropertyResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */