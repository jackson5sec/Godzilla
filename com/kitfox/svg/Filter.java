/*     */ package com.kitfox.svg;
/*     */ 
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ import java.awt.geom.Point2D;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Filter
/*     */   extends SVGElement
/*     */ {
/*     */   public static final String TAG_NAME = "filter";
/*     */   public static final int FU_OBJECT_BOUNDING_BOX = 0;
/*     */   public static final int FU_USER_SPACE_ON_USE = 1;
/*  54 */   protected int filterUnits = 0;
/*     */   public static final int PU_OBJECT_BOUNDING_BOX = 0;
/*     */   public static final int PU_USER_SPACE_ON_USE = 1;
/*  57 */   protected int primitiveUnits = 0;
/*  58 */   float x = 0.0F;
/*  59 */   float y = 0.0F;
/*  60 */   float width = 1.0F;
/*  61 */   float height = 1.0F;
/*  62 */   Point2D filterRes = new Point2D.Double();
/*  63 */   URL href = null;
/*  64 */   final ArrayList<SVGElement> filterEffects = new ArrayList<SVGElement>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTagName() {
/*  76 */     return "filter";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loaderAddChild(SVGLoaderHelper helper, SVGElement child) throws SVGElementException {
/*  86 */     super.loaderAddChild(helper, child);
/*     */     
/*  88 */     if (child instanceof FilterEffects)
/*     */     {
/*  90 */       this.filterEffects.add(child);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void build() throws SVGException {
/*  97 */     super.build();
/*     */     
/*  99 */     StyleAttribute sty = new StyleAttribute();
/*     */ 
/*     */     
/* 102 */     if (getPres(sty.setName("filterUnits"))) {
/*     */       
/* 104 */       String strn = sty.getStringValue().toLowerCase();
/* 105 */       if (strn.equals("userspaceonuse")) {
/*     */         
/* 107 */         this.filterUnits = 1;
/*     */       } else {
/*     */         
/* 110 */         this.filterUnits = 0;
/*     */       } 
/*     */     } 
/*     */     
/* 114 */     if (getPres(sty.setName("primitiveUnits"))) {
/*     */       
/* 116 */       String strn = sty.getStringValue().toLowerCase();
/* 117 */       if (strn.equals("userspaceonuse")) {
/*     */         
/* 119 */         this.primitiveUnits = 1;
/*     */       } else {
/*     */         
/* 122 */         this.primitiveUnits = 0;
/*     */       } 
/*     */     } 
/*     */     
/* 126 */     if (getPres(sty.setName("x")))
/*     */     {
/* 128 */       this.x = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/* 131 */     if (getPres(sty.setName("y")))
/*     */     {
/* 133 */       this.y = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/* 136 */     if (getPres(sty.setName("width")))
/*     */     {
/* 138 */       this.width = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/* 141 */     if (getPres(sty.setName("height")))
/*     */     {
/* 143 */       this.height = sty.getFloatValueWithUnits();
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 148 */       if (getPres(sty.setName("xlink:href"))) {
/*     */         
/* 150 */         URI src = sty.getURIValue(getXMLBase());
/* 151 */         this.href = src.toURL();
/*     */       } 
/* 153 */     } catch (Exception e) {
/*     */       
/* 155 */       throw new SVGException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getX() {
/* 162 */     return this.x;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getY() {
/* 167 */     return this.y;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getWidth() {
/* 172 */     return this.width;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getHeight() {
/* 177 */     return this.height;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean updateTime(double curTime) throws SVGException {
/* 186 */     StyleAttribute sty = new StyleAttribute();
/* 187 */     boolean stateChange = false;
/*     */     
/* 189 */     if (getPres(sty.setName("x"))) {
/*     */       
/* 191 */       float newVal = sty.getFloatValueWithUnits();
/* 192 */       if (newVal != this.x) {
/*     */         
/* 194 */         this.x = newVal;
/* 195 */         stateChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 199 */     if (getPres(sty.setName("y"))) {
/*     */       
/* 201 */       float newVal = sty.getFloatValueWithUnits();
/* 202 */       if (newVal != this.y) {
/*     */         
/* 204 */         this.y = newVal;
/* 205 */         stateChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 209 */     if (getPres(sty.setName("width"))) {
/*     */       
/* 211 */       float newVal = sty.getFloatValueWithUnits();
/* 212 */       if (newVal != this.width) {
/*     */         
/* 214 */         this.width = newVal;
/* 215 */         stateChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 219 */     if (getPres(sty.setName("height"))) {
/*     */       
/* 221 */       float newVal = sty.getFloatValueWithUnits();
/* 222 */       if (newVal != this.height) {
/*     */         
/* 224 */         this.height = newVal;
/* 225 */         stateChange = true;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 231 */       if (getPres(sty.setName("xlink:href"))) {
/*     */         
/* 233 */         URI src = sty.getURIValue(getXMLBase());
/* 234 */         URL newVal = src.toURL();
/*     */         
/* 236 */         if (!newVal.equals(this.href)) {
/*     */           
/* 238 */           this.href = newVal;
/* 239 */           stateChange = true;
/*     */         } 
/*     */       } 
/* 242 */     } catch (Exception e) {
/*     */       
/* 244 */       throw new SVGException(e);
/*     */     } 
/*     */     
/* 247 */     if (getPres(sty.setName("filterUnits"))) {
/*     */       int newVal;
/*     */       
/* 250 */       String strn = sty.getStringValue().toLowerCase();
/* 251 */       if (strn.equals("userspaceonuse")) {
/*     */         
/* 253 */         newVal = 1;
/*     */       } else {
/*     */         
/* 256 */         newVal = 0;
/*     */       } 
/* 258 */       if (newVal != this.filterUnits) {
/*     */         
/* 260 */         this.filterUnits = newVal;
/* 261 */         stateChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 265 */     if (getPres(sty.setName("primitiveUnits"))) {
/*     */       int newVal;
/*     */       
/* 268 */       String strn = sty.getStringValue().toLowerCase();
/* 269 */       if (strn.equals("userspaceonuse")) {
/*     */         
/* 271 */         newVal = 1;
/*     */       } else {
/*     */         
/* 274 */         newVal = 0;
/*     */       } 
/* 276 */       if (newVal != this.filterUnits) {
/*     */         
/* 278 */         this.primitiveUnits = newVal;
/* 279 */         stateChange = true;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 285 */     return stateChange;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\Filter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */