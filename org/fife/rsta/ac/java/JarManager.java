/*     */ package org.fife.rsta.ac.java;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.fife.rsta.ac.java.buildpath.JarLibraryInfo;
/*     */ import org.fife.rsta.ac.java.buildpath.LibraryInfo;
/*     */ import org.fife.rsta.ac.java.buildpath.SourceLocation;
/*     */ import org.fife.rsta.ac.java.classreader.ClassFile;
/*     */ import org.fife.rsta.ac.java.classreader.Util;
/*     */ import org.fife.rsta.ac.java.rjc.ast.ImportDeclaration;
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
/*     */ public class JarManager
/*     */ {
/*     */   private List<JarReader> classFileSources;
/*     */   private static boolean checkModified;
/*     */   
/*     */   public JarManager() {
/*  54 */     this.classFileSources = new ArrayList<>();
/*  55 */     setCheckModifiedDatestamps(true);
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
/*     */   public void addCompletions(CompletionProvider p, String text, Set<Completion> addTo) {
/*  80 */     if (text.length() == 0) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  85 */     if (text.indexOf('.') > -1) {
/*  86 */       String[] pkgNames = Util.splitOnChar(text, 46);
/*  87 */       for (int i = 0; i < this.classFileSources.size(); i++) {
/*  88 */         JarReader jar = this.classFileSources.get(i);
/*  89 */         jar.addCompletions(p, pkgNames, addTo);
/*     */ 
/*     */       
/*     */       }
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/*  99 */       String lowerCaseText = text.toLowerCase();
/* 100 */       for (int i = 0; i < this.classFileSources.size(); i++) {
/* 101 */         JarReader jar = this.classFileSources.get(i);
/*     */         
/* 103 */         List<ClassFile> classFiles = jar.getClassesWithNamesStartingWith(lowerCaseText);
/* 104 */         if (classFiles != null) {
/* 105 */           for (ClassFile cf : classFiles) {
/* 106 */             if (Util.isPublic(cf.getAccessFlags())) {
/* 107 */               addTo.add(new ClassCompletion(p, cf));
/*     */             }
/*     */           } 
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
/*     */ 
/*     */   
/*     */   public boolean addClassFileSource(File jarFile) throws IOException {
/* 131 */     if (jarFile == null) {
/* 132 */       throw new IllegalArgumentException("jarFile cannot be null");
/*     */     }
/* 134 */     return addClassFileSource((LibraryInfo)new JarLibraryInfo(jarFile));
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
/*     */   public boolean addClassFileSource(LibraryInfo info) throws IOException {
/* 156 */     if (info == null) {
/* 157 */       throw new IllegalArgumentException("info cannot be null");
/*     */     }
/*     */ 
/*     */     
/* 161 */     for (int i = 0; i < this.classFileSources.size(); i++) {
/* 162 */       JarReader jar = this.classFileSources.get(i);
/* 163 */       LibraryInfo info2 = jar.getLibraryInfo();
/* 164 */       if (info2.equals(info)) {
/*     */         
/* 166 */         SourceLocation source = info.getSourceLocation();
/* 167 */         SourceLocation source2 = info2.getSourceLocation();
/* 168 */         if ((source == null && source2 != null) || (source != null && 
/* 169 */           !source.equals(source2))) {
/* 170 */           this.classFileSources.set(i, new JarReader((LibraryInfo)info.clone()));
/* 171 */           return true;
/*     */         } 
/* 173 */         return false;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 178 */     this.classFileSources.add(new JarReader(info));
/* 179 */     return true;
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
/*     */   public void addCurrentJreClassFileSource() throws IOException {
/* 193 */     addClassFileSource(LibraryInfo.getMainJreJarInfo());
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
/*     */   public void clearClassFileSources() {
/* 206 */     this.classFileSources.clear();
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
/*     */   public static boolean getCheckModifiedDatestamps() {
/* 226 */     return checkModified;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassFile getClassEntry(String className) {
/* 232 */     String[] items = Util.splitOnChar(className, 46);
/*     */     
/* 234 */     for (int i = 0; i < this.classFileSources.size(); i++) {
/* 235 */       JarReader jar = this.classFileSources.get(i);
/* 236 */       ClassFile cf = jar.getClassEntry(items);
/* 237 */       if (cf != null) {
/* 238 */         return cf;
/*     */       }
/*     */     } 
/*     */     
/* 242 */     return null;
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
/*     */   public List<ClassFile> getClassesWithUnqualifiedName(String name, List<ImportDeclaration> importDeclarations) {
/* 260 */     List<ClassFile> result = null;
/*     */ 
/*     */     
/* 263 */     for (ImportDeclaration idec : importDeclarations) {
/*     */ 
/*     */       
/* 266 */       if (!idec.isStatic()) {
/*     */ 
/*     */         
/* 269 */         if (idec.isWildcard()) {
/*     */           
/* 271 */           String str = idec.getName();
/* 272 */           str = str.substring(0, str.indexOf('*'));
/* 273 */           str = str + name;
/* 274 */           ClassFile classFile = getClassEntry(str);
/* 275 */           if (classFile != null) {
/* 276 */             if (result == null) {
/* 277 */               result = new ArrayList<>(1);
/*     */             }
/* 279 */             result.add(classFile);
/*     */           } 
/*     */ 
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/* 286 */         String name2 = idec.getName();
/* 287 */         String unqualifiedName2 = name2.substring(name2.lastIndexOf('.') + 1);
/* 288 */         if (unqualifiedName2.equals(name)) {
/* 289 */           ClassFile classFile = getClassEntry(name2);
/* 290 */           if (classFile != null) {
/* 291 */             if (result == null) {
/* 292 */               result = new ArrayList<>(1);
/*     */             }
/* 294 */             result.add(classFile);
/*     */             continue;
/*     */           } 
/* 297 */           System.err.println("ERROR: Class not found! - " + name2);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 307 */     String qualified = "java.lang." + name;
/* 308 */     ClassFile entry = getClassEntry(qualified);
/* 309 */     if (entry != null) {
/* 310 */       if (result == null) {
/* 311 */         result = new ArrayList<>(1);
/*     */       }
/* 313 */       result.add(entry);
/*     */     } 
/*     */     
/* 316 */     return result;
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
/*     */   public List<ClassFile> getClassesInPackage(String pkgName, boolean inPkg) {
/* 328 */     List<ClassFile> list = new ArrayList<>();
/* 329 */     String[] pkgs = Util.splitOnChar(pkgName, 46);
/*     */     
/* 331 */     for (int i = 0; i < this.classFileSources.size(); i++) {
/* 332 */       JarReader jar = this.classFileSources.get(i);
/* 333 */       jar.getClassesInPackage(list, pkgs, inPkg);
/*     */     } 
/*     */     
/* 336 */     return list;
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
/*     */   public List<LibraryInfo> getClassFileSources() {
/* 353 */     List<LibraryInfo> jarList = new ArrayList<>(this.classFileSources.size());
/* 354 */     for (JarReader reader : this.classFileSources) {
/* 355 */       jarList.add(reader.getLibraryInfo());
/*     */     }
/* 357 */     return jarList;
/*     */   }
/*     */ 
/*     */   
/*     */   public SourceLocation getSourceLocForClass(String className) {
/* 362 */     SourceLocation sourceLoc = null;
/* 363 */     for (int i = 0; i < this.classFileSources.size(); i++) {
/* 364 */       JarReader jar = this.classFileSources.get(i);
/* 365 */       if (jar.containsClass(className)) {
/* 366 */         sourceLoc = jar.getLibraryInfo().getSourceLocation();
/*     */         break;
/*     */       } 
/*     */     } 
/* 370 */     return sourceLoc;
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
/*     */   public boolean removeClassFileSource(File jar) {
/* 386 */     return removeClassFileSource((LibraryInfo)new JarLibraryInfo(jar));
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
/*     */   public boolean removeClassFileSource(LibraryInfo toRemove) {
/* 401 */     for (Iterator<JarReader> i = this.classFileSources.iterator(); i.hasNext(); ) {
/* 402 */       JarReader reader = i.next();
/* 403 */       LibraryInfo info = reader.getLibraryInfo();
/* 404 */       if (info.equals(toRemove)) {
/* 405 */         i.remove();
/* 406 */         return true;
/*     */       } 
/*     */     } 
/* 409 */     return false;
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
/*     */   public static void setCheckModifiedDatestamps(boolean check) {
/* 429 */     checkModified = check;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\JarManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */