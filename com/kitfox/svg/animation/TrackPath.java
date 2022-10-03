/*     */ package com.kitfox.svg.animation;
/*     */ 
/*     */ import com.kitfox.svg.SVGElementException;
/*     */ import com.kitfox.svg.pathcmd.PathUtil;
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ import java.awt.geom.GeneralPath;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TrackPath
/*     */   extends TrackBase
/*     */ {
/*     */   public TrackPath(AnimationElement ele) throws SVGElementException {
/*  59 */     super(ele.getParent(), ele);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getValue(StyleAttribute attrib, double curTime) {
/*  65 */     GeneralPath path = getValue(curTime);
/*  66 */     if (path == null) return false;
/*     */     
/*  68 */     attrib.setStringValue(PathUtil.buildPathString(path));
/*  69 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public GeneralPath getValue(double curTime) {
/*  74 */     GeneralPath retVal = null;
/*  75 */     AnimationTimeEval state = new AnimationTimeEval();
/*     */     
/*  77 */     for (AnimationElement animationElement : this.animEvents) {
/*  78 */       AnimateBase ele = (AnimateBase)animationElement;
/*  79 */       Animate eleAnim = (Animate)ele;
/*  80 */       ele.evalParametric(state, curTime);
/*     */ 
/*     */       
/*  83 */       if (Double.isNaN(state.interp))
/*     */         continue; 
/*  85 */       if (retVal == null) {
/*     */         
/*  87 */         retVal = eleAnim.evalPath(state.interp);
/*     */         
/*     */         continue;
/*     */       } 
/*  91 */       GeneralPath curPath = eleAnim.evalPath(state.interp);
/*  92 */       switch (ele.getAdditiveType()) {
/*     */         
/*     */         case 0:
/*  95 */           retVal = curPath;
/*     */           continue;
/*     */         case 1:
/*  98 */           throw new RuntimeException("Not implemented");
/*     */       } 
/*     */ 
/*     */       
/* 102 */       throw new RuntimeException();
/*     */     } 
/*     */ 
/*     */     
/* 106 */     return retVal;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\animation\TrackPath.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */