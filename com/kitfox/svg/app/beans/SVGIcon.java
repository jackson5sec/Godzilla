/*     */ package com.kitfox.svg.app.beans;
/*     */ 
/*     */ import com.kitfox.svg.SVGCache;
/*     */ import com.kitfox.svg.SVGDiagram;
/*     */ import com.kitfox.svg.SVGException;
/*     */ import com.kitfox.svg.SVGUniverse;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Image;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.net.URI;
/*     */ import javax.swing.ImageIcon;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SVGIcon
/*     */   extends ImageIcon
/*     */ {
/*     */   public static final long serialVersionUID = 1L;
/*     */   public static final String PROP_AUTOSIZE = "PROP_AUTOSIZE";
/*  69 */   private final PropertyChangeSupport changes = new PropertyChangeSupport(this);
/*     */   
/*  71 */   SVGUniverse svgUniverse = SVGCache.getSVGUniverse();
/*     */   
/*     */   public static final int INTERP_NEAREST_NEIGHBOR = 0;
/*     */   public static final int INTERP_BILINEAR = 1;
/*     */   public static final int INTERP_BICUBIC = 2;
/*     */   private boolean antiAlias;
/*  77 */   private int interpolation = 0;
/*     */   
/*     */   private boolean clipToViewbox;
/*     */   
/*     */   URI svgURI;
/*     */   
/*  83 */   AffineTransform scaleXform = new AffineTransform();
/*     */   
/*     */   public static final int AUTOSIZE_NONE = 0;
/*     */   public static final int AUTOSIZE_HORIZ = 1;
/*     */   public static final int AUTOSIZE_VERT = 2;
/*     */   public static final int AUTOSIZE_BESTFIT = 3;
/*     */   public static final int AUTOSIZE_STRETCH = 4;
/*  90 */   private int autosize = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Dimension preferredSize;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPropertyChangeListener(PropertyChangeListener p) {
/* 101 */     this.changes.addPropertyChangeListener(p);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removePropertyChangeListener(PropertyChangeListener p) {
/* 106 */     this.changes.removePropertyChangeListener(p);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Image getImage() {
/* 112 */     BufferedImage bi = new BufferedImage(getIconWidth(), getIconHeight(), 2);
/* 113 */     paintIcon((Component)null, bi.getGraphics(), 0, 0);
/* 114 */     return bi;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIconHeightIgnoreAutosize() {
/* 122 */     if (this.preferredSize != null && (this.autosize == 2 || this.autosize == 4 || this.autosize == 3))
/*     */     {
/*     */ 
/*     */       
/* 126 */       return this.preferredSize.height;
/*     */     }
/*     */     
/* 129 */     SVGDiagram diagram = this.svgUniverse.getDiagram(this.svgURI);
/* 130 */     if (diagram == null)
/*     */     {
/* 132 */       return 0;
/*     */     }
/* 134 */     return (int)diagram.getHeight();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIconWidthIgnoreAutosize() {
/* 143 */     if (this.preferredSize != null && (this.autosize == 1 || this.autosize == 4 || this.autosize == 3))
/*     */     {
/*     */ 
/*     */       
/* 147 */       return this.preferredSize.width;
/*     */     }
/*     */     
/* 150 */     SVGDiagram diagram = this.svgUniverse.getDiagram(this.svgURI);
/* 151 */     if (diagram == null)
/*     */     {
/* 153 */       return 0;
/*     */     }
/* 155 */     return (int)diagram.getWidth();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isAutoSizeBestFitUseFixedHeight(int iconWidthIgnoreAutosize, int iconHeightIgnoreAutosize, SVGDiagram diagram) {
/* 161 */     return (iconHeightIgnoreAutosize / diagram.getHeight() < iconWidthIgnoreAutosize / diagram.getWidth());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIconWidth() {
/* 167 */     int iconWidthIgnoreAutosize = getIconWidthIgnoreAutosize();
/* 168 */     int iconHeightIgnoreAutosize = getIconHeightIgnoreAutosize();
/* 169 */     SVGDiagram diagram = this.svgUniverse.getDiagram(this.svgURI);
/* 170 */     if (this.preferredSize != null && (this.autosize == 2 || (this.autosize == 3 && 
/* 171 */       isAutoSizeBestFitUseFixedHeight(iconWidthIgnoreAutosize, iconHeightIgnoreAutosize, diagram)))) {
/*     */       
/* 173 */       double aspectRatio = (diagram.getHeight() / diagram.getWidth());
/* 174 */       return (int)(iconHeightIgnoreAutosize / aspectRatio);
/*     */     } 
/*     */ 
/*     */     
/* 178 */     return iconWidthIgnoreAutosize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIconHeight() {
/* 185 */     int iconWidthIgnoreAutosize = getIconWidthIgnoreAutosize();
/* 186 */     int iconHeightIgnoreAutosize = getIconHeightIgnoreAutosize();
/* 187 */     SVGDiagram diagram = this.svgUniverse.getDiagram(this.svgURI);
/* 188 */     if (this.preferredSize != null && (this.autosize == 1 || (this.autosize == 3 && 
/* 189 */       !isAutoSizeBestFitUseFixedHeight(iconWidthIgnoreAutosize, iconHeightIgnoreAutosize, diagram)))) {
/*     */       
/* 191 */       double aspectRatio = (diagram.getHeight() / diagram.getWidth());
/* 192 */       return (int)(iconWidthIgnoreAutosize * aspectRatio);
/*     */     } 
/*     */ 
/*     */     
/* 196 */     return iconHeightIgnoreAutosize;
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
/*     */   public void paintIcon(Component comp, Graphics gg, int x, int y) {
/* 212 */     Graphics2D g = (Graphics2D)gg.create();
/* 213 */     paintIcon(comp, g, x, y);
/* 214 */     g.dispose();
/*     */   }
/*     */ 
/*     */   
/*     */   private void paintIcon(Component comp, Graphics2D g, int x, int y) {
/* 219 */     Object oldAliasHint = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
/* 220 */     g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, this.antiAlias ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
/*     */     
/* 222 */     Object oldInterpolationHint = g.getRenderingHint(RenderingHints.KEY_INTERPOLATION);
/* 223 */     switch (this.interpolation) {
/*     */       
/*     */       case 0:
/* 226 */         g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
/*     */         break;
/*     */       case 1:
/* 229 */         g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
/*     */         break;
/*     */       case 2:
/* 232 */         g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/* 237 */     SVGDiagram diagram = this.svgUniverse.getDiagram(this.svgURI);
/* 238 */     if (diagram == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 243 */     g.translate(x, y);
/* 244 */     diagram.setIgnoringClipHeuristic(!this.clipToViewbox);
/* 245 */     if (this.clipToViewbox)
/*     */     {
/* 247 */       g.setClip(new Rectangle2D.Float(0.0F, 0.0F, diagram.getWidth(), diagram.getHeight()));
/*     */     }
/*     */ 
/*     */     
/* 251 */     if (this.autosize == 0) {
/*     */ 
/*     */       
/*     */       try {
/* 255 */         diagram.render(g);
/* 256 */         g.translate(-x, -y);
/* 257 */         g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAliasHint);
/*     */       }
/* 259 */       catch (Exception e) {
/*     */         
/* 261 */         throw new RuntimeException(e);
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 266 */     int width = getIconWidthIgnoreAutosize();
/* 267 */     int height = getIconHeightIgnoreAutosize();
/*     */ 
/*     */ 
/*     */     
/* 271 */     if (width == 0 || height == 0) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 292 */     double diaWidth = diagram.getWidth();
/* 293 */     double diaHeight = diagram.getHeight();
/*     */     
/* 295 */     double scaleW = 1.0D;
/* 296 */     double scaleH = 1.0D;
/* 297 */     if (this.autosize == 3) {
/*     */       
/* 299 */       scaleW = scaleH = (height / diaHeight < width / diaWidth) ? (height / diaHeight) : (width / diaWidth);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 304 */       scaleW = scaleH = width / diaWidth;
/*     */ 
/*     */ 
/*     */       
/* 308 */       scaleW = scaleH = height / diaHeight;
/*     */       
/* 310 */       if (this.autosize == 4) {
/*     */         
/* 312 */         scaleW = width / diaWidth;
/* 313 */         scaleH = height / diaHeight;
/*     */       } 
/* 315 */     }  this.scaleXform.setToScale(scaleW, scaleH);
/*     */     
/* 317 */     AffineTransform oldXform = g.getTransform();
/* 318 */     g.transform(this.scaleXform);
/*     */ 
/*     */     
/*     */     try {
/* 322 */       diagram.render(g);
/*     */     }
/* 324 */     catch (SVGException e) {
/*     */       
/* 326 */       throw new RuntimeException(e);
/*     */     } 
/*     */     
/* 329 */     g.setTransform(oldXform);
/*     */ 
/*     */     
/* 332 */     g.translate(-x, -y);
/*     */     
/* 334 */     g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAliasHint);
/* 335 */     if (oldInterpolationHint != null)
/*     */     {
/* 337 */       g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, oldInterpolationHint);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SVGUniverse getSvgUniverse() {
/* 346 */     return this.svgUniverse;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSvgUniverse(SVGUniverse svgUniverse) {
/* 351 */     SVGUniverse old = this.svgUniverse;
/* 352 */     this.svgUniverse = svgUniverse;
/* 353 */     this.changes.firePropertyChange("svgUniverse", old, svgUniverse);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI getSvgURI() {
/* 361 */     return this.svgURI;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSvgURI(URI svgURI) {
/* 370 */     URI old = this.svgURI;
/* 371 */     this.svgURI = svgURI;
/*     */     
/* 373 */     SVGDiagram diagram = this.svgUniverse.getDiagram(svgURI);
/* 374 */     if (diagram != null) {
/*     */       
/* 376 */       Dimension size = getPreferredSize();
/* 377 */       if (size == null)
/*     */       {
/* 379 */         size = new Dimension((int)diagram.getRoot().getDeviceWidth(), (int)diagram.getRoot().getDeviceHeight());
/*     */       }
/* 381 */       diagram.setDeviceViewport(new Rectangle(0, 0, size.width, size.height));
/*     */     } 
/*     */     
/* 384 */     this.changes.firePropertyChange("svgURI", old, svgURI);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSvgResourcePath(String resourcePath) {
/* 394 */     URI old = this.svgURI;
/*     */ 
/*     */     
/*     */     try {
/* 398 */       this.svgURI = new URI(getClass().getResource(resourcePath).toString());
/* 399 */       this.changes.firePropertyChange("svgURI", old, this.svgURI);
/*     */       
/* 401 */       SVGDiagram diagram = this.svgUniverse.getDiagram(this.svgURI);
/* 402 */       if (diagram != null)
/*     */       {
/* 404 */         diagram.setDeviceViewport(new Rectangle(0, 0, this.preferredSize.width, this.preferredSize.height));
/*     */       
/*     */       }
/*     */     }
/* 408 */     catch (Exception e) {
/*     */       
/* 410 */       this.svgURI = old;
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
/*     */   public boolean isScaleToFit() {
/* 422 */     return (this.autosize == 4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScaleToFit(boolean scaleToFit) {
/* 430 */     setAutosize(4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Dimension getPreferredSize() {
/* 438 */     if (this.preferredSize == null) {
/*     */       
/* 440 */       SVGDiagram diagram = this.svgUniverse.getDiagram(this.svgURI);
/* 441 */       if (diagram != null)
/*     */       {
/*     */         
/* 444 */         setPreferredSize(new Dimension((int)diagram.getWidth(), (int)diagram.getHeight()));
/*     */       }
/*     */     } 
/*     */     
/* 448 */     return new Dimension(this.preferredSize);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPreferredSize(Dimension preferredSize) {
/* 453 */     Dimension old = this.preferredSize;
/* 454 */     this.preferredSize = preferredSize;
/*     */     
/* 456 */     SVGDiagram diagram = this.svgUniverse.getDiagram(this.svgURI);
/* 457 */     if (diagram != null)
/*     */     {
/* 459 */       diagram.setDeviceViewport(new Rectangle(0, 0, preferredSize.width, preferredSize.height));
/*     */     }
/*     */     
/* 462 */     this.changes.firePropertyChange("preferredSize", old, preferredSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getUseAntiAlias() {
/* 472 */     return getAntiAlias();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUseAntiAlias(boolean antiAlias) {
/* 481 */     setAntiAlias(antiAlias);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getAntiAlias() {
/* 489 */     return this.antiAlias;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAntiAlias(boolean antiAlias) {
/* 497 */     boolean old = this.antiAlias;
/* 498 */     this.antiAlias = antiAlias;
/* 499 */     this.changes.firePropertyChange("antiAlias", old, antiAlias);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getInterpolation() {
/* 507 */     return this.interpolation;
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
/*     */   public void setInterpolation(int interpolation) {
/* 519 */     int old = this.interpolation;
/* 520 */     this.interpolation = interpolation;
/* 521 */     this.changes.firePropertyChange("interpolation", old, interpolation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isClipToViewbox() {
/* 530 */     return this.clipToViewbox;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setClipToViewbox(boolean clipToViewbox) {
/* 535 */     this.clipToViewbox = clipToViewbox;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAutosize() {
/* 543 */     return this.autosize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAutosize(int autosize) {
/* 551 */     int oldAutosize = this.autosize;
/* 552 */     this.autosize = autosize;
/* 553 */     this.changes.firePropertyChange("PROP_AUTOSIZE", oldAutosize, autosize);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\app\beans\SVGIcon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */