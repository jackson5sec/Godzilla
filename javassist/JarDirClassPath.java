/*     */ package javassist;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FilenameFilter;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class JarDirClassPath
/*     */   implements ClassPath
/*     */ {
/*     */   JarClassPath[] jars;
/*     */   
/*     */   JarDirClassPath(String dirName) throws NotFoundException {
/*  90 */     File[] files = (new File(dirName)).listFiles(new FilenameFilter()
/*     */         {
/*     */           public boolean accept(File dir, String name) {
/*  93 */             name = name.toLowerCase();
/*  94 */             return (name.endsWith(".jar") || name.endsWith(".zip"));
/*     */           }
/*     */         });
/*     */     
/*  98 */     if (files != null) {
/*  99 */       this.jars = new JarClassPath[files.length];
/* 100 */       for (int i = 0; i < files.length; i++) {
/* 101 */         this.jars[i] = new JarClassPath(files[i].getPath());
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public InputStream openClassfile(String classname) throws NotFoundException {
/* 107 */     if (this.jars != null)
/* 108 */       for (int i = 0; i < this.jars.length; i++) {
/* 109 */         InputStream is = this.jars[i].openClassfile(classname);
/* 110 */         if (is != null) {
/* 111 */           return is;
/*     */         }
/*     */       }  
/* 114 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public URL find(String classname) {
/* 119 */     if (this.jars != null)
/* 120 */       for (int i = 0; i < this.jars.length; i++) {
/* 121 */         URL url = this.jars[i].find(classname);
/* 122 */         if (url != null) {
/* 123 */           return url;
/*     */         }
/*     */       }  
/* 126 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\JarDirClassPath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */