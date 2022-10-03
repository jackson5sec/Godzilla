/*     */ package org.fife.rsta.ac.js;
/*     */ 
/*     */ import java.util.ResourceBundle;
/*     */ import org.fife.rsta.ac.ShorthandCompletionCache;
/*     */ import org.fife.rsta.ac.js.completion.JavaScriptTemplateCompletion;
/*     */ import org.fife.rsta.ac.js.completion.JavascriptBasicCompletion;
/*     */ import org.fife.ui.autocomplete.AbstractCompletionProvider;
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
/*     */ public class JavaScriptShorthandCompletionCache
/*     */   extends ShorthandCompletionCache
/*     */ {
/*     */   private static final String MSG = "org.fife.rsta.ac.js.resources";
/*  31 */   private static final ResourceBundle msg = ResourceBundle.getBundle("org.fife.rsta.ac.js.resources");
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaScriptShorthandCompletionCache(DefaultCompletionProvider templateProvider, DefaultCompletionProvider commentsProvider, boolean e4xSuppport) {
/*  36 */     super((AbstractCompletionProvider)templateProvider, (AbstractCompletionProvider)commentsProvider);
/*     */ 
/*     */     
/*  39 */     addShorthandCompletion((Completion)new JavascriptBasicCompletion((CompletionProvider)templateProvider, "do"));
/*  40 */     addShorthandCompletion((Completion)new JavascriptBasicCompletion((CompletionProvider)templateProvider, "if"));
/*  41 */     addShorthandCompletion((Completion)new JavascriptBasicCompletion((CompletionProvider)templateProvider, "while"));
/*  42 */     addShorthandCompletion((Completion)new JavascriptBasicCompletion((CompletionProvider)templateProvider, "for"));
/*  43 */     addShorthandCompletion((Completion)new JavascriptBasicCompletion((CompletionProvider)templateProvider, "switch"));
/*  44 */     addShorthandCompletion((Completion)new JavascriptBasicCompletion((CompletionProvider)templateProvider, "try"));
/*  45 */     addShorthandCompletion((Completion)new JavascriptBasicCompletion((CompletionProvider)templateProvider, "catch"));
/*  46 */     addShorthandCompletion((Completion)new JavascriptBasicCompletion((CompletionProvider)templateProvider, "case"));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  51 */     String template = "for (var ${i} = 0; ${i} < ${array}.length; ${i}++) {\n\t${cursor}\n}";
/*  52 */     addShorthandCompletion((Completion)new JavaScriptTemplateCompletion((CompletionProvider)templateProvider, "for", "for-loop-array", template, msg
/*  53 */           .getString("for.array.shortDesc"), msg.getString("for.array.summary")));
/*     */ 
/*     */     
/*  56 */     template = "for (var ${i} = 0; ${i} < ${10}; ${i}++) {\n\t${cursor}\n}";
/*  57 */     addShorthandCompletion((Completion)new JavaScriptTemplateCompletion((CompletionProvider)templateProvider, "for", "for-loop", template, msg
/*  58 */           .getString("for.loop.shortDesc"), msg.getString("for.loop.summary")));
/*     */ 
/*     */     
/*  61 */     template = "for (var ${iterable_element} in ${iterable})\n{\n\t${cursor}\n}";
/*  62 */     addShorthandCompletion((Completion)new JavaScriptTemplateCompletion((CompletionProvider)templateProvider, "for", "for-loop-in", template, msg
/*  63 */           .getString("for.in.shortDesc"), msg.getString("for.in.summary")));
/*     */ 
/*     */     
/*  66 */     if (e4xSuppport) {
/*     */       
/*  68 */       template = "for each (var ${iterable_element} in ${iterable})\n{\n\t${cursor}\n}";
/*  69 */       addShorthandCompletion((Completion)new JavaScriptTemplateCompletion((CompletionProvider)templateProvider, "for", "for-loop-in-each", template, msg
/*  70 */             .getString("for.in.each.shortDesc"), msg.getString("for.in.each.summary")));
/*     */     } 
/*     */ 
/*     */     
/*  74 */     template = "do {\n\t${cursor}\n} while (${condition});";
/*  75 */     addShorthandCompletion((Completion)new JavaScriptTemplateCompletion((CompletionProvider)templateProvider, "do-while", "do-loop", template, msg
/*  76 */           .getString("do.shortDesc"), msg.getString("do.summary")));
/*     */ 
/*     */     
/*  79 */     template = "if (${condition}) {\n\t${cursor}\n}";
/*  80 */     addShorthandCompletion((Completion)new JavaScriptTemplateCompletion((CompletionProvider)templateProvider, "if", "if-cond", template, msg
/*  81 */           .getString("if.cond.shortDesc"), msg.getString("if.cond.summary")));
/*     */ 
/*     */     
/*  84 */     template = "if (${condition}) {\n\t${cursor}\n} else {\n\t\n}";
/*  85 */     addShorthandCompletion((Completion)new JavaScriptTemplateCompletion((CompletionProvider)templateProvider, "if", "if-else", template, msg
/*  86 */           .getString("if.else.shortDesc"), msg.getString("if.else.summary")));
/*     */ 
/*     */     
/*  89 */     template = "while (${condition}) {\n\t${cursor}\n}";
/*  90 */     addShorthandCompletion((Completion)new JavaScriptTemplateCompletion((CompletionProvider)templateProvider, "while", "while-cond", template, msg
/*  91 */           .getString("while.shortDesc"), msg.getString("while.summary")));
/*     */ 
/*     */     
/*  94 */     template = "switch (${key}) {\n\tcase ${value}:\n\t\t${cursor}\n\t\tbreak;\n\tdefault:\n\t\tbreak;\n}";
/*  95 */     addShorthandCompletion((Completion)new JavaScriptTemplateCompletion((CompletionProvider)templateProvider, "switch", "switch-statement", template, msg
/*  96 */           .getString("switch.case.shortDesc"), msg.getString("switch.case.summary")));
/*     */ 
/*     */     
/*  99 */     template = "try {\n\t ${cursor} \n} catch (${err}) {\n\t\n}";
/* 100 */     addShorthandCompletion((Completion)new JavaScriptTemplateCompletion((CompletionProvider)templateProvider, "try", "try-catch", template, msg
/* 101 */           .getString("try.catch.shortDesc"), msg.getString("try.catch.summary")));
/*     */ 
/*     */     
/* 104 */     template = "catch (${err}) {\n\t${cursor}\n}";
/* 105 */     addShorthandCompletion((Completion)new JavaScriptTemplateCompletion((CompletionProvider)templateProvider, "catch", "catch-block", template, msg
/* 106 */           .getString("catch.block.shortDesc"), msg.getString("catch.block.summary")));
/*     */ 
/*     */     
/* 109 */     addCommentCompletion((Completion)new BasicCompletion((CompletionProvider)commentsProvider, "TODO:", null, msg.getString("todo")));
/* 110 */     addCommentCompletion((Completion)new BasicCompletion((CompletionProvider)commentsProvider, "FIXME:", null, msg.getString("fixme")));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\JavaScriptShorthandCompletionCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */