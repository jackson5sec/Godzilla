/*     */ package org.fife.rsta.ac.java;
/*     */ 
/*     */ import java.awt.Point;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import org.fife.rsta.ac.ShorthandCompletionCache;
/*     */ import org.fife.rsta.ac.java.buildpath.LibraryInfo;
/*     */ import org.fife.rsta.ac.java.rjc.ast.CompilationUnit;
/*     */ import org.fife.ui.autocomplete.AbstractCompletionProvider;
/*     */ import org.fife.ui.autocomplete.Completion;
/*     */ import org.fife.ui.autocomplete.CompletionProvider;
/*     */ import org.fife.ui.autocomplete.DefaultCompletionProvider;
/*     */ import org.fife.ui.autocomplete.LanguageAwareCompletionProvider;
/*     */ import org.fife.ui.autocomplete.ParameterizedCompletion;
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
/*     */ public class JavaCompletionProvider
/*     */   extends LanguageAwareCompletionProvider
/*     */ {
/*     */   private SourceCompletionProvider sourceProvider;
/*     */   private CompilationUnit cu;
/*     */   
/*     */   public JavaCompletionProvider() {
/*  49 */     this((JarManager)null);
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
/*     */   public JavaCompletionProvider(JarManager jarManager) {
/*  64 */     super((CompletionProvider)new SourceCompletionProvider(jarManager));
/*  65 */     this
/*  66 */       .sourceProvider = (SourceCompletionProvider)getDefaultCompletionProvider();
/*  67 */     this.sourceProvider.setJavaProvider(this);
/*  68 */     setShorthandCompletionCache(new JavaShorthandCompletionCache(this.sourceProvider, new DefaultCompletionProvider()));
/*     */     
/*  70 */     setDocCommentCompletionProvider((CompletionProvider)new DocCommentCompletionProvider());
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
/*     */   public void addJar(LibraryInfo info) throws IOException {
/*  88 */     this.sourceProvider.addJar(info);
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
/*     */   public void clearJars() {
/* 100 */     this.sourceProvider.clearJars();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAlreadyEnteredText(JTextComponent comp) {
/* 111 */     return this.sourceProvider.getAlreadyEnteredText(comp);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized CompilationUnit getCompilationUnit() {
/* 116 */     return this.cu;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Completion> getCompletionsAt(JTextComponent tc, Point p) {
/* 125 */     return this.sourceProvider.getCompletionsAt(tc, p);
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
/*     */   public List<LibraryInfo> getJars() {
/* 140 */     return this.sourceProvider.getJars();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ParameterizedCompletion> getParameterizedCompletions(JTextComponent tc) {
/* 150 */     return null;
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
/*     */   public boolean removeJar(File jar) {
/* 163 */     return this.sourceProvider.removeJar(jar);
/*     */   }
/*     */ 
/*     */   
/*     */   private void setCommentCompletions(ShorthandCompletionCache shorthandCache) {
/* 168 */     AbstractCompletionProvider provider = shorthandCache.getCommentProvider();
/* 169 */     if (provider != null) {
/* 170 */       for (Completion c : shorthandCache.getCommentCompletions()) {
/* 171 */         provider.addCompletion(c);
/*     */       }
/* 173 */       setCommentCompletionProvider((CompletionProvider)provider);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void setCompilationUnit(CompilationUnit cu) {
/* 179 */     this.cu = cu;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setShorthandCompletionCache(ShorthandCompletionCache cache) {
/* 187 */     this.sourceProvider.setShorthandCache(cache);
/*     */     
/* 189 */     setCommentCompletions(cache);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\JavaCompletionProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */