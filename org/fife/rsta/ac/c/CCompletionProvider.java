/*     */ package org.fife.rsta.ac.c;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.fife.ui.autocomplete.BasicCompletion;
/*     */ import org.fife.ui.autocomplete.Completion;
/*     */ import org.fife.ui.autocomplete.CompletionProvider;
/*     */ import org.fife.ui.autocomplete.DefaultCompletionProvider;
/*     */ import org.fife.ui.autocomplete.LanguageAwareCompletionProvider;
/*     */ import org.fife.ui.autocomplete.ShorthandCompletion;
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
/*     */ public class CCompletionProvider
/*     */   extends LanguageAwareCompletionProvider
/*     */ {
/*     */   public CCompletionProvider() {
/*  39 */     setDefaultCompletionProvider(createCodeCompletionProvider());
/*  40 */     setStringCompletionProvider(createStringCompletionProvider());
/*  41 */     setCommentCompletionProvider(createCommentCompletionProvider());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addShorthandCompletions(DefaultCompletionProvider codeCP) {
/*  51 */     codeCP.addCompletion((Completion)new ShorthandCompletion((CompletionProvider)codeCP, "main", "int main(int argc, char **argv)"));
/*     */     
/*  53 */     codeCP.setAutoActivationRules(true, null);
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
/*     */   protected CompletionProvider createCodeCompletionProvider() {
/*  70 */     DefaultCompletionProvider cp = new DefaultCompletionProvider();
/*  71 */     loadCodeCompletionsFromXml(cp);
/*  72 */     addShorthandCompletions(cp);
/*  73 */     cp.setAutoActivationRules(true, null);
/*  74 */     return (CompletionProvider)cp;
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
/*     */   protected CompletionProvider createCommentCompletionProvider() {
/*  87 */     DefaultCompletionProvider cp = new DefaultCompletionProvider();
/*  88 */     cp.addCompletion((Completion)new BasicCompletion((CompletionProvider)cp, "TODO:", "A to-do reminder"));
/*  89 */     cp.addCompletion((Completion)new BasicCompletion((CompletionProvider)cp, "FIXME:", "A bug that needs to be fixed"));
/*  90 */     cp.setAutoActivationRules(true, null);
/*  91 */     return (CompletionProvider)cp;
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
/*     */   protected CompletionProvider createStringCompletionProvider() {
/* 103 */     DefaultCompletionProvider cp = new DefaultCompletionProvider();
/* 104 */     cp.addCompletion((Completion)new BasicCompletion((CompletionProvider)cp, "%c", "char", "Prints a character"));
/* 105 */     cp.addCompletion((Completion)new BasicCompletion((CompletionProvider)cp, "%i", "signed int", "Prints a signed integer"));
/* 106 */     cp.addCompletion((Completion)new BasicCompletion((CompletionProvider)cp, "%f", "float", "Prints a float"));
/* 107 */     cp.addCompletion((Completion)new BasicCompletion((CompletionProvider)cp, "%s", "string", "Prints a string"));
/* 108 */     cp.addCompletion((Completion)new BasicCompletion((CompletionProvider)cp, "%u", "unsigned int", "Prints an unsigned integer"));
/* 109 */     cp.addCompletion((Completion)new BasicCompletion((CompletionProvider)cp, "\\n", "Newline", "Prints a newline"));
/* 110 */     return (CompletionProvider)cp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getXmlResource() {
/* 120 */     return "data/c.xml";
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
/*     */   protected void loadCodeCompletionsFromXml(DefaultCompletionProvider cp) {
/* 134 */     ClassLoader cl = getClass().getClassLoader();
/* 135 */     String res = getXmlResource();
/* 136 */     if (res != null) {
/* 137 */       InputStream in = cl.getResourceAsStream(res);
/*     */       try {
/* 139 */         if (in != null) {
/* 140 */           cp.loadFromXML(in);
/* 141 */           in.close();
/*     */         } else {
/*     */           
/* 144 */           cp.loadFromXML(new File(res));
/*     */         } 
/* 146 */       } catch (IOException ioe) {
/* 147 */         ioe.printStackTrace();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\c\CCompletionProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */