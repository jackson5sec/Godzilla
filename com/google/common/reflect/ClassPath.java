/*     */ package com.google.common.reflect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.CharMatcher;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.base.StandardSystemProperty;
/*     */ import com.google.common.collect.FluentIterable;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.MultimapBuilder;
/*     */ import com.google.common.collect.SetMultimap;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.common.io.ByteSource;
/*     */ import com.google.common.io.CharSource;
/*     */ import com.google.common.io.Resources;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.jar.Attributes;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.jar.Manifest;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ public final class ClassPath
/*     */ {
/*  79 */   private static final Logger logger = Logger.getLogger(ClassPath.class.getName());
/*     */   
/*  81 */   private static final Predicate<ClassInfo> IS_TOP_LEVEL = new Predicate<ClassInfo>()
/*     */     {
/*     */       public boolean apply(ClassPath.ClassInfo info)
/*     */       {
/*  85 */         return (info.className.indexOf('$') == -1);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*  91 */   private static final Splitter CLASS_PATH_ATTRIBUTE_SEPARATOR = Splitter.on(" ").omitEmptyStrings();
/*     */   
/*     */   private static final String CLASS_FILE_NAME_EXTENSION = ".class";
/*     */   
/*     */   private final ImmutableSet<ResourceInfo> resources;
/*     */   
/*     */   private ClassPath(ImmutableSet<ResourceInfo> resources) {
/*  98 */     this.resources = resources;
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
/*     */   public static ClassPath from(ClassLoader classloader) throws IOException {
/* 118 */     DefaultScanner scanner = new DefaultScanner();
/* 119 */     scanner.scan(classloader);
/* 120 */     return new ClassPath(scanner.getResources());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<ResourceInfo> getResources() {
/* 128 */     return this.resources;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<ClassInfo> getAllClasses() {
/* 137 */     return FluentIterable.from((Iterable)this.resources).filter(ClassInfo.class).toSet();
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSet<ClassInfo> getTopLevelClasses() {
/* 142 */     return FluentIterable.from((Iterable)this.resources).filter(ClassInfo.class).filter(IS_TOP_LEVEL).toSet();
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSet<ClassInfo> getTopLevelClasses(String packageName) {
/* 147 */     Preconditions.checkNotNull(packageName);
/* 148 */     ImmutableSet.Builder<ClassInfo> builder = ImmutableSet.builder();
/* 149 */     for (UnmodifiableIterator<ClassInfo> unmodifiableIterator = getTopLevelClasses().iterator(); unmodifiableIterator.hasNext(); ) { ClassInfo classInfo = unmodifiableIterator.next();
/* 150 */       if (classInfo.getPackageName().equals(packageName)) {
/* 151 */         builder.add(classInfo);
/*     */       } }
/*     */     
/* 154 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<ClassInfo> getTopLevelClassesRecursive(String packageName) {
/* 162 */     Preconditions.checkNotNull(packageName);
/* 163 */     String packagePrefix = packageName + '.';
/* 164 */     ImmutableSet.Builder<ClassInfo> builder = ImmutableSet.builder();
/* 165 */     for (UnmodifiableIterator<ClassInfo> unmodifiableIterator = getTopLevelClasses().iterator(); unmodifiableIterator.hasNext(); ) { ClassInfo classInfo = unmodifiableIterator.next();
/* 166 */       if (classInfo.getName().startsWith(packagePrefix)) {
/* 167 */         builder.add(classInfo);
/*     */       } }
/*     */     
/* 170 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static class ResourceInfo
/*     */   {
/*     */     private final String resourceName;
/*     */ 
/*     */     
/*     */     final ClassLoader loader;
/*     */ 
/*     */ 
/*     */     
/*     */     static ResourceInfo of(String resourceName, ClassLoader loader) {
/* 186 */       if (resourceName.endsWith(".class")) {
/* 187 */         return new ClassPath.ClassInfo(resourceName, loader);
/*     */       }
/* 189 */       return new ResourceInfo(resourceName, loader);
/*     */     }
/*     */ 
/*     */     
/*     */     ResourceInfo(String resourceName, ClassLoader loader) {
/* 194 */       this.resourceName = (String)Preconditions.checkNotNull(resourceName);
/* 195 */       this.loader = (ClassLoader)Preconditions.checkNotNull(loader);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final URL url() {
/* 207 */       URL url = this.loader.getResource(this.resourceName);
/* 208 */       if (url == null) {
/* 209 */         throw new NoSuchElementException(this.resourceName);
/*     */       }
/* 211 */       return url;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final ByteSource asByteSource() {
/* 222 */       return Resources.asByteSource(url());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final CharSource asCharSource(Charset charset) {
/* 234 */       return Resources.asCharSource(url(), charset);
/*     */     }
/*     */ 
/*     */     
/*     */     public final String getResourceName() {
/* 239 */       return this.resourceName;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 244 */       return this.resourceName.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 249 */       if (obj instanceof ResourceInfo) {
/* 250 */         ResourceInfo that = (ResourceInfo)obj;
/* 251 */         return (this.resourceName.equals(that.resourceName) && this.loader == that.loader);
/*     */       } 
/* 253 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 259 */       return this.resourceName;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static final class ClassInfo
/*     */     extends ResourceInfo
/*     */   {
/*     */     private final String className;
/*     */ 
/*     */     
/*     */     ClassInfo(String resourceName, ClassLoader loader) {
/* 273 */       super(resourceName, loader);
/* 274 */       this.className = ClassPath.getClassName(resourceName);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getPackageName() {
/* 284 */       return Reflection.getPackageName(this.className);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getSimpleName() {
/* 294 */       int lastDollarSign = this.className.lastIndexOf('$');
/* 295 */       if (lastDollarSign != -1) {
/* 296 */         String innerClassName = this.className.substring(lastDollarSign + 1);
/*     */ 
/*     */         
/* 299 */         return CharMatcher.digit().trimLeadingFrom(innerClassName);
/*     */       } 
/* 301 */       String packageName = getPackageName();
/* 302 */       if (packageName.isEmpty()) {
/* 303 */         return this.className;
/*     */       }
/*     */ 
/*     */       
/* 307 */       return this.className.substring(packageName.length() + 1);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getName() {
/* 317 */       return this.className;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Class<?> load() {
/*     */       try {
/* 328 */         return this.loader.loadClass(this.className);
/* 329 */       } catch (ClassNotFoundException e) {
/*     */         
/* 331 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 337 */       return this.className;
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
/*     */   static abstract class Scanner
/*     */   {
/* 350 */     private final Set<File> scannedUris = Sets.newHashSet();
/*     */     
/*     */     public final void scan(ClassLoader classloader) throws IOException {
/* 353 */       for (UnmodifiableIterator<Map.Entry<File, ClassLoader>> unmodifiableIterator = getClassPathEntries(classloader).entrySet().iterator(); unmodifiableIterator.hasNext(); ) { Map.Entry<File, ClassLoader> entry = unmodifiableIterator.next();
/* 354 */         scan(entry.getKey(), entry.getValue()); }
/*     */     
/*     */     }
/*     */     
/*     */     @VisibleForTesting
/*     */     final void scan(File file, ClassLoader classloader) throws IOException {
/* 360 */       if (this.scannedUris.add(file.getCanonicalFile())) {
/* 361 */         scanFrom(file, classloader);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     protected abstract void scanDirectory(ClassLoader param1ClassLoader, File param1File) throws IOException;
/*     */ 
/*     */     
/*     */     protected abstract void scanJarFile(ClassLoader param1ClassLoader, JarFile param1JarFile) throws IOException;
/*     */     
/*     */     private void scanFrom(File file, ClassLoader classloader) throws IOException {
/*     */       try {
/* 373 */         if (!file.exists()) {
/*     */           return;
/*     */         }
/* 376 */       } catch (SecurityException e) {
/* 377 */         ClassPath.logger.warning("Cannot access " + file + ": " + e);
/*     */         
/*     */         return;
/*     */       } 
/* 381 */       if (file.isDirectory()) {
/* 382 */         scanDirectory(classloader, file);
/*     */       } else {
/* 384 */         scanJar(file, classloader);
/*     */       } 
/*     */     }
/*     */     
/*     */     private void scanJar(File file, ClassLoader classloader) throws IOException {
/*     */       JarFile jarFile;
/*     */       try {
/* 391 */         jarFile = new JarFile(file);
/* 392 */       } catch (IOException e) {
/*     */         return;
/*     */       } 
/*     */       
/*     */       try {
/* 397 */         for (UnmodifiableIterator<File> unmodifiableIterator = getClassPathFromManifest(file, jarFile.getManifest()).iterator(); unmodifiableIterator.hasNext(); ) { File path = unmodifiableIterator.next();
/* 398 */           scan(path, classloader); }
/*     */         
/* 400 */         scanJarFile(classloader, jarFile);
/*     */       } finally {
/*     */         try {
/* 403 */           jarFile.close();
/* 404 */         } catch (IOException iOException) {}
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @VisibleForTesting
/*     */     static ImmutableSet<File> getClassPathFromManifest(File jarFile, Manifest manifest) {
/* 418 */       if (manifest == null) {
/* 419 */         return ImmutableSet.of();
/*     */       }
/* 421 */       ImmutableSet.Builder<File> builder = ImmutableSet.builder();
/*     */       
/* 423 */       String classpathAttribute = manifest.getMainAttributes().getValue(Attributes.Name.CLASS_PATH.toString());
/* 424 */       if (classpathAttribute != null) {
/* 425 */         for (String path : ClassPath.CLASS_PATH_ATTRIBUTE_SEPARATOR.split(classpathAttribute)) {
/*     */           URL url;
/*     */           try {
/* 428 */             url = getClassPathEntry(jarFile, path);
/* 429 */           } catch (MalformedURLException e) {
/*     */             
/* 431 */             ClassPath.logger.warning("Invalid Class-Path entry: " + path);
/*     */             continue;
/*     */           } 
/* 434 */           if (url.getProtocol().equals("file")) {
/* 435 */             builder.add(ClassPath.toFile(url));
/*     */           }
/*     */         } 
/*     */       }
/* 439 */       return builder.build();
/*     */     }
/*     */     
/*     */     @VisibleForTesting
/*     */     static ImmutableMap<File, ClassLoader> getClassPathEntries(ClassLoader classloader) {
/* 444 */       LinkedHashMap<File, ClassLoader> entries = Maps.newLinkedHashMap();
/*     */       
/* 446 */       ClassLoader parent = classloader.getParent();
/* 447 */       if (parent != null) {
/* 448 */         entries.putAll((Map<? extends File, ? extends ClassLoader>)getClassPathEntries(parent));
/*     */       }
/* 450 */       for (UnmodifiableIterator<URL> unmodifiableIterator = getClassLoaderUrls(classloader).iterator(); unmodifiableIterator.hasNext(); ) { URL url = unmodifiableIterator.next();
/* 451 */         if (url.getProtocol().equals("file")) {
/* 452 */           File file = ClassPath.toFile(url);
/* 453 */           if (!entries.containsKey(file)) {
/* 454 */             entries.put(file, classloader);
/*     */           }
/*     */         }  }
/*     */       
/* 458 */       return ImmutableMap.copyOf(entries);
/*     */     }
/*     */     
/*     */     private static ImmutableList<URL> getClassLoaderUrls(ClassLoader classloader) {
/* 462 */       if (classloader instanceof URLClassLoader) {
/* 463 */         return ImmutableList.copyOf((Object[])((URLClassLoader)classloader).getURLs());
/*     */       }
/* 465 */       if (classloader.equals(ClassLoader.getSystemClassLoader())) {
/* 466 */         return parseJavaClassPath();
/*     */       }
/* 468 */       return ImmutableList.of();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @VisibleForTesting
/*     */     static ImmutableList<URL> parseJavaClassPath() {
/* 477 */       ImmutableList.Builder<URL> urls = ImmutableList.builder();
/* 478 */       for (String entry : Splitter.on(StandardSystemProperty.PATH_SEPARATOR.value()).split(StandardSystemProperty.JAVA_CLASS_PATH.value())) {
/*     */         try {
/*     */           try {
/* 481 */             urls.add((new File(entry)).toURI().toURL());
/* 482 */           } catch (SecurityException e) {
/* 483 */             urls.add(new URL("file", null, (new File(entry)).getAbsolutePath()));
/*     */           } 
/* 485 */         } catch (MalformedURLException e) {
/* 486 */           ClassPath.logger.log(Level.WARNING, "malformed classpath entry: " + entry, e);
/*     */         } 
/*     */       } 
/* 489 */       return urls.build();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @VisibleForTesting
/*     */     static URL getClassPathEntry(File jarFile, String path) throws MalformedURLException {
/* 500 */       return new URL(jarFile.toURI().toURL(), path);
/*     */     }
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static final class DefaultScanner
/*     */     extends Scanner {
/* 507 */     private final SetMultimap<ClassLoader, String> resources = MultimapBuilder.hashKeys().linkedHashSetValues().build();
/*     */     
/*     */     ImmutableSet<ClassPath.ResourceInfo> getResources() {
/* 510 */       ImmutableSet.Builder<ClassPath.ResourceInfo> builder = ImmutableSet.builder();
/* 511 */       for (Map.Entry<ClassLoader, String> entry : (Iterable<Map.Entry<ClassLoader, String>>)this.resources.entries()) {
/* 512 */         builder.add(ClassPath.ResourceInfo.of(entry.getValue(), entry.getKey()));
/*     */       }
/* 514 */       return builder.build();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void scanJarFile(ClassLoader classloader, JarFile file) {
/* 519 */       Enumeration<JarEntry> entries = file.entries();
/* 520 */       while (entries.hasMoreElements()) {
/* 521 */         JarEntry entry = entries.nextElement();
/* 522 */         if (entry.isDirectory() || entry.getName().equals("META-INF/MANIFEST.MF")) {
/*     */           continue;
/*     */         }
/* 525 */         this.resources.get(classloader).add(entry.getName());
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected void scanDirectory(ClassLoader classloader, File directory) throws IOException {
/* 531 */       Set<File> currentPath = new HashSet<>();
/* 532 */       currentPath.add(directory.getCanonicalFile());
/* 533 */       scanDirectory(directory, classloader, "", currentPath);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void scanDirectory(File directory, ClassLoader classloader, String packagePrefix, Set<File> currentPath) throws IOException {
/* 551 */       File[] files = directory.listFiles();
/* 552 */       if (files == null) {
/* 553 */         ClassPath.logger.warning("Cannot read directory " + directory);
/*     */         
/*     */         return;
/*     */       } 
/* 557 */       for (File f : files) {
/* 558 */         String name = f.getName();
/* 559 */         if (f.isDirectory()) {
/* 560 */           File deref = f.getCanonicalFile();
/* 561 */           if (currentPath.add(deref)) {
/* 562 */             scanDirectory(deref, classloader, packagePrefix + name + "/", currentPath);
/* 563 */             currentPath.remove(deref);
/*     */           } 
/*     */         } else {
/* 566 */           String resourceName = packagePrefix + name;
/* 567 */           if (!resourceName.equals("META-INF/MANIFEST.MF")) {
/* 568 */             this.resources.get(classloader).add(resourceName);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static String getClassName(String filename) {
/* 577 */     int classNameEnd = filename.length() - ".class".length();
/* 578 */     return filename.substring(0, classNameEnd).replace('/', '.');
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static File toFile(URL url) {
/* 584 */     Preconditions.checkArgument(url.getProtocol().equals("file"));
/*     */     try {
/* 586 */       return new File(url.toURI());
/* 587 */     } catch (URISyntaxException e) {
/* 588 */       return new File(url.getPath());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\reflect\ClassPath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */