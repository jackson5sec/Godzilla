/*     */ package org.fife.rsta.ac.js.completion;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.fife.rsta.ac.java.JarManager;
/*     */ import org.fife.rsta.ac.java.Util;
/*     */ import org.fife.rsta.ac.java.buildpath.SourceLocation;
/*     */ import org.fife.rsta.ac.java.classreader.ClassFile;
/*     */ import org.fife.rsta.ac.java.classreader.MethodInfo;
/*     */ import org.fife.rsta.ac.java.rjc.ast.CompilationUnit;
/*     */ import org.fife.rsta.ac.java.rjc.ast.FormalParameter;
/*     */ import org.fife.rsta.ac.java.rjc.ast.Member;
/*     */ import org.fife.rsta.ac.java.rjc.ast.Method;
/*     */ import org.fife.rsta.ac.java.rjc.ast.TypeDeclaration;
/*     */ import org.fife.rsta.ac.js.SourceCompletionProvider;
/*     */ import org.fife.ui.autocomplete.CompletionProvider;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JSMethodData
/*     */ {
/*     */   private MethodInfo info;
/*     */   private JarManager jarManager;
/*     */   private ArrayList<String> paramNames;
/*     */   
/*     */   public JSMethodData(MethodInfo info, JarManager jarManager) {
/*  29 */     this.info = info;
/*  30 */     this.jarManager = jarManager;
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
/*     */   
/*     */   public String getParameterName(int index) {
/*  47 */     String name = this.info.getParameterName(index);
/*     */ 
/*     */     
/*  50 */     Method method = getMethod();
/*  51 */     if (method != null)
/*     */     {
/*  53 */       name = method.getParameter(index).getName();
/*     */     }
/*     */ 
/*     */     
/*  57 */     if (name == null) {
/*     */ 
/*     */       
/*  60 */       if (this.paramNames == null) {
/*     */         
/*  62 */         this.paramNames = new ArrayList<>(1);
/*  63 */         int offs = 0;
/*  64 */         String rawSummary = getSummary();
/*     */ 
/*     */         
/*  67 */         if (rawSummary != null && rawSummary.startsWith("/**")) {
/*     */ 
/*     */           
/*  70 */           int summaryLen = rawSummary.length();
/*     */           int nextParam;
/*  72 */           while ((nextParam = rawSummary.indexOf("@param", offs)) > -1) {
/*  73 */             int temp = nextParam + "@param".length() + 1;
/*  74 */             while ((temp < summaryLen && 
/*  75 */               !Character.isJavaIdentifierPart(rawSummary.charAt(temp))) || 
/*  76 */               Character.isWhitespace(rawSummary.charAt(temp))) {
/*  77 */               temp++;
/*     */             }
/*  79 */             if (temp < summaryLen) {
/*  80 */               int start = temp;
/*  81 */               int end = start + 1;
/*  82 */               while (end < summaryLen && 
/*  83 */                 Character.isJavaIdentifierPart(rawSummary.charAt(end))) {
/*  84 */                 end++;
/*     */               }
/*  86 */               this.paramNames.add(rawSummary.substring(start, end));
/*  87 */               offs = end;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  98 */       if (index < this.paramNames.size()) {
/*  99 */         name = this.paramNames.get(index);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 105 */     if (name == null) {
/* 106 */       name = "arg" + index;
/*     */     }
/*     */     
/* 109 */     return name;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getParameterType(String[] paramTypes, int index, CompletionProvider provider) {
/* 115 */     if (paramTypes != null && index < paramTypes.length)
/*     */     {
/* 117 */       return ((SourceCompletionProvider)provider).getTypesFactory().convertJavaScriptType(paramTypes[index], true);
/*     */     }
/* 119 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSummary() {
/* 124 */     ClassFile cf = this.info.getClassFile();
/* 125 */     SourceLocation loc = this.jarManager.getSourceLocForClass(cf
/* 126 */         .getClassName(true));
/* 127 */     String summary = null;
/*     */ 
/*     */ 
/*     */     
/* 131 */     if (loc != null) {
/* 132 */       summary = getSummaryFromSourceLoc(loc, cf);
/*     */     }
/*     */ 
/*     */     
/* 136 */     if (summary == null) {
/*     */       
/* 138 */       this.info.getReturnTypeString(true);
/* 139 */       summary = this.info.getSignature();
/*     */     } 
/* 141 */     return summary;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Method getMethod() {
/* 147 */     ClassFile cf = this.info.getClassFile();
/* 148 */     SourceLocation loc = this.jarManager.getSourceLocForClass(cf
/* 149 */         .getClassName(true));
/* 150 */     return getMethodFromSourceLoc(loc, cf);
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
/*     */   private String getSummaryFromSourceLoc(SourceLocation loc, ClassFile cf) {
/* 164 */     Method method = getMethodFromSourceLoc(loc, cf);
/* 165 */     return (method != null) ? method.getDocComment() : null;
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
/*     */   private Method getMethodFromSourceLoc(SourceLocation loc, ClassFile cf) {
/* 180 */     Method res = null;
/*     */     
/* 182 */     CompilationUnit cu = Util.getCompilationUnitFromDisk(loc, cf);
/*     */ 
/*     */ 
/*     */     
/* 186 */     if (cu != null) {
/*     */       
/* 188 */       Iterator<TypeDeclaration> i = cu.getTypeDeclarationIterator();
/* 189 */       while (i.hasNext()) {
/*     */         
/* 191 */         TypeDeclaration td = i.next();
/* 192 */         String typeName = td.getName();
/*     */ 
/*     */         
/* 195 */         if (typeName.equals(cf.getClassName(false))) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 200 */           List<Method> contenders = null;
/* 201 */           for (Iterator<Member> j = td.getMemberIterator(); j.hasNext(); ) {
/* 202 */             Member member = j.next();
/* 203 */             if (member instanceof Method && member
/* 204 */               .getName().equals(this.info.getName())) {
/* 205 */               Method m2 = (Method)member;
/* 206 */               if (m2.getParameterCount() == this.info.getParameterCount()) {
/* 207 */                 if (contenders == null) {
/* 208 */                   contenders = new ArrayList<>(1);
/*     */                 }
/* 210 */                 contenders.add(m2);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */ 
/*     */           
/* 216 */           if (contenders != null) {
/*     */ 
/*     */ 
/*     */             
/* 220 */             if (contenders.size() == 1) {
/* 221 */               res = contenders.get(0);
/*     */ 
/*     */ 
/*     */               
/*     */               break;
/*     */             } 
/*     */ 
/*     */             
/* 229 */             for (int k = 0; k < contenders.size(); k++) {
/* 230 */               boolean match = true;
/* 231 */               Method method = contenders.get(k);
/* 232 */               for (int p = 0; p < this.info.getParameterCount(); p++) {
/* 233 */                 String type1 = this.info.getParameterType(p, false);
/* 234 */                 FormalParameter fp = method.getParameter(p);
/* 235 */                 String type2 = fp.getType().toString();
/* 236 */                 if (!type1.equals(type2)) {
/* 237 */                   match = false;
/*     */                   break;
/*     */                 } 
/*     */               } 
/* 241 */               if (match) {
/* 242 */                 res = method;
/*     */ 
/*     */ 
/*     */                 
/*     */                 break;
/*     */               } 
/*     */             } 
/*     */           } 
/*     */ 
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 258 */     return res;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodInfo getMethodInfo() {
/* 264 */     return this.info;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getType(boolean qualified) {
/* 269 */     return this.info.getReturnTypeString(qualified);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getParameterCount() {
/* 274 */     return this.info.getParameterCount();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStatic() {
/* 279 */     return this.info.isStatic();
/*     */   }
/*     */   
/*     */   public String getEnclosingClassName(boolean fullyQualified) {
/* 283 */     return this.info.getClassFile().getClassName(fullyQualified);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\completion\JSMethodData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */