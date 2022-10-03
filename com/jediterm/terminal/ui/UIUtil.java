/*    */ package com.jediterm.terminal.ui;
/*    */ 
/*    */ import com.jediterm.terminal.util.Util;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.GraphicsDevice;
/*    */ import java.awt.GraphicsEnvironment;
/*    */ import java.awt.Toolkit;
/*    */ import java.lang.reflect.Field;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UIUtil
/*    */ {
/* 16 */   public static final String OS_NAME = System.getProperty("os.name");
/* 17 */   public static final String OS_VERSION = System.getProperty("os.version").toLowerCase();
/*    */   
/* 19 */   protected static final String _OS_NAME = OS_NAME.toLowerCase();
/* 20 */   public static final boolean isWindows = _OS_NAME.startsWith("windows");
/* 21 */   public static final boolean isOS2 = (_OS_NAME.startsWith("os/2") || _OS_NAME.startsWith("os2"));
/* 22 */   public static final boolean isMac = _OS_NAME.startsWith("mac");
/* 23 */   public static final boolean isLinux = _OS_NAME.startsWith("linux");
/* 24 */   public static final boolean isUnix = (!isWindows && !isOS2);
/* 25 */   private static final boolean IS_ORACLE_JVM = isOracleJvm();
/*    */   
/* 27 */   public static final String JAVA_RUNTIME_VERSION = System.getProperty("java.runtime.version");
/*    */   
/*    */   public static boolean isRetina() {
/* 30 */     if (isJavaVersionAtLeast("1.7.0_40") && IS_ORACLE_JVM) {
/* 31 */       GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
/* 32 */       GraphicsDevice device = env.getDefaultScreenDevice();
/*    */       
/*    */       try {
/* 35 */         Field field = device.getClass().getDeclaredField("scale");
/*    */         
/* 37 */         if (field != null) {
/* 38 */           field.setAccessible(true);
/* 39 */           Object scale = field.get(device);
/*    */           
/* 41 */           if (scale instanceof Integer && ((Integer)scale).intValue() == 2) {
/* 42 */             return true;
/*    */           }
/*    */         }
/*    */       
/* 46 */       } catch (Exception exception) {}
/*    */     } 
/*    */ 
/*    */     
/* 50 */     Float scaleFactor = (Float)Toolkit.getDefaultToolkit().getDesktopProperty("apple.awt.contentScaleFactor");
/*    */     
/* 52 */     if (scaleFactor != null && scaleFactor.intValue() == 2) {
/* 53 */       return true;
/*    */     }
/* 55 */     return false;
/*    */   }
/*    */   
/*    */   private static boolean isOracleJvm() {
/* 59 */     String vendor = getJavaVmVendor();
/* 60 */     return (vendor != null && Util.containsIgnoreCase(vendor, "Oracle"));
/*    */   }
/*    */   
/*    */   public static String getJavaVmVendor() {
/* 64 */     return System.getProperty("java.vm.vendor");
/*    */   }
/*    */   
/*    */   public static boolean isJavaVersionAtLeast(String v) {
/* 68 */     return (Util.compareVersionNumbers(JAVA_RUNTIME_VERSION, v) >= 0);
/*    */   }
/*    */   
/*    */   public static void applyRenderingHints(Graphics g) {
/* 72 */     Graphics2D g2d = (Graphics2D)g;
/* 73 */     Toolkit tk = Toolkit.getDefaultToolkit();
/*    */     
/* 75 */     Map<?, ?> map = (Map)tk.getDesktopProperty("awt.font.desktophints");
/* 76 */     if (map != null)
/* 77 */       g2d.addRenderingHints(map); 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\termina\\ui\UIUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */