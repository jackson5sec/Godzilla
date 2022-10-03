/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ResourceUtils
/*     */ {
/*     */   public static final String CLASSPATH_URL_PREFIX = "classpath:";
/*     */   public static final String FILE_URL_PREFIX = "file:";
/*     */   public static final String JAR_URL_PREFIX = "jar:";
/*     */   public static final String WAR_URL_PREFIX = "war:";
/*     */   public static final String URL_PROTOCOL_FILE = "file";
/*     */   public static final String URL_PROTOCOL_JAR = "jar";
/*     */   public static final String URL_PROTOCOL_WAR = "war";
/*     */   public static final String URL_PROTOCOL_ZIP = "zip";
/*     */   public static final String URL_PROTOCOL_WSJAR = "wsjar";
/*     */   public static final String URL_PROTOCOL_VFSZIP = "vfszip";
/*     */   public static final String URL_PROTOCOL_VFSFILE = "vfsfile";
/*     */   public static final String URL_PROTOCOL_VFS = "vfs";
/*     */   public static final String JAR_FILE_EXTENSION = ".jar";
/*     */   public static final String JAR_URL_SEPARATOR = "!/";
/*     */   public static final String WAR_URL_SEPARATOR = "*/";
/*     */   
/*     */   public static boolean isUrl(@Nullable String resourceLocation) {
/* 105 */     if (resourceLocation == null) {
/* 106 */       return false;
/*     */     }
/* 108 */     if (resourceLocation.startsWith("classpath:")) {
/* 109 */       return true;
/*     */     }
/*     */     try {
/* 112 */       new URL(resourceLocation);
/* 113 */       return true;
/*     */     }
/* 115 */     catch (MalformedURLException ex) {
/* 116 */       return false;
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
/*     */   
/*     */   public static URL getURL(String resourceLocation) throws FileNotFoundException {
/* 130 */     Assert.notNull(resourceLocation, "Resource location must not be null");
/* 131 */     if (resourceLocation.startsWith("classpath:")) {
/* 132 */       String path = resourceLocation.substring("classpath:".length());
/* 133 */       ClassLoader cl = ClassUtils.getDefaultClassLoader();
/* 134 */       URL url = (cl != null) ? cl.getResource(path) : ClassLoader.getSystemResource(path);
/* 135 */       if (url == null) {
/* 136 */         String description = "class path resource [" + path + "]";
/* 137 */         throw new FileNotFoundException(description + " cannot be resolved to URL because it does not exist");
/*     */       } 
/*     */       
/* 140 */       return url;
/*     */     } 
/*     */     
/*     */     try {
/* 144 */       return new URL(resourceLocation);
/*     */     }
/* 146 */     catch (MalformedURLException ex) {
/*     */       
/*     */       try {
/* 149 */         return (new File(resourceLocation)).toURI().toURL();
/*     */       }
/* 151 */       catch (MalformedURLException ex2) {
/* 152 */         throw new FileNotFoundException("Resource location [" + resourceLocation + "] is neither a URL not a well-formed file path");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static File getFile(String resourceLocation) throws FileNotFoundException {
/* 170 */     Assert.notNull(resourceLocation, "Resource location must not be null");
/* 171 */     if (resourceLocation.startsWith("classpath:")) {
/* 172 */       String path = resourceLocation.substring("classpath:".length());
/* 173 */       String description = "class path resource [" + path + "]";
/* 174 */       ClassLoader cl = ClassUtils.getDefaultClassLoader();
/* 175 */       URL url = (cl != null) ? cl.getResource(path) : ClassLoader.getSystemResource(path);
/* 176 */       if (url == null) {
/* 177 */         throw new FileNotFoundException(description + " cannot be resolved to absolute file path because it does not exist");
/*     */       }
/*     */       
/* 180 */       return getFile(url, description);
/*     */     } 
/*     */     
/*     */     try {
/* 184 */       return getFile(new URL(resourceLocation));
/*     */     }
/* 186 */     catch (MalformedURLException ex) {
/*     */       
/* 188 */       return new File(resourceLocation);
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
/*     */   public static File getFile(URL resourceUrl) throws FileNotFoundException {
/* 201 */     return getFile(resourceUrl, "URL");
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
/*     */   public static File getFile(URL resourceUrl, String description) throws FileNotFoundException {
/* 215 */     Assert.notNull(resourceUrl, "Resource URL must not be null");
/* 216 */     if (!"file".equals(resourceUrl.getProtocol())) {
/* 217 */       throw new FileNotFoundException(description + " cannot be resolved to absolute file path because it does not reside in the file system: " + resourceUrl);
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 222 */       return new File(toURI(resourceUrl).getSchemeSpecificPart());
/*     */     }
/* 224 */     catch (URISyntaxException ex) {
/*     */       
/* 226 */       return new File(resourceUrl.getFile());
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
/*     */   
/*     */   public static File getFile(URI resourceUri) throws FileNotFoundException {
/* 240 */     return getFile(resourceUri, "URI");
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
/*     */   public static File getFile(URI resourceUri, String description) throws FileNotFoundException {
/* 255 */     Assert.notNull(resourceUri, "Resource URI must not be null");
/* 256 */     if (!"file".equals(resourceUri.getScheme())) {
/* 257 */       throw new FileNotFoundException(description + " cannot be resolved to absolute file path because it does not reside in the file system: " + resourceUri);
/*     */     }
/*     */ 
/*     */     
/* 261 */     return new File(resourceUri.getSchemeSpecificPart());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isFileURL(URL url) {
/* 271 */     String protocol = url.getProtocol();
/* 272 */     return ("file".equals(protocol) || "vfsfile".equals(protocol) || "vfs"
/* 273 */       .equals(protocol));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isJarURL(URL url) {
/* 283 */     String protocol = url.getProtocol();
/* 284 */     return ("jar".equals(protocol) || "war".equals(protocol) || "zip"
/* 285 */       .equals(protocol) || "vfszip".equals(protocol) || "wsjar"
/* 286 */       .equals(protocol));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isJarFileURL(URL url) {
/* 297 */     return ("file".equals(url.getProtocol()) && url
/* 298 */       .getPath().toLowerCase().endsWith(".jar"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static URL extractJarFileURL(URL jarUrl) throws MalformedURLException {
/* 309 */     String urlFile = jarUrl.getFile();
/* 310 */     int separatorIndex = urlFile.indexOf("!/");
/* 311 */     if (separatorIndex != -1) {
/* 312 */       String jarFile = urlFile.substring(0, separatorIndex);
/*     */       try {
/* 314 */         return new URL(jarFile);
/*     */       }
/* 316 */       catch (MalformedURLException ex) {
/*     */ 
/*     */         
/* 319 */         if (!jarFile.startsWith("/")) {
/* 320 */           jarFile = "/" + jarFile;
/*     */         }
/* 322 */         return new URL("file:" + jarFile);
/*     */       } 
/*     */     } 
/*     */     
/* 326 */     return jarUrl;
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
/*     */   public static URL extractArchiveURL(URL jarUrl) throws MalformedURLException {
/* 342 */     String urlFile = jarUrl.getFile();
/*     */     
/* 344 */     int endIndex = urlFile.indexOf("*/");
/* 345 */     if (endIndex != -1) {
/*     */       
/* 347 */       String warFile = urlFile.substring(0, endIndex);
/* 348 */       if ("war".equals(jarUrl.getProtocol())) {
/* 349 */         return new URL(warFile);
/*     */       }
/* 351 */       int startIndex = warFile.indexOf("war:");
/* 352 */       if (startIndex != -1) {
/* 353 */         return new URL(warFile.substring(startIndex + "war:".length()));
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 358 */     return extractJarFileURL(jarUrl);
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
/*     */   public static URI toURI(URL url) throws URISyntaxException {
/* 370 */     return toURI(url.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static URI toURI(String location) throws URISyntaxException {
/* 381 */     return new URI(StringUtils.replace(location, " ", "%20"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void useCachesIfNecessary(URLConnection con) {
/* 391 */     con.setUseCaches(con.getClass().getSimpleName().startsWith("JNLP"));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\ResourceUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */