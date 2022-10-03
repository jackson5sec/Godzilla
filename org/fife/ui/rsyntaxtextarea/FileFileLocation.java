/*     */ package org.fife.ui.rsyntaxtextarea;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class FileFileLocation
/*     */   extends FileLocation
/*     */ {
/*     */   private File file;
/*     */   
/*     */   FileFileLocation(File file) {
/*     */     try {
/*  41 */       this.file = file.getCanonicalFile();
/*  42 */     } catch (IOException ioe) {
/*  43 */       this.file = file;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected long getActualLastModified() {
/*  50 */     return this.file.lastModified();
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
/*     */   public String getFileFullPath() {
/*  62 */     return this.file.getAbsolutePath();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/*  68 */     return this.file.getName();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected InputStream getInputStream() throws IOException {
/*  74 */     return new FileInputStream(this.file);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected OutputStream getOutputStream() throws IOException {
/*  80 */     return new FileOutputStream(this.file);
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
/*  92 */     return true;
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
/*     */   public boolean isLocalAndExists() {
/* 105 */     return this.file.exists();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\FileFileLocation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */