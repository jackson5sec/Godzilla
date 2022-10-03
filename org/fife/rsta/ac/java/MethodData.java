/*     */ package org.fife.rsta.ac.java;
/*     */ 
/*     */ import org.fife.rsta.ac.java.rjc.ast.Method;
/*     */ import org.fife.rsta.ac.java.rjc.ast.TypeDeclaration;
/*     */ import org.fife.rsta.ac.java.rjc.lang.Modifiers;
/*     */ import org.fife.rsta.ac.java.rjc.lang.Type;
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
/*     */ class MethodData
/*     */   implements MemberCompletion.Data
/*     */ {
/*     */   private Method method;
/*     */   
/*     */   public MethodData(Method method) {
/*  33 */     this.method = method;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEnclosingClassName(boolean fullyQualified) {
/*  44 */     TypeDeclaration td = this.method.getParentTypeDeclaration();
/*  45 */     if (td == null) {
/*  46 */       (new Exception("No parent type declaration for: " + getSignature()))
/*  47 */         .printStackTrace();
/*  48 */       return "";
/*     */     } 
/*  50 */     return td.getName(fullyQualified);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getIcon() {
/*     */     String key;
/*  59 */     Modifiers mod = this.method.getModifiers();
/*  60 */     if (mod == null) {
/*  61 */       key = "methodDefaultIcon";
/*     */     }
/*  63 */     else if (mod.isPrivate()) {
/*  64 */       key = "methodPrivateIcon";
/*     */     }
/*  66 */     else if (mod.isProtected()) {
/*  67 */       key = "methodProtectedIcon";
/*     */     }
/*  69 */     else if (mod.isPublic()) {
/*  70 */       key = "methodPublicIcon";
/*     */     } else {
/*     */       
/*  73 */       key = "methodDefaultIcon";
/*     */     } 
/*     */     
/*  76 */     return key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSignature() {
/*  83 */     return this.method.getNameAndParameters();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSummary() {
/*  89 */     String docComment = this.method.getDocComment();
/*  90 */     return (docComment != null) ? docComment : this.method.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType() {
/*  96 */     Type type = this.method.getType();
/*  97 */     return (type == null) ? "void" : type.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAbstract() {
/* 103 */     return this.method.getModifiers().isAbstract();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConstructor() {
/* 109 */     return this.method.isConstructor();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDeprecated() {
/* 115 */     return this.method.isDeprecated();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFinal() {
/* 121 */     return this.method.getModifiers().isFinal();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStatic() {
/* 127 */     return this.method.getModifiers().isStatic();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\MethodData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */