/*     */ package org.springframework.cglib.reflect;
/*     */ 
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.springframework.asm.ClassVisitor;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.cglib.core.AbstractClassGenerator;
/*     */ import org.springframework.cglib.core.ClassEmitter;
/*     */ import org.springframework.cglib.core.CodeEmitter;
/*     */ import org.springframework.cglib.core.Constants;
/*     */ import org.springframework.cglib.core.EmitUtils;
/*     */ import org.springframework.cglib.core.Local;
/*     */ import org.springframework.cglib.core.MethodInfo;
/*     */ import org.springframework.cglib.core.ProcessArrayCallback;
/*     */ import org.springframework.cglib.core.ReflectUtils;
/*     */ import org.springframework.cglib.core.Signature;
/*     */ import org.springframework.cglib.core.TypeUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class MulticastDelegate
/*     */   implements Cloneable
/*     */ {
/*  27 */   protected Object[] targets = new Object[0];
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List getTargets() {
/*  33 */     return new ArrayList(Arrays.asList(this.targets));
/*     */   }
/*     */   
/*     */   public abstract MulticastDelegate add(Object paramObject);
/*     */   
/*     */   protected MulticastDelegate addHelper(Object target) {
/*  39 */     MulticastDelegate copy = newInstance();
/*  40 */     copy.targets = new Object[this.targets.length + 1];
/*  41 */     System.arraycopy(this.targets, 0, copy.targets, 0, this.targets.length);
/*  42 */     copy.targets[this.targets.length] = target;
/*  43 */     return copy;
/*     */   }
/*     */   
/*     */   public MulticastDelegate remove(Object target) {
/*  47 */     for (int i = this.targets.length - 1; i >= 0; i--) {
/*  48 */       if (this.targets[i].equals(target)) {
/*  49 */         MulticastDelegate copy = newInstance();
/*  50 */         copy.targets = new Object[this.targets.length - 1];
/*  51 */         System.arraycopy(this.targets, 0, copy.targets, 0, i);
/*  52 */         System.arraycopy(this.targets, i + 1, copy.targets, i, this.targets.length - i - 1);
/*  53 */         return copy;
/*     */       } 
/*     */     } 
/*  56 */     return this;
/*     */   }
/*     */   
/*     */   public abstract MulticastDelegate newInstance();
/*     */   
/*     */   public static MulticastDelegate create(Class iface) {
/*  62 */     Generator gen = new Generator();
/*  63 */     gen.setInterface(iface);
/*  64 */     return gen.create();
/*     */   }
/*     */   
/*     */   public static class Generator extends AbstractClassGenerator {
/*  68 */     private static final AbstractClassGenerator.Source SOURCE = new AbstractClassGenerator.Source(MulticastDelegate.class.getName());
/*     */     
/*  70 */     private static final Type MULTICAST_DELEGATE = TypeUtils.parseType("org.springframework.cglib.reflect.MulticastDelegate");
/*  71 */     private static final Signature NEW_INSTANCE = new Signature("newInstance", MULTICAST_DELEGATE, new Type[0]);
/*     */     
/*  73 */     private static final Signature ADD_DELEGATE = new Signature("add", MULTICAST_DELEGATE, new Type[] { Constants.TYPE_OBJECT });
/*     */     
/*  75 */     private static final Signature ADD_HELPER = new Signature("addHelper", MULTICAST_DELEGATE, new Type[] { Constants.TYPE_OBJECT });
/*     */     
/*     */     private Class iface;
/*     */ 
/*     */     
/*     */     public Generator() {
/*  81 */       super(SOURCE);
/*     */     }
/*     */     
/*     */     protected ClassLoader getDefaultClassLoader() {
/*  85 */       return this.iface.getClassLoader();
/*     */     }
/*     */     
/*     */     protected ProtectionDomain getProtectionDomain() {
/*  89 */       return ReflectUtils.getProtectionDomain(this.iface);
/*     */     }
/*     */     
/*     */     public void setInterface(Class iface) {
/*  93 */       this.iface = iface;
/*     */     }
/*     */     
/*     */     public MulticastDelegate create() {
/*  97 */       setNamePrefix(MulticastDelegate.class.getName());
/*  98 */       return (MulticastDelegate)create(this.iface.getName());
/*     */     }
/*     */     
/*     */     public void generateClass(ClassVisitor cv) {
/* 102 */       MethodInfo method = ReflectUtils.getMethodInfo(ReflectUtils.findInterfaceMethod(this.iface));
/*     */       
/* 104 */       ClassEmitter ce = new ClassEmitter(cv);
/* 105 */       ce.begin_class(52, 1, 
/*     */           
/* 107 */           getClassName(), MULTICAST_DELEGATE, new Type[] {
/*     */             
/* 109 */             Type.getType(this.iface) }, "<generated>");
/*     */       
/* 111 */       EmitUtils.null_constructor(ce);
/*     */ 
/*     */       
/* 114 */       emitProxy(ce, method);
/*     */ 
/*     */       
/* 117 */       CodeEmitter e = ce.begin_method(1, NEW_INSTANCE, null);
/* 118 */       e.new_instance_this();
/* 119 */       e.dup();
/* 120 */       e.invoke_constructor_this();
/* 121 */       e.return_value();
/* 122 */       e.end_method();
/*     */ 
/*     */       
/* 125 */       e = ce.begin_method(1, ADD_DELEGATE, null);
/* 126 */       e.load_this();
/* 127 */       e.load_arg(0);
/* 128 */       e.checkcast(Type.getType(this.iface));
/* 129 */       e.invoke_virtual_this(ADD_HELPER);
/* 130 */       e.return_value();
/* 131 */       e.end_method();
/*     */       
/* 133 */       ce.end_class();
/*     */     }
/*     */     
/*     */     private void emitProxy(ClassEmitter ce, final MethodInfo method) {
/* 137 */       int modifiers = 1;
/* 138 */       if ((method.getModifiers() & 0x80) == 128) {
/* 139 */         modifiers |= 0x80;
/*     */       }
/* 141 */       final CodeEmitter e = EmitUtils.begin_method(ce, method, modifiers);
/* 142 */       Type returnType = method.getSignature().getReturnType();
/* 143 */       final boolean returns = (returnType != Type.VOID_TYPE);
/* 144 */       Local result = null;
/* 145 */       if (returns) {
/* 146 */         result = e.make_local(returnType);
/* 147 */         e.zero_or_null(returnType);
/* 148 */         e.store_local(result);
/*     */       } 
/* 150 */       e.load_this();
/* 151 */       e.super_getfield("targets", Constants.TYPE_OBJECT_ARRAY);
/* 152 */       final Local result2 = result;
/* 153 */       EmitUtils.process_array(e, Constants.TYPE_OBJECT_ARRAY, new ProcessArrayCallback() {
/*     */             public void processElement(Type type) {
/* 155 */               e.checkcast(Type.getType(MulticastDelegate.Generator.this.iface));
/* 156 */               e.load_args();
/* 157 */               e.invoke(method);
/* 158 */               if (returns) {
/* 159 */                 e.store_local(result2);
/*     */               }
/*     */             }
/*     */           });
/* 163 */       if (returns) {
/* 164 */         e.load_local(result);
/*     */       }
/* 166 */       e.return_value();
/* 167 */       e.end_method();
/*     */     }
/*     */ 
/*     */     
/*     */     protected Object firstInstance(Class type) {
/* 172 */       return ((MulticastDelegate)ReflectUtils.newInstance(type)).newInstance();
/*     */     }
/*     */     
/*     */     protected Object nextInstance(Object instance) {
/* 176 */       return ((MulticastDelegate)instance).newInstance();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\reflect\MulticastDelegate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */