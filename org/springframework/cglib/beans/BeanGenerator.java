/*     */ package org.springframework.cglib.beans;
/*     */ 
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import org.springframework.asm.ClassVisitor;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.cglib.core.AbstractClassGenerator;
/*     */ import org.springframework.cglib.core.ClassEmitter;
/*     */ import org.springframework.cglib.core.Constants;
/*     */ import org.springframework.cglib.core.EmitUtils;
/*     */ import org.springframework.cglib.core.KeyFactory;
/*     */ import org.springframework.cglib.core.ReflectUtils;
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
/*     */ public class BeanGenerator
/*     */   extends AbstractClassGenerator
/*     */ {
/*  30 */   private static final AbstractClassGenerator.Source SOURCE = new AbstractClassGenerator.Source(BeanGenerator.class.getName());
/*     */   
/*  32 */   private static final BeanGeneratorKey KEY_FACTORY = (BeanGeneratorKey)KeyFactory.create(BeanGeneratorKey.class);
/*     */ 
/*     */ 
/*     */   
/*     */   private Class superclass;
/*     */ 
/*     */   
/*  39 */   private Map props = new HashMap<Object, Object>();
/*     */   private boolean classOnly;
/*     */   
/*     */   public BeanGenerator() {
/*  43 */     super(SOURCE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSuperclass(Class superclass) {
/*  53 */     if (superclass != null && superclass.equals(Object.class)) {
/*  54 */       superclass = null;
/*     */     }
/*  56 */     this.superclass = superclass;
/*     */   }
/*     */   
/*     */   public void addProperty(String name, Class type) {
/*  60 */     if (this.props.containsKey(name)) {
/*  61 */       throw new IllegalArgumentException("Duplicate property name \"" + name + "\"");
/*     */     }
/*  63 */     this.props.put(name, Type.getType(type));
/*     */   }
/*     */   
/*     */   protected ClassLoader getDefaultClassLoader() {
/*  67 */     if (this.superclass != null) {
/*  68 */       return this.superclass.getClassLoader();
/*     */     }
/*  70 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ProtectionDomain getProtectionDomain() {
/*  75 */     return ReflectUtils.getProtectionDomain(this.superclass);
/*     */   }
/*     */   
/*     */   public Object create() {
/*  79 */     this.classOnly = false;
/*  80 */     return createHelper();
/*     */   }
/*     */   
/*     */   public Object createClass() {
/*  84 */     this.classOnly = true;
/*  85 */     return createHelper();
/*     */   }
/*     */   
/*     */   private Object createHelper() {
/*  89 */     if (this.superclass != null) {
/*  90 */       setNamePrefix(this.superclass.getName());
/*     */     }
/*  92 */     String superName = (this.superclass != null) ? this.superclass.getName() : "java.lang.Object";
/*  93 */     Object key = KEY_FACTORY.newInstance(superName, this.props);
/*  94 */     return create(key);
/*     */   }
/*     */   
/*     */   public void generateClass(ClassVisitor v) throws Exception {
/*  98 */     int size = this.props.size();
/*  99 */     String[] names = (String[])this.props.keySet().toArray((Object[])new String[size]);
/* 100 */     Type[] types = new Type[size];
/* 101 */     for (int i = 0; i < size; i++) {
/* 102 */       types[i] = (Type)this.props.get(names[i]);
/*     */     }
/* 104 */     ClassEmitter ce = new ClassEmitter(v);
/* 105 */     ce.begin_class(52, 1, 
/*     */         
/* 107 */         getClassName(), (this.superclass != null) ? 
/* 108 */         Type.getType(this.superclass) : Constants.TYPE_OBJECT, null, null);
/*     */ 
/*     */     
/* 111 */     EmitUtils.null_constructor(ce);
/* 112 */     EmitUtils.add_properties(ce, names, types);
/* 113 */     ce.end_class();
/*     */   }
/*     */   
/*     */   protected Object firstInstance(Class type) {
/* 117 */     if (this.classOnly) {
/* 118 */       return type;
/*     */     }
/* 120 */     return ReflectUtils.newInstance(type);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object nextInstance(Object instance) {
/* 125 */     Class protoclass = (instance instanceof Class) ? (Class)instance : instance.getClass();
/* 126 */     if (this.classOnly) {
/* 127 */       return protoclass;
/*     */     }
/* 129 */     return ReflectUtils.newInstance(protoclass);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addProperties(BeanGenerator gen, Map props) {
/* 134 */     for (Iterator<String> it = props.keySet().iterator(); it.hasNext(); ) {
/* 135 */       String name = it.next();
/* 136 */       gen.addProperty(name, (Class)props.get(name));
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void addProperties(BeanGenerator gen, Class type) {
/* 141 */     addProperties(gen, ReflectUtils.getBeanProperties(type));
/*     */   }
/*     */   
/*     */   public static void addProperties(BeanGenerator gen, PropertyDescriptor[] descriptors) {
/* 145 */     for (int i = 0; i < descriptors.length; i++)
/* 146 */       gen.addProperty(descriptors[i].getName(), descriptors[i].getPropertyType()); 
/*     */   }
/*     */   
/*     */   static interface BeanGeneratorKey {
/*     */     Object newInstance(String param1String, Map param1Map);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\beans\BeanGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */