/*     */ package com.kitfox.svg;
/*     */ 
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ import java.awt.Color;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
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
/*     */ public abstract class Gradient
/*     */   extends FillElement
/*     */ {
/*     */   public static final String TAG_NAME = "gradient";
/*     */   public static final int SM_PAD = 0;
/*     */   public static final int SM_REPEAT = 1;
/*     */   public static final int SM_REFLECT = 2;
/*  59 */   int spreadMethod = 0;
/*     */   public static final int GU_OBJECT_BOUNDING_BOX = 0;
/*     */   public static final int GU_USER_SPACE_ON_USE = 1;
/*  62 */   protected int gradientUnits = 0;
/*     */ 
/*     */   
/*  65 */   ArrayList<Stop> stops = new ArrayList<Stop>();
/*  66 */   URI stopRef = null;
/*  67 */   protected AffineTransform gradientTransform = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   float[] stopFractions;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Color[] stopColors;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTagName() {
/*  83 */     return "gradient";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loaderAddChild(SVGLoaderHelper helper, SVGElement child) throws SVGElementException {
/*  93 */     super.loaderAddChild(helper, child);
/*     */     
/*  95 */     if (!(child instanceof Stop)) {
/*     */       return;
/*     */     }
/*     */     
/*  99 */     appendStop((Stop)child);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void build() throws SVGException {
/* 105 */     super.build();
/*     */     
/* 107 */     StyleAttribute sty = new StyleAttribute();
/*     */ 
/*     */     
/* 110 */     if (getPres(sty.setName("spreadMethod"))) {
/*     */       
/* 112 */       String strn = sty.getStringValue().toLowerCase();
/* 113 */       if (strn.equals("repeat")) {
/*     */         
/* 115 */         this.spreadMethod = 1;
/* 116 */       } else if (strn.equals("reflect")) {
/*     */         
/* 118 */         this.spreadMethod = 2;
/*     */       } else {
/*     */         
/* 121 */         this.spreadMethod = 0;
/*     */       } 
/*     */     } 
/*     */     
/* 125 */     if (getPres(sty.setName("gradientUnits"))) {
/*     */       
/* 127 */       String strn = sty.getStringValue().toLowerCase();
/* 128 */       if (strn.equals("userspaceonuse")) {
/*     */         
/* 130 */         this.gradientUnits = 1;
/*     */       } else {
/*     */         
/* 133 */         this.gradientUnits = 0;
/*     */       } 
/*     */     } 
/*     */     
/* 137 */     if (getPres(sty.setName("gradientTransform")))
/*     */     {
/* 139 */       this.gradientTransform = parseTransform(sty.getStringValue());
/*     */     }
/*     */     
/* 142 */     if (this.gradientTransform == null)
/*     */     {
/* 144 */       this.gradientTransform = new AffineTransform();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 149 */     if (getPres(sty.setName("xlink:href"))) {
/*     */       
/*     */       try {
/*     */         
/* 153 */         this.stopRef = sty.getURIValue(getXMLBase());
/*     */ 
/*     */       
/*     */       }
/* 157 */       catch (Exception e) {
/*     */         
/* 159 */         throw new SVGException("Could not resolve relative URL in Gradient: " + sty.getStringValue() + ", " + getXMLBase(), e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void buildStops() {
/* 166 */     ArrayList<Stop> stopList = new ArrayList<Stop>(this.stops);
/* 167 */     stopList.sort(new Comparator<Stop>()
/*     */         {
/*     */           public int compare(Stop o1, Stop o2) {
/* 170 */             return Float.compare(o1.offset, o2.offset);
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 175 */     for (int i = stopList.size() - 2; i > 0; i--) {
/*     */       
/* 177 */       if (((Stop)stopList.get(i + 1)).offset == ((Stop)stopList.get(i)).offset)
/*     */       {
/* 179 */         stopList.remove(i + 1);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 184 */     this.stopFractions = new float[stopList.size()];
/* 185 */     this.stopColors = new Color[stopList.size()];
/* 186 */     int idx = 0;
/* 187 */     for (Stop stop : stopList) {
/*     */       
/* 189 */       int stopColorVal = stop.color.getRGB();
/* 190 */       Color stopColor = new Color(stopColorVal >> 16 & 0xFF, stopColorVal >> 8 & 0xFF, stopColorVal & 0xFF, clamp((int)(stop.opacity * 255.0F), 0, 255));
/*     */       
/* 192 */       this.stopColors[idx] = stopColor;
/* 193 */       this.stopFractions[idx] = stop.offset;
/* 194 */       idx++;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float[] getStopFractions() {
/* 201 */     if (this.stopRef != null) {
/*     */       
/* 203 */       Gradient grad = (Gradient)this.diagram.getUniverse().getElement(this.stopRef);
/* 204 */       return grad.getStopFractions();
/*     */     } 
/*     */     
/* 207 */     if (this.stopFractions != null)
/*     */     {
/* 209 */       return this.stopFractions;
/*     */     }
/*     */     
/* 212 */     buildStops();
/*     */     
/* 214 */     return this.stopFractions;
/*     */   }
/*     */ 
/*     */   
/*     */   public Color[] getStopColors() {
/* 219 */     if (this.stopRef != null) {
/*     */       
/* 221 */       Gradient grad = (Gradient)this.diagram.getUniverse().getElement(this.stopRef);
/* 222 */       return grad.getStopColors();
/*     */     } 
/*     */     
/* 225 */     if (this.stopColors != null)
/*     */     {
/* 227 */       return this.stopColors;
/*     */     }
/*     */     
/* 230 */     buildStops();
/*     */     
/* 232 */     return this.stopColors;
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
/*     */   private int clamp(int val, int min, int max) {
/* 249 */     if (val < min)
/*     */     {
/* 251 */       return min;
/*     */     }
/* 253 */     if (val > max)
/*     */     {
/* 255 */       return max;
/*     */     }
/* 257 */     return val;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setStopRef(URI grad) {
/* 262 */     this.stopRef = grad;
/*     */   }
/*     */ 
/*     */   
/*     */   public void appendStop(Stop stop) {
/* 267 */     this.stops.add(stop);
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
/*     */   public boolean updateTime(double curTime) throws SVGException {
/* 281 */     boolean stateChange = false;
/*     */ 
/*     */     
/* 284 */     StyleAttribute sty = new StyleAttribute();
/*     */ 
/*     */ 
/*     */     
/* 288 */     if (getPres(sty.setName("spreadMethod"))) {
/*     */       int newVal;
/*     */       
/* 291 */       String strn = sty.getStringValue().toLowerCase();
/* 292 */       if (strn.equals("repeat")) {
/*     */         
/* 294 */         newVal = 1;
/* 295 */       } else if (strn.equals("reflect")) {
/*     */         
/* 297 */         newVal = 2;
/*     */       } else {
/*     */         
/* 300 */         newVal = 0;
/*     */       } 
/* 302 */       if (this.spreadMethod != newVal) {
/*     */         
/* 304 */         this.spreadMethod = newVal;
/* 305 */         stateChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 309 */     if (getPres(sty.setName("gradientUnits"))) {
/*     */       int newVal;
/*     */       
/* 312 */       String strn = sty.getStringValue().toLowerCase();
/* 313 */       if (strn.equals("userspaceonuse")) {
/*     */         
/* 315 */         newVal = 1;
/*     */       } else {
/*     */         
/* 318 */         newVal = 0;
/*     */       } 
/* 320 */       if (newVal != this.gradientUnits) {
/*     */         
/* 322 */         this.gradientUnits = newVal;
/* 323 */         stateChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 327 */     if (getPres(sty.setName("gradientTransform"))) {
/*     */       
/* 329 */       AffineTransform newVal = parseTransform(sty.getStringValue());
/* 330 */       if (newVal != null && newVal.equals(this.gradientTransform)) {
/*     */         
/* 332 */         this.gradientTransform = newVal;
/* 333 */         stateChange = true;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 339 */     if (getPres(sty.setName("xlink:href"))) {
/*     */       
/*     */       try {
/*     */         
/* 343 */         URI newVal = sty.getURIValue(getXMLBase());
/* 344 */         if ((newVal == null && this.stopRef != null) || !newVal.equals(this.stopRef)) {
/*     */           
/* 346 */           this.stopRef = newVal;
/* 347 */           stateChange = true;
/*     */         } 
/* 349 */       } catch (Exception e) {
/*     */         
/* 351 */         Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, "Could not parse xlink:href", e);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 357 */     for (Stop stop : this.stops) {
/* 358 */       if (stop.updateTime(curTime)) {
/*     */         
/* 360 */         stateChange = true;
/* 361 */         this.stopFractions = null;
/* 362 */         this.stopColors = null;
/*     */       } 
/*     */     } 
/*     */     
/* 366 */     return stateChange;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\Gradient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */