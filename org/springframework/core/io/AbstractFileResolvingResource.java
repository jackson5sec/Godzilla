/*     */ package org.springframework.core.io;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.StandardOpenOption;
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
/*     */ public abstract class AbstractFileResolvingResource
/*     */   extends AbstractResource
/*     */ {
/*     */   public boolean exists() {
/*     */     try {
/*  48 */       URL url = getURL();
/*  49 */       if (ResourceUtils.isFileURL(url))
/*     */       {
/*  51 */         return getFile().exists();
/*     */       }
/*     */ 
/*     */       
/*  55 */       URLConnection con = url.openConnection();
/*  56 */       customizeConnection(con);
/*  57 */       HttpURLConnection httpCon = (con instanceof HttpURLConnection) ? (HttpURLConnection)con : null;
/*     */       
/*  59 */       if (httpCon != null) {
/*  60 */         int code = httpCon.getResponseCode();
/*  61 */         if (code == 200) {
/*  62 */           return true;
/*     */         }
/*  64 */         if (code == 404) {
/*  65 */           return false;
/*     */         }
/*     */       } 
/*  68 */       if (con.getContentLengthLong() > 0L) {
/*  69 */         return true;
/*     */       }
/*  71 */       if (httpCon != null) {
/*     */         
/*  73 */         httpCon.disconnect();
/*  74 */         return false;
/*     */       } 
/*     */ 
/*     */       
/*  78 */       getInputStream().close();
/*  79 */       return true;
/*     */ 
/*     */     
/*     */     }
/*  83 */     catch (IOException ex) {
/*  84 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isReadable() {
/*     */     try {
/*  91 */       return checkReadable(getURL());
/*     */     }
/*  93 */     catch (IOException ex) {
/*  94 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   boolean checkReadable(URL url) {
/*     */     try {
/* 100 */       if (ResourceUtils.isFileURL(url)) {
/*     */         
/* 102 */         File file = getFile();
/* 103 */         return (file.canRead() && !file.isDirectory());
/*     */       } 
/*     */ 
/*     */       
/* 107 */       URLConnection con = url.openConnection();
/* 108 */       customizeConnection(con);
/* 109 */       if (con instanceof HttpURLConnection) {
/* 110 */         HttpURLConnection httpCon = (HttpURLConnection)con;
/* 111 */         int code = httpCon.getResponseCode();
/* 112 */         if (code != 200) {
/* 113 */           httpCon.disconnect();
/* 114 */           return false;
/*     */         } 
/*     */       } 
/* 117 */       long contentLength = con.getContentLengthLong();
/* 118 */       if (contentLength > 0L) {
/* 119 */         return true;
/*     */       }
/* 121 */       if (contentLength == 0L)
/*     */       {
/* 123 */         return false;
/*     */       }
/*     */ 
/*     */       
/* 127 */       getInputStream().close();
/* 128 */       return true;
/*     */ 
/*     */     
/*     */     }
/* 132 */     catch (IOException ex) {
/* 133 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFile() {
/*     */     try {
/* 140 */       URL url = getURL();
/* 141 */       if (url.getProtocol().startsWith("vfs")) {
/* 142 */         return VfsResourceDelegate.getResource(url).isFile();
/*     */       }
/* 144 */       return "file".equals(url.getProtocol());
/*     */     }
/* 146 */     catch (IOException ex) {
/* 147 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getFile() throws IOException {
/* 158 */     URL url = getURL();
/* 159 */     if (url.getProtocol().startsWith("vfs")) {
/* 160 */       return VfsResourceDelegate.getResource(url).getFile();
/*     */     }
/* 162 */     return ResourceUtils.getFile(url, getDescription());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected File getFileForLastModifiedCheck() throws IOException {
/* 171 */     URL url = getURL();
/* 172 */     if (ResourceUtils.isJarURL(url)) {
/* 173 */       URL actualUrl = ResourceUtils.extractArchiveURL(url);
/* 174 */       if (actualUrl.getProtocol().startsWith("vfs")) {
/* 175 */         return VfsResourceDelegate.getResource(actualUrl).getFile();
/*     */       }
/* 177 */       return ResourceUtils.getFile(actualUrl, "Jar URL");
/*     */     } 
/*     */     
/* 180 */     return getFile();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isFile(URI uri) {
/*     */     try {
/* 192 */       if (uri.getScheme().startsWith("vfs")) {
/* 193 */         return VfsResourceDelegate.getResource(uri).isFile();
/*     */       }
/* 195 */       return "file".equals(uri.getScheme());
/*     */     }
/* 197 */     catch (IOException ex) {
/* 198 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected File getFile(URI uri) throws IOException {
/* 208 */     if (uri.getScheme().startsWith("vfs")) {
/* 209 */       return VfsResourceDelegate.getResource(uri).getFile();
/*     */     }
/* 211 */     return ResourceUtils.getFile(uri, getDescription());
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
/*     */   public ReadableByteChannel readableChannel() throws IOException {
/*     */     try {
/* 224 */       return FileChannel.open(getFile().toPath(), new OpenOption[] { StandardOpenOption.READ });
/*     */     }
/* 226 */     catch (FileNotFoundException|java.nio.file.NoSuchFileException ex) {
/*     */       
/* 228 */       return super.readableChannel();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public long contentLength() throws IOException {
/* 234 */     URL url = getURL();
/* 235 */     if (ResourceUtils.isFileURL(url)) {
/*     */       
/* 237 */       File file = getFile();
/* 238 */       long length = file.length();
/* 239 */       if (length == 0L && !file.exists()) {
/* 240 */         throw new FileNotFoundException(getDescription() + " cannot be resolved in the file system for checking its content length");
/*     */       }
/*     */       
/* 243 */       return length;
/*     */     } 
/*     */ 
/*     */     
/* 247 */     URLConnection con = url.openConnection();
/* 248 */     customizeConnection(con);
/* 249 */     return con.getContentLengthLong();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long lastModified() throws IOException {
/* 255 */     URL url = getURL();
/* 256 */     boolean fileCheck = false;
/* 257 */     if (ResourceUtils.isFileURL(url) || ResourceUtils.isJarURL(url)) {
/*     */       
/* 259 */       fileCheck = true;
/*     */       try {
/* 261 */         File fileToCheck = getFileForLastModifiedCheck();
/* 262 */         long l = fileToCheck.lastModified();
/* 263 */         if (l > 0L || fileToCheck.exists()) {
/* 264 */           return l;
/*     */         }
/*     */       }
/* 267 */       catch (FileNotFoundException fileNotFoundException) {}
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 272 */     URLConnection con = url.openConnection();
/* 273 */     customizeConnection(con);
/* 274 */     long lastModified = con.getLastModified();
/* 275 */     if (fileCheck && lastModified == 0L && con.getContentLengthLong() <= 0L) {
/* 276 */       throw new FileNotFoundException(getDescription() + " cannot be resolved in the file system for checking its last-modified timestamp");
/*     */     }
/*     */     
/* 279 */     return lastModified;
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
/*     */   protected void customizeConnection(URLConnection con) throws IOException {
/* 292 */     ResourceUtils.useCachesIfNecessary(con);
/* 293 */     if (con instanceof HttpURLConnection) {
/* 294 */       customizeConnection((HttpURLConnection)con);
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
/*     */   protected void customizeConnection(HttpURLConnection con) throws IOException {
/* 306 */     con.setRequestMethod("HEAD");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class VfsResourceDelegate
/*     */   {
/*     */     public static Resource getResource(URL url) throws IOException {
/* 316 */       return new VfsResource(VfsUtils.getRoot(url));
/*     */     }
/*     */     
/*     */     public static Resource getResource(URI uri) throws IOException {
/* 320 */       return new VfsResource(VfsUtils.getRoot(uri));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\io\AbstractFileResolvingResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */