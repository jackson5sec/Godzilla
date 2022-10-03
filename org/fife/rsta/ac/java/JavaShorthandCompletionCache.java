/*    */ package org.fife.rsta.ac.java;
/*    */ 
/*    */ import java.util.ResourceBundle;
/*    */ import org.fife.rsta.ac.ShorthandCompletionCache;
/*    */ import org.fife.ui.autocomplete.AbstractCompletionProvider;
/*    */ import org.fife.ui.autocomplete.BasicCompletion;
/*    */ import org.fife.ui.autocomplete.Completion;
/*    */ import org.fife.ui.autocomplete.CompletionProvider;
/*    */ import org.fife.ui.autocomplete.DefaultCompletionProvider;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JavaShorthandCompletionCache
/*    */   extends ShorthandCompletionCache
/*    */ {
/*    */   private static final String MSG = "org.fife.rsta.ac.java.resources";
/* 30 */   private static final ResourceBundle msg = ResourceBundle.getBundle("org.fife.rsta.ac.java.resources");
/*    */ 
/*    */ 
/*    */   
/*    */   public JavaShorthandCompletionCache(DefaultCompletionProvider templateProvider, DefaultCompletionProvider commentsProvider) {
/* 35 */     super((AbstractCompletionProvider)templateProvider, (AbstractCompletionProvider)commentsProvider);
/*    */ 
/*    */ 
/*    */     
/* 39 */     String template = "System.out.println(${});${cursor}";
/* 40 */     addShorthandCompletion(new JavaTemplateCompletion((CompletionProvider)templateProvider, "sysout", "sysout", template, msg
/* 41 */           .getString("sysout.shortDesc"), msg.getString("sysout.summary")));
/*    */     
/* 43 */     template = "System.err.println(${});${cursor}";
/* 44 */     addShorthandCompletion(new JavaTemplateCompletion((CompletionProvider)templateProvider, "syserr", "syserr", template, msg
/* 45 */           .getString("syserr.shortDesc"), msg.getString("syserr.summary")));
/*    */     
/* 47 */     template = "for (int ${i} = 0; ${i} < ${array}.length; ${i}++) {\n\t${cursor}\n}";
/*    */     
/* 49 */     addShorthandCompletion(new JavaTemplateCompletion((CompletionProvider)templateProvider, "for", "for-loop-array", template, msg
/* 50 */           .getString("for.array.shortDesc"), msg.getString("for.array.summary")));
/*    */     
/* 52 */     template = "for (int ${i} = 0; ${i} < ${10}; ${i}++) {\n\t${cursor}\n}";
/* 53 */     addShorthandCompletion(new JavaTemplateCompletion((CompletionProvider)templateProvider, "for", "for-loop", template, msg
/* 54 */           .getString("for.loop.shortDesc"), msg.getString("for.loop.summary")));
/*    */     
/* 56 */     template = "if (${condition}) {\n\t${cursor}\n}";
/* 57 */     addShorthandCompletion(new JavaTemplateCompletion((CompletionProvider)templateProvider, "if", "if-cond", template, msg
/* 58 */           .getString("if.cond.shortDesc"), msg.getString("if.cond.summary")));
/*    */     
/* 60 */     template = "if (${condition}) {\n\t${cursor}\n}\nelse {\n\t\n}";
/* 61 */     addShorthandCompletion(new JavaTemplateCompletion((CompletionProvider)templateProvider, "if", "if-else", template, msg
/* 62 */           .getString("if.else.shortDesc"), msg.getString("if.else.summary")));
/*    */     
/* 64 */     template = "do {\n\t${cursor}\n} while (${condition});";
/* 65 */     addShorthandCompletion(new JavaTemplateCompletion((CompletionProvider)templateProvider, "do", "do-loop", template, msg
/* 66 */           .getString("do.shortDesc"), msg.getString("do.summary")));
/*    */     
/* 68 */     template = "while (${condition}) {\n\t${cursor}\n}";
/* 69 */     addShorthandCompletion(new JavaTemplateCompletion((CompletionProvider)templateProvider, "while", "while-cond", template, msg
/* 70 */           .getString("while.shortDesc"), msg.getString("while.summary")));
/*    */     
/* 72 */     template = "new Runnable() {\n\tpublic void run() {\n\t\t${cursor}\n\t}\n}";
/* 73 */     addShorthandCompletion(new JavaTemplateCompletion((CompletionProvider)templateProvider, "runnable", "runnable", template, msg
/* 74 */           .getString("runnable.shortDesc")));
/*    */     
/* 76 */     template = "switch (${key}) {\n\tcase ${value}:\n\t\t${cursor}\n\t\tbreak;\n\tdefault:\n\t\tbreak;\n}";
/* 77 */     addShorthandCompletion(new JavaTemplateCompletion((CompletionProvider)templateProvider, "switch", "switch-statement", template, msg
/* 78 */           .getString("switch.case.shortDesc"), msg.getString("switch.case.summary")));
/*    */     
/* 80 */     template = "try {\n\t ${cursor} \n} catch (${err}) {\n\t\n}";
/* 81 */     addShorthandCompletion(new JavaTemplateCompletion((CompletionProvider)templateProvider, "try", "try-catch", template, msg
/* 82 */           .getString("try.catch.shortDesc"), msg.getString("try.catch.summary")));
/*    */     
/* 84 */     template = "catch (${err}) {\n\t${cursor}\n}";
/* 85 */     addShorthandCompletion(new JavaTemplateCompletion((CompletionProvider)templateProvider, "catch", "catch-block", template, msg
/* 86 */           .getString("catch.block.shortDesc"), msg.getString("catch.block.summary")));
/*    */ 
/*    */     
/* 89 */     addCommentCompletion((Completion)new BasicCompletion((CompletionProvider)commentsProvider, "TODO:", null, msg.getString("todo")));
/* 90 */     addCommentCompletion((Completion)new BasicCompletion((CompletionProvider)commentsProvider, "FIXME:", null, msg.getString("fixme")));
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\JavaShorthandCompletionCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */