/*     */ package javassist;
/*     */ 
/*     */ import java.lang.reflect.Modifier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Modifier
/*     */ {
/*     */   public static final int PUBLIC = 1;
/*     */   public static final int PRIVATE = 2;
/*     */   public static final int PROTECTED = 4;
/*     */   public static final int STATIC = 8;
/*     */   public static final int FINAL = 16;
/*     */   public static final int SYNCHRONIZED = 32;
/*     */   public static final int VOLATILE = 64;
/*     */   public static final int VARARGS = 128;
/*     */   public static final int TRANSIENT = 128;
/*     */   public static final int NATIVE = 256;
/*     */   public static final int INTERFACE = 512;
/*     */   public static final int ABSTRACT = 1024;
/*     */   public static final int STRICT = 2048;
/*     */   public static final int ANNOTATION = 8192;
/*     */   public static final int ENUM = 16384;
/*     */   
/*     */   public static boolean isPublic(int mod) {
/*  53 */     return ((mod & 0x1) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isPrivate(int mod) {
/*  61 */     return ((mod & 0x2) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isProtected(int mod) {
/*  69 */     return ((mod & 0x4) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isPackage(int mod) {
/*  77 */     return ((mod & 0x7) == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isStatic(int mod) {
/*  85 */     return ((mod & 0x8) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isFinal(int mod) {
/*  93 */     return ((mod & 0x10) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isSynchronized(int mod) {
/* 101 */     return ((mod & 0x20) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isVolatile(int mod) {
/* 109 */     return ((mod & 0x40) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isTransient(int mod) {
/* 117 */     return ((mod & 0x80) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isNative(int mod) {
/* 125 */     return ((mod & 0x100) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isInterface(int mod) {
/* 133 */     return ((mod & 0x200) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isAnnotation(int mod) {
/* 143 */     return ((mod & 0x2000) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isEnum(int mod) {
/* 153 */     return ((mod & 0x4000) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isAbstract(int mod) {
/* 161 */     return ((mod & 0x400) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isStrict(int mod) {
/* 169 */     return ((mod & 0x800) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isVarArgs(int mod) {
/* 177 */     return ((mod & 0x80) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int setPublic(int mod) {
/* 185 */     return mod & 0xFFFFFFF9 | 0x1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int setProtected(int mod) {
/* 193 */     return mod & 0xFFFFFFFC | 0x4;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int setPrivate(int mod) {
/* 201 */     return mod & 0xFFFFFFFA | 0x2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int setPackage(int mod) {
/* 208 */     return mod & 0xFFFFFFF8;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int clear(int mod, int clearBit) {
/* 215 */     return mod & (clearBit ^ 0xFFFFFFFF);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toString(int mod) {
/* 225 */     return Modifier.toString(mod);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\Modifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */