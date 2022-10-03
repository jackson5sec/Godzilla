/*     */ package com.kitfox.svg;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.event.ComponentAdapter;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.Scrollable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SVGDisplayPanel
/*     */   extends JPanel
/*     */   implements Scrollable
/*     */ {
/*     */   public static final long serialVersionUID = 1L;
/*  53 */   SVGDiagram diagram = null;
/*  54 */   float scale = 1.0F;
/*  55 */   Color bgColor = null;
/*     */ 
/*     */ 
/*     */   
/*     */   public SVGDisplayPanel() {
/*  60 */     initComponents();
/*     */   }
/*     */ 
/*     */   
/*     */   public SVGDiagram getDiagram() {
/*  65 */     return this.diagram;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDiagram(SVGDiagram diagram) {
/*  70 */     this.diagram = diagram;
/*  71 */     diagram.setDeviceViewport(getBounds());
/*     */     
/*  73 */     setDimension();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setScale(float scale) {
/*  78 */     this.scale = scale;
/*  79 */     setDimension();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBgColor(Color col) {
/*  84 */     this.bgColor = col;
/*     */   }
/*     */ 
/*     */   
/*     */   private void setDimension() {
/*  89 */     if (this.diagram == null) {
/*     */       
/*  91 */       setPreferredSize(new Dimension(1, 1));
/*  92 */       revalidate();
/*     */       
/*     */       return;
/*     */     } 
/*  96 */     Rectangle2D.Float rect = new Rectangle2D.Float();
/*  97 */     this.diagram.getViewRect(rect);
/*     */     
/*  99 */     int w = (int)(rect.width * this.scale);
/* 100 */     int h = (int)(rect.height * this.scale);
/*     */     
/* 102 */     setPreferredSize(new Dimension(w, h));
/* 103 */     revalidate();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateTime(double curTime) throws SVGException {
/* 113 */     if (this.diagram == null)
/*     */       return; 
/* 115 */     this.diagram.updateTime(curTime);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void paintComponent(Graphics gg) {
/* 121 */     Graphics2D g = (Graphics2D)gg;
/*     */     
/* 123 */     if (this.bgColor != null) {
/*     */       
/* 125 */       Dimension dim = getSize();
/* 126 */       g.setColor(this.bgColor);
/* 127 */       g.fillRect(0, 0, dim.width, dim.height);
/*     */     } 
/*     */     
/* 130 */     g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/* 131 */     g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
/* 132 */     if (this.diagram != null) {
/*     */       
/*     */       try {
/*     */         
/* 136 */         this.diagram.render(g);
/*     */       }
/* 138 */       catch (SVGException e) {
/*     */         
/* 140 */         Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, "Could not render diagram", e);
/*     */       } 
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
/*     */   private void initComponents() {
/* 155 */     setLayout(new BorderLayout());
/*     */     
/* 157 */     addComponentListener(new ComponentAdapter()
/*     */         {
/*     */           
/*     */           public void componentResized(ComponentEvent evt)
/*     */           {
/* 162 */             SVGDisplayPanel.this.formComponentResized(evt);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void formComponentResized(ComponentEvent evt) {
/* 170 */     if (this.diagram != null) {
/*     */       
/* 172 */       this.diagram.setDeviceViewport(getBounds());
/* 173 */       setDimension();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Dimension getPreferredScrollableViewportSize() {
/* 180 */     return getPreferredSize();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
/* 185 */     if (orientation == 0)
/*     */     {
/* 187 */       return visibleRect.width;
/*     */     }
/* 189 */     return visibleRect.height;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getScrollableTracksViewportHeight() {
/* 194 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getScrollableTracksViewportWidth() {
/* 199 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
/* 204 */     return getScrollableBlockIncrement(visibleRect, orientation, direction) / 16;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\SVGDisplayPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */