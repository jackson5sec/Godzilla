/*     */ package org.fife.rsta.ac.java;
/*     */ 
/*     */ import org.fife.rsta.ac.java.rjc.ast.Field;
/*     */ import org.fife.rsta.ac.java.rjc.ast.TypeDeclaration;
/*     */ import org.fife.rsta.ac.java.rjc.lang.Modifiers;
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
/*     */ class FieldData
/*     */   implements MemberCompletion.Data
/*     */ {
/*     */   private Field field;
/*     */   
/*     */   public FieldData(Field field) {
/*  32 */     this.field = field;
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
/*  43 */     TypeDeclaration td = this.field.getParentTypeDeclaration();
/*  44 */     if (td == null) {
/*  45 */       (new Exception("No parent type declaration for: " + getSignature()))
/*  46 */         .printStackTrace();
/*  47 */       return "";
/*     */     } 
/*  49 */     return td.getName(fullyQualified);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getIcon() {
/*     */     String key;
/*  61 */     Modifiers mod = this.field.getModifiers();
/*  62 */     if (mod == null) {
/*  63 */       key = "fieldDefaultIcon";
/*     */     }
/*  65 */     else if (mod.isPrivate()) {
/*  66 */       key = "fieldPrivateIcon";
/*     */     }
/*  68 */     else if (mod.isProtected()) {
/*  69 */       key = "fieldProtectedIcon";
/*     */     }
/*  71 */     else if (mod.isPublic()) {
/*  72 */       key = "fieldPublicIcon";
/*     */     } else {
/*     */       
/*  75 */       key = "fieldDefaultIcon";
/*     */     } 
/*     */     
/*  78 */     return key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSignature() {
/*  88 */     return this.field.getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSummary() {
/*  97 */     String docComment = this.field.getDocComment();
/*  98 */     return (docComment != null) ? docComment : this.field.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType() {
/* 107 */     return this.field.getType().toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAbstract() {
/* 113 */     return this.field.getModifiers().isAbstract();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConstructor() {
/* 124 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDeprecated() {
/* 133 */     return this.field.isDeprecated();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFinal() {
/* 139 */     return this.field.getModifiers().isFinal();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStatic() {
/* 145 */     return this.field.getModifiers().isStatic();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\FieldData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */