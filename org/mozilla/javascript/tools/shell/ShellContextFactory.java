/*     */ package org.mozilla.javascript.tools.shell;
/*     */ 
/*     */ import org.mozilla.javascript.Context;
/*     */ import org.mozilla.javascript.ContextFactory;
/*     */ import org.mozilla.javascript.ErrorReporter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ShellContextFactory
/*     */   extends ContextFactory
/*     */ {
/*     */   private boolean strictMode;
/*     */   private boolean warningAsError;
/*  15 */   private int languageVersion = 180;
/*     */   
/*     */   private int optimizationLevel;
/*     */   
/*     */   private boolean generatingDebug;
/*     */   private boolean allowReservedKeywords = true;
/*     */   private ErrorReporter errorReporter;
/*     */   private String characterEncoding;
/*     */   
/*     */   protected boolean hasFeature(Context cx, int featureIndex) {
/*  25 */     switch (featureIndex) {
/*     */       case 8:
/*     */       case 9:
/*     */       case 11:
/*  29 */         return this.strictMode;
/*     */       
/*     */       case 3:
/*  32 */         return this.allowReservedKeywords;
/*     */       
/*     */       case 12:
/*  35 */         return this.warningAsError;
/*     */       
/*     */       case 10:
/*  38 */         return this.generatingDebug;
/*     */     } 
/*  40 */     return super.hasFeature(cx, featureIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onContextCreated(Context cx) {
/*  46 */     cx.setLanguageVersion(this.languageVersion);
/*  47 */     cx.setOptimizationLevel(this.optimizationLevel);
/*  48 */     if (this.errorReporter != null) {
/*  49 */       cx.setErrorReporter(this.errorReporter);
/*     */     }
/*  51 */     cx.setGeneratingDebug(this.generatingDebug);
/*  52 */     super.onContextCreated(cx);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setStrictMode(boolean flag) {
/*  57 */     checkNotSealed();
/*  58 */     this.strictMode = flag;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setWarningAsError(boolean flag) {
/*  63 */     checkNotSealed();
/*  64 */     this.warningAsError = flag;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLanguageVersion(int version) {
/*  69 */     Context.checkLanguageVersion(version);
/*  70 */     checkNotSealed();
/*  71 */     this.languageVersion = version;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOptimizationLevel(int optimizationLevel) {
/*  76 */     Context.checkOptimizationLevel(optimizationLevel);
/*  77 */     checkNotSealed();
/*  78 */     this.optimizationLevel = optimizationLevel;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setErrorReporter(ErrorReporter errorReporter) {
/*  83 */     if (errorReporter == null) throw new IllegalArgumentException(); 
/*  84 */     this.errorReporter = errorReporter;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setGeneratingDebug(boolean generatingDebug) {
/*  89 */     this.generatingDebug = generatingDebug;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCharacterEncoding() {
/*  94 */     return this.characterEncoding;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCharacterEncoding(String characterEncoding) {
/*  99 */     this.characterEncoding = characterEncoding;
/*     */   }
/*     */   
/*     */   public void setAllowReservedKeywords(boolean allowReservedKeywords) {
/* 103 */     this.allowReservedKeywords = allowReservedKeywords;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\tools\shell\ShellContextFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */