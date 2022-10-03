/*     */ package org.springframework.core.type.classreading;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.core.io.DefaultResourceLoader;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CachingMetadataReaderFactory
/*     */   extends SimpleMetadataReaderFactory
/*     */ {
/*     */   public static final int DEFAULT_CACHE_LIMIT = 256;
/*     */   @Nullable
/*     */   private Map<Resource, MetadataReader> metadataReaderCache;
/*     */   
/*     */   public CachingMetadataReaderFactory() {
/*  54 */     setCacheLimit(256);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CachingMetadataReaderFactory(@Nullable ClassLoader classLoader) {
/*  63 */     super(classLoader);
/*  64 */     setCacheLimit(256);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CachingMetadataReaderFactory(@Nullable ResourceLoader resourceLoader) {
/*  75 */     super(resourceLoader);
/*  76 */     if (resourceLoader instanceof DefaultResourceLoader) {
/*  77 */       this
/*  78 */         .metadataReaderCache = ((DefaultResourceLoader)resourceLoader).getResourceCache(MetadataReader.class);
/*     */     } else {
/*     */       
/*  81 */       setCacheLimit(256);
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
/*     */   public void setCacheLimit(int cacheLimit) {
/*  93 */     if (cacheLimit <= 0) {
/*  94 */       this.metadataReaderCache = null;
/*     */     }
/*  96 */     else if (this.metadataReaderCache instanceof LocalResourceCache) {
/*  97 */       ((LocalResourceCache)this.metadataReaderCache).setCacheLimit(cacheLimit);
/*     */     } else {
/*     */       
/* 100 */       this.metadataReaderCache = new LocalResourceCache(cacheLimit);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCacheLimit() {
/* 108 */     if (this.metadataReaderCache instanceof LocalResourceCache) {
/* 109 */       return ((LocalResourceCache)this.metadataReaderCache).getCacheLimit();
/*     */     }
/*     */     
/* 112 */     return (this.metadataReaderCache != null) ? Integer.MAX_VALUE : 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MetadataReader getMetadataReader(Resource resource) throws IOException {
/* 119 */     if (this.metadataReaderCache instanceof java.util.concurrent.ConcurrentMap) {
/*     */       
/* 121 */       MetadataReader metadataReader = this.metadataReaderCache.get(resource);
/* 122 */       if (metadataReader == null) {
/* 123 */         metadataReader = super.getMetadataReader(resource);
/* 124 */         this.metadataReaderCache.put(resource, metadataReader);
/*     */       } 
/* 126 */       return metadataReader;
/*     */     } 
/* 128 */     if (this.metadataReaderCache != null) {
/* 129 */       synchronized (this.metadataReaderCache) {
/* 130 */         MetadataReader metadataReader = this.metadataReaderCache.get(resource);
/* 131 */         if (metadataReader == null) {
/* 132 */           metadataReader = super.getMetadataReader(resource);
/* 133 */           this.metadataReaderCache.put(resource, metadataReader);
/*     */         } 
/* 135 */         return metadataReader;
/*     */       } 
/*     */     }
/*     */     
/* 139 */     return super.getMetadataReader(resource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearCache() {
/* 147 */     if (this.metadataReaderCache instanceof LocalResourceCache) {
/* 148 */       synchronized (this.metadataReaderCache) {
/* 149 */         this.metadataReaderCache.clear();
/*     */       }
/*     */     
/* 152 */     } else if (this.metadataReaderCache != null) {
/*     */       
/* 154 */       setCacheLimit(256);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static class LocalResourceCache
/*     */     extends LinkedHashMap<Resource, MetadataReader>
/*     */   {
/*     */     private volatile int cacheLimit;
/*     */     
/*     */     public LocalResourceCache(int cacheLimit) {
/* 165 */       super(cacheLimit, 0.75F, true);
/* 166 */       this.cacheLimit = cacheLimit;
/*     */     }
/*     */     
/*     */     public void setCacheLimit(int cacheLimit) {
/* 170 */       this.cacheLimit = cacheLimit;
/*     */     }
/*     */     
/*     */     public int getCacheLimit() {
/* 174 */       return this.cacheLimit;
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean removeEldestEntry(Map.Entry<Resource, MetadataReader> eldest) {
/* 179 */       return (size() > this.cacheLimit);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\type\classreading\CachingMetadataReaderFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */