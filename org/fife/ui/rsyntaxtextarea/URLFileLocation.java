/*     */ package org.fife.ui.rsyntaxtextarea;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class URLFileLocation
/*     */   extends FileLocation
/*     */ {
/*     */   private URL url;
/*     */   private String fileFullPath;
/*     */   private String fileName;
/*     */   
/*     */   URLFileLocation(URL url) {
/*  48 */     this.url = url;
/*  49 */     this.fileFullPath = createFileFullPath();
/*  50 */     this.fileName = createFileName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String createFileFullPath() {
/*  61 */     String fullPath = this.url.toString();
/*  62 */     fullPath = fullPath.replaceFirst("://([^:]+)(?:.+)@", "://$1@");
/*  63 */     return fullPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String createFileName() {
/*  73 */     String fileName = this.url.getPath();
/*  74 */     if (fileName.startsWith("/%2F/")) {
/*  75 */       fileName = fileName.substring(4);
/*     */     }
/*  77 */     else if (fileName.startsWith("/")) {
/*  78 */       fileName = fileName.substring(1);
/*     */     } 
/*  80 */     return fileName;
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
/*     */   protected long getActualLastModified() {
/*  94 */     return 0L;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileFullPath() {
/* 100 */     return this.fileFullPath;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/* 106 */     return this.fileName;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected InputStream getInputStream() throws IOException {
/* 112 */     return this.url.openStream();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected OutputStream getOutputStream() throws IOException {
/* 118 */     return this.url.openConnection().getOutputStream();
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
/*     */   public boolean isLocal() {
/* 130 */     return "file".equalsIgnoreCase(this.url.getProtocol());
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
/*     */   public boolean isLocalAndExists() {
/* 144 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\URLFileLocation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */