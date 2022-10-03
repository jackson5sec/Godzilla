/*     */ package com.jgoodies.common.base;
/*     */ 
/*     */ import java.awt.HeadlessException;
/*     */ import java.awt.Toolkit;
/*     */ import java.util.logging.Logger;
/*     */ import javax.swing.UIManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SystemUtils
/*     */ {
/*  55 */   protected static final String OS_NAME = getSystemProperty("os.name");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  64 */   protected static final String OS_VERSION = getSystemProperty("os.version");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   protected static final String JAVA_VERSION = getSystemProperty("java.version");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   public static final boolean IS_OS_LINUX = (startsWith(OS_NAME, "Linux") || startsWith(OS_NAME, "LINUX"));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  88 */   public static final boolean IS_OS_MAC = startsWith(OS_NAME, "Mac OS");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  95 */   public static final boolean IS_OS_SOLARIS = startsWith(OS_NAME, "Solaris");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 102 */   public static final boolean IS_OS_WINDOWS = startsWith(OS_NAME, "Windows");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 109 */   public static final boolean IS_OS_WINDOWS_98 = (startsWith(OS_NAME, "Windows 9") && startsWith(OS_VERSION, "4.1"));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 116 */   public static final boolean IS_OS_WINDOWS_ME = (startsWith(OS_NAME, "Windows") && startsWith(OS_VERSION, "4.9"));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 123 */   public static final boolean IS_OS_WINDOWS_2000 = (startsWith(OS_NAME, "Windows") && startsWith(OS_VERSION, "5.0"));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 130 */   public static final boolean IS_OS_WINDOWS_XP = (startsWith(OS_NAME, "Windows") && startsWith(OS_VERSION, "5.1"));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 140 */   public static final boolean IS_OS_WINDOWS_XP_64_BIT_OR_SERVER_2003 = (startsWith(OS_NAME, "Windows") && startsWith(OS_VERSION, "5.2"));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 147 */   public static final boolean IS_OS_WINDOWS_VISTA = (startsWith(OS_NAME, "Windows") && startsWith(OS_VERSION, "6.0"));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 154 */   public static final boolean IS_OS_WINDOWS_7 = (startsWith(OS_NAME, "Windows") && startsWith(OS_VERSION, "6.1"));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 161 */   public static final boolean IS_OS_WINDOWS_8 = (startsWith(OS_NAME, "Windows") && startsWith(OS_VERSION, "6.2"));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 176 */   public static final boolean IS_OS_WINDOWS_6_OR_LATER = (startsWith(OS_NAME, "Windows") && startsWith(OS_VERSION, "6."));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 185 */   public static final boolean IS_JAVA_6 = startsWith(JAVA_VERSION, "1.6");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 192 */   public static final boolean IS_JAVA_7 = startsWith(JAVA_VERSION, "1.7");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 199 */   public static final boolean IS_JAVA_7_OR_LATER = !IS_JAVA_6;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 208 */   public static final boolean IS_JAVA_8 = startsWith(JAVA_VERSION, "1.8");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 218 */   public static final boolean IS_JAVA_8_OR_LATER = (!IS_JAVA_6 && !IS_JAVA_7);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 229 */   public static final boolean HAS_MODERN_RASTERIZER = hasModernRasterizer();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 237 */   public static final boolean IS_LAF_WINDOWS_XP_ENABLED = isWindowsXPLafEnabled();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 246 */   public static final boolean IS_LOW_RESOLUTION = isLowResolution();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String AWT_UTILITIES_CLASS_NAME = "com.sun.awt.AWTUtilities";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isLafAqua() {
/* 259 */     return UIManager.getLookAndFeel().getID().equals("Aqua");
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
/*     */ 
/*     */   
/*     */   protected static String getSystemProperty(String key) {
/*     */     try {
/* 286 */       return System.getProperty(key);
/* 287 */     } catch (SecurityException e) {
/* 288 */       Logger.getLogger(SystemUtils.class.getName()).warning("Can't access the System property " + key + ".");
/*     */       
/* 290 */       return "";
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected static boolean startsWith(String str, String prefix) {
/* 296 */     return (str != null && str.startsWith(prefix));
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
/*     */   private static boolean hasModernRasterizer() {
/*     */     try {
/* 311 */       Class.forName("com.sun.awt.AWTUtilities");
/* 312 */       return true;
/* 313 */     } catch (ClassNotFoundException e) {
/* 314 */       return false;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isWindowsXPLafEnabled() {
/* 335 */     return (IS_OS_WINDOWS && Boolean.TRUE.equals(Toolkit.getDefaultToolkit().getDesktopProperty("win.xpstyle.themeActive")) && getSystemProperty("swing.noxp") == null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isLowResolution() {
/*     */     try {
/* 344 */       return (Toolkit.getDefaultToolkit().getScreenResolution() < 120);
/* 345 */     } catch (HeadlessException e) {
/* 346 */       return true;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\common\base\SystemUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */