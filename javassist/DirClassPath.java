/*    */ package javassist;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.net.MalformedURLException;
/*    */ import java.net.URL;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class DirClassPath
/*    */   implements ClassPath
/*    */ {
/*    */   String directory;
/*    */   
/*    */   DirClassPath(String dirName) {
/* 48 */     this.directory = dirName;
/*    */   }
/*    */ 
/*    */   
/*    */   public InputStream openClassfile(String classname) {
/*    */     
/* 54 */     try { char sep = File.separatorChar;
/*    */       
/* 56 */       String filename = this.directory + sep + classname.replace('.', sep) + ".class";
/* 57 */       return new FileInputStream(filename.toString()); }
/*    */     
/* 59 */     catch (FileNotFoundException fileNotFoundException) {  }
/* 60 */     catch (SecurityException securityException) {}
/* 61 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public URL find(String classname) {
/* 66 */     char sep = File.separatorChar;
/*    */     
/* 68 */     String filename = this.directory + sep + classname.replace('.', sep) + ".class";
/* 69 */     File f = new File(filename);
/* 70 */     if (f.exists()) {
/*    */       
/* 72 */       try { return f.getCanonicalFile().toURI().toURL(); }
/*    */       
/* 74 */       catch (MalformedURLException malformedURLException) {  }
/* 75 */       catch (IOException iOException) {}
/*    */     }
/* 77 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 82 */     return this.directory;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\DirClassPath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */