/*     */ package org.springframework.core.env;
/*     */ 
/*     */ import java.security.AccessControlException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.SpringProperties;
/*     */ import org.springframework.core.convert.support.ConfigurableConversionService;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractEnvironment
/*     */   implements ConfigurableEnvironment
/*     */ {
/*     */   public static final String IGNORE_GETENV_PROPERTY_NAME = "spring.getenv.ignore";
/*     */   public static final String ACTIVE_PROFILES_PROPERTY_NAME = "spring.profiles.active";
/*     */   public static final String DEFAULT_PROFILES_PROPERTY_NAME = "spring.profiles.default";
/*     */   protected static final String RESERVED_DEFAULT_PROFILE_NAME = "default";
/* 105 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/* 107 */   private final Set<String> activeProfiles = new LinkedHashSet<>();
/*     */   
/* 109 */   private final Set<String> defaultProfiles = new LinkedHashSet<>(getReservedDefaultProfiles());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final MutablePropertySources propertySources;
/*     */ 
/*     */ 
/*     */   
/*     */   private final ConfigurablePropertyResolver propertyResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractEnvironment() {
/* 124 */     this(new MutablePropertySources());
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
/*     */   protected AbstractEnvironment(MutablePropertySources propertySources) {
/* 138 */     this.propertySources = propertySources;
/* 139 */     this.propertyResolver = createPropertyResolver(propertySources);
/* 140 */     customizePropertySources(propertySources);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ConfigurablePropertyResolver createPropertyResolver(MutablePropertySources propertySources) {
/* 151 */     return new PropertySourcesPropertyResolver(propertySources);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ConfigurablePropertyResolver getPropertyResolver() {
/* 161 */     return this.propertyResolver;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void customizePropertySources(MutablePropertySources propertySources) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Set<String> getReservedDefaultProfiles() {
/* 254 */     return Collections.singleton("default");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getActiveProfiles() {
/* 264 */     return StringUtils.toStringArray(doGetActiveProfiles());
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
/*     */   protected Set<String> doGetActiveProfiles() {
/* 276 */     synchronized (this.activeProfiles) {
/* 277 */       if (this.activeProfiles.isEmpty()) {
/* 278 */         String profiles = doGetActiveProfilesProperty();
/* 279 */         if (StringUtils.hasText(profiles)) {
/* 280 */           setActiveProfiles(StringUtils.commaDelimitedListToStringArray(
/* 281 */                 StringUtils.trimAllWhitespace(profiles)));
/*     */         }
/*     */       } 
/* 284 */       return this.activeProfiles;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String doGetActiveProfilesProperty() {
/* 295 */     return getProperty("spring.profiles.active");
/*     */   }
/*     */ 
/*     */   
/*     */   public void setActiveProfiles(String... profiles) {
/* 300 */     Assert.notNull(profiles, "Profile array must not be null");
/* 301 */     if (this.logger.isDebugEnabled()) {
/* 302 */       this.logger.debug("Activating profiles " + Arrays.<String>asList(profiles));
/*     */     }
/* 304 */     synchronized (this.activeProfiles) {
/* 305 */       this.activeProfiles.clear();
/* 306 */       for (String profile : profiles) {
/* 307 */         validateProfile(profile);
/* 308 */         this.activeProfiles.add(profile);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addActiveProfile(String profile) {
/* 315 */     if (this.logger.isDebugEnabled()) {
/* 316 */       this.logger.debug("Activating profile '" + profile + "'");
/*     */     }
/* 318 */     validateProfile(profile);
/* 319 */     doGetActiveProfiles();
/* 320 */     synchronized (this.activeProfiles) {
/* 321 */       this.activeProfiles.add(profile);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getDefaultProfiles() {
/* 328 */     return StringUtils.toStringArray(doGetDefaultProfiles());
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
/*     */   protected Set<String> doGetDefaultProfiles() {
/* 343 */     synchronized (this.defaultProfiles) {
/* 344 */       if (this.defaultProfiles.equals(getReservedDefaultProfiles())) {
/* 345 */         String profiles = doGetDefaultProfilesProperty();
/* 346 */         if (StringUtils.hasText(profiles)) {
/* 347 */           setDefaultProfiles(StringUtils.commaDelimitedListToStringArray(
/* 348 */                 StringUtils.trimAllWhitespace(profiles)));
/*     */         }
/*     */       } 
/* 351 */       return this.defaultProfiles;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String doGetDefaultProfilesProperty() {
/* 362 */     return getProperty("spring.profiles.default");
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
/*     */   public void setDefaultProfiles(String... profiles) {
/* 375 */     Assert.notNull(profiles, "Profile array must not be null");
/* 376 */     synchronized (this.defaultProfiles) {
/* 377 */       this.defaultProfiles.clear();
/* 378 */       for (String profile : profiles) {
/* 379 */         validateProfile(profile);
/* 380 */         this.defaultProfiles.add(profile);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public boolean acceptsProfiles(String... profiles) {
/* 388 */     Assert.notEmpty((Object[])profiles, "Must specify at least one profile");
/* 389 */     for (String profile : profiles) {
/* 390 */       if (StringUtils.hasLength(profile) && profile.charAt(0) == '!') {
/* 391 */         if (!isProfileActive(profile.substring(1))) {
/* 392 */           return true;
/*     */         }
/*     */       }
/* 395 */       else if (isProfileActive(profile)) {
/* 396 */         return true;
/*     */       } 
/*     */     } 
/* 399 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean acceptsProfiles(Profiles profiles) {
/* 404 */     Assert.notNull(profiles, "Profiles must not be null");
/* 405 */     return profiles.matches(this::isProfileActive);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isProfileActive(String profile) {
/* 414 */     validateProfile(profile);
/* 415 */     Set<String> currentActiveProfiles = doGetActiveProfiles();
/* 416 */     return (currentActiveProfiles.contains(profile) || (currentActiveProfiles
/* 417 */       .isEmpty() && doGetDefaultProfiles().contains(profile)));
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
/*     */   protected void validateProfile(String profile) {
/* 431 */     if (!StringUtils.hasText(profile)) {
/* 432 */       throw new IllegalArgumentException("Invalid profile [" + profile + "]: must contain text");
/*     */     }
/* 434 */     if (profile.charAt(0) == '!') {
/* 435 */       throw new IllegalArgumentException("Invalid profile [" + profile + "]: must not begin with ! operator");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public MutablePropertySources getPropertySources() {
/* 441 */     return this.propertySources;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Object> getSystemProperties() {
/*     */     try {
/* 448 */       return System.getProperties();
/*     */     }
/* 450 */     catch (AccessControlException ex) {
/* 451 */       return new ReadOnlySystemAttributesMap()
/*     */         {
/*     */           @Nullable
/*     */           protected String getSystemAttribute(String attributeName) {
/*     */             try {
/* 456 */               return System.getProperty(attributeName);
/*     */             }
/* 458 */             catch (AccessControlException ex) {
/* 459 */               if (AbstractEnvironment.this.logger.isInfoEnabled()) {
/* 460 */                 AbstractEnvironment.this.logger.info("Caught AccessControlException when accessing system property '" + attributeName + "'; its value will be returned [null]. Reason: " + ex
/* 461 */                     .getMessage());
/*     */               }
/* 463 */               return null;
/*     */             } 
/*     */           }
/*     */         };
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Object> getSystemEnvironment() {
/* 473 */     if (suppressGetenvAccess()) {
/* 474 */       return Collections.emptyMap();
/*     */     }
/*     */     try {
/* 477 */       return (Map)System.getenv();
/*     */     }
/* 479 */     catch (AccessControlException ex) {
/* 480 */       return new ReadOnlySystemAttributesMap()
/*     */         {
/*     */           @Nullable
/*     */           protected String getSystemAttribute(String attributeName) {
/*     */             try {
/* 485 */               return System.getenv(attributeName);
/*     */             }
/* 487 */             catch (AccessControlException ex) {
/* 488 */               if (AbstractEnvironment.this.logger.isInfoEnabled()) {
/* 489 */                 AbstractEnvironment.this.logger.info("Caught AccessControlException when accessing system environment variable '" + attributeName + "'; its value will be returned [null]. Reason: " + ex
/* 490 */                     .getMessage());
/*     */               }
/* 492 */               return null;
/*     */             } 
/*     */           }
/*     */         };
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
/*     */   protected boolean suppressGetenvAccess() {
/* 511 */     return SpringProperties.getFlag("spring.getenv.ignore");
/*     */   }
/*     */ 
/*     */   
/*     */   public void merge(ConfigurableEnvironment parent) {
/* 516 */     for (PropertySource<?> ps : (Iterable<PropertySource<?>>)parent.getPropertySources()) {
/* 517 */       if (!this.propertySources.contains(ps.getName())) {
/* 518 */         this.propertySources.addLast(ps);
/*     */       }
/*     */     } 
/* 521 */     String[] parentActiveProfiles = parent.getActiveProfiles();
/* 522 */     if (!ObjectUtils.isEmpty((Object[])parentActiveProfiles)) {
/* 523 */       synchronized (this.activeProfiles) {
/* 524 */         Collections.addAll(this.activeProfiles, parentActiveProfiles);
/*     */       } 
/*     */     }
/* 527 */     String[] parentDefaultProfiles = parent.getDefaultProfiles();
/* 528 */     if (!ObjectUtils.isEmpty((Object[])parentDefaultProfiles)) {
/* 529 */       synchronized (this.defaultProfiles) {
/* 530 */         this.defaultProfiles.remove("default");
/* 531 */         Collections.addAll(this.defaultProfiles, parentDefaultProfiles);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConfigurableConversionService getConversionService() {
/* 543 */     return this.propertyResolver.getConversionService();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setConversionService(ConfigurableConversionService conversionService) {
/* 548 */     this.propertyResolver.setConversionService(conversionService);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlaceholderPrefix(String placeholderPrefix) {
/* 553 */     this.propertyResolver.setPlaceholderPrefix(placeholderPrefix);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlaceholderSuffix(String placeholderSuffix) {
/* 558 */     this.propertyResolver.setPlaceholderSuffix(placeholderSuffix);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValueSeparator(@Nullable String valueSeparator) {
/* 563 */     this.propertyResolver.setValueSeparator(valueSeparator);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIgnoreUnresolvableNestedPlaceholders(boolean ignoreUnresolvableNestedPlaceholders) {
/* 568 */     this.propertyResolver.setIgnoreUnresolvableNestedPlaceholders(ignoreUnresolvableNestedPlaceholders);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRequiredProperties(String... requiredProperties) {
/* 573 */     this.propertyResolver.setRequiredProperties(requiredProperties);
/*     */   }
/*     */ 
/*     */   
/*     */   public void validateRequiredProperties() throws MissingRequiredPropertiesException {
/* 578 */     this.propertyResolver.validateRequiredProperties();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsProperty(String key) {
/* 588 */     return this.propertyResolver.containsProperty(key);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getProperty(String key) {
/* 594 */     return this.propertyResolver.getProperty(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getProperty(String key, String defaultValue) {
/* 599 */     return this.propertyResolver.getProperty(key, defaultValue);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <T> T getProperty(String key, Class<T> targetType) {
/* 605 */     return this.propertyResolver.getProperty(key, targetType);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
/* 610 */     return this.propertyResolver.getProperty(key, targetType, defaultValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getRequiredProperty(String key) throws IllegalStateException {
/* 615 */     return this.propertyResolver.getRequiredProperty(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalStateException {
/* 620 */     return this.propertyResolver.getRequiredProperty(key, targetType);
/*     */   }
/*     */ 
/*     */   
/*     */   public String resolvePlaceholders(String text) {
/* 625 */     return this.propertyResolver.resolvePlaceholders(text);
/*     */   }
/*     */ 
/*     */   
/*     */   public String resolveRequiredPlaceholders(String text) throws IllegalArgumentException {
/* 630 */     return this.propertyResolver.resolveRequiredPlaceholders(text);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 636 */     return getClass().getSimpleName() + " {activeProfiles=" + this.activeProfiles + ", defaultProfiles=" + this.defaultProfiles + ", propertySources=" + this.propertySources + "}";
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\env\AbstractEnvironment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */