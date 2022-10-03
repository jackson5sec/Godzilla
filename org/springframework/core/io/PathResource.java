/*     */ package org.springframework.core.io;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.NoSuchFileException;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.StandardOpenOption;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PathResource
/*     */   extends AbstractResource
/*     */   implements WritableResource
/*     */ {
/*     */   private final Path path;
/*     */   
/*     */   public PathResource(Path path) {
/*  71 */     Assert.notNull(path, "Path must not be null");
/*  72 */     this.path = path.normalize();
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
/*     */   public PathResource(String path) {
/*  84 */     Assert.notNull(path, "Path must not be null");
/*  85 */     this.path = Paths.get(path, new String[0]).normalize();
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
/*     */   public PathResource(URI uri) {
/*  97 */     Assert.notNull(uri, "URI must not be null");
/*  98 */     this.path = Paths.get(uri).normalize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getPath() {
/* 106 */     return this.path.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean exists() {
/* 115 */     return Files.exists(this.path, new java.nio.file.LinkOption[0]);
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
/* 126 */     return (Files.isReadable(this.path) && !Files.isDirectory(this.path, new java.nio.file.LinkOption[0]));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getInputStream() throws IOException {
/* 135 */     if (!exists()) {
/* 136 */       throw new FileNotFoundException(getPath() + " (no such file or directory)");
/*     */     }
/* 138 */     if (Files.isDirectory(this.path, new java.nio.file.LinkOption[0])) {
/* 139 */       throw new FileNotFoundException(getPath() + " (is a directory)");
/*     */     }
/* 141 */     return Files.newInputStream(this.path, new OpenOption[0]);
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
/* 152 */     return (Files.isWritable(this.path) && !Files.isDirectory(this.path, new java.nio.file.LinkOption[0]));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OutputStream getOutputStream() throws IOException {
/* 161 */     if (Files.isDirectory(this.path, new java.nio.file.LinkOption[0])) {
/* 162 */       throw new FileNotFoundException(getPath() + " (is a directory)");
/*     */     }
/* 164 */     return Files.newOutputStream(this.path, new OpenOption[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URL getURL() throws IOException {
/* 174 */     return this.path.toUri().toURL();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI getURI() throws IOException {
/* 183 */     return this.path.toUri();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFile() {
/* 191 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getFile() throws IOException {
/*     */     try {
/* 200 */       return this.path.toFile();
/*     */     }
/* 202 */     catch (UnsupportedOperationException ex) {
/*     */ 
/*     */       
/* 205 */       throw new FileNotFoundException(this.path + " cannot be resolved to absolute file path");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReadableByteChannel readableChannel() throws IOException {
/*     */     try {
/* 216 */       return Files.newByteChannel(this.path, new OpenOption[] { StandardOpenOption.READ });
/*     */     }
/* 218 */     catch (NoSuchFileException ex) {
/* 219 */       throw new FileNotFoundException(ex.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WritableByteChannel writableChannel() throws IOException {
/* 229 */     return Files.newByteChannel(this.path, new OpenOption[] { StandardOpenOption.WRITE });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long contentLength() throws IOException {
/* 237 */     return Files.size(this.path);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long lastModified() throws IOException {
/* 248 */     return Files.getLastModifiedTime(this.path, new java.nio.file.LinkOption[0]).toMillis();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Resource createRelative(String relativePath) {
/* 258 */     return new PathResource(this.path.resolve(relativePath));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFilename() {
/* 267 */     return this.path.getFileName().toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 272 */     return "path [" + this.path.toAbsolutePath() + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 281 */     return (this == other || (other instanceof PathResource && this.path
/* 282 */       .equals(((PathResource)other).path)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 290 */     return this.path.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\io\PathResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */