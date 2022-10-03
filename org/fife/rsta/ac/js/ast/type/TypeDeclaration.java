/*     */ package org.fife.rsta.ac.js.ast.type;
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
/*     */ public class TypeDeclaration
/*     */ {
/*     */   private String pkg;
/*     */   private String apiName;
/*     */   private String jsName;
/*     */   private boolean staticsOnly;
/*     */   private boolean supportsBeanProperties;
/*     */   
/*     */   public TypeDeclaration(String pkg, String apiName, String jsName, boolean staticsOnly, boolean supportsBeanProperties) {
/*  23 */     this.staticsOnly = staticsOnly;
/*  24 */     this.pkg = pkg;
/*  25 */     this.apiName = apiName;
/*  26 */     this.jsName = jsName;
/*  27 */     this.supportsBeanProperties = supportsBeanProperties;
/*     */   }
/*     */   
/*     */   public TypeDeclaration(String pkg, String apiName, String jsName, boolean staticsOnly) {
/*  31 */     this(pkg, apiName, jsName, staticsOnly, true);
/*     */   }
/*     */   
/*     */   public TypeDeclaration(String pkg, String apiName, String jsName) {
/*  35 */     this(pkg, apiName, jsName, false, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPackageName() {
/*  40 */     return this.pkg;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAPITypeName() {
/*  45 */     return this.apiName;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getJSName() {
/*  50 */     return this.jsName;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getQualifiedName() {
/*  55 */     return (this.pkg != null && this.pkg.length() > 0) ? (this.pkg + '.' + this.apiName) : this.apiName;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isQualified() {
/*  60 */     return (getQualifiedName().indexOf('.') != -1);
/*     */   }
/*     */   
/*     */   public boolean isStaticsOnly() {
/*  64 */     return this.staticsOnly;
/*     */   }
/*     */   
/*     */   public void setStaticsOnly(boolean staticsOnly) {
/*  68 */     this.staticsOnly = staticsOnly;
/*     */   }
/*     */   
/*     */   public void setSupportsBeanProperties(boolean supportsBeanProperties) {
/*  72 */     this.supportsBeanProperties = supportsBeanProperties;
/*     */   }
/*     */   
/*     */   public boolean supportsBeanProperties() {
/*  76 */     return this.supportsBeanProperties;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  82 */     if (this == obj) {
/*  83 */       return true;
/*     */     }
/*  85 */     if (obj == null || obj.getClass() != getClass()) {
/*  86 */       return false;
/*     */     }
/*  88 */     if (obj instanceof TypeDeclaration) {
/*  89 */       TypeDeclaration dec = (TypeDeclaration)obj;
/*  90 */       return (getQualifiedName().equals(dec.getQualifiedName()) && 
/*  91 */         isStaticsOnly() == dec.isStaticsOnly());
/*     */     } 
/*     */     
/*  94 */     return super.equals(obj);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 105 */     int hash = 7;
/* 106 */     hash = 31 * Boolean.valueOf(this.staticsOnly).hashCode();
/* 107 */     hash = 31 * hash + getQualifiedName().hashCode();
/* 108 */     return hash;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\ast\type\TypeDeclaration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */