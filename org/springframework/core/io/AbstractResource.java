/*     */ package org.springframework.core.io;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.nio.channels.Channels;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.NestedIOException;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ResourceUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractResource
/*     */   implements Resource
/*     */ {
/*     */   public boolean exists() {
/*  58 */     if (isFile()) {
/*     */       try {
/*  60 */         return getFile().exists();
/*     */       }
/*  62 */       catch (IOException ex) {
/*  63 */         Log logger = LogFactory.getLog(getClass());
/*  64 */         if (logger.isDebugEnabled()) {
/*  65 */           logger.debug("Could not retrieve File for existence check of " + getDescription(), ex);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     try {
/*  71 */       getInputStream().close();
/*  72 */       return true;
/*     */     }
/*  74 */     catch (Throwable ex) {
/*  75 */       Log logger = LogFactory.getLog(getClass());
/*  76 */       if (logger.isDebugEnabled()) {
/*  77 */         logger.debug("Could not retrieve InputStream for existence check of " + getDescription(), ex);
/*     */       }
/*  79 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReadable() {
/*  89 */     return exists();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/*  97 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFile() {
/* 105 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URL getURL() throws IOException {
/* 114 */     throw new FileNotFoundException(getDescription() + " cannot be resolved to URL");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI getURI() throws IOException {
/* 123 */     URL url = getURL();
/*     */     try {
/* 125 */       return ResourceUtils.toURI(url);
/*     */     }
/* 127 */     catch (URISyntaxException ex) {
/* 128 */       throw new NestedIOException("Invalid URI [" + url + "]", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getFile() throws IOException {
/* 138 */     throw new FileNotFoundException(getDescription() + " cannot be resolved to absolute file path");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReadableByteChannel readableChannel() throws IOException {
/* 149 */     return Channels.newChannel(getInputStream());
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
/*     */   public long contentLength() throws IOException {
/* 162 */     InputStream is = getInputStream();
/*     */     try {
/* 164 */       long size = 0L;
/* 165 */       byte[] buf = new byte[256];
/*     */       int read;
/* 167 */       while ((read = is.read(buf)) != -1) {
/* 168 */         size += read;
/*     */       }
/* 170 */       return size;
/*     */     } finally {
/*     */       
/*     */       try {
/* 174 */         is.close();
/*     */       }
/* 176 */       catch (IOException ex) {
/* 177 */         Log logger = LogFactory.getLog(getClass());
/* 178 */         if (logger.isDebugEnabled()) {
/* 179 */           logger.debug("Could not close content-length InputStream for " + getDescription(), ex);
/*     */         }
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
/*     */   public long lastModified() throws IOException {
/* 192 */     File fileToCheck = getFileForLastModifiedCheck();
/* 193 */     long lastModified = fileToCheck.lastModified();
/* 194 */     if (lastModified == 0L && !fileToCheck.exists()) {
/* 195 */       throw new FileNotFoundException(getDescription() + " cannot be resolved in the file system for checking its last-modified timestamp");
/*     */     }
/*     */     
/* 198 */     return lastModified;
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
/*     */   protected File getFileForLastModifiedCheck() throws IOException {
/* 210 */     return getFile();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Resource createRelative(String relativePath) throws IOException {
/* 219 */     throw new FileNotFoundException("Cannot create a relative resource for " + getDescription());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getFilename() {
/* 229 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 239 */     return (this == other || (other instanceof Resource && ((Resource)other)
/* 240 */       .getDescription().equals(getDescription())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 249 */     return getDescription().hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 258 */     return getDescription();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\io\AbstractResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */