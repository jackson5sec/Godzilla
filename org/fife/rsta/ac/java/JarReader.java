/*     */ package org.fife.rsta.ac.java;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.fife.rsta.ac.java.buildpath.LibraryInfo;
/*     */ import org.fife.rsta.ac.java.classreader.ClassFile;
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
/*     */ class JarReader
/*     */ {
/*     */   private LibraryInfo info;
/*     */   private PackageMapNode packageMap;
/*     */   private long lastModified;
/*     */   
/*     */   public JarReader(LibraryInfo info) throws IOException {
/*  56 */     this.info = info;
/*  57 */     this.packageMap = new PackageMapNode();
/*  58 */     loadCompletions();
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
/*     */   public void addCompletions(CompletionProvider provider, String[] pkgNames, Set<Completion> addTo) {
/*  73 */     checkLastModified();
/*  74 */     this.packageMap.addCompletions(this.info, provider, pkgNames, addTo);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkLastModified() {
/*  85 */     long newLastModified = this.info.getLastModified();
/*  86 */     if (newLastModified != 0L && newLastModified != this.lastModified) {
/*     */       
/*  88 */       int count = this.packageMap.clearClassFiles();
/*  89 */       System.out.println("DEBUG: Cleared " + count + " cached ClassFiles");
/*  90 */       this.lastModified = newLastModified;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsClass(String className) {
/*  96 */     return this.packageMap.containsClass(className);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsPackage(String pkgName) {
/* 101 */     return this.packageMap.containsPackage(pkgName);
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassFile getClassEntry(String[] items) {
/* 106 */     return this.packageMap.getClassEntry(this.info, items);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getClassesInPackage(List<ClassFile> addTo, String[] pkgs, boolean inPkg) {
/* 112 */     this.packageMap.getClassesInPackage(this.info, addTo, pkgs, inPkg);
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
/*     */   public List<ClassFile> getClassesWithNamesStartingWith(String prefix) {
/* 128 */     List<ClassFile> res = new ArrayList<>();
/* 129 */     String currentPkg = "";
/* 130 */     this.packageMap.getClassesWithNamesStartingWith(this.info, prefix, currentPkg, res);
/*     */     
/* 132 */     return res;
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
/*     */   public LibraryInfo getLibraryInfo() {
/* 146 */     return (LibraryInfo)this.info.clone();
/*     */   }
/*     */ 
/*     */   
/*     */   private void loadCompletions() throws IOException {
/* 151 */     this.packageMap = this.info.createPackageMap();
/* 152 */     this.lastModified = this.info.getLastModified();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 158 */     return "[JarReader: " + getLibraryInfo() + "]";
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\JarReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */