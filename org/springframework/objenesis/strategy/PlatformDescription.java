/*     */ package org.springframework.objenesis.strategy;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import org.springframework.objenesis.ObjenesisException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class PlatformDescription
/*     */ {
/*     */   public static final String GNU = "GNU libgcj";
/*     */   public static final String HOTSPOT = "Java HotSpot";
/*     */   @Deprecated
/*     */   public static final String SUN = "Java HotSpot";
/*     */   public static final String OPENJDK = "OpenJDK";
/*     */   public static final String PERC = "PERC";
/*     */   public static final String DALVIK = "Dalvik";
/*  54 */   public static final String SPECIFICATION_VERSION = System.getProperty("java.specification.version");
/*     */ 
/*     */   
/*  57 */   public static final String VM_VERSION = System.getProperty("java.runtime.version");
/*     */ 
/*     */   
/*  60 */   public static final String VM_INFO = System.getProperty("java.vm.info");
/*     */ 
/*     */   
/*  63 */   public static final String VENDOR_VERSION = System.getProperty("java.vm.version");
/*     */ 
/*     */   
/*  66 */   public static final String VENDOR = System.getProperty("java.vm.vendor");
/*     */ 
/*     */   
/*  69 */   public static final String JVM_NAME = System.getProperty("java.vm.name");
/*     */ 
/*     */   
/*  72 */   public static final int ANDROID_VERSION = getAndroidVersion();
/*     */ 
/*     */   
/*  75 */   public static final boolean IS_ANDROID_OPENJDK = getIsAndroidOpenJDK();
/*     */ 
/*     */   
/*  78 */   public static final String GAE_VERSION = getGaeRuntimeVersion();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String describePlatform() {
/*  86 */     String desc = "Java " + SPECIFICATION_VERSION + " (VM vendor name=\"" + VENDOR + "\", VM vendor version=" + VENDOR_VERSION + ", JVM name=\"" + JVM_NAME + "\", JVM version=" + VM_VERSION + ", JVM info=" + VM_INFO;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  94 */     if (ANDROID_VERSION != 0) {
/*  95 */       desc = desc + ", API level=" + ANDROID_VERSION;
/*     */     }
/*  97 */     desc = desc + ")";
/*     */     
/*  99 */     return desc;
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
/*     */   public static boolean isThisJVM(String name) {
/* 111 */     return JVM_NAME.startsWith(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isAndroidOpenJDK() {
/* 120 */     return IS_ANDROID_OPENJDK;
/*     */   }
/*     */   
/*     */   private static boolean getIsAndroidOpenJDK() {
/* 124 */     if (getAndroidVersion() == 0) {
/* 125 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 129 */     String bootClasspath = System.getProperty("java.boot.class.path");
/* 130 */     return (bootClasspath != null && bootClasspath.toLowerCase().contains("core-oj.jar"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isAfterJigsaw() {
/* 139 */     String version = SPECIFICATION_VERSION;
/* 140 */     return (version.indexOf('.') < 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isAfterJava11() {
/* 149 */     if (!isAfterJigsaw()) {
/* 150 */       return false;
/*     */     }
/* 152 */     int version = Integer.parseInt(SPECIFICATION_VERSION);
/* 153 */     return (version >= 11);
/*     */   }
/*     */   
/*     */   public static boolean isGoogleAppEngine() {
/* 157 */     return (GAE_VERSION != null);
/*     */   }
/*     */   
/*     */   private static String getGaeRuntimeVersion() {
/* 161 */     return System.getProperty("com.google.appengine.runtime.version");
/*     */   }
/*     */   
/*     */   private static int getAndroidVersion() {
/* 165 */     if (!isThisJVM("Dalvik")) {
/* 166 */       return 0;
/*     */     }
/* 168 */     return getAndroidVersion0();
/*     */   } private static int getAndroidVersion0() {
/*     */     Class<?> clazz;
/*     */     Field field;
/*     */     int version;
/*     */     try {
/* 174 */       clazz = Class.forName("android.os.Build$VERSION");
/*     */     }
/* 176 */     catch (ClassNotFoundException e) {
/* 177 */       throw new ObjenesisException(e);
/*     */     } 
/*     */     
/*     */     try {
/* 181 */       field = clazz.getField("SDK_INT");
/*     */     }
/* 183 */     catch (NoSuchFieldException e) {
/*     */       
/* 185 */       return getOldAndroidVersion(clazz);
/*     */     } 
/*     */     
/*     */     try {
/* 189 */       version = ((Integer)field.get(null)).intValue();
/*     */     }
/* 191 */     catch (IllegalAccessException e) {
/* 192 */       throw new RuntimeException(e);
/*     */     } 
/* 194 */     return version;
/*     */   }
/*     */   private static int getOldAndroidVersion(Class<?> versionClass) {
/*     */     Field field;
/*     */     String version;
/*     */     try {
/* 200 */       field = versionClass.getField("SDK");
/*     */     }
/* 202 */     catch (NoSuchFieldException e) {
/* 203 */       throw new ObjenesisException(e);
/*     */     } 
/*     */     
/*     */     try {
/* 207 */       version = (String)field.get(null);
/*     */     }
/* 209 */     catch (IllegalAccessException e) {
/* 210 */       throw new RuntimeException(e);
/*     */     } 
/* 212 */     return Integer.parseInt(version);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\objenesis\strategy\PlatformDescription.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */