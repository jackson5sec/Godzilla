/*     */ package javassist.runtime;
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
/*     */ public class Desc
/*     */ {
/*     */   public static boolean useContextClassLoader = false;
/*     */   
/*  37 */   private static final ThreadLocal<Boolean> USE_CONTEXT_CLASS_LOADER_LOCALLY = new ThreadLocal<Boolean>()
/*     */     {
/*     */       protected Boolean initialValue() {
/*  40 */         return Boolean.valueOf(false);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setUseContextClassLoaderLocally() {
/*  52 */     USE_CONTEXT_CLASS_LOADER_LOCALLY.set(Boolean.valueOf(true));
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
/*     */   public static void resetUseContextClassLoaderLocally() {
/*  64 */     USE_CONTEXT_CLASS_LOADER_LOCALLY.remove();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Class<?> getClassObject(String name) throws ClassNotFoundException {
/*  70 */     if (useContextClassLoader || ((Boolean)USE_CONTEXT_CLASS_LOADER_LOCALLY.get()).booleanValue())
/*  71 */       return Class.forName(name, true, Thread.currentThread().getContextClassLoader()); 
/*  72 */     return Class.forName(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class<?> getClazz(String name) {
/*     */     try {
/*  81 */       return getClassObject(name);
/*     */     }
/*  83 */     catch (ClassNotFoundException e) {
/*  84 */       throw new RuntimeException("$class: internal error, could not find class '" + name + "' (Desc.useContextClassLoader: " + 
/*     */ 
/*     */           
/*  87 */           Boolean.toString(useContextClassLoader) + ")", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class<?>[] getParams(String desc) {
/*  96 */     if (desc.charAt(0) != '(') {
/*  97 */       throw new RuntimeException("$sig: internal error");
/*     */     }
/*  99 */     return getType(desc, desc.length(), 1, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class<?> getType(String desc) {
/* 107 */     Class<?>[] result = getType(desc, desc.length(), 0, 0);
/* 108 */     if (result == null || result.length != 1) {
/* 109 */       throw new RuntimeException("$type: internal error");
/*     */     }
/* 111 */     return result[0];
/*     */   }
/*     */ 
/*     */   
/*     */   private static Class<?>[] getType(String desc, int descLen, int start, int num) {
/*     */     Class<?> clazz, result[];
/* 117 */     if (start >= descLen) {
/* 118 */       return new Class[num];
/*     */     }
/* 120 */     char c = desc.charAt(start);
/* 121 */     switch (c) {
/*     */       case 'Z':
/* 123 */         clazz = boolean.class;
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
/* 156 */         result = getType(desc, descLen, start + 1, num + 1);
/* 157 */         result[num] = clazz;
/* 158 */         return result;case 'C': clazz = char.class; result = getType(desc, descLen, start + 1, num + 1); result[num] = clazz; return result;case 'B': clazz = byte.class; result = getType(desc, descLen, start + 1, num + 1); result[num] = clazz; return result;case 'S': clazz = short.class; result = getType(desc, descLen, start + 1, num + 1); result[num] = clazz; return result;case 'I': clazz = int.class; result = getType(desc, descLen, start + 1, num + 1); result[num] = clazz; return result;case 'J': clazz = long.class; result = getType(desc, descLen, start + 1, num + 1); result[num] = clazz; return result;case 'F': clazz = float.class; result = getType(desc, descLen, start + 1, num + 1); result[num] = clazz; return result;case 'D': clazz = double.class; result = getType(desc, descLen, start + 1, num + 1); result[num] = clazz; return result;case 'V': clazz = void.class; result = getType(desc, descLen, start + 1, num + 1); result[num] = clazz; return result;
/*     */       case 'L':
/*     */       case '[':
/*     */         return getClassType(desc, descLen, start, num);
/*     */     }  return new Class[num]; } private static Class<?>[] getClassType(String desc, int descLen, int start, int num) { String cname;
/* 163 */     int end = start;
/* 164 */     while (desc.charAt(end) == '[') {
/* 165 */       end++;
/*     */     }
/* 167 */     if (desc.charAt(end) == 'L') {
/* 168 */       end = desc.indexOf(';', end);
/* 169 */       if (end < 0) {
/* 170 */         throw new IndexOutOfBoundsException("bad descriptor");
/*     */       }
/*     */     } 
/*     */     
/* 174 */     if (desc.charAt(start) == 'L') {
/* 175 */       cname = desc.substring(start + 1, end);
/*     */     } else {
/* 177 */       cname = desc.substring(start, end + 1);
/*     */     } 
/* 179 */     Class<?>[] result = getType(desc, descLen, end + 1, num + 1);
/*     */     try {
/* 181 */       result[num] = getClassObject(cname.replace('/', '.'));
/*     */     }
/* 183 */     catch (ClassNotFoundException e) {
/*     */       
/* 185 */       throw new RuntimeException(e.getMessage());
/*     */     } 
/*     */     
/* 188 */     return result; }
/*     */ 
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\runtime\Desc.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */