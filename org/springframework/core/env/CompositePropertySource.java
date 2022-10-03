/*     */ package org.springframework.core.env;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public class CompositePropertySource
/*     */   extends EnumerablePropertySource<Object>
/*     */ {
/*  45 */   private final Set<PropertySource<?>> propertySources = new LinkedHashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompositePropertySource(String name) {
/*  53 */     super(name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getProperty(String name) {
/*  60 */     for (PropertySource<?> propertySource : this.propertySources) {
/*  61 */       Object candidate = propertySource.getProperty(name);
/*  62 */       if (candidate != null) {
/*  63 */         return candidate;
/*     */       }
/*     */     } 
/*  66 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsProperty(String name) {
/*  71 */     for (PropertySource<?> propertySource : this.propertySources) {
/*  72 */       if (propertySource.containsProperty(name)) {
/*  73 */         return true;
/*     */       }
/*     */     } 
/*  76 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getPropertyNames() {
/*  81 */     Set<String> names = new LinkedHashSet<>();
/*  82 */     for (PropertySource<?> propertySource : this.propertySources) {
/*  83 */       if (!(propertySource instanceof EnumerablePropertySource)) {
/*  84 */         throw new IllegalStateException("Failed to enumerate property names due to non-enumerable property source: " + propertySource);
/*     */       }
/*     */       
/*  87 */       names.addAll(Arrays.asList(((EnumerablePropertySource)propertySource).getPropertyNames()));
/*     */     } 
/*  89 */     return StringUtils.toStringArray(names);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPropertySource(PropertySource<?> propertySource) {
/*  98 */     this.propertySources.add(propertySource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFirstPropertySource(PropertySource<?> propertySource) {
/* 107 */     List<PropertySource<?>> existing = new ArrayList<>(this.propertySources);
/* 108 */     this.propertySources.clear();
/* 109 */     this.propertySources.add(propertySource);
/* 110 */     this.propertySources.addAll(existing);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<PropertySource<?>> getPropertySources() {
/* 118 */     return this.propertySources;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 124 */     return getClass().getSimpleName() + " {name='" + this.name + "', propertySources=" + this.propertySources + "}";
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\env\CompositePropertySource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */