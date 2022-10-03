/*     */ package com.kitfox.svg.animation;
/*     */ 
/*     */ import com.kitfox.svg.SVGElement;
/*     */ import com.kitfox.svg.SVGException;
/*     */ import com.kitfox.svg.SVGLoaderHelper;
/*     */ import com.kitfox.svg.animation.parser.AnimTimeParser;
/*     */ import com.kitfox.svg.xml.ColorTable;
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ import com.kitfox.svg.xml.XMLParseUtil;
/*     */ import java.awt.Color;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.GeneralPath;
/*     */ import java.awt.geom.PathIterator;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Animate
/*     */   extends AnimateBase
/*     */   implements AnimateColorIface
/*     */ {
/*     */   public static final String TAG_NAME = "animate";
/*     */   public static final int DT_REAL = 0;
/*     */   public static final int DT_COLOR = 1;
/*     */   public static final int DT_PATH = 2;
/*  72 */   int dataType = 0;
/*     */   
/*  74 */   private double fromValue = Double.NaN;
/*  75 */   private double toValue = Double.NaN;
/*  76 */   private double byValue = Double.NaN;
/*     */   
/*     */   private double[] valuesValue;
/*  79 */   private Color fromColor = null;
/*  80 */   private Color toColor = null;
/*     */   
/*  82 */   private GeneralPath fromPath = null;
/*  83 */   private GeneralPath toPath = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTagName() {
/*  93 */     return "animate";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDataType() {
/*  98 */     return this.dataType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loaderStartElement(SVGLoaderHelper helper, Attributes attrs, SVGElement parent) throws SAXException {
/* 105 */     super.loaderStartElement(helper, attrs, parent);
/*     */     
/* 107 */     String strn = attrs.getValue("from");
/* 108 */     if (strn != null)
/*     */     {
/* 110 */       if (XMLParseUtil.isDouble(strn)) {
/*     */         
/* 112 */         this.fromValue = XMLParseUtil.parseDouble(strn);
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */ 
/*     */         
/* 121 */         this.fromColor = ColorTable.parseColor(strn);
/* 122 */         if (this.fromColor == null) {
/*     */ 
/*     */           
/* 125 */           this; this.fromPath = buildPath(strn, 0);
/* 126 */           this.dataType = 2;
/*     */         } else {
/* 128 */           this.dataType = 1;
/*     */         } 
/*     */       } 
/*     */     }
/* 132 */     strn = attrs.getValue("to");
/* 133 */     if (strn != null)
/*     */     {
/* 135 */       if (XMLParseUtil.isDouble(strn)) {
/*     */         
/* 137 */         this.toValue = XMLParseUtil.parseDouble(strn);
/*     */       }
/*     */       else {
/*     */         
/* 141 */         this.toColor = ColorTable.parseColor(strn);
/* 142 */         if (this.toColor == null) {
/*     */ 
/*     */           
/* 145 */           this; this.toPath = buildPath(strn, 0);
/* 146 */           this.dataType = 2;
/*     */         } else {
/* 148 */           this.dataType = 1;
/*     */         } 
/*     */       } 
/*     */     }
/* 152 */     strn = attrs.getValue("by");
/*     */     
/*     */     try {
/* 155 */       if (strn != null) this.byValue = XMLParseUtil.parseDouble(strn); 
/* 156 */     } catch (Exception exception) {}
/*     */     
/* 158 */     strn = attrs.getValue("values");
/*     */     
/*     */     try {
/* 161 */       if (strn != null) this.valuesValue = XMLParseUtil.parseDoubleList(strn); 
/* 162 */     } catch (Exception exception) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double eval(double interp) {
/* 171 */     boolean fromExists = !Double.isNaN(this.fromValue);
/* 172 */     boolean toExists = !Double.isNaN(this.toValue);
/* 173 */     boolean byExists = !Double.isNaN(this.byValue);
/* 174 */     boolean valuesExists = (this.valuesValue != null);
/*     */     
/* 176 */     if (valuesExists) {
/*     */       
/* 178 */       double sp = interp * this.valuesValue.length;
/* 179 */       int ip = (int)sp;
/* 180 */       double fp = sp - ip;
/*     */       
/* 182 */       int i0 = ip;
/* 183 */       int i1 = ip + 1;
/*     */       
/* 185 */       if (i0 < 0) return this.valuesValue[0]; 
/* 186 */       if (i1 >= this.valuesValue.length) return this.valuesValue[this.valuesValue.length - 1]; 
/* 187 */       return this.valuesValue[i0] * (1.0D - fp) + this.valuesValue[i1] * fp;
/*     */     } 
/* 189 */     if (fromExists && toExists)
/*     */     {
/* 191 */       return this.toValue * interp + this.fromValue * (1.0D - interp);
/*     */     }
/* 193 */     if (fromExists && byExists)
/*     */     {
/* 195 */       return this.fromValue + this.byValue * interp;
/*     */     }
/* 197 */     if (toExists && byExists)
/*     */     {
/* 199 */       return this.toValue - this.byValue * (1.0D - interp);
/*     */     }
/* 201 */     if (byExists)
/*     */     {
/* 203 */       return this.byValue * interp;
/*     */     }
/* 205 */     if (toExists) {
/*     */       
/* 207 */       StyleAttribute style = new StyleAttribute(getAttribName());
/*     */       
/*     */       try {
/* 210 */         getParent().getStyle(style, true, false);
/*     */       }
/* 212 */       catch (SVGException ex) {
/*     */         
/* 214 */         Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, "Could not get from value", (Throwable)ex);
/*     */       } 
/*     */       
/* 217 */       double from = style.getDoubleValue();
/* 218 */       return this.toValue * interp + from * (1.0D - interp);
/*     */     } 
/*     */ 
/*     */     
/* 222 */     throw new RuntimeException("Animate tag could not be evalutated - insufficient arguements");
/*     */   }
/*     */ 
/*     */   
/*     */   public Color evalColor(double interp) {
/* 227 */     if (this.fromColor == null && this.toColor != null) {
/*     */       
/* 229 */       float[] toCol = new float[3];
/* 230 */       this.toColor.getColorComponents(toCol);
/* 231 */       return new Color(toCol[0] * (float)interp, toCol[1] * (float)interp, toCol[2] * (float)interp);
/*     */     } 
/*     */ 
/*     */     
/* 235 */     if (this.fromColor != null && this.toColor != null) {
/*     */       
/* 237 */       float nInterp = 1.0F - (float)interp;
/*     */       
/* 239 */       float[] fromCol = new float[3];
/* 240 */       float[] toCol = new float[3];
/* 241 */       this.fromColor.getColorComponents(fromCol);
/* 242 */       this.toColor.getColorComponents(toCol);
/* 243 */       return new Color(fromCol[0] * nInterp + toCol[0] * (float)interp, fromCol[1] * nInterp + toCol[1] * (float)interp, fromCol[2] * nInterp + toCol[2] * (float)interp);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 248 */     throw new RuntimeException("Animate tag could not be evalutated - insufficient arguements");
/*     */   }
/*     */ 
/*     */   
/*     */   public GeneralPath evalPath(double interp) {
/* 253 */     if (this.fromPath == null && this.toPath != null) {
/*     */       
/* 255 */       PathIterator itTo = this.toPath.getPathIterator(new AffineTransform());
/*     */       
/* 257 */       GeneralPath midPath = new GeneralPath();
/* 258 */       float[] coordsTo = new float[6];
/*     */       
/* 260 */       for (; !itTo.isDone(); itTo.next()) {
/*     */         
/* 262 */         int segTo = itTo.currentSegment(coordsTo);
/*     */         
/* 264 */         switch (segTo) {
/*     */           
/*     */           case 4:
/* 267 */             midPath.closePath();
/*     */             break;
/*     */           case 3:
/* 270 */             midPath.curveTo((float)(coordsTo[0] * interp), (float)(coordsTo[1] * interp), (float)(coordsTo[2] * interp), (float)(coordsTo[3] * interp), (float)(coordsTo[4] * interp), (float)(coordsTo[5] * interp));
/*     */             break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           case 1:
/* 280 */             midPath.lineTo((float)(coordsTo[0] * interp), (float)(coordsTo[1] * interp));
/*     */             break;
/*     */ 
/*     */ 
/*     */           
/*     */           case 0:
/* 286 */             midPath.moveTo((float)(coordsTo[0] * interp), (float)(coordsTo[1] * interp));
/*     */             break;
/*     */ 
/*     */ 
/*     */           
/*     */           case 2:
/* 292 */             midPath.quadTo((float)(coordsTo[0] * interp), (float)(coordsTo[1] * interp), (float)(coordsTo[2] * interp), (float)(coordsTo[3] * interp));
/*     */             break;
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       } 
/* 302 */       return midPath;
/*     */     } 
/* 304 */     if (this.toPath != null) {
/*     */       
/* 306 */       PathIterator itFrom = this.fromPath.getPathIterator(new AffineTransform());
/* 307 */       PathIterator itTo = this.toPath.getPathIterator(new AffineTransform());
/*     */       
/* 309 */       GeneralPath midPath = new GeneralPath();
/* 310 */       float[] coordsFrom = new float[6];
/* 311 */       float[] coordsTo = new float[6];
/*     */       
/* 313 */       for (; !itFrom.isDone(); itFrom.next(), itTo.next()) {
/*     */         
/* 315 */         int segFrom = itFrom.currentSegment(coordsFrom);
/* 316 */         int segTo = itTo.currentSegment(coordsTo);
/*     */         
/* 318 */         if (segFrom != segTo)
/*     */         {
/* 320 */           throw new RuntimeException("Path shape mismatch");
/*     */         }
/*     */         
/* 323 */         switch (segFrom) {
/*     */           
/*     */           case 4:
/* 326 */             midPath.closePath();
/*     */             break;
/*     */           case 3:
/* 329 */             midPath.curveTo((float)(coordsFrom[0] * (1.0D - interp) + coordsTo[0] * interp), (float)(coordsFrom[1] * (1.0D - interp) + coordsTo[1] * interp), (float)(coordsFrom[2] * (1.0D - interp) + coordsTo[2] * interp), (float)(coordsFrom[3] * (1.0D - interp) + coordsTo[3] * interp), (float)(coordsFrom[4] * (1.0D - interp) + coordsTo[4] * interp), (float)(coordsFrom[5] * (1.0D - interp) + coordsTo[5] * interp));
/*     */             break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           case 1:
/* 339 */             midPath.lineTo((float)(coordsFrom[0] * (1.0D - interp) + coordsTo[0] * interp), (float)(coordsFrom[1] * (1.0D - interp) + coordsTo[1] * interp));
/*     */             break;
/*     */ 
/*     */ 
/*     */           
/*     */           case 0:
/* 345 */             midPath.moveTo((float)(coordsFrom[0] * (1.0D - interp) + coordsTo[0] * interp), (float)(coordsFrom[1] * (1.0D - interp) + coordsTo[1] * interp));
/*     */             break;
/*     */ 
/*     */ 
/*     */           
/*     */           case 2:
/* 351 */             midPath.quadTo((float)(coordsFrom[0] * (1.0D - interp) + coordsTo[0] * interp), (float)(coordsFrom[1] * (1.0D - interp) + coordsTo[1] * interp), (float)(coordsFrom[2] * (1.0D - interp) + coordsTo[2] * interp), (float)(coordsFrom[3] * (1.0D - interp) + coordsTo[3] * interp));
/*     */             break;
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       } 
/* 361 */       return midPath;
/*     */     } 
/*     */     
/* 364 */     throw new RuntimeException("Animate tag could not be evalutated - insufficient arguements");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double repeatSkipSize(int reps) {
/* 372 */     boolean fromExists = !Double.isNaN(this.fromValue);
/* 373 */     boolean toExists = !Double.isNaN(this.toValue);
/* 374 */     boolean byExists = !Double.isNaN(this.byValue);
/*     */     
/* 376 */     if (fromExists && toExists)
/*     */     {
/* 378 */       return (this.toValue - this.fromValue) * reps;
/*     */     }
/* 380 */     if (fromExists && byExists)
/*     */     {
/* 382 */       return (this.fromValue + this.byValue) * reps;
/*     */     }
/* 384 */     if (toExists && byExists)
/*     */     {
/* 386 */       return this.toValue * reps;
/*     */     }
/* 388 */     if (byExists)
/*     */     {
/* 390 */       return this.byValue * reps;
/*     */     }
/*     */ 
/*     */     
/* 394 */     return 0.0D;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void rebuild(AnimTimeParser animTimeParser) throws SVGException {
/* 400 */     super.rebuild(animTimeParser);
/*     */     
/* 402 */     StyleAttribute sty = new StyleAttribute();
/*     */     
/* 404 */     if (getPres(sty.setName("from"))) {
/*     */       
/* 406 */       String strn = sty.getStringValue();
/* 407 */       if (XMLParseUtil.isDouble(strn)) {
/*     */         
/* 409 */         this.fromValue = XMLParseUtil.parseDouble(strn);
/*     */       }
/*     */       else {
/*     */         
/* 413 */         this.fromColor = ColorTable.parseColor(strn);
/* 414 */         if (this.fromColor == null) {
/*     */ 
/*     */           
/* 417 */           this; this.fromPath = buildPath(strn, 0);
/* 418 */           this.dataType = 2;
/*     */         } else {
/* 420 */           this.dataType = 1;
/*     */         } 
/*     */       } 
/*     */     } 
/* 424 */     if (getPres(sty.setName("to"))) {
/*     */       
/* 426 */       String strn = sty.getStringValue();
/* 427 */       if (XMLParseUtil.isDouble(strn)) {
/*     */         
/* 429 */         this.toValue = XMLParseUtil.parseDouble(strn);
/*     */       }
/*     */       else {
/*     */         
/* 433 */         this.toColor = ColorTable.parseColor(strn);
/* 434 */         if (this.toColor == null) {
/*     */ 
/*     */           
/* 437 */           this; this.toPath = buildPath(strn, 0);
/* 438 */           this.dataType = 2;
/*     */         } else {
/* 440 */           this.dataType = 1;
/*     */         } 
/*     */       } 
/*     */     } 
/* 444 */     if (getPres(sty.setName("by"))) {
/*     */       
/* 446 */       String strn = sty.getStringValue();
/* 447 */       if (strn != null) this.byValue = XMLParseUtil.parseDouble(strn);
/*     */     
/*     */     } 
/* 450 */     if (getPres(sty.setName("values"))) {
/*     */       
/* 452 */       String strn = sty.getStringValue();
/* 453 */       if (strn != null) this.valuesValue = XMLParseUtil.parseDoubleList(strn);
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getFromValue() {
/* 462 */     return this.fromValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFromValue(double fromValue) {
/* 470 */     this.fromValue = fromValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getToValue() {
/* 478 */     return this.toValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setToValue(double toValue) {
/* 486 */     this.toValue = toValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getByValue() {
/* 494 */     return this.byValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setByValue(double byValue) {
/* 502 */     this.byValue = byValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double[] getValuesValue() {
/* 510 */     return this.valuesValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValuesValue(double[] valuesValue) {
/* 518 */     this.valuesValue = valuesValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Color getFromColor() {
/* 526 */     return this.fromColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFromColor(Color fromColor) {
/* 534 */     this.fromColor = fromColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Color getToColor() {
/* 542 */     return this.toColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setToColor(Color toColor) {
/* 550 */     this.toColor = toColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GeneralPath getFromPath() {
/* 558 */     return this.fromPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFromPath(GeneralPath fromPath) {
/* 566 */     this.fromPath = fromPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GeneralPath getToPath() {
/* 574 */     return this.toPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setToPath(GeneralPath toPath) {
/* 582 */     this.toPath = toPath;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\animation\Animate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */