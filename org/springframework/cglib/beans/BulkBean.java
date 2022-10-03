/*     */ package org.springframework.cglib.beans;
/*     */ 
/*     */ import java.security.ProtectionDomain;
/*     */ import org.springframework.asm.ClassVisitor;
/*     */ import org.springframework.cglib.core.AbstractClassGenerator;
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
/*     */ public abstract class BulkBean
/*     */ {
/*  32 */   private static final BulkBeanKey KEY_FACTORY = (BulkBeanKey)KeyFactory.create(BulkBeanKey.class);
/*     */ 
/*     */   
/*     */   protected Class target;
/*     */ 
/*     */   
/*     */   protected String[] getters;
/*     */ 
/*     */   
/*     */   protected String[] setters;
/*     */ 
/*     */   
/*     */   protected Class[] types;
/*     */ 
/*     */   
/*     */   public Object[] getPropertyValues(Object bean) {
/*  48 */     Object[] values = new Object[this.getters.length];
/*  49 */     getPropertyValues(bean, values);
/*  50 */     return values;
/*     */   }
/*     */   
/*     */   public Class[] getPropertyTypes() {
/*  54 */     return (Class[])this.types.clone();
/*     */   }
/*     */   
/*     */   public String[] getGetters() {
/*  58 */     return (String[])this.getters.clone();
/*     */   }
/*     */   
/*     */   public String[] getSetters() {
/*  62 */     return (String[])this.setters.clone();
/*     */   }
/*     */   public abstract void getPropertyValues(Object paramObject, Object[] paramArrayOfObject);
/*     */   public static BulkBean create(Class target, String[] getters, String[] setters, Class[] types) {
/*  66 */     Generator gen = new Generator();
/*  67 */     gen.setTarget(target);
/*  68 */     gen.setGetters(getters);
/*  69 */     gen.setSetters(setters);
/*  70 */     gen.setTypes(types);
/*  71 */     return gen.create();
/*     */   }
/*     */   public abstract void setPropertyValues(Object paramObject, Object[] paramArrayOfObject);
/*     */   
/*  75 */   public static class Generator extends AbstractClassGenerator { private static final AbstractClassGenerator.Source SOURCE = new AbstractClassGenerator.Source(BulkBean.class.getName());
/*     */     private Class target;
/*     */     private String[] getters;
/*     */     private String[] setters;
/*     */     private Class[] types;
/*     */     
/*     */     public Generator() {
/*  82 */       super(SOURCE);
/*     */     }
/*     */     
/*     */     public void setTarget(Class target) {
/*  86 */       this.target = target;
/*     */     }
/*     */     
/*     */     public void setGetters(String[] getters) {
/*  90 */       this.getters = getters;
/*     */     }
/*     */     
/*     */     public void setSetters(String[] setters) {
/*  94 */       this.setters = setters;
/*     */     }
/*     */     
/*     */     public void setTypes(Class[] types) {
/*  98 */       this.types = types;
/*     */     }
/*     */     
/*     */     protected ClassLoader getDefaultClassLoader() {
/* 102 */       return this.target.getClassLoader();
/*     */     }
/*     */     
/*     */     protected ProtectionDomain getProtectionDomain() {
/* 106 */       return ReflectUtils.getProtectionDomain(this.target);
/*     */     }
/*     */     
/*     */     public BulkBean create() {
/* 110 */       setNamePrefix(this.target.getName());
/* 111 */       String targetClassName = this.target.getName();
/* 112 */       String[] typeClassNames = ReflectUtils.getNames(this.types);
/* 113 */       Object key = BulkBean.KEY_FACTORY.newInstance(targetClassName, this.getters, this.setters, typeClassNames);
/* 114 */       return (BulkBean)create(key);
/*     */     }
/*     */     
/*     */     public void generateClass(ClassVisitor v) throws Exception {
/* 118 */       new BulkBeanEmitter(v, getClassName(), this.target, this.getters, this.setters, this.types);
/*     */     }
/*     */     
/*     */     protected Object firstInstance(Class type) {
/* 122 */       BulkBean instance = (BulkBean)ReflectUtils.newInstance(type);
/* 123 */       instance.target = this.target;
/*     */       
/* 125 */       int length = this.getters.length;
/* 126 */       instance.getters = new String[length];
/* 127 */       System.arraycopy(this.getters, 0, instance.getters, 0, length);
/*     */       
/* 129 */       instance.setters = new String[length];
/* 130 */       System.arraycopy(this.setters, 0, instance.setters, 0, length);
/*     */       
/* 132 */       instance.types = new Class[this.types.length];
/* 133 */       System.arraycopy(this.types, 0, instance.types, 0, this.types.length);
/*     */       
/* 135 */       return instance;
/*     */     }
/*     */     
/*     */     protected Object nextInstance(Object instance) {
/* 139 */       return instance;
/*     */     } }
/*     */ 
/*     */   
/*     */   static interface BulkBeanKey {
/*     */     Object newInstance(String param1String, String[] param1ArrayOfString1, String[] param1ArrayOfString2, String[] param1ArrayOfString3);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\beans\BulkBean.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */