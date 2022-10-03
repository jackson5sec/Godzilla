/*     */ package org.springframework.core.io;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.NoSuchFileException;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileSystemResource
/*     */   extends AbstractResource
/*     */   implements WritableResource
/*     */ {
/*     */   private final String path;
/*     */   @Nullable
/*     */   private final File file;
/*     */   private final Path filePath;
/*     */   
/*     */   public FileSystemResource(String path) {
/*  80 */     Assert.notNull(path, "Path must not be null");
/*  81 */     this.path = StringUtils.cleanPath(path);
/*  82 */     this.file = new File(path);
/*  83 */     this.filePath = this.file.toPath();
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
/*     */ 
/*     */ 
/*     */   
/*     */   public FileSystemResource(File file) {
/* 100 */     Assert.notNull(file, "File must not be null");
/* 101 */     this.path = StringUtils.cleanPath(file.getPath());
/* 102 */     this.file = file;
/* 103 */     this.filePath = file.toPath();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileSystemResource(Path filePath) {
/* 126 */     Assert.notNull(filePath, "Path must not be null");
/* 127 */     this.path = StringUtils.cleanPath(filePath.toString());
/* 128 */     this.file = null;
/* 129 */     this.filePath = filePath;
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
/*     */   public FileSystemResource(FileSystem fileSystem, String path) {
/* 143 */     Assert.notNull(fileSystem, "FileSystem must not be null");
/* 144 */     Assert.notNull(path, "Path must not be null");
/* 145 */     this.path = StringUtils.cleanPath(path);
/* 146 */     this.file = null;
/* 147 */     this.filePath = fileSystem.getPath(this.path, new String[0]).normalize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getPath() {
/* 155 */     return this.path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean exists() {
/* 164 */     return (this.file != null) ? this.file.exists() : Files.exists(this.filePath, new java.nio.file.LinkOption[0]);
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
/* 175 */     return (this.file != null) ? ((this.file.canRead() && !this.file.isDirectory())) : ((
/* 176 */       Files.isReadable(this.filePath) && !Files.isDirectory(this.filePath, new java.nio.file.LinkOption[0])));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getInputStream() throws IOException {
/*     */     try {
/* 186 */       return Files.newInputStream(this.filePath, new OpenOption[0]);
/*     */     }
/* 188 */     catch (NoSuchFileException ex) {
/* 189 */       throw new FileNotFoundException(ex.getMessage());
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
/*     */   public boolean isWritable() {
/* 201 */     return (this.file != null) ? ((this.file.canWrite() && !this.file.isDirectory())) : ((
/* 202 */       Files.isWritable(this.filePath) && !Files.isDirectory(this.filePath, new java.nio.file.LinkOption[0])));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OutputStream getOutputStream() throws IOException {
/* 211 */     return Files.newOutputStream(this.filePath, new OpenOption[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URL getURL() throws IOException {
/* 220 */     return (this.file != null) ? this.file.toURI().toURL() : this.filePath.toUri().toURL();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI getURI() throws IOException {
/* 229 */     return (this.file != null) ? this.file.toURI() : this.filePath.toUri();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFile() {
/* 237 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getFile() {
/* 245 */     return (this.file != null) ? this.file : this.filePath.toFile();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReadableByteChannel readableChannel() throws IOException {
/*     */     try {
/* 255 */       return FileChannel.open(this.filePath, new OpenOption[] { StandardOpenOption.READ });
/*     */     }
/* 257 */     catch (NoSuchFileException ex) {
/* 258 */       throw new FileNotFoundException(ex.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WritableByteChannel writableChannel() throws IOException {
/* 268 */     return FileChannel.open(this.filePath, new OpenOption[] { StandardOpenOption.WRITE });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long contentLength() throws IOException {
/* 276 */     if (this.file != null) {
/* 277 */       long length = this.file.length();
/* 278 */       if (length == 0L && !this.file.exists()) {
/* 279 */         throw new FileNotFoundException(getDescription() + " cannot be resolved in the file system for checking its content length");
/*     */       }
/*     */       
/* 282 */       return length;
/*     */     } 
/*     */     
/*     */     try {
/* 286 */       return Files.size(this.filePath);
/*     */     }
/* 288 */     catch (NoSuchFileException ex) {
/* 289 */       throw new FileNotFoundException(ex.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long lastModified() throws IOException {
/* 299 */     if (this.file != null) {
/* 300 */       return super.lastModified();
/*     */     }
/*     */     
/*     */     try {
/* 304 */       return Files.getLastModifiedTime(this.filePath, new java.nio.file.LinkOption[0]).toMillis();
/*     */     }
/* 306 */     catch (NoSuchFileException ex) {
/* 307 */       throw new FileNotFoundException(ex.getMessage());
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
/*     */   public Resource createRelative(String relativePath) {
/* 319 */     String pathToUse = StringUtils.applyRelativePath(this.path, relativePath);
/* 320 */     return (this.file != null) ? new FileSystemResource(pathToUse) : new FileSystemResource(this.filePath
/* 321 */         .getFileSystem(), pathToUse);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFilename() {
/* 330 */     return (this.file != null) ? this.file.getName() : this.filePath.getFileName().toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 340 */     return "file [" + ((this.file != null) ? this.file.getAbsolutePath() : (String)this.filePath.toAbsolutePath()) + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 349 */     return (this == other || (other instanceof FileSystemResource && this.path
/* 350 */       .equals(((FileSystemResource)other).path)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 358 */     return this.path.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\io\FileSystemResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */