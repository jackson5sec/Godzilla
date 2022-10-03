/*     */ package org.springframework.core.type.classreading;
/*     */ 
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import org.springframework.core.io.DefaultResourceLoader;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleMetadataReaderFactory
/*     */   implements MetadataReaderFactory
/*     */ {
/*     */   private final ResourceLoader resourceLoader;
/*     */   
/*     */   public SimpleMetadataReaderFactory() {
/*  44 */     this.resourceLoader = (ResourceLoader)new DefaultResourceLoader();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleMetadataReaderFactory(@Nullable ResourceLoader resourceLoader) {
/*  53 */     this.resourceLoader = (resourceLoader != null) ? resourceLoader : (ResourceLoader)new DefaultResourceLoader();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleMetadataReaderFactory(@Nullable ClassLoader classLoader) {
/*  61 */     this.resourceLoader = (classLoader != null) ? (ResourceLoader)new DefaultResourceLoader(classLoader) : (ResourceLoader)new DefaultResourceLoader();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ResourceLoader getResourceLoader() {
/*  71 */     return this.resourceLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MetadataReader getMetadataReader(String className) throws IOException {
/*     */     try {
/*  79 */       String resourcePath = "classpath:" + ClassUtils.convertClassNameToResourcePath(className) + ".class";
/*  80 */       Resource resource = this.resourceLoader.getResource(resourcePath);
/*  81 */       return getMetadataReader(resource);
/*     */     }
/*  83 */     catch (FileNotFoundException ex) {
/*     */ 
/*     */       
/*  86 */       int lastDotIndex = className.lastIndexOf('.');
/*  87 */       if (lastDotIndex != -1) {
/*     */         
/*  89 */         String innerClassName = className.substring(0, lastDotIndex) + '$' + className.substring(lastDotIndex + 1);
/*     */         
/*  91 */         String innerClassResourcePath = "classpath:" + ClassUtils.convertClassNameToResourcePath(innerClassName) + ".class";
/*  92 */         Resource innerClassResource = this.resourceLoader.getResource(innerClassResourcePath);
/*  93 */         if (innerClassResource.exists()) {
/*  94 */           return getMetadataReader(innerClassResource);
/*     */         }
/*     */       } 
/*  97 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public MetadataReader getMetadataReader(Resource resource) throws IOException {
/* 103 */     return new SimpleMetadataReader(resource, this.resourceLoader.getClassLoader());
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\type\classreading\SimpleMetadataReaderFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */