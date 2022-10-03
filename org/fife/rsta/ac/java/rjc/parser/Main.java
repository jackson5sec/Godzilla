/*     */ package org.fife.rsta.ac.java.rjc.parser;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.fife.rsta.ac.java.rjc.lexer.Scanner;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Main
/*     */ {
/*     */   public static final String PROPERTY_NO_OUTPUT = "no.output";
/*  39 */   private static final boolean LOG = !"true".equals(
/*  40 */       System.getProperty("no.output"));
/*     */ 
/*     */   
/*     */   private static void log(Object text) {
/*  44 */     if (LOG) {
/*  45 */       System.out.println(text);
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
/*     */   public static void main(String[] args) throws IOException {
/*  58 */     PrintStream oldOut = System.out;
/*  59 */     PrintStream oldErr = System.err;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  64 */     ASTFactory fact = new ASTFactory();
/*     */ 
/*     */     
/*  67 */     List<File> toDo = new ArrayList<>();
/*     */     
/*  69 */     if (args.length > 0) {
/*  70 */       toDo.add(new File(args[0]));
/*     */     }
/*     */     else {
/*     */       
/*  74 */       File rootDir = new File("C:/java/32/jdk1.6.0_16/src/");
/*     */ 
/*     */       
/*  77 */       File[] files = rootDir.listFiles();
/*  78 */       Collections.addAll(toDo, files);
/*     */     } 
/*     */     
/*  81 */     int count = 0;
/*  82 */     int typeParamCount = 0;
/*  83 */     int annotationTypeDecCount = 0;
/*  84 */     long entireStart = System.currentTimeMillis();
/*     */     
/*  86 */     for (int i = 0; i < toDo.size(); i++) {
/*     */       
/*  88 */       File file = toDo.get(i);
/*     */       
/*  90 */       if (file.isDirectory()) {
/*  91 */         File[] contents = file.listFiles();
/*  92 */         Collections.addAll(toDo, contents);
/*     */       
/*     */       }
/*  95 */       else if (file.getName().endsWith(".java")) {
/*     */ 
/*     */ 
/*     */         
/*  99 */         BufferedReader r = new BufferedReader(new FileReader(file));
/* 100 */         Scanner scanner = new Scanner(r);
/* 101 */         long start = System.currentTimeMillis();
/*     */         try {
/* 103 */           fact.getCompilationUnit(file.getName(), scanner);
/* 104 */           long time = System.currentTimeMillis() - start;
/*     */           
/* 106 */           log(file.getAbsolutePath() + " (" + file.length() + "): " + time + " ms");
/* 107 */         } catch (InternalError ie) {
/* 108 */           System.err.println(file.getAbsolutePath());
/* 109 */           ie.printStackTrace();
/* 110 */           System.exit(1);
/*     */         } 
/* 112 */         count++;
/* 113 */         r.close();
/*     */       } 
/*     */     } 
/*     */     
/* 117 */     long entireTime = System.currentTimeMillis() - entireStart;
/* 118 */     log(count + " files parsed");
/* 119 */     log("TypeParameter errors: " + typeParamCount);
/* 120 */     log("AnnotationTypeDeclaration errors: " + annotationTypeDecCount);
/* 121 */     log(entireTime + " ms");
/* 122 */     System.setOut(oldOut);
/* 123 */     System.setErr(oldErr);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\rjc\parser\Main.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */