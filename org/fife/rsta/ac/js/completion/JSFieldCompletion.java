/*     */ package org.fife.rsta.ac.js.completion;
/*     */ 
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import org.fife.rsta.ac.java.Util;
/*     */ import org.fife.rsta.ac.java.classreader.FieldInfo;
/*     */ import org.fife.rsta.ac.java.rjc.ast.Field;
/*     */ import org.fife.rsta.ac.js.IconFactory;
/*     */ import org.fife.rsta.ac.js.JavaScriptHelper;
/*     */ import org.fife.rsta.ac.js.SourceCompletionProvider;
/*     */ import org.fife.ui.autocomplete.Completion;
/*     */ import org.fife.ui.autocomplete.CompletionProvider;
/*     */ import org.fife.ui.autocomplete.VariableCompletion;
/*     */ 
/*     */ 
/*     */ public class JSFieldCompletion
/*     */   extends VariableCompletion
/*     */   implements JSCompletion
/*     */ {
/*     */   private JSFieldData fieldData;
/*     */   private Field field;
/*     */   
/*     */   public JSFieldCompletion(CompletionProvider provider, FieldInfo fieldInfo) {
/*  24 */     super(provider, fieldInfo.getName(), null);
/*  25 */     this.fieldData = new JSFieldData(fieldInfo, ((SourceCompletionProvider)provider).getJarManager());
/*  26 */     this.field = this.fieldData.getField();
/*  27 */     setRelevance(this.fieldData);
/*     */   }
/*     */ 
/*     */   
/*     */   private void setRelevance(JSFieldData data) {
/*  32 */     if (data.isStatic()) {
/*  33 */       setRelevance(6);
/*     */     } else {
/*  35 */       setRelevance(8);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSummary() {
/*  42 */     String summary = (this.field != null) ? this.field.getDocComment() : getName();
/*     */ 
/*     */     
/*  45 */     if (summary != null && summary.startsWith("/**")) {
/*  46 */       summary = Util.docCommentToHtml(summary);
/*     */     }
/*     */     
/*  49 */     return summary;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Icon getIcon() {
/*  56 */     return this.fieldData.isStatic() ? IconFactory.getIcon("static_var") : (
/*  57 */       this.fieldData.isPublic() ? IconFactory.getIcon("global_variable") : IconFactory.getIcon("default_variable"));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEnclosingClassName(boolean fullyQualified) {
/*  63 */     return this.fieldData.getEnclosingClassName(fullyQualified);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAlreadyEntered(JTextComponent comp) {
/*  69 */     String temp = getProvider().getAlreadyEnteredText(comp);
/*     */     
/*  71 */     int lastDot = JavaScriptHelper.findLastIndexOfJavaScriptIdentifier(temp);
/*  72 */     if (lastDot > -1) {
/*  73 */       temp = temp.substring(lastDot + 1);
/*     */     }
/*  75 */     return temp;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLookupName() {
/*  81 */     return getReplacementText();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType() {
/*  87 */     return ((SourceCompletionProvider)getProvider()).getTypesFactory().convertJavaScriptType(this.fieldData.getType(true), false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType(boolean qualified) {
/*  94 */     return this.fieldData.getType(true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 100 */     if (obj == this) {
/* 101 */       return true;
/*     */     }
/* 103 */     if (obj instanceof JSFieldCompletion) {
/*     */       
/* 105 */       JSFieldCompletion jsComp = (JSFieldCompletion)obj;
/* 106 */       return getLookupName().equals(jsComp.getLookupName());
/*     */     } 
/* 108 */     return super.equals(obj);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(Completion o) {
/* 116 */     if (o == this) {
/* 117 */       return 0;
/*     */     }
/* 119 */     if (o instanceof JSFieldCompletion) {
/* 120 */       JSFieldCompletion c2 = (JSFieldCompletion)o;
/* 121 */       return getLookupName().compareTo(c2.getLookupName());
/*     */     } 
/* 123 */     return super.compareTo(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 128 */     return getLookupName().hashCode();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\completion\JSFieldCompletion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */