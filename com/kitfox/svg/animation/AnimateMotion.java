/*     */ package com.kitfox.svg.animation;
/*     */ 
/*     */ import com.kitfox.svg.SVGElement;
/*     */ import com.kitfox.svg.SVGException;
/*     */ import com.kitfox.svg.SVGLoaderHelper;
/*     */ import com.kitfox.svg.animation.parser.AnimTimeParser;
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.GeneralPath;
/*     */ import java.awt.geom.PathIterator;
/*     */ import java.awt.geom.Point2D;
/*     */ import java.util.ArrayList;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnimateMotion
/*     */   extends AnimateXform
/*     */ {
/*     */   public static final String TAG_NAME = "animateMotion";
/*  63 */   static final Matcher matchPoint = Pattern.compile("\\s*(\\d+)[^\\d]+(\\d+)\\s*").matcher("");
/*     */ 
/*     */   
/*     */   private GeneralPath path;
/*     */   
/*  68 */   private int rotateType = 0;
/*     */   
/*     */   private double rotate;
/*     */   
/*     */   public static final int RT_ANGLE = 0;
/*     */   public static final int RT_AUTO = 1;
/*  74 */   final ArrayList<Bezier> bezierSegs = new ArrayList<Bezier>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   double curveLength;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTagName() {
/*  85 */     return "animateMotion";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loaderStartElement(SVGLoaderHelper helper, Attributes attrs, SVGElement parent) throws SAXException {
/*  92 */     super.loaderStartElement(helper, attrs, parent);
/*     */ 
/*     */     
/*  95 */     if (this.attribName == null) {
/*     */       
/*  97 */       this.attribName = "transform";
/*  98 */       this.attribType = 2;
/*  99 */       setAdditiveType(1);
/*     */     } 
/*     */ 
/*     */     
/* 103 */     String path = attrs.getValue("path");
/* 104 */     if (path != null)
/*     */     {
/* 106 */       this.path = buildPath(path, 1);
/*     */     }
/*     */ 
/*     */     
/* 110 */     String rotate = attrs.getValue("rotate");
/* 111 */     if (rotate != null)
/*     */     {
/* 113 */       if (rotate.equals("auto")) {
/*     */         
/* 115 */         this.rotateType = 1;
/*     */       } else {
/*     */ 
/*     */         
/* 119 */         try { this.rotate = Math.toRadians(Float.parseFloat(rotate)); } catch (Exception exception) {}
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 124 */     String from = attrs.getValue("from");
/* 125 */     String to = attrs.getValue("to");
/*     */     
/* 127 */     buildPath(from, to);
/*     */   }
/*     */   
/*     */   protected static void setPoint(Point2D.Float pt, String x, String y) {
/*     */     
/* 132 */     try { pt.x = Float.parseFloat(x); } catch (Exception exception) {}
/*     */     
/* 134 */     try { pt.y = Float.parseFloat(y); } catch (Exception exception) {}
/*     */   }
/*     */ 
/*     */   
/*     */   private void buildPath(String from, String to) {
/* 139 */     if (from != null && to != null) {
/*     */       
/* 141 */       Point2D.Float ptFrom = new Point2D.Float(), ptTo = new Point2D.Float();
/*     */       
/* 143 */       matchPoint.reset(from);
/* 144 */       if (matchPoint.matches())
/*     */       {
/* 146 */         setPoint(ptFrom, matchPoint.group(1), matchPoint.group(2));
/*     */       }
/*     */       
/* 149 */       matchPoint.reset(to);
/* 150 */       if (matchPoint.matches())
/*     */       {
/* 152 */         setPoint(ptFrom, matchPoint.group(1), matchPoint.group(2));
/*     */       }
/*     */       
/* 155 */       if (ptFrom != null && ptTo != null) {
/*     */         
/* 157 */         this.path = new GeneralPath();
/* 158 */         this.path.moveTo(ptFrom.x, ptFrom.y);
/* 159 */         this.path.lineTo(ptTo.x, ptTo.y);
/*     */       } 
/*     */     } 
/*     */     
/* 163 */     paramaterizePath();
/*     */   }
/*     */ 
/*     */   
/*     */   private void paramaterizePath() {
/* 168 */     this.bezierSegs.clear();
/* 169 */     this.curveLength = 0.0D;
/*     */     
/* 171 */     double[] coords = new double[6];
/* 172 */     double sx = 0.0D, sy = 0.0D;
/*     */     
/* 174 */     for (PathIterator pathIt = this.path.getPathIterator(new AffineTransform()); !pathIt.isDone(); pathIt.next()) {
/*     */       
/* 176 */       Bezier bezier = null;
/*     */       
/* 178 */       int segType = pathIt.currentSegment(coords);
/*     */       
/* 180 */       switch (segType) {
/*     */ 
/*     */         
/*     */         case 1:
/* 184 */           bezier = new Bezier(sx, sy, coords, 1);
/* 185 */           sx = coords[0];
/* 186 */           sy = coords[1];
/*     */           break;
/*     */ 
/*     */         
/*     */         case 2:
/* 191 */           bezier = new Bezier(sx, sy, coords, 2);
/* 192 */           sx = coords[2];
/* 193 */           sy = coords[3];
/*     */           break;
/*     */ 
/*     */         
/*     */         case 3:
/* 198 */           bezier = new Bezier(sx, sy, coords, 3);
/* 199 */           sx = coords[4];
/* 200 */           sy = coords[5];
/*     */           break;
/*     */ 
/*     */         
/*     */         case 0:
/* 205 */           sx = coords[0];
/* 206 */           sy = coords[1];
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 215 */       if (bezier != null) {
/*     */         
/* 217 */         this.bezierSegs.add(bezier);
/* 218 */         this.curveLength += bezier.getLength();
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
/*     */   public AffineTransform eval(AffineTransform xform, double interp) {
/* 230 */     Point2D.Double point = new Point2D.Double();
/*     */     
/* 232 */     if (interp >= 1.0D) {
/*     */       
/* 234 */       Bezier last = this.bezierSegs.get(this.bezierSegs.size() - 1);
/* 235 */       last.getFinalPoint(point);
/* 236 */       xform.setToTranslation(point.x, point.y);
/* 237 */       return xform;
/*     */     } 
/*     */     
/* 240 */     double curLength = this.curveLength * interp;
/* 241 */     for (Bezier bez : this.bezierSegs) {
/* 242 */       double bezLength = bez.getLength();
/* 243 */       if (curLength < bezLength) {
/*     */         
/* 245 */         double param = curLength / bezLength;
/* 246 */         bez.eval(param, point);
/*     */         
/*     */         break;
/*     */       } 
/* 250 */       curLength -= bezLength;
/*     */     } 
/*     */     
/* 253 */     xform.setToTranslation(point.x, point.y);
/*     */     
/* 255 */     return xform;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void rebuild(AnimTimeParser animTimeParser) throws SVGException {
/* 262 */     super.rebuild(animTimeParser);
/*     */     
/* 264 */     StyleAttribute sty = new StyleAttribute();
/*     */     
/* 266 */     if (getPres(sty.setName("path"))) {
/*     */       
/* 268 */       String strn = sty.getStringValue();
/* 269 */       this.path = buildPath(strn, 1);
/*     */     } 
/*     */     
/* 272 */     if (getPres(sty.setName("rotate"))) {
/*     */       
/* 274 */       String strn = sty.getStringValue();
/* 275 */       if (strn.equals("auto")) {
/*     */         
/* 277 */         this.rotateType = 1;
/*     */       } else {
/*     */ 
/*     */         
/* 281 */         try { this.rotate = Math.toRadians(Float.parseFloat(strn)); } catch (Exception exception) {}
/*     */       } 
/*     */     } 
/*     */     
/* 285 */     String from = null;
/* 286 */     if (getPres(sty.setName("from")))
/*     */     {
/* 288 */       from = sty.getStringValue();
/*     */     }
/*     */     
/* 291 */     String to = null;
/* 292 */     if (getPres(sty.setName("to")))
/*     */     {
/* 294 */       to = sty.getStringValue();
/*     */     }
/*     */     
/* 297 */     buildPath(from, to);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GeneralPath getPath() {
/* 305 */     return this.path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPath(GeneralPath path) {
/* 313 */     this.path = path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRotateType() {
/* 321 */     return this.rotateType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRotateType(int rotateType) {
/* 329 */     this.rotateType = rotateType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getRotate() {
/* 337 */     return this.rotate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRotate(double rotate) {
/* 345 */     this.rotate = rotate;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\animation\AnimateMotion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */