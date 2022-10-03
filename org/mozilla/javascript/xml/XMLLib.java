/*     */ package org.mozilla.javascript.xml;
/*     */ 
/*     */ import org.mozilla.javascript.Context;
/*     */ import org.mozilla.javascript.Ref;
/*     */ import org.mozilla.javascript.ScriptRuntime;
/*     */ import org.mozilla.javascript.Scriptable;
/*     */ import org.mozilla.javascript.ScriptableObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class XMLLib
/*     */ {
/*  13 */   private static final Object XML_LIB_KEY = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static abstract class Factory
/*     */   {
/*     */     public static Factory create(final String className) {
/*  26 */       return new Factory()
/*     */         {
/*     */           public String getImplementationClassName() {
/*  29 */             return className;
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public abstract String getImplementationClassName();
/*     */   }
/*     */   
/*     */   public static XMLLib extractFromScopeOrNull(Scriptable scope) {
/*  39 */     ScriptableObject so = ScriptRuntime.getLibraryScopeOrNull(scope);
/*  40 */     if (so == null)
/*     */     {
/*  42 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  47 */     ScriptableObject.getProperty((Scriptable)so, "XML");
/*     */     
/*  49 */     return (XMLLib)so.getAssociatedValue(XML_LIB_KEY);
/*     */   }
/*     */ 
/*     */   
/*     */   public static XMLLib extractFromScope(Scriptable scope) {
/*  54 */     XMLLib lib = extractFromScopeOrNull(scope);
/*  55 */     if (lib != null) {
/*  56 */       return lib;
/*     */     }
/*  58 */     String msg = ScriptRuntime.getMessage0("msg.XML.not.available");
/*  59 */     throw Context.reportRuntimeError(msg);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final XMLLib bindToScope(Scriptable scope) {
/*  64 */     ScriptableObject so = ScriptRuntime.getLibraryScopeOrNull(scope);
/*  65 */     if (so == null)
/*     */     {
/*  67 */       throw new IllegalStateException();
/*     */     }
/*  69 */     return (XMLLib)so.associateValue(XML_LIB_KEY, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean isXMLName(Context paramContext, Object paramObject);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Ref nameRef(Context paramContext, Object paramObject, Scriptable paramScriptable, int paramInt);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Ref nameRef(Context paramContext, Object paramObject1, Object paramObject2, Scriptable paramScriptable, int paramInt);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String escapeAttributeValue(Object paramObject);
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String escapeTextValue(Object paramObject);
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Object toDefaultXmlNamespace(Context paramContext, Object paramObject);
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIgnoreComments(boolean b) {
/* 103 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public void setIgnoreWhitespace(boolean b) {
/* 107 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public void setIgnoreProcessingInstructions(boolean b) {
/* 111 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public void setPrettyPrinting(boolean b) {
/* 115 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public void setPrettyIndent(int i) {
/* 119 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean isIgnoreComments() {
/* 123 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean isIgnoreProcessingInstructions() {
/* 127 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean isIgnoreWhitespace() {
/* 131 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean isPrettyPrinting() {
/* 135 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public int getPrettyIndent() {
/* 139 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\xml\XMLLib.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */