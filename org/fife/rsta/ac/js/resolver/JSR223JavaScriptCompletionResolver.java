/*     */ package org.fife.rsta.ac.js.resolver;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.fife.rsta.ac.java.classreader.ClassFile;
/*     */ import org.fife.rsta.ac.js.JavaScriptHelper;
/*     */ import org.fife.rsta.ac.js.Logger;
/*     */ import org.fife.rsta.ac.js.SourceCompletionProvider;
/*     */ import org.fife.rsta.ac.js.ast.type.TypeDeclaration;
/*     */ import org.fife.rsta.ac.js.completion.JSMethodData;
/*     */ import org.fife.ui.autocomplete.CompletionProvider;
/*     */ import org.mozilla.javascript.ast.AstNode;
/*     */ import org.mozilla.javascript.ast.FunctionCall;
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
/*     */ public class JSR223JavaScriptCompletionResolver
/*     */   extends JavaScriptCompletionResolver
/*     */ {
/*     */   public JSR223JavaScriptCompletionResolver(SourceCompletionProvider provider) {
/*  27 */     super(provider);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected TypeDeclaration resolveNativeType(AstNode node) {
/*  36 */     TypeDeclaration dec = super.resolveNativeType(node);
/*  37 */     if (dec == null) {
/*  38 */       dec = testJavaStaticType(node);
/*     */     }
/*     */     
/*  41 */     return dec;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLookupText(JSMethodData methodData, String name) {
/*  49 */     StringBuilder sb = new StringBuilder(name);
/*  50 */     sb.append('(');
/*  51 */     int count = methodData.getParameterCount();
/*     */     
/*  53 */     String[] parameterTypes = methodData.getMethodInfo().getParameterTypes();
/*  54 */     for (int i = 0; i < count; i++) {
/*  55 */       String paramName = methodData.getParameterType(parameterTypes, i, (CompletionProvider)this.provider);
/*  56 */       sb.append(paramName);
/*  57 */       if (i < count - 1) {
/*  58 */         sb.append(",");
/*     */       }
/*     */     } 
/*  61 */     sb.append(')');
/*  62 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFunctionNameLookup(FunctionCall call, SourceCompletionProvider provider) {
/*  70 */     if (call != null) {
/*  71 */       StringBuilder sb = new StringBuilder();
/*  72 */       if (call.getTarget() instanceof PropertyGet) {
/*  73 */         PropertyGet get = (PropertyGet)call.getTarget();
/*  74 */         sb.append(get.getProperty().getIdentifier());
/*     */       } 
/*  76 */       sb.append("(");
/*  77 */       int count = call.getArguments().size();
/*     */       
/*  79 */       for (int i = 0; i < count; i++) {
/*  80 */         AstNode paramNode = call.getArguments().get(i);
/*  81 */         JavaScriptResolver resolver = provider.getJavaScriptEngine().getJavaScriptResolver(provider);
/*  82 */         Logger.log("PARAM: " + JavaScriptHelper.convertNodeToSource(paramNode));
/*     */         
/*     */         try {
/*  85 */           TypeDeclaration type = resolver.resolveParamNode(JavaScriptHelper.convertNodeToSource(paramNode));
/*  86 */           String resolved = (type != null) ? type.getQualifiedName() : "any";
/*  87 */           sb.append(resolved);
/*  88 */           if (i < count - 1) {
/*  89 */             sb.append(",");
/*     */           }
/*     */         } catch (IOException io) {
/*  92 */           io.printStackTrace();
/*     */         } 
/*  94 */       }  sb.append(")");
/*  95 */       return sb.toString();
/*     */     } 
/*  97 */     return null;
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
/*     */   protected TypeDeclaration findJavaStaticType(AstNode node) {
/* 109 */     String testName = null;
/* 110 */     if (node.getParent() != null && node
/* 111 */       .getParent().getType() == 33) {
/*     */ 
/*     */       
/* 114 */       String name = node.toSource();
/*     */       
/*     */       try {
/* 117 */         String longName = node.getParent().toSource();
/*     */         
/* 119 */         if (longName.indexOf('[') == -1 && longName.indexOf(']') == -1 && longName
/* 120 */           .indexOf('(') == -1 && longName.indexOf(')') == -1)
/*     */         {
/*     */           
/* 123 */           int index = longName.lastIndexOf(name);
/* 124 */           if (index > -1) {
/* 125 */             testName = longName.substring(0, index + name.length());
/*     */           }
/*     */         }
/*     */       
/* 129 */       } catch (Exception e) {
/*     */         
/* 131 */         Logger.log(e.getMessage());
/*     */       } 
/*     */     } else {
/*     */       
/* 135 */       testName = node.toSource();
/*     */     } 
/*     */     
/* 138 */     if (testName != null) {
/* 139 */       TypeDeclaration dec = JavaScriptHelper.getTypeDeclaration(testName, this.provider);
/*     */       
/* 141 */       if (dec == null) {
/* 142 */         dec = JavaScriptHelper.createNewTypeDeclaration(testName);
/*     */       }
/* 144 */       ClassFile cf = this.provider.getJavaScriptTypesFactory().getClassFile(this.provider
/* 145 */           .getJarManager(), dec);
/* 146 */       if (cf != null) {
/*     */         
/* 148 */         TypeDeclaration returnDec = this.provider.getJavaScriptTypesFactory().createNewTypeDeclaration(cf, true, false);
/* 149 */         return returnDec;
/*     */       } 
/*     */     } 
/* 152 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\resolver\JSR223JavaScriptCompletionResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */