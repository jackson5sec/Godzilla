/*     */ package com.kitfox.svg.animation;
/*     */ 
/*     */ import com.kitfox.svg.SVGElement;
/*     */ import com.kitfox.svg.SVGException;
/*     */ import com.kitfox.svg.SVGLoaderHelper;
/*     */ import com.kitfox.svg.animation.parser.AnimTimeParser;
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ import com.kitfox.svg.xml.XMLParseUtil;
/*     */ import java.awt.geom.AffineTransform;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnimateTransform
/*     */   extends AnimateXform
/*     */ {
/*     */   public static final String TAG_NAME = "animateTransform";
/*     */   private double[][] values;
/*     */   private double[] keyTimes;
/*     */   public static final int AT_REPLACE = 0;
/*     */   public static final int AT_SUM = 1;
/*  69 */   private int additive = 0;
/*     */   
/*     */   public static final int TR_TRANSLATE = 0;
/*     */   
/*     */   public static final int TR_ROTATE = 1;
/*     */   public static final int TR_SCALE = 2;
/*     */   public static final int TR_SKEWY = 3;
/*     */   public static final int TR_SKEWX = 4;
/*     */   public static final int TR_INVALID = 5;
/*  78 */   private int xformType = 5;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTagName() {
/*  88 */     return "animateTransform";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loaderStartElement(SVGLoaderHelper helper, Attributes attrs, SVGElement parent) throws SAXException {
/*  95 */     super.loaderStartElement(helper, attrs, parent);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 101 */     String type = attrs.getValue("type").toLowerCase();
/* 102 */     if (type.equals("translate")) this.xformType = 0; 
/* 103 */     if (type.equals("rotate")) this.xformType = 1; 
/* 104 */     if (type.equals("scale")) this.xformType = 2; 
/* 105 */     if (type.equals("skewx")) this.xformType = 4; 
/* 106 */     if (type.equals("skewy")) this.xformType = 3;
/*     */     
/* 108 */     String fromStrn = attrs.getValue("from");
/* 109 */     String toStrn = attrs.getValue("to");
/* 110 */     if (fromStrn != null && toStrn != null) {
/*     */ 
/*     */       
/* 113 */       double[] fromValue = XMLParseUtil.parseDoubleList(fromStrn);
/* 114 */       fromValue = validate(fromValue);
/*     */ 
/*     */       
/* 117 */       double[] toValue = XMLParseUtil.parseDoubleList(toStrn);
/* 118 */       toValue = validate(toValue);
/*     */       
/* 120 */       this.values = new double[][] { fromValue, toValue };
/* 121 */       this.keyTimes = new double[] { 0.0D, 1.0D };
/*     */     } 
/*     */     
/* 124 */     String keyTimeStrn = attrs.getValue("keyTimes");
/* 125 */     String valuesStrn = attrs.getValue("values");
/* 126 */     if (keyTimeStrn != null && valuesStrn != null) {
/*     */       
/* 128 */       this.keyTimes = XMLParseUtil.parseDoubleList(keyTimeStrn);
/*     */       
/* 130 */       String[] valueList = Pattern.compile(";").split(valuesStrn);
/* 131 */       this.values = new double[valueList.length][];
/* 132 */       for (int i = 0; i < valueList.length; i++) {
/*     */         
/* 134 */         double[] list = XMLParseUtil.parseDoubleList(valueList[i]);
/* 135 */         this.values[i] = validate(list);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 140 */     String additive = attrs.getValue("additive");
/* 141 */     if (additive != null)
/*     */     {
/* 143 */       if (additive.equals("sum")) this.additive = 1;
/*     */     
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private double[] validate(double[] paramList) {
/* 153 */     switch (this.xformType) {
/*     */ 
/*     */       
/*     */       case 2:
/* 157 */         if (paramList == null) {
/*     */           
/* 159 */           paramList = new double[] { 1.0D, 1.0D }; break;
/*     */         } 
/* 161 */         if (paramList.length == 1)
/*     */         {
/* 163 */           paramList = new double[] { paramList[0], paramList[0] };
/*     */         }
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 172 */     return paramList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AffineTransform eval(AffineTransform xform, double interp) {
/*     */     double d1, x1, x0, x, y, d3, y1, d2, d4, x2, y0, d6, y2, d5, d8, theta, d7, d11, d10, d9, d12;
/* 182 */     int idx = 0;
/* 183 */     for (; idx < this.keyTimes.length - 1; idx++) {
/*     */       
/* 185 */       if (interp >= this.keyTimes[idx]) {
/*     */         
/* 187 */         idx--;
/* 188 */         if (idx < 0) idx = 0;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 193 */     double spanStartTime = this.keyTimes[idx];
/* 194 */     double spanEndTime = this.keyTimes[idx + 1];
/*     */ 
/*     */     
/* 197 */     interp = (interp - spanStartTime) / (spanEndTime - spanStartTime);
/* 198 */     double[] fromValue = this.values[idx];
/* 199 */     double[] toValue = this.values[idx + 1];
/*     */     
/* 201 */     switch (this.xformType)
/*     */     
/*     */     { 
/*     */       case 0:
/* 205 */         d1 = (fromValue.length >= 1) ? fromValue[0] : 0.0D;
/* 206 */         d3 = (toValue.length >= 1) ? toValue[0] : 0.0D;
/* 207 */         d4 = (fromValue.length >= 2) ? fromValue[1] : 0.0D;
/* 208 */         d6 = (toValue.length >= 2) ? toValue[1] : 0.0D;
/*     */         
/* 210 */         d8 = lerp(d1, d3, interp);
/* 211 */         d11 = lerp(d4, d6, interp);
/*     */         
/* 213 */         xform.setToTranslation(d8, d11);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 258 */         return xform;case 1: x1 = (fromValue.length == 3) ? fromValue[1] : 0.0D; y1 = (fromValue.length == 3) ? fromValue[2] : 0.0D; x2 = (toValue.length == 3) ? toValue[1] : 0.0D; y2 = (toValue.length == 3) ? toValue[2] : 0.0D; theta = lerp(fromValue[0], toValue[0], interp); d10 = lerp(x1, x2, interp); d12 = lerp(y1, y2, interp); xform.setToRotation(Math.toRadians(theta), d10, d12); return xform;case 2: x0 = (fromValue.length >= 1) ? fromValue[0] : 1.0D; d2 = (toValue.length >= 1) ? toValue[0] : 1.0D; y0 = (fromValue.length >= 2) ? fromValue[1] : 1.0D; d5 = (toValue.length >= 2) ? toValue[1] : 1.0D; d7 = lerp(x0, d2, interp); d9 = lerp(y0, d5, interp); xform.setToScale(d7, d9); return xform;case 4: x = lerp(fromValue[0], toValue[0], interp); xform.setToShear(Math.toRadians(x), 0.0D); return xform;case 3: y = lerp(fromValue[0], toValue[0], interp); xform.setToShear(0.0D, Math.toRadians(y)); return xform; }  xform.setToIdentity(); return xform;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void rebuild(AnimTimeParser animTimeParser) throws SVGException {
/* 264 */     super.rebuild(animTimeParser);
/*     */     
/* 266 */     StyleAttribute sty = new StyleAttribute();
/*     */     
/* 268 */     if (getPres(sty.setName("type"))) {
/*     */       
/* 270 */       String strn = sty.getStringValue().toLowerCase();
/* 271 */       if (strn.equals("translate")) this.xformType = 0; 
/* 272 */       if (strn.equals("rotate")) this.xformType = 1; 
/* 273 */       if (strn.equals("scale")) this.xformType = 2; 
/* 274 */       if (strn.equals("skewx")) this.xformType = 4; 
/* 275 */       if (strn.equals("skewy")) this.xformType = 3;
/*     */     
/*     */     } 
/* 278 */     String fromStrn = null;
/* 279 */     if (getPres(sty.setName("from")))
/*     */     {
/* 281 */       fromStrn = sty.getStringValue();
/*     */     }
/*     */     
/* 284 */     String toStrn = null;
/* 285 */     if (getPres(sty.setName("to")))
/*     */     {
/* 287 */       toStrn = sty.getStringValue();
/*     */     }
/*     */     
/* 290 */     if (fromStrn != null && toStrn != null) {
/*     */       
/* 292 */       double[] fromValue = XMLParseUtil.parseDoubleList(fromStrn);
/* 293 */       fromValue = validate(fromValue);
/*     */       
/* 295 */       double[] toValue = XMLParseUtil.parseDoubleList(toStrn);
/* 296 */       toValue = validate(toValue);
/*     */       
/* 298 */       this.values = new double[][] { fromValue, toValue };
/*     */     } 
/*     */     
/* 301 */     String keyTimeStrn = null;
/* 302 */     if (getPres(sty.setName("keyTimes")))
/*     */     {
/* 304 */       keyTimeStrn = sty.getStringValue();
/*     */     }
/*     */     
/* 307 */     String valuesStrn = null;
/* 308 */     if (getPres(sty.setName("values")))
/*     */     {
/* 310 */       valuesStrn = sty.getStringValue();
/*     */     }
/*     */     
/* 313 */     if (keyTimeStrn != null && valuesStrn != null) {
/*     */       
/* 315 */       this.keyTimes = XMLParseUtil.parseDoubleList(keyTimeStrn);
/*     */       
/* 317 */       String[] valueList = Pattern.compile(";").split(valuesStrn);
/* 318 */       this.values = new double[valueList.length][];
/* 319 */       for (int i = 0; i < valueList.length; i++) {
/*     */         
/* 321 */         double[] list = XMLParseUtil.parseDoubleList(valueList[i]);
/* 322 */         this.values[i] = validate(list);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 328 */     if (getPres(sty.setName("additive"))) {
/*     */       
/* 330 */       String strn = sty.getStringValue().toLowerCase();
/* 331 */       if (strn.equals("sum")) this.additive = 1;
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double[][] getValues() {
/* 340 */     return this.values;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValues(double[][] values) {
/* 348 */     this.values = values;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double[] getKeyTimes() {
/* 356 */     return this.keyTimes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeyTimes(double[] keyTimes) {
/* 364 */     this.keyTimes = keyTimes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAdditive() {
/* 372 */     return this.additive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAdditive(int additive) {
/* 380 */     this.additive = additive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getXformType() {
/* 388 */     return this.xformType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setXformType(int xformType) {
/* 396 */     this.xformType = xformType;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\animation\AnimateTransform.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */