/*     */ package org.springframework.core.io;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.StandardOpenOption;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileUrlResource
/*     */   extends UrlResource
/*     */   implements WritableResource
/*     */ {
/*     */   @Nullable
/*     */   private volatile File file;
/*     */   
/*     */   public FileUrlResource(URL url) {
/*  61 */     super(url);
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
/*     */   public FileUrlResource(String location) throws MalformedURLException {
/*  74 */     super("file", location);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public File getFile() throws IOException {
/*  80 */     File file = this.file;
/*  81 */     if (file != null) {
/*  82 */       return file;
/*     */     }
/*  84 */     file = super.getFile();
/*  85 */     this.file = file;
/*  86 */     return file;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWritable() {
/*     */     try {
/*  92 */       File file = getFile();
/*  93 */       return (file.canWrite() && !file.isDirectory());
/*     */     }
/*  95 */     catch (IOException ex) {
/*  96 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public OutputStream getOutputStream() throws IOException {
/* 102 */     return Files.newOutputStream(getFile().toPath(), new OpenOption[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   public WritableByteChannel writableChannel() throws IOException {
/* 107 */     return FileChannel.open(getFile().toPath(), new OpenOption[] { StandardOpenOption.WRITE });
/*     */   }
/*     */ 
/*     */   
/*     */   public Resource createRelative(String relativePath) throws MalformedURLException {
/* 112 */     return new FileUrlResource(createRelativeURL(relativePath));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\io\FileUrlResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */