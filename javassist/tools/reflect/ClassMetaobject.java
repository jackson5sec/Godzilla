/*     */ package javassist.tools.reflect;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
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
/*     */ public class ClassMetaobject
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   static final String methodPrefix = "_m_";
/*     */   static final int methodPrefixLen = 3;
/*     */   private Class<?> javaClass;
/*     */   private Constructor<?>[] constructors;
/*     */   private Method[] methods;
/*     */   public static boolean useContextClassLoader = false;
/*     */   
/*     */   public ClassMetaobject(String[] params) {
/*     */     try {
/*  78 */       this.javaClass = getClassObject(params[0]);
/*     */     }
/*  80 */     catch (ClassNotFoundException e) {
/*  81 */       throw new RuntimeException("not found: " + params[0] + ", useContextClassLoader: " + 
/*     */           
/*  83 */           Boolean.toString(useContextClassLoader), e);
/*     */     } 
/*     */     
/*  86 */     this.constructors = this.javaClass.getConstructors();
/*  87 */     this.methods = null;
/*     */   }
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/*  91 */     out.writeUTF(this.javaClass.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/*  97 */     this.javaClass = getClassObject(in.readUTF());
/*  98 */     this.constructors = this.javaClass.getConstructors();
/*  99 */     this.methods = null;
/*     */   }
/*     */   
/*     */   private Class<?> getClassObject(String name) throws ClassNotFoundException {
/* 103 */     if (useContextClassLoader)
/* 104 */       return Thread.currentThread().getContextClassLoader()
/* 105 */         .loadClass(name); 
/* 106 */     return Class.forName(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Class<?> getJavaClass() {
/* 113 */     return this.javaClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getName() {
/* 120 */     return this.javaClass.getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isInstance(Object obj) {
/* 127 */     return this.javaClass.isInstance(obj);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Object newInstance(Object[] args) throws CannotCreateException {
/* 138 */     int n = this.constructors.length;
/* 139 */     for (int i = 0; i < n; i++) {
/*     */       try {
/* 141 */         return this.constructors[i].newInstance(args);
/*     */       }
/* 143 */       catch (IllegalArgumentException illegalArgumentException) {
/*     */ 
/*     */       
/* 146 */       } catch (InstantiationException e) {
/* 147 */         throw new CannotCreateException(e);
/*     */       }
/* 149 */       catch (IllegalAccessException e) {
/* 150 */         throw new CannotCreateException(e);
/*     */       }
/* 152 */       catch (InvocationTargetException e) {
/* 153 */         throw new CannotCreateException(e);
/*     */       } 
/*     */     } 
/*     */     
/* 157 */     throw new CannotCreateException("no constructor matches");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object trapFieldRead(String name) {
/* 168 */     Class<?> jc = getJavaClass();
/*     */     try {
/* 170 */       return jc.getField(name).get(null);
/*     */     }
/* 172 */     catch (NoSuchFieldException e) {
/* 173 */       throw new RuntimeException(e.toString());
/*     */     }
/* 175 */     catch (IllegalAccessException e) {
/* 176 */       throw new RuntimeException(e.toString());
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
/*     */   public void trapFieldWrite(String name, Object value) {
/* 188 */     Class<?> jc = getJavaClass();
/*     */     try {
/* 190 */       jc.getField(name).set(null, value);
/*     */     }
/* 192 */     catch (NoSuchFieldException e) {
/* 193 */       throw new RuntimeException(e.toString());
/*     */     }
/* 195 */     catch (IllegalAccessException e) {
/* 196 */       throw new RuntimeException(e.toString());
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
/*     */   public static Object invoke(Object target, int identifier, Object[] args) throws Throwable {
/* 209 */     Method[] allmethods = target.getClass().getMethods();
/* 210 */     int n = allmethods.length;
/* 211 */     String head = "_m_" + identifier;
/* 212 */     for (int i = 0; i < n; i++) {
/* 213 */       if (allmethods[i].getName().startsWith(head)) {
/*     */         try {
/* 215 */           return allmethods[i].invoke(target, args);
/* 216 */         } catch (InvocationTargetException e) {
/* 217 */           throw e.getTargetException();
/* 218 */         } catch (IllegalAccessException e) {
/* 219 */           throw new CannotInvokeException(e);
/*     */         } 
/*     */       }
/*     */     } 
/* 223 */     throw new CannotInvokeException("cannot find a method");
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
/*     */   public Object trapMethodcall(int identifier, Object[] args) throws Throwable {
/*     */     try {
/* 238 */       Method[] m = getReflectiveMethods();
/* 239 */       return m[identifier].invoke(null, args);
/*     */     }
/* 241 */     catch (InvocationTargetException e) {
/* 242 */       throw e.getTargetException();
/*     */     }
/* 244 */     catch (IllegalAccessException e) {
/* 245 */       throw new CannotInvokeException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Method[] getReflectiveMethods() {
/* 254 */     if (this.methods != null) {
/* 255 */       return this.methods;
/*     */     }
/* 257 */     Class<?> baseclass = getJavaClass();
/* 258 */     Method[] allmethods = baseclass.getDeclaredMethods();
/* 259 */     int n = allmethods.length;
/* 260 */     int[] index = new int[n];
/* 261 */     int max = 0; int i;
/* 262 */     for (i = 0; i < n; i++) {
/* 263 */       Method m = allmethods[i];
/* 264 */       String mname = m.getName();
/* 265 */       if (mname.startsWith("_m_")) {
/* 266 */         int k = 0;
/* 267 */         int j = 3; while (true) {
/* 268 */           char c = mname.charAt(j);
/* 269 */           if ('0' <= c && c <= '9') {
/* 270 */             k = k * 10 + c - 48;
/*     */             j++;
/*     */           } 
/*     */           break;
/*     */         } 
/* 275 */         index[i] = ++k;
/* 276 */         if (k > max) {
/* 277 */           max = k;
/*     */         }
/*     */       } 
/*     */     } 
/* 281 */     this.methods = new Method[max];
/* 282 */     for (i = 0; i < n; i++) {
/* 283 */       if (index[i] > 0)
/* 284 */         this.methods[index[i] - 1] = allmethods[i]; 
/*     */     } 
/* 286 */     return this.methods;
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
/*     */   public final Method getMethod(int identifier) {
/* 302 */     return getReflectiveMethods()[identifier];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getMethodName(int identifier) {
/*     */     char c;
/* 310 */     String mname = getReflectiveMethods()[identifier].getName();
/* 311 */     int j = 3;
/*     */     do {
/* 313 */       c = mname.charAt(j++);
/* 314 */     } while (c >= '0' && '9' >= c);
/*     */ 
/*     */ 
/*     */     
/* 318 */     return mname.substring(j);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Class<?>[] getParameterTypes(int identifier) {
/* 327 */     return getReflectiveMethods()[identifier].getParameterTypes();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Class<?> getReturnType(int identifier) {
/* 335 */     return getReflectiveMethods()[identifier].getReturnType();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getMethodIndex(String originalName, Class<?>[] argTypes) throws NoSuchMethodException {
/* 359 */     Method[] mthds = getReflectiveMethods();
/* 360 */     for (int i = 0; i < mthds.length; i++) {
/* 361 */       if (mthds[i] != null)
/*     */       {
/*     */ 
/*     */         
/* 365 */         if (getMethodName(i).equals(originalName) && 
/* 366 */           Arrays.equals((Object[])argTypes, (Object[])mthds[i].getParameterTypes()))
/* 367 */           return i; 
/*     */       }
/*     */     } 
/* 370 */     throw new NoSuchMethodException("Method " + originalName + " not found");
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\tools\reflect\ClassMetaobject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */