/*     */ package org.fife.rsta.ac.java;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.fife.rsta.ac.java.buildpath.SourceLocation;
/*     */ import org.fife.rsta.ac.java.classreader.ClassFile;
/*     */ import org.fife.rsta.ac.java.classreader.MethodInfo;
/*     */ import org.fife.rsta.ac.java.classreader.Util;
/*     */ import org.fife.rsta.ac.java.rjc.ast.CompilationUnit;
/*     */ import org.fife.rsta.ac.java.rjc.ast.FormalParameter;
/*     */ import org.fife.rsta.ac.java.rjc.ast.Member;
/*     */ import org.fife.rsta.ac.java.rjc.ast.Method;
/*     */ import org.fife.rsta.ac.java.rjc.ast.TypeDeclaration;
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
/*     */ class MethodInfoData
/*     */   implements MemberCompletion.Data
/*     */ {
/*     */   private SourceCompletionProvider provider;
/*     */   private MethodInfo info;
/*     */   private List<String> paramNames;
/*     */   
/*     */   public MethodInfoData(MethodInfo info, SourceCompletionProvider provider) {
/*  61 */     this.info = info;
/*  62 */     this.provider = provider;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEnclosingClassName(boolean fullyQualified) {
/*  71 */     return this.info.getClassFile().getClassName(fullyQualified);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getIcon() {
/*     */     String key;
/*  82 */     int flags = this.info.getAccessFlags();
/*     */     
/*  84 */     if (Util.isDefault(flags)) {
/*  85 */       key = "methodDefaultIcon";
/*     */     }
/*  87 */     else if (Util.isPrivate(flags)) {
/*  88 */       key = "methodPrivateIcon";
/*     */     }
/*  90 */     else if (Util.isProtected(flags)) {
/*  91 */       key = "methodProtectedIcon";
/*     */     }
/*  93 */     else if (Util.isPublic(flags)) {
/*  94 */       key = "methodPublicIcon";
/*     */     } else {
/*     */       
/*  97 */       key = "methodDefaultIcon";
/*     */     } 
/*     */     
/* 100 */     return key;
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
/*     */   private Method getMethodFromSourceLoc(SourceLocation loc, ClassFile cf) {
/* 117 */     Method res = null;
/*     */     
/* 119 */     CompilationUnit cu = Util.getCompilationUnitFromDisk(loc, cf);
/*     */ 
/*     */ 
/*     */     
/* 123 */     if (cu != null) {
/*     */       
/* 125 */       Iterator<TypeDeclaration> i = cu.getTypeDeclarationIterator();
/* 126 */       while (i.hasNext()) {
/*     */         
/* 128 */         TypeDeclaration td = i.next();
/* 129 */         String typeName = td.getName();
/*     */ 
/*     */         
/* 132 */         if (typeName.equals(cf.getClassName(false))) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 137 */           List<Method> contenders = null;
/* 138 */           for (int j = 0; j < td.getMemberCount(); j++) {
/* 139 */             Member member = td.getMember(j);
/* 140 */             if (member instanceof Method && member
/* 141 */               .getName().equals(this.info.getName())) {
/* 142 */               Method m2 = (Method)member;
/* 143 */               if (m2.getParameterCount() == this.info.getParameterCount()) {
/* 144 */                 if (contenders == null) {
/* 145 */                   contenders = new ArrayList<>(1);
/*     */                 }
/* 147 */                 contenders.add(m2);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */ 
/*     */           
/* 153 */           if (contenders != null) {
/*     */ 
/*     */ 
/*     */             
/* 157 */             if (contenders.size() == 1) {
/* 158 */               res = contenders.get(0);
/*     */ 
/*     */ 
/*     */               
/*     */               break;
/*     */             } 
/*     */ 
/*     */             
/* 166 */             for (Method method : contenders) {
/* 167 */               boolean match = true;
/* 168 */               for (int p = 0; p < this.info.getParameterCount(); p++) {
/* 169 */                 String type1 = this.info.getParameterType(p, false);
/* 170 */                 FormalParameter fp = method.getParameter(p);
/* 171 */                 String type2 = fp.getType().toString();
/* 172 */                 if (!type1.equals(type2)) {
/* 173 */                   match = false;
/*     */                   break;
/*     */                 } 
/*     */               } 
/* 177 */               if (match) {
/* 178 */                 res = method;
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
/* 194 */     return res;
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
/*     */ 
/*     */   
/*     */   public String getParameterName(int index) {
/* 213 */     String name = this.info.getParameterName(index);
/*     */ 
/*     */     
/* 216 */     if (name == null) {
/*     */ 
/*     */       
/* 219 */       if (this.paramNames == null) {
/*     */         
/* 221 */         this.paramNames = new ArrayList<>(1);
/* 222 */         int offs = 0;
/* 223 */         String rawSummary = getSummary();
/*     */ 
/*     */         
/* 226 */         if (rawSummary != null && rawSummary.startsWith("/**")) {
/*     */ 
/*     */           
/* 229 */           int summaryLen = rawSummary.length();
/*     */           int nextParam;
/* 231 */           while ((nextParam = rawSummary.indexOf("@param", offs)) > -1) {
/* 232 */             int temp = nextParam + "@param".length() + 1;
/* 233 */             while (temp < summaryLen && 
/* 234 */               Character.isWhitespace(rawSummary.charAt(temp))) {
/* 235 */               temp++;
/*     */             }
/* 237 */             if (temp < summaryLen) {
/* 238 */               int start = temp;
/* 239 */               int end = start + 1;
/* 240 */               while (end < summaryLen && 
/* 241 */                 Character.isJavaIdentifierPart(rawSummary.charAt(end))) {
/* 242 */                 end++;
/*     */               }
/* 244 */               this.paramNames.add(rawSummary.substring(start, end));
/* 245 */               offs = end;
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
/* 256 */       if (index < this.paramNames.size()) {
/* 257 */         name = this.paramNames.get(index);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 263 */     if (name == null) {
/* 264 */       name = "arg" + index;
/*     */     }
/*     */     
/* 267 */     return name;
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
/*     */   public String getSignature() {
/* 279 */     StringBuilder sb = new StringBuilder(this.info.getName());
/*     */     
/* 281 */     sb.append('(');
/* 282 */     int paramCount = this.info.getParameterCount();
/* 283 */     for (int i = 0; i < paramCount; i++) {
/* 284 */       sb.append(this.info.getParameterType(i, false));
/* 285 */       sb.append(' ');
/* 286 */       sb.append(getParameterName(i));
/* 287 */       if (i < paramCount - 1) {
/* 288 */         sb.append(", ");
/*     */       }
/*     */     } 
/* 291 */     sb.append(')');
/*     */     
/* 293 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSummary() {
/* 301 */     ClassFile cf = this.info.getClassFile();
/* 302 */     SourceLocation loc = this.provider.getSourceLocForClass(cf.getClassName(true));
/* 303 */     String summary = null;
/*     */ 
/*     */ 
/*     */     
/* 307 */     if (loc != null) {
/* 308 */       summary = getSummaryFromSourceLoc(loc, cf);
/*     */     }
/*     */ 
/*     */     
/* 312 */     if (summary == null) {
/* 313 */       summary = this.info.getSignature();
/*     */     }
/* 315 */     return summary;
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
/*     */   private String getSummaryFromSourceLoc(SourceLocation loc, ClassFile cf) {
/* 331 */     Method method = getMethodFromSourceLoc(loc, cf);
/* 332 */     return (method != null) ? method.getDocComment() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType() {
/* 341 */     return this.info.getReturnTypeString(false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAbstract() {
/* 347 */     return this.info.isAbstract();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConstructor() {
/* 356 */     return this.info.isConstructor();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDeprecated() {
/* 365 */     return this.info.isDeprecated();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFinal() {
/* 371 */     return this.info.isFinal();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStatic() {
/* 377 */     return this.info.isStatic();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\MethodInfoData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */