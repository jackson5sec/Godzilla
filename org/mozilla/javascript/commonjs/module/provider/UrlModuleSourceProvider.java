/*     */ package org.mozilla.javascript.commonjs.module.provider;
/*     */ 
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.io.Serializable;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UrlModuleSourceProvider
/*     */   extends ModuleSourceProviderBase
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private final Iterable<URI> privilegedUris;
/*     */   private final Iterable<URI> fallbackUris;
/*     */   private final UrlConnectionSecurityDomainProvider urlConnectionSecurityDomainProvider;
/*     */   private final UrlConnectionExpiryCalculator urlConnectionExpiryCalculator;
/*     */   
/*     */   public UrlModuleSourceProvider(Iterable<URI> privilegedUris, Iterable<URI> fallbackUris) {
/*  56 */     this(privilegedUris, fallbackUris, new DefaultUrlConnectionExpiryCalculator(), null);
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
/*     */ 
/*     */   
/*     */   public UrlModuleSourceProvider(Iterable<URI> privilegedUris, Iterable<URI> fallbackUris, UrlConnectionExpiryCalculator urlConnectionExpiryCalculator, UrlConnectionSecurityDomainProvider urlConnectionSecurityDomainProvider) {
/*  81 */     this.privilegedUris = privilegedUris;
/*  82 */     this.fallbackUris = fallbackUris;
/*  83 */     this.urlConnectionExpiryCalculator = urlConnectionExpiryCalculator;
/*  84 */     this.urlConnectionSecurityDomainProvider = urlConnectionSecurityDomainProvider;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ModuleSource loadFromPrivilegedLocations(String moduleId, Object validator) throws IOException, URISyntaxException {
/*  93 */     return loadFromPathList(moduleId, validator, this.privilegedUris);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ModuleSource loadFromFallbackLocations(String moduleId, Object validator) throws IOException, URISyntaxException {
/* 101 */     return loadFromPathList(moduleId, validator, this.fallbackUris);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ModuleSource loadFromPathList(String moduleId, Object validator, Iterable<URI> paths) throws IOException, URISyntaxException {
/* 108 */     if (paths == null) {
/* 109 */       return null;
/*     */     }
/* 111 */     for (URI path : paths) {
/* 112 */       ModuleSource moduleSource = loadFromUri(path.resolve(moduleId), path, validator);
/*     */       
/* 114 */       if (moduleSource != null) {
/* 115 */         return moduleSource;
/*     */       }
/*     */     } 
/* 118 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ModuleSource loadFromUri(URI uri, URI base, Object validator) throws IOException, URISyntaxException {
/* 126 */     URI fullUri = new URI(uri + ".js");
/* 127 */     ModuleSource source = loadFromActualUri(fullUri, base, validator);
/*     */ 
/*     */     
/* 130 */     return (source != null) ? source : loadFromActualUri(uri, base, validator);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected ModuleSource loadFromActualUri(URI uri, URI base, Object validator) throws IOException {
/*     */     URLValidator applicableValidator;
/* 137 */     URL url = new URL((base == null) ? null : base.toURL(), uri.toString());
/* 138 */     long request_time = System.currentTimeMillis();
/* 139 */     URLConnection urlConnection = openUrlConnection(url);
/*     */     
/* 141 */     if (validator instanceof URLValidator) {
/* 142 */       URLValidator uriValidator = (URLValidator)validator;
/* 143 */       applicableValidator = uriValidator.appliesTo(uri) ? uriValidator : null;
/*     */     }
/*     */     else {
/*     */       
/* 147 */       applicableValidator = null;
/*     */     } 
/* 149 */     if (applicableValidator != null) {
/* 150 */       applicableValidator.applyConditionals(urlConnection);
/*     */     }
/*     */     try {
/* 153 */       urlConnection.connect();
/* 154 */       if (applicableValidator != null && applicableValidator.updateValidator(urlConnection, request_time, this.urlConnectionExpiryCalculator)) {
/*     */ 
/*     */ 
/*     */         
/* 158 */         close(urlConnection);
/* 159 */         return NOT_MODIFIED;
/*     */       } 
/*     */       
/* 162 */       return new ModuleSource(getReader(urlConnection), getSecurityDomain(urlConnection), uri, base, new URLValidator(uri, urlConnection, request_time, this.urlConnectionExpiryCalculator));
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 167 */     catch (FileNotFoundException e) {
/* 168 */       return null;
/*     */     }
/* 170 */     catch (RuntimeException e) {
/* 171 */       close(urlConnection);
/* 172 */       throw e;
/*     */     }
/* 174 */     catch (IOException e) {
/* 175 */       close(urlConnection);
/* 176 */       throw e;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Reader getReader(URLConnection urlConnection) throws IOException {
/* 183 */     return new InputStreamReader(urlConnection.getInputStream(), getCharacterEncoding(urlConnection));
/*     */   }
/*     */ 
/*     */   
/*     */   private static String getCharacterEncoding(URLConnection urlConnection) {
/* 188 */     ParsedContentType pct = new ParsedContentType(urlConnection.getContentType());
/*     */     
/* 190 */     String encoding = pct.getEncoding();
/* 191 */     if (encoding != null) {
/* 192 */       return encoding;
/*     */     }
/* 194 */     String contentType = pct.getContentType();
/* 195 */     if (contentType != null && contentType.startsWith("text/")) {
/* 196 */       return "8859_1";
/*     */     }
/*     */     
/* 199 */     return "utf-8";
/*     */   }
/*     */ 
/*     */   
/*     */   private Object getSecurityDomain(URLConnection urlConnection) {
/* 204 */     return (this.urlConnectionSecurityDomainProvider == null) ? null : this.urlConnectionSecurityDomainProvider.getSecurityDomain(urlConnection);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void close(URLConnection urlConnection) {
/*     */     try {
/* 211 */       urlConnection.getInputStream().close();
/*     */     }
/* 213 */     catch (IOException e) {
/* 214 */       onFailedClosingUrlConnection(urlConnection, e);
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
/*     */   protected void onFailedClosingUrlConnection(URLConnection urlConnection, IOException cause) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected URLConnection openUrlConnection(URL url) throws IOException {
/* 236 */     return url.openConnection();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean entityNeedsRevalidation(Object validator) {
/* 241 */     return (!(validator instanceof URLValidator) || ((URLValidator)validator).entityNeedsRevalidation());
/*     */   }
/*     */ 
/*     */   
/*     */   private static class URLValidator
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     private final URI uri;
/*     */     private final long lastModified;
/*     */     private final String entityTags;
/*     */     private long expiry;
/*     */     
/*     */     public URLValidator(URI uri, URLConnection urlConnection, long request_time, UrlConnectionExpiryCalculator urlConnectionExpiryCalculator) {
/* 256 */       this.uri = uri;
/* 257 */       this.lastModified = urlConnection.getLastModified();
/* 258 */       this.entityTags = getEntityTags(urlConnection);
/* 259 */       this.expiry = calculateExpiry(urlConnection, request_time, urlConnectionExpiryCalculator);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean updateValidator(URLConnection urlConnection, long request_time, UrlConnectionExpiryCalculator urlConnectionExpiryCalculator) throws IOException {
/* 267 */       boolean isResourceChanged = isResourceChanged(urlConnection);
/* 268 */       if (!isResourceChanged) {
/* 269 */         this.expiry = calculateExpiry(urlConnection, request_time, urlConnectionExpiryCalculator);
/*     */       }
/*     */       
/* 272 */       return isResourceChanged;
/*     */     }
/*     */ 
/*     */     
/*     */     private boolean isResourceChanged(URLConnection urlConnection) throws IOException {
/* 277 */       if (urlConnection instanceof HttpURLConnection) {
/* 278 */         return (((HttpURLConnection)urlConnection).getResponseCode() == 304);
/*     */       }
/*     */       
/* 281 */       return (this.lastModified == urlConnection.getLastModified());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private long calculateExpiry(URLConnection urlConnection, long request_time, UrlConnectionExpiryCalculator urlConnectionExpiryCalculator) {
/* 288 */       if ("no-cache".equals(urlConnection.getHeaderField("Pragma"))) {
/* 289 */         return 0L;
/*     */       }
/* 291 */       String cacheControl = urlConnection.getHeaderField("Cache-Control");
/*     */       
/* 293 */       if (cacheControl != null) {
/* 294 */         if (cacheControl.indexOf("no-cache") != -1) {
/* 295 */           return 0L;
/*     */         }
/* 297 */         int max_age = getMaxAge(cacheControl);
/* 298 */         if (-1 != max_age) {
/* 299 */           long response_time = System.currentTimeMillis();
/* 300 */           long apparent_age = Math.max(0L, response_time - urlConnection.getDate());
/*     */           
/* 302 */           long corrected_received_age = Math.max(apparent_age, urlConnection.getHeaderFieldInt("Age", 0) * 1000L);
/*     */           
/* 304 */           long response_delay = response_time - request_time;
/* 305 */           long corrected_initial_age = corrected_received_age + response_delay;
/*     */           
/* 307 */           long creation_time = response_time - corrected_initial_age;
/*     */           
/* 309 */           return max_age * 1000L + creation_time;
/*     */         } 
/*     */       } 
/* 312 */       long explicitExpiry = urlConnection.getHeaderFieldDate("Expires", -1L);
/*     */       
/* 314 */       if (explicitExpiry != -1L) {
/* 315 */         return explicitExpiry;
/*     */       }
/* 317 */       return (urlConnectionExpiryCalculator == null) ? 0L : urlConnectionExpiryCalculator.calculateExpiry(urlConnection);
/*     */     }
/*     */     
/*     */     private int getMaxAge(String cacheControl) {
/*     */       String strAge;
/* 322 */       int maxAgeIndex = cacheControl.indexOf("max-age");
/* 323 */       if (maxAgeIndex == -1) {
/* 324 */         return -1;
/*     */       }
/* 326 */       int eq = cacheControl.indexOf('=', maxAgeIndex + 7);
/* 327 */       if (eq == -1) {
/* 328 */         return -1;
/*     */       }
/* 330 */       int comma = cacheControl.indexOf(',', eq + 1);
/*     */       
/* 332 */       if (comma == -1) {
/* 333 */         strAge = cacheControl.substring(eq + 1);
/*     */       } else {
/*     */         
/* 336 */         strAge = cacheControl.substring(eq + 1, comma);
/*     */       } 
/*     */       try {
/* 339 */         return Integer.parseInt(strAge);
/*     */       }
/* 341 */       catch (NumberFormatException e) {
/* 342 */         return -1;
/*     */       } 
/*     */     }
/*     */     
/*     */     private String getEntityTags(URLConnection urlConnection) {
/* 347 */       List<String> etags = urlConnection.getHeaderFields().get("ETag");
/* 348 */       if (etags == null || etags.isEmpty()) {
/* 349 */         return null;
/*     */       }
/* 351 */       StringBuilder b = new StringBuilder();
/* 352 */       Iterator<String> it = etags.iterator();
/* 353 */       b.append(it.next());
/* 354 */       while (it.hasNext()) {
/* 355 */         b.append(", ").append(it.next());
/*     */       }
/* 357 */       return b.toString();
/*     */     }
/*     */     
/*     */     boolean appliesTo(URI uri) {
/* 361 */       return this.uri.equals(uri);
/*     */     }
/*     */     
/*     */     void applyConditionals(URLConnection urlConnection) {
/* 365 */       if (this.lastModified != 0L) {
/* 366 */         urlConnection.setIfModifiedSince(this.lastModified);
/*     */       }
/* 368 */       if (this.entityTags != null && this.entityTags.length() > 0) {
/* 369 */         urlConnection.addRequestProperty("If-None-Match", this.entityTags);
/*     */       }
/*     */     }
/*     */     
/*     */     boolean entityNeedsRevalidation() {
/* 374 */       return (System.currentTimeMillis() > this.expiry);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\commonjs\module\provider\UrlModuleSourceProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */