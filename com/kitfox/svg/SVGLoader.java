/*     */ package com.kitfox.svg;
/*     */ 
/*     */ import com.kitfox.svg.animation.Animate;
/*     */ import com.kitfox.svg.animation.AnimateColor;
/*     */ import com.kitfox.svg.animation.AnimateMotion;
/*     */ import com.kitfox.svg.animation.AnimateTransform;
/*     */ import com.kitfox.svg.animation.SetSmil;
/*     */ import java.net.URI;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SVGLoader
/*     */   extends DefaultHandler
/*     */ {
/*  55 */   final HashMap<String, Class<?>> nodeClasses = new HashMap<String, Class<?>>();
/*     */   
/*  57 */   final LinkedList<SVGElement> buildStack = new LinkedList<SVGElement>();
/*     */   
/*  59 */   final HashSet<String> ignoreClasses = new HashSet<String>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final SVGLoaderHelper helper;
/*     */ 
/*     */ 
/*     */   
/*     */   final SVGDiagram diagram;
/*     */ 
/*     */ 
/*     */   
/*  72 */   int skipNonSVGTagDepth = 0;
/*  73 */   int indent = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final boolean verbose;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SVGLoader(URI xmlBase, SVGUniverse universe) {
/*  84 */     this(xmlBase, universe, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public SVGLoader(URI xmlBase, SVGUniverse universe, boolean verbose) {
/*  89 */     this.verbose = verbose;
/*     */     
/*  91 */     this.diagram = new SVGDiagram(xmlBase, universe);
/*     */ 
/*     */     
/*  94 */     this.nodeClasses.put("a", A.class);
/*  95 */     this.nodeClasses.put("animate", Animate.class);
/*  96 */     this.nodeClasses.put("animatecolor", AnimateColor.class);
/*  97 */     this.nodeClasses.put("animatemotion", AnimateMotion.class);
/*  98 */     this.nodeClasses.put("animatetransform", AnimateTransform.class);
/*  99 */     this.nodeClasses.put("circle", Circle.class);
/* 100 */     this.nodeClasses.put("clippath", ClipPath.class);
/* 101 */     this.nodeClasses.put("defs", Defs.class);
/* 102 */     this.nodeClasses.put("desc", Desc.class);
/* 103 */     this.nodeClasses.put("ellipse", Ellipse.class);
/* 104 */     this.nodeClasses.put("filter", Filter.class);
/* 105 */     this.nodeClasses.put("font", Font.class);
/* 106 */     this.nodeClasses.put("font-face", FontFace.class);
/* 107 */     this.nodeClasses.put("g", Group.class);
/* 108 */     this.nodeClasses.put("glyph", Glyph.class);
/* 109 */     this.nodeClasses.put("hkern", Hkern.class);
/* 110 */     this.nodeClasses.put("image", ImageSVG.class);
/* 111 */     this.nodeClasses.put("line", Line.class);
/* 112 */     this.nodeClasses.put("lineargradient", LinearGradient.class);
/* 113 */     this.nodeClasses.put("marker", Marker.class);
/* 114 */     this.nodeClasses.put("metadata", Metadata.class);
/* 115 */     this.nodeClasses.put("missing-glyph", MissingGlyph.class);
/* 116 */     this.nodeClasses.put("path", Path.class);
/* 117 */     this.nodeClasses.put("pattern", PatternSVG.class);
/* 118 */     this.nodeClasses.put("polygon", Polygon.class);
/* 119 */     this.nodeClasses.put("polyline", Polyline.class);
/* 120 */     this.nodeClasses.put("radialgradient", RadialGradient.class);
/* 121 */     this.nodeClasses.put("rect", Rect.class);
/* 122 */     this.nodeClasses.put("set", SetSmil.class);
/* 123 */     this.nodeClasses.put("shape", ShapeElement.class);
/* 124 */     this.nodeClasses.put("stop", Stop.class);
/* 125 */     this.nodeClasses.put("style", Style.class);
/* 126 */     this.nodeClasses.put("svg", SVGRoot.class);
/* 127 */     this.nodeClasses.put("symbol", Symbol.class);
/* 128 */     this.nodeClasses.put("text", Text.class);
/* 129 */     this.nodeClasses.put("title", Title.class);
/* 130 */     this.nodeClasses.put("tspan", Tspan.class);
/* 131 */     this.nodeClasses.put("use", Use.class);
/*     */     
/* 133 */     this.ignoreClasses.add("midpointstop");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 138 */     this.helper = new SVGLoaderHelper(xmlBase, universe, this.diagram);
/*     */   }
/*     */ 
/*     */   
/*     */   private String printIndent(int indent, String indentStrn) {
/* 143 */     StringBuffer sb = new StringBuffer();
/* 144 */     for (int i = 0; i < indent; i++)
/*     */     {
/* 146 */       sb.append(indentStrn);
/*     */     }
/* 148 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startDocument() throws SAXException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void endDocument() throws SAXException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startElement(String namespaceURI, String sName, String qName, Attributes attrs) throws SAXException {
/* 168 */     if (this.verbose)
/*     */     {
/* 170 */       System.err.println(printIndent(this.indent, " ") + "Starting parse of tag " + sName + ": " + namespaceURI);
/*     */     }
/* 172 */     this.indent++;
/*     */     
/* 174 */     if (this.skipNonSVGTagDepth != 0 || (!namespaceURI.equals("") && !namespaceURI.equals("http://www.w3.org/2000/svg"))) {
/*     */       
/* 176 */       this.skipNonSVGTagDepth++;
/*     */       
/*     */       return;
/*     */     } 
/* 180 */     sName = sName.toLowerCase();
/*     */ 
/*     */ 
/*     */     
/* 184 */     Object<?> obj = (Object<?>)this.nodeClasses.get(sName);
/* 185 */     if (obj == null) {
/*     */       
/* 187 */       if (!this.ignoreClasses.contains(sName))
/*     */       {
/* 189 */         if (this.verbose)
/*     */         {
/* 191 */           System.err.println("SVGLoader: Could not identify tag '" + sName + "'");
/*     */         }
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 202 */       Class<?> cls = (Class)obj;
/* 203 */       SVGElement svgEle = (SVGElement)cls.newInstance();
/*     */       
/* 205 */       SVGElement parent = null;
/* 206 */       if (this.buildStack.size() != 0) parent = this.buildStack.getLast(); 
/* 207 */       svgEle.loaderStartElement(this.helper, attrs, parent);
/*     */       
/* 209 */       this.buildStack.addLast(svgEle);
/*     */     }
/* 211 */     catch (Exception e) {
/*     */       
/* 213 */       Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, "Could not load", e);
/*     */       
/* 215 */       throw new SAXException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void endElement(String namespaceURI, String sName, String qName) throws SAXException {
/* 224 */     this.indent--;
/* 225 */     if (this.verbose)
/*     */     {
/* 227 */       System.err.println(printIndent(this.indent, " ") + "Ending parse of tag " + sName + ": " + namespaceURI);
/*     */     }
/*     */     
/* 230 */     if (this.skipNonSVGTagDepth != 0) {
/*     */       
/* 232 */       this.skipNonSVGTagDepth--;
/*     */       
/*     */       return;
/*     */     } 
/* 236 */     sName = sName.toLowerCase();
/*     */     
/* 238 */     Object<?> obj = (Object<?>)this.nodeClasses.get(sName);
/* 239 */     if (obj == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 246 */       SVGElement svgEle = this.buildStack.removeLast();
/*     */       
/* 248 */       svgEle.loaderEndElement(this.helper);
/*     */       
/* 250 */       SVGElement parent = null;
/* 251 */       if (this.buildStack.size() != 0)
/*     */       {
/* 253 */         parent = this.buildStack.getLast();
/*     */       }
/*     */ 
/*     */       
/* 257 */       if (parent != null)
/*     */       {
/* 259 */         parent.loaderAddChild(this.helper, svgEle);
/*     */       }
/*     */       else
/*     */       {
/* 263 */         this.diagram.setRoot((SVGRoot)svgEle);
/*     */       }
/*     */     
/*     */     }
/* 267 */     catch (Exception e) {
/*     */       
/* 269 */       Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, "Could not parse", e);
/*     */       
/* 271 */       throw new SAXException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void characters(char[] buf, int offset, int len) throws SAXException {
/* 279 */     if (this.skipNonSVGTagDepth != 0) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 284 */     if (this.buildStack.size() != 0) {
/*     */       
/* 286 */       SVGElement parent = this.buildStack.getLast();
/* 287 */       String s = new String(buf, offset, len);
/* 288 */       parent.loaderAddText(this.helper, s);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void processingInstruction(String target, String data) throws SAXException {}
/*     */ 
/*     */ 
/*     */   
/*     */   public SVGDiagram getLoadedDiagram() {
/* 300 */     return this.diagram;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\SVGLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */