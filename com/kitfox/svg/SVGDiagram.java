/*     */ package com.kitfox.svg;
/*     */ 
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.Point2D;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.io.Serializable;
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SVGDiagram
/*     */   implements Serializable
/*     */ {
/*     */   public static final long serialVersionUID = 0L;
/*  64 */   final HashMap<String, SVGElement> idMap = new HashMap<String, SVGElement>();
/*     */ 
/*     */   
/*     */   SVGRoot root;
/*     */ 
/*     */   
/*     */   final SVGUniverse universe;
/*     */   
/*  72 */   private Rectangle deviceViewport = new Rectangle(100, 100);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean ignoreClipHeuristic = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final URI xmlBase;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SVGDiagram(URI xmlBase, SVGUniverse universe) {
/*  99 */     this.universe = universe;
/*     */     
/* 101 */     this.xmlBase = xmlBase;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void render(Graphics2D g) throws SVGException {
/* 111 */     this.root.renderToViewport(g);
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
/*     */   public List<List<SVGElement>> pick(Point2D point, List<List<SVGElement>> retVec) throws SVGException {
/* 129 */     return pick(point, false, retVec);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<List<SVGElement>> pick(Point2D point, boolean boundingBox, List<List<SVGElement>> retVec) throws SVGException {
/* 134 */     if (retVec == null)
/*     */     {
/* 136 */       retVec = new ArrayList<List<SVGElement>>();
/*     */     }
/*     */     
/* 139 */     this.root.pick(point, boundingBox, retVec);
/*     */     
/* 141 */     return retVec;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<List<SVGElement>> pick(Rectangle2D pickArea, List<List<SVGElement>> retVec) throws SVGException {
/* 146 */     return pick(pickArea, false, retVec);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<List<SVGElement>> pick(Rectangle2D pickArea, boolean boundingBox, List<List<SVGElement>> retVec) throws SVGException {
/* 151 */     if (retVec == null)
/*     */     {
/* 153 */       retVec = new ArrayList<List<SVGElement>>();
/*     */     }
/*     */     
/* 156 */     this.root.pick(pickArea, new AffineTransform(), boundingBox, retVec);
/*     */     
/* 158 */     return retVec;
/*     */   }
/*     */ 
/*     */   
/*     */   public SVGUniverse getUniverse() {
/* 163 */     return this.universe;
/*     */   }
/*     */ 
/*     */   
/*     */   public URI getXMLBase() {
/* 168 */     return this.xmlBase;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getWidth() {
/* 178 */     if (this.root == null) return 0.0F; 
/* 179 */     return this.root.getDeviceWidth();
/*     */   }
/*     */ 
/*     */   
/*     */   public float getHeight() {
/* 184 */     if (this.root == null) return 0.0F; 
/* 185 */     return this.root.getDeviceHeight();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle2D getViewRect(Rectangle2D rect) {
/* 195 */     if (this.root != null) return this.root.getDeviceRect(rect); 
/* 196 */     return rect;
/*     */   }
/*     */ 
/*     */   
/*     */   public Rectangle2D getViewRect() {
/* 201 */     return getViewRect(new Rectangle2D.Double());
/*     */   }
/*     */ 
/*     */   
/*     */   public SVGElement getElement(String name) {
/* 206 */     return this.idMap.get(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setElement(String name, SVGElement node) {
/* 211 */     this.idMap.put(name, node);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeElement(String name) {
/* 216 */     this.idMap.remove(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public SVGRoot getRoot() {
/* 221 */     return this.root;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRoot(SVGRoot root) {
/* 226 */     this.root = root;
/* 227 */     root.setDiagram(this);
/*     */   }
/*     */   public boolean ignoringClipHeuristic() {
/* 230 */     return this.ignoreClipHeuristic;
/*     */   } public void setIgnoringClipHeuristic(boolean ignoreClipHeuristic) {
/* 232 */     this.ignoreClipHeuristic = ignoreClipHeuristic;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateTime(double curTime) throws SVGException {
/* 242 */     if (this.root == null)
/* 243 */       return;  this.root.updateTime(curTime);
/*     */   }
/*     */ 
/*     */   
/*     */   public Rectangle getDeviceViewport() {
/* 248 */     return this.deviceViewport;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDeviceViewport(Rectangle deviceViewport) {
/* 259 */     this.deviceViewport.setBounds(deviceViewport);
/* 260 */     if (this.root != null)
/*     */       
/*     */       try {
/*     */         
/* 264 */         this.root.build();
/* 265 */       } catch (SVGException ex) {
/*     */         
/* 267 */         Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, "Could not build document", ex);
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\SVGDiagram.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */