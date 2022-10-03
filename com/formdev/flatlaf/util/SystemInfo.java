/*     */ package com.formdev.flatlaf.util;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SystemInfo
/*     */ {
/*     */   public static final boolean isWindows;
/*     */   public static final boolean isMacOS;
/*     */   public static final boolean isLinux;
/*     */   
/*     */   static {
/*  56 */     String osName = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
/*  57 */     isWindows = osName.startsWith("windows");
/*  58 */     isMacOS = osName.startsWith("mac");
/*  59 */     isLinux = osName.startsWith("linux");
/*     */   }
/*     */   
/*  62 */   public static final long osVersion = scanVersion(System.getProperty("os.version"));
/*  63 */   public static final boolean isWindows_10_orLater = (isWindows && osVersion >= toVersion(10, 0, 0, 0));
/*  64 */   public static final boolean isMacOS_10_11_ElCapitan_orLater = (isMacOS && osVersion >= toVersion(10, 11, 0, 0));
/*  65 */   public static final boolean isMacOS_10_14_Mojave_orLater = (isMacOS && osVersion >= toVersion(10, 14, 0, 0));
/*  66 */   public static final boolean isMacOS_10_15_Catalina_orLater = (isMacOS && osVersion >= toVersion(10, 15, 0, 0));
/*     */ 
/*     */   
/*  69 */   public static final long javaVersion = scanVersion(System.getProperty("java.version"));
/*  70 */   public static final boolean isJava_9_orLater = (javaVersion >= toVersion(9, 0, 0, 0));
/*  71 */   public static final boolean isJava_11_orLater = (javaVersion >= toVersion(11, 0, 0, 0));
/*  72 */   public static final boolean isJava_15_orLater = (javaVersion >= toVersion(15, 0, 0, 0));
/*     */ 
/*     */   
/*  75 */   public static final boolean isJetBrainsJVM = System.getProperty("java.vm.vendor", "Unknown")
/*  76 */     .toLowerCase(Locale.ENGLISH).contains("jetbrains");
/*  77 */   public static final boolean isJetBrainsJVM_11_orLater = (isJetBrainsJVM && isJava_11_orLater);
/*     */ 
/*     */   
/*  80 */   public static final boolean isKDE = (isLinux && System.getenv("KDE_FULL_SESSION") != null);
/*     */ 
/*     */   
/*     */   public static long scanVersion(String version) {
/*  84 */     int major = 1;
/*  85 */     int minor = 0;
/*  86 */     int micro = 0;
/*  87 */     int patch = 0;
/*     */     try {
/*  89 */       StringTokenizer st = new StringTokenizer(version, "._-+");
/*  90 */       major = Integer.parseInt(st.nextToken());
/*  91 */       minor = Integer.parseInt(st.nextToken());
/*  92 */       micro = Integer.parseInt(st.nextToken());
/*  93 */       patch = Integer.parseInt(st.nextToken());
/*  94 */     } catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */     
/*  98 */     return toVersion(major, minor, micro, patch);
/*     */   }
/*     */   
/*     */   public static long toVersion(int major, int minor, int micro, int patch) {
/* 102 */     return (major << 48L) + (minor << 32L) + (micro << 16L) + patch;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\util\SystemInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */