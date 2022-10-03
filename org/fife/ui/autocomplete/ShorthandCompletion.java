/*     */ package org.fife.ui.autocomplete;
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
/*     */ public class ShorthandCompletion
/*     */   extends BasicCompletion
/*     */ {
/*     */   private String inputText;
/*     */   
/*     */   public ShorthandCompletion(CompletionProvider provider, String inputText, String replacementText) {
/*  39 */     super(provider, replacementText);
/*  40 */     this.inputText = inputText;
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
/*     */   public ShorthandCompletion(CompletionProvider provider, String inputText, String replacementText, String shortDesc) {
/*  55 */     super(provider, replacementText, shortDesc);
/*  56 */     this.inputText = inputText;
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
/*     */   public ShorthandCompletion(CompletionProvider provider, String inputText, String replacementText, String shortDesc, String summary) {
/*  73 */     super(provider, replacementText, shortDesc, summary);
/*  74 */     this.inputText = inputText;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getInputText() {
/*  85 */     return this.inputText;
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
/*     */   public String getSummary() {
/*  99 */     String summary = super.getSummary();
/* 100 */     return (summary != null) ? summary : ("<html><body>" + getSummaryBody());
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
/*     */   protected String getSummaryBody() {
/* 113 */     return "<code>" + getReplacementText();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\autocomplete\ShorthandCompletion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */