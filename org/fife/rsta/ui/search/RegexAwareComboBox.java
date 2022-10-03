/*     */ package org.fife.rsta.ui.search;
/*     */ 
/*     */ import java.awt.Image;
/*     */ import javax.swing.ComboBoxModel;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import org.fife.rsta.ui.ContentAssistable;
/*     */ import org.fife.rsta.ui.MaxWidthComboBox;
/*     */ import org.fife.rsta.ui.RComboBoxModel;
/*     */ import org.fife.ui.autocomplete.AutoCompletion;
/*     */ import org.fife.ui.autocomplete.BasicCompletion;
/*     */ import org.fife.ui.autocomplete.Completion;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RegexAwareComboBox<E>
/*     */   extends MaxWidthComboBox<E>
/*     */   implements ContentAssistable
/*     */ {
/*     */   private boolean enabled;
/*     */   private boolean replace;
/*     */   private AutoCompletion ac;
/*     */   private RegexAwareProvider provider;
/*     */   private Image contentAssistImage;
/*     */   
/*     */   public RegexAwareComboBox(boolean replace) {
/*  50 */     this((ComboBoxModel<E>)new RComboBoxModel(), 200, replace);
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
/*     */   public RegexAwareComboBox(ComboBoxModel<E> model, int maxWidth, boolean replace) {
/*  65 */     super(model, maxWidth);
/*  66 */     setEditable(true);
/*  67 */     this.replace = replace;
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
/*     */   private void addFindFieldCompletions(RegexAwareProvider p) {
/*  80 */     p.addCompletion((Completion)new RegexCompletion((CompletionProvider)p, "\\\\", "\\\\", "\\\\ - Backslash"));
/*  81 */     p.addCompletion((Completion)new RegexCompletion((CompletionProvider)p, "\\t", "\\t", "\\t - Tab"));
/*  82 */     p.addCompletion((Completion)new RegexCompletion((CompletionProvider)p, "\\n", "\\n", "\\n - Newline"));
/*     */ 
/*     */     
/*  85 */     p.addCompletion((Completion)new RegexCompletion((CompletionProvider)p, "[", "[", "[abc] - Any of a, b, or c"));
/*  86 */     p.addCompletion((Completion)new RegexCompletion((CompletionProvider)p, "[^", "[^", "[^abc] - Any character except a, b, or c"));
/*     */ 
/*     */     
/*  89 */     p.addCompletion((Completion)new RegexCompletion((CompletionProvider)p, ".", ".", ". - Any character"));
/*  90 */     p.addCompletion((Completion)new RegexCompletion((CompletionProvider)p, "\\d", "\\d", "\\d - A digit"));
/*  91 */     p.addCompletion((Completion)new RegexCompletion((CompletionProvider)p, "\\D", "\\D", "\\D - Not a digit"));
/*  92 */     p.addCompletion((Completion)new RegexCompletion((CompletionProvider)p, "\\s", "\\s", "\\s - A whitespace"));
/*  93 */     p.addCompletion((Completion)new RegexCompletion((CompletionProvider)p, "\\S", "\\S", "\\S - Not a whitespace"));
/*  94 */     p.addCompletion((Completion)new RegexCompletion((CompletionProvider)p, "\\w", "\\w", "\\w - An alphanumeric (word character)"));
/*  95 */     p.addCompletion((Completion)new RegexCompletion((CompletionProvider)p, "\\W", "\\W", "\\W - Not an alphanumeric"));
/*     */ 
/*     */     
/*  98 */     p.addCompletion((Completion)new RegexCompletion((CompletionProvider)p, "^", "^", "^ - Line Start"));
/*  99 */     p.addCompletion((Completion)new RegexCompletion((CompletionProvider)p, "$", "$", "$ - Line End"));
/* 100 */     p.addCompletion((Completion)new RegexCompletion((CompletionProvider)p, "\\b", "\b", "\\b - Word beginning or end"));
/* 101 */     p.addCompletion((Completion)new RegexCompletion((CompletionProvider)p, "\\B", "\\B", "\\B - Not a word beginning or end"));
/*     */ 
/*     */     
/* 104 */     p.addCompletion((Completion)new RegexCompletion((CompletionProvider)p, "?", "?", "X? - Greedy match, 0 or 1 times"));
/* 105 */     p.addCompletion((Completion)new RegexCompletion((CompletionProvider)p, "*", "*", "X* - Greedy match, 0 or more times"));
/* 106 */     p.addCompletion((Completion)new RegexCompletion((CompletionProvider)p, "+", "+", "X+ - Greedy match, 1 or more times"));
/* 107 */     p.addCompletion((Completion)new RegexCompletion((CompletionProvider)p, "{", "{", "X{n} - Greedy match, exactly n times"));
/* 108 */     p.addCompletion((Completion)new RegexCompletion((CompletionProvider)p, "{", "{", "X{n,} - Greedy match, at least n times"));
/* 109 */     p.addCompletion((Completion)new RegexCompletion((CompletionProvider)p, "{", "{", "X{n,m} - Greedy match, at least n but no more than m times"));
/* 110 */     p.addCompletion((Completion)new RegexCompletion((CompletionProvider)p, "??", "??", "X?? - Lazy match, 0 or 1 times"));
/* 111 */     p.addCompletion((Completion)new RegexCompletion((CompletionProvider)p, "*?", "*?", "X*? - Lazy match, 0 or more times"));
/* 112 */     p.addCompletion((Completion)new RegexCompletion((CompletionProvider)p, "+?", "+?", "X+? - Lazy match, 1 or more times"));
/* 113 */     p.addCompletion((Completion)new RegexCompletion((CompletionProvider)p, "?+", "?+", "X?+ - Possessive match, 0 or 1 times"));
/* 114 */     p.addCompletion((Completion)new RegexCompletion((CompletionProvider)p, "*+", "*+", "X*+ - Possessive match, 0 or more times"));
/* 115 */     p.addCompletion((Completion)new RegexCompletion((CompletionProvider)p, "++", "++", "X++ - Possessive match, 0 or more times"));
/*     */ 
/*     */     
/* 118 */     p.addCompletion((Completion)new RegexCompletion((CompletionProvider)p, "\\i", "\\i", "\\i - Match of the capturing group i"));
/*     */ 
/*     */     
/* 121 */     p.addCompletion((Completion)new RegexCompletion((CompletionProvider)p, "(", "(", "(Expr) - Mark Expr as capturing group"));
/* 122 */     p.addCompletion((Completion)new RegexCompletion((CompletionProvider)p, "(?:", "(?:", "(?:Expr) - Non-capturing group"));
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
/*     */   private void addReplaceFieldCompletions(RegexAwareProvider p) {
/* 134 */     p.addCompletion((Completion)new RegexCompletion((CompletionProvider)p, "$", "$", "$i - Match of the capturing group i"));
/* 135 */     p.addCompletion((Completion)new RegexCompletion((CompletionProvider)p, "\\", "\\", "\\ - Quote next character"));
/* 136 */     p.addCompletion((Completion)new RegexCompletion((CompletionProvider)p, "\\t", "\\t", "\\t - Tab"));
/* 137 */     p.addCompletion((Completion)new RegexCompletion((CompletionProvider)p, "\\n", "\\n", "\\n - Newline"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private AutoCompletion getAutoCompletion() {
/* 147 */     if (this.ac == null) {
/* 148 */       this.ac = new AutoCompletion(getCompletionProvider());
/*     */     }
/* 150 */     return this.ac;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized CompletionProvider getCompletionProvider() {
/* 160 */     if (this.provider == null) {
/* 161 */       this.provider = new RegexAwareProvider();
/* 162 */       if (this.replace) {
/* 163 */         addReplaceFieldCompletions(this.provider);
/*     */       } else {
/*     */         
/* 166 */         addFindFieldCompletions(this.provider);
/*     */       } 
/*     */     } 
/* 169 */     return (CompletionProvider)this.provider;
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
/*     */   public Image getContentAssistImage() {
/* 181 */     if (this.contentAssistImage != null) {
/* 182 */       return this.contentAssistImage;
/*     */     }
/* 184 */     return AbstractSearchDialog.getContentAssistImage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hideAutoCompletePopups() {
/* 194 */     return (this.ac != null && this.ac.hideChildWindows());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAutoCompleteEnabled() {
/* 205 */     return this.enabled;
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
/*     */   public void setAutoCompleteEnabled(boolean enabled) {
/* 218 */     if (this.enabled != enabled) {
/* 219 */       this.enabled = enabled;
/* 220 */       if (enabled) {
/* 221 */         AutoCompletion ac = getAutoCompletion();
/*     */         
/* 223 */         JTextComponent tc = (JTextComponent)getEditor().getEditorComponent();
/* 224 */         ac.install(tc);
/*     */       } else {
/*     */         
/* 227 */         this.ac.uninstall();
/*     */       } 
/* 229 */       String prop = "AssistanceImage";
/*     */ 
/*     */ 
/*     */       
/* 233 */       if (enabled) {
/* 234 */         firePropertyChange(prop, null, getContentAssistImage());
/*     */       } else {
/*     */         
/* 237 */         firePropertyChange(prop, null, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContentAssistImage(Image image) {
/* 253 */     this.contentAssistImage = image;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class RegexAwareProvider
/*     */     extends DefaultCompletionProvider
/*     */   {
/*     */     private RegexAwareProvider() {}
/*     */ 
/*     */     
/*     */     protected boolean isValidChar(char ch) {
/* 264 */       switch (ch) {
/*     */         case '$':
/*     */         case '(':
/*     */         case '*':
/*     */         case '+':
/*     */         case '.':
/*     */         case ':':
/*     */         case '?':
/*     */         case '[':
/*     */         case '\\':
/*     */         case '^':
/*     */         case '{':
/* 276 */           return true;
/*     */       } 
/* 278 */       return false;
/*     */     }
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
/*     */   private static class RegexCompletion
/*     */     extends BasicCompletion
/*     */   {
/*     */     private String inputText;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     RegexCompletion(CompletionProvider provider, String inputText, String replacementText, String shortDesc) {
/* 303 */       super(provider, replacementText, shortDesc);
/* 304 */       this.inputText = inputText;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getInputText() {
/* 309 */       return this.inputText;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 314 */       return getShortDescription();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rst\\ui\search\RegexAwareComboBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */