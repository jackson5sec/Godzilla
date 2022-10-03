/*     */ package org.fife.rsta.ac.sh;
/*     */ 
/*     */ import org.fife.rsta.ac.c.CCompletionProvider;
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
/*     */ public class ShellCompletionProvider
/*     */   extends CCompletionProvider
/*     */ {
/*     */   private static boolean useLocalManPages;
/*     */   
/*     */   protected void addShorthandCompletions(DefaultCompletionProvider codeCP) {}
/*     */   
/*     */   protected CompletionProvider createStringCompletionProvider() {
/*  53 */     DefaultCompletionProvider cp = new DefaultCompletionProvider();
/*  54 */     cp.setAutoActivationRules(true, null);
/*  55 */     return (CompletionProvider)cp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char getParameterListEnd() {
/*  64 */     return Character.MIN_VALUE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char getParameterListStart() {
/*  73 */     return Character.MIN_VALUE;
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
/*     */   public static boolean getUseLocalManPages() {
/*  87 */     return useLocalManPages;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getXmlResource() {
/*  96 */     return "data/sh.xml";
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
/*     */   public static void setUseLocalManPages(boolean use) {
/* 110 */     useLocalManPages = use;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\sh\ShellCompletionProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */