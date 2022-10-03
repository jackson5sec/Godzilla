/*     */ package org.fife.rsta.ac.java.buildpath;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class ClasspathLibraryInfo
/*     */   extends LibraryInfo
/*     */ {
/*     */   private Map<String, ClassFile> classNameToClassFile;
/*     */   
/*     */   public ClasspathLibraryInfo(String[] classes) {
/*  68 */     this(Arrays.asList(classes), null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClasspathLibraryInfo(List<String> classes) {
/*  79 */     this(classes, null);
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
/*     */   public ClasspathLibraryInfo(List<String> classes, SourceLocation sourceLoc) {
/*  92 */     setSourceLocation(sourceLoc);
/*  93 */     this.classNameToClassFile = new HashMap<>();
/*  94 */     int count = (classes == null) ? 0 : classes.size();
/*  95 */     for (int i = 0; i < count; i++) {
/*     */ 
/*     */       
/*  98 */       String entryName = classes.get(i);
/*  99 */       entryName = entryName.replace('.', '/') + ".class";
/* 100 */       this.classNameToClassFile.put(entryName, null);
/*     */     } 
/*     */   }
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
/*     */   public void bulkClassFileCreationStart() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(LibraryInfo info) {
/* 120 */     if (info == this) {
/* 121 */       return 0;
/*     */     }
/* 123 */     int res = -1;
/*     */     
/* 125 */     if (info instanceof ClasspathLibraryInfo) {
/* 126 */       ClasspathLibraryInfo other = (ClasspathLibraryInfo)info;
/*     */       
/* 128 */       res = this.classNameToClassFile.size() - other.classNameToClassFile.size();
/* 129 */       if (res == 0) {
/* 130 */         for (String key : this.classNameToClassFile.keySet()) {
/* 131 */           if (!other.classNameToClassFile.containsKey(key)) {
/* 132 */             res = -1;
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/* 139 */     return res;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassFile createClassFile(String entryName) throws IOException {
/* 146 */     return createClassFileBulk(entryName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassFile createClassFileBulk(String entryName) throws IOException {
/* 154 */     ClassFile cf = null;
/* 155 */     if (this.classNameToClassFile.containsKey(entryName)) {
/* 156 */       cf = this.classNameToClassFile.get(entryName);
/* 157 */       if (cf == null) {
/* 158 */         cf = createClassFileImpl(entryName);
/* 159 */         this.classNameToClassFile.put(entryName, cf);
/*     */       } 
/*     */     } 
/* 162 */     return cf;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private ClassFile createClassFileImpl(String res) throws IOException {
/* 168 */     ClassFile cf = null;
/*     */     
/* 170 */     InputStream in = getClass().getClassLoader().getResourceAsStream(res);
/* 171 */     if (in != null) {
/*     */       
/*     */       try {
/* 174 */         BufferedInputStream bin = new BufferedInputStream(in);
/* 175 */         DataInputStream din = new DataInputStream(bin);
/* 176 */         cf = new ClassFile(din);
/*     */       } finally {
/* 178 */         in.close();
/*     */       } 
/*     */     }
/*     */     
/* 182 */     return cf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PackageMapNode createPackageMap() {
/* 189 */     PackageMapNode root = new PackageMapNode();
/* 190 */     for (String className : this.classNameToClassFile.keySet()) {
/* 191 */       root.add(className);
/*     */     }
/* 193 */     return root;
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
/*     */   public long getLastModified() {
/* 205 */     return 0L;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLocationAsString() {
/* 211 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 217 */     return this.classNameToClassFile.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\buildpath\ClasspathLibraryInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */