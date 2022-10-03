/*     */ package javassist;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class JarClassPath
/*     */   implements ClassPath
/*     */ {
/*     */   Set<String> jarfileEntries;
/*     */   String jarfileURL;
/*     */   
/*     */   JarClassPath(String pathname) throws NotFoundException {
/* 135 */     JarFile jarfile = null;
/*     */     
/* 137 */     try { jarfile = new JarFile(pathname);
/* 138 */       this.jarfileEntries = new HashSet<>();
/* 139 */       for (JarEntry je : Collections.<JarEntry>list(jarfile.entries())) {
/* 140 */         if (je.getName().endsWith(".class"))
/* 141 */           this.jarfileEntries.add(je.getName()); 
/* 142 */       }  this
/* 143 */         .jarfileURL = (new File(pathname)).getCanonicalFile().toURI().toURL().toString();
/*     */       return; }
/* 145 */     catch (IOException iOException) {  }
/*     */     finally
/* 147 */     { if (null != jarfile)
/*     */         try {
/* 149 */           jarfile.close();
/* 150 */         } catch (IOException iOException) {}  }
/*     */     
/* 152 */     throw new NotFoundException(pathname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream openClassfile(String classname) throws NotFoundException {
/* 159 */     URL jarURL = find(classname);
/* 160 */     if (null != jarURL) {
/*     */       try {
/* 162 */         if (ClassPool.cacheOpenedJarFile) {
/* 163 */           return jarURL.openConnection().getInputStream();
/*     */         }
/* 165 */         URLConnection con = jarURL.openConnection();
/* 166 */         con.setUseCaches(false);
/* 167 */         return con.getInputStream();
/*     */       
/*     */       }
/* 170 */       catch (IOException e) {
/* 171 */         throw new NotFoundException("broken jar file?: " + classname);
/*     */       } 
/*     */     }
/* 174 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public URL find(String classname) {
/* 179 */     String jarname = classname.replace('.', '/') + ".class";
/* 180 */     if (this.jarfileEntries.contains(jarname))
/*     */       try {
/* 182 */         return new URL(String.format("jar:%s!/%s", new Object[] { this.jarfileURL, jarname }));
/*     */       }
/* 184 */       catch (MalformedURLException malformedURLException) {} 
/* 185 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 190 */     return (this.jarfileURL == null) ? "<null>" : this.jarfileURL.toString();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\JarClassPath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */