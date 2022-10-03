/*     */ package org.mozilla.javascript;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Map;
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
/*     */ public class NativeJavaClass
/*     */   extends NativeJavaObject
/*     */   implements Function
/*     */ {
/*     */   static final long serialVersionUID = -6460763940409461664L;
/*     */   static final String javaClassPropertyName = "__javaObject__";
/*     */   private Map<String, FieldAndMethods> staticFieldAndMethods;
/*     */   
/*     */   public NativeJavaClass() {}
/*     */   
/*     */   public NativeJavaClass(Scriptable scope, Class<?> cl) {
/*  39 */     this(scope, cl, false);
/*     */   }
/*     */   
/*     */   public NativeJavaClass(Scriptable scope, Class<?> cl, boolean isAdapter) {
/*  43 */     super(scope, cl, (Class<?>)null, isAdapter);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void initMembers() {
/*  48 */     Class<?> cl = (Class)this.javaObject;
/*  49 */     this.members = JavaMembers.lookupClass(this.parent, cl, cl, this.isAdapter);
/*  50 */     this.staticFieldAndMethods = this.members.getFieldAndMethodsObjects(this, cl, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  55 */     return "JavaClass";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean has(String name, Scriptable start) {
/*  60 */     return (this.members.has(name, true) || "__javaObject__".equals(name));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object get(String name, Scriptable start) {
/*  69 */     if (name.equals("prototype")) {
/*  70 */       return null;
/*     */     }
/*  72 */     if (this.staticFieldAndMethods != null) {
/*  73 */       Object result = this.staticFieldAndMethods.get(name);
/*  74 */       if (result != null) {
/*  75 */         return result;
/*     */       }
/*     */     } 
/*  78 */     if (this.members.has(name, true)) {
/*  79 */       return this.members.get(this, name, this.javaObject, true);
/*     */     }
/*     */     
/*  82 */     Context cx = Context.getContext();
/*  83 */     Scriptable scope = ScriptableObject.getTopLevelScope(start);
/*  84 */     WrapFactory wrapFactory = cx.getWrapFactory();
/*     */     
/*  86 */     if ("__javaObject__".equals(name)) {
/*  87 */       return wrapFactory.wrap(cx, scope, this.javaObject, ScriptRuntime.ClassClass);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  93 */     Class<?> nestedClass = findNestedClass(getClassObject(), name);
/*  94 */     if (nestedClass != null) {
/*  95 */       Scriptable nestedValue = wrapFactory.wrapJavaClass(cx, scope, nestedClass);
/*     */       
/*  97 */       nestedValue.setParentScope(this);
/*  98 */       return nestedValue;
/*     */     } 
/*     */     
/* 101 */     throw this.members.reportMemberNotFound(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public void put(String name, Scriptable start, Object value) {
/* 106 */     this.members.put(this, name, this.javaObject, value, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] getIds() {
/* 111 */     return this.members.getIds(true);
/*     */   }
/*     */   
/*     */   public Class<?> getClassObject() {
/* 115 */     return (Class)unwrap();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getDefaultValue(Class<?> hint) {
/* 120 */     if (hint == null || hint == ScriptRuntime.StringClass)
/* 121 */       return toString(); 
/* 122 */     if (hint == ScriptRuntime.BooleanClass)
/* 123 */       return Boolean.TRUE; 
/* 124 */     if (hint == ScriptRuntime.NumberClass)
/* 125 */       return ScriptRuntime.NaNobj; 
/* 126 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/* 135 */     if (args.length == 1 && args[0] instanceof Scriptable) {
/* 136 */       Class<?> c = getClassObject();
/* 137 */       Scriptable p = (Scriptable)args[0];
/*     */       do {
/* 139 */         if (p instanceof Wrapper) {
/* 140 */           Object o = ((Wrapper)p).unwrap();
/* 141 */           if (c.isInstance(o))
/* 142 */             return p; 
/*     */         } 
/* 144 */         p = p.getPrototype();
/* 145 */       } while (p != null);
/*     */     } 
/* 147 */     return construct(cx, scope, args);
/*     */   }
/*     */ 
/*     */   
/*     */   public Scriptable construct(Context cx, Scriptable scope, Object[] args) {
/* 152 */     Class<?> classObject = getClassObject();
/* 153 */     int modifiers = classObject.getModifiers();
/* 154 */     if (!Modifier.isInterface(modifiers) && !Modifier.isAbstract(modifiers)) {
/*     */ 
/*     */       
/* 157 */       NativeJavaMethod ctors = this.members.ctors;
/* 158 */       int index = ctors.findCachedFunction(cx, args);
/* 159 */       if (index < 0) {
/* 160 */         String sig = NativeJavaMethod.scriptSignature(args);
/* 161 */         throw Context.reportRuntimeError2("msg.no.java.ctor", classObject.getName(), sig);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 166 */       return constructSpecific(cx, scope, args, ctors.methods[index]);
/*     */     } 
/* 168 */     if (args.length == 0) {
/* 169 */       throw Context.reportRuntimeError0("msg.adapter.zero.args");
/*     */     }
/* 171 */     Scriptable topLevel = ScriptableObject.getTopLevelScope(this);
/* 172 */     String msg = "";
/*     */ 
/*     */     
/*     */     try {
/* 176 */       if ("Dalvik".equals(System.getProperty("java.vm.name")) && classObject.isInterface()) {
/*     */         
/* 178 */         Object obj = createInterfaceAdapter(classObject, ScriptableObject.ensureScriptableObject(args[0]));
/*     */         
/* 180 */         return cx.getWrapFactory().wrapAsJavaObject(cx, scope, obj, null);
/*     */       } 
/*     */ 
/*     */       
/* 184 */       Object v = topLevel.get("JavaAdapter", topLevel);
/* 185 */       if (v != NOT_FOUND) {
/* 186 */         Function f = (Function)v;
/*     */         
/* 188 */         Object[] adapterArgs = { this, args[0] };
/* 189 */         return f.construct(cx, topLevel, adapterArgs);
/*     */       } 
/* 191 */     } catch (Exception ex) {
/*     */       
/* 193 */       String m = ex.getMessage();
/* 194 */       if (m != null)
/* 195 */         msg = m; 
/*     */     } 
/* 197 */     throw Context.reportRuntimeError2("msg.cant.instantiate", msg, classObject.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Scriptable constructSpecific(Context cx, Scriptable scope, Object[] args, MemberBox ctor) {
/* 205 */     Object instance = constructInternal(args, ctor);
/*     */ 
/*     */     
/* 208 */     Scriptable topLevel = ScriptableObject.getTopLevelScope(scope);
/* 209 */     return cx.getWrapFactory().wrapNewObject(cx, topLevel, instance);
/*     */   }
/*     */ 
/*     */   
/*     */   static Object constructInternal(Object[] args, MemberBox ctor) {
/* 214 */     Class<?>[] argTypes = ctor.argTypes;
/*     */     
/* 216 */     if (ctor.vararg) {
/*     */       
/* 218 */       Object varArgs, newArgs[] = new Object[argTypes.length];
/* 219 */       for (int i = 0; i < argTypes.length - 1; i++) {
/* 220 */         newArgs[i] = Context.jsToJava(args[i], argTypes[i]);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 227 */       if (args.length == argTypes.length && (args[args.length - 1] == null || args[args.length - 1] instanceof NativeArray || args[args.length - 1] instanceof NativeJavaArray)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 233 */         varArgs = Context.jsToJava(args[args.length - 1], argTypes[argTypes.length - 1]);
/*     */       }
/*     */       else {
/*     */         
/* 237 */         Class<?> componentType = argTypes[argTypes.length - 1].getComponentType();
/*     */         
/* 239 */         varArgs = Array.newInstance(componentType, args.length - argTypes.length + 1);
/*     */         
/* 241 */         for (int j = 0; j < Array.getLength(varArgs); j++) {
/* 242 */           Object value = Context.jsToJava(args[argTypes.length - 1 + j], componentType);
/*     */           
/* 244 */           Array.set(varArgs, j, value);
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 249 */       newArgs[argTypes.length - 1] = varArgs;
/*     */       
/* 251 */       args = newArgs;
/*     */     } else {
/* 253 */       Object[] origArgs = args;
/* 254 */       for (int i = 0; i < args.length; i++) {
/* 255 */         Object arg = args[i];
/* 256 */         Object x = Context.jsToJava(arg, argTypes[i]);
/* 257 */         if (x != arg) {
/* 258 */           if (args == origArgs) {
/* 259 */             args = (Object[])origArgs.clone();
/*     */           }
/* 261 */           args[i] = x;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 266 */     return ctor.newInstance(args);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 271 */     return "[JavaClass " + getClassObject().getName() + "]";
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
/*     */   public boolean hasInstance(Scriptable value) {
/* 285 */     if (value instanceof Wrapper && !(value instanceof NativeJavaClass)) {
/*     */       
/* 287 */       Object instance = ((Wrapper)value).unwrap();
/*     */       
/* 289 */       return getClassObject().isInstance(instance);
/*     */     } 
/*     */ 
/*     */     
/* 293 */     return false;
/*     */   }
/*     */   
/*     */   private static Class<?> findNestedClass(Class<?> parentClass, String name) {
/* 297 */     String nestedClassName = parentClass.getName() + '$' + name;
/* 298 */     ClassLoader loader = parentClass.getClassLoader();
/* 299 */     if (loader == null)
/*     */     {
/*     */ 
/*     */ 
/*     */       
/* 304 */       return Kit.classOrNull(nestedClassName);
/*     */     }
/* 306 */     return Kit.classOrNull(loader, nestedClassName);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\NativeJavaClass.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */