/*     */ package org.fife.ui.autocomplete;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.GraphicsDevice;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.Rectangle;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URI;
/*     */ import java.security.AccessControlException;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.swing.JLabel;
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
/*     */ public final class Util
/*     */ {
/*     */   public static final String PROPERTY_DONT_USE_SUBSTANCE_RENDERERS = "org.fife.ui.autocomplete.DontUseSubstanceRenderers";
/*     */   public static final String PROPERTY_ALLOW_DECORATED_AUTOCOMPLETE_WINDOWS = "org.fife.ui.autocomplete.allowDecoratedAutoCompleteWindows";
/*  62 */   public static final Color LIGHT_HYPERLINK_FG = new Color(14221311);
/*     */   
/*  64 */   private static final Pattern TAG_PATTERN = Pattern.compile("<[^>]*>");
/*     */   
/*     */   private static final boolean USE_SUBSTANCE_RENDERERS;
/*     */   private static boolean desktopCreationAttempted;
/*     */   private static Object desktop;
/*  69 */   private static final Object LOCK_DESKTOP_CREATION = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean browse(URI uri) {
/*  85 */     boolean success = false;
/*     */     
/*  87 */     if (uri != null) {
/*  88 */       Object desktop = getDesktop();
/*  89 */       if (desktop != null) {
/*     */         try {
/*  91 */           Method m = desktop.getClass().getDeclaredMethod("browse", new Class[] { URI.class });
/*     */           
/*  93 */           m.invoke(desktop, new Object[] { uri });
/*  94 */           success = true;
/*  95 */         } catch (RuntimeException re) {
/*  96 */           throw re;
/*  97 */         } catch (Exception e) {}
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 103 */     return success;
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
/*     */   private static Object getDesktop() {
/* 117 */     synchronized (LOCK_DESKTOP_CREATION) {
/*     */       
/* 119 */       if (!desktopCreationAttempted) {
/*     */         
/* 121 */         desktopCreationAttempted = true;
/*     */         
/*     */         try {
/* 124 */           Class<?> desktopClazz = Class.forName("java.awt.Desktop");
/*     */           
/* 126 */           Method m = desktopClazz.getDeclaredMethod("isDesktopSupported", new Class[0]);
/*     */           
/* 128 */           boolean supported = ((Boolean)m.invoke(null, new Object[0])).booleanValue();
/* 129 */           if (supported) {
/* 130 */             m = desktopClazz.getDeclaredMethod("getDesktop", new Class[0]);
/* 131 */             desktop = m.invoke(null, new Object[0]);
/*     */           }
/*     */         
/* 134 */         } catch (RuntimeException re) {
/* 135 */           throw re;
/* 136 */         } catch (Exception e) {}
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 144 */     return desktop;
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
/*     */   public static String getHexString(Color c) {
/* 158 */     if (c == null) {
/* 159 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 165 */     StringBuilder sb = new StringBuilder("#");
/* 166 */     int r = c.getRed();
/* 167 */     if (r < 16) {
/* 168 */       sb.append('0');
/*     */     }
/* 170 */     sb.append(Integer.toHexString(r));
/* 171 */     int g = c.getGreen();
/* 172 */     if (g < 16) {
/* 173 */       sb.append('0');
/*     */     }
/* 175 */     sb.append(Integer.toHexString(g));
/* 176 */     int b = c.getBlue();
/* 177 */     if (b < 16) {
/* 178 */       sb.append('0');
/*     */     }
/* 180 */     sb.append(Integer.toHexString(b));
/*     */     
/* 182 */     return sb.toString();
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
/*     */   static Color getHyperlinkForeground() {
/* 200 */     Color fg = UIManager.getColor("Label.foreground");
/* 201 */     if (fg == null) {
/* 202 */       fg = (new JLabel()).getForeground();
/*     */     }
/*     */     
/* 205 */     return isLightForeground(fg) ? LIGHT_HYPERLINK_FG : Color.blue;
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
/*     */   public static Rectangle getScreenBoundsForPoint(int x, int y) {
/* 221 */     GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
/* 222 */     GraphicsDevice[] devices = env.getScreenDevices();
/* 223 */     for (GraphicsDevice device : devices) {
/* 224 */       GraphicsConfiguration config = device.getDefaultConfiguration();
/* 225 */       Rectangle gcBounds = config.getBounds();
/* 226 */       if (gcBounds.contains(x, y)) {
/* 227 */         return gcBounds;
/*     */       }
/*     */     } 
/*     */     
/* 231 */     return env.getMaximumWindowBounds();
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
/*     */   public static boolean getShouldAllowDecoratingMainAutoCompleteWindows() {
/*     */     try {
/* 246 */       return Boolean.getBoolean("org.fife.ui.autocomplete.allowDecoratedAutoCompleteWindows");
/*     */     }
/* 248 */     catch (AccessControlException ace) {
/* 249 */       return false;
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
/*     */   public static boolean getUseSubstanceRenderers() {
/* 263 */     return USE_SUBSTANCE_RENDERERS;
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
/*     */   public static boolean isLightForeground(Color fg) {
/* 276 */     return (fg.getRed() > 160 && fg.getGreen() > 160 && fg.getBlue() > 160);
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
/*     */   public static boolean startsWithIgnoreCase(String str, String prefix) {
/* 289 */     int prefixLength = prefix.length();
/* 290 */     if (str.length() >= prefixLength) {
/* 291 */       return str.regionMatches(true, 0, prefix, 0, prefixLength);
/*     */     }
/* 293 */     return false;
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
/*     */   public static String stripHtml(String text) {
/* 305 */     if (text == null || !text.startsWith("<html>")) {
/* 306 */       return text;
/*     */     }
/*     */     
/* 309 */     return TAG_PATTERN.matcher(text).replaceAll("");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*     */     try {
/* 317 */       bool = !Boolean.getBoolean("org.fife.ui.autocomplete.DontUseSubstanceRenderers") ? true : false;
/* 318 */     } catch (AccessControlException ace) {
/* 319 */       bool = true;
/*     */     } 
/* 321 */     USE_SUBSTANCE_RENDERERS = bool;
/*     */   }
/*     */   
/*     */   static {
/*     */     boolean bool;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\autocomplete\Util.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */