/*    */ package org.fife.rsta.ac.java.buildpath;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.File;
/*    */ import java.io.FileReader;
/*    */ import java.io.IOException;
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
/*    */ public class DirSourceLocation
/*    */   implements SourceLocation
/*    */ {
/*    */   private File dir;
/*    */   
/*    */   public DirSourceLocation(String dir) {
/* 41 */     this(new File(dir));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DirSourceLocation(File dir) {
/* 51 */     this.dir = dir;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CompilationUnit getCompilationUnit(ClassFile cf) throws IOException {
/* 61 */     CompilationUnit cu = null;
/*    */     
/* 63 */     String entryName = cf.getClassName(true);
/* 64 */     entryName = entryName.replace('.', '/');
/* 65 */     entryName = entryName + ".java";
/*    */     
/* 67 */     File file = new File(this.dir, entryName);
/* 68 */     if (!file.isFile())
/*    */     {
/* 70 */       file = new File(this.dir, "src/" + entryName);
/*    */     }
/*    */     
/* 73 */     if (file.isFile()) {
/* 74 */       try (BufferedReader r = new BufferedReader(new FileReader(file))) {
/* 75 */         Scanner s = new Scanner(r);
/* 76 */         cu = (new ASTFactory()).getCompilationUnit(entryName, s);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/* 81 */     return cu;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getLocationAsString() {
/* 91 */     return this.dir.getAbsolutePath();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\buildpath\DirSourceLocation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */