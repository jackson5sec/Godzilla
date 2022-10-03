/*     */ package org.mozilla.javascript;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Method;
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
/*     */ final class NativeError
/*     */   extends IdScriptableObject
/*     */ {
/*     */   static final long serialVersionUID = -5338413581437645187L;
/*  22 */   private static final Object ERROR_TAG = "Error"; private static final Method ERROR_DELEGATE_GET_STACK;
/*     */   private static final Method ERROR_DELEGATE_SET_STACK;
/*     */   public static final int DEFAULT_STACK_LIMIT = -1;
/*     */   private static final String STACK_HIDE_KEY = "_stackHide";
/*     */   private RhinoException stackProvider;
/*     */   
/*     */   static {
/*     */     try {
/*  30 */       ERROR_DELEGATE_GET_STACK = NativeError.class.getMethod("getStackDelegated", new Class[] { Scriptable.class });
/*  31 */       ERROR_DELEGATE_SET_STACK = NativeError.class.getMethod("setStackDelegated", new Class[] { Scriptable.class, Object.class });
/*  32 */     } catch (NoSuchMethodException nsm) {
/*  33 */       throw new RuntimeException(nsm);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static final int Id_constructor = 1;
/*     */   
/*     */   private static final int Id_toString = 2;
/*     */   
/*     */   private static final int Id_toSource = 3;
/*     */   private static final int ConstructorId_captureStackTrace = -1;
/*     */   private static final int MAX_PROTOTYPE_ID = 3;
/*     */   
/*     */   static void init(Scriptable scope, boolean sealed) {
/*  47 */     NativeError obj = new NativeError();
/*  48 */     ScriptableObject.putProperty(obj, "name", "Error");
/*  49 */     ScriptableObject.putProperty(obj, "message", "");
/*  50 */     ScriptableObject.putProperty(obj, "fileName", "");
/*  51 */     ScriptableObject.putProperty(obj, "lineNumber", Integer.valueOf(0));
/*  52 */     obj.setAttributes("name", 2);
/*  53 */     obj.setAttributes("message", 2);
/*  54 */     obj.exportAsJSClass(3, scope, sealed);
/*  55 */     NativeCallSite.init(obj, sealed);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static NativeError make(Context cx, Scriptable scope, IdFunctionObject ctorObj, Object[] args) {
/*  61 */     Scriptable proto = (Scriptable)ctorObj.get("prototype", ctorObj);
/*     */     
/*  63 */     NativeError obj = new NativeError();
/*  64 */     obj.setPrototype(proto);
/*  65 */     obj.setParentScope(scope);
/*     */     
/*  67 */     int arglen = args.length;
/*  68 */     if (arglen >= 1) {
/*  69 */       if (args[0] != Undefined.instance) {
/*  70 */         ScriptableObject.putProperty(obj, "message", ScriptRuntime.toString(args[0]));
/*     */       }
/*     */       
/*  73 */       if (arglen >= 2) {
/*  74 */         ScriptableObject.putProperty(obj, "fileName", args[1]);
/*  75 */         if (arglen >= 3) {
/*  76 */           int line = ScriptRuntime.toInt32(args[2]);
/*  77 */           ScriptableObject.putProperty(obj, "lineNumber", Integer.valueOf(line));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  82 */     return obj;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void fillConstructorProperties(IdFunctionObject ctor) {
/*  88 */     addIdFunctionProperty(ctor, ERROR_TAG, -1, "captureStackTrace", 2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  94 */     ProtoProps protoProps = new ProtoProps();
/*  95 */     associateValue("_ErrorPrototypeProps", protoProps);
/*     */ 
/*     */     
/*  98 */     ctor.defineProperty("stackTraceLimit", protoProps, ProtoProps.GET_STACK_LIMIT, ProtoProps.SET_STACK_LIMIT, 0);
/*     */     
/* 100 */     ctor.defineProperty("prepareStackTrace", protoProps, ProtoProps.GET_PREPARE_STACK, ProtoProps.SET_PREPARE_STACK, 0);
/*     */ 
/*     */     
/* 103 */     super.fillConstructorProperties(ctor);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/* 109 */     return "Error";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 116 */     Object toString = js_toString(this);
/* 117 */     return (toString instanceof String) ? (String)toString : super.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initPrototypeId(int id) {
/*     */     String s;
/*     */     int arity;
/* 125 */     switch (id) { case 1:
/* 126 */         arity = 1; s = "constructor"; break;
/* 127 */       case 2: arity = 0; s = "toString"; break;
/* 128 */       case 3: arity = 0; s = "toSource"; break;
/* 129 */       default: throw new IllegalArgumentException(String.valueOf(id)); }
/*     */     
/* 131 */     initPrototypeMethod(ERROR_TAG, id, s, arity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/* 138 */     if (!f.hasTag(ERROR_TAG)) {
/* 139 */       return super.execIdCall(f, cx, scope, thisObj, args);
/*     */     }
/* 141 */     int id = f.methodId();
/* 142 */     switch (id) {
/*     */       case 1:
/* 144 */         return make(cx, scope, f, args);
/*     */       
/*     */       case 2:
/* 147 */         return js_toString(thisObj);
/*     */       
/*     */       case 3:
/* 150 */         return js_toSource(cx, scope, thisObj);
/*     */       
/*     */       case -1:
/* 153 */         js_captureStackTrace(cx, thisObj, args);
/* 154 */         return Undefined.instance;
/*     */     } 
/* 156 */     throw new IllegalArgumentException(String.valueOf(id));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStackProvider(RhinoException re) {
/* 164 */     if (this.stackProvider == null) {
/* 165 */       this.stackProvider = re;
/* 166 */       defineProperty("stack", this, ERROR_DELEGATE_GET_STACK, ERROR_DELEGATE_SET_STACK, 2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getStackDelegated(Scriptable target) {
/*     */     Object value;
/* 173 */     if (this.stackProvider == null) {
/* 174 */       return NOT_FOUND;
/*     */     }
/*     */ 
/*     */     
/* 178 */     int limit = -1;
/* 179 */     Function prepare = null;
/* 180 */     NativeError cons = (NativeError)getPrototype();
/* 181 */     ProtoProps pp = (ProtoProps)cons.getAssociatedValue("_ErrorPrototypeProps");
/*     */     
/* 183 */     if (pp != null) {
/* 184 */       limit = pp.getStackTraceLimit();
/* 185 */       prepare = pp.getPrepareStackTrace();
/*     */     } 
/*     */ 
/*     */     
/* 189 */     String hideFunc = (String)getAssociatedValue("_stackHide");
/* 190 */     ScriptStackElement[] stack = this.stackProvider.getScriptStack(limit, hideFunc);
/*     */ 
/*     */ 
/*     */     
/* 194 */     if (prepare == null) {
/* 195 */       value = RhinoException.formatStackTrace(stack, this.stackProvider.details());
/*     */     } else {
/* 197 */       value = callPrepareStack(prepare, stack);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 202 */     setStackDelegated(target, value);
/* 203 */     return value;
/*     */   }
/*     */   
/*     */   public void setStackDelegated(Scriptable target, Object value) {
/* 207 */     target.delete("stack");
/* 208 */     this.stackProvider = null;
/* 209 */     target.put("stack", target, value);
/*     */   }
/*     */ 
/*     */   
/*     */   private Object callPrepareStack(Function prepare, ScriptStackElement[] stack) {
/* 214 */     Context cx = Context.getCurrentContext();
/* 215 */     Object[] elts = new Object[stack.length];
/*     */ 
/*     */     
/* 218 */     for (int i = 0; i < stack.length; i++) {
/* 219 */       NativeCallSite site = (NativeCallSite)cx.newObject(this, "CallSite");
/* 220 */       site.setElement(stack[i]);
/* 221 */       elts[i] = site;
/*     */     } 
/*     */     
/* 224 */     Scriptable eltArray = cx.newArray(this, elts);
/* 225 */     return prepare.call(cx, prepare, this, new Object[] { this, eltArray });
/*     */   }
/*     */   
/*     */   private static Object js_toString(Scriptable thisObj) {
/* 229 */     Object name = ScriptableObject.getProperty(thisObj, "name");
/* 230 */     if (name == NOT_FOUND || name == Undefined.instance) {
/* 231 */       name = "Error";
/*     */     } else {
/* 233 */       name = ScriptRuntime.toString(name);
/*     */     } 
/* 235 */     Object msg = ScriptableObject.getProperty(thisObj, "message");
/* 236 */     if (msg == NOT_FOUND || msg == Undefined.instance) {
/* 237 */       msg = "";
/*     */     } else {
/* 239 */       msg = ScriptRuntime.toString(msg);
/*     */     } 
/* 241 */     if (name.toString().length() == 0)
/* 242 */       return msg; 
/* 243 */     if (msg.toString().length() == 0) {
/* 244 */       return name;
/*     */     }
/* 246 */     return (String)name + ": " + (String)msg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String js_toSource(Context cx, Scriptable scope, Scriptable thisObj) {
/* 254 */     Object name = ScriptableObject.getProperty(thisObj, "name");
/* 255 */     Object message = ScriptableObject.getProperty(thisObj, "message");
/* 256 */     Object fileName = ScriptableObject.getProperty(thisObj, "fileName");
/* 257 */     Object lineNumber = ScriptableObject.getProperty(thisObj, "lineNumber");
/*     */     
/* 259 */     StringBuilder sb = new StringBuilder();
/* 260 */     sb.append("(new ");
/* 261 */     if (name == NOT_FOUND) {
/* 262 */       name = Undefined.instance;
/*     */     }
/* 264 */     sb.append(ScriptRuntime.toString(name));
/* 265 */     sb.append("(");
/* 266 */     if (message != NOT_FOUND || fileName != NOT_FOUND || lineNumber != NOT_FOUND) {
/*     */ 
/*     */ 
/*     */       
/* 270 */       if (message == NOT_FOUND) {
/* 271 */         message = "";
/*     */       }
/* 273 */       sb.append(ScriptRuntime.uneval(cx, scope, message));
/* 274 */       if (fileName != NOT_FOUND || lineNumber != NOT_FOUND) {
/* 275 */         sb.append(", ");
/* 276 */         if (fileName == NOT_FOUND) {
/* 277 */           fileName = "";
/*     */         }
/* 279 */         sb.append(ScriptRuntime.uneval(cx, scope, fileName));
/* 280 */         if (lineNumber != NOT_FOUND) {
/* 281 */           int line = ScriptRuntime.toInt32(lineNumber);
/* 282 */           if (line != 0) {
/* 283 */             sb.append(", ");
/* 284 */             sb.append(ScriptRuntime.toString(line));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 289 */     sb.append("))");
/* 290 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private static void js_captureStackTrace(Context cx, Scriptable thisObj, Object[] args) {
/* 295 */     ScriptableObject obj = (ScriptableObject)ScriptRuntime.toObjectOrNull(cx, args[0], thisObj);
/* 296 */     Function func = null;
/* 297 */     if (args.length > 1) {
/* 298 */       func = (Function)ScriptRuntime.toObjectOrNull(cx, args[1], thisObj);
/*     */     }
/*     */ 
/*     */     
/* 302 */     NativeError err = (NativeError)cx.newObject(thisObj, "Error");
/*     */     
/* 304 */     err.setStackProvider(new EvaluatorException("[object Object]"));
/*     */ 
/*     */     
/* 307 */     if (func != null) {
/* 308 */       Object funcName = func.get("name", func);
/* 309 */       if (funcName != null && !Undefined.instance.equals(funcName)) {
/* 310 */         err.associateValue("_stackHide", Context.toString(funcName));
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 317 */     obj.defineProperty("stack", err, ERROR_DELEGATE_GET_STACK, ERROR_DELEGATE_SET_STACK, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int findPrototypeId(String s) {
/* 327 */     int id = 0; String X = null;
/* 328 */     int s_length = s.length();
/* 329 */     if (s_length == 8)
/* 330 */     { int c = s.charAt(3);
/* 331 */       if (c == 111) { X = "toSource"; id = 3; }
/* 332 */       else if (c == 116) { X = "toString"; id = 2; }
/*     */        }
/* 334 */     else if (s_length == 11) { X = "constructor"; id = 1; }
/* 335 */      if (X != null && X != s && !X.equals(s)) id = 0;
/*     */ 
/*     */ 
/*     */     
/* 339 */     return id;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class ProtoProps
/*     */     implements Serializable
/*     */   {
/*     */     static final String KEY = "_ErrorPrototypeProps";
/*     */ 
/*     */     
/*     */     static final Method GET_STACK_LIMIT;
/*     */ 
/*     */     
/*     */     static final Method SET_STACK_LIMIT;
/*     */ 
/*     */     
/*     */     static final Method GET_PREPARE_STACK;
/*     */ 
/*     */     
/*     */     static final Method SET_PREPARE_STACK;
/*     */ 
/*     */     
/*     */     private static final long serialVersionUID = 1907180507775337939L;
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/*     */       try {
/* 368 */         GET_STACK_LIMIT = ProtoProps.class.getMethod("getStackTraceLimit", new Class[] { Scriptable.class });
/* 369 */         SET_STACK_LIMIT = ProtoProps.class.getMethod("setStackTraceLimit", new Class[] { Scriptable.class, Object.class });
/* 370 */         GET_PREPARE_STACK = ProtoProps.class.getMethod("getPrepareStackTrace", new Class[] { Scriptable.class });
/* 371 */         SET_PREPARE_STACK = ProtoProps.class.getMethod("setPrepareStackTrace", new Class[] { Scriptable.class, Object.class });
/* 372 */       } catch (NoSuchMethodException nsm) {
/* 373 */         throw new RuntimeException(nsm);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 379 */     private int stackTraceLimit = -1;
/*     */     private Function prepareStackTrace;
/*     */     
/*     */     public Object getStackTraceLimit(Scriptable thisObj) {
/* 383 */       if (this.stackTraceLimit >= 0) {
/* 384 */         return Integer.valueOf(this.stackTraceLimit);
/*     */       }
/* 386 */       return Double.valueOf(Double.POSITIVE_INFINITY);
/*     */     }
/*     */ 
/*     */     
/*     */     public int getStackTraceLimit() {
/* 391 */       return this.stackTraceLimit;
/*     */     }
/*     */     
/*     */     public void setStackTraceLimit(Scriptable thisObj, Object value) {
/* 395 */       double limit = Context.toNumber(value);
/* 396 */       if (Double.isNaN(limit) || Double.isInfinite(limit)) {
/* 397 */         this.stackTraceLimit = -1;
/*     */       } else {
/* 399 */         this.stackTraceLimit = (int)limit;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Object getPrepareStackTrace(Scriptable thisObj) {
/* 405 */       Object ps = getPrepareStackTrace();
/* 406 */       return (ps == null) ? Undefined.instance : ps;
/*     */     }
/*     */     
/*     */     public Function getPrepareStackTrace() {
/* 410 */       return this.prepareStackTrace;
/*     */     }
/*     */     
/*     */     public void setPrepareStackTrace(Scriptable thisObj, Object value) {
/* 414 */       if (value == null || Undefined.instance.equals(value)) {
/* 415 */         this.prepareStackTrace = null;
/* 416 */       } else if (value instanceof Function) {
/* 417 */         this.prepareStackTrace = (Function)value;
/*     */       } 
/*     */     }
/*     */     
/*     */     private ProtoProps() {}
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\NativeError.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */