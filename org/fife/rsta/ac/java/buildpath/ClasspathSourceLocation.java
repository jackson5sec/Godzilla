/*    */ package org.fife.rsta.ac.java.buildpath;
/*    */ 
/*    */ import java.io.InputStream;
/*    */ import java.io.InputStreamReader;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ClasspathSourceLocation
/*    */   implements SourceLocation
/*    */ {
/*    */   public CompilationUnit getCompilationUnit(ClassFile cf) {
/* 48 */     CompilationUnit cu = null;
/*    */     
/* 50 */     String res = cf.getClassName(true).replace('.', '/') + ".java";
/* 51 */     InputStream in = getClass().getClassLoader().getResourceAsStream(res);
/* 52 */     if (in != null) {
/* 53 */       Scanner s = new Scanner(new InputStreamReader(in));
/* 54 */       cu = (new ASTFactory()).getCompilationUnit(res, s);
/*    */     } 
/*    */     
/* 57 */     return cu;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getLocationAsString() {
/* 67 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\buildpath\ClasspathSourceLocation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */