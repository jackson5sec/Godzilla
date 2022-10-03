/*     */ package org.mozilla.javascript;
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
/*     */ class NativeScript
/*     */   extends BaseFunction
/*     */ {
/*     */   static final long serialVersionUID = -6795101161980121700L;
/*  27 */   private static final Object SCRIPT_TAG = "Script"; private static final int Id_constructor = 1; private static final int Id_toString = 2;
/*     */   private static final int Id_compile = 3;
/*     */   
/*     */   static void init(Scriptable scope, boolean sealed) {
/*  31 */     NativeScript obj = new NativeScript(null);
/*  32 */     obj.exportAsJSClass(4, scope, sealed);
/*     */   }
/*     */   private static final int Id_exec = 4; private static final int MAX_PROTOTYPE_ID = 4; private Script script;
/*     */   
/*     */   private NativeScript(Script script) {
/*  37 */     this.script = script;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  46 */     return "Script";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/*  53 */     if (this.script != null) {
/*  54 */       return this.script.exec(cx, scope);
/*     */     }
/*  56 */     return Undefined.instance;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Scriptable construct(Context cx, Scriptable scope, Object[] args) {
/*  62 */     throw Context.reportRuntimeError0("msg.script.is.not.constructor");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLength() {
/*  68 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getArity() {
/*  74 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   String decompile(int indent, int flags) {
/*  80 */     if (this.script instanceof NativeFunction) {
/*  81 */       return ((NativeFunction)this.script).decompile(indent, flags);
/*     */     }
/*  83 */     return super.decompile(indent, flags);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initPrototypeId(int id) {
/*     */     String s;
/*     */     int arity;
/*  91 */     switch (id) { case 1:
/*  92 */         arity = 1; s = "constructor"; break;
/*  93 */       case 2: arity = 0; s = "toString"; break;
/*  94 */       case 4: arity = 0; s = "exec"; break;
/*  95 */       case 3: arity = 1; s = "compile"; break;
/*  96 */       default: throw new IllegalArgumentException(String.valueOf(id)); }
/*     */     
/*  98 */     initPrototypeMethod(SCRIPT_TAG, id, s, arity);
/*     */   } public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/*     */     String source;
/*     */     NativeScript real;
/*     */     Script script, realScript;
/*     */     String str1;
/*     */     NativeScript nscript;
/* 105 */     if (!f.hasTag(SCRIPT_TAG)) {
/* 106 */       return super.execIdCall(f, cx, scope, thisObj, args);
/*     */     }
/* 108 */     int id = f.methodId();
/* 109 */     switch (id) {
/*     */       case 1:
/* 111 */         source = (args.length == 0) ? "" : ScriptRuntime.toString(args[0]);
/*     */ 
/*     */         
/* 114 */         script = compile(cx, source);
/* 115 */         nscript = new NativeScript(script);
/* 116 */         ScriptRuntime.setObjectProtoAndParent(nscript, scope);
/* 117 */         return nscript;
/*     */ 
/*     */       
/*     */       case 2:
/* 121 */         real = realThis(thisObj, f);
/* 122 */         realScript = real.script;
/* 123 */         if (realScript == null) return ""; 
/* 124 */         return cx.decompileScript(realScript, 0);
/*     */ 
/*     */       
/*     */       case 4:
/* 128 */         throw Context.reportRuntimeError1("msg.cant.call.indirect", "exec");
/*     */ 
/*     */ 
/*     */       
/*     */       case 3:
/* 133 */         real = realThis(thisObj, f);
/* 134 */         str1 = ScriptRuntime.toString(args, 0);
/* 135 */         real.script = compile(cx, str1);
/* 136 */         return real;
/*     */     } 
/*     */     
/* 139 */     throw new IllegalArgumentException(String.valueOf(id));
/*     */   }
/*     */ 
/*     */   
/*     */   private static NativeScript realThis(Scriptable thisObj, IdFunctionObject f) {
/* 144 */     if (!(thisObj instanceof NativeScript))
/* 145 */       throw incompatibleCallError(f); 
/* 146 */     return (NativeScript)thisObj;
/*     */   }
/*     */ 
/*     */   
/*     */   private static Script compile(Context cx, String source) {
/* 151 */     int[] linep = { 0 };
/* 152 */     String filename = Context.getSourcePositionFromStack(linep);
/* 153 */     if (filename == null) {
/* 154 */       filename = "<Script object>";
/* 155 */       linep[0] = 1;
/*     */     } 
/*     */     
/* 158 */     ErrorReporter reporter = DefaultErrorReporter.forEval(cx.getErrorReporter());
/* 159 */     return cx.compileString(source, null, reporter, filename, linep[0], null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int findPrototypeId(String s) {
/* 170 */     int id = 0; String X = null;
/* 171 */     switch (s.length()) { case 4:
/* 172 */         X = "exec"; id = 4; break;
/* 173 */       case 7: X = "compile"; id = 3; break;
/* 174 */       case 8: X = "toString"; id = 2; break;
/* 175 */       case 11: X = "constructor"; id = 1; break; }
/*     */     
/* 177 */     if (X != null && X != s && !X.equals(s)) id = 0;
/*     */ 
/*     */ 
/*     */     
/* 181 */     return id;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\NativeScript.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */