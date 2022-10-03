/*     */ package org.fife.rsta.ac.java.buildpath;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import org.fife.rsta.ac.java.PackageMapNode;
/*     */ import org.fife.rsta.ac.java.classreader.ClassFile;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DirLibraryInfo
/*     */   extends LibraryInfo
/*     */ {
/*     */   private File dir;
/*     */   
/*     */   public DirLibraryInfo(File dir) {
/*  37 */     this(dir, (SourceLocation)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public DirLibraryInfo(String dir) {
/*  42 */     this(new File(dir));
/*     */   }
/*     */ 
/*     */   
/*     */   public DirLibraryInfo(File dir, SourceLocation sourceLoc) {
/*  47 */     setDirectory(dir);
/*  48 */     setSourceLocation(sourceLoc);
/*     */   }
/*     */ 
/*     */   
/*     */   public DirLibraryInfo(String dir, SourceLocation sourceLoc) {
/*  53 */     this(new File(dir), sourceLoc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void bulkClassFileCreationEnd() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void bulkClassFileCreationStart() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(LibraryInfo info) {
/*  78 */     if (info == this) {
/*  79 */       return 0;
/*     */     }
/*  81 */     int result = -1;
/*  82 */     if (info instanceof DirLibraryInfo) {
/*  83 */       return this.dir.compareTo(((DirLibraryInfo)info).dir);
/*     */     }
/*  85 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassFile createClassFile(String entryName) throws IOException {
/*  91 */     return createClassFileBulk(entryName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassFile createClassFileBulk(String entryName) throws IOException {
/*  97 */     File file = new File(this.dir, entryName);
/*  98 */     if (!file.isFile()) {
/*  99 */       System.err.println("ERROR: Invalid class file: " + file.getAbsolutePath());
/* 100 */       return null;
/*     */     } 
/* 102 */     return new ClassFile(file);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PackageMapNode createPackageMap() {
/* 108 */     PackageMapNode root = new PackageMapNode();
/* 109 */     getPackageMapImpl(this.dir, (String)null, root);
/* 110 */     return root;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getLastModified() {
/* 116 */     return this.dir.lastModified();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLocationAsString() {
/* 122 */     return this.dir.getAbsolutePath();
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
/*     */   private void getPackageMapImpl(File dir, String pkg, PackageMapNode root) {
/* 135 */     File[] children = dir.listFiles();
/*     */     
/* 137 */     for (File child : children) {
/* 138 */       if (child.isFile() && child.getName().endsWith(".class")) {
/* 139 */         if (pkg != null) {
/*     */ 
/*     */           
/* 142 */           root.add(pkg + "/" + child.getName());
/*     */         } else {
/*     */           
/* 145 */           root.add(child.getName());
/*     */         }
/*     */       
/* 148 */       } else if (child.isDirectory()) {
/*     */         
/* 150 */         String subpkg = (pkg == null) ? child.getName() : (pkg + "/" + child.getName());
/* 151 */         getPackageMapImpl(child, subpkg, root);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 160 */     return this.dir.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setDirectory(File dir) {
/* 170 */     if (dir == null || !dir.isDirectory()) {
/* 171 */       String name = (dir == null) ? "null" : dir.getAbsolutePath();
/* 172 */       throw new IllegalArgumentException("Directory does not exist: " + name);
/*     */     } 
/* 174 */     this.dir = dir;
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
/*     */   public String toString() {
/* 186 */     return "[DirLibraryInfo: jar=" + this.dir
/* 187 */       .getAbsolutePath() + "; source=" + 
/* 188 */       getSourceLocation() + "]";
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\buildpath\DirLibraryInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */