/*     */ package org.springframework.core.io.support;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.JarURLConnection;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.net.URLConnection;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Enumeration;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.zip.ZipException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.io.DefaultResourceLoader;
/*     */ import org.springframework.core.io.FileSystemResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.core.io.UrlResource;
/*     */ import org.springframework.core.io.VfsResource;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.AntPathMatcher;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.PathMatcher;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PathMatchingResourcePatternResolver
/*     */   implements ResourcePatternResolver
/*     */ {
/* 185 */   private static final Log logger = LogFactory.getLog(PathMatchingResourcePatternResolver.class);
/*     */   
/*     */   @Nullable
/*     */   private static Method equinoxResolveMethod;
/*     */   private final ResourceLoader resourceLoader;
/*     */   
/*     */   static {
/*     */     try {
/* 193 */       Class<?> fileLocatorClass = ClassUtils.forName("org.eclipse.core.runtime.FileLocator", PathMatchingResourcePatternResolver.class
/* 194 */           .getClassLoader());
/* 195 */       equinoxResolveMethod = fileLocatorClass.getMethod("resolve", new Class[] { URL.class });
/* 196 */       logger.trace("Found Equinox FileLocator for OSGi bundle URL resolution");
/*     */     }
/* 198 */     catch (Throwable ex) {
/* 199 */       equinoxResolveMethod = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 206 */   private PathMatcher pathMatcher = (PathMatcher)new AntPathMatcher();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PathMatchingResourcePatternResolver() {
/* 215 */     this.resourceLoader = (ResourceLoader)new DefaultResourceLoader();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PathMatchingResourcePatternResolver(ResourceLoader resourceLoader) {
/* 225 */     Assert.notNull(resourceLoader, "ResourceLoader must not be null");
/* 226 */     this.resourceLoader = resourceLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PathMatchingResourcePatternResolver(@Nullable ClassLoader classLoader) {
/* 237 */     this.resourceLoader = (ResourceLoader)new DefaultResourceLoader(classLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceLoader getResourceLoader() {
/* 245 */     return this.resourceLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ClassLoader getClassLoader() {
/* 251 */     return getResourceLoader().getClassLoader();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPathMatcher(PathMatcher pathMatcher) {
/* 260 */     Assert.notNull(pathMatcher, "PathMatcher must not be null");
/* 261 */     this.pathMatcher = pathMatcher;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PathMatcher getPathMatcher() {
/* 268 */     return this.pathMatcher;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Resource getResource(String location) {
/* 274 */     return getResourceLoader().getResource(location);
/*     */   }
/*     */ 
/*     */   
/*     */   public Resource[] getResources(String locationPattern) throws IOException {
/* 279 */     Assert.notNull(locationPattern, "Location pattern must not be null");
/* 280 */     if (locationPattern.startsWith("classpath*:")) {
/*     */       
/* 282 */       if (getPathMatcher().isPattern(locationPattern.substring("classpath*:".length())))
/*     */       {
/* 284 */         return findPathMatchingResources(locationPattern);
/*     */       }
/*     */ 
/*     */       
/* 288 */       return findAllClassPathResources(locationPattern.substring("classpath*:".length()));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 295 */     int prefixEnd = locationPattern.startsWith("war:") ? (locationPattern.indexOf("*/") + 1) : (locationPattern.indexOf(':') + 1);
/* 296 */     if (getPathMatcher().isPattern(locationPattern.substring(prefixEnd)))
/*     */     {
/* 298 */       return findPathMatchingResources(locationPattern);
/*     */     }
/*     */ 
/*     */     
/* 302 */     return new Resource[] { getResourceLoader().getResource(locationPattern) };
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
/*     */   protected Resource[] findAllClassPathResources(String location) throws IOException {
/* 317 */     String path = location;
/* 318 */     if (path.startsWith("/")) {
/* 319 */       path = path.substring(1);
/*     */     }
/* 321 */     Set<Resource> result = doFindAllClassPathResources(path);
/* 322 */     if (logger.isTraceEnabled()) {
/* 323 */       logger.trace("Resolved classpath location [" + location + "] to resources " + result);
/*     */     }
/* 325 */     return result.<Resource>toArray(new Resource[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Set<Resource> doFindAllClassPathResources(String path) throws IOException {
/* 336 */     Set<Resource> result = new LinkedHashSet<>(16);
/* 337 */     ClassLoader cl = getClassLoader();
/* 338 */     Enumeration<URL> resourceUrls = (cl != null) ? cl.getResources(path) : ClassLoader.getSystemResources(path);
/* 339 */     while (resourceUrls.hasMoreElements()) {
/* 340 */       URL url = resourceUrls.nextElement();
/* 341 */       result.add(convertClassLoaderURL(url));
/*     */     } 
/* 343 */     if (!StringUtils.hasLength(path))
/*     */     {
/*     */       
/* 346 */       addAllClassLoaderJarRoots(cl, result);
/*     */     }
/* 348 */     return result;
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
/*     */   protected Resource convertClassLoaderURL(URL url) {
/* 360 */     return (Resource)new UrlResource(url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addAllClassLoaderJarRoots(@Nullable ClassLoader classLoader, Set<Resource> result) {
/* 371 */     if (classLoader instanceof URLClassLoader) {
/*     */       try {
/* 373 */         for (URL url : ((URLClassLoader)classLoader).getURLs()) {
/*     */           try {
/* 375 */             UrlResource jarResource = "jar".equals(url.getProtocol()) ? new UrlResource(url) : new UrlResource("jar:" + url + "!/");
/*     */ 
/*     */             
/* 378 */             if (jarResource.exists()) {
/* 379 */               result.add(jarResource);
/*     */             }
/*     */           }
/* 382 */           catch (MalformedURLException ex) {
/* 383 */             if (logger.isDebugEnabled()) {
/* 384 */               logger.debug("Cannot search for matching files underneath [" + url + "] because it cannot be converted to a valid 'jar:' URL: " + ex
/* 385 */                   .getMessage());
/*     */             }
/*     */           }
/*     */         
/*     */         } 
/* 390 */       } catch (Exception ex) {
/* 391 */         if (logger.isDebugEnabled()) {
/* 392 */           logger.debug("Cannot introspect jar files since ClassLoader [" + classLoader + "] does not support 'getURLs()': " + ex);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 398 */     if (classLoader == ClassLoader.getSystemClassLoader())
/*     */     {
/* 400 */       addClassPathManifestEntries(result);
/*     */     }
/*     */     
/* 403 */     if (classLoader != null) {
/*     */       
/*     */       try {
/* 406 */         addAllClassLoaderJarRoots(classLoader.getParent(), result);
/*     */       }
/* 408 */       catch (Exception ex) {
/* 409 */         if (logger.isDebugEnabled()) {
/* 410 */           logger.debug("Cannot introspect jar files in parent ClassLoader since [" + classLoader + "] does not support 'getParent()': " + ex);
/*     */         }
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
/*     */   protected void addClassPathManifestEntries(Set<Resource> result) {
/*     */     try {
/* 425 */       String javaClassPathProperty = System.getProperty("java.class.path");
/* 426 */       for (String path : StringUtils.delimitedListToStringArray(javaClassPathProperty, 
/* 427 */           System.getProperty("path.separator"))) {
/*     */         try {
/* 429 */           String filePath = (new File(path)).getAbsolutePath();
/* 430 */           int prefixIndex = filePath.indexOf(':');
/* 431 */           if (prefixIndex == 1)
/*     */           {
/* 433 */             filePath = StringUtils.capitalize(filePath);
/*     */           }
/*     */           
/* 436 */           filePath = StringUtils.replace(filePath, "#", "%23");
/*     */           
/* 438 */           UrlResource jarResource = new UrlResource("jar:file:" + filePath + "!/");
/*     */ 
/*     */           
/* 441 */           if (!result.contains(jarResource) && !hasDuplicate(filePath, result) && jarResource.exists()) {
/* 442 */             result.add(jarResource);
/*     */           }
/*     */         }
/* 445 */         catch (MalformedURLException ex) {
/* 446 */           if (logger.isDebugEnabled()) {
/* 447 */             logger.debug("Cannot search for matching files underneath [" + path + "] because it cannot be converted to a valid 'jar:' URL: " + ex
/* 448 */                 .getMessage());
/*     */           }
/*     */         }
/*     */       
/*     */       } 
/* 453 */     } catch (Exception ex) {
/* 454 */       if (logger.isDebugEnabled()) {
/* 455 */         logger.debug("Failed to evaluate 'java.class.path' manifest entries: " + ex);
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
/*     */   private boolean hasDuplicate(String filePath, Set<Resource> result) {
/* 469 */     if (result.isEmpty()) {
/* 470 */       return false;
/*     */     }
/* 472 */     String duplicatePath = filePath.startsWith("/") ? filePath.substring(1) : ("/" + filePath);
/*     */     try {
/* 474 */       return result.contains(new UrlResource("jar:file:" + duplicatePath + "!/"));
/*     */     
/*     */     }
/* 477 */     catch (MalformedURLException ex) {
/*     */       
/* 479 */       return false;
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
/*     */   protected Resource[] findPathMatchingResources(String locationPattern) throws IOException {
/* 495 */     String rootDirPath = determineRootDir(locationPattern);
/* 496 */     String subPattern = locationPattern.substring(rootDirPath.length());
/* 497 */     Resource[] rootDirResources = getResources(rootDirPath);
/* 498 */     Set<Resource> result = new LinkedHashSet<>(16);
/* 499 */     for (Resource rootDirResource : rootDirResources) {
/* 500 */       UrlResource urlResource; rootDirResource = resolveRootDirResource(rootDirResource);
/* 501 */       URL rootDirUrl = rootDirResource.getURL();
/* 502 */       if (equinoxResolveMethod != null && rootDirUrl.getProtocol().startsWith("bundle")) {
/* 503 */         URL resolvedUrl = (URL)ReflectionUtils.invokeMethod(equinoxResolveMethod, null, new Object[] { rootDirUrl });
/* 504 */         if (resolvedUrl != null) {
/* 505 */           rootDirUrl = resolvedUrl;
/*     */         }
/* 507 */         urlResource = new UrlResource(rootDirUrl);
/*     */       } 
/* 509 */       if (rootDirUrl.getProtocol().startsWith("vfs")) {
/* 510 */         result.addAll(VfsResourceMatchingDelegate.findMatchingResources(rootDirUrl, subPattern, getPathMatcher()));
/*     */       }
/* 512 */       else if (ResourceUtils.isJarURL(rootDirUrl) || isJarResource((Resource)urlResource)) {
/* 513 */         result.addAll(doFindPathMatchingJarResources((Resource)urlResource, rootDirUrl, subPattern));
/*     */       } else {
/*     */         
/* 516 */         result.addAll(doFindPathMatchingFileResources((Resource)urlResource, subPattern));
/*     */       } 
/*     */     } 
/* 519 */     if (logger.isTraceEnabled()) {
/* 520 */       logger.trace("Resolved location pattern [" + locationPattern + "] to resources " + result);
/*     */     }
/* 522 */     return result.<Resource>toArray(new Resource[0]);
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
/*     */   protected String determineRootDir(String location) {
/* 538 */     int prefixEnd = location.indexOf(':') + 1;
/* 539 */     int rootDirEnd = location.length();
/* 540 */     while (rootDirEnd > prefixEnd && getPathMatcher().isPattern(location.substring(prefixEnd, rootDirEnd))) {
/* 541 */       rootDirEnd = location.lastIndexOf('/', rootDirEnd - 2) + 1;
/*     */     }
/* 543 */     if (rootDirEnd == 0) {
/* 544 */       rootDirEnd = prefixEnd;
/*     */     }
/* 546 */     return location.substring(0, rootDirEnd);
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
/*     */   protected Resource resolveRootDirResource(Resource original) throws IOException {
/* 560 */     return original;
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
/*     */   protected boolean isJarResource(Resource resource) throws IOException {
/* 576 */     return false;
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
/*     */   protected Set<Resource> doFindPathMatchingJarResources(Resource rootDirResource, URL rootDirURL, String subPattern) throws IOException {
/*     */     JarFile jarFile;
/*     */     String jarFileUrl, rootEntryPath;
/*     */     boolean closeJarFile;
/* 594 */     URLConnection con = rootDirURL.openConnection();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 600 */     if (con instanceof JarURLConnection) {
/*     */       
/* 602 */       JarURLConnection jarCon = (JarURLConnection)con;
/* 603 */       ResourceUtils.useCachesIfNecessary(jarCon);
/* 604 */       jarFile = jarCon.getJarFile();
/* 605 */       jarFileUrl = jarCon.getJarFileURL().toExternalForm();
/* 606 */       JarEntry jarEntry = jarCon.getJarEntry();
/* 607 */       rootEntryPath = (jarEntry != null) ? jarEntry.getName() : "";
/* 608 */       closeJarFile = !jarCon.getUseCaches();
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 615 */       String urlFile = rootDirURL.getFile();
/*     */       try {
/* 617 */         int separatorIndex = urlFile.indexOf("*/");
/* 618 */         if (separatorIndex == -1) {
/* 619 */           separatorIndex = urlFile.indexOf("!/");
/*     */         }
/* 621 */         if (separatorIndex != -1) {
/* 622 */           jarFileUrl = urlFile.substring(0, separatorIndex);
/* 623 */           rootEntryPath = urlFile.substring(separatorIndex + 2);
/* 624 */           jarFile = getJarFile(jarFileUrl);
/*     */         } else {
/*     */           
/* 627 */           jarFile = new JarFile(urlFile);
/* 628 */           jarFileUrl = urlFile;
/* 629 */           rootEntryPath = "";
/*     */         } 
/* 631 */         closeJarFile = true;
/*     */       }
/* 633 */       catch (ZipException ex) {
/* 634 */         if (logger.isDebugEnabled()) {
/* 635 */           logger.debug("Skipping invalid jar classpath entry [" + urlFile + "]");
/*     */         }
/* 637 */         return Collections.emptySet();
/*     */       } 
/*     */     } 
/*     */     
/*     */     try {
/* 642 */       if (logger.isTraceEnabled()) {
/* 643 */         logger.trace("Looking for matching resources in jar file [" + jarFileUrl + "]");
/*     */       }
/* 645 */       if (StringUtils.hasLength(rootEntryPath) && !rootEntryPath.endsWith("/"))
/*     */       {
/*     */         
/* 648 */         rootEntryPath = rootEntryPath + "/";
/*     */       }
/* 650 */       Set<Resource> result = new LinkedHashSet<>(8);
/* 651 */       for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements(); ) {
/* 652 */         JarEntry entry = entries.nextElement();
/* 653 */         String entryPath = entry.getName();
/* 654 */         if (entryPath.startsWith(rootEntryPath)) {
/* 655 */           String relativePath = entryPath.substring(rootEntryPath.length());
/* 656 */           if (getPathMatcher().match(subPattern, relativePath)) {
/* 657 */             result.add(rootDirResource.createRelative(relativePath));
/*     */           }
/*     */         } 
/*     */       } 
/* 661 */       return result;
/*     */     } finally {
/*     */       
/* 664 */       if (closeJarFile) {
/* 665 */         jarFile.close();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JarFile getJarFile(String jarFileUrl) throws IOException {
/* 674 */     if (jarFileUrl.startsWith("file:")) {
/*     */       try {
/* 676 */         return new JarFile(ResourceUtils.toURI(jarFileUrl).getSchemeSpecificPart());
/*     */       }
/* 678 */       catch (URISyntaxException ex) {
/*     */         
/* 680 */         return new JarFile(jarFileUrl.substring("file:".length()));
/*     */       } 
/*     */     }
/*     */     
/* 684 */     return new JarFile(jarFileUrl);
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
/*     */   protected Set<Resource> doFindPathMatchingFileResources(Resource rootDirResource, String subPattern) throws IOException {
/*     */     File rootDir;
/*     */     try {
/* 703 */       rootDir = rootDirResource.getFile().getAbsoluteFile();
/*     */     }
/* 705 */     catch (FileNotFoundException ex) {
/* 706 */       if (logger.isDebugEnabled()) {
/* 707 */         logger.debug("Cannot search for matching files underneath " + rootDirResource + " in the file system: " + ex
/* 708 */             .getMessage());
/*     */       }
/* 710 */       return Collections.emptySet();
/*     */     }
/* 712 */     catch (Exception ex) {
/* 713 */       if (logger.isInfoEnabled()) {
/* 714 */         logger.info("Failed to resolve " + rootDirResource + " in the file system: " + ex);
/*     */       }
/* 716 */       return Collections.emptySet();
/*     */     } 
/* 718 */     return doFindMatchingFileSystemResources(rootDir, subPattern);
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
/*     */   protected Set<Resource> doFindMatchingFileSystemResources(File rootDir, String subPattern) throws IOException {
/* 732 */     if (logger.isTraceEnabled()) {
/* 733 */       logger.trace("Looking for matching resources in directory tree [" + rootDir.getPath() + "]");
/*     */     }
/* 735 */     Set<File> matchingFiles = retrieveMatchingFiles(rootDir, subPattern);
/* 736 */     Set<Resource> result = new LinkedHashSet<>(matchingFiles.size());
/* 737 */     for (File file : matchingFiles) {
/* 738 */       result.add(new FileSystemResource(file));
/*     */     }
/* 740 */     return result;
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
/*     */   protected Set<File> retrieveMatchingFiles(File rootDir, String pattern) throws IOException {
/* 753 */     if (!rootDir.exists()) {
/*     */       
/* 755 */       if (logger.isDebugEnabled()) {
/* 756 */         logger.debug("Skipping [" + rootDir.getAbsolutePath() + "] because it does not exist");
/*     */       }
/* 758 */       return Collections.emptySet();
/*     */     } 
/* 760 */     if (!rootDir.isDirectory()) {
/*     */       
/* 762 */       if (logger.isInfoEnabled()) {
/* 763 */         logger.info("Skipping [" + rootDir.getAbsolutePath() + "] because it does not denote a directory");
/*     */       }
/* 765 */       return Collections.emptySet();
/*     */     } 
/* 767 */     if (!rootDir.canRead()) {
/* 768 */       if (logger.isInfoEnabled()) {
/* 769 */         logger.info("Skipping search for matching files underneath directory [" + rootDir.getAbsolutePath() + "] because the application is not allowed to read the directory");
/*     */       }
/*     */       
/* 772 */       return Collections.emptySet();
/*     */     } 
/* 774 */     String fullPattern = StringUtils.replace(rootDir.getAbsolutePath(), File.separator, "/");
/* 775 */     if (!pattern.startsWith("/")) {
/* 776 */       fullPattern = fullPattern + "/";
/*     */     }
/* 778 */     fullPattern = fullPattern + StringUtils.replace(pattern, File.separator, "/");
/* 779 */     Set<File> result = new LinkedHashSet<>(8);
/* 780 */     doRetrieveMatchingFiles(fullPattern, rootDir, result);
/* 781 */     return result;
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
/*     */   protected void doRetrieveMatchingFiles(String fullPattern, File dir, Set<File> result) throws IOException {
/* 794 */     if (logger.isTraceEnabled()) {
/* 795 */       logger.trace("Searching directory [" + dir.getAbsolutePath() + "] for files matching pattern [" + fullPattern + "]");
/*     */     }
/*     */     
/* 798 */     for (File content : listDirectory(dir)) {
/* 799 */       String currPath = StringUtils.replace(content.getAbsolutePath(), File.separator, "/");
/* 800 */       if (content.isDirectory() && getPathMatcher().matchStart(fullPattern, currPath + "/")) {
/* 801 */         if (!content.canRead()) {
/* 802 */           if (logger.isDebugEnabled()) {
/* 803 */             logger.debug("Skipping subdirectory [" + dir.getAbsolutePath() + "] because the application is not allowed to read the directory");
/*     */           }
/*     */         }
/*     */         else {
/*     */           
/* 808 */           doRetrieveMatchingFiles(fullPattern, content, result);
/*     */         } 
/*     */       }
/* 811 */       if (getPathMatcher().match(fullPattern, currPath)) {
/* 812 */         result.add(content);
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
/*     */   protected File[] listDirectory(File dir) {
/* 825 */     File[] files = dir.listFiles();
/* 826 */     if (files == null) {
/* 827 */       if (logger.isInfoEnabled()) {
/* 828 */         logger.info("Could not retrieve contents of directory [" + dir.getAbsolutePath() + "]");
/*     */       }
/* 830 */       return new File[0];
/*     */     } 
/* 832 */     Arrays.sort(files, Comparator.comparing(File::getName));
/* 833 */     return files;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class VfsResourceMatchingDelegate
/*     */   {
/*     */     public static Set<Resource> findMatchingResources(URL rootDirURL, String locationPattern, PathMatcher pathMatcher) throws IOException {
/* 845 */       Object root = VfsPatternUtils.findRoot(rootDirURL);
/*     */       
/* 847 */       PathMatchingResourcePatternResolver.PatternVirtualFileVisitor visitor = new PathMatchingResourcePatternResolver.PatternVirtualFileVisitor(VfsPatternUtils.getPath(root), locationPattern, pathMatcher);
/* 848 */       VfsPatternUtils.visit(root, visitor);
/* 849 */       return visitor.getResources();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class PatternVirtualFileVisitor
/*     */     implements InvocationHandler
/*     */   {
/*     */     private final String subPattern;
/*     */ 
/*     */     
/*     */     private final PathMatcher pathMatcher;
/*     */ 
/*     */     
/*     */     private final String rootPath;
/*     */     
/* 866 */     private final Set<Resource> resources = new LinkedHashSet<>();
/*     */     
/*     */     public PatternVirtualFileVisitor(String rootPath, String subPattern, PathMatcher pathMatcher) {
/* 869 */       this.subPattern = subPattern;
/* 870 */       this.pathMatcher = pathMatcher;
/* 871 */       this.rootPath = (rootPath.isEmpty() || rootPath.endsWith("/")) ? rootPath : (rootPath + "/");
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/* 877 */       String methodName = method.getName();
/* 878 */       if (Object.class == method.getDeclaringClass()) {
/* 879 */         if (methodName.equals("equals"))
/*     */         {
/* 881 */           return Boolean.valueOf((proxy == args[0]));
/*     */         }
/* 883 */         if (methodName.equals("hashCode")) {
/* 884 */           return Integer.valueOf(System.identityHashCode(proxy));
/*     */         }
/*     */       } else {
/* 887 */         if ("getAttributes".equals(methodName)) {
/* 888 */           return getAttributes();
/*     */         }
/* 890 */         if ("visit".equals(methodName)) {
/* 891 */           visit(args[0]);
/* 892 */           return null;
/*     */         } 
/* 894 */         if ("toString".equals(methodName)) {
/* 895 */           return toString();
/*     */         }
/*     */       } 
/* 898 */       throw new IllegalStateException("Unexpected method invocation: " + method);
/*     */     }
/*     */     
/*     */     public void visit(Object vfsResource) {
/* 902 */       if (this.pathMatcher.match(this.subPattern, 
/* 903 */           VfsPatternUtils.getPath(vfsResource).substring(this.rootPath.length()))) {
/* 904 */         this.resources.add(new VfsResource(vfsResource));
/*     */       }
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public Object getAttributes() {
/* 910 */       return VfsPatternUtils.getVisitorAttributes();
/*     */     }
/*     */     
/*     */     public Set<Resource> getResources() {
/* 914 */       return this.resources;
/*     */     }
/*     */     
/*     */     public int size() {
/* 918 */       return this.resources.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 923 */       return "sub-pattern: " + this.subPattern + ", resources: " + this.resources;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\io\support\PathMatchingResourcePatternResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */