/*     */ package org.fife.rsta.ac.js;
/*     */ 
/*     */ import org.fife.rsta.ac.ShorthandCompletionCache;
/*     */ import org.fife.rsta.ac.java.JarManager;
/*     */ import org.fife.ui.autocomplete.AbstractCompletionProvider;
/*     */ import org.fife.ui.autocomplete.Completion;
/*     */ import org.fife.ui.autocomplete.CompletionProvider;
/*     */ import org.fife.ui.autocomplete.DefaultCompletionProvider;
/*     */ import org.fife.ui.autocomplete.LanguageAwareCompletionProvider;
/*     */ import org.mozilla.javascript.ast.AstRoot;
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
/*     */ public class JavaScriptCompletionProvider
/*     */   extends LanguageAwareCompletionProvider
/*     */ {
/*     */   private AstRoot astRoot;
/*     */   private SourceCompletionProvider sourceProvider;
/*     */   private JavaScriptLanguageSupport languageSupport;
/*     */   
/*     */   public JavaScriptCompletionProvider(JarManager jarManager, JavaScriptLanguageSupport languageSupport) {
/*  46 */     this(new SourceCompletionProvider(languageSupport.isXmlAvailable()), jarManager, languageSupport);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaScriptCompletionProvider(SourceCompletionProvider provider, JarManager jarManager, JavaScriptLanguageSupport ls) {
/*  53 */     super((CompletionProvider)provider);
/*  54 */     this.sourceProvider = (SourceCompletionProvider)getDefaultCompletionProvider();
/*  55 */     this.sourceProvider.setJarManager(jarManager);
/*  56 */     this.languageSupport = ls;
/*     */     
/*  58 */     setShorthandCompletionCache(new JavaScriptShorthandCompletionCache(this.sourceProvider, new DefaultCompletionProvider(), ls
/*  59 */           .isXmlAvailable()));
/*  60 */     this.sourceProvider.setParent(this);
/*     */     
/*  62 */     setDocCommentCompletionProvider((CompletionProvider)new JsDocCompletionProvider());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized AstRoot getASTRoot() {
/*  72 */     return this.astRoot;
/*     */   }
/*     */ 
/*     */   
/*     */   public JarManager getJarManager() {
/*  77 */     return ((SourceCompletionProvider)getDefaultCompletionProvider())
/*  78 */       .getJarManager();
/*     */   }
/*     */   
/*     */   public JavaScriptLanguageSupport getLanguageSupport() {
/*  82 */     return this.languageSupport;
/*     */   }
/*     */ 
/*     */   
/*     */   public SourceCompletionProvider getProvider() {
/*  87 */     return this.sourceProvider;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setShorthandCompletionCache(ShorthandCompletionCache shorthandCache) {
/*  95 */     this.sourceProvider.setShorthandCache(shorthandCache);
/*     */     
/*  97 */     setCommentCompletions(shorthandCache);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setCommentCompletions(ShorthandCompletionCache shorthandCache) {
/* 105 */     AbstractCompletionProvider provider = shorthandCache.getCommentProvider();
/* 106 */     if (provider != null) {
/* 107 */       for (Completion c : shorthandCache.getCommentCompletions()) {
/* 108 */         provider.addCompletion(c);
/*     */       }
/* 110 */       setCommentCompletionProvider((CompletionProvider)provider);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setASTRoot(AstRoot root) {
/* 121 */     this.astRoot = root;
/*     */   }
/*     */ 
/*     */   
/*     */   protected synchronized void reparseDocument(int offset) {
/* 126 */     this.sourceProvider.parseDocument(offset);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\JavaScriptCompletionProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */