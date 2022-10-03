/*     */ package org.fife.rsta.ac.js.ast.type;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.fife.rsta.ac.js.ast.type.ecma.TypeDeclarations;
/*     */ import org.fife.rsta.ac.js.ast.type.ecma.client.ClientBrowserAdditions;
/*     */ import org.fife.rsta.ac.js.ast.type.ecma.client.DOMAddtions;
/*     */ import org.fife.rsta.ac.js.ast.type.ecma.client.HTMLDOMAdditions;
/*     */ import org.fife.rsta.ac.js.ast.type.ecma.e4x.ECMAvE4xAdditions;
/*     */ import org.fife.rsta.ac.js.ast.type.ecma.v5.TypeDeclarationsECMAv5;
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
/*     */ public class TypeDeclarationFactory
/*     */ {
/*     */   private TypeDeclarations ecma;
/*     */   
/*     */   public TypeDeclarationFactory() {
/*  37 */     setTypeDeclarationVersion(null, false, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> setTypeDeclarationVersion(String ecmaVersion, boolean xmlSupported, boolean client) {
/*     */     try {
/*  46 */       ecmaVersion = (ecmaVersion == null) ? getDefaultECMAVersion() : ecmaVersion;
/*     */       
/*  48 */       Class<?> ecmaClass = TypeDeclarationFactory.class.getClassLoader().loadClass(ecmaVersion);
/*  49 */       this.ecma = (TypeDeclarations)ecmaClass.newInstance();
/*     */     }
/*  51 */     catch (Exception e) {
/*     */ 
/*     */ 
/*     */       
/*  55 */       this.ecma = (TypeDeclarations)new TypeDeclarationsECMAv5();
/*     */     } 
/*     */     
/*  58 */     if (xmlSupported) {
/*  59 */       (new ECMAvE4xAdditions()).addAdditionalTypes(this.ecma);
/*     */     }
/*     */     
/*  62 */     if (client) {
/*     */       
/*  64 */       (new ClientBrowserAdditions()).addAdditionalTypes(this.ecma);
/*  65 */       (new DOMAddtions()).addAdditionalTypes(this.ecma);
/*  66 */       (new HTMLDOMAdditions()).addAdditionalTypes(this.ecma);
/*     */     } 
/*     */ 
/*     */     
/*  70 */     return this.ecma.getAllClasses();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getDefaultECMAVersion() {
/*  78 */     return TypeDeclarationsECMAv5.class.getName();
/*     */   }
/*     */   
/*     */   public List<TypeDeclaration> getAllJavaScriptTypes() {
/*  82 */     return this.ecma.getAllJavaScriptTypeDeclarations();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeType(String name) {
/*  92 */     this.ecma.removeType(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isJavaScriptType(TypeDeclaration td) {
/* 102 */     return this.ecma.isJavaScriptType(td);
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
/*     */   public TypeDeclaration getTypeDeclaration(String name) {
/* 114 */     return this.ecma.getTypeDeclaration(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getJSTypeDeclarationAsString(String name) {
/* 123 */     TypeDeclaration dec = getTypeDeclaration(name);
/* 124 */     return (dec != null) ? dec.getJSName() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String convertJavaScriptType(String lookupName, boolean qualified) {
/* 135 */     if (lookupName != null) {
/* 136 */       if (TypeDeclarations.NULL_TYPE.equals(lookupName)) {
/* 137 */         return null;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 142 */       if (lookupName.indexOf('<') > -1) {
/* 143 */         lookupName = lookupName.substring(0, lookupName.indexOf('<'));
/*     */       }
/*     */       
/* 146 */       String lookup = !qualified ? getJSTypeDeclarationAsString(lookupName) : lookupName;
/*     */       
/* 148 */       lookupName = (lookup != null) ? lookup : lookupName;
/* 149 */       if (!qualified && 
/* 150 */         lookupName != null && lookupName.contains(".")) {
/* 151 */         return lookupName.substring(lookupName
/* 152 */             .lastIndexOf(".") + 1, lookupName
/* 153 */             .length());
/*     */       }
/*     */     } 
/*     */     
/* 157 */     return lookupName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeDeclaration getDefaultTypeDeclaration() {
/* 165 */     return getTypeDeclaration("any");
/*     */   }
/*     */   
/*     */   public void addType(String name, TypeDeclaration dec) {
/* 169 */     this.ecma.addTypeDeclaration(name, dec);
/*     */   }
/*     */   
/*     */   public String getClassName(String lookup) throws RuntimeException {
/* 173 */     TypeDeclaration td = getTypeDeclaration(lookup);
/* 174 */     if (td != null) {
/* 175 */       return td.getQualifiedName();
/*     */     }
/*     */     
/* 178 */     throw new RuntimeException("Error finding TypeDeclaration for: " + lookup);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<TypeDeclarations.JavaScriptObject> getECMAScriptObjects() {
/* 186 */     return this.ecma.getJavaScriptObjects();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canJavaScriptBeInstantiated(String name) {
/* 195 */     return this.ecma.canECMAObjectBeInstantiated(name);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\ast\type\TypeDeclarationFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */