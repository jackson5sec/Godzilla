/*     */ package org.fife.rsta.ac.js.ast.jsType;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import org.fife.rsta.ac.java.JarManager;
/*     */ import org.fife.rsta.ac.java.classreader.ClassFile;
/*     */ import org.fife.rsta.ac.js.JavaScriptHelper;
/*     */ import org.fife.rsta.ac.js.ast.parser.RhinoJavaScriptAstParser;
/*     */ import org.fife.rsta.ac.js.ast.type.TypeDeclaration;
/*     */ import org.fife.rsta.ac.js.ast.type.TypeDeclarationFactory;
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
/*     */ public class RhinoJavaScriptTypesFactory
/*     */   extends JSR223JavaScriptTypesFactory
/*     */ {
/*  27 */   private LinkedHashSet<String> importClasses = new LinkedHashSet<>();
/*  28 */   private LinkedHashSet<String> importPackages = new LinkedHashSet<>();
/*     */ 
/*     */   
/*     */   public RhinoJavaScriptTypesFactory(TypeDeclarationFactory typesFactory) {
/*  32 */     super(typesFactory);
/*     */   }
/*     */   
/*     */   public void addImportClass(String qualifiedClass) {
/*  36 */     this.importClasses.add(qualifiedClass);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addImportPackage(String packageName) {
/*  41 */     this.importPackages.add(packageName);
/*     */   }
/*     */ 
/*     */   
/*     */   public void mergeImports(HashSet<String> packages, HashSet<String> classes) {
/*  46 */     mergeImports(packages, this.importPackages, true);
/*  47 */     mergeImports(classes, this.importClasses, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void mergeImports(HashSet<String> newImports, LinkedHashSet<String> oldImports, boolean packages) {
/*  53 */     HashSet<String> remove = new HashSet<>();
/*  54 */     for (String obj : oldImports) {
/*  55 */       if (!newImports.contains(obj)) {
/*  56 */         remove.add(obj);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  62 */     if (!remove.isEmpty()) {
/*     */       
/*  64 */       HashSet<TypeDeclaration> removeTypes = new HashSet<>();
/*  65 */       for (String name : remove) {
/*  66 */         for (TypeDeclaration dec : this.cachedTypes.keySet()) {
/*  67 */           if ((packages && dec.getQualifiedName().startsWith(name)) || (!packages && dec.getQualifiedName().equals(name))) {
/*     */             
/*  69 */             removeAllTypes(this.cachedTypes.get(dec));
/*  70 */             removeTypes.add(dec);
/*     */           } 
/*     */         } 
/*     */       } 
/*  74 */       this.cachedTypes.keySet().removeAll(removeTypes);
/*     */     } 
/*     */     
/*  77 */     if (canClearCache(newImports, oldImports)) {
/*     */ 
/*     */       
/*  80 */       oldImports.clear();
/*     */       
/*  82 */       clearAllImportTypes();
/*     */       
/*  84 */       oldImports.addAll(newImports);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean canClearCache(HashSet<String> newImports, LinkedHashSet<String> oldImports) {
/*  95 */     if (newImports.size() != oldImports.size()) {
/*  96 */       return true;
/*     */     }
/*     */     
/*  99 */     for (String im : oldImports) {
/* 100 */       if (!newImports.contains(im)) {
/* 101 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 105 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearImportCache() {
/* 110 */     this.importClasses.clear();
/* 111 */     this.importPackages.clear();
/* 112 */     clearAllImportTypes();
/*     */   }
/*     */ 
/*     */   
/*     */   private void clearAllImportTypes() {
/* 117 */     HashSet<TypeDeclaration> removeTypes = new HashSet<>();
/*     */     
/* 119 */     for (Iterator<TypeDeclaration> i = this.cachedTypes.keySet().iterator(); i.hasNext(); ) {
/* 120 */       TypeDeclaration dec = i.next();
/* 121 */       if (!this.typesFactory.isJavaScriptType(dec) && !dec.equals(this.typesFactory.getDefaultTypeDeclaration())) {
/* 122 */         removeAllTypes(this.cachedTypes.get(dec));
/* 123 */         removeTypes.add(dec);
/*     */       } 
/*     */     } 
/* 126 */     this.cachedTypes.keySet().removeAll(removeTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void removeAllTypes(JavaScriptType type) {
/* 135 */     if (type != null) {
/*     */       
/* 137 */       this.typesFactory.removeType(type.getType().getQualifiedName());
/* 138 */       if (type.getExtendedClasses().size() > 0)
/*     */       {
/* 140 */         for (Iterator<JavaScriptType> i = type.getExtendedClasses().iterator(); i.hasNext(); ) {
/*     */           
/* 142 */           JavaScriptType extendedType = i.next();
/* 143 */           removeAllTypes(extendedType);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassFile getClassFile(JarManager manager, TypeDeclaration type) {
/* 156 */     String qName = removePackagesFromType(type.getQualifiedName());
/* 157 */     ClassFile file = super.getClassFile(manager, JavaScriptHelper.createNewTypeDeclaration(qName));
/* 158 */     if (file == null) {
/* 159 */       file = findFromClasses(manager, qName);
/* 160 */       if (file == null) {
/* 161 */         file = findFromImport(manager, qName);
/*     */       }
/*     */     } 
/* 164 */     return file;
/*     */   }
/*     */ 
/*     */   
/*     */   private String removePackagesFromType(String type) {
/* 169 */     if (type.startsWith("Packages."))
/*     */     {
/* 171 */       return RhinoJavaScriptAstParser.removePackages(type);
/*     */     }
/* 173 */     return type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ClassFile findFromClasses(JarManager manager, String name) {
/* 183 */     ClassFile file = null;
/* 184 */     for (String cls : this.importClasses) {
/* 185 */       if (cls.endsWith(name)) {
/* 186 */         file = manager.getClassEntry(cls);
/* 187 */         if (file != null)
/*     */           break; 
/*     */       } 
/*     */     } 
/* 191 */     return file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ClassFile findFromImport(JarManager manager, String name) {
/* 201 */     ClassFile file = null;
/* 202 */     for (String packageName : this.importPackages) {
/* 203 */       String cls = name.startsWith(".") ? (packageName + name) : (packageName + "." + name);
/* 204 */       file = manager.getClassEntry(cls);
/* 205 */       if (file != null)
/*     */         break; 
/*     */     } 
/* 208 */     return file;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\ast\jsType\RhinoJavaScriptTypesFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */