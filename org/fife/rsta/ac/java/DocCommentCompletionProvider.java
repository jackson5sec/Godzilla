/*     */ package org.fife.rsta.ac.java;
/*     */ 
/*     */ import java.awt.Graphics;
/*     */ import javax.swing.Icon;
/*     */ import org.fife.ui.autocomplete.BasicCompletion;
/*     */ import org.fife.ui.autocomplete.CompletionProvider;
/*     */ import org.fife.ui.autocomplete.DefaultCompletionProvider;
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
/*     */ class DocCommentCompletionProvider
/*     */   extends DefaultCompletionProvider
/*     */ {
/*     */   public DocCommentCompletionProvider() {
/*  33 */     addCompletion(new JavadocCompletion((CompletionProvider)this, "@author"));
/*  34 */     addCompletion(new JavadocCompletion((CompletionProvider)this, "@deprecated"));
/*  35 */     addCompletion(new JavadocCompletion((CompletionProvider)this, "@exception"));
/*  36 */     addCompletion(new JavadocCompletion((CompletionProvider)this, "@param"));
/*  37 */     addCompletion(new JavadocCompletion((CompletionProvider)this, "@return"));
/*  38 */     addCompletion(new JavadocCompletion((CompletionProvider)this, "@see"));
/*  39 */     addCompletion(new JavadocCompletion((CompletionProvider)this, "@serial"));
/*  40 */     addCompletion(new JavadocCompletion((CompletionProvider)this, "@serialData"));
/*  41 */     addCompletion(new JavadocCompletion((CompletionProvider)this, "@serialField"));
/*  42 */     addCompletion(new JavadocCompletion((CompletionProvider)this, "@since"));
/*  43 */     addCompletion(new JavadocCompletion((CompletionProvider)this, "@throws"));
/*  44 */     addCompletion(new JavadocCompletion((CompletionProvider)this, "@version"));
/*     */ 
/*     */     
/*  47 */     addCompletion(new JavadocCompletion((CompletionProvider)this, "@category"));
/*  48 */     addCompletion(new JavadocCompletion((CompletionProvider)this, "@example"));
/*  49 */     addCompletion(new JavadocCompletion((CompletionProvider)this, "@tutorial"));
/*  50 */     addCompletion(new JavadocCompletion((CompletionProvider)this, "@index"));
/*  51 */     addCompletion(new JavadocCompletion((CompletionProvider)this, "@exclude"));
/*  52 */     addCompletion(new JavadocCompletion((CompletionProvider)this, "@todo"));
/*  53 */     addCompletion(new JavadocCompletion((CompletionProvider)this, "@internal"));
/*  54 */     addCompletion(new JavadocCompletion((CompletionProvider)this, "@obsolete"));
/*  55 */     addCompletion(new JavadocCompletion((CompletionProvider)this, "@threadsafety"));
/*     */ 
/*     */     
/*  58 */     addCompletion(new JavadocTemplateCompletion((CompletionProvider)this, "{@code}", "{@code}", "{@code ${}}${cursor}"));
/*  59 */     addCompletion(new JavadocTemplateCompletion((CompletionProvider)this, "{@docRoot}", "{@docRoot}", "{@docRoot ${}}${cursor}"));
/*  60 */     addCompletion(new JavadocTemplateCompletion((CompletionProvider)this, "{@inheritDoc}", "{@inheritDoc}", "{@inheritDoc ${}}${cursor}"));
/*  61 */     addCompletion(new JavadocTemplateCompletion((CompletionProvider)this, "{@link}", "{@link}", "{@link ${}}${cursor}"));
/*  62 */     addCompletion(new JavadocTemplateCompletion((CompletionProvider)this, "{@linkplain}", "{@linkplain}", "{@linkplain ${}}${cursor}"));
/*  63 */     addCompletion(new JavadocTemplateCompletion((CompletionProvider)this, "{@literal}", "{@literal}", "{@literal ${}}${cursor}"));
/*  64 */     addCompletion(new JavadocTemplateCompletion((CompletionProvider)this, "{@value}", "{@value}", "{@value ${}}${cursor}"));
/*     */ 
/*     */     
/*  67 */     addCompletion(new JavaShorthandCompletion((CompletionProvider)this, "null", "<code>null</code>", "<code>null</code>"));
/*  68 */     addCompletion(new JavaShorthandCompletion((CompletionProvider)this, "true", "<code>true</code>", "<code>true</code>"));
/*  69 */     addCompletion(new JavaShorthandCompletion((CompletionProvider)this, "false", "<code>false</code>", "<code>false</code>"));
/*     */     
/*  71 */     setAutoActivationRules(false, "{@");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isValidChar(char ch) {
/*  81 */     return (Character.isLetterOrDigit(ch) || ch == '_' || ch == '@' || ch == '{' || ch == '}');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class JavadocCompletion
/*     */     extends BasicCompletion
/*     */     implements JavaSourceCompletion
/*     */   {
/*     */     public JavadocCompletion(CompletionProvider provider, String replacementText) {
/*  94 */       super(provider, replacementText);
/*     */     }
/*     */ 
/*     */     
/*     */     public Icon getIcon() {
/*  99 */       return IconFactory.get().getIcon("javadocItemIcon");
/*     */     }
/*     */ 
/*     */     
/*     */     public void rendererText(Graphics g, int x, int y, boolean selected) {
/* 104 */       g.drawString(getReplacementText(), x, y);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class JavadocTemplateCompletion
/*     */     extends JavaTemplateCompletion
/*     */   {
/*     */     public JavadocTemplateCompletion(CompletionProvider provider, String inputText, String definitionString, String template) {
/* 115 */       super(provider, inputText, definitionString, template);
/* 116 */       setIcon("javadocItemIcon");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\DocCommentCompletionProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */