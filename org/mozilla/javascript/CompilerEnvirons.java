/*     */ package org.mozilla.javascript;
/*     */ 
/*     */ import java.util.Set;
/*     */ import org.mozilla.javascript.ast.ErrorCollector;
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
/*     */ public class CompilerEnvirons
/*     */ {
/*  17 */   private ErrorReporter errorReporter = DefaultErrorReporter.instance;
/*  18 */   private int languageVersion = 0;
/*     */   private boolean generateDebugInfo = true;
/*     */   private boolean reservedKeywordAsIdentifier = true;
/*     */   private boolean allowMemberExprAsFunctionName = false;
/*     */   private boolean xmlAvailable = true;
/*  23 */   private int optimizationLevel = 0;
/*     */   
/*     */   private boolean generatingSource = true;
/*     */   
/*     */   private boolean strictMode = false;
/*     */   private boolean warningAsError = false;
/*     */   private boolean generateObserverCount = false;
/*     */   private boolean recordingComments;
/*     */   
/*     */   public void initFromContext(Context cx) {
/*  33 */     setErrorReporter(cx.getErrorReporter());
/*  34 */     this.languageVersion = cx.getLanguageVersion();
/*  35 */     this.generateDebugInfo = (!cx.isGeneratingDebugChanged() || cx.isGeneratingDebug());
/*     */     
/*  37 */     this.reservedKeywordAsIdentifier = cx.hasFeature(3);
/*     */     
/*  39 */     this.allowMemberExprAsFunctionName = cx.hasFeature(2);
/*     */     
/*  41 */     this.strictMode = cx.hasFeature(11);
/*     */     
/*  43 */     this.warningAsError = cx.hasFeature(12);
/*  44 */     this.xmlAvailable = cx.hasFeature(6);
/*     */ 
/*     */     
/*  47 */     this.optimizationLevel = cx.getOptimizationLevel();
/*     */     
/*  49 */     this.generatingSource = cx.isGeneratingSource();
/*  50 */     this.activationNames = cx.activationNames;
/*     */ 
/*     */     
/*  53 */     this.generateObserverCount = cx.generateObserverCount;
/*     */   }
/*     */   private boolean recordingLocalJsDocComments; private boolean recoverFromErrors; private boolean warnTrailingComma; private boolean ideMode; private boolean allowSharpComments = false; Set<String> activationNames;
/*     */   
/*     */   public final ErrorReporter getErrorReporter() {
/*  58 */     return this.errorReporter;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setErrorReporter(ErrorReporter errorReporter) {
/*  63 */     if (errorReporter == null) throw new IllegalArgumentException(); 
/*  64 */     this.errorReporter = errorReporter;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getLanguageVersion() {
/*  69 */     return this.languageVersion;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLanguageVersion(int languageVersion) {
/*  74 */     Context.checkLanguageVersion(languageVersion);
/*  75 */     this.languageVersion = languageVersion;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isGenerateDebugInfo() {
/*  80 */     return this.generateDebugInfo;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setGenerateDebugInfo(boolean flag) {
/*  85 */     this.generateDebugInfo = flag;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isReservedKeywordAsIdentifier() {
/*  90 */     return this.reservedKeywordAsIdentifier;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setReservedKeywordAsIdentifier(boolean flag) {
/*  95 */     this.reservedKeywordAsIdentifier = flag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isAllowMemberExprAsFunctionName() {
/* 104 */     return this.allowMemberExprAsFunctionName;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAllowMemberExprAsFunctionName(boolean flag) {
/* 109 */     this.allowMemberExprAsFunctionName = flag;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isXmlAvailable() {
/* 114 */     return this.xmlAvailable;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setXmlAvailable(boolean flag) {
/* 119 */     this.xmlAvailable = flag;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getOptimizationLevel() {
/* 124 */     return this.optimizationLevel;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOptimizationLevel(int level) {
/* 129 */     Context.checkOptimizationLevel(level);
/* 130 */     this.optimizationLevel = level;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isGeneratingSource() {
/* 135 */     return this.generatingSource;
/*     */   }
/*     */   
/*     */   public boolean getWarnTrailingComma() {
/* 139 */     return this.warnTrailingComma;
/*     */   }
/*     */   
/*     */   public void setWarnTrailingComma(boolean warn) {
/* 143 */     this.warnTrailingComma = warn;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isStrictMode() {
/* 148 */     return this.strictMode;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setStrictMode(boolean strict) {
/* 153 */     this.strictMode = strict;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean reportWarningAsError() {
/* 158 */     return this.warningAsError;
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
/*     */   public void setGeneratingSource(boolean generatingSource) {
/* 172 */     this.generatingSource = generatingSource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isGenerateObserverCount() {
/* 180 */     return this.generateObserverCount;
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
/*     */   public void setGenerateObserverCount(boolean generateObserverCount) {
/* 195 */     this.generateObserverCount = generateObserverCount;
/*     */   }
/*     */   
/*     */   public boolean isRecordingComments() {
/* 199 */     return this.recordingComments;
/*     */   }
/*     */   
/*     */   public void setRecordingComments(boolean record) {
/* 203 */     this.recordingComments = record;
/*     */   }
/*     */   
/*     */   public boolean isRecordingLocalJsDocComments() {
/* 207 */     return this.recordingLocalJsDocComments;
/*     */   }
/*     */   
/*     */   public void setRecordingLocalJsDocComments(boolean record) {
/* 211 */     this.recordingLocalJsDocComments = record;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRecoverFromErrors(boolean recover) {
/* 220 */     this.recoverFromErrors = recover;
/*     */   }
/*     */   
/*     */   public boolean recoverFromErrors() {
/* 224 */     return this.recoverFromErrors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIdeMode(boolean ide) {
/* 232 */     this.ideMode = ide;
/*     */   }
/*     */   
/*     */   public boolean isIdeMode() {
/* 236 */     return this.ideMode;
/*     */   }
/*     */   
/*     */   public Set<String> getActivationNames() {
/* 240 */     return this.activationNames;
/*     */   }
/*     */   
/*     */   public void setActivationNames(Set<String> activationNames) {
/* 244 */     this.activationNames = activationNames;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAllowSharpComments(boolean allow) {
/* 251 */     this.allowSharpComments = allow;
/*     */   }
/*     */   
/*     */   public boolean getAllowSharpComments() {
/* 255 */     return this.allowSharpComments;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CompilerEnvirons ideEnvirons() {
/* 264 */     CompilerEnvirons env = new CompilerEnvirons();
/* 265 */     env.setRecoverFromErrors(true);
/* 266 */     env.setRecordingComments(true);
/* 267 */     env.setStrictMode(true);
/* 268 */     env.setWarnTrailingComma(true);
/* 269 */     env.setLanguageVersion(170);
/* 270 */     env.setReservedKeywordAsIdentifier(true);
/* 271 */     env.setIdeMode(true);
/* 272 */     env.setErrorReporter((ErrorReporter)new ErrorCollector());
/* 273 */     return env;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\CompilerEnvirons.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */