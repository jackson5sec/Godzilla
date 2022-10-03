/*     */ package org.fife.rsta.ac.js.ast.jsType;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.fife.rsta.ac.js.SourceCompletionProvider;
/*     */ import org.fife.rsta.ac.js.ast.type.TypeDeclaration;
/*     */ import org.fife.rsta.ac.js.completion.JSCompletion;
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
/*     */ public class JavaScriptType
/*     */ {
/*     */   protected TypeDeclaration type;
/*     */   protected HashMap<String, JSCompletion> methodFieldCompletions;
/*     */   protected HashMap<String, JSCompletion> constructors;
/*     */   protected JSCompletion classType;
/*     */   private ArrayList<JavaScriptType> extended;
/*     */   
/*     */   public JavaScriptType(TypeDeclaration type) {
/*  47 */     this.type = type;
/*  48 */     this.methodFieldCompletions = new HashMap<>();
/*  49 */     this.constructors = new HashMap<>();
/*  50 */     this.extended = new ArrayList<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCompletion(JSCompletion completion) {
/*  61 */     this.methodFieldCompletions.put(completion.getLookupName(), completion);
/*     */   }
/*     */ 
/*     */   
/*     */   public JSCompletion removeCompletion(String completionLookup, SourceCompletionProvider provider) {
/*  66 */     JSCompletion completion = getCompletion(completionLookup, provider);
/*  67 */     if (completion != null) {
/*  68 */       removeCompletion(this, completion);
/*     */     }
/*  70 */     return completion;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void removeCompletion(JavaScriptType type, JSCompletion completion) {
/*  80 */     if (type.methodFieldCompletions.containsKey(completion.getLookupName())) {
/*  81 */       type.methodFieldCompletions.remove(completion.getLookupName());
/*     */     }
/*     */     
/*  84 */     for (JavaScriptType extendedType : type.extended) {
/*  85 */       removeCompletion(extendedType, completion);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConstructor(JSCompletion completion) {
/*  94 */     this.constructors.put(completion.getLookupName(), completion);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeConstructor(JSCompletion completion) {
/*  99 */     this.constructors.remove(completion.getLookupName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClassTypeCompletion(JSCompletion classType) {
/* 107 */     this.classType = classType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSCompletion getClassTypeCompletion() {
/* 114 */     return this.classType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSCompletion getCompletion(String completionLookup, SourceCompletionProvider provider) {
/* 124 */     return getCompletion(this, completionLookup, provider);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JSCompletion _getCompletion(String completionLookup, SourceCompletionProvider provider) {
/* 134 */     return this.methodFieldCompletions.get(completionLookup);
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
/*     */   private static JSCompletion getCompletion(JavaScriptType cachedType, String completionLookup, SourceCompletionProvider provider) {
/* 148 */     JSCompletion completion = cachedType._getCompletion(completionLookup, provider);
/*     */     
/* 150 */     if (completion == null) {
/*     */       
/* 152 */       Iterator<JavaScriptType> i = cachedType.getExtendedClasses().iterator();
/* 153 */       while (i.hasNext()) {
/* 154 */         completion = getCompletion(i.next(), completionLookup, provider);
/* 155 */         if (completion != null)
/*     */           break; 
/*     */       } 
/*     */     } 
/* 159 */     return completion;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HashMap<String, JSCompletion> getMethodFieldCompletions() {
/* 168 */     return this.methodFieldCompletions;
/*     */   }
/*     */   
/*     */   public HashMap<String, JSCompletion> getConstructorCompletions() {
/* 172 */     return this.constructors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeDeclaration getType() {
/* 182 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addExtension(JavaScriptType type) {
/* 193 */     this.extended.add(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<JavaScriptType> getExtendedClasses() {
/* 202 */     return this.extended;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 208 */     if (o == this) {
/* 209 */       return true;
/*     */     }
/* 211 */     if (o instanceof JavaScriptType) {
/* 212 */       JavaScriptType ct = (JavaScriptType)o;
/* 213 */       return ct.getType().equals(getType());
/*     */     } 
/*     */     
/* 216 */     if (o instanceof TypeDeclaration) {
/* 217 */       TypeDeclaration dec = (TypeDeclaration)o;
/* 218 */       return dec.equals(getType());
/*     */     } 
/*     */     
/* 221 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 232 */     return getType().hashCode();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\ast\jsType\JavaScriptType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */