/*     */ package org.fife.rsta.ac.java.buildpath;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.zip.ZipEntry;
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
/*     */ public class JarLibraryInfo
/*     */   extends LibraryInfo
/*     */ {
/*     */   private File jarFile;
/*     */   private JarFile bulkCreateJar;
/*     */   
/*     */   public JarLibraryInfo(String jarFile) {
/*  41 */     this(new File(jarFile));
/*     */   }
/*     */ 
/*     */   
/*     */   public JarLibraryInfo(File jarFile) {
/*  46 */     this(jarFile, (SourceLocation)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public JarLibraryInfo(File jarFile, SourceLocation sourceLoc) {
/*  51 */     setJarFile(jarFile);
/*  52 */     setSourceLocation(sourceLoc);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void bulkClassFileCreationEnd() {
/*     */     try {
/*  59 */       this.bulkCreateJar.close();
/*  60 */     } catch (IOException ioe) {
/*  61 */       ioe.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void bulkClassFileCreationStart() {
/*     */     try {
/*  69 */       this.bulkCreateJar = new JarFile(this.jarFile);
/*  70 */     } catch (IOException ioe) {
/*  71 */       ioe.printStackTrace();
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
/*     */   public int compareTo(LibraryInfo info) {
/*  85 */     if (info == this) {
/*  86 */       return 0;
/*     */     }
/*  88 */     int result = -1;
/*  89 */     if (info instanceof JarLibraryInfo) {
/*  90 */       result = this.jarFile.compareTo(((JarLibraryInfo)info).jarFile);
/*     */     }
/*  92 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassFile createClassFile(String entryName) throws IOException {
/*  98 */     try (JarFile jar = new JarFile(this.jarFile)) {
/*  99 */       return createClassFileImpl(jar, entryName);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassFile createClassFileBulk(String entryName) throws IOException {
/* 106 */     return createClassFileImpl(this.bulkCreateJar, entryName);
/*     */   }
/*     */ 
/*     */   
/*     */   private static ClassFile createClassFileImpl(JarFile jar, String entryName) throws IOException {
/*     */     ClassFile cf;
/* 112 */     JarEntry entry = (JarEntry)jar.getEntry(entryName);
/* 113 */     if (entry == null) {
/* 114 */       System.err.println("ERROR: Invalid entry: " + entryName);
/* 115 */       return null;
/*     */     } 
/*     */     
/* 118 */     DataInputStream in = new DataInputStream(new BufferedInputStream(jar.getInputStream(entry)));
/*     */     
/*     */     try {
/* 121 */       cf = new ClassFile(in);
/*     */     } finally {
/* 123 */       in.close();
/*     */     } 
/* 125 */     return cf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PackageMapNode createPackageMap() throws IOException {
/* 132 */     PackageMapNode root = new PackageMapNode();
/*     */     
/* 134 */     try (JarFile jar = new JarFile(this.jarFile)) {
/*     */       
/* 136 */       Enumeration<JarEntry> e = jar.entries();
/* 137 */       while (e.hasMoreElements()) {
/* 138 */         ZipEntry entry = e.nextElement();
/* 139 */         String entryName = entry.getName();
/* 140 */         if (entryName.endsWith(".class")) {
/* 141 */           root.add(entryName);
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 147 */     return root;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getLastModified() {
/* 154 */     return this.jarFile.lastModified();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLocationAsString() {
/* 160 */     return this.jarFile.getAbsolutePath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getJarFile() {
/* 170 */     return this.jarFile;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 176 */     return this.jarFile.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setJarFile(File jarFile) {
/* 186 */     if (jarFile == null || !jarFile.exists()) {
/* 187 */       String name = (jarFile == null) ? "null" : jarFile.getAbsolutePath();
/* 188 */       throw new IllegalArgumentException("Jar does not exist: " + name);
/*     */     } 
/* 190 */     this.jarFile = jarFile;
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
/* 202 */     return "[JarLibraryInfo: jar=" + this.jarFile
/* 203 */       .getAbsolutePath() + "; source=" + 
/* 204 */       getSourceLocation() + "]";
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\buildpath\JarLibraryInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */