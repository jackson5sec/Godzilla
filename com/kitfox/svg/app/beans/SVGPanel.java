/*     */ package com.kitfox.svg.app.beans;
/*     */ 
/*     */ import com.kitfox.svg.SVGCache;
/*     */ import com.kitfox.svg.SVGDiagram;
/*     */ import com.kitfox.svg.SVGException;
/*     */ import com.kitfox.svg.SVGUniverse;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.net.URI;
/*     */ import javax.swing.JPanel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SVGPanel
/*     */   extends JPanel
/*     */ {
/*     */   public static final long serialVersionUID = 1L;
/*     */   public static final String PROP_AUTOSIZE = "PROP_AUTOSIZE";
/*  54 */   SVGUniverse svgUniverse = SVGCache.getSVGUniverse();
/*     */ 
/*     */   
/*     */   private boolean antiAlias;
/*     */ 
/*     */   
/*     */   URI svgURI;
/*     */   
/*  62 */   AffineTransform scaleXform = new AffineTransform();
/*     */   
/*     */   public static final int AUTOSIZE_NONE = 0;
/*     */   public static final int AUTOSIZE_HORIZ = 1;
/*     */   public static final int AUTOSIZE_VERT = 2;
/*     */   public static final int AUTOSIZE_BESTFIT = 3;
/*     */   public static final int AUTOSIZE_STRETCH = 4;
/*  69 */   private int autosize = 0;
/*     */ 
/*     */ 
/*     */   
/*     */   public SVGPanel() {
/*  74 */     initComponents();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSVGHeight() {
/*  79 */     if (this.autosize == 2 || this.autosize == 4 || this.autosize == 3)
/*     */     {
/*     */       
/*  82 */       return (getPreferredSize()).height;
/*     */     }
/*     */     
/*  85 */     SVGDiagram diagram = this.svgUniverse.getDiagram(this.svgURI);
/*  86 */     if (diagram == null)
/*     */     {
/*  88 */       return 0;
/*     */     }
/*  90 */     return (int)diagram.getHeight();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSVGWidth() {
/*  95 */     if (this.autosize == 1 || this.autosize == 4 || this.autosize == 3)
/*     */     {
/*     */       
/*  98 */       return (getPreferredSize()).width;
/*     */     }
/*     */     
/* 101 */     SVGDiagram diagram = this.svgUniverse.getDiagram(this.svgURI);
/* 102 */     if (diagram == null)
/*     */     {
/* 104 */       return 0;
/*     */     }
/* 106 */     return (int)diagram.getWidth();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void paintComponent(Graphics gg) {
/* 112 */     super.paintComponent(gg);
/*     */     
/* 114 */     Graphics2D g = (Graphics2D)gg.create();
/* 115 */     paintComponent(g);
/* 116 */     g.dispose();
/*     */   }
/*     */ 
/*     */   
/*     */   private void paintComponent(Graphics2D g) {
/* 121 */     Object oldAliasHint = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
/* 122 */     g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, this.antiAlias ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
/*     */ 
/*     */     
/* 125 */     SVGDiagram diagram = this.svgUniverse.getDiagram(this.svgURI);
/* 126 */     if (diagram == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 131 */     if (this.autosize == 0) {
/*     */ 
/*     */       
/*     */       try {
/* 135 */         diagram.render(g);
/* 136 */         g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAliasHint);
/*     */       }
/* 138 */       catch (SVGException e) {
/*     */         
/* 140 */         throw new RuntimeException(e);
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 145 */     Dimension dim = getSize();
/* 146 */     int width = dim.width;
/* 147 */     int height = dim.height;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 152 */     double diaWidth = diagram.getWidth();
/* 153 */     double diaHeight = diagram.getHeight();
/*     */     
/* 155 */     double scaleW = 1.0D;
/* 156 */     double scaleH = 1.0D;
/* 157 */     if (this.autosize == 3) {
/*     */       
/* 159 */       scaleW = scaleH = (height / diaHeight < width / diaWidth) ? (height / diaHeight) : (width / diaWidth);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 164 */       scaleW = scaleH = width / diaWidth;
/*     */ 
/*     */ 
/*     */       
/* 168 */       scaleW = scaleH = height / diaHeight;
/*     */       
/* 170 */       if (this.autosize == 4) {
/*     */         
/* 172 */         scaleW = width / diaWidth;
/* 173 */         scaleH = height / diaHeight;
/*     */       } 
/* 175 */     }  this.scaleXform.setToScale(scaleW, scaleH);
/*     */     
/* 177 */     AffineTransform oldXform = g.getTransform();
/* 178 */     g.transform(this.scaleXform);
/*     */ 
/*     */     
/*     */     try {
/* 182 */       diagram.render(g);
/*     */     }
/* 184 */     catch (SVGException e) {
/*     */       
/* 186 */       throw new RuntimeException(e);
/*     */     } 
/*     */     
/* 189 */     g.setTransform(oldXform);
/*     */     
/* 191 */     g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAliasHint);
/*     */   }
/*     */ 
/*     */   
/*     */   public SVGUniverse getSvgUniverse() {
/* 196 */     return this.svgUniverse;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSvgUniverse(SVGUniverse svgUniverse) {
/* 201 */     SVGUniverse old = this.svgUniverse;
/* 202 */     this.svgUniverse = svgUniverse;
/* 203 */     firePropertyChange("svgUniverse", old, svgUniverse);
/*     */   }
/*     */ 
/*     */   
/*     */   public URI getSvgURI() {
/* 208 */     return this.svgURI;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSvgURI(URI svgURI) {
/* 213 */     URI old = this.svgURI;
/* 214 */     this.svgURI = svgURI;
/* 215 */     firePropertyChange("svgURI", old, svgURI);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSvgResourcePath(String resourcePath) throws SVGException {
/* 225 */     URI old = this.svgURI;
/*     */ 
/*     */     
/*     */     try {
/* 229 */       this.svgURI = new URI(getClass().getResource(resourcePath).toString());
/*     */ 
/*     */       
/* 232 */       firePropertyChange("svgURI", old, this.svgURI);
/*     */       
/* 234 */       repaint();
/*     */     }
/* 236 */     catch (Exception e) {
/*     */       
/* 238 */       throw new SVGException("Could not resolve path " + resourcePath, e);
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
/*     */   public boolean isScaleToFit() {
/* 251 */     return (this.autosize == 4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScaleToFit(boolean scaleToFit) {
/* 259 */     setAutosize(4);
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
/*     */   public boolean getUseAntiAlias() {
/* 271 */     return getAntiAlias();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUseAntiAlias(boolean antiAlias) {
/* 280 */     setAntiAlias(antiAlias);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getAntiAlias() {
/* 288 */     return this.antiAlias;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAntiAlias(boolean antiAlias) {
/* 296 */     boolean old = this.antiAlias;
/* 297 */     this.antiAlias = antiAlias;
/* 298 */     firePropertyChange("antiAlias", old, antiAlias);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAutosize() {
/* 306 */     return this.autosize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAutosize(int autosize) {
/* 314 */     int oldAutosize = this.autosize;
/* 315 */     this.autosize = autosize;
/* 316 */     firePropertyChange("PROP_AUTOSIZE", oldAutosize, autosize);
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
/*     */   private void initComponents() {
/* 328 */     setLayout(new BorderLayout());
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\app\beans\SVGPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */