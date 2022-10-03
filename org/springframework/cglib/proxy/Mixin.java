/*     */ package org.springframework.cglib.proxy;
/*     */ 
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import org.springframework.asm.ClassVisitor;
/*     */ import org.springframework.cglib.core.AbstractClassGenerator;
/*     */ import org.springframework.cglib.core.ClassesKey;
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
/*     */ public abstract class Mixin
/*     */ {
/*  37 */   private static final MixinKey KEY_FACTORY = (MixinKey)KeyFactory.create(MixinKey.class, KeyFactory.CLASS_BY_NAME);
/*  38 */   private static final Map ROUTE_CACHE = Collections.synchronizedMap(new HashMap<Object, Object>());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int STYLE_INTERFACES = 0;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int STYLE_BEANS = 1;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int STYLE_EVERYTHING = 2;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Mixin create(Object[] delegates) {
/*  57 */     Generator gen = new Generator();
/*  58 */     gen.setDelegates(delegates);
/*  59 */     return gen.create();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Mixin create(Class[] interfaces, Object[] delegates) {
/*  69 */     Generator gen = new Generator();
/*  70 */     gen.setClasses(interfaces);
/*  71 */     gen.setDelegates(delegates);
/*  72 */     return gen.create();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Mixin createBean(Object[] beans) {
/*  78 */     return createBean(null, beans);
/*     */   }
/*     */ 
/*     */   
/*     */   static interface MixinKey
/*     */   {
/*     */     Object newInstance(int param1Int, String[] param1ArrayOfString, int[] param1ArrayOfint);
/*     */   }
/*     */   
/*     */   public static Mixin createBean(ClassLoader loader, Object[] beans) {
/*  88 */     Generator gen = new Generator();
/*  89 */     gen.setStyle(1);
/*  90 */     gen.setDelegates(beans);
/*  91 */     gen.setClassLoader(loader);
/*  92 */     return gen.create();
/*     */   }
/*     */   
/*     */   public static class Generator extends AbstractClassGenerator {
/*  96 */     private static final AbstractClassGenerator.Source SOURCE = new AbstractClassGenerator.Source(Mixin.class.getName());
/*     */     
/*     */     private Class[] classes;
/*     */     private Object[] delegates;
/* 100 */     private int style = 0;
/*     */     
/*     */     private int[] route;
/*     */     
/*     */     public Generator() {
/* 105 */       super(SOURCE);
/*     */     }
/*     */     
/*     */     protected ClassLoader getDefaultClassLoader() {
/* 109 */       return this.classes[0].getClassLoader();
/*     */     }
/*     */     
/*     */     protected ProtectionDomain getProtectionDomain() {
/* 113 */       return ReflectUtils.getProtectionDomain(this.classes[0]);
/*     */     }
/*     */     
/*     */     public void setStyle(int style) {
/* 117 */       switch (style) {
/*     */         case 0:
/*     */         case 1:
/*     */         case 2:
/* 121 */           this.style = style;
/*     */           return;
/*     */       } 
/* 124 */       throw new IllegalArgumentException("Unknown mixin style: " + style);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setClasses(Class[] classes) {
/* 129 */       this.classes = classes;
/*     */     }
/*     */     
/*     */     public void setDelegates(Object[] delegates) {
/* 133 */       this.delegates = delegates;
/*     */     }
/*     */     
/*     */     public Mixin create() {
/* 137 */       if (this.classes == null && this.delegates == null) {
/* 138 */         throw new IllegalStateException("Either classes or delegates must be set");
/*     */       }
/* 140 */       switch (this.style) {
/*     */         case 0:
/* 142 */           if (this.classes == null) {
/* 143 */             Mixin.Route r = Mixin.route(this.delegates);
/* 144 */             this.classes = r.classes;
/* 145 */             this.route = r.route;
/*     */           } 
/*     */           break;
/*     */         
/*     */         case 1:
/*     */         case 2:
/* 151 */           if (this.classes == null) {
/* 152 */             this.classes = ReflectUtils.getClasses(this.delegates); break;
/*     */           } 
/* 154 */           if (this.delegates != null) {
/* 155 */             Class[] temp = ReflectUtils.getClasses(this.delegates);
/* 156 */             if (this.classes.length != temp.length) {
/* 157 */               throw new IllegalStateException("Specified classes are incompatible with delegates");
/*     */             }
/* 159 */             for (int i = 0; i < this.classes.length; i++) {
/* 160 */               if (!this.classes[i].isAssignableFrom(temp[i])) {
/* 161 */                 throw new IllegalStateException("Specified class " + this.classes[i] + " is incompatible with delegate class " + temp[i] + " (index " + i + ")");
/*     */               }
/*     */             } 
/*     */           } 
/*     */           break;
/*     */       } 
/* 167 */       setNamePrefix(this.classes[ReflectUtils.findPackageProtected(this.classes)].getName());
/*     */       
/* 169 */       return (Mixin)create(Mixin.KEY_FACTORY.newInstance(this.style, ReflectUtils.getNames(this.classes), this.route));
/*     */     }
/*     */     
/*     */     public void generateClass(ClassVisitor v) {
/* 173 */       switch (this.style) {
/*     */         case 0:
/* 175 */           new MixinEmitter(v, getClassName(), this.classes, this.route);
/*     */           break;
/*     */         case 1:
/* 178 */           new MixinBeanEmitter(v, getClassName(), this.classes);
/*     */           break;
/*     */         case 2:
/* 181 */           new MixinEverythingEmitter(v, getClassName(), this.classes);
/*     */           break;
/*     */       } 
/*     */     }
/*     */     
/*     */     protected Object firstInstance(Class type) {
/* 187 */       return ((Mixin)ReflectUtils.newInstance(type)).newInstance(this.delegates);
/*     */     }
/*     */     
/*     */     protected Object nextInstance(Object instance) {
/* 191 */       return ((Mixin)instance).newInstance(this.delegates);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Class[] getClasses(Object[] delegates) {
/* 196 */     return (Class[])(route(delegates)).classes.clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Route route(Object[] delegates) {
/* 204 */     Object key = ClassesKey.create(delegates);
/* 205 */     Route route = (Route)ROUTE_CACHE.get(key);
/* 206 */     if (route == null) {
/* 207 */       ROUTE_CACHE.put(key, route = new Route(delegates));
/*     */     }
/* 209 */     return route;
/*     */   }
/*     */   
/*     */   public abstract Mixin newInstance(Object[] paramArrayOfObject);
/*     */   
/*     */   private static class Route { private Class[] classes;
/*     */     private int[] route;
/*     */     
/*     */     Route(Object[] delegates) {
/* 218 */       Map<Object, Object> map = new HashMap<Object, Object>();
/* 219 */       ArrayList collect = new ArrayList();
/* 220 */       for (int i = 0; i < delegates.length; i++) {
/* 221 */         Class<?> delegate = delegates[i].getClass();
/* 222 */         collect.clear();
/* 223 */         ReflectUtils.addAllInterfaces(delegate, collect);
/* 224 */         for (Iterator<Class<?>> iterator = collect.iterator(); iterator.hasNext(); ) {
/* 225 */           Class iface = iterator.next();
/* 226 */           if (!map.containsKey(iface)) {
/* 227 */             map.put(iface, new Integer(i));
/*     */           }
/*     */         } 
/*     */       } 
/* 231 */       this.classes = new Class[map.size()];
/* 232 */       this.route = new int[map.size()];
/* 233 */       int index = 0;
/* 234 */       for (Iterator<Class<?>> it = map.keySet().iterator(); it.hasNext(); ) {
/* 235 */         Class key = it.next();
/* 236 */         this.classes[index] = key;
/* 237 */         this.route[index] = ((Integer)map.get(key)).intValue();
/* 238 */         index++;
/*     */       } 
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\proxy\Mixin.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */