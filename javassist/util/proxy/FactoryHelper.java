/*     */ package javassist.util.proxy;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.lang.invoke.MethodHandles;
/*     */ import java.security.ProtectionDomain;
/*     */ import javassist.CannotCompileException;
/*     */ import javassist.bytecode.ClassFile;
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
/*     */ public class FactoryHelper
/*     */ {
/*     */   public static final int typeIndex(Class<?> type) {
/*  43 */     for (int i = 0; i < primitiveTypes.length; i++) {
/*  44 */       if (primitiveTypes[i] == type)
/*  45 */         return i; 
/*     */     } 
/*  47 */     throw new RuntimeException("bad type:" + type.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   public static final Class<?>[] primitiveTypes = new Class[] { boolean.class, byte.class, char.class, short.class, int.class, long.class, float.class, double.class, void.class };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   public static final String[] wrapperTypes = new String[] { "java.lang.Boolean", "java.lang.Byte", "java.lang.Character", "java.lang.Short", "java.lang.Integer", "java.lang.Long", "java.lang.Float", "java.lang.Double", "java.lang.Void" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   public static final String[] wrapperDesc = new String[] { "(Z)V", "(B)V", "(C)V", "(S)V", "(I)V", "(J)V", "(F)V", "(D)V" };
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
/*  81 */   public static final String[] unwarpMethods = new String[] { "booleanValue", "byteValue", "charValue", "shortValue", "intValue", "longValue", "floatValue", "doubleValue" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  90 */   public static final String[] unwrapDesc = new String[] { "()Z", "()B", "()C", "()S", "()I", "()J", "()F", "()D" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  98 */   public static final int[] dataSize = new int[] { 1, 1, 1, 1, 1, 2, 1, 2 };
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
/*     */   public static Class<?> toClass(ClassFile cf, ClassLoader loader) throws CannotCompileException {
/* 113 */     return toClass(cf, null, loader, null);
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
/*     */   public static Class<?> toClass(ClassFile cf, ClassLoader loader, ProtectionDomain domain) throws CannotCompileException {
/* 131 */     return toClass(cf, null, loader, domain);
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
/*     */   public static Class<?> toClass(ClassFile cf, Class<?> neighbor, ClassLoader loader, ProtectionDomain domain) throws CannotCompileException {
/*     */     try {
/* 150 */       byte[] b = toBytecode(cf);
/* 151 */       if (ProxyFactory.onlyPublicMethods) {
/* 152 */         return DefineClassHelper.toPublicClass(cf.getName(), b);
/*     */       }
/* 154 */       return DefineClassHelper.toClass(cf.getName(), neighbor, loader, domain, b);
/*     */     
/*     */     }
/* 157 */     catch (IOException e) {
/* 158 */       throw new CannotCompileException(e);
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
/*     */   public static Class<?> toClass(ClassFile cf, MethodHandles.Lookup lookup) throws CannotCompileException {
/*     */     try {
/* 172 */       byte[] b = toBytecode(cf);
/* 173 */       return DefineClassHelper.toClass(lookup, b);
/*     */     }
/* 175 */     catch (IOException e) {
/* 176 */       throw new CannotCompileException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static byte[] toBytecode(ClassFile cf) throws IOException {
/* 181 */     ByteArrayOutputStream barray = new ByteArrayOutputStream();
/* 182 */     DataOutputStream out = new DataOutputStream(barray);
/*     */     try {
/* 184 */       cf.write(out);
/*     */     } finally {
/*     */       
/* 187 */       out.close();
/*     */     } 
/*     */     
/* 190 */     return barray.toByteArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writeFile(ClassFile cf, String directoryName) throws CannotCompileException {
/*     */     try {
/* 199 */       writeFile0(cf, directoryName);
/*     */     }
/* 201 */     catch (IOException e) {
/* 202 */       throw new CannotCompileException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void writeFile0(ClassFile cf, String directoryName) throws CannotCompileException, IOException {
/* 208 */     String classname = cf.getName();
/*     */     
/* 210 */     String filename = directoryName + File.separatorChar + classname.replace('.', File.separatorChar) + ".class";
/* 211 */     int pos = filename.lastIndexOf(File.separatorChar);
/* 212 */     if (pos > 0) {
/* 213 */       String dir = filename.substring(0, pos);
/* 214 */       if (!dir.equals(".")) {
/* 215 */         (new File(dir)).mkdirs();
/*     */       }
/*     */     } 
/* 218 */     DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(filename)));
/*     */     
/*     */     try {
/* 221 */       cf.write(out);
/*     */     }
/* 223 */     catch (IOException e) {
/* 224 */       throw e;
/*     */     } finally {
/*     */       
/* 227 */       out.close();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassis\\util\proxy\FactoryHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */