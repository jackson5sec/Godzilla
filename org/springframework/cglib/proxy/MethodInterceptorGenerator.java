/*     */ package org.springframework.cglib.proxy;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.asm.Label;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.cglib.core.ClassEmitter;
/*     */ import org.springframework.cglib.core.ClassInfo;
/*     */ import org.springframework.cglib.core.CodeEmitter;
/*     */ import org.springframework.cglib.core.CollectionUtils;
/*     */ import org.springframework.cglib.core.Constants;
/*     */ import org.springframework.cglib.core.EmitUtils;
/*     */ import org.springframework.cglib.core.Local;
/*     */ import org.springframework.cglib.core.MethodInfo;
/*     */ import org.springframework.cglib.core.ObjectSwitchCallback;
/*     */ import org.springframework.cglib.core.Signature;
/*     */ import org.springframework.cglib.core.Transformer;
/*     */ import org.springframework.cglib.core.TypeUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ class MethodInterceptorGenerator
/*     */   implements CallbackGenerator
/*     */ {
/*  27 */   public static final MethodInterceptorGenerator INSTANCE = new MethodInterceptorGenerator();
/*     */   
/*     */   static final String EMPTY_ARGS_NAME = "CGLIB$emptyArgs";
/*     */   static final String FIND_PROXY_NAME = "CGLIB$findMethodProxy";
/*  31 */   static final Class[] FIND_PROXY_TYPES = new Class[] { Signature.class };
/*     */ 
/*     */   
/*  34 */   private static final Type ABSTRACT_METHOD_ERROR = TypeUtils.parseType("AbstractMethodError");
/*     */   
/*  36 */   private static final Type METHOD = TypeUtils.parseType("java.lang.reflect.Method");
/*     */   
/*  38 */   private static final Type REFLECT_UTILS = TypeUtils.parseType("org.springframework.cglib.core.ReflectUtils");
/*     */   
/*  40 */   private static final Type METHOD_PROXY = TypeUtils.parseType("org.springframework.cglib.proxy.MethodProxy");
/*     */   
/*  42 */   private static final Type METHOD_INTERCEPTOR = TypeUtils.parseType("org.springframework.cglib.proxy.MethodInterceptor");
/*     */   
/*  44 */   private static final Signature GET_DECLARED_METHODS = TypeUtils.parseSignature("java.lang.reflect.Method[] getDeclaredMethods()");
/*     */   
/*  46 */   private static final Signature GET_DECLARING_CLASS = TypeUtils.parseSignature("Class getDeclaringClass()");
/*     */   
/*  48 */   private static final Signature FIND_METHODS = TypeUtils.parseSignature("java.lang.reflect.Method[] findMethods(String[], java.lang.reflect.Method[])");
/*  49 */   private static final Signature MAKE_PROXY = new Signature("create", METHOD_PROXY, new Type[] { Constants.TYPE_CLASS, Constants.TYPE_CLASS, Constants.TYPE_STRING, Constants.TYPE_STRING, Constants.TYPE_STRING });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   private static final Signature INTERCEPT = new Signature("intercept", Constants.TYPE_OBJECT, new Type[] { Constants.TYPE_OBJECT, METHOD, Constants.TYPE_OBJECT_ARRAY, METHOD_PROXY });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  64 */   private static final Signature FIND_PROXY = new Signature("CGLIB$findMethodProxy", METHOD_PROXY, new Type[] { Constants.TYPE_SIGNATURE });
/*     */ 
/*     */   
/*  67 */   private static final Signature TO_STRING = TypeUtils.parseSignature("String toString()");
/*  68 */   private static final Transformer METHOD_TO_CLASS = new Transformer() {
/*     */       public Object transform(Object value) {
/*  70 */         return ((MethodInfo)value).getClassInfo();
/*     */       }
/*     */     };
/*     */   
/*  74 */   private static final Signature CSTRUCT_SIGNATURE = TypeUtils.parseConstructor("String, String");
/*     */   
/*     */   private String getMethodField(Signature impl) {
/*  77 */     return impl.getName() + "$Method";
/*     */   }
/*     */   private String getMethodProxyField(Signature impl) {
/*  80 */     return impl.getName() + "$Proxy";
/*     */   }
/*     */   
/*     */   public void generate(ClassEmitter ce, CallbackGenerator.Context context, List methods) {
/*  84 */     Map<Object, Object> sigMap = new HashMap<Object, Object>();
/*  85 */     for (Iterator<MethodInfo> it = methods.iterator(); it.hasNext(); ) {
/*  86 */       MethodInfo method = it.next();
/*  87 */       Signature sig = method.getSignature();
/*  88 */       Signature impl = context.getImplSignature(method);
/*     */       
/*  90 */       String methodField = getMethodField(impl);
/*  91 */       String methodProxyField = getMethodProxyField(impl);
/*     */       
/*  93 */       sigMap.put(sig.toString(), methodProxyField);
/*  94 */       ce.declare_field(26, methodField, METHOD, null);
/*  95 */       ce.declare_field(26, methodProxyField, METHOD_PROXY, null);
/*  96 */       ce.declare_field(26, "CGLIB$emptyArgs", Constants.TYPE_OBJECT_ARRAY, null);
/*     */ 
/*     */ 
/*     */       
/* 100 */       CodeEmitter e = ce.begin_method(16, impl, method
/*     */           
/* 102 */           .getExceptionTypes());
/* 103 */       superHelper(e, method, context);
/* 104 */       e.return_value();
/* 105 */       e.end_method();
/*     */ 
/*     */       
/* 108 */       e = context.beginMethod(ce, method);
/* 109 */       Label nullInterceptor = e.make_label();
/* 110 */       context.emitCallback(e, context.getIndex(method));
/* 111 */       e.dup();
/* 112 */       e.ifnull(nullInterceptor);
/*     */       
/* 114 */       e.load_this();
/* 115 */       e.getfield(methodField);
/*     */       
/* 117 */       if ((sig.getArgumentTypes()).length == 0) {
/* 118 */         e.getfield("CGLIB$emptyArgs");
/*     */       } else {
/* 120 */         e.create_arg_array();
/*     */       } 
/*     */       
/* 123 */       e.getfield(methodProxyField);
/* 124 */       e.invoke_interface(METHOD_INTERCEPTOR, INTERCEPT);
/* 125 */       e.unbox_or_zero(sig.getReturnType());
/* 126 */       e.return_value();
/*     */       
/* 128 */       e.mark(nullInterceptor);
/* 129 */       superHelper(e, method, context);
/* 130 */       e.return_value();
/* 131 */       e.end_method();
/*     */     } 
/* 133 */     generateFindProxy(ce, sigMap);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void superHelper(CodeEmitter e, MethodInfo method, CallbackGenerator.Context context) {
/* 138 */     if (TypeUtils.isAbstract(method.getModifiers())) {
/* 139 */       e.throw_exception(ABSTRACT_METHOD_ERROR, method.toString() + " is abstract");
/*     */     } else {
/* 141 */       e.load_this();
/* 142 */       context.emitLoadArgsAndInvoke(e, method);
/*     */     } 
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
/*     */   public void generateStatic(CodeEmitter e, CallbackGenerator.Context context, List methods) throws Exception {
/* 160 */     e.push(0);
/* 161 */     e.newarray();
/* 162 */     e.putfield("CGLIB$emptyArgs");
/*     */     
/* 164 */     Local thisclass = e.make_local();
/* 165 */     Local declaringclass = e.make_local();
/* 166 */     EmitUtils.load_class_this(e);
/* 167 */     e.store_local(thisclass);
/*     */     
/* 169 */     Map methodsByClass = CollectionUtils.bucket(methods, METHOD_TO_CLASS);
/* 170 */     for (Iterator<ClassInfo> i = methodsByClass.keySet().iterator(); i.hasNext(); ) {
/* 171 */       ClassInfo classInfo = i.next();
/*     */       
/* 173 */       List<MethodInfo> classMethods = (List)methodsByClass.get(classInfo);
/* 174 */       e.push(2 * classMethods.size());
/* 175 */       e.newarray(Constants.TYPE_STRING); int index;
/* 176 */       for (index = 0; index < classMethods.size(); index++) {
/* 177 */         MethodInfo method = classMethods.get(index);
/* 178 */         Signature sig = method.getSignature();
/* 179 */         e.dup();
/* 180 */         e.push(2 * index);
/* 181 */         e.push(sig.getName());
/* 182 */         e.aastore();
/* 183 */         e.dup();
/* 184 */         e.push(2 * index + 1);
/* 185 */         e.push(sig.getDescriptor());
/* 186 */         e.aastore();
/*     */       } 
/*     */       
/* 189 */       EmitUtils.load_class(e, classInfo.getType());
/* 190 */       e.dup();
/* 191 */       e.store_local(declaringclass);
/* 192 */       e.invoke_virtual(Constants.TYPE_CLASS, GET_DECLARED_METHODS);
/* 193 */       e.invoke_static(REFLECT_UTILS, FIND_METHODS);
/*     */       
/* 195 */       for (index = 0; index < classMethods.size(); index++) {
/* 196 */         MethodInfo method = classMethods.get(index);
/* 197 */         Signature sig = method.getSignature();
/* 198 */         Signature impl = context.getImplSignature(method);
/* 199 */         e.dup();
/* 200 */         e.push(index);
/* 201 */         e.array_load(METHOD);
/* 202 */         e.putfield(getMethodField(impl));
/*     */         
/* 204 */         e.load_local(declaringclass);
/* 205 */         e.load_local(thisclass);
/* 206 */         e.push(sig.getDescriptor());
/* 207 */         e.push(sig.getName());
/* 208 */         e.push(impl.getName());
/* 209 */         e.invoke_static(METHOD_PROXY, MAKE_PROXY);
/* 210 */         e.putfield(getMethodProxyField(impl));
/*     */       } 
/* 212 */       e.pop();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void generateFindProxy(ClassEmitter ce, final Map sigMap) {
/* 217 */     final CodeEmitter e = ce.begin_method(9, FIND_PROXY, null);
/*     */ 
/*     */     
/* 220 */     e.load_arg(0);
/* 221 */     e.invoke_virtual(Constants.TYPE_OBJECT, TO_STRING);
/* 222 */     ObjectSwitchCallback callback = new ObjectSwitchCallback() {
/*     */         public void processCase(Object key, Label end) {
/* 224 */           e.getfield((String)sigMap.get(key));
/* 225 */           e.return_value();
/*     */         }
/*     */         public void processDefault() {
/* 228 */           e.aconst_null();
/* 229 */           e.return_value();
/*     */         }
/*     */       };
/* 232 */     EmitUtils.string_switch(e, (String[])sigMap
/* 233 */         .keySet().toArray((Object[])new String[0]), 1, callback);
/*     */ 
/*     */     
/* 236 */     e.end_method();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\proxy\MethodInterceptorGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */