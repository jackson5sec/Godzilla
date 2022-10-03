/*     */ package org.fife.ui.rsyntaxtextarea;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class FileLocation
/*     */ {
/*     */   public static FileLocation create(String fileFullPath) {
/*  36 */     if (fileFullPath.startsWith("http://") || fileFullPath
/*  37 */       .startsWith("https://") || fileFullPath
/*  38 */       .startsWith("ftp://")) {
/*     */       try {
/*  40 */         return new URLFileLocation(new URL(fileFullPath));
/*  41 */       } catch (MalformedURLException mue) {
/*  42 */         throw new IllegalArgumentException("Not a valid URL: " + fileFullPath, mue);
/*     */       } 
/*     */     }
/*     */     
/*  46 */     return new FileFileLocation(new File(fileFullPath));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static FileLocation create(File file) {
/*  57 */     return new FileFileLocation(file);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static FileLocation create(URL url) {
/*  68 */     if ("file".equalsIgnoreCase(url.getProtocol())) {
/*  69 */       return new FileFileLocation(new File(url.getPath()));
/*     */     }
/*  71 */     return new URLFileLocation(url);
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
/*     */   protected abstract long getActualLastModified();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String getFileFullPath();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String getFileName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract InputStream getInputStream() throws IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract OutputStream getOutputStream() throws IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean isLocal();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean isLocalAndExists();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRemote() {
/* 148 */     return !isLocal();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\FileLocation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */