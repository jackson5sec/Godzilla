/*     */ package org.springframework.core.io;
/*     */ 
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ResourceUtils;
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
/*     */ public class DefaultResourceLoader
/*     */   implements ResourceLoader
/*     */ {
/*     */   @Nullable
/*     */   private ClassLoader classLoader;
/*  53 */   private final Set<ProtocolResolver> protocolResolvers = new LinkedHashSet<>(4);
/*     */   
/*  55 */   private final Map<Class<?>, Map<Resource, ?>> resourceCaches = new ConcurrentHashMap<>(4);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultResourceLoader(@Nullable ClassLoader classLoader) {
/*  74 */     this.classLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClassLoader(@Nullable ClassLoader classLoader) {
/*  85 */     this.classLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ClassLoader getClassLoader() {
/*  97 */     return (this.classLoader != null) ? this.classLoader : ClassUtils.getDefaultClassLoader();
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
/*     */   public void addProtocolResolver(ProtocolResolver resolver) {
/* 109 */     Assert.notNull(resolver, "ProtocolResolver must not be null");
/* 110 */     this.protocolResolvers.add(resolver);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<ProtocolResolver> getProtocolResolvers() {
/* 119 */     return this.protocolResolvers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> Map<Resource, T> getResourceCache(Class<T> valueType) {
/* 130 */     return (Map<Resource, T>)this.resourceCaches.computeIfAbsent(valueType, key -> new ConcurrentHashMap<>());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearResourceCaches() {
/* 139 */     this.resourceCaches.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Resource getResource(String location) {
/* 145 */     Assert.notNull(location, "Location must not be null");
/*     */     
/* 147 */     for (ProtocolResolver protocolResolver : getProtocolResolvers()) {
/* 148 */       Resource resource = protocolResolver.resolve(location, this);
/* 149 */       if (resource != null) {
/* 150 */         return resource;
/*     */       }
/*     */     } 
/*     */     
/* 154 */     if (location.startsWith("/")) {
/* 155 */       return getResourceByPath(location);
/*     */     }
/* 157 */     if (location.startsWith("classpath:")) {
/* 158 */       return new ClassPathResource(location.substring("classpath:".length()), getClassLoader());
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 163 */       URL url = new URL(location);
/* 164 */       return ResourceUtils.isFileURL(url) ? new FileUrlResource(url) : new UrlResource(url);
/*     */     }
/* 166 */     catch (MalformedURLException ex) {
/*     */       
/* 168 */       return getResourceByPath(location);
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
/*     */   
/*     */   protected Resource getResourceByPath(String path) {
/* 185 */     return new ClassPathContextResource(path, getClassLoader());
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultResourceLoader() {}
/*     */   
/*     */   protected static class ClassPathContextResource
/*     */     extends ClassPathResource
/*     */     implements ContextResource
/*     */   {
/*     */     public ClassPathContextResource(String path, @Nullable ClassLoader classLoader) {
/* 196 */       super(path, classLoader);
/*     */     }
/*     */ 
/*     */     
/*     */     public String getPathWithinContext() {
/* 201 */       return getPath();
/*     */     }
/*     */ 
/*     */     
/*     */     public Resource createRelative(String relativePath) {
/* 206 */       String pathToUse = StringUtils.applyRelativePath(getPath(), relativePath);
/* 207 */       return new ClassPathContextResource(pathToUse, getClassLoader());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\io\DefaultResourceLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */