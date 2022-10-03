/*     */ package org.springframework.cglib.reflect;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.ProtectionDomain;
/*     */ import org.springframework.asm.ClassVisitor;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.cglib.core.AbstractClassGenerator;
/*     */ import org.springframework.cglib.core.ClassEmitter;
/*     */ import org.springframework.cglib.core.CodeEmitter;
/*     */ import org.springframework.cglib.core.Constants;
/*     */ import org.springframework.cglib.core.EmitUtils;
/*     */ import org.springframework.cglib.core.KeyFactory;
/*     */ import org.springframework.cglib.core.MethodInfo;
/*     */ import org.springframework.cglib.core.ReflectUtils;
/*     */ import org.springframework.cglib.core.Signature;
/*     */ import org.springframework.cglib.core.TypeUtils;
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
/*     */ public abstract class MethodDelegate
/*     */ {
/* 110 */   private static final MethodDelegateKey KEY_FACTORY = (MethodDelegateKey)KeyFactory.create(MethodDelegateKey.class, KeyFactory.CLASS_BY_NAME);
/*     */ 
/*     */   
/*     */   protected Object target;
/*     */ 
/*     */   
/*     */   protected String eqMethod;
/*     */ 
/*     */   
/*     */   public static MethodDelegate createStatic(Class targetClass, String methodName, Class iface) {
/* 120 */     Generator gen = new Generator();
/* 121 */     gen.setTargetClass(targetClass);
/* 122 */     gen.setMethodName(methodName);
/* 123 */     gen.setInterface(iface);
/* 124 */     return gen.create();
/*     */   }
/*     */   
/*     */   public static MethodDelegate create(Object target, String methodName, Class iface) {
/* 128 */     Generator gen = new Generator();
/* 129 */     gen.setTarget(target);
/* 130 */     gen.setMethodName(methodName);
/* 131 */     gen.setInterface(iface);
/* 132 */     return gen.create();
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/* 136 */     MethodDelegate other = (MethodDelegate)obj;
/* 137 */     return (other != null && this.target == other.target && this.eqMethod.equals(other.eqMethod));
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 141 */     return this.target.hashCode() ^ this.eqMethod.hashCode();
/*     */   }
/*     */   
/*     */   public Object getTarget() {
/* 145 */     return this.target;
/*     */   }
/*     */   
/*     */   public abstract MethodDelegate newInstance(Object paramObject);
/*     */   
/*     */   public static class Generator extends AbstractClassGenerator {
/* 151 */     private static final AbstractClassGenerator.Source SOURCE = new AbstractClassGenerator.Source(MethodDelegate.class.getName());
/*     */     
/* 153 */     private static final Type METHOD_DELEGATE = TypeUtils.parseType("org.springframework.cglib.reflect.MethodDelegate");
/* 154 */     private static final Signature NEW_INSTANCE = new Signature("newInstance", METHOD_DELEGATE, new Type[] { Constants.TYPE_OBJECT });
/*     */     
/*     */     private Object target;
/*     */     
/*     */     private Class targetClass;
/*     */     private String methodName;
/*     */     private Class iface;
/*     */     
/*     */     public Generator() {
/* 163 */       super(SOURCE);
/*     */     }
/*     */     
/*     */     public void setTarget(Object target) {
/* 167 */       this.target = target;
/* 168 */       this.targetClass = target.getClass();
/*     */     }
/*     */     
/*     */     public void setTargetClass(Class targetClass) {
/* 172 */       this.targetClass = targetClass;
/*     */     }
/*     */     
/*     */     public void setMethodName(String methodName) {
/* 176 */       this.methodName = methodName;
/*     */     }
/*     */     
/*     */     public void setInterface(Class iface) {
/* 180 */       this.iface = iface;
/*     */     }
/*     */     
/*     */     protected ClassLoader getDefaultClassLoader() {
/* 184 */       return this.targetClass.getClassLoader();
/*     */     }
/*     */     
/*     */     protected ProtectionDomain getProtectionDomain() {
/* 188 */       return ReflectUtils.getProtectionDomain(this.targetClass);
/*     */     }
/*     */     
/*     */     public MethodDelegate create() {
/* 192 */       setNamePrefix(this.targetClass.getName());
/* 193 */       Object key = MethodDelegate.KEY_FACTORY.newInstance(this.targetClass, this.methodName, this.iface);
/* 194 */       return (MethodDelegate)create(key);
/*     */     }
/*     */     
/*     */     protected Object firstInstance(Class type) {
/* 198 */       return ((MethodDelegate)ReflectUtils.newInstance(type)).newInstance(this.target);
/*     */     }
/*     */     
/*     */     protected Object nextInstance(Object instance) {
/* 202 */       return ((MethodDelegate)instance).newInstance(this.target);
/*     */     }
/*     */     
/*     */     public void generateClass(ClassVisitor v) throws NoSuchMethodException {
/* 206 */       Method proxy = ReflectUtils.findInterfaceMethod(this.iface);
/* 207 */       Method method = this.targetClass.getMethod(this.methodName, proxy.getParameterTypes());
/* 208 */       if (!proxy.getReturnType().isAssignableFrom(method.getReturnType())) {
/* 209 */         throw new IllegalArgumentException("incompatible return types");
/*     */       }
/*     */       
/* 212 */       MethodInfo methodInfo = ReflectUtils.getMethodInfo(method);
/*     */       
/* 214 */       boolean isStatic = TypeUtils.isStatic(methodInfo.getModifiers());
/* 215 */       if ((((this.target == null) ? 1 : 0) ^ isStatic) != 0) {
/* 216 */         throw new IllegalArgumentException("Static method " + (isStatic ? "not " : "") + "expected");
/*     */       }
/*     */       
/* 219 */       ClassEmitter ce = new ClassEmitter(v);
/*     */       
/* 221 */       ce.begin_class(52, 1, 
/*     */           
/* 223 */           getClassName(), METHOD_DELEGATE, new Type[] {
/*     */             
/* 225 */             Type.getType(this.iface) }, "<generated>");
/*     */       
/* 227 */       ce.declare_field(26, "eqMethod", Constants.TYPE_STRING, null);
/* 228 */       EmitUtils.null_constructor(ce);
/*     */ 
/*     */       
/* 231 */       MethodInfo proxied = ReflectUtils.getMethodInfo(this.iface.getDeclaredMethods()[0]);
/* 232 */       int modifiers = 1;
/* 233 */       if ((proxied.getModifiers() & 0x80) == 128) {
/* 234 */         modifiers |= 0x80;
/*     */       }
/* 236 */       CodeEmitter e = EmitUtils.begin_method(ce, proxied, modifiers);
/* 237 */       e.load_this();
/* 238 */       e.super_getfield("target", Constants.TYPE_OBJECT);
/* 239 */       e.checkcast(methodInfo.getClassInfo().getType());
/* 240 */       e.load_args();
/* 241 */       e.invoke(methodInfo);
/* 242 */       e.return_value();
/* 243 */       e.end_method();
/*     */ 
/*     */       
/* 246 */       e = ce.begin_method(1, NEW_INSTANCE, null);
/* 247 */       e.new_instance_this();
/* 248 */       e.dup();
/* 249 */       e.dup2();
/* 250 */       e.invoke_constructor_this();
/* 251 */       e.getfield("eqMethod");
/* 252 */       e.super_putfield("eqMethod", Constants.TYPE_STRING);
/* 253 */       e.load_arg(0);
/* 254 */       e.super_putfield("target", Constants.TYPE_OBJECT);
/* 255 */       e.return_value();
/* 256 */       e.end_method();
/*     */ 
/*     */       
/* 259 */       e = ce.begin_static();
/* 260 */       e.push(methodInfo.getSignature().toString());
/* 261 */       e.putfield("eqMethod");
/* 262 */       e.return_value();
/* 263 */       e.end_method();
/*     */       
/* 265 */       ce.end_class();
/*     */     }
/*     */   }
/*     */   
/*     */   static interface MethodDelegateKey {
/*     */     Object newInstance(Class param1Class1, String param1String, Class param1Class2);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\reflect\MethodDelegate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */