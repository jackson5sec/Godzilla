/*     */ package org.springframework.core.io;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ResourceUtils;
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
/*     */ public class UrlResource
/*     */   extends AbstractFileResolvingResource
/*     */ {
/*     */   @Nullable
/*     */   private final URI uri;
/*     */   private final URL url;
/*     */   @Nullable
/*     */   private volatile URL cleanedUrl;
/*     */   
/*     */   public UrlResource(URI uri) throws MalformedURLException {
/*  70 */     Assert.notNull(uri, "URI must not be null");
/*  71 */     this.uri = uri;
/*  72 */     this.url = uri.toURL();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UrlResource(URL url) {
/*  80 */     Assert.notNull(url, "URL must not be null");
/*  81 */     this.uri = null;
/*  82 */     this.url = url;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UrlResource(String path) throws MalformedURLException {
/*  93 */     Assert.notNull(path, "Path must not be null");
/*  94 */     this.uri = null;
/*  95 */     this.url = new URL(path);
/*  96 */     this.cleanedUrl = getCleanedUrl(this.url, path);
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
/*     */   public UrlResource(String protocol, String location) throws MalformedURLException {
/* 110 */     this(protocol, location, null);
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
/*     */   public UrlResource(String protocol, String location, @Nullable String fragment) throws MalformedURLException {
/*     */     try {
/* 127 */       this.uri = new URI(protocol, location, fragment);
/* 128 */       this.url = this.uri.toURL();
/*     */     }
/* 130 */     catch (URISyntaxException ex) {
/* 131 */       MalformedURLException exToThrow = new MalformedURLException(ex.getMessage());
/* 132 */       exToThrow.initCause(ex);
/* 133 */       throw exToThrow;
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
/*     */   
/*     */   private static URL getCleanedUrl(URL originalUrl, String originalPath) {
/* 146 */     String cleanedPath = StringUtils.cleanPath(originalPath);
/* 147 */     if (!cleanedPath.equals(originalPath)) {
/*     */       try {
/* 149 */         return new URL(cleanedPath);
/*     */       }
/* 151 */       catch (MalformedURLException malformedURLException) {}
/*     */     }
/*     */ 
/*     */     
/* 155 */     return originalUrl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private URL getCleanedUrl() {
/* 163 */     URL cleanedUrl = this.cleanedUrl;
/* 164 */     if (cleanedUrl != null) {
/* 165 */       return cleanedUrl;
/*     */     }
/* 167 */     cleanedUrl = getCleanedUrl(this.url, ((this.uri != null) ? this.uri : this.url).toString());
/* 168 */     this.cleanedUrl = cleanedUrl;
/* 169 */     return cleanedUrl;
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
/*     */   public InputStream getInputStream() throws IOException {
/* 183 */     URLConnection con = this.url.openConnection();
/* 184 */     ResourceUtils.useCachesIfNecessary(con);
/*     */     try {
/* 186 */       return con.getInputStream();
/*     */     }
/* 188 */     catch (IOException ex) {
/*     */       
/* 190 */       if (con instanceof HttpURLConnection) {
/* 191 */         ((HttpURLConnection)con).disconnect();
/*     */       }
/* 193 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URL getURL() {
/* 202 */     return this.url;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI getURI() throws IOException {
/* 211 */     if (this.uri != null) {
/* 212 */       return this.uri;
/*     */     }
/*     */     
/* 215 */     return super.getURI();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFile() {
/* 221 */     if (this.uri != null) {
/* 222 */       return isFile(this.uri);
/*     */     }
/*     */     
/* 225 */     return super.isFile();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getFile() throws IOException {
/* 236 */     if (this.uri != null) {
/* 237 */       return getFile(this.uri);
/*     */     }
/*     */     
/* 240 */     return super.getFile();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Resource createRelative(String relativePath) throws MalformedURLException {
/* 251 */     return new UrlResource(createRelativeURL(relativePath));
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
/*     */   protected URL createRelativeURL(String relativePath) throws MalformedURLException {
/* 263 */     if (relativePath.startsWith("/")) {
/* 264 */       relativePath = relativePath.substring(1);
/*     */     }
/*     */     
/* 267 */     relativePath = StringUtils.replace(relativePath, "#", "%23");
/*     */     
/* 269 */     return new URL(this.url, relativePath);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFilename() {
/* 278 */     return StringUtils.getFilename(getCleanedUrl().getPath());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 286 */     return "URL [" + this.url + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 295 */     return (this == other || (other instanceof UrlResource && 
/* 296 */       getCleanedUrl().equals(((UrlResource)other).getCleanedUrl())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 304 */     return getCleanedUrl().hashCode();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\io\UrlResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */