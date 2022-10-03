/*     */ package org.springframework.cglib.core;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.springframework.asm.ClassVisitor;
/*     */ import org.springframework.asm.Label;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.cglib.core.internal.CustomizerRegistry;
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
/*     */ public abstract class KeyFactory
/*     */ {
/*  61 */   private static final Signature GET_NAME = TypeUtils.parseSignature("String getName()");
/*     */ 
/*     */   
/*  64 */   private static final Signature GET_CLASS = TypeUtils.parseSignature("Class getClass()");
/*     */ 
/*     */   
/*  67 */   private static final Signature HASH_CODE = TypeUtils.parseSignature("int hashCode()");
/*     */ 
/*     */   
/*  70 */   private static final Signature EQUALS = TypeUtils.parseSignature("boolean equals(Object)");
/*     */ 
/*     */   
/*  73 */   private static final Signature TO_STRING = TypeUtils.parseSignature("String toString()");
/*     */ 
/*     */   
/*  76 */   private static final Signature APPEND_STRING = TypeUtils.parseSignature("StringBuffer append(String)");
/*     */ 
/*     */   
/*  79 */   private static final Type KEY_FACTORY = TypeUtils.parseType("org.springframework.cglib.core.KeyFactory");
/*     */ 
/*     */   
/*  82 */   private static final Signature GET_SORT = TypeUtils.parseSignature("int getSort()");
/*     */ 
/*     */   
/*  85 */   private static final int[] PRIMES = new int[] { 11, 73, 179, 331, 521, 787, 1213, 1823, 2609, 3691, 5189, 7247, 10037, 13931, 19289, 26627, 36683, 50441, 69403, 95401, 131129, 180179, 247501, 340057, 467063, 641371, 880603, 1209107, 1660097, 2279161, 3129011, 4295723, 5897291, 8095873, 11114263, 15257791, 20946017, 28754629, 39474179, 54189869, 74391461, 102123817, 140194277, 192456917, 264202273, 362693231, 497900099, 683510293, 938313161, 1288102441, 1768288259 };
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
/* 101 */   public static final Customizer CLASS_BY_NAME = new Customizer() {
/*     */       public void customize(CodeEmitter e, Type type) {
/* 103 */         if (type.equals(Constants.TYPE_CLASS)) {
/* 104 */           e.invoke_virtual(Constants.TYPE_CLASS, KeyFactory.GET_NAME);
/*     */         }
/*     */       }
/*     */     };
/*     */   
/* 109 */   public static final FieldTypeCustomizer STORE_CLASS_AS_STRING = new FieldTypeCustomizer() {
/*     */       public void customize(CodeEmitter e, int index, Type type) {
/* 111 */         if (type.equals(Constants.TYPE_CLASS))
/* 112 */           e.invoke_virtual(Constants.TYPE_CLASS, KeyFactory.GET_NAME); 
/*     */       }
/*     */       
/*     */       public Type getOutType(int index, Type type) {
/* 116 */         if (type.equals(Constants.TYPE_CLASS)) {
/* 117 */           return Constants.TYPE_STRING;
/*     */         }
/* 119 */         return type;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 127 */   public static final HashCodeCustomizer HASH_ASM_TYPE = new HashCodeCustomizer() {
/*     */       public boolean customize(CodeEmitter e, Type type) {
/* 129 */         if (Constants.TYPE_TYPE.equals(type)) {
/* 130 */           e.invoke_virtual(type, KeyFactory.GET_SORT);
/* 131 */           return true;
/*     */         } 
/* 133 */         return false;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 142 */   public static final Customizer OBJECT_BY_CLASS = new Customizer() {
/*     */       public void customize(CodeEmitter e, Type type) {
/* 144 */         e.invoke_virtual(Constants.TYPE_OBJECT, KeyFactory.GET_CLASS);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static KeyFactory create(Class keyInterface) {
/* 152 */     return create(keyInterface, null);
/*     */   }
/*     */   
/*     */   public static KeyFactory create(Class keyInterface, Customizer customizer) {
/* 156 */     return create(keyInterface.getClassLoader(), keyInterface, customizer);
/*     */   }
/*     */   
/*     */   public static KeyFactory create(Class keyInterface, KeyFactoryCustomizer first, List<KeyFactoryCustomizer> next) {
/* 160 */     return create(keyInterface.getClassLoader(), keyInterface, first, next);
/*     */   }
/*     */   
/*     */   public static KeyFactory create(ClassLoader loader, Class keyInterface, Customizer customizer) {
/* 164 */     return create(loader, keyInterface, customizer, Collections.emptyList());
/*     */   }
/*     */ 
/*     */   
/*     */   public static KeyFactory create(ClassLoader loader, Class keyInterface, KeyFactoryCustomizer customizer, List<KeyFactoryCustomizer> next) {
/* 169 */     Generator gen = new Generator();
/* 170 */     gen.setInterface(keyInterface);
/*     */     
/* 172 */     gen.setContextClass(keyInterface);
/*     */ 
/*     */     
/* 175 */     if (customizer != null) {
/* 176 */       gen.addCustomizer(customizer);
/*     */     }
/* 178 */     if (next != null && !next.isEmpty()) {
/* 179 */       for (KeyFactoryCustomizer keyFactoryCustomizer : next) {
/* 180 */         gen.addCustomizer(keyFactoryCustomizer);
/*     */       }
/*     */     }
/* 183 */     gen.setClassLoader(loader);
/* 184 */     return gen.create();
/*     */   }
/*     */   
/*     */   public static class Generator
/*     */     extends AbstractClassGenerator
/*     */   {
/* 190 */     private static final AbstractClassGenerator.Source SOURCE = new AbstractClassGenerator.Source(KeyFactory.class.getName());
/*     */     
/* 192 */     private static final Class[] KNOWN_CUSTOMIZER_TYPES = new Class[] { Customizer.class, FieldTypeCustomizer.class };
/*     */ 
/*     */     
/*     */     private Class keyInterface;
/*     */     
/* 197 */     private CustomizerRegistry customizers = new CustomizerRegistry(KNOWN_CUSTOMIZER_TYPES);
/*     */     
/*     */     private int constant;
/*     */     
/*     */     private int multiplier;
/*     */     
/*     */     public Generator() {
/* 204 */       super(SOURCE);
/*     */     }
/*     */     
/*     */     protected ClassLoader getDefaultClassLoader() {
/* 208 */       return this.keyInterface.getClassLoader();
/*     */     }
/*     */     
/*     */     protected ProtectionDomain getProtectionDomain() {
/* 212 */       return ReflectUtils.getProtectionDomain(this.keyInterface);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public void setCustomizer(Customizer customizer) {
/* 220 */       this.customizers = CustomizerRegistry.singleton(customizer);
/*     */     }
/*     */     
/*     */     public void addCustomizer(KeyFactoryCustomizer customizer) {
/* 224 */       this.customizers.add(customizer);
/*     */     }
/*     */     
/*     */     public <T> List<T> getCustomizers(Class<T> klass) {
/* 228 */       return this.customizers.get(klass);
/*     */     }
/*     */     
/*     */     public void setInterface(Class keyInterface) {
/* 232 */       this.keyInterface = keyInterface;
/*     */     }
/*     */     
/*     */     public KeyFactory create() {
/* 236 */       setNamePrefix(this.keyInterface.getName());
/* 237 */       return (KeyFactory)create(this.keyInterface.getName());
/*     */     }
/*     */     
/*     */     public void setHashConstant(int constant) {
/* 241 */       this.constant = constant;
/*     */     }
/*     */     
/*     */     public void setHashMultiplier(int multiplier) {
/* 245 */       this.multiplier = multiplier;
/*     */     }
/*     */     
/*     */     protected Object firstInstance(Class type) {
/* 249 */       return ReflectUtils.newInstance(type);
/*     */     }
/*     */     
/*     */     protected Object nextInstance(Object instance) {
/* 253 */       return instance;
/*     */     }
/*     */     
/*     */     public void generateClass(ClassVisitor v) {
/* 257 */       ClassEmitter ce = new ClassEmitter(v);
/*     */       
/* 259 */       Method newInstance = ReflectUtils.findNewInstance(this.keyInterface);
/* 260 */       if (!newInstance.getReturnType().equals(Object.class)) {
/* 261 */         throw new IllegalArgumentException("newInstance method must return Object");
/*     */       }
/*     */       
/* 264 */       Type[] parameterTypes = TypeUtils.getTypes(newInstance.getParameterTypes());
/* 265 */       ce.begin_class(52, 1, 
/*     */           
/* 267 */           getClassName(), KeyFactory
/* 268 */           .KEY_FACTORY, new Type[] {
/* 269 */             Type.getType(this.keyInterface) }, "<generated>");
/*     */       
/* 271 */       EmitUtils.null_constructor(ce);
/* 272 */       EmitUtils.factory_method(ce, ReflectUtils.getSignature(newInstance));
/*     */       
/* 274 */       int seed = 0;
/* 275 */       CodeEmitter e = ce.begin_method(1, 
/* 276 */           TypeUtils.parseConstructor(parameterTypes), null);
/*     */       
/* 278 */       e.load_this();
/* 279 */       e.super_invoke_constructor();
/* 280 */       e.load_this();
/* 281 */       List<FieldTypeCustomizer> fieldTypeCustomizers = getCustomizers(FieldTypeCustomizer.class);
/* 282 */       for (int i = 0; i < parameterTypes.length; i++) {
/* 283 */         Type parameterType = parameterTypes[i];
/* 284 */         Type fieldType = parameterType;
/* 285 */         for (FieldTypeCustomizer customizer : fieldTypeCustomizers) {
/* 286 */           fieldType = customizer.getOutType(i, fieldType);
/*     */         }
/* 288 */         seed += fieldType.hashCode();
/* 289 */         ce.declare_field(18, 
/* 290 */             getFieldName(i), fieldType, null);
/*     */ 
/*     */         
/* 293 */         e.dup();
/* 294 */         e.load_arg(i);
/* 295 */         for (FieldTypeCustomizer customizer : fieldTypeCustomizers) {
/* 296 */           customizer.customize(e, i, parameterType);
/*     */         }
/* 298 */         e.putfield(getFieldName(i));
/*     */       } 
/* 300 */       e.return_value();
/* 301 */       e.end_method();
/*     */ 
/*     */       
/* 304 */       e = ce.begin_method(1, KeyFactory.HASH_CODE, null);
/* 305 */       int hc = (this.constant != 0) ? this.constant : KeyFactory.PRIMES[Math.abs(seed) % KeyFactory.PRIMES.length];
/* 306 */       int hm = (this.multiplier != 0) ? this.multiplier : KeyFactory.PRIMES[Math.abs(seed * 13) % KeyFactory.PRIMES.length];
/* 307 */       e.push(hc);
/* 308 */       for (int j = 0; j < parameterTypes.length; j++) {
/* 309 */         e.load_this();
/* 310 */         e.getfield(getFieldName(j));
/* 311 */         EmitUtils.hash_code(e, parameterTypes[j], hm, this.customizers);
/*     */       } 
/* 313 */       e.return_value();
/* 314 */       e.end_method();
/*     */ 
/*     */       
/* 317 */       e = ce.begin_method(1, KeyFactory.EQUALS, null);
/* 318 */       Label fail = e.make_label();
/* 319 */       e.load_arg(0);
/* 320 */       e.instance_of_this();
/* 321 */       e.if_jump(153, fail); int k;
/* 322 */       for (k = 0; k < parameterTypes.length; k++) {
/* 323 */         e.load_this();
/* 324 */         e.getfield(getFieldName(k));
/* 325 */         e.load_arg(0);
/* 326 */         e.checkcast_this();
/* 327 */         e.getfield(getFieldName(k));
/* 328 */         EmitUtils.not_equals(e, parameterTypes[k], fail, this.customizers);
/*     */       } 
/* 330 */       e.push(1);
/* 331 */       e.return_value();
/* 332 */       e.mark(fail);
/* 333 */       e.push(0);
/* 334 */       e.return_value();
/* 335 */       e.end_method();
/*     */ 
/*     */       
/* 338 */       e = ce.begin_method(1, KeyFactory.TO_STRING, null);
/* 339 */       e.new_instance(Constants.TYPE_STRING_BUFFER);
/* 340 */       e.dup();
/* 341 */       e.invoke_constructor(Constants.TYPE_STRING_BUFFER);
/* 342 */       for (k = 0; k < parameterTypes.length; k++) {
/* 343 */         if (k > 0) {
/* 344 */           e.push(", ");
/* 345 */           e.invoke_virtual(Constants.TYPE_STRING_BUFFER, KeyFactory.APPEND_STRING);
/*     */         } 
/* 347 */         e.load_this();
/* 348 */         e.getfield(getFieldName(k));
/* 349 */         EmitUtils.append_string(e, parameterTypes[k], EmitUtils.DEFAULT_DELIMITERS, this.customizers);
/*     */       } 
/* 351 */       e.invoke_virtual(Constants.TYPE_STRING_BUFFER, KeyFactory.TO_STRING);
/* 352 */       e.return_value();
/* 353 */       e.end_method();
/*     */       
/* 355 */       ce.end_class();
/*     */     }
/*     */     
/*     */     private String getFieldName(int arg) {
/* 359 */       return "FIELD_" + arg;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\core\KeyFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */