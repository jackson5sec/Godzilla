/*     */ package javassist.bytecode.annotation;
/*     */ 
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtClass;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.bytecode.AnnotationDefaultAttribute;
/*     */ import javassist.bytecode.ClassFile;
/*     */ import javassist.bytecode.MethodInfo;
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
/*     */ public class AnnotationImpl
/*     */   implements InvocationHandler
/*     */ {
/*     */   private static final String JDK_ANNOTATION_CLASS_NAME = "java.lang.annotation.Annotation";
/*  40 */   private static Method JDK_ANNOTATION_TYPE_METHOD = null;
/*     */   
/*     */   private Annotation annotation;
/*     */   private ClassPool pool;
/*     */   private ClassLoader classLoader;
/*     */   private transient Class<?> annotationType;
/*  46 */   private transient int cachedHashCode = Integer.MIN_VALUE;
/*     */ 
/*     */   
/*     */   static {
/*     */     try {
/*  51 */       Class<?> clazz = Class.forName("java.lang.annotation.Annotation");
/*  52 */       JDK_ANNOTATION_TYPE_METHOD = clazz.getMethod("annotationType", (Class[])null);
/*     */     }
/*  54 */     catch (Exception exception) {}
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
/*     */ 
/*     */   
/*     */   public static Object make(ClassLoader cl, Class<?> clazz, ClassPool cp, Annotation anon) throws IllegalArgumentException {
/*  73 */     AnnotationImpl handler = new AnnotationImpl(anon, cp, cl);
/*  74 */     return Proxy.newProxyInstance(cl, new Class[] { clazz }, handler);
/*     */   }
/*     */   
/*     */   private AnnotationImpl(Annotation a, ClassPool cp, ClassLoader loader) {
/*  78 */     this.annotation = a;
/*  79 */     this.pool = cp;
/*  80 */     this.classLoader = loader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTypeName() {
/*  89 */     return this.annotation.getTypeName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Class<?> getAnnotationType() {
/*  99 */     if (this.annotationType == null) {
/* 100 */       String typeName = this.annotation.getTypeName();
/*     */       try {
/* 102 */         this.annotationType = this.classLoader.loadClass(typeName);
/*     */       }
/* 104 */       catch (ClassNotFoundException e) {
/* 105 */         NoClassDefFoundError error = new NoClassDefFoundError("Error loading annotation class: " + typeName);
/* 106 */         error.setStackTrace(e.getStackTrace());
/* 107 */         throw error;
/*     */       } 
/*     */     } 
/* 110 */     return this.annotationType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Annotation getAnnotation() {
/* 119 */     return this.annotation;
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
/*     */   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/* 133 */     String name = method.getName();
/* 134 */     if (Object.class == method.getDeclaringClass()) {
/* 135 */       if ("equals".equals(name)) {
/* 136 */         Object obj = args[0];
/* 137 */         return Boolean.valueOf(checkEquals(obj));
/*     */       } 
/* 139 */       if ("toString".equals(name))
/* 140 */         return this.annotation.toString(); 
/* 141 */       if ("hashCode".equals(name)) {
/* 142 */         return Integer.valueOf(hashCode());
/*     */       }
/* 144 */     } else if ("annotationType".equals(name) && (method
/* 145 */       .getParameterTypes()).length == 0) {
/* 146 */       return getAnnotationType();
/*     */     } 
/* 148 */     MemberValue mv = this.annotation.getMemberValue(name);
/* 149 */     if (mv == null)
/* 150 */       return getDefault(name, method); 
/* 151 */     return mv.getValue(this.classLoader, this.pool, method);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Object getDefault(String name, Method method) throws ClassNotFoundException, RuntimeException {
/* 157 */     String classname = this.annotation.getTypeName();
/* 158 */     if (this.pool != null) {
/*     */       try {
/* 160 */         CtClass cc = this.pool.get(classname);
/* 161 */         ClassFile cf = cc.getClassFile2();
/* 162 */         MethodInfo minfo = cf.getMethod(name);
/* 163 */         if (minfo != null) {
/*     */ 
/*     */           
/* 166 */           AnnotationDefaultAttribute ainfo = (AnnotationDefaultAttribute)minfo.getAttribute("AnnotationDefault");
/* 167 */           if (ainfo != null) {
/* 168 */             MemberValue mv = ainfo.getDefaultValue();
/* 169 */             return mv.getValue(this.classLoader, this.pool, method);
/*     */           }
/*     */         
/*     */         } 
/* 173 */       } catch (NotFoundException e) {
/* 174 */         throw new RuntimeException("cannot find a class file: " + classname);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 179 */     throw new RuntimeException("no default value: " + classname + "." + name + "()");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 188 */     if (this.cachedHashCode == Integer.MIN_VALUE) {
/* 189 */       int hashCode = 0;
/*     */ 
/*     */       
/* 192 */       getAnnotationType();
/*     */       
/* 194 */       Method[] methods = this.annotationType.getDeclaredMethods();
/* 195 */       for (int i = 0; i < methods.length; i++) {
/* 196 */         String name = methods[i].getName();
/* 197 */         int valueHashCode = 0;
/*     */ 
/*     */         
/* 200 */         MemberValue mv = this.annotation.getMemberValue(name);
/* 201 */         Object value = null;
/*     */         try {
/* 203 */           if (mv != null)
/* 204 */             value = mv.getValue(this.classLoader, this.pool, methods[i]); 
/* 205 */           if (value == null) {
/* 206 */             value = getDefault(name, methods[i]);
/*     */           }
/* 208 */         } catch (RuntimeException e) {
/* 209 */           throw e;
/*     */         }
/* 211 */         catch (Exception e) {
/* 212 */           throw new RuntimeException("Error retrieving value " + name + " for annotation " + this.annotation.getTypeName(), e);
/*     */         } 
/*     */ 
/*     */         
/* 216 */         if (value != null)
/* 217 */           if (value.getClass().isArray()) {
/* 218 */             valueHashCode = arrayHashCode(value);
/*     */           } else {
/* 220 */             valueHashCode = value.hashCode();
/*     */           }  
/* 222 */         hashCode += 127 * name.hashCode() ^ valueHashCode;
/*     */       } 
/*     */       
/* 225 */       this.cachedHashCode = hashCode;
/*     */     } 
/* 227 */     return this.cachedHashCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean checkEquals(Object obj) throws Exception {
/* 238 */     if (obj == null) {
/* 239 */       return false;
/*     */     }
/*     */     
/* 242 */     if (obj instanceof Proxy) {
/* 243 */       InvocationHandler ih = Proxy.getInvocationHandler(obj);
/* 244 */       if (ih instanceof AnnotationImpl) {
/* 245 */         AnnotationImpl other = (AnnotationImpl)ih;
/* 246 */         return this.annotation.equals(other.annotation);
/*     */       } 
/*     */     } 
/*     */     
/* 250 */     Class<?> otherAnnotationType = (Class)JDK_ANNOTATION_TYPE_METHOD.invoke(obj, new Object[0]);
/* 251 */     if (!getAnnotationType().equals(otherAnnotationType)) {
/* 252 */       return false;
/*     */     }
/* 254 */     Method[] methods = this.annotationType.getDeclaredMethods();
/* 255 */     for (int i = 0; i < methods.length; i++) {
/* 256 */       String name = methods[i].getName();
/*     */ 
/*     */       
/* 259 */       MemberValue mv = this.annotation.getMemberValue(name);
/* 260 */       Object value = null;
/* 261 */       Object otherValue = null;
/*     */       try {
/* 263 */         if (mv != null)
/* 264 */           value = mv.getValue(this.classLoader, this.pool, methods[i]); 
/* 265 */         if (value == null)
/* 266 */           value = getDefault(name, methods[i]); 
/* 267 */         otherValue = methods[i].invoke(obj, new Object[0]);
/*     */       }
/* 269 */       catch (RuntimeException e) {
/* 270 */         throw e;
/*     */       }
/* 272 */       catch (Exception e) {
/* 273 */         throw new RuntimeException("Error retrieving value " + name + " for annotation " + this.annotation.getTypeName(), e);
/*     */       } 
/*     */       
/* 276 */       if (value == null && otherValue != null)
/* 277 */         return false; 
/* 278 */       if (value != null && !value.equals(otherValue)) {
/* 279 */         return false;
/*     */       }
/*     */     } 
/* 282 */     return true;
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
/*     */   private static int arrayHashCode(Object object) {
/* 294 */     if (object == null) {
/* 295 */       return 0;
/*     */     }
/* 297 */     int result = 1;
/*     */     
/* 299 */     Object[] array = (Object[])object;
/* 300 */     for (int i = 0; i < array.length; i++) {
/* 301 */       int elementHashCode = 0;
/* 302 */       if (array[i] != null)
/* 303 */         elementHashCode = array[i].hashCode(); 
/* 304 */       result = 31 * result + elementHashCode;
/*     */     } 
/* 306 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\annotation\AnnotationImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */