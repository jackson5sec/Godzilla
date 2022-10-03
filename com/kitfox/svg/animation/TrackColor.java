/*     */ package com.kitfox.svg.animation;
/*     */ 
/*     */ import com.kitfox.svg.SVGElementException;
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ import java.awt.Color;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TrackColor
/*     */   extends TrackBase
/*     */ {
/*     */   public TrackColor(AnimationElement ele) throws SVGElementException {
/*  58 */     super(ele.getParent(), ele);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getValue(StyleAttribute attrib, double curTime) {
/*  64 */     Color col = getValue(curTime);
/*  65 */     if (col == null) return false;
/*     */     
/*  67 */     attrib.setStringValue("#" + Integer.toHexString(col.getRGB()));
/*  68 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Color getValue(double curTime) {
/*  73 */     Color retVal = null;
/*  74 */     AnimationTimeEval state = new AnimationTimeEval();
/*     */     
/*  76 */     for (AnimationElement animationElement : this.animEvents) {
/*  77 */       AnimateBase ele = (AnimateBase)animationElement;
/*  78 */       AnimateColorIface eleColor = (AnimateColorIface)ele;
/*  79 */       ele.evalParametric(state, curTime);
/*     */ 
/*     */       
/*  82 */       if (Double.isNaN(state.interp))
/*     */         continue; 
/*  84 */       if (retVal == null) {
/*     */         
/*  86 */         retVal = eleColor.evalColor(state.interp);
/*     */         
/*     */         continue;
/*     */       } 
/*  90 */       Color curCol = eleColor.evalColor(state.interp);
/*  91 */       switch (ele.getAdditiveType()) {
/*     */         
/*     */         case 0:
/*  94 */           retVal = curCol;
/*     */         
/*     */         case 1:
/*  97 */           retVal = new Color(curCol.getRed() + retVal.getRed(), curCol.getGreen() + retVal.getGreen(), curCol.getBlue() + retVal.getBlue());
/*     */       } 
/*     */ 
/*     */     
/*     */     } 
/* 102 */     return retVal;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\animation\TrackColor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */