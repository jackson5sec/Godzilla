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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class LibraryInfo
/*     */   implements Comparable<LibraryInfo>, Cloneable
/*     */ {
/*     */   private SourceLocation sourceLoc;
/*     */   
/*     */   public abstract void bulkClassFileCreationEnd() throws IOException;
/*     */   
/*     */   public abstract void bulkClassFileCreationStart() throws IOException;
/*     */   
/*     */   public Object clone() {
/*     */     try {
/*  91 */       return super.clone();
/*  92 */     } catch (CloneNotSupportedException cnse) {
/*  93 */       throw new IllegalStateException("Doesn't support cloning, but should! - " + 
/*  94 */           getClass().getName());
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
/*     */   public abstract ClassFile createClassFile(String paramString) throws IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract ClassFile createClassFileBulk(String paramString) throws IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract PackageMapNode createPackageMap() throws IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 157 */     return (o instanceof LibraryInfo && 
/* 158 */       compareTo((LibraryInfo)o) == 0);
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
/*     */   public static LibraryInfo getJreJarInfo(File jreHome) {
/*     */     File sourceZip;
/* 174 */     LibraryInfo info = null;
/*     */     
/* 176 */     File mainJar = new File(jreHome, "lib/rt.jar");
/*     */ 
/*     */     
/* 179 */     if (mainJar.isFile()) {
/* 180 */       sourceZip = new File(jreHome, "src.zip");
/* 181 */       if (!sourceZip.isFile())
/*     */       {
/* 183 */         sourceZip = new File(jreHome, "../src.zip");
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 188 */       mainJar = new File(jreHome, "../Classes/classes.jar");
/*     */       
/* 190 */       sourceZip = new File(jreHome, "src.jar");
/*     */     } 
/*     */     
/* 193 */     if (mainJar.isFile()) {
/* 194 */       info = new JarLibraryInfo(mainJar);
/* 195 */       if (sourceZip.isFile()) {
/* 196 */         info.setSourceLocation(new ZipSourceLocation(sourceZip));
/*     */       }
/*     */     } else {
/*     */       
/* 200 */       System.err.println("[ERROR]: Cannot locate JRE jar in " + jreHome
/* 201 */           .getAbsolutePath());
/* 202 */       mainJar = null;
/*     */     } 
/*     */     
/* 205 */     return info;
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
/*     */   public abstract long getLastModified();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String getLocationAsString();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LibraryInfo getMainJreJarInfo() {
/* 245 */     String javaHome = System.getProperty("java.home");
/* 246 */     return getJreJarInfo(new File(javaHome));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SourceLocation getSourceLocation() {
/* 257 */     return this.sourceLoc;
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
/*     */   public abstract int hashCode();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSourceLocation(SourceLocation sourceLoc) {
/* 280 */     this.sourceLoc = sourceLoc;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\buildpath\LibraryInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */