/*     */ package org.springframework.core.io;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import org.springframework.core.NestedIOException;
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
/*     */ public class VfsResource
/*     */   extends AbstractResource
/*     */ {
/*     */   private final Object resource;
/*     */   
/*     */   public VfsResource(Object resource) {
/*  54 */     Assert.notNull(resource, "VirtualFile must not be null");
/*  55 */     this.resource = resource;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getInputStream() throws IOException {
/*  61 */     return VfsUtils.getInputStream(this.resource);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean exists() {
/*  66 */     return VfsUtils.exists(this.resource);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isReadable() {
/*  71 */     return VfsUtils.isReadable(this.resource);
/*     */   }
/*     */ 
/*     */   
/*     */   public URL getURL() throws IOException {
/*     */     try {
/*  77 */       return VfsUtils.getURL(this.resource);
/*     */     }
/*  79 */     catch (Exception ex) {
/*  80 */       throw new NestedIOException("Failed to obtain URL for file " + this.resource, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public URI getURI() throws IOException {
/*     */     try {
/*  87 */       return VfsUtils.getURI(this.resource);
/*     */     }
/*  89 */     catch (Exception ex) {
/*  90 */       throw new NestedIOException("Failed to obtain URI for " + this.resource, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public File getFile() throws IOException {
/*  96 */     return VfsUtils.getFile(this.resource);
/*     */   }
/*     */ 
/*     */   
/*     */   public long contentLength() throws IOException {
/* 101 */     return VfsUtils.getSize(this.resource);
/*     */   }
/*     */ 
/*     */   
/*     */   public long lastModified() throws IOException {
/* 106 */     return VfsUtils.getLastModified(this.resource);
/*     */   }
/*     */ 
/*     */   
/*     */   public Resource createRelative(String relativePath) throws IOException {
/* 111 */     if (!relativePath.startsWith(".") && relativePath.contains("/")) {
/*     */       try {
/* 113 */         return new VfsResource(VfsUtils.getChild(this.resource, relativePath));
/*     */       }
/* 115 */       catch (IOException iOException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 120 */     return new VfsResource(VfsUtils.getRelative(new URL(getURL(), relativePath)));
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFilename() {
/* 125 */     return VfsUtils.getName(this.resource);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 130 */     return "VFS resource [" + this.resource + "]";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 135 */     return (this == other || (other instanceof VfsResource && this.resource
/* 136 */       .equals(((VfsResource)other).resource)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 141 */     return this.resource.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\io\VfsResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */