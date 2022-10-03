/*     */ package com.kitfox.svg;
/*     */ 
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FeSpotLight
/*     */   extends FeLight
/*     */ {
/*     */   public static final String TAG_NAME = "fespotlight";
/*  48 */   float x = 0.0F;
/*  49 */   float y = 0.0F;
/*  50 */   float z = 0.0F;
/*  51 */   float pointsAtX = 0.0F;
/*  52 */   float pointsAtY = 0.0F;
/*  53 */   float pointsAtZ = 0.0F;
/*  54 */   float specularComponent = 0.0F;
/*  55 */   float limitingConeAngle = 0.0F;
/*     */ 
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
/*  67 */     return "fespotlight";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void build() throws SVGException {
/*  73 */     super.build();
/*     */     
/*  75 */     StyleAttribute sty = new StyleAttribute();
/*     */     
/*  77 */     if (getPres(sty.setName("x")))
/*     */     {
/*  79 */       this.x = sty.getFloatValueWithUnits();
/*     */     }
/*  81 */     if (getPres(sty.setName("y")))
/*     */     {
/*  83 */       this.y = sty.getFloatValueWithUnits();
/*     */     }
/*  85 */     if (getPres(sty.setName("z")))
/*     */     {
/*  87 */       this.z = sty.getFloatValueWithUnits();
/*     */     }
/*  89 */     if (getPres(sty.setName("pointsAtX")))
/*     */     {
/*  91 */       this.pointsAtX = sty.getFloatValueWithUnits();
/*     */     }
/*  93 */     if (getPres(sty.setName("pointsAtY")))
/*     */     {
/*  95 */       this.pointsAtY = sty.getFloatValueWithUnits();
/*     */     }
/*  97 */     if (getPres(sty.setName("pointsAtZ")))
/*     */     {
/*  99 */       this.pointsAtZ = sty.getFloatValueWithUnits();
/*     */     }
/* 101 */     if (getPres(sty.setName("specularComponent")))
/*     */     {
/* 103 */       this.specularComponent = sty.getFloatValueWithUnits();
/*     */     }
/* 105 */     if (getPres(sty.setName("limitingConeAngle")))
/*     */     {
/* 107 */       this.limitingConeAngle = sty.getFloatValueWithUnits();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getX() {
/* 114 */     return this.x;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getY() {
/* 120 */     return this.y;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getZ() {
/* 125 */     return this.z;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPointsAtX() {
/* 130 */     return this.pointsAtX;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPointsAtY() {
/* 135 */     return this.pointsAtY;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPointsAtZ() {
/* 140 */     return this.pointsAtZ;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getSpecularComponent() {
/* 145 */     return this.specularComponent;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getLimitingConeAngle() {
/* 150 */     return this.limitingConeAngle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean updateTime(double curTime) throws SVGException {
/* 159 */     StyleAttribute sty = new StyleAttribute();
/* 160 */     boolean stateChange = false;
/*     */     
/* 162 */     if (getPres(sty.setName("x"))) {
/*     */       
/* 164 */       float newVal = sty.getFloatValueWithUnits();
/* 165 */       if (newVal != this.x) {
/*     */         
/* 167 */         this.x = newVal;
/* 168 */         stateChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 172 */     if (getPres(sty.setName("y"))) {
/*     */       
/* 174 */       float newVal = sty.getFloatValueWithUnits();
/* 175 */       if (newVal != this.y) {
/*     */         
/* 177 */         this.y = newVal;
/* 178 */         stateChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 182 */     if (getPres(sty.setName("z"))) {
/*     */       
/* 184 */       float newVal = sty.getFloatValueWithUnits();
/* 185 */       if (newVal != this.z) {
/*     */         
/* 187 */         this.z = newVal;
/* 188 */         stateChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 192 */     if (getPres(sty.setName("pointsAtX"))) {
/*     */       
/* 194 */       float newVal = sty.getFloatValueWithUnits();
/* 195 */       if (newVal != this.pointsAtX) {
/*     */         
/* 197 */         this.pointsAtX = newVal;
/* 198 */         stateChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 202 */     if (getPres(sty.setName("pointsAtY"))) {
/*     */       
/* 204 */       float newVal = sty.getFloatValueWithUnits();
/* 205 */       if (newVal != this.pointsAtY) {
/*     */         
/* 207 */         this.pointsAtY = newVal;
/* 208 */         stateChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 212 */     if (getPres(sty.setName("pointsAtZ"))) {
/*     */       
/* 214 */       float newVal = sty.getFloatValueWithUnits();
/* 215 */       if (newVal != this.pointsAtZ) {
/*     */         
/* 217 */         this.pointsAtZ = newVal;
/* 218 */         stateChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 222 */     if (getPres(sty.setName("specularComponent"))) {
/*     */       
/* 224 */       float newVal = sty.getFloatValueWithUnits();
/* 225 */       if (newVal != this.specularComponent) {
/*     */         
/* 227 */         this.specularComponent = newVal;
/* 228 */         stateChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 232 */     if (getPres(sty.setName("limitingConeAngle"))) {
/*     */       
/* 234 */       float newVal = sty.getFloatValueWithUnits();
/* 235 */       if (newVal != this.limitingConeAngle) {
/*     */         
/* 237 */         this.limitingConeAngle = newVal;
/* 238 */         stateChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 242 */     return stateChange;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\FeSpotLight.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */