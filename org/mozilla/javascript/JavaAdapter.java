/*      */ package org.mozilla.javascript;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.security.CodeSource;
/*      */ import java.security.ProtectionDomain;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashSet;
/*      */ import java.util.Map;
/*      */ import org.mozilla.classfile.ClassFileWriter;
/*      */ 
/*      */ 
/*      */ public final class JavaAdapter
/*      */   implements IdFunctionCall
/*      */ {
/*      */   static class JavaAdapterSignature
/*      */   {
/*      */     Class<?> superClass;
/*      */     Class<?>[] interfaces;
/*      */     ObjToIntMap names;
/*      */     
/*      */     JavaAdapterSignature(Class<?> superClass, Class<?>[] interfaces, ObjToIntMap names) {
/*   30 */       this.superClass = superClass;
/*   31 */       this.interfaces = interfaces;
/*   32 */       this.names = names;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(Object obj) {
/*   38 */       if (!(obj instanceof JavaAdapterSignature))
/*   39 */         return false; 
/*   40 */       JavaAdapterSignature sig = (JavaAdapterSignature)obj;
/*   41 */       if (this.superClass != sig.superClass)
/*   42 */         return false; 
/*   43 */       if (this.interfaces != sig.interfaces) {
/*   44 */         if (this.interfaces.length != sig.interfaces.length)
/*   45 */           return false; 
/*   46 */         for (int i = 0; i < this.interfaces.length; i++) {
/*   47 */           if (this.interfaces[i] != sig.interfaces[i])
/*   48 */             return false; 
/*      */         } 
/*   50 */       }  if (this.names.size() != sig.names.size())
/*   51 */         return false; 
/*   52 */       ObjToIntMap.Iterator iter = new ObjToIntMap.Iterator(this.names);
/*   53 */       iter.start(); for (; !iter.done(); iter.next()) {
/*   54 */         String name = (String)iter.getKey();
/*   55 */         int arity = iter.getValue();
/*   56 */         if (arity != sig.names.get(name, arity + 1))
/*   57 */           return false; 
/*      */       } 
/*   59 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*   65 */       return this.superClass.hashCode() + Arrays.hashCode((Object[])this.interfaces) ^ this.names.size();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static void init(Context cx, Scriptable scope, boolean sealed) {
/*   71 */     JavaAdapter obj = new JavaAdapter();
/*   72 */     IdFunctionObject ctor = new IdFunctionObject(obj, FTAG, 1, "JavaAdapter", 1, scope);
/*      */     
/*   74 */     ctor.markAsConstructor((Scriptable)null);
/*   75 */     if (sealed) {
/*   76 */       ctor.sealObject();
/*      */     }
/*   78 */     ctor.exportAsScopeProperty();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/*   84 */     if (f.hasTag(FTAG) && 
/*   85 */       f.methodId() == 1) {
/*   86 */       return js_createAdapter(cx, scope, args);
/*      */     }
/*      */     
/*   89 */     throw f.unknown();
/*      */   }
/*      */ 
/*      */   
/*      */   public static Object convertResult(Object result, Class<?> c) {
/*   94 */     if (result == Undefined.instance && c != ScriptRuntime.ObjectClass && c != ScriptRuntime.StringClass)
/*      */     {
/*      */ 
/*      */ 
/*      */       
/*   99 */       return null;
/*      */     }
/*  101 */     return Context.jsToJava(result, c);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Scriptable createAdapterWrapper(Scriptable obj, Object adapter) {
/*  106 */     Scriptable scope = ScriptableObject.getTopLevelScope(obj);
/*  107 */     NativeJavaObject res = new NativeJavaObject(scope, adapter, null, true);
/*  108 */     res.setPrototype(obj);
/*  109 */     return res;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object getAdapterSelf(Class<?> adapterClass, Object adapter) throws NoSuchFieldException, IllegalAccessException {
/*  115 */     Field self = adapterClass.getDeclaredField("self");
/*  116 */     return self.get(adapter);
/*      */   }
/*      */ 
/*      */   
/*      */   static Object js_createAdapter(Context cx, Scriptable scope, Object[] args) {
/*  121 */     int N = args.length;
/*  122 */     if (N == 0) {
/*  123 */       throw ScriptRuntime.typeError0("msg.adapter.zero.args");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int classCount;
/*      */ 
/*      */ 
/*      */     
/*  133 */     for (classCount = 0; classCount < N - 1; classCount++) {
/*  134 */       Object arg = args[classCount];
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  139 */       if (arg instanceof NativeObject) {
/*      */         break;
/*      */       }
/*  142 */       if (!(arg instanceof NativeJavaClass)) {
/*  143 */         throw ScriptRuntime.typeError2("msg.not.java.class.arg", String.valueOf(classCount), ScriptRuntime.toString(arg));
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  148 */     Class<?> superClass = null;
/*  149 */     Class<?>[] intfs = new Class[classCount];
/*  150 */     int interfaceCount = 0;
/*  151 */     for (int i = 0; i < classCount; i++) {
/*  152 */       Class<?> c = ((NativeJavaClass)args[i]).getClassObject();
/*  153 */       if (!c.isInterface()) {
/*  154 */         if (superClass != null) {
/*  155 */           throw ScriptRuntime.typeError2("msg.only.one.super", superClass.getName(), c.getName());
/*      */         }
/*      */         
/*  158 */         superClass = c;
/*      */       } else {
/*  160 */         intfs[interfaceCount++] = c;
/*      */       } 
/*      */     } 
/*      */     
/*  164 */     if (superClass == null) {
/*  165 */       superClass = ScriptRuntime.ObjectClass;
/*      */     }
/*      */     
/*  168 */     Class<?>[] interfaces = new Class[interfaceCount];
/*  169 */     System.arraycopy(intfs, 0, interfaces, 0, interfaceCount);
/*      */     
/*  171 */     Scriptable obj = ScriptableObject.ensureScriptable(args[classCount]);
/*      */     
/*  173 */     Class<?> adapterClass = getAdapterClass(scope, superClass, interfaces, obj);
/*      */ 
/*      */     
/*  176 */     int argsCount = N - classCount - 1; try {
/*      */       Object adapter;
/*  178 */       if (argsCount > 0) {
/*      */ 
/*      */ 
/*      */         
/*  182 */         Object[] ctorArgs = new Object[argsCount + 2];
/*  183 */         ctorArgs[0] = obj;
/*  184 */         ctorArgs[1] = cx.getFactory();
/*  185 */         System.arraycopy(args, classCount + 1, ctorArgs, 2, argsCount);
/*      */         
/*  187 */         NativeJavaClass classWrapper = new NativeJavaClass(scope, adapterClass, true);
/*      */         
/*  189 */         NativeJavaMethod ctors = classWrapper.members.ctors;
/*  190 */         int index = ctors.findCachedFunction(cx, ctorArgs);
/*  191 */         if (index < 0) {
/*  192 */           String sig = NativeJavaMethod.scriptSignature(args);
/*  193 */           throw Context.reportRuntimeError2("msg.no.java.ctor", adapterClass.getName(), sig);
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/*  198 */         adapter = NativeJavaClass.constructInternal(ctorArgs, ctors.methods[index]);
/*      */       } else {
/*  200 */         Class<?>[] ctorParms = new Class[] { ScriptRuntime.ScriptableClass, ScriptRuntime.ContextFactoryClass };
/*      */ 
/*      */ 
/*      */         
/*  204 */         Object[] ctorArgs = { obj, cx.getFactory() };
/*  205 */         adapter = adapterClass.getConstructor(ctorParms).newInstance(ctorArgs);
/*      */       } 
/*      */       
/*  208 */       Object self = getAdapterSelf(adapterClass, adapter);
/*      */       
/*  210 */       if (self instanceof Wrapper) {
/*  211 */         Object unwrapped = ((Wrapper)self).unwrap();
/*  212 */         if (unwrapped instanceof Scriptable) {
/*  213 */           if (unwrapped instanceof ScriptableObject) {
/*  214 */             ScriptRuntime.setObjectProtoAndParent((ScriptableObject)unwrapped, scope);
/*      */           }
/*      */           
/*  217 */           return unwrapped;
/*      */         } 
/*      */       } 
/*  220 */       return self;
/*  221 */     } catch (Exception ex) {
/*  222 */       throw Context.throwAsScriptRuntimeEx(ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void writeAdapterObject(Object javaObject, ObjectOutputStream out) throws IOException {
/*  231 */     Class<?> cl = javaObject.getClass();
/*  232 */     out.writeObject(cl.getSuperclass().getName());
/*      */     
/*  234 */     Class<?>[] interfaces = cl.getInterfaces();
/*  235 */     String[] interfaceNames = new String[interfaces.length];
/*      */     
/*  237 */     for (int i = 0; i < interfaces.length; i++) {
/*  238 */       interfaceNames[i] = interfaces[i].getName();
/*      */     }
/*  240 */     out.writeObject(interfaceNames);
/*      */ 
/*      */     
/*  243 */     try { Object delegee = cl.getField("delegee").get(javaObject);
/*  244 */       out.writeObject(delegee);
/*      */       return; }
/*  246 */     catch (IllegalAccessException e) {  }
/*  247 */     catch (NoSuchFieldException e) {}
/*      */     
/*  249 */     throw new IOException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object readAdapterObject(Scriptable self, ObjectInputStream in) throws IOException, ClassNotFoundException {
/*      */     ContextFactory factory;
/*  258 */     Context cx = Context.getCurrentContext();
/*  259 */     if (cx != null) {
/*  260 */       factory = cx.getFactory();
/*      */     } else {
/*  262 */       factory = null;
/*      */     } 
/*      */     
/*  265 */     Class<?> superClass = Class.forName((String)in.readObject());
/*      */     
/*  267 */     String[] interfaceNames = (String[])in.readObject();
/*  268 */     Class<?>[] interfaces = new Class[interfaceNames.length];
/*      */     
/*  270 */     for (int i = 0; i < interfaceNames.length; i++) {
/*  271 */       interfaces[i] = Class.forName(interfaceNames[i]);
/*      */     }
/*  273 */     Scriptable delegee = (Scriptable)in.readObject();
/*      */     
/*  275 */     Class<?> adapterClass = getAdapterClass(self, superClass, interfaces, delegee);
/*      */ 
/*      */     
/*  278 */     Class<?>[] ctorParms = new Class[] { ScriptRuntime.ContextFactoryClass, ScriptRuntime.ScriptableClass, ScriptRuntime.ScriptableClass };
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  283 */     Object[] ctorArgs = { factory, delegee, self };
/*      */     
/*  285 */     try { return adapterClass.getConstructor(ctorParms).newInstance(ctorArgs); }
/*  286 */     catch (InstantiationException e) {  }
/*  287 */     catch (IllegalAccessException e) {  }
/*  288 */     catch (InvocationTargetException e) {  }
/*  289 */     catch (NoSuchMethodException e) {}
/*      */ 
/*      */     
/*  292 */     throw new ClassNotFoundException("adapter");
/*      */   }
/*      */ 
/*      */   
/*      */   private static ObjToIntMap getObjectFunctionNames(Scriptable obj) {
/*  297 */     Object[] ids = ScriptableObject.getPropertyIds(obj);
/*  298 */     ObjToIntMap map = new ObjToIntMap(ids.length);
/*  299 */     for (int i = 0; i != ids.length; i++) {
/*  300 */       if (ids[i] instanceof String) {
/*      */         
/*  302 */         String id = (String)ids[i];
/*  303 */         Object value = ScriptableObject.getProperty(obj, id);
/*  304 */         if (value instanceof Function) {
/*  305 */           Function f = (Function)value;
/*  306 */           int length = ScriptRuntime.toInt32(ScriptableObject.getProperty(f, "length"));
/*      */           
/*  308 */           if (length < 0) {
/*  309 */             length = 0;
/*      */           }
/*  311 */           map.put(id, length);
/*      */         } 
/*      */       } 
/*  314 */     }  return map;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static Class<?> getAdapterClass(Scriptable scope, Class<?> superClass, Class<?>[] interfaces, Scriptable obj) {
/*  320 */     ClassCache cache = ClassCache.get(scope);
/*  321 */     Map<JavaAdapterSignature, Class<?>> generated = cache.getInterfaceAdapterCacheMap();
/*      */ 
/*      */     
/*  324 */     ObjToIntMap names = getObjectFunctionNames(obj);
/*      */     
/*  326 */     JavaAdapterSignature sig = new JavaAdapterSignature(superClass, interfaces, names);
/*  327 */     Class<?> adapterClass = generated.get(sig);
/*  328 */     if (adapterClass == null) {
/*  329 */       String adapterName = "adapter" + cache.newClassSerialNumber();
/*  330 */       byte[] code = createAdapterCode(names, adapterName, superClass, interfaces, null);
/*      */ 
/*      */       
/*  333 */       adapterClass = loadAdapterClass(adapterName, code);
/*  334 */       if (cache.isCachingEnabled()) {
/*  335 */         generated.put(sig, adapterClass);
/*      */       }
/*      */     } 
/*  338 */     return adapterClass;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] createAdapterCode(ObjToIntMap functionNames, String adapterName, Class<?> superClass, Class<?>[] interfaces, String scriptClassName) {
/*      */     // Byte code:
/*      */     //   0: new org/mozilla/classfile/ClassFileWriter
/*      */     //   3: dup
/*      */     //   4: aload_1
/*      */     //   5: aload_2
/*      */     //   6: invokevirtual getName : ()Ljava/lang/String;
/*      */     //   9: ldc '<adapter>'
/*      */     //   11: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
/*      */     //   14: astore #5
/*      */     //   16: aload #5
/*      */     //   18: ldc 'factory'
/*      */     //   20: ldc 'Lorg/mozilla/javascript/ContextFactory;'
/*      */     //   22: bipush #17
/*      */     //   24: invokevirtual addField : (Ljava/lang/String;Ljava/lang/String;S)V
/*      */     //   27: aload #5
/*      */     //   29: ldc 'delegee'
/*      */     //   31: ldc 'Lorg/mozilla/javascript/Scriptable;'
/*      */     //   33: bipush #17
/*      */     //   35: invokevirtual addField : (Ljava/lang/String;Ljava/lang/String;S)V
/*      */     //   38: aload #5
/*      */     //   40: ldc 'self'
/*      */     //   42: ldc 'Lorg/mozilla/javascript/Scriptable;'
/*      */     //   44: bipush #17
/*      */     //   46: invokevirtual addField : (Ljava/lang/String;Ljava/lang/String;S)V
/*      */     //   49: aload_3
/*      */     //   50: ifnonnull -> 57
/*      */     //   53: iconst_0
/*      */     //   54: goto -> 59
/*      */     //   57: aload_3
/*      */     //   58: arraylength
/*      */     //   59: istore #6
/*      */     //   61: iconst_0
/*      */     //   62: istore #7
/*      */     //   64: iload #7
/*      */     //   66: iload #6
/*      */     //   68: if_icmpge -> 96
/*      */     //   71: aload_3
/*      */     //   72: iload #7
/*      */     //   74: aaload
/*      */     //   75: ifnull -> 90
/*      */     //   78: aload #5
/*      */     //   80: aload_3
/*      */     //   81: iload #7
/*      */     //   83: aaload
/*      */     //   84: invokevirtual getName : ()Ljava/lang/String;
/*      */     //   87: invokevirtual addInterface : (Ljava/lang/String;)V
/*      */     //   90: iinc #7, 1
/*      */     //   93: goto -> 64
/*      */     //   96: aload_2
/*      */     //   97: invokevirtual getName : ()Ljava/lang/String;
/*      */     //   100: bipush #46
/*      */     //   102: bipush #47
/*      */     //   104: invokevirtual replace : (CC)Ljava/lang/String;
/*      */     //   107: astore #7
/*      */     //   109: aload_2
/*      */     //   110: invokevirtual getDeclaredConstructors : ()[Ljava/lang/reflect/Constructor;
/*      */     //   113: astore #8
/*      */     //   115: aload #8
/*      */     //   117: astore #9
/*      */     //   119: aload #9
/*      */     //   121: arraylength
/*      */     //   122: istore #10
/*      */     //   124: iconst_0
/*      */     //   125: istore #11
/*      */     //   127: iload #11
/*      */     //   129: iload #10
/*      */     //   131: if_icmpge -> 180
/*      */     //   134: aload #9
/*      */     //   136: iload #11
/*      */     //   138: aaload
/*      */     //   139: astore #12
/*      */     //   141: aload #12
/*      */     //   143: invokevirtual getModifiers : ()I
/*      */     //   146: istore #13
/*      */     //   148: iload #13
/*      */     //   150: invokestatic isPublic : (I)Z
/*      */     //   153: ifne -> 164
/*      */     //   156: iload #13
/*      */     //   158: invokestatic isProtected : (I)Z
/*      */     //   161: ifeq -> 174
/*      */     //   164: aload #5
/*      */     //   166: aload_1
/*      */     //   167: aload #7
/*      */     //   169: aload #12
/*      */     //   171: invokestatic generateCtor : (Lorg/mozilla/classfile/ClassFileWriter;Ljava/lang/String;Ljava/lang/String;Ljava/lang/reflect/Constructor;)V
/*      */     //   174: iinc #11, 1
/*      */     //   177: goto -> 127
/*      */     //   180: aload #5
/*      */     //   182: aload_1
/*      */     //   183: aload #7
/*      */     //   185: invokestatic generateSerialCtor : (Lorg/mozilla/classfile/ClassFileWriter;Ljava/lang/String;Ljava/lang/String;)V
/*      */     //   188: aload #4
/*      */     //   190: ifnull -> 203
/*      */     //   193: aload #5
/*      */     //   195: aload_1
/*      */     //   196: aload #7
/*      */     //   198: aload #4
/*      */     //   200: invokestatic generateEmptyCtor : (Lorg/mozilla/classfile/ClassFileWriter;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
/*      */     //   203: new org/mozilla/javascript/ObjToIntMap
/*      */     //   206: dup
/*      */     //   207: invokespecial <init> : ()V
/*      */     //   210: astore #9
/*      */     //   212: new org/mozilla/javascript/ObjToIntMap
/*      */     //   215: dup
/*      */     //   216: invokespecial <init> : ()V
/*      */     //   219: astore #10
/*      */     //   221: iconst_0
/*      */     //   222: istore #11
/*      */     //   224: iload #11
/*      */     //   226: iload #6
/*      */     //   228: if_icmpge -> 406
/*      */     //   231: aload_3
/*      */     //   232: iload #11
/*      */     //   234: aaload
/*      */     //   235: invokevirtual getMethods : ()[Ljava/lang/reflect/Method;
/*      */     //   238: astore #12
/*      */     //   240: iconst_0
/*      */     //   241: istore #13
/*      */     //   243: iload #13
/*      */     //   245: aload #12
/*      */     //   247: arraylength
/*      */     //   248: if_icmpge -> 400
/*      */     //   251: aload #12
/*      */     //   253: iload #13
/*      */     //   255: aaload
/*      */     //   256: astore #14
/*      */     //   258: aload #14
/*      */     //   260: invokevirtual getModifiers : ()I
/*      */     //   263: istore #15
/*      */     //   265: iload #15
/*      */     //   267: invokestatic isStatic : (I)Z
/*      */     //   270: ifne -> 394
/*      */     //   273: iload #15
/*      */     //   275: invokestatic isFinal : (I)Z
/*      */     //   278: ifeq -> 284
/*      */     //   281: goto -> 394
/*      */     //   284: aload #14
/*      */     //   286: invokevirtual getName : ()Ljava/lang/String;
/*      */     //   289: astore #16
/*      */     //   291: aload #14
/*      */     //   293: invokevirtual getParameterTypes : ()[Ljava/lang/Class;
/*      */     //   296: astore #17
/*      */     //   298: aload_0
/*      */     //   299: aload #16
/*      */     //   301: invokevirtual has : (Ljava/lang/Object;)Z
/*      */     //   304: ifne -> 321
/*      */     //   307: aload_2
/*      */     //   308: aload #16
/*      */     //   310: aload #17
/*      */     //   312: invokevirtual getMethod : (Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
/*      */     //   315: pop
/*      */     //   316: goto -> 394
/*      */     //   319: astore #18
/*      */     //   321: aload #14
/*      */     //   323: aload #17
/*      */     //   325: invokestatic getMethodSignature : (Ljava/lang/reflect/Method;[Ljava/lang/Class;)Ljava/lang/String;
/*      */     //   328: astore #18
/*      */     //   330: new java/lang/StringBuilder
/*      */     //   333: dup
/*      */     //   334: invokespecial <init> : ()V
/*      */     //   337: aload #16
/*      */     //   339: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   342: aload #18
/*      */     //   344: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   347: invokevirtual toString : ()Ljava/lang/String;
/*      */     //   350: astore #19
/*      */     //   352: aload #9
/*      */     //   354: aload #19
/*      */     //   356: invokevirtual has : (Ljava/lang/Object;)Z
/*      */     //   359: ifne -> 394
/*      */     //   362: aload #5
/*      */     //   364: aload_1
/*      */     //   365: aload #16
/*      */     //   367: aload #17
/*      */     //   369: aload #14
/*      */     //   371: invokevirtual getReturnType : ()Ljava/lang/Class;
/*      */     //   374: iconst_1
/*      */     //   375: invokestatic generateMethod : (Lorg/mozilla/classfile/ClassFileWriter;Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Class;Ljava/lang/Class;Z)V
/*      */     //   378: aload #9
/*      */     //   380: aload #19
/*      */     //   382: iconst_0
/*      */     //   383: invokevirtual put : (Ljava/lang/Object;I)V
/*      */     //   386: aload #10
/*      */     //   388: aload #16
/*      */     //   390: iconst_0
/*      */     //   391: invokevirtual put : (Ljava/lang/Object;I)V
/*      */     //   394: iinc #13, 1
/*      */     //   397: goto -> 243
/*      */     //   400: iinc #11, 1
/*      */     //   403: goto -> 224
/*      */     //   406: aload_2
/*      */     //   407: invokestatic getOverridableMethods : (Ljava/lang/Class;)[Ljava/lang/reflect/Method;
/*      */     //   410: astore #11
/*      */     //   412: iconst_0
/*      */     //   413: istore #12
/*      */     //   415: iload #12
/*      */     //   417: aload #11
/*      */     //   419: arraylength
/*      */     //   420: if_icmpge -> 575
/*      */     //   423: aload #11
/*      */     //   425: iload #12
/*      */     //   427: aaload
/*      */     //   428: astore #13
/*      */     //   430: aload #13
/*      */     //   432: invokevirtual getModifiers : ()I
/*      */     //   435: istore #14
/*      */     //   437: iload #14
/*      */     //   439: invokestatic isAbstract : (I)Z
/*      */     //   442: istore #15
/*      */     //   444: aload #13
/*      */     //   446: invokevirtual getName : ()Ljava/lang/String;
/*      */     //   449: astore #16
/*      */     //   451: iload #15
/*      */     //   453: ifne -> 465
/*      */     //   456: aload_0
/*      */     //   457: aload #16
/*      */     //   459: invokevirtual has : (Ljava/lang/Object;)Z
/*      */     //   462: ifeq -> 569
/*      */     //   465: aload #13
/*      */     //   467: invokevirtual getParameterTypes : ()[Ljava/lang/Class;
/*      */     //   470: astore #17
/*      */     //   472: aload #13
/*      */     //   474: aload #17
/*      */     //   476: invokestatic getMethodSignature : (Ljava/lang/reflect/Method;[Ljava/lang/Class;)Ljava/lang/String;
/*      */     //   479: astore #18
/*      */     //   481: new java/lang/StringBuilder
/*      */     //   484: dup
/*      */     //   485: invokespecial <init> : ()V
/*      */     //   488: aload #16
/*      */     //   490: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   493: aload #18
/*      */     //   495: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   498: invokevirtual toString : ()Ljava/lang/String;
/*      */     //   501: astore #19
/*      */     //   503: aload #9
/*      */     //   505: aload #19
/*      */     //   507: invokevirtual has : (Ljava/lang/Object;)Z
/*      */     //   510: ifne -> 569
/*      */     //   513: aload #5
/*      */     //   515: aload_1
/*      */     //   516: aload #16
/*      */     //   518: aload #17
/*      */     //   520: aload #13
/*      */     //   522: invokevirtual getReturnType : ()Ljava/lang/Class;
/*      */     //   525: iconst_1
/*      */     //   526: invokestatic generateMethod : (Lorg/mozilla/classfile/ClassFileWriter;Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Class;Ljava/lang/Class;Z)V
/*      */     //   529: aload #9
/*      */     //   531: aload #19
/*      */     //   533: iconst_0
/*      */     //   534: invokevirtual put : (Ljava/lang/Object;I)V
/*      */     //   537: aload #10
/*      */     //   539: aload #16
/*      */     //   541: iconst_0
/*      */     //   542: invokevirtual put : (Ljava/lang/Object;I)V
/*      */     //   545: iload #15
/*      */     //   547: ifne -> 569
/*      */     //   550: aload #5
/*      */     //   552: aload_1
/*      */     //   553: aload #7
/*      */     //   555: aload #16
/*      */     //   557: aload #18
/*      */     //   559: aload #17
/*      */     //   561: aload #13
/*      */     //   563: invokevirtual getReturnType : ()Ljava/lang/Class;
/*      */     //   566: invokestatic generateSuper : (Lorg/mozilla/classfile/ClassFileWriter;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Class;Ljava/lang/Class;)V
/*      */     //   569: iinc #12, 1
/*      */     //   572: goto -> 415
/*      */     //   575: new org/mozilla/javascript/ObjToIntMap$Iterator
/*      */     //   578: dup
/*      */     //   579: aload_0
/*      */     //   580: invokespecial <init> : (Lorg/mozilla/javascript/ObjToIntMap;)V
/*      */     //   583: astore #12
/*      */     //   585: aload #12
/*      */     //   587: invokevirtual start : ()V
/*      */     //   590: aload #12
/*      */     //   592: invokevirtual done : ()Z
/*      */     //   595: ifne -> 681
/*      */     //   598: aload #12
/*      */     //   600: invokevirtual getKey : ()Ljava/lang/Object;
/*      */     //   603: checkcast java/lang/String
/*      */     //   606: astore #13
/*      */     //   608: aload #10
/*      */     //   610: aload #13
/*      */     //   612: invokevirtual has : (Ljava/lang/Object;)Z
/*      */     //   615: ifeq -> 621
/*      */     //   618: goto -> 673
/*      */     //   621: aload #12
/*      */     //   623: invokevirtual getValue : ()I
/*      */     //   626: istore #14
/*      */     //   628: iload #14
/*      */     //   630: anewarray java/lang/Class
/*      */     //   633: astore #15
/*      */     //   635: iconst_0
/*      */     //   636: istore #16
/*      */     //   638: iload #16
/*      */     //   640: iload #14
/*      */     //   642: if_icmpge -> 659
/*      */     //   645: aload #15
/*      */     //   647: iload #16
/*      */     //   649: getstatic org/mozilla/javascript/ScriptRuntime.ObjectClass : Ljava/lang/Class;
/*      */     //   652: aastore
/*      */     //   653: iinc #16, 1
/*      */     //   656: goto -> 638
/*      */     //   659: aload #5
/*      */     //   661: aload_1
/*      */     //   662: aload #13
/*      */     //   664: aload #15
/*      */     //   666: getstatic org/mozilla/javascript/ScriptRuntime.ObjectClass : Ljava/lang/Class;
/*      */     //   669: iconst_0
/*      */     //   670: invokestatic generateMethod : (Lorg/mozilla/classfile/ClassFileWriter;Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Class;Ljava/lang/Class;Z)V
/*      */     //   673: aload #12
/*      */     //   675: invokevirtual next : ()V
/*      */     //   678: goto -> 590
/*      */     //   681: aload #5
/*      */     //   683: invokevirtual toByteArray : ()[B
/*      */     //   686: areturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #347	-> 0
/*      */     //   #350	-> 16
/*      */     //   #353	-> 27
/*      */     //   #356	-> 38
/*      */     //   #359	-> 49
/*      */     //   #360	-> 61
/*      */     //   #361	-> 71
/*      */     //   #362	-> 78
/*      */     //   #360	-> 90
/*      */     //   #365	-> 96
/*      */     //   #366	-> 109
/*      */     //   #367	-> 115
/*      */     //   #368	-> 141
/*      */     //   #369	-> 148
/*      */     //   #370	-> 164
/*      */     //   #367	-> 174
/*      */     //   #373	-> 180
/*      */     //   #374	-> 188
/*      */     //   #375	-> 193
/*      */     //   #378	-> 203
/*      */     //   #379	-> 212
/*      */     //   #382	-> 221
/*      */     //   #383	-> 231
/*      */     //   #384	-> 240
/*      */     //   #385	-> 251
/*      */     //   #386	-> 258
/*      */     //   #387	-> 265
/*      */     //   #388	-> 281
/*      */     //   #390	-> 284
/*      */     //   #391	-> 291
/*      */     //   #392	-> 298
/*      */     //   #394	-> 307
/*      */     //   #398	-> 316
/*      */     //   #399	-> 319
/*      */     //   #405	-> 321
/*      */     //   #406	-> 330
/*      */     //   #407	-> 352
/*      */     //   #408	-> 362
/*      */     //   #410	-> 378
/*      */     //   #411	-> 386
/*      */     //   #384	-> 394
/*      */     //   #382	-> 400
/*      */     //   #420	-> 406
/*      */     //   #421	-> 412
/*      */     //   #422	-> 423
/*      */     //   #423	-> 430
/*      */     //   #427	-> 437
/*      */     //   #428	-> 444
/*      */     //   #429	-> 451
/*      */     //   #432	-> 465
/*      */     //   #433	-> 472
/*      */     //   #434	-> 481
/*      */     //   #435	-> 503
/*      */     //   #436	-> 513
/*      */     //   #438	-> 529
/*      */     //   #439	-> 537
/*      */     //   #443	-> 545
/*      */     //   #444	-> 550
/*      */     //   #421	-> 569
/*      */     //   #454	-> 575
/*      */     //   #455	-> 585
/*      */     //   #456	-> 598
/*      */     //   #457	-> 608
/*      */     //   #458	-> 618
/*      */     //   #459	-> 621
/*      */     //   #460	-> 628
/*      */     //   #461	-> 635
/*      */     //   #462	-> 645
/*      */     //   #461	-> 653
/*      */     //   #463	-> 659
/*      */     //   #455	-> 673
/*      */     //   #466	-> 681
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   64	32	7	i	I
/*      */     //   148	26	13	mod	I
/*      */     //   141	33	12	ctor	Ljava/lang/reflect/Constructor;
/*      */     //   119	61	9	arr$	[Ljava/lang/reflect/Constructor;
/*      */     //   124	56	10	len$	I
/*      */     //   127	53	11	i$	I
/*      */     //   321	0	18	e	Ljava/lang/NoSuchMethodException;
/*      */     //   258	136	14	method	Ljava/lang/reflect/Method;
/*      */     //   265	129	15	mods	I
/*      */     //   291	103	16	methodName	Ljava/lang/String;
/*      */     //   298	96	17	argTypes	[Ljava/lang/Class;
/*      */     //   330	64	18	methodSignature	Ljava/lang/String;
/*      */     //   352	42	19	methodKey	Ljava/lang/String;
/*      */     //   243	157	13	j	I
/*      */     //   240	160	12	methods	[Ljava/lang/reflect/Method;
/*      */     //   224	182	11	i	I
/*      */     //   472	97	17	argTypes	[Ljava/lang/Class;
/*      */     //   481	88	18	methodSignature	Ljava/lang/String;
/*      */     //   503	66	19	methodKey	Ljava/lang/String;
/*      */     //   430	139	13	method	Ljava/lang/reflect/Method;
/*      */     //   437	132	14	mods	I
/*      */     //   444	125	15	isAbstractMethod	Z
/*      */     //   451	118	16	methodName	Ljava/lang/String;
/*      */     //   415	160	12	j	I
/*      */     //   638	21	16	k	I
/*      */     //   608	65	13	functionName	Ljava/lang/String;
/*      */     //   628	45	14	length	I
/*      */     //   635	38	15	parms	[Ljava/lang/Class;
/*      */     //   0	687	0	functionNames	Lorg/mozilla/javascript/ObjToIntMap;
/*      */     //   0	687	1	adapterName	Ljava/lang/String;
/*      */     //   0	687	2	superClass	Ljava/lang/Class;
/*      */     //   0	687	3	interfaces	[Ljava/lang/Class;
/*      */     //   0	687	4	scriptClassName	Ljava/lang/String;
/*      */     //   16	671	5	cfw	Lorg/mozilla/classfile/ClassFileWriter;
/*      */     //   61	626	6	interfacesCount	I
/*      */     //   109	578	7	superName	Ljava/lang/String;
/*      */     //   115	572	8	ctors	[Ljava/lang/reflect/Constructor;
/*      */     //   212	475	9	generatedOverrides	Lorg/mozilla/javascript/ObjToIntMap;
/*      */     //   221	466	10	generatedMethods	Lorg/mozilla/javascript/ObjToIntMap;
/*      */     //   412	275	11	methods	[Ljava/lang/reflect/Method;
/*      */     //   585	102	12	iter	Lorg/mozilla/javascript/ObjToIntMap$Iterator;
/*      */     // Local variable type table:
/*      */     //   start	length	slot	name	signature
/*      */     //   141	33	12	ctor	Ljava/lang/reflect/Constructor<*>;
/*      */     //   298	96	17	argTypes	[Ljava/lang/Class<*>;
/*      */     //   472	97	17	argTypes	[Ljava/lang/Class<*>;
/*      */     //   635	38	15	parms	[Ljava/lang/Class<*>;
/*      */     //   0	687	2	superClass	Ljava/lang/Class<*>;
/*      */     //   0	687	3	interfaces	[Ljava/lang/Class<*>;
/*      */     //   115	572	8	ctors	[Ljava/lang/reflect/Constructor<*>;
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   307	316	319	java/lang/NoSuchMethodException
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Method[] getOverridableMethods(Class<?> clazz) {
/*  471 */     ArrayList<Method> list = new ArrayList<Method>();
/*  472 */     HashSet<String> skip = new HashSet<String>();
/*      */ 
/*      */     
/*      */     Class<?> c;
/*      */     
/*  477 */     for (c = clazz; c != null; c = c.getSuperclass()) {
/*  478 */       appendOverridableMethods(c, list, skip);
/*      */     }
/*  480 */     for (c = clazz; c != null; c = c.getSuperclass()) {
/*  481 */       for (Class<?> intf : c.getInterfaces())
/*  482 */         appendOverridableMethods(intf, list, skip); 
/*      */     } 
/*  484 */     return list.<Method>toArray(new Method[list.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static void appendOverridableMethods(Class<?> c, ArrayList<Method> list, HashSet<String> skip) {
/*  490 */     Method[] methods = c.getDeclaredMethods();
/*  491 */     for (int i = 0; i < methods.length; i++) {
/*  492 */       String methodKey = methods[i].getName() + getMethodSignature(methods[i], methods[i].getParameterTypes());
/*      */ 
/*      */       
/*  495 */       if (!skip.contains(methodKey)) {
/*      */         
/*  497 */         int mods = methods[i].getModifiers();
/*  498 */         if (!Modifier.isStatic(mods))
/*      */         {
/*  500 */           if (Modifier.isFinal(mods)) {
/*      */ 
/*      */             
/*  503 */             skip.add(methodKey);
/*      */           
/*      */           }
/*  506 */           else if (Modifier.isPublic(mods) || Modifier.isProtected(mods)) {
/*  507 */             list.add(methods[i]);
/*  508 */             skip.add(methodKey);
/*      */           }  } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   static Class<?> loadAdapterClass(String className, byte[] classBytes) {
/*      */     Object staticDomain;
/*  516 */     Class<?> domainClass = SecurityController.getStaticSecurityDomainClass();
/*  517 */     if (domainClass == CodeSource.class || domainClass == ProtectionDomain.class) {
/*      */       
/*  519 */       ProtectionDomain protectionDomain = SecurityUtilities.getScriptProtectionDomain();
/*  520 */       if (protectionDomain == null) {
/*  521 */         protectionDomain = JavaAdapter.class.getProtectionDomain();
/*      */       }
/*  523 */       if (domainClass == CodeSource.class) {
/*  524 */         staticDomain = (protectionDomain == null) ? null : protectionDomain.getCodeSource();
/*      */       } else {
/*      */         
/*  527 */         staticDomain = protectionDomain;
/*      */       } 
/*      */     } else {
/*      */       
/*  531 */       staticDomain = null;
/*      */     } 
/*  533 */     GeneratedClassLoader loader = SecurityController.createLoader(null, staticDomain);
/*      */     
/*  535 */     Class<?> result = loader.defineClass(className, classBytes);
/*  536 */     loader.linkClass(result);
/*  537 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public static Function getFunction(Scriptable obj, String functionName) {
/*  542 */     Object x = ScriptableObject.getProperty(obj, functionName);
/*  543 */     if (x == Scriptable.NOT_FOUND)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  549 */       return null;
/*      */     }
/*  551 */     if (!(x instanceof Function)) {
/*  552 */       throw ScriptRuntime.notFunctionError(x, functionName);
/*      */     }
/*  554 */     return (Function)x;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object callMethod(ContextFactory factory, final Scriptable thisObj, final Function f, final Object[] args, final long argsToWrap) {
/*  566 */     if (f == null)
/*      */     {
/*  568 */       return null;
/*      */     }
/*  570 */     if (factory == null) {
/*  571 */       factory = ContextFactory.getGlobal();
/*      */     }
/*      */     
/*  574 */     final Scriptable scope = f.getParentScope();
/*  575 */     if (argsToWrap == 0L) {
/*  576 */       return Context.call(factory, f, scope, thisObj, args);
/*      */     }
/*      */     
/*  579 */     Context cx = Context.getCurrentContext();
/*  580 */     if (cx != null) {
/*  581 */       return doCall(cx, scope, thisObj, f, args, argsToWrap);
/*      */     }
/*  583 */     return factory.call(new ContextAction()
/*      */         {
/*      */           public Object run(Context cx) {
/*  586 */             return JavaAdapter.doCall(cx, scope, thisObj, f, args, argsToWrap);
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Object doCall(Context cx, Scriptable scope, Scriptable thisObj, Function f, Object[] args, long argsToWrap) {
/*  597 */     for (int i = 0; i != args.length; i++) {
/*  598 */       if (0L != (argsToWrap & (1 << i))) {
/*  599 */         Object arg = args[i];
/*  600 */         if (!(arg instanceof Scriptable)) {
/*  601 */           args[i] = cx.getWrapFactory().wrap(cx, scope, arg, null);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  606 */     return f.call(cx, scope, thisObj, args);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Scriptable runScript(final Script script) {
/*  611 */     return (Scriptable)ContextFactory.getGlobal().call(new ContextAction()
/*      */         {
/*      */           public Object run(Context cx)
/*      */           {
/*  615 */             ScriptableObject global = ScriptRuntime.getGlobal(cx);
/*  616 */             script.exec(cx, global);
/*  617 */             return global;
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static void generateCtor(ClassFileWriter cfw, String adapterName, String superName, Constructor<?> superCtor) {
/*  625 */     short locals = 3;
/*  626 */     Class<?>[] parameters = superCtor.getParameterTypes();
/*      */ 
/*      */ 
/*      */     
/*  630 */     if (parameters.length == 0) {
/*  631 */       cfw.startMethod("<init>", "(Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/ContextFactory;)V", (short)1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  637 */       cfw.add(42);
/*  638 */       cfw.addInvoke(183, superName, "<init>", "()V");
/*      */     } else {
/*  640 */       StringBuilder sig = new StringBuilder("(Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/ContextFactory;");
/*      */ 
/*      */       
/*  643 */       int marker = sig.length();
/*  644 */       for (Class<?> c : parameters) {
/*  645 */         appendTypeString(sig, c);
/*      */       }
/*  647 */       sig.append(")V");
/*  648 */       cfw.startMethod("<init>", sig.toString(), (short)1);
/*      */ 
/*      */       
/*  651 */       cfw.add(42);
/*  652 */       short paramOffset = 3;
/*  653 */       for (Class<?> parameter : parameters) {
/*  654 */         paramOffset = (short)(paramOffset + generatePushParam(cfw, paramOffset, parameter));
/*      */       }
/*  656 */       locals = paramOffset;
/*  657 */       sig.delete(1, marker);
/*  658 */       cfw.addInvoke(183, superName, "<init>", sig.toString());
/*      */     } 
/*      */ 
/*      */     
/*  662 */     cfw.add(42);
/*  663 */     cfw.add(43);
/*  664 */     cfw.add(181, adapterName, "delegee", "Lorg/mozilla/javascript/Scriptable;");
/*      */ 
/*      */ 
/*      */     
/*  668 */     cfw.add(42);
/*  669 */     cfw.add(44);
/*  670 */     cfw.add(181, adapterName, "factory", "Lorg/mozilla/javascript/ContextFactory;");
/*      */ 
/*      */     
/*  673 */     cfw.add(42);
/*      */     
/*  675 */     cfw.add(43);
/*  676 */     cfw.add(42);
/*  677 */     cfw.addInvoke(184, "org/mozilla/javascript/JavaAdapter", "createAdapterWrapper", "(Lorg/mozilla/javascript/Scriptable;Ljava/lang/Object;)Lorg/mozilla/javascript/Scriptable;");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  683 */     cfw.add(181, adapterName, "self", "Lorg/mozilla/javascript/Scriptable;");
/*      */ 
/*      */     
/*  686 */     cfw.add(177);
/*  687 */     cfw.stopMethod(locals);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void generateSerialCtor(ClassFileWriter cfw, String adapterName, String superName) {
/*  694 */     cfw.startMethod("<init>", "(Lorg/mozilla/javascript/ContextFactory;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;)V", (short)1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  702 */     cfw.add(42);
/*  703 */     cfw.addInvoke(183, superName, "<init>", "()V");
/*      */ 
/*      */     
/*  706 */     cfw.add(42);
/*  707 */     cfw.add(43);
/*  708 */     cfw.add(181, adapterName, "factory", "Lorg/mozilla/javascript/ContextFactory;");
/*      */ 
/*      */ 
/*      */     
/*  712 */     cfw.add(42);
/*  713 */     cfw.add(44);
/*  714 */     cfw.add(181, adapterName, "delegee", "Lorg/mozilla/javascript/Scriptable;");
/*      */ 
/*      */     
/*  717 */     cfw.add(42);
/*  718 */     cfw.add(45);
/*  719 */     cfw.add(181, adapterName, "self", "Lorg/mozilla/javascript/Scriptable;");
/*      */ 
/*      */     
/*  722 */     cfw.add(177);
/*  723 */     cfw.stopMethod((short)4);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void generateEmptyCtor(ClassFileWriter cfw, String adapterName, String superName, String scriptClassName) {
/*  731 */     cfw.startMethod("<init>", "()V", (short)1);
/*      */ 
/*      */     
/*  734 */     cfw.add(42);
/*  735 */     cfw.addInvoke(183, superName, "<init>", "()V");
/*      */ 
/*      */     
/*  738 */     cfw.add(42);
/*  739 */     cfw.add(1);
/*  740 */     cfw.add(181, adapterName, "factory", "Lorg/mozilla/javascript/ContextFactory;");
/*      */ 
/*      */ 
/*      */     
/*  744 */     cfw.add(187, scriptClassName);
/*  745 */     cfw.add(89);
/*  746 */     cfw.addInvoke(183, scriptClassName, "<init>", "()V");
/*      */ 
/*      */     
/*  749 */     cfw.addInvoke(184, "org/mozilla/javascript/JavaAdapter", "runScript", "(Lorg/mozilla/javascript/Script;)Lorg/mozilla/javascript/Scriptable;");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  754 */     cfw.add(76);
/*      */ 
/*      */     
/*  757 */     cfw.add(42);
/*  758 */     cfw.add(43);
/*  759 */     cfw.add(181, adapterName, "delegee", "Lorg/mozilla/javascript/Scriptable;");
/*      */ 
/*      */     
/*  762 */     cfw.add(42);
/*      */     
/*  764 */     cfw.add(43);
/*  765 */     cfw.add(42);
/*  766 */     cfw.addInvoke(184, "org/mozilla/javascript/JavaAdapter", "createAdapterWrapper", "(Lorg/mozilla/javascript/Scriptable;Ljava/lang/Object;)Lorg/mozilla/javascript/Scriptable;");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  772 */     cfw.add(181, adapterName, "self", "Lorg/mozilla/javascript/Scriptable;");
/*      */ 
/*      */     
/*  775 */     cfw.add(177);
/*  776 */     cfw.stopMethod((short)2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void generatePushWrappedArgs(ClassFileWriter cfw, Class<?>[] argTypes, int arrayLength) {
/*  789 */     cfw.addPush(arrayLength);
/*  790 */     cfw.add(189, "java/lang/Object");
/*  791 */     int paramOffset = 1;
/*  792 */     for (int i = 0; i != argTypes.length; i++) {
/*  793 */       cfw.add(89);
/*  794 */       cfw.addPush(i);
/*  795 */       paramOffset += generateWrapArg(cfw, paramOffset, argTypes[i]);
/*  796 */       cfw.add(83);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int generateWrapArg(ClassFileWriter cfw, int paramOffset, Class<?> argType) {
/*  808 */     int size = 1;
/*  809 */     if (!argType.isPrimitive()) {
/*  810 */       cfw.add(25, paramOffset);
/*      */     }
/*  812 */     else if (argType == boolean.class) {
/*      */       
/*  814 */       cfw.add(187, "java/lang/Boolean");
/*  815 */       cfw.add(89);
/*  816 */       cfw.add(21, paramOffset);
/*  817 */       cfw.addInvoke(183, "java/lang/Boolean", "<init>", "(Z)V");
/*      */     
/*      */     }
/*  820 */     else if (argType == char.class) {
/*      */       
/*  822 */       cfw.add(21, paramOffset);
/*  823 */       cfw.addInvoke(184, "java/lang/String", "valueOf", "(C)Ljava/lang/String;");
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  828 */       cfw.add(187, "java/lang/Double");
/*  829 */       cfw.add(89);
/*  830 */       String typeName = argType.getName();
/*  831 */       switch (typeName.charAt(0)) {
/*      */         
/*      */         case 'b':
/*      */         case 'i':
/*      */         case 's':
/*  836 */           cfw.add(21, paramOffset);
/*  837 */           cfw.add(135);
/*      */           break;
/*      */         
/*      */         case 'l':
/*  841 */           cfw.add(22, paramOffset);
/*  842 */           cfw.add(138);
/*  843 */           size = 2;
/*      */           break;
/*      */         
/*      */         case 'f':
/*  847 */           cfw.add(23, paramOffset);
/*  848 */           cfw.add(141);
/*      */           break;
/*      */         case 'd':
/*  851 */           cfw.add(24, paramOffset);
/*  852 */           size = 2;
/*      */           break;
/*      */       } 
/*  855 */       cfw.addInvoke(183, "java/lang/Double", "<init>", "(D)V");
/*      */     } 
/*      */     
/*  858 */     return size;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void generateReturnResult(ClassFileWriter cfw, Class<?> retType, boolean callConvertResult) {
/*  871 */     if (retType == void.class) {
/*  872 */       cfw.add(87);
/*  873 */       cfw.add(177);
/*      */     }
/*  875 */     else if (retType == boolean.class) {
/*  876 */       cfw.addInvoke(184, "org/mozilla/javascript/Context", "toBoolean", "(Ljava/lang/Object;)Z");
/*      */ 
/*      */       
/*  879 */       cfw.add(172);
/*      */     }
/*  881 */     else if (retType == char.class) {
/*      */ 
/*      */ 
/*      */       
/*  885 */       cfw.addInvoke(184, "org/mozilla/javascript/Context", "toString", "(Ljava/lang/Object;)Ljava/lang/String;");
/*      */ 
/*      */ 
/*      */       
/*  889 */       cfw.add(3);
/*  890 */       cfw.addInvoke(182, "java/lang/String", "charAt", "(I)C");
/*      */       
/*  892 */       cfw.add(172);
/*      */     } else {
/*  894 */       if (retType.isPrimitive()) {
/*  895 */         cfw.addInvoke(184, "org/mozilla/javascript/Context", "toNumber", "(Ljava/lang/Object;)D");
/*      */ 
/*      */         
/*  898 */         String typeName = retType.getName();
/*  899 */         switch (typeName.charAt(0)) {
/*      */           case 'b':
/*      */           case 'i':
/*      */           case 's':
/*  903 */             cfw.add(142);
/*  904 */             cfw.add(172);
/*      */             return;
/*      */           case 'l':
/*  907 */             cfw.add(143);
/*  908 */             cfw.add(173);
/*      */             return;
/*      */           case 'f':
/*  911 */             cfw.add(144);
/*  912 */             cfw.add(174);
/*      */             return;
/*      */           case 'd':
/*  915 */             cfw.add(175);
/*      */             return;
/*      */         } 
/*  918 */         throw new RuntimeException("Unexpected return type " + retType.toString());
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  923 */       String retTypeStr = retType.getName();
/*  924 */       if (callConvertResult) {
/*  925 */         cfw.addLoadConstant(retTypeStr);
/*  926 */         cfw.addInvoke(184, "java/lang/Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;");
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  931 */         cfw.addInvoke(184, "org/mozilla/javascript/JavaAdapter", "convertResult", "(Ljava/lang/Object;Ljava/lang/Class;)Ljava/lang/Object;");
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  939 */       cfw.add(192, retTypeStr);
/*  940 */       cfw.add(176);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void generateMethod(ClassFileWriter cfw, String genName, String methodName, Class<?>[] parms, Class<?> returnType, boolean convertResult) {
/*  948 */     StringBuilder sb = new StringBuilder();
/*  949 */     int paramsEnd = appendMethodSignature(parms, returnType, sb);
/*  950 */     String methodSignature = sb.toString();
/*  951 */     cfw.startMethod(methodName, methodSignature, (short)1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  957 */     cfw.add(42);
/*  958 */     cfw.add(180, genName, "factory", "Lorg/mozilla/javascript/ContextFactory;");
/*      */ 
/*      */ 
/*      */     
/*  962 */     cfw.add(42);
/*  963 */     cfw.add(180, genName, "self", "Lorg/mozilla/javascript/Scriptable;");
/*      */ 
/*      */ 
/*      */     
/*  967 */     cfw.add(42);
/*  968 */     cfw.add(180, genName, "delegee", "Lorg/mozilla/javascript/Scriptable;");
/*      */     
/*  970 */     cfw.addPush(methodName);
/*  971 */     cfw.addInvoke(184, "org/mozilla/javascript/JavaAdapter", "getFunction", "(Lorg/mozilla/javascript/Scriptable;Ljava/lang/String;)Lorg/mozilla/javascript/Function;");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  979 */     generatePushWrappedArgs(cfw, parms, parms.length);
/*      */ 
/*      */     
/*  982 */     if (parms.length > 64)
/*      */     {
/*      */       
/*  985 */       throw Context.reportRuntimeError0("JavaAdapter can not subclass methods with more then 64 arguments.");
/*      */     }
/*      */ 
/*      */     
/*  989 */     long convertionMask = 0L;
/*  990 */     for (int i = 0; i != parms.length; i++) {
/*  991 */       if (!parms[i].isPrimitive()) {
/*  992 */         convertionMask |= (1 << i);
/*      */       }
/*      */     } 
/*  995 */     cfw.addPush(convertionMask);
/*      */ 
/*      */ 
/*      */     
/*  999 */     cfw.addInvoke(184, "org/mozilla/javascript/JavaAdapter", "callMethod", "(Lorg/mozilla/javascript/ContextFactory;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Function;[Ljava/lang/Object;J)Ljava/lang/Object;");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1009 */     generateReturnResult(cfw, returnType, convertResult);
/*      */     
/* 1011 */     cfw.stopMethod((short)paramsEnd);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int generatePushParam(ClassFileWriter cfw, int paramOffset, Class<?> paramType) {
/* 1021 */     if (!paramType.isPrimitive()) {
/* 1022 */       cfw.addALoad(paramOffset);
/* 1023 */       return 1;
/*      */     } 
/* 1025 */     String typeName = paramType.getName();
/* 1026 */     switch (typeName.charAt(0)) {
/*      */       
/*      */       case 'b':
/*      */       case 'c':
/*      */       case 'i':
/*      */       case 's':
/*      */       case 'z':
/* 1033 */         cfw.addILoad(paramOffset);
/* 1034 */         return 1;
/*      */       
/*      */       case 'l':
/* 1037 */         cfw.addLLoad(paramOffset);
/* 1038 */         return 2;
/*      */       
/*      */       case 'f':
/* 1041 */         cfw.addFLoad(paramOffset);
/* 1042 */         return 1;
/*      */       case 'd':
/* 1044 */         cfw.addDLoad(paramOffset);
/* 1045 */         return 2;
/*      */     } 
/* 1047 */     throw Kit.codeBug();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void generatePopResult(ClassFileWriter cfw, Class<?> retType) {
/* 1058 */     if (retType.isPrimitive()) {
/* 1059 */       String typeName = retType.getName();
/* 1060 */       switch (typeName.charAt(0)) {
/*      */         case 'b':
/*      */         case 'c':
/*      */         case 'i':
/*      */         case 's':
/*      */         case 'z':
/* 1066 */           cfw.add(172);
/*      */           break;
/*      */         case 'l':
/* 1069 */           cfw.add(173);
/*      */           break;
/*      */         case 'f':
/* 1072 */           cfw.add(174);
/*      */           break;
/*      */         case 'd':
/* 1075 */           cfw.add(175);
/*      */           break;
/*      */       } 
/*      */     } else {
/* 1079 */       cfw.add(176);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void generateSuper(ClassFileWriter cfw, String genName, String superName, String methodName, String methodSignature, Class<?>[] parms, Class<?> returnType) {
/* 1093 */     cfw.startMethod("super$" + methodName, methodSignature, (short)1);
/*      */ 
/*      */ 
/*      */     
/* 1097 */     cfw.add(25, 0);
/*      */ 
/*      */     
/* 1100 */     int paramOffset = 1;
/* 1101 */     for (Class<?> parm : parms) {
/* 1102 */       paramOffset += generatePushParam(cfw, paramOffset, parm);
/*      */     }
/*      */ 
/*      */     
/* 1106 */     cfw.addInvoke(183, superName, methodName, methodSignature);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1112 */     Class<?> retType = returnType;
/* 1113 */     if (!retType.equals(void.class)) {
/* 1114 */       generatePopResult(cfw, retType);
/*      */     } else {
/* 1116 */       cfw.add(177);
/*      */     } 
/* 1118 */     cfw.stopMethod((short)(paramOffset + 1));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String getMethodSignature(Method method, Class<?>[] argTypes) {
/* 1126 */     StringBuilder sb = new StringBuilder();
/* 1127 */     appendMethodSignature(argTypes, method.getReturnType(), sb);
/* 1128 */     return sb.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int appendMethodSignature(Class<?>[] argTypes, Class<?> returnType, StringBuilder sb) {
/* 1135 */     sb.append('(');
/* 1136 */     int firstLocal = 1 + argTypes.length;
/* 1137 */     for (Class<?> type : argTypes) {
/* 1138 */       appendTypeString(sb, type);
/* 1139 */       if (type == long.class || type == double.class)
/*      */       {
/* 1141 */         firstLocal++;
/*      */       }
/*      */     } 
/* 1144 */     sb.append(')');
/* 1145 */     appendTypeString(sb, returnType);
/* 1146 */     return firstLocal;
/*      */   }
/*      */ 
/*      */   
/*      */   private static StringBuilder appendTypeString(StringBuilder sb, Class<?> type) {
/* 1151 */     while (type.isArray()) {
/* 1152 */       sb.append('[');
/* 1153 */       type = type.getComponentType();
/*      */     } 
/* 1155 */     if (type.isPrimitive()) {
/*      */       char typeLetter;
/* 1157 */       if (type == boolean.class) {
/* 1158 */         typeLetter = 'Z';
/* 1159 */       } else if (type == long.class) {
/* 1160 */         typeLetter = 'J';
/*      */       } else {
/* 1162 */         String typeName = type.getName();
/* 1163 */         typeLetter = Character.toUpperCase(typeName.charAt(0));
/*      */       } 
/* 1165 */       sb.append(typeLetter);
/*      */     } else {
/* 1167 */       sb.append('L');
/* 1168 */       sb.append(type.getName().replace('.', '/'));
/* 1169 */       sb.append(';');
/*      */     } 
/* 1171 */     return sb;
/*      */   }
/*      */ 
/*      */   
/*      */   static int[] getArgsToConvert(Class<?>[] argTypes) {
/* 1176 */     int count = 0;
/* 1177 */     for (int i = 0; i != argTypes.length; i++) {
/* 1178 */       if (!argTypes[i].isPrimitive())
/* 1179 */         count++; 
/*      */     } 
/* 1181 */     if (count == 0)
/* 1182 */       return null; 
/* 1183 */     int[] array = new int[count];
/* 1184 */     count = 0;
/* 1185 */     for (int j = 0; j != argTypes.length; j++) {
/* 1186 */       if (!argTypes[j].isPrimitive())
/* 1187 */         array[count++] = j; 
/*      */     } 
/* 1189 */     return array;
/*      */   }
/*      */   
/* 1192 */   private static final Object FTAG = "JavaAdapter";
/*      */   private static final int Id_JavaAdapter = 1;
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\JavaAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */