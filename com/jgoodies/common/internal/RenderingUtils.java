/*     */ package com.jgoodies.common.internal;
/*     */ 
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.GraphicsDevice;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.Toolkit;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.plaf.basic.BasicGraphicsUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class RenderingUtils
/*     */ {
/*     */   private static final String PROP_DESKTOPHINTS = "awt.font.desktophints";
/*     */   private static final String SWING_UTILITIES2_NAME = "sun.swing.SwingUtilities2";
/*  70 */   private static Method drawStringMethod = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   private static Method drawStringUnderlineCharAtMethod = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   private static Method getFontMetricsMethod = null;
/*     */   
/*     */   static {
/*  84 */     drawStringMethod = getMethodDrawString();
/*  85 */     drawStringUnderlineCharAtMethod = getMethodDrawStringUnderlineCharAt();
/*  86 */     getFontMetricsMethod = getMethodGetFontMetrics();
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
/*     */   public static void drawString(JComponent c, Graphics g, String text, int x, int y) {
/* 105 */     if (drawStringMethod != null) {
/*     */       try {
/* 107 */         drawStringMethod.invoke(null, new Object[] { c, g, text, Integer.valueOf(x), Integer.valueOf(y) });
/*     */         
/*     */         return;
/* 110 */       } catch (IllegalArgumentException e) {
/*     */       
/* 112 */       } catch (IllegalAccessException e) {
/*     */       
/* 114 */       } catch (InvocationTargetException e) {}
/*     */     }
/*     */ 
/*     */     
/* 118 */     Graphics2D g2 = (Graphics2D)g;
/* 119 */     Map<?, ?> oldRenderingHints = installDesktopHints(g2);
/* 120 */     BasicGraphicsUtils.drawStringUnderlineCharAt(g, text, -1, x, y);
/* 121 */     if (oldRenderingHints != null) {
/* 122 */       g2.addRenderingHints(oldRenderingHints);
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
/*     */   public static void drawStringUnderlineCharAt(JComponent c, Graphics g, String text, int underlinedIndex, int x, int y) {
/* 140 */     if (drawStringUnderlineCharAtMethod != null) {
/*     */       try {
/* 142 */         drawStringUnderlineCharAtMethod.invoke(null, new Object[] { c, g, text, new Integer(underlinedIndex), new Integer(x), new Integer(y) });
/*     */ 
/*     */         
/*     */         return;
/* 146 */       } catch (IllegalArgumentException e) {
/*     */       
/* 148 */       } catch (IllegalAccessException e) {
/*     */       
/* 150 */       } catch (InvocationTargetException e) {}
/*     */     }
/*     */ 
/*     */     
/* 154 */     Graphics2D g2 = (Graphics2D)g;
/* 155 */     Map<?, ?> oldRenderingHints = installDesktopHints(g2);
/* 156 */     BasicGraphicsUtils.drawStringUnderlineCharAt(g, text, underlinedIndex, x, y);
/* 157 */     if (oldRenderingHints != null) {
/* 158 */       g2.addRenderingHints(oldRenderingHints);
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
/*     */ 
/*     */   
/*     */   public static FontMetrics getFontMetrics(JComponent c, Graphics g) {
/* 181 */     if (getFontMetricsMethod != null) {
/*     */       try {
/* 183 */         return (FontMetrics)getFontMetricsMethod.invoke(null, new Object[] { c, g });
/* 184 */       } catch (IllegalArgumentException e) {
/*     */       
/* 186 */       } catch (IllegalAccessException e) {
/*     */       
/* 188 */       } catch (InvocationTargetException e) {}
/*     */     }
/*     */ 
/*     */     
/* 192 */     return c.getFontMetrics(g.getFont());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Method getMethodDrawString() {
/*     */     try {
/* 200 */       Class<?> clazz = Class.forName("sun.swing.SwingUtilities2");
/* 201 */       return clazz.getMethod("drawString", new Class[] { JComponent.class, Graphics.class, String.class, int.class, int.class });
/*     */ 
/*     */     
/*     */     }
/* 205 */     catch (ClassNotFoundException e) {
/*     */     
/* 207 */     } catch (SecurityException e) {
/*     */     
/* 209 */     } catch (NoSuchMethodException e) {}
/*     */ 
/*     */     
/* 212 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private static Method getMethodDrawStringUnderlineCharAt() {
/*     */     try {
/* 218 */       Class<?> clazz = Class.forName("sun.swing.SwingUtilities2");
/* 219 */       return clazz.getMethod("drawStringUnderlineCharAt", new Class[] { JComponent.class, Graphics.class, String.class, int.class, int.class, int.class });
/*     */ 
/*     */     
/*     */     }
/* 223 */     catch (ClassNotFoundException e) {
/*     */     
/* 225 */     } catch (SecurityException e) {
/*     */     
/* 227 */     } catch (NoSuchMethodException e) {}
/*     */ 
/*     */     
/* 230 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private static Method getMethodGetFontMetrics() {
/*     */     try {
/* 236 */       Class<?> clazz = Class.forName("sun.swing.SwingUtilities2");
/* 237 */       return clazz.getMethod("getFontMetrics", new Class[] { JComponent.class, Graphics.class });
/*     */ 
/*     */     
/*     */     }
/* 241 */     catch (ClassNotFoundException e) {
/*     */     
/* 243 */     } catch (SecurityException e) {
/*     */     
/* 245 */     } catch (NoSuchMethodException e) {}
/*     */ 
/*     */     
/* 248 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private static Map installDesktopHints(Graphics2D g2) {
/* 253 */     Map<Object, Object> oldRenderingHints = null;
/* 254 */     Map<?, ?> desktopHints = desktopHints(g2);
/* 255 */     if (desktopHints != null && !desktopHints.isEmpty()) {
/* 256 */       oldRenderingHints = new HashMap<Object, Object>(desktopHints.size());
/*     */       
/* 258 */       for (Iterator<RenderingHints.Key> i = desktopHints.keySet().iterator(); i.hasNext(); ) {
/* 259 */         RenderingHints.Key key = i.next();
/* 260 */         oldRenderingHints.put(key, g2.getRenderingHint(key));
/*     */       } 
/* 262 */       g2.addRenderingHints(desktopHints);
/*     */     } 
/* 264 */     return oldRenderingHints;
/*     */   }
/*     */ 
/*     */   
/*     */   private static Map desktopHints(Graphics2D g2) {
/* 269 */     if (isPrinting(g2)) {
/* 270 */       return null;
/*     */     }
/* 272 */     Toolkit toolkit = Toolkit.getDefaultToolkit();
/* 273 */     GraphicsDevice device = g2.getDeviceConfiguration().getDevice();
/* 274 */     Map desktopHints = (Map)toolkit.getDesktopProperty("awt.font.desktophints." + device.getIDstring());
/*     */     
/* 276 */     if (desktopHints == null) {
/* 277 */       desktopHints = (Map)toolkit.getDesktopProperty("awt.font.desktophints");
/*     */     }
/*     */     
/* 280 */     if (desktopHints != null) {
/* 281 */       Object aaHint = desktopHints.get(RenderingHints.KEY_TEXT_ANTIALIASING);
/* 282 */       if (aaHint == RenderingHints.VALUE_TEXT_ANTIALIAS_OFF || aaHint == RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT)
/*     */       {
/* 284 */         desktopHints = null;
/*     */       }
/*     */     } 
/* 287 */     return desktopHints;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isPrinting(Graphics g) {
/* 292 */     return (g instanceof java.awt.PrintGraphics || g instanceof java.awt.print.PrinterGraphics);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\common\internal\RenderingUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */