/*     */ package org.fife.rsta.ac.js.ast.parser;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import org.fife.rsta.ac.js.JavaScriptHelper;
/*     */ import org.fife.rsta.ac.js.SourceCompletionProvider;
/*     */ import org.fife.rsta.ac.js.ast.CodeBlock;
/*     */ import org.fife.rsta.ac.js.ast.TypeDeclarationOptions;
/*     */ import org.fife.rsta.ac.js.ast.jsType.JavaScriptTypesFactory;
/*     */ import org.fife.rsta.ac.js.ast.jsType.RhinoJavaScriptTypesFactory;
/*     */ import org.fife.ui.autocomplete.Completion;
/*     */ import org.mozilla.javascript.ast.AstNode;
/*     */ import org.mozilla.javascript.ast.AstRoot;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RhinoJavaScriptAstParser
/*     */   extends JavaScriptAstParser
/*     */ {
/*     */   public static final String PACKAGES = "Packages.";
/*  28 */   private LinkedHashSet<String> importClasses = new LinkedHashSet<>();
/*  29 */   private LinkedHashSet<String> importPackages = new LinkedHashSet<>();
/*     */ 
/*     */   
/*     */   public RhinoJavaScriptAstParser(SourceCompletionProvider provider, int dot, TypeDeclarationOptions options) {
/*  33 */     super(provider, dot, options);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearImportCache(SourceCompletionProvider provider) {
/*  41 */     JavaScriptTypesFactory typesFactory = provider.getJavaScriptTypesFactory();
/*  42 */     if (typesFactory instanceof RhinoJavaScriptTypesFactory) {
/*  43 */       ((RhinoJavaScriptTypesFactory)typesFactory).clearImportCache();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CodeBlock convertAstNodeToCodeBlock(AstRoot root, Set<Completion> set, String entered) {
/*     */     try {
/*  52 */       return super.convertAstNodeToCodeBlock(root, set, entered);
/*     */     } finally {
/*     */       
/*  55 */       mergeImportCache(this.importPackages, this.importClasses);
/*     */       
/*  57 */       this.importClasses.clear();
/*  58 */       this.importPackages.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void mergeImportCache(HashSet<String> packages, HashSet<String> classes) {
/*  63 */     JavaScriptTypesFactory typesFactory = this.provider.getJavaScriptTypesFactory();
/*  64 */     if (typesFactory instanceof RhinoJavaScriptTypesFactory) {
/*  65 */       ((RhinoJavaScriptTypesFactory)typesFactory).mergeImports(packages, classes);
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
/*     */   protected void iterateNode(AstNode child, Set<Completion> set, String entered, CodeBlock block, int offset) {
/*     */     boolean importFound;
/*  79 */     switch (child.getType()) {
/*     */       case 134:
/*  81 */         importFound = processImportNode(child, set, entered, block, offset);
/*  82 */         if (importFound) {
/*     */           return;
/*     */         }
/*     */         break;
/*     */     } 
/*  87 */     super.iterateNode(child, set, entered, block, offset);
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
/*     */   private boolean processImportNode(AstNode child, Set<Completion> set, String entered, CodeBlock block, int offset) {
/* 103 */     String src = JavaScriptHelper.convertNodeToSource(child);
/* 104 */     if (src != null) {
/* 105 */       if (src.startsWith("importPackage")) {
/* 106 */         processImportPackage(src);
/* 107 */         return true;
/*     */       } 
/* 109 */       if (src.startsWith("importClass")) {
/* 110 */         processImportClass(src);
/* 111 */         return true;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 116 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String removePackages(String src) {
/* 121 */     if (src.startsWith("Packages.")) {
/*     */       
/* 123 */       String pkg = src.substring("Packages.".length());
/* 124 */       if (pkg != null) {
/* 125 */         StringBuilder sb = new StringBuilder();
/*     */         
/* 127 */         char[] chars = pkg.toCharArray();
/* 128 */         for (int i = 0; i < chars.length; i++) {
/* 129 */           char ch = chars[i];
/* 130 */           if (Character.isJavaIdentifierPart(ch) || ch == '.') {
/* 131 */             sb.append(ch);
/*     */           }
/*     */         } 
/* 134 */         if (sb.length() > 0) {
/* 135 */           return sb.toString();
/*     */         }
/*     */       } 
/*     */     } 
/* 139 */     return src;
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
/*     */   private String extractNameFromSrc(String src) {
/* 152 */     int startIndex = src.indexOf("(");
/* 153 */     int endIndex = src.indexOf(")");
/* 154 */     if (startIndex != -1 && endIndex != -1) {
/* 155 */       return removePackages(src.substring(startIndex + 1, endIndex));
/*     */     }
/* 157 */     return removePackages(src);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processImportPackage(String src) {
/* 165 */     String pkg = extractNameFromSrc(src);
/* 166 */     this.importPackages.add(pkg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processImportClass(String src) {
/* 174 */     String cls = extractNameFromSrc(src);
/* 175 */     this.importClasses.add(cls);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\ast\parser\RhinoJavaScriptAstParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */