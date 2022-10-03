/*     */ package org.fife.ui.autocomplete;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public class MarkupTagCompletion
/*     */   extends AbstractCompletion
/*     */ {
/*     */   private String name;
/*     */   private String desc;
/*     */   private String definedIn;
/*     */   private List<ParameterizedCompletion.Parameter> attrs;
/*     */   
/*     */   public MarkupTagCompletion(CompletionProvider provider, String name) {
/*  43 */     super(provider);
/*  44 */     this.name = name;
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
/*     */   protected void addAttributes(StringBuilder sb) {
/*  57 */     int attrCount = getAttributeCount();
/*  58 */     if (attrCount > 0) {
/*  59 */       sb.append("<b>Attributes:</b><br>");
/*  60 */       sb.append("<center><table width='90%'><tr><td>");
/*  61 */       for (int i = 0; i < attrCount; i++) {
/*  62 */         ParameterizedCompletion.Parameter attr = getAttribute(i);
/*  63 */         sb.append("&nbsp;&nbsp;&nbsp;<b>");
/*  64 */         sb.append((attr.getName() != null) ? attr.getName() : attr
/*  65 */             .getType());
/*  66 */         sb.append("</b>&nbsp;");
/*  67 */         String desc = attr.getDescription();
/*  68 */         if (desc != null) {
/*  69 */           sb.append(desc);
/*     */         }
/*  71 */         sb.append("<br>");
/*     */       } 
/*  73 */       sb.append("</td></tr></table></center><br><br>");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addDefinitionString(StringBuilder sb) {
/*  80 */     sb.append("<html><b>").append(this.name).append("</b>");
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
/*     */   public List<ParameterizedCompletion.Parameter> getAttributes() {
/*  92 */     return this.attrs;
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
/*     */   public ParameterizedCompletion.Parameter getAttribute(int index) {
/* 104 */     return this.attrs.get(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAttributeCount() {
/* 115 */     return (this.attrs == null) ? 0 : this.attrs.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefinedIn() {
/* 126 */     return this.definedIn;
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
/*     */   public String getDescription() {
/* 139 */     return this.desc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 149 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getReplacementText() {
/* 158 */     return getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSummary() {
/* 167 */     StringBuilder sb = new StringBuilder();
/* 168 */     addDefinitionString(sb);
/* 169 */     possiblyAddDescription(sb);
/* 170 */     addAttributes(sb);
/* 171 */     possiblyAddDefinedIn(sb);
/* 172 */     return sb.toString();
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
/* 183 */     if (this.definedIn != null) {
/* 184 */       sb.append("<hr>Defined in:");
/* 185 */       sb.append(" <em>").append(this.definedIn).append("</em>");
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
/*     */   protected void possiblyAddDescription(StringBuilder sb) {
/* 197 */     if (this.desc != null) {
/* 198 */       sb.append("<hr><br>");
/* 199 */       sb.append(this.desc);
/* 200 */       sb.append("<br><br><br>");
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
/*     */   public void setDefinedIn(String definedIn) {
/* 212 */     this.definedIn = definedIn;
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
/*     */   public void setDescription(String desc) {
/* 225 */     this.desc = desc;
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
/*     */   public void setAttributes(List<? extends ParameterizedCompletion.Parameter> attrs) {
/* 238 */     this.attrs = new ArrayList<>(attrs);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\autocomplete\MarkupTagCompletion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */