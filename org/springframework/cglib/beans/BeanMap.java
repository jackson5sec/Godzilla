/*     */ package org.springframework.cglib.beans;
/*     */ 
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class BeanMap
/*     */   implements Map
/*     */ {
/*     */   public static final int REQUIRE_GETTER = 1;
/*     */   public static final int REQUIRE_SETTER = 2;
/*     */   protected Object bean;
/*     */   
/*     */   public abstract BeanMap newInstance(Object paramObject);
/*     */   
/*     */   public abstract Class getPropertyType(String paramString);
/*     */   
/*     */   public static BeanMap create(Object bean) {
/*  57 */     Generator gen = new Generator();
/*  58 */     gen.setBean(bean);
/*  59 */     return gen.create();
/*     */   }
/*     */   
/*     */   public static class Generator extends AbstractClassGenerator {
/*  63 */     private static final AbstractClassGenerator.Source SOURCE = new AbstractClassGenerator.Source(BeanMap.class.getName());
/*     */ 
/*     */     
/*  66 */     private static final BeanMapKey KEY_FACTORY = (BeanMapKey)KeyFactory.create(BeanMapKey.class, KeyFactory.CLASS_BY_NAME);
/*     */ 
/*     */     
/*     */     private Object bean;
/*     */     
/*     */     private Class beanClass;
/*     */     
/*     */     private int require;
/*     */ 
/*     */     
/*     */     public Generator() {
/*  77 */       super(SOURCE);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setBean(Object bean) {
/*  88 */       this.bean = bean;
/*  89 */       if (bean != null) {
/*  90 */         this.beanClass = bean.getClass();
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setBeanClass(Class beanClass) {
/*  99 */       this.beanClass = beanClass;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setRequire(int require) {
/* 108 */       this.require = require;
/*     */     }
/*     */     
/*     */     protected ClassLoader getDefaultClassLoader() {
/* 112 */       return this.beanClass.getClassLoader();
/*     */     }
/*     */     
/*     */     protected ProtectionDomain getProtectionDomain() {
/* 116 */       return ReflectUtils.getProtectionDomain(this.beanClass);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public BeanMap create() {
/* 124 */       if (this.beanClass == null)
/* 125 */         throw new IllegalArgumentException("Class of bean unknown"); 
/* 126 */       setNamePrefix(this.beanClass.getName());
/* 127 */       return (BeanMap)create(KEY_FACTORY.newInstance(this.beanClass, this.require));
/*     */     }
/*     */     
/*     */     public void generateClass(ClassVisitor v) throws Exception {
/* 131 */       new BeanMapEmitter(v, getClassName(), this.beanClass, this.require);
/*     */     }
/*     */     
/*     */     protected Object firstInstance(Class type) {
/* 135 */       return ((BeanMap)ReflectUtils.newInstance(type)).newInstance(this.bean);
/*     */     }
/*     */     
/*     */     protected Object nextInstance(Object instance) {
/* 139 */       return ((BeanMap)instance).newInstance(this.bean);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static interface BeanMapKey
/*     */     {
/*     */       Object newInstance(Class param2Class, int param2Int);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BeanMap() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BeanMap(Object bean) {
/* 164 */     setBean(bean);
/*     */   }
/*     */   
/*     */   public Object get(Object key) {
/* 168 */     return get(this.bean, key);
/*     */   }
/*     */   
/*     */   public Object put(Object key, Object value) {
/* 172 */     return put(this.bean, key, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Object get(Object paramObject1, Object paramObject2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Object put(Object paramObject1, Object paramObject2, Object paramObject3);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBean(Object bean) {
/* 201 */     this.bean = bean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getBean() {
/* 210 */     return this.bean;
/*     */   }
/*     */   
/*     */   public void clear() {
/* 214 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean containsKey(Object key) {
/* 218 */     return keySet().contains(key);
/*     */   }
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 222 */     for (Iterator<K> it = keySet().iterator(); it.hasNext(); ) {
/* 223 */       Object v = get(it.next());
/* 224 */       if ((value == null && v == null) || (value != null && value.equals(v)))
/* 225 */         return true; 
/*     */     } 
/* 227 */     return false;
/*     */   }
/*     */   
/*     */   public int size() {
/* 231 */     return keySet().size();
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 235 */     return (size() == 0);
/*     */   }
/*     */   
/*     */   public Object remove(Object key) {
/* 239 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public void putAll(Map t) {
/* 243 */     for (Iterator it = t.keySet().iterator(); it.hasNext(); ) {
/* 244 */       Object key = it.next();
/* 245 */       put(key, t.get(key));
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 250 */     if (o == null || !(o instanceof Map)) {
/* 251 */       return false;
/*     */     }
/* 253 */     Map other = (Map)o;
/* 254 */     if (size() != other.size()) {
/* 255 */       return false;
/*     */     }
/* 257 */     for (Iterator<K> it = keySet().iterator(); it.hasNext(); ) {
/* 258 */       Object key = it.next();
/* 259 */       if (!other.containsKey(key)) {
/* 260 */         return false;
/*     */       }
/* 262 */       Object v1 = get(key);
/* 263 */       Object v2 = other.get(key);
/* 264 */       if ((v1 == null) ? (v2 == null) : v1.equals(v2))
/* 265 */         continue;  return false;
/*     */     } 
/*     */     
/* 268 */     return true;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 272 */     int code = 0;
/* 273 */     for (Iterator<K> it = keySet().iterator(); it.hasNext(); ) {
/* 274 */       Object key = it.next();
/* 275 */       Object value = get(key);
/* 276 */       code += ((key == null) ? 0 : key.hashCode()) ^ ((value == null) ? 0 : value
/* 277 */         .hashCode());
/*     */     } 
/* 279 */     return code;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set entrySet() {
/* 284 */     HashMap<Object, Object> copy = new HashMap<Object, Object>();
/* 285 */     for (Iterator<K> it = keySet().iterator(); it.hasNext(); ) {
/* 286 */       Object key = it.next();
/* 287 */       copy.put(key, get(key));
/*     */     } 
/* 289 */     return Collections.<Object, Object>unmodifiableMap(copy).entrySet();
/*     */   }
/*     */   
/*     */   public Collection values() {
/* 293 */     Set<K> keys = keySet();
/* 294 */     List<Object> values = new ArrayList(keys.size());
/* 295 */     for (Iterator<K> it = keys.iterator(); it.hasNext();) {
/* 296 */       values.add(get(it.next()));
/*     */     }
/* 298 */     return Collections.unmodifiableCollection(values);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 306 */     StringBuffer sb = new StringBuffer();
/* 307 */     sb.append('{');
/* 308 */     for (Iterator<K> it = keySet().iterator(); it.hasNext(); ) {
/* 309 */       Object key = it.next();
/* 310 */       sb.append(key);
/* 311 */       sb.append('=');
/* 312 */       sb.append(get(key));
/* 313 */       if (it.hasNext()) {
/* 314 */         sb.append(", ");
/*     */       }
/*     */     } 
/* 317 */     sb.append('}');
/* 318 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\beans\BeanMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */