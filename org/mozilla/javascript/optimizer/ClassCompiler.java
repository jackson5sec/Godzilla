/*     */ package org.mozilla.javascript.optimizer;
/*     */ 
/*     */ import org.mozilla.javascript.CompilerEnvirons;
/*     */ import org.mozilla.javascript.IRFactory;
/*     */ import org.mozilla.javascript.JavaAdapter;
/*     */ import org.mozilla.javascript.ObjToIntMap;
/*     */ import org.mozilla.javascript.Parser;
/*     */ import org.mozilla.javascript.ScriptRuntime;
/*     */ import org.mozilla.javascript.ast.AstRoot;
/*     */ import org.mozilla.javascript.ast.FunctionNode;
/*     */ import org.mozilla.javascript.ast.ScriptNode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClassCompiler
/*     */ {
/*     */   private String mainMethodClassName;
/*     */   private CompilerEnvirons compilerEnv;
/*     */   private Class<?> targetExtends;
/*     */   private Class<?>[] targetImplements;
/*     */   
/*     */   public ClassCompiler(CompilerEnvirons compilerEnv) {
/*  29 */     if (compilerEnv == null) throw new IllegalArgumentException(); 
/*  30 */     this.compilerEnv = compilerEnv;
/*  31 */     this.mainMethodClassName = "org.mozilla.javascript.optimizer.OptRuntime";
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
/*     */   public void setMainMethodClass(String className) {
/*  45 */     this.mainMethodClassName = className;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMainMethodClass() {
/*  54 */     return this.mainMethodClassName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompilerEnvirons getCompilerEnv() {
/*  62 */     return this.compilerEnv;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getTargetExtends() {
/*  70 */     return this.targetExtends;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetExtends(Class<?> extendsClass) {
/*  80 */     this.targetExtends = extendsClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?>[] getTargetImplements() {
/*  88 */     return (this.targetImplements == null) ? null : (Class[])this.targetImplements.clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetImplements(Class<?>[] implementsClasses) {
/*  99 */     this.targetImplements = (implementsClasses == null) ? null : (Class[])implementsClasses.clone();
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
/*     */   protected String makeAuxiliaryClassName(String mainClassName, String auxMarker) {
/* 112 */     return mainClassName + auxMarker;
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
/*     */   
/*     */   public Object[] compileToClassFiles(String source, String sourceLocation, int lineno, String mainClassName) {
/*     */     String scriptClassName;
/* 133 */     Parser p = new Parser(this.compilerEnv);
/* 134 */     AstRoot ast = p.parse(source, sourceLocation, lineno);
/* 135 */     IRFactory irf = new IRFactory(this.compilerEnv);
/* 136 */     ScriptNode tree = irf.transformTree(ast);
/*     */ 
/*     */     
/* 139 */     irf = null;
/* 140 */     ast = null;
/* 141 */     p = null;
/*     */     
/* 143 */     Class<?> superClass = getTargetExtends();
/* 144 */     Class<?>[] interfaces = getTargetImplements();
/*     */     
/* 146 */     boolean isPrimary = (interfaces == null && superClass == null);
/* 147 */     if (isPrimary) {
/* 148 */       scriptClassName = mainClassName;
/*     */     } else {
/* 150 */       scriptClassName = makeAuxiliaryClassName(mainClassName, "1");
/*     */     } 
/*     */     
/* 153 */     Codegen codegen = new Codegen();
/* 154 */     codegen.setMainMethodClass(this.mainMethodClassName);
/* 155 */     byte[] scriptClassBytes = codegen.compileToClassFile(this.compilerEnv, scriptClassName, tree, tree.getEncodedSource(), false);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 160 */     if (isPrimary) {
/* 161 */       return new Object[] { scriptClassName, scriptClassBytes };
/*     */     }
/* 163 */     int functionCount = tree.getFunctionCount();
/* 164 */     ObjToIntMap functionNames = new ObjToIntMap(functionCount);
/* 165 */     for (int i = 0; i != functionCount; i++) {
/* 166 */       FunctionNode ofn = tree.getFunctionNode(i);
/* 167 */       String name = ofn.getName();
/* 168 */       if (name != null && name.length() != 0) {
/* 169 */         functionNames.put(name, ofn.getParamCount());
/*     */       }
/*     */     } 
/* 172 */     if (superClass == null) {
/* 173 */       superClass = ScriptRuntime.ObjectClass;
/*     */     }
/* 175 */     byte[] mainClassBytes = JavaAdapter.createAdapterCode(functionNames, mainClassName, superClass, interfaces, scriptClassName);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 180 */     return new Object[] { mainClassName, mainClassBytes, scriptClassName, scriptClassBytes };
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\optimizer\ClassCompiler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */