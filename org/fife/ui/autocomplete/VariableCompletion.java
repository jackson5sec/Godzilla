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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VariableCompletion
/*     */   extends BasicCompletion
/*     */ {
/*     */   private String type;
/*     */   private String definedIn;
/*     */   
/*     */   public VariableCompletion(CompletionProvider provider, String name, String type) {
/*  49 */     super(provider, name);
/*  50 */     this.type = type;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addDefinitionString(StringBuilder sb) {
/*  55 */     sb.append("<html><b>").append(getDefinitionString()).append("</b>");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefinitionString() {
/*  61 */     StringBuilder sb = new StringBuilder();
/*     */ 
/*     */     
/*  64 */     if (this.type != null) {
/*  65 */       sb.append(this.type).append(' ');
/*     */     }
/*     */ 
/*     */     
/*  69 */     sb.append(getName());
/*     */     
/*  71 */     return sb.toString();
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
/*     */   public String getDefinedIn() {
/*  83 */     return this.definedIn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  93 */     return getReplacementText();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSummary() {
/* 102 */     StringBuilder sb = new StringBuilder();
/* 103 */     addDefinitionString(sb);
/* 104 */     possiblyAddDescription(sb);
/* 105 */     possiblyAddDefinedIn(sb);
/* 106 */     return sb.toString();
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getToolTipText() {
/* 128 */     return getDefinitionString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType() {
/* 138 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void possiblyAddDefinedIn(StringBuilder sb) {
/* 149 */     if (this.definedIn != null) {
/* 150 */       sb.append("<hr>Defined in:");
/* 151 */       sb.append(" <em>").append(this.definedIn).append("</em>");
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
/*     */   protected boolean possiblyAddDescription(StringBuilder sb) {
/* 164 */     if (getShortDescription() != null) {
/* 165 */       sb.append("<hr><br>");
/* 166 */       sb.append(getShortDescription());
/* 167 */       sb.append("<br><br><br>");
/* 168 */       return true;
/*     */     } 
/* 170 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefinedIn(String definedIn) {
/* 181 */     this.definedIn = definedIn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 192 */     return getName();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\autocomplete\VariableCompletion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */