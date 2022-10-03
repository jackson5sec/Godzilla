/*     */ package com.formdev.flatlaf.extras;
/*     */ 
/*     */ import com.formdev.flatlaf.FlatIconColors;
/*     */ import com.formdev.flatlaf.FlatLaf;
/*     */ import com.formdev.flatlaf.ui.FlatUIUtils;
/*     */ import com.formdev.flatlaf.util.Graphics2DProxy;
/*     */ import com.formdev.flatlaf.util.GrayFilter;
/*     */ import com.formdev.flatlaf.util.MultiResolutionImageSupport;
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import com.kitfox.svg.SVGDiagram;
/*     */ import com.kitfox.svg.SVGException;
/*     */ import com.kitfox.svg.SVGUniverse;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Image;
/*     */ import java.awt.Paint;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.RGBImageFilter;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.ImageIcon;
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
/*     */ public class FlatSVGIcon
/*     */   extends ImageIcon
/*     */   implements FlatLaf.DisabledIconProvider
/*     */ {
/*  60 */   private static final SVGUniverse svgUniverse = new SVGUniverse();
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final int width;
/*     */   
/*     */   private final int height;
/*     */   
/*     */   private final float scale;
/*     */   
/*     */   private final boolean disabled;
/*     */   
/*     */   private final ClassLoader classLoader;
/*     */   
/*     */   private SVGDiagram diagram;
/*     */   
/*     */   private boolean dark;
/*     */   
/*     */   private static Boolean darkLaf;
/*     */ 
/*     */   
/*     */   public FlatSVGIcon(String name) {
/*  82 */     this(name, -1, -1, 1.0F, false, null);
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
/*     */   public FlatSVGIcon(String name, ClassLoader classLoader) {
/*  97 */     this(name, -1, -1, 1.0F, false, classLoader);
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
/*     */   public FlatSVGIcon(String name, int width, int height) {
/* 111 */     this(name, width, height, 1.0F, false, null);
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
/*     */   public FlatSVGIcon(String name, int width, int height, ClassLoader classLoader) {
/* 127 */     this(name, width, height, 1.0F, false, classLoader);
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
/*     */   public FlatSVGIcon(String name, float scale) {
/* 142 */     this(name, -1, -1, scale, false, null);
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
/*     */   public FlatSVGIcon(String name, float scale, ClassLoader classLoader) {
/* 159 */     this(name, -1, -1, scale, false, classLoader);
/*     */   }
/*     */   
/*     */   private FlatSVGIcon(String name, int width, int height, float scale, boolean disabled, ClassLoader classLoader) {
/* 163 */     this.name = name;
/* 164 */     this.classLoader = classLoader;
/* 165 */     this.width = width;
/* 166 */     this.height = height;
/* 167 */     this.scale = scale;
/* 168 */     this.disabled = disabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FlatSVGIcon derive(int width, int height) {
/* 179 */     if (width == this.width && height == this.height) {
/* 180 */       return this;
/*     */     }
/* 182 */     FlatSVGIcon icon = new FlatSVGIcon(this.name, width, height, this.scale, false, this.classLoader);
/* 183 */     icon.diagram = this.diagram;
/* 184 */     icon.dark = this.dark;
/* 185 */     return icon;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FlatSVGIcon derive(float scale) {
/* 195 */     if (scale == this.scale) {
/* 196 */       return this;
/*     */     }
/* 198 */     FlatSVGIcon icon = new FlatSVGIcon(this.name, this.width, this.height, scale, false, this.classLoader);
/* 199 */     icon.diagram = this.diagram;
/* 200 */     icon.dark = this.dark;
/* 201 */     return icon;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Icon getDisabledIcon() {
/* 211 */     if (this.disabled) {
/* 212 */       return this;
/*     */     }
/* 214 */     FlatSVGIcon icon = new FlatSVGIcon(this.name, this.width, this.height, this.scale, true, this.classLoader);
/* 215 */     icon.diagram = this.diagram;
/* 216 */     icon.dark = this.dark;
/* 217 */     return icon;
/*     */   }
/*     */   
/*     */   private void update() {
/* 221 */     if (this.dark == isDarkLaf() && this.diagram != null) {
/*     */       return;
/*     */     }
/* 224 */     this.dark = isDarkLaf();
/* 225 */     URL url = getIconURL(this.name, this.dark);
/* 226 */     if ((((url == null) ? 1 : 0) & this.dark) != 0) {
/* 227 */       url = getIconURL(this.name, false);
/*     */     }
/*     */     
/*     */     try {
/* 231 */       this.diagram = svgUniverse.getDiagram(url.toURI());
/* 232 */     } catch (URISyntaxException ex) {
/* 233 */       ex.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   private URL getIconURL(String name, boolean dark) {
/* 238 */     if (dark) {
/* 239 */       int dotIndex = name.lastIndexOf('.');
/* 240 */       name = name.substring(0, dotIndex) + "_dark" + name.substring(dotIndex);
/*     */     } 
/*     */     
/* 243 */     ClassLoader cl = (this.classLoader != null) ? this.classLoader : FlatSVGIcon.class.getClassLoader();
/* 244 */     return cl.getResource(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasFound() {
/* 253 */     update();
/* 254 */     return (this.diagram != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIconWidth() {
/* 262 */     if (this.width > 0) {
/* 263 */       return scaleSize(this.width);
/*     */     }
/* 265 */     update();
/* 266 */     return scaleSize((this.diagram != null) ? Math.round(this.diagram.getWidth()) : 16);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIconHeight() {
/* 274 */     if (this.height > 0) {
/* 275 */       return scaleSize(this.height);
/*     */     }
/* 277 */     update();
/* 278 */     return scaleSize((this.diagram != null) ? Math.round(this.diagram.getHeight()) : 16);
/*     */   }
/*     */   
/*     */   private int scaleSize(int size) {
/* 282 */     int scaledSize = UIScale.scale(size);
/* 283 */     if (this.scale != 1.0F)
/* 284 */       scaledSize = Math.round(scaledSize * this.scale); 
/* 285 */     return scaledSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public void paintIcon(Component c, Graphics g, int x, int y) {
/* 290 */     update();
/*     */ 
/*     */     
/* 293 */     Rectangle clipBounds = g.getClipBounds();
/* 294 */     if (clipBounds != null && !clipBounds.intersects(new Rectangle(x, y, getIconWidth(), getIconHeight()))) {
/*     */       return;
/*     */     }
/*     */     
/* 298 */     RGBImageFilter grayFilter = null;
/* 299 */     if (this.disabled) {
/* 300 */       Object grayFilterObj = UIManager.get("Component.grayFilter");
/*     */ 
/*     */       
/* 303 */       grayFilter = (grayFilterObj instanceof RGBImageFilter) ? (RGBImageFilter)grayFilterObj : (RGBImageFilter)GrayFilter.createDisabledIconFilter(this.dark);
/*     */     } 
/*     */     
/* 306 */     GraphicsFilter graphicsFilter = new GraphicsFilter((Graphics2D)g.create(), ColorFilter.getInstance(), grayFilter);
/*     */     
/*     */     try {
/* 309 */       FlatUIUtils.setRenderingHints((Graphics)graphicsFilter);
/* 310 */       graphicsFilter.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
/*     */       
/* 312 */       paintSvg((Graphics2D)graphicsFilter, x, y);
/*     */     } finally {
/* 314 */       graphicsFilter.dispose();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void paintSvg(Graphics2D g, int x, int y) {
/* 319 */     if (this.diagram == null) {
/* 320 */       paintSvgError(g, x, y);
/*     */       
/*     */       return;
/*     */     } 
/* 324 */     g.translate(x, y);
/* 325 */     g.clipRect(0, 0, getIconWidth(), getIconHeight());
/*     */     
/* 327 */     UIScale.scaleGraphics(g);
/* 328 */     if (this.width > 0 || this.height > 0) {
/* 329 */       double sx = (this.width > 0) ? (this.width / this.diagram.getWidth()) : 1.0D;
/* 330 */       double sy = (this.height > 0) ? (this.height / this.diagram.getHeight()) : 1.0D;
/* 331 */       if (sx != 1.0D || sy != 1.0D)
/* 332 */         g.scale(sx, sy); 
/*     */     } 
/* 334 */     if (this.scale != 1.0F) {
/* 335 */       g.scale(this.scale, this.scale);
/*     */     }
/* 337 */     this.diagram.setIgnoringClipHeuristic(true);
/*     */     
/*     */     try {
/* 340 */       this.diagram.render(g);
/* 341 */     } catch (SVGException ex) {
/* 342 */       paintSvgError(g, 0, 0);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void paintSvgError(Graphics2D g, int x, int y) {
/* 347 */     g.setColor(Color.red);
/* 348 */     g.fillRect(x, y, getIconWidth(), getIconHeight());
/*     */   }
/*     */ 
/*     */   
/*     */   public Image getImage() {
/* 353 */     update();
/*     */ 
/*     */     
/* 356 */     int iconWidth = getIconWidth();
/* 357 */     int iconHeight = getIconHeight();
/*     */     
/* 359 */     Dimension[] dimensions = { new Dimension(iconWidth, iconHeight), new Dimension(iconWidth * 2, iconHeight * 2) };
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 364 */     Function<Dimension, Image> producer = size -> {
/*     */         BufferedImage image = new BufferedImage(size.width, size.height, 2);
/*     */         
/*     */         Graphics2D g = image.createGraphics();
/*     */         
/*     */         try {
/*     */           double sx = (size.width > 0) ? (size.width / iconWidth) : 1.0D;
/*     */           double sy = (size.height > 0) ? (size.height / iconHeight) : 1.0D;
/*     */           if (sx != 1.0D || sy != 1.0D) {
/*     */             g.scale(sx, sy);
/*     */           }
/*     */           paintIcon(null, g, 0, 0);
/*     */         } finally {
/*     */           g.dispose();
/*     */         } 
/*     */         return image;
/*     */       };
/* 381 */     return MultiResolutionImageSupport.create(0, dimensions, producer);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isDarkLaf() {
/* 387 */     if (darkLaf == null) {
/* 388 */       lafChanged();
/*     */       
/* 390 */       UIManager.addPropertyChangeListener(e -> lafChanged());
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 395 */     return darkLaf.booleanValue();
/*     */   }
/*     */   
/*     */   private static void lafChanged() {
/* 399 */     darkLaf = Boolean.valueOf(FlatLaf.isLafDark());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class ColorFilter
/*     */   {
/*     */     private static ColorFilter instance;
/*     */     
/* 408 */     private final Map<Integer, String> rgb2keyMap = new HashMap<>();
/* 409 */     private final Map<Color, Color> color2colorMap = new HashMap<>();
/*     */     
/*     */     public static ColorFilter getInstance() {
/* 412 */       if (instance == null)
/* 413 */         instance = new ColorFilter(); 
/* 414 */       return instance;
/*     */     }
/*     */     
/*     */     public ColorFilter() {
/* 418 */       for (FlatIconColors c : FlatIconColors.values())
/* 419 */         this.rgb2keyMap.put(Integer.valueOf(c.rgb), c.key); 
/*     */     }
/*     */     
/*     */     public void addAll(Map<Color, Color> from2toMap) {
/* 423 */       this.color2colorMap.putAll(from2toMap);
/*     */     }
/*     */     
/*     */     public void add(Color from, Color to) {
/* 427 */       this.color2colorMap.put(from, to);
/*     */     }
/*     */     
/*     */     public void remove(Color from) {
/* 431 */       this.color2colorMap.remove(from);
/*     */     }
/*     */     
/*     */     public Color filter(Color color) {
/* 435 */       Color newColor = this.color2colorMap.get(color);
/* 436 */       if (newColor != null) {
/* 437 */         return newColor;
/*     */       }
/* 439 */       String colorKey = this.rgb2keyMap.get(Integer.valueOf(color.getRGB() & 0xFFFFFF));
/* 440 */       if (colorKey == null) {
/* 441 */         return color;
/*     */       }
/* 443 */       newColor = UIManager.getColor(colorKey);
/* 444 */       if (newColor == null) {
/* 445 */         return color;
/*     */       }
/* 447 */       return (newColor.getAlpha() != color.getAlpha()) ? new Color(newColor
/* 448 */           .getRGB() & 0xFFFFFF | color.getRGB() & 0xFF000000) : newColor;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class GraphicsFilter
/*     */     extends Graphics2DProxy
/*     */   {
/*     */     private final FlatSVGIcon.ColorFilter colorFilter;
/*     */     
/*     */     private final RGBImageFilter grayFilter;
/*     */ 
/*     */     
/*     */     public GraphicsFilter(Graphics2D delegate, FlatSVGIcon.ColorFilter colorFilter, RGBImageFilter grayFilter) {
/* 462 */       super(delegate);
/* 463 */       this.colorFilter = colorFilter;
/* 464 */       this.grayFilter = grayFilter;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setColor(Color c) {
/* 469 */       super.setColor(filterColor(c));
/*     */     }
/*     */ 
/*     */     
/*     */     public void setPaint(Paint paint) {
/* 474 */       if (paint instanceof Color)
/* 475 */         paint = filterColor((Color)paint); 
/* 476 */       super.setPaint(paint);
/*     */     }
/*     */     
/*     */     private Color filterColor(Color color) {
/* 480 */       if (this.colorFilter != null)
/* 481 */         color = this.colorFilter.filter(color); 
/* 482 */       if (this.grayFilter != null) {
/* 483 */         int oldRGB = color.getRGB();
/* 484 */         int newRGB = this.grayFilter.filterRGB(0, 0, oldRGB);
/* 485 */         color = (newRGB != oldRGB) ? new Color(newRGB, true) : color;
/*     */       } 
/* 487 */       return color;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\extras\FlatSVGIcon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */