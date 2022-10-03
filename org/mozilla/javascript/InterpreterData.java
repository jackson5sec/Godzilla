/*     */ package org.mozilla.javascript;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.mozilla.javascript.debug.DebuggableScript;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class InterpreterData
/*     */   implements Serializable, DebuggableScript
/*     */ {
/*     */   static final long serialVersionUID = 5067677351589230234L;
/*     */   static final int INITIAL_MAX_ICODE_LENGTH = 1024;
/*     */   static final int INITIAL_STRINGTABLE_SIZE = 64;
/*     */   static final int INITIAL_NUMBERTABLE_SIZE = 64;
/*     */   String itsName;
/*     */   String itsSourceFile;
/*     */   boolean itsNeedsActivation;
/*     */   int itsFunctionType;
/*     */   String[] itsStringTable;
/*     */   double[] itsDoubleTable;
/*     */   InterpreterData[] itsNestedFunctions;
/*     */   Object[] itsRegExpLiterals;
/*     */   byte[] itsICode;
/*     */   int[] itsExceptionTable;
/*     */   int itsMaxVars;
/*     */   int itsMaxLocals;
/*     */   int itsMaxStack;
/*     */   int itsMaxFrameArray;
/*     */   String[] argNames;
/*     */   boolean[] argIsConst;
/*     */   int argCount;
/*     */   int itsMaxCalleeArgs;
/*     */   String encodedSource;
/*     */   int encodedSourceStart;
/*     */   int encodedSourceEnd;
/*     */   int languageVersion;
/*     */   boolean isStrict;
/*     */   boolean topLevel;
/*     */   Object[] literalIds;
/*     */   UintMap longJumps;
/*     */   int firstLinePC;
/*     */   InterpreterData parentData;
/*     */   boolean evalScriptFlag;
/*     */   
/*     */   InterpreterData(int languageVersion, String sourceFile, String encodedSource, boolean isStrict) {
/*  86 */     this.firstLinePC = -1; this.languageVersion = languageVersion; this.itsSourceFile = sourceFile; this.encodedSource = encodedSource; this.isStrict = isStrict; init(); } InterpreterData(InterpreterData parent) { this.firstLinePC = -1;
/*     */     this.parentData = parent;
/*     */     this.languageVersion = parent.languageVersion;
/*     */     this.itsSourceFile = parent.itsSourceFile;
/*     */     this.encodedSource = parent.encodedSource;
/*     */     init(); }
/*     */   private void init() { this.itsICode = new byte[1024];
/*     */     this.itsStringTable = new String[64]; } public boolean isTopLevel() {
/*  94 */     return this.topLevel;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFunction() {
/*  99 */     return (this.itsFunctionType != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFunctionName() {
/* 104 */     return this.itsName;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getParamCount() {
/* 109 */     return this.argCount;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getParamAndVarCount() {
/* 114 */     return this.argNames.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getParamOrVarName(int index) {
/* 119 */     return this.argNames[index];
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getParamOrVarConst(int index) {
/* 124 */     return this.argIsConst[index];
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSourceName() {
/* 129 */     return this.itsSourceFile;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isGeneratedScript() {
/* 134 */     return ScriptRuntime.isGeneratedScript(this.itsSourceFile);
/*     */   }
/*     */ 
/*     */   
/*     */   public int[] getLineNumbers() {
/* 139 */     return Interpreter.getLineNumbers(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFunctionCount() {
/* 144 */     return (this.itsNestedFunctions == null) ? 0 : this.itsNestedFunctions.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public DebuggableScript getFunction(int index) {
/* 149 */     return this.itsNestedFunctions[index];
/*     */   }
/*     */ 
/*     */   
/*     */   public DebuggableScript getParent() {
/* 154 */     return this.parentData;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\InterpreterData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */