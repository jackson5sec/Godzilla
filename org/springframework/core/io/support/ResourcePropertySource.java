/*     */ package org.springframework.core.io.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import org.springframework.core.env.PropertiesPropertySource;
/*     */ import org.springframework.core.io.DefaultResourceLoader;
/*     */ import org.springframework.core.io.Resource;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResourcePropertySource
/*     */   extends PropertiesPropertySource
/*     */ {
/*     */   @Nullable
/*     */   private final String resourceName;
/*     */   
/*     */   public ResourcePropertySource(String name, EncodedResource resource) throws IOException {
/*  57 */     super(name, PropertiesLoaderUtils.loadProperties(resource));
/*  58 */     this.resourceName = getNameForResource(resource.getResource());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourcePropertySource(EncodedResource resource) throws IOException {
/*  67 */     super(getNameForResource(resource.getResource()), PropertiesLoaderUtils.loadProperties(resource));
/*  68 */     this.resourceName = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourcePropertySource(String name, Resource resource) throws IOException {
/*  76 */     super(name, PropertiesLoaderUtils.loadProperties(new EncodedResource(resource)));
/*  77 */     this.resourceName = getNameForResource(resource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourcePropertySource(Resource resource) throws IOException {
/*  86 */     super(getNameForResource(resource), PropertiesLoaderUtils.loadProperties(new EncodedResource(resource)));
/*  87 */     this.resourceName = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourcePropertySource(String name, String location, ClassLoader classLoader) throws IOException {
/*  96 */     this(name, (new DefaultResourceLoader(classLoader)).getResource(location));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourcePropertySource(String location, ClassLoader classLoader) throws IOException {
/* 107 */     this((new DefaultResourceLoader(classLoader)).getResource(location));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourcePropertySource(String name, String location) throws IOException {
/* 117 */     this(name, (new DefaultResourceLoader()).getResource(location));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourcePropertySource(String location) throws IOException {
/* 126 */     this((new DefaultResourceLoader()).getResource(location));
/*     */   }
/*     */   
/*     */   private ResourcePropertySource(String name, @Nullable String resourceName, Map<String, Object> source) {
/* 130 */     super(name, source);
/* 131 */     this.resourceName = resourceName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourcePropertySource withName(String name) {
/* 141 */     if (this.name.equals(name)) {
/* 142 */       return this;
/*     */     }
/*     */     
/* 145 */     if (this.resourceName != null) {
/* 146 */       if (this.resourceName.equals(name)) {
/* 147 */         return new ResourcePropertySource(this.resourceName, null, (Map<String, Object>)this.source);
/*     */       }
/*     */       
/* 150 */       return new ResourcePropertySource(name, this.resourceName, (Map<String, Object>)this.source);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 155 */     return new ResourcePropertySource(name, this.name, (Map<String, Object>)this.source);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourcePropertySource withResourceName() {
/* 166 */     if (this.resourceName == null) {
/* 167 */       return this;
/*     */     }
/* 169 */     return new ResourcePropertySource(this.resourceName, null, (Map<String, Object>)this.source);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getNameForResource(Resource resource) {
/* 179 */     String name = resource.getDescription();
/* 180 */     if (!StringUtils.hasText(name)) {
/* 181 */       name = resource.getClass().getSimpleName() + "@" + System.identityHashCode(resource);
/*     */     }
/* 183 */     return name;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\io\support\ResourcePropertySource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */