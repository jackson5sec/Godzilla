/*     */ package org.mozilla.javascript.tools.shell;
/*     */ 
/*     */ import java.util.Map;
/*     */ import org.mozilla.javascript.ScriptRuntime;
/*     */ import org.mozilla.javascript.Scriptable;
/*     */ import org.mozilla.javascript.ScriptableObject;
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
/*     */ public class Environment
/*     */   extends ScriptableObject
/*     */ {
/*     */   static final long serialVersionUID = -430727378460177065L;
/*  33 */   private Environment thePrototypeInstance = null;
/*     */   
/*     */   public static void defineClass(ScriptableObject scope) {
/*     */     try {
/*  37 */       ScriptableObject.defineClass((Scriptable)scope, Environment.class);
/*  38 */     } catch (Exception e) {
/*  39 */       throw new Error(e.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  45 */     return "Environment";
/*     */   }
/*     */   
/*     */   public Environment() {
/*  49 */     if (this.thePrototypeInstance == null)
/*  50 */       this.thePrototypeInstance = this; 
/*     */   }
/*     */   
/*     */   public Environment(ScriptableObject scope) {
/*  54 */     setParentScope((Scriptable)scope);
/*  55 */     Object ctor = ScriptRuntime.getTopLevelProp((Scriptable)scope, "Environment");
/*  56 */     if (ctor != null && ctor instanceof Scriptable) {
/*  57 */       Scriptable s = (Scriptable)ctor;
/*  58 */       setPrototype((Scriptable)s.get("prototype", s));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean has(String name, Scriptable start) {
/*  64 */     if (this == this.thePrototypeInstance) {
/*  65 */       return super.has(name, start);
/*     */     }
/*  67 */     return (System.getProperty(name) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object get(String name, Scriptable start) {
/*  72 */     if (this == this.thePrototypeInstance) {
/*  73 */       return super.get(name, start);
/*     */     }
/*  75 */     String result = System.getProperty(name);
/*  76 */     if (result != null) {
/*  77 */       return ScriptRuntime.toObject(getParentScope(), result);
/*     */     }
/*  79 */     return Scriptable.NOT_FOUND;
/*     */   }
/*     */ 
/*     */   
/*     */   public void put(String name, Scriptable start, Object value) {
/*  84 */     if (this == this.thePrototypeInstance) {
/*  85 */       super.put(name, start, value);
/*     */     } else {
/*  87 */       System.getProperties().put(name, ScriptRuntime.toString(value));
/*     */     } 
/*     */   }
/*     */   private Object[] collectIds() {
/*  91 */     Map<Object, Object> props = System.getProperties();
/*  92 */     return props.keySet().toArray();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] getIds() {
/*  97 */     if (this == this.thePrototypeInstance)
/*  98 */       return super.getIds(); 
/*  99 */     return collectIds();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] getAllIds() {
/* 104 */     if (this == this.thePrototypeInstance)
/* 105 */       return super.getAllIds(); 
/* 106 */     return collectIds();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\tools\shell\Environment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */