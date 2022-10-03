/*      */ package javassist.util.proxy;
/*      */ 
/*      */ import java.lang.invoke.MethodHandles;
/*      */ import java.lang.ref.Reference;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Member;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.security.ProtectionDomain;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.WeakHashMap;
/*      */ import javassist.CannotCompileException;
/*      */ import javassist.bytecode.AttributeInfo;
/*      */ import javassist.bytecode.Bytecode;
/*      */ import javassist.bytecode.ClassFile;
/*      */ import javassist.bytecode.CodeAttribute;
/*      */ import javassist.bytecode.ConstPool;
/*      */ import javassist.bytecode.Descriptor;
/*      */ import javassist.bytecode.DuplicateMemberException;
/*      */ import javassist.bytecode.ExceptionsAttribute;
/*      */ import javassist.bytecode.FieldInfo;
/*      */ import javassist.bytecode.MethodInfo;
/*      */ import javassist.bytecode.SignatureAttribute;
/*      */ import javassist.bytecode.StackMapTable;
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
/*      */ public class ProxyFactory
/*      */ {
/*      */   private Class<?> superClass;
/*      */   private Class<?>[] interfaces;
/*      */   private MethodFilter methodFilter;
/*      */   private MethodHandler handler;
/*      */   private List<Map.Entry<String, Method>> signatureMethods;
/*      */   private boolean hasGetHandler;
/*      */   private byte[] signature;
/*      */   private String classname;
/*      */   private String basename;
/*      */   private String superName;
/*      */   private Class<?> thisClass;
/*      */   private String genericSignature;
/*      */   private boolean factoryUseCache;
/*      */   private boolean factoryWriteReplace;
/*      */   public static boolean onlyPublicMethods = false;
/*      */   public String writeDirectory;
/*  236 */   private static final Class<?> OBJECT_TYPE = Object.class;
/*      */   
/*      */   private static final String HOLDER = "_methods_";
/*      */   private static final String HOLDER_TYPE = "[Ljava/lang/reflect/Method;";
/*      */   private static final String FILTER_SIGNATURE_FIELD = "_filter_signature";
/*      */   private static final String FILTER_SIGNATURE_TYPE = "[B";
/*      */   private static final String HANDLER = "handler";
/*      */   private static final String NULL_INTERCEPTOR_HOLDER = "javassist.util.proxy.RuntimeSupport";
/*      */   private static final String DEFAULT_INTERCEPTOR = "default_interceptor";
/*  245 */   private static final String HANDLER_TYPE = 'L' + MethodHandler.class
/*  246 */     .getName().replace('.', '/') + ';';
/*      */   private static final String HANDLER_SETTER = "setHandler";
/*  248 */   private static final String HANDLER_SETTER_TYPE = "(" + HANDLER_TYPE + ")V";
/*      */   
/*      */   private static final String HANDLER_GETTER = "getHandler";
/*  251 */   private static final String HANDLER_GETTER_TYPE = "()" + HANDLER_TYPE;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String SERIAL_VERSION_UID_FIELD = "serialVersionUID";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String SERIAL_VERSION_UID_TYPE = "J";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final long SERIAL_VERSION_UID_VALUE = -1L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static volatile boolean useCache = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static volatile boolean useWriteReplace = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isUseCache() {
/*  301 */     return this.factoryUseCache;
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
/*      */   public void setUseCache(boolean useCache) {
/*  313 */     if (this.handler != null && useCache) {
/*  314 */       throw new RuntimeException("caching cannot be enabled if the factory default interceptor has been set");
/*      */     }
/*  316 */     this.factoryUseCache = useCache;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isUseWriteReplace() {
/*  325 */     return this.factoryWriteReplace;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUseWriteReplace(boolean useWriteReplace) {
/*  335 */     this.factoryWriteReplace = useWriteReplace;
/*      */   }
/*      */   
/*  338 */   private static Map<ClassLoader, Map<String, ProxyDetails>> proxyCache = new WeakHashMap<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isProxyClass(Class<?> cl) {
/*  349 */     return Proxy.class.isAssignableFrom(cl);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class ProxyDetails
/*      */   {
/*      */     byte[] signature;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Reference<Class<?>> proxyClass;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean isUseWriteReplace;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     ProxyDetails(byte[] signature, Class<?> proxyClass, boolean isUseWriteReplace) {
/*  377 */       this.signature = signature;
/*  378 */       this.proxyClass = new WeakReference<>(proxyClass);
/*  379 */       this.isUseWriteReplace = isUseWriteReplace;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ProxyFactory() {
/*  387 */     this.superClass = null;
/*  388 */     this.interfaces = null;
/*  389 */     this.methodFilter = null;
/*  390 */     this.handler = null;
/*  391 */     this.signature = null;
/*  392 */     this.signatureMethods = null;
/*  393 */     this.hasGetHandler = false;
/*  394 */     this.thisClass = null;
/*  395 */     this.genericSignature = null;
/*  396 */     this.writeDirectory = null;
/*  397 */     this.factoryUseCache = useCache;
/*  398 */     this.factoryWriteReplace = useWriteReplace;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSuperclass(Class<?> clazz) {
/*  405 */     this.superClass = clazz;
/*      */     
/*  407 */     this.signature = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Class<?> getSuperclass() {
/*  415 */     return this.superClass;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInterfaces(Class<?>[] ifs) {
/*  421 */     this.interfaces = ifs;
/*      */     
/*  423 */     this.signature = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Class<?>[] getInterfaces() {
/*  431 */     return this.interfaces;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFilter(MethodFilter mf) {
/*  437 */     this.methodFilter = mf;
/*      */     
/*  439 */     this.signature = null;
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
/*      */   public void setGenericSignature(String sig) {
/*  467 */     this.genericSignature = sig;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Class<?> createClass() {
/*  478 */     if (this.signature == null) {
/*  479 */       computeSignature(this.methodFilter);
/*      */     }
/*  481 */     return createClass1(null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Class<?> createClass(MethodFilter filter) {
/*  490 */     computeSignature(filter);
/*  491 */     return createClass1(null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Class<?> createClass(byte[] signature) {
/*  501 */     installSignature(signature);
/*  502 */     return createClass1(null);
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
/*      */   public Class<?> createClass(MethodHandles.Lookup lookup) {
/*  518 */     if (this.signature == null) {
/*  519 */       computeSignature(this.methodFilter);
/*      */     }
/*  521 */     return createClass1(lookup);
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
/*      */   public Class<?> createClass(MethodHandles.Lookup lookup, MethodFilter filter) {
/*  535 */     computeSignature(filter);
/*  536 */     return createClass1(lookup);
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
/*      */   Class<?> createClass(MethodHandles.Lookup lookup, byte[] signature) {
/*  550 */     installSignature(signature);
/*  551 */     return createClass1(lookup);
/*      */   }
/*      */   
/*      */   private Class<?> createClass1(MethodHandles.Lookup lookup) {
/*  555 */     Class<?> result = this.thisClass;
/*  556 */     if (result == null) {
/*  557 */       ClassLoader cl = getClassLoader();
/*  558 */       synchronized (proxyCache) {
/*  559 */         if (this.factoryUseCache) {
/*  560 */           createClass2(cl, lookup);
/*      */         } else {
/*  562 */           createClass3(cl, lookup);
/*      */         } 
/*  564 */         result = this.thisClass;
/*      */         
/*  566 */         this.thisClass = null;
/*      */       } 
/*      */     } 
/*      */     
/*  570 */     return result;
/*      */   }
/*      */   
/*  573 */   private static char[] hexDigits = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getKey(Class<?> superClass, Class<?>[] interfaces, byte[] signature, boolean useWriteReplace) {
/*  579 */     StringBuffer sbuf = new StringBuffer();
/*  580 */     if (superClass != null) {
/*  581 */       sbuf.append(superClass.getName());
/*      */     }
/*  583 */     sbuf.append(":"); int i;
/*  584 */     for (i = 0; i < interfaces.length; i++) {
/*  585 */       sbuf.append(interfaces[i].getName());
/*  586 */       sbuf.append(":");
/*      */     } 
/*  588 */     for (i = 0; i < signature.length; i++) {
/*  589 */       byte b = signature[i];
/*  590 */       int lo = b & 0xF;
/*  591 */       int hi = b >> 4 & 0xF;
/*  592 */       sbuf.append(hexDigits[lo]);
/*  593 */       sbuf.append(hexDigits[hi]);
/*      */     } 
/*  595 */     if (useWriteReplace) {
/*  596 */       sbuf.append(":w");
/*      */     }
/*      */     
/*  599 */     return sbuf.toString();
/*      */   }
/*      */   
/*      */   private void createClass2(ClassLoader cl, MethodHandles.Lookup lookup) {
/*  603 */     String key = getKey(this.superClass, this.interfaces, this.signature, this.factoryWriteReplace);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  610 */     Map<String, ProxyDetails> cacheForTheLoader = proxyCache.get(cl);
/*      */     
/*  612 */     if (cacheForTheLoader == null) {
/*  613 */       cacheForTheLoader = new HashMap<>();
/*  614 */       proxyCache.put(cl, cacheForTheLoader);
/*      */     } 
/*  616 */     ProxyDetails details = cacheForTheLoader.get(key);
/*  617 */     if (details != null) {
/*  618 */       Reference<Class<?>> reference = details.proxyClass;
/*  619 */       this.thisClass = reference.get();
/*  620 */       if (this.thisClass != null) {
/*      */         return;
/*      */       }
/*      */     } 
/*  624 */     createClass3(cl, lookup);
/*  625 */     details = new ProxyDetails(this.signature, this.thisClass, this.factoryWriteReplace);
/*  626 */     cacheForTheLoader.put(key, details);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createClass3(ClassLoader cl, MethodHandles.Lookup lookup) {
/*  632 */     allocateClassName();
/*      */     
/*      */     try {
/*  635 */       ClassFile cf = make();
/*  636 */       if (this.writeDirectory != null) {
/*  637 */         FactoryHelper.writeFile(cf, this.writeDirectory);
/*      */       }
/*  639 */       if (lookup == null) {
/*  640 */         this.thisClass = FactoryHelper.toClass(cf, getClassInTheSamePackage(), cl, getDomain());
/*      */       } else {
/*  642 */         this.thisClass = FactoryHelper.toClass(cf, lookup);
/*      */       } 
/*  644 */       setField("_filter_signature", this.signature);
/*      */       
/*  646 */       if (!this.factoryUseCache) {
/*  647 */         setField("default_interceptor", this.handler);
/*      */       }
/*      */     }
/*  650 */     catch (CannotCompileException e) {
/*  651 */       throw new RuntimeException(e.getMessage(), e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Class<?> getClassInTheSamePackage() {
/*  662 */     if (this.basename.startsWith("javassist.util.proxy."))
/*  663 */       return getClass(); 
/*  664 */     if (this.superClass != null && this.superClass != OBJECT_TYPE)
/*  665 */       return this.superClass; 
/*  666 */     if (this.interfaces != null && this.interfaces.length > 0) {
/*  667 */       return this.interfaces[0];
/*      */     }
/*  669 */     return getClass();
/*      */   }
/*      */   
/*      */   private void setField(String fieldName, Object value) {
/*  673 */     if (this.thisClass != null && value != null)
/*      */       try {
/*  675 */         Field f = this.thisClass.getField(fieldName);
/*  676 */         SecurityActions.setAccessible(f, true);
/*  677 */         f.set((Object)null, value);
/*  678 */         SecurityActions.setAccessible(f, false);
/*      */       }
/*  680 */       catch (Exception e) {
/*  681 */         throw new RuntimeException(e);
/*      */       }  
/*      */   }
/*      */   
/*      */   static byte[] getFilterSignature(Class<?> clazz) {
/*  686 */     return (byte[])getField(clazz, "_filter_signature");
/*      */   }
/*      */   
/*      */   private static Object getField(Class<?> clazz, String fieldName) {
/*      */     try {
/*  691 */       Field f = clazz.getField(fieldName);
/*  692 */       f.setAccessible(true);
/*  693 */       Object value = f.get((Object)null);
/*  694 */       f.setAccessible(false);
/*  695 */       return value;
/*      */     }
/*  697 */     catch (Exception e) {
/*  698 */       throw new RuntimeException(e);
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
/*      */   public static MethodHandler getHandler(Proxy p) {
/*      */     try {
/*  711 */       Field f = p.getClass().getDeclaredField("handler");
/*  712 */       f.setAccessible(true);
/*  713 */       Object value = f.get(p);
/*  714 */       f.setAccessible(false);
/*  715 */       return (MethodHandler)value;
/*      */     }
/*  717 */     catch (Exception e) {
/*  718 */       throw new RuntimeException(e);
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
/*  757 */   public static ClassLoaderProvider classLoaderProvider = new ClassLoaderProvider()
/*      */     {
/*      */       public ClassLoader get(ProxyFactory pf)
/*      */       {
/*  761 */         return pf.getClassLoader0();
/*      */       }
/*      */     };
/*      */   
/*      */   protected ClassLoader getClassLoader() {
/*  766 */     return classLoaderProvider.get(this);
/*      */   }
/*      */   
/*      */   protected ClassLoader getClassLoader0() {
/*  770 */     ClassLoader loader = null;
/*  771 */     if (this.superClass != null && !this.superClass.getName().equals("java.lang.Object")) {
/*  772 */       loader = this.superClass.getClassLoader();
/*  773 */     } else if (this.interfaces != null && this.interfaces.length > 0) {
/*  774 */       loader = this.interfaces[0].getClassLoader();
/*      */     } 
/*  776 */     if (loader == null) {
/*  777 */       loader = getClass().getClassLoader();
/*      */       
/*  779 */       if (loader == null) {
/*  780 */         loader = Thread.currentThread().getContextClassLoader();
/*  781 */         if (loader == null) {
/*  782 */           loader = ClassLoader.getSystemClassLoader();
/*      */         }
/*      */       } 
/*      */     } 
/*  786 */     return loader;
/*      */   }
/*      */   
/*      */   protected ProtectionDomain getDomain() {
/*      */     Class<?> clazz;
/*  791 */     if (this.superClass != null && !this.superClass.getName().equals("java.lang.Object")) {
/*  792 */       clazz = this.superClass;
/*  793 */     } else if (this.interfaces != null && this.interfaces.length > 0) {
/*  794 */       clazz = this.interfaces[0];
/*      */     } else {
/*  796 */       clazz = getClass();
/*      */     } 
/*  798 */     return clazz.getProtectionDomain();
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
/*      */   public Object create(Class<?>[] paramTypes, Object[] args, MethodHandler mh) throws NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
/*  813 */     Object obj = create(paramTypes, args);
/*  814 */     ((Proxy)obj).setHandler(mh);
/*  815 */     return obj;
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
/*      */   public Object create(Class<?>[] paramTypes, Object[] args) throws NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
/*  828 */     Class<?> c = createClass();
/*  829 */     Constructor<?> cons = c.getConstructor(paramTypes);
/*  830 */     return cons.newInstance(args);
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
/*      */   @Deprecated
/*      */   public void setHandler(MethodHandler mi) {
/*  846 */     if (this.factoryUseCache && mi != null) {
/*  847 */       this.factoryUseCache = false;
/*      */       
/*  849 */       this.thisClass = null;
/*      */     } 
/*  851 */     this.handler = mi;
/*      */ 
/*      */     
/*  854 */     setField("default_interceptor", this.handler);
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
/*  876 */   public static UniqueName nameGenerator = new UniqueName() {
/*  877 */       private final String sep = "_$$_jvst" + Integer.toHexString(hashCode() & 0xFFF) + "_";
/*  878 */       private int counter = 0;
/*      */ 
/*      */       
/*      */       public String get(String classname) {
/*  882 */         return classname + this.sep + Integer.toHexString(this.counter++);
/*      */       }
/*      */     }; private static final String packageForJavaBase = "javassist.util.proxy.";
/*      */   
/*      */   private static String makeProxyName(String classname) {
/*  887 */     synchronized (nameGenerator) {
/*  888 */       return nameGenerator.get(classname);
/*      */     } 
/*      */   }
/*      */   
/*      */   private ClassFile make() throws CannotCompileException {
/*  893 */     ClassFile cf = new ClassFile(false, this.classname, this.superName);
/*  894 */     cf.setAccessFlags(1);
/*  895 */     setInterfaces(cf, this.interfaces, this.hasGetHandler ? Proxy.class : ProxyObject.class);
/*  896 */     ConstPool pool = cf.getConstPool();
/*      */ 
/*      */     
/*  899 */     if (!this.factoryUseCache) {
/*  900 */       FieldInfo finfo = new FieldInfo(pool, "default_interceptor", HANDLER_TYPE);
/*  901 */       finfo.setAccessFlags(9);
/*  902 */       cf.addField(finfo);
/*      */     } 
/*      */ 
/*      */     
/*  906 */     FieldInfo finfo2 = new FieldInfo(pool, "handler", HANDLER_TYPE);
/*  907 */     finfo2.setAccessFlags(2);
/*  908 */     cf.addField(finfo2);
/*      */ 
/*      */     
/*  911 */     FieldInfo finfo3 = new FieldInfo(pool, "_filter_signature", "[B");
/*  912 */     finfo3.setAccessFlags(9);
/*  913 */     cf.addField(finfo3);
/*      */ 
/*      */     
/*  916 */     FieldInfo finfo4 = new FieldInfo(pool, "serialVersionUID", "J");
/*  917 */     finfo4.setAccessFlags(25);
/*  918 */     cf.addField(finfo4);
/*      */     
/*  920 */     if (this.genericSignature != null) {
/*  921 */       SignatureAttribute sa = new SignatureAttribute(pool, this.genericSignature);
/*  922 */       cf.addAttribute((AttributeInfo)sa);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  927 */     makeConstructors(this.classname, cf, pool, this.classname);
/*      */     
/*  929 */     List<Find2MethodsArgs> forwarders = new ArrayList<>();
/*  930 */     int s = overrideMethods(cf, pool, this.classname, forwarders);
/*  931 */     addClassInitializer(cf, pool, this.classname, s, forwarders);
/*  932 */     addSetter(this.classname, cf, pool);
/*  933 */     if (!this.hasGetHandler) {
/*  934 */       addGetter(this.classname, cf, pool);
/*      */     }
/*  936 */     if (this.factoryWriteReplace) {
/*      */       try {
/*  938 */         cf.addMethod(makeWriteReplace(pool));
/*      */       }
/*  940 */       catch (DuplicateMemberException duplicateMemberException) {}
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  945 */     this.thisClass = null;
/*  946 */     return cf;
/*      */   }
/*      */   
/*      */   private void checkClassAndSuperName() {
/*  950 */     if (this.interfaces == null) {
/*  951 */       this.interfaces = new Class[0];
/*      */     }
/*  953 */     if (this.superClass == null) {
/*  954 */       this.superClass = OBJECT_TYPE;
/*  955 */       this.superName = this.superClass.getName();
/*  956 */       this
/*  957 */         .basename = (this.interfaces.length == 0) ? this.superName : this.interfaces[0].getName();
/*      */     } else {
/*  959 */       this.superName = this.superClass.getName();
/*  960 */       this.basename = this.superName;
/*      */     } 
/*      */     
/*  963 */     if (Modifier.isFinal(this.superClass.getModifiers())) {
/*  964 */       throw new RuntimeException(this.superName + " is final");
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  969 */     if (this.basename.startsWith("java.") || this.basename.startsWith("jdk.") || onlyPublicMethods) {
/*  970 */       this.basename = "javassist.util.proxy." + this.basename.replace('.', '_');
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void allocateClassName() {
/*  976 */     this.classname = makeProxyName(this.basename);
/*      */   }
/*      */   
/*  979 */   private static Comparator<Map.Entry<String, Method>> sorter = new Comparator<Map.Entry<String, Method>>()
/*      */     {
/*      */       public int compare(Map.Entry<String, Method> e1, Map.Entry<String, Method> e2)
/*      */       {
/*  983 */         return ((String)e1.getKey()).compareTo(e2.getKey());
/*      */       }
/*      */     };
/*      */   
/*      */   private void makeSortedMethodList() {
/*  988 */     checkClassAndSuperName();
/*      */     
/*  990 */     this.hasGetHandler = false;
/*  991 */     Map<String, Method> allMethods = getMethods(this.superClass, this.interfaces);
/*  992 */     this.signatureMethods = new ArrayList<>(allMethods.entrySet());
/*  993 */     Collections.sort(this.signatureMethods, sorter);
/*      */   }
/*      */   private static final String HANDLER_GETTER_KEY = "getHandler:()";
/*      */   
/*      */   private void computeSignature(MethodFilter filter) {
/*  998 */     makeSortedMethodList();
/*      */     
/* 1000 */     int l = this.signatureMethods.size();
/* 1001 */     int maxBytes = l + 7 >> 3;
/* 1002 */     this.signature = new byte[maxBytes];
/* 1003 */     for (int idx = 0; idx < l; idx++) {
/*      */       
/* 1005 */       Method m = (Method)((Map.Entry)this.signatureMethods.get(idx)).getValue();
/* 1006 */       int mod = m.getModifiers();
/* 1007 */       if (!Modifier.isFinal(mod) && !Modifier.isStatic(mod) && 
/* 1008 */         isVisible(mod, this.basename, m) && (filter == null || filter.isHandled(m))) {
/* 1009 */         setBit(this.signature, idx);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void installSignature(byte[] signature) {
/* 1016 */     makeSortedMethodList();
/*      */     
/* 1018 */     int l = this.signatureMethods.size();
/* 1019 */     int maxBytes = l + 7 >> 3;
/* 1020 */     if (signature.length != maxBytes) {
/* 1021 */       throw new RuntimeException("invalid filter signature length for deserialized proxy class");
/*      */     }
/*      */     
/* 1024 */     this.signature = signature;
/*      */   }
/*      */   
/*      */   private boolean testBit(byte[] signature, int idx) {
/* 1028 */     int byteIdx = idx >> 3;
/* 1029 */     if (byteIdx > signature.length)
/* 1030 */       return false; 
/* 1031 */     int bitIdx = idx & 0x7;
/* 1032 */     int mask = 1 << bitIdx;
/* 1033 */     int sigByte = signature[byteIdx];
/* 1034 */     return ((sigByte & mask) != 0);
/*      */   }
/*      */   
/*      */   private void setBit(byte[] signature, int idx) {
/* 1038 */     int byteIdx = idx >> 3;
/* 1039 */     if (byteIdx < signature.length) {
/* 1040 */       int bitIdx = idx & 0x7;
/* 1041 */       int mask = 1 << bitIdx;
/* 1042 */       int sigByte = signature[byteIdx];
/* 1043 */       signature[byteIdx] = (byte)(sigByte | mask);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void setInterfaces(ClassFile cf, Class<?>[] interfaces, Class<?> proxyClass) {
/* 1048 */     String list[], setterIntf = proxyClass.getName();
/*      */     
/* 1050 */     if (interfaces == null || interfaces.length == 0) {
/* 1051 */       list = new String[] { setterIntf };
/*      */     } else {
/* 1053 */       list = new String[interfaces.length + 1];
/* 1054 */       for (int i = 0; i < interfaces.length; i++) {
/* 1055 */         list[i] = interfaces[i].getName();
/*      */       }
/* 1057 */       list[interfaces.length] = setterIntf;
/*      */     } 
/*      */     
/* 1060 */     cf.setInterfaces(list);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void addClassInitializer(ClassFile cf, ConstPool cp, String classname, int size, List<Find2MethodsArgs> forwarders) throws CannotCompileException {
/* 1067 */     FieldInfo finfo = new FieldInfo(cp, "_methods_", "[Ljava/lang/reflect/Method;");
/* 1068 */     finfo.setAccessFlags(10);
/* 1069 */     cf.addField(finfo);
/* 1070 */     MethodInfo minfo = new MethodInfo(cp, "<clinit>", "()V");
/* 1071 */     minfo.setAccessFlags(8);
/* 1072 */     setThrows(minfo, cp, new Class[] { ClassNotFoundException.class });
/*      */     
/* 1074 */     Bytecode code = new Bytecode(cp, 0, 2);
/* 1075 */     code.addIconst(size * 2);
/* 1076 */     code.addAnewarray("java.lang.reflect.Method");
/* 1077 */     int varArray = 0;
/* 1078 */     code.addAstore(0);
/*      */ 
/*      */ 
/*      */     
/* 1082 */     code.addLdc(classname);
/* 1083 */     code.addInvokestatic("java.lang.Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;");
/*      */     
/* 1085 */     int varClass = 1;
/* 1086 */     code.addAstore(1);
/*      */     
/* 1088 */     for (Find2MethodsArgs args : forwarders) {
/* 1089 */       callFind2Methods(code, args.methodName, args.delegatorName, args.origIndex, args.descriptor, 1, 0);
/*      */     }
/*      */     
/* 1092 */     code.addAload(0);
/* 1093 */     code.addPutstatic(classname, "_methods_", "[Ljava/lang/reflect/Method;");
/*      */     
/* 1095 */     code.addLconst(-1L);
/* 1096 */     code.addPutstatic(classname, "serialVersionUID", "J");
/* 1097 */     code.addOpcode(177);
/* 1098 */     minfo.setCodeAttribute(code.toCodeAttribute());
/* 1099 */     cf.addMethod(minfo);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void callFind2Methods(Bytecode code, String superMethod, String thisMethod, int index, String desc, int classVar, int arrayVar) {
/* 1107 */     String findClass = RuntimeSupport.class.getName();
/* 1108 */     String findDesc = "(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;[Ljava/lang/reflect/Method;)V";
/*      */ 
/*      */     
/* 1111 */     code.addAload(classVar);
/* 1112 */     code.addLdc(superMethod);
/* 1113 */     if (thisMethod == null) {
/* 1114 */       code.addOpcode(1);
/*      */     } else {
/* 1116 */       code.addLdc(thisMethod);
/*      */     } 
/* 1118 */     code.addIconst(index);
/* 1119 */     code.addLdc(desc);
/* 1120 */     code.addAload(arrayVar);
/* 1121 */     code.addInvokestatic(findClass, "find2Methods", findDesc);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static void addSetter(String classname, ClassFile cf, ConstPool cp) throws CannotCompileException {
/* 1127 */     MethodInfo minfo = new MethodInfo(cp, "setHandler", HANDLER_SETTER_TYPE);
/*      */     
/* 1129 */     minfo.setAccessFlags(1);
/* 1130 */     Bytecode code = new Bytecode(cp, 2, 2);
/* 1131 */     code.addAload(0);
/* 1132 */     code.addAload(1);
/* 1133 */     code.addPutfield(classname, "handler", HANDLER_TYPE);
/* 1134 */     code.addOpcode(177);
/* 1135 */     minfo.setCodeAttribute(code.toCodeAttribute());
/* 1136 */     cf.addMethod(minfo);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static void addGetter(String classname, ClassFile cf, ConstPool cp) throws CannotCompileException {
/* 1142 */     MethodInfo minfo = new MethodInfo(cp, "getHandler", HANDLER_GETTER_TYPE);
/*      */     
/* 1144 */     minfo.setAccessFlags(1);
/* 1145 */     Bytecode code = new Bytecode(cp, 1, 1);
/* 1146 */     code.addAload(0);
/* 1147 */     code.addGetfield(classname, "handler", HANDLER_TYPE);
/* 1148 */     code.addOpcode(176);
/* 1149 */     minfo.setCodeAttribute(code.toCodeAttribute());
/* 1150 */     cf.addMethod(minfo);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private int overrideMethods(ClassFile cf, ConstPool cp, String className, List<Find2MethodsArgs> forwarders) throws CannotCompileException {
/* 1156 */     String prefix = makeUniqueName("_d", this.signatureMethods);
/* 1157 */     Iterator<Map.Entry<String, Method>> it = this.signatureMethods.iterator();
/* 1158 */     int index = 0;
/* 1159 */     while (it.hasNext()) {
/* 1160 */       Map.Entry<String, Method> e = it.next();
/* 1161 */       if ((ClassFile.MAJOR_VERSION < 49 || !isBridge(e.getValue())) && 
/* 1162 */         testBit(this.signature, index)) {
/* 1163 */         override(className, e.getValue(), prefix, index, 
/* 1164 */             keyToDesc(e.getKey(), e.getValue()), cf, cp, forwarders);
/*      */       }
/*      */       
/* 1167 */       index++;
/*      */     } 
/*      */     
/* 1170 */     return index;
/*      */   }
/*      */   
/*      */   private static boolean isBridge(Method m) {
/* 1174 */     return m.isBridge();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void override(String thisClassname, Method meth, String prefix, int index, String desc, ClassFile cf, ConstPool cp, List<Find2MethodsArgs> forwarders) throws CannotCompileException {
/* 1182 */     Class<?> declClass = meth.getDeclaringClass();
/* 1183 */     String delegatorName = prefix + index + meth.getName();
/* 1184 */     if (Modifier.isAbstract(meth.getModifiers())) {
/* 1185 */       delegatorName = null;
/*      */     } else {
/*      */       
/* 1188 */       MethodInfo delegator = makeDelegator(meth, desc, cp, declClass, delegatorName);
/*      */       
/* 1190 */       delegator.setAccessFlags(delegator.getAccessFlags() & 0xFFFFFFBF);
/* 1191 */       cf.addMethod(delegator);
/*      */     } 
/*      */ 
/*      */     
/* 1195 */     MethodInfo forwarder = makeForwarder(thisClassname, meth, desc, cp, declClass, delegatorName, index, forwarders);
/*      */     
/* 1197 */     cf.addMethod(forwarder);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void makeConstructors(String thisClassName, ClassFile cf, ConstPool cp, String classname) throws CannotCompileException {
/* 1203 */     Constructor[] arrayOfConstructor = (Constructor[])SecurityActions.getDeclaredConstructors(this.superClass);
/*      */     
/* 1205 */     boolean doHandlerInit = !this.factoryUseCache;
/* 1206 */     for (int i = 0; i < arrayOfConstructor.length; i++) {
/* 1207 */       Constructor<?> c = arrayOfConstructor[i];
/* 1208 */       int mod = c.getModifiers();
/* 1209 */       if (!Modifier.isFinal(mod) && !Modifier.isPrivate(mod) && 
/* 1210 */         isVisible(mod, this.basename, c)) {
/* 1211 */         MethodInfo m = makeConstructor(thisClassName, c, cp, this.superClass, doHandlerInit);
/* 1212 */         cf.addMethod(m);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private static String makeUniqueName(String name, List<Map.Entry<String, Method>> sortedMethods) {
/* 1218 */     if (makeUniqueName0(name, sortedMethods.iterator())) {
/* 1219 */       return name;
/*      */     }
/* 1221 */     for (int i = 100; i < 999; i++) {
/* 1222 */       String s = name + i;
/* 1223 */       if (makeUniqueName0(s, sortedMethods.iterator())) {
/* 1224 */         return s;
/*      */       }
/*      */     } 
/* 1227 */     throw new RuntimeException("cannot make a unique method name");
/*      */   }
/*      */   
/*      */   private static boolean makeUniqueName0(String name, Iterator<Map.Entry<String, Method>> it) {
/* 1231 */     while (it.hasNext()) {
/* 1232 */       if (((String)((Map.Entry)it.next()).getKey()).startsWith(name))
/* 1233 */         return false; 
/* 1234 */     }  return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean isVisible(int mod, String from, Member meth) {
/* 1243 */     if ((mod & 0x2) != 0)
/* 1244 */       return false; 
/* 1245 */     if ((mod & 0x5) != 0) {
/* 1246 */       return true;
/*      */     }
/* 1248 */     String p = getPackageName(from);
/* 1249 */     String q = getPackageName(meth.getDeclaringClass().getName());
/* 1250 */     if (p == null)
/* 1251 */       return (q == null); 
/* 1252 */     return p.equals(q);
/*      */   }
/*      */ 
/*      */   
/*      */   private static String getPackageName(String name) {
/* 1257 */     int i = name.lastIndexOf('.');
/* 1258 */     if (i < 0)
/* 1259 */       return null; 
/* 1260 */     return name.substring(0, i);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Map<String, Method> getMethods(Class<?> superClass, Class<?>[] interfaceTypes) {
/* 1266 */     Map<String, Method> hash = new HashMap<>();
/* 1267 */     Set<Class<?>> set = new HashSet<>();
/* 1268 */     for (int i = 0; i < interfaceTypes.length; i++) {
/* 1269 */       getMethods(hash, interfaceTypes[i], set);
/*      */     }
/* 1271 */     getMethods(hash, superClass, set);
/* 1272 */     return hash;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void getMethods(Map<String, Method> hash, Class<?> clazz, Set<Class<?>> visitedClasses) {
/* 1278 */     if (!visitedClasses.add(clazz)) {
/*      */       return;
/*      */     }
/* 1281 */     Class<?>[] ifs = clazz.getInterfaces();
/* 1282 */     for (int i = 0; i < ifs.length; i++) {
/* 1283 */       getMethods(hash, ifs[i], visitedClasses);
/*      */     }
/* 1285 */     Class<?> parent = clazz.getSuperclass();
/* 1286 */     if (parent != null) {
/* 1287 */       getMethods(hash, parent, visitedClasses);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1294 */     Method[] methods = SecurityActions.getDeclaredMethods(clazz);
/* 1295 */     for (int j = 0; j < methods.length; j++) {
/* 1296 */       if (!Modifier.isPrivate(methods[j].getModifiers())) {
/* 1297 */         Method m = methods[j];
/* 1298 */         String key = m.getName() + ':' + RuntimeSupport.makeDescriptor(m);
/* 1299 */         if (key.startsWith("getHandler:()")) {
/* 1300 */           this.hasGetHandler = true;
/*      */         }
/*      */ 
/*      */         
/* 1304 */         Method oldMethod = hash.put(key, m);
/*      */ 
/*      */ 
/*      */         
/* 1308 */         if (null != oldMethod && isBridge(m) && 
/* 1309 */           !Modifier.isPublic(oldMethod.getDeclaringClass().getModifiers()) && 
/* 1310 */           !Modifier.isAbstract(oldMethod.getModifiers()) && !isDuplicated(j, methods)) {
/* 1311 */           hash.put(key, oldMethod);
/*      */         }
/*      */         
/* 1314 */         if (null != oldMethod && Modifier.isPublic(oldMethod.getModifiers()) && 
/* 1315 */           !Modifier.isPublic(m.getModifiers()))
/*      */         {
/*      */           
/* 1318 */           hash.put(key, oldMethod); } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private static boolean isDuplicated(int index, Method[] methods) {
/* 1324 */     String name = methods[index].getName();
/* 1325 */     for (int i = 0; i < methods.length; i++) {
/* 1326 */       if (i != index && 
/* 1327 */         name.equals(methods[i].getName()) && areParametersSame(methods[index], methods[i]))
/* 1328 */         return true; 
/*      */     } 
/* 1330 */     return false;
/*      */   }
/*      */   
/*      */   private static boolean areParametersSame(Method method, Method targetMethod) {
/* 1334 */     Class<?>[] methodTypes = method.getParameterTypes();
/* 1335 */     Class<?>[] targetMethodTypes = targetMethod.getParameterTypes();
/* 1336 */     if (methodTypes.length == targetMethodTypes.length) {
/* 1337 */       for (int i = 0; i < methodTypes.length; ) {
/* 1338 */         if (methodTypes[i].getName().equals(targetMethodTypes[i].getName())) {
/*      */           i++; continue;
/*      */         } 
/* 1341 */         return false;
/*      */       } 
/*      */       
/* 1344 */       return true;
/*      */     } 
/* 1346 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String keyToDesc(String key, Method m) {
/* 1353 */     return key.substring(key.indexOf(':') + 1);
/*      */   }
/*      */ 
/*      */   
/*      */   private static MethodInfo makeConstructor(String thisClassName, Constructor<?> cons, ConstPool cp, Class<?> superClass, boolean doHandlerInit) {
/* 1358 */     String desc = RuntimeSupport.makeDescriptor(cons.getParameterTypes(), void.class);
/*      */     
/* 1360 */     MethodInfo minfo = new MethodInfo(cp, "<init>", desc);
/* 1361 */     minfo.setAccessFlags(1);
/* 1362 */     setThrows(minfo, cp, cons.getExceptionTypes());
/* 1363 */     Bytecode code = new Bytecode(cp, 0, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1368 */     if (doHandlerInit) {
/* 1369 */       code.addAload(0);
/* 1370 */       code.addGetstatic(thisClassName, "default_interceptor", HANDLER_TYPE);
/* 1371 */       code.addPutfield(thisClassName, "handler", HANDLER_TYPE);
/* 1372 */       code.addGetstatic(thisClassName, "default_interceptor", HANDLER_TYPE);
/* 1373 */       code.addOpcode(199);
/* 1374 */       code.addIndex(10);
/*      */     } 
/*      */ 
/*      */     
/* 1378 */     code.addAload(0);
/* 1379 */     code.addGetstatic("javassist.util.proxy.RuntimeSupport", "default_interceptor", HANDLER_TYPE);
/* 1380 */     code.addPutfield(thisClassName, "handler", HANDLER_TYPE);
/* 1381 */     int pc = code.currentPc();
/*      */     
/* 1383 */     code.addAload(0);
/* 1384 */     int s = addLoadParameters(code, cons.getParameterTypes(), 1);
/* 1385 */     code.addInvokespecial(superClass.getName(), "<init>", desc);
/* 1386 */     code.addOpcode(177);
/* 1387 */     code.setMaxLocals(s + 1);
/* 1388 */     CodeAttribute ca = code.toCodeAttribute();
/* 1389 */     minfo.setCodeAttribute(ca);
/*      */     
/* 1391 */     StackMapTable.Writer writer = new StackMapTable.Writer(32);
/* 1392 */     writer.sameFrame(pc);
/* 1393 */     ca.setAttribute(writer.toStackMapTable(cp));
/* 1394 */     return minfo;
/*      */   }
/*      */ 
/*      */   
/*      */   private MethodInfo makeDelegator(Method meth, String desc, ConstPool cp, Class<?> declClass, String delegatorName) {
/* 1399 */     MethodInfo delegator = new MethodInfo(cp, delegatorName, desc);
/* 1400 */     delegator.setAccessFlags(0x11 | meth
/* 1401 */         .getModifiers() & 0xFFFFFAD9);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1406 */     setThrows(delegator, cp, meth);
/* 1407 */     Bytecode code = new Bytecode(cp, 0, 0);
/* 1408 */     code.addAload(0);
/* 1409 */     int s = addLoadParameters(code, meth.getParameterTypes(), 1);
/* 1410 */     Class<?> targetClass = invokespecialTarget(declClass);
/* 1411 */     code.addInvokespecial(targetClass.isInterface(), cp.addClassInfo(targetClass.getName()), meth
/* 1412 */         .getName(), desc);
/* 1413 */     addReturn(code, meth.getReturnType());
/* 1414 */     code.setMaxLocals(++s);
/* 1415 */     delegator.setCodeAttribute(code.toCodeAttribute());
/* 1416 */     return delegator;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Class<?> invokespecialTarget(Class<?> declClass) {
/* 1425 */     if (declClass.isInterface())
/* 1426 */       for (Class<?> i : this.interfaces) {
/* 1427 */         if (declClass.isAssignableFrom(i))
/* 1428 */           return i; 
/*      */       }  
/* 1430 */     return this.superClass;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static MethodInfo makeForwarder(String thisClassName, Method meth, String desc, ConstPool cp, Class<?> declClass, String delegatorName, int index, List<Find2MethodsArgs> forwarders) {
/* 1440 */     MethodInfo forwarder = new MethodInfo(cp, meth.getName(), desc);
/* 1441 */     forwarder.setAccessFlags(0x10 | meth
/* 1442 */         .getModifiers() & 0xFFFFFADF);
/*      */ 
/*      */     
/* 1445 */     setThrows(forwarder, cp, meth);
/* 1446 */     int args = Descriptor.paramSize(desc);
/* 1447 */     Bytecode code = new Bytecode(cp, 0, args + 2);
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
/* 1460 */     int origIndex = index * 2;
/* 1461 */     int delIndex = index * 2 + 1;
/* 1462 */     int arrayVar = args + 1;
/* 1463 */     code.addGetstatic(thisClassName, "_methods_", "[Ljava/lang/reflect/Method;");
/* 1464 */     code.addAstore(arrayVar);
/*      */     
/* 1466 */     forwarders.add(new Find2MethodsArgs(meth.getName(), delegatorName, desc, origIndex));
/*      */     
/* 1468 */     code.addAload(0);
/* 1469 */     code.addGetfield(thisClassName, "handler", HANDLER_TYPE);
/* 1470 */     code.addAload(0);
/*      */     
/* 1472 */     code.addAload(arrayVar);
/* 1473 */     code.addIconst(origIndex);
/* 1474 */     code.addOpcode(50);
/*      */     
/* 1476 */     code.addAload(arrayVar);
/* 1477 */     code.addIconst(delIndex);
/* 1478 */     code.addOpcode(50);
/*      */     
/* 1480 */     makeParameterList(code, meth.getParameterTypes());
/* 1481 */     code.addInvokeinterface(MethodHandler.class.getName(), "invoke", "(Ljava/lang/Object;Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;[Ljava/lang/Object;)Ljava/lang/Object;", 5);
/*      */ 
/*      */     
/* 1484 */     Class<?> retType = meth.getReturnType();
/* 1485 */     addUnwrapper(code, retType);
/* 1486 */     addReturn(code, retType);
/*      */     
/* 1488 */     CodeAttribute ca = code.toCodeAttribute();
/* 1489 */     forwarder.setCodeAttribute(ca);
/* 1490 */     return forwarder;
/*      */   }
/*      */   
/*      */   static class Find2MethodsArgs {
/*      */     String methodName;
/*      */     String delegatorName;
/*      */     
/*      */     Find2MethodsArgs(String mname, String dname, String desc, int index) {
/* 1498 */       this.methodName = mname;
/* 1499 */       this.delegatorName = dname;
/* 1500 */       this.descriptor = desc;
/* 1501 */       this.origIndex = index;
/*      */     }
/*      */     String descriptor; int origIndex; }
/*      */   
/*      */   private static void setThrows(MethodInfo minfo, ConstPool cp, Method orig) {
/* 1506 */     Class<?>[] exceptions = orig.getExceptionTypes();
/* 1507 */     setThrows(minfo, cp, exceptions);
/*      */   }
/*      */ 
/*      */   
/*      */   private static void setThrows(MethodInfo minfo, ConstPool cp, Class<?>[] exceptions) {
/* 1512 */     if (exceptions.length == 0) {
/*      */       return;
/*      */     }
/* 1515 */     String[] list = new String[exceptions.length];
/* 1516 */     for (int i = 0; i < exceptions.length; i++) {
/* 1517 */       list[i] = exceptions[i].getName();
/*      */     }
/* 1519 */     ExceptionsAttribute ea = new ExceptionsAttribute(cp);
/* 1520 */     ea.setExceptions(list);
/* 1521 */     minfo.setExceptionsAttribute(ea);
/*      */   }
/*      */ 
/*      */   
/*      */   private static int addLoadParameters(Bytecode code, Class<?>[] params, int offset) {
/* 1526 */     int stacksize = 0;
/* 1527 */     int n = params.length;
/* 1528 */     for (int i = 0; i < n; i++) {
/* 1529 */       stacksize += addLoad(code, stacksize + offset, params[i]);
/*      */     }
/* 1531 */     return stacksize;
/*      */   }
/*      */   
/*      */   private static int addLoad(Bytecode code, int n, Class<?> type) {
/* 1535 */     if (type.isPrimitive()) {
/* 1536 */       if (type == long.class) {
/* 1537 */         code.addLload(n);
/* 1538 */         return 2;
/*      */       } 
/* 1540 */       if (type == float.class)
/* 1541 */       { code.addFload(n); }
/* 1542 */       else { if (type == double.class) {
/* 1543 */           code.addDload(n);
/* 1544 */           return 2;
/*      */         } 
/*      */         
/* 1547 */         code.addIload(n); }
/*      */     
/*      */     } else {
/* 1550 */       code.addAload(n);
/*      */     } 
/* 1552 */     return 1;
/*      */   }
/*      */   
/*      */   private static int addReturn(Bytecode code, Class<?> type) {
/* 1556 */     if (type.isPrimitive()) {
/* 1557 */       if (type == long.class) {
/* 1558 */         code.addOpcode(173);
/* 1559 */         return 2;
/*      */       } 
/* 1561 */       if (type == float.class)
/* 1562 */       { code.addOpcode(174); }
/* 1563 */       else { if (type == double.class) {
/* 1564 */           code.addOpcode(175);
/* 1565 */           return 2;
/*      */         } 
/* 1567 */         if (type == void.class) {
/* 1568 */           code.addOpcode(177);
/* 1569 */           return 0;
/*      */         } 
/*      */         
/* 1572 */         code.addOpcode(172); }
/*      */     
/*      */     } else {
/* 1575 */       code.addOpcode(176);
/*      */     } 
/* 1577 */     return 1;
/*      */   }
/*      */   
/*      */   private static void makeParameterList(Bytecode code, Class<?>[] params) {
/* 1581 */     int regno = 1;
/* 1582 */     int n = params.length;
/* 1583 */     code.addIconst(n);
/* 1584 */     code.addAnewarray("java/lang/Object");
/* 1585 */     for (int i = 0; i < n; i++) {
/* 1586 */       code.addOpcode(89);
/* 1587 */       code.addIconst(i);
/* 1588 */       Class<?> type = params[i];
/* 1589 */       if (type.isPrimitive()) {
/* 1590 */         regno = makeWrapper(code, type, regno);
/*      */       } else {
/* 1592 */         code.addAload(regno);
/* 1593 */         regno++;
/*      */       } 
/*      */       
/* 1596 */       code.addOpcode(83);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static int makeWrapper(Bytecode code, Class<?> type, int regno) {
/* 1601 */     int index = FactoryHelper.typeIndex(type);
/* 1602 */     String wrapper = FactoryHelper.wrapperTypes[index];
/* 1603 */     code.addNew(wrapper);
/* 1604 */     code.addOpcode(89);
/* 1605 */     addLoad(code, regno, type);
/* 1606 */     code.addInvokespecial(wrapper, "<init>", FactoryHelper.wrapperDesc[index]);
/*      */     
/* 1608 */     return regno + FactoryHelper.dataSize[index];
/*      */   }
/*      */   
/*      */   private static void addUnwrapper(Bytecode code, Class<?> type) {
/* 1612 */     if (type.isPrimitive()) {
/* 1613 */       if (type == void.class) {
/* 1614 */         code.addOpcode(87);
/*      */       } else {
/* 1616 */         int index = FactoryHelper.typeIndex(type);
/* 1617 */         String wrapper = FactoryHelper.wrapperTypes[index];
/* 1618 */         code.addCheckcast(wrapper);
/* 1619 */         code.addInvokevirtual(wrapper, FactoryHelper.unwarpMethods[index], FactoryHelper.unwrapDesc[index]);
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 1625 */       code.addCheckcast(type.getName());
/*      */     } 
/*      */   }
/*      */   private static MethodInfo makeWriteReplace(ConstPool cp) {
/* 1629 */     MethodInfo minfo = new MethodInfo(cp, "writeReplace", "()Ljava/lang/Object;");
/* 1630 */     String[] list = new String[1];
/* 1631 */     list[0] = "java.io.ObjectStreamException";
/* 1632 */     ExceptionsAttribute ea = new ExceptionsAttribute(cp);
/* 1633 */     ea.setExceptions(list);
/* 1634 */     minfo.setExceptionsAttribute(ea);
/* 1635 */     Bytecode code = new Bytecode(cp, 0, 1);
/* 1636 */     code.addAload(0);
/* 1637 */     code.addInvokestatic("javassist.util.proxy.RuntimeSupport", "makeSerializedProxy", "(Ljava/lang/Object;)Ljavassist/util/proxy/SerializedProxy;");
/*      */ 
/*      */     
/* 1640 */     code.addOpcode(176);
/* 1641 */     minfo.setCodeAttribute(code.toCodeAttribute());
/* 1642 */     return minfo;
/*      */   }
/*      */   
/*      */   public static interface UniqueName {
/*      */     String get(String param1String);
/*      */   }
/*      */   
/*      */   public static interface ClassLoaderProvider {
/*      */     ClassLoader get(ProxyFactory param1ProxyFactory);
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassis\\util\proxy\ProxyFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */