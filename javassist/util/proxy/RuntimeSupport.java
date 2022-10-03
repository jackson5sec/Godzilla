/*     */ package javassist.util.proxy;
/*     */ 
/*     */ import java.io.InvalidClassException;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Method;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RuntimeSupport
/*     */ {
/*  31 */   public static MethodHandler default_interceptor = new DefaultMethodHandler();
/*     */ 
/*     */ 
/*     */   
/*     */   static class DefaultMethodHandler
/*     */     implements MethodHandler, Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */ 
/*     */     
/*     */     public Object invoke(Object self, Method m, Method proceed, Object[] args) throws Exception {
/*  42 */       return proceed.invoke(self, args);
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
/*     */ 
/*     */   
/*     */   public static void find2Methods(Class<?> clazz, String superMethod, String thisMethod, int index, String desc, Method[] methods) {
/*  57 */     methods[index + 1] = (thisMethod == null) ? null : 
/*  58 */       findMethod(clazz, thisMethod, desc);
/*  59 */     methods[index] = findSuperClassMethod(clazz, superMethod, desc);
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
/*     */   @Deprecated
/*     */   public static void find2Methods(Object self, String superMethod, String thisMethod, int index, String desc, Method[] methods) {
/*  77 */     methods[index + 1] = (thisMethod == null) ? null : 
/*  78 */       findMethod(self, thisMethod, desc);
/*  79 */     methods[index] = findSuperMethod(self, superMethod, desc);
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
/*     */   @Deprecated
/*     */   public static Method findMethod(Object self, String name, String desc) {
/*  93 */     Method m = findMethod2(self.getClass(), name, desc);
/*  94 */     if (m == null) {
/*  95 */       error(self.getClass(), name, desc);
/*     */     }
/*  97 */     return m;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Method findMethod(Class<?> clazz, String name, String desc) {
/* 107 */     Method m = findMethod2(clazz, name, desc);
/* 108 */     if (m == null) {
/* 109 */       error(clazz, name, desc);
/*     */     }
/* 111 */     return m;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Method findSuperMethod(Object self, String name, String desc) {
/* 122 */     Class<?> clazz = self.getClass();
/* 123 */     return findSuperClassMethod(clazz, name, desc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Method findSuperClassMethod(Class<?> clazz, String name, String desc) {
/* 133 */     Method m = findSuperMethod2(clazz.getSuperclass(), name, desc);
/* 134 */     if (m == null) {
/* 135 */       m = searchInterfaces(clazz, name, desc);
/*     */     }
/* 137 */     if (m == null) {
/* 138 */       error(clazz, name, desc);
/*     */     }
/* 140 */     return m;
/*     */   }
/*     */   
/*     */   private static void error(Class<?> clazz, String name, String desc) {
/* 144 */     throw new RuntimeException("not found " + name + ":" + desc + " in " + clazz
/* 145 */         .getName());
/*     */   }
/*     */   
/*     */   private static Method findSuperMethod2(Class<?> clazz, String name, String desc) {
/* 149 */     Method m = findMethod2(clazz, name, desc);
/* 150 */     if (m != null) {
/* 151 */       return m;
/*     */     }
/* 153 */     Class<?> superClass = clazz.getSuperclass();
/* 154 */     if (superClass != null) {
/* 155 */       m = findSuperMethod2(superClass, name, desc);
/* 156 */       if (m != null) {
/* 157 */         return m;
/*     */       }
/*     */     } 
/* 160 */     return searchInterfaces(clazz, name, desc);
/*     */   }
/*     */   
/*     */   private static Method searchInterfaces(Class<?> clazz, String name, String desc) {
/* 164 */     Method m = null;
/* 165 */     Class<?>[] interfaces = clazz.getInterfaces();
/* 166 */     for (int i = 0; i < interfaces.length; i++) {
/* 167 */       m = findSuperMethod2(interfaces[i], name, desc);
/* 168 */       if (m != null) {
/* 169 */         return m;
/*     */       }
/*     */     } 
/* 172 */     return m;
/*     */   }
/*     */   
/*     */   private static Method findMethod2(Class<?> clazz, String name, String desc) {
/* 176 */     Method[] methods = SecurityActions.getDeclaredMethods(clazz);
/* 177 */     int n = methods.length;
/* 178 */     for (int i = 0; i < n; i++) {
/* 179 */       if (methods[i].getName().equals(name) && 
/* 180 */         makeDescriptor(methods[i]).equals(desc))
/* 181 */         return methods[i]; 
/*     */     } 
/* 183 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String makeDescriptor(Method m) {
/* 190 */     Class<?>[] params = m.getParameterTypes();
/* 191 */     return makeDescriptor(params, m.getReturnType());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String makeDescriptor(Class<?>[] params, Class<?> retType) {
/* 201 */     StringBuffer sbuf = new StringBuffer();
/* 202 */     sbuf.append('(');
/* 203 */     for (int i = 0; i < params.length; i++) {
/* 204 */       makeDesc(sbuf, params[i]);
/*     */     }
/* 206 */     sbuf.append(')');
/* 207 */     if (retType != null) {
/* 208 */       makeDesc(sbuf, retType);
/*     */     }
/* 210 */     return sbuf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String makeDescriptor(String params, Class<?> retType) {
/* 220 */     StringBuffer sbuf = new StringBuffer(params);
/* 221 */     makeDesc(sbuf, retType);
/* 222 */     return sbuf.toString();
/*     */   }
/*     */   
/*     */   private static void makeDesc(StringBuffer sbuf, Class<?> type) {
/* 226 */     if (type.isArray()) {
/* 227 */       sbuf.append('[');
/* 228 */       makeDesc(sbuf, type.getComponentType());
/*     */     }
/* 230 */     else if (type.isPrimitive()) {
/* 231 */       if (type == void.class) {
/* 232 */         sbuf.append('V');
/* 233 */       } else if (type == int.class) {
/* 234 */         sbuf.append('I');
/* 235 */       } else if (type == byte.class) {
/* 236 */         sbuf.append('B');
/* 237 */       } else if (type == long.class) {
/* 238 */         sbuf.append('J');
/* 239 */       } else if (type == double.class) {
/* 240 */         sbuf.append('D');
/* 241 */       } else if (type == float.class) {
/* 242 */         sbuf.append('F');
/* 243 */       } else if (type == char.class) {
/* 244 */         sbuf.append('C');
/* 245 */       } else if (type == short.class) {
/* 246 */         sbuf.append('S');
/* 247 */       } else if (type == boolean.class) {
/* 248 */         sbuf.append('Z');
/*     */       } else {
/* 250 */         throw new RuntimeException("bad type: " + type.getName());
/*     */       } 
/*     */     } else {
/* 253 */       sbuf.append('L').append(type.getName().replace('.', '/'))
/* 254 */         .append(';');
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
/*     */   public static SerializedProxy makeSerializedProxy(Object proxy) throws InvalidClassException {
/* 267 */     Class<?> clazz = proxy.getClass();
/*     */     
/* 269 */     MethodHandler methodHandler = null;
/* 270 */     if (proxy instanceof ProxyObject) {
/* 271 */       methodHandler = ((ProxyObject)proxy).getHandler();
/* 272 */     } else if (proxy instanceof Proxy) {
/* 273 */       methodHandler = ProxyFactory.getHandler((Proxy)proxy);
/*     */     } 
/* 275 */     return new SerializedProxy(clazz, ProxyFactory.getFilterSignature(clazz), methodHandler);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassis\\util\proxy\RuntimeSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */