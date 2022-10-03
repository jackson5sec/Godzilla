/*     */ package org.fife.rsta.ac.js.completion;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import org.fife.rsta.ac.java.Util;
/*     */ import org.fife.rsta.ac.java.classreader.MethodInfo;
/*     */ import org.fife.rsta.ac.java.rjc.ast.Method;
/*     */ import org.fife.rsta.ac.js.IconFactory;
/*     */ import org.fife.rsta.ac.js.JavaScriptHelper;
/*     */ import org.fife.rsta.ac.js.SourceCompletionProvider;
/*     */ import org.fife.ui.autocomplete.Completion;
/*     */ import org.fife.ui.autocomplete.CompletionProvider;
/*     */ import org.fife.ui.autocomplete.FunctionCompletion;
/*     */ import org.fife.ui.autocomplete.ParameterizedCompletion;
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
/*     */ public class JSFunctionCompletion
/*     */   extends FunctionCompletion
/*     */   implements JSCompletion
/*     */ {
/*     */   private JSMethodData methodData;
/*     */   private String compareString;
/*     */   private String nameAndParameters;
/*     */   
/*     */   public JSFunctionCompletion(CompletionProvider provider, MethodInfo method) {
/*  38 */     this(provider, method, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JSFunctionCompletion(CompletionProvider provider, MethodInfo methodInfo, boolean showParameterType) {
/*  44 */     super(provider, getMethodName(methodInfo, provider), null);
/*  45 */     this
/*  46 */       .methodData = new JSMethodData(methodInfo, ((SourceCompletionProvider)provider).getJarManager());
/*  47 */     List<ParameterizedCompletion.Parameter> params = populateParams(this.methodData, showParameterType);
/*  48 */     setParams(params);
/*     */   }
/*     */ 
/*     */   
/*     */   private static String getMethodName(MethodInfo info, CompletionProvider provider) {
/*  53 */     if (info.isConstructor()) {
/*  54 */       return ((SourceCompletionProvider)provider).getTypesFactory().convertJavaScriptType(info.getClassFile().getClassName(true), false);
/*     */     }
/*  56 */     return info.getName();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private List<ParameterizedCompletion.Parameter> populateParams(JSMethodData methodData, boolean showParameterType) {
/*  62 */     MethodInfo methodInfo = methodData.getMethodInfo();
/*  63 */     int count = methodInfo.getParameterCount();
/*  64 */     String[] paramTypes = methodInfo.getParameterTypes();
/*  65 */     List<ParameterizedCompletion.Parameter> params = new ArrayList<>(count);
/*  66 */     for (int i = 0; i < count; i++) {
/*  67 */       String name = methodData.getParameterName(i);
/*  68 */       String type = methodData.getParameterType(paramTypes, i, getProvider());
/*  69 */       params.add(new JSFunctionParam(type, name, showParameterType, getProvider()));
/*     */     } 
/*     */     
/*  72 */     return params;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(Completion other) {
/*  81 */     int rc = -1;
/*  82 */     if (other == this) {
/*  83 */       rc = 0;
/*     */     }
/*  85 */     else if (other instanceof JSCompletion) {
/*  86 */       JSCompletion c2 = (JSCompletion)other;
/*  87 */       rc = getLookupName().compareTo(c2.getLookupName());
/*     */     }
/*  89 */     else if (other != null) {
/*  90 */       rc = toString().compareTo(other.toString());
/*  91 */       if (rc == 0) {
/*  92 */         String clazz1 = getClass().getName();
/*  93 */         clazz1 = clazz1.substring(clazz1.lastIndexOf('.'));
/*  94 */         String clazz2 = other.getClass().getName();
/*  95 */         clazz2 = clazz2.substring(clazz2.lastIndexOf('.'));
/*  96 */         rc = clazz1.compareTo(clazz2);
/*     */       } 
/*     */     } 
/*     */     
/* 100 */     return rc;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 106 */     return (obj instanceof JSCompletion && ((JSCompletion)obj)
/* 107 */       .getLookupName().equals(
/* 108 */         getLookupName()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAlreadyEntered(JTextComponent comp) {
/* 114 */     String temp = getProvider().getAlreadyEnteredText(comp);
/*     */     
/* 116 */     int lastDot = JavaScriptHelper.findLastIndexOfJavaScriptIdentifier(temp);
/* 117 */     if (lastDot > -1) {
/* 118 */       temp = temp.substring(lastDot + 1);
/*     */     }
/* 120 */     return temp;
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
/*     */   private String getCompareString() {
/* 134 */     if (this.compareString == null)
/*     */     {
/* 136 */       this.compareString = getLookupName();
/*     */     }
/*     */     
/* 139 */     return this.compareString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLookupName() {
/* 146 */     SourceCompletionProvider provider = (SourceCompletionProvider)getProvider();
/* 147 */     return provider.getJavaScriptEngine().getJavaScriptResolver(provider).getLookupText(this.methodData, getName());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefinitionString() {
/* 153 */     return getSignature();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getMethodSummary() {
/* 162 */     Method method = this.methodData.getMethod();
/* 163 */     String summary = (method != null) ? method.getDocComment() : null;
/*     */     
/* 165 */     if (summary != null && summary.startsWith("/**")) {
/* 166 */       summary = Util.docCommentToHtml(summary);
/*     */     }
/*     */     
/* 169 */     return (summary != null) ? summary : getNameAndParameters();
/*     */   }
/*     */ 
/*     */   
/*     */   private String getNameAndParameters() {
/* 174 */     if (this.nameAndParameters == null) {
/* 175 */       this.nameAndParameters = formatMethodAtString(getName(), this.methodData);
/*     */     }
/* 177 */     return this.nameAndParameters;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String formatMethodAtString(String name, JSMethodData method) {
/* 182 */     StringBuilder sb = new StringBuilder(name);
/* 183 */     sb.append('(');
/* 184 */     int count = method.getParameterCount();
/* 185 */     for (int i = 0; i < count; i++) {
/* 186 */       sb.append(method.getParameterName(i));
/* 187 */       if (i < count - 1) {
/* 188 */         sb.append(", ");
/*     */       }
/*     */     } 
/* 191 */     sb.append(')');
/* 192 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSignature() {
/* 197 */     return getNameAndParameters();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSummary() {
/* 203 */     String summary = getMethodSummary();
/*     */ 
/*     */     
/* 206 */     if (summary != null && summary.startsWith("/**")) {
/* 207 */       summary = Util.docCommentToHtml(summary);
/*     */     }
/*     */     
/* 210 */     return summary;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 216 */     return getCompareString().hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 225 */     return getSignature();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType() {
/* 231 */     String value = getType(true);
/* 232 */     return ((SourceCompletionProvider)getProvider()).getTypesFactory().convertJavaScriptType(value, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType(boolean qualified) {
/* 238 */     return ((SourceCompletionProvider)getProvider()).getTypesFactory().convertJavaScriptType(this.methodData
/* 239 */         .getType(qualified), qualified);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Icon getIcon() {
/* 245 */     return this.methodData.isStatic() ? 
/* 246 */       IconFactory.getIcon("public_static_function") : 
/* 247 */       IconFactory.getIcon("default_function");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRelevance() {
/* 253 */     return 4;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEnclosingClassName(boolean fullyQualified) {
/* 259 */     return this.methodData.getEnclosingClassName(fullyQualified);
/*     */   }
/*     */ 
/*     */   
/*     */   public JSMethodData getMethodData() {
/* 264 */     return this.methodData;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class JSFunctionParam
/*     */     extends ParameterizedCompletion.Parameter
/*     */   {
/*     */     private boolean showParameterType;
/*     */ 
/*     */     
/*     */     private CompletionProvider provider;
/*     */ 
/*     */     
/*     */     public JSFunctionParam(Object type, String name, boolean showParameterType, CompletionProvider provider) {
/* 279 */       super(type, name);
/* 280 */       this.showParameterType = showParameterType;
/* 281 */       this.provider = provider;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String getType() {
/* 287 */       return this.showParameterType ? ((SourceCompletionProvider)this.provider).getTypesFactory()
/* 288 */         .convertJavaScriptType(super.getType(), false) : null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\completion\JSFunctionCompletion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */