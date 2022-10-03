/*     */ package com.kitfox.svg.animation;
/*     */ 
/*     */ import com.kitfox.svg.SVGElement;
/*     */ import com.kitfox.svg.SVGElementException;
/*     */ import com.kitfox.svg.SVGException;
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ import java.awt.geom.AffineTransform;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TrackMotion
/*     */   extends TrackBase
/*     */ {
/*     */   public TrackMotion(AnimationElement ele) throws SVGElementException {
/*  59 */     super(ele.getParent(), ele);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getValue(StyleAttribute attrib, double curTime) throws SVGException {
/*  65 */     AffineTransform retVal = new AffineTransform();
/*  66 */     retVal = getValue(retVal, curTime);
/*     */ 
/*     */ 
/*     */     
/*  70 */     double[] mat = new double[6];
/*  71 */     retVal.getMatrix(mat);
/*  72 */     attrib.setStringValue("matrix(" + mat[0] + " " + mat[1] + " " + mat[2] + " " + mat[3] + " " + mat[4] + " " + mat[5] + ")");
/*  73 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AffineTransform getValue(AffineTransform retVal, double curTime) throws SVGException {
/*  79 */     StyleAttribute attr = null;
/*  80 */     switch (this.attribType) {
/*     */       
/*     */       case 0:
/*  83 */         attr = this.parent.getStyleAbsolute(this.attribName);
/*  84 */         retVal.setTransform(SVGElement.parseSingleTransform(attr.getStringValue()));
/*     */         break;
/*     */       case 1:
/*  87 */         attr = this.parent.getPresAbsolute(this.attribName);
/*  88 */         retVal.setTransform(SVGElement.parseSingleTransform(attr.getStringValue()));
/*     */         break;
/*     */       case 2:
/*  91 */         attr = this.parent.getStyleAbsolute(this.attribName);
/*  92 */         if (attr == null) attr = this.parent.getPresAbsolute(this.attribName); 
/*  93 */         retVal.setTransform(SVGElement.parseSingleTransform(attr.getStringValue()));
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  99 */     AnimationTimeEval state = new AnimationTimeEval();
/* 100 */     AffineTransform xform = new AffineTransform();
/*     */ 
/*     */     
/* 103 */     for (AnimationElement animationElement : this.animEvents) {
/* 104 */       AnimateMotion ele = (AnimateMotion)animationElement;
/* 105 */       ele.evalParametric(state, curTime);
/*     */ 
/*     */       
/* 108 */       if (Double.isNaN(state.interp))
/*     */         continue; 
/* 110 */       switch (ele.getAdditiveType()) {
/*     */         
/*     */         case 1:
/* 113 */           retVal.concatenate(ele.eval(xform, state.interp));
/*     */         
/*     */         case 0:
/* 116 */           retVal.setTransform(ele.eval(xform, state.interp));
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     } 
/* 135 */     return retVal;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\animation\TrackMotion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */