/*     */ package org.mozilla.javascript.typedarrays;
/*     */ 
/*     */ import org.mozilla.javascript.Context;
/*     */ import org.mozilla.javascript.IdFunctionObject;
/*     */ import org.mozilla.javascript.IdScriptableObject;
/*     */ import org.mozilla.javascript.ScriptRuntime;
/*     */ import org.mozilla.javascript.Scriptable;
/*     */ import org.mozilla.javascript.Undefined;
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
/*     */ public class NativeArrayBuffer
/*     */   extends IdScriptableObject
/*     */ {
/*     */   private static final long serialVersionUID = 3110411773054879549L;
/*     */   public static final String CLASS_NAME = "ArrayBuffer";
/*  28 */   private static final byte[] EMPTY_BUF = new byte[0];
/*     */   
/*  30 */   public static final NativeArrayBuffer EMPTY_BUFFER = new NativeArrayBuffer();
/*     */   final byte[] buffer;
/*     */   private static final int Id_constructor = 1;
/*     */   private static final int Id_slice = 2;
/*     */   private static final int Id_isView = 3;
/*     */   
/*     */   public String getClassName() {
/*  37 */     return "ArrayBuffer";
/*     */   }
/*     */   private static final int MAX_PROTOTYPE_ID = 3; private static final int ConstructorId_isView = -3; private static final int Id_byteLength = 1; private static final int MAX_INSTANCE_ID = 1;
/*     */   
/*     */   public static void init(Context cx, Scriptable scope, boolean sealed) {
/*  42 */     NativeArrayBuffer na = new NativeArrayBuffer();
/*  43 */     na.exportAsJSClass(3, scope, sealed);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NativeArrayBuffer() {
/*  51 */     this.buffer = EMPTY_BUF;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NativeArrayBuffer(int len) {
/*  59 */     if (len < 0) {
/*  60 */       throw ScriptRuntime.constructError("RangeError", "Negative array length " + len);
/*     */     }
/*  62 */     if (len == 0) {
/*  63 */       this.buffer = EMPTY_BUF;
/*     */     } else {
/*  65 */       this.buffer = new byte[len];
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLength() {
/*  73 */     return this.buffer.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getBuffer() {
/*  81 */     return this.buffer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NativeArrayBuffer slice(int s, int e) {
/* 100 */     int end = Math.max(0, Math.min(this.buffer.length, (e < 0) ? (this.buffer.length + e) : e));
/* 101 */     int start = Math.min(end, Math.max(0, (s < 0) ? (this.buffer.length + s) : s));
/* 102 */     int len = end - start;
/*     */     
/* 104 */     NativeArrayBuffer newBuf = new NativeArrayBuffer(len);
/* 105 */     System.arraycopy(this.buffer, start, newBuf.buffer, 0, len);
/* 106 */     return newBuf;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/*     */     int length;
/*     */     NativeArrayBuffer self;
/*     */     int start, end;
/* 115 */     if (!f.hasTag("ArrayBuffer")) {
/* 116 */       return super.execIdCall(f, cx, scope, thisObj, args);
/*     */     }
/* 118 */     int id = f.methodId();
/* 119 */     switch (id) {
/*     */       case -3:
/* 121 */         return Boolean.valueOf((isArg(args, 0) && args[0] instanceof NativeArrayBufferView));
/*     */       
/*     */       case 1:
/* 124 */         length = isArg(args, 0) ? ScriptRuntime.toInt32(args[0]) : 0;
/* 125 */         return new NativeArrayBuffer(length);
/*     */       
/*     */       case 2:
/* 128 */         self = realThis(thisObj, f);
/* 129 */         start = isArg(args, 0) ? ScriptRuntime.toInt32(args[0]) : 0;
/* 130 */         end = isArg(args, 1) ? ScriptRuntime.toInt32(args[1]) : self.buffer.length;
/* 131 */         return self.slice(start, end);
/*     */     } 
/* 133 */     throw new IllegalArgumentException(String.valueOf(id));
/*     */   }
/*     */ 
/*     */   
/*     */   private static NativeArrayBuffer realThis(Scriptable thisObj, IdFunctionObject f) {
/* 138 */     if (!(thisObj instanceof NativeArrayBuffer))
/* 139 */       throw incompatibleCallError(f); 
/* 140 */     return (NativeArrayBuffer)thisObj;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isArg(Object[] args, int i) {
/* 145 */     return (args.length > i && !Undefined.instance.equals(args[i]));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initPrototypeId(int id) {
/*     */     String s;
/*     */     int arity;
/* 153 */     switch (id) { case 1:
/* 154 */         arity = 1; s = "constructor"; break;
/* 155 */       case 2: arity = 1; s = "slice"; break;
/* 156 */       default: throw new IllegalArgumentException(String.valueOf(id)); }
/*     */     
/* 158 */     initPrototypeMethod("ArrayBuffer", id, s, arity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int findPrototypeId(String s) {
/* 168 */     int id = 0; String X = null;
/* 169 */     int s_length = s.length();
/* 170 */     if (s_length == 5) { X = "slice"; id = 2; }
/* 171 */     else if (s_length == 6) { X = "isView"; id = 3; }
/* 172 */     else if (s_length == 11) { X = "constructor"; id = 1; }
/* 173 */      if (X != null && X != s && !X.equals(s)) id = 0;
/*     */ 
/*     */ 
/*     */     
/* 177 */     return id;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void fillConstructorProperties(IdFunctionObject ctor) {
/* 196 */     addIdFunctionProperty((Scriptable)ctor, "ArrayBuffer", -3, "isView", 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getMaxInstanceId() {
/* 204 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getInstanceIdName(int id) {
/* 210 */     if (id == 1) return "byteLength"; 
/* 211 */     return super.getInstanceIdName(id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object getInstanceIdValue(int id) {
/* 217 */     if (id == 1) {
/* 218 */       return ScriptRuntime.wrapInt(this.buffer.length);
/*     */     }
/* 220 */     return super.getInstanceIdValue(id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int findInstanceIdInfo(String s) {
/* 226 */     if ("byteLength".equals(s)) {
/* 227 */       return instanceIdInfo(5, 1);
/*     */     }
/* 229 */     return super.findInstanceIdInfo(s);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\typedarrays\NativeArrayBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */