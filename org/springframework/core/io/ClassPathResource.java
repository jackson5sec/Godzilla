/*     */ package org.springframework.core.io;
/*     */ 
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public class ClassPathResource
/*     */   extends AbstractFileResolvingResource
/*     */ {
/*     */   private final String path;
/*     */   @Nullable
/*     */   private ClassLoader classLoader;
/*     */   @Nullable
/*     */   private Class<?> clazz;
/*     */   
/*     */   public ClassPathResource(String path) {
/*  66 */     this(path, (ClassLoader)null);
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
/*     */   public ClassPathResource(String path, @Nullable ClassLoader classLoader) {
/*  79 */     Assert.notNull(path, "Path must not be null");
/*  80 */     String pathToUse = StringUtils.cleanPath(path);
/*  81 */     if (pathToUse.startsWith("/")) {
/*  82 */       pathToUse = pathToUse.substring(1);
/*     */     }
/*  84 */     this.path = pathToUse;
/*  85 */     this.classLoader = (classLoader != null) ? classLoader : ClassUtils.getDefaultClassLoader();
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
/*     */   public ClassPathResource(String path, @Nullable Class<?> clazz) {
/*  97 */     Assert.notNull(path, "Path must not be null");
/*  98 */     this.path = StringUtils.cleanPath(path);
/*  99 */     this.clazz = clazz;
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
/*     */   @Deprecated
/*     */   protected ClassPathResource(String path, @Nullable ClassLoader classLoader, @Nullable Class<?> clazz) {
/* 113 */     this.path = StringUtils.cleanPath(path);
/* 114 */     this.classLoader = classLoader;
/* 115 */     this.clazz = clazz;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getPath() {
/* 123 */     return this.path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final ClassLoader getClassLoader() {
/* 131 */     return (this.clazz != null) ? this.clazz.getClassLoader() : this.classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean exists() {
/* 142 */     return (resolveURL() != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReadable() {
/* 153 */     URL url = resolveURL();
/* 154 */     return (url != null && checkReadable(url));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected URL resolveURL() {
/*     */     try {
/* 164 */       if (this.clazz != null) {
/* 165 */         return this.clazz.getResource(this.path);
/*     */       }
/* 167 */       if (this.classLoader != null) {
/* 168 */         return this.classLoader.getResource(this.path);
/*     */       }
/*     */       
/* 171 */       return ClassLoader.getSystemResource(this.path);
/*     */     
/*     */     }
/* 174 */     catch (IllegalArgumentException ex) {
/*     */ 
/*     */       
/* 177 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getInputStream() throws IOException {
/*     */     InputStream is;
/* 189 */     if (this.clazz != null) {
/* 190 */       is = this.clazz.getResourceAsStream(this.path);
/*     */     }
/* 192 */     else if (this.classLoader != null) {
/* 193 */       is = this.classLoader.getResourceAsStream(this.path);
/*     */     } else {
/*     */       
/* 196 */       is = ClassLoader.getSystemResourceAsStream(this.path);
/*     */     } 
/* 198 */     if (is == null) {
/* 199 */       throw new FileNotFoundException(getDescription() + " cannot be opened because it does not exist");
/*     */     }
/* 201 */     return is;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URL getURL() throws IOException {
/* 212 */     URL url = resolveURL();
/* 213 */     if (url == null) {
/* 214 */       throw new FileNotFoundException(getDescription() + " cannot be resolved to URL because it does not exist");
/*     */     }
/* 216 */     return url;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Resource createRelative(String relativePath) {
/* 226 */     String pathToUse = StringUtils.applyRelativePath(this.path, relativePath);
/* 227 */     return (this.clazz != null) ? new ClassPathResource(pathToUse, this.clazz) : new ClassPathResource(pathToUse, this.classLoader);
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
/*     */   public String getFilename() {
/* 239 */     return StringUtils.getFilename(this.path);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 247 */     StringBuilder builder = new StringBuilder("class path resource [");
/* 248 */     String pathToUse = this.path;
/* 249 */     if (this.clazz != null && !pathToUse.startsWith("/")) {
/* 250 */       builder.append(ClassUtils.classPackageAsResourcePath(this.clazz));
/* 251 */       builder.append('/');
/*     */     } 
/* 253 */     if (pathToUse.startsWith("/")) {
/* 254 */       pathToUse = pathToUse.substring(1);
/*     */     }
/* 256 */     builder.append(pathToUse);
/* 257 */     builder.append(']');
/* 258 */     return builder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 267 */     if (this == other) {
/* 268 */       return true;
/*     */     }
/* 270 */     if (!(other instanceof ClassPathResource)) {
/* 271 */       return false;
/*     */     }
/* 273 */     ClassPathResource otherRes = (ClassPathResource)other;
/* 274 */     return (this.path.equals(otherRes.path) && 
/* 275 */       ObjectUtils.nullSafeEquals(this.classLoader, otherRes.classLoader) && 
/* 276 */       ObjectUtils.nullSafeEquals(this.clazz, otherRes.clazz));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 285 */     return this.path.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\io\ClassPathResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */