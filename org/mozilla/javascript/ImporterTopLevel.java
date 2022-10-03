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
/*     */ public class ImporterTopLevel
/*     */   extends TopLevel
/*     */ {
/*     */   static final long serialVersionUID = -9095380847465315412L;
/*  44 */   private static final Object IMPORTER_TAG = "Importer";
/*     */   private static final int Id_constructor = 1;
/*     */   private static final int Id_importClass = 2;
/*     */   
/*     */   public ImporterTopLevel(Context cx) {
/*  49 */     this(cx, false);
/*     */   }
/*     */   private static final int Id_importPackage = 3; private static final int MAX_PROTOTYPE_ID = 3;
/*     */   public ImporterTopLevel() {}
/*     */   public ImporterTopLevel(Context cx, boolean sealed) {
/*  54 */     initStandardObjects(cx, sealed);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  60 */     return this.topScopeFlag ? "global" : "JavaImporter";
/*     */   }
/*     */ 
/*     */   
/*     */   public static void init(Context cx, Scriptable scope, boolean sealed) {
/*  65 */     ImporterTopLevel obj = new ImporterTopLevel();
/*  66 */     obj.exportAsJSClass(3, scope, sealed);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initStandardObjects(Context cx, boolean sealed) {
/*  73 */     cx.initStandardObjects(this, sealed);
/*  74 */     this.topScopeFlag = true;
/*     */ 
/*     */ 
/*     */     
/*  78 */     IdFunctionObject ctor = exportAsJSClass(3, this, false);
/*  79 */     if (sealed) {
/*  80 */       ctor.sealObject();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  85 */     delete("constructor");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean has(String name, Scriptable start) {
/*  90 */     return (super.has(name, start) || getPackageProperty(name, start) != NOT_FOUND);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object get(String name, Scriptable start) {
/*  96 */     Object result = super.get(name, start);
/*  97 */     if (result != NOT_FOUND)
/*  98 */       return result; 
/*  99 */     result = getPackageProperty(name, start);
/* 100 */     return result;
/*     */   }
/*     */   
/*     */   private Object getPackageProperty(String name, Scriptable start) {
/* 104 */     Object elements[], result = NOT_FOUND;
/*     */     
/* 106 */     synchronized (this.importedPackages) {
/* 107 */       elements = this.importedPackages.toArray();
/*     */     } 
/* 109 */     for (int i = 0; i < elements.length; i++) {
/* 110 */       NativeJavaPackage p = (NativeJavaPackage)elements[i];
/* 111 */       Object v = p.getPkgProperty(name, start, false);
/* 112 */       if (v != null && !(v instanceof NativeJavaPackage)) {
/* 113 */         if (result == NOT_FOUND) {
/* 114 */           result = v;
/*     */         } else {
/* 116 */           throw Context.reportRuntimeError2("msg.ambig.import", result.toString(), v.toString());
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 121 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void importPackage(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
/* 131 */     js_importPackage(args);
/*     */   }
/*     */ 
/*     */   
/*     */   private Object js_construct(Scriptable scope, Object[] args) {
/* 136 */     ImporterTopLevel result = new ImporterTopLevel();
/* 137 */     for (int i = 0; i != args.length; i++) {
/* 138 */       Object arg = args[i];
/* 139 */       if (arg instanceof NativeJavaClass) {
/* 140 */         result.importClass((NativeJavaClass)arg);
/* 141 */       } else if (arg instanceof NativeJavaPackage) {
/* 142 */         result.importPackage((NativeJavaPackage)arg);
/*     */       } else {
/* 144 */         throw Context.reportRuntimeError1("msg.not.class.not.pkg", Context.toString(arg));
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 153 */     result.setParentScope(scope);
/* 154 */     result.setPrototype(this);
/* 155 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   private Object js_importClass(Object[] args) {
/* 160 */     for (int i = 0; i != args.length; i++) {
/* 161 */       Object arg = args[i];
/* 162 */       if (!(arg instanceof NativeJavaClass)) {
/* 163 */         throw Context.reportRuntimeError1("msg.not.class", Context.toString(arg));
/*     */       }
/*     */       
/* 166 */       importClass((NativeJavaClass)arg);
/*     */     } 
/* 168 */     return Undefined.instance;
/*     */   }
/*     */ 
/*     */   
/*     */   private Object js_importPackage(Object[] args) {
/* 173 */     for (int i = 0; i != args.length; i++) {
/* 174 */       Object arg = args[i];
/* 175 */       if (!(arg instanceof NativeJavaPackage)) {
/* 176 */         throw Context.reportRuntimeError1("msg.not.pkg", Context.toString(arg));
/*     */       }
/*     */       
/* 179 */       importPackage((NativeJavaPackage)arg);
/*     */     } 
/* 181 */     return Undefined.instance;
/*     */   }
/*     */ 
/*     */   
/*     */   private void importPackage(NativeJavaPackage pkg) {
/* 186 */     if (pkg == null) {
/*     */       return;
/*     */     }
/* 189 */     synchronized (this.importedPackages) {
/* 190 */       for (int j = 0; j != this.importedPackages.size(); j++) {
/* 191 */         if (pkg.equals(this.importedPackages.get(j))) {
/*     */           return;
/*     */         }
/*     */       } 
/* 195 */       this.importedPackages.add(pkg);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void importClass(NativeJavaClass cl) {
/* 201 */     String s = cl.getClassObject().getName();
/* 202 */     String n = s.substring(s.lastIndexOf('.') + 1);
/* 203 */     Object val = get(n, this);
/* 204 */     if (val != NOT_FOUND && val != cl) {
/* 205 */       throw Context.reportRuntimeError1("msg.prop.defined", n);
/*     */     }
/*     */     
/* 208 */     put(n, this, cl);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initPrototypeId(int id) {
/*     */     String s;
/*     */     int arity;
/* 216 */     switch (id) { case 1:
/* 217 */         arity = 0; s = "constructor"; break;
/* 218 */       case 2: arity = 1; s = "importClass"; break;
/* 219 */       case 3: arity = 1; s = "importPackage"; break;
/* 220 */       default: throw new IllegalArgumentException(String.valueOf(id)); }
/*     */     
/* 222 */     initPrototypeMethod(IMPORTER_TAG, id, s, arity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/* 229 */     if (!f.hasTag(IMPORTER_TAG)) {
/* 230 */       return super.execIdCall(f, cx, scope, thisObj, args);
/*     */     }
/* 232 */     int id = f.methodId();
/* 233 */     switch (id) {
/*     */       case 1:
/* 235 */         return js_construct(scope, args);
/*     */       
/*     */       case 2:
/* 238 */         return realThis(thisObj, f).js_importClass(args);
/*     */       
/*     */       case 3:
/* 241 */         return realThis(thisObj, f).js_importPackage(args);
/*     */     } 
/* 243 */     throw new IllegalArgumentException(String.valueOf(id));
/*     */   }
/*     */ 
/*     */   
/*     */   private ImporterTopLevel realThis(Scriptable thisObj, IdFunctionObject f) {
/* 248 */     if (this.topScopeFlag)
/*     */     {
/*     */       
/* 251 */       return this;
/*     */     }
/* 253 */     if (!(thisObj instanceof ImporterTopLevel))
/* 254 */       throw incompatibleCallError(f); 
/* 255 */     return (ImporterTopLevel)thisObj;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int findPrototypeId(String s) {
/* 265 */     int id = 0; String X = null;
/* 266 */     int s_length = s.length();
/* 267 */     if (s_length == 11)
/* 268 */     { int c = s.charAt(0);
/* 269 */       if (c == 99) { X = "constructor"; id = 1; }
/* 270 */       else if (c == 105) { X = "importClass"; id = 2; }
/*     */        }
/* 272 */     else if (s_length == 13) { X = "importPackage"; id = 3; }
/* 273 */      if (X != null && X != s && !X.equals(s)) id = 0;
/*     */ 
/*     */ 
/*     */     
/* 277 */     return id;
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
/* 288 */   private ObjArray importedPackages = new ObjArray();
/*     */   private boolean topScopeFlag;
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ImporterTopLevel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */