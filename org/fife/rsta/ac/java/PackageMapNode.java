/*     */ package org.fife.rsta.ac.java;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import org.fife.rsta.ac.java.buildpath.LibraryInfo;
/*     */ import org.fife.rsta.ac.java.classreader.ClassFile;
/*     */ import org.fife.rsta.ac.java.classreader.Util;
/*     */ import org.fife.ui.autocomplete.Completion;
/*     */ import org.fife.ui.autocomplete.CompletionProvider;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PackageMapNode
/*     */ {
/*  58 */   private SortedMap<String, PackageMapNode> subpackages = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
/*     */   
/*  60 */   private SortedMap<String, ClassFile> classFiles = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(String className) {
/*  75 */     String[] tokens = Util.splitOnChar(className, 47);
/*  76 */     PackageMapNode pmn = this;
/*     */     
/*  78 */     for (int i = 0; i < tokens.length - 1; i++) {
/*  79 */       String pkg = tokens[i];
/*  80 */       PackageMapNode child = pmn.subpackages.get(pkg);
/*  81 */       if (child == null) {
/*  82 */         child = new PackageMapNode();
/*  83 */         pmn.subpackages.put(pkg, child);
/*     */       } 
/*  85 */       pmn = child;
/*     */     } 
/*     */     
/*  88 */     className = tokens[tokens.length - 1];
/*     */ 
/*     */     
/*  91 */     className = className.substring(0, className.length() - 6);
/*  92 */     pmn.classFiles.put(className, null);
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
/*     */   public void addCompletions(LibraryInfo info, CompletionProvider provider, String[] pkgNames, Set<Completion> addTo) {
/* 109 */     PackageMapNode map = this;
/* 110 */     for (int i = 0; i < pkgNames.length - 1; i++) {
/* 111 */       map = map.subpackages.get(pkgNames[i]);
/* 112 */       if (map == null) {
/*     */         return;
/*     */       }
/*     */     } 
/*     */     
/* 117 */     String fromKey = pkgNames[pkgNames.length - 1];
/* 118 */     String toKey = fromKey + '{';
/*     */ 
/*     */     
/* 121 */     SortedMap<String, PackageMapNode> subpackages = map.subpackages.subMap(fromKey, toKey);
/* 122 */     if (!subpackages.isEmpty()) {
/*     */       
/* 124 */       StringBuilder sb = new StringBuilder();
/* 125 */       for (int j = 0; j < pkgNames.length - 1; j++) {
/* 126 */         sb.append(pkgNames[j]).append('.');
/*     */       }
/* 128 */       String earlierPackages = sb.toString();
/*     */       
/* 130 */       for (Map.Entry<String, PackageMapNode> entry : subpackages.entrySet()) {
/* 131 */         String completionPackageName = entry.getKey();
/* 132 */         String text = earlierPackages + completionPackageName;
/* 133 */         addTo.add(new PackageNameCompletion(provider, text, fromKey));
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 139 */     SortedMap<String, ClassFile> sm = map.classFiles.subMap(fromKey, toKey);
/* 140 */     for (Map.Entry<String, ClassFile> entry : sm.entrySet()) {
/*     */       
/* 142 */       String key = entry.getKey();
/* 143 */       ClassFile cf = entry.getValue();
/*     */ 
/*     */       
/* 146 */       if (cf != null) {
/* 147 */         boolean inPkg = false;
/* 148 */         if (inPkg || Util.isPublic(cf.getAccessFlags())) {
/* 149 */           addTo.add(new ClassCompletion(provider, cf));
/*     */         }
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 155 */       String[] items = new String[pkgNames.length];
/* 156 */       System.arraycopy(pkgNames, 0, items, 0, pkgNames.length - 1);
/* 157 */       items[items.length - 1] = key;
/* 158 */       cf = getClassEntry(info, items);
/* 159 */       if (cf != null) {
/* 160 */         boolean inPkg = false;
/* 161 */         if (inPkg || Util.isPublic(cf.getAccessFlags())) {
/* 162 */           addTo.add(new ClassCompletion(provider, cf));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int clearClassFiles() {
/* 182 */     return clearClassFilesImpl(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int clearClassFilesImpl(PackageMapNode pmn) {
/* 188 */     int clearedCount = 0;
/*     */     
/* 190 */     for (Map.Entry<String, ClassFile> entry : pmn.classFiles.entrySet()) {
/* 191 */       entry.setValue(null);
/* 192 */       clearedCount++;
/*     */     } 
/*     */     
/* 195 */     for (Map.Entry<String, PackageMapNode> entry : pmn.subpackages.entrySet()) {
/* 196 */       clearedCount += clearClassFilesImpl(entry.getValue());
/*     */     }
/*     */     
/* 199 */     return clearedCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsClass(String className) {
/* 206 */     String[] items = className.split("\\.");
/*     */     
/* 208 */     PackageMapNode pmn = this;
/* 209 */     for (int i = 0; i < items.length - 1; i++) {
/*     */ 
/*     */ 
/*     */       
/* 213 */       pmn = pmn.subpackages.get(items[i]);
/* 214 */       if (pmn == null) {
/* 215 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 219 */     return pmn.classFiles.containsKey(items[items.length - 1]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsPackage(String pkgName) {
/* 226 */     String[] items = Util.splitOnChar(pkgName, 46);
/*     */     
/* 228 */     PackageMapNode pmn = this;
/* 229 */     for (int i = 0; i < items.length; i++) {
/*     */ 
/*     */ 
/*     */       
/* 233 */       pmn = pmn.subpackages.get(items[i]);
/* 234 */       if (pmn == null) {
/* 235 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 239 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassFile getClassEntry(LibraryInfo info, String[] items) {
/* 246 */     PackageMapNode pmn = this;
/* 247 */     for (int i = 0; i < items.length - 1; i++) {
/* 248 */       pmn = pmn.subpackages.get(items[i]);
/* 249 */       if (pmn == null) {
/* 250 */         return null;
/*     */       }
/*     */     } 
/*     */     
/* 254 */     String className = items[items.length - 1];
/* 255 */     if (pmn.classFiles.containsKey(className)) {
/* 256 */       ClassFile value = pmn.classFiles.get(className);
/* 257 */       if (value != null) {
/* 258 */         return value;
/*     */       }
/*     */       
/*     */       try {
/* 262 */         StringBuilder name = new StringBuilder(items[0]);
/* 263 */         for (int j = 1; j < items.length; j++) {
/* 264 */           name.append('/').append(items[j]);
/*     */         }
/* 266 */         name.append(".class");
/* 267 */         ClassFile cf = info.createClassFile(name.toString());
/* 268 */         pmn.classFiles.put(className, cf);
/* 269 */         return cf;
/* 270 */       } catch (IOException ioe) {
/* 271 */         ioe.printStackTrace();
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 276 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void getClassesInPackage(LibraryInfo info, List<ClassFile> addTo, String[] pkgs, boolean inPkg) {
/* 284 */     PackageMapNode map = this;
/*     */     
/* 286 */     for (int i = 0; i < pkgs.length; i++) {
/* 287 */       map = map.subpackages.get(pkgs[i]);
/* 288 */       if (map == null) {
/*     */         return;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 295 */       info.bulkClassFileCreationStart();
/*     */       
/*     */       try {
/* 298 */         for (Map.Entry<String, ClassFile> entry : map.classFiles.entrySet())
/*     */         {
/* 300 */           ClassFile cf = entry.getValue();
/* 301 */           if (cf == null) {
/* 302 */             StringBuilder name = new StringBuilder(pkgs[0]);
/* 303 */             for (int j = 1; j < pkgs.length; j++) {
/* 304 */               name.append('/').append(pkgs[j]);
/*     */             }
/* 306 */             name.append('/');
/* 307 */             name.append(entry.getKey()).append(".class");
/* 308 */             cf = info.createClassFileBulk(name.toString());
/* 309 */             map.classFiles.put(entry.getKey(), cf);
/* 310 */             possiblyAddTo(addTo, cf, inPkg);
/*     */             
/*     */             continue;
/*     */           } 
/* 314 */           possiblyAddTo(addTo, cf, inPkg);
/*     */         }
/*     */       
/*     */       } finally {
/*     */         
/* 319 */         info.bulkClassFileCreationEnd();
/*     */       }
/*     */     
/* 322 */     } catch (IOException ioe) {
/* 323 */       ioe.printStackTrace();
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
/*     */ 
/*     */ 
/*     */   
/*     */   void getClassesWithNamesStartingWith(LibraryInfo info, String prefix, String currentPkg, List<ClassFile> addTo) {
/* 343 */     int prefixLen = prefix.length();
/*     */     
/* 345 */     for (Map.Entry<String, PackageMapNode> children : this.subpackages.entrySet()) {
/* 346 */       String key = children.getKey();
/* 347 */       PackageMapNode child = children.getValue();
/* 348 */       child.getClassesWithNamesStartingWith(info, prefix, currentPkg + key + "/", addTo);
/*     */     } 
/*     */ 
/*     */     
/* 352 */     for (Map.Entry<String, ClassFile> cfEntry : this.classFiles.entrySet()) {
/*     */ 
/*     */ 
/*     */       
/* 356 */       String className = cfEntry.getKey();
/* 357 */       if (className.regionMatches(true, 0, prefix, 0, prefixLen)) {
/* 358 */         ClassFile cf = cfEntry.getValue();
/* 359 */         if (cf == null) {
/* 360 */           String fqClassName = currentPkg + className + ".class";
/*     */           try {
/* 362 */             cf = info.createClassFile(fqClassName);
/* 363 */             cfEntry.setValue(cf);
/* 364 */           } catch (IOException ioe) {
/* 365 */             ioe.printStackTrace();
/*     */           } 
/*     */         } 
/* 368 */         if (cf != null) {
/* 369 */           addTo.add(cf);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final void possiblyAddTo(Collection<ClassFile> addTo, ClassFile cf, boolean inPkg) {
/* 379 */     if (inPkg || Util.isPublic(cf.getAccessFlags()))
/* 380 */       addTo.add(cf); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\PackageMapNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */