/*     */ package org.mozilla.javascript;public class NativeCallSite extends IdScriptableObject {
/*     */   private static final String CALLSITE_TAG = "CallSite";
/*     */   private ScriptStackElement element;
/*     */   private static final int Id_constructor = 1;
/*     */   private static final int Id_getThis = 2;
/*     */   private static final int Id_getTypeName = 3;
/*     */   private static final int Id_getFunction = 4;
/*     */   private static final int Id_getFunctionName = 5;
/*     */   private static final int Id_getMethodName = 6;
/*     */   private static final int Id_getFileName = 7;
/*     */   private static final int Id_getLineNumber = 8;
/*     */   private static final int Id_getColumnNumber = 9;
/*     */   private static final int Id_getEvalOrigin = 10;
/*     */   private static final int Id_isToplevel = 11;
/*     */   private static final int Id_isEval = 12;
/*     */   private static final int Id_isNative = 13;
/*     */   private static final int Id_isConstructor = 14;
/*     */   private static final int Id_toString = 15;
/*     */   private static final int MAX_PROTOTYPE_ID = 15;
/*     */   
/*     */   static void init(Scriptable scope, boolean sealed) {
/*  22 */     NativeCallSite cs = new NativeCallSite();
/*  23 */     cs.exportAsJSClass(15, scope, sealed);
/*     */   }
/*     */ 
/*     */   
/*     */   static NativeCallSite make(Scriptable scope, Scriptable ctorObj) {
/*  28 */     NativeCallSite cs = new NativeCallSite();
/*  29 */     Scriptable proto = (Scriptable)ctorObj.get("prototype", ctorObj);
/*  30 */     cs.setParentScope(scope);
/*  31 */     cs.setPrototype(proto);
/*  32 */     return cs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setElement(ScriptStackElement elt) {
/*  41 */     this.element = elt;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  47 */     return "CallSite";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initPrototypeId(int id) {
/*     */     String s;
/*     */     int arity;
/*  55 */     switch (id) { case 1:
/*  56 */         arity = 0; s = "constructor"; break;
/*  57 */       case 2: arity = 0; s = "getThis"; break;
/*  58 */       case 3: arity = 0; s = "getTypeName"; break;
/*  59 */       case 4: arity = 0; s = "getFunction"; break;
/*  60 */       case 5: arity = 0; s = "getFunctionName"; break;
/*  61 */       case 6: arity = 0; s = "getMethodName"; break;
/*  62 */       case 7: arity = 0; s = "getFileName"; break;
/*  63 */       case 8: arity = 0; s = "getLineNumber"; break;
/*  64 */       case 9: arity = 0; s = "getColumnNumber"; break;
/*  65 */       case 10: arity = 0; s = "getEvalOrigin"; break;
/*  66 */       case 11: arity = 0; s = "isToplevel"; break;
/*  67 */       case 12: arity = 0; s = "isEval"; break;
/*  68 */       case 13: arity = 0; s = "isNative"; break;
/*  69 */       case 14: arity = 0; s = "isConstructor"; break;
/*  70 */       case 15: arity = 0; s = "toString"; break;
/*  71 */       default: throw new IllegalArgumentException(String.valueOf(id)); }
/*     */     
/*  73 */     initPrototypeMethod("CallSite", id, s, arity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/*  80 */     if (!f.hasTag("CallSite")) {
/*  81 */       return super.execIdCall(f, cx, scope, thisObj, args);
/*     */     }
/*  83 */     int id = f.methodId();
/*  84 */     switch (id) {
/*     */       case 1:
/*  86 */         return make(scope, f);
/*     */       case 5:
/*  88 */         return getFunctionName(thisObj);
/*     */       case 7:
/*  90 */         return getFileName(thisObj);
/*     */       case 8:
/*  92 */         return getLineNumber(thisObj);
/*     */       case 2:
/*     */       case 3:
/*     */       case 4:
/*     */       case 9:
/*  97 */         return getUndefined();
/*     */       case 6:
/*  99 */         return getNull();
/*     */       case 10:
/*     */       case 12:
/*     */       case 14:
/* 103 */         return getFalse();
/*     */       case 15:
/* 105 */         return js_toString(thisObj);
/*     */     } 
/* 107 */     throw new IllegalArgumentException(String.valueOf(id));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 114 */     if (this.element == null) {
/* 115 */       return "";
/*     */     }
/* 117 */     return this.element.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private Object js_toString(Scriptable obj) {
/* 122 */     while (obj != null && !(obj instanceof NativeCallSite)) {
/* 123 */       obj = obj.getPrototype();
/*     */     }
/* 125 */     if (obj == null) {
/* 126 */       return NOT_FOUND;
/*     */     }
/* 128 */     NativeCallSite cs = (NativeCallSite)obj;
/* 129 */     StringBuilder sb = new StringBuilder();
/* 130 */     cs.element.renderJavaStyle(sb);
/* 131 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private Object getUndefined() {
/* 136 */     return Undefined.instance;
/*     */   }
/*     */ 
/*     */   
/*     */   private Object getNull() {
/* 141 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private Object getFalse() {
/* 146 */     return Boolean.FALSE;
/*     */   }
/*     */ 
/*     */   
/*     */   private Object getFunctionName(Scriptable obj) {
/* 151 */     while (obj != null && !(obj instanceof NativeCallSite)) {
/* 152 */       obj = obj.getPrototype();
/*     */     }
/* 154 */     if (obj == null) {
/* 155 */       return NOT_FOUND;
/*     */     }
/* 157 */     NativeCallSite cs = (NativeCallSite)obj;
/* 158 */     return (cs.element == null) ? null : cs.element.functionName;
/*     */   }
/*     */ 
/*     */   
/*     */   private Object getFileName(Scriptable obj) {
/* 163 */     while (obj != null && !(obj instanceof NativeCallSite)) {
/* 164 */       obj = obj.getPrototype();
/*     */     }
/* 166 */     if (obj == null) {
/* 167 */       return NOT_FOUND;
/*     */     }
/* 169 */     NativeCallSite cs = (NativeCallSite)obj;
/* 170 */     return (cs.element == null) ? null : cs.element.fileName;
/*     */   }
/*     */ 
/*     */   
/*     */   private Object getLineNumber(Scriptable obj) {
/* 175 */     while (obj != null && !(obj instanceof NativeCallSite)) {
/* 176 */       obj = obj.getPrototype();
/*     */     }
/* 178 */     if (obj == null) {
/* 179 */       return NOT_FOUND;
/*     */     }
/* 181 */     NativeCallSite cs = (NativeCallSite)obj;
/* 182 */     if (cs.element == null || cs.element.lineNumber < 0) {
/* 183 */       return Undefined.instance;
/*     */     }
/* 185 */     return Integer.valueOf(cs.element.lineNumber);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int findPrototypeId(String s) {
/* 195 */     int c, id = 0; String X = null;
/* 196 */     switch (s.length()) { case 6:
/* 197 */         X = "isEval"; id = 12; break;
/* 198 */       case 7: X = "getThis"; id = 2; break;
/* 199 */       case 8: c = s.charAt(0);
/* 200 */         if (c == 105) { X = "isNative"; id = 13; break; }
/* 201 */          if (c == 116) { X = "toString"; id = 15; }  break;
/*     */       case 10:
/* 203 */         X = "isToplevel"; id = 11; break;
/* 204 */       case 11: switch (s.charAt(4)) { case 'i':
/* 205 */             X = "getFileName"; id = 7; break;
/* 206 */           case 't': X = "constructor"; id = 1; break;
/* 207 */           case 'u': X = "getFunction"; id = 4; break;
/* 208 */           case 'y': X = "getTypeName"; id = 3; break; }  break;
/*     */       case 13:
/* 210 */         switch (s.charAt(3)) { case 'E':
/* 211 */             X = "getEvalOrigin"; id = 10; break;
/* 212 */           case 'L': X = "getLineNumber"; id = 8; break;
/* 213 */           case 'M': X = "getMethodName"; id = 6; break;
/* 214 */           case 'o': X = "isConstructor"; id = 14; break; }  break;
/*     */       case 15:
/* 216 */         c = s.charAt(3);
/* 217 */         if (c == 67) { X = "getColumnNumber"; id = 9; break; }
/* 218 */          if (c == 70) { X = "getFunctionName"; id = 5; }
/*     */          break; }
/*     */     
/* 221 */     if (X != null && X != s && !X.equals(s)) id = 0;
/*     */ 
/*     */ 
/*     */     
/* 225 */     return id;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\NativeCallSite.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */