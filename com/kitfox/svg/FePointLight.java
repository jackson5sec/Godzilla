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
/*     */ public class FePointLight
/*     */   extends FeLight
/*     */ {
/*     */   public static final String TAG_NAME = "fepointlight";
/*  48 */   float x = 0.0F;
/*  49 */   float y = 0.0F;
/*  50 */   float z = 0.0F;
/*     */ 
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
/*  62 */     return "fepointlight";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void build() throws SVGException {
/*  68 */     super.build();
/*     */     
/*  70 */     StyleAttribute sty = new StyleAttribute();
/*     */     
/*  72 */     if (getPres(sty.setName("x")))
/*     */     {
/*  74 */       this.x = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/*  77 */     if (getPres(sty.setName("y")))
/*     */     {
/*  79 */       this.y = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/*  82 */     if (getPres(sty.setName("z")))
/*     */     {
/*  84 */       this.z = sty.getFloatValueWithUnits();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getX() {
/*  91 */     return this.x;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getY() {
/*  97 */     return this.y;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getZ() {
/* 102 */     return this.z;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean updateTime(double curTime) throws SVGException {
/* 111 */     StyleAttribute sty = new StyleAttribute();
/* 112 */     boolean stateChange = false;
/*     */     
/* 114 */     if (getPres(sty.setName("x"))) {
/*     */       
/* 116 */       float newVal = sty.getFloatValueWithUnits();
/* 117 */       if (newVal != this.x) {
/*     */         
/* 119 */         this.x = newVal;
/* 120 */         stateChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 124 */     if (getPres(sty.setName("y"))) {
/*     */       
/* 126 */       float newVal = sty.getFloatValueWithUnits();
/* 127 */       if (newVal != this.y) {
/*     */         
/* 129 */         this.y = newVal;
/* 130 */         stateChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 134 */     if (getPres(sty.setName("z"))) {
/*     */       
/* 136 */       float newVal = sty.getFloatValueWithUnits();
/* 137 */       if (newVal != this.z) {
/*     */         
/* 139 */         this.z = newVal;
/* 140 */         stateChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 144 */     return stateChange;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\FePointLight.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */