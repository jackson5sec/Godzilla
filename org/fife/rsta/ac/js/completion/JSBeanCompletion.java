/*     */ package org.fife.rsta.ac.js.completion;
/*     */ 
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import org.fife.rsta.ac.java.JarManager;
/*     */ import org.fife.rsta.ac.java.Util;
/*     */ import org.fife.rsta.ac.java.classreader.MethodInfo;
/*     */ import org.fife.rsta.ac.java.rjc.ast.Method;
/*     */ import org.fife.rsta.ac.js.IconFactory;
/*     */ import org.fife.rsta.ac.js.JavaScriptHelper;
/*     */ import org.fife.rsta.ac.js.SourceCompletionProvider;
/*     */ import org.fife.ui.autocomplete.Completion;
/*     */ import org.fife.ui.autocomplete.CompletionProvider;
/*     */ import org.fife.ui.autocomplete.VariableCompletion;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JSBeanCompletion
/*     */   extends VariableCompletion
/*     */   implements JSCompletion
/*     */ {
/*     */   private JSMethodData methodData;
/*     */   private Method method;
/*     */   
/*     */   public JSBeanCompletion(CompletionProvider provider, MethodInfo methodInfo, JarManager jarManager) {
/*  26 */     super(provider, convertNameToBean(methodInfo.getName()), null);
/*  27 */     setRelevance(5);
/*  28 */     this.methodData = new JSMethodData(methodInfo, jarManager);
/*  29 */     this.method = this.methodData.getMethod();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  35 */     return (obj instanceof JSBeanCompletion && ((JSBeanCompletion)obj)
/*  36 */       .getName().equals(getName()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Icon getIcon() {
/*  42 */     return IconFactory.getIcon("global_variable");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAlreadyEntered(JTextComponent comp) {
/*  48 */     String temp = getProvider().getAlreadyEnteredText(comp);
/*     */     
/*  50 */     int lastDot = JavaScriptHelper.findLastIndexOfJavaScriptIdentifier(temp);
/*  51 */     if (lastDot > -1) {
/*  52 */       temp = temp.substring(lastDot + 1);
/*     */     }
/*  54 */     return temp;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getType() {
/*  59 */     String value = getType(true);
/*  60 */     return ((SourceCompletionProvider)getProvider()).getTypesFactory().convertJavaScriptType(value, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType(boolean qualified) {
/*  66 */     return ((SourceCompletionProvider)getProvider()).getTypesFactory().convertJavaScriptType(this.methodData
/*  67 */         .getType(qualified), qualified);
/*     */   }
/*     */ 
/*     */   
/*     */   private String getMethodSummary() {
/*  72 */     String docComment = (this.method != null) ? this.method.getDocComment() : getName();
/*  73 */     return (docComment != null) ? docComment : ((this.method != null) ? this.method
/*  74 */       .toString() : null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSummary() {
/*  80 */     String summary = getMethodSummary();
/*     */ 
/*     */     
/*  83 */     if (summary != null && summary.startsWith("/**")) {
/*  84 */       summary = Util.docCommentToHtml(summary);
/*     */     }
/*     */     
/*  87 */     return summary;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLookupName() {
/*  93 */     return getName();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEnclosingClassName(boolean fullyQualified) {
/*  99 */     return this.methodData.getEnclosingClassName(fullyQualified);
/*     */   }
/*     */ 
/*     */   
/*     */   public JSMethodData getMethodData() {
/* 104 */     return this.methodData;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String convertNameToBean(String name) {
/* 109 */     boolean memberIsGetMethod = name.startsWith("get");
/* 110 */     boolean memberIsSetMethod = name.startsWith("set");
/* 111 */     boolean memberIsIsMethod = name.startsWith("is");
/* 112 */     if (memberIsGetMethod || memberIsIsMethod || memberIsSetMethod) {
/*     */       
/* 114 */       String nameComponent = name.substring(memberIsIsMethod ? 2 : 3);
/* 115 */       if (nameComponent.length() == 0) {
/* 116 */         return name;
/*     */       }
/*     */       
/* 119 */       String beanPropertyName = nameComponent;
/* 120 */       char ch0 = nameComponent.charAt(0);
/* 121 */       if (Character.isUpperCase(ch0)) {
/* 122 */         if (nameComponent.length() == 1) {
/* 123 */           beanPropertyName = nameComponent.toLowerCase();
/*     */         } else {
/*     */           
/* 126 */           char ch1 = nameComponent.charAt(1);
/* 127 */           if (!Character.isUpperCase(ch1))
/*     */           {
/* 129 */             beanPropertyName = Character.toLowerCase(ch0) + nameComponent.substring(1);
/*     */           }
/*     */         } 
/*     */       }
/* 133 */       name = beanPropertyName;
/*     */     } 
/* 135 */     return name;
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
/*     */   public int hashCode() {
/* 147 */     return getName().hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(Completion o) {
/* 155 */     if (o == this) {
/* 156 */       return 0;
/*     */     }
/* 158 */     if (o instanceof JSBeanCompletion) {
/* 159 */       JSBeanCompletion c2 = (JSBeanCompletion)o;
/* 160 */       return getLookupName().compareTo(c2.getLookupName());
/*     */     } 
/* 162 */     return super.compareTo(o);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\completion\JSBeanCompletion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */