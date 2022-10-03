/*     */ package org.fife.rsta.ac.java.buildpath;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClassEnumerationReader
/*     */ {
/*     */   public static List<String> getClassNames(InputStream in) throws IOException {
/*  70 */     String lastPkg = null;
/*     */     
/*  72 */     List<String> classNames = new ArrayList<>();
/*     */     
/*  74 */     try (BufferedReader r = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
/*     */       String line;
/*  76 */       while ((line = r.readLine()) != null) {
/*     */ 
/*     */         
/*  79 */         line = line.trim();
/*  80 */         if (line.length() == 0 || line.charAt(0) == '#') {
/*     */           continue;
/*     */         }
/*     */ 
/*     */         
/*  85 */         if (line.charAt(0) == '-') {
/*  86 */           line = line.substring(1).trim();
/*  87 */           classNames.add(line);
/*  88 */           int lastDot = line.lastIndexOf('.');
/*  89 */           lastPkg = line.substring(0, lastDot + 1);
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/*  94 */         String className = line;
/*  95 */         if (lastPkg != null) {
/*  96 */           className = lastPkg + className;
/*     */         }
/*  98 */         classNames.add(className);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 105 */     return classNames;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\buildpath\ClassEnumerationReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */