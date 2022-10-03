/*     */ package org.mozilla.javascript.serialize;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.StringTokenizer;
/*     */ import org.mozilla.javascript.Scriptable;
/*     */ import org.mozilla.javascript.ScriptableObject;
/*     */ import org.mozilla.javascript.UniqueTag;
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
/*     */ public class ScriptableOutputStream
/*     */   extends ObjectOutputStream
/*     */ {
/*     */   private Scriptable scope;
/*     */   private Map<Object, String> table;
/*     */   
/*     */   public ScriptableOutputStream(OutputStream out, Scriptable scope) throws IOException {
/*  47 */     super(out);
/*  48 */     this.scope = scope;
/*  49 */     this.table = new HashMap<Object, String>();
/*  50 */     this.table.put(scope, "");
/*  51 */     enableReplaceObject(true);
/*  52 */     excludeStandardObjectNames();
/*     */   }
/*     */   
/*     */   public void excludeAllIds(Object[] ids) {
/*  56 */     for (Object id : ids) {
/*  57 */       if (id instanceof String && this.scope.get((String)id, this.scope) instanceof Scriptable)
/*     */       {
/*     */         
/*  60 */         addExcludedName((String)id);
/*     */       }
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
/*     */ 
/*     */   
/*     */   public void addOptionalExcludedName(String name) {
/*  76 */     Object obj = lookupQualifiedName(this.scope, name);
/*  77 */     if (obj != null && obj != UniqueTag.NOT_FOUND) {
/*  78 */       if (!(obj instanceof Scriptable)) {
/*  79 */         throw new IllegalArgumentException("Object for excluded name " + name + " is not a Scriptable, it is " + obj.getClass().getName());
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  84 */       this.table.put(obj, name);
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
/*     */   
/*     */   public void addExcludedName(String name) {
/*  98 */     Object obj = lookupQualifiedName(this.scope, name);
/*  99 */     if (!(obj instanceof Scriptable)) {
/* 100 */       throw new IllegalArgumentException("Object for excluded name " + name + " not found.");
/*     */     }
/*     */     
/* 103 */     this.table.put(obj, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasExcludedName(String name) {
/* 110 */     return (this.table.get(name) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeExcludedName(String name) {
/* 117 */     this.table.remove(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void excludeStandardObjectNames() {
/* 125 */     String[] names = { "Object", "Object.prototype", "Function", "Function.prototype", "String", "String.prototype", "Math", "Array", "Array.prototype", "Error", "Error.prototype", "Number", "Number.prototype", "Date", "Date.prototype", "RegExp", "RegExp.prototype", "Script", "Script.prototype", "Continuation", "Continuation.prototype" };
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
/* 137 */     for (int i = 0; i < names.length; i++) {
/* 138 */       addExcludedName(names[i]);
/*     */     }
/*     */     
/* 141 */     String[] optionalNames = { "XML", "XML.prototype", "XMLList", "XMLList.prototype" };
/*     */ 
/*     */ 
/*     */     
/* 145 */     for (int j = 0; j < optionalNames.length; j++) {
/* 146 */       addOptionalExcludedName(optionalNames[j]);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static Object lookupQualifiedName(Scriptable scope, String qualifiedName) {
/* 153 */     StringTokenizer st = new StringTokenizer(qualifiedName, ".");
/* 154 */     Object result = scope;
/* 155 */     while (st.hasMoreTokens()) {
/* 156 */       String s = st.nextToken();
/* 157 */       result = ScriptableObject.getProperty((Scriptable)result, s);
/* 158 */       if (result == null || !(result instanceof Scriptable))
/*     */         break; 
/*     */     } 
/* 161 */     return result;
/*     */   }
/*     */   
/*     */   static class PendingLookup implements Serializable { static final long serialVersionUID = -2692990309789917727L;
/*     */     private String name;
/*     */     
/*     */     PendingLookup(String name) {
/* 168 */       this.name = name;
/*     */     } String getName() {
/* 170 */       return this.name;
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object replaceObject(Object obj) throws IOException {
/* 179 */     String name = this.table.get(obj);
/* 180 */     if (name == null)
/* 181 */       return obj; 
/* 182 */     return new PendingLookup(name);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\serialize\ScriptableOutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */