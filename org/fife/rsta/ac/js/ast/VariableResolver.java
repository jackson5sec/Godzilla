/*     */ package org.fife.rsta.ac.js.ast;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import org.fife.rsta.ac.js.ast.type.TypeDeclaration;
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
/*     */ public class VariableResolver
/*     */ {
/*  26 */   private HashMap<String, JavaScriptVariableDeclaration> localVariables = new HashMap<>();
/*     */ 
/*     */   
/*  29 */   private HashMap<String, JavaScriptVariableDeclaration> preProcessedVariables = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  34 */   private HashMap<String, JavaScriptVariableDeclaration> systemVariables = new HashMap<>();
/*     */ 
/*     */   
/*  37 */   private HashMap<String, JavaScriptFunctionDeclaration> localFunctions = new HashMap<>();
/*     */   
/*  39 */   private HashMap<String, JavaScriptFunctionDeclaration> preProcessedFunctions = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addLocalVariable(JavaScriptVariableDeclaration declaration) {
/*  49 */     this.localVariables.put(declaration.getName(), declaration);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPreProcessingVariable(JavaScriptVariableDeclaration declaration) {
/*  59 */     this.preProcessedVariables.put(declaration.getName(), declaration);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSystemVariable(JavaScriptVariableDeclaration declaration) {
/*  69 */     this.systemVariables.put(declaration.getName(), declaration);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removePreProcessingVariable(String name) {
/*  79 */     this.preProcessedVariables.remove(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeSystemVariable(String name) {
/*  89 */     this.systemVariables.remove(name);
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
/*     */   public JavaScriptVariableDeclaration findDeclaration(String name, int dot) {
/* 101 */     JavaScriptVariableDeclaration findDeclaration = findDeclaration(this.localVariables, name, dot);
/*     */ 
/*     */     
/* 104 */     findDeclaration = (findDeclaration == null) ? findDeclaration(this.preProcessedVariables, name, dot) : findDeclaration;
/*     */ 
/*     */     
/* 107 */     return (findDeclaration == null) ? findDeclaration(this.systemVariables, name, dot) : findDeclaration;
/*     */   }
/*     */ 
/*     */   
/*     */   public JavaScriptVariableDeclaration findDeclaration(String name, int dot, boolean local, boolean preProcessed, boolean system) {
/* 112 */     JavaScriptVariableDeclaration findDeclaration = local ? findDeclaration(this.localVariables, name, dot) : null;
/*     */     
/* 114 */     findDeclaration = (findDeclaration == null) ? (preProcessed ? findDeclaration(this.preProcessedVariables, name, dot) : null) : findDeclaration;
/*     */     
/* 116 */     return (findDeclaration == null) ? (system ? findDeclaration(this.systemVariables, name, dot) : null) : findDeclaration;
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
/*     */   public JavaScriptVariableDeclaration findNonLocalDeclaration(String name, int dot) {
/* 129 */     JavaScriptVariableDeclaration findDeclaration = findDeclaration(this.preProcessedVariables, name, dot);
/*     */     
/* 131 */     return (findDeclaration == null) ? findDeclaration(this.systemVariables, name, dot) : findDeclaration;
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
/*     */   private JavaScriptVariableDeclaration findDeclaration(HashMap<String, JavaScriptVariableDeclaration> variables, String name, int dot) {
/* 146 */     JavaScriptVariableDeclaration dec = variables.get(name);
/*     */     
/* 148 */     if (dec != null && (
/* 149 */       dec.getCodeBlock() == null || dec.getCodeBlock().contains(dot))) {
/* 150 */       int decOffs = dec.getOffset();
/* 151 */       if (dot <= decOffs) {
/* 152 */         return dec;
/*     */       }
/*     */     } 
/*     */     
/* 156 */     return null;
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
/*     */   public TypeDeclaration getTypeDeclarationForVariable(String name, int dot) {
/* 169 */     JavaScriptVariableDeclaration dec = findDeclaration(name, dot);
/* 170 */     return (dec != null) ? dec.getTypeDeclaration() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetLocalVariables() {
/* 178 */     this.localVariables.clear();
/* 179 */     this.localFunctions.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetPreProcessingVariables(boolean clear) {
/* 184 */     if (clear) {
/* 185 */       this.preProcessedVariables.clear();
/* 186 */       this.preProcessedFunctions.clear();
/*     */     } else {
/*     */       
/* 189 */       for (JavaScriptVariableDeclaration dec : this.preProcessedVariables.values()) {
/* 190 */         dec.resetVariableToOriginalType();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetSystemVariables() {
/* 197 */     this.systemVariables.clear();
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
/*     */   public TypeDeclaration resolveType(String varName, int dot) {
/* 212 */     return getTypeDeclarationForVariable(varName, dot);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addLocalFunction(JavaScriptFunctionDeclaration func) {
/* 218 */     this.localFunctions.put(func.getName(), func);
/*     */   }
/*     */ 
/*     */   
/*     */   public JavaScriptFunctionDeclaration findFunctionDeclaration(String name) {
/* 223 */     JavaScriptFunctionDeclaration dec = this.localFunctions.get(name);
/* 224 */     if (dec == null) {
/* 225 */       dec = this.preProcessedFunctions.get(name);
/*     */     }
/* 227 */     return dec;
/*     */   }
/*     */ 
/*     */   
/*     */   public JavaScriptFunctionDeclaration findFunctionDeclaration(String name, boolean local, boolean preProcessed) {
/* 232 */     JavaScriptFunctionDeclaration dec = local ? this.localFunctions.get(name) : null;
/* 233 */     if (dec == null) {
/* 234 */       dec = preProcessed ? this.preProcessedFunctions.get(name) : null;
/*     */     }
/* 236 */     return dec;
/*     */   }
/*     */   
/*     */   public JavaScriptFunctionDeclaration findFunctionDeclarationByFunctionName(String name, boolean local, boolean preprocessed) {
/* 240 */     JavaScriptFunctionDeclaration func = local ? findFirstFunction(name, this.localFunctions) : null;
/* 241 */     if (func == null) {
/* 242 */       func = preprocessed ? findFirstFunction(name, this.preProcessedFunctions) : null;
/*     */     }
/* 244 */     return func;
/*     */   }
/*     */ 
/*     */   
/*     */   private JavaScriptFunctionDeclaration findFirstFunction(String name, HashMap<String, JavaScriptFunctionDeclaration> functions) {
/* 249 */     for (JavaScriptFunctionDeclaration func : functions.values()) {
/* 250 */       if (name.equals(func.getFunctionName())) {
/* 251 */         return func;
/*     */       }
/*     */     } 
/* 254 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPreProcessingFunction(JavaScriptFunctionDeclaration func) {
/* 264 */     this.preProcessedFunctions.put(func.getName(), func);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\ast\VariableResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */