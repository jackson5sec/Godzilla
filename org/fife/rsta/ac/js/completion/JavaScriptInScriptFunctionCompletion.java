/*     */ package org.fife.rsta.ac.js.completion;
/*     */ 
/*     */ import javax.swing.Icon;
/*     */ import org.fife.rsta.ac.java.Util;
/*     */ import org.fife.rsta.ac.js.IconFactory;
/*     */ import org.fife.rsta.ac.js.SourceCompletionProvider;
/*     */ import org.fife.rsta.ac.js.ast.type.TypeDeclaration;
/*     */ import org.fife.ui.autocomplete.Completion;
/*     */ import org.fife.ui.autocomplete.CompletionProvider;
/*     */ import org.fife.ui.autocomplete.FunctionCompletion;
/*     */ 
/*     */ 
/*     */ public class JavaScriptInScriptFunctionCompletion
/*     */   extends FunctionCompletion
/*     */   implements JSCompletion
/*     */ {
/*     */   private TypeDeclaration returnType;
/*     */   
/*     */   public JavaScriptInScriptFunctionCompletion(CompletionProvider provider, String name, TypeDeclaration returnType) {
/*  20 */     super(provider, name, null);
/*  21 */     setRelevance(4);
/*  22 */     this.returnType = returnType;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSummary() {
/*  28 */     String summary = getShortDescription();
/*     */ 
/*     */ 
/*     */     
/*  32 */     if (summary != null && summary.startsWith("/**")) {
/*  33 */       summary = Util.docCommentToHtml(summary);
/*     */     }
/*     */     
/*  36 */     return summary;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Icon getIcon() {
/*  42 */     return IconFactory.getIcon("default_function");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLookupName() {
/*  48 */     StringBuilder sb = new StringBuilder(getName());
/*  49 */     sb.append('(');
/*  50 */     int count = getParamCount();
/*  51 */     for (int i = 0; i < count; i++) {
/*  52 */       sb.append("p");
/*  53 */       if (i < count - 1) {
/*  54 */         sb.append(",");
/*     */       }
/*     */     } 
/*  57 */     sb.append(')');
/*  58 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType() {
/*  64 */     String value = getType(true);
/*  65 */     return ((SourceCompletionProvider)getProvider()).getTypesFactory().convertJavaScriptType(value, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType(boolean qualified) {
/*  71 */     String type = (this.returnType != null) ? this.returnType.getQualifiedName() : null;
/*  72 */     return ((SourceCompletionProvider)getProvider()).getTypesFactory().convertJavaScriptType(type, qualified);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEnclosingClassName(boolean fullyQualified) {
/*  79 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  85 */     if (obj == this) {
/*  86 */       return true;
/*     */     }
/*  88 */     if (obj instanceof JSCompletion) {
/*     */       
/*  90 */       JSCompletion jsComp = (JSCompletion)obj;
/*  91 */       return getLookupName().equals(jsComp.getLookupName());
/*     */     } 
/*  93 */     return super.equals(obj);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  99 */     return getLookupName().hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 104 */     return getLookupName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(Completion other) {
/* 112 */     if (other == this) {
/* 113 */       return 0;
/*     */     }
/* 115 */     if (other instanceof JSCompletion) {
/* 116 */       JSCompletion c2 = (JSCompletion)other;
/* 117 */       return getLookupName().compareTo(c2.getLookupName());
/*     */     } 
/* 119 */     if (other != null) {
/* 120 */       return toString().compareTo(other.toString());
/*     */     }
/* 122 */     return -1;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\completion\JavaScriptInScriptFunctionCompletion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */