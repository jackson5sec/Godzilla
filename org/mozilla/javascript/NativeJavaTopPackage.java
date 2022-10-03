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
/*     */ public class NativeJavaTopPackage
/*     */   extends NativeJavaPackage
/*     */   implements Function, IdFunctionCall
/*     */ {
/*     */   static final long serialVersionUID = -1455787259477709999L;
/*  29 */   private static final String[][] commonPackages = new String[][] { { "java", "lang", "reflect" }, { "java", "io" }, { "java", "math" }, { "java", "net" }, { "java", "util", "zip" }, { "java", "text", "resources" }, { "java", "applet" }, { "javax", "swing" } };
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
/*     */   NativeJavaTopPackage(ClassLoader loader) {
/*  42 */     super(true, "", loader);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/*  48 */     return construct(cx, scope, args);
/*     */   }
/*     */ 
/*     */   
/*     */   public Scriptable construct(Context cx, Scriptable scope, Object[] args) {
/*  53 */     ClassLoader loader = null;
/*  54 */     if (args.length != 0) {
/*  55 */       Object arg = args[0];
/*  56 */       if (arg instanceof Wrapper) {
/*  57 */         arg = ((Wrapper)arg).unwrap();
/*     */       }
/*  59 */       if (arg instanceof ClassLoader) {
/*  60 */         loader = (ClassLoader)arg;
/*     */       }
/*     */     } 
/*  63 */     if (loader == null) {
/*  64 */       Context.reportRuntimeError0("msg.not.classloader");
/*  65 */       return null;
/*     */     } 
/*  67 */     NativeJavaPackage pkg = new NativeJavaPackage(true, "", loader);
/*  68 */     ScriptRuntime.setObjectProtoAndParent(pkg, scope);
/*  69 */     return pkg;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void init(Context cx, Scriptable scope, boolean sealed) {
/*  74 */     ClassLoader loader = cx.getApplicationClassLoader();
/*  75 */     NativeJavaTopPackage top = new NativeJavaTopPackage(loader);
/*  76 */     top.setPrototype(getObjectPrototype(scope));
/*  77 */     top.setParentScope(scope);
/*     */     
/*  79 */     for (int i = 0; i != commonPackages.length; i++) {
/*  80 */       NativeJavaPackage parent = top;
/*  81 */       for (int m = 0; m != (commonPackages[i]).length; m++) {
/*  82 */         parent = parent.forcePackage(commonPackages[i][m], scope);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  87 */     IdFunctionObject getClass = new IdFunctionObject(top, FTAG, 1, "getClass", 1, scope);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  93 */     String[] topNames = ScriptRuntime.getTopPackageNames();
/*  94 */     NativeJavaPackage[] topPackages = new NativeJavaPackage[topNames.length];
/*  95 */     for (int j = 0; j < topNames.length; j++) {
/*  96 */       topPackages[j] = (NativeJavaPackage)top.get(topNames[j], top);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 101 */     ScriptableObject global = (ScriptableObject)scope;
/*     */     
/* 103 */     if (sealed) {
/* 104 */       getClass.sealObject();
/*     */     }
/* 106 */     getClass.exportAsScopeProperty();
/* 107 */     global.defineProperty("Packages", top, 2);
/* 108 */     for (int k = 0; k < topNames.length; k++) {
/* 109 */       global.defineProperty(topNames[k], topPackages[k], 2);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/* 117 */     if (f.hasTag(FTAG) && 
/* 118 */       f.methodId() == 1) {
/* 119 */       return js_getClass(cx, scope, args);
/*     */     }
/*     */     
/* 122 */     throw f.unknown();
/*     */   }
/*     */ 
/*     */   
/*     */   private Scriptable js_getClass(Context cx, Scriptable scope, Object[] args) {
/* 127 */     if (args.length > 0 && args[0] instanceof Wrapper) {
/* 128 */       Scriptable result = this;
/* 129 */       Class<?> cl = ((Wrapper)args[0]).unwrap().getClass();
/*     */ 
/*     */       
/* 132 */       String name = cl.getName();
/* 133 */       int offset = 0;
/*     */       while (true) {
/* 135 */         int index = name.indexOf('.', offset);
/* 136 */         String propName = (index == -1) ? name.substring(offset) : name.substring(offset, index);
/*     */ 
/*     */         
/* 139 */         Object prop = result.get(propName, result);
/* 140 */         if (!(prop instanceof Scriptable))
/*     */           break; 
/* 142 */         result = (Scriptable)prop;
/* 143 */         if (index == -1)
/* 144 */           return result; 
/* 145 */         offset = index + 1;
/*     */       } 
/*     */     } 
/* 148 */     throw Context.reportRuntimeError0("msg.not.java.obj");
/*     */   }
/*     */   
/* 151 */   private static final Object FTAG = "JavaTopPackage";
/*     */   private static final int Id_getClass = 1;
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\NativeJavaTopPackage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */