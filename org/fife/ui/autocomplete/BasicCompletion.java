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
/*     */ public class BasicCompletion
/*     */   extends AbstractCompletion
/*     */ {
/*     */   private String replacementText;
/*     */   private String shortDesc;
/*     */   private String summary;
/*     */   
/*     */   public BasicCompletion(CompletionProvider provider, String replacementText) {
/*  38 */     this(provider, replacementText, null);
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
/*     */   public BasicCompletion(CompletionProvider provider, String replacementText, String shortDesc) {
/*  52 */     this(provider, replacementText, shortDesc, null);
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
/*     */   public BasicCompletion(CompletionProvider provider, String replacementText, String shortDesc, String summary) {
/*  68 */     super(provider);
/*  69 */     this.replacementText = replacementText;
/*  70 */     this.shortDesc = shortDesc;
/*  71 */     this.summary = summary;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getReplacementText() {
/*  80 */     return this.replacementText;
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
/*     */   public String getShortDescription() {
/*  92 */     return this.shortDesc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSummary() {
/* 101 */     return this.summary;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setShortDescription(String shortDesc) {
/* 112 */     this.shortDesc = shortDesc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSummary(String summary) {
/* 123 */     this.summary = summary;
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
/*     */   public String toString() {
/* 139 */     if (this.shortDesc == null) {
/* 140 */       return getInputText();
/*     */     }
/* 142 */     return getInputText() + " - " + this.shortDesc;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\autocomplete\BasicCompletion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */