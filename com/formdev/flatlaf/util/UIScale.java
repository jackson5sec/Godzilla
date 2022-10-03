/*     */ package com.formdev.flatlaf.util;
/*     */ 
/*     */ import com.formdev.flatlaf.FlatSystemProperties;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Toolkit;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.lang.reflect.Method;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.DimensionUIResource;
/*     */ import javax.swing.plaf.FontUIResource;
/*     */ import javax.swing.plaf.InsetsUIResource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UIScale
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*     */   private static PropertyChangeSupport changeSupport;
/*     */   private static Boolean jreHiDPI;
/*     */   
/*     */   public static void addPropertyChangeListener(PropertyChangeListener listener) {
/*  78 */     if (changeSupport == null)
/*  79 */       changeSupport = new PropertyChangeSupport(UIScale.class); 
/*  80 */     changeSupport.addPropertyChangeListener(listener);
/*     */   }
/*     */   
/*     */   public static void removePropertyChangeListener(PropertyChangeListener listener) {
/*  84 */     if (changeSupport == null)
/*     */       return; 
/*  86 */     changeSupport.removePropertyChangeListener(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isSystemScalingEnabled() {
/*  97 */     if (jreHiDPI != null) {
/*  98 */       return jreHiDPI.booleanValue();
/*     */     }
/* 100 */     jreHiDPI = Boolean.valueOf(false);
/*     */     
/* 102 */     if (SystemInfo.isJava_9_orLater) {
/*     */       
/* 104 */       jreHiDPI = Boolean.valueOf(true);
/* 105 */     } else if (SystemInfo.isJetBrainsJVM) {
/*     */ 
/*     */       
/*     */       try {
/* 109 */         GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
/* 110 */         Class<?> sunGeClass = Class.forName("sun.java2d.SunGraphicsEnvironment");
/* 111 */         if (sunGeClass.isInstance(ge)) {
/* 112 */           Method m = sunGeClass.getDeclaredMethod("isUIScaleOn", new Class[0]);
/* 113 */           jreHiDPI = (Boolean)m.invoke(ge, new Object[0]);
/*     */         } 
/* 115 */       } catch (Throwable throwable) {}
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 120 */     return jreHiDPI.booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double getSystemScaleFactor(Graphics2D g) {
/* 127 */     return isSystemScalingEnabled() ? getSystemScaleFactor(g.getDeviceConfiguration()) : 1.0D;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double getSystemScaleFactor(GraphicsConfiguration gc) {
/* 134 */     return (isSystemScalingEnabled() && gc != null) ? gc.getDefaultTransform().getScaleX() : 1.0D;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 139 */   private static float scaleFactor = 1.0F;
/*     */   private static boolean initialized;
/*     */   
/*     */   private static void initialize() {
/* 143 */     if (initialized)
/*     */       return; 
/* 145 */     initialized = true;
/*     */     
/* 147 */     if (!isUserScalingEnabled()) {
/*     */       return;
/*     */     }
/*     */     
/* 151 */     PropertyChangeListener listener = new PropertyChangeListener()
/*     */       {
/*     */         public void propertyChange(PropertyChangeEvent e) {
/* 154 */           switch (e.getPropertyName()) {
/*     */             
/*     */             case "lookAndFeel":
/* 157 */               if (e.getNewValue() instanceof javax.swing.LookAndFeel)
/* 158 */                 UIManager.getLookAndFeelDefaults().addPropertyChangeListener(this); 
/* 159 */               UIScale.updateScaleFactor();
/*     */               break;
/*     */             
/*     */             case "defaultFont":
/*     */             case "Label.font":
/* 164 */               UIScale.updateScaleFactor();
/*     */               break;
/*     */           } 
/*     */         }
/*     */       };
/* 169 */     UIManager.addPropertyChangeListener(listener);
/* 170 */     UIManager.getDefaults().addPropertyChangeListener(listener);
/* 171 */     UIManager.getLookAndFeelDefaults().addPropertyChangeListener(listener);
/*     */     
/* 173 */     updateScaleFactor();
/*     */   }
/*     */   private static void updateScaleFactor() {
/*     */     float newScaleFactor;
/* 177 */     if (!isUserScalingEnabled()) {
/*     */       return;
/*     */     }
/*     */     
/* 181 */     float customScaleFactor = getCustomScaleFactor();
/* 182 */     if (customScaleFactor > 0.0F) {
/* 183 */       setUserScaleFactor(customScaleFactor);
/*     */ 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */     
/* 191 */     Font font = UIManager.getFont("defaultFont");
/* 192 */     if (font == null) {
/* 193 */       font = UIManager.getFont("Label.font");
/*     */     }
/*     */     
/* 196 */     if (SystemInfo.isWindows) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 206 */       if (font instanceof javax.swing.plaf.UIResource) {
/* 207 */         if (isSystemScalingEnabled())
/*     */         {
/*     */ 
/*     */           
/* 211 */           newScaleFactor = 1.0F;
/*     */ 
/*     */         
/*     */         }
/*     */         else
/*     */         {
/*     */ 
/*     */           
/* 219 */           Font winFont = (Font)Toolkit.getDefaultToolkit().getDesktopProperty("win.defaultGUI.font");
/* 220 */           newScaleFactor = computeScaleFactor((winFont != null) ? winFont : font);
/*     */         
/*     */         }
/*     */ 
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 228 */         newScaleFactor = computeScaleFactor(font);
/*     */       } 
/*     */     } else {
/* 231 */       newScaleFactor = computeScaleFactor(font);
/*     */     } 
/* 233 */     setUserScaleFactor(newScaleFactor);
/*     */   }
/*     */ 
/*     */   
/*     */   private static float computeScaleFactor(Font font) {
/* 238 */     float fontSizeDivider = 12.0F;
/*     */     
/* 240 */     if (SystemInfo.isWindows) {
/*     */ 
/*     */ 
/*     */       
/* 244 */       if ("Tahoma".equals(font.getFamily()))
/* 245 */         fontSizeDivider = 11.0F; 
/* 246 */     } else if (SystemInfo.isMacOS) {
/*     */       
/* 248 */       fontSizeDivider = 13.0F;
/* 249 */     } else if (SystemInfo.isLinux) {
/*     */       
/* 251 */       fontSizeDivider = SystemInfo.isKDE ? 13.0F : 15.0F;
/*     */     } 
/*     */     
/* 254 */     return font.getSize() / fontSizeDivider;
/*     */   }
/*     */   
/*     */   private static boolean isUserScalingEnabled() {
/* 258 */     return FlatSystemProperties.getBoolean("flatlaf.uiScale.enabled", true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static FontUIResource applyCustomScaleFactor(FontUIResource font) {
/* 266 */     if (!isUserScalingEnabled()) {
/* 267 */       return font;
/*     */     }
/* 269 */     float scaleFactor = getCustomScaleFactor();
/* 270 */     if (scaleFactor <= 0.0F) {
/* 271 */       return font;
/*     */     }
/* 273 */     float fontScaleFactor = computeScaleFactor(font);
/* 274 */     if (scaleFactor == fontScaleFactor) {
/* 275 */       return font;
/*     */     }
/* 277 */     int newFontSize = Math.round(font.getSize() / fontScaleFactor * scaleFactor);
/* 278 */     return new FontUIResource(font.deriveFont(newFontSize));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static float getCustomScaleFactor() {
/* 285 */     return parseScaleFactor(System.getProperty("flatlaf.uiScale"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static float parseScaleFactor(String s) {
/* 292 */     if (s == null) {
/* 293 */       return -1.0F;
/*     */     }
/* 295 */     float units = 1.0F;
/* 296 */     if (s.endsWith("x")) {
/* 297 */       s = s.substring(0, s.length() - 1);
/* 298 */     } else if (s.endsWith("dpi")) {
/* 299 */       units = 96.0F;
/* 300 */       s = s.substring(0, s.length() - 3);
/* 301 */     } else if (s.endsWith("%")) {
/* 302 */       units = 100.0F;
/* 303 */       s = s.substring(0, s.length() - 1);
/*     */     } 
/*     */     
/*     */     try {
/* 307 */       float scale = Float.parseFloat(s);
/* 308 */       return (scale > 0.0F) ? (scale / units) : -1.0F;
/* 309 */     } catch (NumberFormatException ex) {
/* 310 */       return -1.0F;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float getUserScaleFactor() {
/* 318 */     initialize();
/* 319 */     return scaleFactor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void setUserScaleFactor(float scaleFactor) {
/* 326 */     if (scaleFactor <= 1.0F) {
/* 327 */       scaleFactor = 1.0F;
/*     */     } else {
/* 329 */       scaleFactor = Math.round(scaleFactor * 4.0F) / 4.0F;
/*     */     } 
/* 331 */     float oldScaleFactor = UIScale.scaleFactor;
/* 332 */     UIScale.scaleFactor = scaleFactor;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 337 */     if (changeSupport != null) {
/* 338 */       changeSupport.firePropertyChange("userScaleFactor", Float.valueOf(oldScaleFactor), Float.valueOf(scaleFactor));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static float scale(float value) {
/* 345 */     initialize();
/* 346 */     return (scaleFactor == 1.0F) ? value : (value * scaleFactor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int scale(int value) {
/* 353 */     initialize();
/* 354 */     return (scaleFactor == 1.0F) ? value : Math.round(value * scaleFactor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int scale2(int value) {
/* 363 */     initialize();
/* 364 */     return (scaleFactor == 1.0F) ? value : (int)(value * scaleFactor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float unscale(float value) {
/* 371 */     initialize();
/* 372 */     return (scaleFactor == 1.0F) ? value : (value / scaleFactor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int unscale(int value) {
/* 379 */     initialize();
/* 380 */     return (scaleFactor == 1.0F) ? value : Math.round(value / scaleFactor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void scaleGraphics(Graphics2D g) {
/* 388 */     initialize();
/* 389 */     if (scaleFactor != 1.0F) {
/* 390 */       g.scale(scaleFactor, scaleFactor);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Dimension scale(Dimension dimension) {
/* 401 */     initialize();
/* 402 */     return (dimension == null || scaleFactor == 1.0F) ? dimension : ((dimension instanceof javax.swing.plaf.UIResource) ? new DimensionUIResource(
/*     */ 
/*     */         
/* 405 */         scale(dimension.width), scale(dimension.height)) : new Dimension(
/* 406 */         scale(dimension.width), scale(dimension.height)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Insets scale(Insets insets) {
/* 417 */     initialize();
/* 418 */     return (insets == null || scaleFactor == 1.0F) ? insets : ((insets instanceof javax.swing.plaf.UIResource) ? new InsetsUIResource(
/*     */ 
/*     */         
/* 421 */         scale(insets.top), scale(insets.left), scale(insets.bottom), scale(insets.right)) : new Insets(
/* 422 */         scale(insets.top), scale(insets.left), scale(insets.bottom), scale(insets.right)));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\util\UIScale.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */