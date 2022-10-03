/*     */ package org.springframework.cglib.proxy;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import org.springframework.asm.ClassVisitor;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.cglib.core.AbstractClassGenerator;
/*     */ import org.springframework.cglib.core.ClassEmitter;
/*     */ import org.springframework.cglib.core.ReflectUtils;
/*     */ import org.springframework.cglib.core.Signature;
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
/*     */ public class InterfaceMaker
/*     */   extends AbstractClassGenerator
/*     */ {
/*  34 */   private static final AbstractClassGenerator.Source SOURCE = new AbstractClassGenerator.Source(InterfaceMaker.class.getName());
/*  35 */   private Map signatures = new HashMap<Object, Object>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InterfaceMaker() {
/*  43 */     super(SOURCE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(Signature sig, Type[] exceptions) {
/*  52 */     this.signatures.put(sig, exceptions);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(Method method) {
/*  61 */     add(ReflectUtils.getSignature(method), 
/*  62 */         ReflectUtils.getExceptionTypes(method));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(Class clazz) {
/*  72 */     Method[] methods = clazz.getMethods();
/*  73 */     for (int i = 0; i < methods.length; i++) {
/*  74 */       Method m = methods[i];
/*  75 */       if (!m.getDeclaringClass().getName().equals("java.lang.Object")) {
/*  76 */         add(m);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class create() {
/*  85 */     setUseCache(false);
/*  86 */     return (Class)create(this);
/*     */   }
/*     */   
/*     */   protected ClassLoader getDefaultClassLoader() {
/*  90 */     return null;
/*     */   }
/*     */   
/*     */   protected Object firstInstance(Class type) {
/*  94 */     return type;
/*     */   }
/*     */   
/*     */   protected Object nextInstance(Object instance) {
/*  98 */     throw new IllegalStateException("InterfaceMaker does not cache");
/*     */   }
/*     */   
/*     */   public void generateClass(ClassVisitor v) throws Exception {
/* 102 */     ClassEmitter ce = new ClassEmitter(v);
/* 103 */     ce.begin_class(52, 1537, 
/*     */         
/* 105 */         getClassName(), null, null, "<generated>");
/*     */ 
/*     */ 
/*     */     
/* 109 */     for (Iterator<Signature> it = this.signatures.keySet().iterator(); it.hasNext(); ) {
/* 110 */       Signature sig = it.next();
/* 111 */       Type[] exceptions = (Type[])this.signatures.get(sig);
/* 112 */       ce.begin_method(1025, sig, exceptions)
/*     */         
/* 114 */         .end_method();
/*     */     } 
/* 116 */     ce.end_class();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\proxy\InterfaceMaker.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */