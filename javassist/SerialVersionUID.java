/*     */ package javassist;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import javassist.bytecode.ClassFile;
/*     */ import javassist.bytecode.Descriptor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SerialVersionUID
/*     */ {
/*     */   public static void setSerialVersionUID(CtClass clazz) throws CannotCompileException, NotFoundException {
/*     */     try {
/*  48 */       clazz.getDeclaredField("serialVersionUID");
/*     */       
/*     */       return;
/*  51 */     } catch (NotFoundException notFoundException) {
/*     */ 
/*     */       
/*  54 */       if (!isSerializable(clazz)) {
/*     */         return;
/*     */       }
/*     */       
/*  58 */       CtField field = new CtField(CtClass.longType, "serialVersionUID", clazz);
/*     */       
/*  60 */       field.setModifiers(26);
/*     */       
/*  62 */       clazz.addField(field, calculateDefault(clazz) + "L");
/*     */       return;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isSerializable(CtClass clazz) throws NotFoundException {
/*  71 */     ClassPool pool = clazz.getClassPool();
/*  72 */     return clazz.subtypeOf(pool.get("java.io.Serializable"));
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
/*     */   public static long calculateDefault(CtClass clazz) throws CannotCompileException {
/*     */     try {
/*  85 */       ByteArrayOutputStream bout = new ByteArrayOutputStream();
/*  86 */       DataOutputStream out = new DataOutputStream(bout);
/*  87 */       ClassFile classFile = clazz.getClassFile();
/*     */ 
/*     */       
/*  90 */       String javaName = javaName(clazz);
/*  91 */       out.writeUTF(javaName);
/*     */       
/*  93 */       CtMethod[] methods = clazz.getDeclaredMethods();
/*     */ 
/*     */       
/*  96 */       int classMods = clazz.getModifiers();
/*  97 */       if ((classMods & 0x200) != 0)
/*  98 */         if (methods.length > 0) {
/*  99 */           classMods |= 0x400;
/*     */         } else {
/* 101 */           classMods &= 0xFFFFFBFF;
/*     */         }  
/* 103 */       out.writeInt(classMods);
/*     */ 
/*     */       
/* 106 */       String[] interfaces = classFile.getInterfaces(); int i;
/* 107 */       for (i = 0; i < interfaces.length; i++) {
/* 108 */         interfaces[i] = javaName(interfaces[i]);
/*     */       }
/* 110 */       Arrays.sort((Object[])interfaces);
/* 111 */       for (i = 0; i < interfaces.length; i++) {
/* 112 */         out.writeUTF(interfaces[i]);
/*     */       }
/*     */       
/* 115 */       CtField[] fields = clazz.getDeclaredFields();
/* 116 */       Arrays.sort(fields, new Comparator<CtField>()
/*     */           {
/*     */             public int compare(CtField field1, CtField field2) {
/* 119 */               return field1.getName().compareTo(field2.getName());
/*     */             }
/*     */           });
/*     */       
/* 123 */       for (int j = 0; j < fields.length; j++) {
/* 124 */         CtField field = fields[j];
/* 125 */         int mods = field.getModifiers();
/* 126 */         if ((mods & 0x2) == 0 || (mods & 0x88) == 0) {
/*     */           
/* 128 */           out.writeUTF(field.getName());
/* 129 */           out.writeInt(mods);
/* 130 */           out.writeUTF(field.getFieldInfo2().getDescriptor());
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 135 */       if (classFile.getStaticInitializer() != null) {
/* 136 */         out.writeUTF("<clinit>");
/* 137 */         out.writeInt(8);
/* 138 */         out.writeUTF("()V");
/*     */       } 
/*     */ 
/*     */       
/* 142 */       CtConstructor[] constructors = clazz.getDeclaredConstructors();
/* 143 */       Arrays.sort(constructors, new Comparator<CtConstructor>()
/*     */           {
/*     */             public int compare(CtConstructor c1, CtConstructor c2) {
/* 146 */               return c1.getMethodInfo2().getDescriptor().compareTo(c2
/* 147 */                   .getMethodInfo2().getDescriptor());
/*     */             }
/*     */           });
/*     */       int k;
/* 151 */       for (k = 0; k < constructors.length; k++) {
/* 152 */         CtConstructor constructor = constructors[k];
/* 153 */         int mods = constructor.getModifiers();
/* 154 */         if ((mods & 0x2) == 0) {
/* 155 */           out.writeUTF("<init>");
/* 156 */           out.writeInt(mods);
/* 157 */           out.writeUTF(constructor.getMethodInfo2()
/* 158 */               .getDescriptor().replace('/', '.'));
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 163 */       Arrays.sort(methods, new Comparator<CtMethod>()
/*     */           {
/*     */             public int compare(CtMethod m1, CtMethod m2) {
/* 166 */               int value = m1.getName().compareTo(m2.getName());
/* 167 */               if (value == 0)
/*     */               {
/* 169 */                 value = m1.getMethodInfo2().getDescriptor().compareTo(m2.getMethodInfo2().getDescriptor());
/*     */               }
/* 171 */               return value;
/*     */             }
/*     */           });
/*     */       
/* 175 */       for (k = 0; k < methods.length; k++) {
/* 176 */         CtMethod method = methods[k];
/* 177 */         int mods = method.getModifiers() & 0xD3F;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 182 */         if ((mods & 0x2) == 0) {
/* 183 */           out.writeUTF(method.getName());
/* 184 */           out.writeInt(mods);
/* 185 */           out.writeUTF(method.getMethodInfo2()
/* 186 */               .getDescriptor().replace('/', '.'));
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 191 */       out.flush();
/* 192 */       MessageDigest digest = MessageDigest.getInstance("SHA");
/* 193 */       byte[] digested = digest.digest(bout.toByteArray());
/* 194 */       long hash = 0L;
/* 195 */       for (int m = Math.min(digested.length, 8) - 1; m >= 0; m--) {
/* 196 */         hash = hash << 8L | (digested[m] & 0xFF);
/*     */       }
/* 198 */       return hash;
/*     */     }
/* 200 */     catch (IOException e) {
/* 201 */       throw new CannotCompileException(e);
/*     */     }
/* 203 */     catch (NoSuchAlgorithmException e) {
/* 204 */       throw new CannotCompileException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static String javaName(CtClass clazz) {
/* 209 */     return Descriptor.toJavaName(Descriptor.toJvmName(clazz));
/*     */   }
/*     */   
/*     */   private static String javaName(String name) {
/* 213 */     return Descriptor.toJavaName(Descriptor.toJvmName(name));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\SerialVersionUID.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */