/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Executable;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.asm.ClassReader;
/*     */ import org.springframework.asm.ClassVisitor;
/*     */ import org.springframework.asm.Label;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public class LocalVariableTableParameterNameDiscoverer
/*     */   implements ParameterNameDiscoverer
/*     */ {
/*  59 */   private static final Log logger = LogFactory.getLog(LocalVariableTableParameterNameDiscoverer.class);
/*     */ 
/*     */   
/*  62 */   private static final Map<Executable, String[]> NO_DEBUG_INFO_MAP = (Map)Collections.emptyMap();
/*     */ 
/*     */   
/*  65 */   private final Map<Class<?>, Map<Executable, String[]>> parameterNamesCache = new ConcurrentHashMap<>(32);
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String[] getParameterNames(Method method) {
/*  71 */     Method originalMethod = BridgeMethodResolver.findBridgedMethod(method);
/*  72 */     return doGetParameterNames(originalMethod);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String[] getParameterNames(Constructor<?> ctor) {
/*  78 */     return doGetParameterNames(ctor);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private String[] doGetParameterNames(Executable executable) {
/*  83 */     Class<?> declaringClass = executable.getDeclaringClass();
/*  84 */     Map<Executable, String[]> map = this.parameterNamesCache.computeIfAbsent(declaringClass, this::inspectClass);
/*  85 */     return (map != NO_DEBUG_INFO_MAP) ? map.get(executable) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<Executable, String[]> inspectClass(Class<?> clazz) {
/*  94 */     InputStream is = clazz.getResourceAsStream(ClassUtils.getClassFileName(clazz));
/*  95 */     if (is == null) {
/*     */ 
/*     */       
/*  98 */       if (logger.isDebugEnabled()) {
/*  99 */         logger.debug("Cannot find '.class' file for class [" + clazz + "] - unable to determine constructor/method parameter names");
/*     */       }
/*     */       
/* 102 */       return NO_DEBUG_INFO_MAP;
/*     */     } 
/*     */     try {
/* 105 */       ClassReader classReader = new ClassReader(is);
/* 106 */       Map<Executable, String[]> map = (Map)new ConcurrentHashMap<>(32);
/* 107 */       classReader.accept(new ParameterNameDiscoveringVisitor(clazz, map), 0);
/* 108 */       return map;
/*     */     }
/* 110 */     catch (IOException ex) {
/* 111 */       if (logger.isDebugEnabled()) {
/* 112 */         logger.debug("Exception thrown while reading '.class' file for class [" + clazz + "] - unable to determine constructor/method parameter names", ex);
/*     */       
/*     */       }
/*     */     }
/* 116 */     catch (IllegalArgumentException ex) {
/* 117 */       if (logger.isDebugEnabled()) {
/* 118 */         logger.debug("ASM ClassReader failed to parse class file [" + clazz + "], probably due to a new Java class file version that isn't supported yet - unable to determine constructor/method parameter names", ex);
/*     */       }
/*     */     } finally {
/*     */ 
/*     */       
/*     */       try {
/*     */         
/* 125 */         is.close();
/*     */       }
/* 127 */       catch (IOException iOException) {}
/*     */     } 
/*     */ 
/*     */     
/* 131 */     return NO_DEBUG_INFO_MAP;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ParameterNameDiscoveringVisitor
/*     */     extends ClassVisitor
/*     */   {
/*     */     private static final String STATIC_CLASS_INIT = "<clinit>";
/*     */ 
/*     */     
/*     */     private final Class<?> clazz;
/*     */     
/*     */     private final Map<Executable, String[]> executableMap;
/*     */ 
/*     */     
/*     */     public ParameterNameDiscoveringVisitor(Class<?> clazz, Map<Executable, String[]> executableMap) {
/* 148 */       super(17432576);
/* 149 */       this.clazz = clazz;
/* 150 */       this.executableMap = executableMap;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
/* 157 */       if (!isSyntheticOrBridged(access) && !"<clinit>".equals(name)) {
/* 158 */         return new LocalVariableTableParameterNameDiscoverer.LocalVariableTableVisitor(this.clazz, this.executableMap, name, desc, isStatic(access));
/*     */       }
/* 160 */       return null;
/*     */     }
/*     */     
/*     */     private static boolean isSyntheticOrBridged(int access) {
/* 164 */       return ((access & 0x1000 | access & 0x40) > 0);
/*     */     }
/*     */     
/*     */     private static boolean isStatic(int access) {
/* 168 */       return ((access & 0x8) > 0);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class LocalVariableTableVisitor
/*     */     extends MethodVisitor
/*     */   {
/*     */     private static final String CONSTRUCTOR = "<init>";
/*     */ 
/*     */     
/*     */     private final Class<?> clazz;
/*     */     
/*     */     private final Map<Executable, String[]> executableMap;
/*     */     
/*     */     private final String name;
/*     */     
/*     */     private final Type[] args;
/*     */     
/*     */     private final String[] parameterNames;
/*     */     
/*     */     private final boolean isStatic;
/*     */     
/*     */     private boolean hasLvtInfo = false;
/*     */     
/*     */     private final int[] lvtSlotIndex;
/*     */ 
/*     */     
/*     */     public LocalVariableTableVisitor(Class<?> clazz, Map<Executable, String[]> map, String name, String desc, boolean isStatic) {
/* 198 */       super(17432576);
/* 199 */       this.clazz = clazz;
/* 200 */       this.executableMap = map;
/* 201 */       this.name = name;
/* 202 */       this.args = Type.getArgumentTypes(desc);
/* 203 */       this.parameterNames = new String[this.args.length];
/* 204 */       this.isStatic = isStatic;
/* 205 */       this.lvtSlotIndex = computeLvtSlotIndices(isStatic, this.args);
/*     */     }
/*     */ 
/*     */     
/*     */     public void visitLocalVariable(String name, String description, String signature, Label start, Label end, int index) {
/* 210 */       this.hasLvtInfo = true;
/* 211 */       for (int i = 0; i < this.lvtSlotIndex.length; i++) {
/* 212 */         if (this.lvtSlotIndex[i] == index) {
/* 213 */           this.parameterNames[i] = name;
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void visitEnd() {
/* 220 */       if (this.hasLvtInfo || (this.isStatic && this.parameterNames.length == 0))
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 225 */         this.executableMap.put(resolveExecutable(), this.parameterNames);
/*     */       }
/*     */     }
/*     */     
/*     */     private Executable resolveExecutable() {
/* 230 */       ClassLoader loader = this.clazz.getClassLoader();
/* 231 */       Class<?>[] argTypes = new Class[this.args.length];
/* 232 */       for (int i = 0; i < this.args.length; i++) {
/* 233 */         argTypes[i] = ClassUtils.resolveClassName(this.args[i].getClassName(), loader);
/*     */       }
/*     */       try {
/* 236 */         if ("<init>".equals(this.name)) {
/* 237 */           return this.clazz.getDeclaredConstructor(argTypes);
/*     */         }
/* 239 */         return this.clazz.getDeclaredMethod(this.name, argTypes);
/*     */       }
/* 241 */       catch (NoSuchMethodException ex) {
/* 242 */         throw new IllegalStateException("Method [" + this.name + "] was discovered in the .class file but cannot be resolved in the class object", ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private static int[] computeLvtSlotIndices(boolean isStatic, Type[] paramTypes) {
/* 248 */       int[] lvtIndex = new int[paramTypes.length];
/* 249 */       int nextIndex = isStatic ? 0 : 1;
/* 250 */       for (int i = 0; i < paramTypes.length; i++) {
/* 251 */         lvtIndex[i] = nextIndex;
/* 252 */         if (isWideType(paramTypes[i])) {
/* 253 */           nextIndex += 2;
/*     */         } else {
/*     */           
/* 256 */           nextIndex++;
/*     */         } 
/*     */       } 
/* 259 */       return lvtIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     private static boolean isWideType(Type aType) {
/* 264 */       return (aType == Type.LONG_TYPE || aType == Type.DOUBLE_TYPE);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\LocalVariableTableParameterNameDiscoverer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */