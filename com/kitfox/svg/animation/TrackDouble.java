/*     */ package com.kitfox.svg.animation;
/*     */ 
/*     */ import com.kitfox.svg.SVGElementException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TrackDouble
/*     */   extends TrackBase
/*     */ {
/*     */   public TrackDouble(AnimationElement ele) throws SVGElementException {
/*  56 */     super(ele.getParent(), ele);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getValue(StyleAttribute attrib, double curTime) {
/*  62 */     double val = getValue(curTime);
/*  63 */     if (Double.isNaN(val)) return false;
/*     */     
/*  65 */     attrib.setStringValue("" + val);
/*  66 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getValue(double curTime) {
/*  71 */     double retVal = Double.NaN;
/*     */     
/*  73 */     StyleAttribute attr = null;
/*  74 */     switch (this.attribType) {
/*     */       
/*     */       case 0:
/*  77 */         attr = this.parent.getStyleAbsolute(this.attribName);
/*  78 */         retVal = attr.getDoubleValue();
/*     */         break;
/*     */       case 1:
/*  81 */         attr = this.parent.getPresAbsolute(this.attribName);
/*  82 */         retVal = attr.getDoubleValue();
/*     */         break;
/*     */       case 2:
/*  85 */         attr = this.parent.getStyleAbsolute(this.attribName);
/*  86 */         if (attr == null) attr = this.parent.getPresAbsolute(this.attribName); 
/*  87 */         retVal = attr.getDoubleValue();
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  93 */     AnimationTimeEval state = new AnimationTimeEval();
/*     */ 
/*     */     
/*  96 */     for (AnimationElement animationElement : this.animEvents) {
/*  97 */       Animate ele = (Animate)animationElement;
/*  98 */       ele.evalParametric(state, curTime);
/*     */ 
/*     */       
/* 101 */       if (Double.isNaN(state.interp))
/*     */         continue; 
/* 103 */       switch (ele.getAdditiveType()) {
/*     */         
/*     */         case 1:
/* 106 */           retVal += ele.eval(state.interp);
/*     */           break;
/*     */         case 0:
/* 109 */           retVal = ele.eval(state.interp);
/*     */           break;
/*     */       } 
/*     */ 
/*     */       
/* 114 */       if (state.rep > 0)
/*     */       {
/* 116 */         switch (ele.getAccumulateType()) {
/*     */           
/*     */           case 1:
/* 119 */             retVal += ele.repeatSkipSize(state.rep);
/*     */         } 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*     */     } 
/* 126 */     return retVal;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\animation\TrackDouble.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */