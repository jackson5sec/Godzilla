/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import com.formdev.flatlaf.FlatLaf;
/*     */ import com.formdev.flatlaf.FlatSystemProperties;
/*     */ import com.formdev.flatlaf.util.HiDPIUtils;
/*     */ import com.formdev.flatlaf.util.SystemInfo;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.HierarchyEvent;
/*     */ import java.awt.event.HierarchyListener;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JRootPane;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.BorderUIResource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JBRCustomDecorations
/*     */ {
/*     */   private static boolean initialized;
/*     */   private static Method Window_hasCustomDecoration;
/*     */   private static Method Window_setHasCustomDecoration;
/*     */   private static Method WWindowPeer_setCustomDecorationHitTestSpots;
/*     */   private static Method WWindowPeer_setCustomDecorationTitleBarHeight;
/*     */   private static Method AWTAccessor_getComponentAccessor;
/*     */   private static Method AWTAccessor_ComponentAccessor_getPeer;
/*     */   
/*     */   public static boolean isSupported() {
/*  67 */     initialize();
/*  68 */     return (Window_setHasCustomDecoration != null);
/*     */   }
/*     */   
/*     */   static void install(final JRootPane rootPane) {
/*  72 */     if (!isSupported()) {
/*     */       return;
/*     */     }
/*     */     
/*  76 */     if (rootPane.getParent() != null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  83 */     HierarchyListener addListener = new HierarchyListener()
/*     */       {
/*     */         public void hierarchyChanged(HierarchyEvent e) {
/*  86 */           if (e.getChanged() != rootPane || (e.getChangeFlags() & 0x1L) == 0L) {
/*     */             return;
/*     */           }
/*  89 */           Container parent = e.getChangedParent();
/*  90 */           if (parent instanceof Window) {
/*  91 */             JBRCustomDecorations.install((Window)parent);
/*     */           }
/*     */ 
/*     */           
/*  95 */           EventQueue.invokeLater(() -> rootPane.removeHierarchyListener(this));
/*     */         }
/*     */       };
/*     */ 
/*     */     
/* 100 */     rootPane.addHierarchyListener(addListener);
/*     */   }
/*     */   
/*     */   static void install(Window window) {
/* 104 */     if (!isSupported()) {
/*     */       return;
/*     */     }
/*     */     
/* 108 */     if (UIManager.getLookAndFeel().getSupportsWindowDecorations()) {
/*     */       return;
/*     */     }
/* 111 */     if (window instanceof JFrame) {
/* 112 */       JFrame frame = (JFrame)window;
/*     */ 
/*     */ 
/*     */       
/* 116 */       if (!JFrame.isDefaultLookAndFeelDecorated() && 
/* 117 */         !FlatSystemProperties.getBoolean("flatlaf.useJetBrainsCustomDecorations", false)) {
/*     */         return;
/*     */       }
/*     */       
/* 121 */       if (frame.isUndecorated()) {
/*     */         return;
/*     */       }
/*     */       
/* 125 */       setHasCustomDecoration(frame);
/*     */ 
/*     */       
/* 128 */       frame.getRootPane().setWindowDecorationStyle(1);
/*     */     }
/* 130 */     else if (window instanceof JDialog) {
/* 131 */       JDialog dialog = (JDialog)window;
/*     */ 
/*     */ 
/*     */       
/* 135 */       if (!JDialog.isDefaultLookAndFeelDecorated() && 
/* 136 */         !FlatSystemProperties.getBoolean("flatlaf.useJetBrainsCustomDecorations", false)) {
/*     */         return;
/*     */       }
/*     */       
/* 140 */       if (dialog.isUndecorated()) {
/*     */         return;
/*     */       }
/*     */       
/* 144 */       setHasCustomDecoration(dialog);
/*     */ 
/*     */       
/* 147 */       dialog.getRootPane().setWindowDecorationStyle(2);
/*     */     } 
/*     */   }
/*     */   
/*     */   static boolean hasCustomDecoration(Window window) {
/* 152 */     if (!isSupported()) {
/* 153 */       return false;
/*     */     }
/*     */     try {
/* 156 */       return ((Boolean)Window_hasCustomDecoration.invoke(window, new Object[0])).booleanValue();
/* 157 */     } catch (Exception ex) {
/* 158 */       Logger.getLogger(FlatLaf.class.getName()).log(Level.SEVERE, (String)null, ex);
/* 159 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   static void setHasCustomDecoration(Window window) {
/* 164 */     if (!isSupported()) {
/*     */       return;
/*     */     }
/*     */     try {
/* 168 */       Window_setHasCustomDecoration.invoke(window, new Object[0]);
/* 169 */     } catch (Exception ex) {
/* 170 */       Logger.getLogger(FlatLaf.class.getName()).log(Level.SEVERE, (String)null, ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   static void setHitTestSpotsAndTitleBarHeight(Window window, List<Rectangle> hitTestSpots, int titleBarHeight) {
/* 175 */     if (!isSupported()) {
/*     */       return;
/*     */     }
/*     */     try {
/* 179 */       Object compAccessor = AWTAccessor_getComponentAccessor.invoke((Object)null, new Object[0]);
/* 180 */       Object peer = AWTAccessor_ComponentAccessor_getPeer.invoke(compAccessor, new Object[] { window });
/* 181 */       WWindowPeer_setCustomDecorationHitTestSpots.invoke(peer, new Object[] { hitTestSpots });
/* 182 */       WWindowPeer_setCustomDecorationTitleBarHeight.invoke(peer, new Object[] { Integer.valueOf(titleBarHeight) });
/* 183 */     } catch (Exception ex) {
/* 184 */       Logger.getLogger(FlatLaf.class.getName()).log(Level.SEVERE, (String)null, ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void initialize() {
/* 189 */     if (initialized)
/*     */       return; 
/* 191 */     initialized = true;
/*     */ 
/*     */     
/* 194 */     if (!SystemInfo.isJetBrainsJVM_11_orLater || !SystemInfo.isWindows_10_orLater) {
/*     */       return;
/*     */     }
/* 197 */     if (!FlatSystemProperties.getBoolean("flatlaf.useJetBrainsCustomDecorations", true)) {
/*     */       return;
/*     */     }
/*     */     try {
/* 201 */       Class<?> awtAcessorClass = Class.forName("sun.awt.AWTAccessor");
/* 202 */       Class<?> compAccessorClass = Class.forName("sun.awt.AWTAccessor$ComponentAccessor");
/* 203 */       AWTAccessor_getComponentAccessor = awtAcessorClass.getDeclaredMethod("getComponentAccessor", new Class[0]);
/* 204 */       AWTAccessor_ComponentAccessor_getPeer = compAccessorClass.getDeclaredMethod("getPeer", new Class[] { Component.class });
/*     */       
/* 206 */       Class<?> peerClass = Class.forName("sun.awt.windows.WWindowPeer");
/* 207 */       WWindowPeer_setCustomDecorationHitTestSpots = peerClass.getDeclaredMethod("setCustomDecorationHitTestSpots", new Class[] { List.class });
/* 208 */       WWindowPeer_setCustomDecorationTitleBarHeight = peerClass.getDeclaredMethod("setCustomDecorationTitleBarHeight", new Class[] { int.class });
/* 209 */       WWindowPeer_setCustomDecorationHitTestSpots.setAccessible(true);
/* 210 */       WWindowPeer_setCustomDecorationTitleBarHeight.setAccessible(true);
/*     */       
/* 212 */       Window_hasCustomDecoration = Window.class.getDeclaredMethod("hasCustomDecoration", new Class[0]);
/* 213 */       Window_setHasCustomDecoration = Window.class.getDeclaredMethod("setHasCustomDecoration", new Class[0]);
/* 214 */       Window_hasCustomDecoration.setAccessible(true);
/* 215 */       Window_setHasCustomDecoration.setAccessible(true);
/* 216 */     } catch (Exception exception) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class JBRWindowTopBorder
/*     */     extends BorderUIResource.EmptyBorderUIResource
/*     */   {
/*     */     private static JBRWindowTopBorder instance;
/*     */ 
/*     */     
/* 228 */     private final Color defaultActiveBorder = new Color(7368816);
/* 229 */     private final Color inactiveLightColor = new Color(11184810);
/*     */     
/*     */     private boolean colorizationAffectsBorders;
/* 232 */     private Color activeColor = this.defaultActiveBorder;
/*     */     
/*     */     static JBRWindowTopBorder getInstance() {
/* 235 */       if (instance == null)
/* 236 */         instance = new JBRWindowTopBorder(); 
/* 237 */       return instance;
/*     */     }
/*     */     
/*     */     private JBRWindowTopBorder() {
/* 241 */       super(1, 0, 0, 0);
/*     */       
/* 243 */       this.colorizationAffectsBorders = calculateAffectsBorders();
/* 244 */       this.activeColor = calculateActiveBorderColor();
/*     */       
/* 246 */       Toolkit toolkit = Toolkit.getDefaultToolkit();
/* 247 */       toolkit.addPropertyChangeListener("win.dwm.colorizationColor.affects.borders", e -> {
/*     */             this.colorizationAffectsBorders = calculateAffectsBorders();
/*     */             
/*     */             this.activeColor = calculateActiveBorderColor();
/*     */           });
/* 252 */       PropertyChangeListener l = e -> this.activeColor = calculateActiveBorderColor();
/*     */ 
/*     */       
/* 255 */       toolkit.addPropertyChangeListener("win.dwm.colorizationColor", l);
/* 256 */       toolkit.addPropertyChangeListener("win.dwm.colorizationColorBalance", l);
/* 257 */       toolkit.addPropertyChangeListener("win.frame.activeBorderColor", l);
/*     */     }
/*     */     
/*     */     private boolean calculateAffectsBorders() {
/* 261 */       Object value = Toolkit.getDefaultToolkit().getDesktopProperty("win.dwm.colorizationColor.affects.borders");
/* 262 */       return (value instanceof Boolean) ? ((Boolean)value).booleanValue() : true;
/*     */     }
/*     */     
/*     */     private Color calculateActiveBorderColor() {
/* 266 */       if (!this.colorizationAffectsBorders) {
/* 267 */         return this.defaultActiveBorder;
/*     */       }
/* 269 */       Toolkit toolkit = Toolkit.getDefaultToolkit();
/* 270 */       Color colorizationColor = (Color)toolkit.getDesktopProperty("win.dwm.colorizationColor");
/* 271 */       if (colorizationColor != null) {
/* 272 */         Object colorizationColorBalanceObj = toolkit.getDesktopProperty("win.dwm.colorizationColorBalance");
/* 273 */         if (colorizationColorBalanceObj instanceof Integer) {
/* 274 */           int colorizationColorBalance = ((Integer)colorizationColorBalanceObj).intValue();
/* 275 */           if (colorizationColorBalance < 0 || colorizationColorBalance > 100) {
/* 276 */             colorizationColorBalance = 100;
/*     */           }
/* 278 */           if (colorizationColorBalance == 0)
/* 279 */             return new Color(14277081); 
/* 280 */           if (colorizationColorBalance == 100) {
/* 281 */             return colorizationColor;
/*     */           }
/* 283 */           float alpha = colorizationColorBalance / 100.0F;
/* 284 */           float remainder = 1.0F - alpha;
/* 285 */           int r = Math.round(colorizationColor.getRed() * alpha + 217.0F * remainder);
/* 286 */           int g = Math.round(colorizationColor.getGreen() * alpha + 217.0F * remainder);
/* 287 */           int b = Math.round(colorizationColor.getBlue() * alpha + 217.0F * remainder);
/*     */ 
/*     */           
/* 290 */           r = Math.min(Math.max(r, 0), 255);
/* 291 */           g = Math.min(Math.max(g, 0), 255);
/* 292 */           b = Math.min(Math.max(b, 0), 255);
/*     */           
/* 294 */           return new Color(r, g, b);
/*     */         } 
/* 296 */         return colorizationColor;
/*     */       } 
/*     */       
/* 299 */       Color activeBorderColor = (Color)toolkit.getDesktopProperty("win.frame.activeBorderColor");
/* 300 */       return (activeBorderColor != null) ? activeBorderColor : UIManager.getColor("MenuBar.borderColor");
/*     */     }
/*     */ 
/*     */     
/*     */     public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
/* 305 */       Window window = SwingUtilities.windowForComponent(c);
/* 306 */       boolean active = (window != null) ? window.isActive() : false;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 311 */       boolean paintTopBorder = (!FlatLaf.isLafDark() || (active && this.colorizationAffectsBorders));
/* 312 */       if (!paintTopBorder) {
/*     */         return;
/*     */       }
/* 315 */       g.setColor(active ? this.activeColor : this.inactiveLightColor);
/* 316 */       HiDPIUtils.paintAtScale1x((Graphics2D)g, x, y, width, height, this::paintImpl);
/*     */     }
/*     */     
/*     */     private void paintImpl(Graphics2D g, int x, int y, int width, int height, double scaleFactor) {
/* 320 */       g.drawRect(x, y, width - 1, 0);
/*     */     }
/*     */     
/*     */     void repaintBorder(Component c) {
/* 324 */       c.repaint(0, 0, c.getWidth(), 1);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\JBRCustomDecorations.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */