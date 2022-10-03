/*      */ package org.springframework.cglib.proxy;
/*      */ 
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.security.ProtectionDomain;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import org.springframework.asm.ClassVisitor;
/*      */ import org.springframework.asm.Label;
/*      */ import org.springframework.asm.Type;
/*      */ import org.springframework.cglib.core.AbstractClassGenerator;
/*      */ import org.springframework.cglib.core.ClassEmitter;
/*      */ import org.springframework.cglib.core.CodeEmitter;
/*      */ import org.springframework.cglib.core.CodeGenerationException;
/*      */ import org.springframework.cglib.core.CollectionUtils;
/*      */ import org.springframework.cglib.core.Constants;
/*      */ import org.springframework.cglib.core.DuplicatesPredicate;
/*      */ import org.springframework.cglib.core.EmitUtils;
/*      */ import org.springframework.cglib.core.KeyFactory;
/*      */ import org.springframework.cglib.core.KeyFactoryCustomizer;
/*      */ import org.springframework.cglib.core.Local;
/*      */ import org.springframework.cglib.core.MethodInfo;
/*      */ import org.springframework.cglib.core.MethodInfoTransformer;
/*      */ import org.springframework.cglib.core.MethodWrapper;
/*      */ import org.springframework.cglib.core.ObjectSwitchCallback;
/*      */ import org.springframework.cglib.core.Predicate;
/*      */ import org.springframework.cglib.core.ProcessSwitchCallback;
/*      */ import org.springframework.cglib.core.ReflectUtils;
/*      */ import org.springframework.cglib.core.RejectModifierPredicate;
/*      */ import org.springframework.cglib.core.Signature;
/*      */ import org.springframework.cglib.core.Transformer;
/*      */ import org.springframework.cglib.core.TypeUtils;
/*      */ import org.springframework.cglib.core.VisibilityPredicate;
/*      */ import org.springframework.cglib.core.WeakCacheKey;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Enhancer
/*      */   extends AbstractClassGenerator
/*      */ {
/*   95 */   private static final CallbackFilter ALL_ZERO = new CallbackFilter() {
/*      */       public int accept(Method method) {
/*   97 */         return 0;
/*      */       }
/*      */     };
/*      */   
/*  101 */   private static final AbstractClassGenerator.Source SOURCE = new AbstractClassGenerator.Source(Enhancer.class.getName());
/*      */ 
/*      */   
/*  104 */   private static final EnhancerKey KEY_FACTORY = (EnhancerKey)KeyFactory.create(EnhancerKey.class, (KeyFactoryCustomizer)KeyFactory.HASH_ASM_TYPE, null);
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String BOUND_FIELD = "CGLIB$BOUND";
/*      */ 
/*      */   
/*      */   private static final String FACTORY_DATA_FIELD = "CGLIB$FACTORY_DATA";
/*      */ 
/*      */   
/*      */   private static final String THREAD_CALLBACKS_FIELD = "CGLIB$THREAD_CALLBACKS";
/*      */ 
/*      */   
/*      */   private static final String STATIC_CALLBACKS_FIELD = "CGLIB$STATIC_CALLBACKS";
/*      */ 
/*      */   
/*      */   private static final String SET_THREAD_CALLBACKS_NAME = "CGLIB$SET_THREAD_CALLBACKS";
/*      */ 
/*      */   
/*      */   private static final String SET_STATIC_CALLBACKS_NAME = "CGLIB$SET_STATIC_CALLBACKS";
/*      */ 
/*      */   
/*      */   private static final String CONSTRUCTED_FIELD = "CGLIB$CONSTRUCTED";
/*      */ 
/*      */   
/*      */   private static final String CALLBACK_FILTER_FIELD = "CGLIB$CALLBACK_FILTER";
/*      */ 
/*      */   
/*  132 */   private static final Type OBJECT_TYPE = TypeUtils.parseType("Object");
/*      */ 
/*      */   
/*  135 */   private static final Type FACTORY = TypeUtils.parseType("org.springframework.cglib.proxy.Factory");
/*      */ 
/*      */   
/*  138 */   private static final Type ILLEGAL_STATE_EXCEPTION = TypeUtils.parseType("IllegalStateException");
/*      */ 
/*      */   
/*  141 */   private static final Type ILLEGAL_ARGUMENT_EXCEPTION = TypeUtils.parseType("IllegalArgumentException");
/*      */ 
/*      */   
/*  144 */   private static final Type THREAD_LOCAL = TypeUtils.parseType("ThreadLocal");
/*      */ 
/*      */   
/*  147 */   private static final Type CALLBACK = TypeUtils.parseType("org.springframework.cglib.proxy.Callback");
/*      */ 
/*      */   
/*  150 */   private static final Type CALLBACK_ARRAY = Type.getType(Callback[].class);
/*      */ 
/*      */   
/*  153 */   private static final Signature CSTRUCT_NULL = TypeUtils.parseConstructor("");
/*      */   
/*  155 */   private static final Signature SET_THREAD_CALLBACKS = new Signature("CGLIB$SET_THREAD_CALLBACKS", Type.VOID_TYPE, new Type[] { CALLBACK_ARRAY });
/*      */ 
/*      */   
/*  158 */   private static final Signature SET_STATIC_CALLBACKS = new Signature("CGLIB$SET_STATIC_CALLBACKS", Type.VOID_TYPE, new Type[] { CALLBACK_ARRAY });
/*      */ 
/*      */   
/*  161 */   private static final Signature NEW_INSTANCE = new Signature("newInstance", Constants.TYPE_OBJECT, new Type[] { CALLBACK_ARRAY });
/*      */ 
/*      */   
/*  164 */   private static final Signature MULTIARG_NEW_INSTANCE = new Signature("newInstance", Constants.TYPE_OBJECT, new Type[] { Constants.TYPE_CLASS_ARRAY, Constants.TYPE_OBJECT_ARRAY, CALLBACK_ARRAY });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  171 */   private static final Signature SINGLE_NEW_INSTANCE = new Signature("newInstance", Constants.TYPE_OBJECT, new Type[] { CALLBACK });
/*      */ 
/*      */   
/*  174 */   private static final Signature SET_CALLBACK = new Signature("setCallback", Type.VOID_TYPE, new Type[] { Type.INT_TYPE, CALLBACK });
/*      */ 
/*      */   
/*  177 */   private static final Signature GET_CALLBACK = new Signature("getCallback", CALLBACK, new Type[] { Type.INT_TYPE });
/*      */ 
/*      */   
/*  180 */   private static final Signature SET_CALLBACKS = new Signature("setCallbacks", Type.VOID_TYPE, new Type[] { CALLBACK_ARRAY });
/*      */ 
/*      */   
/*  183 */   private static final Signature GET_CALLBACKS = new Signature("getCallbacks", CALLBACK_ARRAY, new Type[0]);
/*      */ 
/*      */ 
/*      */   
/*  187 */   private static final Signature THREAD_LOCAL_GET = TypeUtils.parseSignature("Object get()");
/*      */ 
/*      */   
/*  190 */   private static final Signature THREAD_LOCAL_SET = TypeUtils.parseSignature("void set(Object)");
/*      */ 
/*      */   
/*  193 */   private static final Signature BIND_CALLBACKS = TypeUtils.parseSignature("void CGLIB$BIND_CALLBACKS(Object)");
/*      */ 
/*      */ 
/*      */   
/*      */   private EnhancerFactoryData currentData;
/*      */ 
/*      */ 
/*      */   
/*      */   private Object currentKey;
/*      */ 
/*      */ 
/*      */   
/*      */   private Class[] interfaces;
/*      */ 
/*      */ 
/*      */   
/*      */   private CallbackFilter filter;
/*      */ 
/*      */ 
/*      */   
/*      */   private Callback[] callbacks;
/*      */ 
/*      */ 
/*      */   
/*      */   private Type[] callbackTypes;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean validateCallbackTypes;
/*      */ 
/*      */   
/*      */   private boolean classOnly;
/*      */ 
/*      */   
/*      */   private Class superclass;
/*      */ 
/*      */   
/*      */   private Class[] argumentTypes;
/*      */ 
/*      */   
/*      */   private Object[] arguments;
/*      */ 
/*      */   
/*      */   private boolean useFactory = true;
/*      */ 
/*      */   
/*      */   private Long serialVersionUID;
/*      */ 
/*      */   
/*      */   private boolean interceptDuringConstruction = true;
/*      */ 
/*      */ 
/*      */   
/*      */   public Enhancer() {
/*  247 */     super(SOURCE);
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
/*      */   public void setSuperclass(Class superclass) {
/*  260 */     if (superclass != null && superclass.isInterface()) {
/*  261 */       setInterfaces(new Class[] { superclass });
/*      */       
/*  263 */       setContextClass(superclass);
/*      */     
/*      */     }
/*  266 */     else if (superclass != null && superclass.equals(Object.class)) {
/*      */       
/*  268 */       this.superclass = null;
/*      */     } else {
/*      */       
/*  271 */       this.superclass = superclass;
/*      */       
/*  273 */       setContextClass(superclass);
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
/*      */   public void setInterfaces(Class[] interfaces) {
/*  285 */     this.interfaces = interfaces;
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
/*      */   public void setCallbackFilter(CallbackFilter filter) {
/*  297 */     this.filter = filter;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCallback(Callback callback) {
/*  308 */     setCallbacks(new Callback[] { callback });
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
/*      */   public void setCallbacks(Callback[] callbacks) {
/*  321 */     if (callbacks != null && callbacks.length == 0) {
/*  322 */       throw new IllegalArgumentException("Array cannot be empty");
/*      */     }
/*  324 */     this.callbacks = callbacks;
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
/*      */   public void setUseFactory(boolean useFactory) {
/*  337 */     this.useFactory = useFactory;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInterceptDuringConstruction(boolean interceptDuringConstruction) {
/*  347 */     this.interceptDuringConstruction = interceptDuringConstruction;
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
/*      */   public void setCallbackType(Class callbackType) {
/*  359 */     setCallbackTypes(new Class[] { callbackType });
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
/*      */   public void setCallbackTypes(Class[] callbackTypes) {
/*  372 */     if (callbackTypes != null && callbackTypes.length == 0) {
/*  373 */       throw new IllegalArgumentException("Array cannot be empty");
/*      */     }
/*  375 */     this.callbackTypes = CallbackInfo.determineTypes(callbackTypes);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object create() {
/*  385 */     this.classOnly = false;
/*  386 */     this.argumentTypes = null;
/*  387 */     return createHelper();
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
/*      */   public Object create(Class[] argumentTypes, Object[] arguments) {
/*  400 */     this.classOnly = false;
/*  401 */     if (argumentTypes == null || arguments == null || argumentTypes.length != arguments.length) {
/*  402 */       throw new IllegalArgumentException("Arguments must be non-null and of equal length");
/*      */     }
/*  404 */     this.argumentTypes = argumentTypes;
/*  405 */     this.arguments = arguments;
/*  406 */     return createHelper();
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
/*      */   public Class createClass() {
/*  418 */     this.classOnly = true;
/*  419 */     return (Class)createHelper();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSerialVersionUID(Long sUID) {
/*  427 */     this.serialVersionUID = sUID;
/*      */   }
/*      */   
/*      */   private void preValidate() {
/*  431 */     if (this.callbackTypes == null) {
/*  432 */       this.callbackTypes = CallbackInfo.determineTypes(this.callbacks, false);
/*  433 */       this.validateCallbackTypes = true;
/*      */     } 
/*  435 */     if (this.filter == null) {
/*  436 */       if (this.callbackTypes.length > 1) {
/*  437 */         throw new IllegalStateException("Multiple callback types possible but no filter specified");
/*      */       }
/*  439 */       this.filter = ALL_ZERO;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void validate() {
/*  444 */     if ((this.classOnly ^ ((this.callbacks == null) ? 1 : 0)) != 0) {
/*  445 */       if (this.classOnly) {
/*  446 */         throw new IllegalStateException("createClass does not accept callbacks");
/*      */       }
/*      */       
/*  449 */       throw new IllegalStateException("Callbacks are required");
/*      */     } 
/*      */     
/*  452 */     if (this.classOnly && this.callbackTypes == null) {
/*  453 */       throw new IllegalStateException("Callback types are required");
/*      */     }
/*  455 */     if (this.validateCallbackTypes) {
/*  456 */       this.callbackTypes = null;
/*      */     }
/*  458 */     if (this.callbacks != null && this.callbackTypes != null) {
/*  459 */       if (this.callbacks.length != this.callbackTypes.length) {
/*  460 */         throw new IllegalStateException("Lengths of callback and callback types array must be the same");
/*      */       }
/*  462 */       Type[] check = CallbackInfo.determineTypes(this.callbacks);
/*  463 */       for (int i = 0; i < check.length; i++) {
/*  464 */         if (!check[i].equals(this.callbackTypes[i])) {
/*  465 */           throw new IllegalStateException("Callback " + check[i] + " is not assignable to " + this.callbackTypes[i]);
/*      */         }
/*      */       }
/*      */     
/*  469 */     } else if (this.callbacks != null) {
/*  470 */       this.callbackTypes = CallbackInfo.determineTypes(this.callbacks);
/*      */     } 
/*  472 */     if (this.interfaces != null) {
/*  473 */       for (int i = 0; i < this.interfaces.length; i++) {
/*  474 */         if (this.interfaces[i] == null) {
/*  475 */           throw new IllegalStateException("Interfaces cannot be null");
/*      */         }
/*  477 */         if (!this.interfaces[i].isInterface()) {
/*  478 */           throw new IllegalStateException(this.interfaces[i] + " is not an interface");
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static class EnhancerFactoryData
/*      */   {
/*      */     public final Class generatedClass;
/*      */ 
/*      */     
/*      */     private final Method setThreadCallbacks;
/*      */ 
/*      */     
/*      */     private final Class[] primaryConstructorArgTypes;
/*      */     
/*      */     private final Constructor primaryConstructor;
/*      */ 
/*      */     
/*      */     public EnhancerFactoryData(Class generatedClass, Class[] primaryConstructorArgTypes, boolean classOnly) {
/*  500 */       this.generatedClass = generatedClass;
/*      */       try {
/*  502 */         this.setThreadCallbacks = Enhancer.getCallbacksSetter(generatedClass, "CGLIB$SET_THREAD_CALLBACKS");
/*  503 */         if (classOnly) {
/*  504 */           this.primaryConstructorArgTypes = null;
/*  505 */           this.primaryConstructor = null;
/*      */         } else {
/*      */           
/*  508 */           this.primaryConstructorArgTypes = primaryConstructorArgTypes;
/*  509 */           this.primaryConstructor = ReflectUtils.getConstructor(generatedClass, primaryConstructorArgTypes);
/*      */         }
/*      */       
/*  512 */       } catch (NoSuchMethodException e) {
/*  513 */         throw new CodeGenerationException(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Object newInstance(Class[] argumentTypes, Object[] arguments, Callback[] callbacks) {
/*  530 */       setThreadCallbacks(callbacks);
/*      */       
/*      */       try {
/*  533 */         if (this.primaryConstructorArgTypes == argumentTypes || 
/*  534 */           Arrays.equals((Object[])this.primaryConstructorArgTypes, (Object[])argumentTypes))
/*      */         {
/*      */           
/*  537 */           return ReflectUtils.newInstance(this.primaryConstructor, arguments);
/*      */         }
/*      */         
/*  540 */         return ReflectUtils.newInstance(this.generatedClass, argumentTypes, arguments);
/*      */       }
/*      */       finally {
/*      */         
/*  544 */         setThreadCallbacks(null);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     private void setThreadCallbacks(Callback[] callbacks) {
/*      */       try {
/*  551 */         this.setThreadCallbacks.invoke(this.generatedClass, new Object[] { callbacks });
/*      */       }
/*  553 */       catch (IllegalAccessException e) {
/*  554 */         throw new CodeGenerationException(e);
/*      */       }
/*  556 */       catch (InvocationTargetException e) {
/*  557 */         throw new CodeGenerationException(e.getTargetException());
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private Object createHelper() {
/*  563 */     preValidate();
/*  564 */     Object key = KEY_FACTORY.newInstance((this.superclass != null) ? this.superclass.getName() : null, 
/*  565 */         ReflectUtils.getNames(this.interfaces), (this.filter == ALL_ZERO) ? null : new WeakCacheKey(this.filter), this.callbackTypes, this.useFactory, this.interceptDuringConstruction, this.serialVersionUID);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  571 */     this.currentKey = key;
/*  572 */     Object result = create(key);
/*  573 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   protected Class generate(AbstractClassGenerator.ClassLoaderData data) {
/*  578 */     validate();
/*  579 */     if (this.superclass != null) {
/*  580 */       setNamePrefix(this.superclass.getName());
/*      */     }
/*  582 */     else if (this.interfaces != null) {
/*  583 */       setNamePrefix(this.interfaces[ReflectUtils.findPackageProtected(this.interfaces)].getName());
/*      */     } 
/*  585 */     return super.generate(data);
/*      */   }
/*      */   
/*      */   protected ClassLoader getDefaultClassLoader() {
/*  589 */     if (this.superclass != null) {
/*  590 */       return this.superclass.getClassLoader();
/*      */     }
/*  592 */     if (this.interfaces != null) {
/*  593 */       return this.interfaces[0].getClassLoader();
/*      */     }
/*      */     
/*  596 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   protected ProtectionDomain getProtectionDomain() {
/*  601 */     if (this.superclass != null) {
/*  602 */       return ReflectUtils.getProtectionDomain(this.superclass);
/*      */     }
/*  604 */     if (this.interfaces != null) {
/*  605 */       return ReflectUtils.getProtectionDomain(this.interfaces[0]);
/*      */     }
/*      */     
/*  608 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   private Signature rename(Signature sig, int index) {
/*  613 */     return new Signature("CGLIB$" + sig.getName() + "$" + index, sig
/*  614 */         .getDescriptor());
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
/*      */   public static void getMethods(Class superclass, Class[] interfaces, List methods) {
/*  631 */     getMethods(superclass, interfaces, methods, (List)null, (Set)null);
/*      */   }
/*      */   
/*      */   private static void getMethods(Class superclass, Class[] interfaces, List methods, List interfaceMethods, Set forcePublic) {
/*  635 */     ReflectUtils.addAllMethods(superclass, methods);
/*  636 */     List target = (interfaceMethods != null) ? interfaceMethods : methods;
/*  637 */     if (interfaces != null) {
/*  638 */       for (int i = 0; i < interfaces.length; i++) {
/*  639 */         if (interfaces[i] != Factory.class) {
/*  640 */           ReflectUtils.addAllMethods(interfaces[i], target);
/*      */         }
/*      */       } 
/*      */     }
/*  644 */     if (interfaceMethods != null) {
/*  645 */       if (forcePublic != null) {
/*  646 */         forcePublic.addAll(MethodWrapper.createSet(interfaceMethods));
/*      */       }
/*  648 */       methods.addAll(interfaceMethods);
/*      */     } 
/*  650 */     CollectionUtils.filter(methods, (Predicate)new RejectModifierPredicate(8));
/*  651 */     CollectionUtils.filter(methods, (Predicate)new VisibilityPredicate(superclass, true));
/*  652 */     CollectionUtils.filter(methods, (Predicate)new DuplicatesPredicate());
/*  653 */     CollectionUtils.filter(methods, (Predicate)new RejectModifierPredicate(16));
/*      */   }
/*      */   
/*      */   public void generateClass(ClassVisitor v) throws Exception {
/*  657 */     Class sc = (this.superclass == null) ? Object.class : this.superclass;
/*      */     
/*  659 */     if (TypeUtils.isFinal(sc.getModifiers()))
/*  660 */       throw new IllegalArgumentException("Cannot subclass final class " + sc.getName()); 
/*  661 */     List constructors = new ArrayList(Arrays.asList((Object[])sc.getDeclaredConstructors()));
/*  662 */     filterConstructors(sc, constructors);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  667 */     List actualMethods = new ArrayList();
/*  668 */     List interfaceMethods = new ArrayList();
/*  669 */     final Set forcePublic = new HashSet();
/*  670 */     getMethods(sc, this.interfaces, actualMethods, interfaceMethods, forcePublic);
/*      */     
/*  672 */     List methods = CollectionUtils.transform(actualMethods, new Transformer() {
/*      */           public Object transform(Object value) {
/*  674 */             Method method = (Method)value;
/*      */             
/*  676 */             int modifiers = 0x10 | method.getModifiers() & 0xFFFFFBFF & 0xFFFFFEFF & 0xFFFFFFDF;
/*      */ 
/*      */ 
/*      */             
/*  680 */             if (forcePublic.contains(MethodWrapper.create(method))) {
/*  681 */               modifiers = modifiers & 0xFFFFFFFB | 0x1;
/*      */             }
/*  683 */             return ReflectUtils.getMethodInfo(method, modifiers);
/*      */           }
/*      */         });
/*      */     
/*  687 */     ClassEmitter e = new ClassEmitter(v);
/*  688 */     if (this.currentData == null) {
/*  689 */       e.begin_class(52, 1, 
/*      */           
/*  691 */           getClassName(), 
/*  692 */           Type.getType(sc), this.useFactory ? 
/*      */           
/*  694 */           TypeUtils.add(TypeUtils.getTypes(this.interfaces), FACTORY) : 
/*  695 */           TypeUtils.getTypes(this.interfaces), "<generated>");
/*      */     }
/*      */     else {
/*      */       
/*  699 */       e.begin_class(52, 1, 
/*      */           
/*  701 */           getClassName(), null, new Type[] { FACTORY }, "<generated>");
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  706 */     List constructorInfo = CollectionUtils.transform(constructors, (Transformer)MethodInfoTransformer.getInstance());
/*      */     
/*  708 */     e.declare_field(2, "CGLIB$BOUND", Type.BOOLEAN_TYPE, null);
/*  709 */     e.declare_field(9, "CGLIB$FACTORY_DATA", OBJECT_TYPE, null);
/*  710 */     if (!this.interceptDuringConstruction) {
/*  711 */       e.declare_field(2, "CGLIB$CONSTRUCTED", Type.BOOLEAN_TYPE, null);
/*      */     }
/*  713 */     e.declare_field(26, "CGLIB$THREAD_CALLBACKS", THREAD_LOCAL, null);
/*  714 */     e.declare_field(26, "CGLIB$STATIC_CALLBACKS", CALLBACK_ARRAY, null);
/*  715 */     if (this.serialVersionUID != null) {
/*  716 */       e.declare_field(26, "serialVersionUID", Type.LONG_TYPE, this.serialVersionUID);
/*      */     }
/*      */     
/*  719 */     for (int i = 0; i < this.callbackTypes.length; i++) {
/*  720 */       e.declare_field(2, getCallbackField(i), this.callbackTypes[i], null);
/*      */     }
/*      */     
/*  723 */     e.declare_field(10, "CGLIB$CALLBACK_FILTER", OBJECT_TYPE, null);
/*      */     
/*  725 */     if (this.currentData == null) {
/*  726 */       emitMethods(e, methods, actualMethods);
/*  727 */       emitConstructors(e, constructorInfo);
/*      */     } else {
/*      */       
/*  730 */       emitDefaultConstructor(e);
/*      */     } 
/*  732 */     emitSetThreadCallbacks(e);
/*  733 */     emitSetStaticCallbacks(e);
/*  734 */     emitBindCallbacks(e);
/*      */     
/*  736 */     if (this.useFactory || this.currentData != null) {
/*  737 */       int[] keys = getCallbackKeys();
/*  738 */       emitNewInstanceCallbacks(e);
/*  739 */       emitNewInstanceCallback(e);
/*  740 */       emitNewInstanceMultiarg(e, constructorInfo);
/*  741 */       emitGetCallback(e, keys);
/*  742 */       emitSetCallback(e, keys);
/*  743 */       emitGetCallbacks(e);
/*  744 */       emitSetCallbacks(e);
/*      */     } 
/*      */     
/*  747 */     e.end_class();
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
/*      */   protected void filterConstructors(Class sc, List constructors) {
/*  761 */     CollectionUtils.filter(constructors, (Predicate)new VisibilityPredicate(sc, true));
/*  762 */     if (constructors.size() == 0) {
/*  763 */       throw new IllegalArgumentException("No visible constructors in " + sc);
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
/*      */   protected Object firstInstance(Class type) throws Exception {
/*  776 */     if (this.classOnly) {
/*  777 */       return type;
/*      */     }
/*      */     
/*  780 */     return createUsingReflection(type);
/*      */   }
/*      */ 
/*      */   
/*      */   protected Object nextInstance(Object instance) {
/*  785 */     EnhancerFactoryData data = (EnhancerFactoryData)instance;
/*      */     
/*  787 */     if (this.classOnly) {
/*  788 */       return data.generatedClass;
/*      */     }
/*      */     
/*  791 */     Class[] argumentTypes = this.argumentTypes;
/*  792 */     Object[] arguments = this.arguments;
/*  793 */     if (argumentTypes == null) {
/*  794 */       argumentTypes = Constants.EMPTY_CLASS_ARRAY;
/*  795 */       arguments = null;
/*      */     } 
/*  797 */     return data.newInstance(argumentTypes, arguments, this.callbacks);
/*      */   }
/*      */ 
/*      */   
/*      */   protected Object wrapCachedClass(Class klass) {
/*  802 */     Class[] argumentTypes = this.argumentTypes;
/*  803 */     if (argumentTypes == null) {
/*  804 */       argumentTypes = Constants.EMPTY_CLASS_ARRAY;
/*      */     }
/*  806 */     EnhancerFactoryData factoryData = new EnhancerFactoryData(klass, argumentTypes, this.classOnly);
/*  807 */     Field factoryDataField = null;
/*      */ 
/*      */     
/*      */     try {
/*  811 */       factoryDataField = klass.getField("CGLIB$FACTORY_DATA");
/*  812 */       factoryDataField.set((Object)null, factoryData);
/*  813 */       Field callbackFilterField = klass.getDeclaredField("CGLIB$CALLBACK_FILTER");
/*  814 */       callbackFilterField.setAccessible(true);
/*  815 */       callbackFilterField.set((Object)null, this.filter);
/*      */     }
/*  817 */     catch (NoSuchFieldException e) {
/*  818 */       throw new CodeGenerationException(e);
/*      */     }
/*  820 */     catch (IllegalAccessException e) {
/*  821 */       throw new CodeGenerationException(e);
/*      */     } 
/*  823 */     return new WeakReference<>(factoryData);
/*      */   }
/*      */ 
/*      */   
/*      */   protected Object unwrapCachedValue(Object cached) {
/*  828 */     if (this.currentKey instanceof EnhancerKey) {
/*  829 */       EnhancerFactoryData data = ((WeakReference<EnhancerFactoryData>)cached).get();
/*  830 */       return data;
/*      */     } 
/*  832 */     return super.unwrapCachedValue(cached);
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
/*      */   public static void registerCallbacks(Class generatedClass, Callback[] callbacks) {
/*  859 */     setThreadCallbacks(generatedClass, callbacks);
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
/*      */   public static void registerStaticCallbacks(Class generatedClass, Callback[] callbacks) {
/*  872 */     setCallbacksHelper(generatedClass, callbacks, "CGLIB$SET_STATIC_CALLBACKS");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isEnhanced(Class type) {
/*      */     try {
/*  882 */       getCallbacksSetter(type, "CGLIB$SET_THREAD_CALLBACKS");
/*  883 */       return true;
/*      */     }
/*  885 */     catch (NoSuchMethodException e) {
/*  886 */       return false;
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void setThreadCallbacks(Class type, Callback[] callbacks) {
/*  891 */     setCallbacksHelper(type, callbacks, "CGLIB$SET_THREAD_CALLBACKS");
/*      */   }
/*      */ 
/*      */   
/*      */   private static void setCallbacksHelper(Class type, Callback[] callbacks, String methodName) {
/*      */     try {
/*  897 */       Method setter = getCallbacksSetter(type, methodName);
/*  898 */       setter.invoke(null, new Object[] { callbacks });
/*      */     }
/*  900 */     catch (NoSuchMethodException e) {
/*  901 */       throw new IllegalArgumentException(type + " is not an enhanced class");
/*      */     }
/*  903 */     catch (IllegalAccessException e) {
/*  904 */       throw new CodeGenerationException(e);
/*      */     }
/*  906 */     catch (InvocationTargetException e) {
/*  907 */       throw new CodeGenerationException(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static Method getCallbacksSetter(Class type, String methodName) throws NoSuchMethodException {
/*  912 */     return type.getDeclaredMethod(methodName, new Class[] { Callback[].class });
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
/*      */   private Object createUsingReflection(Class type) {
/*  924 */     setThreadCallbacks(type, this.callbacks);
/*      */     
/*      */     try {
/*  927 */       if (this.argumentTypes != null)
/*      */       {
/*  929 */         return ReflectUtils.newInstance(type, this.argumentTypes, this.arguments);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  934 */       return ReflectUtils.newInstance(type);
/*      */     
/*      */     }
/*      */     finally {
/*      */ 
/*      */       
/*  940 */       setThreadCallbacks(type, (Callback[])null);
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
/*      */   public static Object create(Class type, Callback callback) {
/*  952 */     Enhancer e = new Enhancer();
/*  953 */     e.setSuperclass(type);
/*  954 */     e.setCallback(callback);
/*  955 */     return e.create();
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
/*      */   public static Object create(Class superclass, Class[] interfaces, Callback callback) {
/*  967 */     Enhancer e = new Enhancer();
/*  968 */     e.setSuperclass(superclass);
/*  969 */     e.setInterfaces(interfaces);
/*  970 */     e.setCallback(callback);
/*  971 */     return e.create();
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
/*      */   public static Object create(Class superclass, Class[] interfaces, CallbackFilter filter, Callback[] callbacks) {
/*  984 */     Enhancer e = new Enhancer();
/*  985 */     e.setSuperclass(superclass);
/*  986 */     e.setInterfaces(interfaces);
/*  987 */     e.setCallbackFilter(filter);
/*  988 */     e.setCallbacks(callbacks);
/*  989 */     return e.create();
/*      */   }
/*      */   
/*      */   private void emitDefaultConstructor(ClassEmitter ce) {
/*      */     Constructor<Object> declaredConstructor;
/*      */     try {
/*  995 */       declaredConstructor = Object.class.getDeclaredConstructor(new Class[0]);
/*      */     }
/*  997 */     catch (NoSuchMethodException noSuchMethodException) {
/*  998 */       throw new IllegalStateException("Object should have default constructor ", noSuchMethodException);
/*      */     } 
/* 1000 */     MethodInfo constructor = (MethodInfo)MethodInfoTransformer.getInstance().transform(declaredConstructor);
/* 1001 */     CodeEmitter e = EmitUtils.begin_method(ce, constructor, 1);
/* 1002 */     e.load_this();
/* 1003 */     e.dup();
/* 1004 */     Signature sig = constructor.getSignature();
/* 1005 */     e.super_invoke_constructor(sig);
/* 1006 */     e.return_value();
/* 1007 */     e.end_method();
/*      */   }
/*      */   
/*      */   private void emitConstructors(ClassEmitter ce, List constructors) {
/* 1011 */     boolean seenNull = false;
/* 1012 */     for (Iterator<MethodInfo> it = constructors.iterator(); it.hasNext(); ) {
/* 1013 */       MethodInfo constructor = it.next();
/* 1014 */       if (this.currentData != null && !"()V".equals(constructor.getSignature().getDescriptor())) {
/*      */         continue;
/*      */       }
/* 1017 */       CodeEmitter e = EmitUtils.begin_method(ce, constructor, 1);
/* 1018 */       e.load_this();
/* 1019 */       e.dup();
/* 1020 */       e.load_args();
/* 1021 */       Signature sig = constructor.getSignature();
/* 1022 */       seenNull = (seenNull || sig.getDescriptor().equals("()V"));
/* 1023 */       e.super_invoke_constructor(sig);
/* 1024 */       if (this.currentData == null) {
/* 1025 */         e.invoke_static_this(BIND_CALLBACKS);
/* 1026 */         if (!this.interceptDuringConstruction) {
/* 1027 */           e.load_this();
/* 1028 */           e.push(1);
/* 1029 */           e.putfield("CGLIB$CONSTRUCTED");
/*      */         } 
/*      */       } 
/* 1032 */       e.return_value();
/* 1033 */       e.end_method();
/*      */     } 
/* 1035 */     if (!this.classOnly && !seenNull && this.arguments == null)
/* 1036 */       throw new IllegalArgumentException("Superclass has no null constructors but no arguments were given"); 
/*      */   }
/*      */   
/*      */   private int[] getCallbackKeys() {
/* 1040 */     int[] keys = new int[this.callbackTypes.length];
/* 1041 */     for (int i = 0; i < this.callbackTypes.length; i++) {
/* 1042 */       keys[i] = i;
/*      */     }
/* 1044 */     return keys;
/*      */   }
/*      */   
/*      */   private void emitGetCallback(ClassEmitter ce, int[] keys) {
/* 1048 */     final CodeEmitter e = ce.begin_method(1, GET_CALLBACK, null);
/* 1049 */     e.load_this();
/* 1050 */     e.invoke_static_this(BIND_CALLBACKS);
/* 1051 */     e.load_this();
/* 1052 */     e.load_arg(0);
/* 1053 */     e.process_switch(keys, new ProcessSwitchCallback() {
/*      */           public void processCase(int key, Label end) {
/* 1055 */             e.getfield(Enhancer.getCallbackField(key));
/* 1056 */             e.goTo(end);
/*      */           }
/*      */           
/*      */           public void processDefault() {
/* 1060 */             e.pop();
/* 1061 */             e.aconst_null();
/*      */           }
/*      */         });
/* 1064 */     e.return_value();
/* 1065 */     e.end_method();
/*      */   }
/*      */   
/*      */   private void emitSetCallback(ClassEmitter ce, int[] keys) {
/* 1069 */     final CodeEmitter e = ce.begin_method(1, SET_CALLBACK, null);
/* 1070 */     e.load_arg(0);
/* 1071 */     e.process_switch(keys, new ProcessSwitchCallback() {
/*      */           public void processCase(int key, Label end) {
/* 1073 */             e.load_this();
/* 1074 */             e.load_arg(1);
/* 1075 */             e.checkcast(Enhancer.this.callbackTypes[key]);
/* 1076 */             e.putfield(Enhancer.getCallbackField(key));
/* 1077 */             e.goTo(end);
/*      */           }
/*      */ 
/*      */ 
/*      */           
/*      */           public void processDefault() {}
/*      */         });
/* 1084 */     e.return_value();
/* 1085 */     e.end_method();
/*      */   }
/*      */   
/*      */   private void emitSetCallbacks(ClassEmitter ce) {
/* 1089 */     CodeEmitter e = ce.begin_method(1, SET_CALLBACKS, null);
/* 1090 */     e.load_this();
/* 1091 */     e.load_arg(0);
/* 1092 */     for (int i = 0; i < this.callbackTypes.length; i++) {
/* 1093 */       e.dup2();
/* 1094 */       e.aaload(i);
/* 1095 */       e.checkcast(this.callbackTypes[i]);
/* 1096 */       e.putfield(getCallbackField(i));
/*      */     } 
/* 1098 */     e.return_value();
/* 1099 */     e.end_method();
/*      */   }
/*      */   
/*      */   private void emitGetCallbacks(ClassEmitter ce) {
/* 1103 */     CodeEmitter e = ce.begin_method(1, GET_CALLBACKS, null);
/* 1104 */     e.load_this();
/* 1105 */     e.invoke_static_this(BIND_CALLBACKS);
/* 1106 */     e.load_this();
/* 1107 */     e.push(this.callbackTypes.length);
/* 1108 */     e.newarray(CALLBACK);
/* 1109 */     for (int i = 0; i < this.callbackTypes.length; i++) {
/* 1110 */       e.dup();
/* 1111 */       e.push(i);
/* 1112 */       e.load_this();
/* 1113 */       e.getfield(getCallbackField(i));
/* 1114 */       e.aastore();
/*      */     } 
/* 1116 */     e.return_value();
/* 1117 */     e.end_method();
/*      */   }
/*      */   
/*      */   private void emitNewInstanceCallbacks(ClassEmitter ce) {
/* 1121 */     CodeEmitter e = ce.begin_method(1, NEW_INSTANCE, null);
/* 1122 */     Type thisType = getThisType(e);
/* 1123 */     e.load_arg(0);
/* 1124 */     e.invoke_static(thisType, SET_THREAD_CALLBACKS, false);
/* 1125 */     emitCommonNewInstance(e);
/*      */   }
/*      */   
/*      */   private Type getThisType(CodeEmitter e) {
/* 1129 */     if (this.currentData == null) {
/* 1130 */       return e.getClassEmitter().getClassType();
/*      */     }
/*      */     
/* 1133 */     return Type.getType(this.currentData.generatedClass);
/*      */   }
/*      */ 
/*      */   
/*      */   private void emitCommonNewInstance(CodeEmitter e) {
/* 1138 */     Type thisType = getThisType(e);
/* 1139 */     e.new_instance(thisType);
/* 1140 */     e.dup();
/* 1141 */     e.invoke_constructor(thisType);
/* 1142 */     e.aconst_null();
/* 1143 */     e.invoke_static(thisType, SET_THREAD_CALLBACKS, false);
/* 1144 */     e.return_value();
/* 1145 */     e.end_method();
/*      */   }
/*      */   
/*      */   private void emitNewInstanceCallback(ClassEmitter ce) {
/* 1149 */     CodeEmitter e = ce.begin_method(1, SINGLE_NEW_INSTANCE, null);
/* 1150 */     switch (this.callbackTypes.length) {
/*      */       case 0:
/*      */         break;
/*      */ 
/*      */       
/*      */       case 1:
/* 1156 */         e.push(1);
/* 1157 */         e.newarray(CALLBACK);
/* 1158 */         e.dup();
/* 1159 */         e.push(0);
/* 1160 */         e.load_arg(0);
/* 1161 */         e.aastore();
/* 1162 */         e.invoke_static(getThisType(e), SET_THREAD_CALLBACKS, false);
/*      */         break;
/*      */       default:
/* 1165 */         e.throw_exception(ILLEGAL_STATE_EXCEPTION, "More than one callback object required"); break;
/*      */     } 
/* 1167 */     emitCommonNewInstance(e);
/*      */   }
/*      */   
/*      */   private void emitNewInstanceMultiarg(ClassEmitter ce, List constructors) {
/* 1171 */     final CodeEmitter e = ce.begin_method(1, MULTIARG_NEW_INSTANCE, null);
/* 1172 */     final Type thisType = getThisType(e);
/* 1173 */     e.load_arg(2);
/* 1174 */     e.invoke_static(thisType, SET_THREAD_CALLBACKS, false);
/* 1175 */     e.new_instance(thisType);
/* 1176 */     e.dup();
/* 1177 */     e.load_arg(0);
/* 1178 */     EmitUtils.constructor_switch(e, constructors, new ObjectSwitchCallback() {
/*      */           public void processCase(Object key, Label end) {
/* 1180 */             MethodInfo constructor = (MethodInfo)key;
/* 1181 */             Type[] types = constructor.getSignature().getArgumentTypes();
/* 1182 */             for (int i = 0; i < types.length; i++) {
/* 1183 */               e.load_arg(1);
/* 1184 */               e.push(i);
/* 1185 */               e.aaload();
/* 1186 */               e.unbox(types[i]);
/*      */             } 
/* 1188 */             e.invoke_constructor(thisType, constructor.getSignature());
/* 1189 */             e.goTo(end);
/*      */           }
/*      */           
/*      */           public void processDefault() {
/* 1193 */             e.throw_exception(Enhancer.ILLEGAL_ARGUMENT_EXCEPTION, "Constructor not found");
/*      */           }
/*      */         });
/* 1196 */     e.aconst_null();
/* 1197 */     e.invoke_static(thisType, SET_THREAD_CALLBACKS, false);
/* 1198 */     e.return_value();
/* 1199 */     e.end_method();
/*      */   }
/*      */   
/*      */   private void emitMethods(ClassEmitter ce, List methods, List<E> actualMethods) {
/* 1203 */     CallbackGenerator[] generators = CallbackInfo.getGenerators(this.callbackTypes);
/*      */     
/* 1205 */     Map<Object, Object> groups = new HashMap<>();
/* 1206 */     final Map<Object, Object> indexes = new HashMap<>();
/* 1207 */     final Map<Object, Object> originalModifiers = new HashMap<>();
/* 1208 */     final Map positions = CollectionUtils.getIndexMap(methods);
/* 1209 */     Map<Object, Object> declToBridge = new HashMap<>();
/*      */     
/* 1211 */     Iterator<MethodInfo> it1 = methods.iterator();
/* 1212 */     Iterator<E> it2 = (actualMethods != null) ? actualMethods.iterator() : null;
/*      */     
/* 1214 */     while (it1.hasNext()) {
/* 1215 */       MethodInfo method = it1.next();
/* 1216 */       Method actualMethod = (it2 != null) ? (Method)it2.next() : null;
/* 1217 */       int index = this.filter.accept(actualMethod);
/* 1218 */       if (index >= this.callbackTypes.length) {
/* 1219 */         throw new IllegalArgumentException("Callback filter returned an index that is too large: " + index);
/*      */       }
/* 1221 */       originalModifiers.put(method, Integer.valueOf((actualMethod != null) ? actualMethod.getModifiers() : method.getModifiers()));
/* 1222 */       indexes.put(method, Integer.valueOf(index));
/* 1223 */       List<MethodInfo> group = (List)groups.get(generators[index]);
/* 1224 */       if (group == null) {
/* 1225 */         groups.put(generators[index], group = new ArrayList(methods.size()));
/*      */       }
/* 1227 */       group.add(method);
/*      */ 
/*      */ 
/*      */       
/* 1231 */       if (TypeUtils.isBridge(actualMethod.getModifiers())) {
/* 1232 */         Set<Signature> bridges = (Set)declToBridge.get(actualMethod.getDeclaringClass());
/* 1233 */         if (bridges == null) {
/* 1234 */           bridges = new HashSet();
/* 1235 */           declToBridge.put(actualMethod.getDeclaringClass(), bridges);
/*      */         } 
/* 1237 */         bridges.add(method.getSignature());
/*      */       } 
/*      */     } 
/*      */     
/* 1241 */     final Map bridgeToTarget = (new BridgeMethodResolver(declToBridge, getClassLoader())).resolveAll();
/*      */     
/* 1243 */     Set<CallbackGenerator> seenGen = new HashSet();
/* 1244 */     CodeEmitter se = ce.getStaticHook();
/* 1245 */     se.new_instance(THREAD_LOCAL);
/* 1246 */     se.dup();
/* 1247 */     se.invoke_constructor(THREAD_LOCAL, CSTRUCT_NULL);
/* 1248 */     se.putfield("CGLIB$THREAD_CALLBACKS");
/*      */     
/* 1250 */     Object[] state = new Object[1];
/* 1251 */     CallbackGenerator.Context context = new CallbackGenerator.Context() {
/*      */         public ClassLoader getClassLoader() {
/* 1253 */           return Enhancer.this.getClassLoader();
/*      */         }
/*      */         
/*      */         public int getOriginalModifiers(MethodInfo method) {
/* 1257 */           return ((Integer)originalModifiers.get(method)).intValue();
/*      */         }
/*      */         
/*      */         public int getIndex(MethodInfo method) {
/* 1261 */           return ((Integer)indexes.get(method)).intValue();
/*      */         }
/*      */         
/*      */         public void emitCallback(CodeEmitter e, int index) {
/* 1265 */           Enhancer.this.emitCurrentCallback(e, index);
/*      */         }
/*      */         
/*      */         public Signature getImplSignature(MethodInfo method) {
/* 1269 */           return Enhancer.this.rename(method.getSignature(), ((Integer)positions.get(method)).intValue());
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         public void emitLoadArgsAndInvoke(CodeEmitter e, MethodInfo method) {
/* 1277 */           Signature bridgeTarget = (Signature)bridgeToTarget.get(method.getSignature());
/* 1278 */           if (bridgeTarget != null) {
/*      */             
/* 1280 */             for (int i = 0; i < (bridgeTarget.getArgumentTypes()).length; i++) {
/* 1281 */               e.load_arg(i);
/* 1282 */               Type target = bridgeTarget.getArgumentTypes()[i];
/* 1283 */               if (!target.equals(method.getSignature().getArgumentTypes()[i])) {
/* 1284 */                 e.checkcast(target);
/*      */               }
/*      */             } 
/*      */             
/* 1288 */             e.invoke_virtual_this(bridgeTarget);
/*      */             
/* 1290 */             Type retType = method.getSignature().getReturnType();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1301 */             if (!retType.equals(bridgeTarget.getReturnType())) {
/* 1302 */               e.checkcast(retType);
/*      */             }
/*      */           } else {
/*      */             
/* 1306 */             e.load_args();
/* 1307 */             e.super_invoke(method.getSignature());
/*      */           } 
/*      */         }
/*      */         
/*      */         public CodeEmitter beginMethod(ClassEmitter ce, MethodInfo method) {
/* 1312 */           CodeEmitter e = EmitUtils.begin_method(ce, method);
/* 1313 */           if (!Enhancer.this.interceptDuringConstruction && 
/* 1314 */             !TypeUtils.isAbstract(method.getModifiers())) {
/* 1315 */             Label constructed = e.make_label();
/* 1316 */             e.load_this();
/* 1317 */             e.getfield("CGLIB$CONSTRUCTED");
/* 1318 */             e.if_jump(154, constructed);
/* 1319 */             e.load_this();
/* 1320 */             e.load_args();
/* 1321 */             e.super_invoke();
/* 1322 */             e.return_value();
/* 1323 */             e.mark(constructed);
/*      */           } 
/* 1325 */           return e;
/*      */         }
/*      */       };
/* 1328 */     for (int i = 0; i < this.callbackTypes.length; i++) {
/* 1329 */       CallbackGenerator gen = generators[i];
/* 1330 */       if (!seenGen.contains(gen)) {
/* 1331 */         seenGen.add(gen);
/* 1332 */         List fmethods = (List)groups.get(gen);
/* 1333 */         if (fmethods != null) {
/*      */           try {
/* 1335 */             gen.generate(ce, context, fmethods);
/* 1336 */             gen.generateStatic(se, context, fmethods);
/*      */           }
/* 1338 */           catch (RuntimeException x) {
/* 1339 */             throw x;
/*      */           }
/* 1341 */           catch (Exception x) {
/* 1342 */             throw new CodeGenerationException(x);
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/* 1347 */     se.return_value();
/* 1348 */     se.end_method();
/*      */   }
/*      */   
/*      */   private void emitSetThreadCallbacks(ClassEmitter ce) {
/* 1352 */     CodeEmitter e = ce.begin_method(9, SET_THREAD_CALLBACKS, null);
/*      */ 
/*      */     
/* 1355 */     e.getfield("CGLIB$THREAD_CALLBACKS");
/* 1356 */     e.load_arg(0);
/* 1357 */     e.invoke_virtual(THREAD_LOCAL, THREAD_LOCAL_SET);
/* 1358 */     e.return_value();
/* 1359 */     e.end_method();
/*      */   }
/*      */   
/*      */   private void emitSetStaticCallbacks(ClassEmitter ce) {
/* 1363 */     CodeEmitter e = ce.begin_method(9, SET_STATIC_CALLBACKS, null);
/*      */ 
/*      */     
/* 1366 */     e.load_arg(0);
/* 1367 */     e.putfield("CGLIB$STATIC_CALLBACKS");
/* 1368 */     e.return_value();
/* 1369 */     e.end_method();
/*      */   }
/*      */   
/*      */   private void emitCurrentCallback(CodeEmitter e, int index) {
/* 1373 */     e.load_this();
/* 1374 */     e.getfield(getCallbackField(index));
/* 1375 */     e.dup();
/* 1376 */     Label end = e.make_label();
/* 1377 */     e.ifnonnull(end);
/* 1378 */     e.pop();
/* 1379 */     e.load_this();
/* 1380 */     e.invoke_static_this(BIND_CALLBACKS);
/* 1381 */     e.load_this();
/* 1382 */     e.getfield(getCallbackField(index));
/* 1383 */     e.mark(end);
/*      */   }
/*      */   
/*      */   private void emitBindCallbacks(ClassEmitter ce) {
/* 1387 */     CodeEmitter e = ce.begin_method(26, BIND_CALLBACKS, null);
/*      */ 
/*      */     
/* 1390 */     Local me = e.make_local();
/* 1391 */     e.load_arg(0);
/* 1392 */     e.checkcast_this();
/* 1393 */     e.store_local(me);
/*      */     
/* 1395 */     Label end = e.make_label();
/* 1396 */     e.load_local(me);
/* 1397 */     e.getfield("CGLIB$BOUND");
/* 1398 */     e.if_jump(154, end);
/* 1399 */     e.load_local(me);
/* 1400 */     e.push(1);
/* 1401 */     e.putfield("CGLIB$BOUND");
/*      */     
/* 1403 */     e.getfield("CGLIB$THREAD_CALLBACKS");
/* 1404 */     e.invoke_virtual(THREAD_LOCAL, THREAD_LOCAL_GET);
/* 1405 */     e.dup();
/* 1406 */     Label found_callback = e.make_label();
/* 1407 */     e.ifnonnull(found_callback);
/* 1408 */     e.pop();
/*      */     
/* 1410 */     e.getfield("CGLIB$STATIC_CALLBACKS");
/* 1411 */     e.dup();
/* 1412 */     e.ifnonnull(found_callback);
/* 1413 */     e.pop();
/* 1414 */     e.goTo(end);
/*      */     
/* 1416 */     e.mark(found_callback);
/* 1417 */     e.checkcast(CALLBACK_ARRAY);
/* 1418 */     e.load_local(me);
/* 1419 */     e.swap();
/* 1420 */     for (int i = this.callbackTypes.length - 1; i >= 0; i--) {
/* 1421 */       if (i != 0) {
/* 1422 */         e.dup2();
/*      */       }
/* 1424 */       e.aaload(i);
/* 1425 */       e.checkcast(this.callbackTypes[i]);
/* 1426 */       e.putfield(getCallbackField(i));
/*      */     } 
/*      */     
/* 1429 */     e.mark(end);
/* 1430 */     e.return_value();
/* 1431 */     e.end_method();
/*      */   }
/*      */   
/*      */   private static String getCallbackField(int index) {
/* 1435 */     return "CGLIB$CALLBACK_" + index;
/*      */   }
/*      */   
/*      */   public static interface EnhancerKey {
/*      */     Object newInstance(String param1String, String[] param1ArrayOfString, WeakCacheKey<CallbackFilter> param1WeakCacheKey, Type[] param1ArrayOfType, boolean param1Boolean1, boolean param1Boolean2, Long param1Long);
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\proxy\Enhancer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */