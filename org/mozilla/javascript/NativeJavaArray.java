/*     */ package org.mozilla.javascript;
/*     */ 
/*     */ import java.lang.reflect.Array;
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
/*     */ public class NativeJavaArray
/*     */   extends NativeJavaObject
/*     */ {
/*     */   static final long serialVersionUID = -924022554283675333L;
/*     */   Object array;
/*     */   int length;
/*     */   Class<?> cls;
/*     */   
/*     */   public String getClassName() {
/*  26 */     return "JavaArray";
/*     */   }
/*     */   
/*     */   public static NativeJavaArray wrap(Scriptable scope, Object array) {
/*  30 */     return new NativeJavaArray(scope, array);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object unwrap() {
/*  35 */     return this.array;
/*     */   }
/*     */   
/*     */   public NativeJavaArray(Scriptable scope, Object array) {
/*  39 */     super(scope, (Object)null, ScriptRuntime.ObjectClass);
/*  40 */     Class<?> cl = array.getClass();
/*  41 */     if (!cl.isArray()) {
/*  42 */       throw new RuntimeException("Array expected");
/*     */     }
/*  44 */     this.array = array;
/*  45 */     this.length = Array.getLength(array);
/*  46 */     this.cls = cl.getComponentType();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean has(String id, Scriptable start) {
/*  51 */     return (id.equals("length") || super.has(id, start));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean has(int index, Scriptable start) {
/*  56 */     return (0 <= index && index < this.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object get(String id, Scriptable start) {
/*  61 */     if (id.equals("length"))
/*  62 */       return Integer.valueOf(this.length); 
/*  63 */     Object result = super.get(id, start);
/*  64 */     if (result == NOT_FOUND && !ScriptableObject.hasProperty(getPrototype(), id))
/*     */     {
/*     */       
/*  67 */       throw Context.reportRuntimeError2("msg.java.member.not.found", this.array.getClass().getName(), id);
/*     */     }
/*     */     
/*  70 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object get(int index, Scriptable start) {
/*  75 */     if (0 <= index && index < this.length) {
/*  76 */       Context cx = Context.getContext();
/*  77 */       Object obj = Array.get(this.array, index);
/*  78 */       return cx.getWrapFactory().wrap(cx, this, obj, this.cls);
/*     */     } 
/*  80 */     return Undefined.instance;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(String id, Scriptable start, Object value) {
/*  86 */     if (!id.equals("length")) {
/*  87 */       throw Context.reportRuntimeError1("msg.java.array.member.not.found", id);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void put(int index, Scriptable start, Object value) {
/*  93 */     if (0 <= index && index < this.length) {
/*  94 */       Array.set(this.array, index, Context.jsToJava(value, this.cls));
/*     */     } else {
/*     */       
/*  97 */       throw Context.reportRuntimeError2("msg.java.array.index.out.of.bounds", String.valueOf(index), String.valueOf(this.length - 1));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getDefaultValue(Class<?> hint) {
/* 105 */     if (hint == null || hint == ScriptRuntime.StringClass)
/* 106 */       return this.array.toString(); 
/* 107 */     if (hint == ScriptRuntime.BooleanClass)
/* 108 */       return Boolean.TRUE; 
/* 109 */     if (hint == ScriptRuntime.NumberClass)
/* 110 */       return ScriptRuntime.NaNobj; 
/* 111 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] getIds() {
/* 116 */     Object[] result = new Object[this.length];
/* 117 */     int i = this.length;
/* 118 */     while (--i >= 0)
/* 119 */       result[i] = Integer.valueOf(i); 
/* 120 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasInstance(Scriptable value) {
/* 125 */     if (!(value instanceof Wrapper))
/* 126 */       return false; 
/* 127 */     Object instance = ((Wrapper)value).unwrap();
/* 128 */     return this.cls.isInstance(instance);
/*     */   }
/*     */ 
/*     */   
/*     */   public Scriptable getPrototype() {
/* 133 */     if (this.prototype == null) {
/* 134 */       this.prototype = ScriptableObject.getArrayPrototype(getParentScope());
/*     */     }
/*     */     
/* 137 */     return this.prototype;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\NativeJavaArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */