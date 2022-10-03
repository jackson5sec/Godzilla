/*     */ package org.fife.rsta.ac.js.resolver;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import org.fife.rsta.ac.java.classreader.ClassFile;
/*     */ import org.fife.rsta.ac.js.JavaScriptHelper;
/*     */ import org.fife.rsta.ac.js.JavaScriptParser;
/*     */ import org.fife.rsta.ac.js.Logger;
/*     */ import org.fife.rsta.ac.js.SourceCompletionProvider;
/*     */ import org.fife.rsta.ac.js.ast.JavaScriptFunctionDeclaration;
/*     */ import org.fife.rsta.ac.js.ast.jsType.JavaScriptType;
/*     */ import org.fife.rsta.ac.js.ast.type.TypeDeclaration;
/*     */ import org.fife.rsta.ac.js.completion.JSCompletion;
/*     */ import org.fife.rsta.ac.js.completion.JSMethodData;
/*     */ import org.fife.ui.autocomplete.DefaultCompletionProvider;
/*     */ import org.mozilla.javascript.CompilerEnvirons;
/*     */ import org.mozilla.javascript.ErrorReporter;
/*     */ import org.mozilla.javascript.Parser;
/*     */ import org.mozilla.javascript.ast.AstNode;
/*     */ import org.mozilla.javascript.ast.AstRoot;
/*     */ import org.mozilla.javascript.ast.ExpressionStatement;
/*     */ import org.mozilla.javascript.ast.FunctionCall;
/*     */ import org.mozilla.javascript.ast.Name;
/*     */ import org.mozilla.javascript.ast.NodeVisitor;
/*     */ import org.mozilla.javascript.ast.PropertyGet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JavaScriptCompletionResolver
/*     */   extends JavaScriptResolver
/*     */ {
/*     */   protected JavaScriptType lastJavaScriptType;
/*  45 */   protected String lastLookupName = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaScriptCompletionResolver(SourceCompletionProvider provider) {
/*  53 */     super(provider);
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
/*     */   public JavaScriptType compileText(String text) throws IOException {
/*  67 */     CompilerEnvirons env = JavaScriptParser.createCompilerEnvironment((ErrorReporter)new JavaScriptParser.JSErrorReporter(), this.provider.getLanguageSupport());
/*     */     
/*  69 */     String parseText = JavaScriptHelper.removeLastDotFromText(text);
/*     */     
/*  71 */     int charIndex = JavaScriptHelper.findIndexOfFirstOpeningBracket(parseText);
/*  72 */     env.setRecoverFromErrors(true);
/*  73 */     Parser parser = new Parser(env);
/*  74 */     StringReader r = new StringReader(parseText);
/*  75 */     AstRoot root = parser.parse(r, null, 0);
/*  76 */     CompilerNodeVisitor visitor = new CompilerNodeVisitor((charIndex == 0));
/*  77 */     root.visitAll(visitor);
/*  78 */     return this.lastJavaScriptType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeDeclaration resolveParamNode(String text) throws IOException {
/*  89 */     if (text != null) {
/*  90 */       CompilerEnvirons env = JavaScriptParser.createCompilerEnvironment((ErrorReporter)new JavaScriptParser.JSErrorReporter(), this.provider.getLanguageSupport());
/*     */ 
/*     */       
/*  93 */       int charIndex = JavaScriptHelper.findIndexOfFirstOpeningBracket(text);
/*  94 */       env.setRecoverFromErrors(true);
/*  95 */       Parser parser = new Parser(env);
/*  96 */       StringReader r = new StringReader(text);
/*  97 */       AstRoot root = parser.parse(r, null, 0);
/*  98 */       CompilerNodeVisitor visitor = new CompilerNodeVisitor((charIndex == 0));
/*  99 */       root.visitAll(visitor);
/*     */     } 
/*     */     
/* 102 */     return (this.lastJavaScriptType != null) ? this.lastJavaScriptType.getType() : this.provider
/* 103 */       .getTypesFactory().getDefaultTypeDeclaration();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeDeclaration resolveNode(AstNode node) {
/* 113 */     if (node == null) return this.provider.getTypesFactory().getDefaultTypeDeclaration(); 
/* 114 */     CompilerNodeVisitor visitor = new CompilerNodeVisitor(true);
/* 115 */     node.visit(visitor);
/* 116 */     return (this.lastJavaScriptType != null) ? this.lastJavaScriptType.getType() : this.provider
/* 117 */       .getTypesFactory().getDefaultTypeDeclaration();
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
/*     */   protected TypeDeclaration resolveNativeType(AstNode node) {
/* 130 */     TypeDeclaration dec = JavaScriptHelper.tokenToNativeTypeDeclaration(node, this.provider);
/* 131 */     if (dec == null) {
/* 132 */       dec = testJavaStaticType(node);
/*     */     }
/*     */     
/* 135 */     return dec;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected TypeDeclaration testJavaStaticType(AstNode node) {
/* 145 */     switch (node.getType()) {
/*     */       case 39:
/* 147 */         return findJavaStaticType(node);
/*     */     } 
/* 149 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected TypeDeclaration findJavaStaticType(AstNode node) {
/* 159 */     String testName = node.toSource();
/*     */     
/* 161 */     if (testName != null) {
/* 162 */       TypeDeclaration dec = JavaScriptHelper.getTypeDeclaration(testName, this.provider);
/*     */       
/* 164 */       if (dec != null) {
/*     */         
/* 166 */         ClassFile cf = this.provider.getJavaScriptTypesFactory().getClassFile(this.provider
/* 167 */             .getJarManager(), dec);
/* 168 */         if (cf != null) {
/*     */           
/* 170 */           TypeDeclaration returnDec = this.provider.getJavaScriptTypesFactory().createNewTypeDeclaration(cf, true, false);
/* 171 */           return returnDec;
/*     */         } 
/*     */       } 
/*     */     } 
/* 175 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class CompilerNodeVisitor
/*     */     implements NodeVisitor
/*     */   {
/*     */     private boolean ignoreParams;
/*     */ 
/*     */     
/* 186 */     private HashSet<AstNode> paramNodes = new HashSet<>();
/*     */ 
/*     */ 
/*     */     
/*     */     private CompilerNodeVisitor(boolean ignoreParams) {
/* 191 */       this.ignoreParams = ignoreParams;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean visit(AstNode node) {
/*     */       TypeDeclaration dec;
/* 198 */       Logger.log(JavaScriptHelper.convertNodeToSource(node));
/* 199 */       Logger.log(node.shortName());
/*     */       
/* 201 */       if (!validNode(node)) {
/*     */ 
/*     */         
/* 204 */         JavaScriptCompletionResolver.this.lastJavaScriptType = null;
/* 205 */         return false;
/*     */       } 
/*     */ 
/*     */       
/* 209 */       if (ignore(node, this.ignoreParams)) {
/* 210 */         return true;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 216 */       if (JavaScriptCompletionResolver.this.lastJavaScriptType == null) {
/* 217 */         dec = JavaScriptCompletionResolver.this.resolveNativeType(node);
/* 218 */         if (dec == null && node.getType() == 39) {
/* 219 */           JavaScriptCompletionResolver.this.lastJavaScriptType = null;
/* 220 */           return false;
/*     */         } 
/*     */       } else {
/*     */         
/* 224 */         dec = JavaScriptCompletionResolver.this.resolveTypeFromLastJavaScriptType(node);
/*     */       } 
/*     */       
/* 227 */       if (dec != null) {
/*     */         
/* 229 */         JavaScriptType jsType = JavaScriptCompletionResolver.this.provider.getJavaScriptTypesFactory().getCachedType(dec, JavaScriptCompletionResolver.this.provider
/* 230 */             .getJarManager(), (DefaultCompletionProvider)JavaScriptCompletionResolver.this.provider, 
/* 231 */             JavaScriptHelper.convertNodeToSource(node));
/*     */         
/* 233 */         if (jsType != null) {
/* 234 */           JavaScriptCompletionResolver.this.lastJavaScriptType = jsType;
/*     */           
/* 236 */           return false;
/*     */         }
/*     */       
/* 239 */       } else if (JavaScriptCompletionResolver.this.lastJavaScriptType != null) {
/* 240 */         if (node.getType() == 39)
/*     */         {
/* 242 */           JavaScriptType jsType = JavaScriptCompletionResolver.this.lookupFromName(node, JavaScriptCompletionResolver.this.lastJavaScriptType);
/* 243 */           if (jsType == null)
/*     */           {
/*     */             
/* 246 */             jsType = JavaScriptCompletionResolver.this.lookupFunctionCompletion(node, JavaScriptCompletionResolver.this.lastJavaScriptType);
/*     */           }
/* 248 */           JavaScriptCompletionResolver.this.lastJavaScriptType = jsType;
/*     */         }
/*     */       
/* 251 */       } else if (node instanceof FunctionCall) {
/*     */         
/* 253 */         FunctionCall fn = (FunctionCall)node;
/* 254 */         String lookupText = createLookupString(fn);
/* 255 */         JavaScriptFunctionDeclaration funcDec = JavaScriptCompletionResolver.this.provider.getVariableResolver().findFunctionDeclaration(lookupText);
/* 256 */         if (funcDec != null) {
/*     */           
/* 258 */           JavaScriptType jsType = JavaScriptCompletionResolver.this.provider.getJavaScriptTypesFactory().getCachedType(funcDec
/* 259 */               .getTypeDeclaration(), JavaScriptCompletionResolver.this.provider.getJarManager(), (DefaultCompletionProvider)JavaScriptCompletionResolver.this.provider, 
/* 260 */               JavaScriptHelper.convertNodeToSource(node));
/* 261 */           if (jsType != null) {
/* 262 */             JavaScriptCompletionResolver.this.lastJavaScriptType = jsType;
/*     */             
/* 264 */             return false;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 269 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     private boolean validNode(AstNode node) {
/* 274 */       switch (node.getType()) {
/*     */         case 39:
/* 276 */           return (((Name)node).getIdentifier() != null && ((Name)node).getIdentifier().length() > 0);
/*     */       } 
/* 278 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     private String createLookupString(FunctionCall fn) {
/* 283 */       StringBuilder sb = new StringBuilder();
/* 284 */       String name = "";
/* 285 */       switch (fn.getTarget().getType()) {
/*     */         case 39:
/* 287 */           name = ((Name)fn.getTarget()).getIdentifier();
/*     */           break;
/*     */       } 
/* 290 */       sb.append(name);
/* 291 */       sb.append("(");
/* 292 */       Iterator<AstNode> i = fn.getArguments().iterator();
/* 293 */       while (i.hasNext()) {
/*     */         
/* 295 */         i.next();
/* 296 */         sb.append("p");
/* 297 */         if (i.hasNext())
/* 298 */           sb.append(","); 
/*     */       } 
/* 300 */       sb.append(")");
/* 301 */       return sb.toString();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean ignore(AstNode node, boolean ignoreParams) {
/* 312 */       switch (node.getType()) {
/*     */         
/*     */         case 133:
/*     */         case 134:
/* 316 */           return 
/* 317 */             (((ExpressionStatement)node).getExpression().getType() == -1);
/*     */         case -1:
/*     */         case 33:
/*     */         case 136:
/* 321 */           return true;
/*     */       } 
/* 323 */       if (isParameter(node)) {
/* 324 */         collectAllNodes(node);
/*     */         
/* 326 */         return ignoreParams;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 335 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void collectAllNodes(AstNode node) {
/* 345 */       if (node.getType() == 38) {
/*     */         
/* 347 */         FunctionCall call = (FunctionCall)node;
/* 348 */         for (AstNode arg : call.getArguments()) {
/* 349 */           JavaScriptCompletionResolver.VisitorAll all = new JavaScriptCompletionResolver.VisitorAll();
/* 350 */           arg.visit(all);
/* 351 */           this.paramNodes.addAll(all.getAllNodes());
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean isParameter(AstNode node) {
/* 365 */       if (this.paramNodes.contains(node)) {
/* 366 */         return true;
/*     */       }
/* 368 */       FunctionCall fc = JavaScriptHelper.findFunctionCallFromNode(node);
/* 369 */       if (fc != null && node != fc) {
/* 370 */         collectAllNodes((AstNode)fc);
/* 371 */         if (this.paramNodes.contains(node)) {
/* 372 */           return true;
/*     */         }
/*     */       } 
/* 375 */       return false;
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
/*     */ 
/*     */ 
/*     */   
/*     */   protected JavaScriptType lookupFromName(AstNode node, JavaScriptType lastJavaScriptType) {
/* 391 */     JavaScriptType javaScriptType = null;
/* 392 */     if (lastJavaScriptType != null) {
/* 393 */       String lookupText = null;
/* 394 */       switch (node.getType()) {
/*     */         case 39:
/* 396 */           lookupText = ((Name)node).getIdentifier();
/*     */           break;
/*     */       } 
/* 399 */       if (lookupText == null)
/*     */       {
/* 401 */         lookupText = node.toSource();
/*     */       }
/* 403 */       javaScriptType = lookupJavaScriptType(lastJavaScriptType, lookupText);
/*     */     } 
/*     */     
/* 406 */     return javaScriptType;
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
/*     */   protected JavaScriptType lookupFunctionCompletion(AstNode node, JavaScriptType lastJavaScriptType) {
/* 421 */     JavaScriptType javaScriptType = null;
/* 422 */     if (lastJavaScriptType != null) {
/*     */       
/* 424 */       String lookupText = JavaScriptHelper.getFunctionNameLookup(node, this.provider);
/* 425 */       javaScriptType = lookupJavaScriptType(lastJavaScriptType, lookupText);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 430 */     return javaScriptType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLookupText(JSMethodData method, String name) {
/* 438 */     StringBuilder sb = new StringBuilder(name);
/* 439 */     sb.append('(');
/* 440 */     int count = method.getParameterCount();
/* 441 */     for (int i = 0; i < count; i++) {
/* 442 */       sb.append("p");
/* 443 */       if (i < count - 1) {
/* 444 */         sb.append(",");
/*     */       }
/*     */     } 
/* 447 */     sb.append(')');
/* 448 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFunctionNameLookup(FunctionCall call, SourceCompletionProvider provider) {
/* 455 */     if (call != null) {
/* 456 */       StringBuilder sb = new StringBuilder();
/* 457 */       if (call.getTarget() instanceof PropertyGet) {
/* 458 */         PropertyGet get = (PropertyGet)call.getTarget();
/* 459 */         sb.append(get.getProperty().getIdentifier());
/*     */       } 
/* 461 */       sb.append("(");
/* 462 */       int count = call.getArguments().size();
/* 463 */       for (int i = 0; i < count; i++) {
/* 464 */         sb.append("p");
/* 465 */         if (i < count - 1) {
/* 466 */           sb.append(",");
/*     */         }
/*     */       } 
/* 469 */       sb.append(")");
/* 470 */       return sb.toString();
/*     */     } 
/* 472 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JavaScriptType lookupJavaScriptType(JavaScriptType lastJavaScriptType, String lookupText) {
/* 479 */     JavaScriptType javaScriptType = null;
/* 480 */     if (lookupText != null && !lookupText.equals(this.lastLookupName)) {
/*     */ 
/*     */       
/* 483 */       JSCompletion completion = lastJavaScriptType.getCompletion(lookupText, this.provider);
/* 484 */       if (completion != null) {
/* 485 */         String type = completion.getType(true);
/* 486 */         if (type != null) {
/*     */           
/* 488 */           TypeDeclaration newType = this.provider.getTypesFactory().getTypeDeclaration(type);
/* 489 */           if (newType != null) {
/*     */             
/* 491 */             javaScriptType = this.provider.getJavaScriptTypesFactory().getCachedType(newType, this.provider
/* 492 */                 .getJarManager(), (DefaultCompletionProvider)this.provider, lookupText);
/*     */           }
/*     */           else {
/*     */             
/* 496 */             javaScriptType = createNewTypeDeclaration(this.provider, type, lookupText);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 502 */     this.lastLookupName = lookupText;
/* 503 */     return javaScriptType;
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
/*     */   private JavaScriptType createNewTypeDeclaration(SourceCompletionProvider provider, String type, String text) {
/* 516 */     if (provider.getJavaScriptTypesFactory() != null) {
/* 517 */       ClassFile cf = provider.getJarManager().getClassEntry(type);
/*     */       
/* 519 */       if (cf != null) {
/*     */         
/* 521 */         TypeDeclaration newType = provider.getJavaScriptTypesFactory().createNewTypeDeclaration(cf, false);
/* 522 */         return provider.getJavaScriptTypesFactory()
/* 523 */           .getCachedType(newType, provider.getJarManager(), (DefaultCompletionProvider)provider, text);
/*     */       } 
/*     */     } 
/*     */     
/* 527 */     return null;
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
/*     */   protected TypeDeclaration resolveTypeFromLastJavaScriptType(AstNode node) {
/* 539 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class VisitorAll
/*     */     implements NodeVisitor
/*     */   {
/* 548 */     private ArrayList<AstNode> all = new ArrayList<>();
/*     */ 
/*     */     
/*     */     public boolean visit(AstNode node) {
/* 552 */       this.all.add(node);
/* 553 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public ArrayList<AstNode> getAllNodes() {
/* 558 */       return this.all;
/*     */     }
/*     */     
/*     */     private VisitorAll() {}
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\resolver\JavaScriptCompletionResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */