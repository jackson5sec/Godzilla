/*     */ package org.fife.rsta.ac.js.completion;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.swing.Icon;
/*     */ import org.fife.rsta.ac.java.Util;
/*     */ import org.fife.rsta.ac.java.rjc.ast.FormalParameter;
/*     */ import org.fife.rsta.ac.java.rjc.ast.Method;
/*     */ import org.fife.rsta.ac.js.IconFactory;
/*     */ import org.fife.rsta.ac.js.SourceCompletionProvider;
/*     */ import org.fife.ui.autocomplete.Completion;
/*     */ import org.fife.ui.autocomplete.CompletionProvider;
/*     */ import org.fife.ui.autocomplete.FunctionCompletion;
/*     */ import org.fife.ui.autocomplete.ParameterizedCompletion;
/*     */ 
/*     */ 
/*     */ public class JavaScriptMethodCompletion
/*     */   extends FunctionCompletion
/*     */   implements JSCompletion
/*     */ {
/*     */   private Method method;
/*     */   private String compareString;
/*     */   private boolean systemFunction;
/*     */   private String nameAndParameters;
/*     */   
/*     */   public JavaScriptMethodCompletion(CompletionProvider provider, Method method) {
/*  27 */     super(provider, method.getName(), null);
/*  28 */     this.method = method;
/*  29 */     int count = method.getParameterCount();
/*  30 */     List<ParameterizedCompletion.Parameter> params = new ArrayList<>(count);
/*  31 */     for (int i = 0; i < count; i++) {
/*  32 */       FormalParameter param = method.getParameter(i);
/*  33 */       String name = param.getName();
/*  34 */       params.add(new ParameterizedCompletion.Parameter(null, name));
/*     */     } 
/*  36 */     setParams(params);
/*     */   }
/*     */ 
/*     */   
/*     */   private String createNameAndParameters() {
/*  41 */     StringBuilder sb = new StringBuilder(getName());
/*  42 */     sb.append('(');
/*  43 */     int count = this.method.getParameterCount();
/*  44 */     for (int i = 0; i < count; i++) {
/*  45 */       FormalParameter fp = this.method.getParameter(i);
/*  46 */       sb.append(fp.getName());
/*  47 */       if (i < count - 1) {
/*  48 */         sb.append(", ");
/*     */       }
/*     */     } 
/*  51 */     sb.append(')');
/*  52 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Icon getIcon() {
/*  58 */     return IconFactory.getIcon(this.systemFunction ? "function" : "default_function");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRelevance() {
/*  65 */     return this.systemFunction ? 3 : 4;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSystemFunction(boolean systemFunction) {
/*  70 */     this.systemFunction = systemFunction;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSystemFunction() {
/*  75 */     return this.systemFunction;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSummary() {
/*  81 */     String summary = getMethodSummary();
/*     */ 
/*     */     
/*  84 */     if (summary != null && summary.startsWith("/**")) {
/*  85 */       summary = Util.docCommentToHtml(summary);
/*     */     }
/*     */     
/*  88 */     return summary;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSignature() {
/*  93 */     if (this.nameAndParameters == null) {
/*  94 */       this.nameAndParameters = createNameAndParameters();
/*     */     }
/*  96 */     return this.nameAndParameters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 105 */     return getCompareString().hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 114 */     return getSignature();
/*     */   }
/*     */ 
/*     */   
/*     */   private String getMethodSummary() {
/* 119 */     String docComment = this.method.getDocComment();
/* 120 */     return (docComment != null) ? docComment : this.method.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(Completion other) {
/* 129 */     int rc = -1;
/* 130 */     if (other == this) {
/* 131 */       rc = 0;
/*     */     }
/* 133 */     else if (other instanceof JSCompletion) {
/* 134 */       JSCompletion c2 = (JSCompletion)other;
/* 135 */       rc = getLookupName().compareTo(c2.getLookupName());
/*     */     }
/* 137 */     else if (other != null) {
/* 138 */       Completion c2 = other;
/* 139 */       rc = toString().compareTo(c2.toString());
/* 140 */       if (rc == 0) {
/* 141 */         String clazz1 = getClass().getName();
/* 142 */         clazz1 = clazz1.substring(clazz1.lastIndexOf('.'));
/* 143 */         String clazz2 = c2.getClass().getName();
/* 144 */         clazz2 = clazz2.substring(clazz2.lastIndexOf('.'));
/* 145 */         rc = clazz1.compareTo(clazz2);
/*     */       } 
/*     */     } 
/*     */     
/* 149 */     return rc;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 155 */     if (obj == this) {
/* 156 */       return true;
/*     */     }
/* 158 */     if (obj instanceof JSCompletion) {
/*     */       
/* 160 */       JSCompletion jsComp = (JSCompletion)obj;
/* 161 */       return getLookupName().equals(jsComp.getLookupName());
/*     */     } 
/* 163 */     return super.equals(obj);
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
/* 177 */     if (this.compareString == null) {
/* 178 */       StringBuilder sb = new StringBuilder(getName());
/*     */       
/* 180 */       int paramCount = getParamCount();
/* 181 */       if (paramCount < 10) {
/* 182 */         sb.append('0');
/*     */       }
/* 184 */       sb.append(paramCount);
/* 185 */       for (int i = 0; i < paramCount; i++) {
/* 186 */         String type = getParam(i).getType();
/* 187 */         sb.append(type);
/* 188 */         if (i < paramCount - 1) {
/* 189 */           sb.append(',');
/*     */         }
/*     */       } 
/* 192 */       this.compareString = sb.toString();
/*     */     } 
/*     */     
/* 195 */     return this.compareString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefinitionString() {
/* 202 */     return getSignature();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getType(boolean qualified) {
/* 207 */     return ((SourceCompletionProvider)getProvider()).getTypesFactory().convertJavaScriptType("void", qualified);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getEnclosingClassName(boolean fullyQualified) {
/* 212 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLookupName() {
/* 218 */     StringBuilder sb = new StringBuilder(getName());
/* 219 */     sb.append('(');
/* 220 */     int count = getParamCount();
/* 221 */     for (int i = 0; i < count; i++) {
/* 222 */       sb.append("p");
/* 223 */       if (i < count - 1) {
/* 224 */         sb.append(",");
/*     */       }
/*     */     } 
/* 227 */     sb.append(')');
/* 228 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\completion\JavaScriptMethodCompletion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */