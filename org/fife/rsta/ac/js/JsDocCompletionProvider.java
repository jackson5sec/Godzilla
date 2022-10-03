/*     */ package org.fife.rsta.ac.js;
/*     */ 
/*     */ import javax.swing.Icon;
/*     */ import org.fife.ui.autocomplete.BasicCompletion;
/*     */ import org.fife.ui.autocomplete.Completion;
/*     */ import org.fife.ui.autocomplete.CompletionProvider;
/*     */ import org.fife.ui.autocomplete.DefaultCompletionProvider;
/*     */ import org.fife.ui.autocomplete.TemplateCompletion;
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
/*     */ public class JsDocCompletionProvider
/*     */   extends DefaultCompletionProvider
/*     */ {
/*     */   public JsDocCompletionProvider() {
/*  33 */     String[] simpleTags = { "abstract", "access", "alias", "augments", "author", "borrows", "callback", "classdesc", "constant", "constructor", "constructs", "copyright", "default", "deprecated", "desc", "enum", "event", "example", "exports", "external", "file", "fires", "global", "ignore", "inner", "instance", "kind", "lends", "license", "member", "memberof", "method", "mixes", "mixin", "module", "name", "namespace", "private", "property", "protected", "public", "readonly", "requires", "see", "since", "static", "summary", "this", "throws", "todo", "type", "typedef", "variation", "version" };
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
/*  45 */     for (int i = 0; i < simpleTags.length; i++) {
/*  46 */       addCompletion((Completion)new JsDocCompletion((CompletionProvider)this, "@" + simpleTags[i]));
/*     */     }
/*     */ 
/*     */     
/*  50 */     addCompletion((Completion)new JsDocParameterizedCompletion((CompletionProvider)this, "@param", "@param {type} varName", "@param {${}} ${varName} ${cursor}"));
/*     */     
/*  52 */     addCompletion((Completion)new JsDocParameterizedCompletion((CompletionProvider)this, "@return", "@return {type} description", "@return {${type}} ${}"));
/*     */     
/*  54 */     addCompletion((Completion)new JsDocParameterizedCompletion((CompletionProvider)this, "@returns", "@returns {type} description", "@returns {${type}} ${}"));
/*     */ 
/*     */ 
/*     */     
/*  58 */     addCompletion((Completion)new JsDocParameterizedCompletion((CompletionProvider)this, "{@link}", "{@link}", "{@link ${}}${cursor}"));
/*  59 */     addCompletion((Completion)new JsDocParameterizedCompletion((CompletionProvider)this, "{@linkplain}", "{@linkplain}", "{@linkplain ${}}${cursor}"));
/*  60 */     addCompletion((Completion)new JsDocParameterizedCompletion((CompletionProvider)this, "{@linkcode}", "{@linkcode}", "{@linkcode ${}}${cursor}"));
/*  61 */     addCompletion((Completion)new JsDocParameterizedCompletion((CompletionProvider)this, "{@tutorial}", "{@tutorial}", "{@tutorial ${tutorialID}}${cursor}"));
/*     */ 
/*     */     
/*  64 */     addCompletion((Completion)new JsDocCompletion((CompletionProvider)this, "null", "<code>null</code>", "&lt;code>null&lt;/code>", "template"));
/*  65 */     addCompletion((Completion)new JsDocCompletion((CompletionProvider)this, "true", "<code>true</code>", "&lt;code>true&lt;/code>", "template"));
/*  66 */     addCompletion((Completion)new JsDocCompletion((CompletionProvider)this, "false", "<code>false</code>", "&lt;code>false&lt;/code>", "template"));
/*     */     
/*  68 */     setAutoActivationRules(false, "{@");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isValidChar(char ch) {
/*  78 */     return (Character.isLetterOrDigit(ch) || ch == '_' || ch == '@' || ch == '{' || ch == '}');
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class JsDocCompletion
/*     */     extends BasicCompletion
/*     */   {
/*     */     private String inputText;
/*     */ 
/*     */     
/*     */     private String icon;
/*     */ 
/*     */     
/*     */     public JsDocCompletion(CompletionProvider provider, String replacementText) {
/*  93 */       super(provider, replacementText);
/*  94 */       this.inputText = replacementText;
/*  95 */       this.icon = "jsdoc_item";
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public JsDocCompletion(CompletionProvider provider, String inputText, String replacementText, String shortDesc, String icon) {
/* 101 */       super(provider, replacementText, shortDesc, shortDesc);
/* 102 */       this.inputText = inputText;
/* 103 */       this.icon = icon;
/*     */     }
/*     */ 
/*     */     
/*     */     public Icon getIcon() {
/* 108 */       return IconFactory.getIcon(this.icon);
/*     */     }
/*     */ 
/*     */     
/*     */     public String getInputText() {
/* 113 */       return this.inputText;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class JsDocParameterizedCompletion
/*     */     extends TemplateCompletion
/*     */   {
/*     */     private String icon;
/*     */ 
/*     */     
/*     */     public JsDocParameterizedCompletion(CompletionProvider provider, String inputText, String definitionString, String template) {
/* 125 */       this(provider, inputText, definitionString, template, "jsdoc_item");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public JsDocParameterizedCompletion(CompletionProvider provider, String inputText, String definitionString, String template, String icon) {
/* 132 */       super(provider, inputText, definitionString, template);
/* 133 */       setIcon(icon);
/*     */     }
/*     */ 
/*     */     
/*     */     public Icon getIcon() {
/* 138 */       return IconFactory.getIcon(this.icon);
/*     */     }
/*     */     
/*     */     public void setIcon(String icon) {
/* 142 */       this.icon = icon;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\JsDocCompletionProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */