/*    */ package org.fife.rsta.ac.java.buildpath;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.InputStreamReader;
/*    */ import java.util.zip.ZipEntry;
/*    */ import java.util.zip.ZipFile;
/*    */ import org.fife.rsta.ac.java.classreader.ClassFile;
/*    */ import org.fife.rsta.ac.java.rjc.ast.CompilationUnit;
/*    */ import org.fife.rsta.ac.java.rjc.lexer.Scanner;
/*    */ import org.fife.rsta.ac.java.rjc.parser.ASTFactory;
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
/*    */ public class ZipSourceLocation
/*    */   implements SourceLocation
/*    */ {
/*    */   private File archive;
/*    */   
/*    */   public ZipSourceLocation(String archive) {
/* 47 */     this(new File(archive));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ZipSourceLocation(File archive) {
/* 57 */     this.archive = archive;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CompilationUnit getCompilationUnit(ClassFile cf) throws IOException {
/* 67 */     CompilationUnit cu = null;
/*    */     
/* 69 */     try (ZipFile zipFile = new ZipFile(this.archive)) {
/*    */       
/* 71 */       String entryName = cf.getClassName(true).replaceAll("\\.", "/");
/* 72 */       entryName = entryName + ".java";
/*    */       
/* 74 */       ZipEntry entry = zipFile.getEntry(entryName);
/* 75 */       if (entry == null)
/*    */       {
/* 77 */         entry = zipFile.getEntry("src/" + entryName);
/*    */       }
/*    */       
/* 80 */       if (entry != null) {
/* 81 */         InputStream in = zipFile.getInputStream(entry);
/* 82 */         Scanner s = new Scanner(new InputStreamReader(in));
/* 83 */         cu = (new ASTFactory()).getCompilationUnit(entryName, s);
/*    */       } 
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 89 */     return cu;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getLocationAsString() {
/* 99 */     return this.archive.getAbsolutePath();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\buildpath\ZipSourceLocation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */