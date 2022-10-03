/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import com.formdev.flatlaf.FlatClientProperties;
/*     */ import com.formdev.flatlaf.util.DerivedColor;
/*     */ import com.formdev.flatlaf.util.Graphics2DProxy;
/*     */ import com.formdev.flatlaf.util.HiDPIUtils;
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.GraphicsDevice;
/*     */ import java.awt.Insets;
/*     */ import java.awt.KeyboardFocusManager;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.Shape;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.awt.event.FocusListener;
/*     */ import java.awt.geom.Ellipse2D;
/*     */ import java.awt.geom.Path2D;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.geom.RoundRectangle2D;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.WeakHashMap;
/*     */ import java.util.function.Supplier;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.border.CompoundBorder;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FlatUIUtils
/*     */ {
/*  65 */   public static final boolean MAC_USE_QUARTZ = Boolean.getBoolean("apple.awt.graphics.UseQuartz");
/*     */   
/*  67 */   private static WeakHashMap<LookAndFeel, IdentityHashMap<Object, ComponentUI>> sharedUIinstances = new WeakHashMap<>();
/*     */   
/*     */   public static Rectangle addInsets(Rectangle r, Insets insets) {
/*  70 */     return new Rectangle(r.x - insets.left, r.y - insets.top, r.width + insets.left + insets.right, r.height + insets.top + insets.bottom);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Rectangle subtractInsets(Rectangle r, Insets insets) {
/*  78 */     return new Rectangle(r.x + insets.left, r.y + insets.top, r.width - insets.left - insets.right, r.height - insets.top - insets.bottom);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Dimension addInsets(Dimension dim, Insets insets) {
/*  86 */     return new Dimension(dim.width + insets.left + insets.right, dim.height + insets.top + insets.bottom);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Insets addInsets(Insets insets1, Insets insets2) {
/*  92 */     return new Insets(insets1.top + insets2.top, insets1.left + insets2.left, insets1.bottom + insets2.bottom, insets1.right + insets2.right);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setInsets(Insets dest, Insets src) {
/* 100 */     dest.top = src.top;
/* 101 */     dest.left = src.left;
/* 102 */     dest.bottom = src.bottom;
/* 103 */     dest.right = src.right;
/*     */   }
/*     */   
/*     */   public static Color getUIColor(String key, int defaultColorRGB) {
/* 107 */     Color color = UIManager.getColor(key);
/* 108 */     return (color != null) ? color : new Color(defaultColorRGB);
/*     */   }
/*     */   
/*     */   public static Color getUIColor(String key, Color defaultColor) {
/* 112 */     Color color = UIManager.getColor(key);
/* 113 */     return (color != null) ? color : defaultColor;
/*     */   }
/*     */   
/*     */   public static Color getUIColor(String key, String defaultKey) {
/* 117 */     Color color = UIManager.getColor(key);
/* 118 */     return (color != null) ? color : UIManager.getColor(defaultKey);
/*     */   }
/*     */   
/*     */   public static int getUIInt(String key, int defaultValue) {
/* 122 */     Object value = UIManager.get(key);
/* 123 */     return (value instanceof Integer) ? ((Integer)value).intValue() : defaultValue;
/*     */   }
/*     */   
/*     */   public static float getUIFloat(String key, float defaultValue) {
/* 127 */     Object value = UIManager.get(key);
/* 128 */     return (value instanceof Number) ? ((Number)value).floatValue() : defaultValue;
/*     */   }
/*     */   
/*     */   public static boolean isChevron(String arrowType) {
/* 132 */     return !"triangle".equals(arrowType);
/*     */   }
/*     */   
/*     */   public static Color nonUIResource(Color c) {
/* 136 */     return (c instanceof javax.swing.plaf.UIResource) ? new Color(c.getRGB(), true) : c;
/*     */   }
/*     */   
/*     */   public static Font nonUIResource(Font font) {
/* 140 */     return (font instanceof javax.swing.plaf.UIResource) ? font.deriveFont(font.getStyle()) : font;
/*     */   }
/*     */   
/*     */   public static int minimumWidth(JComponent c, int minimumWidth) {
/* 144 */     return FlatClientProperties.clientPropertyInt(c, "JComponent.minimumWidth", minimumWidth);
/*     */   }
/*     */   
/*     */   public static int minimumHeight(JComponent c, int minimumHeight) {
/* 148 */     return FlatClientProperties.clientPropertyInt(c, "JComponent.minimumHeight", minimumHeight);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isCellEditor(Component c) {
/* 153 */     Component c2 = c;
/* 154 */     for (int i = 0; i <= 2 && c2 != null; i++) {
/* 155 */       Container parent = c2.getParent();
/* 156 */       if (parent instanceof JTable && ((JTable)parent).getEditorComponent() == c2) {
/* 157 */         return true;
/*     */       }
/* 159 */       c2 = parent;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 165 */     String name = c.getName();
/* 166 */     if ("Table.editor".equals(name) || "Tree.cellEditor".equals(name)) {
/* 167 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 171 */     return (c instanceof JComponent && Boolean.TRUE.equals(((JComponent)c).getClientProperty("JComboBox.isTableCellEditor")));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isPermanentFocusOwner(Component c) {
/* 179 */     KeyboardFocusManager keyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
/* 180 */     return (keyboardFocusManager.getPermanentFocusOwner() == c && keyboardFocusManager
/* 181 */       .getActiveWindow() == SwingUtilities.windowForComponent(c));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isFullScreen(Component c) {
/* 188 */     GraphicsConfiguration gc = c.getGraphicsConfiguration();
/* 189 */     GraphicsDevice gd = (gc != null) ? gc.getDevice() : null;
/* 190 */     Window fullScreenWindow = (gd != null) ? gd.getFullScreenWindow() : null;
/* 191 */     return (fullScreenWindow != null && fullScreenWindow == SwingUtilities.windowForComponent(c));
/*     */   }
/*     */   
/*     */   public static Boolean isRoundRect(Component c) {
/* 195 */     return (c instanceof JComponent) ? 
/* 196 */       FlatClientProperties.clientPropertyBooleanStrict((JComponent)c, "JComponent.roundRect", null) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float getBorderFocusWidth(JComponent c) {
/* 205 */     FlatBorder border = getOutsideFlatBorder(c);
/* 206 */     return (border != null) ? 
/* 207 */       UIScale.scale(border.getFocusWidth(c)) : 0.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float getBorderArc(JComponent c) {
/* 215 */     FlatBorder border = getOutsideFlatBorder(c);
/* 216 */     return (border != null) ? 
/* 217 */       UIScale.scale(border.getArc(c)) : 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean hasRoundBorder(JComponent c) {
/* 222 */     return (getBorderArc(c) >= c.getHeight());
/*     */   }
/*     */   
/*     */   public static FlatBorder getOutsideFlatBorder(JComponent c) {
/* 226 */     Border border = c.getBorder();
/*     */     while (true) {
/* 228 */       if (border instanceof FlatBorder)
/* 229 */         return (FlatBorder)border; 
/* 230 */       if (border instanceof CompoundBorder) {
/* 231 */         border = ((CompoundBorder)border).getOutsideBorder(); continue;
/*     */       }  break;
/* 233 */     }  return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object[] setRenderingHints(Graphics g) {
/* 241 */     Graphics2D g2 = (Graphics2D)g;
/*     */ 
/*     */     
/* 244 */     Object[] oldRenderingHints = { g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING), g2.getRenderingHint(RenderingHints.KEY_STROKE_CONTROL) };
/*     */ 
/*     */     
/* 247 */     g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/* 248 */     g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, MAC_USE_QUARTZ ? RenderingHints.VALUE_STROKE_PURE : RenderingHints.VALUE_STROKE_NORMALIZE);
/*     */ 
/*     */     
/* 251 */     return oldRenderingHints;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void resetRenderingHints(Graphics g, Object[] oldRenderingHints) {
/* 258 */     Graphics2D g2 = (Graphics2D)g;
/* 259 */     g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldRenderingHints[0]);
/* 260 */     g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, oldRenderingHints[1]);
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
/*     */   public static void runWithoutRenderingHints(Graphics g, Object[] oldRenderingHints, Runnable runnable) {
/* 277 */     if (oldRenderingHints == null) {
/* 278 */       runnable.run();
/*     */       
/*     */       return;
/*     */     } 
/* 282 */     Graphics2D g2 = (Graphics2D)g;
/*     */ 
/*     */     
/* 285 */     Object[] oldRenderingHints2 = { g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING), g2.getRenderingHint(RenderingHints.KEY_STROKE_CONTROL) };
/*     */ 
/*     */     
/* 288 */     resetRenderingHints(g2, oldRenderingHints);
/* 289 */     runnable.run();
/* 290 */     resetRenderingHints(g2, oldRenderingHints2);
/*     */   }
/*     */   
/*     */   public static Color deriveColor(Color color, Color baseColor) {
/* 294 */     return (color instanceof DerivedColor) ? ((DerivedColor)color)
/* 295 */       .derive(baseColor) : color;
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
/*     */   public static void paintComponentOuterBorder(Graphics2D g, int x, int y, int width, int height, float focusWidth, float lineWidth, float arc) {
/* 312 */     if (focusWidth + lineWidth == 0.0F) {
/*     */       return;
/*     */     }
/* 315 */     double systemScaleFactor = UIScale.getSystemScaleFactor(g);
/* 316 */     if (systemScaleFactor != 1.0D && systemScaleFactor != 2.0D) {
/*     */       
/* 318 */       HiDPIUtils.paintAtScale1x(g, x, y, width, height, (g2d, x2, y2, width2, height2, scaleFactor) -> paintComponentOuterBorderImpl(g2d, x2, y2, width2, height2, (float)(focusWidth * scaleFactor), (float)(lineWidth * scaleFactor), (float)(arc * scaleFactor)));
/*     */ 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */     
/* 326 */     paintComponentOuterBorderImpl(g, x, y, width, height, focusWidth, lineWidth, arc);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void paintComponentOuterBorderImpl(Graphics2D g, int x, int y, int width, int height, float focusWidth, float lineWidth, float arc) {
/* 332 */     float ow = focusWidth + lineWidth;
/* 333 */     float outerArc = arc + focusWidth * 2.0F;
/* 334 */     float innerArc = arc - lineWidth * 2.0F;
/*     */ 
/*     */     
/* 337 */     if (arc > 0.0F && arc < UIScale.scale(10)) {
/* 338 */       outerArc -= UIScale.scale(2.0F);
/*     */     }
/* 340 */     Path2D path = new Path2D.Float(0);
/* 341 */     path.append(createComponentRectangle(x, y, width, height, outerArc), false);
/* 342 */     path.append(createComponentRectangle(x + ow, y + ow, width - ow * 2.0F, height - ow * 2.0F, innerArc), false);
/* 343 */     g.fill(path);
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
/*     */   public static void paintComponentBorder(Graphics2D g, int x, int y, int width, int height, float focusWidth, float lineWidth, float arc) {
/* 360 */     if (lineWidth == 0.0F) {
/*     */       return;
/*     */     }
/* 363 */     double systemScaleFactor = UIScale.getSystemScaleFactor(g);
/* 364 */     if (systemScaleFactor != 1.0D && systemScaleFactor != 2.0D) {
/*     */       
/* 366 */       HiDPIUtils.paintAtScale1x(g, x, y, width, height, (g2d, x2, y2, width2, height2, scaleFactor) -> paintComponentBorderImpl(g2d, x2, y2, width2, height2, (float)(focusWidth * scaleFactor), (float)(lineWidth * scaleFactor), (float)(arc * scaleFactor)));
/*     */ 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */     
/* 374 */     paintComponentBorderImpl(g, x, y, width, height, focusWidth, lineWidth, arc);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void paintComponentBorderImpl(Graphics2D g, int x, int y, int width, int height, float focusWidth, float lineWidth, float arc) {
/* 380 */     float x1 = x + focusWidth;
/* 381 */     float y1 = y + focusWidth;
/* 382 */     float width1 = width - focusWidth * 2.0F;
/* 383 */     float height1 = height - focusWidth * 2.0F;
/* 384 */     float arc2 = arc - lineWidth * 2.0F;
/*     */     
/* 386 */     Shape r1 = createComponentRectangle(x1, y1, width1, height1, arc);
/* 387 */     Shape r2 = createComponentRectangle(x1 + lineWidth, y1 + lineWidth, width1 - lineWidth * 2.0F, height1 - lineWidth * 2.0F, arc2);
/*     */ 
/*     */ 
/*     */     
/* 391 */     Path2D border = new Path2D.Float(0);
/* 392 */     border.append(r1, false);
/* 393 */     border.append(r2, false);
/* 394 */     g.fill(border);
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
/*     */   public static void paintComponentBackground(Graphics2D g, int x, int y, int width, int height, float focusWidth, float arc) {
/* 410 */     double systemScaleFactor = UIScale.getSystemScaleFactor(g);
/* 411 */     if (systemScaleFactor != 1.0D && systemScaleFactor != 2.0D) {
/*     */       
/* 413 */       HiDPIUtils.paintAtScale1x(g, x, y, width, height, (g2d, x2, y2, width2, height2, scaleFactor) -> paintComponentBackgroundImpl(g2d, x2, y2, width2, height2, (float)(focusWidth * scaleFactor), (float)(arc * scaleFactor)));
/*     */ 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */     
/* 421 */     paintComponentBackgroundImpl(g, x, y, width, height, focusWidth, arc);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void paintComponentBackgroundImpl(Graphics2D g, int x, int y, int width, int height, float focusWidth, float arc) {
/* 427 */     g.fill(createComponentRectangle(x + focusWidth, y + focusWidth, width - focusWidth * 2.0F, height - focusWidth * 2.0F, arc));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Shape createComponentRectangle(float x, float y, float w, float h, float arc) {
/* 437 */     if (arc <= 0.0F) {
/* 438 */       return new Rectangle2D.Float(x, y, w, h);
/*     */     }
/* 440 */     arc = Math.min(arc, Math.min(w, h));
/* 441 */     return new RoundRectangle2D.Float(x, y, w, h, arc, arc);
/*     */   }
/*     */   
/*     */   static void paintFilledRectangle(Graphics g, Color color, float x, float y, float w, float h) {
/* 445 */     Graphics2D g2 = (Graphics2D)g.create();
/*     */     try {
/* 447 */       setRenderingHints(g2);
/* 448 */       g2.setColor(color);
/* 449 */       g2.fill(new Rectangle2D.Float(x, y, w, h));
/*     */     } finally {
/* 451 */       g2.dispose();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void paintGrip(Graphics g, int x, int y, int width, int height, boolean horizontal, int dotCount, int dotSize, int gap, boolean centerPrecise) {
/*     */     float gx, gy;
/* 458 */     dotSize = UIScale.scale(dotSize);
/* 459 */     gap = UIScale.scale(gap);
/* 460 */     int gripSize = dotSize * dotCount + gap * (dotCount - 1);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 465 */     if (horizontal) {
/* 466 */       gx = (x + Math.round((width - gripSize) / 2.0F));
/* 467 */       gy = y + (height - dotSize) / 2.0F;
/*     */       
/* 469 */       if (!centerPrecise) {
/* 470 */         gy = Math.round(gy);
/*     */       }
/*     */     } else {
/* 473 */       gx = x + (width - dotSize) / 2.0F;
/* 474 */       gy = (y + Math.round((height - gripSize) / 2.0F));
/*     */       
/* 476 */       if (!centerPrecise) {
/* 477 */         gx = Math.round(gx);
/*     */       }
/*     */     } 
/*     */     
/* 481 */     for (int i = 0; i < dotCount; i++) {
/* 482 */       ((Graphics2D)g).fill(new Ellipse2D.Float(gx, gy, dotSize, dotSize));
/* 483 */       if (horizontal) {
/* 484 */         gx += (dotSize + gap);
/*     */       } else {
/* 486 */         gy += (dotSize + gap);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void paintParentBackground(Graphics g, JComponent c) {
/* 495 */     Container parent = findOpaqueParent(c);
/* 496 */     if (parent != null) {
/* 497 */       g.setColor(parent.getBackground());
/* 498 */       g.fillRect(0, 0, c.getWidth(), c.getHeight());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Color getParentBackground(JComponent c) {
/* 506 */     Container parent = findOpaqueParent(c);
/* 507 */     return (parent != null) ? parent
/* 508 */       .getBackground() : 
/* 509 */       UIManager.getColor("Panel.background");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Container findOpaqueParent(Container c) {
/* 516 */     while ((c = c.getParent()) != null) {
/* 517 */       if (c.isOpaque())
/* 518 */         return c; 
/*     */     } 
/* 520 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Path2D createRectangle(float x, float y, float width, float height, float lineWidth) {
/* 527 */     Path2D path = new Path2D.Float(0);
/* 528 */     path.append(new Rectangle2D.Float(x, y, width, height), false);
/* 529 */     path.append(new Rectangle2D.Float(x + lineWidth, y + lineWidth, width - lineWidth * 2.0F, height - lineWidth * 2.0F), false);
/*     */     
/* 531 */     return path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Path2D createRoundRectangle(float x, float y, float width, float height, float lineWidth, float arcTopLeft, float arcTopRight, float arcBottomLeft, float arcBottomRight) {
/* 540 */     Path2D path = new Path2D.Float(0);
/* 541 */     path.append(createRoundRectanglePath(x, y, width, height, arcTopLeft, arcTopRight, arcBottomLeft, arcBottomRight), false);
/* 542 */     path.append(createRoundRectanglePath(x + lineWidth, y + lineWidth, width - lineWidth * 2.0F, height - lineWidth * 2.0F, arcTopLeft - lineWidth, arcTopRight - lineWidth, arcBottomLeft - lineWidth, arcBottomRight - lineWidth), false);
/*     */     
/* 544 */     return path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Shape createRoundRectanglePath(float x, float y, float width, float height, float arcTopLeft, float arcTopRight, float arcBottomLeft, float arcBottomRight) {
/* 553 */     if (arcTopLeft <= 0.0F && arcTopRight <= 0.0F && arcBottomLeft <= 0.0F && arcBottomRight <= 0.0F) {
/* 554 */       return new Rectangle2D.Float(x, y, width, height);
/*     */     }
/*     */     
/* 557 */     float maxArc = Math.min(width, height) / 2.0F;
/* 558 */     arcTopLeft = (arcTopLeft > 0.0F) ? Math.min(arcTopLeft, maxArc) : 0.0F;
/* 559 */     arcTopRight = (arcTopRight > 0.0F) ? Math.min(arcTopRight, maxArc) : 0.0F;
/* 560 */     arcBottomLeft = (arcBottomLeft > 0.0F) ? Math.min(arcBottomLeft, maxArc) : 0.0F;
/* 561 */     arcBottomRight = (arcBottomRight > 0.0F) ? Math.min(arcBottomRight, maxArc) : 0.0F;
/*     */     
/* 563 */     float x2 = x + width;
/* 564 */     float y2 = y + height;
/*     */ 
/*     */     
/* 567 */     double c = 0.5522847498307933D;
/* 568 */     double ci = 1.0D - c;
/* 569 */     double ciTopLeft = arcTopLeft * ci;
/* 570 */     double ciTopRight = arcTopRight * ci;
/* 571 */     double ciBottomLeft = arcBottomLeft * ci;
/* 572 */     double ciBottomRight = arcBottomRight * ci;
/*     */     
/* 574 */     Path2D rect = new Path2D.Float();
/* 575 */     rect.moveTo((x2 - arcTopRight), y);
/* 576 */     rect.curveTo(x2 - ciTopRight, y, x2, y + ciTopRight, x2, (y + arcTopRight));
/*     */ 
/*     */     
/* 579 */     rect.lineTo(x2, (y2 - arcBottomRight));
/* 580 */     rect.curveTo(x2, y2 - ciBottomRight, x2 - ciBottomRight, y2, (x2 - arcBottomRight), y2);
/*     */ 
/*     */     
/* 583 */     rect.lineTo((x + arcBottomLeft), y2);
/* 584 */     rect.curveTo(x + ciBottomLeft, y2, x, y2 - ciBottomLeft, x, (y2 - arcBottomLeft));
/*     */ 
/*     */     
/* 587 */     rect.lineTo(x, (y + arcTopLeft));
/* 588 */     rect.curveTo(x, y + ciTopLeft, x + ciTopLeft, y, (x + arcTopLeft), y);
/*     */ 
/*     */     
/* 591 */     rect.closePath();
/*     */     
/* 593 */     return rect;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Path2D createPath(double... points) {
/* 600 */     return createPath(true, points);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Path2D createPath(boolean close, double... points) {
/* 607 */     Path2D path = new Path2D.Float();
/* 608 */     path.moveTo(points[0], points[1]);
/* 609 */     for (int i = 2; i < points.length; i += 2)
/* 610 */       path.lineTo(points[i], points[i + 1]); 
/* 611 */     if (close)
/* 612 */       path.closePath(); 
/* 613 */     return path;
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
/*     */   public static void drawString(JComponent c, Graphics g, String text, int x, int y) {
/* 626 */     HiDPIUtils.drawStringWithYCorrection(c, (Graphics2D)g, text, x, y);
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
/*     */   public static void drawStringUnderlineCharAt(JComponent c, Graphics g, String text, int underlinedIndex, int x, int y) {
/*     */     Graphics2DProxy graphics2DProxy;
/* 640 */     if (underlinedIndex >= 0 && UIScale.getUserScaleFactor() > 1.0F) {
/* 641 */       graphics2DProxy = new Graphics2DProxy((Graphics2D)g)
/*     */         {
/*     */           public void fillRect(int x, int y, int width, int height) {
/* 644 */             if (height == 1) {
/*     */ 
/*     */               
/* 647 */               height = Math.round(UIScale.scale(0.9F));
/* 648 */               y += height - 1;
/*     */             } 
/*     */             
/* 651 */             super.fillRect(x, y, width, height);
/*     */           }
/*     */         };
/*     */     }
/*     */     
/* 656 */     HiDPIUtils.drawStringUnderlineCharAtWithYCorrection(c, (Graphics2D)graphics2DProxy, text, underlinedIndex, x, y);
/*     */   }
/*     */   
/*     */   public static boolean hasOpaqueBeenExplicitlySet(JComponent c) {
/* 660 */     boolean oldOpaque = c.isOpaque();
/* 661 */     LookAndFeel.installProperty(c, "opaque", Boolean.valueOf(!oldOpaque));
/* 662 */     boolean explicitlySet = (c.isOpaque() == oldOpaque);
/* 663 */     LookAndFeel.installProperty(c, "opaque", Boolean.valueOf(oldOpaque));
/* 664 */     return explicitlySet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ComponentUI createSharedUI(Object key, Supplier<ComponentUI> newInstanceSupplier) {
/* 675 */     return ((IdentityHashMap<Object, ComponentUI>)sharedUIinstances
/* 676 */       .computeIfAbsent(UIManager.getLookAndFeel(), k -> new IdentityHashMap<>()))
/* 677 */       .computeIfAbsent(key, k -> (ComponentUI)newInstanceSupplier.get());
/*     */   }
/*     */ 
/*     */   
/*     */   public static class RepaintFocusListener
/*     */     implements FocusListener
/*     */   {
/*     */     private final Component repaintComponent;
/*     */ 
/*     */     
/*     */     public RepaintFocusListener(Component repaintComponent) {
/* 688 */       this.repaintComponent = repaintComponent;
/*     */     }
/*     */ 
/*     */     
/*     */     public void focusGained(FocusEvent e) {
/* 693 */       this.repaintComponent.repaint();
/*     */     }
/*     */ 
/*     */     
/*     */     public void focusLost(FocusEvent e) {
/* 698 */       this.repaintComponent.repaint();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatUIUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */